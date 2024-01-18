package com.joaquin.curso.springboot.app.crudjpa.springbootcrudjpa.security;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class SimpleGrantedAuthoritiesJsonCreator {
    
    @JsonCreator
    public SimpleGrantedAuthoritiesJsonCreator(@JsonProperty("authority") String role){

    }
}
