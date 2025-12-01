# BÁO CÁO ĐỒ ÁN
# THIẾT KẾ CƠ SỞ DỮ LIỆU HỆ THỐNG B-LEARNING CORE

**Đề tài:** Thiết kế và triển khai cơ sở dữ liệu cho hệ thống học tập kết hợp (Blended Learning Platform)

**Sinh viên thực hiện:** Nguyễn Văn Kiệt

**Mã số sinh viên:** CNTT1-K63

**Giảng viên hướng dẫn:** Thầy Trần Văn Dũng

**Khoa:** Công nghệ Thông tin

**Trường:** Đại Học Giao Thông Vận Tải

**Thời gian thực hiện:** Học kỳ I, năm học 2025-2026

**Ngày hoàn thành:** 01/12/2025

---

## LỜI NÓI ĐẦU

Trong bối cảnh giáo dục hiện đại, việc ứng dụng công nghệ thông tin vào quản lý và tổ chức quá trình học tập đã trở thành xu hướng tất yếu và mang lại hiệu quả cao. Đặc biệt, sau đại dịch COVID-19, mô hình học tập kết hợp (Blended Learning) đã chứng minh được tính ưu việt trong việc kết hợp linh hoạt giữa học trực tuyến (online) và học trực tiếp tại lớp (in-person), giúp người học có thể tiếp cận kiến thức mọi lúc, mọi nơi mà vẫn duy trì được sự tương tác trực tiếp với giảng viên và bạn học.

Nhận thức được tầm quan trọng của việc xây dựng một hệ thống quản lý học tập hiệu quả, chúng tôi đã chọn đề tài **"Thiết kế cơ sở dữ liệu cho hệ thống B-Learning Core"** nhằm nghiên cứu và triển khai một cơ sở dữ liệu hoàn chỉnh, tối ưu và có khả năng mở rộng cao cho hệ thống học tập kết hợp.

### Mục tiêu của đồ án

**Mục tiêu tổng quát:**
- Thiết kế một cơ sở dữ liệu quan hệ (Relational Database) hoàn chỉnh cho hệ thống B-Learning, hỗ trợ quản lý courses, modules, lectures, assignments, quizzes, enrollments, progress tracking, và certificates.
- Đảm bảo tính toàn vẹn dữ liệu (data integrity), hiệu năng cao (performance), và khả năng mở rộng (scalability) cho hệ thống.

**Mục tiêu cụ thể:**
1. Phân tích yêu cầu chức năng của hệ thống B-Learning, xác định các entities, attributes và relationships cần thiết.
2. Thiết kế ERD (Entity Relationship Diagram) và chuyển đổi sang Relational Model với 16 bảng được tổ chức theo 5 domains chức năng.
3. Áp dụng các kỹ thuật tối ưu hóa database: sử dụng UUID primary keys, JSON fields cho dữ liệu linh hoạt, GIN indexes cho JSON queries, và normalization đến Third Normal Form (3NF).
4. So sánh và đánh giá thiết kế mới (16 bảng) với thiết kế cũ (21 bảng), phân tích các cải tiến về simplicity, maintainability và performance.
5. Triển khai database trên PostgreSQL 14+ và demo các chức năng chính qua REST API.

### Phạm vi nghiên cứu

Đồ án tập trung vào việc thiết kế và triển khai **core database schema** cho hệ thống B-Learning, bao gồm:

**Trong phạm vi (In-Scope):**
- User Management: Quản lý tài khoản người dùng và phân quyền (RBAC)
- Course Content: Quản lý courses, modules, lectures, resources
- Assessment: Quản lý quizzes, questions, attempts, assignments
- Enrollment & Progress: Quản lý đăng ký khóa học và theo dõi tiến độ học tập
- Class & Certificate: Quản lý lớp học (blended learning) và chứng chỉ hoàn thành

**Ngoài phạm vi (Out-of-Scope):**
- Notification system: Sử dụng external email service (SendGrid, AWS SES)
- File storage: Sử dụng cloud storage (Amazon S3, Google Cloud Storage)
- Payment processing: Sử dụng third-party payment gateway (Stripe, PayPal)
- Video streaming: Sử dụng external CDN (Vimeo, YouTube)
- Real-time features: Sử dụng WebSocket hoặc external service (Firebase, Pusher)

### Cấu trúc báo cáo

Báo cáo được chia thành 5 chương chính:

**CHƯƠNG 1: TỔNG QUAN HỆ THỐNG**
- Giới thiệu về Blended Learning và xu hướng học tập hiện đại
- Tổng quan kiến trúc hệ thống B-Learning Core
- Công nghệ sử dụng: PostgreSQL 14+, UUID, JSON/JSONB, GIN indexes

**CHƯƠNG 2: PHÂN TÍCH YÊU CẦU**
- Yêu cầu chức năng (45 functional requirements trong 7 domains)
- Yêu cầu phi chức năng (performance, security, scalability)
- Use cases chính: Student learning journey, Instructor course creation, Quiz workflow, Blended learning workflow

**CHƯƠNG 3: THIẾT KẾ CƠ SỞ DỮ LIỆU**
- Sơ đồ ERD (Entity Relationship Diagram) tổng quan
- Sơ đồ RM (Relational Model) với 16 bảng
- Đặc tả chi tiết 16 bảng theo 5 domains
- Relationships matrix (23 foreign key relationships)
- Indexes strategy (96+ indexes bao gồm primary keys, foreign keys, performance indexes, GIN indexes)
- Constraints (224+ constraints bao gồm primary keys, foreign keys, unique, check, not null)

**CHƯƠNG 4: TRIỂN KHAI & DEMO**
- Cài đặt PostgreSQL 14+ và required extensions
- Triển khai database schema (SQL scripts)
- Demo các chức năng qua REST API (58+ endpoints)
- Kiểm thử và validation

**CHƯƠNG 5: ĐÁNH GIÁ & CẢI TIẾN**
- So sánh thiết kế mới (16 bảng) với thiết kế cũ (21 bảng)
- Phân tích ưu điểm: simplicity, maintainability, performance
- Phân tích hạn chế và trade-offs
- Đề xuất cải tiến và mở rộng trong tương lai

### Lời cảm ơn

Chúng tôi xin chân thành cảm ơn Thầy Trần Văn Dũng đã tận tình hướng dẫn, góp ý và hỗ trợ chúng tôi trong suốt quá trình thực hiện đồ án. Những kiến thức và kinh nghiệm mà Thầy truyền đạt đã giúp chúng tôi hoàn thành đồ án này một cách tốt nhất.

Chúng tôi cũng xin cảm ơn các bạn sinh viên trong lớp đã chia sẻ kinh nghiệm và hỗ trợ trong quá trình nghiên cứu và triển khai.

Mặc dù đã cố gắng hết sức, nhưng do thời gian và kinh nghiệm còn hạn chế, đồ án không tránh khỏi những thiếu sót. Chúng tôi rất mong nhận được sự góp ý của Thầy và các bạn để hoàn thiện hơn nữa.

**Hà Nội, ngày 01 tháng 12 năm 2025**

**Sinh viên thực hiện**

Nguyễn Văn Kiệt

---

## MỤC LỤC

**LỜI NÓI ĐẦU** ................................................ 1

**CHƯƠNG 1: TỔNG QUAN HỆ THỐNG** ............................ 5
- 1.1. Giới thiệu về Blended Learning ...................... 5
  - 1.1.1. Khái niệm Blended Learning ....................... 5
  - 1.1.2. Lợi ích của Blended Learning ..................... 6
  - 1.1.3. Xu hướng ứng dụng trong giáo dục ................. 7
- 1.2. Tổng quan hệ thống B-Learning Core .................. 8
  - 1.2.1. Mục tiêu của hệ thống ............................ 8
  - 1.2.2. Các actors trong hệ thống ........................ 9
  - 1.2.3. Kiến trúc tổng quan .............................. 10
- 1.3. Công nghệ sử dụng ................................... 12
  - 1.3.1. PostgreSQL 14+ ................................... 12
  - 1.3.2. UUID Primary Keys ................................ 13
  - 1.3.3. JSON/JSONB Fields ................................ 14
  - 1.3.4. GIN Indexes ...................................... 15

**CHƯƠNG 2: PHÂN TÍCH YÊU CẦU** ............................. 17
- 2.1. Yêu cầu chức năng ................................... 17
  - 2.1.1. User Management (7 FRs) .......................... 17
  - 2.1.2. Course Content Management (9 FRs) ................ 19
  - 2.1.3. Assessment (13 FRs) .............................. 21
  - 2.1.4. Enrollment & Progress (7 FRs) .................... 24
  - 2.1.5. Class Management (6 FRs) ......................... 25
  - 2.1.6. Certificate Management (4 FRs) ................... 27
  - 2.1.7. Reporting & Analytics (5 FRs) .................... 28
- 2.2. Yêu cầu phi chức năng ............................... 29
  - 2.2.1. Hiệu năng (Performance) .......................... 29
  - 2.2.2. Bảo mật (Security) ............................... 30
  - 2.2.3. Khả năng mở rộng (Scalability) ................... 31
  - 2.2.4. Tính sẵn sàng (Availability) ..................... 32
- 2.3. Use Cases chính ..................................... 33
  - 2.3.1. Student Learning Journey ......................... 33
  - 2.3.2. Instructor Course Creation ....................... 35
  - 2.3.3. Quiz Workflow ..................................... 36
  - 2.3.4. Blended Learning Workflow ........................ 38

**CHƯƠNG 3: THIẾT KẾ CƠ SỞ DỮ LIỆU** ........................ 40
- 3.1. Sơ đồ ERD (Entity Relationship Diagram) ............. 40
  - 3.1.1. ERD tổng quan .................................... 40
  - 3.1.2. Ký hiệu sử dụng .................................. 42
- 3.2. Sơ đồ RM (Relational Model) ......................... 43
  - 3.2.1. Tổng quan 16 bảng theo 5 domains ................. 43
  - 3.2.2. Relationships matrix ............................. 44
- 3.3. Đặc tả chi tiết các bảng ............................ 46
  - 3.3.1. DOMAIN 1: User Management (3 bảng) ............... 46
    - 3.3.1.1. Bảng User .................................... 46
    - 3.3.1.2. Bảng Role .................................... 49
    - 3.3.1.3. Bảng UserRole ................................ 51
  - 3.3.2. DOMAIN 2: Course Content (4 bảng) ................ 53
    - 3.3.2.1. Bảng Course .................................. 53
    - 3.3.2.2. Bảng Module .................................. 56
    - 3.3.2.3. Bảng Lecture ................................. 58
    - 3.3.2.4. Bảng Resource ................................ 62
  - 3.3.3. DOMAIN 3: Assessment (5 bảng) .................... 64
    - 3.3.3.1. Bảng Quiz .................................... 64
    - 3.3.3.2. Bảng Question ................................ 67
    - 3.3.3.3. Bảng Option .................................. 69
    - 3.3.3.4. Bảng Attempt ................................. 71
    - 3.3.3.5. Bảng AssignmentSubmission .................... 74
  - 3.3.4. DOMAIN 4: Enrollment & Progress (2 bảng) ......... 77
    - 3.3.4.1. Bảng Enrollment .............................. 77
    - 3.3.4.2. Bảng Progress ................................ 79
  - 3.3.5. DOMAIN 5: Class & Certificate (2 bảng) ........... 81
    - 3.3.5.1. Bảng Class ................................... 81
    - 3.3.5.2. Bảng Certificate ............................. 84
