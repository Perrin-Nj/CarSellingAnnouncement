package com.projet_bd.carselling.repository;

import com.projet_bd.carselling.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    Car findCarByName(String name);

    List<Car> findCarsByMarque(String marque);

    List<Car> findCarsByType(String type);

    List<Car> findCarsByPrice(Double price);

    Car findCarByNumChassi(String numChassi);
}
