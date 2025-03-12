package ru.petu.course.weatherRestApp2025.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.petu.course.weatherRestApp2025.models.City;
import ru.petu.course.weatherRestApp2025.models.Weather;
import ru.petu.course.weatherRestApp2025.services.CityService;

import java.util.List;

@RestController
@RequestMapping("/cities")
public class CityController {

    private final CityService cityService;

    @Autowired
    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping("/findAll")
    public List<City> getCities(){
        List<City> getCities = cityService.findAll();
        return getCities;
    }



//    @GetMapping("/{cityId}/weather")
//    public Weather getWeather(@PathVariable int cityId) {
//        return cityService.saveWeatherByCityId(cityId);
//    }
//
//    @GetMapping("/showCities")
//    public List<City> getCitiesByCountry(@RequestParam String countryName) {
//        return cityService.getCitiesByCountryName(countryName);
//    }
}
