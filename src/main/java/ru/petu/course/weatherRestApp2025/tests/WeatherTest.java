//package ru.petu.course.weatherRestApp2025.tests;
//
//import org.junit.Test;
//import ru.petu.course.weatherRestApp2025.models.Weather;
//
//import java.math.BigDecimal;
//import java.time.Instant;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//
//public class WeatherTest {
//    @Test
//    public void testWeatherEntity() {
//        Weather weather = new Weather();
//        weather.setDescription("Clear sky");
//        weather.setTemperature(BigDecimal.valueOf(25.5));
//        weather.setHumidity(60);
//        weather.setSunrise(Instant.now());
//        weather.setSunset(Instant.now().plusSeconds(12 * 60 * 60)); // 12 hours later
//
//        assertNotNull(weather.getWeatherDate());
//        assertEquals("Clear sky", weather.getDescription());
//        assertEquals(BigDecimal.valueOf(25.5), weather.getTemperature());
//        assertEquals(60, weather.getHumidity());
//    }
//}
