# REQ-4: Plan Task - Tạo Báo Cáo Database B-Learning

**Mục tiêu:** Tạo báo cáo hoàn chỉnh cho thiết kế database B-Learning theo khuôn mẫu chuyên nghiệp, phong cách báo cáo sinh viên.

---

## 📋 OVERVIEW

Tạo 2 báo cáo chính:
1. **Báo cáo chính** (MAIN_REPORT.md) - Báo cáo đầy đủ theo khuôn mẫu tham khảo
2. **Báo cáo FAQ & Giải thích** (FAQ_EXPLANATION.md) - Câu hỏi vấn đáp và giải thích chi tiết

---

## 🎯 TASK 1: Phân tích tài liệu tham khảo

### 1.1. Đọc và phân tích Scripts/Tham_khao.pdf

**Mục tiêu:** Hiểu rõ cấu trúc, format, phong cách của báo cáo mẫu

**Nhiệm vụ:**
- [ ] Đọc toàn bộ file Scripts/Tham_khao.pdf
- [ ] Ghi chú cấu trúc báo cáo:
  - Bố cục chương mục (TOC - Table of Contents)
  - Cách trình bày phần MỞ ĐẦU
  - Cách trình bày phần NỘI DUNG CHÍNH
  - Cách trình bày phần KẾT LUẬN
  - Định dạng tiêu đề (Heading levels)
  - Cách đánh số (1.1, 1.1.1, ...)
  - Style danh sách (bullet points, numbered list)
- [ ] Ghi chú cách trình bày hình ảnh/sơ đồ:
  - Vị trí đặt ảnh (trước/sau đoạn văn)
  - Cách đánh số hình (Hình 1.1, Hình 2.1, ...)
  - Caption format
  - Cách tham chiếu ảnh trong văn bản
- [ ] Ghi chú giọng văn (tone):
  - Ngôi thứ (ngôi thứ 1, thứ 3?)
  - Formal/informal level
  - Cách diễn đạt chuyên môn
  - Cách giải thích thuật ngữ kỹ thuật
- [ ] Ghi chú format bảng biểu:
  - Border style
  - Header format
  - Cách trình bày data trong bảng
- [ ] Tạo file **ANALYSIS_TEMPLATE.md** tổng hợp cấu trúc mẫu

**Output:** File ANALYSIS_TEMPLATE.md chứa:
- Cấu trúc chương mục
- Template cho từng phần
- Style guide
- Examples từ tài liệu tham khảo

---

### 1.2. Đọc và phân tích Scripts/DTPM_B-Learning.pdf

**Mục tiêu:** Hiểu database CŨ (SAI) để so sánh và highlight điểm cải tiến

**Nhiệm vụ:**
- [ ] Đọc toàn bộ file Scripts/DTPM_B-Learning.pdf
- [ ] Liệt kê tất cả vấn đề của DB cũ:
  - Thiếu bảng nào?
  - Quan hệ sai ở đâu?
  - Thiết kế không hợp lý ở điểm nào?
  - Missing constraints?
  - Performance issues?
- [ ] Tạo bảng so sánh: DB CŨ vs DB MỚI (98-B-Learning-Core)
- [ ] Ghi chú những điểm cần NHẤN MẠNH trong báo cáo chính:
  - "Cải tiến từ phiên bản cũ"
  - "Giải quyết vấn đề X của DB cũ"

**Output:** File COMPARISON_OLD_VS_NEW.md chứa:
- Danh sách vấn đề DB cũ
- Bảng so sánh chi tiết
- Điểm cải tiến cần highlight

---

## 🎯 TASK 2: Thu thập thông tin Database Mới (98-B-Learning-Core)

### 2.1. Đọc toàn bộ tài liệu hiện có

**Nhiệm vụ:**
- [ ] Đọc 98-B-Learing-Core/documents/DATABASE-SCHEMA.md
- [ ] Đọc 98-B-Learing-Core/documents/ERD-SPEC.md
- [ ] Đọc 98-B-Learing-Core/documents/FUNCTIONAL-REQUIREMENTS.md
- [ ] Đọc 98-B-Learing-Core/documents/TABLES-EXPLANATION-VI.md
- [ ] Đọc 98-B-Learing-Core/documents/BFD-SPEC.md
- [ ] Đọc 98-B-Learing-Core/documents/API-ENDPOINTS.md

**Output:** Tổng hợp kiến thức về:
- 31 tables với đầy đủ fields, types, constraints
- Tất cả relationships (1-1, 1-N, N-N)
- Business logic
- Use cases

---

### 2.2. Phân tích SQL Scripts

**Nhiệm vụ:**
- [ ] Đọc 98-B-Learing-Core/sql/01-schema.sql - Structure
- [ ] Đọc 98-B-Learing-Core/sql/02-indexes.sql - Performance optimization
- [ ] Đọc 98-B-Learing-Core/sql/03-constraints.sql - Business rules
- [ ] Đọc 98-B-Learing-Core/sql/04-seed-data.sql - Sample data

**Output:** Hiểu rõ:
- DDL statements
- Indexes strategy
- Constraint strategy
- Data patterns

---

## 🎯 TASK 3: Tạo Báo Cáo Chính (MAIN_REPORT.md)

### 3.1. Cấu trúc báo cáo (theo ANALYSIS_TEMPLATE.md)

**Đề xuất cấu trúc:**

