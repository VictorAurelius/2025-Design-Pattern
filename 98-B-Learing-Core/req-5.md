# Yêu cầu: Tạo lại MAIN_REPORT theo format chuẩn

**Mục tiêu:** Tạo báo cáo MAIN_REPORT_V2.md có thể copy trực tiếp vào Word với format chuẩn theo Scripts/Tham_khao.pdf

**Đặc điểm quan trọng:**
- Dạng text thuần túy, dễ copy vào Word
- Format rõ ràng, đủ ý
- Có thể vẽ lại BFD dễ dàng từ mô tả
- Tuân thủ 100% format của file tham khảo

---

## CẤU TRÚC BÁO CÁO

### TRANG BÌA
```
TRƯỜNG ĐẠI HỌC GIAO THÔNG VẬN TẢI
KHOA: CÔNG NGHỆ THÔNG TIN

[Logo trường]

BÁO CÁO KẾT QUẢ BÀI TẬP LỚN HỌC PHẦN:
ĐẶC TẢ PHẦN MỀM
XÂY DỰNG HỆ THỐNG QUẢN LÝ HỌC TẬP TRỰC TUYẾN
(B-LEARNING CORE v1.0)

Người lập: Nguyễn Văn Kiệt - CNTT1-K63
Ngày lập: 02/12/2025

Người xem xét:...........................................
Ngày lập: ……/……./.………. ................
```

---

### LỜI NÓI ĐẦU

Viết 2-3 đoạn văn về:
1. Bối cảnh: Giáo dục trực tuyến phát triển mạnh, nhu cầu hệ thống LMS toàn diện
2. Lý do chọn đề tài: Xây dựng hệ thống quản lý khóa học với tính năng quản lý nội dung, bài tập, và theo dõi tiến độ
3. Phạm vi: Tập trung vào 2 chức năng chính là Course Management (Instructor) và Submission Management (Student)
4. Mục tiêu: Xây dựng sản phẩm chất lượng, dễ sử dụng, hiệu quả

**Lưu ý:** Copy style từ "Lời nói đầu" trong Tham_khao.pdf nhưng điều chỉnh cho B-Learning context

---

### MỤC LỤC

```
CHƯƠNG 1: ĐẶC TẢ YÊU CẦU PHẦN MỀM............................................................ 4
1. Giới thiệu chung ...................................................................................................... 4
   1.1. Mục đích ............................................................................................................ 4
   1.2. Phạm vi .............................................................................................................. 5
2. Sơ đồ phân rã chức năng ........................................................................................ 7
   2.1. Sơ đồ phân rã chức năng dưới góc nhìn của Instructor (Course Management)........ 7
   2.2. Sơ đồ phân rã chức năng dưới góc nhìn của Student (Submission Management).... 10
3. Mô hình hóa dữ liệu .............................................................................................. 13
   3.1. Sơ đồ ER .......................................................................................................... 13
   3.2. Sơ đồ RM......................................................................................................... 14
   3.3. Đặc tả chi tiết cơ sở dữ liệu............................................................................ 15

CHƯƠNG 2: ĐẶC TẢ YÊU CẦU CHỨC NĂNG ....................................................... 25
1. Đặc tả chức năng: Quản lý khóa học (Course Management) .............................. 25
   1.1. Giao diện Form và thành phần ..................................................................... 25
   1.2. Truy vấn dữ liệu.............................................................................................. 26
   1.3. Cập nhật dữ liệu.............................................................................................. 27
   1.4. Tiền và hậu điều kiện ..................................................................................... 27
2. Đặc tả chức năng: Nộp bài tập (Submission Management) ................................ 28
   2.1. Giao diện Form và Thành phần.................................................................... 28
   2.2. Truy vấn dữ liệu.............................................................................................. 29
   2.3. Cập nhật dữ liệu.............................................................................................. 29
   2.4. Tiền và hậu điều kiện ..................................................................................... 30
```

---

## CHƯƠNG 1: ĐẶC TẢ YÊU CẦU PHẦN MỀM

### 1. Giới thiệu chung

#### 1.1. Mục đích

Viết 2-3 đoạn văn mô tả:
- Hệ thống B-Learning Core là gì
- Mục đích xây dựng: Cung cấp nền tảng LMS đầy đủ cho giảng viên và sinh viên
- Các tính năng chính:
  - **Instructor**: Tạo và quản lý khóa học, modules, lectures, assignments, quizzes
  - **Student**: Đăng ký khóa học, học bài giảng, làm bài tập, kiểm tra
- Yêu cầu phi chức năng: Bảo mật, hiệu năng, độ tin cậy
- Công nghệ: PostgreSQL 14+, Web-based platform

**Format theo Tham_khao.pdf section 1.1**

#### 1.2. Phạm vi

Phần mềm này là một hệ thống quản lý học tập trực tuyến (LMS), bao gồm ứng dụng web cho sinh viên và nền tảng web quản trị cho giảng viên. Phạm vi của ứng dụng tập trung vào hai bên liên quan chính:

**− Instructor (Giảng viên):**
• Quản lý thông tin khóa học (tạo, cập nhật, xóa, xuất bản).
• Quản lý Module và Lecture: Tổ chức nội dung theo cấu trúc phân cấp.
• Quản lý Assignment và Quiz: Tạo bài tập, câu hỏi, thiết lập deadline.
• Chấm điểm và phản hồi: Chấm bài tập của sinh viên, cung cấp feedback.
• Theo dõi tiến độ lớp học: Xem báo cáo tiến độ của từng sinh viên.

**− Student (Sinh viên):**
• Đăng ký và quản lý tài khoản cá nhân.
• Đăng ký khóa học (Enrollment).
• Học Lecture: Xem video, đọc tài liệu, đánh dấu hoàn thành.
• Làm Assignment: Upload file bài làm, xem điểm và phản hồi.
• Làm Quiz: Trả lời câu hỏi, nhận điểm tự động.
• Theo dõi tiến độ cá nhân: Xem completion percentage, điểm số.
• Nhận Certificate: Tải chứng chỉ hoàn thành khóa học.

**Lưu ý:**
- Chi tiết hóa theo 16 tables từ DATABASE-SCHEMA.md
- Format theo style của Tham_khao.pdf section 1.2 (dạng bullet points rõ ràng)

---

### 2. Sơ đồ phân rã chức năng

#### 2.1. Sơ đồ phân rã chức năng dưới góc nhìn của Instructor (Course Management)

##### 2.1.1. Sơ đồ phân rã chức năng

**Hướng dẫn vẽ BFD:**

