package com.projet_bd.carselling.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class ProprietorEmail {
    
    @Id    
    private String email;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Proprietor proprietor;
}