```markdown
# BÁO CÁO THIẾT KẾ DATABASE HỆ THỐNG B-LEARNING

## DANH MỤC (Table of Contents)
[Tự động generate từ headings]

## DANH MỤC HÌNH ẢNH
[List tất cả hình với caption]

## DANH MỤC BẢNG
[List tất cả bảng với caption]

## LỜI MỞ ĐẦU
- Giới thiệu đề tài
- Mục tiêu của báo cáo
- Phạm vi nghiên cứu
- Cấu trúc báo cáo

## CHƯƠNG 1: TỔNG QUAN VỀ HỆ THỐNG
### 1.1. Giới thiệu hệ thống B-Learning
### 1.2. Mục tiêu và phạm vi
### 1.3. Đối tượng sử dụng
### 1.4. Các chức năng chính
### 1.5. Yêu cầu phi chức năng

## CHƯƠNG 2: PHÂN TÍCH YÊU CẦU
### 2.1. Yêu cầu chức năng
#### 2.1.1. Quản lý người dùng
#### 2.1.2. Quản lý khóa học
#### 2.1.3. Quản lý nội dung
#### 2.1.4. Quản lý bài tập và kiểm tra
#### 2.1.5. Theo dõi tiến độ
#### 2.1.6. Tương tác và giao tiếp
#### 2.1.7. Báo cáo và phân tích
### 2.2. Yêu cầu phi chức năng
#### 2.2.1. Hiệu năng
#### 2.2.2. Bảo mật
#### 2.2.3. Khả năng mở rộng
#### 2.2.4. Tính sẵn sàng

## CHƯƠNG 3: THIẾT KẾ CƠ SỞ DỮ LIỆU
### 3.1. Lựa chọn DBMS
#### 3.1.1. So sánh các DBMS
#### 3.1.2. Lý do chọn PostgreSQL
### 3.2. Sơ đồ ERD (Entity Relationship Diagram)
[Chỉ dẫn: Hình 3.1 - ERD tổng thể]
[Giải thích các entities chính]
### 3.3. Phân tích các nhóm chức năng
#### 3.3.1. User Management (6 tables)
[Chỉ dẫn: Hình 3.2 - ERD User Management]
- User
- Role
- UserRole
- UserProfile
- UserSettings
- AuditLog
#### 3.3.2. Course Management (7 tables)
[Chỉ dẫn: Hình 3.3 - ERD Course Management]
- Course
- Category
- Module
- Lecture
- LectureContent
- Attachment
- Tag + CourseTag
#### 3.3.3. Class & Enrollment (2 tables)
[Chỉ dẫn: Hình 3.4 - ERD Class & Enrollment]
- Class
- Enrollment
#### 3.3.4. Learning Progress (3 tables)
[Chỉ dẫn: Hình 3.5 - ERD Learning Progress]
- LectureProgress
- Certificate
- UserBadge
#### 3.3.5. Assessments (4 tables)
[Chỉ dẫn: Hình 3.6 - ERD Assessments]
- Quiz
- Question
- QuizAttempt
- Answer
#### 3.3.6. Assignments (2 tables)
[Chỉ dẫn: Hình 3.7 - ERD Assignments]
- AssignmentSubmission
- SubmissionReview
#### 3.3.7. Grading (2 tables)
[Chỉ dẫn: Hình 3.8 - ERD Grading]
- GradeBook
- GradeScale
#### 3.3.8. Interaction (3 tables)
[Chỉ dẫn: Hình 3.9 - ERD Interaction]
- Discussion
- DiscussionReply
- Notification
#### 3.3.9. System (1 table)
[Chỉ dẫn: Hình 3.10 - ERD System]
- SystemConfig

### 3.4. Mô tả chi tiết các bảng
[Cho mỗi bảng:]
#### 3.4.X. Bảng [TÊN_BẢNG]
**Mục đích:** [Giải thích tại sao cần bảng này]

**Cấu trúc:**
[Bảng với columns: Tên trường | Kiểu dữ liệu | Ràng buộc | Mô tả]

**Ví dụ:**
| Tên trường | Kiểu dữ liệu | Ràng buộc | Mô tả |
|------------|--------------|-----------|-------|
| user_id | UUID | PK, NOT NULL | ID duy nhất của người dùng |
| email | VARCHAR(255) | UNIQUE, NOT NULL | Email đăng nhập |
| ... | ... | ... | ... |

**Quan hệ:**
- [Bảng A] 1-N [Bảng này]: [Giải thích]
- [Bảng này] N-1 [Bảng B]: [Giải thích]

**Indexes:**
- idx_[tên]: [Lý do tạo index này]

**Business Rules:**
- [Rule 1]: [Giải thích]
- [Rule 2]: [Giải thích]

**Ví dụ dữ liệu:**
[Bảng với 2-3 rows sample data]

### 3.5. Chiến lược Indexing
#### 3.5.1. Primary Keys
#### 3.5.2. Foreign Keys
#### 3.5.3. Unique Constraints
#### 3.5.4. Performance Indexes
#### 3.5.5. Full-text Search Indexes

### 3.6. Constraints và Business Rules
#### 3.6.1. NOT NULL Constraints
#### 3.6.2. CHECK Constraints
#### 3.6.3. UNIQUE Constraints
#### 3.6.4. Foreign Key Constraints
#### 3.6.5. Business Logic Constraints

### 3.7. Triggers và Stored Procedures
[Nếu có]

## CHƯƠNG 4: TRIỂN KHAI VÀ DEMO
### 4.1. Môi trường triển khai
#### 4.1.1. Technology Stack
- Backend: Python 3.11 + FastAPI
- Frontend: Next.js 14 + React 18 + TypeScript
- Database: PostgreSQL 14
- Deployment: Docker + Docker Compose
#### 4.1.2. Kiến trúc hệ thống
[Chỉ dẫn: Hình 4.1 - System Architecture]
### 4.2. Database Setup
#### 4.2.1. Schema Creation
#### 4.2.2. Seed Data
#### 4.2.3. Indexes & Constraints
### 4.3. Demo Application
#### 4.3.1. Các màn hình chính
[Chỉ dẫn: Screenshots từ B-Learning-Demo]
- Course Management
- Module & Lecture Management
- Enrollment Management
- Assignment Submission
- Grading System
#### 4.3.2. Workflow demo
[Chỉ dẫn: Hình 4.X - Workflow từ Create Course → Grade Submission]

## CHƯƠNG 5: ĐÁNH GIÁ VÀ CẢI TIẾN
### 5.1. So sánh với phiên bản cũ
[Sử dụng COMPARISON_OLD_VS_NEW.md]
#### 5.1.1. Vấn đề của DB cũ
#### 5.1.2. Cải tiến trong DB mới
#### 5.1.3. Bảng so sánh chi tiết
### 5.2. Ưu điểm của thiết kế
#### 5.2.1. Normalization (3NF)
#### 5.2.2. Scalability
#### 5.2.3. Performance
#### 5.2.4. Maintainability
### 5.3. Hạn chế và hướng phát triển
#### 5.3.1. Các hạn chế hiện tại
#### 5.3.2. Kế hoạch mở rộng
#### 5.3.3. Tính năng tương lai

## KẾT LUẬN
- Tổng kết những gì đã làm
- Đánh giá kết quả đạt được
- Bài học kinh nghiệm
- Hướng phát triển

## TÀI LIỆU THAM KHẢO
[Danh sách tài liệu, websites, books]

## PHU LỤC
### Phụ lục A: SQL Scripts đầy đủ
### Phụ lục B: API Endpoints
### Phụ lục C: Sample Data
### Phụ lục D: Glossary (Thuật ngữ)
```

