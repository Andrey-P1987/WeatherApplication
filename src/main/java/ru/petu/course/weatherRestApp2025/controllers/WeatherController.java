package ru.petu.course.weatherRestApp2025.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.petu.course.weatherRestApp2025.dto.WeatherResponse;
import ru.petu.course.weatherRestApp2025.exceptions.ResourceNotFoundException;
import ru.petu.course.weatherRestApp2025.models.Weather;
import ru.petu.course.weatherRestApp2025.repositories.WeatherRepository;
import ru.petu.course.weatherRestApp2025.services.WeatherService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    private final WeatherService weatherService;
    @Autowired
    private WeatherRepository weatherRepository;

    @Autowired
    public WeatherController(WeatherService weatherService) {

        this.weatherService = weatherService;
    }

    @GetMapping("/{cityId}")
    public WeatherResponse getWeather(@PathVariable Integer cityId) {
        return weatherService.fetchWeatherForCity(cityId);
    }

    //Requests weather from API and puts to DB
    @GetMapping("/allCities")
    public List<WeatherResponse> getWeatherAllCities() {

        return weatherService.fetchWeatherForAllCities();
    }

    @GetMapping("/allWeathersAndCities")
    public List<Weather> allWeathersAndCities(){

        return weatherService.allWeathersAndCities();
    }

    @GetMapping("/daysBelow")
    public int getDaysBelowTemperature(
            @RequestParam String cityName,
            @RequestParam int year,
            @RequestParam int month,
            @RequestParam double temperatureThreshold){


        YearMonth targetMonth = YearMonth.of(year, month);
        Instant startDate = targetMonth.atDay(1).atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant endDate = targetMonth.atEndOfMonth().atStartOfDay(ZoneId.systemDefault()).toInstant();


        List<Weather> weatherData = weatherRepository.findByCityNameAndWeatherDateBetween(
                cityName, startDate, endDate);

        int daysBelowThreshold = 0;
        for (Weather weather : weatherData) {
            if (weather.getTemperature().doubleValue() < temperatureThreshold) {
                daysBelowThreshold++;
            }
        }
        return daysBelowThreshold;

    }

    //Method returns Weather for a City on specific date or average year temperature for a City
    @GetMapping("/getCityWeather")
    public ResponseEntity<?> cityWeatherDate(@RequestParam String cityName,
                                   @RequestParam(required = false) Integer year,
                                   @RequestParam(required = false) Integer month,
                                   @RequestParam(required = false) Integer day){
        if (year != null && month == null && day == null) {
            // Calculate average temperature for the year
            double averageTemperature = calculateAverageTemperatureForYear(cityName, year);
            return ResponseEntity.ok(Map.of("averageTemperature", averageTemperature));
        } else if (year != null && month != null && day != null) {
            // Fetch weather data for the specific date
            LocalDate date = LocalDate.of(year, month, day);
            Instant startOfDay = date.atStartOfDay(ZoneId.systemDefault()).toInstant();
            Instant endOfDay = date.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();

            Weather weather = weatherRepository.findByCityNameAndWeatherDateBetween(cityName, startOfDay, endOfDay)
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Weather data not found for city: " + cityName + " on date: " + date));

            return ResponseEntity.ok(weather);
        } else {
            // Invalid combination of parameters
            return ResponseEntity.badRequest().body("Invalid parameters. Provide either (cityName and year) or (cityName, year, month, and day).");
        }
    }
    private double calculateAverageTemperatureForYear(String cityName, int year) {
        // Define the start and end of the year
        Instant startOfYear = LocalDate.of(year, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant endOfYear = LocalDate.of(year + 1, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant();

        // Fetch all weather data for the specified city and year
        List<Weather> weatherData = weatherRepository.findByCityNameAndWeatherDateBetween(cityName, startOfYear, endOfYear);

        // Calculate the average temperature
        if (weatherData.isEmpty()) {
            throw new ResourceNotFoundException("No weather data found for city: " + cityName + " in year: " + year);
        }

        double totalTemperature = weatherData.stream()
                .mapToDouble(weather -> weather.getTemperature().doubleValue())
                .sum();
        double averageTemperature = totalTemperature / weatherData.size();
        BigDecimal roundedAverage = BigDecimal.valueOf(averageTemperature)
                .setScale(2, RoundingMode.HALF_UP);

        return roundedAverage.doubleValue();
    }


}
