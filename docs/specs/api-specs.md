# API Specification

## 1. Phạm vi cho pha 1

API chỉ bao gồm:
- Đăng nhập và hồ sơ người dùng tối thiểu.
- Tra cứu các lớp học và số chỗ còn lại.
- Đăng ký một hoặc nhiều lớp trong một request, xử lý từng lớp độc lập.
- Hủy đăng ký và tra cứu các lớp đã đăng ký.
- Quản trị viên tạo lớp và cấu hình một đợt đăng ký chung.

Không thuộc MVP: resource `Course` tách biệt với lớp, học kỳ, nhiều đợt đăng ký, môn tiên quyết, transcript/GPA, lịch học tổng hợp, thông báo, thanh toán, danh sách chờ, đổi lớp và chỉnh sửa/đóng lớp trong khi đợt đăng ký đang mở.

## 2. Actors và authentication

- `STUDENT`: tra cứu lớp, đăng ký/hủy lớp và xem các đăng ký của chính mình.
- `SYSTEM_ADMIN`: tạo lớp và cấu hình thời gian mở/đóng đăng ký.
- `Public`: endpoint không yêu cầu đăng nhập.
- `Authenticated`: yêu cầu JWT hợp lệ.

Các endpoint được bảo vệ dùng `Authorization: Bearer <accessToken>`. Danh tính sinh viên lấy từ JWT; không nhận `studentId` hoặc `userId` của người đăng nhập trong request body để quyết định chủ thể thao tác.

### 2.1. Authentication & user profile

| Method | Endpoint | Permission | Mục đích |
|---|---|---|---|
| `POST` | `/api/v1/auth/login` | Public | Đăng nhập bằng email hoặc mã sinh viên và mật khẩu; trả access token và refresh token. |
| `POST` | `/api/v1/auth/refresh-token` | Public | Đổi refresh token hợp lệ lấy access token mới theo chính sách refresh token. |
| `GET` | `/api/v1/users/me` | Authenticated | Lấy hồ sơ của người dùng đang đăng nhập. |
| `PUT` | `/api/v1/users/me/password` | Authenticated | Đổi mật khẩu của chính người dùng đang đăng nhập. |

`POST /api/v1/auth/login` request:

```json
{
  "identifier": "student@example.edu",
  "password": "example-password"
}
```

`identifier` chấp nhận email hoặc mã sinh viên. Không trả `passwordHash` trong response.

Response thành công (cấu trúc minh họa):

```json
{
  "accessToken": "<jwt>",
  "refreshToken": "<refresh-token>",
  "tokenType": "Bearer",
  "expiresIn": 900
}
```

Thời hạn thực tế của token và chính sách rotation/revocation của refresh token phải được quy định trong tài liệu security/authentication.

## 3. Course classes

**Resource:** `/api/v1/course-classes`

Một course class là đơn vị sinh viên đăng ký. Mã lớp do hệ thống sinh, không do client tự chọn. Thông tin tối thiểu của lớp gồm tên, số tín chỉ, sức chứa và lịch học.

| Method | Endpoint | Permission | Mục đích |
|---|---|---|---|
| `GET` | `/api/v1/course-classes` | Public | Liệt kê lớp; hỗ trợ phân trang và các bộ lọc phù hợp với dữ liệu lớp thực tế. |
| `GET` | `/api/v1/course-classes/{classId}` | Public | Xem chi tiết lớp, lịch học, sức chứa, số chỗ đã dùng và số chỗ còn lại. |
| `POST` | `/api/v1/course-classes` | `SYSTEM_ADMIN` | Tạo lớp trước khi đợt đăng ký mở. |

Ví dụ request tạo lớp:

```json
{
  "name": "Cơ sở dữ liệu - Nhóm 01",
  "credits": 3,
  "capacity": 60,
  "schedule": [
    {
      "dayOfWeek": 2,
      "shift": 1
    },
    {
      "dayOfWeek": 4,
      "shift": 1
    }
  ]
}
```

`classId` được hệ thống sinh và trả trong response. `dayOfWeek` và `shift` phải tuân theo enum/quy ước được công bố trong API schema; ví dụ trên chỉ minh họa hình dạng request, không định nghĩa giá trị số chính thức.

