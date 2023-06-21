package com.projet_bd.carselling.controller;


import com.projet_bd.carselling.model.Car;
import com.projet_bd.carselling.model.Photo;
import com.projet_bd.carselling.model.ResponsePhoto;
import com.projet_bd.carselling.service.CarService;
import com.projet_bd.carselling.service.PhotoStorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

//@RequestMapping("api/v1/car")
/*
@Controller
@CrossOrigin("*")
public class CarController {

    private final CarService carService;
    private final PhotoStorageService storageService;

    public CarController(CarService carService, PhotoStorageService storageService) {
        this.carService = carService;
        this.storageService = storageService;
    }

    @PostMapping(value = "/upload")
    public ResponseEntity<Car> uploadFile(
            @RequestParam("photo") MultipartFile[] photos,
            @RequestParam("name") String name,
            @RequestParam("marque") String marque,
            @RequestParam("type") String type,
            @RequestParam("prix") Double prix,
            @RequestParam("numChassi") String numChassi
    ) throws IOException {
        List<Photo> postedCarPhotos = new ArrayList<>();
        Car postedCar = new Car();
        postedCar.setName(name);
        postedCar.setPrice(prix);
        postedCar.setMarque(marque);
        postedCar.setType(type);
        postedCar.setNumChassi(numChassi);
        if(photos != null){
            for (MultipartFile file: photos){
                postedCarPhotos.add(storageService.store(file, postedCar));
            }
            postedCar.setListImages(postedCarPhotos);
        }


        Car car = carService.saveCar(postedCar);

        return ResponseEntity.ok(car);
        // ...
    }

    @GetMapping("/cars")
    public ResponseEntity<List<Car>> getListFiles() {
        List<Car> cars = carService.findAll();
        if(cars == null) return ResponseEntity.badRequest().build();
        List<Car> newCars = new ArrayList<>();
        AtomicReference<String> flag = new AtomicReference<>("null link");
        for(Car car:cars){
            List<ResponsePhoto> files = storageService.getAllFiles().map(dbFile -> {
                if (dbFile.getCar().equals(car)) {
                    String fileDownloadUri = ServletUriComponentsBuilder
                            .fromCurrentContextPath()
                            .path("/cars/")
                            .path(dbFile.getId())
                            .toUriString();
                    flag.set(fileDownloadUri);
                }
                return new ResponsePhoto(
                        dbFile.getName(),
                        flag.get(),
                        dbFile.getType(),
                        dbFile.getData().length);
            }).toList();
            car.setImageLinks(new ArrayList<>());
            for(ResponsePhoto file : files){
                car.getImageLinks().add(file.getUrl());
            }
            newCars.add(car);
        }
        return ResponseEntity.status(HttpStatus.OK).body(newCars);
    }
/*
        @GetMapping("/files/{id}")
        public ResponseEntity<byte[]> getFile(@PathVariable String id) {
            FileDB fileDB = storageService.getFile(id);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
                    .body(fileDB.getData());
        }

}*/
