package soap.crud.demo.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
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

import soap.crud.demo.entities.WeatherEntity;
import soap.crud.demo.services.WeatherService;

@Endpoint
public class WeatherEndpoint {
    private static final String NAMESPACE_URI = "http://soap.demo.com/weather";
    private static final String DELETED_MESSAGE = "deleted successfully";

    @Autowired
    private WeatherService weatherService;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "CreateWeatherRequest")
    @ResponsePayload
    public CreateWeatherResponse create(@RequestPayload CreateWeatherRequest request) {
        WeatherEntity entity = new WeatherEntity();
        entity.setLocation(request.getLocation());
        entity.setTemperature(request.getTemperature());

        WeatherEntity saved = weatherService.create(entity);

        CreateWeatherResponse res = new CreateWeatherResponse();
        res.setWeather(weatherService.toSoap(saved));

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
        weatherService.delete(request.getId());
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
