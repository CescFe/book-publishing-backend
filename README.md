# Book Publishing Backend

[![GitHub release](https://img.shields.io/github/v/release/CescFe/book-publishing-backend?color=blue)](https://github.com/CescFe/book-publishing-backend/releases/latest)
[![GitHub license](https://img.shields.io/github/license/CescFe/book-publishing-backend?color=blue)](https://github.com/CescFe/book-publishing-backend/blob/main/LICENSE)

Backend service for the Book Publishing platform.

---

## 📑 Table of Contents

- [Overview](#-overview)
- [Tech Stack](#-tech-stack)
- [Architecture](#-architecture)
- [Getting Started](#-getting-started)
- [Environment Profiles](#environment-profiles)
- [Testing Policy](#-testing-policy)
- [Code Quality](#code-quality)
- [CI/CD Workflow](#-cicd-workflow)
- [API Documentation](#api-documentation)

---

## 📌 Overview

This repository implements the [book-publishing-api-spec](https://github.com/CescFe/book-publishing-api-spec) RESTful API to manage books, authors, and collections.

## 🔎 Tech Stack

| Category   | Technologies                                                  |
|------------|---------------------------------------------------------------|
| Language   | Kotlin 2.1                                                    |
| Framework  | Spring Boot 3.5, Spring Web, Spring Data JPA, Spring Security |
| Database   | PostgreSQL                                                    |
| Migrations | Liquibase                                                     |
| Testing    | JUnit 5, Testcontainers, MockMvc                              |
| Build      | Gradle                                                        |
| CI/CD      | GitHub Actions                                                |
| Container  | Docker                                                        |

## 🏗️ Architecture

This project follows **Hexagonal Architecture** (Ports and Adapters), **Domain-Driven Design** with **Vertical Slice Architecture**:

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

## 🚀 Getting Started

### ✏️ Prerequisites

- Java 21 or higher
- Docker and Docker Compose
- 💡 In local environment, is required a `.env` file to set the `Token PAT` credentials

### 🏃 Running the Application

#### 1. Start infrastructure (PostgreSQL + Liquibase)
>docker-compose up -d
#### 2. Run the application
>./gradlew bootRun --args='--spring.profiles.active=local'

#### Stop services (preserving data)
>docker-compose down
#### Stop services and delete all data
>docker-compose down -v

### API Endpoints
- `GET /api/v1/health` - Health check endpoint

#### Authentication
- `POST /api/v1/auth/login` - Authenticate user and get access token

#### Authors
- `GET /api/v1/authors` - Get all authors (paginated)
- `POST /api/v1/authors` - Create a new author
- `GET /api/v1/authors/{id}` - Get author by ID
- `PUT /api/v1/authors/{id}` - Update author
- `DELETE /api/v1/authors/{id}` - Delete author

#### Collections
- `GET /api/v1/collections` - Get all collections (paginated)
- `POST /api/v1/collections` - Create a new collection
- `GET /api/v1/collections/{id}` - Get collection by ID
- `DELETE /api/v1/collections/{id}` - Delete collection

#### Books
- `GET /api/v1/books` - Get all books (paginated)
- `POST /api/v1/books` - Create a new book
- `GET /api/v1/books/{id}` - Get book by ID
- `DELETE /api/v1/books/{id}` - Delete book

## Environment Profiles

- `local` → Development with local PostgreSQL
- `test` → Testcontainers (auto-enabled)
- `development` → GitHub CI/CD workflow
- `pro` → Connected to real Database

## 🧪 Testing Policy

The project follows a testing strategy aligned with Hexagonal Architecture:
- *Unit Tests*: Validate mappers, domain logic and application services in isolation, without Spring or infrastructure.
- *Integration Tests*: Use Testcontainers + PostgreSQL to validate persistence adapters, entity mappings, auditing fields, JSONB handling, and repository behaviour.
- *Dataset-based Tests*: Certain integration tests load SQL datasets (/datasets/*.sql) to verify JPA mapping and audit fields against realistic database records.
- *What is intentionally not tested*: JPA built-ins do not wrapped with custom logic
  The goal is to ensure correctness without duplicating framework tests or introducing unnecessary maintenance burden.

## Code Quality

```bash
# Check formatting
./gradlew spotlessCheck

# Apply formatting
./gradlew spotlessApply
```

## 🕹️ CI/CD Workflow

### Automatic Validation

Every pull request automatically runs:
- ✅ **Pull Request Validation** - Spotless check + all Gradle tests (unit + integration)
- ✅ **Integration Validation** (PRs in `Ready for Review`) - Builds JAR, starts Docker services, runs Newman/Postman E2E tests

### Release Process

The release process is partially automated:
- ✅ **Deploy Backend** - Render automatically deploys pro using Dockerfile when there is a Push to `main`
- ✅ **Migrate Database** - Run manually **Deploy Liquibase to pro** GitHub Action

### Create Tag and Release

1. Go to **Actions** → **Create Release Tag**
2. Run manually with the desired version (e.g., `v0.1.0`)
3. This creates a Git tag
4. Go to **Tags** → Click on **Release**

## API Documentation

- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI Spec**: `http://localhost:8080/v3/api-docs`