```
                        [Quản lý ứng dụng LMS - Instructor]
                                    |
        ____________________________________________________________
        |                    |                   |                |
    [Quản lý         [Quản lý nội dung      [Quản lý        [Quản lý lớp học
    Tài khoản]         khóa học]           Đánh giá]         & Sinh viên]
        |                    |                   |                |
    - Đăng nhập       - Tạo khóa học      - Tạo Assignment   - Xem danh sách
    - Cập nhật        - Cập nhật khóa học - Tạo Quiz           Enrollment
      thông tin       - Xuất bản khóa học - Chấm điểm       - Theo dõi tiến độ
    - Đăng xuất       - Xóa khóa học      - Xem báo cáo     - Cấp Certificate
                      - Tạo Module          đánh giá
                      - Tạo Lecture
                      - Upload tài liệu
```

**Giải thích cấu trúc BFD:**
- Level 0: Chức năng tổng quát nhất (Quản lý ứng dụng LMS - Instructor)
- Level 1: 4 nhóm chức năng chính
- Level 2: Các chức năng con chi tiết

**Lưu ý vẽ:**
- Dùng hình chữ nhật cho mỗi chức năng
- Đường kẻ ngang thể hiện phân rã từ trên xuống
- Style giống Tham_khao.pdf page 6, 8, 11, 13

##### 2.1.2. Đặc tả chức năng

**− Quản lý Tài khoản:**
• Mục đích: Cho phép giảng viên đăng nhập và quản lý thông tin cá nhân.
• Ngữ cảnh: Được sử dụng bởi giảng viên trên nền tảng web.
• Điều kiện tiên quyết: Thiết bị đã kết nối Internet.
• Mô tả:
  o Đăng nhập: Giảng viên nhập username/email và password để truy cập hệ thống.
  o Cập nhật thông tin: Cập nhật UserProfile (bio, avatar, preferences).
  o Đăng xuất: Thoát khỏi hệ thống một cách an toàn.
• Kết quả: Giảng viên có thể truy cập các chức năng của hệ thống. Thông tin cá nhân được cập nhật chính xác.

**− Quản lý Nội dung Khóa học:**
• Mục đích: Giúp giảng viên tạo, cập nhật, và tổ chức nội dung khóa học.
• Ngữ cảnh: Giảng viên sử dụng trên nền tảng web để quản lý Course, Module, Lecture.
• Điều kiện tiên quyết: Giảng viên đã đăng nhập thành công vào hệ thống.
• Mô tả:
  o Tạo khóa học: Nhập thông tin Course (title, description, thumbnail). Trạng thái mặc định là 'draft'.
  o Cập nhật khóa học: Chỉnh sửa thông tin Course, thay đổi trạng thái (draft → published → archived).
  o Xuất bản khóa học: Thay đổi status thành 'published' để sinh viên có thể đăng ký.
  o Xóa khóa học: Xóa Course (CASCADE sẽ xóa tất cả Module, Lecture, Assignment liên quan).
  o Tạo Module: Tạo Module thuộc Course, thiết lập order_num và prerequisite_module_ids.
  o Tạo Lecture: Tạo Lecture trong Module, upload video/tài liệu, thiết lập duration.
  o Upload tài liệu: Upload file resources (PDF, video) và lưu URL vào Lecture.resources JSONB.
• Kết quả: Nội dung khóa học được tổ chức rõ ràng theo cấu trúc Course → Module → Lecture. Sinh viên có thể truy cập nội dung khi khóa học được published.

**− Quản lý Đánh giá:**
• Mục đích: Cho phép giảng viên tạo Assignment và Quiz, chấm điểm sinh viên.
• Ngữ cảnh: Sử dụng để đánh giá kết quả học tập của sinh viên.
• Điều kiện tiên quyết: Giảng viên đã tạo Course và Module.
• Mô tả:
  o Tạo Assignment: Tạo Assignment trong Module, thiết lập title, instructions, due_date, max_score.
  o Tạo Quiz: Tạo Quiz với questions (JSONB), thiết lập passing_score, time_limit, max_attempts.
  o Chấm điểm: Xem AssignmentSubmission của sinh viên, nhập score và feedback.
  o Xem báo cáo đánh giá: Xem danh sách Attempt và AssignmentSubmission, thống kê điểm số.
• Kết quả: Sinh viên có Assignment và Quiz để làm. Giảng viên có thể chấm điểm và cung cấp feedback.

**− Quản lý Lớp học & Sinh viên:**
• Mục đích: Theo dõi sinh viên đã đăng ký, xem tiến độ và cấp chứng chỉ.
• Ngữ cảnh: Sử dụng để quản lý Enrollment và Progress của sinh viên.
• Điều kiện tiên quyết: Có sinh viên đã enroll vào khóa học.
• Mô tả:
  o Xem danh sách Enrollment: Xem tất cả sinh viên đã đăng ký khóa học.
  o Theo dõi tiến độ: Xem completion_percentage của từng sinh viên, xem Progress records.
  o Cấp Certificate: Khi sinh viên hoàn thành khóa học (completion_percentage = 100), hệ thống tự động tạo Certificate.
• Kết quả: Giảng viên nắm rõ tình hình học tập của từng sinh viên. Sinh viên nhận được Certificate khi hoàn thành.

---

#### 2.2. Sơ đồ phân rã chức năng dưới góc nhìn của Student (Submission Management)

##### 2.2.1. Sơ đồ phân rã chức năng

**Hướng dẫn vẽ BFD:**

```
                        [Quản lý ứng dụng LMS - Student]
                                    |
        ____________________________________________________________
        |                    |                   |                |
    [Quản lý         [Đăng ký &              [Làm bài tập    [Theo dõi
    Tài khoản]       Học tập]                 & Kiểm tra]     Tiến độ]
        |                    |                   |                |
    - Đăng ký         - Đăng ký khóa học   - Xem Assignment   - Xem completion
    - Đăng nhập       - Xem danh sách      - Nộp bài tập        percentage
    - Cập nhật          Lecture            - Làm Quiz         - Xem điểm số
      thông tin       - Xem video/tài liệu - Xem điểm và      - Tải Certificate
    - Đăng xuất       - Đánh dấu hoàn        feedback
                        thành Lecture
```

##### 2.2.2. Đặc tả chức năng

