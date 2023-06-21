package com.projet_bd.carselling.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;
    private String marque;
    private String type;
    private Double price;
    private String numChassi;

    @OneToMany(mappedBy = "car", fetch = FetchType.EAGER)
    private List<Photo> listImages;

}
