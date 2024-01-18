package com.joaquin.curso.springboot.app.crudjpa.springbootcrudjpa.Validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.joaquin.curso.springboot.app.crudjpa.springbootcrudjpa.services.IProductService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class IsExistDBValidation implements ConstraintValidator<IsExistDb,String>{
    @Autowired
    private IProductService service;
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(service == null){
            return true;
        }
        return  !service.existsBySku(value);
    }
    
}
