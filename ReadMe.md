# ServiceDesk-back

Backend part of the Service Desk project.

## Stack

- Java 17 (Temurin)
- Spring Boot 3.5.13
- Spring Security
- JWT Authentication
- OAuth2 Client
- PostgreSQL (Aiven)
- Spring Data JPA / Hibernate
- Liquibase
- Lombok
- Swagger / OpenAPI
- JUnit 5
- Mockito
- MockMvc

---

## Features

### Authentication & Security

- JWT authentication with access and refresh tokens
- Refresh token rotation
- Stateless authentication
- HttpOnly cookie-based authorization
- Custom JWT authentication filter
- Logout with cookie invalidation
- OAuth2 authentication support
- Custom authentication error handling
- Email validation with custom validator

### Backend Architecture

- Layered architecture:
    - Controller
    - Service
    - Repository
- DTO-based API communication
- Global exception handling
- Reusable validation components
- Refresh token persistence

### Database

- PostgreSQL database
- Database versioning with Liquibase
- JPA / Hibernate entity mapping

### Testing

Project includes:
- Unit tests for services
- Unit tests for validators
- Controller tests using MockMvc
- Security filter tests
- Mockito-based isolation testing
- Deterministic JWT expiration testing using Java Clock

---

## API Documentation

Swagger UI is available after application startup.

Example:
```text
http://localhost:8080/swagger-ui/index.html
```

## Current Status

The project is currently focused on:

backend architecture improvement
authentication/security refinement
test coverage expansion
preparation for further business logic development