**− Quản lý Tài khoản:**
• Mục đích: Cho phép sinh viên tạo tài khoản, đăng nhập và quản lý thông tin cá nhân.
• Ngữ cảnh: Sử dụng bởi sinh viên trên nền tảng web/mobile.
• Điều kiện tiên quyết: Thiết bị đã kết nối Internet.
• Mô tả:
  o Đăng ký: Sinh viên cung cấp email, password, full_name để tạo tài khoản. Role mặc định là 'student'.
  o Đăng nhập: Nhập email/username và password để truy cập hệ thống.
  o Cập nhật thông tin: Cập nhật UserProfile, thay đổi avatar, bio, preferences.
  o Đăng xuất: Thoát khỏi hệ thống.
• Kết quả: Sinh viên có tài khoản và có thể truy cập các chức năng học tập.

**− Đăng ký & Học tập:**
• Mục đích: Cho phép sinh viên đăng ký khóa học và học các Lecture.
• Ngữ cảnh: Sinh viên tìm và đăng ký khóa học quan tâm, sau đó học tập.
• Điều kiện tiên quyết: Sinh viên đã đăng nhập. Khóa học có status = 'published'.
• Mô tả:
  o Đăng ký khóa học: Tạo Enrollment record với user_id, course_id. Nếu có Class, thêm class_id.
  o Xem danh sách Lecture: Xem tất cả Lecture trong Course theo Module order.
  o Xem video/tài liệu: Xem Lecture.video_url và Lecture.resources (JSONB).
  o Đánh dấu hoàn thành: Tạo Progress record với percentage = 100 khi hoàn thành Lecture.
• Kết quả: Sinh viên đã enroll và có thể học Lecture. Progress được tracking.

**− Làm bài tập & Kiểm tra:**
• Mục đích: Cho phép sinh viên làm Assignment và Quiz.
• Ngữ cảnh: Sinh viên làm bài tập và kiểm tra để đánh giá kiến thức.
• Điều kiện tiên quyết: Sinh viên đã enroll khóa học.
• Mô tả:
  o Xem Assignment: Xem Assignment.title, instructions, due_date, max_score.
  o Nộp bài tập: Tạo AssignmentSubmission với file_url (upload file), submission_text.
  o Làm Quiz: Tạo Attempt với answers (JSONB). Hệ thống auto-grade multiple choice questions.
  o Xem điểm và feedback: Xem AssignmentSubmission.score, feedback và Attempt.score sau khi giảng viên chấm.
• Kết quả: Bài tập và quiz được nộp thành công. Sinh viên nhận điểm và feedback.

**− Theo dõi Tiến độ:**
• Mục đích: Cho phép sinh viên theo dõi tiến độ học tập của mình.
• Ngữ cảnh: Sinh viên muốn biết đã hoàn thành bao nhiêu % khóa học.
• Điều kiện tiên quyết: Sinh viên đã enroll khóa học.
• Mô tả:
  o Xem completion percentage: Xem Enrollment.completion_percentage.
  o Xem điểm số: Xem danh sách AssignmentSubmission và Attempt với điểm số.
  o Tải Certificate: Khi completion_percentage = 100, sinh viên có thể tải Certificate.
• Kết quả: Sinh viên nắm rõ tiến độ học tập của mình. Nhận được Certificate khi hoàn thành.

---

### 3. Mô hình hóa dữ liệu

#### 3.1. Sơ đồ ER

**Hướng dẫn vẽ ERD:**

**Entities (16 tables):**
1. User (user_id PK, email, password_hash, full_name, role, status)
2. UserProfile (profile_id PK, user_id FK, bio, avatar_url, preferences JSONB)
3. Notification (notification_id PK, user_id FK, type, message, is_read)
4. Course (course_id PK, instructor_id FK, title, description, thumbnail_url, status)
5. Module (module_id PK, course_id FK, title, order_num, prerequisite_module_ids ARRAY)
6. Lecture (lecture_id PK, module_id FK, title, content, video_url, duration_seconds, resources JSONB)
7. Assignment (assignment_id PK, module_id FK, title, instructions, due_date, max_score, config JSONB)
8. Quiz (quiz_id PK, module_id FK, title, questions JSONB, passing_score, time_limit_minutes, max_attempts)
9. Attempt (attempt_id PK, enrollment_id FK, quiz_id FK, answers JSONB, score, status, attempt_number)
10. Enrollment (enrollment_id PK, user_id FK, course_id FK, class_id FK nullable, enrolled_at, completion_percentage, status)
11. Progress (progress_id PK, enrollment_id FK, lecture_id FK, completed_at, percentage)
12. Class (class_id PK, course_id FK, class_name, start_date, end_date, max_students, schedules JSONB)
13. Certificate (certificate_id PK, enrollment_id FK, certificate_number, issued_at, verification_url)
14. AssignmentSubmission (submission_id PK, assignment_id FK, enrollment_id FK, submission_text, file_url, submitted_at, score, feedback, graded_at)

**Relationships:**
- User 1 --- * UserProfile (1-1)
- User 1 --- * Notification (1-N)
- User 1 --- * Course (1-N as instructor)
- Course 1 --- * Module (1-N, CASCADE)
- Module 1 --- * Lecture (1-N, CASCADE)
- Module 1 --- * Assignment (1-N, CASCADE)
- Module 1 --- * Quiz (1-N, CASCADE)
- User 1 --- * Enrollment (1-N, RESTRICT)
- Course 1 --- * Enrollment (1-N, RESTRICT)
- Course 1 --- * Class (1-N)
- Class 1 --- * Enrollment (1-N optional, SET NULL)
- Enrollment 1 --- * Progress (1-N, CASCADE)
- Enrollment 1 --- * Attempt (1-N, CASCADE)
- Enrollment 1 --- 1 Certificate (1-1 optional, RESTRICT)
- Quiz 1 --- * Attempt (1-N)
- Assignment 1 --- * AssignmentSubmission (1-N)
- Enrollment 1 --- * AssignmentSubmission (1-N)
- Lecture 1 --- * Progress (1-N)

**Cách vẽ:**
- Dùng hình chữ nhật cho Entity
- Ghi rõ PK, FK, attributes quan trọng
- Dùng đường kẻ với ký hiệu 1, N, 1..1, 0..N để thể hiện relationships
- Style giống Tham_khao.pdf page 16 (ERD tổng quát, clean, dễ hiểu)

**Lưu ý:**
- Vẽ ERD ở mức tổng quát, không cần chi tiết tất cả attributes
- Nhấn mạnh relationships chính
- Group theo domains nếu cần (User Management, Course Content, Assessment, Enrollment & Progress, Class & Certificate)

---

#### 3.2. Sơ đồ RM (Relational Model)

**Hướng dẫn vẽ RM:**

