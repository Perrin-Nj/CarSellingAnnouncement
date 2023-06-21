package com.projet_bd.carselling.service;

import com.projet_bd.carselling.model.Car;
import com.projet_bd.carselling.model.Photo;
import com.projet_bd.carselling.repository.PhotoRepository;
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
@Service
public class PhotoStorageService {
    private final PhotoRepository photoRepository;

    public Photo store(MultipartFile file, Car car ) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Photo FileDB = new Photo(fileName, file.getContentType(), file.getBytes());

        FileDB.setCar(car);
        return photoRepository.save(FileDB);
    }

    public Photo getFile(String id) {
        return photoRepository.findById(id).get();
    }

    public Stream<Photo> getAllFiles() {
        return photoRepository.findAll().stream();
    }

}
