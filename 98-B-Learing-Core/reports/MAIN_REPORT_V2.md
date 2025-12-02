# BÁO CÁO KẾT QUẢ BÀI TẬP LỚN HỌC PHẦN: ĐẶC TẢ PHẦN MỀM

---

## TRANG BÌA

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
Ngày xem xét: ……/……./.………. ................
```

---

## LỜI NÓI ĐẦU

Trong bối cảnh chuyển đổi số diễn ra mạnh mẽ, giáo dục trực tuyến đã trở thành xu hướng tất yếu của ngành giáo dục hiện đại. Đặc biệt sau đại dịch COVID-19, nhu cầu về các hệ thống quản lý học tập (Learning Management System - LMS) toàn diện, hiệu quả và dễ sử dụng ngày càng tăng cao. Các cơ sở giáo dục, từ trường phổ thông đến đại học và các tổ chức đào tạo doanh nghiệp, đều cần một nền tảng kỹ thuật số để tổ chức, phân phối và theo dõi các hoạt động dạy và học.

Hệ thống B-Learning Core được xây dựng nhằm đáp ứng nhu cầu quản lý học tập trực tuyến với mô hình kết hợp (Blended Learning). Hệ thống tập trung vào hai nhóm người dùng chính: Instructor (Giảng viên) với chức năng Course Management - quản lý khóa học, tạo nội dung bài giảng, thiết lập bài tập và chấm điểm; và Student (Sinh viên) với chức năng Submission Management - đăng ký khóa học, học tập, nộp bài và theo dõi tiến độ. Việc giới hạn phạm vi vào hai chức năng cốt lõi này giúp đảm bảo tính khả thi trong triển khai và tập trung nguồn lực phát triển vào những tính năng thiết yếu nhất.

Báo cáo này trình bày chi tiết đặc tả yêu cầu phần mềm cho hệ thống B-Learning Core, bao gồm: phân tích chức năng thông qua sơ đồ phân rã chức năng (BFD), mô hình hóa dữ liệu với sơ đồ ER và Relational Model, đặc tả chi tiết cơ sở dữ liệu PostgreSQL 14+, và đặc tả yêu cầu chức năng cho hai module chính. Mục tiêu cuối cùng là xây dựng một sản phẩm phần mềm chất lượng, dễ sử dụng, đáp ứng đầy đủ các yêu cầu nghiệp vụ của một hệ thống LMS hiện đại.

---

## MỤC LỤC

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

# CHƯƠNG 1: ĐẶC TẢ YÊU CẦU PHẦN MỀM

## 1. Giới thiệu chung

### 1.1. Mục đích

Hệ thống B-Learning Core là một nền tảng quản lý học tập trực tuyến (Learning Management System - LMS) được thiết kế để hỗ trợ mô hình học tập kết hợp (Blended Learning). Mục đích chính của hệ thống là cung cấp một giải pháp toàn diện cho việc tổ chức, quản lý và theo dõi các hoạt động dạy và học trong môi trường giáo dục hiện đại.

Hệ thống được xây dựng với các mục tiêu cụ thể sau:

**− Đối với Instructor (Giảng viên):**
• Cung cấp công cụ tạo và quản lý khóa học với cấu trúc phân cấp: Course → Module → Lecture.
• Hỗ trợ tạo các loại nội dung đa dạng: video, PDF, slide, audio, text và assignment.
• Cho phép tạo Quiz với câu hỏi đa dạng: MCQ (Multiple Choice Question), True/False, Essay, Short Answer.
• Cung cấp chức năng chấm điểm Assignment và Quiz, gửi feedback cho sinh viên.
• Theo dõi tiến độ học tập của từng sinh viên trong khóa học.

**− Đối với Student (Sinh viên):**
• Đăng ký và truy cập các khóa học đã xuất bản.
• Học tập theo cấu trúc Module, xem video và tài liệu bài giảng.
• Nộp bài Assignment qua upload file hoặc nhập text.
• Làm Quiz trực tuyến và nhận kết quả tự động (cho MCQ/True-False).
• Theo dõi tiến độ học tập và nhận Certificate khi hoàn thành khóa học.

**− Yêu cầu phi chức năng:**
• Bảo mật: Mã hóa password với bcrypt, xác thực qua token, phân quyền theo role.
• Hiệu năng: Tối ưu truy vấn với indexes, hỗ trợ GIN index cho JSON queries.
• Độ tin cậy: Backup dữ liệu định kỳ, transaction support với PostgreSQL.
• Khả năng mở rộng: Sử dụng UUID làm primary key, JSON fields linh hoạt.

**− Công nghệ sử dụng:**
• Database: PostgreSQL 14+ với hỗ trợ JSONB, UUID, Full-text search.
• Backend: RESTful API (Python/FastAPI hoặc Node.js/Express).
• Frontend: Web-based platform (React/Next.js).
• Storage: S3/GCS cho file upload.

---

### 1.2. Phạm vi

Phần mềm này là một hệ thống quản lý học tập trực tuyến (LMS), bao gồm ứng dụng web cho sinh viên và nền tảng web quản trị cho giảng viên. Hệ thống được thiết kế với kiến trúc 5 domain, 16 bảng dữ liệu, hỗ trợ cả mô hình học tự điều khiển (self-paced) và học theo lớp (blended learning).

Phạm vi của ứng dụng tập trung vào hai bên liên quan chính:

**− Instructor (Giảng viên):**

• **Quản lý tài khoản:**
  o Đăng nhập với email và password.
  o Cập nhật thông tin cá nhân (UserProfile), preferences (JSON).
  o Đăng xuất an toàn.

• **Quản lý nội dung khóa học:**
  o Tạo khóa học (Course) với các trường: code, title, description, difficulty_level.
  o Thiết lập trạng thái khóa học: DRAFT → PUBLISHED → ARCHIVED.
  o Tạo Module với thứ tự (order_num) và module tiên quyết (prerequisite_module_ids).
  o Tạo Lecture với các loại: VIDEO, PDF, SLIDE, AUDIO, TEXT, ASSIGNMENT.
  o Upload Resource (file đính kèm) cho Lecture.

• **Quản lý đánh giá:**
  o Tạo Quiz với questions (JSON), passing_score, duration_minutes, max_attempts.
  o Tạo Question bank với các loại: MCQ, TRUE_FALSE, ESSAY, SHORT_ANSWER.
  o Tạo Option (đáp án) cho câu hỏi MCQ/TRUE_FALSE.
  o Chấm điểm AssignmentSubmission: nhập score, feedback.
  o Chấm điểm Essay/Short Answer trong Quiz Attempt.

• **Quản lý lớp học và sinh viên:**
  o Tạo Class với thông tin: class_name, start_date, end_date, max_students.
  o Thiết lập schedules (JSON): lịch học, địa điểm, attendances.
  o Xem danh sách Enrollment của khóa học.
  o Theo dõi Progress của sinh viên theo từng Module.
  o Cấp Certificate khi sinh viên hoàn thành khóa học.

**− Student (Sinh viên):**

• **Quản lý tài khoản:**
  o Đăng ký tài khoản với email, password, first_name, last_name.
  o Đăng nhập và xác thực email (account_status: PENDING_VERIFICATION → ACTIVE).
  o Cập nhật preferences (notifications, locale, timezone, theme).
  o Đăng xuất.

• **Đăng ký và học tập:**
  o Đăng ký khóa học (Enrollment) - self-paced (class_id = NULL) hoặc theo lớp (class_id).
  o Xem danh sách Module theo order_num.
  o Học Lecture: xem video, đọc tài liệu, download Resource.
  o Cập nhật Progress khi hoàn thành Module.

• **Làm bài tập và kiểm tra:**
  o Xem Assignment (Lecture với type = 'ASSIGNMENT', assignment_config JSON).
  o Nộp AssignmentSubmission: upload file (file_urls JSON) hoặc nhập text.
  o Làm Quiz: tạo Attempt với answers (JSON).
  o Xem điểm và feedback sau khi được chấm.

• **Theo dõi tiến độ:**
  o Xem Progress: completion_percentage theo từng Module.
  o Xem điểm Quiz (Attempt.score) và Assignment (AssignmentSubmission.score).
  o Tải Certificate khi hoàn thành khóa học (Enrollment.status = COMPLETED).

**− Cấu trúc cơ sở dữ liệu (16 tables):**

| Domain | Tables |
|--------|--------|
| User Management (3) | User, Role, UserRole |
| Course Content (4) | Course, Module, Lecture, Resource |
| Assessment (5) | Quiz, Question, Option, Attempt, AssignmentSubmission |
| Enrollment & Progress (2) | Enrollment, Progress |
| Class & Certificate (2) | Class, Certificate |

---

## 2. Sơ đồ phân rã chức năng

### 2.1. Sơ đồ phân rã chức năng dưới góc nhìn của Instructor (Course Management)

#### 2.1.1. Sơ đồ phân rã chức năng

**Hướng dẫn vẽ BFD - Instructor:**

```
                    ┌─────────────────────────────────────────────┐
                    │   QUẢN LÝ ỨNG DỤNG LMS - INSTRUCTOR         │
                    │              (Level 0)                       │
                    └─────────────────────────────────────────────┘
                                        │
        ┌───────────────────────────────┼───────────────────────────────┐
        │                               │                               │
        ▼                               ▼                               ▼
┌───────────────────┐       ┌───────────────────┐       ┌───────────────────┐
│   QUẢN LÝ         │       │   QUẢN LÝ         │       │   QUẢN LÝ         │
│   TÀI KHOẢN       │       │   NỘI DUNG        │       │   LỚP HỌC &       │
│   (Level 1.1)     │       │   KHÓA HỌC        │       │   SINH VIÊN       │
│                   │       │   (Level 1.2)     │       │   (Level 1.3)     │
└───────────────────┘       └───────────────────┘       └───────────────────┘
        │                               │                               │
        ▼                               ▼                               ▼
┌───────────────────┐       ┌───────────────────┐       ┌───────────────────┐
│ - Đăng nhập       │       │ - Quản lý Course  │       │ - Quản lý Class   │
│ - Cập nhật        │       │ - Quản lý Module  │       │ - Quản lý         │
│   thông tin       │       │ - Quản lý Lecture │       │   Enrollment      │
│ - Đăng xuất       │       │ - Quản lý         │       │ - Theo dõi        │
│                   │       │   Assessment      │       │   Progress        │
│                   │       │   (Quiz/Assignment)│       │ - Cấp Certificate │
└───────────────────┘       └───────────────────┘       └───────────────────┘
```

**Phân rã chi tiết Level 2 - Quản lý Nội dung Khóa học:**

```
                    ┌─────────────────────────────────────────────┐
                    │   QUẢN LÝ NỘI DUNG KHÓA HỌC (Level 1.2)     │
                    └─────────────────────────────────────────────┘
                                        │
    ┌───────────────┬───────────────────┼───────────────────┬───────────────┐
    │               │                   │                   │               │
    ▼               ▼                   ▼                   ▼               ▼
┌─────────┐   ┌─────────┐       ┌─────────────┐     ┌─────────┐     ┌─────────┐
│ Quản lý │   │ Quản lý │       │  Quản lý    │     │ Quản lý │     │ Quản lý │
│ Course  │   │ Module  │       │  Lecture    │     │  Quiz   │     │Question │
│(2.2.1)  │   │(2.2.2)  │       │  (2.2.3)    │     │ (2.2.4) │     │ (2.2.5) │
└─────────┘   └─────────┘       └─────────────┘     └─────────┘     └─────────┘
    │               │                   │                   │               │
    ▼               ▼                   ▼                   ▼               ▼