---

### 3.2. Hướng dẫn chi tiết cho từng phần

#### 3.2.1. Lời mở đầu
**Nội dung cần có:**
- Giới thiệu tổng quan về E-learning và tầm quan trọng
- Lý do chọn đề tài thiết kế DB cho hệ thống B-Learning
- Mục tiêu cụ thể của báo cáo
- Phạm vi: chỉ tập trung vào database design (không làm full application)
- Cấu trúc báo cáo: tóm tắt nội dung các chương

**Giọng văn:** Formal, học thuật, ngôi thứ 3 hoặc "chúng tôi"

**Độ dài:** 1-2 trang

---

#### 3.2.2. Chương 1: Tổng quan
**Nội dung cần có:**
- Định nghĩa B-Learning (Blended Learning)
- Các tính năng cốt lõi của 1 LMS (Learning Management System)
- Đối tượng: Students, Instructors, TAs, Admins
- Use cases chính
- Yêu cầu phi chức năng: Performance, Security, Scalability

**Chỉ dẫn hình ảnh:**
- Hình 1.1: Use Case Diagram (tổng quát)
- Hình 1.2: Actor Diagram

**Độ dài:** 3-4 trang

---

#### 3.2.3. Chương 2: Phân tích yêu cầu
**Nội dung cần có:**
- Chi tiết từng chức năng (dựa vào FUNCTIONAL-REQUIREMENTS.md)
- User stories cho từng actor
- Business rules

**Chỉ dẫn hình ảnh:**
- Hình 2.1: Business Process Diagram (Course Creation)
- Hình 2.2: Business Flow Diagram (Assignment Submission & Grading)
- Hình 2.3: Business Flow Diagram (Quiz Taking)

**Độ dài:** 5-7 trang

---

#### 3.2.4. Chương 3: Thiết kế CSDL (QUAN TRỌNG NHẤT)
**Nội dung cần có:**
- Giải thích từng bảng (31 tables)
- ERD cho từng nhóm chức năng
- Quan hệ giữa các bảng
- Constraints và lý do
- Indexes và lý do
- Normalization analysis

**Chỉ dẫn hình ảnh:**
- Hình 3.1: ERD Tổng thể (31 tables)
- Hình 3.2 - 3.10: ERD từng nhóm (như đã liệt kê)
- Bảng mô tả cho TỪNG table (31 bảng)

**Độ dài:** 30-40 trang (phần dài nhất)

**Style:**
```markdown
#### 3.4.1. Bảng User

**Mục đích:**
Lưu trữ thông tin cơ bản của tất cả người dùng trong hệ thống, bao gồm students, instructors, teaching assistants và administrators. Đây là bảng trung tâm cho quản lý authentication và authorization.

**Cấu trúc:**

| Tên trường | Kiểu dữ liệu | Ràng buộc | Mô tả |
|------------|--------------|-----------|-------|
| user_id | UUID | PRIMARY KEY | Mã định danh duy nhất của người dùng |
| email | VARCHAR(255) | UNIQUE, NOT NULL | Email đăng nhập, phải unique trong hệ thống |
| password_hash | VARCHAR(255) | NOT NULL | Mật khẩu đã được mã hóa (bcrypt) |
| first_name | VARCHAR(100) | NOT NULL | Tên |
| last_name | VARCHAR(100) | NOT NULL | Họ và tên đệm |
| account_status | VARCHAR(20) | NOT NULL, CHECK | Trạng thái: ACTIVE, SUSPENDED, PENDING |
| created_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Thời điểm tạo tài khoản |
| updated_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Thời điểm cập nhật gần nhất |

**Quan hệ:**
- User 1-N UserRole: Một user có thể có nhiều roles (ví dụ: vừa là student vừa là instructor)
- User 1-1 UserProfile: Mỗi user có 1 profile chứa thông tin chi tiết
- User 1-N Enrollment: Một user có thể enroll vào nhiều courses
- User 1-N AssignmentSubmission: Một student có thể submit nhiều assignments
- User 1-N AuditLog: Ghi lại tất cả actions của user

**Indexes:**
- `idx_user_email`: Tăng tốc độ login và tìm kiếm user theo email
- `idx_user_status`: Lọc users theo trạng thái (ACTIVE, SUSPENDED)
- `idx_user_created`: Sắp xếp users theo thời gian đăng ký

**Business Rules:**
- Email phải unique và follow format chuẩn (check tại application layer)
- Password phải được hash trước khi lưu (NEVER store plain text)
- Account status chỉ được phép: ACTIVE, SUSPENDED, PENDING, DELETED
- Khi user bị SUSPENDED, không thể login và access resources
- Soft delete: Khi xóa user, chuyển status thành DELETED thay vì xóa record

**Ví dụ dữ liệu:**

| user_id | email | first_name | last_name | account_status | created_at |
|---------|-------|------------|-----------|----------------|------------|
| 20000000-... | admin@example.com | Admin | User | ACTIVE | 2025-01-01 |
| 20000001-... | john.doe@student.edu | John | Doe | ACTIVE | 2025-01-15 |
| 20000002-... | jane.smith@instructor.edu | Jane | Smith | ACTIVE | 2025-01-10 |
```

