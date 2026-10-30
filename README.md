# Hệ Thống Đăng Ký Học Phần (Course Registration System)

## Mục lục

* [Công nghệ sử dụng](#công-nghệ-sử-dụng)
* [Bắt đầu nhanh](#bắt-đầu-nhanh)
* [Triển khai](#triển-khai)
* [Tài liệu hướng dẫn (Documentation)](#tài-liệu-hướng-dẫn-documentation)

## Công nghệ sử dụng

- Java 21
- Spring Boot
- SQLite
- Maven
- OpenAPI/Swagger
- Docker Compose
- nginx

## Bắt đầu nhanh

Yêu cầu môi trường cài đặt Java 21 (Maven đã được tích hợp sẵn qua `./mvnw`).

```bash
./mvnw spring-boot:run          # Chạy ứng dụng ở môi trường local
./mvnw clean package            # Đóng gói file jar vào thư mục target/

```

Giao diện Swagger UI: `http://localhost:8080/swagger-ui/index.html`
Swagger UI: `http://localhost:8080/swagger-ui/index.html`
OpenAPI JSON: `http://localhost:8080/v3/docs`

## Triển khai

```bash
mkdir -p data                   # Tạo thư mục trước khi compose (cần thiết nếu sử dụng SQLite để lưu trữ và cập nhật DB sau này)
docker compose up -d --build    # Ứng dụng chạy phía sau nginx tại http://localhost

```

## Tài liệu hướng dẫn (Documentation)

| Tài liệu (Document) | Mô tả tiếng Việt | Description (English) |
| --- | --- | --- |
| [Problem Statement](docs/requirements/problem_statement_vi.md) | Bài toán và yêu cầu nghiệp vụ | Problem statement |
| [Architecture](docs/specs/architecture_vi.md) | Kiến trúc dự án và quy tắc phụ thuộc | Project structure and dependency rules |
| [API Specification](docs/specs/api_specs_vi.md) | Đặc tả giao diện lập trình ứng dụng (API contract) | API contract |
| [DB Schema](docs/specs/db_schema.md) | Thiết kế lược đồ cơ sở dữ liệu | Database design |
| [Deployment](.github/DEPLOYMENT.md) | Hướng dẫn triển khai bằng Docker Compose | Docker Compose deployment |