package com.joaquin.curso.springboot.app.crudjpa.springbootcrudjpa.Validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RequiredValidation implements ConstraintValidator<IsRequired,String>{//En el constrain ponemos la validación y el tipo de dato a validar

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) { //Es el método de validación
        if(value!=null && !value.isEmpty() && !value.isBlank()){
            return true;
        }
        return false;
    } 
    
}
