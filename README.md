# Backend - Inventario Online (Spring Boot)

Este módulo contiene la lógica de negocio, seguridad, controladores REST y acceso a datos del sistema de gestión de inventario online. Es parte del Trabajo de Fin de Grado (TFG) de Ingeniería Informática.

## 🚀 Tecnologías

Java 17

Spring Boot 3

PostgreSQL

JWT (JSON Web Token)

Swagger / OpenAPI

Maven

## 📁 Estructura del Proyecto

```
code/back/
├── src/
│   ├── main/java/com/tfg/
│   │   ├── config/                # Configuraciones (Swagger, Seeder)
│   │   ├── controller/            # Controladores REST por entidad
│   │   ├── dto/                   # DTOs para comunicación segura
│   │   ├── entity/                # Entidades JPA (modelo de datos)
│   │   ├── exception/             # Excepciones personalizadas y global handler
│   │   ├── mapper/                # Conversores entre entidades y DTOs
│   │   ├── repository/            # Interfaces JPA
│   │   ├── security/              # Autenticación JWT y configuración de seguridad
│   │   └── service/               # Servicios de negocio e implementaciones
│   └── resources/
│       └── application.properties # Configuración de base de datos y puertos entre otros
├── test/                          # Tests unitarios y de integración
├── pom.xml                        # Dependencias Maven
```

## 🔐 Seguridad

El sistema implementa autenticación basada en tokens JWT. Los usuarios se autentican a través del endpoint /auth/login y reciben un token que deben incluir en el header Authorization: Bearer <token> para acceder al resto de la API.

Configuración en [SecurityConfig.java](/code/back/src/main/java/com/tfg/security/config/SecurityConfig.java)

Filtros y utilidades JWT en [security.jwt.*](/code/back/src/main/java/com/tfg/security/jwt)

Gestión de usuarios con CustomUserDetailsService

## 📦 Endpoints y Documentación

Todos los endpoints están documentados automáticamente con Swagger. Una vez levantada la aplicación, accede a:

http://localhost:8080/swagger-ui/index.html

## 🛠️ Scripts y Seeders

Si no existen datos en la BBDD se ejecuta un seeder que la puebla completamente.

## ▶️ Cómo ejecutar

Asegúrate de tener PostgreSQL activo y configurado correctamente en application.properties.

### Desde la raíz del backend
```bash
./mvnw spring-boot:run
```
### Para generar un JAR ejecutable:

```bash
./mvnw clean package
java -jar target/inventario-back-0.0.1-SNAPSHOT.jar
```
## 🧾 Notas adicionales

Todas las entidades están desacopladas del frontend gracias a los DTOs.

La base de datos se estructura con claves foráneas, ENUMs y triggers ([ver script SQL](/docs/scripts/Script_Inicial.sql)).

El código está organizado en capas (Controller → Service → Repository).

## 👨‍💻 Autor

Eduardo García Romera egarcia3266@alumno.uned.es

Este backend forma parte del sistema completo de gestión de inventario, cuya interfaz de usuario se encuentra en el módulo [front](/code/front/)

## 📋 Plantilla application.properties

Por seguridad evito publicar en este repositorio el application.properties pero dejo una plantilla a completar que se tiene que pegar en la carpeta [resources](/code/back/src/main/resources)


```properties
# PostgreSQL Config
spring.datasource.url=jdbc:postgresql://localhost:5433/gestion_inventario
spring.datasource.username= #tu usuario de BBDD
spring.datasource.password= #tu contraseña de BBDD

# Hibernate
spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect

# Puerto de la aplicacion
server.port=8080

# JWT Config
app.jwtSecret= #introducir secret propio
app.jwtExpirationMs= #introducir tiempo de expiración 24h = 86400000ms

# Open AI key
OPENAI_API_KEY= #añadir tu propia key

#Configuración seeder
tema_inventario=ropa y accesorios de moda
num_categorias=5
```