---

#### 3.2.5. Chương 4: Triển khai và Demo
**Nội dung cần có:**
- Giải thích tech stack
- Hướng dẫn setup database
- Screenshots demo application
- Workflow demo cụ thể

**Chỉ dẫn hình ảnh:**
- Hình 4.1: System Architecture
- Hình 4.2: Database Setup Flow
- Hình 4.3-4.10: Screenshots từ demo app
- Hình 4.11: Workflow diagram (Create Course → Enroll → Submit → Grade)

**Độ dài:** 8-10 trang

---

#### 3.2.6. Chương 5: Đánh giá
**Nội dung cần có:**
- So sánh DB cũ vs DB mới (COMPARISON_OLD_VS_NEW.md)
- Highlight improvements
- Phân tích ưu điểm thiết kế
- Thừa nhận hạn chế
- Đề xuất hướng phát triển

**Chỉ dẫn hình ảnh:**
- Bảng so sánh DB cũ vs mới
- Biểu đồ performance (nếu có metrics)

**Độ dài:** 5-6 trang

---

#### 3.2.7. Kết luận
**Nội dung cần có:**
- Tóm tắt những gì đã làm được
- Đánh giá mức độ đạt được mục tiêu
- Kiến thức và kỹ năng học được
- Bài học kinh nghiệm
- Lời cảm ơn (nếu cần)

**Độ dài:** 1-2 trang

---

### 3.3. Yêu cầu về hình ảnh và sơ đồ

**Danh sách hình cần có:**
1. ERD tổng thể (31 tables) - Hình 3.1
2. ERD từng nhóm chức năng (9 hình) - Hình 3.2 đến 3.10
3. Use Case Diagram - Hình 1.1
4. Business Flow Diagrams (3-4 hình) - Hình 2.x
5. System Architecture - Hình 4.1
6. Screenshots demo app (7-10 hình) - Hình 4.x
7. Comparison charts - Hình 5.x

**Format hình:**
- Tất cả sơ đồ ERD: sử dụng StarUML hoặc draw.io
- Business diagrams: draw.io hoặc Lucidchart
- Screenshots: PNG, 1920x1080 hoặc cao hơn

**Chỉ dẫn trong báo cáo:**
```markdown
[CHỈ DẪN: Tại đây cần chèn Hình 3.1 - ERD Tổng thể]
- Format: PNG
- Size: Full width
- Caption: "Hình 3.1: Sơ đồ ERD tổng thể của hệ thống B-Learning (31 tables)"
- Nội dung: Hiển thị tất cả 31 tables với primary keys, foreign keys
- Màu sắc: Phân nhóm theo chức năng (User = xanh, Course = vàng, Assessment = đỏ, ...)
```

---

### 3.4. Yêu cầu về giọng văn

**Ngôi thứ:** Ngôi thứ 1 số nhiều ("chúng tôi") hoặc câu bị động

**Ví dụ ĐÚNG:**
- "Chúng tôi đã thiết kế bảng User để lưu trữ thông tin người dùng..."
- "Bảng User được thiết kế để lưu trữ thông tin người dùng..."
- "Trong quá trình phân tích, chúng tôi nhận thấy..."

**Ví dụ SAI:**
- "Tôi thiết kế..." (ngôi thứ 1 số ít)
- "Mình làm..." (informal)
- "Database này rất tốt" (không chuyên nghiệp)

**Tone:**
- Formal, học thuật
- Khách quan, chuyên nghiệp
- Giải thích rõ ràng, có ví dụ cụ thể
- Tránh subjective opinions không có data chứng minh

**Thuật ngữ:**
- Sử dụng thuật ngữ tiếng Anh khi cần: "Entity", "Relationship", "Normalization"
- Giải thích thuật ngữ lần đầu tiên xuất hiện
- Consistency: dùng 1 thuật ngữ throughout (không đổi qua lại)

---

### 3.5. Format và Style Guide

**Headings:**
```markdown
# CHƯƠNG 1: TÊN CHƯƠNG (H1)
## 1.1. Mục cấp 1 (H2)
### 1.1.1. Mục cấp 2 (H3)
#### 1.1.1.1. Mục cấp 3 (H4)
```

**Lists:**
```markdown
- Bullet point
  - Sub-point
  - Sub-point

1. Numbered list
   1.1. Sub-item
   1.2. Sub-item
```

**Tables:**
```markdown
| Header 1 | Header 2 | Header 3 |
|----------|----------|----------|
| Data 1   | Data 2   | Data 3   |
```

**Code blocks:**
```markdown
```sql
CREATE TABLE "User" (
    user_id UUID PRIMARY KEY DEFAULT gen_random_uuid()
);
```
```

**Emphasis:**
- **Bold** cho keywords, terminology
- *Italic* cho emphasis
- `Code` cho table names, column names, SQL keywords

---

## 🎯 TASK 4: Tạo Báo Cáo FAQ & Giải thích (FAQ_EXPLANATION.md)

### 4.1. Mục đích

Tạo tài liệu riêng để:
- Giải thích chi tiết ý nghĩa từng bảng
- Giải thích tại sao cần các quan hệ
- Chuẩn bị câu hỏi vấn đáp cho presentation/defense
- Giúp người đọc (và chính user) hiểu sâu về DB design

---

### 4.2. Cấu trúc đề xuất

