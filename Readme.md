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

- Java 21
- Spring Boot
- SQLite (qua `DaoSqlite` trong `libs/`)
- Spring Security
- JWT
- Maven
- OpenAPI / Swagger
- Docker / Docker Compose, nginx

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
│   │           ├── utils/
│   │           └── SoftwareArchitectApplication.java
│   │
│   └── resources/
│
└── test/
```

Chi tiết về cấu trúc và dependency rules:

- [Kiến trúc hệ thống](docs/specs/architecture.md)

API contract:

- [Đặc tả API](docs/specs/api-specs.md)

## Requirements

- Java 21
- Maven (hoặc dùng Maven Wrapper `./mvnw`)
- Docker và Docker Compose v2 (nếu deploy bằng container)

## Configuration

Project sử dụng environment variables cho cấu hình (xem `.env.example`).

| Biến              | Mặc định        | Mô tả                                 |
|-------------------|-----------------|---------------------------------------|
| `DAO_SQLITE_PATH` | `./data/app.db` | File SQLite database dùng bởi `DaoSqlite` |

Thư mục `data/` không được commit. Không commit secrets vào repository.

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
java --enable-native-access=ALL-UNNAMED -jar target/software-architect-0.0.1-SNAPSHOT.jar
```

## Run with Docker

Build image và chạy app sau nginx (port 80), database được lưu ở `./data` trên host:

```bash
docker compose up -d --build
```

Chi tiết về setup, deploy, backup và vận hành:

- [Deployment](.github/DEPLOYMENT.md)

## API Documentation

API được mô tả bằng OpenAPI/Swagger.

Sau khi application chạy, Swagger UI có thể được sử dụng để xem và test các endpoint được cấu hình trong project.

## Development

Các thay đổi về code cần tuân theo kiến trúc được mô tả trong:

- [Kiến trúc hệ thống](docs/specs/architecture.md)
- [Đặc tả API](docs/specs/api-specs.md)

Business Layer không phụ thuộc vào Spring hoặc tầng lưu trữ dữ liệu (SQLite / `DaoSqlite`).

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
