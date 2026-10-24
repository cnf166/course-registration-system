# Phát biểu bài toán (bản nháp)

## Bối cảnh

Khi mở đợt đăng ký học phần, mọi sinh viên cùng đăng ký vào một thời điểm. Các hệ thống đăng ký của trường mà chúng tôi từng dùng đều gặp sự cố đúng vào lúc đó: server ngừng phản hồi khi vượt quá một số lượng sinh viên truy cập đồng thời, và sinh viên không biết đăng ký của mình đã thành công hay chưa.

Dự án này xây dựng một hệ thống đăng ký mới, thiết kế cho đúng thời điểm đó.

## Ai gặp vấn đề

- **Sinh viên** (actor chính): cần có được chỗ trong các lớp mình muốn, và cần một câu trả lời rõ ràng, đáng tin cậy về việc mình đã có chỗ hay chưa.
- **System admin**: thiết lập việc đăng ký trước khi mở (tạo lớp, đặt thời gian đợt đăng ký). Admin không chịu áp lực trong thời điểm cao điểm.

## Bài toán

Khi đợt đăng ký mở, **hơn 2.000 sinh viên** gửi yêu cầu đăng ký **cùng một lúc**. Dưới đợt tăng đột biến này, hệ thống vẫn phải trả lời **chính xác** mọi yêu cầu. Chậm thì chấp nhận được; sai thì không.

## Hệ thống phải đảm bảo

| # | Đảm bảo | Ý nghĩa |
|---|---|---|
| G1 | Không vượt sĩ số | Số sinh viên đã đăng ký của một lớp không bao giờ vượt quá sức chứa, kể cả khi nhiều yêu cầu cùng tranh chỗ cuối cùng. |
| G2 | Không mất đăng ký | Khi sinh viên đã được báo "đăng ký thành công", chỗ đó thuộc về họ và không mất khi hệ thống sập hay khởi động lại. |
| G3 | Không trùng lặp | Gửi lại hoặc bấm hai lần chỉ đăng ký sinh viên tối đa một lần cho mỗi lớp. |
| G4 | Câu trả lời rõ ràng | Mọi yêu cầu đều kết thúc ở một trong ba trạng thái: thành công, bị từ chối (kèm lý do) hoặc "hệ thống bận, thử lại". Không bao giờ ở trạng thái không xác định. |
| G5 | Suy giảm an toàn | Khi quá tải, hệ thống có thể chậm lại hoặc yêu cầu sinh viên thử lại, nhưng không bao giờ vi phạm G1–G4. |

Khi độ tin cậy (G1–G4) mâu thuẫn với tính sẵn sàng lúc cao điểm, **độ tin cậy được ưu tiên**.

## Phạm vi

### Trong phạm vi

**Sinh viên**

- Xem danh sách lớp đang mở và số chỗ còn lại.
- Đăng ký một hoặc nhiều lớp trong một yêu cầu. Mỗi lớp được chấp nhận hoặc từ chối độc lập; một lớp bị từ chối không hủy các lớp còn lại.
- Hủy một lớp đã đăng ký khi đợt đăng ký còn mở. Chỗ được giải phóng ngay cho sinh viên khác.
- Xem các lớp mình đã đăng ký (cần để xác nhận kết quả sau khi bị timeout hoặc nhận "hệ thống bận, thử lại").

**Quy tắc đăng ký** (kiểm tra cho từng lớp khi đăng ký)

- Lớp còn chỗ (chưa đạt sức chứa).
- Không trùng lịch học với các lớp sinh viên đã đăng ký.
- Tổng số tín chỉ đã đăng ký không vượt quá một giới hạn cố định được định nghĩa trong code.
- Chỉ được đăng ký và hủy khi đợt đăng ký đang mở.

**System admin**

- Tạo lớp (tên, số tín chỉ, sức chứa, lịch học). Mã lớp do hệ thống tự sinh.
- Đặt đợt đăng ký (thời gian mở và đóng). Chỉ có một đợt chung cho mọi sinh viên.

### Ngoài phạm vi

- Học phần (nhóm các lớp theo học phần), học kỳ và nhiều đợt đăng ký.
- Đóng hoặc sửa lớp khi đợt đăng ký đang mở.
- Đổi lớp, danh sách chờ.
- Môn tiên quyết, số tín chỉ tối thiểu, giới hạn tín chỉ riêng cho từng sinh viên.
- Thanh toán, điểm, xếp thời khóa biểu, thông báo.
