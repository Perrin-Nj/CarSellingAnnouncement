package com.projet_bd.carselling.service;

import com.projet_bd.carselling.model.Car;
import com.projet_bd.carselling.model.Photo;
import com.projet_bd.carselling.repository.PhotoRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

@RequiredArgsConstructor

@Transactional
@Service
public class PhotoStorageService {
    private final PhotoRepository photoRepository;
    private final EntityManagerFactory entityManagerFactory;



    public Photo store(MultipartFile file, Car car ) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Photo FileDB = new Photo(fileName, file.getContentType(), file.getBytes());

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();
        car = entityManager.find(Car.class, car.getId());
        FileDB = entityManager.merge(FileDB);
        FileDB.setCar(car);
        
        transaction.commit();
        entityManager.close();

        return FileDB;
        
    }

    public Photo getFile(String id) {
        return photoRepository.findById(id).get();
    }

    public Stream<Photo> getAllFiles() {
        return photoRepository.findAll().stream();
    }

}
