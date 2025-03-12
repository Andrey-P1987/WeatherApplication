package ru.petu.course.weatherRestApp2025.models;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "sp_city")
@Getter
@Setter
@NoArgsConstructor
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_city")
    private Integer idCity;
    @Getter
    @Setter
    @Column(name = "name")
    private String name;



    @Column(name = "coordinate_latitude")
    private BigDecimal coordinateLatitude;



    @Column(name = "coordinate_longitude")
    private BigDecimal coordinateLongitude;

    @ManyToOne
    @JoinColumn(name = "country_id", referencedColumnName = "country_id")
    private Country country;








//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }


}