package com.joaquin.curso.springboot.app.crudjpa.springbootcrudjpa.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import com.joaquin.curso.springboot.app.crudjpa.springbootcrudjpa.Validation.ProductValidation;
import com.joaquin.curso.springboot.app.crudjpa.springbootcrudjpa.entities.Product;
import com.joaquin.curso.springboot.app.crudjpa.springbootcrudjpa.services.IProductService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private IProductService service;

    //@Autowired
    //private ProductValidation validation;

    @GetMapping
    public List<Product> list(){
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> view(@PathVariable Long id){
        Optional<Product> productOptioanal = service.findById(id);
        if(productOptioanal.isPresent()){
            return ResponseEntity.ok(productOptioanal.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }
    
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Product product, BindingResult result){ //Agregamos validation, y ponemos la anotación @Valid para validar de que se envie un dato correcto y en el producto (Entity creado), ponemos las reglas de validación
        //validation.validate(product, result); //Errores personalizados ubicados en ProductValidation
        if(result.hasFieldErrors()){//Esto es si ocurre algun error
            return validation(result);
        }
        Product productNew = service.save(product);                                                   //Tambien tenemos que poner el BindingResult, el cual siempre tiene que estar a la derecha del objeto a validar, este tiene todas las validaciones y mensajes de error que da el request
        return ResponseEntity.status(HttpStatus.CREATED).body(productNew);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update( @PathVariable Long id, @Valid @RequestBody Product product, BindingResult result){ //El valid siempre va al lado del @RequestBody
        //validation.validate(product, result);
        if(result.hasFieldErrors()){//Esto es si ocurre algun error es de la manera ya establecida el validation viene del @Valid
            return validation(result);
        }
        
        product.setId(id);
        Optional<Product> productOptioanl = service.update(id,product);
       
        if(productOptioanl.isPresent()){
            Product productNew;
            productNew = productOptioanl.orElseThrow();
            return ResponseEntity.status(HttpStatus.CREATED).body(productNew);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        Optional<Product> productOptioanal = service.delete(id);
        if(productOptioanal.isPresent()){
            return ResponseEntity.ok(productOptioanal.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    private ResponseEntity<Map<String, String>> validation(BindingResult result){
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(err ->{ //Da una lista de mensajesel getFieldErrors y lo recorremos para ir creando los mensajes
            errors.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors); //Siempre que se pasa un status 400 se hace un badRequest
    }

}
