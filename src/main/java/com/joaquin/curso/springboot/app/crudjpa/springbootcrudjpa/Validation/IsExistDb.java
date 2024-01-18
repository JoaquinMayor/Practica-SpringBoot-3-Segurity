package com.joaquin.curso.springboot.app.crudjpa.springbootcrudjpa.Validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IsExistDBValidation.class)
public @interface IsExistDb {
    
    String message() default "ya existe en la base de datos!";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
}
