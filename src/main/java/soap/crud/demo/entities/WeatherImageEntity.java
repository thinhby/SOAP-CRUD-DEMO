package soap.crud.demo.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class WeatherImageEntity {
    @Id
    @GeneratedValue
    private Long id;

    private String fileName;

    private Long fileSize;

    private String path;

    private String content;

    @ManyToOne
    @JoinColumn(name = "weather_id")
    private WeatherEntity weather;
}
