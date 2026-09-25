package com.joaquin.curso.springboot.app.crudjpa.springbootcrudjpa.Validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.joaquin.curso.springboot.app.crudjpa.springbootcrudjpa.entities.Product;
@Component
public class ProductValidation implements Validator{

    @Override
    public boolean supports(Class<?> clazz) { //Este método da soporte a la clase que vamos a validar
        return Product.class.isAssignableFrom(clazz); //Retornamos la clase a la que le queramos dar soporte de esta manera
    }

    @Override
    public void validate(Object target, Errors errors) { //Es un método que luego se usará en el controlador, el primer parámetro es el elemento a validar y el segundo es el BindingResult
       Product product = (Product)target;
       ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", null,"es Requerido!");  //ValidationUtils tiene varios métodos de validación, en los parámetros se suele pasar primero el error, luego el atributo a validar como string y el mensaje de error
       //ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "NotBlanck.product.description");  
       
       if(product.getDescription() == null || product.getDescription().isBlank()){ //Manera aún más personalizada y gestionada
            errors.rejectValue("description", null, "es requerido por favor!");
       }

       if(product.getPrice()== null ){
        errors.rejectValue("price", null,"no puede ser nulo ok?");
       }else if(product.getPrice() < 500){
        errors.rejectValue("price", null,"debe ser un número mayor o igual a 500");
       }
    }
    
}
