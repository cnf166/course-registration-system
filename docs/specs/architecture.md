# Project Structure 

The backend is split into three layers: **API**, **Business**, **Data**. Business sits in the middle and knows nothing about the framework or the database; API and Data both depend on it.

```text
API ──→ Business ←── Data
```

A request passes through the layers as follows:

```text
HTTP → Controller → Service → Repository (interface) → Adapter → Spring Data/JPA → PostgreSQL
```

# System Documentation

### Below is the system design:

![Course Registration System Architecture](/docs/highlevel_sd.png)

*Figure 1: Proposed high-level system design for the course registration system.*

### And below is the architecture the team chose for the system:

![Course Registration System Architecture](/docs/layered_architecture.png)

*Figure 2: System architecture diagram.*

## Directory Structure

```text
src/main/java/com/course_registration_system/
├── SoftwareArchitectApplication.java
├── api/
│   ├── controller/
│   ├── dto/{request,response}/
│   ├── mapper/          # DTO <-> Business Model
│   └── error/           # GlobalExceptionHandler, ErrorResponse
├── business/
│   ├── auth/            # AuthService, PasswordHasher, TokenService
│   ├── user/
│   ├── course/
│   ├── courseclass/
│   ├── enrollment/
│   ├── semester/        # includes EnrollmentPeriod
│   └── exception/
├── data/
│   ├── entity/          # JPA entity
│   ├── repository/      # Spring Data repo + adapter
│   └── mapper/          # Business Model <-> Entity
├── security/            # JWT, BCrypt, SecurityConfig
└── config/              # BeanConfig, OpenApiConfig, DataSeeder

src/main/resources/
├── application.properties
└── db/migration/
```

Each subpackage in `business/` has the same trio: model, service, and repository interface (e.g. `course/` has `Course`, `CourseService`, `CourseRepository`).