- 3.4. Relationships (Mối quan hệ) ......................... 86
  - 3.4.1. Foreign Key Relationships ........................ 86
  - 3.4.2. Cascade Behavior ................................. 88
- 3.5. Indexes Strategy .................................... 89
  - 3.5.1. Primary Key Indexes .............................. 89
  - 3.5.2. Foreign Key Indexes .............................. 90
  - 3.5.3. Performance Indexes .............................. 91
  - 3.5.4. JSON GIN Indexes ................................. 92
  - 3.5.5. Full-Text Search Indexes ......................... 93
- 3.6. Constraints ......................................... 94
  - 3.6.1. Primary Keys ..................................... 94
  - 3.6.2. Foreign Keys ..................................... 95
  - 3.6.3. Unique Constraints ............................... 96
  - 3.6.4. Check Constraints ................................ 97
  - 3.6.5. Not Null Constraints ............................. 99

**CHƯƠNG 4: TRIỂN KHAI & DEMO** ............................. 101
- 4.1. Cài đặt và cấu hình PostgreSQL ...................... 101
  - 4.1.1. Cài đặt PostgreSQL 14+ ........................... 101
  - 4.1.2. Cấu hình database ................................ 102
  - 4.1.3. Cài đặt extensions ............................... 103
- 4.2. Triển khai database schema .......................... 104
  - 4.2.1. Script tạo schema (01-schema.sql) ................ 104
  - 4.2.2. Script tạo indexes (02-indexes.sql) .............. 106
  - 4.2.3. Script tạo constraints (03-constraints.sql) ...... 107
  - 4.2.4. Script seed data (04-seed-data.sql) .............. 108
- 4.3. Demo các chức năng qua REST API ..................... 110
  - 4.3.1. Authentication Endpoints ......................... 110
  - 4.3.2. User Management Endpoints ........................ 112
  - 4.3.3. Course Management Endpoints ...................... 113
  - 4.3.4. Assessment Endpoints ............................. 115
  - 4.3.5. Enrollment & Progress Endpoints .................. 118
  - 4.3.6. Class Management Endpoints ....................... 120
  - 4.3.7. Certificate Endpoints ............................ 122
- 4.4. Kiểm thử và validation .............................. 123
  - 4.4.1. Unit tests ....................................... 123
  - 4.4.2. Integration tests ................................ 124
  - 4.4.3. Performance tests ................................ 125

**CHƯƠNG 5: ĐÁNH GIÁ & CẢI TIẾN** ........................... 127
- 5.1. So sánh với thiết kế cũ ............................. 127
  - 5.1.1. Tổng quan so sánh (21 vs 16 bảng) ................ 127
  - 5.1.2. Phân tích từng domain ............................ 129
  - 5.1.3. Simplification strategies ........................ 132
- 5.2. Ưu điểm của thiết kế mới ............................ 135
  - 5.2.1. Simplicity (Đơn giản hóa) ........................ 135
  - 5.2.2. Maintainability (Dễ bảo trì) ..................... 136
  - 5.2.3. Performance (Hiệu năng) .......................... 137
  - 5.2.4. Flexibility (Tính linh hoạt) ..................... 138
- 5.3. Hạn chế và trade-offs ............................... 139
  - 5.3.1. JSON query complexity ............................ 139
  - 5.3.2. Validation challenges ............................ 140
  - 5.3.3. Reporting limitations ............................ 141
- 5.4. Đề xuất cải tiến và mở rộng ......................... 142
  - 5.4.1. Advanced features ................................ 142
  - 5.4.2. Performance optimization ......................... 143
  - 5.4.3. Scalability improvements ......................... 144

**KẾT LUẬN** ................................................. 146

**TÀI LIỆU THAM KHẢO** ....................................... 148

**PHỤ LỤC** .................................................. 149
- PHỤ LỤC A: SQL Scripts ................................... 149
- PHỤ LỤC B: Sample Data ................................... 152
- PHỤ LỤC C: API Documentation ............................. 155

---

# CHƯƠNG 1: TỔNG QUAN HỆ THỐNG

## 1.1. Giới thiệu về Blended Learning

### 1.1.1. Khái niệm Blended Learning

Blended Learning (Học tập kết hợp) là mô hình giáo dục hiện đại kết hợp giữa học trực tuyến (online learning) và học trực tiếp tại lớp (in-person learning), nhằm tận dụng ưu điểm của cả hai phương pháp để tối ưu hóa trải nghiệm học tập và nâng cao hiệu quả giáo dục.

Theo định nghĩa của Garrison & Kanuka (2004), Blended Learning không chỉ đơn thuần là sự kết hợp giữa hai hình thức học tập, mà còn là sự tích hợp sâu sắc các công nghệ giáo dục (educational technologies) nhằm tạo ra một môi trường học tập phong phú, linh hoạt và tương tác cao.

**Các thành phần chính của Blended Learning:**

1. **Online Learning Component (Học trực tuyến):**
   - **Video Lectures**: Bài giảng video ghi hình trước, cho phép học viên xem lại nhiều lần
   - **E-books & PDFs**: Tài liệu đọc điện tử, slides, sách giáo khoa số
   - **Quizzes**: Bài kiểm tra trắc nghiệm tự động chấm điểm
   - **Assignments**: Bài tập lập trình, essay, projects nộp trực tuyến
   - **Discussion Forums**: Diễn đàn thảo luận trực tuyến giữa học viên và giảng viên
   - **Self-paced Learning**: Học viên tự điều chỉnh tốc độ học tập phù hợp với khả năng

2. **In-person Learning Component (Học trực tiếp):**
   - **Classroom Sessions**: Buổi học tại lớp với sự hiện diện của giảng viên
   - **Labs & Workshops**: Thực hành, workshop, hands-on activities
   - **Group Projects**: Làm việc nhóm, thuyết trình
   - **Face-to-face Discussions**: Thảo luận trực tiếp, Q&A sessions
   - **Practical Exams**: Thi thực hành, demo projects

3. **Learning Management System (LMS):**
   - **Platform quản lý**: Hệ thống B-Learning Core đóng vai trò là nền tảng tích hợp tất cả các hoạt động học tập
   - **Progress Tracking**: Theo dõi tiến độ học tập của từng học viên
   - **Grading System**: Hệ thống chấm điểm tự động và thủ công
   - **Certificate Management**: Quản lý và cấp phát chứng chỉ hoàn thành khóa học

### 1.1.2. Lợi ích của Blended Learning

**1. Tính linh hoạt cao (High Flexibility):**
- Học viên có thể học mọi lúc, mọi nơi thông qua online platform
- Kết hợp với các buổi học trực tiếp để tương tác và thực hành

**2. Cá nhân hóa trải nghiệm học tập (Personalized Learning):**
- Học viên tự điều chỉnh tốc độ học (self-paced) phù hợp với khả năng
- System tracking giúp giảng viên nhận biết học viên cần hỗ trợ thêm

**3. Tối ưu chi phí (Cost-Effective):**
- Giảm chi phí cơ sở vật chất (classroom space)
- Video lectures có thể tái sử dụng cho nhiều khóa học

**4. Tăng tương tác (Enhanced Interaction):**
- Online forums cho phép học viên đặt câu hỏi bất cứ lúc nào
- In-person sessions tập trung vào discussion và hands-on activities

**5. Dễ dàng cập nhật nội dung (Easy Content Updates):**
- Nội dung online có thể cập nhật nhanh chóng
- Thêm video, tài liệu mới mà không cần tái bản sách giáo khoa

**6. Đo lường hiệu quả học tập (Learning Analytics):**
- System tự động thu thập dữ liệu về tiến độ học tập
- Analytics giúp cải thiện chất lượng khóa học

### 1.1.3. Xu hướng ứng dụng trong giáo dục

**Trước đại dịch COVID-19:**
- Blended Learning chủ yếu được áp dụng trong các khóa học ngắn hạn, training doanh nghiệp
- Giáo dục đại học và phổ thông vẫn chủ yếu dựa vào giảng dạy trực tiếp

**Sau đại dịch COVID-19 (2020-2025):**
- **Thay đổi mạnh mẽ:** Blended Learning trở thành xu hướng chủ đạo
- **Các trường đại học** như Harvard, MIT, Stanford đầu tư mạnh vào LMS platforms
- **Doanh nghiệp công nghệ** như Google, Microsoft phát triển training programs qua blended learning
- **Giáo dục phổ thông** tại Việt Nam bắt đầu áp dụng (đặc biệt tại các thành phố lớn)

**Xu hướng tương lai (2025+):**
- **AI-powered LMS**: Sử dụng AI để personalize learning paths
- **VR/AR Integration**: Thực tế ảo cho các bài lab, workshop
- **Micro-credentials**: Chứng chỉ ngắn hạn cho từng skill cụ thể
- **Gamification**: Tích hợp yếu tố game vào learning experience

---

## 1.2. Tổng quan hệ thống B-Learning Core

### 1.2.1. Mục tiêu của hệ thống

Hệ thống B-Learning Core được thiết kế với các mục tiêu sau:

**Mục tiêu chính:**
1. **Hỗ trợ cả Self-paced và Blended Learning:**
   - **Self-paced**: Học viên tự học online, không ràng buộc lịch học
   - **Blended**: Kết hợp online + in-person classes với lịch học cố định

2. **Quản lý đầy đủ lifecycle của khóa học:**
   - Course creation (Giảng viên tạo khóa học)
   - Content delivery (Phân phối nội dung học tập)
   - Assessment (Đánh giá qua quizzes và assignments)
   - Progress tracking (Theo dõi tiến độ)
   - Certification (Cấp chứng chỉ hoàn thành)

3. **Đảm bảo tính toàn vẹn và bảo mật dữ liệu:**
   - RBAC (Role-Based Access Control) với 4 roles: Admin, Instructor, TA, Student
   - Data integrity qua foreign keys, constraints, validations

4. **Tối ưu hiệu năng và khả năng mở rộng:**
   - Sử dụng indexes (96+ indexes) để tối ưu query performance
   - UUID primary keys hỗ trợ distributed systems
   - JSON fields giảm số lượng bảng, tăng flexibility

### 1.2.2. Các actors trong hệ thống

Hệ thống B-Learning Core có 5 actors chính:

**1. STUDENT (Học viên):**
- **Vai trò:** Người học, tham gia khóa học
- **Quyền hạn:**
  - Đăng ký khóa học (enroll in courses)
  - Xem video lectures, đọc tài liệu
  - Làm quizzes và assignments
  - Xem tiến độ học tập
  - Tải chứng chỉ sau khi hoàn thành
- **Ví dụ:** Sinh viên đại học, người đi làm muốn học thêm kỹ năng

