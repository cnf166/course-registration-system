# Sprint 1 — Khung dự án

**Thời gian:** Thứ Hai 2026-10-05 → Thứ Năm 2026-10-15

**Mục tiêu:** mọi tầng của hệ thống đều được dựng khung và chạy được, để sprint sau chỉ cần điền logic vào. Ba task chạy song song; không ai phải chờ task khác xong.

**Mục tiêu mở rộng (cuối tuần 1):** API skeleton được host ở nơi cả team truy cập được, mở sẵn Swagger UI. Vinh phụ trách phần hosting.

| Dev | Task |
|---|---|
| Việt | Bố cục API controller + DTO + tài liệu Swagger |
| Việt Anh | Database, ánh xạ entity, auth service |
| Vinh | Khởi tạo hệ thống (business layer, DI, bố cục service) |

Tham khảo: [API specs](../specs/api-specs.md), [architecture](../specs/architecture.md), [DB schema](../specs/db_schema.md).

---

## Task 1 — Bố cục API controller (Việt)

Dựng tầng API mà không cần chờ service. DTO thiết kế theo ý bạn; khi nối logic thật vào và thấy dùng thế nào thì chỉnh sau.

**Phạm vi**

- Mỗi resource một controller class, khai báo đủ 20 endpoint trong API specs. Body là stub (trả về `501 Not Implemented`).
- DTO cho request và response, có validation annotation trên request.
- Swagger annotation trên mọi endpoint: summary, response code, bearer auth cho endpoint cần bảo vệ, nhóm theo tag.
- Khung `ErrorResponse` và `GlobalExceptionHandler`.

**Definition of done**

- Code đã merge vào `develop`.
- Swagger UI hiển thị đầy đủ contract: mọi endpoint, kèm cấu trúc request và response, theo [chuẩn OpenAPI](https://swagger.io/open-api/).
- Không controller nào trả về JPA entity hay chứa business logic.
- Các field trong spec chưa có bảng (môn tiên quyết, giảng viên, bảng điểm, trạng thái lớp) vẫn được khai báo và đánh dấu là chưa có dữ liệu thật (not backed).

## Task 2 — Database, entity, auth service (Việt Anh)

**Phạm vi**

- JPA entity khớp với DB schema, và mỗi entity một Spring Data repository.
- Auth hoàn chỉnh: đăng nhập, refresh token, đổi mật khẩu, JWT filter, hash mật khẩu, quy tắc phân quyền theo cột permission trong API specs.
- Đăng nhập chấp nhận email hoặc mã sinh viên. Thêm unique constraint cho `student_id` (cả entity và tài liệu DB schema).
- Phụ trách `security/`, `business/auth` và `business/user` (model, repository port và adapter của nó). Phần này là mẫu tham khảo cho cách chia tầng một module.
- `DataSeeder` với một admin và một sinh viên, đủ để đăng nhập.
- **Sản phẩm đầu tiên:** một `SecurityConfig` tối thiểu cho phép truy cập Swagger UI và các route public. Chừng nào chưa có, Spring Security chặn mọi endpoint và không xem được Task 1.

**Definition of done**

- Design doc và code đã merge vào `develop`.
- App khởi động được trên một database PostgreSQL trống.
- Đăng nhập qua Swagger bằng email hoặc mã sinh viên trả về token; token mở được một stub endpoint cần bảo vệ, và sai role thì nhận `403`.
- Design doc nêu rõ: thời hạn token, chiến lược refresh, nơi kiểm tra role.

## Task 3 — Khởi tạo hệ thống (Vinh)

**Phạm vi**

- Các package `business/` cho `course`, `courseclass`, `enrollment`, `semester`: business model, service class (chữ ký method, body rỗng) và repository port cho mỗi module.
- Lớp cơ sở cho business exception và các loại exception.
- `BeanConfig` wire các business service; `business/` không import gì từ Spring hay JPA.

**Definition of done**

- Design doc và code đã merge vào `develop`.
- App khởi động được với mọi service đã được wire.
- Một module (không phải auth) có một luồng chạy thật tối giản từ controller tới database, làm mẫu cho các module khác.
- Design doc nêu rõ: quy tắc chia tầng, request đi từ controller tới service tới port như thế nào, cách thêm một module mới.

---

## Thỏa thuận làm việc

- Design doc nằm trong `docs/`, viết song song với lúc code, không viết sau.
- Sơ đồ viết bằng Mermaid. Dùng [vpascode.com](https://vpascode.com) để xem.
- Ranh giới giữa Task 2 và Task 3: Task 3 định nghĩa các repository port; adapter cho `course`, `courseclass`, `enrollment` và `semester` được viết ở sprint sau, khi đã có port.
