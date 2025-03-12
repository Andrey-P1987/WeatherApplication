package ru.petu.course.weatherRestApp2025.services;


import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.petu.course.weatherRestApp2025.dto.WeatherResponse;
import ru.petu.course.weatherRestApp2025.models.City;
import ru.petu.course.weatherRestApp2025.models.Weather;
import ru.petu.course.weatherRestApp2025.models.Wind;
import ru.petu.course.weatherRestApp2025.repositories.CityRepository;
import ru.petu.course.weatherRestApp2025.repositories.WeatherRepository;
import ru.petu.course.weatherRestApp2025.repositories.WindRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WeatherService {

    private final RestTemplate restTemplate;
    private final CityRepository cityRepository;
    private final WeatherRepository weatherRepository;
    private final WindRepository windRepository;

    @Value("${weather.api.key}")  // API Key from application.properties
    private String apiKey;

    public WeatherService(RestTemplate restTemplate, CityRepository cityRepository, WeatherRepository weatherRepository, WindRepository windRepository) {
        this.restTemplate = restTemplate;
        this.cityRepository = cityRepository;
        this.weatherRepository = weatherRepository;
        this.windRepository = windRepository;
    }

    @Transactional
    public WeatherResponse fetchWeatherForCity(Integer cityId) {
        Optional<City> cityOpt = cityRepository.findById(cityId);
        if (cityOpt.isEmpty()) {
            throw new RuntimeException("City not found");
        }

        City city = cityOpt.get();
        BigDecimal latitude = city.getCoordinateLatitude();
        BigDecimal longitude = city.getCoordinateLongitude();

        // Constructing API request URL
        String url = String.format(
                "https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=%s",
                latitude, longitude, apiKey
        );

        WeatherResponse weatherResponse = restTemplate.getForObject(url, WeatherResponse.class);
        if (weatherResponse == null) {
            throw new RuntimeException("Failed to fetch weather data");
        }

        saveWeatherData(city, weatherResponse);

        return weatherResponse;
    }

    @Transactional
    public List<WeatherResponse> fetchWeatherForAllCities(){
        List<City> cities = cityRepository.findAll();
        List<WeatherResponse> weatherResponses = new ArrayList<>();

        for(City city : cities) {
            BigDecimal latitude = city.getCoordinateLatitude();
            BigDecimal longitude = city.getCoordinateLongitude();
            String url = String.format(
                    "https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=%s",
                    latitude, longitude, apiKey
            );
            try {
                WeatherResponse weatherResponse = restTemplate.getForObject(url, WeatherResponse.class);
                if (weatherResponse != null) {
                    saveWeatherData(city, weatherResponse);
                    weatherResponses.add(weatherResponse);
                } else {
                    System.err.println("Failed to fetch weather data for city: " + city.getName());
                }
                }catch (Exception e){
                    System.err.println("Error fetching weather for city " + city.getName() + ": " + e.getMessage());
                }
        }
        return weatherResponses;
    }

    public List<Weather> allWeathersAndCities(){
         List<Weather>weathers = weatherRepository.findAll();
        System.out.println("Cities returned: " + weathers.size());
         return weathers;
    }





    private void saveWeatherData(City city, WeatherResponse response) {
        // Convert timestamps to Instant format
        Instant sunrise = Instant.ofEpochSecond(response.getSys().getSunrise());
        Instant sunset = Instant.ofEpochSecond(response.getSys().getSunset());

        // Convert temperature from Kelvin to Celsius (handle null case)
        BigDecimal tempKelvin = response.getMain() != null ? response.getMain().getTemp() : null;
        BigDecimal tempCelsius = (tempKelvin != null) ? tempKelvin.subtract(BigDecimal.valueOf(273.15)) : null;

        // Create and save Weather entity
        Weather weather = new Weather();
        weather.setCity(city);
        weather.setTemperature(tempCelsius);
        weather.setHumidity(response.getMain().getHumidity());
        weather.setDescription(response.getWeather().get(0).getDescription());
        weather.setSunrise(sunrise);
        weather.setSunset(sunset);
        weather.setWeatherDate(Instant.parse("2000-01-01T00:00:00Z")); // Added for Test!!!!!
        weatherRepository.save(weather);

        // Create and save Wind entity
        Wind wind = new Wind();
        wind.setWeather(weather);
        wind.setSpeed(response.getWind().getSpeed());
        wind.setDeg(response.getWind().getDeg());
        windRepository.save(wind);
    }
}