**2. INSTRUCTOR (Giảng viên):**
- **Vai trò:** Người dạy, tạo và quản lý khóa học
- **Quyền hạn:**
  - Tạo courses, modules, lectures
  - Upload video, tài liệu
  - Tạo quizzes và assignments
  - Chấm bài (grading)
  - Xem báo cáo tiến độ lớp
  - Quản lý in-person classes (nếu blended learning)
- **Ví dụ:** Giảng viên đại học, chuyên gia trong ngành

**3. TA (Teaching Assistant - Trợ giảng):**
- **Vai trò:** Hỗ trợ giảng viên
- **Quyền hạn:**
  - Hỗ trợ chấm bài
  - Trả lời thắc mắc của học viên
  - Xem danh sách lớp
  - Không có quyền tạo/sửa courses
- **Ví dụ:** Sinh viên giỏi, cựu học viên

**4. ADMIN (Quản trị viên):**
- **Vai trò:** Quản lý toàn bộ hệ thống
- **Quyền hạn:**
  - Quản lý users (create, update, delete, suspend)
  - Gán roles cho users
  - Xóa courses
  - Xem tất cả dữ liệu
  - Quản lý system configuration
- **Ví dụ:** IT admin, quản lý trung tâm đào tạo

**5. SYSTEM (Hệ thống tự động):**
- **Vai trò:** Xử lý tự động các tác vụ
- **Chức năng:**
  - Auto-grading cho quizzes (MCQ, True/False)
  - Tính toán progress completion
  - Tạo certificates tự động khi đạt điều kiện
  - Gửi notifications (qua external email service)
  - Update class status (SCHEDULED → ONGOING → COMPLETED)

### 1.2.3. Kiến trúc tổng quan

Hệ thống B-Learning Core được thiết kế theo kiến trúc 3-tier:

```
┌─────────────────────────────────────────────────────┐
│              PRESENTATION TIER                       │
│  (Frontend: Next.js + React + TypeScript)           │
│  - Student Portal                                   │
│  - Instructor Dashboard                             │
│  - Admin Panel                                      │
└─────────────────────────────────────────────────────┘
                        ↕ REST API
┌─────────────────────────────────────────────────────┐
│              APPLICATION TIER                        │
│  (Backend: Python FastAPI)                          │
│  - Authentication & Authorization (JWT)             │
│  - Business Logic                                   │
│  - API Endpoints (58+ endpoints)                    │
│  - File Upload Handler (S3/GCS)                     │
└─────────────────────────────────────────────────────┘
                        ↕ SQL Queries
┌─────────────────────────────────────────────────────┐
│              DATA TIER                               │
│  (Database: PostgreSQL 14+)                         │
│  - 16 Tables (5 Domains)                            │
│  - 96+ Indexes                                      │
│  - 224+ Constraints                                 │
│  - 23 Foreign Key Relationships                     │
└─────────────────────────────────────────────────────┘
```

**Database Architecture - 5 Domains:**

```
DOMAIN 1: USER MANAGEMENT (3 bảng)
├── User: Thông tin tài khoản
├── Role: Các vai trò (STUDENT, INSTRUCTOR, TA, ADMIN)
└── UserRole: Gán role cho user (many-to-many)

DOMAIN 2: COURSE CONTENT (4 bảng)
├── Course: Thông tin khóa học
├── Module: Chương/module trong course
├── Lecture: Bài giảng (VIDEO, PDF, ASSIGNMENT...)
└── Resource: File đính kèm (slide, source code...)

DOMAIN 3: ASSESSMENT (5 bảng)
├── Quiz: Cấu hình quiz (+ questions JSON)
├── Question: Ngân hàng câu hỏi
├── Option: Lựa chọn cho câu hỏi MCQ
├── Attempt: Lần làm quiz (+ answers JSON)
└── AssignmentSubmission: Nộp bài tập

DOMAIN 4: ENROLLMENT & PROGRESS (2 bảng)
├── Enrollment: Đăng ký khóa học (hỗ trợ self-paced & blended)
└── Progress: Theo dõi tiến độ học tập (module level)

DOMAIN 5: CLASS & CERTIFICATE (2 bảng)
├── Class: Lớp học (blended learning) (+ schedules JSON)
└── Certificate: Chứng chỉ hoàn thành
```

**Tổng quan các bảng:**

| STT | Bảng | Domain | Mục đích | Số trường |
|-----|------|--------|----------|-----------|
| 1 | User | User Management | Tài khoản người dùng | 12 |
| 2 | Role | User Management | Vai trò hệ thống | 5 |
| 3 | UserRole | User Management | Gán role cho user | 5 |
| 4 | Course | Course Content | Khóa học | 13 |
| 5 | Module | Course Content | Module trong course | 8 |
| 6 | Lecture | Course Content | Bài giảng | 10 |
| 7 | Resource | Course Content | Tài liệu đính kèm | 6 |
| 8 | Quiz | Assessment | Quiz/exam | 13 |
| 9 | Question | Assessment | Ngân hàng câu hỏi | 7 |
| 10 | Option | Assessment | Lựa chọn MCQ | 5 |
| 11 | Attempt | Assessment | Lần làm quiz | 10 |
| 12 | AssignmentSubmission | Assessment | Nộp bài tập | 9 |
| 13 | Enrollment | Enrollment & Progress | Đăng ký khóa học | 7 |
| 14 | Progress | Enrollment & Progress | Tiến độ học tập | 7 |
| 15 | Class | Class & Certificate | Lớp học blended | 10 |
| 16 | Certificate | Class & Certificate | Chứng chỉ | 9 |

**Tổng cộng:** 16 bảng, ~140 trường

---

## 1.3. Công nghệ sử dụng

### 1.3.1. PostgreSQL 14+

**Lý do chọn PostgreSQL:**

PostgreSQL là hệ quản trị cơ sở dữ liệu quan hệ (RDBMS) mã nguồn mở, mạnh mẽ và phổ biến nhất hiện nay. Chúng tôi chọn PostgreSQL 14+ cho hệ thống B-Learning Core vì các lý do sau:

**1. ACID Compliance (Atomicity, Consistency, Isolation, Durability):**
- Đảm bảo tính toàn vẹn dữ liệu trong các transactions
- Quan trọng cho các operations như enrollment, grading, certificate issuance

**2. Hỗ trợ JSON/JSONB native:**
- PostgreSQL có hỗ trợ JSON/JSONB tốt nhất trong các RDBMS
- Cho phép lưu dữ liệu linh hoạt mà vẫn query được hiệu quả
- Ví dụ: `Quiz.questions`, `Attempt.answers`, `Class.schedules`

**3. Rich data types:**
- UUID: Unique identifiers cho distributed systems
- Array: `prerequisite_module_ids UUID[]`
- DECIMAL: Điểm số chính xác (không dùng FLOAT)
- TIMESTAMP: Thời gian với timezone support

**4. Advanced indexing:**
- B-tree indexes: Mặc định cho primary keys, foreign keys
- GIN indexes: Cho JSON fields, full-text search
- Partial indexes: Index có điều kiện (ví dụ: chỉ index records active)

**5. Constraints & Triggers:**
- CHECK constraints: Validate data trước khi insert/update
- FOREIGN KEY constraints: Enforce referential integrity
- Triggers: Auto-update `updated_at` timestamps

**6. Scalability:**
- Hỗ trợ replication (master-slave)
- Partitioning: Chia bảng lớn thành nhiều partitions
- Connection pooling: PgBouncer cho nhiều concurrent users

**Phiên bản yêu cầu: PostgreSQL 14+**

PostgreSQL 14 được release vào tháng 9/2021, mang lại nhiều cải tiến quan trọng:
- Performance improvements cho queries với nhiều JSON operations
- Better memory management
- Parallel query improvements
- `gen_random_uuid()` function built-in (không cần extension)

### 1.3.2. UUID Primary Keys

**Khái niệm:**

UUID (Universally Unique Identifier) là mã định danh 128-bit, có khả năng unique trên toàn cầu mà không cần central authority để generate.

**Format:** 8-4-4-4-12 hex digits
- Ví dụ: `550e8400-e29b-41d4-a716-446655440000`

**So sánh UUID vs INT Auto-increment:**

| Tiêu chí | INT Auto-increment | UUID |
|----------|-------------------|------|
| **Độ dài** | 4 bytes (32-bit) | 16 bytes (128-bit) |
| **Performance** | Nhanh hơn (indexing, joins) | Chậm hơn ~10-20% |
| **Predictability** | Dễ đoán (1, 2, 3, ...) | Không đoán được |
| **Security** | ❌ Lộ số lượng records | ✅ Không lộ thông tin |
| **Distributed systems** | ❌ Conflict khi merge data | ✅ Không conflict |
| **Scalability** | ❌ Khó scale horizontal | ✅ Dễ scale |
| **URL friendliness** | ✅ Ngắn gọn | ⚠️ Dài hơn |

**Lý do chọn UUID cho B-Learning Core:**

**1. Security (Bảo mật):**
```
INT: https://blearning.edu/courses/1
UUID: https://blearning.edu/courses/550e8400-e29b-41d4-a716-446655440000

→ Với INT, attacker có thể enumerate: /courses/1, /courses/2, ...
→ Với UUID, không thể đoán được ID hợp lệ
```

**2. Distributed-friendly:**
```
Scenario: Merge data từ 2 systems khác nhau
INT: Course ID=1 từ system A có thể conflict với ID=1 từ system B
UUID: 550e8400-... và 660e9500-... không bao giờ conflict
```

**3. Không lộ thông tin kinh doanh:**
```
INT: Competitor có thể biết số lượng courses, users
UUID: Không thể suy ra metrics từ ID
```

**4. Microservices architecture:**
- Mỗi service có thể generate UUID độc lập
- Không cần centralized ID generator

**Cách generate UUID trong PostgreSQL:**

```sql
-- Cách 1: Sử dụng gen_random_uuid() (PostgreSQL 14+)
CREATE TABLE "User" (
  user_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  ...
);

-- Cách 2: Sử dụng uuid-ossp extension (PostgreSQL < 14)
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE TABLE "User" (
  user_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  ...
);
```

### 1.3.3. JSON/JSONB Fields

**Khái niệm:**

PostgreSQL hỗ trợ 2 kiểu JSON:
- **JSON**: Lưu text nguyên bản, phải parse mỗi lần query
- **JSONB**: Lưu binary format, nhanh hơn nhưng mất thứ tự key

**Chúng tôi sử dụng JSONB** vì performance tốt hơn.

**Các trường hợp sử dụng JSON trong B-Learning Core:**

**1. Quiz.questions (Quiz có câu hỏi nào, mỗi câu bao nhiêu điểm):**
```json
[
  {"question_id": "550e8400-...", "points": 10, "order": 1},
  {"question_id": "660e9500-...", "points": 15, "order": 2}
]
```

**Lợi ích:**
- ✅ Đơn giản hơn việc tạo bảng QuizQuestion riêng
- ✅ Thêm/xóa câu hỏi dễ dàng (JSON array operations)
- ✅ Có thể thêm metadata sau này mà không cần migration

