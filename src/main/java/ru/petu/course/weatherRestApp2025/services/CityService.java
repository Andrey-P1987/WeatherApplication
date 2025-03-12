package ru.petu.course.weatherRestApp2025.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.petu.course.weatherRestApp2025.dto.WeatherResponse;
import ru.petu.course.weatherRestApp2025.models.City;
import ru.petu.course.weatherRestApp2025.models.Weather;
import ru.petu.course.weatherRestApp2025.repositories.CityRepository;
import ru.petu.course.weatherRestApp2025.repositories.WeatherRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;


@Service
public class CityService {

    private final RestTemplate restTemplate;
    private final CityRepository cityRepository;

    @Value("${weather.api.key}")
    private String apiKey;

    public CityService(RestTemplate restTemplate, CityRepository cityRepository) {
        this.restTemplate = restTemplate;
        this.cityRepository = cityRepository;
    }

    public String getWeatherForCity(Integer cityId) {
        Optional<City> cityOpt = cityRepository.findById(cityId);
        if (cityOpt.isEmpty()) {
            throw new RuntimeException("City not found");
        }

        City city = cityOpt.get();
        BigDecimal latitude = city.getCoordinateLatitude();
        BigDecimal longitude = city.getCoordinateLongitude();

        String url = String.format(
                "https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=%s",
                latitude, longitude, apiKey
        );

        return restTemplate.getForObject(url, String.class);
    }

    public List<City> findAll(){
        return cityRepository.findAll();
    }
}