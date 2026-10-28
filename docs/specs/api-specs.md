
# **TÀI LIỆU ĐẶC TẢ API (API SPECIFICATION)**

## 1. Quy định chung
*   **Base Path:** `/api/v1`
*   **Authentication:** JWT qua header `Authorization: Bearer <accessToken>`. 
*   **Định danh:** Thông tin `studentId`/`userId` phải được extract từ JWT, **tuyệt đối không** lấy từ request body/path parameter để quyết định quyền sở hữu dữ liệu.
*   **Định dạng thời gian:** ISO 8601 kèm timezone (vd: `2026-11-01T08:00:00+07:00`).

## 2. Roles & Permissions
*   `Public`: Không yêu cầu token.
*   `Authenticated`: Yêu cầu JWT hợp lệ (bất kỳ role nào).
*   `STUDENT`: Sinh viên thực hiện tra cứu, đăng ký, hủy lớp.
*   `SYSTEM_ADMIN`: Quản trị viên tạo lớp và mở/đóng đợt đăng ký.

---

## 3. Đặc tả Endpoints & Payloads

### 3.1. Authentication & Users
| Method | Endpoint | Auth | Chức năng |
| :--- | :--- | :--- | :--- |
| `POST` | `/auth/login` | Public | Đăng nhập (Email/Mã SV). |
| `POST` | `/auth/refresh-token` | Public | Cấp lại access token. |
| `GET` | `/users/me` | Auth | Lấy profile user hiện tại. |
| `PUT` | `/users/me/password` | Auth | Đổi mật khẩu user hiện tại. |

**Payload `POST /auth/login`:**
*Request:*
```json
{
  "identifier": "student@example.edu",
  "password": "example-password"
}

```

*Response (200 OK):*

```json
{
  "accessToken": "<jwt-string>",
  "refreshToken": "<refresh-token-string>",
  "tokenType": "Bearer",
  "expiresIn": 900
}

```

*(Lưu ý: Không trả về passwordHash trong bất kỳ response nào).*

### 3.2. Cấu hình đợt đăng ký (Enrollment Period)

Chỉ hỗ trợ **01 đợt đăng ký chung** duy nhất trong hệ thống. Không cho phép sửa khi đợt đăng ký đang mở.

| Method | Endpoint | Auth | Chức năng |
| --- | --- | --- | --- |
| `GET` | `/enrollment-periods/current` | Public | Xem trạng thái/thời gian đợt đăng ký. |
| `PUT` | `/enrollment-periods/current` | ADMIN | Cập nhật thời gian mở/đóng. |

**Payload `PUT /enrollment-periods/current`:**
*Request:*

```json
{
  "startAt": "2026-11-01T08:00:00+07:00",
  "endAt": "2026-11-03T17:00:00+07:00"
}

```

### 3.3. Lớp học (Course Classes)

`classId` do hệ thống tự sinh. `availableSeats` tính toán realtime từ DB, không phải là thông số giữ chỗ tĩnh.

| Method | Endpoint | Auth | Chức năng |
| --- | --- | --- | --- |
| `GET` | `/course-classes` | Public | Danh sách lớp (có phân trang/filter). |
| `GET` | `/course-classes/{classId}` | Public | Chi tiết một lớp (sức chứa, chỗ còn trống). |
| `POST` | `/course-classes` | ADMIN | Tạo lớp mới (chỉ tạo trước khi mở đợt ĐK). |

**Payload `POST /course-classes`:**
*Request:*

```json
{
  "name": "Cơ sở dữ liệu - Nhóm 01",
  "credits": 3,
  "capacity": 60,
  "schedule": [
    { "dayOfWeek": 2, "shift": 1 },
    { "dayOfWeek": 4, "shift": 1 }
  ]
}

```

*Response (201 Created):*

```json
{
  "classId": "CLS-12345",
  "name": "Cơ sở dữ liệu - Nhóm 01",
  "credits": 3,
  "capacity": 60,
  "enrolledCount": 0,
  "availableSeats": 60,
  "schedule": [
    { "dayOfWeek": 2, "shift": 1 },
    { "dayOfWeek": 4, "shift": 1 }
  ]
}

```