Ví dụ dữ liệu lớp trong response:

```json
{
  "classId": "CLS-...",
  "name": "Cơ sở dữ liệu - Nhóm 01",
  "credits": 3,
  "capacity": 60,
  "enrolledCount": 42,
  "availableSeats": 18,
  "schedule": [
    {
      "dayOfWeek": 2,
      "shift": 1
    }
  ]
}
```

`availableSeats` là giá trị suy ra từ dữ liệu đăng ký hiện tại; không được dùng như một bảo đảm giữ chỗ. Việc xác nhận đăng ký phải dựa trên thao tác ghi có kiểm soát đồng thời ở backend/database.

## 4. Enrollment period

MVP chỉ có **một đợt đăng ký chung cho tất cả sinh viên**. Không có học kỳ hay các khung giờ riêng theo khóa/khoa trong phạm vi này.

| Method | Endpoint | Permission | Mục đích |
|---|---|---|---|
| `GET` | `/api/v1/enrollment-periods/current` | Public | Xem trạng thái và thời điểm mở/đóng của đợt đăng ký chung. |
| `PUT` | `/api/v1/enrollment-periods/current` | `SYSTEM_ADMIN` | Thiết lập/cập nhật thời điểm mở và đóng trước khi đợt đăng ký bắt đầu. |

Request minh họa:

```json
{
  "startAt": "2026-11-01T08:00:00+07:00",
  "endAt": "2026-11-03T17:00:00+07:00"
}
```

Không cho phép thay đổi lớp hoặc cấu hình đợt đăng ký theo cách làm sai lệch kết quả của các request đang xử lý. Quy tắc cho phép sửa thời gian sau khi đợt đăng ký đã mở cần được chốt; mặc định MVP không cho sửa trong thời gian đang mở.

## 5. Enrollments

**Resource:** `/api/v1/enrollments`

### 5.1. Đăng ký một hoặc nhiều lớp

`POST /api/v1/enrollments` — permission: `STUDENT`

Request nhận danh sách `classIds`, bởi một yêu cầu có thể đăng ký nhiều lớp. Mỗi lớp được xét độc lập; việc một lớp bị từ chối không hủy kết quả của các lớp còn lại.

```json
{
  "classIds": [
    "CLS-101",
    "CLS-205",
    "CLS-307"
  ]
}
```

Backend kiểm tra cho từng lớp:
1. Đợt đăng ký chung đang mở.
2. Lớp tồn tại và còn chỗ tại thời điểm ghi nhận đăng ký.
3. Không trùng lịch với các lớp mà sinh viên đã đăng ký thành công.
4. Tổng tín chỉ sau khi thêm lớp không vượt quá giới hạn cố định được cấu hình trong code.
5. Sinh viên chưa đăng ký lớp đó trước đây.

Không kiểm tra môn tiên quyết, giới hạn tín chỉ riêng theo sinh viên hoặc các điều kiện ngoài phạm vi MVP.

