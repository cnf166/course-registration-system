# Project Structure — Pha 1

## 1. Tổng quan

Project sử dụng kiến trúc phân tầng:

```text
API
 ↓
Business
 ↑
Data Access
```

Runtime flow:

```text
HTTP Request
    ↓
API / Controller
    ↓
Business Service
    ↓
Repository Port
    ↓
Repository Adapter
    ↓
Spring Data / JPA
    ↓
PostgreSQL
```

Business Layer là phần độc lập với framework và database.

---

## 2. Cấu trúc project

```text
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── course_registration_system/
│   │           ├── SoftwareArchitectApplication.java
│   │           │
│   │           ├── api/
│   │           │   ├── controller/
│   │           │   ├── dto/
│   │           │   │   ├── request/
│   │           │   │   └── response/
│   │           │   ├── mapper/
│   │           │   └── error/
│   │           │
│   │           ├── business/
│   │           │   ├── exception/
│   │           │   ├── auth/
│   │           │   ├── user/
│   │           │   ├── course/
│   │           │   ├── courseclass/
│   │           │   ├── enrollment/
│   │           │   └── semester/
│   │           │
│   │           ├── data/
│   │           │   ├── entity/
│   │           │   ├── repository/
│   │           │   └── mapper/
│   │           │
│   │           ├── security/
│   │           │
│   │           └── config/
│   │
│   └── resources/
│       ├── application.properties
│       └── db/
│           └── migration/
│
└── test/
    └── java/
        └── com/
            └── course_registration_system/
```

---

## 3. API Layer

Package:

```text
api/
├── controller/
├── dto/
├── mapper/
└── error/
```

Chịu trách nhiệm:

- HTTP request/response.
- REST API.
- Request validation.
- DTO.
- HTTP status.
- Chuyển đổi DTO ↔ Business Model.
- Xử lý exception ở mức HTTP.

### `controller/`

- Nhận HTTP request.
- Gọi Business Service.
- Trả HTTP response.
- Không chứa business rules.
- Không truy cập trực tiếp database.

### `dto/`

- `request/`: dữ liệu từ client.
- `response/`: dữ liệu trả về client.
- Không sử dụng DTO làm Business Model.

### `mapper/`

- Chuyển đổi giữa API DTO và Business Model.

### `error/`

- `GlobalExceptionHandler`.
- `ErrorResponse`.
- Chuyển Business Exception thành HTTP response.

---

## 4. Business Layer

Package:

```text
business/
├── exception/
├── auth/
├── user/
├── course/
├── courseclass/
├── enrollment/
└── semester/
```

Chịu trách nhiệm:

- Business Model.
- Business Service.
- Business Rules.
- Repository Interfaces / Ports.
- Business Exceptions.

### Dependency rule

Business không phụ thuộc vào:

```text
Spring Web
Spring Data
JPA
Hibernate
PostgreSQL
HTTP
JSON
API DTO
JPA Entity
```

Business Layer phải giữ ở dạng Plain Java.

### `auth/`

- `AuthService`
- `PasswordHasher`
- `TokenService`

Chứa logic authentication và các abstraction liên quan.

### `user/`

- `UserEntity`
- `UserService`
- `JpaUserRepository`

### `course/`

- `Course`
- `CourseService`
- `CourseRepository`

### `courseclass/`

- `CourseClass`
- `CourseClassService`
- `CourseClassRepository`

### `enrollment/`

- `Enrollment`
- `EnrollmentService`
- `EnrollmentRepository`

Chứa các business rules liên quan đến đăng ký học phần.

### `semester/`

- `Semester`
- `EnrollmentPeriod`
- `SemesterService`
- `SemesterRepository`
- `EnrollmentPeriodRepository`

### `exception/`

Chứa các exception thuộc business, không phụ thuộc HTTP.

---

## 5. Data Access Layer

Package:

```text
data/
├── entity/
├── repository/
└── mapper/
```

Chịu trách nhiệm:

- Database access.
- JPA.
- Hibernate.
- Spring Data.
- PostgreSQL.
- Mapping Business Model ↔ Entity.

### `entity/`

Chứa các JPA Entity:

```text
UserEntity
CourseEntity
CourseClassEntity
EnrollmentEntity
SemesterEntity
EnrollmentPeriodEntity
```

