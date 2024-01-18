package com.joaquin.curso.springboot.app.crudjpa.springbootcrudjpa.Validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.joaquin.curso.springboot.app.crudjpa.springbootcrudjpa.services.IUserService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class ExistByUsernameValidation implements ConstraintValidator<ExistByUsername,String>{
    @Autowired
    private IUserService service;

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
      if(service == null){
        return true;
    }
      
      return !service.existsByUsername(username);
    }
    
}
