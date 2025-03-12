package ru.petu.course.weatherRestApp2025.models;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@Table(name = "weather")
@NoArgsConstructor
@AllArgsConstructor
public class Weather {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_weather")
    private Integer idWeather;


    @ManyToOne
    @JoinColumn(name = "id_city", referencedColumnName = "id_city")
    private City city;

    @Column(name = "description")
    private String description;

    @Column(name = "temperature", nullable = false)
    private BigDecimal temperature;

    @Column(name = "humidity")
    private int humidity;

    @Column(name = "sunrise")
    private Instant sunrise;

    @Column(name = "sunset")
    private Instant sunset;

    @Column(name = "weather_date", nullable = false)
    private Instant weatherDate = Instant.now();


//    public void setCity(City city) {
//
//        this.city = city;
//    }
}
