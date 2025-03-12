package ru.petu.course.weatherRestApp2025.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.petu.course.weatherRestApp2025.models.City;
import ru.petu.course.weatherRestApp2025.models.Country;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City, Integer> {

    @Query("SELECT c FROM City c JOIN c.country cn WHERE cn.name = :countryName")
    List<City> findCitiesByCountryName(String countryName);

    List<City> findByCountry_Name(String countryName);


}
