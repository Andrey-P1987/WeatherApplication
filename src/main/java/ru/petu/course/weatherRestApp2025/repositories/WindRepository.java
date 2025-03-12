package ru.petu.course.weatherRestApp2025.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.petu.course.weatherRestApp2025.models.Weather;
import ru.petu.course.weatherRestApp2025.models.Wind;

@Repository
public interface WindRepository extends JpaRepository<Wind, Integer> {

}
