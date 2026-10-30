# Cấu trúc dự án

Backend hệ thống được chia thành ba tầng: **API**, **Business (Nghiệp Vụ)**, **Data**. Business nằm ở giữa và không biết gì về framework hay database; API và Data cùng phụ thuộc vào nó.

```text
API -> Business <- Data
```

Một request đi qua các lớp như sau:

```text
HTTP -> Controller -> Service -> Repository (interface) -> Adapter -> Spring Data/JPA -> SQLite 
```

# Tài liệu Hệ thống

### Dưới đây là phần thiết kế hệ thống:

![Kiến trúc Hệ thống Đăng ký Môn học](/docs/highlevel_sd.png)

*Hình 1: Sơ đồ thiết kế hệ thống mức cao đề xuất cho hệ thống đăng ký môn học.*

### Còn dưới đây là mô tả kiến trúc nhóm lựa chọn cho hệ thống:

![Kiến trúc Hệ thống Đăng ký Môn học](/docs/layered_architecture.png)

*Hình 2: Sơ đồ kiến trúc hệ thống.*

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

