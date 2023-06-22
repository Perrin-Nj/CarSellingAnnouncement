package com.projet_bd.carselling.service;

import com.projet_bd.carselling.model.Car;
import com.projet_bd.carselling.model.Photo;
import com.projet_bd.carselling.repository.CarRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;
    private final PhotoStorageService storageService;


    private final EntityManagerFactory entityManagerFactory;

    public Car findById(Long id) {
        try {
            return carRepository.findById(id).get();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Car> findAll() {
        try {
            return carRepository.findAll();
        }
        catch (Exception e) {
            return null;
        }
    }

   // @Transactional
    public Car saveCar(Car car) {
        try {
            return carRepository.save(car);
        }
        catch (Exception e) {
            return null;
        }
    }

    public Car findCarByName(String carName) {

        try {
            return carRepository.findCarByName(carName);
        }
        catch (Exception e) {
            System.out.println("can't find it");
            e.printStackTrace();
            return null;
        }
    }

    public List<Car> findByCarsType(String carType) {

        try {
            return carRepository.findCarsByType(carType);
        }
        catch (Exception e) {
            return null;
        }
    }

    public Car findByCarChassi(String numChassi) {

        try {
            return carRepository.findCarByNumChassi(numChassi);
        }
        catch (Exception e) {
            return null;
        }
    }

    public List<Car> findByCarPrice(Double carPrice) {

        try {
            return carRepository.findCarsByPrice(carPrice);
        }
        catch (Exception e) {
            return null;
        }
    }

    public List<Car> findByCarMarque(String carMarque) {

        try {
            return carRepository.findCarsByMarque(carMarque);
        }
        catch (Exception e) {
            return null;
        }
    }

}