- Tạo mới     - Tạo Module    - Tạo Lecture      - Tạo Quiz      - Tạo Question
- Cập nhật    - Sắp xếp       - Upload Resource  - Thêm Question - Tạo Option
- Xuất bản      order_num     - Thiết lập        - Thiết lập     - Đánh dấu
- Archive     - Thiết lập       assignment_config  passing_score   is_correct
- Xóa           prerequisite  - Xóa Lecture      - Xuất bản Quiz - Xóa Question
```

**Cách vẽ sơ đồ BFD:**
- Sử dụng hình chữ nhật cho mỗi chức năng.
- Đường kẻ thẳng đứng và ngang thể hiện quan hệ phân rã từ trên xuống.
- Level 0: Chức năng tổng quát nhất (root).
- Level 1: Các nhóm chức năng chính (3-5 nhóm).
- Level 2: Các chức năng con chi tiết.

---

#### 2.1.2. Đặc tả chức năng

**− Quản lý Tài khoản (Level 1.1):**

• **Mục đích:** Cho phép giảng viên đăng nhập và quản lý thông tin cá nhân trên hệ thống.

• **Ngữ cảnh:** Được sử dụng bởi giảng viên trên nền tảng web quản trị.

• **Điều kiện tiên quyết:** Thiết bị đã kết nối Internet. Tài khoản giảng viên đã được tạo và kích hoạt (account_status = 'ACTIVE').

• **Mô tả:**
  o **Đăng nhập:** Giảng viên nhập email và password. Hệ thống xác thực với User.email và User.password_hash (bcrypt). Kiểm tra UserRole có role = 'INSTRUCTOR'.
  o **Cập nhật thông tin:** Cập nhật User (first_name, last_name). Cập nhật User.preferences (JSON): notifications, locale, timezone, theme.
  o **Đăng xuất:** Hủy session/token, thoát khỏi hệ thống an toàn.

• **Kết quả:** Giảng viên có thể truy cập các chức năng quản lý của hệ thống. Thông tin cá nhân và preferences được cập nhật chính xác trong database.

---

**− Quản lý Nội dung Khóa học (Level 1.2):**

• **Mục đích:** Giúp giảng viên tạo, cập nhật, tổ chức và xuất bản nội dung khóa học theo cấu trúc phân cấp Course → Module → Lecture.

• **Ngữ cảnh:** Giảng viên sử dụng trên nền tảng web để quản lý toàn bộ nội dung học tập.

• **Điều kiện tiên quyết:** Giảng viên đã đăng nhập thành công. UserRole.role_id tương ứng với role 'INSTRUCTOR'.

• **Mô tả:**

  o **Tạo khóa học (Course):**
    - Nhập thông tin: code (unique, format ^[A-Z0-9]{3,10}$), title, description, difficulty_level.
    - Trạng thái mặc định: status = 'DRAFT'.
    - Lưu created_by = user_id của giảng viên hiện tại.

  o **Cập nhật khóa học:**
    - Chỉnh sửa title, description, difficulty_level, credits.
    - Chỉ cho phép khi Course.created_by = user_id hoặc có quyền admin.

  o **Xuất bản khóa học:**
    - Thay đổi status từ 'DRAFT' → 'PUBLISHED'.
    - Sinh viên có thể thấy và đăng ký khóa học sau khi published.

  o **Archive khóa học:**
    - Thay đổi status thành 'ARCHIVED'.
    - Sinh viên không thể đăng ký mới nhưng enrollment hiện tại vẫn hoạt động.

  o **Tạo Module:**
    - Tạo Module thuộc Course với: title, description, order_num.
    - Thiết lập prerequisite_module_ids (UUID[]) - danh sách module cần học trước.
    - UNIQUE constraint: (course_id, order_num).

  o **Tạo Lecture:**
    - Tạo Lecture trong Module với: title, description, type, order_num, duration_minutes.
    - Type: VIDEO, PDF, SLIDE, AUDIO, TEXT, ASSIGNMENT.
    - Nếu type = 'ASSIGNMENT': bắt buộc có assignment_config (JSON).

  o **Upload Resource:**
    - Upload file lên S3/GCS, lưu file_url, file_type, file_size_bytes.
    - Liên kết với lecture_id.

  o **Xóa khóa học:**
    - DELETE FROM Course WHERE course_id = ?
    - CASCADE sẽ xóa tất cả Module, Lecture, Resource, Quiz liên quan.

• **Kết quả:** Nội dung khóa học được tổ chức rõ ràng theo cấu trúc phân cấp. Sinh viên có thể truy cập nội dung khi khóa học được published.

---

**− Quản lý Assessment (Quiz & Assignment):**

• **Mục đích:** Cho phép giảng viên tạo Quiz, Question bank và chấm điểm sinh viên.

• **Ngữ cảnh:** Sử dụng để đánh giá kết quả học tập của sinh viên.

• **Điều kiện tiên quyết:** Giảng viên đã tạo Course.

• **Mô tả:**

  o **Tạo Quiz:**
    - Tạo Quiz với: title, description, instructions, duration_minutes, total_points, passing_score.
    - Thiết lập max_attempts, available_from, available_until.
    - Thêm questions (JSON): [{"question_id": "uuid", "points": 10, "order": 1}].

  o **Tạo Question:**
    - Tạo Question với: question_text, type (MCQ, TRUE_FALSE, ESSAY, SHORT_ANSWER), default_points.
    - Thuộc về một course_id để tái sử dụng trong nhiều Quiz.

  o **Tạo Option:**
    - Tạo Option cho MCQ/TRUE_FALSE: option_text, is_correct, order_num.
    - UNIQUE: (question_id, order_num).

  o **Chấm điểm Assignment:**
    - Xem AssignmentSubmission: submission_text, file_urls.
    - Nhập score, feedback, cập nhật status = 'GRADED'.

  o **Chấm điểm Quiz (Essay/Short Answer):**
    - Xem Attempt.answers (JSON).
    - Cập nhật score cho từng câu hỏi Essay/Short Answer.
    - Tổng hợp điểm và cập nhật Attempt.score, Attempt.status = 'GRADED'.

• **Kết quả:** Sinh viên có Quiz và Assignment để làm. Giảng viên chấm điểm và cung cấp feedback kịp thời.

---

**− Quản lý Lớp học & Sinh viên (Level 1.3):**

• **Mục đích:** Theo dõi sinh viên đã đăng ký, xem tiến độ và cấp chứng chỉ.

• **Ngữ cảnh:** Sử dụng để quản lý Enrollment, Class và Certificate.

• **Điều kiện tiên quyết:** Có sinh viên đã enroll vào khóa học.

• **Mô tả:**

  o **Tạo Class:**
    - Tạo Class với: class_name, start_date, end_date, max_students.
    - Thiết lập instructor_id, schedules (JSON) chứa lịch học và attendances.

  o **Xem danh sách Enrollment:**
    - Query Enrollment JOIN User để lấy danh sách sinh viên đã đăng ký.
    - Filter theo course_id và/hoặc class_id.

  o **Theo dõi Progress:**
    - Xem Progress của từng sinh viên theo Module.
    - Tính completion_percentage = (completed_modules / total_modules) * 100.

  o **Cấp Certificate:**
    - Khi Progress của sinh viên = 100% cho tất cả Module.
    - Tạo Certificate với: certificate_code (unique), verification_code, issue_date.
    - Cập nhật Enrollment.status = 'COMPLETED'.

• **Kết quả:** Giảng viên nắm rõ tình hình học tập của từng sinh viên. Sinh viên nhận được Certificate khi hoàn thành khóa học.

---

### 2.2. Sơ đồ phân rã chức năng dưới góc nhìn của Student (Submission Management)

#### 2.2.1. Sơ đồ phân rã chức năng

**Hướng dẫn vẽ BFD - Student:**

```
                    ┌─────────────────────────────────────────────┐
                    │    QUẢN LÝ ỨNG DỤNG LMS - STUDENT           │
                    │              (Level 0)                       │
                    └─────────────────────────────────────────────┘
                                        │
        ┌───────────────────────────────┼───────────────────────────────┐
        │                               │                               │
        ▼                               ▼                               ▼
┌───────────────────┐       ┌───────────────────┐       ┌───────────────────┐
│   QUẢN LÝ         │       │   ĐĂNG KÝ &       │       │   THEO DÕI        │
│   TÀI KHOẢN       │       │   HỌC TẬP         │       │   TIẾN ĐỘ         │
│   (Level 1.1)     │       │   (Level 1.2)     │       │   (Level 1.3)     │
└───────────────────┘       └───────────────────┘       └───────────────────┘
        │                               │                               │
        ▼                               ▼                               ▼
┌───────────────────┐       ┌───────────────────┐       ┌───────────────────┐
│ - Đăng ký         │       │ - Đăng ký khóa học│       │ - Xem completion  │
│ - Đăng nhập       │       │ - Xem Module/     │       │   percentage      │
│ - Cập nhật        │       │   Lecture         │       │ - Xem điểm số     │
│   thông tin       │       │ - Làm Assignment  │       │ - Tải Certificate │
│ - Đăng xuất       │       │ - Làm Quiz        │       │                   │
└───────────────────┘       └───────────────────┘       └───────────────────┘
```

**Phân rã chi tiết Level 2 - Đăng ký & Học tập:**

```
                    ┌─────────────────────────────────────────────┐
                    │      ĐĂNG KÝ & HỌC TẬP (Level 1.2)          │
                    └─────────────────────────────────────────────┘
                                        │
        ┌───────────────────────────────┼───────────────────────────────┐
        │                               │                               │
        ▼                               ▼                               ▼
┌───────────────────┐       ┌───────────────────┐       ┌───────────────────┐
│   ĐĂNG KÝ         │       │   HỌC LECTURE     │       │   LÀM BÀI TẬP     │
│   KHÓA HỌC        │       │                   │       │   & KIỂM TRA      │
│   (Level 2.1)     │       │   (Level 2.2)     │       │   (Level 2.3)     │
└───────────────────┘       └───────────────────┘       └───────────────────┘
        │                               │                               │
        ▼                               ▼                               ▼
