package com.projet_bd.carselling.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Photo {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String photoName;

    @Column(name = "image", columnDefinition="BLOB")
    private byte[] image;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Car car;

    public Photo(String photoName, byte[] byteImage){
        this.photoName = photoName;
        this.image = byteImage;
    }
}