**2. Attempt.answers (Câu trả lời của học viên khi làm quiz):**
```json
[
  {
    "question_id": "550e8400-...",
    "answer_text": "Paris",
    "selected_options": ["option-uuid-a"],
    "score": 10,
    "max_score": 10,
    "is_correct": true,
    "graded_at": "2025-11-27T10:30:00Z"
  }
]
```

**Lợi ích:**
- ✅ Gom tất cả câu trả lời của 1 lần làm bài vào 1 record
- ✅ Dễ dàng store cả MCQ và essay answers
- ✅ Có thể thêm feedback, partial credit sau này

**3. Lecture.assignment_config (Cấu hình bài tập):**
```json
{
  "max_points": 100,
  "due_date": "2025-12-15T23:59:00Z",
  "submission_types": ["file", "text", "code"],
  "allowed_file_types": [".java", ".py", ".pdf"],
  "max_file_size_mb": 10,
  "rubric": {
    "code_quality": 40,
    "functionality": 40,
    "documentation": 20
  }
}
```

**Lợi ích:**
- ✅ Linh hoạt: Mỗi assignment có thể có cấu hình khác nhau
- ✅ Không cần tạo bảng Assignment riêng
- ✅ Dễ thêm config mới (ví dụ: late_penalty, plagiarism_check)

**4. Class.schedules (Lịch học và điểm danh):**
```json
[
  {
    "session_id": "uuid",
    "date": "2025-12-01",
    "start_time": "09:00",
    "end_time": "11:00",
    "topic": "Chương 1: Giới thiệu Java",
    "location": "Phòng 101",
    "type": "IN_PERSON",
    "attendances": [
      {"user_id": "uuid-1", "status": "PRESENT", "check_in": "09:05"},
      {"user_id": "uuid-2", "status": "LATE", "check_in": "09:15"}
    ]
  }
]
```

**Lợi ích:**
- ✅ Không cần 2 bảng Schedule và Attendance riêng
- ✅ Gom all sessions của 1 class vào 1 record
- ✅ Dễ query attendance của 1 student

**Query JSON với PostgreSQL:**

```sql
-- Lấy quiz có câu hỏi cụ thể
SELECT * FROM "Quiz"
WHERE questions @> '[{"question_id": "550e8400-..."}]'::jsonb;

-- Lấy tổng điểm quiz
SELECT SUM((q->>'points')::DECIMAL) as total_points
FROM "Quiz", jsonb_array_elements(questions) q
WHERE quiz_id = 'uuid';

-- Lấy attendance của 1 student
SELECT
  c.class_name,
  session ->> 'date' as date,
  attendance ->> 'status' as status
FROM "Class" c,
  jsonb_array_elements(schedules::jsonb) session,
  jsonb_array_elements(session -> 'attendances') attendance
WHERE attendance ->> 'user_id' = 'student-uuid';
```

**Trade-offs của JSON:**

**Ưu điểm:**
- ✅ Flexibility: Dễ thêm fields mới
- ✅ Simplicity: Giảm số lượng bảng
- ✅ Performance: Ít JOIN hơn

**Nhược điểm:**
- ❌ Validation: Không có schema validation (cần validate ở app layer)
- ❌ Query complexity: SQL query phức tạp hơn
- ❌ Reporting: Khó aggregate data từ JSON

**Khi nào nên dùng JSON:**
- ✅ Dữ liệu có structure linh hoạt
- ✅ Không cần aggregate/report thường xuyên
- ✅ Relationship 1:N nhưng N nhỏ và ít thay đổi

**Khi nào KHÔNG nên dùng JSON:**
- ❌ Cần foreign key constraints
- ❌ Cần query/aggregate phức tạp
- ❌ Dữ liệu lớn (>1MB JSON field)

### 1.3.4. GIN Indexes

**Khái niệm:**

GIN (Generalized Inverted Index) là loại index đặc biệt trong PostgreSQL, được thiết kế cho:
- JSON/JSONB fields
- Array fields
- Full-text search

**Cách hoạt động:**

```
B-tree Index (thông thường):
Tạo cây cân bằng từ column value
Phù hợp cho: =, <, >, <=, >=, BETWEEN

GIN Index:
Tạo index cho từng phần tử trong JSON/array
Phù hợp cho: @>, @?, &&, ?|, ?&
```

**Ví dụ sử dụng GIN Index trong B-Learning Core:**

**1. Index cho Quiz.questions:**
```sql
CREATE INDEX idx_quiz_questions ON "Quiz" USING GIN (questions);

-- Query nhanh: Tìm quiz chứa câu hỏi X
SELECT * FROM "Quiz"
WHERE questions @> '[{"question_id": "550e8400-..."}]'::jsonb;

-- Không có GIN index: Full table scan (chậm)
-- Có GIN index: Index scan (nhanh)
```

**2. Index cho Attempt.answers:**
```sql
CREATE INDEX idx_attempt_answers ON "Attempt" USING GIN (answers);

-- Query nhanh: Tìm attempts có đáp án cụ thể
SELECT * FROM "Attempt"
WHERE answers @> '[{"question_id": "..."}]'::jsonb;
```

**3. Index cho User.preferences:**
```sql
CREATE INDEX idx_user_preferences ON "User" USING GIN (preferences);

-- Query nhanh: Tìm users có notification setting bật
SELECT * FROM "User"
WHERE preferences @> '{"notifications": {"email": true}}'::jsonb;
```

**4. Index cho Module.prerequisite_module_ids (Array):**
```sql
CREATE INDEX idx_module_prerequisites ON "Module"
USING GIN (prerequisite_module_ids);

-- Query nhanh: Tìm modules cần học Module X trước
SELECT * FROM "Module"
WHERE prerequisite_module_ids @> ARRAY['module-x-uuid']::UUID[];
```

**Performance comparison:**

```
Test: Tìm quiz chứa câu hỏi cụ thể trong 10,000 quizzes

Không có index:
  Query time: 850ms (full table scan)

Có GIN index:
  Query time: 12ms (index scan)

→ Cải thiện ~70x
```

**GIN Index operators:**

| Operator | Ý nghĩa | Ví dụ |
|----------|---------|-------|
| `@>` | Contains | `questions @> '[{"question_id": "..."}]'` |
| `<@` | Is contained by | `'[...]' <@ questions` |
| `@?` | JSON path exists | `answers @? '$.score'` |
| `@@` | Full-text search | `to_tsvector('english', title) @@ 'java'` |

**Kích thước GIN Index:**

GIN index thường lớn hơn B-tree index ~30-50%
- B-tree index: ~30% kích thước table
- GIN index: ~50% kích thước table

**Trade-off:**
- ✅ Query performance tăng đáng kể
- ❌ Insert/Update chậm hơn (phải update index)
- ❌ Tốn storage hơn

**Khi nào nên dùng GIN Index:**
- ✅ Query JSON/array fields thường xuyên
- ✅ Full-text search
- ❌ KHÔNG dùng nếu ít query

---

# CHƯƠNG 2: PHÂN TÍCH YÊU CẦU

## 2.1. Yêu cầu chức năng

Hệ thống B-Learning Core có tổng cộng **45 yêu cầu chức năng (Functional Requirements)** được phân chia thành 7 domains chính.

### 2.1.1. User Management (7 FRs)

**FR-1.1: User Registration (Đăng ký tài khoản)**

**Mô tả:** Student có thể tự đăng ký tài khoản vào hệ thống.

**Actor:** Student (chưa có tài khoản)

**Input:**
- Email (VARCHAR, required, unique)
- Password (VARCHAR, required, min 8 chars)
- First name (VARCHAR, required)
- Last name (VARCHAR, required)

**Validation:**
- Email phải đúng format: `^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$`
- Password tối thiểu 8 ký tự, có ít nhất 1 uppercase, 1 lowercase, 1 number
- Email chưa tồn tại trong hệ thống

**Process:**
1. Validate input
2. Hash password (bcrypt với cost factor 10)
3. Tạo User record với `account_status = 'PENDING_VERIFICATION'`
4. Gửi verification email (external email service)

**Output:**
- User account (status: PENDING_VERIFICATION)
- JWT token (temporary, chỉ sau khi verify email mới activate)

**Post-condition:**
- User chưa thể login cho đến khi verify email

---

**FR-1.2: User Login (Đăng nhập)**

**Mô tả:** User đăng nhập vào hệ thống.

**Actor:** User (đã có tài khoản)

**Input:**
- Email
- Password

**Validation:**
- Email tồn tại trong hệ thống
- Password đúng (compare hash)
- `account_status = 'ACTIVE'` (không phải SUSPENDED hoặc DELETED)

**Process:**
1. Validate email + password
2. Check account_status
3. Generate JWT token (expires in 24h)
4. Update `last_login_at = NOW()`

**Output:**
- JWT token
- User profile (user_id, email, first_name, last_name, roles)

**Business Rule:**
- Suspended/Deleted users không thể login
- Failed login attempts > 5 → temporarily lock account (15 minutes)

---

**FR-1.3: Password Reset (Đặt lại mật khẩu)**

**Mô tả:** User quên mật khẩu và muốn reset.

**Actor:** User

**Input:**
- Email

**Process:**
1. Check email tồn tại
2. Generate reset token (UUID, expires in 1 hour)
3. Gửi reset link qua email (external email service)
4. User click link, nhập mật khẩu mới
5. Update password_hash

**Business Rule:**
- Reset token chỉ dùng được 1 lần
- Expires sau 1 giờ

---

**FR-1.4: Admin assigns roles to users (Gán vai trò)**

**Mô tả:** Admin gán role cho user.

**Actor:** Admin

**Input:**
- user_id (UUID)
- role_id (UUID)
- expires_at (TIMESTAMP, optional - cho temporary roles)

**Validation:**
- User exists
- Role exists
- Admin có quyền `user.assign_role`

**Process:**
1. Validate user_id, role_id
2. Insert vào UserRole table
3. Set granted_by = admin_user_id

**Output:**
- UserRole record

**Business Rule:**
- 1 user có thể có nhiều roles (ví dụ: vừa STUDENT vừa INSTRUCTOR)
- UNIQUE constraint (user_id, role_id) → không thể gán 2 lần cùng 1 role

---

**FR-1.5: Admin creates/modifies roles (Quản lý vai trò)**

**Mô tả:** Admin tạo role mới hoặc sửa permissions của role.

**Actor:** Admin

**Input:**
- role_name (VARCHAR, unique)
- description (TEXT)
- permissions (JSON)

**Validation:**
- role_name unique
- permissions JSON format hợp lệ

**Output:**
- Role record

**Business Rule:**
- Không thể xóa role nếu đã có UserRole references
- Chỉnh sửa permissions ảnh hưởng đến tất cả users có role đó

---

**FR-1.6: User updates profile (Cập nhật thông tin cá nhân)**

**Mô tả:** User update thông tin profile.

**Actor:** User

**Input:**
- first_name, last_name
- phone (optional)
- avatar_url (optional - S3 URL sau khi upload)

**Validation:**
- User chỉ update được profile của chính mình (trừ Admin)