Đây là database representation, không dùng trực tiếp trong Business Layer.

### `repository/`

Gồm:

- Spring Data repositories.
- Repository adapters.

Quan hệ:

```text
Business Repository Interface
        ↑
        │ implements
Repository Adapter
        ↓
Spring Data Repository
        ↓
JPA Entity
```

### `mapper/`

Chuyển đổi:

```text
Business Model ↔ JPA Entity
```

Không xử lý API DTO.

---

## 6. Security

Package:

```text
security/
├── JwtAuthenticationFilter.java
├── JwtTokenService.java
├── BcryptPasswordHasher.java
└── SecurityConfig.java
```

Chịu trách nhiệm:

- JWT authentication.
- Password hashing.
- Authentication filter.
- Authorization.
- Security configuration.

Authentication flow:

```text
HTTP Request
    ↓
JwtAuthenticationFilter
    ↓
SecurityContext
    ↓
Controller
```

Security implementation thực hiện các interface được định nghĩa trong Business Layer:

```text
TokenService
PasswordHasher
```

---

## 7. Config

Package:

```text
config/
├── BeanConfig.java
├── OpenApiConfig.java
└── DataSeeder.java
```

- `BeanConfig`: cấu hình và wire các Business Service.
- `OpenApiConfig`: cấu hình OpenAPI/Swagger.
- `DataSeeder`: dữ liệu ban đầu cho môi trường development.

---

## 8. Resources

```text
resources/
├── application.properties
└── db/
    └── migration/
```

`application.properties`:

- Database configuration.
- JPA configuration.
- JWT configuration.
- Server configuration.

Database credentials và secrets không hard-code trong source code.

`db/migration/` chứa database migration scripts.

---

## 9. Model Boundaries

Project có ba loại representation:

```text
API DTO
   ↓
Business Model
   ↓
JPA Entity
   ↓
Database
```

Ranh giới giữa các tầng:

```text
API Mapper
    ↓
DTO ↔ Business Model

Data Mapper
    ↓
Business Model ↔ JPA Entity
```

Không dùng:

- JPA Entity trong Business Layer.
- API DTO trong Business Layer.
- JPA Entity làm API Response.

---

## 10. Dependency Rules

### Được phép

```text
API → Business

Data → Business
```

Data triển khai các interface do Business định nghĩa.

### Không được phép

```text
Business → API
Business → Data
Business → Security
Business → Spring Web
Business → Spring Data
Business → JPA
Business → Hibernate
Business → PostgreSQL
```

### Các rule chính

- Business Service không phụ thuộc Spring.
- Business Repository chỉ là interface.
- JPA Repository chỉ nằm trong Data Layer.
- JPA Entity chỉ nằm trong Data Layer.
- Controller không chứa business rules.
- Controller không truy cập database trực tiếp.
- Không trả JPA Entity trực tiếp qua API.
- API Mapper và Data Mapper là hai boundary khác nhau.
- Không tạo circular dependency giữa các Business Service.
- JWT được xử lý tại Security Layer.
- Nếu identity đã có từ JWT, không dùng `studentId` từ request body để xác định người dùng.

---

## 11. Core Architecture

```text
                         CLIENT
                           │
                           ▼
                    ┌─────────────┐
                    │     API     │
                    │ Controllers │
                    │ DTOs        │
                    │ Mappers     │
                    └──────┬──────┘
                           │
                           ▼
                    ┌─────────────┐
                    │  BUSINESS   │
                    │ Models      │
                    │ Services    │
                    │ Rules       │
                    │ Ports       │
                    └──────▲──────┘
                           │
                       implements
                           │
                    ┌──────┴──────┐
                    │    DATA     │
                    │ Adapters    │
                    │ JPA         │
                    │ Entities    │
                    │ Mappers     │
                    └──────┬──────┘
                           │
                           ▼
                       PostgreSQL

              ┌──────────────────────────┐
              │        SECURITY          │
              │ JWT / BCrypt             │
              └──────────────────────────┘

              ┌──────────────────────────┐
              │         CONFIG           │
              │ Bean / OpenAPI / Seeder  │
              └──────────────────────────┘
```

### Core rule

```text
API ──────────────→ BUSINESS ←────────────── DATA

                     │
                     ├── No framework
                     └── No database
```
