# Manejo de Spring Security 

### En el siguiente código encontraremos configuración, gestión, creación y manejo de Spring Segurity con sistema de Token y niveles de acceso mediante los roles de usuarios, brindando asi permisos y accesos a los diferentes controladores.

#### *Todos los archivos cuentan con sus respectivos comentarios para facilitar la comprensión *

#### Dentro de la carpeta *filter * que se encuentra en la carpeta *security* encontraremos todos los archivos relacionados con la gestion y creación del token de seguridad.

#### Dentro del archivo SpringSecurityConfig dentro de la carpeta *security* podremos ingresar al codigo de gestión de permisos de usuarios y configuración de accesos para el frontEnd.

#### Dentro de la carpeta *Validation* encontraremos distintos archivos para poder hacer nuestras validaciones personalizadas mediante anotaciones dentro de nuestros @Entity.

<p>
Todas las Evaluaciones se hacen con PostMan
</p>

<p>
La aplicación cuenta con 3 @Entity (Product, Role y User), cuyas vistas de los productos y gestion vamn a estar determinados por el tipo de acceso y Rol del User.
</p>

<p align="center">
<img src="https://blog.knoldus.com/wp-content/uploads/2021/12/image-23.png" alt="Spring Security">
</p>

---

## Objetivo

Proyecto de práctica del curso de Spring Boot 3 que parte del CRUD de productos con JPA y validaciones, y lo protege con **Spring Security + JWT**. Los usuarios se registran, inician sesión en `/login` y reciben un token (`Bearer`) que luego se envía en cada petición; según los **roles** del usuario (`ROLE_USER` / `ROLE_ADMIN`) se permite o se deniega el acceso a cada endpoint. Además incluye configuración de **CORS** para consumir la API desde un front-end (Angular en `http://localhost:4200`).

## Tecnologías

| Tecnología | Versión |
|---|---|
| Java | 21 |
| Spring Boot (starter parent) | 3.2.1 |
| Spring Web | gestionada por Spring Boot |
| Spring Data JPA (Hibernate) | gestionada por Spring Boot |
| Spring Security | gestionada por Spring Boot |
| Spring Boot Validation (Jakarta Bean Validation) | gestionada por Spring Boot |
| JJWT (`jjwt-api`, `jjwt-impl`, `jjwt-jackson`) | 0.12.3 |
| MySQL (`mysql-connector-j`) | gestionada por Spring Boot |
| Spring Boot DevTools | gestionada por Spring Boot |
| Maven (Maven Wrapper incluido) | - |

## Estructura del proyecto

```
src/main/java/com/joaquin/curso/springboot/app/crudjpa/springbootcrudjpa
├── SpringbootCrudJpaApplication.java        # Clase principal
├── AppConfig.java                           # Carga messages.properties con @PropertySource
├── controllers
│   ├── ProductController.java               # CRUD /api/products
│   └── UserController.java                  # Listado, creación y registro en /api/users
├── entities
│   ├── Product.java                         # Tabla "products" con validaciones
│   ├── User.java                            # Tabla "users" (ManyToMany con roles mediante "users_roles")
│   └── Role.java                            # Tabla "roles"
├── repository
│   ├── IProductRepository.java              # CrudRepository<Product, Long> + existsBySku
│   ├── IUserRepository.java                 # existsByUsername / findByUsername
│   ├── IRoleRepository.java                 # findByName
│   └── ICosasRespository.java               # Repositorio adicional de Product (sin métodos propios)
├── security
│   ├── SpringSecurityConfig.java            # SecurityFilterChain, reglas por rol, BCrypt, CORS
│   ├── TokenJwtConfig.java                  # SECRET_KEY, prefijo "Bearer " y header "Authorization"
│   ├── SimpleGrantedAuthoritiesJsonCreator.java # Mixin de Jackson para reconstruir los roles del token
│   └── filter
│       ├── JwtAuthenticationFilter.java     # Login: valida credenciales y genera el JWT
│       └── JwtValidationFilter.java         # Valida el JWT en cada request y autentica al usuario
├── services
│   ├── IProductService.java / ProductServiceImpl.java  # Lógica de productos (@Transactional)
│   ├── IUserService.java / UserServiceImpl.java        # Alta de usuarios: asigna roles y encripta password
│   └── JpaUserDetailsService.java           # UserDetailsService: carga el usuario y sus roles desde la BD
└── Validation
    ├── IsRequired.java / RequiredValidation.java           # Campo obligatorio
    ├── IsExistDb.java / IsExistDBValidation.java           # SKU no repetido en la BD
    ├── ExistByUsername.java / ExistByUsernameValidation.java # Username no repetido en la BD
    └── ProductValidation.java               # Validador programático (Validator de Spring)
```

### Entidades

- **Product** (`products`): `id`, `sku` (`@IsExistDb`, `@IsRequired`), `name` (`@NotEmpty`, `@Size(3,20)`), `price` (`@Min(500)`, `@NotNull`), `description` (`@IsRequired`).
- **User** (`users`): `id`, `username` (único, `@ExistByUsername`, `@NotBlank`, `@Size(4,12)`), `password` (`@NotBlank`, solo escritura en JSON), `roles` (`@ManyToMany` con tabla intermedia `users_roles`), `enabled` (se pone en `true` con `@PrePersist`) y `admin` (`@Transient`, bandera para asignar el rol de admin).
- **Role** (`roles`): `id`, `name` (único) y la relación inversa `users`.

