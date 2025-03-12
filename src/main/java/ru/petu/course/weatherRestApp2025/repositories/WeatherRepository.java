package ru.petu.course.weatherRestApp2025.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.petu.course.weatherRestApp2025.models.City;
import ru.petu.course.weatherRestApp2025.models.Weather;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface WeatherRepository extends JpaRepository<Weather, Integer> {
    Optional<Weather> findTopByCityOrderByWeatherDateDesc(City city);
    Weather findTopByOrderByWeatherDateDesc();
    List<Weather> findByCityNameAndWeatherDateBetween(String cityName, Instant startDate, Instant endDate);


}