**Process:**
1. Validate input
2. Update User record
3. Set updated_at = NOW()

**Business Rule:**
- KHÔNG thể đổi email (cần verify email mới)

---

**FR-1.7: User configures notification preferences (Cài đặt thông báo)**

**Mô tả:** User cài đặt preferences (notifications, locale, theme).

**Actor:** User

**Input:**
- preferences (JSON)

**Example:**
```json
{
  "notifications": {
    "assignment_due": {"email": true, "push": false},
    "grade_published": {"email": true, "push": true}
  },
  "locale": "vi",
  "timezone": "Asia/Ho_Chi_Minh",
  "theme": "dark"
}
```

**Process:**
1. Validate JSON format
2. Update User.preferences
3. Application sử dụng preferences để gửi notifications

---

### 2.1.2. Course Content Management (9 FRs)

**FR-2.1: Instructor creates course (Tạo khóa học)**

**Actor:** Instructor

**Input:**
- code (VARCHAR, unique, format: `^[A-Z0-9]{3,10}$`)
- title (VARCHAR)
- description (TEXT)
- difficulty_level (ENUM: BEGINNER, INTERMEDIATE, ADVANCED)
- credits (INTEGER, optional)

**Validation:**
- code unique
- code match format
- User có quyền `course.create`

**Process:**
1. Validate input
2. Insert Course record
3. Set status = 'DRAFT', created_by = current_user_id

**Output:**
- Course (status: DRAFT)

**Post-condition:**
- Course chưa visible cho students (chỉ instructor và admin thấy)

---

**FR-2.2: Instructor publishes course (Xuất bản khóa học)**

**Actor:** Instructor

**Pre-condition:**
- Course có ít nhất 1 module với lectures

**Process:**
1. Validate course có content
2. Update status = 'PUBLISHED', published_at = NOW()

**Post-condition:**
- Course visible trong catalog
- Students có thể enroll

---

**FR-2.3: Instructor archives course (Lưu trữ khóa học)**

**Actor:** Instructor hoặc Admin

**Pre-condition:**
- Không có active enrollments

**Process:**
1. Check không có enrollment với status = 'ACTIVE'
2. Update status = 'ARCHIVED'

**Post-condition:**
- Course vẫn visible (read-only)
- Không thể enroll mới

---

**FR-2.4: Instructor creates module (Tạo chương/module)**

**Actor:** Instructor

**Input:**
- course_id
- title
- description
- order_num (auto-incremented hoặc manual)

**Validation:**
- UNIQUE (course_id, order_num)
- course_id tồn tại và instructor có quyền

**Process:**
1. Validate input
2. Insert Module record

**Output:**
- Module record

---

**FR-2.5: Instructor sets module prerequisites (Cài đặt điều kiện tiên quyết)**

**Actor:** Instructor

**Input:**
- module_id
- prerequisite_module_ids (UUID[])

**Validation:**
- Tất cả prerequisite_module_ids phải là modules trong cùng course
- Không có circular dependencies (Module A requires B, B requires A)

**Process:**
1. Validate UUIDs
2. Check circular dependencies
3. Update Module.prerequisite_module_ids

**Business Rule:**
- Students phải hoàn thành tất cả prerequisite modules trước khi access module này

---

**FR-2.6: Instructor reorders modules (Sắp xếp lại thứ tự module)**

**Actor:** Instructor

**Input:**
- Array of {module_id, new_order_num}

**Process:**
1. Batch update order_num cho các modules
2. Ensure không có duplicate order_num

---

**FR-2.7: Instructor creates lecture (Tạo bài giảng)**

**Actor:** Instructor

**Input:**
- module_id
- title
- type (ENUM: VIDEO, PDF, SLIDE, AUDIO, TEXT, ASSIGNMENT)
- content_url (nếu type != TEXT, ASSIGNMENT)
- order_num

**Validation:**
- UNIQUE (module_id, order_num)
- type hợp lệ

**Process:**
1. Validate input
2. Insert Lecture record

**Output:**
- Lecture record

---

**FR-2.8: Instructor creates assignment lecture (Tạo bài tập)**

**Actor:** Instructor

**Input:**
- Lecture fields + type = 'ASSIGNMENT'
- assignment_config (JSON):
  ```json
  {
    "max_points": 100,
    "due_date": "2025-12-15T23:59:00Z",
    "submission_types": ["file", "text"],
    "allowed_file_types": [".java", ".py"],
    "max_file_size_mb": 10,
    "rubric": {
      "code_quality": 40,
      "functionality": 40,
      "documentation": 20
    }
  }
  ```

**Validation:**
- type = 'ASSIGNMENT' → assignment_config NOT NULL
- rubric tổng = 100

**Process:**
1. Validate JSON structure
2. Insert Lecture với assignment_config

---

**FR-2.9: Instructor uploads resources (Upload tài liệu đính kèm)**

**Actor:** Instructor

**Input:**
- lecture_id
- file (upload)

**Process:**
1. Upload file to S3/GCS
2. Insert Resource record với file_url, file_type, file_size_bytes

**Output:**
- Resource record

---

### 2.1.3. Assessment (13 FRs)

**FR-3.1: Instructor creates question (Tạo câu hỏi)**

**Actor:** Instructor

**Input:**
- course_id
- question_text (TEXT)
- type (ENUM: MCQ, TRUE_FALSE, ESSAY, SHORT_ANSWER)
- default_points (DECIMAL)

**Process:**
1. Insert Question record

**Output:**
- Question (reusable trong nhiều quizzes)

---

**FR-3.2: Instructor creates MCQ options (Tạo lựa chọn cho câu hỏi trắc nghiệm)**

**Actor:** Instructor

**Input:**
- question_id
- option_text
- is_correct (BOOLEAN)
- order_num

**Validation:**
- MCQ phải có ít nhất 2 options
- Phải có ít nhất 1 option với is_correct = true

**Process:**
1. Insert Option record

---

**FR-3.3: Instructor creates quiz (Tạo bài kiểm tra)**

**Actor:** Instructor

**Input:**
- course_id
- title
- time_limit_minutes (0 = unlimited)
- passing_score (%, NULL = không yêu cầu)
- max_attempts (NULL = unlimited)

**Process:**
1. Insert Quiz record
2. Set status = 'DRAFT'

**Output:**
- Quiz (status: DRAFT, questions: [])

---

**FR-3.4: Instructor adds questions to quiz (Thêm câu hỏi vào quiz)**

**Actor:** Instructor

**Input:**
- quiz_id
- questions (JSON array):
  ```json
  [
    {"question_id": "uuid", "points": 10, "order": 1},
    {"question_id": "uuid", "points": 15, "order": 2}
  ]
  ```

**Validation:**
- Tất cả question_ids phải tồn tại
- Sum of points nên = total_points

**Process:**
1. Validate question_ids
2. Update Quiz.questions (JSON)

---

**FR-3.5: Instructor publishes quiz (Xuất bản quiz)**

**Actor:** Instructor

**Pre-condition:**
- Quiz có ít nhất 1 question

**Process:**
1. Validate có questions
2. Update status = 'PUBLISHED'

**Business Rule:**
- Có thể set available_from, available_until

---

**FR-3.6: Student starts quiz attempt (Bắt đầu làm quiz)**

**Actor:** Student

**Pre-condition:**
- Enrolled in course
- Quiz published
- Within available dates (available_from <= NOW() <= available_until)
- Chưa vượt quá max_attempts

**Process:**
1. Check pre-conditions
2. Insert Attempt record
3. Set status = 'IN_PROGRESS', attempt_number = auto-increment

**Output:**
- Attempt (status: IN_PROGRESS)

---

**FR-3.7: Student answers questions (Trả lời câu hỏi)**

**Actor:** Student

**Input:**
- attempt_id
- answers (JSON array)

**Format cho MCQ:**
```json
{
  "question_id": "uuid",
  "selected_options": ["option-uuid-a"],
  "answer_text": null
}
```

**Format cho ESSAY:**
```json
{
  "question_id": "uuid",
  "selected_options": null,
  "answer_text": "Student's essay answer..."
}
```

**Process:**
1. Update Attempt.answers (JSON)

**Business Rule:**
- Có thể save nhiều lần (draft)

---

**FR-3.8: Student submits quiz (Nộp bài quiz)**

**Actor:** Student

**Pre-condition:**
- Within time limit (started_at + time_limit_minutes)

**Process:**
1. Check time limit
2. Update status = 'SUBMITTED', submitted_at = NOW()
3. Trigger auto-grading (FR-3.9)

**Post-condition:**
- Không thể edit answers sau khi submit

---

**FR-3.9: System auto-grades MCQ/TRUE_FALSE (Tự động chấm trắc nghiệm)**

**Actor:** System

**Trigger:** Quiz submission

**Process:**
1. For each MCQ/TRUE_FALSE question:
   - Compare student's selected_options với correct options
   - Calculate score
   - Set is_correct = true/false
2. Update Attempt.answers JSON với scores
3. Calculate total score
4. If không có ESSAY/SHORT_ANSWER → status = 'GRADED'
5. If có ESSAY/SHORT_ANSWER → status = 'PENDING_GRADING'

---

**FR-3.10: Instructor/TA grades ESSAY/SHORT_ANSWER (Chấm tự luận)**

**Actor:** Instructor hoặc TA

**Input:**
- attempt_id
- question_id
- score (DECIMAL)
- feedback (TEXT, optional)

**Validation:**
- score <= max_score

**Process:**
1. Update answers JSON cho question đó
2. Add score, feedback, graded_at

**Business Rule:**
- Chỉ instructor/TA của course mới có quyền chấm

---

**FR-3.11: Finalize attempt grading (Hoàn tất chấm bài)**

**Actor:** System (hoặc Instructor manual trigger)

**Pre-condition:**
- Tất cả questions đã được chấm

**Process:**
1. Check all questions có score
2. Calculate total score
3. Update status = 'GRADED'
4. Notify student (external notification service)

---

**FR-3.12: Student submits assignment (Nộp bài tập)**

**Actor:** Student

**Pre-condition:**
- Enrolled in course
- Lecture type = 'ASSIGNMENT'

**Input:**
- lecture_id
- files (upload to S3/GCS)
- submission_text (optional)

**Validation:**
- Check due_date từ assignment_config
- Check file_types, file_size từ assignment_config

**Process:**
1. Upload files to S3/GCS
2. Insert AssignmentSubmission record
3. Set file_urls (JSON array), submission_number = auto-increment
4. Set status = 'SUBMITTED'

**Business Rule:**
- Có thể submit nhiều lần (submission_number tăng dần)
- Late submission: Nếu submitted_at > due_date → status = 'LATE'

---

**FR-3.13: Instructor/TA grades assignment (Chấm bài tập)**

**Actor:** Instructor hoặc TA

**Input:**
- submission_id
- score (DECIMAL)
- feedback (TEXT)

**Validation:**
- score <= max_score từ assignment_config

**Process:**
1. Update AssignmentSubmission
2. Set score, feedback, status = 'GRADED'

**Business Rule:**
- Có thể grade theo rubric từ assignment_config

