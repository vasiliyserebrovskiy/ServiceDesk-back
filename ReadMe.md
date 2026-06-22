# ServiceDesk-back

Backend part of the Service Desk system — a pet project built to practice Spring Security,
REST API design, and ServiceNow integration patterns.

## Tech Stack

- Java 17 (Temurin)
- Spring Boot 3.3.5
- Spring Security + JWT Authentication
- OAuth2 Client
- PostgreSQL
- Spring Data JPA / Hibernate
- Liquibase (database versioning)
- MapStruct (DTO mapping)
- Lombok
- Swagger / OpenAPI (springdoc)
- JUnit 5 + Mockito + MockMvc

---

## Features

### Authentication & Security

- JWT authentication with access and refresh tokens
- Refresh token rotation with persistence
- Stateless authentication via HttpOnly cookies
- Custom JWT authentication filter
- Logout with cookie invalidation
- Role-based access control (ADMIN, MANAGER, USER)
- Custom UserDetails implementation
- Global exception handling with structured error responses

### Domain Model

The system implements a simplified ITSM domain:

**User Management**
- User registration and profile management
- Role assignment (ADMIN, MANAGER, USER)
- Group membership
- Password change (self-service) and admin reset

**Reference Data**
- Categories with ticket type flags (Incident / Problem / Request / Change)
- Subcategories linked to parent categories
- Statuses with ticket type flags
- Configuration Items (CI) — name, type, manufacturer, serial number, model

**Incidents**
- Incident lifecycle management (create, update, view)
- Auto-generated incident number via database sequence (INC0000001 format)
- Priority calculation based on Impact × Urgency matrix (4×4)
- Links to: requester, category, subcategory, status, CI, group, assignee
- ServiceNow integration fields: servicenow_number, servicenow_synced, servicenow_synced_at
- Incidents are never deleted — only closed via status change

### Architecture

- Layered architecture: Controller → Service → Repository
- Interface-based controllers (API contracts with Swagger annotations)
- DTO-based API communication with validation
- MapStruct for entity-to-DTO mapping
- Centralized validation messages via ValidationMessages.properties
- Separation of concerns: auth layer independent from user management

### Database

- PostgreSQL with Liquibase migrations
- UUID primary keys for all entities
- Database sequence for incident numbering
- H2 in-memory database for testing

### Testing

- Unit tests for all services (Mockito)
- Unit tests for DTO validation
- Controller tests with MockMvc
- Spring Security test support
- ~80+ test cases across the project

---

## API Documentation

Swagger UI available after startup:
```
http://localhost:8080/swagger-ui/index.html
```

---

## Environment Variables

The application requires the following environment variables:
```
DB_URL=jdbc:postgresql://localhost:5432/servicedesk
DB_USERNAME=your_username
DB_PASSWORD=your_password
JWT_SECRET=your_secret_key
JWT_AT_LIVE_IN_MIN=15
JWT_RT_LIVE_IN_MIN=10080
```
---

## Running Locally

```bash
# Build and run tests
mvn clean install

# Run the application
mvn spring-boot:run
```

---

## Project Goals

This project was built to:
- Practice Spring Security with JWT and refresh token rotation
- Implement a realistic ITSM domain model
- Prepare integration with ServiceNow REST API
- Build a portfolio project demonstrating full-stack development