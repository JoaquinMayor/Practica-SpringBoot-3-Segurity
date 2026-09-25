package com.joaquin.curso.springboot.app.crudjpa.springbootcrudjpa.repository;

import com.joaquin.curso.springboot.app.crudjpa.springbootcrudjpa.entities.Product;
import org.springframework.data.repository.CrudRepository;

public interface ICosasRespository extends CrudRepository<Product,Long> {
}