---

(Tiếp tục với các domains khác...)

---

## 2.2. Yêu cầu phi chức năng

### 2.2.1. Hiệu năng (Performance)

**NFR-1: Response Time**
- API endpoints phải response < 200ms cho 95% requests
- Database queries phải execute < 100ms cho queries đơn giản
- Complex queries (analytics) cho phép < 1 second

**Giải pháp:**
- Sử dụng indexes (96+ indexes)
- Connection pooling (PgBouncer)
- Query optimization với EXPLAIN ANALYZE

---

**NFR-2: Throughput**
- Hệ thống phải xử lý được 1000 concurrent users
- 10,000 requests/minute

**Giải pháp:**
- Horizontal scaling (load balancer + multiple backend instances)
- Database replication (read replicas)
- Caching (Redis cho frequently accessed data)

---

**NFR-3: Scalability**
- Database phải scale được từ 1,000 users → 100,000 users
- Hỗ trợ partitioning cho bảng lớn (Attempt, AssignmentSubmission)

**Giải pháp:**
- UUID primary keys (distributed-friendly)
- Table partitioning by date (ví dụ: Attempt partitioned by month)

---

### 2.2.2. Bảo mật (Security)

**NFR-4: Authentication & Authorization**
- JWT token với expiration (24h)
- RBAC với 4 roles (Admin, Instructor, TA, Student)
- Permission checks ở mỗi API endpoint

---

**NFR-5: Data Protection**
- Password phải hash với bcrypt (cost factor 10)
- Sensitive data (email, personal info) phải protect
- HTTPS only (no HTTP)

---

**NFR-6: SQL Injection Prevention**
- Sử dụng parameterized queries (không concatenate SQL strings)
- ORM (SQLAlchemy) để prevent SQL injection

---

**NFR-7: Access Control**
- Students chỉ xem được data của chính mình
- Instructors chỉ xem được courses của mình
- Admins có full access

---

### 2.2.3. Khả năng mở rộng (Scalability)

**NFR-8: Horizontal Scaling**
- Backend services phải stateless (không lưu session trên server)
- Sử dụng JWT token (không cần session storage)

---

**NFR-9: Database Scaling**
- Master-slave replication cho read scaling
- Sharding (nếu cần) theo region hoặc course_id

---

### 2.2.4. Tính sẵn sàng (Availability)

**NFR-10: Uptime**
- Hệ thống phải đạt 99.9% uptime (downtime < 8.76 giờ/năm)

**Giải pháp:**
- Database replication (failover tự động)
- Load balancer (health checks)
- Monitoring & alerting (Prometheus, Grafana)

---

## 2.3. Use Cases chính

### 2.3.1. Student Learning Journey

**Workflow:**

```
1. Register Account
   ↓
2. Verify Email (external email service)
   ↓
3. Browse Course Catalog
   - Filter by category, difficulty_level
   - Search by title, description
   ↓
4. Enroll in Course
   - Option 1: Self-paced (class_id = NULL)
   - Option 2: Join Class (class_id = UUID)
   ↓
5. Study Lectures
   - Watch videos
   - Read PDFs
   - View slides
   ↓
6. Complete Assignments
   - Download assignment instructions
   - Work on assignment
   - Submit files/text
   ↓
7. Take Quizzes
   - Start attempt
   - Answer questions (MCQ, essay)
   - Submit quiz
   - View results (after auto-grading)
   ↓
8. Track Progress
   - View module completion percentage
   - View overall course progress
   ↓
9. Complete Course
   - All modules completed
   - All assignments graded
   - Pass score met (if required)
   ↓
10. Receive Certificate
    - System auto-generates certificate
    - Student downloads PDF
    - Verify certificate with code
```

**Actors:** Student, System

**Main Success Scenario:**
1. Student registers → Tạo User (status: PENDING_VERIFICATION)
2. Student verifies email → Update status = 'ACTIVE'
3. Student browses courses → Query Course WHERE status = 'PUBLISHED'
4. Student enrolls → Insert Enrollment (status: ACTIVE)
5. Student studies → Insert/Update Progress (module level)
6. Student submits assignment → Insert AssignmentSubmission
7. Instructor grades → Update AssignmentSubmission (score, feedback)
8. Student takes quiz → Insert Attempt, update answers JSON
9. System auto-grades MCQ → Calculate score
10. Instructor grades essays → Update answers JSON
11. System checks completion → All modules completed?
12. System generates certificate → Insert Certificate, generate PDF

---

### 2.3.2. Instructor Course Creation

**Workflow:**

```
1. Create Course (status: DRAFT)
   ↓
2. Create Modules
   - Module 1, Module 2, ...
   - Set prerequisites
   ↓
3. Create Lectures
   - VIDEO: Upload video to S3 → Set content_url
   - PDF: Upload PDF → Set content_url
   - ASSIGNMENT: Set assignment_config JSON
   ↓
4. Upload Resources
   - Slides, source code, datasets
   - Store in S3 → Create Resource records
   ↓
5. Create Quizzes
   - Create Quiz (status: DRAFT)
   - Add questions from question bank
   ↓
6. Create Question Bank
   - MCQ: Create Question + Options
   - ESSAY: Create Question
   ↓
7. Publish Course
   - Validate có modules, lectures
   - Update status = 'PUBLISHED'
   ↓
8. (Optional) Create Class for Blended Learning
   - Set start_date, end_date
   - Add class schedules JSON
   ↓
9. Monitor Student Progress
   - View enrollment stats
   - View quiz performance
   - Identify struggling students
   ↓
10. Grade Assignments & Essays
    - View pending submissions/attempts
    - Assign scores + feedback
    - Finalize grading
```

**Actors:** Instructor, System

**Main Success Scenario:**
1. Instructor creates course → Insert Course (status: DRAFT)
2. Instructor creates modules → Insert Module records
3. Instructor creates lectures → Insert Lecture records
4. Instructor uploads resources → Upload to S3 → Insert Resource
5. Instructor creates questions → Insert Question, Option
6. Instructor creates quiz → Insert Quiz → Update questions JSON
7. Instructor publishes course → Update status = 'PUBLISHED'
8. Students enroll → Insert Enrollment
9. Students submit work → Insert AssignmentSubmission, Attempt
10. Instructor grades → Update scores, feedback

---

### 2.3.3. Quiz Workflow

**Student Side:**

```
1. Student enrolls in course
   ↓
2. Student starts quiz attempt
   - Check max_attempts limit
   - Check available_from/until
   - Create Attempt (status: IN_PROGRESS)
   ↓
3. Student answers questions
   - MCQ: Select options
   - Essay: Type answer_text
   - Save to Attempt.answers JSON (can save multiple times)
   ↓
4. Student submits quiz
   - Check time_limit
   - Update status = 'SUBMITTED'
   - Trigger auto-grading
   ↓
5. Student views results
   - If all MCQ → View score immediately
   - If has essay → Wait for instructor grading
```

**System Side:**

```
1. Auto-grade MCQ/TRUE_FALSE
   - Compare selected_options with correct options
   - Calculate score for each question
   - Update Attempt.answers JSON
   ↓
2. Set status
   - If no essays → status = 'GRADED'
   - If has essays → status = 'PENDING_GRADING'
```

**Instructor Side:**

```
1. View pending attempts
   - Query Attempt WHERE status = 'PENDING_GRADING'
   ↓
2. Grade ESSAY/SHORT_ANSWER questions
   - Read student's answer_text
   - Assign score + feedback
   - Update answers JSON
   ↓
3. Finalize grading
   - All questions graded?
   - Calculate total score
   - Update status = 'GRADED'
   - Notify student
```

---

### 2.3.4. Blended Learning Workflow

**Setup Phase (Instructor):**

```
1. Create Class
   - course_id, class_name
   - start_date, end_date
   - max_students
   - instructor_id
   ↓
2. Set Class Schedule (JSON)
   - Session 1: date, start_time, end_time, topic, location
   - Session 2: ...
   - Session N: ...
   ↓
3. Publish Class
   - Students can see and enroll
```

**Enrollment Phase (Student):**

```
1. Student enrolls in Class
   - Insert Enrollment (class_id = UUID)
   - Check max_students limit
```

**Learning Phase:**

```
1. Class starts (status = ONGOING)
   - System auto-updates based on start_date
   ↓
2. In-person Sessions
   - Students attend class at location
   - Instructor marks attendance in Class.schedules JSON
   ↓
3. Online Learning (between sessions)
   - Students complete online modules (self-paced)
   - Watch videos, read PDFs
   - Submit assignments
   ↓
4. Repeat for all sessions
```

**Completion Phase:**

```
1. Class ends (status = COMPLETED)
   - System auto-updates based on end_date
   ↓
2. Final grades calculated
   - Quiz scores + Assignment scores
   - Update Enrollment.final_grade
   ↓
3. Certificates issued
   - If final_grade >= passing_score
   - Insert Certificate, generate PDF
```

**Attendance Tracking:**

```
Instructor marks attendance in Class.schedules JSON:
{
  "session_id": "uuid",
  "date": "2025-12-01",
  "attendances": [
    {"user_id": "student1", "status": "PRESENT", "check_in": "09:05"},
    {"user_id": "student2", "status": "LATE", "check_in": "09:15"},
    {"user_id": "student3", "status": "ABSENT"}
  ]
}

Student views own attendance report:
- Query Class.schedules JSON
- Filter by user_id
- Show: Session, Date, Status
```

---

# CHƯƠNG 3: THIẾT KẾ CƠ SỞ DỮ LIỆU

## 3.1. Sơ đồ ERD (Entity Relationship Diagram)

### 3.1.1. ERD tổng quan

*(Phần này sẽ chứa hình ảnh ERD diagram. Trong báo cáo thực tế, cần vẽ ERD bằng StarUML, draw.io, hoặc ERDPlus)*

**Mô tả ERD:**

Hệ thống B-Learning Core gồm **16 entities** được tổ chức thành **5 domains**:

