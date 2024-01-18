package com.joaquin.curso.springboot.app.crudjpa.springbootcrudjpa.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.joaquin.curso.springboot.app.crudjpa.springbootcrudjpa.entities.Role;
import com.joaquin.curso.springboot.app.crudjpa.springbootcrudjpa.entities.User;
import com.joaquin.curso.springboot.app.crudjpa.springbootcrudjpa.repository.IRoleRepository;
import com.joaquin.curso.springboot.app.crudjpa.springbootcrudjpa.repository.IUserRepository;



@Service
public class UserServiceImpl implements IUserService{
    @Autowired
    private IUserRepository repository;
    @Autowired
    private IRoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return (List<User>)repository.findAll();
    }

    @Override
    @Transactional
    public User save(User user) {
        Optional<Role> optionalRoleUser = roleRepository.findByName("ROLE_USER");
        List<Role> roles = new ArrayList<>();
        optionalRoleUser.ifPresent(role ->roles.add(role));
        if(user.isAdmin()){
            Optional<Role> optionalRoleAdmin = roleRepository.findByName("ROLE_ADMIN");
            optionalRoleAdmin.ifPresent(role -> roles.add(role));
        }

        user.setRoles(roles);
        String passwordEncoded = passwordEncoder.encode(user.getPassword()) ; //Codificación de la contraseña
        user.setPassword(passwordEncoded);
        return repository.save(user);
    }

    @Override
    public boolean existsByUsername(String username) {
        return repository.existsByUsername(username);
    }
    
}
