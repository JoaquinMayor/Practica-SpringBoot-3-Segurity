package com.joaquin.curso.springboot.app.crudjpa.springbootcrudjpa.entities;

import com.joaquin.curso.springboot.app.crudjpa.springbootcrudjpa.Validation.IsExistDb;
import com.joaquin.curso.springboot.app.crudjpa.springbootcrudjpa.Validation.IsRequired;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import jakarta.validation.constraints.Min;
//import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @IsExistDb
    @IsRequired
     private String sku;

    @NotEmpty(message="{NotEmpty.product.name}") //Validación de que no sea vacio, se usa en Strings, para los objetos se usa @NotNull
    @Size(min=3, max=20)//Determina el mínimo y el máximo del String
    private String name; //Para editar el mensaje primero lo ponemos en el properties (correctamente configurado), y luego le podemos editar caracteristicas, en este caso el mensaje
    
    //@Pattern Para hacer un tipo de validqación personalizada
    @Min(value = 500, message = "{Min.product.price}") //Valida que el valor mínimo sea 500, tambien existe el @Max
    @NotNull(message = "{NotNull.product.price}")
    private Integer price;
    
    //@NotBlank(message = "{NotBlanck.product.description}") //Valida que el atributo no este vacio o tenga un caracter en blanco
    @IsRequired
    private String description;
    
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getPrice() {
        return price;
    }
    public void setPrice(Integer price) {
        this.price = price;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getSku() {
        return sku;
    }
    public void setSku(String sku) {
        this.sku = sku;
    }
    

    
}
