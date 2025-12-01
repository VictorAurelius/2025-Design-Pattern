# COMPARISON_OLD_VS_NEW.md

**So sánh thiết kế cơ sở dữ liệu B-Learning cũ và mới**

**Nguồn:**
- **Thiết kế cũ:** Scripts/DTPM_B-Learning.pdf (16 trang, 21 bảng)
- **Thiết kế mới:** 98-B-Learing-Core/documents/ (31 bảng)

**Tác giả:** Nguyễn Văn Kiệt - CNTT1-K63

**Ngày so sánh:** 2025-12-01

---

## MỤC LỤC

1. [Tổng quan so sánh](#1-tổng-quan-so-sánh)
2. [So sánh số lượng bảng](#2-so-sánh-số-lượng-bảng)
3. [Bảng được giữ nguyên (21 bảng)](#3-bảng-được-giữ-nguyên-21-bảng)
4. [Bảng mới được thêm vào (10 bảng)](#4-bảng-mới-được-thêm-vào-10-bảng)
5. [So sánh chi tiết từng nhóm](#5-so-sánh-chi-tiết-từng-nhóm)
6. [Cải tiến về mặt thiết kế](#6-cải-tiến-về-mặt-thiết-kế)
7. [Đánh giá tổng thể](#7-đánh-giá-tổng-thể)

---

## 1. TỔNG QUAN SO SÁNH

### 1.1. Thống kê cơ bản

| Tiêu chí | Thiết kế cũ | Thiết kế mới | Chênh lệch |
|----------|-------------|--------------|------------|
| **Số bảng** | 21 | 31 | +10 (+47.6%) |
| **Số nhóm chức năng** | 6 | 9 | +3 |
| **Số trường (ước tính)** | ~180 | ~280 | +100 (+55.6%) |
| **Độ dài báo cáo** | 16 trang | 40-60 trang | +24-44 trang |
| **Khóa chính** | INT (auto-increment) | UUID | Thay đổi |
| **Có ERD chi tiết** | Có (đơn giản) | Có (đầy đủ) | Cải thiện |
| **Có FAQ** | Không | Có (50-70 Q&A) | Mới |

### 1.2. Phân bổ bảng theo nhóm chức năng

**Thiết kế cũ (6 nhóm):**
1. User Management: 3 bảng (User, Role, UserRole)
2. Course Management: 4 bảng (Course, Module, Lecture, Resource)
3. Class & Enrollment: 3 bảng (Class, CourseEnrollment, ClassEnrollment)
4. Learning Progress: 2 bảng (Schedule, Attendance, Progress)
5. Assessments: 6 bảng (Question, Option, Quiz, QuizQuestion, Attempt, Submission)
6. Interaction: 2 bảng (Thread, Post)

**Thiết kế mới (9 nhóm):**
1. User Management: **6 bảng** (+3)
2. Course Management: **7 bảng** (+3)
3. Class & Enrollment: **2 bảng** (-1)
4. Learning Progress: **3 bảng** (không đổi)
5. Assessments: **4 bảng** (-2)
6. Assignments: **2 bảng** (tách riêng)
7. Grading: **2 bảng** (mới)
8. Interaction: **3 bảng** (+1)
9. System: **1 bảng** (mới)

---

## 2. SO SÁNH SỐ LƯỢNG BẢNG

### 2.1. Bảng tồn tại trong cả hai thiết kế (21 bảng)

| STT | Tên bảng cũ | Tên bảng mới | Thay đổi |
|-----|-------------|--------------|----------|
| 1 | User | Users | Đổi tên (số nhiều) |
| 2 | Role | Roles | Đổi tên (số nhiều) |
| 3 | UserRole | UserRoles | Đổi tên (số nhiều) |
| 4 | Course | Courses | Đổi tên (số nhiều) |
| 5 | CourseEnrollment | Enrollments | Đổi tên, gộp với ClassEnrollment |
| 6 | Module | Modules | Đổi tên (số nhiều) |
| 7 | Lecture | Lectures | Đổi tên (số nhiều) |
| 8 | Resource | Resources | Đổi tên (số nhiều) |
| 9 | Class | Classes | Đổi tên (số nhiều) |
| 10 | ClassEnrollment | *(gộp vào Enrollments)* | Bị gộp |
| 11 | Schedule | Schedules | Đổi tên (số nhiều) |
| 12 | Attendance | Attendance | Giữ nguyên |
| 13 | Progress | Progress | Giữ nguyên |
| 14 | Question | Questions | Đổi tên (số nhiều) |
| 15 | Option | QuizOptions | Đổi tên rõ ràng hơn |
| 16 | Quiz | Quizzes | Đổi tên (số nhiều) |
| 17 | QuizQuestion | QuizQuestions | Đổi tên (số nhiều) |
| 18 | Attempt | QuizAttempts | Đổi tên rõ ràng hơn |
| 19 | Submission | AssignmentSubmissions | Đổi tên rõ ràng hơn |
| 20 | Thread | DiscussionThreads | Đổi tên rõ ràng hơn |
| 21 | Post | DiscussionPosts | Đổi tên rõ ràng hơn |

**Nhận xét:**
- Tất cả 21 bảng cũ đều được giữ lại trong thiết kế mới
- Chủ yếu đổi tên sang dạng số nhiều (plural) theo convention
- Một số bảng được đổi tên rõ ràng hơn (QuizOptions, AssignmentSubmissions, DiscussionThreads)
- CourseEnrollment và ClassEnrollment được gộp thành Enrollments

### 2.2. Bảng mới được thêm vào (10 bảng)

| STT | Tên bảng | Nhóm | Mục đích |
|-----|----------|------|----------|
| 1 | **Permissions** | User Management | Quản lý quyền hạn chi tiết (RBAC) |
| 2 | **RolePermissions** | User Management | Gán permissions cho roles |
| 3 | **UserSettings** | User Management | Lưu cài đặt cá nhân (theme, language, timezone) |
| 4 | **Categories** | Course Management | Phân loại courses theo danh mục |
| 5 | **Tags** | Course Management | Gắn tags cho courses (tìm kiếm, filter) |
| 6 | **CourseTags** | Course Management | Bảng trung gian N:N (Courses ↔ Tags) |
| 7 | **Grades** | Grading (mới) | Lưu điểm số cho submissions |
| 8 | **GradeScale** | Grading (mới) | Thang điểm (A, B, C, D, F) |
| 9 | **Notifications** | Interaction | Thông báo cho users |
| 10 | **AuditLog** | System (mới) | Ghi log audit (WHO did WHAT WHEN) |

**Nhận xét:**
- 10 bảng mới bổ sung các chức năng thiếu trong thiết kế cũ
- RBAC (Role-Based Access Control) được triển khai đầy đủ với Permissions
- Course discovery được cải thiện với Categories và Tags
- Grading system được tách riêng thành module độc lập
- Audit trail được thêm vào để theo dõi hành động users

---

## 3. BẢNG ĐƯỢC GIỮ NGUYÊN (21 BẢNG)

### 3.1. User Management (3 bảng → 3 bảng core)

#### 3.1.1. User → Users

**Thay đổi cấu trúc:**

| Trường | Thiết kế cũ | Thiết kế mới | Ghi chú |
|--------|-------------|--------------|---------|
| **PK** | user_id (INT) | user_id (UUID) | UUID an toàn hơn |
| email | VARCHAR(100) | VARCHAR(255) | Tăng độ dài |
| password | VARCHAR(100) | password_hash VARCHAR(255) | Đổi tên rõ ràng hơn |
| full_name | VARCHAR(200) | VARCHAR(255) | Tăng độ dài |
| avatar_url | *(không có)* | TEXT | **Mới** |
| bio | *(không có)* | TEXT | **Mới** |
| date_of_birth | *(không có)* | DATE | **Mới** |
| phone | *(không có)* | VARCHAR(20) | **Mới** |
| address | *(không có)* | TEXT | **Mới** |
| status | *(không có)* | VARCHAR(20) DEFAULT 'ACTIVE' | **Mới** |
| created_at | TIMESTAMP | TIMESTAMP | Giữ nguyên |
| updated_at | *(không có)* | TIMESTAMP | **Mới** |

**Cải tiến:**
- ✅ Thêm 7 trường mới (avatar_url, bio, date_of_birth, phone, address, status, updated_at)
- ✅ Chuyển từ INT sang UUID để tránh lộ thông tin
- ✅ Thêm status để quản lý trạng thái tài khoản (ACTIVE, INACTIVE, SUSPENDED)
- ✅ Thêm updated_at để audit trail

#### 3.1.2. Role → Roles

**Thay đổi cấu trúc:**

| Trường | Thiết kế cũ | Thiết kế mới | Ghi chú |
|--------|-------------|--------------|---------|
| **PK** | role_id (INT) | role_id (UUID) | UUID |
| role_name | VARCHAR(50) | name VARCHAR(100) | Đổi tên trường |
| description | TEXT | description TEXT | Giữ nguyên |
| created_at | *(không có)* | TIMESTAMP | **Mới** |
| updated_at | *(không có)* | TIMESTAMP | **Mới** |

**Cải tiến:**
- ✅ Thêm timestamps (created_at, updated_at)
- ✅ Đổi role_name → name (ngắn gọn hơn)

#### 3.1.3. UserRole → UserRoles

**Thay đổi cấu trúc:**

| Trường | Thiết kế cũ | Thiết kế mới | Ghi chú |
|--------|-------------|--------------|---------|
| **PK** | user_role_id (INT) | user_role_id (UUID) | UUID |
| **FK** | user_id (INT) | user_id (UUID) | UUID |
| **FK** | role_id (INT) | role_id (UUID) | UUID |
| assigned_at | TIMESTAMP | assigned_at TIMESTAMP | Giữ nguyên |

**Cải tiến:**
- ✅ Chuyển tất cả khóa từ INT sang UUID

---

### 3.2. Course Management (4 bảng → 4 bảng core)

#### 3.2.1. Course → Courses

**Thay đổi cấu trúc:**

| Trường | Thiết kế cũ | Thiết kế mới | Ghi chú |
|--------|-------------|--------------|---------|
| **PK** | course_id (INT) | course_id (UUID) | UUID |
| **FK** | *(không có)* | category_id (UUID) | **Mới** - FK to Categories |
| **FK** | instructor_id (INT) | instructor_id (UUID) | UUID |
| code | VARCHAR(20) | code VARCHAR(20) | Giữ nguyên |
| title | VARCHAR(255) | title VARCHAR(255) | Giữ nguyên |
| description | TEXT | description TEXT | Giữ nguyên |
| thumbnail_url | *(không có)* | thumbnail_url TEXT | **Mới** |
| difficulty_level | *(không có)* | difficulty_level VARCHAR(20) | **Mới** |
| language | *(không có)* | language VARCHAR(10) DEFAULT 'vi' | **Mới** |
| estimated_hours | *(không có)* | estimated_hours INTEGER | **Mới** |
| enrollment_count | *(không có)* | enrollment_count INTEGER DEFAULT 0 | **Mới** |
| average_rating | *(không có)* | average_rating DECIMAL(3,2) | **Mới** |
| status | VARCHAR(20) | status VARCHAR(20) DEFAULT 'DRAFT' | Giữ nguyên |
| created_at | TIMESTAMP | created_at TIMESTAMP | Giữ nguyên |
| updated_at | TIMESTAMP | updated_at TIMESTAMP | Giữ nguyên |
| published_at | *(không có)* | published_at TIMESTAMP | **Mới** |

**Cải tiến:**
- ✅ Thêm category_id để phân loại courses
- ✅ Thêm metadata (thumbnail, difficulty, language, estimated_hours)
- ✅ Thêm metrics (enrollment_count, average_rating)
- ✅ Thêm published_at để track thời gian publish

#### 3.2.2. Module → Modules

**Thay đổi cấu trúc:**

| Trường | Thiết kế cũ | Thiết kế mới | Ghi chú |
|--------|-------------|--------------|---------|
| **PK** | module_id (INT) | module_id (UUID) | UUID |
| **FK** | course_id (INT) | course_id (UUID) | UUID |
| title | VARCHAR(255) | title VARCHAR(255) | Giữ nguyên |
| description | TEXT | description TEXT | Giữ nguyên |
| order_num | INTEGER | order_num INTEGER | Giữ nguyên |
| duration_minutes | *(không có)* | duration_minutes INTEGER | **Mới** |
| is_published | *(không có)* | is_published BOOLEAN DEFAULT FALSE | **Mới** |
| created_at | TIMESTAMP | created_at TIMESTAMP | Giữ nguyên |
| updated_at | *(không có)* | updated_at TIMESTAMP | **Mới** |

**Cải tiến:**
- ✅ Thêm duration_minutes để ước tính thời gian học
- ✅ Thêm is_published để quản lý trạng thái xuất bản

#### 3.2.3. Lecture → Lectures

**Thay đổi cấu trúc:**

| Trường | Thiết kế cũ | Thiết kế mới | Ghi chú |
|--------|-------------|--------------|---------|
| **PK** | lecture_id (INT) | lecture_id (UUID) | UUID |
| **FK** | module_id (INT) | module_id (UUID) | UUID |
| title | VARCHAR(255) | title VARCHAR(255) | Giữ nguyên |
| description | TEXT | description TEXT | Giữ nguyên |
| type | VARCHAR(20) | type VARCHAR(20) | Giữ nguyên (VIDEO, READING, ASSIGNMENT) |
| order_num | INTEGER | order_num INTEGER | Giữ nguyên |
| content | TEXT | *(bỏ)* | **Bỏ** - chuyển sang Resources |
| video_url | VARCHAR(255) | *(bỏ)* | **Bỏ** - chuyển sang Resources |
| duration_minutes | INTEGER | *(bỏ)* | **Bỏ** - chuyển sang Resources |
| assignment_config | *(không có)* | assignment_config JSONB | **Mới** |
| created_at | TIMESTAMP | created_at TIMESTAMP | Giữ nguyên |
| updated_at | *(không có)* | updated_at TIMESTAMP | **Mới** |

**Cải tiến:**
- ✅ Thêm assignment_config (JSONB) để lưu config linh hoạt
- ✅ Tách content, video_url, duration_minutes ra bảng Resources
- ✅ Thiết kế chuẩn hơn, tách concerns

#### 3.2.4. Resource → Resources

**Thay đổi cấu trúc:**

| Trường | Thiết kế cũ | Thiết kế mới | Ghi chú |
|--------|-------------|--------------|---------|
| **PK** | resource_id (INT) | resource_id (UUID) | UUID |
| **FK** | lecture_id (INT) | lecture_id (UUID) | UUID |
| type | VARCHAR(20) | type VARCHAR(20) | Giữ nguyên (VIDEO, PDF, LINK, ...) |
| title | VARCHAR(255) | title VARCHAR(255) | Giữ nguyên |
| url | VARCHAR(255) | url TEXT | Tăng độ dài |
| file_size | *(không có)* | file_size BIGINT | **Mới** |
| mime_type | *(không có)* | mime_type VARCHAR(100) | **Mới** |
| duration_seconds | *(không có)* | duration_seconds INTEGER | **Mới** |
| is_downloadable | *(không có)* | is_downloadable BOOLEAN DEFAULT TRUE | **Mới** |
| created_at | TIMESTAMP | created_at TIMESTAMP | Giữ nguyên |
| updated_at | *(không có)* | updated_at TIMESTAMP | **Mới** |

**Cải tiến:**
- ✅ Thêm metadata (file_size, mime_type, duration_seconds)
- ✅ Thêm is_downloadable để kiểm soát download

---

### 3.3. Class & Enrollment (3 bảng → 2 bảng)

#### 3.3.1. Class → Classes

**Thay đổi cấu trúc:**

| Trường | Thiết kế cũ | Thiết kế mới | Ghi chú |
|--------|-------------|--------------|---------|
| **PK** | class_id (INT) | class_id (UUID) | UUID |
| **FK** | course_id (INT) | course_id (UUID) | UUID |
| **FK** | instructor_id (INT) | instructor_id (UUID) | UUID |
| class_code | VARCHAR(20) | code VARCHAR(20) | Đổi tên |
| semester | VARCHAR(20) | semester VARCHAR(20) | Giữ nguyên |
| year | INTEGER | year INTEGER | Giữ nguyên |
| start_date | DATE | start_date DATE | Giữ nguyên |
| end_date | DATE | end_date DATE | Giữ nguyên |
| max_students | INTEGER | max_students INTEGER | Giữ nguyên |
| status | VARCHAR(20) | status VARCHAR(20) | Giữ nguyên |
| created_at | TIMESTAMP | created_at TIMESTAMP | Giữ nguyên |
| updated_at | *(không có)* | updated_at TIMESTAMP | **Mới** |

**Cải tiến:**
- ✅ Đổi class_code → code (ngắn gọn hơn)
- ✅ Thêm updated_at

#### 3.3.2. CourseEnrollment + ClassEnrollment → Enrollments

**Thiết kế cũ:**
- CourseEnrollment: enrollment vào Course (self-paced)
- ClassEnrollment: enrollment vào Class (instructor-led)

**Thiết kế mới:**
- **Enrollments**: Gộp cả hai, sử dụng trường `class_id` (nullable)
  - Nếu class_id = NULL → Course enrollment (self-paced)
  - Nếu class_id != NULL → Class enrollment (instructor-led)

**Thay đổi cấu trúc:**

| Trường | Thiết kế cũ | Thiết kế mới | Ghi chú |
|--------|-------------|--------------|---------|
| **PK** | enrollment_id (INT) | enrollment_id (UUID) | UUID |
| **FK** | user_id (INT) | user_id (UUID) | UUID |
| **FK** | course_id (INT) | course_id (UUID) | UUID |
| **FK** | *(class_id trong ClassEnrollment)* | class_id (UUID) NULL | **Gộp** |
| role | VARCHAR(20) | role VARCHAR(20) | Giữ nguyên (STUDENT, TA, INSTRUCTOR) |
| status | VARCHAR(20) | status VARCHAR(20) | Giữ nguyên |
| enrolled_at | TIMESTAMP | enrolled_at TIMESTAMP | Giữ nguyên |
| completed_at | *(không có)* | completed_at TIMESTAMP | **Mới** |
| final_grade | *(không có)* | final_grade DECIMAL(5,2) | **Mới** |

**Cải tiến:**
- ✅ Gộp CourseEnrollment và ClassEnrollment thành một bảng
- ✅ Thêm completed_at để track thời gian hoàn thành
- ✅ Thêm final_grade để lưu điểm cuối khóa

---

### 3.4. Learning Progress (3 bảng → 3 bảng)

#### 3.4.1. Schedule → Schedules

**Thay đổi cấu trúc:**

| Trường | Thiết kế cũ | Thiết kế mới | Ghi chú |
|--------|-------------|--------------|---------|
| **PK** | schedule_id (INT) | schedule_id (UUID) | UUID |
| **FK** | class_id (INT) | class_id (UUID) | UUID |
| day_of_week | INTEGER | day_of_week INTEGER | Giữ nguyên (0=CN, 1=T2, ...) |
| start_time | TIME | start_time TIME | Giữ nguyên |
| end_time | TIME | end_time TIME | Giữ nguyên |
| room | VARCHAR(50) | room VARCHAR(100) | Tăng độ dài |
| created_at | *(không có)* | created_at TIMESTAMP | **Mới** |
| updated_at | *(không có)* | updated_at TIMESTAMP | **Mới** |

**Cải tiến:**
- ✅ Thêm timestamps (created_at, updated_at)
- ✅ Tăng độ dài trường room

#### 3.4.2. Attendance → Attendance

**Thay đổi cấu trúc:**

| Trường | Thiết kế cũ | Thiết kế mới | Ghi chú |
|--------|-------------|--------------|---------|
| **PK** | attendance_id (INT) | attendance_id (UUID) | UUID |
| **FK** | enrollment_id (INT) | enrollment_id (UUID) | UUID |
| **FK** | schedule_id (INT) | schedule_id (UUID) | UUID |
| date | DATE | date DATE | Giữ nguyên |
| status | VARCHAR(20) | status VARCHAR(20) | Giữ nguyên (PRESENT, ABSENT, LATE, EXCUSED) |
| note | TEXT | note TEXT | Giữ nguyên |
| created_at | TIMESTAMP | created_at TIMESTAMP | Giữ nguyên |
| updated_at | *(không có)* | updated_at TIMESTAMP | **Mới** |

**Cải tiến:**
- ✅ Thêm updated_at

#### 3.4.3. Progress → Progress

**Thay đổi cấu trúc:**

| Trường | Thiết kế cũ | Thiết kế mới | Ghi chú |
|--------|-------------|--------------|---------|
| **PK** | progress_id (INT) | progress_id (UUID) | UUID |
| **FK** | enrollment_id (INT) | enrollment_id (UUID) | UUID |
| **FK** | lecture_id (INT) | lecture_id (UUID) | UUID |
| status | VARCHAR(20) | status VARCHAR(20) | Giữ nguyên (NOT_STARTED, IN_PROGRESS, COMPLETED) |
| completion_percentage | *(không có)* | completion_percentage INTEGER DEFAULT 0 | **Mới** |
| time_spent_seconds | *(không có)* | time_spent_seconds INTEGER DEFAULT 0 | **Mới** |
| started_at | *(không có)* | started_at TIMESTAMP | **Mới** |
| completed_at | TIMESTAMP | completed_at TIMESTAMP | Giữ nguyên |
| last_accessed_at | *(không có)* | last_accessed_at TIMESTAMP | **Mới** |

**Cải tiến:**
- ✅ Thêm completion_percentage để theo dõi % hoàn thành
- ✅ Thêm time_spent_seconds để track thời gian học
- ✅ Thêm started_at và last_accessed_at để analytics

---

### 3.5. Assessments (6 bảng → 4 bảng)

#### 3.5.1. Question → Questions

**Thay đổi cấu trúc:**

| Trường | Thiết kế cũ | Thiết kế mới | Ghi chú |
|--------|-------------|--------------|---------|
| **PK** | question_id (INT) | question_id (UUID) | UUID |
| **FK** | *(không có)* | created_by (UUID) | **Mới** - FK to Users |
| type | VARCHAR(20) | type VARCHAR(20) | Giữ nguyên (MULTIPLE_CHOICE, TRUE_FALSE, SHORT_ANSWER, ESSAY) |
| difficulty | *(không có)* | difficulty VARCHAR(20) | **Mới** |
| category | *(không có)* | category VARCHAR(100) | **Mới** |
| question_text | TEXT | question_text TEXT | Giữ nguyên |
| explanation | *(không có)* | explanation TEXT | **Mới** |
| points | DECIMAL(5,2) | points DECIMAL(5,2) | Giữ nguyên |
| is_published | *(không có)* | is_published BOOLEAN DEFAULT FALSE | **Mới** |
| created_at | TIMESTAMP | created_at TIMESTAMP | Giữ nguyên |
| updated_at | *(không có)* | updated_at TIMESTAMP | **Mới** |

**Cải tiến:**
- ✅ Thêm created_by để track tác giả câu hỏi
- ✅ Thêm difficulty và category để phân loại
- ✅ Thêm explanation để hiển thị giải thích đáp án
- ✅ Thêm is_published để kiểm soát xuất bản

#### 3.5.2. Option → QuizOptions

**Thay đổi cấu trúc:**

| Trường | Thiết kế cũ | Thiết kế mới | Ghi chú |
|--------|-------------|--------------|---------|
| **PK** | option_id (INT) | quiz_option_id (UUID) | UUID, đổi tên |
| **FK** | question_id (INT) | question_id (UUID) | UUID |
| option_text | TEXT | option_text TEXT | Giữ nguyên |
| is_correct | BOOLEAN | is_correct BOOLEAN | Giữ nguyên |
| order_num | *(không có)* | order_num INTEGER | **Mới** |
| created_at | *(không có)* | created_at TIMESTAMP | **Mới** |
| updated_at | *(không có)* | updated_at TIMESTAMP | **Mới** |

**Cải tiến:**
- ✅ Đổi tên option_id → quiz_option_id (rõ ràng hơn)
- ✅ Thêm order_num để sắp xếp thứ tự options
- ✅ Thêm timestamps

#### 3.5.3. Quiz → Quizzes

**Thay đổi cấu trúc:**

| Trường | Thiết kế cũ | Thiết kế mới | Ghi chú |
|--------|-------------|--------------|---------|
| **PK** | quiz_id (INT) | quiz_id (UUID) | UUID |
| **FK** | lecture_id (INT) | lecture_id (UUID) | UUID |
| title | VARCHAR(255) | title VARCHAR(255) | Giữ nguyên |
| description | TEXT | description TEXT | Giữ nguyên |
| time_limit_minutes | INTEGER | time_limit_minutes INTEGER | Giữ nguyên |
| passing_score | DECIMAL(5,2) | passing_score DECIMAL(5,2) | Giữ nguyên |
| max_attempts | INTEGER | max_attempts INTEGER | Giữ nguyên |
| is_randomized | *(không có)* | is_randomized BOOLEAN DEFAULT FALSE | **Mới** |
| show_answers | *(không có)* | show_answers BOOLEAN DEFAULT TRUE | **Mới** |
| available_from | *(không có)* | available_from TIMESTAMP | **Mới** |
| available_until | *(không có)* | available_until TIMESTAMP | **Mới** |
| created_at | TIMESTAMP | created_at TIMESTAMP | Giữ nguyên |
| updated_at | *(không có)* | updated_at TIMESTAMP | **Mới** |

**Cải tiến:**
- ✅ Thêm is_randomized để random câu hỏi
- ✅ Thêm show_answers để kiểm soát hiển thị đáp án
- ✅ Thêm available_from/until để kiểm soát thời gian

#### 3.5.4. QuizQuestion → QuizQuestions

**Thay đổi cấu trúc:**

| Trường | Thiết kế cũ | Thiết kế mới | Ghi chú |
|--------|-------------|--------------|---------|
| **PK** | quiz_question_id (INT) | quiz_question_id (UUID) | UUID |
| **FK** | quiz_id (INT) | quiz_id (UUID) | UUID |
| **FK** | question_id (INT) | question_id (UUID) | UUID |
| order_num | INTEGER | order_num INTEGER | Giữ nguyên |

**Cải tiến:**
- ✅ Chuyển INT sang UUID

#### 3.5.5. Attempt → QuizAttempts

**Thay đổi cấu trúc:**

| Trường | Thiết kế cũ | Thiết kế mới | Ghi chú |
|--------|-------------|--------------|---------|
| **PK** | attempt_id (INT) | quiz_attempt_id (UUID) | UUID, đổi tên |
| **FK** | user_id (INT) | user_id (UUID) | UUID |
| **FK** | quiz_id (INT) | quiz_id (UUID) | UUID |
| attempt_number | INTEGER | attempt_number INTEGER | Giữ nguyên |
| score | DECIMAL(5,2) | score DECIMAL(5,2) | Giữ nguyên |
| max_score | *(không có)* | max_score DECIMAL(5,2) | **Mới** |
| status | VARCHAR(20) | status VARCHAR(20) | Giữ nguyên (IN_PROGRESS, SUBMITTED, GRADED) |
| answers | *(không có)* | answers JSONB | **Mới** |
| started_at | TIMESTAMP | started_at TIMESTAMP | Giữ nguyên |
| submitted_at | TIMESTAMP | submitted_at TIMESTAMP | Giữ nguyên |
| graded_at | *(không có)* | graded_at TIMESTAMP | **Mới** |

**Cải tiến:**
- ✅ Đổi tên attempt_id → quiz_attempt_id
- ✅ Thêm max_score để tính % điểm
- ✅ Thêm answers (JSONB) để lưu câu trả lời
- ✅ Thêm graded_at để track thời gian chấm

#### 3.5.6. Submission → AssignmentSubmissions

**Thay đổi cấu trúc:**

| Trường | Thiết kế cũ | Thiết kế mới | Ghi chú |
|--------|-------------|--------------|---------|
| **PK** | submission_id (INT) | assignment_submission_id (UUID) | UUID, đổi tên |
| **FK** | user_id (INT) | user_id (UUID) | UUID |
| **FK** | lecture_id (INT) | lecture_id (UUID) | UUID (lecture type=ASSIGNMENT) |
| submission_number | *(không có)* | submission_number INTEGER DEFAULT 1 | **Mới** |
| content | TEXT | content TEXT | Giữ nguyên |
| file_urls | *(không có)* | file_urls TEXT[] | **Mới** |
| status | VARCHAR(20) | status VARCHAR(20) | Giữ nguyên (DRAFT, SUBMITTED, GRADED) |
| score | *(không có)* | *(bỏ - chuyển sang Grades)* | **Bỏ** |
| feedback | *(không có)* | *(bỏ - chuyển sang Grades)* | **Bỏ** |
| submitted_at | TIMESTAMP | submitted_at TIMESTAMP | Giữ nguyên |
| graded_at | *(không có)* | *(bỏ - chuyển sang Grades)* | **Bỏ** |
| created_at | *(không có)* | created_at TIMESTAMP | **Mới** |
| updated_at | *(không có)* | updated_at TIMESTAMP | **Mới** |

**Cải tiến:**
- ✅ Đổi tên submission_id → assignment_submission_id (rõ ràng hơn)
- ✅ Thêm submission_number để hỗ trợ multiple submissions
- ✅ Thêm file_urls (TEXT[]) để đính kèm nhiều files
- ✅ Tách score, feedback, graded_at sang bảng Grades riêng

---

### 3.6. Interaction (2 bảng → 3 bảng)

#### 3.6.1. Thread → DiscussionThreads

**Thay đổi cấu trúc:**

| Trường | Thiết kế cũ | Thiết kế mới | Ghi chú |
|--------|-------------|--------------|---------|
| **PK** | thread_id (INT) | thread_id (UUID) | UUID |
| **FK** | course_id (INT) | course_id (UUID) | UUID |
| **FK** | created_by (INT) | created_by (UUID) | UUID |
| title | VARCHAR(255) | title VARCHAR(255) | Giữ nguyên |
| content | TEXT | *(bỏ - chuyển sang first post)* | **Bỏ** |
| is_pinned | *(không có)* | is_pinned BOOLEAN DEFAULT FALSE | **Mới** |
| is_locked | *(không có)* | is_locked BOOLEAN DEFAULT FALSE | **Mới** |
| views_count | *(không có)* | views_count INTEGER DEFAULT 0 | **Mới** |
| created_at | TIMESTAMP | created_at TIMESTAMP | Giữ nguyên |
| updated_at | *(không có)* | updated_at TIMESTAMP | **Mới** |

**Cải tiến:**
- ✅ Đổi tên Thread → DiscussionThreads (rõ ràng hơn)
- ✅ Thêm is_pinned để ghim thread quan trọng
- ✅ Thêm is_locked để khóa thread
- ✅ Thêm views_count để đếm lượt xem

#### 3.6.2. Post → DiscussionPosts

**Thay đổi cấu trúc:**

| Trường | Thiết kế cũ | Thiết kế mới | Ghi chú |
|--------|-------------|--------------|---------|
| **PK** | post_id (INT) | post_id (UUID) | UUID |
| **FK** | thread_id (INT) | thread_id (UUID) | UUID |
| **FK** | parent_post_id (INT) | parent_post_id (UUID) | UUID (for nested replies) |
| **FK** | created_by (INT) | created_by (UUID) | UUID |
| content | TEXT | content TEXT | Giữ nguyên |
| is_answer | *(không có)* | is_answer BOOLEAN DEFAULT FALSE | **Mới** |
| upvotes_count | *(không có)* | upvotes_count INTEGER DEFAULT 0 | **Mới** |
| created_at | TIMESTAMP | created_at TIMESTAMP | Giữ nguyên |
| updated_at | *(không có)* | updated_at TIMESTAMP | **Mới** |

**Cải tiến:**
- ✅ Đổi tên Post → DiscussionPosts
- ✅ Thêm is_answer để đánh dấu câu trả lời đúng
- ✅ Thêm upvotes_count để vote posts

---

## 4. BẢNG MỚI ĐƯỢC THÊM VÀO (10 BẢNG)

### 4.1. User Management (3 bảng mới)

#### 4.1.1. Permissions (Mới)

**Mục đích:** Quản lý quyền hạn chi tiết (RBAC - Role-Based Access Control)

**Cấu trúc:**

| Trường | Kiểu | Ràng buộc | Mô tả |
|--------|------|-----------|-------|
| permission_id | UUID | PK | Mã định danh quyền |
| name | VARCHAR(100) | UNIQUE, NOT NULL | Tên quyền (ví dụ: CREATE_COURSE, EDIT_COURSE) |
| description | TEXT | NULL | Mô tả chi tiết |
| resource | VARCHAR(50) | NOT NULL | Tài nguyên (COURSE, USER, ASSIGNMENT) |
| action | VARCHAR(50) | NOT NULL | Hành động (CREATE, READ, UPDATE, DELETE) |
| created_at | TIMESTAMP | NOT NULL | Thời gian tạo |
| updated_at | TIMESTAMP | NOT NULL | Thời gian cập nhật |

**Ví dụ quyền:**
- CREATE_COURSE: Tạo course mới
- EDIT_COURSE: Sửa course
- DELETE_COURSE: Xóa course
- GRADE_SUBMISSION: Chấm bài
- VIEW_ANALYTICS: Xem báo cáo thống kê

**Lợi ích:**
- ✅ Phân quyền chi tiết, linh hoạt
- ✅ Dễ mở rộng thêm quyền mới
- ✅ Kiểm soát access control tốt hơn

#### 4.1.2. RolePermissions (Mới)

**Mục đích:** Bảng trung gian N:N giữa Roles và Permissions

**Cấu trúc:**

| Trường | Kiểu | Ràng buộc | Mô tả |
|--------|------|-----------|-------|
| role_permission_id | UUID | PK | Mã định danh |
| role_id | UUID | FK → Roles, NOT NULL | Vai trò |
| permission_id | UUID | FK → Permissions, NOT NULL | Quyền |
| granted_at | TIMESTAMP | NOT NULL | Thời gian gán quyền |

**Ví dụ:**
- INSTRUCTOR role có quyền: CREATE_COURSE, EDIT_COURSE, GRADE_SUBMISSION
- STUDENT role có quyền: VIEW_COURSE, SUBMIT_ASSIGNMENT
- ADMIN role có quyền: *ALL*

**Lợi ích:**
- ✅ Gán nhiều quyền cho một role
- ✅ Một quyền có thể thuộc nhiều roles
- ✅ Dễ quản lý và thay đổi quyền

#### 4.1.3. UserSettings (Mới)

**Mục đích:** Lưu cài đặt cá nhân của user

**Cấu trúc:**

| Trường | Kiểu | Ràng buộc | Mô tả |
|--------|------|-----------|-------|
| user_setting_id | UUID | PK | Mã định danh |
| user_id | UUID | FK → Users, UNIQUE, NOT NULL | User |
| theme | VARCHAR(20) | DEFAULT 'LIGHT' | Giao diện (LIGHT, DARK) |
| language | VARCHAR(10) | DEFAULT 'vi' | Ngôn ngữ (vi, en) |
| timezone | VARCHAR(50) | DEFAULT 'Asia/Ho_Chi_Minh' | Múi giờ |
| notifications_enabled | BOOLEAN | DEFAULT TRUE | Bật thông báo |
| email_notifications | BOOLEAN | DEFAULT TRUE | Thông báo qua email |
| created_at | TIMESTAMP | NOT NULL | Thời gian tạo |
| updated_at | TIMESTAMP | NOT NULL | Thời gian cập nhật |

**Lợi ích:**
- ✅ Cá nhân hóa trải nghiệm người dùng
- ✅ Quản lý preferences riêng biệt
- ✅ Không làm phình bảng Users

---

### 4.2. Course Management (3 bảng mới)

#### 4.2.1. Categories (Mới)

**Mục đích:** Phân loại courses theo danh mục

**Cấu trúc:**

| Trường | Kiểu | Ràng buộc | Mô tả |
|--------|------|-----------|-------|
| category_id | UUID | PK | Mã định danh danh mục |
| name | VARCHAR(100) | UNIQUE, NOT NULL | Tên danh mục |
| slug | VARCHAR(100) | UNIQUE, NOT NULL | Slug cho URL |
| description | TEXT | NULL | Mô tả danh mục |
| parent_category_id | UUID | FK → Categories, NULL | Danh mục cha (nested categories) |
| display_order | INTEGER | DEFAULT 0 | Thứ tự hiển thị |
| is_active | BOOLEAN | DEFAULT TRUE | Trạng thái hoạt động |
| created_at | TIMESTAMP | NOT NULL | Thời gian tạo |
| updated_at | TIMESTAMP | NOT NULL | Thời gian cập nhật |

**Ví dụ danh mục:**
- Programming (lập trình)
  - Web Development
  - Mobile Development
  - Data Science
- Business (kinh doanh)
- Design (thiết kế)

**Lợi ích:**
- ✅ Tổ chức courses có hệ thống
- ✅ Tìm kiếm và filter dễ dàng
- ✅ Hỗ trợ nested categories (danh mục con)

#### 4.2.2. Tags (Mới)

**Mục đích:** Gắn tags cho courses để tìm kiếm và filter

**Cấu trúc:**

| Trường | Kiểu | Ràng buộc | Mô tả |
|--------|------|-----------|-------|
| tag_id | UUID | PK | Mã định danh tag |
| name | VARCHAR(50) | UNIQUE, NOT NULL | Tên tag |
| slug | VARCHAR(50) | UNIQUE, NOT NULL | Slug cho URL |
| usage_count | INTEGER | DEFAULT 0 | Số lần sử dụng |
| created_at | TIMESTAMP | NOT NULL | Thời gian tạo |

**Ví dụ tags:**
- javascript, python, java, react, nodejs, sql, machine-learning, beginner, advanced

**Lợi ích:**
- ✅ Tìm kiếm courses theo từ khóa
- ✅ Filter courses linh hoạt
- ✅ Gợi ý courses liên quan

#### 4.2.3. CourseTags (Mới)

**Mục đích:** Bảng trung gian N:N giữa Courses và Tags

**Cấu trúc:**

| Trường | Kiểu | Ràng buộc | Mô tả |
|--------|------|-----------|-------|
| course_id | UUID | FK → Courses, NOT NULL | Course |
| tag_id | UUID | FK → Tags, NOT NULL | Tag |
| created_at | TIMESTAMP | NOT NULL | Thời gian gắn tag |

**Composite PK:** (course_id, tag_id)

**Ví dụ:**
- Course "Lập trình Web với React" có tags: javascript, react, frontend, beginner
- Course "Python cho Data Science" có tags: python, data-science, machine-learning, intermediate

**Lợi ích:**
- ✅ Một course có nhiều tags
- ✅ Một tag thuộc nhiều courses
- ✅ Tăng khả năng discovery

---

### 4.3. Grading (2 bảng mới)

#### 4.3.1. Grades (Mới)

**Mục đích:** Lưu điểm số và feedback cho submissions (tách riêng khỏi AssignmentSubmissions)

**Cấu trúc:**

| Trường | Kiểu | Ràng buộc | Mô tả |
|--------|------|-----------|-------|
| grade_id | UUID | PK | Mã định danh điểm |
| assignment_submission_id | UUID | FK → AssignmentSubmissions, UNIQUE, NOT NULL | Submission |
| graded_by | UUID | FK → Users, NOT NULL | Người chấm (instructor) |
| score | DECIMAL(5,2) | NOT NULL | Điểm số |
| max_score | DECIMAL(5,2) | NOT NULL | Điểm tối đa |
| feedback | TEXT | NULL | Nhận xét của giảng viên |
| rubric_scores | JSONB | NULL | Điểm theo rubric (chi tiết) |
| graded_at | TIMESTAMP | NOT NULL | Thời gian chấm |
| created_at | TIMESTAMP | NOT NULL | Thời gian tạo |
| updated_at | TIMESTAMP | NOT NULL | Thời gian cập nhật |

**Ví dụ rubric_scores:**
```json
{
  "code_quality": 8.5,
  "correctness": 9.0,
  "documentation": 7.5,
  "testing": 8.0
}
```

**Lợi ích:**
- ✅ Tách concerns: Submission vs. Grading
- ✅ Lưu lịch sử chấm điểm (updated_at)
- ✅ Hỗ trợ rubric grading chi tiết
- ✅ Track người chấm (graded_by)

#### 4.3.2. GradeScale (Mới)

**Mục đích:** Định nghĩa thang điểm (A, B, C, D, F) cho courses

**Cấu trúc:**

| Trường | Kiểu | Ràng buộc | Mô tả |
|--------|------|-----------|-------|
| grade_scale_id | UUID | PK | Mã định danh thang điểm |
| course_id | UUID | FK → Courses, NOT NULL | Course áp dụng |
| letter_grade | VARCHAR(5) | NOT NULL | Điểm chữ (A, A-, B+, B, ...) |
| min_percentage | DECIMAL(5,2) | NOT NULL | % tối thiểu (ví dụ: 90.00 cho A) |
| max_percentage | DECIMAL(5,2) | NOT NULL | % tối đa (ví dụ: 100.00 cho A) |
| gpa_value | DECIMAL(3,2) | NULL | Giá trị GPA (4.0 cho A) |
| description | TEXT | NULL | Mô tả |
| created_at | TIMESTAMP | NOT NULL | Thời gian tạo |

**Ví dụ thang điểm:**

| Letter Grade | Min % | Max % | GPA |
|--------------|-------|-------|-----|
| A | 90.00 | 100.00 | 4.0 |
| B+ | 85.00 | 89.99 | 3.5 |
| B | 80.00 | 84.99 | 3.0 |
| C+ | 75.00 | 79.99 | 2.5 |
| C | 70.00 | 74.99 | 2.0 |
| D | 60.00 | 69.99 | 1.0 |
| F | 0.00 | 59.99 | 0.0 |

**Lợi ích:**
- ✅ Mỗi course có thể định nghĩa thang điểm riêng
- ✅ Tự động convert điểm số sang điểm chữ
- ✅ Tính GPA chính xác

---

### 4.4. Interaction (1 bảng mới)

#### 4.4.1. Notifications (Mới)

**Mục đích:** Gửi thông báo cho users

**Cấu trúc:**

| Trường | Kiểu | Ràng buộc | Mô tả |
|--------|------|-----------|-------|
| notification_id | UUID | PK | Mã định danh thông báo |
| user_id | UUID | FK → Users, NOT NULL | User nhận thông báo |
| type | VARCHAR(50) | NOT NULL | Loại (NEW_ASSIGNMENT, GRADE_POSTED, NEW_ANNOUNCEMENT) |
| title | VARCHAR(255) | NOT NULL | Tiêu đề |
| content | TEXT | NOT NULL | Nội dung |
| link | TEXT | NULL | Link liên quan |
| is_read | BOOLEAN | DEFAULT FALSE | Đã đọc chưa |
| read_at | TIMESTAMP | NULL | Thời gian đọc |
| created_at | TIMESTAMP | NOT NULL | Thời gian tạo |

**Ví dụ loại thông báo:**
- NEW_ASSIGNMENT: Có assignment mới
- GRADE_POSTED: Điểm đã được đăng
- NEW_ANNOUNCEMENT: Thông báo mới từ instructor
- NEW_REPLY: Có người trả lời discussion thread
- DEADLINE_REMINDER: Nhắc nhở hạn nộp bài

**Lợi ích:**
- ✅ Tăng engagement của users
- ✅ Giữ users cập nhật thông tin
- ✅ Reduce missed deadlines

---

### 4.5. System (1 bảng mới)

#### 4.5.1. AuditLog (Mới)

**Mục đích:** Ghi log audit trail - theo dõi hành động của users

**Cấu trúc:**

| Trường | Kiểu | Ràng buộc | Mô tả |
|--------|------|-----------|-------|
| audit_log_id | UUID | PK | Mã định danh log |
| user_id | UUID | FK → Users, NULL | User thực hiện (NULL nếu system) |
| action | VARCHAR(50) | NOT NULL | Hành động (CREATE, UPDATE, DELETE, LOGIN, LOGOUT) |
| table_name | VARCHAR(50) | NOT NULL | Bảng bị tác động |
| record_id | UUID | NULL | ID của record bị tác động |
| old_values | JSONB | NULL | Giá trị cũ (trước khi thay đổi) |
| new_values | JSONB | NULL | Giá trị mới (sau khi thay đổi) |
| ip_address | VARCHAR(45) | NULL | Địa chỉ IP |
| user_agent | TEXT | NULL | Trình duyệt/device |
| created_at | TIMESTAMP | NOT NULL | Thời gian hành động |

**Ví dụ log:**
```
User "nvkiet@example.com" (20000001-...)
updated Course "CS101" (40000001-...)
at 2025-12-01 10:30:00
from IP 192.168.1.100
Old: { "status": "DRAFT" }
New: { "status": "PUBLISHED" }
```

**Lợi ích:**
- ✅ Theo dõi WHO did WHAT WHEN WHERE
- ✅ Phát hiện hành vi bất thường
- ✅ Compliance và security
- ✅ Rollback changes nếu cần

---

## 5. SO SÁNH CHI TIẾT TỪNG NHÓM

### 5.1. User Management

| Tiêu chí | Thiết kế cũ | Thiết kế mới | Cải tiến |
|----------|-------------|--------------|----------|
| **Số bảng** | 3 | 6 | +3 bảng (Permissions, RolePermissions, UserSettings) |
| **RBAC** | Cơ bản (Roles only) | Đầy đủ (Roles + Permissions) | ✅ Phân quyền chi tiết hơn |
| **User info** | 8 trường | 12 trường | ✅ Thêm avatar, bio, phone, address, status |
| **Settings** | Không có | UserSettings table | ✅ Cá nhân hóa trải nghiệm |
| **Khóa chính** | INT | UUID | ✅ An toàn, scalable hơn |

**Kết luận:** Thiết kế mới hỗ trợ RBAC đầy đủ, phân quyền linh hoạt, lưu thông tin user chi tiết hơn.

---

### 5.2. Course Management

| Tiêu chí | Thiết kế cũ | Thiết kế mới | Cải tiến |
|----------|-------------|--------------|----------|
| **Số bảng** | 4 | 7 | +3 bảng (Categories, Tags, CourseTags) |
| **Phân loại** | Không có | Categories + Tags | ✅ Tổ chức có hệ thống, tìm kiếm tốt hơn |
| **Course metadata** | 10 trường | 15 trường | ✅ Thêm thumbnail, difficulty, language, metrics |
| **Resource management** | Cơ bản | Chi tiết (file_size, mime_type, duration) | ✅ Quản lý tài nguyên tốt hơn |
| **Lecture types** | VIDEO, READING, ASSIGNMENT | Giữ nguyên | Không đổi |

**Kết luận:** Thiết kế mới hỗ trợ course discovery tốt hơn với Categories và Tags, metadata phong phú hơn.

---

### 5.3. Learning Progress

| Tiêu chí | Thiết kế cũ | Thiết kế mới | Cải tiến |
|----------|-------------|--------------|----------|
| **Số bảng** | 3 | 3 | Không đổi |
| **Progress tracking** | Cơ bản (status) | Chi tiết (completion %, time spent) | ✅ Analytics tốt hơn |
| **Attendance** | Có | Có | Giữ nguyên |
| **Schedule** | Có | Có | Giữ nguyên |

**Kết luận:** Thiết kế mới bổ sung progress tracking chi tiết hơn với completion % và time spent.

---

### 5.4. Assessments & Grading

| Tiêu chí | Thiết kế cũ | Thiết kế mới | Cải tiến |
|----------|-------------|--------------|----------|
| **Số bảng (Assessments)** | 6 | 4 | -2 (tách Assignment ra) |
| **Số bảng (Grading)** | 0 | 2 | +2 (Grades, GradeScale) |
| **Quiz features** | Cơ bản | Advanced (randomize, time control, show answers) | ✅ Tính năng phong phú hơn |
| **Grading** | Embedded trong Submission | Tách riêng (Grades table) | ✅ Separation of concerns |
| **Rubric grading** | Không có | Có (JSONB rubric_scores) | ✅ Chấm điểm chi tiết hơn |
| **Grade scale** | Không có | Có (GradeScale table) | ✅ Thang điểm linh hoạt |

**Kết luận:** Thiết kế mới tách Assignments và Grading riêng biệt, hỗ trợ rubric grading và grade scale.

---

### 5.5. Interaction

| Tiêu chí | Thiết kế cũ | Thiết kế mới | Cải tiến |
|----------|-------------|--------------|----------|
| **Số bảng** | 2 | 3 | +1 (Notifications) |
| **Discussion features** | Cơ bản | Advanced (pin, lock, views, upvotes) | ✅ Tính năng forum đầy đủ |
| **Notifications** | Không có | Có | ✅ Tăng engagement |

**Kết luận:** Thiết kế mới bổ sung Notifications và cải thiện discussion features.

---

### 5.6. System

| Tiêu chí | Thiết kế cũ | Thiết kế mới | Cải tiến |
|----------|-------------|--------------|----------|
| **Audit log** | Không có | Có (AuditLog table) | ✅ Security và compliance |
| **System config** | Không có | Có thể bổ sung | Có thể mở rộng |

**Kết luận:** Thiết kế mới thêm AuditLog để theo dõi hành động users, tăng cường security.

---

## 6. CẢI TIẾN VỀ MẶT THIẾT KẾ

### 6.1. Khóa chính (Primary Key)

| Tiêu chí | Thiết kế cũ | Thiết kế mới | Lý do |
|----------|-------------|--------------|-------|
| **Kiểu dữ liệu** | INT (auto-increment) | UUID (uuid_generate_v4()) | ✅ An toàn hơn, không lộ thông tin số lượng records |
| **Performance** | Nhanh hơn (INT 4 bytes) | Chậm hơn một chút (UUID 16 bytes) | ⚠️ Trade-off chấp nhận được |
| **Scalability** | Khó scale horizontal | Dễ scale, không xung đột IDs | ✅ Phù hợp microservices |
| **Security** | Dễ đoán ID | Khó đoán, random | ✅ Tránh enumeration attacks |

**Kết luận:** UUID phù hợp hơn cho hệ thống production, mặc dù có nhược điểm về performance nhỏ.

---

### 6.2. Naming Convention

| Tiêu chí | Thiết kế cũ | Thiết kế mới | Lý do |
|----------|-------------|--------------|-------|
| **Bảng** | Singular (User, Course) | Plural (Users, Courses) | ✅ Convention phổ biến |
| **Trường** | Inconsistent | snake_case nhất quán | ✅ Dễ đọc, chuẩn PostgreSQL |
| **FK naming** | Inconsistent | Nhất quán (*_id) | ✅ Dễ hiểu, dễ maintain |

**Kết luận:** Thiết kế mới tuân thủ naming convention tốt hơn.

---

### 6.3. Normalization

| Tiêu chí | Thiết kế cũ | Thiết kế mới | Lý do |
|----------|-------------|--------------|-------|
| **Separation of concerns** | Submission chứa cả grading | Tách Submission và Grades | ✅ 3NF, dễ maintain |
| **Duplicate data** | Có (content trong Thread) | Không (content trong DiscussionPosts) | ✅ Giảm redundancy |
| **Flexible data** | Hardcoded fields | JSONB cho dynamic data | ✅ Linh hoạt hơn |

**Kết luận:** Thiết kế mới tuân thủ normalization tốt hơn, giảm redundancy.

---

### 6.4. Indexes và Performance

| Tiêu chí | Thiết kế cũ | Thiết kế mới | Lý do |
|----------|-------------|--------------|-------|
| **Indexes** | Cơ bản (PK, FK) | Đầy đủ (PK, FK, status, email, ...) | ✅ Query nhanh hơn |
| **Composite indexes** | Ít | Nhiều hơn (course_id + user_id) | ✅ Optimize JOIN queries |
| **JSONB indexes** | Không có | Có (GIN index) | ✅ Query JSONB nhanh |

**Kết luận:** Thiết kế mới có index strategy tốt hơn, tối ưu query performance.

---

### 6.5. Data Types

| Tiêu chí | Thiết kế cũ | Thiết kế mới | Lý do |
|----------|-------------|--------------|-------|
| **URL fields** | VARCHAR(255) | TEXT | ✅ Không giới hạn độ dài |
| **JSON data** | TEXT | JSONB | ✅ Query và validate tốt hơn |
| **Boolean** | TINYINT | BOOLEAN | ✅ Semantic rõ ràng |
| **Timestamps** | TIMESTAMP | TIMESTAMP (timezone-aware) | ✅ Hỗ trợ múi giờ |

**Kết luận:** Thiết kế mới sử dụng data types phù hợp hơn với PostgreSQL.

---

## 7. ĐÁNH GIÁ TỔNG THỂ

### 7.1. Điểm mạnh của thiết kế mới

1. **Đầy đủ tính năng hơn:**
   - ✅ Thêm 10 bảng mới (31 vs 21)
   - ✅ RBAC đầy đủ (Permissions)
   - ✅ Course discovery (Categories, Tags)
   - ✅ Grading system riêng biệt
   - ✅ Notifications
   - ✅ Audit log

2. **Thiết kế tốt hơn:**
   - ✅ UUID thay vì INT
   - ✅ Naming convention nhất quán
   - ✅ Normalization tốt hơn
   - ✅ Separation of concerns rõ ràng

3. **Metadata phong phú hơn:**
   - ✅ User: +7 trường (avatar, bio, phone, address, status, ...)
   - ✅ Course: +5 trường (thumbnail, difficulty, language, metrics, ...)
   - ✅ Progress: +4 trường (completion %, time spent, ...)

4. **Performance tốt hơn:**
   - ✅ Index strategy đầy đủ
   - ✅ JSONB cho dynamic data
   - ✅ Composite indexes cho JOIN queries

5. **Security tốt hơn:**
   - ✅ UUID (không lộ thông tin)
   - ✅ RBAC chi tiết
   - ✅ Audit log
   - ✅ Status fields để quản lý trạng thái

---

### 7.2. Điểm yếu của thiết kế cũ

1. **Thiếu tính năng:**
   - ❌ Không có RBAC đầy đủ
   - ❌ Không có Categories/Tags
   - ❌ Không có Grading system riêng
   - ❌ Không có Notifications
   - ❌ Không có Audit log

2. **Thiết kế chưa tối ưu:**
   - ❌ INT primary key (lộ thông tin, khó scale)
   - ❌ Naming convention không nhất quán
   - ❌ Submission chứa cả grading (không tách concerns)

3. **Metadata thiếu:**
   - ❌ User thiếu thông tin cá nhân
   - ❌ Course thiếu metadata (thumbnail, difficulty, ...)
   - ❌ Progress không track completion %

4. **Báo cáo ngắn:**
   - ❌ Chỉ 16 trang (thiếu chi tiết)
   - ❌ Không có FAQ
   - ❌ Không có đánh giá so sánh

---

### 7.3. Kết luận

**Thiết kế mới (31 bảng) vượt trội hơn thiết kế cũ (21 bảng) về:**
1. ✅ Tính năng đầy đủ hơn (+10 bảng mới)
2. ✅ Thiết kế chuẩn hơn (UUID, naming, normalization)
3. ✅ Metadata phong phú hơn
4. ✅ Security tốt hơn (RBAC, audit log)
5. ✅ Performance tối ưu hơn (indexes)

**Trade-offs:**
- ⚠️ Phức tạp hơn (31 bảng vs 21 bảng)
- ⚠️ Cần nhiều storage hơn (UUID 16 bytes vs INT 4 bytes)
- ⚠️ JOIN queries phức tạp hơn

**Nhưng trade-offs này chấp nhận được** vì:
- Tính năng và flexibility tăng đáng kể
- Security và scalability tốt hơn
- Phù hợp với hệ thống production

---

**Khuyến nghị:** Sử dụng thiết kế mới (31 bảng) cho hệ thống B-Learning.

---

**Ngày so sánh:** 2025-12-01
**Tác giả:** Nguyễn Văn Kiệt - CNTT1-K63
**Nguồn:** Scripts/DTPM_B-Learning.pdf (old) vs. 98-B-Learing-Core/documents/ (new)
