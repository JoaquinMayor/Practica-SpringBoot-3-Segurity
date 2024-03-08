package com.joaquin.curso.springboot.app.crudjpa.springbootcrudjpa.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.joaquin.curso.springboot.app.crudjpa.springbootcrudjpa.entities.User;
import com.joaquin.curso.springboot.app.crudjpa.springbootcrudjpa.repository.IUserRepository;

@Service
public class JpaUserDetailsService implements UserDetailsService{
    @Autowired
    private IUserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { //Devuelve al usuario por nombre pero de springSecurity
       Optional<User> userOptional =userRepository.findByUsername(username);
      
       User user = userOptional.orElseThrow(() -> new UsernameNotFoundException(String.format("Username %s no existe en el sistema!", username)));
       List<GrantedAuthority> authorities = user.getRoles().stream()
       .map(role-> new SimpleGrantedAuthority(role.getName()))
       .collect(Collectors.toList());
       
       return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), 
       user.isEnabled(),true,true,true,authorities);
    }
    
}
