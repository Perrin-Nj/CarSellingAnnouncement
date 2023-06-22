package com.projet_bd.carselling.controller;


import com.projet_bd.carselling.model.Announcement;
import com.projet_bd.carselling.model.Car;
import com.projet_bd.carselling.model.Photo;
import com.projet_bd.carselling.model.Proprietor;
import com.projet_bd.carselling.model.ResponseMessage;
import com.projet_bd.carselling.model.ResponsePhoto;
import com.projet_bd.carselling.pojo.request.UpdateRequest;
import com.projet_bd.carselling.repository.CarRepository;
import com.projet_bd.carselling.repository.PhotoRepository;
import com.projet_bd.carselling.service.CarService;
import com.projet_bd.carselling.service.PhotoStorageService;
import com.projet_bd.carselling.service.ProprietorService;

import jakarta.persistence.UniqueConstraint;
import org.hibernate.QueryException;
import org.springframework.beans.factory.annotation.Autowired;
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

@Controller
@CrossOrigin("*")
public class CarController {
    @Autowired
    private CarService carService;
    @Autowired
    private PhotoStorageService storageService;
    @Autowired ProprietorService proprietorService;
    
    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private CarRepository carRepository;


    @PostMapping(value = "/car/{id}/announcement/upload")
    public ResponseEntity<Car> uploadFile(
            @PathVariable("id") Long id,
            @RequestParam("photo") MultipartFile[] photos,
            @RequestParam("name") String name,
            @RequestParam("marque") String marque,
            @RequestParam("type") String type,
            @RequestParam("prix") Double prix,
            @RequestParam("titre") String title,
            @RequestParam("description") String description
    ) throws IOException {
        List<Photo> postedCarPhotos = new ArrayList<>();
        Car postedCar = new Car();
        postedCar.setName(name);
        postedCar.setPrice(prix);
        postedCar.setMarque(marque);
        postedCar.setType(type);

        Proprietor proprietor = proprietorService.findById(id);
        
        Announcement announcement = new Announcement(null, title, description, postedCar, proprietor);
        
        postedCar.setAnnouncement(announcement);

        Car car = carService.saveCar(postedCar);

        if(photos != null){
            for (MultipartFile file: photos){
                postedCarPhotos.add(storageService.store(file, postedCar));
            }
            postedCar.setListImages(postedCarPhotos);
        }




        return ResponseEntity.ok(car);
    }


    @PutMapping("/car/{id}/price")
    public ResponseEntity<?> updateCarPrice(@PathVariable("id") Long id, @RequestBody UpdateRequest request) {        Car car = carService.findById(id);
        car.setPrice(Double.valueOf(request.getContent()));
        
        carService.saveCar(car);
        
        return ResponseEntity.ok(new ResponseMessage("car price updated"));
    }


    @PutMapping("/car/{id}/name")
    public ResponseEntity<?> updateCarName(@PathVariable("id") Long id, @RequestBody UpdateRequest request) {        Car car = carService.findById(id);
        car.setName(request.getContent());
        
        carService.saveCar(car);
        
        return ResponseEntity.ok(new ResponseMessage("car name updated"));
    }

    @PutMapping("/car/{id}")
    public ResponseEntity<?> updateCar(@PathVariable("id") Long id, @RequestBody Car car) {

        car = carService.saveCar(car);
    
        if(car == null) return ResponseEntity.badRequest().body(new ResponseMessage("an error occured"));
        return ResponseEntity.ok(new ResponseMessage("car information updated"));
    }


    @PutMapping("/car/{id}/marque")
    public ResponseEntity<?> updateCarMarque(@PathVariable("id") Long id, @RequestBody UpdateRequest request) {        Car car = carService.findById(id);
        car.setMarque(request.getContent());
        
        carService.saveCar(car);
        
        return ResponseEntity.ok(new ResponseMessage("car brand updated"));
    }



    @PutMapping("/car/{id}/announcement/title")
    public ResponseEntity<?> updateAnnouncementTitle(@PathVariable("id") Long id, @RequestBody UpdateRequest request) {

        Car car = carService.findById(id);
        Announcement announcement = car.getAnnouncement();
        announcement.setTitle(request.getContent());
        announcement.setCar(car);
        car.setAnnouncement(announcement); 
        carRepository.save(car);

        return ResponseEntity.ok(new ResponseMessage("updated"));

    }

    @PutMapping("/car/{id}/announcement/description")
    public ResponseEntity<?> updateAnnouncementDescription(@PathVariable("id") Long id, @RequestBody UpdateRequest request) {

        Car car = carService.findById(id);
        Announcement announcement = car.getAnnouncement();
        announcement.setDescription(request.getContent());
        announcement.setCar(car);
        car.setAnnouncement(announcement); 
        carRepository.save(car);

        return ResponseEntity.ok(new ResponseMessage("updated"));

    }