```markdown
# FAQ & GIẢI THÍCH CHI TIẾT - DATABASE B-LEARNING

## MỤC LỤC
1. [Câu hỏi chung về thiết kế](#general)
2. [User Management](#user-management)
3. [Course Management](#course-management)
4. [Enrollment & Class](#enrollment)
5. [Learning Progress](#progress)
6. [Assessments](#assessments)
7. [Assignments](#assignments)
8. [Grading](#grading)
9. [Interaction](#interaction)
10. [System](#system)
11. [Relationships & Constraints](#relationships)
12. [Performance & Optimization](#performance)

---

## 1. CÂU HỎI CHUNG VỀ THIẾT KẾ

### Q1.1: Tại sao chọn PostgreSQL thay vì MySQL hay MongoDB?

**Trả lời:**

PostgreSQL được chọn vì các lý do sau:

1. **ACID Compliance mạnh mẽ:**
   - LMS cần đảm bảo data consistency cao (điểm số, submissions, enrollments)
   - PostgreSQL có transaction support tốt hơn MySQL

2. **Advanced Data Types:**
   - JSON/JSONB cho flexible data (assignment_config, quiz_config)
   - Array types (file_urls[], tags[])
   - UUID native support

3. **Full-text Search:**
   - Tìm kiếm courses, lectures, discussions
   - PostgreSQL có trgm extension mạnh

4. **Performance:**
   - Better query optimizer
   - Parallel query execution
   - Materialized views cho reporting

5. **Extensibility:**
   - Custom functions, triggers
   - Support nhiều extensions

**Tại sao KHÔNG chọn MongoDB:**
- LMS có schema rõ ràng, không cần schema-less
- Cần ACID transactions cho grading, enrollment
- Relationships phức tạp, SQL phù hợp hơn NoSQL

---

### Q1.2: Database có bao nhiêu bảng? Tại sao lại cần nhiều bảng như vậy?

**Trả lời:**

Database có **31 bảng**, chia thành 9 nhóm chức năng:

1. **User Management (6 bảng):** User, Role, UserRole, UserProfile, UserSettings, AuditLog
2. **Course Management (7 bảng):** Course, Category, Module, Lecture, LectureContent, Attachment, Tag
3. **Class & Enrollment (2 bảng):** Class, Enrollment
4. **Learning Progress (3 bảng):** LectureProgress, Certificate, UserBadge
5. **Assessments (4 bảng):** Quiz, Question, QuizAttempt, Answer
6. **Assignments (2 bảng):** AssignmentSubmission, SubmissionReview
7. **Grading (2 bảng):** GradeBook, GradeScale
8. **Interaction (3 bảng):** Discussion, DiscussionReply, Notification
9. **System (1 bảng):** SystemConfig

**Lý do cần nhiều bảng:**
- **Normalization (3NF):** Tránh data redundancy
- **Separation of Concerns:** Mỗi bảng có 1 responsibility rõ ràng
- **Scalability:** Dễ mở rộng từng phần mà không ảnh hưởng toàn bộ
- **Performance:** Index riêng cho từng entity
- **Maintainability:** Dễ maintain và debug

---

### Q1.3: Database có follow Normalization không? Đến level nào?

**Trả lời:**

Database follow **Third Normal Form (3NF)**:

**1NF (First Normal Form):**
- ✅ Tất cả columns đều atomic (không có multi-valued attributes)
- ✅ Mỗi table có primary key
- ✅ No repeating groups

**2NF (Second Normal Form):**
- ✅ Satisfy 1NF
- ✅ No partial dependencies (tất cả non-key attributes phụ thuộc hoàn toàn vào PK)
- Ví dụ: Trong bảng Enrollment, final_grade phụ thuộc vào enrollment_id (PK), không phụ thuộc riêng user_id hay course_id

**3NF (Third Normal Form):**
- ✅ Satisfy 2NF
- ✅ No transitive dependencies
- Ví dụ: Thông tin course (title, description) không lưu trong Enrollment, mà lưu trong Course table và reference qua course_id

**Exceptions (có chủ đích):**
- `User.full_name` có thể tính từ `first_name + last_name` → Denormalization for performance
- Một số aggregated fields trong GradeBook → Denormalization to avoid expensive JOINs

---

## 2. USER MANAGEMENT

### Q2.1: Tại sao tách User và UserProfile thành 2 bảng riêng?

**Trả lời:**

**Lý do thiết kế:**

1. **Separation of Concerns:**
   - **User table:** Core authentication data (email, password, status)
     - Được access thường xuyên cho login/logout
     - Cần performance cao
     - Sensitive data
   - **UserProfile table:** Extended personal info (phone, address, bio, avatar)
     - Được access ít hơn
     - Có thể NULL (optional)
     - Less sensitive

2. **Performance:**
   - Login chỉ cần query User table (nhỏ, nhanh)
   - Không cần load avatar/bio khi authentication
   - Giảm size của frequently-accessed table

3. **Security:**
   - Có thể apply different access control cho 2 tables
   - Password hash isolated trong User table

4. **Scalability:**
   - Có thể move UserProfile sang separate storage (S3 for avatars)
   - Có thể partition UserProfile theo user activity

**Trade-off:**
- ❌ Cần JOIN khi cần full user info
- ✅ Nhưng most queries chỉ cần User table → overall faster

---

### Q2.2: Tại sao lại có bảng Role và UserRole riêng biệt? Tại sao không lưu role trực tiếp trong User?

**Trả lời:**

**Thiết kế hiện tại:**
```
User (1) ←→ (N) UserRole (N) ←→ (1) Role
```

**Lý do:**

1. **Many-to-Many Relationship:**
   - Một user có thể có nhiều roles
   - Ví dụ: John vừa là STUDENT (học course A), vừa là INSTRUCTOR (dạy course B)
   - Không thể lưu nhiều roles trong 1 field của User

2. **Role Management:**
   - Roles được quản lý tập trung trong bảng Role
   - Dễ thêm role mới (ADMIN, TA, MODERATOR, ...) mà không modify User table
   - Role có thể có permissions riêng

3. **Audit Trail:**
   - UserRole có `assigned_at` → biết khi nào user được assign role
   - Có thể track history (nếu thêm `removed_at`)

4. **Flexibility:**
   - User có thể có role khác nhau trong contexts khác nhau
   - Ví dụ: User A là STUDENT trong course X, nhưng là TA trong course Y
   - (Mặc dù trong design hiện tại, role là global, nhưng có thể extend thành course-specific)

**Alternative (bị loại bỏ):**
```sql
-- BAD: Store role as string in User table
ALTER TABLE "User" ADD COLUMN role VARCHAR(50);
```
❌ Không support multiple roles
❌ Khó validate (typo: "STUDNET")
❌ Không có metadata về role

---

### Q2.3: Bảng AuditLog có vai trò gì? Tại sao cần audit log?

**Trả lời:**

**Vai trò của AuditLog:**

Ghi lại **TẤT CẢ** actions quan trọng trong hệ thống để:

1. **Security & Compliance:**
   - Phát hiện unauthorized access
   - Track who changed what, when
   - Ví dụ: "User A changed grade của student B từ 8.5 → 9.0 lúc 2:30 AM"
   - Required for GDPR compliance (right to know what data was accessed)

2. **Debugging:**
   - Khi có bug, trace back để tìm nguyên nhân
   - Ví dụ: "Tại sao submission của student X bị missing?"
   - Check AuditLog: "Admin Y deleted submission at [time]"

3. **Analytics:**
   - User behavior analysis
   - Ví dụ: "Students thường submit assignment vào giờ nào?"
   - "Feature nào được dùng nhiều nhất?"

4. **Legal Protection:**
   - Chứng minh hành động hợp pháp
   - Ví dụ: Student complain về điểm
   - AuditLog shows: "Student submitted late → penalty applied → final grade calculated correctly"

**Cấu trúc:**
```sql
CREATE TABLE "AuditLog" (
    log_id UUID PRIMARY KEY,
    user_id UUID,  -- Who did the action
    action VARCHAR(100),  -- CREATE, UPDATE, DELETE, LOGIN, etc.
    entity_type VARCHAR(50),  -- User, Course, Submission, etc.
    entity_id UUID,  -- Which record
    old_value JSONB,  -- Before change
    new_value JSONB,  -- After change
    ip_address INET,  -- From where
    created_at TIMESTAMP  -- When
);
```

**Example:**
```json
{
  "log_id": "...",
  "user_id": "instructor-123",
  "action": "UPDATE",
  "entity_type": "AssignmentSubmission",
  "entity_id": "submission-456",
  "old_value": {"score": null, "status": "SUBMITTED"},
  "new_value": {"score": 85.5, "status": "GRADED"},
  "ip_address": "192.168.1.100",
  "created_at": "2025-12-01 14:30:00"
}
```

---

## 3. COURSE MANAGEMENT

### Q3.1: Tại sao cần bảng Module? Tại sao không lưu Lectures trực tiếp trong Course?

**Trả lời:**

**Lý do cần Module:**

1. **Organization Hierarchy:**
   ```
   Course (Khóa học Java)
    └── Module 1: Introduction
         ├── Lecture 1.1: What is Java
         ├── Lecture 1.2: Setup Environment
         └── Lecture 1.3: First Program
    └── Module 2: OOP Concepts
         ├── Lecture 2.1: Classes and Objects
         ├── Lecture 2.2: Inheritance
         └── Lecture 2.3: Polymorphism
   ```
   - Module giúp nhóm các lectures liên quan
   - Dễ navigate (table of contents)

2. **Sequential Learning:**
   - Students học theo thứ tự: Module 1 → Module 2 → Module 3
   - Có thể lock Module 2 until Module 1 completed
   - Module có `order_num` để sắp xếp

3. **Flexible Structure:**
   - Một course có thể có 5-10 modules
   - Mỗi module có 3-10 lectures
   - Nếu lưu trực tiếp Course → Lectures: 50-100 lectures trong 1 flat list (khó quản lý)

4. **Reusability:**
   - Có thể move/copy module giữa các courses
   - Ví dụ: "Introduction to Programming" module dùng chung cho Java course và Python course

5. **Progress Tracking:**
   - Track progress theo module
   - "Student đã hoàn thành 2/5 modules"

**Alternative bị loại:**
```sql
-- BAD: No Module, lectures directly in course
CREATE TABLE "Lecture" (
    course_id UUID,  -- Direct reference to course
    ...
);
```
❌ No hierarchy
❌ Khó organize 100+ lectures
❌ No module-level progress tracking

---

### Q3.2: Bảng LectureContent và bảng Attachment khác nhau như thế nào?

**Trả lời:**

**So sánh:**

| Aspect | LectureContent | Attachment |
|--------|----------------|------------|
| **Purpose** | Main content của lecture | Supporting files |
| **Content Type** | VIDEO, READING, ASSIGNMENT | PDF, DOC, PPT, ZIP, etc. |
| **Relationship** | 1 Lecture có 1 LectureContent | 1 Lecture có NHIỀU Attachments |
| **Storage** | content_url (video link, text) | file_url, file_size, file_type |
| **Required** | YES (mọi lecture phải có content) | NO (optional) |

**LectureContent Example:**
```json
{
  "lecture_id": "60000001",
  "content_type": "VIDEO",
  "content_url": "https://youtube.com/watch?v=xxx",
  "duration_minutes": 45,
  "video_config": {
    "resolution": "1080p",
    "has_subtitles": true,
    "allow_download": false
  }
}
```

**Attachment Example:**
```json
{
  "attachment_id": "70000001",
  "lecture_id": "60000001",
  "file_name": "Lecture_1_Slides.pdf",
  "file_url": "https://s3.../lecture1.pdf",
  "file_type": "PDF",
  "file_size": 2048576,  // 2MB
  "uploaded_at": "2025-11-01"
}
```

**Use Case:**
- **Lecture:** "Introduction to Java"
- **LectureContent:** Video bài giảng (45 phút)
- **Attachments:**
  1. Slides.pdf (2MB)
  2. Sample_Code.zip (500KB)
  3. Reading_Material.docx (1MB)

**Tại sao tách riêng:**
- Performance: Load content trước, attachments sau (lazy loading)
- Scalability: Attachments có thể lưu ở separate storage service
- Flexibility: Có thể có 0, 1, hoặc nhiều attachments

---

### Q3.3: Bảng Tag và CourseTag có tác dụng gì?

**Trả lời:**

**Purpose:**

**Tag:** Taxonomy/vocabulary (tập hợp các tags có thể dùng)
**CourseTag:** Many-to-Many relationship giữa Course và Tag

**Use Cases:**

1. **Course Discovery:**
   - Student search: "JavaScript", "Beginner", "Free"
   - System filters courses có tags matching

2. **Recommendation:**
   - Student học course tagged ["JavaScript", "Frontend"]
   - Recommend courses tagged ["React", "Vue", "Angular"] (similar tags)

3. **Analytics:**
   - "Tag nào phổ biến nhất?"
   - "Courses về AI đang trending"

**Example:**

```sql
-- Tags
INSERT INTO "Tag" VALUES
  ('tag-001', 'JavaScript'),
  ('tag-002', 'Beginner'),
  ('tag-003', 'Free'),
  ('tag-004', 'Frontend');

