package ru.petu.course.weatherRestApp2025.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "wind")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Wind {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_wind")
    private Integer idWind;

    @ManyToOne
    @JoinColumn(name = "id_weather", referencedColumnName = "id_weather", nullable = false)
    private Weather weather;

    @Column(name = "speed", nullable = false)
    private BigDecimal speed;

    @Column(name = "deg", nullable = false)
    private int deg;


}