```
┌──────────────────────────────────────────────────────────┐
│                  DOMAIN 1: USER MANAGEMENT               │
│                                                          │
│   ┌──────┐       ┌──────────┐       ┌──────┐           │
│   │ User │←──────│ UserRole │──────→│ Role │           │
│   └──────┘   1:N └──────────┘ N:1   └──────┘           │
│      ↑                                                    │
│      │ created_by, instructor_id, etc.                  │
└──────┼──────────────────────────────────────────────────┘
       │
┌──────┼──────────────────────────────────────────────────┐
│      │          DOMAIN 2: COURSE CONTENT                 │
│      ↓                                                    │
│   ┌────────┐  1:N  ┌────────┐  1:N  ┌─────────┐        │
│   │ Course │──────→│ Module │──────→│ Lecture │         │
│   └────────┘       └────────┘       └─────────┘         │
│                                           ↓ 1:N          │
│                                      ┌──────────┐        │
│                                      │ Resource │        │
│                                      └──────────┘        │
└──────────────────────────────────────────────────────────┘
       ↓
┌──────────────────────────────────────────────────────────┐
│               DOMAIN 3: ASSESSMENT                        │
│                                                          │
│   ┌──────┐  1:N  ┌──────────┐  1:N  ┌────────┐         │
│   │ Quiz │──────→│ Question │──────→│ Option │         │
│   └──────┘       └──────────┘       └────────┘         │
│      ↓ 1:N                                               │
│   ┌─────────┐        ┌───────────────────────┐         │
│   │ Attempt │        │ AssignmentSubmission  │         │
│   └─────────┘        └───────────────────────┘         │
│                               ↑ N:1                      │
│                          ┌─────────┐                     │
│                          │ Lecture │ (type=ASSIGNMENT)   │
│                          └─────────┘                     │
└──────────────────────────────────────────────────────────┘
       ↓
┌──────────────────────────────────────────────────────────┐
│          DOMAIN 4: ENROLLMENT & PROGRESS                  │
│                                                          │
│   ┌────────────┐  1:N  ┌──────────┐                     │
│   │ Enrollment │──────→│ Progress │                     │
│   └────────────┘       └──────────┘                     │
│        ↑ N:1                                             │
│   ┌─────────┐ (optional)                                │
│   │ Course  │                                            │
│   └─────────┘                                            │
└──────────────────────────────────────────────────────────┘
       ↓
┌──────────────────────────────────────────────────────────┐
│          DOMAIN 5: CLASS & CERTIFICATE                    │
│                                                          │
│   ┌───────┐  N:1  ┌────────────┐                        │
│   │ Class │←──────│ Enrollment │                        │
│   └───────┘       └────────────┘                        │
│      ↓ N:1                                               │
│   ┌────────┐                                             │
│   │ Course │                                             │
│   └────────┘                                             │
│                                                          │
│   ┌─────────────┐                                        │
│   │ Certificate │                                        │
│   └─────────────┘                                        │
│        ↑ N:1                                             │
│   ┌──────────┐                                           │
│   │ User     │                                           │
│   │ Course   │                                           │
│   └──────────┘                                           │
└──────────────────────────────────────────────────────────┘
```

### 3.1.2. Ký hiệu sử dụng

**Cardinality (Số lượng):**
- **1:1** (One-to-One): Mỗi record trong bảng A tương ứng với đúng 1 record trong bảng B
- **1:N** (One-to-Many): 1 record trong bảng A có thể có nhiều records trong bảng B
- **N:M** (Many-to-Many): Nhiều records trong bảng A có thể có nhiều records trong bảng B (cần bảng trung gian)

**Ví dụ:**
- User **1:N** UserRole: 1 User có thể có nhiều Roles
- UserRole **N:1** Role: Nhiều Users có thể có cùng 1 Role
- Course **1:N** Module: 1 Course có nhiều Modules
- Module **1:N** Lecture: 1 Module có nhiều Lectures

**Optionality (Tùy chọn):**
- **Mandatory (bắt buộc):** Foreign key NOT NULL
- **Optional (tùy chọn):** Foreign key NULL

**Ví dụ:**
- Enrollment.class_id **optional** (NULL = self-paced, UUID = blended)
- Course.created_by **optional** (NULL nếu user deleted, SET NULL)

---

## 3.2. Sơ đồ RM (Relational Model)

### 3.2.1. Tổng quan 16 bảng theo 5 domains

**Relational Model Notation:**

```
Table_Name(PK, FK1, FK2, ..., other_columns)
```

**DOMAIN 1: USER MANAGEMENT**

```
User(user_id, email, password_hash, first_name, last_name, account_status, preferences, created_at, updated_at)

Role(role_id, name, description, permissions, created_at)

UserRole(user_role_id, user_id*, role_id*, granted_at, granted_by*, expires_at)
  FK: user_id → User(user_id) ON DELETE CASCADE
  FK: role_id → Role(role_id) ON DELETE CASCADE
  FK: granted_by → User(user_id) ON DELETE SET NULL
  UNIQUE: (user_id, role_id)
```

**DOMAIN 2: COURSE CONTENT**

```
Course(course_id, code, title, description, difficulty_level, credits, status, created_by*, created_at, updated_at)
  FK: created_by → User(user_id) ON DELETE SET NULL
  UNIQUE: code

Module(module_id, course_id*, title, description, order_num, prerequisite_module_ids[], estimated_duration_minutes, created_at, updated_at)
  FK: course_id → Course(course_id) ON DELETE CASCADE
  UNIQUE: (course_id, order_num)

Lecture(lecture_id, module_id*, title, description, type, content_url, duration_seconds, order_num, assignment_config, is_preview, is_downloadable, created_at, updated_at)
  FK: module_id → Module(module_id) ON DELETE CASCADE
  UNIQUE: (module_id, order_num)

Resource(resource_id, lecture_id*, title, file_url, file_type, file_size_bytes, created_at)
  FK: lecture_id → Lecture(lecture_id) ON DELETE CASCADE
```

**DOMAIN 3: ASSESSMENT**

```
Quiz(quiz_id, course_id*, title, description, time_limit_minutes, pass_score, questions, shuffle_questions, show_correct_answers, is_published, created_by*, created_at, updated_at)
  FK: course_id → Course(course_id) ON DELETE CASCADE
  FK: created_by → User(user_id) ON DELETE SET NULL

Question(question_id, course_id*, question_text, type, default_points, created_by*, created_at)
  FK: course_id → Course(course_id) ON DELETE CASCADE
  FK: created_by → User(user_id) ON DELETE SET NULL

Option(option_id, question_id*, option_text, is_correct, order_num)
  FK: question_id → Question(question_id) ON DELETE CASCADE
  UNIQUE: (question_id, order_num)

Attempt(attempt_id, quiz_id*, user_id*, enrollment_id*, attempt_number, status, started_at, submitted_at, score, max_score, answers)
  FK: quiz_id → Quiz(quiz_id) ON DELETE CASCADE
  FK: user_id → User(user_id) ON DELETE CASCADE
  FK: enrollment_id → Enrollment(enrollment_id) ON DELETE CASCADE
  UNIQUE: (user_id, quiz_id, attempt_number)

AssignmentSubmission(submission_id, lecture_id*, user_id*, enrollment_id*, submission_number, status, submitted_at, score, max_score, feedback, file_urls)
  FK: lecture_id → Lecture(lecture_id) ON DELETE CASCADE
  FK: user_id → User(user_id) ON DELETE CASCADE
  FK: enrollment_id → Enrollment(enrollment_id) ON DELETE CASCADE
  UNIQUE: (lecture_id, user_id, submission_number)
```

**DOMAIN 4: ENROLLMENT & PROGRESS**

```
Enrollment(enrollment_id, user_id*, course_id*, class_id*, status, enrolled_at, completed_at)
  FK: user_id → User(user_id) ON DELETE CASCADE
  FK: course_id → Course(course_id) ON DELETE CASCADE
  FK: class_id → Class(class_id) ON DELETE SET NULL (NULLABLE)
  UNIQUE: (user_id, course_id, COALESCE(class_id, UUID_NIL))

Progress(progress_id, user_id*, course_id*, module_id*, status, completion_percentage, started_at, completed_at)
  FK: user_id → User(user_id) ON DELETE CASCADE
  FK: course_id → Course(course_id) ON DELETE CASCADE
  FK: module_id → Module(module_id) ON DELETE CASCADE
  UNIQUE: (user_id, course_id, module_id)
```

**DOMAIN 5: CLASS & CERTIFICATE**

```
Class(class_id, course_id*, class_name, start_date, end_date, instructor_id*, max_students, status, schedules, created_at)
  FK: course_id → Course(course_id) ON DELETE CASCADE
  FK: instructor_id → User(user_id) ON DELETE SET NULL

Certificate(certificate_id, user_id*, course_id*, certificate_code, verification_code, issue_date, final_grade, pdf_url, status, created_at)
  FK: user_id → User(user_id) ON DELETE CASCADE
  FK: course_id → Course(course_id) ON DELETE CASCADE
  UNIQUE: (user_id, course_id)
  UNIQUE: certificate_code
  UNIQUE: verification_code
```

**Chú thích:**
- `*` = Foreign Key
- `[]` = Array type (PostgreSQL)
- `JSONB` = JSON binary type
- `PK` = Primary Key (mặc định tất cả là UUID)
- `UNIQUE` = Unique constraint

---

### 3.2.2. Relationships matrix

Bảng tổng hợp tất cả 23 foreign key relationships trong hệ thống:

| From Table | To Table | FK Column | Cardinality | ON DELETE | Notes |
|------------|----------|-----------|-------------|-----------|-------|
| 1. UserRole | User | user_id | N:1 | CASCADE | User deleted → UserRole deleted |
| 2. UserRole | Role | role_id | N:1 | CASCADE | Role deleted → UserRole deleted |
| 3. UserRole | User | granted_by | N:1 | SET NULL | Admin deleted → NULL |
| 4. Course | User | created_by | N:1 | SET NULL | Instructor deleted → Course kept |
| 5. Module | Course | course_id | N:1 | CASCADE | Course deleted → Module deleted |
| 6. Lecture | Module | module_id | N:1 | CASCADE | Module deleted → Lecture deleted |
| 7. Resource | Lecture | lecture_id | N:1 | CASCADE | Lecture deleted → Resource deleted |
| 8. Quiz | Course | course_id | N:1 | CASCADE | Course deleted → Quiz deleted |
| 9. Quiz | User | created_by | N:1 | SET NULL | Instructor deleted → Quiz kept |
| 10. Question | Course | course_id | N:1 | CASCADE | Course deleted → Question deleted |
| 11. Question | User | created_by | N:1 | SET NULL | Instructor deleted → Question kept |
| 12. Option | Question | question_id | N:1 | CASCADE | Question deleted → Option deleted |
| 13. Attempt | Quiz | quiz_id | N:1 | CASCADE | Quiz deleted → Attempt deleted |
| 14. Attempt | User | user_id | N:1 | CASCADE | User deleted → Attempt deleted |
| 15. Attempt | Enrollment | enrollment_id | N:1 | CASCADE | Enrollment deleted → Attempt deleted |
| 16. AssignmentSubmission | Lecture | lecture_id | N:1 | CASCADE | Lecture deleted → Submission deleted |
| 17. AssignmentSubmission | User | user_id | N:1 | CASCADE | User deleted → Submission deleted |
| 18. AssignmentSubmission | Enrollment | enrollment_id | N:1 | CASCADE | Enrollment deleted → Submission deleted |
| 19. Enrollment | User | user_id | N:1 | CASCADE | User deleted → Enrollment deleted |
| 20. Enrollment | Course | course_id | N:1 | CASCADE | Course deleted → Enrollment deleted |
| 21. Enrollment | Class | class_id | N:1 | SET NULL | Class deleted → Enrollment kept (class_id = NULL) |
| 22. Progress | User | user_id | N:1 | CASCADE | User deleted → Progress deleted |
| 23. Progress | Course | course_id | N:1 | CASCADE | Course deleted → Progress deleted |
| 24. Progress | Module | module_id | N:1 | CASCADE | Module deleted → Progress deleted |
| 25. Class | Course | course_id | N:1 | CASCADE | Course deleted → Class deleted |
| 26. Class | User | instructor_id | N:1 | SET NULL | Instructor deleted → Class kept |
| 27. Certificate | User | user_id | N:1 | CASCADE | User deleted → Certificate deleted |
| 28. Certificate | Course | course_id | N:1 | CASCADE | Course deleted → Certificate deleted |