Response khi backend đã xử lý được từng mục:

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
      "status": "REJECTED",
      "reason": "SCHEDULE_CONFLICT"
    }
  ]
}
```

Các giá trị `status` cho mỗi lớp:
- `ENROLLED`: đăng ký đã được ghi nhận thành công và commit.
- `REJECTED`: không đăng ký; `reason` phải nêu lý do rõ ràng.
- `RETRY`: chưa có kết quả cuối cùng cho lớp này vì backend tạm thời không thể xử lý; client có thể thử lại.

Các mã `reason` dự kiến gồm `CLASS_FULL`, `SCHEDULE_CONFLICT`, `CREDIT_LIMIT_EXCEEDED`, `ALREADY_ENROLLED`, `ENROLLMENT_PERIOD_CLOSED` và `CLASS_NOT_FOUND`. Có thể bổ sung mã nghiệp vụ khác nếu tài liệu và implementation được cập nhật đồng bộ.

Nếu hệ thống quá tải đến mức không thể xử lý request an toàn, API có thể trả `503 Service Unavailable`, kèm thông báo có thể thử lại và `Retry-After` nếu xác định được thời gian. Không được trả thành công cho một lớp trước khi đăng ký của lớp đó đã được commit.

Nếu client bị timeout hoặc không nhận được response, client phải dùng `GET /api/v1/enrollments/my-classes` để xác nhận các đăng ký đã được ghi nhận trước khi quyết định gửi lại. Việc gửi lại cùng lớp không được tạo đăng ký thứ hai.

### 5.2. Hủy đăng ký

`DELETE /api/v1/enrollments/{classId}` — permission: `STUDENT`

Hủy đăng ký của sinh viên hiện tại cho lớp chỉ định. Chỉ được hủy khi đợt đăng ký đang mở. Khi hủy thành công, chỗ được giải phóng để sinh viên khác có thể đăng ký.

API không nhận `studentId` trong path hoặc body; sinh viên được xác định từ JWT.

### 5.3. Xem đăng ký của tôi

`GET /api/v1/enrollments/my-classes` — permission: `STUDENT`

Trả các lớp mà sinh viên hiện tại đã đăng ký thành công. Endpoint này là cách để kiểm tra kết quả sau timeout hoặc khi client không nhận được phản hồi của request đăng ký.

Response minh họa:

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

`totalCredits` là tổng tín chỉ của các lớp hiện đang được đăng ký thành công, không phải GPA/CPA hay tín chỉ tích lũy toàn khóa.

## 6. Quy ước lỗi và HTTP status

Response lỗi dùng chung một cấu trúc nhất quán, ví dụ:

```json
{
  "code": "CLASS_FULL",
  "message": "Lớp đã đủ sức chứa.",
  "retryable": false
}
```

| HTTP status | Ý nghĩa dự kiến |
|---|---|
| `200 OK` | Truy vấn thành công hoặc request đăng ký nhiều lớp đã được xử lý và trả kết quả theo từng lớp. |
| `201 Created` | Tạo lớp thành công. |
| `400 Bad Request` | Request sai định dạng hoặc vi phạm validation đầu vào. |
| `401 Unauthorized` | Thiếu hoặc không hợp lệ thông tin xác thực. |
| `403 Forbidden` | Đã xác thực nhưng không đủ quyền. |
| `404 Not Found` | Resource không tồn tại, khi phù hợp với endpoint. |
| `409 Conflict` | Xung đột nghiệp vụ không được biểu diễn trong kết quả theo từng lớp. |
| `503 Service Unavailable` | Hệ thống tạm thời không thể xử lý an toàn; client có thể thử lại theo hướng dẫn. |

Không sử dụng `500` cho lỗi nghiệp vụ có thể xác định được. Không tuyên bố request đã thành công nếu transaction chưa commit.

## 7. Nguyên tắc nhất quán và tải cao điểm

Các quy tắc sau là yêu cầu bắt buộc đối với implementation, không chỉ là chi tiết giao diện API:

- **Không vượt sức chứa:** nhiều request tranh chỗ cuối cùng không được khiến số đăng ký thành công vượt `capacity`.
- **Không mất đăng ký:** chỉ trả `ENROLLED` sau khi dữ liệu đã commit bền vững.
- **Không đăng ký trùng:** gửi lại request hoặc bấm hai lần không tạo nhiều đăng ký cho cùng một sinh viên/lớp.
- **Kết quả rõ ràng:** mỗi lớp có kết quả cuối cùng hoặc được đánh dấu cần thử lại; lỗi hệ thống không được giả làm kết quả thành công.
- **Suy giảm an toàn:** khi quá tải, trả lỗi retryable hoặc `503` thay vì bỏ qua các bảo đảm trên.

Các endpoint đăng ký phải được kiểm thử với request đồng thời; việc chỉ kiểm tra số chỗ trước khi insert không đủ để đảm bảo tính đúng đắn.

## 8. Quy ước API chung

- Base path: `/api/v1`.
- Resource dùng tên số nhiều, ngoại trừ các endpoint đại diện cho tài nguyên singleton như `current`.
- Dùng DTO riêng cho request/response; không trả trực tiếp JPA Entity.
- Endpoint yêu cầu đăng nhập dùng JWT Bearer Authentication.
- Không tin `studentId`/`userId` do client gửi để xác định chủ sở hữu đăng ký.
- Thời gian truyền qua API phải có timezone/offset rõ ràng.
- Mọi thay đổi API phải được phản ánh đồng thời trong controller, DTO, OpenAPI và tài liệu này.