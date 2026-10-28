# Course Registration System

Backend service for course registration: students browse courses, enroll in or drop class sections, and view their schedule and transcript; the System Admin manages courses, class sections, semesters and enrollment periods.

## Table of Contents

- [Tech Stack](#tech-stack)
- [Getting Started](#getting-started)
- [Deploy](#deploy)
- [Documentation](#documentation)

## Tech Stack

Java 21 · Spring Boot · Spring Security · SQLite (`DaoSqlite`) · Maven · OpenAPI/Swagger · Docker Compose · nginx

## Getting Started

Requires Java 21 (Maven is provided via `./mvnw`).

```bash
./mvnw spring-boot:run          # run locally
./mvnw clean package            # build the jar into target/
```

Swagger UI: `http://localhost:8080/swagger-ui/index.html`

## Deploy

```bash
mkdir -p data                   # create before compose, should do this if you gonna use SQLite, and modify the db for future use
docker compose up -d --build    # app behind nginx at http://localhost
```
Deployment, backup and operations: [DEPLOYMENT.md](.github/DEPLOYMENT.md).

## Documentation

| Document | Description |
|----------|-------------|
| [Problem Statement](docs/requirements/problem_statement.md) | Problem statement |
| [Architecture](docs/specs/architecture.md) | Project structure and dependency rules |
| [API Specification](docs/specs/api-specs.md) | API contract |
| [DB Schema](docs/specs/db_schema.md) | Database design |
| [Sprints](docs/sprints/) | Sprint plans |
| [Deployment](.github/DEPLOYMENT.md) | Docker Compose deployment |