Vẽ schema với format:
- Tên bảng in đậm (ví dụ: **USER**)
- Liệt kê attributes với kiểu dữ liệu
- Gạch chân PK
- Đánh dấu FK với mũi tên → bảng tham chiếu
- Đánh dấu INT, NN (NOT NULL)

**Style giống Tham_khao.pdf page 17:**

```
USER
- user_id (PK) - INT
- email - varchar(255) - NN
- password_hash - varchar(255) - NN
- full_name - varchar(200) - NN
- role - varchar(20) - NN
- status - varchar(20) - NN
- created_at - datetime - NN

USERPROFILE
- profile_id (PK) - INT
- user_id (FK → USER) - INT - NN
- bio - text
- avatar_url - varchar(500)
- preferences - JSONB
- updated_at - datetime

COURSE
- course_id (PK) - INT
- instructor_id (FK → USER) - INT - NN
- title - varchar(200) - NN
- description - text
- thumbnail_url - varchar(500)
- status - varchar(20) - NN
- created_at - datetime - NN
- published_at - datetime

MODULE
- module_id (PK) - INT
- course_id (FK → COURSE) - INT - NN
- title - varchar(200) - NN
- order_num - INT - NN
- prerequisite_module_ids - UUID[] - NN
- created_at - datetime - NN

... (tiếp tục cho 16 tables)
```

**Lưu ý:**
- Vẽ tất cả 16 tables
- Rõ ràng về kiểu dữ liệu (UUID, varchar, text, JSONB, array, decimal, datetime)
- Đánh dấu FK relationships với mũi tên
- Group theo domains để dễ nhìn

---

#### 3.3. Đặc tả chi tiết cơ sở dữ liệu

**Format theo Tham_khao.pdf pages 18-20:**

##### 3.3.1. Quản lý tài khoản và người dùng

**− Bảng: User**
• user_id: PK, UUID (gen_random_uuid()), Khóa chính.
• email: VARCHAR(255), UNIQUE NOT NULL, Email đăng nhập.
• password_hash: VARCHAR(255), NOT NULL, Mật khẩu đã mã hóa (bcrypt).
• full_name: VARCHAR(200), NOT NULL, Họ tên người dùng.
• role: VARCHAR(20), NOT NULL, CHECK (role IN ('student', 'instructor', 'admin')), Vai trò.
• status: VARCHAR(20), NOT NULL, DEFAULT 'active', CHECK (status IN ('active', 'inactive', 'suspended')), Trạng thái tài khoản.
• created_at: TIMESTAMPTZ, NOT NULL, DEFAULT CURRENT_TIMESTAMP, Thời gian tạo tài khoản.
• updated_at: TIMESTAMPTZ, DEFAULT CURRENT_TIMESTAMP, Thời gian cập nhật cuối.

**− Bảng: UserProfile**
• profile_id: PK, UUID, Khóa chính.
• user_id: FK (→ User), UUID, NOT NULL, UNIQUE, Liên kết 1-1 với User.
• bio: TEXT, Tiểu sử (Tùy chọn).
• avatar_url: VARCHAR(500), URL hình đại diện (Tùy chọn).
• preferences: JSONB, Cấu hình cá nhân (theme, language, notification settings) (Tùy chọn).
• updated_at: TIMESTAMPTZ, DEFAULT CURRENT_TIMESTAMP, Thời gian cập nhật.

**− Bảng: Notification**
• notification_id: PK, UUID, Khóa chính.
• user_id: FK (→ User), UUID, NOT NULL, Người nhận thông báo.
• type: VARCHAR(50), NOT NULL, Loại thông báo (assignment_due, quiz_available, course_published).
• message: TEXT, NOT NULL, Nội dung thông báo.
• is_read: BOOLEAN, NOT NULL, DEFAULT false, Trạng thái đã đọc.
• created_at: TIMESTAMPTZ, NOT NULL, DEFAULT CURRENT_TIMESTAMP, Thời gian tạo.

##### 3.3.2. Quản lý nội dung khóa học

**− Bảng: Course**
• course_id: PK, UUID, Khóa chính.
• instructor_id: FK (→ User), UUID, NOT NULL, Giảng viên sở hữu.
• title: VARCHAR(200), NOT NULL, Tên khóa học.
• description: TEXT, Mô tả chi tiết (Tùy chọn).
• thumbnail_url: VARCHAR(500), URL hình đại diện (Tùy chọn).
• status: VARCHAR(20), NOT NULL, DEFAULT 'draft', CHECK (status IN ('draft', 'published', 'archived')), Trạng thái khóa học.
• created_at: TIMESTAMPTZ, NOT NULL, DEFAULT CURRENT_TIMESTAMP.
• published_at: TIMESTAMPTZ, Thời gian xuất bản (Chỉ có khi status = 'published').
• updated_at: TIMESTAMPTZ, DEFAULT CURRENT_TIMESTAMP.

**− Bảng: Module**
• module_id: PK, UUID, Khóa chính.
• course_id: FK (→ Course), UUID, NOT NULL, ON DELETE CASCADE, Khóa học chứa module.
• title: VARCHAR(200), NOT NULL, Tên module.
• order_num: INT, NOT NULL, Thứ tự hiển thị.
• prerequisite_module_ids: UUID[], NOT NULL DEFAULT '{}', Danh sách module tiên quyết.
• created_at: TIMESTAMPTZ, NOT NULL, DEFAULT CURRENT_TIMESTAMP.
• updated_at: TIMESTAMPTZ, DEFAULT CURRENT_TIMESTAMP.

**− Bảng: Lecture**
• lecture_id: PK, UUID, Khóa chính.
• module_id: FK (→ Module), UUID, NOT NULL, ON DELETE CASCADE, Module chứa lecture.
• title: VARCHAR(200), NOT NULL, Tiêu đề bài giảng.
• content: TEXT, Nội dung text/HTML (Tùy chọn).
• video_url: VARCHAR(500), URL video (Tùy chọn).
• duration_seconds: INT, Độ dài video (giây) (Tùy chọn).
• resources: JSONB, Tài liệu đính kèm (PDF, links) dạng array JSON (Tùy chọn).
• order_num: INT, NOT NULL, Thứ tự trong Module.
• created_at: TIMESTAMPTZ, NOT NULL, DEFAULT CURRENT_TIMESTAMP.
• updated_at: TIMESTAMPTZ, DEFAULT CURRENT_TIMESTAMP.

##### 3.3.3. Quản lý đánh giá

