package com.projet_bd.carselling.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projet_bd.carselling.model.ProprietorEmail;

public interface ProprietorEmailRepository extends JpaRepository<ProprietorEmail, String> {
    
}
