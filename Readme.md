# Hệ thống đăng ký học phần

Backend service cho hệ thống đăng ký học phần môn học.

## Overview

Hệ thống hỗ trợ sinh viên:

- Tra cứu môn học.
- Xem lớp học phần.
- Đăng ký và hủy học phần.
- Xem thời khóa biểu.
- Xem bảng điểm và thông tin học tập.

System Admin quản lý:

- Môn học.
- Lớp học phần.
- Học kỳ.
- Các đợt đăng ký học phần.

## Tech Stack

- Java
- Spring Boot
- Spring Data JPA
- Hibernate
- PostgreSQL
- Spring Security
- JWT
- Maven
- OpenAPI / Swagger

## Project Structure

```text
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── course_registration_system/
│   │           ├── api/
│   │           ├── business/
│   │           ├── data/
│   │           ├── security/
│   │           ├── config/
│   │           └── SoftwareArchitectApplication.java
│   │
│   └── resources/
│
└── test/
```

Chi tiết về cấu trúc và dependency rules:

- [Kiến trúc hệ thống](docs/specs/architecture.md)

API contract:

- [Đặc tả API](docs/specs/api-specification.md)

## Requirements

- Java
- Maven
- PostgreSQL

## Configuration

Project sử dụng environment variables cho các thông tin cấu hình nhạy cảm.

Các cấu hình chính:

```text
DB_URL
DB_USERNAME
DB_PASSWORD
JWT_SECRET
```

Không commit database credentials hoặc secrets vào repository.

## Run the Application

Sử dụng Maven Wrapper:

```bash
./mvnw spring-boot:run
```

Build project:

```bash
./mvnw clean package
```

Chạy file JAR sau khi build:

```bash
java -jar target/<application>.jar
```

## API Documentation

API được mô tả bằng OpenAPI/Swagger.

Sau khi application chạy, Swagger UI có thể được sử dụng để xem và test các endpoint được cấu hình trong project.

## Development

Các thay đổi về code cần tuân theo kiến trúc được mô tả trong:

- [Kiến trúc hệ thống](docs/architecture.md)
- [Đặc tả API](docs/api-specification.md)

Business Layer không phụ thuộc vào Spring, JPA, Hibernate hoặc PostgreSQL.

## Git Workflow

```text
feature branch
      ↓
Pull Request
      ↓
Review
      ↓
master
```

Các thay đổi được thực hiện trên feature branch và đưa vào `master` thông qua Pull Request.
