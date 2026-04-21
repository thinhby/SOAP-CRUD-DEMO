package soap.crud.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import soap.crud.demo.entities.WeatherEntity;

public interface WeatherRepository extends JpaRepository<WeatherEntity, Long> {
}
