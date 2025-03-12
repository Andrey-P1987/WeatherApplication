package ru.petu.course.weatherRestApp2025.models;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "sp_country")
@Data
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "country_id")
    private int countryId;

    @Column(name = "name")
    private String name;

//    @OneToMany(mappedBy = "country" , cascade = CascadeType.ALL)
//    private List<City> cities;

}