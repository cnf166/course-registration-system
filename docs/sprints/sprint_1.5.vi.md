# Sprint 1.5 — Chỉnh sửa theo nhận xét của giảng viên

**Thời gian:** Thứ Hai 2026-10-19 → Thứ Năm 2026-10-22, 07:00

**Mục tiêu:** thu hẹp dự án về một bài toán được định nghĩa rõ ràng và viết tài liệu đầy đủ trước khi code thêm: phát biểu bài toán, đặc tả nghiệp vụ và user flow. Sau đó API spec và code được cắt gọn cho khớp.

Tham khảo: [Sprint 1](sprint_1.vi.md), [API specs](../specs/api-specs.md), [architecture](../specs/architecture.md), [DB schema](../specs/db_schema.md).

---

## Nhận xét của giảng viên

1. Phạm vi quá rộng. Chọn một bài toán cốt lõi, cắt hoặc hoãn mọi thứ không phục vụ bài toán đó.
2. Viết tài liệu phát biểu bài toán: ai gặp vấn đề, hiện tại vấn đề gì xảy ra, và hệ thống phải đảm bảo điều gì.
3. Viết đặc tả nghiệp vụ (actor, quy tắc, ràng buộc) và tài liệu user flow. Không cần sequence diagram.
4. Một số endpoint trong [API specs](../specs/api-specs.md) được đánh dấu "Public / Auth", gây khó hiểu. Nếu người chưa đăng nhập và người đã đăng nhập nhận hành vi khác nhau, mô tả riêng từng trường hợp; nếu giống nhau, đánh dấu là Public.

## Sản phẩm

- **Phát biểu bài toán** — bài toán duy nhất mà hệ thống giải quyết, và những gì nằm ngoài phạm vi.
- **Đặc tả nghiệp vụ** — actor, quy tắc nghiệp vụ và ràng buộc của bài toán đã chọn.
- **User flow** — mỗi use case chính một flow, viết bằng Mermaid.
- **API spec đã chỉnh sửa** — endpoint được cắt theo phạm vi mới, không còn quyền gộp "Public / Auth".
- **Code khớp với spec mới** — endpoint, DTO và entity nằm ngoài phạm vi được xóa hoặc hoãn lại.

## Trọng tâm

Hệ thống giải quyết một bài toán: **giữ cho việc đăng ký học phần nhanh và chính xác trong thời gian mở đăng ký**, khi mọi sinh viên cùng truy cập hệ thống một lúc.

Các đặc tính kiến trúc chính:

- **Scalability (khả năng mở rộng)** — chịu được lượng truy cập tăng đột biến khi một đợt đăng ký mở.
- **Reliability (độ tin cậy)** — mọi lượt đăng ký đều được xử lý đúng: không lớp nào vượt sĩ số, không lượt đăng ký đã được chấp nhận nào bị mất. Khi xung đột với availability lúc tải cao điểm, reliability được ưu tiên: hệ thống có thể chậm lại hoặc yêu cầu sinh viên thử lại, nhưng không bao giờ trả kết quả sai.

## Task

| Task | Lead | Cùng với |
|---|---|---|
| 1. Phát biểu bài toán và đặc tả nghiệp vụ | Vinh | Cả team (họp) |
| 2. User flow | Việt | Vinh review |
| 3. Chỉnh sửa API spec và code cho khớp | Việt Anh | — |

### Task 1 — Phát biểu bài toán và đặc tả nghiệp vụ (Vinh)

**Deadline:** Thứ Ba 2026-10-20, 09:00

Vinh viết bản nháp trước; buổi họp của team dùng để xác nhận chứ không viết lại từ đầu.

**Phạm vi**

- Phát biểu bài toán: ai gặp vấn đề, vấn đề gì xảy ra lúc đăng ký, hệ thống phải đảm bảo điều gì, và những gì nằm ngoài phạm vi.
- Đặc tả nghiệp vụ: actor, use case, quy tắc nghiệp vụ và ràng buộc của việc đăng ký.

**Definition of done**

- Team đã xác nhận bản nháp trong buổi họp.
- Tài liệu đã merge vào `develop`.

### Task 2 — User flow (Việt)

**Phạm vi**

- Mỗi use case trong đặc tả nghiệp vụ một user flow, viết bằng Mermaid.

**Definition of done**

- Mọi use case trong đặc tả nghiệp vụ đều có flow.
- Vinh đã review và merge vào `develop`.

### Task 3 — Chỉnh sửa API spec và code cho khớp (Việt Anh)

**Phạm vi**

- Cắt API spec chỉ còn những endpoint mà các user flow cần.
- Mỗi endpoint chỉ có một quyền; không gộp "Public / Auth".
- Xóa hoặc hoãn các endpoint, DTO và entity nằm ngoài phạm vi.

**Definition of done**

- API spec mới và code đã merge vào `develop`.
- Mọi endpoint trong spec đều được ít nhất một user flow sử dụng.
- Swagger UI khớp với spec mới.