### 3.4. Giao dịch Đăng ký học (Enrollments)

Xử lý độc lập từng lớp trong danh sách request. Lớp bị lỗi không làm rollback lớp thành công.

| Method | Endpoint | Auth | Chức năng |
| --- | --- | --- | --- |
| `POST` | `/enrollments` | STUDENT | Đăng ký 1 hoặc nhiều lớp (Batch). |
| `DELETE` | `/enrollments/{classId}` | STUDENT | Hủy đăng ký lớp (giải phóng chỗ). |
| `GET` | `/enrollments/my-classes` | STUDENT | Xem danh sách lớp đã đăng ký thành công. |

**Payload `POST /enrollments`:**
*Request:*

```json
{
  "classIds": ["CLS-101", "CLS-205", "CLS-307"]
}

```

*Điều kiện Validate (backend thực hiện cho từng lớp):*

1. Đợt đăng ký đang mở.
2. Lớp tồn tại & `availableSeats > 0` (chặn Overbooking).
3. Không trùng lịch với các lớp đã `ENROLLED`.
4. Tổng tín chỉ $\le$ Limit config trong hệ thống.
5. Chưa từng đăng ký lớp này (chặn Duplicate).

*Response (200 OK):*

```json
{
  "results": [
    {
      "classId": "CLS-101",
      "status": "ENROLLED",
      "reason": null
    },
    {
      "classId": "CLS-205",
      "status": "REJECTED",
      "reason": "CLASS_FULL"
    },
    {
      "classId": "CLS-307",
      "status": "RETRY",
      "reason": "SYSTEM_BUSY"
    }
  ]
}

```

*(Enum Status: `ENROLLED`, `REJECTED`, `RETRY`. Enum Reason: `CLASS_FULL`, `SCHEDULE_CONFLICT`, `CREDIT_LIMIT_EXCEEDED`, `ALREADY_ENROLLED`, `ENROLLMENT_PERIOD_CLOSED`, `CLASS_NOT_FOUND`, `SYSTEM_BUSY`)*

**Payload `GET /enrollments/my-classes`:**
*Response (200 OK):*

```json
{
  "items": [
    {
      "classId": "CLS-101",
      "name": "Cơ sở dữ liệu - Nhóm 01",
      "credits": 3,
      "status": "ENROLLED"
    }
  ],
  "totalCredits": 3
}

```

*(Dùng endpoint này để xác nhận trạng thái nếu client gửi `POST /enrollments` bị timeout).*

---

## 4. Chuẩn hóa Lỗi & Ràng buộc Kỹ thuật

### 4.1. HTTP Status & Error Object

*Response lỗi chung (4xx, 5xx):*

```json
{
  "code": "CLASS_FULL",
  "message": "Lớp đã đủ sức chứa.",
  "retryable": false
}

```

| HTTP | Mô tả |
| --- | --- |
| `200` | OK. Với `POST /enrollments`, trả 200 kèm danh sách kết quả từng lớp (kể cả có lớp REJECTED). |
| `201` | Tạo resource thành công. |
| `400` | Bad Request (Sai schema, vi phạm validation đầu vào). |
| `401` | Unauthorized (Thiếu/sai JWT). |
| `403` | Forbidden (Sai role). |
| `404` | Not Found. |
| `409` | Conflict (Xung đột trạng thái). |
| `503` | Service Unavailable (Overload, client xử lý Retry/Exponential Backoff). |

### 4.2. Ràng buộc ACID & Concurrency (Bắt buộc)

1. **Chống Overbooking:** Xử lý tranh chấp (Race conditions) chặt chẽ bằng DB Locking hoặc Message Queue. Không bao giờ số `ENROLLED` vượt quá `capacity`.
2. **Commit trước, Response sau:** Trạng thái `ENROLLED` chỉ được trả về khi transaction đã commit xuống DB thành công.
3. **Idempotency:** Đảm bảo retry request đăng ký nhiều lần không sinh ra dữ liệu rác hoặc ghi nhận đăng ký 2 lần cho 1 lớp.
4. **Graceful Degradation:** Gặp tải quá cao, API phải chủ động ngắt và trả `503 Service Unavailable` thay vì treo request dẫn đến crash DB.

```

