package com.projet_bd.carselling.service;



import org.hibernate.TransactionException;
import org.springframework.stereotype.Service;

import com.projet_bd.carselling.model.Proprietor;
import com.projet_bd.carselling.model.ProprietorEmail;
import com.projet_bd.carselling.repository.ProprietorEmailRepository;
import com.projet_bd.carselling.repository.ProprietorRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class ProprietorService {

    private final ProprietorEmailRepository proprietorEmailRepository;
    private final ProprietorRepository proprietorRepository;
    private final EntityManagerFactory entityManagerFactory;

    public Proprietor findById(Long id) {
        try {
            return proprietorRepository.findById(id).get();
        }
        catch (Exception e) {
            return null;
        }
        
    }

    @Transactional
    public boolean save(Proprietor proprietor) {
        

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
            for (ProprietorEmail email : proprietor.getEmails()) {
                email.setProprietor(proprietor);
                
            }
            entityManager.merge(proprietor);
            transaction.commit();

            entityManager.close();

            return true;
        }
        catch (TransactionException e) {
            return false;
        }
       
    }
    
    public boolean existsByEmail(String email) {
        return proprietorEmailRepository.existsById(email);
    }

    
    public Proprietor findByEmailAndPassword( String email, String password) {
        return proprietorRepository.findByEmailAndPassword(email, password);
    }

    public Proprietor update (Proprietor proprietor) {
        try {
            return proprietorRepository.save(proprietor);
        }
        catch (Exception e) {
            return null;
        }
    }

    
    public boolean deleteEmail(String email) {
        try {
            proprietorEmailRepository.deleteById(email);
            return true;
        }
        catch (Exception e) {
            return false;
        }

    }


}
