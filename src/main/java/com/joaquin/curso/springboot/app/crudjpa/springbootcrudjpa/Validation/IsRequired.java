package com.joaquin.curso.springboot.app.crudjpa.springbootcrudjpa.Validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = RequiredValidation.class) //Enlace con la clase de validación
@Retention(RetentionPolicy.RUNTIME) //Se ejecuta en tiempo de ejecución 
@Target({ElementType.FIELD, ElementType.METHOD})//Anotamos en cual va a servir
public @interface IsRequired { //Para hacerlo como anotación se hace con @interface, luego se tiene que complementar con una clase java de validación para desarrollarla
    String message() default "es requerido usando anotaciones";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
}
