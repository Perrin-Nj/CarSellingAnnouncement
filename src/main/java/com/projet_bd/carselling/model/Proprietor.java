package com.projet_bd.carselling.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Proprietor extends User {
  
    @JsonIgnore
    @OneToMany(mappedBy = "proprietor")
    private List<Announcement> announcements;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "proprietor", cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    private List<ProprietorEmail> emails;
}
