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

    @Column(unique = true)
    private String name;

    private String marque;
    private String type;
    private Double price;

    @Column(unique = true)
    private String numChassi;

    private List<String> imageLinks;

    @OneToMany(mappedBy = "car", fetch = FetchType.EAGER)
    private List<Photo> listImages;

}
