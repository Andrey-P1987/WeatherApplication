package ru.petu.course.weatherRestApp2025.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.petu.course.weatherRestApp2025.models.Wind;
import ru.petu.course.weatherRestApp2025.repositories.WindRepository;
import ru.petu.course.weatherRestApp2025.services.WindService;

import java.util.List;

@RestController
@RequestMapping("/winds")
public class WindController {
    private final WindService windService;

    public WindController(WindService windService){
        this.windService = windService;
    }

    @GetMapping("/allWeatherForAllCities")
    public List<Wind>getWindsAndCities(){

        return windService.windsAndWeathers();
    }
}