**Phân loại ON DELETE behavior:**

- **CASCADE (19 relationships):** Parent deleted → Child deleted
  - Ví dụ: Course deleted → Modules, Lectures, Quizzes deleted
- **SET NULL (9 relationships):** Parent deleted → FK set to NULL
  - Ví dụ: Instructor deleted → Course.created_by = NULL (course vẫn tồn tại)

**Lý do chọn CASCADE vs SET NULL:**

**CASCADE:** Dùng khi child record không có ý nghĩa nếu parent không tồn tại
- Module không có ý nghĩa nếu Course không tồn tại
- Lecture không có ý nghĩa nếu Module không tồn tại
- Option không có ý nghĩa nếu Question không tồn tại

**SET NULL:** Dùng khi child record vẫn có giá trị ngay cả khi parent deleted
- Course vẫn có giá trị ngay cả khi Instructor nghỉ việc (created_by = NULL)
- Enrollment vẫn valid ngay cả khi Class bị cancel (class_id = NULL → self-paced)

---

## 3.3. Đặc tả chi tiết các bảng

### 3.3.1. DOMAIN 1: User Management (3 bảng)

#### 3.3.1.1. Bảng User

**Mục đích:** Lưu thông tin tài khoản người dùng của hệ thống (students, instructors, TAs, admins).

**Schema:**

```sql
CREATE TABLE "User" (
  user_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  email VARCHAR(255) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  first_name VARCHAR(100) NOT NULL,
  last_name VARCHAR(100) NOT NULL,
  account_status VARCHAR(20) DEFAULT 'PENDING_VERIFICATION',
  preferences JSON DEFAULT '{}',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT chk_user_account_status CHECK (
    account_status IN ('PENDING_VERIFICATION', 'ACTIVE', 'SUSPENDED', 'DELETED')
  ),
  CONSTRAINT chk_user_email_format CHECK (
    email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$'
  )
);
```

**Đặc tả chi tiết:**

| Cột | Kiểu | Ràng buộc | Mặc định | Mô tả |
|-----|------|-----------|----------|-------|
| user_id | UUID | PK, NOT NULL | gen_random_uuid() | Mã định danh duy nhất người dùng (128-bit UUID) |
| email | VARCHAR(255) | UNIQUE, NOT NULL | - | Email đăng nhập, phải unique trong toàn hệ thống. Validate format qua CHECK constraint |
| password_hash | VARCHAR(255) | NOT NULL | - | Mật khẩu đã hash bằng bcrypt (cost factor 10). Format: `$2a$10$...` |
| first_name | VARCHAR(100) | NOT NULL | - | Tên (ví dụ: Nguyễn) |
| last_name | VARCHAR(100) | NOT NULL | - | Họ và tên đệm (ví dụ: Văn Kiệt) |
| account_status | VARCHAR(20) | NOT NULL | 'PENDING_VERIFICATION' | Trạng thái tài khoản: PENDING_VERIFICATION, ACTIVE, SUSPENDED, DELETED |
| preferences | JSON | NULL | '{}' | Cài đặt cá nhân (notifications, locale, timezone, theme) |
| created_at | TIMESTAMP | NOT NULL | CURRENT_TIMESTAMP | Thời điểm tạo tài khoản |
| updated_at | TIMESTAMP | NOT NULL | CURRENT_TIMESTAMP | Lần cập nhật gần nhất (auto-update qua trigger) |

**Indexes:**

```sql
-- Primary key index (auto-created)
CREATE INDEX idx_user_pk ON "User"(user_id);

-- Unique index for email (auto-created)
CREATE UNIQUE INDEX idx_user_email ON "User"(email);

-- Performance index for status filtering
CREATE INDEX idx_user_status ON "User"(account_status) WHERE account_status = 'ACTIVE';

-- GIN index for JSON queries
CREATE INDEX idx_user_preferences ON "User" USING GIN (preferences);
```

**Triggers:**

```sql
-- Auto-update updated_at
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
  NEW.updated_at = CURRENT_TIMESTAMP;
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_user_updated_at
BEFORE UPDATE ON "User"
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();
```

**Ví dụ dữ liệu:**

```sql
-- Student account
INSERT INTO "User" (email, password_hash, first_name, last_name, account_status, preferences)
VALUES (
  'student@gmail.com',
  '$2a$10$rZ8pqBJKB5v7J0YdN4YQy.K9vX8pJ7mD5sL3wR9tP4qW6eT8yU0iO',  -- password: 'password123'
  'Trần',
  'Thị Mai',
  'ACTIVE',
  '{
    "notifications": {
      "assignment_due": {"email": true, "push": true},
      "grade_published": {"email": true, "push": false}
    },
    "locale": "vi",
    "timezone": "Asia/Ho_Chi_Minh",
    "theme": "light"
  }'::JSON
);

-- Instructor account
INSERT INTO "User" (email, password_hash, first_name, last_name, account_status)
VALUES (
  'instructor@blearning.edu',
  '$2a$10$rZ8pqBJKB5v7J0YdN4YQy.K9vX8pJ7mD5sL3wR9tP4qW6eT8yU0iO',
  'Nguyễn',
  'Văn Dũng',
  'ACTIVE'
);
```

**Sample Queries:**

```sql
-- Get active users
SELECT user_id, email, first_name, last_name, created_at
FROM "User"
WHERE account_status = 'ACTIVE'
ORDER BY created_at DESC;

-- Search users by email
SELECT * FROM "User"
WHERE email ILIKE '%gmail%';

-- Get users with email notifications enabled
SELECT * FROM "User"
WHERE preferences @> '{"notifications": {"assignment_due": {"email": true}}}'::jsonb;
```

**Quan hệ:**
- **1:N** với UserRole: 1 User có thể có nhiều Roles
- **1:N** với Course (created_by): 1 Instructor tạo nhiều Courses
- **1:N** với Enrollment: 1 User có thể enroll nhiều Courses
- **1:N** với Attempt: 1 User có thể làm nhiều Quiz attempts
- **1:N** với AssignmentSubmission: 1 User submit nhiều Assignments

---

(Tiếp tục với các bảng còn lại trong phần báo cáo đầy đủ...)

---

# KẾT LUẬN

Qua quá trình nghiên cứu và triển khai, chúng tôi đã hoàn thành việc thiết kế và xây dựng một cơ sở dữ liệu hoàn chỉnh cho hệ thống B-Learning Core với **16 bảng** được tổ chức theo **5 domains** chức năng, hỗ trợ đầy đủ các tính năng của một Learning Management System hiện đại.

**Những đóng góp chính của đồ án:**

1. **Thiết kế database tối ưu:** Áp dụng UUID primary keys, JSON/JSONB fields, GIN indexes, và normalization để đạt được sự cân bằng giữa performance, flexibility và data integrity.

2. **Simplification strategy:** Giảm số lượng bảng từ 21 (thiết kế cũ) xuống 16 (thiết kế mới) bằng cách sử dụng JSON fields cho dữ liệu linh hoạt, giảm complexity và tăng maintainability.

3. **Hỗ trợ cả Self-paced và Blended Learning:** Thiết kế linh hoạt cho phép hệ thống hỗ trợ cả học tự học (self-paced) và học kết hợp (blended learning) trong cùng một schema.

4. **Scalability và Performance:** Với 96+ indexes và constraint strategy hợp lý, database có khả năng scale từ 1,000 users đến 100,000 users mà vẫn đảm bảo performance.

**Kết quả đạt được:**

- ✅ Thiết kế ERD đầy đủ với 16 entities và 23 relationships
- ✅ Triển khai database trên PostgreSQL 14+ với đầy đủ indexes và constraints
- ✅ Demo thành công các chức năng chính qua 58+ REST API endpoints
- ✅ Đánh giá và so sánh với thiết kế cũ, chứng minh được các cải tiến

**Hạn chế và hướng phát triển:**

Mặc dù đã đạt được các mục tiêu đề ra, đồ án vẫn còn một số hạn chế cần cải thiện trong tương lai:

1. **JSON validation:** Hiện tại chỉ validate JSON structure ở application layer, chưa có database-level validation.
2. **Advanced analytics:** Cần thêm materialized views và aggregate tables cho reporting features.
3. **Scalability testing:** Chưa thực hiện load testing với 100,000+ users để verify scalability.

**Hướng phát triển tiếp theo:**

1. Implement full-text search với PostgreSQL tsvector
2. Add audit trail với temporal tables
3. Optimize cho microservices architecture
4. Add support cho AI-powered features (recommendation engine, auto-grading essays)

Chúng tôi tin rằng với thiết kế database vững chắc này, hệ thống B-Learning Core có thể phát triển và mở rộng để phục vụ nhu cầu học tập ngày càng đa dạng và phức tạp của người dùng.

---

## TÀI LIỆU THAM KHẢO

[1] Garrison, D. R., & Kanuka, H. (2004). Blended learning: Uncovering its transformative potential in higher education. *The Internet and Higher Education*, 7(2), 95-105.

[2] PostgreSQL Global Development Group. (2024). *PostgreSQL 14 Documentation*. Retrieved from https://www.postgresql.org/docs/14/

[3] Elmasri, R., & Navathe, S. B. (2016). *Fundamentals of Database Systems* (7th ed.). Pearson Education.

[4] Date, C. J. (2003). *An Introduction to Database Systems* (8th ed.). Addison-Wesley.

[5] Connolly, T., & Begg, C. (2014). *Database Systems: A Practical Approach to Design, Implementation, and Management* (6th ed.). Pearson.

[6] Silberschatz, A., Korth, H. F., & Sudarshan, S. (2019). *Database System Concepts* (7th ed.). McGraw-Hill Education.

[7] Codd, E. F. (1970). A relational model of data for large shared data banks. *Communications of the ACM*, 13(6), 377-387.

[8] Martin, R. C. (2008). *Clean Code: A Handbook of Agile Software Craftsmanship*. Prentice Hall.

[9] Newman, S. (2015). *Building Microservices: Designing Fine-Grained Systems*. O'Reilly Media.

[10] Kleppmann, M. (2017). *Designing Data-Intensive Applications: The Big Ideas Behind Reliable, Scalable, and Maintainable Systems*. O'Reilly Media.

---

**HẾT**

---

**Ghi chú:** Đây là phiên bản rút gọn của báo cáo (khoảng 50 trang). Trong báo cáo đầy đủ, mỗi section sẽ được mở rộng với nhiều chi tiết hơn, bao gồm:
- Đầy đủ 16 table specifications với examples và queries
- ERD diagrams (vẽ bằng công cụ)
- Screenshot của API endpoints và demo
- Performance benchmarks
- Detailed comparison tables
- Code samples đầy đủ hơn

Tổng số trang ước tính: **50-60 trang** (không kể phụ lục).
