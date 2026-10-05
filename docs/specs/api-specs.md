# API Specification

## Actors

- **Student**
- **System Admin**

---

## 1. Authentication & User Profile

**Base paths:**

- `/api/v1/auth`
- `/api/v1/users`

| Method | Endpoint | Permission | Description |
|---|---|---|---|
| `POST` | `/api/v1/auth/login` | Public | Đăng nhập bằng mã sinh viên/email và password. Trả về Access Token (JWT) và Refresh Token. |
| `POST` | `/api/v1/auth/refresh-token` | Public | Cấp mới Access Token khi token cũ hết hạn. |
| `GET` | `/api/v1/users/me` | Authenticated | Lấy thông tin cá nhân của người dùng đang đăng nhập: họ tên, MSSV, khoa/viện và vai trò. |
| `PUT` | `/api/v1/users/me/password` | Authenticated | Đổi mật khẩu cá nhân. |

---

## 2. Training Programs & Courses

**Base path:**

- `/api/v1/courses`

| Method | Endpoint | Permission | Description |
|---|---|---|---|
| `GET` | `/api/v1/courses` | Public / Auth | Tra cứu danh mục môn học. Hỗ trợ phân trang, filter theo khoa, số tín chỉ và từ khóa. |
| `GET` | `/api/v1/courses/{courseCode}` | Public / Auth | Xem chi tiết môn học, danh sách môn tiên quyết (prerequisites) và môn học song hành. |
| `POST` | `/api/v1/courses` | ADMIN | Thêm môn học mới vào hệ thống. |
| `PUT` | `/api/v1/courses/{courseCode}` | ADMIN | Cập nhật thông tin môn học, số tín chỉ và điều kiện tiên quyết. |

---

## 3. Semesters & Enrollment Periods

**Base paths:**

- `/api/v1/semesters`
- `/api/v1/enrollment-periods`

### Enrollment Session

| Method | Endpoint | Permission | Description |
|---|---|---|---|
| `GET` | `/api/v1/semesters/current` | Public / Auth | Lấy thông tin học kỳ hiện tại đang kích hoạt. |
| `GET` | `/api/v1/enrollment-periods/active` | Public / Auth | Lấy danh sách các đợt đăng ký tín chỉ đang mở, kèm khung giờ mở cho từng khóa/khoa. |
| `POST` | `/api/v1/enrollment-periods` | ADMIN | Thiết lập đợt mở đăng ký: thời gian bắt đầu, kết thúc và giới hạn tín chỉ tối đa/tối thiểu. |

---

## 4. Course Classes

**Base path:**

- `/api/v1/course-classes`

| Method | Endpoint | Permission | Description |
|---|---|---|---|
| `GET` | `/api/v1/course-classes` | Public / Auth | Tra cứu danh sách lớp mở trong kỳ. Hỗ trợ filter theo `semesterId`, `courseCode`, thứ trong tuần và giảng viên. |
| `GET` | `/api/v1/course-classes/{classId}` | Public / Auth | Xem chi tiết lớp: thời khóa biểu, phòng học, giảng viên, sĩ số hiện tại và sĩ số tối đa (`enrolledCount` / `capacity`). |
| `POST` | `/api/v1/course-classes` | ADMIN | Tạo mới một lớp học phần kèm lịch học và phân bổ phòng. |
| `PUT` | `/api/v1/course-classes/{classId}/status` | ADMIN | Đóng/mở lớp thủ công hoặc hủy lớp nếu không đủ sĩ số tối thiểu. |

---

## 5. Enrollments & Academic Results

**Base paths:**

- `/api/v1/enrollments`
- `/api/v1/schedules`
- `/api/v1/transcripts`

| Method | Endpoint | Permission | Description |
|---|---|---|---|
| `POST` | `/api/v1/enrollments` | STUDENT | **Đăng ký học phần:** gửi `classId` để đăng ký vào lớp. Hệ thống kiểm tra điều kiện tiên quyết, trùng lịch và trừ slot giữ chỗ. |
| `DELETE` | `/api/v1/enrollments/{classId}` | STUDENT | **Hủy học phần:** hủy lớp đã đăng ký trong thời gian cho phép và nhả lại slot. |
| `GET` | `/api/v1/enrollments/my-classes` | STUDENT | Xem danh sách các lớp học phần sinh viên đã đăng ký thành công trong kỳ, kèm tổng số tín chỉ tích lũy. |
| `GET` | `/api/v1/schedules/me` | STUDENT | Lấy thời khóa biểu tuần/tháng cá nhân dựa trên các lớp đã ghi danh. |
| `GET` | `/api/v1/transcripts/me` | STUDENT | Xem bảng điểm tích lũy và GPA/CPA các kỳ trước. Dữ liệu được sử dụng để validate điều kiện môn tiên quyết. |

---

## 6. API Permission Summary

| Role | Scope |
|---|---|
| `Public` | Authentication và các API tra cứu được công khai |
| `Authenticated` | API yêu cầu người dùng đăng nhập |
| `STUDENT` | Đăng ký/hủy học phần và xem dữ liệu học tập cá nhân |
| `ADMIN` | Quản lý môn học, lớp học phần và đợt đăng ký |

---

## 7. API Conventions

- Base URL sử dụng prefix `/api/v1`.
- Resource names sử dụng dạng plural.
- `GET` dùng cho truy vấn dữ liệu.
- `POST` dùng cho tạo resource hoặc thực hiện operation tạo mới.
- `PUT` dùng cho cập nhật resource.
- `DELETE` dùng cho xóa/hủy resource.
- Các API yêu cầu đăng nhập sử dụng JWT Bearer Authentication.
- Identity của người dùng được xác định từ JWT, không lấy `studentId` từ request body khi đã có authenticated context.
- API không trả trực tiếp JPA Entity.
- Request/Response sử dụng DTO riêng cho API contract.