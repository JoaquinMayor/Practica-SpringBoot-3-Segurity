package com.joaquin.curso.springboot.app.crudjpa.springbootcrudjpa.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.joaquin.curso.springboot.app.crudjpa.springbootcrudjpa.security.filter.JwtAuthenticationFilter;
import com.joaquin.curso.springboot.app.crudjpa.springbootcrudjpa.security.filter.JwtValidationFilter;

@Configuration
@EnableMethodSecurity(prePostEnabled = true) //hace que la determinación de acceso se pueda hacer con anotación al cual le dimos true
public class SpringSecurityConfig {
    @Autowired
    private AuthenticationConfiguration authenticationConfiguration; // Para configurar el filtro de autentificación

    @Bean
    AuthenticationManager authenticationManager() throws Exception{
        return authenticationConfiguration.getAuthenticationManager(); //nos permite obtener el autentication manager de spring security
    }
    @Bean
    PasswordEncoder passwordEncoder(){ //Configuración de spring security
        return new BCryptPasswordEncoder(); //Encriptación de contraseñas
    }
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception{//Para dar o quitar permisos en el tema de seguridad
        return http.authorizeHttpRequests((authz) -> authz
        .requestMatchers(HttpMethod.GET,"/api/users").permitAll()
        .requestMatchers(HttpMethod.POST,"/api/users/register").permitAll()
        .requestMatchers(HttpMethod.POST,"/api/users/users").hasRole("ADMIN") //Este link, solo pueden acceder y trabajar los admin
        .requestMatchers(HttpMethod.POST,"/api/products").hasRole("ADMIN")
        .requestMatchers(HttpMethod.GET,"/api/products","/api/products/{id}").hasAnyRole("ADMIN","USER")
        .requestMatchers(HttpMethod.PUT,"/api/products/{id}").hasRole("ADMIN")
        .requestMatchers(HttpMethod.DELETE,"/api/products/{id}").hasRole("ADMIN")
        .anyRequest().authenticated())//hacemos que a esa direccion sea pública, pero que al resto se le necesite una autentificación
        .addFilter(new JwtAuthenticationFilter(authenticationManager())) //Agregamos el filtro creado
        .addFilter(new JwtValidationFilter(authenticationManager()))
        .csrf(config ->config.disable())//csrf genera un token único para evitar vulnerabilidad, en este caso lo desactivamos porque estamos trabajando con un apirest y no con vistas, como tymeleaf
        .cors(cors-> cors.configurationSource(corsConfigurationSource())) //Le pasamos la configuración desarrollada abajo para que se pueda acceder al front end
        .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))//Por defecto se guarda con estado en la sesion http
        .build(); 
    } 
        
    @Bean
    CorsConfigurationSource corsConfigurationSource(){//Configuración para compartir el backend con el front
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(Arrays.asList("*")); //Acá van las rutas
        config.setAllowedMethods(Arrays.asList("GET","POST","DELETE","PUT")); //Aclaramos en que tipo de métodos los queremos agregar
        config.setAllowedHeaders(Arrays.asList("Authorization","Content-Type"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); //Decimos que métodos queremos que se ejecuten, como es desde la raiz ponemos /** 
        return source;
    }

    @Bean
    FilterRegistrationBean<CorsFilter> corsFilter(){//Método para que siempre se aplique la configuración de arriba
        FilterRegistrationBean<CorsFilter> corsBean = new FilterRegistrationBean<>(new CorsFilter(corsConfigurationSource())); // Le estamos pasando el método realizado arriba

        corsBean.setOrder(Ordered.HIGHEST_PRECEDENCE);//Le añadimos la más alta prioridad

        return corsBean;
    }
}