┌───────────────────┐       ┌───────────────────┐       ┌───────────────────┐
│ - Tìm kiếm        │       │ - Xem danh sách   │       │ - Xem Assignment  │
│   khóa học        │       │   Module          │       │ - Nộp bài tập     │
│ - Xem chi tiết    │       │ - Xem Lecture     │       │ - Làm Quiz        │
│   Course          │       │   (video/PDF/...) │       │ - Xem điểm &      │
│ - Đăng ký         │       │ - Download        │       │   feedback        │
│   Enrollment      │       │   Resource        │       │                   │
│ - Hủy đăng ký     │       │ - Đánh dấu        │       │                   │
│                   │       │   hoàn thành      │       │                   │
└───────────────────┘       └───────────────────┘       └───────────────────┘
```

---

#### 2.2.2. Đặc tả chức năng

**− Quản lý Tài khoản (Level 1.1):**

• **Mục đích:** Cho phép sinh viên tạo tài khoản, đăng nhập và quản lý thông tin cá nhân.

• **Ngữ cảnh:** Sử dụng bởi sinh viên trên nền tảng web/mobile.

• **Điều kiện tiên quyết:** Thiết bị đã kết nối Internet.

• **Mô tả:**
  o **Đăng ký:** Sinh viên cung cấp email (unique), password, first_name, last_name. Hệ thống tạo User với account_status = 'PENDING_VERIFICATION'. Gửi email xác thực.
  o **Đăng nhập:** Nhập email và password. Xác thực với bcrypt compare. Kiểm tra account_status = 'ACTIVE'.
  o **Cập nhật thông tin:** Cập nhật first_name, last_name. Cập nhật preferences (JSON): notifications, locale, timezone, theme.
  o **Đăng xuất:** Hủy session, thoát khỏi hệ thống.

• **Kết quả:** Sinh viên có tài khoản và có thể truy cập các chức năng học tập.

---

**− Đăng ký & Học tập (Level 1.2):**

• **Mục đích:** Cho phép sinh viên đăng ký khóa học và học các Lecture.

• **Ngữ cảnh:** Sinh viên tìm và đăng ký khóa học quan tâm, sau đó học tập theo cấu trúc Module → Lecture.

• **Điều kiện tiên quyết:** Sinh viên đã đăng nhập. Khóa học có status = 'PUBLISHED'.

• **Mô tả:**

  o **Đăng ký khóa học (Enrollment):**
    - Tạo Enrollment record với user_id, course_id.
    - Nếu có Class: thêm class_id (blended learning).
    - Nếu không: class_id = NULL (self-paced).
    - Kiểm tra UNIQUE constraint: (user_id, course_id, COALESCE(class_id, UUID_NIL)).

  o **Xem danh sách Module:**
    - Query Module WHERE course_id = ? ORDER BY order_num.
    - Kiểm tra prerequisite_module_ids để unlock Module.

  o **Xem Lecture:**
    - Query Lecture WHERE module_id = ? ORDER BY order_num.
    - Hiển thị nội dung theo type: VIDEO, PDF, SLIDE, AUDIO, TEXT.
    - Nếu type = 'ASSIGNMENT': hiển thị assignment_config (instructions, due_date).

  o **Download Resource:**
    - Lấy Resource.file_url và redirect download từ S3/GCS.

  o **Đánh dấu hoàn thành:**
    - Tạo/Update Progress với status = 'COMPLETED', completion_percentage = 100.
    - Recalculate Enrollment completion dựa trên số Module hoàn thành.

• **Kết quả:** Sinh viên đã enroll và có thể học Lecture. Progress được tracking theo từng Module.

---

**− Làm bài tập & Kiểm tra (Level 2.3):**

• **Mục đích:** Cho phép sinh viên làm Assignment và Quiz.

• **Ngữ cảnh:** Sinh viên làm bài tập và kiểm tra để đánh giá kiến thức.

• **Điều kiện tiên quyết:** Sinh viên đã enroll khóa học (Enrollment.status = 'ACTIVE').

• **Mô tả:**

  o **Xem Assignment:**
    - Query Lecture WHERE type = 'ASSIGNMENT' AND module_id IN (course modules).
    - Hiển thị assignment_config: instructions, max_points, due_date, submission_types, allowed_file_types.

  o **Nộp bài tập (AssignmentSubmission):**
    - Tạo AssignmentSubmission với lecture_id, user_id, enrollment_id.
    - Upload file → lưu vào file_urls (JSON array).
    - Hoặc nhập text (nếu submission_types includes 'text').
    - Cập nhật status = 'SUBMITTED', submitted_at = CURRENT_TIMESTAMP.
    - Nếu submitted_at > assignment_config.due_date: status = 'LATE'.

  o **Làm Quiz:**
    - Kiểm tra Attempt count < Quiz.max_attempts.
    - Tạo Attempt với quiz_id, user_id, enrollment_id, attempt_number.
    - Hiển thị từng Question theo Quiz.questions order.
    - Sinh viên chọn Option (MCQ/TRUE_FALSE) hoặc nhập text (ESSAY/SHORT_ANSWER).
    - Submit: cập nhật Attempt.answers (JSON), Attempt.submitted_at.
    - Auto-grade MCQ/TRUE_FALSE: so sánh selected_options với Option.is_correct.
    - ESSAY/SHORT_ANSWER: status = 'PENDING_GRADING'.

  o **Xem điểm và feedback:**
    - Query AssignmentSubmission: score, feedback, graded_at.
    - Query Attempt: score, max_score, answers[].feedback.

• **Kết quả:** Bài tập và Quiz được nộp thành công. Sinh viên nhận điểm và feedback sau khi giảng viên chấm.

---

**− Theo dõi Tiến độ (Level 1.3):**

• **Mục đích:** Cho phép sinh viên theo dõi tiến độ học tập của mình.

• **Ngữ cảnh:** Sinh viên muốn biết đã hoàn thành bao nhiêu % khóa học.

• **Điều kiện tiên quyết:** Sinh viên đã enroll khóa học.

• **Mô tả:**

  o **Xem completion percentage:**
    - Tính: completed_modules / total_modules * 100.
    - Query Progress WHERE user_id = ? AND course_id = ? AND status = 'COMPLETED'.

  o **Xem điểm số:**
    - Danh sách AssignmentSubmission với score, max_score.
    - Danh sách Attempt với score, max_score, passed (score >= Quiz.passing_score).

  o **Tải Certificate:**
    - Khi completion = 100% và Enrollment.status = 'COMPLETED'.
    - Query Certificate WHERE user_id = ? AND course_id = ?.
    - Redirect download Certificate.pdf_url.

• **Kết quả:** Sinh viên nắm rõ tiến độ học tập. Nhận được Certificate khi hoàn thành khóa học.

---

## 3. Mô hình hóa dữ liệu

### 3.1. Sơ đồ ER

**Hướng dẫn vẽ ERD:**

**Entities (16 tables):**

```
DOMAIN 1: USER MANAGEMENT
┌─────────────────────────────────────────────────────────────────────────┐
│                                                                         │
│  ┌─────────────┐         ┌─────────────┐         ┌─────────────┐       │
│  │    USER     │ 1     N │  USERROLE   │ N     1 │    ROLE     │       │
│  │─────────────│─────────│─────────────│─────────│─────────────│       │
│  │ user_id (PK)│         │user_role_id │         │ role_id (PK)│       │
│  │ email       │         │ user_id (FK)│         │ name        │       │
│  │ password_   │         │ role_id (FK)│         │ permissions │       │
│  │   hash      │         │ expires_at  │         │   (JSON)    │       │
│  │ first_name  │         └─────────────┘         └─────────────┘       │
│  │ last_name   │                                                        │
│  │ preferences │                                                        │
│  │   (JSON)    │                                                        │
│  └─────────────┘                                                        │
│         │                                                                │
└─────────│────────────────────────────────────────────────────────────────┘
          │ 1
          │
          ├──────────────────────────────────────────────────────┐
          │                                                      │
          ▼ N                                                    ▼ N
