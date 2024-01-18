package com.joaquin.curso.springboot.app.crudjpa.springbootcrudjpa.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.joaquin.curso.springboot.app.crudjpa.springbootcrudjpa.entities.Role;

public interface IRoleRepository extends CrudRepository<Role,Long>{
    
    Optional<Role> findByName(String name);
    
}
