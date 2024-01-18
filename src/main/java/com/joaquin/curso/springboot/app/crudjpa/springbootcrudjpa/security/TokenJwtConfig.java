package com.joaquin.curso.springboot.app.crudjpa.springbootcrudjpa.security;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Jwts;

public class TokenJwtConfig {
    
    public static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build(); //Creando llave de seguridad, la primera parte de la clave, solo se queda en el servidor no tiene que ir a ningun otro lado
    public static final String PREFIX_TOKEN = "Bearer ";
    public static final String HEADER_AUTORIZATION = "Authorization";
}
