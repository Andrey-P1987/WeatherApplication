package ru.petu.course.weatherRestApp2025.services;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import ru.petu.course.weatherRestApp2025.models.Weather;
import ru.petu.course.weatherRestApp2025.models.Wind;
import ru.petu.course.weatherRestApp2025.repositories.CityRepository;
import ru.petu.course.weatherRestApp2025.repositories.WeatherRepository;
import ru.petu.course.weatherRestApp2025.repositories.WindRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class WindService {
    private final WindRepository windRepository;
    private final WeatherRepository weatherRepository;
    private final CityRepository cityRepository;


    public WindService(WindRepository windRepository, WeatherRepository weatherRepository,
                       CityRepository cityRepository){
        this.windRepository = windRepository;
        this.weatherRepository = weatherRepository;
        this.cityRepository = cityRepository;
    }

    @Transactional
    public List<Wind> windsAndWeathers() {
        List<Wind> winds = windRepository.findAll();
        List<Wind> newWinds = new ArrayList<>();

        Weather lastWeather = weatherRepository.findTopByOrderByWeatherDateDesc();

        Instant nextWeatherInstant;
//        if (lastWeather != null) {
        // Convert Instant to LocalDate, increment by 1 day
        LocalDate lastWeatherLocalDate = Instant.ofEpochSecond(lastWeather.getWeatherDate().getEpochSecond())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        LocalDate nextWeatherDate = lastWeatherLocalDate.plusDays(1);

        // Convert back to Instant at start of the new day
        nextWeatherInstant = nextWeatherDate.atStartOfDay(ZoneOffset.UTC).toInstant(); //? ZoneOffset.UTC ? ZoneId.systemDefault()?
        // Define the end date (2025-03-01)
        Instant endDate = Instant.parse("2025-03-11T00:00:00Z");


//        } else {
        // If no records exist, start from 01.01.2000
//            nextWeatherInstant = LocalDate.of(2000, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant();
//        }


        List<String> weatherDescriptions = Arrays.asList(
                "Clear Sky", "Few Clouds", "Scattered Clouds", "Broken Clouds", "Overcast Clouds",
                "Overcast", "Rain", "Drizzle", "Showers", "Thunderstorms", "Mist", "Fog",
                "Hazy", "Smoke", "Windy", "Frost", "Snow", "Light Snow", "Flurries", "Sleet",
                "Hail", "Blizzard"
        );

        Random random = new Random();
        while (nextWeatherInstant.isBefore(endDate)) {

            for (Wind oldWind : winds) {
                Weather oldWeather = oldWind.getWeather();

                BigDecimal newTemperature = BigDecimal.valueOf(-50 + Math.random() * 100).setScale(2, RoundingMode.HALF_UP);
                BigDecimal newHumidity = BigDecimal.valueOf(Math.random() * 100).setScale(2, RoundingMode.HALF_UP);

                Weather newWeather = new Weather();
                newWeather.setCity(oldWeather.getCity());
                newWeather.setTemperature(newTemperature);
                newWeather.setHumidity(newHumidity.intValue());
                newWeather.setDescription(weatherDescriptions.get(random.nextInt(weatherDescriptions.size())));
                newWeather.setSunrise(oldWeather.getSunrise());
                newWeather.setSunset(oldWeather.getSunset());

                newWeather.setWeatherDate(nextWeatherInstant);
                //newWeather.setWeatherDate(Instant.parse("2000-01-01T00:00:00Z"));

                weatherRepository.save(newWeather);

                Wind newWind = new Wind();
                newWind.setWeather(newWeather);
                newWind.setSpeed(BigDecimal.valueOf(Math.random() * 30).setScale(2, RoundingMode.HALF_UP));
                newWind.setDeg((int) (Math.random() * 360));

                newWinds.add(newWind);
//            System.out.println("Previous date: " + lastWeather.getWeatherDate());
//            System.out.println("Next date: " + nextWeatherInstant);
            }
            nextWeatherInstant = nextWeatherInstant.plus(1, ChronoUnit.DAYS);
        }
        return windRepository.saveAll(newWinds);
        // return windRepository.findAll();
    }
}
