package com.projet_bd.carselling.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projet_bd.carselling.model.Proprietor;
import com.projet_bd.carselling.model.ProprietorEmail;
import com.projet_bd.carselling.pojo.request.LoginRequest;
import com.projet_bd.carselling.pojo.request.UpdateRequest;
import com.projet_bd.carselling.pojo.response.ResponseMessage;
import com.projet_bd.carselling.service.ProprietorService;

import lombok.RequiredArgsConstructor;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/proprietor")
@RequiredArgsConstructor
public class ProprietorController {

    private final ProprietorService proprietorService;




    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Proprietor proprietor) {
        
        for (ProprietorEmail email: proprietor.getEmails()) {

            if (proprietorService.existsByEmail(email.getEmail()))
                    return ResponseEntity.badRequest()
                        .body(new ResponseMessage(email.getEmail() + " is already used"));
            
        }

        boolean ok = proprietorService.save(proprietor);

        if (ok) return ResponseEntity.ok(new ResponseMessage("account created"));

        return ResponseEntity.badRequest().body(new ResponseMessage("an error occured"));
    

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

        Proprietor proprietor = proprietorService.findByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword());

        if (proprietor == null) return ResponseEntity.ok(new ResponseMessage("Incorrect email or password"));
        
        return ResponseEntity.ok(proprietor);
    }

   

    @PutMapping("/{id}/password")
    public ResponseEntity<?> updatePassword(@PathVariable("id") Long id,  @RequestBody UpdateRequest request) {

        Proprietor proprietor = proprietorService.findById(id);
        
        proprietor.setPassword(request.getContent());
        proprietor = proprietorService.update(proprietor);

        return ResponseEntity.ok( new ResponseMessage("password updated"));


    }

    @PutMapping("/{id}/username")
    public ResponseEntity<?> udpateUsername(@PathVariable("id") Long id, @RequestBody UpdateRequest request) {

        Proprietor proprietor = proprietorService.findById(id);
        proprietor.setUsername(request.getContent());

        return ResponseEntity.ok( new ResponseMessage("username updated"));
    }


    @DeleteMapping("/{id}/email")
    public ResponseEntity<?> deleteEmail(@RequestBody UpdateRequest request) {
        
        proprietorService.deleteEmail(request.getContent());
        return ResponseEntity.ok(
            new ResponseMessage(request.getContent() + " has been deleted from your account"));

    }
}