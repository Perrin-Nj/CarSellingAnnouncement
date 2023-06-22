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

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String marque;
    
    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private Double price;

    @Transient
    private List<String> imageLinks;

    @OneToOne(mappedBy = "car", cascade = CascadeType.ALL)
    private Announcement announcement;

    @OneToMany(mappedBy = "car", fetch = FetchType.EAGER)
    private List<Photo> listImages;

}
