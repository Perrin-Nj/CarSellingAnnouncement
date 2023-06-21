package com.projet_bd.carselling.repository;

import com.projet_bd.carselling.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long> {

    Car findByName(String name);

    Car findByMarque(String marque);

    Car findByType(String type);

    Car findByPrice(Double price);

    Car findByNumChassi(String numChassi);
}
