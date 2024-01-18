package com.joaquin.curso.springboot.app.crudjpa.springbootcrudjpa.security.filter;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joaquin.curso.springboot.app.crudjpa.springbootcrudjpa.entities.User;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import static com.joaquin.curso.springboot.app.crudjpa.springbootcrudjpa.security.TokenJwtConfig.*;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{ //Creación del token una vez ingresa el username y el password
    
    private AuthenticationManager authenticationManager;
    



    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) //se trabaja desde el postman con localhost:8080/login
            throws AuthenticationException {
            User user = null;
            String username = null;
            String password = null;

            try {
                user = new ObjectMapper().readValue(request.getInputStream(), User.class); //Del reques viene nuestro json con el usuario que manejamos en el jpauserDetailsService y con el primer parámetro le pasamos el json en formato de Stream, luego se le pasa el tipo de objeto al que lo queremos convertir
                username = user.getUsername();
                password = user.getPassword();
            } catch (StreamReadException e) {
                e.printStackTrace();
            } catch (DatabindException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } 
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password); //Aquí se crea un objeto `UsernamePasswordAuthenticationToken`, que es una implementación de la interfaz `Authentication` de Spring Security. Este objeto representa la información de autenticación proporcionada por el usuario, en este caso, el nombre de usuario y la contraseña.

            return authenticationManager.authenticate(authenticationToken);//Finalmente, se utiliza el `AuthenticationManager` para autenticar al usuario. El `AuthenticationManager` es un componente de Spring Security que se encarga de realizar la autenticación. 
            //Al llamar al método `authenticate` del `authenticationManager` y pasarle el objeto `UsernamePasswordAuthenticationToken`, se inicia el proceso de autenticación. Dependiendo de la configuración de autenticación de tu aplicación, se realizarán las validaciones necesarias, como verificar las credenciales del usuario y la existencia del usuario en la base de datos.
    }
     //Para implementar este método es necesario desde el github de jwt implementarlo en el pom
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, //Es un método que se ejecuta si todo sale bien
            Authentication authResult) throws IOException, ServletException {
                
                org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User)authResult.getPrincipal(); //usuario pero de springsecurity
                String username = user.getUsername();
                Collection<? extends GrantedAuthority> roles = authResult.getAuthorities(); //Asignamos los roles que 
                Claims claims = Jwts.claims().add("authorities", new ObjectMapper().writeValueAsString(roles)).build(); //Construye el claims que es un tipo de información que queresmos agregar al token
                

                String token = Jwts.builder().subject(username).expiration(new Date(System.currentTimeMillis() + 3600000))//El token expira en una hora
                .claims(claims)
                .issuedAt(new Date()) //Fecha en la que se crea
                .signWith(SECRET_KEY).compact();//Generando el Token
                response.addHeader(HEADER_AUTORIZATION, PREFIX_TOKEN + token); //Se pasa el token al usuario

                Map<String, String> body = new HashMap<>();
                body.put("token", token);
                body.put("username",username);
                body.put("message", String.format("Hola %s has iniciado sesión con éxito!", username));

                response.getWriter().write(new ObjectMapper().writeValueAsString(body));
                response.setContentType("aplication/json");
                response.setStatus(200);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, //Método por si falla la verificación
            AuthenticationException failed) throws IOException, ServletException {
                Map<String, String> body = new HashMap<>();
                body.put("message", "Error en la autenticación o password incorrectos");
                body.put("error", failed.getMessage());

                response.getWriter().write(new ObjectMapper().writeValueAsString(body));
                response.setStatus(401);
                response.setContentType("aplication/json");
 
    }

    

    
}
