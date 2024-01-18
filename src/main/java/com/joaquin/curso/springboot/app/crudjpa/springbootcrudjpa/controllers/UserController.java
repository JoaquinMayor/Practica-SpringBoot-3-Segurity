package com.joaquin.curso.springboot.app.crudjpa.springbootcrudjpa.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.joaquin.curso.springboot.app.crudjpa.springbootcrudjpa.entities.User;
import com.joaquin.curso.springboot.app.crudjpa.springbootcrudjpa.services.IUserService;

import jakarta.validation.Valid;

@CrossOrigin(origins = "http://localhost:4200") // Para comincar con el front-end, primero ponemos en origin la dirección del frontend
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private IUserService service;

    @GetMapping
    public List<User> list(){
        return service.findAll();
    }
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody User user, BindingResult result){
        if(result.hasFieldErrors()){//Esto es si ocurre algun error
            return validation(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(user));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody User user, BindingResult result){
        if(result.hasFieldErrors()){//Esto es si ocurre algun error
            return validation(result);
        }
        user.setAdmin(false);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(user));
    }

    private ResponseEntity<Map<String, String>> validation(BindingResult result){
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(err ->{ //Da una lista de mensajesel getFieldErrors y lo recorremos para ir creando los mensajes
            errors.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors); //Siempre que se pasa un status 400 se hace un badRequest
    }
}