## Conceptos aplicados

- **`SecurityFilterChain`** con `authorizeHttpRequests` y `requestMatchers` para definir rutas públicas y rutas restringidas por rol (`hasRole`, `hasAnyRole`), y `anyRequest().authenticated()` para el resto.
- **`@EnableMethodSecurity(prePostEnabled = true)`** para habilitar la seguridad por anotaciones.
- **Autenticación con JWT**:
  - `JwtAuthenticationFilter` (extiende `UsernamePasswordAuthenticationFilter`): lee el JSON con `username` y `password`, autentica con el `AuthenticationManager` y en `successfulAuthentication` genera el token con los roles como *claim* `authorities`, fecha de emisión y expiración de **1 hora**, firmado con `SECRET_KEY` (HS256). En `unsuccessfulAuthentication` responde 401.
  - `JwtValidationFilter` (extiende `BasicAuthenticationFilter`): toma el header `Authorization`, quita el prefijo `Bearer `, verifica la firma, reconstruye las authorities y registra la autenticación en el `SecurityContextHolder`; si el token es inválido responde 401.
- **`UserDetailsService`** propio (`JpaUserDetailsService`) que convierte el usuario de la BD y sus roles en un `UserDetails` de Spring Security.
- **Encriptación de contraseñas** con `BCryptPasswordEncoder`.
- **Sesión `STATELESS`** y **CSRF deshabilitado** (se trabaja con una API REST y no con vistas tipo Thymeleaf).
- **CORS**: `CorsConfigurationSource` (orígenes, métodos GET/POST/PUT/DELETE, headers `Authorization` y `Content-Type`) registrado con un `FilterRegistrationBean<CorsFilter>` de máxima prioridad, y `@CrossOrigin(origins = "http://localhost:4200")` en los controladores.
- **Relación `@ManyToMany`** entre usuarios y roles con `@JoinTable` y restricción única, usando `@JsonIgnoreProperties` para evitar ciclos infinitos al serializar.
- **`@JsonProperty(access = WRITE_ONLY)`** para que el password no se devuelva en las respuestas y **`@Transient`** para atributos que no se persisten.
- **Validaciones** con Bean Validation, mensajes en `messages.properties`, validador programático y anotaciones personalizadas (incluida `@ExistByUsername`, que consulta la BD).

## Endpoints

| Método | Ruta | Acceso | Descripción |
|---|---|---|---|
| POST | `/login` | Público | Inicia sesión con `{"username", "password"}`; retorna el token, el username y un mensaje (y el header `Authorization: Bearer <token>`). |
| GET | `/api/users` | Público | Lista los usuarios. |
| POST | `/api/users/register` | Público | Registra un usuario con rol `ROLE_USER` (fuerza `admin = false`). |
| POST | `/api/users` | Autenticado | Crea un usuario; si se envía `"admin": true` también se le asigna `ROLE_ADMIN`. |
| GET | `/api/products` | ADMIN o USER | Lista todos los productos. |
| GET | `/api/products/{id}` | ADMIN o USER | Obtiene un producto por id (404 si no existe). |
| POST | `/api/products` | ADMIN | Crea un producto validado (201 / 400). |
| PUT | `/api/products/{id}` | ADMIN | Actualiza un producto (201 / 400 / 404). |
| DELETE | `/api/products/{id}` | ADMIN | Elimina un producto (200 / 404). |

> En `SpringSecurityConfig` también existe la regla `POST /api/users/users` restringida a ADMIN, pero el controlador no expone esa ruta; la creación de usuarios en `POST /api/users` queda cubierta por `anyRequest().authenticated()`.

Para las rutas protegidas se debe enviar el header:

```
Authorization: Bearer <token>
```

Ejemplo de flujo con Postman:

```
POST http://localhost:8080/api/users/register   {"username": "pepe", "password": "12345"}
POST http://localhost:8080/login                {"username": "pepe", "password": "12345"}
GET  http://localhost:8080/api/products         (header Authorization: Bearer <token>)
```

## Configuración

En `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/db_jpa_crud
spring.datasource.username=root
spring.datasource.password=<tu_password>
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
spring.jpa.show-sql=true
```

- Se necesita un servidor **MySQL** en `localhost:3306` con la base de datos `db_jpa_crud`.
- No está configurado `spring.jpa.hibernate.ddl-auto`, por lo que las tablas `products`, `users`, `roles` y `users_roles` deben existir previamente.
- La tabla `roles` debe contener los roles `ROLE_USER` y `ROLE_ADMIN`, ya que `UserServiceImpl` los busca por nombre al crear usuarios.
- La clave del JWT (`TokenJwtConfig.SECRET_KEY`) se genera en memoria al iniciar la aplicación, por lo que los tokens dejan de ser válidos al reiniciarla.

## Cómo ejecutarlo

```bash
# Linux / macOS
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

La API queda disponible en `http://localhost:8080`.
