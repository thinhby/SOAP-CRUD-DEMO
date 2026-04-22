package soap.crud.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import soap.crud.demo.entities.WeatherImageEntity;

public interface WeatherImageRepository extends JpaRepository<WeatherImageEntity, Long> {
}