    @GetMapping("/cars")
    public ResponseEntity<?> getListCars() {
        try{
        List<Car> cars = carService.findAll();
        List<ResponsePhoto> responsePhotos = storageService.getAllFiles().map(dbFile -> {
            String fileDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/files/")
                    .path(dbFile.getId())
                    .toUriString();

            return new ResponsePhoto(
                    dbFile.getName(),
                    fileDownloadUri,
                    dbFile.getType(),
                    dbFile.getData().length);
        }).collect(Collectors.toList());

        List<Photo> photos = photoRepository.findAll();
        List<Car> carListFinal = new ArrayList<>();

        //init links
        for(Car car: cars){
            car.setImageLinks(new ArrayList<>());
        }

        for(ResponsePhoto responsePhoto: responsePhotos){
            for(Photo photo: photos){
                for(Car car: cars){

                    if(responsePhoto.getName().equals(photo.getName())){
                        if(photo.getCar().getId().equals(car.getId())){
                            car.getImageLinks().add(responsePhoto.getUrl());
                        }
                    }
                    carListFinal.add(car);
                }
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(cars);}catch ( Exception e){
            return ResponseEntity.badRequest().body("An error occured, an image name should be unique");
        }
    }

    @GetMapping("/car/{id}")
    public ResponseEntity<?> findCarById(@PathVariable("id") Long id){
        Car car = carService.findById(id);
        if(car == null) return ResponseEntity.badRequest().body("No car with the specified id");
        car.setImageLinks(new ArrayList<>());
        List<ResponsePhoto> photoResponses = storageService.getAllFiles().map(dbFile -> {
            String fileDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/files/")
                    .path(dbFile.getId())
                    .toUriString();

            return new ResponsePhoto(
                    dbFile.getName(),
                    fileDownloadUri,
                    dbFile.getType(),
                    dbFile.getData().length);
        }).collect(Collectors.toList());
        List<Photo> photos = photoRepository.findAll();

        for(ResponsePhoto responsePhoto: photoResponses){
            for(Photo photo: photos){
                    if(responsePhoto.getName().equals(photo.getName())){
                        if(photo.getCar().getId().equals(car.getId())){
                            car.getImageLinks().add(responsePhoto.getUrl());
                        }
                    }
            }
        }
        return ResponseEntity.ok(car);
    }
    @GetMapping("/car/name/{name}/")
    public ResponseEntity<?> findCarByName(@PathVariable("name") String name){
        Car car = carService.findCarByName(name);

        if(car == null) return ResponseEntity.badRequest().body("No car with the specified name");
        car.setImageLinks(new ArrayList<>());
        List<ResponsePhoto> photoResponses = storageService.getAllFiles().map(dbFile -> {
            String fileDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/files/")
                    .path(dbFile.getId())
                    .toUriString();

            return new ResponsePhoto(
                    dbFile.getName(),
                    fileDownloadUri,
                    dbFile.getType(),
                    dbFile.getData().length);
        }).collect(Collectors.toList());
        List<Photo> photos = photoRepository.findAll();

        for(ResponsePhoto responsePhoto: photoResponses){
            for(Photo photo: photos){
                if(responsePhoto.getName().equals(photo.getName())){
                    if(photo.getCar().getId().equals(car.getId())){
                        car.getImageLinks().add(responsePhoto.getUrl());
                    }
                }
            }
        }
        return ResponseEntity.ok(car);
    }

    @GetMapping("/car/chassi/{id}/")
    public ResponseEntity<?> findCarByNumChassi(@PathVariable("id") Long id){
        Car car = carService.findById(id);
        if(car == null) return ResponseEntity.badRequest().body("No car with the specified chassi");
        car.setImageLinks(new ArrayList<>());
        List<ResponsePhoto> photoResponses = storageService.getAllFiles().map(dbFile -> {
            String fileDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/files/")
                    .path(dbFile.getId())
                    .toUriString();

            return new ResponsePhoto(
                    dbFile.getName(),
                    fileDownloadUri,
                    dbFile.getType(),
                    dbFile.getData().length);
        }).collect(Collectors.toList());
        List<Photo> photos = photoRepository.findAll();

        for(ResponsePhoto responsePhoto: photoResponses){
            for(Photo photo: photos){
                if(responsePhoto.getName().equals(photo.getName())){
                    if(photo.getCar().getId().equals(car.getId())){
                        car.getImageLinks().add(responsePhoto.getUrl());
                    }
                }
            }
        }
        return ResponseEntity.ok(car);
    }

    @GetMapping("/cars/marque/{marque}")
    public ResponseEntity<?> getCarsByMarque(@PathVariable("marque") String  marque) {
        try{
            List<Car> cars = carService.findByCarMarque(marque);
            List<ResponsePhoto> responsePhotos = storageService.getAllFiles().map(dbFile -> {
                String fileDownloadUri = ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .path("/files/")
                        .path(dbFile.getId())
                        .toUriString();

                return new ResponsePhoto(
                        dbFile.getName(),
                        fileDownloadUri,
                        dbFile.getType(),
                        dbFile.getData().length);
            }).collect(Collectors.toList());

            List<Photo> photos = photoRepository.findAll();
            List<Car> carListFinal = new ArrayList<>();

            //init links
            for(Car car: cars){
                car.setImageLinks(new ArrayList<>());
            }

            for(ResponsePhoto responsePhoto: responsePhotos){
                for(Photo photo: photos){
                    for(Car car: cars){

                        if(responsePhoto.getName().equals(photo.getName())){
                            if(photo.getCar().getId().equals(car.getId())){
                                car.getImageLinks().add(responsePhoto.getUrl());
                            }
                        }
                        carListFinal.add(car);
                    }
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(cars);}catch ( Exception e){
            return ResponseEntity.badRequest().body("An error occured, an image name should be unique");
        }
    }

    @GetMapping("/cars/type/{type}")
    public ResponseEntity<?> getCarsByType(@PathVariable("type") String  type) {
        try{
            List<Car> cars = carService.findByCarsType(type);
            if(cars.isEmpty()) return ResponseEntity.ok(new ResponseMessage("no results found"));
            List<ResponsePhoto> responsePhotos = storageService.getAllFiles().map(dbFile -> {
                String fileDownloadUri = ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .path("/files/")
                        .path(dbFile.getId())
                        .toUriString();

                return new ResponsePhoto(
                        dbFile.getName(),
                        fileDownloadUri,
                        dbFile.getType(),
                        dbFile.getData().length);
            }).collect(Collectors.toList());

            List<Photo> photos = photoRepository.findAll();
            List<Car> carListFinal = new ArrayList<>();

            //init links
            for(Car car: cars){
                car.setImageLinks(new ArrayList<>());
            }

            for(ResponsePhoto responsePhoto: responsePhotos){
                for(Photo photo: photos){
                    for(Car car: cars){

                        if(responsePhoto.getName().equals(photo.getName())){
                            if(photo.getCar().getId().equals(car.getId())){
                                car.getImageLinks().add(responsePhoto.getUrl());
                            }
                        }
                        carListFinal.add(car);
                    }
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(cars);}catch ( Exception e){
                e.printStackTrace();
            return ResponseEntity.badRequest().body("An error occured, an image name should be unique");
        }
    }


    @GetMapping("/cars/price/{price}")
    public ResponseEntity<?> getCarsByPrice(@PathVariable("price") Double  price) {
        try{
            List<Car> cars = carService.findByCarPrice(price);
            List<ResponsePhoto> responsePhotos = storageService.getAllFiles().map(dbFile -> {
                String fileDownloadUri = ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .path("/files/")
                        .path(dbFile.getId())
                        .toUriString();

                return new ResponsePhoto(
                        dbFile.getName(),
                        fileDownloadUri,
                        dbFile.getType(),
                        dbFile.getData().length);
            }).collect(Collectors.toList());

            List<Photo> photos = photoRepository.findAll();
            List<Car> carListFinal = new ArrayList<>();

            //init links
            for(Car car: cars){
                car.setImageLinks(new ArrayList<>());
            }

            for(ResponsePhoto responsePhoto: responsePhotos){
                for(Photo photo: photos){
                    for(Car car: cars){
                        if(responsePhoto.getName().equals(photo.getName())){
                            if(photo.getCar().getId().equals(car.getId())){
                                car.getImageLinks().add(responsePhoto.getUrl());
                            }
                        }
                        carListFinal.add(car);
                    }
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(cars);}catch ( Exception e){
            return ResponseEntity.badRequest().body("An error occured, an image name should be unique");
        }
    }


    @DeleteMapping("/car/{id}")
    public ResponseEntity<?> deleteCar(@PathVariable("id") Long id) {
        Car car = carService.findById(id);

        boolean ok = carService.delete(id);
        
        if (ok == true) return ResponseEntity.ok(new ResponseMessage("announcement deleted"));
   
        return ResponseEntity.badRequest().body(new ResponseMessage("an error occured"));
    }

}