**− Bảng: Assignment**
• assignment_id: PK, UUID, Khóa chính.
• module_id: FK (→ Module), UUID, NOT NULL, ON DELETE CASCADE, Module chứa assignment.
• title: VARCHAR(200), NOT NULL, Tiêu đề bài tập.
• instructions: TEXT, Hướng dẫn làm bài (Tùy chọn).
• due_date: TIMESTAMPTZ, Hạn nộp (Tùy chọn).
• max_score: DECIMAL(5,2), NOT NULL, Điểm tối đa.
• config: JSONB, Cấu hình bổ sung (allowed_file_types, max_file_size) (Tùy chọn).
• created_at: TIMESTAMPTZ, NOT NULL, DEFAULT CURRENT_TIMESTAMP.
• updated_at: TIMESTAMPTZ, DEFAULT CURRENT_TIMESTAMP.

**− Bảng: Quiz**
• quiz_id: PK, UUID, Khóa chính.
• module_id: FK (→ Module), UUID, NOT NULL, ON DELETE CASCADE, Module chứa quiz.
• title: VARCHAR(200), NOT NULL, Tiêu đề quiz.
• questions: JSONB, NOT NULL, Danh sách câu hỏi dạng JSON array với structure: [{id, type, question_text, points, options, correct_answer, explanation}].
• passing_score: DECIMAL(5,2), NOT NULL, Điểm đạt (0-100).
• time_limit_minutes: INT, Thời gian làm bài (phút) (Tùy chọn, NULL = không giới hạn).
• max_attempts: INT, Số lần làm tối đa (Tùy chọn, NULL = không giới hạn).
• available_from: TIMESTAMPTZ, Thời gian mở (Tùy chọn).
• available_until: TIMESTAMPTZ, Thời gian đóng (Tùy chọn).
• status: VARCHAR(20), NOT NULL, DEFAULT 'draft', CHECK (status IN ('draft', 'published', 'archived')).
• created_at: TIMESTAMPTZ, NOT NULL, DEFAULT CURRENT_TIMESTAMP.
• updated_at: TIMESTAMPTZ, DEFAULT CURRENT_TIMESTAMP.

**− Bảng: Attempt**
• attempt_id: PK, UUID, Khóa chính.
• enrollment_id: FK (→ Enrollment), UUID, NOT NULL, ON DELETE CASCADE, Sinh viên làm quiz.
• quiz_id: FK (→ Quiz), UUID, NOT NULL, Quiz được làm.
• answers: JSONB, NOT NULL, Câu trả lời của sinh viên dạng JSON array: [{question_id, answer, is_correct, points_earned, time_spent_seconds, requires_grading}].
• score: DECIMAL(5,2), Điểm số (0-100) (Tùy chọn, NULL nếu chưa chấm xong).
• status: VARCHAR(20), NOT NULL, DEFAULT 'in_progress', CHECK (status IN ('in_progress', 'submitted', 'graded', 'requires_grading')).
• attempt_number: INT, NOT NULL, Lần thử thứ mấy (1, 2, 3...).
• started_at: TIMESTAMPTZ, NOT NULL, DEFAULT CURRENT_TIMESTAMP, Thời gian bắt đầu.
• submitted_at: TIMESTAMPTZ, Thời gian nộp (Tùy chọn).
• graded_at: TIMESTAMPTZ, Thời gian chấm (Tùy chọn).
• UNIQUE(enrollment_id, quiz_id, attempt_number), Đảm bảo không trùng lần thử.

**− Bảng: AssignmentSubmission**
• submission_id: PK, UUID, Khóa chính.
• assignment_id: FK (→ Assignment), UUID, NOT NULL, Bài tập được nộp.
• enrollment_id: FK (→ Enrollment), UUID, NOT NULL, Sinh viên nộp bài.
• submission_text: TEXT, Nội dung text (Tùy chọn).
• file_url: VARCHAR(500), URL file đã upload (Tùy chọn).
• submitted_at: TIMESTAMPTZ, NOT NULL, DEFAULT CURRENT_TIMESTAMP, Thời gian nộp.
• score: DECIMAL(5,2), Điểm số (Tùy chọn, NULL nếu chưa chấm).
• feedback: TEXT, Phản hồi của giảng viên (Tùy chọn).
• graded_at: TIMESTAMPTZ, Thời gian chấm (Tùy chọn).
• status: VARCHAR(20), NOT NULL, DEFAULT 'submitted', CHECK (status IN ('submitted', 'graded', 'late')).

##### 3.3.4. Quản lý đăng ký và tiến độ

**− Bảng: Enrollment**
• enrollment_id: PK, UUID, Khóa chính.
• user_id: FK (→ User), UUID, NOT NULL, ON DELETE RESTRICT, Sinh viên đăng ký.
• course_id: FK (→ Course), UUID, NOT NULL, ON DELETE RESTRICT, Khóa học được đăng ký.
• class_id: FK (→ Class), UUID, ON DELETE SET NULL, Lớp học (Tùy chọn, NULL = self-paced).
• enrolled_at: TIMESTAMPTZ, NOT NULL, DEFAULT CURRENT_TIMESTAMP, Thời gian đăng ký.
• completion_percentage: DECIMAL(5,2), NOT NULL, DEFAULT 0, CHECK (completion_percentage >= 0 AND completion_percentage <= 100), Tỷ lệ hoàn thành (%).
• status: VARCHAR(20), NOT NULL, DEFAULT 'active', CHECK (status IN ('active', 'completed', 'dropped')), Trạng thái enrollment.
• completed_at: TIMESTAMPTZ, Thời gian hoàn thành (Khi completion_percentage = 100).
• UNIQUE(user_id, course_id), Một sinh viên chỉ enroll một khóa học một lần.

**− Bảng: Progress**
• progress_id: PK, UUID, Khóa chính.
• enrollment_id: FK (→ Enrollment), UUID, NOT NULL, ON DELETE CASCADE, Enrollment liên quan.
• lecture_id: FK (→ Lecture), UUID, NOT NULL, Lecture đã học.
• completed_at: TIMESTAMPTZ, NOT NULL, DEFAULT CURRENT_TIMESTAMP, Thời gian hoàn thành.
• percentage: INT, NOT NULL, DEFAULT 100, CHECK (percentage >= 0 AND percentage <= 100), Tỷ lệ hoàn thành lecture (thường là 100).
• UNIQUE(enrollment_id, lecture_id), Một lecture chỉ được đánh dấu hoàn thành một lần.

##### 3.3.5. Quản lý lớp học và chứng chỉ

