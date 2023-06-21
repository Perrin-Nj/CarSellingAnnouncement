package com.projet_bd.carselling.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.projet_bd.carselling.model.Proprietor;

public interface ProprietorRepository extends JpaRepository<Proprietor, Long> {



    @Query(value = "select proprietor.* from proprietor, proprietor_email" +
            " where proprietor_email.email=:e and proprietor.password=:p" +
            " and proprietor.id = proprietor_email.proprietor_id", nativeQuery = true)
    public Proprietor findByEmailAndPassword(@Param("e") String email, @Param("p") String password);

}
