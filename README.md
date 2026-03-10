# Pension Benefit Microservice Platform

A production-style backend project simulating a simplified government pension system. Built for CV demonstration as a Java backend developer.

## Tech Stack

- **Java 21**
- **Spring Boot 3.2**
- **Maven**
- **PostgreSQL**
- **Spring Data JPA**
- **Spring Web** (REST APIs)
- **Spring Validation**
- **Spring Security** (Basic Auth)
- **ActiveMQ** (JMS messaging)
- **Flyway** (database migrations)
- **MapStruct** (DTO mapping)
- **OpenAPI/Swagger** (API documentation)
- **JUnit 5** + **Testcontainers**

## Architecture

Clean layered architecture with domain-driven design:

```
com.shotaroi.pensionservice
├── citizen/       # Citizen registration
├── contribution/ # Pension contributions per year
├── pension/      # Pension calculation
├── payment/      # Monthly pension payouts
├── domain/       # Shared entities
├── config/       # Security, JMS, etc.
├── exception/    # Global exception handling
└── messaging/    # JMS events
```

## Services

| Service | Endpoints | Description |
|---------|-----------|-------------|
| **Citizen** | `POST /citizens`, `GET /citizens/{id}`, `GET /citizens`, `GET /citizens/{id}/contributions` | Register and manage citizens |
| **Contribution** | `POST /contributions`, `GET /contributions/citizens/{citizenId}` | Store pension contributions (18.5% of salary) |
| **Pension Calculation** | `POST /pension/calculate` | Calculate estimated monthly pension |
| **Payment** | `POST /payments/generate`, `GET /payments/{citizenId}` | Handle pension payouts |

## Quick Start

### Prerequisites

- **Java 21** (recommended; Java 24 may cause Mockito/ByteBuddy compatibility issues)
- Maven 3.9+
- Docker & Docker Compose (for PostgreSQL and ActiveMQ)

### Run Infrastructure

```bash
docker-compose up -d
```

### Run Application

```bash
mvn spring-boot:run
```

### API Documentation

- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/api-docs

### Authentication

- Username: `pension_admin`
- Password: `admin123`

Example with curl:

```bash
curl -u pension_admin:admin123 http://localhost:8080/citizens
```

## Example Flow

1. **Register a citizen**

```bash
curl -u pension_admin:admin123 -X POST http://localhost:8080/citizens \
  -H "Content-Type: application/json" \
  -d '{
    "personalNumber": "19700101-1234",
    "firstName": "Anna",
    "lastName": "Svensson",
    "birthDate": "1970-01-01"
  }'
```

2. **Add contributions**

```bash
curl -u pension_admin:admin123 -X POST http://localhost:8080/contributions \
  -H "Content-Type: application/json" \
  -d '{
    "citizenId": 1,
    "year": 2023,
    "salary": 500000
  }'
```

3. **Calculate pension** (triggers JMS event → Payment Service generates first payment)

```bash
curl -u pension_admin:admin123 -X POST http://localhost:8080/pension/calculate \
  -H "Content-Type: application/json" \
  -d '{
    "citizenId": 1,
    "retirementAge": 65
  }'
```

## Async Messaging (JMS)

When a pension calculation completes, a `PensionCalculatedEvent` is published to the `pension.calculated` queue. The Payment Service listens and automatically generates the first pension payment.

## Testing

```bash
mvn test
```

- **Unit tests**: CitizenService, ContributionService
- **Controller tests**: CitizenController (MockMvc)
- **Integration test**: Full pension calculation flow with Testcontainers

## Project Structure

```
src/main/java/com/shotaroi/pensionservice/
├── PensionServiceApplication.java
├── citizen/
│   ├── controller/
│   ├── service/
│   ├── repository/
│   ├── domain/ (uses shared domain)
│   ├── dto/
│   └── mapper/
├── contribution/
├── pension/
├── payment/
│   └── listener/  # JMS listener for PensionCalculatedEvent
├── domain/
├── config/
├── exception/
└── messaging/
```

## License

MIT