-- Course: "Modern JavaScript for Beginners"
INSERT INTO "CourseTag" VALUES
  ('ct-001', 'course-101', 'tag-001'),  -- JavaScript
  ('ct-002', 'course-101', 'tag-002'),  -- Beginner
  ('ct-003', 'course-101', 'tag-004');  -- Frontend
```

**Query:**
```sql
-- Find all courses tagged "JavaScript"
SELECT c.*
FROM "Course" c
JOIN "CourseTag" ct ON c.course_id = ct.course_id
JOIN "Tag" t ON ct.tag_id = t.tag_id
WHERE t.name = 'JavaScript';
```

**Many-to-Many:**
- 1 Course có nhiều Tags (JavaScript, Beginner, Free)
- 1 Tag gắn với nhiều Courses (Tag "JavaScript" có 100+ courses)

---

[TIẾP TỤC cho tất cả 31 bảng...]

## 4. ENROLLMENT & CLASS

### Q4.1: Bảng Class và Enrollment khác nhau thế nào?

[Giải thích chi tiết...]

## 5. LEARNING PROGRESS

### Q5.1: Tại sao cần track LectureProgress? Chỉ track course-level progress không được sao?

[Giải thích chi tiết...]

## 6. ASSESSMENTS

### Q6.1: Bảng Quiz, Question, QuizAttempt, Answer - quan hệ ra sao?

[Giải thích chi tiết với diagram...]

## 7. ASSIGNMENTS

### Q7.1: Tại sao lại có AssignmentSubmission và SubmissionReview riêng?

[Giải thích chi tiết...]

### Q7.2: Tại sao submission_number lại cho phép submit nhiều lần?

[Giải thích business logic...]

## 8. GRADING

### Q8.1: GradeBook và GradeScale khác nhau thế nào?

[Giải thích chi tiết...]

## 9. RELATIONSHIPS & CONSTRAINTS

### Q9.1: Cascade Delete hoạt động như thế nào? Cho ví dụ?

[Giải thích với examples cụ thể...]

### Q9.2: Tại sao dùng UUID thay vì INT auto-increment?

[Giải thích pros/cons...]

## 10. PERFORMANCE & OPTIMIZATION

### Q10.1: Indexes được đặt ở đâu? Tại sao?

[List tất cả indexes + lý do...]

### Q10.2: Làm thế nào để optimize queries với 31 tables?

[Strategies...]
```

