package com.projet_bd.carselling.controller;


import com.projet_bd.carselling.model.Car;
import com.projet_bd.carselling.model.Photo;
import com.projet_bd.carselling.model.ResponsePhoto;
import com.projet_bd.carselling.service.CarService;
import com.projet_bd.carselling.service.PhotoStorageService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("api/v1/car")
@RestController
@AllArgsConstructor
public class CarController {

    private CarService carService;
    private PhotoStorageService storageService;

    @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
    public ResponseEntity<Car> uploadFile(
            @RequestParam("photo") MultipartFile tof,
            @RequestParam("name") String name,
            @RequestParam("marque") String marque,
            @RequestParam("type") String type,
            @RequestParam("prix") Double prix,
            @RequestParam("numChassi") String numChassi
    ) throws IOException {

        Car postedCar = new Car();
        postedCar.setName(name);
        postedCar.setPrice(prix);
        postedCar.setMarque(marque);
        postedCar.setType(type);
        postedCar.setNumChassi(numChassi);

        Car car = carService.saveCar(postedCar, tof);
        return ResponseEntity.ok(car);
        // ...
    }







        @GetMapping("/files")
        public ResponseEntity<List<ResponsePhoto>> getListFiles() {
            List<ResponsePhoto> files = storageService.getAllFiles().map(dbFile -> {
                String fileDownloadUri = ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .path("/files/")
                        .path(dbFile.getId())
                        .toUriString();

                    return new ResponsePhoto(
                        dbFile.getPhotoName(),
                        fileDownloadUri,
                        dbFile.getCar().getName());
            }).collect(Collectors.toList());

            return ResponseEntity.status(HttpStatus.OK).body(files);
        }
/*
        @GetMapping("/files/{id}")
        public ResponseEntity<byte[]> getFile(@PathVariable String id) {
            FileDB fileDB = storageService.getFile(id);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
                    .body(fileDB.getData());
        }
*/
}
