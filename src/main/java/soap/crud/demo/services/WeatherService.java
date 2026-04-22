package soap.crud.demo.services;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;

import com.demo.soap.weather.Weather;
import com.demo.soap.weather.WeatherImage;

import soap.crud.demo.entities.WeatherEntity;
import soap.crud.demo.entities.WeatherImageEntity;
import soap.crud.demo.repositories.WeatherRepository;

@Service
public class WeatherService {
    private final WeatherRepository repo;

    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public WeatherService(WeatherRepository repo) {
        this.repo = repo;
    }

    public WeatherEntity create(WeatherEntity w) {
        return repo.save(w);
    }

    public WeatherEntity get(Long id) {
        return repo.findById(id).orElse(null);
    }

    public List<WeatherEntity> getAll() {
        return repo.findAll();
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public WeatherEntity findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Weather not found"));
    }

    public WeatherEntity update(WeatherEntity entity) {
        return repo.save(entity);
    }

    public Weather toSoap(WeatherEntity entity) {
        if (entity == null)
            return null;

        Weather weather = new Weather();
        weather.setId(entity.getId());
        weather.setLocation(entity.getLocation());
        weather.setTemperature(entity.getTemperature());

        weather.setCreatedAt(entity.getCreatedAt() != null ? entity.getCreatedAt().format(DATETIME_FORMATTER) : null);

        weather.setUpdatedAt(entity.getUpdatedAt() != null ? entity.getUpdatedAt().format(DATETIME_FORMATTER) : null);

        // 👇 map images
        if (entity.getImages() != null) {
            for (WeatherImageEntity img : entity.getImages()) {

                WeatherImage soapImg = new WeatherImage();
                soapImg.setId(img.getId());
                soapImg.setFileName(img.getFileName());
                soapImg.setPath(img.getPath());
                soapImg.setContent(img.getContent());

                weather.getImages().add(soapImg);
            }
        }

        return weather;
    }

    // =================================================================
    /////////////// private helper methods (if needed)////////////////
    // =================================================================
}
