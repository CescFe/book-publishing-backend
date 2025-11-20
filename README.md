# Book Publishing Backend

[![GitHub release](https://img.shields.io/github/v/release/CescFe/book-publishing-backend?color=blue)](https://github.com/CescFe/book-publishing-backend/releases/latest)
[![GitHub license](https://img.shields.io/github/license/CescFe/book-publishing-backend?color=blue)](https://github.com/CescFe/book-publishing-backend/blob/main/LICENSE)

Backend service for the Book Publishing platform. Implements the [book-publishing-api-spec](https://github.com/CescFe/book-publishing-api-spec) RESTful API to manage books, authors, and collections.

## 📌 About the Project

### 🔎 Tech Stack

- Kotlin 2.1
- Spring Boot 3.5
- Spring Web / Validation
- Spring Data JPA
- PostgreSQL
- Testcontainers
- Liquibase
- Gradle
- GitHub Actions
- Docker

### 🏗️ Architecture

This project follows **Hexagonal Architecture** (Ports and Adapters) with **Vertical Slice Architecture**:

- **Domain**: Business entities and rules
- **Application**: Use cases and application services
- **Infrastructure**: External adapters (controllers, repositories, external APIs)
- **Shared**: Cross-cutting concerns and common infrastructure

### 🧱 Structure
```
src/main/kotlin/org/cescfe/bookpublishing/
├── context/
│ └── application/
│   └── port/input/
│     └── interactor/
│       └── UseCaseImpl.kt
│     └── mapper/
│       └── UseCaseMapper.kt
│     └── UseCaseInterface.kt
│ └── domain/
│   └── model/
│     └── ValueObject.kt
│   └── port/
│     └── VORepositoryView.kt
│ └── infrastructure/
│   └── adapters/
│     └── input/rest/
│       └── mapper/
│       └── Controller.kt
│     └── output/persistence/
│       └── entity/
│         └── Entity.kt
│       └── mapper/
│         └── PersistenceMapper.kt
│       └── repository/
│         └── JpaEntityRepositorInterface.kt
│         └── JpaRepository.kt
└── BookPublishingApplication.kt
```

### 🧪 Testing Policy

The project follows a testing strategy aligned with Hexagonal Architecture:
- *Unit Tests*: Validate mappers, domain logic and application services in isolation, without Spring or infrastructure.
- *Integration Tests*: Use Testcontainers + PostgreSQL to validate persistence adapters, entity mappings, auditing fields, JSONB handling, and repository behaviour.
- *Dataset-based Tests*: Certain integration tests load SQL datasets (/datasets/*.sql) to verify JPA mapping and audit fields against realistic database records.
- *What is intentionally not tested*: JPA built-ins (count, existsById, deleteById) unless wrapped with custom logic
The goal is to ensure correctness without duplicating framework tests or introducing unnecessary maintenance burden.

## 🚀 Getting Started

### ✏️ Prerequisites

- Java 21 or higher
- Docker and Docker Compose

### 🚀 Running the Application

#### Local env
1. Start PostgreSQL
    >docker-compose up -d
2. Run the application
    >./gradlew bootRun --args='--spring.profiles.active=local'

#### Clean (saving data)
>docker-compose down

#### Clean ALL (data included)
>docker-compose down -v

The application will start on `http://localhost:8080`

### Available Endpoints
- `GET /api/v1/health` - Health check endpoint

#### Authentication
- `POST /api/v1/auth/login` - Authenticate user and get access token

#### Authors
- `GET /api/v1/authors` - Get all authors (paginated)
- `POST /api/v1/authors` - Create a new author
- `GET /api/v1/authors/{id}` - Get author by ID
- `PUT /api/v1/authors/{id}` - Update author
- `DELETE /api/v1/authors/{id}` - Delete author

### Database

- **Local Development**: PostgreSQL (via Docker)

### Environment Profiles

- `local` → Development with local PostgreSQL
- `test` → Testcontainers (auto-enabled)
- `development` → GitHub CI/CD workflow

### Code Quality

```bash
# Check code style
./gradlew ktlintCheck

# Fix code style issues
./gradlew ktlintFormat

# Check all formatting
./gradlew spotlessCheck

# Apply formatting
./gradlew spotlessApply

# Run all quality checks
./gradlew check
```

## Testing

```bash
# Run all tests
./gradlew test

# Run specific test
./gradlew test --tests "HealthControllerTest"

# Run tests with coverage
./gradlew test jacocoTestReport
```

## 🕹️ Building

```bash
# Build JAR
./gradlew build

# Build Docker image
docker build -t book-publishing-backend .

# Build with Jib (Google Container Tools)
./gradlew jib
```

## API Documentation

- **Swagger UI**: `http://localhost:8080/swagger-ui.html` (when available)
- **OpenAPI Spec**: `http://localhost:8080/v3/api-docs`

## License

This project is licensed under the MIT License (see the [LICENSE](LICENSE) file for details).
