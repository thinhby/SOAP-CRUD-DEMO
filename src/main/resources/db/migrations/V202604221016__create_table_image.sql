CREATE TABLE weather_images (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    file_name VARCHAR(255),
    file_size BIGINT,

    path VARCHAR(500),

    content VARCHAR(500),

    weather_id BIGINT,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_weather_image
        FOREIGN KEY (weather_id)
        REFERENCES weather(id)
        ON DELETE CASCADE
);