---

### 4.3. Yêu cầu cho FAQ Document

**Số lượng câu hỏi:** Tối thiểu **50-70 câu hỏi** covering:
- Mỗi bảng: 1-3 câu
- Relationships: 10-15 câu
- Design decisions: 10-15 câu
- Performance: 5-10 câu
- Business logic: 10-15 câu

**Format câu hỏi:**
```markdown
### Q[Section].[Number]: [Câu hỏi ngắn gọn, cụ thể]

**Trả lời:**

[Trả lời chi tiết with:]
1. Direct answer (1-2 câu)
2. Reasoning/Explanation
3. Examples (SQL, data, diagrams nếu cần)
4. Trade-offs (pros/cons)
5. Alternatives considered (nếu có)

**Code/Data Examples:** (nếu applicable)
```sql
...
```

**Diagram:** (nếu cần)
[ASCII art hoặc chỉ dẫn vẽ diagram]
```

---

## 🎯 TASK 5: Checklist và Deliverables

### 5.1. Checklist hoàn thành

**Giai đoạn 1: Phân tích (1-2 ngày)**
- [ ] Đọc xong Scripts/Tham_khao.pdf
- [ ] Tạo ANALYSIS_TEMPLATE.md
- [ ] Đọc xong Scripts/DTPM_B-Learning.pdf
- [ ] Tạo COMPARISON_OLD_VS_NEW.md
- [ ] Đọc hết documents trong 98-B-Learning-Core/documents/
- [ ] Đọc hết SQL scripts trong 98-B-Learning-Core/sql/

**Giai đoạn 2: Tạo nội dung (3-5 ngày)**
- [ ] Viết Lời mở đầu
- [ ] Viết Chương 1: Tổng quan
- [ ] Viết Chương 2: Phân tích yêu cầu
- [ ] Viết Chương 3: Thiết kế CSDL (31 tables)
- [ ] Viết Chương 4: Triển khai và Demo
- [ ] Viết Chương 5: Đánh giá
- [ ] Viết Kết luận
- [ ] Viết Tài liệu tham khảo
- [ ] Viết Phụ lục

