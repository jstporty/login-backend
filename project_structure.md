# Project Structure and Architecture

This document serves as the foundational guide for the `login-backend` project.

## Tech Stack
- **Language**: Kotlin 1.9.x
- **Framework**: Spring Boot 3.2.x
- **Build Tool**: Gradle (Kotlin DSL)
- **Database**: MySQL 8.0
- **JDK**: Java 21

## Directory Structure
```
/src/main/kotlin/com/example/loginbackend
├── controller       # REST Controllers (API Endpoints)
│   └── AuthController.kt
├── service          # Business Logic
│   └── AuthService.kt
├── repository       # Data Access Layer (Spring Data JPA)
│   └── UserRepository.kt
├── domain           # JPA Entities (Database Tables)
│   └── User.kt
└── LoginBackendApplication.kt # Main Entry Point
```

## Configuration
- **Application Properties**: `src/main/resources/application.yml`
    - Contains database connection settings (URL, Username, Password).
    - configured for MySQL 8 dialect.

## Development Guide
1. **Prerequisites**:
   - Java 21 SDK installed.
   - MySQL 8 server running.
   - Create a database named `your_db_name` (or update `application.yml`).

2. **Running the App**:
   ```bash
   ./gradlew bootRun
   ```

## Architecture
- **Layered Architecture**: Controller -> Service -> Repository -> Database.
- **MCP Integration**: Designed to be extensible for Model Context Protocol integration.
