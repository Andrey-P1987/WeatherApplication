package ru.petu.course.weatherRestApp2025.dto;

import com.sun.tools.javac.Main;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


@Getter
@Setter
public class WeatherResponse {

    private String name;
    private int timezone;
    private Map<String, BigDecimal> coord;  // Contains "lat" and "lon"
    private WeatherMain main;               // Temperature, humidity, etc.
    private WeatherWind wind;               // Wind speed, direction
    private List<WeatherDescription> weather; // Contains "description"
    private Sys sys;                 // Contains "sunrise" and "sunset"


    @Getter
    @Setter
    public static class WeatherMain {
        private BigDecimal temp;
        private int humidity;
        private int pressure;
        private BigDecimal feels_like;
        private BigDecimal temp_min;
        private BigDecimal temp_max;

    }


    @Getter
    @Setter
    public static class WeatherWind {
        private BigDecimal speed;
        private int deg;

    }


    @Getter
    @Setter
    public static class WeatherDescription {
        private String description;  // Weather condition description

    }


    @Getter
    @Setter
    public static class Sys {
        private long sunrise;  // Timestamp for sunrise
        private long sunset;   // Timestamp for sunset

    }


}