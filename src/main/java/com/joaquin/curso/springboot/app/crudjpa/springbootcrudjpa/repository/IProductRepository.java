package com.joaquin.curso.springboot.app.crudjpa.springbootcrudjpa.repository;

import org.springframework.data.repository.CrudRepository;

import com.joaquin.curso.springboot.app.crudjpa.springbootcrudjpa.entities.Product;

public interface IProductRepository extends CrudRepository<Product,Long>{
    
    boolean existsBySku(String sku);
}