**− Bảng: Class**
• class_id: PK, UUID, Khóa chính.
• course_id: FK (→ Course), UUID, NOT NULL, Khóa học tổ chức lớp.
• class_name: VARCHAR(100), NOT NULL, Tên lớp (VD: "CNTT1-K63").
• start_date: DATE, NOT NULL, Ngày bắt đầu lớp.
• end_date: DATE, NOT NULL, Ngày kết thúc lớp, CHECK (end_date >= start_date).
• max_students: INT, Số sinh viên tối đa (Tùy chọn, NULL = không giới hạn).
• schedules: JSONB, Lịch học dạng JSON array: [{day_of_week, start_time, end_time, room}] (Tùy chọn).
• created_at: TIMESTAMPTZ, NOT NULL, DEFAULT CURRENT_TIMESTAMP.

**− Bảng: Certificate**
• certificate_id: PK, UUID, Khóa chính.
• enrollment_id: FK (→ Enrollment), UUID, NOT NULL, UNIQUE, ON DELETE RESTRICT, Enrollment nhận certificate (1-1).
• certificate_number: VARCHAR(50), NOT NULL, UNIQUE, Mã chứng chỉ.
• issued_at: TIMESTAMPTZ, NOT NULL, DEFAULT CURRENT_TIMESTAMP, Thời gian cấp.
• verification_url: VARCHAR(500), URL xác minh chứng chỉ (Tùy chọn).
• status: VARCHAR(20), NOT NULL, DEFAULT 'active', CHECK (status IN ('active', 'revoked')).
• revoked_at: TIMESTAMPTZ, Thời gian thu hồi (Tùy chọn).
• revocation_reason: TEXT, Lý do thu hồi (Tùy chọn).

---

## CHƯƠNG 2: ĐẶC TẢ YÊU CẦU CHỨC NĂNG

**Lưu ý:** Tập trung vào 2 chức năng chính:
1. Course Management (Instructor tạo/cập nhật khóa học)
2. Submission Management (Student nộp bài tập)

### 1. Đặc tả chức năng: Quản lý khóa học (Course Management)

#### 1.1. Giao diện Form và thành phần

**− Tên Form:** Form Quản lý Khóa học

**− Canvas chính (Chi tiết khóa học):** Chứa các trường nhập/chỉnh sửa thông tin Course và Module.

**− Canvas phụ (Danh sách Module):** Hiển thị danh sách Module của khóa học, cho phép thêm/sửa/xóa Module.

**− Windows:** Pop-up thông báo lỗi/thành công khi lưu dữ liệu.

| Thành phần | Loại điều khiển | Trường dữ liệu | Bảng và kiểu dữ liệu | Thao tác nhập/hiển thị |
|------------|-----------------|----------------|----------------------|------------------------|
| ID Khóa học | Text (Readonly) | course_id | Course (UUID) | Hiển thị mã khóa học (UUID). |
| Tên khóa học | Text Input | title | Course (VARCHAR(200)) | Nhập/Sửa tên khóa học. |
| Mô tả | Text Area | description | Course (TEXT) | Nhập/Sửa mô tả chi tiết (Tùy chọn). |
| Hình đại diện | File Upload | thumbnail_url | Course (VARCHAR(500)) | Nhập file ảnh. Hệ thống upload và lưu URL. |
| Trạng thái | Dropdown | status | Course (VARCHAR(20)) | Chọn 'draft', 'published', hoặc 'archived'. |
| Giảng viên | Text (Readonly) | instructor_id | Course (UUID) | Hiển thị tên giảng viên (lấy từ User table). |
| Danh sách Module | Data Grid | N/A | Module | Hiển thị danh sách Module với các cột: title, order_num. Cho phép thêm/sửa/xóa Module. |
| Nút Lưu | Button | N/A | N/A | Kích hoạt truy vấn UPDATE/INSERT. |
| Nút Xuất bản | Button | N/A | N/A | Thay đổi status thành 'published' và set published_at = CURRENT_TIMESTAMP. |

#### 1.2. Truy vấn dữ liệu

| Loại truy vấn | Bảng | Trường | Hiển thị dữ liệu |
|---------------|------|--------|------------------|
| Hiển thị chính | Course | course_id, title, description, thumbnail_url, status, instructor_id | Hiển thị dữ liệu hiện tại của khóa học để Instructor chỉnh sửa. |
| Hiển thị Module | Module | module_id, course_id, title, order_num, prerequisite_module_ids | Hiển thị danh sách Module của khóa học. |
| Thông tin Instructor | User | user_id, full_name | Lấy tên giảng viên để hiển thị. |

#### 1.3. Cập nhật dữ liệu

| Bảng cập nhật | Lệnh SQL | Điều kiện cập nhật |
|---------------|----------|-------------------|
| Course | UPDATE "Course" SET title = [...], description = [...], thumbnail_url = [...], status = [...], updated_at = CURRENT_TIMESTAMP WHERE course_id = [ID Khóa học] | WHERE course_id = [ID Khóa học] AND instructor_id = [ID Giảng viên hiện tại] |
| Course (Xuất bản) | UPDATE "Course" SET status = 'published', published_at = CURRENT_TIMESTAMP, updated_at = CURRENT_TIMESTAMP WHERE course_id = [ID Khóa học] | WHERE course_id = [ID Khóa học] AND instructor_id = [ID Giảng viên hiện tại] AND status = 'draft' |
| Module (Thêm mới) | INSERT INTO "Module" (module_id, course_id, title, order_num, prerequisite_module_ids) VALUES (gen_random_uuid(), [course_id], [title], [order_num], [prerequisite_module_ids]) | N/A |
| Module (Cập nhật) | UPDATE "Module" SET title = [...], order_num = [...], prerequisite_module_ids = [...], updated_at = CURRENT_TIMESTAMP WHERE module_id = [ID Module] | WHERE module_id = [ID Module] AND course_id = [ID Khóa học] |
| Module (Xóa) | DELETE FROM "Module" WHERE module_id = [ID Module] | WHERE module_id = [ID Module] AND course_id = [ID Khóa học] (CASCADE sẽ xóa Lecture, Assignment, Quiz liên quan) |

#### 1.4. Tiền và hậu điều kiện

**− Tiền điều kiện:**
• Người dùng phải đăng nhập với vai trò 'instructor'.
• course_id của khóa học cần chỉnh sửa phải tồn tại trong bảng Course.
• Khóa học đó phải thuộc về giảng viên này (kiểm tra Course.instructor_id = User.user_id).
• Khi xuất bản, Course.status phải là 'draft' (không thể xuất bản khóa học đã published hoặc archived).

