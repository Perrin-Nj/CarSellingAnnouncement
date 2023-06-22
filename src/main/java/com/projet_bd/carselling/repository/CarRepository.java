package com.projet_bd.carselling.repository;


import java.util.List;

import com.projet_bd.carselling.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CarRepository extends JpaRepository<Car, Long> {

    Car findByName(String name);

    List<Car> findCarsByMarque(String marque);
    
    List<Car> findCarsByType(String type);

    List<Car> findCarsByPrice(Double price);

}
