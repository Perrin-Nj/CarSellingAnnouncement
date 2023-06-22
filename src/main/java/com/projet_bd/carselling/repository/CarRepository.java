package com.projet_bd.carselling.repository;

import com.projet_bd.carselling.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    Car findCarByName(String name);

    //Car findByMarque(String marque);

    //Car findByType(String type);

    //Car findByPrice(Double price);

    Car findCarByNumChassi(String numChassi);
}
