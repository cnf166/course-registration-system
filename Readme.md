# API Specification

**Actors:** Student & System-Admin

---

## 1. Authentication & User Profile

**Base paths:** `/api/v1/auth`, `/api/v1/users`

| Method | Endpoint | Permission | Description |
|---|---|---|---|
| `POST` | `/api/v1/auth/login` | Public | Đăng nhập bằng mã sinh viên/email + password, trả về Access Token (JWT) & Refresh Token. |
| `POST` | `/api/v1/auth/refresh-token` | Public | Cấp mới Access Token khi token cũ hết hạn. |
| `GET` | `/api/v1/users/me` | Authenticated | Lấy thông tin cá nhân của người dùng đang đăng nhập (họ tên, MSSV, khoa/viện, vai trò). |
| `PUT` | `/api/v1/users/me/password` | Authenticated | Đổi mật khẩu cá nhân. |

---

## 2. Training Programs & Courses

**Base path:** `/api/v1/courses`

| Method | Endpoint | Permission | Description |
|---|---|---|---|
| `GET` | `/api/v1/courses` | Public / Auth | Tra cứu danh mục môn học (hỗ trợ phân trang, filter theo khoa, số tín chỉ, từ khóa). |
| `GET` | `/api/v1/courses/{courseCode}` | Public / Auth | Chi tiết môn học, danh sách môn tiên quyết (prerequisites), môn học song hành. |
| `POST` | `/api/v1/courses` | ADMIN | Thêm môn học mới vào hệ sinh thái đào tạo. |
| `PUT` | `/api/v1/courses/{courseCode}` | ADMIN | Cập nhật thông tin môn học, số tín chỉ, điều kiện tiên quyết. |

---

## 3. Semesters & Registration Periods

**Base paths:** `/api/v1/semesters`, `/api/v1/registration-periods`

### Registration Session

| Method | Endpoint | Permission | Description |
|---|---|---|---|
| `GET` | `/api/v1/semesters/current` | Public / Auth | Lấy thông tin học kỳ hiện tại đang kích hoạt. |
| `GET` | `/api/v1/registration-periods/active` | Public / Auth | Lấy danh sách các đợt đăng ký tín chỉ đang mở (kèm khung giờ mở cho từng khóa/khoa). |
| `POST` | `/api/v1/registration-periods` | ADMIN | Thiết lập đợt mở đăng ký (thời gian bắt đầu, kết thúc, giới hạn tín chỉ tối đa/tối thiểu). |

---

## 4. Course Classes

**Base path:** `/api/v1/course-classes`

| Method | Endpoint | Permission | Description |
|---|---|---|---|
| `GET` | `/api/v1/course-classes` | Public / Auth | Tra cứu danh sách lớp mở trong kỳ (filter: semesterId, courseCode, thứ trong tuần, giảng viên). |
| `GET` | `/api/v1/course-classes/{classId}` | Public / Auth | Chi tiết lớp: thời khóa biểu chi tiết, phòng học, giảng viên, sĩ số hiện tại / sĩ số tối đa (`enrolledCount` / `capacity`). |
| `POST` | `/api/v1/course-classes` | ADMIN | Tạo mới một lớp học phần kèm lịch học và phân bổ phòng. |
| `PUT` | `/api/v1/course-classes/{classId}/status` | ADMIN | Đóng/mở lớp thủ công, hoặc hủy lớp nếu không đủ sĩ số tối thiểu. |

---

## 5. Enrollments & Academic Results

**Base paths:** `/api/v1/enrollments`, `/api/v1/schedules`

| Method | Endpoint | Permission | Description |
|---|---|---|---|
| `POST` | `/api/v1/enrollments` | STUDENT | **Đăng ký học phần (Core):** Gửi `classId` để đăng ký vào lớp. Hệ thống kiểm tra điều kiện tiên quyết, **trùng lịch**, và trừ slot **giữ chỗ**. |
| `DELETE` | `/api/v1/enrollments/{classId}` | STUDENT | **Hủy học phần:** Hủy lớp đã đăng ký trong thời gian cho phép, nhả lại slot cho sinh viên khác. |
| `GET` | `/api/v1/enrollments/my-classes` | STUDENT | Xem danh sách các lớp học phần sinh viên đã đăng ký **thành công** trong kỳ (kèm tổng số tín chỉ tích lũy). |
| `GET` | `/api/v1/schedules/me` | STUDENT | Lấy thời khóa biểu tuần/tháng cá nhân dựa trên các lớp đã ghi danh. |
| `GET` | `/api/v1/transcripts/me` | STUDENT | Xem bảng điểm tích lũy, điểm GPA/CPA các kỳ trước (dùng để validate điều kiện môn tiên quyết). |