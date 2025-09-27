# Book Publishing Backend

Backend service for the Book Publishing platform built with Spring Boot 3.5.6, Kotlin, and Gradle.

## Prerequisites

- Java 21 or higher
- Gradle 9.1 (included via wrapper)

## Getting Started

### Running the Application

```bash
# Build the project
./gradlew build

# Run the application
./gradlew bootRun
```

The application will start on `http://localhost:8080`

### Available Endpoints

- `GET /api/v1/health` - Health check endpoint
- `GET /h2-console` - H2 Database Console (development only)

### Running with Docker

```bash
# Build and run with Docker Compose
docker-compose up --build
```

## Project Structure
```
src/main/kotlin/org/cescfe/bookpublishing/
├── shared/
│ └── infrastructure/
│ └── adapters/
│ └── input/
│ └── rest/
│ └── HealthController.kt
└── BookPublishingApplication.kt
```

## Architecture

This project follows **Hexagonal Architecture** (Ports and Adapters) with **Vertical Slice Architecture**:

- **Domain**: Business entities and rules
- **Application**: Use cases and application services
- **Infrastructure**: External adapters (controllers, repositories, external APIs)
- **Shared**: Cross-cutting concerns and common infrastructure

## Dependencies

- Spring Boot 3.5.6
- Spring Data JPA
- Spring Web
- Spring Validation
- Kotlin 2.1.21
- Jackson Kotlin Module
- H2 Database (development)
- PostgreSQL (production)
- book-publishing-api-spec (custom library)

## Development

### Database

- **Development**: H2 in-memory database
- **Production**: PostgreSQL

### Environment Profiles

- `default` - Development with H2
- `docker` - Production with PostgreSQL

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

## Building

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

## Contributing

1. Follow the code style guidelines (ktlint + spotless)
2. Write tests for new features
3. Update documentation as needed
4. Ensure all tests pass before submitting PR

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
