package com.joaquin.curso.springboot.app.crudjpa.springbootcrudjpa.services;

import java.util.List;

import com.joaquin.curso.springboot.app.crudjpa.springbootcrudjpa.entities.User;

public interface IUserService {
    
    List<User> findAll();

    User save(User user);

    boolean existsByUsername(String username);
}
