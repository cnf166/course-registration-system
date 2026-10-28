# Bài toán đặt ra

## 1. Bối cảnh

Khi cổng đăng ký học phần mở, sinh viên thường truy cập và gửi yêu cầu đăng ký cùng một lúc. Các hệ thống đăng ký mà nhóm từng sử dụng thường gặp sự cố đúng vào thời điểm này: máy chủ không phản hồi khi lượng truy cập đồng thời vượt quá một ngưỡng nhất định, khiến sinh viên không biết yêu cầu đăng ký của mình đã được xử lý thành công hay chưa.

Dự án hướng đến xây dựng một hệ thống đăng ký học phần có thể xử lý tốt tình huống này.

## 2. Đối tượng sử dụng

- Sinh viên (đối tượng chính): cần đăng ký được lớp học phần mong muốn và nhận được kết quả rõ ràng, đáng tin cậy.
- Quản trị viên hệ thống: chuẩn bị đợt đăng ký trước khi mở cổng, bao gồm tạo lớp học phần và thiết lập thời gian đăng ký. Quản trị viên không chịu áp lực xử lý lượng truy cập lớn trong giờ cao điểm.

## 3. Vấn đề cần giải quyết

Khi đợt đăng ký bắt đầu, hơn 3.000 sinh viên có thể gửi yêu cầu đăng ký cùng một thời điểm. Ngay cả khi hệ thống chịu tải lớn, mọi yêu cầu vẫn phải được xử lý chính xác.

Hệ thống có thể phản hồi chậm, nhưng không được xử lý sai.

## 4. Các yêu cầu hệ thống phải đảm bảo

| Mã | Yêu cầu             | Ý nghĩa                                                                                                                                                                 |
| -- | ------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| G1 | Không vượt sức chứa | Số sinh viên đăng ký một lớp không bao giờ vượt quá sức chứa, kể cả khi nhiều yêu cầu cùng tranh chỗ cuối cùng.                                                         |
| G2 | Không mất đăng ký   | Khi hệ thống thông báo đăng ký thành công, chỗ đó phải được giữ cho sinh viên và dữ liệu đăng ký vẫn tồn tại sau khi hệ thống gặp sự cố hoặc khởi động lại.             |
| G3 | Không đăng ký trùng | Khi gửi lại yêu cầu hoặc nhấn nút đăng ký nhiều lần, sinh viên vẫn chỉ có tối đa một đăng ký cho mỗi lớp.                                                               |
| G4 | Trả kết quả rõ ràng | Mỗi yêu cầu phải có kết quả: đăng ký thành công, bị từ chối kèm lý do hoặc hệ thống đang quá tải và yêu cầu thử lại. Không được để kết quả ở trạng thái không xác định. |
| G5 | Suy giảm an toàn    | Khi quá tải, hệ thống có thể phản hồi chậm hoặc yêu cầu sinh viên thử lại, nhưng vẫn phải đảm bảo G1–G4.                                                                |

Khi phải lựa chọn giữa độ tin cậy của kết quả (G1–G4) và khả năng tiếp nhận mọi yêu cầu trong giờ cao điểm, hệ thống ưu tiên tính đúng đắn và độ tin cậy.

## 5. Phạm vi dự án

### Trong phạm vi

Sinh viên

- Xem các lớp học phần đang mở đăng ký và số chỗ còn lại.
- Đăng ký một hoặc nhiều lớp trong cùng một yêu cầu. Mỗi lớp được xét độc lập; một lớp bị từ chối không làm hủy các lớp đã đăng ký thành công.
- Hủy đăng ký một lớp khi đợt đăng ký còn mở. Chỗ vừa được giải phóng có thể được sinh viên khác đăng ký.
- Xem danh sách lớp đã đăng ký để xác nhận kết quả khi yêu cầu trước đó bị timeout hoặc hệ thống thông báo quá tải và yêu cầu thử lại.

Quy tắc đăng ký - được kiểm tra riêng cho từng lớp tại thời điểm đăng ký

- Lớp còn chỗ, chưa đạt sức chứa tối đa.
- Không trùng lịch với các lớp sinh viên đã đăng ký.
- Tổng số tín chỉ đã đăng ký không vượt quá giới hạn cố định được quy định trong mã nguồn.
- Chỉ được đăng ký hoặc hủy đăng ký trong thời gian đăng ký đã thiết lập.

Quản trị viên hệ thống

- Tạo lớp học phần với các thông tin: tên lớp, số tín chỉ, sức chứa và lịch học. Mã lớp do hệ thống tự sinh.
- Thiết lập thời gian bắt đầu và kết thúc đợt đăng ký. Một đợt đăng ký được áp dụng chung cho tất cả sinh viên.

# Các vấn đề nghiệp vụ

1. **Vượt sức chứa:** Nhiều sinh viên đăng ký cùng lúc khiến số lượng đăng ký vượt sức chứa lớp.
2. **Đăng ký trùng:** Sinh viên gửi lại yêu cầu hoặc nhấn đăng ký nhiều lần.
3. **Trùng lịch:** Các lớp đã đăng ký có lịch học trùng nhau.
4. **Vượt giới hạn tín chỉ:** Tổng số tín chỉ vượt mức cho phép.
5. **Sai thời gian đăng ký:** Đăng ký ngoài thời gian quy định.
6. **Hủy đăng ký:** Chỗ không được giải phóng chính xác sau khi hủy.
7. **Không rõ kết quả:** Request timeout nhưng không biết đăng ký đã thành công hay chưa.
8. **Đăng ký nhiều lớp:** Một lớp thất bại ảnh hưởng đến kết quả các lớp khác.
9. **Mất dữ liệu:** Đăng ký đã xác nhận nhưng không được lưu bền vững.
10. **Thay đổi thông tin lớp:** Sức chứa hoặc lịch học thay đổi trong đợt đăng ký.