**Giai đoạn 3: Hình ảnh và sơ đồ (2-3 ngày)**
- [ ] Tạo ERD tổng thể (31 tables)
- [ ] Tạo ERD từng nhóm (9 hình)
- [ ] Tạo Use Case Diagram
- [ ] Tạo Business Flow Diagrams
- [ ] Tạo System Architecture diagram
- [ ] Chụp screenshots demo app
- [ ] Tạo comparison charts

**Giai đoạn 4: FAQ Document (2-3 ngày)**
- [ ] Viết 50-70 câu hỏi
- [ ] Trả lời chi tiết từng câu
- [ ] Thêm examples và diagrams
- [ ] Review và refine

**Giai đoạn 5: Review và hoàn thiện (1-2 ngày)**
- [ ] Kiểm tra chính tả, ngữ pháp
- [ ] Kiểm tra consistency (terminology, numbering)
- [ ] Kiểm tra tất cả hình ảnh có caption
- [ ] Kiểm tra tất cả references đúng
- [ ] Generate Table of Contents
- [ ] Generate Danh mục hình ảnh
- [ ] Generate Danh mục bảng
- [ ] Final review

---

### 5.2. Deliverables (Sản phẩm cuối cùng)

**98-B-Learning-Core/reports/**

1. **MAIN_REPORT.md** (40-60 trang)
   - Báo cáo chính theo cấu trúc đã định
   - Đầy đủ 5 chương + mở đầu + kết luận + phụ lục
   - Chỉ dẫn vị trí tất cả hình ảnh

2. **FAQ_EXPLANATION.md** (30-40 trang)
   - 50-70 câu hỏi và trả lời chi tiết
   - Examples và diagrams
   - Cover all 31 tables + relationships + design decisions

3. **ANALYSIS_TEMPLATE.md** (5-10 trang)
   - Phân tích báo cáo tham khảo
   - Template và style guide

4. **COMPARISON_OLD_VS_NEW.md** (5-8 trang)
   - So sánh DB cũ vs DB mới
   - Bảng chi tiết
   - Highlight improvements

5. **images/** (folder)
   - Tất cả ERD diagrams (PNG)
   - Use case diagrams (PNG)
   - Business flow diagrams (PNG)
   - Screenshots (PNG)
   - Charts (PNG)

6. **CHECKLIST.md**
   - Checklist tracking progress
   - Review checklist

---

## 📝 LƯU Ý QUAN TRỌNG

### Về nội dung:
1. **Accuracy:** Tất cả thông tin phải chính xác 100% (dựa trên SQL scripts và documents)
2. **Completeness:** Cover HẾT 31 tables, không bỏ sót
3. **Clarity:** Giải thích rõ ràng, dễ hiểu cho người không chuyên
4. **Examples:** Mỗi concept cần có ví dụ cụ thể

### Về hình thức:
1. **Professional:** Trông như báo cáo chính thức, không casual
2. **Consistent:** Terminology, numbering, formatting nhất quán
3. **Well-structured:** Logic flow, easy to navigate
4. **Visual:** Nhiều diagrams, tables, examples (không chỉ text wall)

### Về giọng văn:
1. **Formal but accessible:** Học thuật nhưng không quá khô khan
2. **Vietnamese:** Toàn bộ tiếng Việt, thuật ngữ tiếng Anh để trong ngoặc
3. **Objective:** Không subjective opinions, có evidence
4. **Student-friendly:** Phong cách sinh viên năm cuối, không quá phức tạp

---

## 🔄 WORKFLOW ĐỀ XUẤT

### Week 1: Research & Analysis
- Days 1-2: Đọc tài liệu tham khảo, phân tích cấu trúc
- Days 3-4: Đọc tài liệu DB cũ, tạo comparison
- Days 5-7: Đọc hết documents và SQL scripts của DB mới

### Week 2: Main Report Content
- Days 1-2: Viết Chương 1, 2
- Days 3-5: Viết Chương 3 (31 tables - phần dài nhất)
- Days 6-7: Viết Chương 4, 5

### Week 3: Diagrams & FAQ
- Days 1-3: Tạo tất cả diagrams và charts
- Days 4-6: Viết FAQ document
- Day 7: Integration và review

### Week 4: Polish & Finalize
- Days 1-2: Review toàn bộ, fix errors
- Days 3-4: Generate TOC, lists, references
- Days 5-7: Final polish, formatting, delivery

**Total estimated time: 3-4 weeks**

---

## ✅ ACCEPTANCE CRITERIA

Báo cáo được coi là hoàn thành khi:

1. **Completeness:**
   - ✅ Tất cả 31 tables được mô tả chi tiết
   - ✅ Tất cả relationships được giải thích
   - ✅ Tất cả indexes và constraints được documented

2. **Quality:**
   - ✅ Không có lỗi chính tả, ngữ pháp
   - ✅ Consistent terminology throughout
   - ✅ Professional formatting

3. **Visual:**
   - ✅ Có đầy đủ diagrams (ERD, use case, flow, architecture)
   - ✅ Tất cả diagrams có caption và số thứ tự
   - ✅ Screenshots chất lượng cao

4. **FAQ:**
   - ✅ Tối thiểu 50 câu hỏi
   - ✅ Mỗi câu trả lời đầy đủ, có ví dụ
   - ✅ Cover all aspects của DB design

5. **Usability:**
   - ✅ Dễ navigate (TOC, headings clear)
   - ✅ Dễ hiểu (examples, không quá technical)
   - ✅ Có thể dùng cho presentation/defense

---

## 📞 SUPPORT

Nếu có thắc mắc trong quá trình thực hiện:
- Check lại documents trong 98-B-Learning-Core/documents/
- Check SQL scripts trong 98-B-Learning-Core/sql/
- Tham khảo demo app trong B-Learning-Demo/
- Hỏi user để clarify requirements

---

**END OF PLAN TASK REQ-4**

---

Generated: 2025-12-01
Author: Nguyễn Văn Kiệt - CNTT1-K63
Project: B-Learning Database Design
