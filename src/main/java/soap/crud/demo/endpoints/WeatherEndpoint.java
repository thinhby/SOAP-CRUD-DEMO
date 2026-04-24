package soap.crud.demo.endpoints;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.demo.soap.weather.CreateWeatherRequest;
import com.demo.soap.weather.CreateWeatherResponse;
import com.demo.soap.weather.DeleteWeatherRequest;
import com.demo.soap.weather.DeleteWeatherResponse;
import com.demo.soap.weather.GetAllWeatherResponse;
import com.demo.soap.weather.GetWeatherRequest;
import com.demo.soap.weather.GetWeatherResponse;
import com.demo.soap.weather.UpdateWeatherRequest;
import com.demo.soap.weather.UpdateWeatherResponse;
import com.demo.soap.weather.WeatherImageRequest;

import jakarta.activation.DataHandler;
import soap.crud.demo.entities.WeatherEntity;
import soap.crud.demo.entities.WeatherImageEntity;
import soap.crud.demo.repositories.WeatherImageRepository;
import soap.crud.demo.services.WeatherService;

@Endpoint
@Validated
public class WeatherEndpoint {
    private static final String NAMESPACE_URI = "http://soap.demo.com/weather";
    private static final String DELETED_MESSAGE = "deleted successfully";

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private WeatherImageRepository weatherImageRepository;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "CreateWeatherRequest")
    @ResponsePayload
    public CreateWeatherResponse create(@RequestPayload CreateWeatherRequest request) {

        // 1. save weather
        WeatherEntity entity = new WeatherEntity();
        entity.setLocation(request.getLocation());
        entity.setTemperature(request.getTemperature());

        WeatherEntity saved = weatherService.create(entity);

        // 2. xử lý images (MTOM)
        if (request.getImages() != null) {
            for (WeatherImageRequest img : request.getImages()) {
                try {
                    DataHandler file = img.getFile();
                    System.out.println(file.getContentType());

                    String fileName = UUID.randomUUID() + "_" + img.getFileName();
                    Path path = Paths.get("uploads/" + fileName);

                    Files.copy(file.getInputStream(), path);

                    // save DB
                    WeatherImageEntity imageEntity = new WeatherImageEntity();
                    imageEntity.setFileName(fileName);
                    imageEntity.setPath(path.toString());
                    imageEntity.setFileSize(img.getSize());
                    imageEntity.setWeather(saved);

                    weatherImageRepository.save(imageEntity);

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        // 3. load lại weather + images
        WeatherEntity full = weatherService.findById(saved.getId());

        // 4. response
        CreateWeatherResponse res = new CreateWeatherResponse();
        res.setWeather(weatherService.toSoap(full));

        return res;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetWeatherRequest")
    @ResponsePayload
    public GetWeatherResponse get(@RequestPayload GetWeatherRequest request) {
        WeatherEntity entity = weatherService.get(request.getId());

        GetWeatherResponse res = new GetWeatherResponse();
        res.setWeather(weatherService.toSoap(entity));

        return res;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetAllWeatherRequest")
    @ResponsePayload
    public GetAllWeatherResponse getAll() {
        GetAllWeatherResponse res = new GetAllWeatherResponse();

        for (WeatherEntity entity : weatherService.getAll()) {
            res.getWeathers().add(weatherService.toSoap(entity));
        }

        return res;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "DeleteWeatherRequest")
    @ResponsePayload
    public DeleteWeatherResponse delete(@RequestPayload DeleteWeatherRequest request) {
        WeatherEntity entity = weatherService.get(request.getId());
        if (entity == null) {
            throw new RuntimeException("Weather not found id=" + request.getId());
        }

        weatherService.delete(entity.getId());
        DeleteWeatherResponse res = new DeleteWeatherResponse();
        res.setMessage(DELETED_MESSAGE);

        return res;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "UpdateWeatherRequest")
    @ResponsePayload
    public UpdateWeatherResponse update(@RequestPayload UpdateWeatherRequest request) {

        WeatherEntity entity = weatherService.get(request.getId());

        if (entity == null) {
            throw new RuntimeException("Weather not found id=" + request.getId());
        }

        // update data
        entity.setLocation(request.getLocation());
        entity.setTemperature(request.getTemperature());

        WeatherEntity saved = weatherService.update(entity);

        UpdateWeatherResponse res = new UpdateWeatherResponse();
        res.setWeather(weatherService.toSoap(saved));

        return res;
    }
}