**− Hậu điều kiện:**
• **Nếu thành công:**
  - Các trường dữ liệu trong bảng Course được cập nhật.
  - Nếu xuất bản, status = 'published' và published_at = thời gian hiện tại.
  - Module được thêm/sửa/xóa theo yêu cầu.
  - Hiển thị thông báo: "Khóa học đã được cập nhật thành công."
• **Nếu thất bại:**
  - Course không thay đổi.
  - Hiển thị thông báo lỗi (ví dụ: "Bạn không có quyền chỉnh sửa khóa học này" hoặc "Lỗi kết nối cơ sở dữ liệu").

**− Ràng buộc nghiệp vụ:**
• Không thể xóa Course nếu đã có Enrollment (ON DELETE RESTRICT).
• Khi xóa Module, tất cả Lecture, Assignment, Quiz thuộc Module đó cũng bị xóa (CASCADE).
• Prerequisite_module_ids phải chứa các module_id hợp lệ thuộc cùng course_id.

---

### 2. Đặc tả chức năng: Nộp bài tập (Submission Management)

#### 2.1. Giao diện Form và Thành phần

**− Tên Form:** Form Nộp bài tập

**− Canvas 1 (Chi tiết bài tập):** Hiển thị thông tin Assignment (title, instructions, due_date, max_score).

**− Canvas 2 (Nộp bài):** Cho phép sinh viên nhập submission_text và upload file.

**− Windows:** Pop-up xác nhận nộp bài/Lỗi upload file.

| Thành phần | Loại điều khiển | Trường dữ liệu | Bảng và kiểu dữ liệu | Thao tác nhập/hiển thị |
|------------|-----------------|----------------|----------------------|------------------------|
| Tên bài tập | Text (Readonly) | title | Assignment (VARCHAR(200)) | Hiển thị tên bài tập. |
| Hướng dẫn | Text Area (Readonly) | instructions | Assignment (TEXT) | Hiển thị hướng dẫn làm bài. |
| Hạn nộp | Text (Readonly) | due_date | Assignment (TIMESTAMPTZ) | Hiển thị deadline (format: DD/MM/YYYY HH:MM). |
| Điểm tối đa | Text (Readonly) | max_score | Assignment (DECIMAL(5,2)) | Hiển thị điểm tối đa. |
| Nội dung bài làm | Text Area | submission_text | AssignmentSubmission (TEXT) | Nhập nội dung text (Tùy chọn). |
| File đính kèm | File Upload | file_url | AssignmentSubmission (VARCHAR(500)) | Nhập file. Hệ thống upload và lưu URL. |
| Trạng thái nộp | Text (Readonly) | status | AssignmentSubmission (VARCHAR(20)) | Hiển thị 'submitted', 'graded', hoặc 'late'. |
| Điểm số | Text (Readonly) | score | AssignmentSubmission (DECIMAL(5,2)) | Hiển thị điểm (nếu đã chấm). |
| Phản hồi | Text Area (Readonly) | feedback | AssignmentSubmission (TEXT) | Hiển thị feedback của giảng viên (nếu có). |
| Nút Nộp bài | Button | N/A | N/A | Kích hoạt truy vấn INSERT vào AssignmentSubmission. |

#### 2.2. Truy vấn dữ liệu

| Loại truy vấn | Bảng | Trường | Hiển thị dữ liệu |
|---------------|------|--------|------------------|
| Thông tin Assignment | Assignment | assignment_id, title, instructions, due_date, max_score, module_id | Lấy thông tin bài tập để hiển thị. |
| Kiểm tra Enrollment | Enrollment | enrollment_id, user_id, course_id | Xác nhận sinh viên đã enroll khóa học chứa Assignment này (JOIN: Assignment → Module → Course → Enrollment). |
| Submission hiện tại | AssignmentSubmission | submission_id, submission_text, file_url, submitted_at, score, feedback, status | Kiểm tra xem sinh viên đã nộp bài chưa. Nếu có, hiển thị thông tin submission. |

#### 2.3. Cập nhật dữ liệu

| Bảng cập nhật | Lệnh SQL | Mục đích |
|---------------|----------|----------|
| AssignmentSubmission (Nộp lần đầu) | INSERT INTO "AssignmentSubmission" (submission_id, assignment_id, enrollment_id, submission_text, file_url, submitted_at, status) VALUES (gen_random_uuid(), [assignment_id], [enrollment_id], [submission_text], [file_url], CURRENT_TIMESTAMP, 'submitted') | Tạo submission mới khi sinh viên nộp bài lần đầu. |
| AssignmentSubmission (Nộp lại) | UPDATE "AssignmentSubmission" SET submission_text = [...], file_url = [...], submitted_at = CURRENT_TIMESTAMP, status = CASE WHEN CURRENT_TIMESTAMP > (SELECT due_date FROM "Assignment" WHERE assignment_id = [...]) THEN 'late' ELSE 'submitted' END WHERE submission_id = [ID Submission] | Cập nhật submission nếu sinh viên nộp lại (trước khi giảng viên chấm). Kiểm tra late submission. |

**Logic xác định status:**
```sql
-- Nếu submitted_at > due_date thì status = 'late'
-- Ngược lại status = 'submitted'
-- Sau khi giảng viên chấm, status = 'graded'
```

#### 2.4. Tiền và hậu điều kiện

**− Tiền điều kiện:**
• Người dùng phải đăng nhập với vai trò 'student'.
• assignment_id phải tồn tại trong bảng Assignment.
• Sinh viên phải đã enroll khóa học chứa Assignment này:
  - Kiểm tra: Enrollment.user_id = [user_id hiện tại] AND Enrollment.course_id = (SELECT Course.course_id FROM Assignment JOIN Module ON Assignment.module_id = Module.module_id JOIN Course ON Module.course_id = Course.course_id WHERE Assignment.assignment_id = [assignment_id])
  - Enrollment.status = 'active'.
• Nếu đã nộp bài trước đó, chỉ cho phép nộp lại nếu giảng viên chưa chấm (AssignmentSubmission.score IS NULL).
• File upload (nếu có) phải đúng định dạng cho phép (kiểm tra từ Assignment.config JSONB: allowed_file_types).

**− Hậu điều kiện:**
• **Nếu Transaction thành công:**
  - Bảng AssignmentSubmission được INSERT (lần đầu) hoặc UPDATE (nộp lại) dữ liệu.
  - Status được set:
    - 'submitted' nếu nộp đúng hạn (submitted_at <= due_date).
    - 'late' nếu nộp trễ (submitted_at > due_date).
  - File được upload lên storage (S3/local) và file_url được lưu.
  - Hiển thị thông báo: "Bài tập đã được nộp thành công."
  - Gửi Notification cho giảng viên về submission mới.
