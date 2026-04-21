package soap.crud.demo.services;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;

import com.demo.soap.weather.Weather;

import soap.crud.demo.entities.WeatherEntity;
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

        weather.setCreatedAt(
                entity.getCreatedAt() != null
                        ? entity.getCreatedAt().format(DATETIME_FORMATTER)
                        : null);

        // updatedAt
        weather.setUpdatedAt(
                entity.getUpdatedAt() != null
                        ? entity.getUpdatedAt().format(DATETIME_FORMATTER)
                        : null);

        return weather;
    }
}
