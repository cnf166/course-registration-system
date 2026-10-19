# Project Structure — Pha 1

Backend hệ thống được chia thành ba tầng: **API**, **Business (Nghiệp Vụ)**, **Data**. Business nằm ở giữa và không biết gì về framework hay database; API và Data cùng phụ thuộc vào nó.

```text
API ──→ Business ←── Data
```

Một request đi qua các lớp như sau:

```text
HTTP → Controller → Service → Repository (interface) → Adapter → Spring Data/JPA → PostgreSQL
```

## Cấu trúc thư mục

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
│   ├── semester/        # gồm cả EnrollmentPeriod
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

Mỗi package con trong `business/` có cùng bộ ba: model, service, và interface repository (ví dụ `course/` có `Course`, `CourseService`, `CourseRepository`).

## Các tầng

**API** xử lý phần HTTP: nhận request, validate, gọi service, trả response với status phù hợp. Controller không chứa business rule và không đụng database. `GlobalExceptionHandler` đổi business exception thành HTTP response.

**Business** là Plain Java: model, service, rule, interface repository và exception. Interface repository (port) do Business định nghĩa, Data implement.

**Data** lo mọi thứ liên quan đến lưu trữ. Adapter implement interface repository của Business, bên trong gọi Spring Data repository, rồi dùng mapper để đổi qua lại giữa entity và business model.

**Security** xử lý JWT và hash password. `JwtTokenService` và `BcryptPasswordHasher` implement `TokenService` và `PasswordHasher` của Business. `JwtAuthenticationFilter` đọc token, đặt identity vào `SecurityContext` trước khi request tới controller.

**Config**: `BeanConfig` wire các service, `OpenApiConfig` cấu hình Swagger, `DataSeeder` nạp dữ liệu cho môi trường dev.

## Ba loại model

| Model | Nằm ở | Dùng để |
|---|---|---|
| DTO | `api/dto` | Dữ liệu vào/ra qua HTTP |
| Business Model | `business/*` | Logic nghiệp vụ |
| JPA Entity | `data/entity` | Ánh xạ bảng database |

Chuyển đổi chỉ xảy ra ở hai mapper: API Mapper (DTO ↔ Business Model) và Data Mapper (Business Model ↔ Entity). Không dùng chéo: không đưa DTO hoặc Entity vào Business, không trả Entity thẳng ra API.

## Quy tắc phụ thuộc

Business không import Spring, JPA, Hibernate, HTTP/JSON, DTO, Entity hay bất cứ thứ gì từ `api`, `data`, `security`.

Ngoài ra:

- Business Service không được phụ thuộc vòng tròn lẫn nhau.
- JPA Repository và Entity chỉ tồn tại trong `data/`.
- Khi đã có identity từ JWT thì lấy user từ đó, không tin `studentId` trong request body.

## Cấu hình

`application.properties` chứa cấu hình database, JPA, JWT và server. Credential và secret lấy từ biến môi trường, không hard-code. Migration script nằm trong `db/migration/`.