• **Nếu Transaction thất bại:**
  - Không có dữ liệu nào được chèn/cập nhật vào CSDL (Rollback).
  - File không được lưu.
  - Hiển thị thông báo lỗi:
    - "Bạn chưa đăng ký khóa học này."
    - "File upload không hợp lệ. Vui lòng chọn file đúng định dạng."
    - "Bài tập đã được chấm điểm, không thể nộp lại."
    - "Lỗi hệ thống. Vui lòng thử lại sau."

**− Ràng buộc nghiệp vụ:**
• Một sinh viên chỉ có một AssignmentSubmission cho mỗi Assignment (UNIQUE constraint hoặc check trước khi INSERT).
• Không thể xóa AssignmentSubmission sau khi đã nộp (chỉ giảng viên mới có quyền xóa nếu cần).
• Khi giảng viên chấm điểm (UPDATE score, feedback), status tự động chuyển thành 'graded' và graded_at = CURRENT_TIMESTAMP.

**− Validation (Application Layer):**
```python
# Pseudo-code validation
def validate_submission(assignment_id, enrollment_id, file, submission_text):
    # 1. Check enrollment
    enrollment = db.query("SELECT * FROM Enrollment WHERE enrollment_id = ? AND status = 'active'", enrollment_id)
    if not enrollment:
        raise ValidationError("Bạn chưa đăng ký khóa học này.")

    # 2. Check assignment exists
    assignment = db.query("SELECT * FROM Assignment WHERE assignment_id = ?", assignment_id)
    if not assignment:
        raise ValidationError("Bài tập không tồn tại.")

    # 3. Check if already submitted and graded
    existing_submission = db.query("SELECT * FROM AssignmentSubmission WHERE assignment_id = ? AND enrollment_id = ?", assignment_id, enrollment_id)
    if existing_submission and existing_submission.score is not None:
        raise ValidationError("Bài tập đã được chấm điểm, không thể nộp lại.")

    # 4. Validate file if uploaded
    if file:
        config = assignment.config  # JSONB
        allowed_types = config.get('allowed_file_types', ['pdf', 'doc', 'docx'])
        max_size = config.get('max_file_size', 10 * 1024 * 1024)  # 10MB default

        file_ext = file.filename.split('.')[-1].lower()
        if file_ext not in allowed_types:
            raise ValidationError(f"File không đúng định dạng. Chỉ chấp nhận: {', '.join(allowed_types)}")

        if file.size > max_size:
            raise ValidationError(f"File quá lớn. Kích thước tối đa: {max_size / 1024 / 1024}MB")

    # 5. Check deadline (warning only, không block)
    if datetime.now() > assignment.due_date:
        # Still allow submission but mark as 'late'
        return {'warning': 'Bạn đang nộp trễ. Bài tập sẽ được đánh dấu là late submission.'}

    return {'valid': True}
```

---

## YÊU CẦU ĐẶC BIỆT VỀ FORMAT

### 1. Format văn bản
- Sử dụng Markdown thuần túy
- Heading rõ ràng: # CHƯƠNG 1, ## 1. Giới thiệu, ### 1.1. Mục đích
- Bullet points với dấu •, o cho sub-levels
- Tables với border rõ ràng (|---|---|---|)
- Code blocks với ``` cho SQL examples

### 2. Hướng dẫn vẽ BFD
- Mô tả bằng ASCII art hoặc cấu trúc text
- Giải thích rõ ràng relationships
- Ghi chú cách vẽ: "Vẽ hình chữ nhật, kẻ đường nối..."
- Có thể copy description để vẽ lại trong tool khác

### 3. Đặc tả CSDL
- Format bullet points rõ ràng
- Ghi chú đầy đủ: PK, FK, NOT NULL, DEFAULT, CHECK constraints
- Giải thích ý nghĩa của từng field
- Ghi rõ ON DELETE behaviors

### 4. Đặc tả chức năng
- Tables cho Form components (như Tham_khao.pdf)
- Tables cho truy vấn dữ liệu
- SQL examples trong code blocks
- Tiền/Hậu điều kiện format bullet points

### 5. Cấu trúc file output
```
/mnt/e/person/2025-Design-Pattern/98-B-Learing-Core/reports/MAIN_REPORT_V2.md
```

---

## CHECKLIST HOÀN THÀNH

Khi viết MAIN_REPORT_V2.md, đảm bảo:

- [ ] Trang bìa đầy đủ thông tin
- [ ] Lời nói đầu 2-3 đoạn văn
- [ ] Mục lục chi tiết với số trang
- [ ] CHƯƠNG 1: Giới thiệu chung (Mục đích, Phạm vi)
- [ ] CHƯƠNG 1: BFD cho 2 actors (Instructor, Student)
- [ ] CHƯƠNG 1: Đặc tả BFD cho từng chức năng
- [ ] CHƯƠNG 1: Sơ đồ ER với hướng dẫn vẽ
- [ ] CHƯƠNG 1: Sơ đồ RM đầy đủ 16 tables
- [ ] CHƯƠNG 1: Đặc tả chi tiết CSDL (16 tables, đầy đủ constraints)
- [ ] CHƯƠNG 2: Đặc tả Course Management (4 sections: Form, Truy vấn, Cập nhật, Tiền/Hậu)
- [ ] CHƯƠNG 2: Đặc tả Submission Management (4 sections: Form, Truy vấn, Cập nhật, Tiền/Hậu)
- [ ] Format text dễ copy vào Word
- [ ] Tables rõ ràng, border đầy đủ
- [ ] SQL examples trong code blocks
- [ ] Validation logic và business rules đầy đủ

---

## GHI CHÚ QUAN TRỌNG

1. **Tuyệt đối tuân thủ format của Tham_khao.pdf**
2. **Dạng text thuần túy, KHÔNG dùng HTML/CSS**
3. **Tables phải có border rõ ràng (|---|---|---)**
4. **BFD phải mô tả chi tiết cách vẽ**
5. **Đặc tả CSDL phải đầy đủ 16 tables với tất cả constraints**
6. **CHƯƠNG 2 chỉ tập trung 2 chức năng: Course Management và Submission Management**
7. **Tiền/Hậu điều kiện phải chi tiết, bao gồm validation rules**
8. **Mọi thứ có thể copy trực tiếp vào Word mà không cần chỉnh sửa**

---

**END OF REQUIREMENTS**