DOMAIN 2: COURSE CONTENT                                  DOMAIN 4 & 5
┌─────────────────────────────────────────────────────────────────────────┐
│                                                                         │
│  ┌─────────────┐                                                        │
│  │   COURSE    │ 1                                                      │
│  │─────────────│──────────────────────────────────────────────────┐     │
│  │course_id(PK)│                                                  │     │
│  │ code        │ 1                                                │     │
│  │ title       │──────────────────────────────────┐               │     │
│  │ status      │                                  │               │     │
│  │ created_by  │──→ User.user_id                  │               │     │
│  └─────────────┘                                  │               │     │
│         │ 1                                       │               │     │
│         │                                         │               │     │
│         ▼ N                                       ▼ N             ▼ N   │
│  ┌─────────────┐                           ┌─────────────┐ ┌──────────┐ │
│  │   MODULE    │                           │    QUIZ     │ │  CLASS   │ │
│  │─────────────│                           │─────────────│ │──────────│ │
│  │module_id(PK)│                           │quiz_id (PK) │ │class_id  │ │
│  │course_id(FK)│                           │course_id(FK)│ │course_id │ │
│  │ title       │                           │ title       │ │class_name│ │
│  │ order_num   │                           │ questions   │ │schedules │ │
│  │prerequisite_│                           │   (JSON)    │ │  (JSON)  │ │
│  │ module_ids  │                           │passing_score│ └──────────┘ │
│  └─────────────┘                           └─────────────┘       │      │
│         │ 1                                       │ 1            │      │
│         │                                         │              │      │
│         ▼ N                                       │              │      │
│  ┌─────────────┐                                  │              │      │
│  │   LECTURE   │                                  │              │      │
│  │─────────────│                                  │              │      │
│  │lecture_id   │                                  │              │      │
│  │module_id(FK)│                                  │              │      │
│  │ title       │                                  │              │      │
│  │ type        │                                  │              │      │
│  │assignment_  │                                  │              │      │
│  │  config(JSON│                                  │              │      │
│  └─────────────┘                                  │              │      │
│         │ 1                                       │              │      │
│         │                                         │              │      │
│         ▼ N                                       │              │      │
│  ┌─────────────┐                                  │              │      │
│  │  RESOURCE   │                                  │              │      │
│  │─────────────│                                  │              │      │
│  │resource_id  │                                  │              │      │
│  │lecture_id   │                                  │              │      │
│  │ file_url    │                                  │              │      │
│  └─────────────┘                                  │              │      │
│                                                   │              │      │
└───────────────────────────────────────────────────│──────────────│──────┘
                                                    │              │
DOMAIN 3: ASSESSMENT                                │              │
┌───────────────────────────────────────────────────│──────────────│──────┐
│                                                   │              │      │
│  ┌─────────────┐                                  │              │      │
│  │  QUESTION   │ 1                                │              │      │
│  │─────────────│─────────────────┐                │              │      │
│  │question_id  │                 │                │              │      │
│  │course_id(FK)│                 ▼ N              │              │      │
│  │question_text│          ┌─────────────┐         │              │      │
│  │ type        │          │   OPTION    │         │              │      │
│  └─────────────┘          │─────────────│         │              │      │
│                           │ option_id   │         │              │      │
│                           │question_id  │         │              │      │
│                           │ is_correct  │         │              │      │
│                           └─────────────┘         │              │      │
│                                                   │              │      │
└───────────────────────────────────────────────────│──────────────│──────┘
                                                    │              │
DOMAIN 4: ENROLLMENT & PROGRESS                     │              │
┌───────────────────────────────────────────────────│──────────────│──────┐
│                                                   │              │      │
│  ┌─────────────────────────────────────────────┐  │              │      │
│  │              ENROLLMENT                      │  │              │      │
│  │─────────────────────────────────────────────│  │              │      │
│  │ enrollment_id (PK)                          │  │              │      │
│  │ user_id (FK) → User                         │  │              │      │
│  │ course_id (FK) → Course                     │  │              │      │
│  │ class_id (FK, nullable) → Class             │──┘              │      │
│  │ status: ACTIVE, COMPLETED, DROPPED          │                 │      │
│  └─────────────────────────────────────────────┘                 │      │
│         │ 1                                                      │      │
│         │                                                        │      │
│         ├──────────────────┬──────────────────┐                  │      │
│         │                  │                  │                  │      │
│         ▼ N                ▼ N                ▼ 0..1             │      │
│  ┌─────────────┐    ┌─────────────┐    ┌─────────────────┐       │      │
│  │   ATTEMPT   │    │ ASSIGNMENT  │    │   CERTIFICATE   │       │      │
│  │─────────────│    │ SUBMISSION  │    │─────────────────│       │      │
│  │ attempt_id  │    │─────────────│    │ certificate_id  │       │      │
│  │enrollment_id│    │submission_id│    │ user_id (FK)    │       │      │
│  │ quiz_id (FK)│────│enrollment_id│    │ course_id (FK)  │       │      │
│  │ answers     │    │ lecture_id  │    │ certificate_code│       │      │
│  │   (JSON)    │    │ file_urls   │    │ verification_   │       │      │
│  │ score       │    │   (JSON)    │    │   code          │       │      │
│  └─────────────┘    │ score       │    └─────────────────┘       │      │
│         │           └─────────────┘                              │      │
│         │                                                        │      │
│         ▼                                                        │      │
│  ┌─────────────┐                                                 │      │
│  │  PROGRESS   │                                                 │      │
│  │─────────────│                                                 │      │
│  │ progress_id │                                                 │      │
│  │ user_id (FK)│                                                 │      │
│  │course_id(FK)│                                                 │      │
│  │module_id(FK)│                                                 │      │
│  │ status      │                                                 │      │
│  │ completion_ │                                                 │      │
│  │  percentage │                                                 │      │
│  └─────────────┘                                                 │      │
│                                                                  │      │
└──────────────────────────────────────────────────────────────────┘      │
                                                                          │
                                              Class.class_id ◄────────────┘
```

**Relationships chính:**

| From | To | Cardinality | ON DELETE |
|------|-----|-------------|-----------|
| User | UserRole | 1:N | CASCADE |
| Role | UserRole | 1:N | CASCADE |
| User | Course | 1:N | SET NULL |
| Course | Module | 1:N | CASCADE |
| Module | Lecture | 1:N | CASCADE |
| Lecture | Resource | 1:N | CASCADE |
| Course | Quiz | 1:N | CASCADE |
| Course | Question | 1:N | CASCADE |
| Question | Option | 1:N | CASCADE |
| User | Enrollment | 1:N | CASCADE |
| Course | Enrollment | 1:N | CASCADE |
| Class | Enrollment | 1:N | SET NULL |
| Enrollment | Attempt | 1:N | CASCADE |
| Enrollment | AssignmentSubmission | 1:N | CASCADE |
| Enrollment | Progress | 1:N | CASCADE |
| User | Certificate | 1:N | CASCADE |
| Course | Certificate | 1:N | CASCADE |

**Cách vẽ ERD:**
- Sử dụng hình chữ nhật cho Entity (table).
- Ghi rõ PK (Primary Key), FK (Foreign Key) trong mỗi entity.
- Sử dụng đường kẻ với ký hiệu 1, N để thể hiện cardinality.
- Nhóm theo domain để dễ đọc.

---

### 3.2. Sơ đồ RM (Relational Model)

**DOMAIN 1: USER MANAGEMENT**

**USER**
- user_id (PK) - UUID - gen_random_uuid()
- email - VARCHAR(255) - UNIQUE NOT NULL
- password_hash - VARCHAR(255) - NOT NULL
- first_name - VARCHAR(100) - NOT NULL
- last_name - VARCHAR(100) - NOT NULL
- account_status - VARCHAR(20) - DEFAULT 'PENDING_VERIFICATION'
- preferences - JSON - DEFAULT '{}'
- created_at - TIMESTAMP - DEFAULT CURRENT_TIMESTAMP
- updated_at - TIMESTAMP - DEFAULT CURRENT_TIMESTAMP

**ROLE**
- role_id (PK) - UUID
- name - VARCHAR(50) - UNIQUE NOT NULL
- description - TEXT
- permissions - JSON - DEFAULT '[]'
- created_at - TIMESTAMP - DEFAULT CURRENT_TIMESTAMP

**USERROLE**
- user_role_id (PK) - UUID
- user_id (FK → USER) - UUID - NOT NULL - ON DELETE CASCADE
- role_id (FK → ROLE) - UUID - NOT NULL - ON DELETE CASCADE
- assigned_at - TIMESTAMP - DEFAULT CURRENT_TIMESTAMP
- expires_at - TIMESTAMP
- UNIQUE(user_id, role_id)

---

**DOMAIN 2: COURSE CONTENT**

**COURSE**
- course_id (PK) - UUID
- code - VARCHAR(20) - UNIQUE NOT NULL
- title - VARCHAR(255) - NOT NULL
- description - TEXT
- difficulty_level - VARCHAR(20) - DEFAULT 'BEGINNER'
- credits - INTEGER
- status - VARCHAR(20) - DEFAULT 'DRAFT'
- created_by (FK → USER) - UUID - ON DELETE SET NULL
- created_at - TIMESTAMP - DEFAULT CURRENT_TIMESTAMP
- updated_at - TIMESTAMP - DEFAULT CURRENT_TIMESTAMP

**MODULE**
- module_id (PK) - UUID
- course_id (FK → COURSE) - UUID - NOT NULL - ON DELETE CASCADE
- title - VARCHAR(255) - NOT NULL
- description - TEXT
- order_num - INTEGER - NOT NULL
- estimated_duration_minutes - INTEGER
- prerequisite_module_ids - UUID[] - DEFAULT '{}'
- created_at - TIMESTAMP - DEFAULT CURRENT_TIMESTAMP
- updated_at - TIMESTAMP - DEFAULT CURRENT_TIMESTAMP
- UNIQUE(course_id, order_num)

**LECTURE**
- lecture_id (PK) - UUID
- module_id (FK → MODULE) - UUID - NOT NULL - ON DELETE CASCADE
- title - VARCHAR(255) - NOT NULL
- description - TEXT
- type - VARCHAR(20) - NOT NULL
- order_num - INTEGER - NOT NULL
- duration_minutes - INTEGER
- assignment_config - JSON
- created_at - TIMESTAMP - DEFAULT CURRENT_TIMESTAMP
- updated_at - TIMESTAMP - DEFAULT CURRENT_TIMESTAMP
- UNIQUE(module_id, order_num)

**RESOURCE**
- resource_id (PK) - UUID
- lecture_id (FK → LECTURE) - UUID - NOT NULL - ON DELETE CASCADE
- file_url - VARCHAR(500) - NOT NULL
- file_type - VARCHAR(100)
- file_size_bytes - BIGINT
- created_at - TIMESTAMP - DEFAULT CURRENT_TIMESTAMP

---

**DOMAIN 3: ASSESSMENT**

**QUIZ**
- quiz_id (PK) - UUID
- course_id (FK → COURSE) - UUID - NOT NULL - ON DELETE CASCADE
- title - VARCHAR(255) - NOT NULL
- description - TEXT
- instructions - TEXT
- status - VARCHAR(20) - DEFAULT 'DRAFT'
- duration_minutes - INTEGER
- total_points - DECIMAL(10,2)
- passing_score - DECIMAL(10,2)
- max_attempts - INTEGER
- available_from - TIMESTAMP
- available_until - TIMESTAMP
- questions - JSON - DEFAULT '[]'
- created_by (FK → USER) - UUID - ON DELETE SET NULL
- created_at - TIMESTAMP - DEFAULT CURRENT_TIMESTAMP

**QUESTION**
- question_id (PK) - UUID
- course_id (FK → COURSE) - UUID - NOT NULL - ON DELETE CASCADE
- question_text - TEXT - NOT NULL
- type - VARCHAR(20) - NOT NULL
- default_points - DECIMAL(10,2)
- created_by (FK → USER) - UUID - ON DELETE SET NULL
- created_at - TIMESTAMP - DEFAULT CURRENT_TIMESTAMP

**OPTION**
- option_id (PK) - UUID
- question_id (FK → QUESTION) - UUID - NOT NULL - ON DELETE CASCADE
- option_text - TEXT - NOT NULL
- is_correct - BOOLEAN - DEFAULT FALSE
- order_num - INTEGER - NOT NULL
- UNIQUE(question_id, order_num)

**ATTEMPT**
- attempt_id (PK) - UUID
- quiz_id (FK → QUIZ) - UUID - NOT NULL - ON DELETE CASCADE
- user_id (FK → USER) - UUID - NOT NULL - ON DELETE CASCADE
- enrollment_id (FK → ENROLLMENT) - UUID - NOT NULL - ON DELETE CASCADE
- attempt_number - INTEGER - NOT NULL
- status - VARCHAR(20) - DEFAULT 'IN_PROGRESS'
- started_at - TIMESTAMP - DEFAULT CURRENT_TIMESTAMP
- submitted_at - TIMESTAMP
- score - DECIMAL(10,2)
- max_score - DECIMAL(10,2)
- answers - JSON - DEFAULT '[]'
- UNIQUE(user_id, quiz_id, attempt_number)

**ASSIGNMENTSUBMISSION**
- submission_id (PK) - UUID
- lecture_id (FK → LECTURE) - UUID - NOT NULL - ON DELETE CASCADE
- user_id (FK → USER) - UUID - NOT NULL - ON DELETE CASCADE
- enrollment_id (FK → ENROLLMENT) - UUID - NOT NULL - ON DELETE CASCADE
- submission_number - INTEGER - NOT NULL
- status - VARCHAR(20) - DEFAULT 'DRAFT'
- submitted_at - TIMESTAMP
- score - DECIMAL(10,2)
- max_score - DECIMAL(10,2)
- feedback - TEXT
- file_urls - JSON - DEFAULT '[]'
- UNIQUE(lecture_id, user_id, submission_number)

---

**DOMAIN 4: ENROLLMENT & PROGRESS**

**ENROLLMENT**
- enrollment_id (PK) - UUID
- user_id (FK → USER) - UUID - NOT NULL - ON DELETE CASCADE
- course_id (FK → COURSE) - UUID - NOT NULL - ON DELETE CASCADE
- class_id (FK → CLASS) - UUID - ON DELETE SET NULL (nullable)
- status - VARCHAR(20) - DEFAULT 'ACTIVE'
- enrolled_at - TIMESTAMP - DEFAULT CURRENT_TIMESTAMP
- completed_at - TIMESTAMP
- UNIQUE(user_id, course_id, COALESCE(class_id, UUID_NIL))

**PROGRESS**
- progress_id (PK) - UUID
- user_id (FK → USER) - UUID - NOT NULL - ON DELETE CASCADE
- course_id (FK → COURSE) - UUID - NOT NULL - ON DELETE CASCADE
- module_id (FK → MODULE) - UUID - NOT NULL - ON DELETE CASCADE
- status - VARCHAR(20) - DEFAULT 'NOT_STARTED'
- completion_percentage - INTEGER - DEFAULT 0
- started_at - TIMESTAMP
- completed_at - TIMESTAMP
- UNIQUE(user_id, course_id, module_id)

---

**DOMAIN 5: CLASS & CERTIFICATE**

**CLASS**
- class_id (PK) - UUID
- course_id (FK → COURSE) - UUID - NOT NULL - ON DELETE CASCADE
- class_name - VARCHAR(100) - NOT NULL
- start_date - DATE - NOT NULL
- end_date - DATE - NOT NULL
- instructor_id (FK → USER) - UUID - ON DELETE SET NULL
- max_students - INTEGER
- status - VARCHAR(20) - DEFAULT 'SCHEDULED'
- schedules - JSON - DEFAULT '[]'
- created_at - TIMESTAMP - DEFAULT CURRENT_TIMESTAMP

**CERTIFICATE**
- certificate_id (PK) - UUID
- user_id (FK → USER) - UUID - NOT NULL - ON DELETE CASCADE
- course_id (FK → COURSE) - UUID - NOT NULL - ON DELETE CASCADE
- certificate_code - VARCHAR(50) - UNIQUE NOT NULL
- verification_code - VARCHAR(100) - UNIQUE NOT NULL
- issue_date - DATE - NOT NULL
- final_grade - DECIMAL(5,2)
- pdf_url - VARCHAR(500)
- status - VARCHAR(20) - DEFAULT 'ACTIVE'
- created_at - TIMESTAMP - DEFAULT CURRENT_TIMESTAMP
- UNIQUE(user_id, course_id)

---

### 3.3. Đặc tả chi tiết cơ sở dữ liệu

#### 3.3.1. Domain 1: Quản lý tài khoản và người dùng

**− Bảng: User**

| Trường | Kiểu dữ liệu | Ràng buộc | Mô tả |
|--------|--------------|-----------|-------|
| user_id | UUID | PK, DEFAULT gen_random_uuid() | Khóa chính, tự động sinh. |
| email | VARCHAR(255) | UNIQUE, NOT NULL | Email đăng nhập, duy nhất trong hệ thống. |
| password_hash | VARCHAR(255) | NOT NULL | Mật khẩu đã mã hóa bcrypt ($2a$10$...). |
| first_name | VARCHAR(100) | NOT NULL | Tên người dùng. |
| last_name | VARCHAR(100) | NOT NULL | Họ người dùng. |
| account_status | VARCHAR(20) | DEFAULT 'PENDING_VERIFICATION', CHECK | Trạng thái: PENDING_VERIFICATION, ACTIVE, SUSPENDED, DELETED. |
| preferences | JSON | DEFAULT '{}' | Cấu hình cá nhân (JSON): notifications, locale, timezone, theme. |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Thời gian tạo tài khoản. |
| updated_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Thời gian cập nhật cuối. |

**Cấu trúc preferences JSON:**
```json
{
  "notifications": {
    "assignment_due": {"email": true, "push": true},
    "grade_published": {"email": true, "push": false}
  },
  "locale": "vi",
  "timezone": "Asia/Ho_Chi_Minh",
  "theme": "light"
}
```

---

**− Bảng: Role**

| Trường | Kiểu dữ liệu | Ràng buộc | Mô tả |
|--------|--------------|-----------|-------|
| role_id | UUID | PK | Khóa chính. |
| name | VARCHAR(50) | UNIQUE, NOT NULL | Tên role: STUDENT, INSTRUCTOR, TA, ADMIN. |
| description | TEXT | | Mô tả role (Tùy chọn). |
| permissions | JSON | DEFAULT '[]' | Danh sách quyền: ["course.create", "grade.manage"]. |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Thời gian tạo. |

---

**− Bảng: UserRole**

| Trường | Kiểu dữ liệu | Ràng buộc | Mô tả |
|--------|--------------|-----------|-------|
| user_role_id | UUID | PK | Khóa chính. |
| user_id | UUID | FK → User, NOT NULL, ON DELETE CASCADE | Người dùng được gán role. |
| role_id | UUID | FK → Role, NOT NULL, ON DELETE CASCADE | Role được gán. |
| assigned_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Thời gian gán role. |
| expires_at | TIMESTAMP | | Thời gian hết hạn (Tùy chọn, cho role tạm thời). |
| | | UNIQUE(user_id, role_id) | Một user chỉ có một role_id cụ thể một lần. |

---

#### 3.3.2. Domain 2: Quản lý nội dung khóa học

**− Bảng: Course**

| Trường | Kiểu dữ liệu | Ràng buộc | Mô tả |
|--------|--------------|-----------|-------|
| course_id | UUID | PK | Khóa chính. |
| code | VARCHAR(20) | UNIQUE, NOT NULL, CHECK (^[A-Z0-9]{3,10}$) | Mã khóa học (VD: "CS101", "JAVA2024"). |
| title | VARCHAR(255) | NOT NULL | Tên khóa học. |
| description | TEXT | | Mô tả chi tiết (Tùy chọn). |
| difficulty_level | VARCHAR(20) | DEFAULT 'BEGINNER', CHECK | Độ khó: BEGINNER, INTERMEDIATE, ADVANCED. |
| credits | INTEGER | | Số tín chỉ (Tùy chọn). |
| status | VARCHAR(20) | DEFAULT 'DRAFT', CHECK | Trạng thái: DRAFT, PUBLISHED, ARCHIVED. |
| created_by | UUID | FK → User, ON DELETE SET NULL | Giảng viên tạo khóa học. |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Thời gian tạo. |
| updated_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Thời gian cập nhật. |

---

**− Bảng: Module**

| Trường | Kiểu dữ liệu | Ràng buộc | Mô tả |
|--------|--------------|-----------|-------|
| module_id | UUID | PK | Khóa chính. |
| course_id | UUID | FK → Course, NOT NULL, ON DELETE CASCADE | Khóa học chứa module. |
| title | VARCHAR(255) | NOT NULL | Tên module. |
| description | TEXT | | Mô tả module (Tùy chọn). |
| order_num | INTEGER | NOT NULL | Thứ tự hiển thị trong khóa học. |
| estimated_duration_minutes | INTEGER | | Thời lượng ước tính (phút). |
| prerequisite_module_ids | UUID[] | DEFAULT '{}' | Danh sách module_id cần hoàn thành trước. |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Thời gian tạo. |
| updated_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Thời gian cập nhật. |
| | | UNIQUE(course_id, order_num) | Không trùng order trong một khóa học. |

---

**− Bảng: Lecture**

| Trường | Kiểu dữ liệu | Ràng buộc | Mô tả |
|--------|--------------|-----------|-------|
| lecture_id | UUID | PK | Khóa chính. |
| module_id | UUID | FK → Module, NOT NULL, ON DELETE CASCADE | Module chứa lecture. |
| title | VARCHAR(255) | NOT NULL | Tiêu đề bài giảng. |
| description | TEXT | | Mô tả bài giảng (Tùy chọn). |
| type | VARCHAR(20) | NOT NULL, CHECK | Loại: VIDEO, PDF, SLIDE, AUDIO, TEXT, ASSIGNMENT. |
| order_num | INTEGER | NOT NULL | Thứ tự trong Module. |
| duration_minutes | INTEGER | | Độ dài (phút) - cho VIDEO, AUDIO. |
| assignment_config | JSON | | Cấu hình bài tập (bắt buộc khi type='ASSIGNMENT'). |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Thời gian tạo. |
| updated_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Thời gian cập nhật. |
| | | UNIQUE(module_id, order_num) | Không trùng order trong một module. |
| | | CHECK: type='ASSIGNMENT' → assignment_config NOT NULL | Ràng buộc logic. |

**Cấu trúc assignment_config JSON:**
```json
{
  "max_points": 100,
  "due_date": "2025-12-15T23:59:00Z",
  "submission_types": ["file", "text", "code"],
  "allowed_file_types": [".java", ".py", ".pdf"],
  "max_file_size_mb": 10,
  "instructions": "Viết chương trình Java...",
  "rubric": {
    "code_quality": 40,
    "functionality": 40,
    "documentation": 20
  }
}
```

---

**− Bảng: Resource**

| Trường | Kiểu dữ liệu | Ràng buộc | Mô tả |
|--------|--------------|-----------|-------|
| resource_id | UUID | PK | Khóa chính. |
| lecture_id | UUID | FK → Lecture, NOT NULL, ON DELETE CASCADE | Lecture chứa resource. |
| file_url | VARCHAR(500) | NOT NULL | URL file trên S3/GCS. |
| file_type | VARCHAR(100) | | MIME type: video/mp4, application/pdf. |
| file_size_bytes | BIGINT | | Kích thước file (bytes). |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Thời gian tạo. |

---

#### 3.3.3. Domain 3: Quản lý đánh giá

**− Bảng: Quiz**

| Trường | Kiểu dữ liệu | Ràng buộc | Mô tả |
|--------|--------------|-----------|-------|
| quiz_id | UUID | PK | Khóa chính. |
| course_id | UUID | FK → Course, NOT NULL, ON DELETE CASCADE | Khóa học chứa quiz. |
| title | VARCHAR(255) | NOT NULL | Tiêu đề quiz. |
| description | TEXT | | Mô tả quiz (Tùy chọn). |
| instructions | TEXT | | Hướng dẫn làm bài (Tùy chọn). |
| status | VARCHAR(20) | DEFAULT 'DRAFT', CHECK | Trạng thái: DRAFT, PUBLISHED, ARCHIVED. |
| duration_minutes | INTEGER | | Thời gian làm bài (phút, NULL = không giới hạn). |
| total_points | DECIMAL(10,2) | | Tổng điểm. |
| passing_score | DECIMAL(10,2) | | Điểm đạt. |
| max_attempts | INTEGER | | Số lần làm tối đa (NULL = không giới hạn). |
| available_from | TIMESTAMP | | Thời gian mở quiz. |
| available_until | TIMESTAMP | | Thời gian đóng quiz. |
| questions | JSON | DEFAULT '[]' | Danh sách câu hỏi (JSON array). |
| created_by | UUID | FK → User, ON DELETE SET NULL | Người tạo quiz. |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Thời gian tạo. |

**Cấu trúc questions JSON:**
```json
[
  {"question_id": "uuid-1", "points": 10, "order": 1},
  {"question_id": "uuid-2", "points": 15, "order": 2}
]
```

---

**− Bảng: Question**

| Trường | Kiểu dữ liệu | Ràng buộc | Mô tả |
|--------|--------------|-----------|-------|
| question_id | UUID | PK | Khóa chính. |
| course_id | UUID | FK → Course, NOT NULL, ON DELETE CASCADE | Khóa học chứa câu hỏi (question bank). |
| question_text | TEXT | NOT NULL | Nội dung câu hỏi. |
| type | VARCHAR(20) | NOT NULL, CHECK | Loại: MCQ, TRUE_FALSE, ESSAY, SHORT_ANSWER. |
| default_points | DECIMAL(10,2) | | Điểm mặc định. |
| created_by | UUID | FK → User, ON DELETE SET NULL | Người tạo câu hỏi. |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Thời gian tạo. |

---

**− Bảng: Option**

| Trường | Kiểu dữ liệu | Ràng buộc | Mô tả |
|--------|--------------|-----------|-------|
| option_id | UUID | PK | Khóa chính. |
| question_id | UUID | FK → Question, NOT NULL, ON DELETE CASCADE | Câu hỏi chứa option. |
| option_text | TEXT | NOT NULL | Nội dung đáp án. |
| is_correct | BOOLEAN | DEFAULT FALSE | Đánh dấu đáp án đúng. |
| order_num | INTEGER | NOT NULL | Thứ tự hiển thị. |
| | | UNIQUE(question_id, order_num) | Không trùng order. |

---

**− Bảng: Attempt**

| Trường | Kiểu dữ liệu | Ràng buộc | Mô tả |
|--------|--------------|-----------|-------|
| attempt_id | UUID | PK | Khóa chính. |
| quiz_id | UUID | FK → Quiz, NOT NULL, ON DELETE CASCADE | Quiz được làm. |
| user_id | UUID | FK → User, NOT NULL, ON DELETE CASCADE | Sinh viên làm quiz. |
| enrollment_id | UUID | FK → Enrollment, NOT NULL, ON DELETE CASCADE | Enrollment của sinh viên. |
| attempt_number | INTEGER | NOT NULL | Lần thử thứ mấy (1, 2, 3...). |
| status | VARCHAR(20) | DEFAULT 'IN_PROGRESS', CHECK | Trạng thái: IN_PROGRESS, SUBMITTED, GRADED, PENDING_GRADING. |
| started_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Thời gian bắt đầu. |
| submitted_at | TIMESTAMP | | Thời gian nộp. |
| score | DECIMAL(10,2) | | Điểm số. |
| max_score | DECIMAL(10,2) | | Điểm tối đa. |
| answers | JSON | DEFAULT '[]' | Câu trả lời của sinh viên (JSON array). |
| | | UNIQUE(user_id, quiz_id, attempt_number) | Không trùng lần thử. |

**Cấu trúc answers JSON:**
```json
[
  {
    "question_id": "uuid-1",
    "answer_text": null,
    "selected_options": ["option-uuid-a"],
    "score": 10,
    "max_score": 10,
    "is_correct": true,
    "graded_at": "2025-11-27T10:45:00Z"
  },
  {
    "question_id": "uuid-2",
    "answer_text": "Essay answer here...",
    "selected_options": null,
    "score": 12.5,
    "max_score": 15,
    "is_correct": null,
    "feedback": "Good answer...",
    "graded_at": "2025-11-27T11:00:00Z"
  }
]
```

---

**− Bảng: AssignmentSubmission**

| Trường | Kiểu dữ liệu | Ràng buộc | Mô tả |
|--------|--------------|-----------|-------|
| submission_id | UUID | PK | Khóa chính. |
| lecture_id | UUID | FK → Lecture, NOT NULL, ON DELETE CASCADE | Lecture (type=ASSIGNMENT) được nộp. |
| user_id | UUID | FK → User, NOT NULL, ON DELETE CASCADE | Sinh viên nộp bài. |
| enrollment_id | UUID | FK → Enrollment, NOT NULL, ON DELETE CASCADE | Enrollment của sinh viên. |
| submission_number | INTEGER | NOT NULL | Lần nộp thứ mấy. |
| status | VARCHAR(20) | DEFAULT 'DRAFT', CHECK | Trạng thái: DRAFT, SUBMITTED, GRADED, PENDING_GRADING, LATE. |
| submitted_at | TIMESTAMP | | Thời gian nộp. |
| score | DECIMAL(10,2) | | Điểm số. |
| max_score | DECIMAL(10,2) | | Điểm tối đa (từ assignment_config.max_points). |
| feedback | TEXT | | Phản hồi của giảng viên. |
| file_urls | JSON | DEFAULT '[]' | Danh sách URL file đã upload. |
| | | UNIQUE(lecture_id, user_id, submission_number) | Không trùng lần nộp. |

**Cấu trúc file_urls JSON:**
```json
[
  "https://s3.amazonaws.com/blearning/submissions/file1.zip",
  "https://s3.amazonaws.com/blearning/submissions/file2.pdf"
]
```

---

#### 3.3.4. Domain 4: Quản lý đăng ký và tiến độ

**− Bảng: Enrollment**

| Trường | Kiểu dữ liệu | Ràng buộc | Mô tả |
|--------|--------------|-----------|-------|
| enrollment_id | UUID | PK | Khóa chính. |
| user_id | UUID | FK → User, NOT NULL, ON DELETE CASCADE | Sinh viên đăng ký. |
| course_id | UUID | FK → Course, NOT NULL, ON DELETE CASCADE | Khóa học được đăng ký. |
| class_id | UUID | FK → Class, ON DELETE SET NULL | Lớp học (NULL = self-paced). |
| status | VARCHAR(20) | DEFAULT 'ACTIVE', CHECK | Trạng thái: ACTIVE, COMPLETED, DROPPED, SUSPENDED. |
| enrolled_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Thời gian đăng ký. |
| completed_at | TIMESTAMP | | Thời gian hoàn thành. |
| | | UNIQUE(user_id, course_id, COALESCE(class_id, UUID_NIL)) | Không trùng enrollment. |

**Đặc điểm quan trọng:**
- `class_id` nullable cho phép 2 mode học tập:
  - NULL = Self-paced learning (học theo tiến độ cá nhân)
  - UUID = Class-based/Blended learning (học theo lớp)

---

**− Bảng: Progress**

| Trường | Kiểu dữ liệu | Ràng buộc | Mô tả |
|--------|--------------|-----------|-------|
| progress_id | UUID | PK | Khóa chính. |
| user_id | UUID | FK → User, NOT NULL, ON DELETE CASCADE | Sinh viên. |
| course_id | UUID | FK → Course, NOT NULL, ON DELETE CASCADE | Khóa học. |
| module_id | UUID | FK → Module, NOT NULL, ON DELETE CASCADE | Module đang học. |
| status | VARCHAR(20) | DEFAULT 'NOT_STARTED', CHECK | Trạng thái: NOT_STARTED, IN_PROGRESS, COMPLETED. |
| completion_percentage | INTEGER | DEFAULT 0, CHECK (0-100) | Tỷ lệ hoàn thành module (%). |
| started_at | TIMESTAMP | | Thời gian bắt đầu học. |
| completed_at | TIMESTAMP | | Thời gian hoàn thành. |
| | | UNIQUE(user_id, course_id, module_id) | Không trùng progress. |

---

#### 3.3.5. Domain 5: Quản lý lớp học và chứng chỉ

**− Bảng: Class**

| Trường | Kiểu dữ liệu | Ràng buộc | Mô tả |
|--------|--------------|-----------|-------|
| class_id | UUID | PK | Khóa chính. |
| course_id | UUID | FK → Course, NOT NULL, ON DELETE CASCADE | Khóa học tổ chức lớp. |
| class_name | VARCHAR(100) | NOT NULL | Tên lớp (VD: "CNTT1-K63"). |
| start_date | DATE | NOT NULL | Ngày bắt đầu lớp. |
| end_date | DATE | NOT NULL, CHECK (>= start_date) | Ngày kết thúc lớp. |
| instructor_id | UUID | FK → User, ON DELETE SET NULL | Giảng viên phụ trách. |
| max_students | INTEGER | | Số sinh viên tối đa. |
| status | VARCHAR(20) | DEFAULT 'SCHEDULED', CHECK | Trạng thái: SCHEDULED, ONGOING, COMPLETED, CANCELLED. |
| schedules | JSON | DEFAULT '[]' | Lịch học và điểm danh (JSON array). |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Thời gian tạo. |

**Cấu trúc schedules JSON:**
```json
[
  {
    "session_id": "uuid",
    "date": "2025-12-01",
    "start_time": "09:00",
    "end_time": "11:00",
    "topic": "Chương 1: Giới thiệu",
    "location": "Phòng 101",
    "type": "IN_PERSON",
    "attendances": [
      {"user_id": "uuid-1", "status": "PRESENT", "check_in": "09:05"},
      {"user_id": "uuid-2", "status": "LATE", "check_in": "09:15"},
      {"user_id": "uuid-3", "status": "ABSENT"}
    ]
  }
]
```

---

**− Bảng: Certificate**

| Trường | Kiểu dữ liệu | Ràng buộc | Mô tả |
|--------|--------------|-----------|-------|
| certificate_id | UUID | PK | Khóa chính. |
| user_id | UUID | FK → User, NOT NULL, ON DELETE CASCADE | Sinh viên nhận certificate. |
| course_id | UUID | FK → Course, NOT NULL, ON DELETE CASCADE | Khóa học hoàn thành. |
| certificate_code | VARCHAR(50) | UNIQUE, NOT NULL | Mã chứng chỉ (VD: "BL-2025-000001"). |
| verification_code | VARCHAR(100) | UNIQUE, NOT NULL | Mã xác minh (UUID-based). |
| issue_date | DATE | NOT NULL | Ngày cấp. |
| final_grade | DECIMAL(5,2) | | Điểm tổng kết. |
| pdf_url | VARCHAR(500) | | URL file PDF certificate. |
| status | VARCHAR(20) | DEFAULT 'ACTIVE', CHECK | Trạng thái: ACTIVE, REVOKED. |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Thời gian tạo. |
| | | UNIQUE(user_id, course_id) | Một sinh viên chỉ có một certificate cho một khóa học. |

---

#### 3.3.6. Tổng hợp Indexes

| Loại Index | Số lượng | Mục đích |
|------------|----------|----------|
| Primary Keys | 16 | B-tree indexes tự động cho PK |
| Foreign Keys | 28 | Tăng tốc JOIN operations |
| Performance | 26 | Tối ưu truy vấn phổ biến |
| JSON GIN | 8 | Fast JSON queries với @>, ->> |
| Full-Text Search | 5 | tsvector-based search |
| Composite | 7 | Multi-column queries |
| Partial | 6 | Filtered indexes |
| **TOTAL** | **96** | |

**GIN Indexes cho JSON:**
```sql
CREATE INDEX idx_user_preferences ON "User" USING GIN (preferences);
CREATE INDEX idx_quiz_questions ON "Quiz" USING GIN (questions);
CREATE INDEX idx_attempt_answers ON "Attempt" USING GIN (answers);
CREATE INDEX idx_class_schedules ON "Class" USING GIN (schedules);
CREATE INDEX idx_lecture_assignment_config ON "Lecture" USING GIN (assignment_config);
```

---

#### 3.3.7. Tổng hợp Constraints

| Loại Constraint | Số lượng | Ví dụ |
|-----------------|----------|-------|
| Primary Keys | 16 | Tất cả tables |
| Foreign Keys | 27 | Tất cả relationships |
| Unique | 14 | email, course.code, certificate.code |
| Check | 47 | Status enums, ranges, formats |
| Not Null | 120+ | Required fields |
| **TOTAL** | **224+** | |

---

# CHƯƠNG 2: ĐẶC TẢ YÊU CẦU CHỨC NĂNG

## 1. Đặc tả chức năng: Quản lý khóa học (Course Management)

### 1.1. Giao diện Form và thành phần

**− Tên Form:** Form Quản lý Khóa học (Course Management Form)

**− Canvas chính (Chi tiết khóa học):** Chứa các trường nhập/chỉnh sửa thông tin Course.

**− Canvas phụ (Danh sách Module):** Hiển thị danh sách Module của khóa học theo order_num, cho phép thêm/sửa/xóa Module.

**− Windows:** Pop-up thông báo lỗi/thành công khi thực hiện thao tác.

| Thành phần | Loại điều khiển | Trường dữ liệu | Bảng và kiểu dữ liệu | Thao tác nhập/hiển thị |
|------------|-----------------|----------------|----------------------|------------------------|
| ID Khóa học | Text (Readonly) | course_id | Course (UUID) | Hiển thị mã khóa học (UUID). Tự động sinh khi tạo mới. |
| Mã khóa học | Text Input | code | Course (VARCHAR(20)) | Nhập/Sửa mã khóa học. Validate: ^[A-Z0-9]{3,10}$. Unique. |
| Tên khóa học | Text Input | title | Course (VARCHAR(255)) | Nhập/Sửa tên khóa học. Required. |
| Mô tả | Text Area | description | Course (TEXT) | Nhập/Sửa mô tả chi tiết (Tùy chọn). |
| Độ khó | Dropdown | difficulty_level | Course (VARCHAR(20)) | Chọn: BEGINNER, INTERMEDIATE, ADVANCED. |
| Số tín chỉ | Number Input | credits | Course (INTEGER) | Nhập số tín chỉ (Tùy chọn). |
| Trạng thái | Dropdown | status | Course (VARCHAR(20)) | Hiển thị/Chọn: DRAFT, PUBLISHED, ARCHIVED. |
| Giảng viên | Text (Readonly) | created_by | Course (UUID) → User | Hiển thị tên giảng viên (first_name + last_name từ User table). |
| Ngày tạo | Text (Readonly) | created_at | Course (TIMESTAMP) | Hiển thị ngày tạo (format: DD/MM/YYYY HH:MM). |
| Danh sách Module | Data Grid | N/A | Module | Hiển thị danh sách Module với các cột: title, order_num, estimated_duration_minutes. Cho phép thêm/sửa/xóa Module. |
| Nút Lưu | Button | N/A | N/A | Kích hoạt truy vấn UPDATE Course. |
| Nút Xuất bản | Button | N/A | N/A | Thay đổi status = 'PUBLISHED'. Chỉ active khi status = 'DRAFT'. |
| Nút Archive | Button | N/A | N/A | Thay đổi status = 'ARCHIVED'. Chỉ active khi status = 'PUBLISHED'. |
| Nút Thêm Module | Button | N/A | N/A | Mở form thêm Module mới. |

---

### 1.2. Truy vấn dữ liệu

| Loại truy vấn | Bảng | Trường | Mô tả/SQL |
|---------------|------|--------|-----------|
| Hiển thị chi tiết Course | Course | course_id, code, title, description, difficulty_level, credits, status, created_by, created_at, updated_at | Hiển thị dữ liệu hiện tại của khóa học để Instructor chỉnh sửa. |
| Lấy tên Instructor | User | first_name, last_name | JOIN với Course.created_by để hiển thị tên giảng viên. |
| Danh sách Module | Module | module_id, title, order_num, estimated_duration_minutes, prerequisite_module_ids | Hiển thị danh sách Module thuộc khóa học. |
| Danh sách Lecture | Lecture | lecture_id, title, type, order_num | Hiển thị Lecture khi click vào Module. |

**SQL mẫu - Lấy chi tiết Course:**
```sql
SELECT
  c.course_id,
  c.code,
  c.title,
  c.description,
  c.difficulty_level,
  c.credits,
  c.status,
  c.created_at,
  c.updated_at,
  u.first_name || ' ' || u.last_name AS instructor_name
FROM "Course" c
LEFT JOIN "User" u ON c.created_by = u.user_id
WHERE c.course_id = $1;
```

**SQL mẫu - Lấy danh sách Module:**
```sql
SELECT
  module_id,
  title,
  order_num,
  estimated_duration_minutes,
  prerequisite_module_ids
FROM "Module"
WHERE course_id = $1
ORDER BY order_num ASC;
```

---

### 1.3. Cập nhật dữ liệu

| Bảng cập nhật | Lệnh SQL | Điều kiện/Mô tả |
|---------------|----------|-----------------|
| Course (Cập nhật) | `UPDATE "Course" SET code = $1, title = $2, description = $3, difficulty_level = $4, credits = $5, updated_at = CURRENT_TIMESTAMP WHERE course_id = $6` | WHERE course_id = [ID Khóa học] AND created_by = [user_id hiện tại]. Chỉ instructor sở hữu mới được cập nhật. |
| Course (Xuất bản) | `UPDATE "Course" SET status = 'PUBLISHED', updated_at = CURRENT_TIMESTAMP WHERE course_id = $1 AND status = 'DRAFT'` | Chỉ cho phép xuất bản khi status = 'DRAFT'. |
| Course (Archive) | `UPDATE "Course" SET status = 'ARCHIVED', updated_at = CURRENT_TIMESTAMP WHERE course_id = $1 AND status = 'PUBLISHED'` | Chỉ cho phép archive khi status = 'PUBLISHED'. |
| Module (Thêm mới) | `INSERT INTO "Module" (module_id, course_id, title, order_num, estimated_duration_minutes, prerequisite_module_ids) VALUES (gen_random_uuid(), $1, $2, $3, $4, $5)` | order_num phải unique trong course. |
| Module (Cập nhật) | `UPDATE "Module" SET title = $1, order_num = $2, estimated_duration_minutes = $3, prerequisite_module_ids = $4, updated_at = CURRENT_TIMESTAMP WHERE module_id = $5` | Validate prerequisite_module_ids chứa UUID hợp lệ. |
| Module (Xóa) | `DELETE FROM "Module" WHERE module_id = $1` | CASCADE sẽ xóa Lecture, Resource liên quan. |

---

### 1.4. Tiền và hậu điều kiện

**− Tiền điều kiện:**

• **Authentication & Authorization:**
  o Người dùng phải đăng nhập (có valid session/token).
  o UserRole.role_id tương ứng với Role.name = 'INSTRUCTOR' hoặc 'ADMIN'.

• **Data Validation:**
  o course_id phải tồn tại trong bảng Course.
  o Khóa học phải thuộc về giảng viên này: Course.created_by = User.user_id (hoặc có quyền ADMIN).
  o Course.code phải unique và match pattern ^[A-Z0-9]{3,10}$.
  o Course.title không được rỗng (NOT NULL).

• **Business Rules:**
  o Xuất bản: Course.status phải là 'DRAFT'.
  o Archive: Course.status phải là 'PUBLISHED'.
  o Module.order_num phải unique trong một Course.
  o Module.prerequisite_module_ids phải chứa UUID hợp lệ thuộc cùng course_id.

**− Hậu điều kiện:**

• **Nếu Transaction thành công:**
  o Các trường dữ liệu trong bảng Course được cập nhật đúng.
  o Course.updated_at = CURRENT_TIMESTAMP.
  o Nếu xuất bản: status = 'PUBLISHED', sinh viên có thể thấy và enroll.
  o Module được thêm/sửa/xóa theo yêu cầu.
  o Hiển thị thông báo: "Khóa học đã được cập nhật thành công." / "Khóa học đã được xuất bản."

• **Nếu Transaction thất bại:**
  o ROLLBACK - không có dữ liệu nào thay đổi.
  o Hiển thị thông báo lỗi tương ứng:
    - "Mã khóa học đã tồn tại. Vui lòng chọn mã khác."
    - "Bạn không có quyền chỉnh sửa khóa học này."
    - "Không thể xuất bản. Khóa học không ở trạng thái Draft."
    - "Lỗi kết nối cơ sở dữ liệu. Vui lòng thử lại sau."

**− Ràng buộc nghiệp vụ:**

• **Cascade Delete:**
  o Khi xóa Module → CASCADE xóa tất cả Lecture, Resource thuộc Module đó.
  o Khi xóa Course → CASCADE xóa Module, Lecture, Resource, Quiz, Question.

• **Restrict Delete:**
  o KHÔNG thể xóa Course nếu có Enrollment tồn tại (protect student data).
  o Query kiểm tra: `SELECT COUNT(*) FROM "Enrollment" WHERE course_id = $1`.

• **Status Transitions:**
  o DRAFT → PUBLISHED (xuất bản)
  o PUBLISHED → ARCHIVED (archive)
  o ARCHIVED → không thể chuyển trạng thái (immutable)

---

## 2. Đặc tả chức năng: Nộp bài tập (Submission Management)

### 2.1. Giao diện Form và Thành phần

**− Tên Form:** Form Nộp bài tập (Assignment Submission Form)

**− Canvas 1 (Chi tiết bài tập):** Hiển thị thông tin Assignment từ Lecture.assignment_config.

**− Canvas 2 (Nộp bài):** Cho phép sinh viên upload file và/hoặc nhập text.

**− Windows:** Pop-up xác nhận nộp bài / Thông báo lỗi upload file.

| Thành phần | Loại điều khiển | Trường dữ liệu | Bảng và kiểu dữ liệu | Thao tác nhập/hiển thị |
|------------|-----------------|----------------|----------------------|------------------------|
| Tên bài tập | Text (Readonly) | title | Lecture (VARCHAR(255)) | Hiển thị tên bài tập từ Lecture.title. |
| Hướng dẫn | Text Area (Readonly) | assignment_config.instructions | Lecture (JSON) | Hiển thị hướng dẫn làm bài từ JSON. |
| Hạn nộp | Text (Readonly) | assignment_config.due_date | Lecture (JSON) | Hiển thị deadline (format: DD/MM/YYYY HH:MM). |
| Điểm tối đa | Text (Readonly) | assignment_config.max_points | Lecture (JSON) | Hiển thị điểm tối đa. |
| Loại file cho phép | Text (Readonly) | assignment_config.allowed_file_types | Lecture (JSON) | Hiển thị danh sách extensions: .java, .py, .pdf. |
| Kích thước tối đa | Text (Readonly) | assignment_config.max_file_size_mb | Lecture (JSON) | Hiển thị giới hạn: "10 MB". |
| File đính kèm | File Upload (Multiple) | file_urls | AssignmentSubmission (JSON) | Upload nhiều file. Validate type và size. |
| Nội dung text | Text Area | submission_text | (App layer) | Nhập nội dung text nếu submission_types includes 'text'. Lưu trong file hoặc submission metadata. |
| Trạng thái nộp | Text (Readonly) | status | AssignmentSubmission (VARCHAR(20)) | Hiển thị: DRAFT, SUBMITTED, GRADED, LATE. |
| Lần nộp | Text (Readonly) | submission_number | AssignmentSubmission (INTEGER) | Hiển thị số lần nộp hiện tại. |
| Thời gian nộp | Text (Readonly) | submitted_at | AssignmentSubmission (TIMESTAMP) | Hiển thị thời gian nộp bài. |
| Điểm số | Text (Readonly) | score | AssignmentSubmission (DECIMAL(10,2)) | Hiển thị điểm (nếu status = 'GRADED'). |
| Phản hồi | Text Area (Readonly) | feedback | AssignmentSubmission (TEXT) | Hiển thị feedback của giảng viên. |
| Nút Lưu nháp | Button | N/A | N/A | Lưu submission với status = 'DRAFT'. |
| Nút Nộp bài | Button | N/A | N/A | Submit với status = 'SUBMITTED' hoặc 'LATE'. |

---

### 2.2. Truy vấn dữ liệu

| Loại truy vấn | Bảng | Trường | Mô tả |
|---------------|------|--------|-------|
| Thông tin Assignment | Lecture | lecture_id, title, assignment_config | Lấy thông tin bài tập. Filter: type = 'ASSIGNMENT'. |
| Kiểm tra Enrollment | Enrollment | enrollment_id, user_id, course_id, status | Xác nhận sinh viên đã enroll khóa học chứa Assignment. |
| Module → Course mapping | Module | module_id, course_id | JOIN để tìm course_id từ lecture_id. |
| Submission hiện tại | AssignmentSubmission | submission_id, status, submitted_at, score, feedback, file_urls | Kiểm tra submission trước đó của sinh viên. |

**SQL mẫu - Lấy thông tin Assignment:**
```sql
SELECT
  l.lecture_id,
  l.title,
  l.assignment_config,
  m.course_id
FROM "Lecture" l
JOIN "Module" m ON l.module_id = m.module_id
WHERE l.lecture_id = $1 AND l.type = 'ASSIGNMENT';
```

**SQL mẫu - Kiểm tra Enrollment:**
```sql
SELECT e.enrollment_id, e.status
FROM "Enrollment" e
JOIN "Module" m ON e.course_id = m.course_id
JOIN "Lecture" l ON l.module_id = m.module_id
WHERE l.lecture_id = $1
  AND e.user_id = $2
  AND e.status = 'ACTIVE';
```

**SQL mẫu - Lấy Submission hiện tại:**
```sql
SELECT
  submission_id,
  submission_number,
  status,
  submitted_at,
  score,
  feedback,
  file_urls
FROM "AssignmentSubmission"
WHERE lecture_id = $1
  AND user_id = $2
ORDER BY submission_number DESC
LIMIT 1;
```

---

### 2.3. Cập nhật dữ liệu

| Bảng cập nhật | Lệnh SQL | Mục đích |
|---------------|----------|----------|
| AssignmentSubmission (Tạo mới) | `INSERT INTO "AssignmentSubmission" (submission_id, lecture_id, user_id, enrollment_id, submission_number, status, file_urls) VALUES (gen_random_uuid(), $1, $2, $3, $4, 'DRAFT', $5)` | Tạo submission mới với status = DRAFT. |
| AssignmentSubmission (Submit) | `UPDATE "AssignmentSubmission" SET status = CASE WHEN CURRENT_TIMESTAMP > $1 THEN 'LATE' ELSE 'SUBMITTED' END, submitted_at = CURRENT_TIMESTAMP, max_score = $2 WHERE submission_id = $3` | Submit bài tập. Check due_date để set LATE. |
| AssignmentSubmission (Cập nhật file) | `UPDATE "AssignmentSubmission" SET file_urls = $1 WHERE submission_id = $2 AND status = 'DRAFT'` | Cập nhật file_urls khi còn ở DRAFT. |

**Logic xác định status:**
```sql
-- Pseudo-code
IF current_timestamp > assignment_config.due_date THEN
  status = 'LATE'
ELSE
  status = 'SUBMITTED'
END IF

-- Sau khi giảng viên chấm:
UPDATE "AssignmentSubmission"
SET status = 'GRADED', score = $1, feedback = $2
WHERE submission_id = $3;
```

---

### 2.4. Tiền và hậu điều kiện

**− Tiền điều kiện:**

• **Authentication & Authorization:**
  o Người dùng phải đăng nhập với role = 'STUDENT'.
  o User.account_status = 'ACTIVE'.

• **Data Validation:**
  o lecture_id phải tồn tại và Lecture.type = 'ASSIGNMENT'.
  o Lecture.assignment_config phải NOT NULL.
  o Sinh viên phải đã enroll khóa học chứa Assignment:
    ```sql
    SELECT COUNT(*) FROM "Enrollment" e
    JOIN "Module" m ON e.course_id = m.course_id
    JOIN "Lecture" l ON l.module_id = m.module_id
    WHERE l.lecture_id = $1 AND e.user_id = $2 AND e.status = 'ACTIVE';
    -- Result must be > 0
    ```
  o Enrollment.status = 'ACTIVE' (không phải DROPPED/SUSPENDED).

• **File Validation:**
  o File extension phải trong assignment_config.allowed_file_types.
  o File size <= assignment_config.max_file_size_mb * 1024 * 1024 bytes.
  o Số lượng file <= giới hạn (default: 5 files).

• **Submission Rules:**
  o Nếu đã có submission với status = 'GRADED': không cho phép nộp lại.
  o Nếu status = 'DRAFT' hoặc 'SUBMITTED' (chưa chấm): cho phép cập nhật/nộp lại.

**− Hậu điều kiện:**

• **Nếu Transaction thành công:**
  o AssignmentSubmission được INSERT (lần đầu) hoặc UPDATE (nộp lại).
  o File được upload lên S3/GCS, URL lưu vào file_urls JSON.
  o Status được set đúng:
    - 'DRAFT': khi chỉ lưu nháp
    - 'SUBMITTED': khi nộp đúng hạn
    - 'LATE': khi nộp sau due_date
  o submitted_at = CURRENT_TIMESTAMP.
  o max_score = assignment_config.max_points.
  o Hiển thị thông báo: "Bài tập đã được nộp thành công."
  o (Optional) Tạo Notification cho Instructor về submission mới.

• **Nếu Transaction thất bại:**
  o ROLLBACK - không có dữ liệu thay đổi.
  o File không được lưu lên storage.
  o Hiển thị thông báo lỗi tương ứng:
    - "Bạn chưa đăng ký khóa học này."
    - "File không đúng định dạng. Chỉ chấp nhận: .java, .py, .pdf"
    - "File quá lớn. Kích thước tối đa: 10 MB"
    - "Bài tập đã được chấm điểm, không thể nộp lại."
    - "Lỗi upload file. Vui lòng thử lại sau."

**− Ràng buộc nghiệp vụ:**

• **Unique Constraint:**
  o UNIQUE(lecture_id, user_id, submission_number): một sinh viên chỉ có một submission cho mỗi lần nộp.
  o Nếu cho phép nộp lại: tăng submission_number.

• **Late Submission Policy:**
  o Hệ thống vẫn cho phép nộp sau due_date.
  o Status = 'LATE' để giảng viên biết.
  o (Optional) Áp dụng penalty: score = score * (1 - late_penalty_percentage).

• **Grading Lock:**
  o Sau khi status = 'GRADED', submission bị lock.
  o Sinh viên không thể sửa hoặc nộp lại.
  o Chỉ Instructor có thể unlock bằng cách set score = NULL.

**− Validation (Application Layer):**

```python
def validate_submission(lecture_id: UUID, user_id: UUID, files: List[File]):
    """
    Validate submission trước khi lưu vào database.
    """
    # 1. Lấy thông tin Assignment
    lecture = db.query("""
        SELECT l.*, m.course_id
        FROM "Lecture" l
        JOIN "Module" m ON l.module_id = m.module_id
        WHERE l.lecture_id = %s AND l.type = 'ASSIGNMENT'
    """, lecture_id)

    if not lecture:
        raise ValidationError("Bài tập không tồn tại.")

    assignment_config = lecture.assignment_config

    # 2. Kiểm tra Enrollment
    enrollment = db.query("""
        SELECT e.* FROM "Enrollment" e
        WHERE e.course_id = %s AND e.user_id = %s AND e.status = 'ACTIVE'
    """, lecture.course_id, user_id)

    if not enrollment:
        raise ValidationError("Bạn chưa đăng ký khóa học này.")

    # 3. Kiểm tra submission cũ
    existing = db.query("""
        SELECT * FROM "AssignmentSubmission"
        WHERE lecture_id = %s AND user_id = %s
        ORDER BY submission_number DESC LIMIT 1
    """, lecture_id, user_id)

    if existing and existing.status == 'GRADED':
        raise ValidationError("Bài tập đã được chấm điểm, không thể nộp lại.")

    # 4. Validate files
    allowed_types = assignment_config.get('allowed_file_types', ['.pdf', '.doc', '.docx'])
    max_size_mb = assignment_config.get('max_file_size_mb', 10)
    max_size_bytes = max_size_mb * 1024 * 1024

    for file in files:
        ext = '.' + file.filename.split('.')[-1].lower()
        if ext not in allowed_types:
            raise ValidationError(
                f"File {file.filename} không đúng định dạng. "
                f"Chỉ chấp nhận: {', '.join(allowed_types)}"
            )

        if file.size > max_size_bytes:
            raise ValidationError(
                f"File {file.filename} quá lớn. Kích thước tối đa: {max_size_mb} MB"
            )

    # 5. Check deadline (warning only)
    due_date = assignment_config.get('due_date')
    is_late = False
    if due_date and datetime.now() > datetime.fromisoformat(due_date):
        is_late = True

    return {
        'valid': True,
        'enrollment_id': enrollment.enrollment_id,
        'is_late': is_late,
        'submission_number': (existing.submission_number + 1) if existing else 1,
        'max_score': assignment_config.get('max_points', 100)
    }
```

---

## PHỤ LỤC

### A. Danh sách CHECK Constraints

```sql
-- User
CONSTRAINT chk_user_account_status CHECK (
  account_status IN ('PENDING_VERIFICATION', 'ACTIVE', 'SUSPENDED', 'DELETED')
)

-- Course
CONSTRAINT chk_course_status CHECK (status IN ('DRAFT', 'PUBLISHED', 'ARCHIVED'))
CONSTRAINT chk_course_difficulty CHECK (
  difficulty_level IN ('BEGINNER', 'INTERMEDIATE', 'ADVANCED')
)
CONSTRAINT chk_course_code_format CHECK (code ~ '^[A-Z0-9]{3,10}$')

-- Lecture
CONSTRAINT chk_lecture_type CHECK (
  type IN ('VIDEO', 'PDF', 'SLIDE', 'AUDIO', 'TEXT', 'ASSIGNMENT')
)

-- Question
CONSTRAINT chk_question_type CHECK (
  type IN ('MCQ', 'TRUE_FALSE', 'ESSAY', 'SHORT_ANSWER')
)

-- Quiz
CONSTRAINT chk_quiz_status CHECK (status IN ('DRAFT', 'PUBLISHED', 'ARCHIVED'))

-- Attempt
CONSTRAINT chk_attempt_status CHECK (
  status IN ('IN_PROGRESS', 'SUBMITTED', 'GRADED', 'PENDING_GRADING')
)

-- AssignmentSubmission
CONSTRAINT chk_assignment_status CHECK (
  status IN ('DRAFT', 'SUBMITTED', 'GRADED', 'PENDING_GRADING', 'LATE')
)

-- Enrollment
CONSTRAINT chk_enrollment_status CHECK (
  status IN ('ACTIVE', 'COMPLETED', 'DROPPED', 'SUSPENDED')
)

-- Progress
CONSTRAINT chk_progress_status CHECK (
  status IN ('NOT_STARTED', 'IN_PROGRESS', 'COMPLETED')
)
CONSTRAINT chk_progress_percentage CHECK (
  completion_percentage >= 0 AND completion_percentage <= 100
)

-- Class
CONSTRAINT chk_class_status CHECK (
  status IN ('SCHEDULED', 'ONGOING', 'COMPLETED', 'CANCELLED')
)
CONSTRAINT chk_class_dates CHECK (end_date >= start_date)

-- Certificate
CONSTRAINT chk_certificate_status CHECK (status IN ('ACTIVE', 'REVOKED'))
```

### B. Danh sách ON DELETE Behaviors

| Bảng | FK Column | Target | ON DELETE | Lý do |
|------|-----------|--------|-----------|-------|
| UserRole | user_id | User | CASCADE | User xóa → roles xóa |
| UserRole | role_id | Role | CASCADE | Role xóa → assignments xóa |
| Course | created_by | User | SET NULL | Giữ course khi instructor xóa |
| Module | course_id | Course | CASCADE | Course xóa → modules xóa |
| Lecture | module_id | Module | CASCADE | Module xóa → lectures xóa |
| Resource | lecture_id | Lecture | CASCADE | Lecture xóa → resources xóa |
| Quiz | course_id | Course | CASCADE | Course xóa → quizzes xóa |
| Quiz | created_by | User | SET NULL | Giữ quiz khi instructor xóa |
| Question | course_id | Course | CASCADE | Course xóa → questions xóa |
| Option | question_id | Question | CASCADE | Question xóa → options xóa |
| Attempt | quiz_id | Quiz | CASCADE | Quiz xóa → attempts xóa |
| Attempt | user_id | User | CASCADE | User xóa → attempts xóa |
| Attempt | enrollment_id | Enrollment | CASCADE | Enrollment xóa → attempts xóa |
| AssignmentSubmission | lecture_id | Lecture | CASCADE | Lecture xóa → submissions xóa |
| AssignmentSubmission | enrollment_id | Enrollment | CASCADE | Enrollment xóa → submissions xóa |
| Enrollment | user_id | User | CASCADE | User xóa → enrollments xóa |
| Enrollment | course_id | Course | CASCADE | Course xóa → enrollments xóa |
| Enrollment | class_id | Class | SET NULL | Class xóa → enrollment giữ (self-paced) |
| Progress | module_id | Module | CASCADE | Module xóa → progress xóa |
| Class | course_id | Course | CASCADE | Course xóa → classes xóa |
| Class | instructor_id | User | SET NULL | Giữ class khi instructor xóa |
| Certificate | user_id | User | CASCADE | User xóa → certificates xóa |
| Certificate | course_id | Course | CASCADE | Course xóa → certificates xóa |

---

**KẾT THÚC BÁO CÁO**

---

```
Ngày hoàn thành: 02/12/2025
Phiên bản: v2.0
Người lập: Nguyễn Văn Kiệt - CNTT1-K63
```
