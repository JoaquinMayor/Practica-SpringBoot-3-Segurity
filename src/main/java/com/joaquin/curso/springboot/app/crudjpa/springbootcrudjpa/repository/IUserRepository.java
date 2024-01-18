package com.joaquin.curso.springboot.app.crudjpa.springbootcrudjpa.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.joaquin.curso.springboot.app.crudjpa.springbootcrudjpa.entities.User;

public interface IUserRepository extends CrudRepository<User,Long>{
    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);
}
