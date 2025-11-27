# Plan Task: Thiết Kế Database Tinh Giản - B-Learning Core

## Mục tiêu
Xây dựng database tinh giản cho hệ thống B-Learning (Blended Learning) với **16 bảng** (giảm 48% từ 31 bảng), tập trung vào các chức năng core, dễ hiểu và dễ bảo trì.

---

## Yêu cầu đầu ra

### 1. Tài liệu đặc tả hệ thống (documents/)
- **BFD-SPEC.md** - Business Function Diagram specification
- **ERD-SPEC.md** - Entity Relationship Diagram specification
- **DATABASE-SCHEMA.md** - Chi tiết schema với DDL đầy đủ
- **FUNCTIONAL-REQUIREMENTS.md** - Yêu cầu chức năng chi tiết
- **API-ENDPOINTS.md** - API endpoints và business logic
- **TABLES-EXPLANATION-VI.md** - ⭐ **MỚI**: Giải thích ý nghĩa bảng và thuộc tính bằng tiếng Việt

### 2. Sơ đồ UML (StarUML format)
- **BFD.mdj** - Business Function Diagram
- **ERD.mdj** - Entity Relationship Diagram với đầy đủ PK, FK, attributes

### 3. SQL Scripts (sql/)
- **01-schema.sql** - Create tables với comments tiếng Việt
- **02-indexes.sql** - Performance indexes
- **03-constraints.sql** - Foreign key & check constraints
- **04-seed-data.sql** - Sample data for testing

**KHÔNG CẦN:** triggers.sql, views.sql, drop-all.sql

---

## Phạm vi hệ thống

### ✅ Chức năng GIỮ LẠI (100% core)
1. **User Management** - Quản lý người dùng và phân quyền
2. **Course Management** - Quản lý khóa học, module, lecture
3. **Assessment** - Quiz, Assignment, chấm điểm
4. **Progress Tracking** - Theo dõi tiến độ học tập
5. **Class Management** - Quản lý lớp học (blended)
6. **Enrollment** - Đăng ký khóa học/lớp học
7. **Certificate** - Cấp chứng chỉ

### ⚠️ Chức năng ĐƠN GIẢN HÓA
1. **Quiz System** - Dùng JSON thay vì QuizQuestion table
2. **Attempt System** - Gộp QuizSubmission vào Attempt.answers
3. **Enrollment** - Gộp CourseEnrollment + ClassEnrollment
4. **Progress** - Chỉ tracking ở module level (không tracking lecture)
5. **Class** - Lưu schedules trong JSON (không tách Schedule table)
6. **Certificate** - Giảm từ 3 bảng xuống 1 bảng

### ❌ Chức năng BỎ ĐI (dùng external services)
1. **Notification System** - Dùng email service (SendGrid, AWS SES)
2. **Activity Logging** - Dùng application logs (Winston, Logtail)
3. **File Management** - Dùng cloud storage (S3, GCS)
4. **System Settings** - Dùng config files (.env)
5. **GradeBook** - Tính toán động (không lưu table)

---

## Danh sách 16 bảng

### Domain 1: User Management (3 bảng)
1. **User** - Tài khoản người dùng
2. **Role** - Vai trò hệ thống
3. **UserRole** - Phân quyền user-role

### Domain 2: Course Content (4 bảng)
4. **Course** - Khóa học
5. **Module** - Chương/module
6. **Lecture** - Bài giảng (bao gồm cả Assignment)
7. **Resource** - Tài liệu đính kèm

### Domain 3: Assessment (5 bảng)
8. **Quiz** - Bài kiểm tra (+ questions JSON)
9. **Question** - Ngân hàng câu hỏi
10. **Option** - Lựa chọn cho MCQ
11. **Attempt** - Lần làm bài (+ answers JSON)
12. **AssignmentSubmission** - Nộp bài tập

### Domain 4: Enrollment & Progress (2 bảng)
13. **Enrollment** - Đăng ký khóa học/lớp
14. **Progress** - Tiến độ học tập

### Domain 5: Class & Certificate (2 bảng)
15. **Class** - Lớp học (+ schedules JSON)
16. **Certificate** - Chứng chỉ

---

## Các bước thực hiện

## PHASE 1: PHÂN TÍCH & THIẾT KẾ

### Bước 1: Đọc tài liệu tham khảo

#### 1.1. Đọc đề xuất tinh giản
- Đọc file `99-B-Learing/documents/DATABASE-SIMPLIFICATION-PROPOSAL.md`
- Hiểu rõ lý do tinh giản từ 31 → 16 bảng
- Nắm các thay đổi chính:
  - Bỏ 9 bảng (Notification, ActivityLog, File, ...)
  - Gộp 6 bảng → 3 bảng (QuizQuestion, QuizSubmission, Enrollment)
  - Đơn giản hóa 3 bảng (Certificate, Assignment, Progress)

#### 1.2. Tham khảo thiết kế v2.0
- Xem ERD trong `99-B-Learing/ERD.mdj`
- Đọc schema trong `99-B-Learing/documents/DATABASE-SCHEMA.md`
- Hiểu business logic từ `99-B-Learing/documents/FUNCTIONAL-REQUIREMENTS.md`

---

### Bước 2: Thiết kế Business Function Diagram (BFD)

#### 2.1. Xác định các actors (GIỮ NGUYÊN)
1. **Student** (Học viên)
2. **Instructor** (Giảng viên)
3. **Teaching Assistant (TA)**
4. **Admin** (Quản trị viên)
5. **System** (Hệ thống tự động)

#### 2.2. Xác định các chức năng chính (CORE ONLY)

##### A. User Management
```
1. Authentication & Authorization
   - Register
   - Login / Logout
   - Password reset
   - Email verification
   - Role management

2. Profile Management
   - View profile
   - Update profile
   - Upload avatar
```

##### B. Course Management (Instructor/Admin)
```
1. Course CRUD
   - Create course
   - Update course info
   - Publish/Archive course
   - Delete course

2. Module Management
   - Create/Update/Delete module
   - Set prerequisites
   - Reorder modules

3. Lecture Management
   - Upload video/PDF/slides
   - Set lecture duration
   - Add resources
   - Create assignment (lecture type = ASSIGNMENT)
```

##### C. Assessment Management (Instructor)
```
1. Quiz Management
   - Create quiz with questions (JSON)
   - Auto-grading setup
   - Time limit & attempts

2. Assignment Management
   - Create assignment (as Lecture)
   - Set deadline
   - Review submissions
```

##### D. Learning (Student)
```
1. Course Enrollment
   - Browse courses
   - Enroll in course (self-paced or class)
   - View enrolled courses

2. Learning Activities
   - Watch lectures
   - Take quizzes
   - Submit assignments
   - Track progress (module level)

3. Results & Certificate
   - View grades
   - Download certificate
```

##### E. Class Management (Blended Learning)
```
1. Class Setup (Instructor/Admin)
   - Create class
   - Set schedules (JSON)
   - Assign instructor
   - Link to course

2. Class Operations
   - Attendance tracking (in schedules JSON)
   - In-person sessions
```

##### F. Grading (Instructor/TA)
```
1. Assignment Grading
   - View submissions
   - Grade assignments
   - Provide feedback

2. Quiz Review
   - Auto-grading for MCQ
   - Manual grading for essay
   - Grade adjustments
```

##### G. Certificate Management (System/Admin)
```
1. Auto-issuance
   - Check completion criteria
   - Generate certificate
   - Email notification (via email service)

2. Verification
   - Public verification page
   - Code verification
```

#### 2.3. Vẽ BFD trong StarUML

**Tạo file BFD.mdj** với cấu trúc:

```
Actor          →    Use Cases                →    Sub-functions
[Student]      →    [Enroll in Course]       →    [Self-paced / Join Class]
               →    [Learn Course]           →    [Watch Lecture, Take Quiz, Submit Assignment]
               →    [Track Progress]         →    [View Module Progress, View Grades]

[Instructor]   →    [Manage Course]          →    [Create, Update, Publish]
               →    [Create Content]         →    [Upload Lecture, Create Quiz, Create Assignment]
               →    [Grade Students]         →    [Grade Assignment, Review Quiz]

[Admin]        →    [Manage Users]           →    [Create User, Assign Role]
               →    [Manage System]          →    [System Settings]

[System]       →    [Auto Grade Quiz]        →    [Calculate Score MCQ]
               →    [Issue Certificate]      →    [Check Criteria, Generate PDF]
```

**Lưu ý:**
- Đơn giản hơn v2.0 (bỏ Notification, File upload use cases)
- Focus vào core learning flow

---

### Bước 3: Thiết kế Entity Relationship Diagram (ERD)

#### 3.1. Xác định các thực thể (16 bảng)

##### DOMAIN 1: USER MANAGEMENT (3 bảng)

**1. User** - Người dùng
```
PK: user_id (UUID)
Attributes:
  - email (VARCHAR 255, UNIQUE, NOT NULL)
  - password_hash (VARCHAR 255, NOT NULL)
  - first_name (VARCHAR 100, NOT NULL)
  - last_name (VARCHAR 100, NOT NULL)
  - avatar_url (VARCHAR 500)
  - phone (VARCHAR 20)
  - account_status (VARCHAR 30, DEFAULT 'ACTIVE')
    Values: PENDING_VERIFICATION, ACTIVE, SUSPENDED, DELETED
  - preferences (JSON) -- ⭐ MỚI: Lưu notification preferences, locale, timezone
  - email_verified_at (TIMESTAMP)
  - last_login_at (TIMESTAMP)
  - created_at (TIMESTAMP, NOT NULL)
  - updated_at (TIMESTAMP, NOT NULL)
```

**2. Role** - Vai trò
```
PK: role_id (UUID)
Attributes:
  - name (VARCHAR 50, UNIQUE, NOT NULL)
    Values: STUDENT, INSTRUCTOR, TA, ADMIN
  - description (TEXT)
  - permissions (JSON)
  - created_at (TIMESTAMP)
```

**3. UserRole** - Quan hệ User-Role
```
PK: user_role_id (UUID)
FK: user_id → User.user_id (CASCADE)
FK: role_id → Role.role_id (CASCADE)
Attributes:
  - granted_at (TIMESTAMP, DEFAULT CURRENT_TIMESTAMP)
  - granted_by (UUID, FK → User.user_id)
  - expires_at (TIMESTAMP)

UNIQUE(user_id, role_id)
```

---

##### DOMAIN 2: COURSE CONTENT (4 bảng)

**4. Course** - Khóa học
```
PK: course_id (UUID)
Attributes:
  - code (VARCHAR 50, UNIQUE, NOT NULL)
  - title (VARCHAR 200, NOT NULL)
  - description (TEXT)
  - short_description (VARCHAR 500)
  - thumbnail_url (VARCHAR 500)
  - category (VARCHAR 100)
  - difficulty_level (VARCHAR 20)
    Values: BEGINNER, INTERMEDIATE, ADVANCED
  - estimated_hours (DECIMAL 5,2)
  - status (VARCHAR 20, DEFAULT 'DRAFT')
    Values: DRAFT, PUBLISHED, ARCHIVED
  - published_at (TIMESTAMP)
  - created_by (UUID, FK → User.user_id)
  - created_at (TIMESTAMP, NOT NULL)
  - updated_at (TIMESTAMP, NOT NULL)
```

**5. Module** - Chương/Module
```
PK: module_id (UUID)
FK: course_id → Course.course_id (CASCADE)
Attributes:
  - title (VARCHAR 200, NOT NULL)
  - description (TEXT)
  - order_num (INT, NOT NULL)
  - prerequisite_module_ids (UUID[])
  - estimated_duration_minutes (INT)
  - created_at (TIMESTAMP)
  - updated_at (TIMESTAMP)

UNIQUE(course_id, order_num)
```

**6. Lecture** - Bài giảng (bao gồm cả Assignment)
```
PK: lecture_id (UUID)
FK: module_id → Module.module_id (CASCADE)
Attributes:
  - title (VARCHAR 200, NOT NULL)
  - description (TEXT)
  - type (VARCHAR 20, NOT NULL)
    Values: VIDEO, PDF, SLIDE, AUDIO, TEXT, ASSIGNMENT
  - content_url (VARCHAR 1024)
  - duration_seconds (INT)
  - order_num (INT, NOT NULL)

  -- ⭐ MỚI: Assignment configuration (chỉ dùng khi type = ASSIGNMENT)
  - assignment_config (JSON)
    Structure: {
      "max_points": 100,
      "due_date": "2025-12-15T23:59:00Z",
      "submission_types": ["file", "text", "code"],
      "instructions": "Submit your code..."
    }

  - is_preview (BOOLEAN, DEFAULT FALSE)
  - is_downloadable (BOOLEAN, DEFAULT TRUE)
  - created_at (TIMESTAMP)
  - updated_at (TIMESTAMP)

UNIQUE(module_id, order_num)
```

**7. Resource** - Tài liệu đính kèm
```
PK: resource_id (UUID)
FK: lecture_id → Lecture.lecture_id (CASCADE)
Attributes:
  - title (VARCHAR 200, NOT NULL)
  - file_url (VARCHAR 500, NOT NULL)
  - file_type (VARCHAR 100)
  - file_size_bytes (BIGINT)
  - created_at (TIMESTAMP)
```

---

##### DOMAIN 3: ASSESSMENT (5 bảng)

**8. Quiz** - Bài kiểm tra
```
PK: quiz_id (UUID)
FK: course_id → Course.course_id (CASCADE)
Attributes:
  - title (VARCHAR 200, NOT NULL)
  - description (TEXT)
  - time_limit_minutes (INT)
  - pass_score (DECIMAL 5,2)

  -- ⭐ THAY ĐỔI: Gộp QuizQuestion vào đây
  - questions (JSON, NOT NULL)
    Structure: [
      {
        "question_id": "uuid",
        "points": 10,
        "order": 1
      }
    ]

  - shuffle_questions (BOOLEAN, DEFAULT FALSE)
  - show_correct_answers (BOOLEAN, DEFAULT TRUE)
  - is_published (BOOLEAN, DEFAULT FALSE)
  - created_by (UUID, FK → User.user_id)
  - created_at (TIMESTAMP)
  - updated_at (TIMESTAMP)
```

**9. Question** - Câu hỏi
```
PK: question_id (UUID)
FK: course_id → Course.course_id (CASCADE)
Attributes:
  - text (TEXT, NOT NULL)
  - type (VARCHAR 20, NOT NULL)
    Values: MCQ, TRUE_FALSE, ESSAY, SHORT_ANSWER
  - difficulty (VARCHAR 20, DEFAULT 'MEDIUM')
    Values: EASY, MEDIUM, HARD
  - max_points (DECIMAL 5,2, NOT NULL, DEFAULT 1.00)
  - explanation (TEXT)
  - is_active (BOOLEAN, DEFAULT TRUE)
  - created_by (UUID, FK → User.user_id)
  - created_at (TIMESTAMP)
  - updated_at (TIMESTAMP)
```

**10. Option** - Lựa chọn
```
PK: option_id (UUID)
FK: question_id → Question.question_id (CASCADE)
Attributes:
  - option_text (TEXT, NOT NULL)
  - is_correct (BOOLEAN, NOT NULL, DEFAULT FALSE)
  - order_num (INT, NOT NULL)
  - feedback (TEXT)

UNIQUE(question_id, order_num)
```

**11. Attempt** - Lần làm bài quiz
```
PK: attempt_id (UUID)
FK: quiz_id → Quiz.quiz_id (CASCADE)
FK: user_id → User.user_id (CASCADE)
FK: enrollment_id → Enrollment.enrollment_id (CASCADE) -- ⭐ THAY ĐỔI
Attributes:
  - attempt_number (INT, NOT NULL)
  - started_at (TIMESTAMP, NOT NULL, DEFAULT CURRENT_TIMESTAMP)
  - submitted_at (TIMESTAMP)
  - time_spent_seconds (INT, DEFAULT 0)
  - status (VARCHAR 20, NOT NULL, DEFAULT 'IN_PROGRESS')
    Values: IN_PROGRESS, SUBMITTED

  -- ⭐ THAY ĐỔI: Gộp QuizSubmission vào đây
  - answers (JSON)
    Structure: [
      {
        "question_id": "uuid",
        "answer_text": "Paris",
        "selected_options": ["uuid1"],
        "score": 10,
        "max_score": 10,
        "is_correct": true
      }
    ]

  - total_score (DECIMAL 6,2)
  - max_possible_score (DECIMAL 6,2, NOT NULL)
  - percentage_score (DECIMAL 5,2)
  - graded_at (TIMESTAMP)
  - graded_by (UUID, FK → User.user_id)

UNIQUE(user_id, quiz_id, attempt_number)
```

**12. AssignmentSubmission** - Nộp bài tập
```
PK: submission_id (UUID)
FK: lecture_id → Lecture.lecture_id (CASCADE) -- ⭐ THAY ĐỔI: Link to Lecture
FK: user_id → User.user_id (CASCADE)
FK: enrollment_id → Enrollment.enrollment_id (CASCADE)
Attributes:
  - submission_number (INT, NOT NULL, DEFAULT 1)
  - submitted_at (TIMESTAMP, NOT NULL, DEFAULT CURRENT_TIMESTAMP)
  - content (TEXT)
  - file_urls (JSON) -- Array of URLs: ["s3://...", "s3://..."]
  - code_submission (TEXT)
  - is_late (BOOLEAN, DEFAULT FALSE)
  - status (VARCHAR 20, NOT NULL, DEFAULT 'SUBMITTED')
    Values: SUBMITTED, GRADING, GRADED, RETURNED
  - score (DECIMAL 6,2)
  - max_score (DECIMAL 6,2)
  - feedback (TEXT)
  - graded_at (TIMESTAMP)
  - graded_by (UUID, FK → User.user_id)

UNIQUE(lecture_id, user_id, submission_number)
```

---

##### DOMAIN 4: ENROLLMENT & PROGRESS (2 bảng)

**13. Enrollment** - Đăng ký khóa học/lớp
```
PK: enrollment_id (UUID)
FK: user_id → User.user_id (CASCADE)
FK: course_id → Course.course_id (CASCADE)
FK: class_id → Class.class_id (SET NULL) -- ⭐ NULLABLE cho self-paced
Attributes:
  - role (VARCHAR 20, NOT NULL)
    Values: STUDENT, INSTRUCTOR, TA
  - status (VARCHAR 20, DEFAULT 'ACTIVE')
    Values: ACTIVE, COMPLETED, DROPPED, SUSPENDED
  - enrolled_at (TIMESTAMP, DEFAULT CURRENT_TIMESTAMP)
  - completed_at (TIMESTAMP)
  - final_grade (DECIMAL 5,2)
  - completion_percentage (DECIMAL 5,2, DEFAULT 0.00)
  - last_accessed_at (TIMESTAMP)

UNIQUE(user_id, course_id, COALESCE(class_id, '00000000-0000-0000-0000-000000000000'::UUID))
```

**14. Progress** - Tiến độ học tập (Module level only)
```
PK: progress_id (UUID)
FK: user_id → User.user_id (CASCADE)
FK: course_id → Course.course_id (CASCADE)
FK: module_id → Module.module_id (CASCADE)
Attributes:
  - status (VARCHAR 20, NOT NULL, DEFAULT 'NOT_STARTED')
    Values: NOT_STARTED, IN_PROGRESS, COMPLETED
  - started_at (TIMESTAMP)
  - completed_at (TIMESTAMP)

  -- ⭐ OPTIONAL: Lecture progress (nếu muốn track chi tiết)
  - lecture_progress (JSON)
    Structure: {
      "lecture_uuid_1": {"status": "COMPLETED", "percent": 100},
      "lecture_uuid_2": {"status": "IN_PROGRESS", "percent": 45}
    }

UNIQUE(user_id, course_id, module_id)
```

---

##### DOMAIN 5: CLASS & CERTIFICATE (2 bảng)

**15. Class** - Lớp học
```
PK: class_id (UUID)
FK: course_id → Course.course_id (CASCADE)
FK: instructor_id → User.user_id (SET NULL)
Attributes:
  - name (VARCHAR 100, NOT NULL)
  - start_date (DATE)
  - end_date (DATE)
  - status (VARCHAR 20, DEFAULT 'SCHEDULED')
    Values: SCHEDULED, ONGOING, COMPLETED, CANCELLED
  - max_students (INT)
  - location (VARCHAR 200)

  -- ⭐ THAY ĐỔI: Gộp Schedule + Attendance vào đây
  - schedules (JSON)
    Structure: [
      {
        "session_id": "uuid",
        "date": "2025-12-01",
        "start_time": "09:00",
        "end_time": "11:00",
        "topic": "Introduction",
        "location": "Room 101",
        "type": "IN_PERSON",
        "attendances": [
          {"user_id": "uuid1", "status": "PRESENT", "check_in": "09:05"},
          {"user_id": "uuid2", "status": "LATE", "check_in": "09:15"}
        ]
      }
    ]

  - created_at (TIMESTAMP)
  - updated_at (TIMESTAMP)
```

**16. Certificate** - Chứng chỉ
```
PK: certificate_id (UUID)
FK: user_id → User.user_id (CASCADE)
FK: course_id → Course.course_id (CASCADE)
Attributes:
  - certificate_code (VARCHAR 50, NOT NULL, UNIQUE)
  - verification_code (VARCHAR 100, NOT NULL, UNIQUE)
  - issue_date (DATE, NOT NULL)
  - final_grade (DECIMAL 5,2)
  - pdf_url (VARCHAR 500)
  - status (VARCHAR 20, DEFAULT 'ACTIVE')
    Values: ACTIVE, REVOKED
  - created_at (TIMESTAMP, DEFAULT CURRENT_TIMESTAMP)

UNIQUE(user_id, course_id)
```

---

#### 3.2. Mối quan hệ giữa các thực thể

##### Relationships Matrix (16 bảng)

| From Table | To Table | Type | Cardinality | FK Field | Cascade |
|------------|----------|------|-------------|----------|---------|
| UserRole | User | Many-to-One | N:1 | user_id | CASCADE |
| UserRole | Role | Many-to-One | N:1 | role_id | CASCADE |
| Module | Course | Many-to-One | N:1 | course_id | CASCADE |
| Lecture | Module | Many-to-One | N:1 | module_id | CASCADE |
| Resource | Lecture | Many-to-One | N:1 | lecture_id | CASCADE |
| Quiz | Course | Many-to-One | N:1 | course_id | CASCADE |
| Question | Course | Many-to-One | N:1 | course_id | CASCADE |
| Option | Question | Many-to-One | N:1 | question_id | CASCADE |
| Attempt | Quiz | Many-to-One | N:1 | quiz_id | CASCADE |
| Attempt | User | Many-to-One | N:1 | user_id | CASCADE |
| Attempt | Enrollment | Many-to-One | N:1 | enrollment_id | CASCADE |
| AssignmentSubmission | Lecture | Many-to-One | N:1 | lecture_id | CASCADE |
| AssignmentSubmission | User | Many-to-One | N:1 | user_id | CASCADE |
| AssignmentSubmission | Enrollment | Many-to-One | N:1 | enrollment_id | CASCADE |
| Enrollment | User | Many-to-One | N:1 | user_id | CASCADE |
| Enrollment | Course | Many-to-One | N:1 | course_id | CASCADE |
| Enrollment | Class | Many-to-One | N:1 | class_id | SET NULL |
| Progress | User | Many-to-One | N:1 | user_id | CASCADE |
| Progress | Course | Many-to-One | N:1 | course_id | CASCADE |
| Progress | Module | Many-to-One | N:1 | module_id | CASCADE |
| Class | Course | Many-to-One | N:1 | course_id | CASCADE |
| Class | User (instructor) | Many-to-One | N:1 | instructor_id | SET NULL |
| Certificate | User | Many-to-One | N:1 | user_id | CASCADE |
| Certificate | Course | Many-to-One | N:1 | course_id | CASCADE |

**Tổng: 23 relationships** (giảm từ 45+ ở v2.0)

---

#### 3.3. Vẽ ERD trong StarUML

**Tạo file ERD.mdj** với:

##### Layout Strategy (5 domains)

```
┌─────────────────────────────────────────────────────────────┐
│                     USER MANAGEMENT                         │
│  [User]  [Role]  [UserRole]                                 │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│                    COURSE CONTENT                           │
│  [Course]  [Module]  [Lecture]  [Resource]                  │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│                     ASSESSMENT                              │
│  [Quiz]  [Question]  [Option]                               │
│  [Attempt]  [AssignmentSubmission]                          │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│                 ENROLLMENT & PROGRESS                       │
│  [Enrollment]  [Progress]                                   │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│                  CLASS & CERTIFICATE                        │
│  [Class]  [Certificate]                                     │
└─────────────────────────────────────────────────────────────┘
```

**Lưu ý:**
- Đơn giản hơn nhiều so với v2.0 (31 bảng)
- Rõ ràng hơn cho người mới
- Ít relationships hơn

---

### Bước 4: Viết tài liệu đặc tả chi tiết

#### 4.1. BFD-SPEC.md

Nội dung:
```markdown
# Business Function Diagram Specification - B-Learning Core

## 1. Overview
Hệ thống B-Learning (Blended Learning) phiên bản Core với 16 bảng, tập trung vào chức năng học tập cốt lõi.

## 2. Actors
- Student (Học viên)
- Instructor (Giảng viên)
- Teaching Assistant (TA)
- Admin (Quản trị viên)
- System (Hệ thống tự động)

## 3. Use Cases by Actor
### Student
- UC-S01: Enroll in Course (Self-paced hoặc Class)
- UC-S02: Watch Lecture
- UC-S03: Take Quiz
- UC-S04: Submit Assignment
- UC-S05: View Progress
- UC-S06: View Grades
- UC-S07: Download Certificate

### Instructor
- UC-I01: Create Course
- UC-I02: Create Module
- UC-I03: Upload Lecture
- UC-I04: Create Quiz (with questions JSON)
- UC-I05: Create Assignment (as Lecture type)
- UC-I06: Grade Assignment
- UC-I07: Review Quiz Attempts

### Admin
- UC-A01: Manage Users
- UC-A02: Assign Roles
- UC-A03: Manage Courses
- UC-A04: System Settings (via config)

### System
- UC-SYS01: Auto-grade MCQ Quiz
- UC-SYS02: Auto-issue Certificate
- UC-SYS03: Calculate Progress

## 4. Use Case Descriptions
(Chi tiết từng use case với preconditions, main flow, alternative flows)

## 5. Business Rules
- Enrollment rules
- Grading policies
- Certificate issuance criteria
- Progress calculation
```

---

#### 4.2. ERD-SPEC.md

Nội dung:
```markdown
# Entity Relationship Diagram Specification - B-Learning Core

## 1. Overview
16 bảng, 5 domains, tinh giản 48% so với v2.0

## 2. Domains
1. User Management (3 bảng)
2. Course Content (4 bảng)
3. Assessment (5 bảng)
4. Enrollment & Progress (2 bảng)
5. Class & Certificate (2 bảng)

## 3. Key Changes from v2.0
### Bỏ (9 bảng):
- QuizQuestion → Quiz.questions (JSON)
- QuizSubmission → Attempt.answers (JSON)
- Assignment → Lecture.assignment_config (JSON)
- CourseEnrollment, ClassEnrollment → Enrollment
- Schedule, Attendance → Class.schedules (JSON)
- GradeBook → Dynamic calculation
- Notification, NotificationPreference, NotificationLog → Email service
- ActivityLog → Application logs
- File → Cloud storage
- SystemSettings → Config files
- CertificateTemplate, CertificateVerification → Simplified

## 4. Entities Detail
(Chi tiết 16 bảng từ Bước 3.1)

## 5. Relationships
(Chi tiết từ Bước 3.2)

## 6. JSON Structures
### Quiz.questions
[{"question_id": "uuid", "points": 10, "order": 1}]

### Attempt.answers
[{"question_id": "uuid", "answer_text": "...", "selected_options": ["uuid"], "score": 10}]

### Lecture.assignment_config
{"max_points": 100, "due_date": "...", "submission_types": [...]}

### Class.schedules
[{"date": "2025-12-01", "start_time": "09:00", "attendances": [...]}]

### User.preferences
{"notifications": {...}, "locale": "vi", "timezone": "Asia/Ho_Chi_Minh"}

## 7. Indexes Strategy
- Primary keys (UUID)
- Foreign keys
- Performance indexes
- JSON indexes (GIN)

## 8. Constraints
- Primary Key constraints
- Foreign Key constraints with CASCADE behaviors
- UNIQUE constraints
- CHECK constraints
- DEFAULT values
```

---

#### 4.3. DATABASE-SCHEMA.md

Nội dung:
```markdown
# Database Schema Specification - B-Learning Core

## 1. Overview
- Database: PostgreSQL 14+
- Total tables: 16
- Naming convention: PascalCase (tables), snake_case (columns)
- Primary keys: UUID
- Timestamps: TIMESTAMP

## 2. Extensions
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pg_trgm";
CREATE EXTENSION IF NOT EXISTS "btree_gin";

## 3. Table Definitions
### 3.1. User Management (3 bảng)
#### User
(Full DDL với comments tiếng Việt)

#### Role
(Full DDL)

#### UserRole
(Full DDL)

### 3.2. Course Content (4 bảng)
...

(Chi tiết cho tất cả 16 bảng)

## 4. Indexes
(All indexes từ 02-indexes.sql)

## 5. Constraints
(All constraints từ 03-constraints.sql)

## 6. Seed Data
(Sample data từ 04-seed-data.sql)
```

---

#### 4.4. FUNCTIONAL-REQUIREMENTS.md

Nội dung:
```markdown
# Functional Requirements Specification - B-Learning Core

## 1. User Management
### FR-UM-001: User Registration
- Input: email, password, first_name, last_name
- Process: Hash password, create user, send verification email
- Output: User account created

### FR-UM-002: User Login
...

## 2. Course Management
### FR-CM-001: Create Course
### FR-CM-002: Create Module
### FR-CM-003: Upload Lecture
### FR-CM-004: Create Assignment (as Lecture)

## 3. Assessment
### FR-AS-001: Create Quiz with Questions (JSON)
### FR-AS-002: Take Quiz
### FR-AS-003: Auto-grade MCQ
### FR-AS-004: Submit Assignment
### FR-AS-005: Grade Assignment

## 4. Enrollment & Progress
### FR-EP-001: Enroll in Course (Self-paced)
### FR-EP-002: Enroll in Class
### FR-EP-003: Track Module Progress
### FR-EP-004: Calculate Completion Percentage

## 5. Certificate
### FR-CT-001: Auto-issue Certificate
### FR-CT-002: Verify Certificate by Code

## 6. External Services Integration
### FR-EX-001: Send Email Notification (via SendGrid/SES)
### FR-EX-002: Upload File to Cloud Storage (S3/GCS)
### FR-EX-003: Application Logging (Winston/Logtail)
```

---

#### 4.5. API-ENDPOINTS.md

Nội dung:
```markdown
# API Endpoints Specification - B-Learning Core

## 1. Authentication
POST /api/auth/register
POST /api/auth/login
POST /api/auth/logout
POST /api/auth/verify-email
POST /api/auth/reset-password

## 2. User Management
GET /api/users/profile
PUT /api/users/profile
PUT /api/users/preferences

## 3. Courses
GET /api/courses
GET /api/courses/:id
POST /api/courses (Instructor/Admin)
PUT /api/courses/:id (Instructor/Admin)
DELETE /api/courses/:id (Admin)

GET /api/courses/:id/modules
GET /api/courses/:id/lectures

## 4. Enrollment
POST /api/enrollments (Enroll in course or class)
GET /api/enrollments/my-courses
GET /api/enrollments/:id/progress

## 5. Learning
GET /api/lectures/:id
POST /api/progress (Update module progress)

## 6. Assessment
GET /api/quizzes/:id
POST /api/attempts (Start quiz)
PUT /api/attempts/:id/submit (Submit quiz with answers JSON)
GET /api/attempts/:id/result

POST /api/assignments/:id/submit (Submit assignment)
GET /api/assignments/:id/submissions (Instructor)

## 7. Grading
PUT /api/submissions/:id/grade (Instructor)
GET /api/enrollments/:id/grades (Calculate dynamic)

## 8. Certificate
GET /api/certificates/my-certificates
GET /api/certificates/verify/:code

## 9. Class Management
POST /api/classes (Instructor/Admin)
GET /api/classes/:id
PUT /api/classes/:id/schedules (Update schedules JSON)
POST /api/classes/:id/attendance (Record attendance in schedules)
```

---

#### 4.6. TABLES-EXPLANATION-VI.md ⭐ MỚI

Nội dung:
```markdown
# GIẢI THÍCH BẢNG VÀ THUỘC TÍNH - HỆ THỐNG B-LEARNING CORE

**Mục đích:** Giúp developer và người tìm hiểu hiểu rõ ý nghĩa từng bảng và thuộc tính bằng tiếng Việt.

---

## DOMAIN 1: QUẢN LÝ NGƯỜI DÙNG (3 bảng)

### Bảng 1: User (Người dùng)
**Mục đích:** Lưu thông tin tài khoản người dùng

| Cột | Kiểu dữ liệu | Ý nghĩa | Ví dụ |
|-----|--------------|---------|-------|
| user_id | UUID | Mã định danh duy nhất của người dùng | 550e8400-e29b... |
| email | VARCHAR(255) | Email đăng nhập (duy nhất) | student@gmail.com |
| password_hash | VARCHAR(255) | Mật khẩu đã mã hóa (bcrypt) | $2a$10$rZ8pqBJKB... |
| first_name | VARCHAR(100) | Tên | Nguyễn |
| last_name | VARCHAR(100) | Họ và tên đệm | Văn Kiệt |
| avatar_url | VARCHAR(500) | Đường dẫn ảnh đại diện | https://... |
| phone | VARCHAR(20) | Số điện thoại | 0123456789 |
| account_status | VARCHAR(30) | Trạng thái tài khoản | ACTIVE, SUSPENDED |
| preferences | JSON | Tùy chọn người dùng | {"locale": "vi", ...} |
| email_verified_at | TIMESTAMP | Thời điểm xác thực email | 2025-11-27 10:00:00 |
| last_login_at | TIMESTAMP | Lần đăng nhập gần nhất | 2025-11-27 15:30:00 |
| created_at | TIMESTAMP | Thời điểm tạo tài khoản | 2025-11-01 09:00:00 |
| updated_at | TIMESTAMP | Lần cập nhật gần nhất | 2025-11-27 16:00:00 |

**Ví dụ preferences JSON:**
```json
{
  "notifications": {
    "assignment_due": {"email": true, "push": true},
    "grade_published": {"email": true, "push": false}
  },
  "locale": "vi",
  "timezone": "Asia/Ho_Chi_Minh"
}
```

---

### Bảng 2: Role (Vai trò)
**Mục đích:** Định nghĩa các vai trò trong hệ thống (STUDENT, INSTRUCTOR, TA, ADMIN)

| Cột | Kiểu dữ liệu | Ý nghĩa | Ví dụ |
|-----|--------------|---------|-------|
| role_id | UUID | Mã định danh vai trò | 550e8400... |
| name | VARCHAR(50) | Tên vai trò (duy nhất) | STUDENT, INSTRUCTOR |
| description | TEXT | Mô tả vai trò | Học viên - có thể học và làm bài |
| permissions | JSON | Quyền hạn | ["view_course", "submit_quiz"] |
| created_at | TIMESTAMP | Thời điểm tạo | 2025-11-01 00:00:00 |

**Các vai trò trong hệ thống:**
- **STUDENT**: Học viên - Đăng ký khóa học, học bài, làm quiz, nộp assignment
- **INSTRUCTOR**: Giảng viên - Tạo khóa học, upload bài giảng, chấm bài
- **TA**: Trợ giảng - Hỗ trợ chấm bài, trả lời thắc mắc
- **ADMIN**: Quản trị viên - Quản lý toàn bộ hệ thống

---

### Bảng 3: UserRole (Phân quyền)
**Mục đích:** Gán vai trò cho người dùng (1 user có thể có nhiều vai trò)

| Cột | Kiểu dữ liệu | Ý nghĩa | Ví dụ |
|-----|--------------|---------|-------|
| user_role_id | UUID | Mã định danh | 550e8400... |
| user_id | UUID | Người dùng nào | Link to User |
| role_id | UUID | Vai trò gì | Link to Role |
| granted_at | TIMESTAMP | Cấp quyền khi nào | 2025-11-01 10:00:00 |
| granted_by | UUID | Ai cấp quyền | Link to Admin User |
| expires_at | TIMESTAMP | Hết hạn khi nào (nullable) | 2026-11-01 (hoặc NULL) |

**Ví dụ:**
- User "Nguyễn Văn A" có role STUDENT (học bài)
- User "Nguyễn Văn A" cũng có role TA (trợ giảng cho khóa khác)

---

## DOMAIN 2: NỘI DUNG KHÓA HỌC (4 bảng)

### Bảng 4: Course (Khóa học)
**Mục đích:** Thông tin khóa học

| Cột | Kiểu dữ liệu | Ý nghĩa | Ví dụ |
|-----|--------------|---------|-------|
| course_id | UUID | Mã khóa học | 550e8400... |
| code | VARCHAR(50) | Mã code (duy nhất) | CS101, MATH201 |
| title | VARCHAR(200) | Tên khóa học | Lập trình Java cơ bản |
| description | TEXT | Mô tả chi tiết | Khóa học này sẽ giúp bạn... |
| short_description | VARCHAR(500) | Mô tả ngắn | Học Java từ cơ bản đến nâng cao |
| thumbnail_url | VARCHAR(500) | Ảnh thumbnail | https://... |
| category | VARCHAR(100) | Danh mục | Programming, Math |
| difficulty_level | VARCHAR(20) | Độ khó | BEGINNER, INTERMEDIATE, ADVANCED |
| estimated_hours | DECIMAL(5,2) | Thời lượng ước tính (giờ) | 40.50 |
| status | VARCHAR(20) | Trạng thái | DRAFT, PUBLISHED, ARCHIVED |
| published_at | TIMESTAMP | Thời điểm publish | 2025-11-01 00:00:00 |
| created_by | UUID | Người tạo (Instructor) | Link to User |
| created_at | TIMESTAMP | Thời điểm tạo | 2025-10-15 10:00:00 |
| updated_at | TIMESTAMP | Lần cập nhật gần nhất | 2025-11-20 14:00:00 |

**Trạng thái khóa học:**
- **DRAFT**: Nháp - chưa public, chỉ instructor thấy
- **PUBLISHED**: Đã xuất bản - học viên có thể đăng ký
- **ARCHIVED**: Lưu trữ - không cho đăng ký mới

---

### Bảng 5: Module (Chương/Module)
**Mục đích:** Phân chia khóa học thành các chương

| Cột | Kiểu dữ liệu | Ý nghĩa | Ví dụ |
|-----|--------------|---------|-------|
| module_id | UUID | Mã module | 550e8400... |
| course_id | UUID | Thuộc khóa học nào | Link to Course |
| title | VARCHAR(200) | Tên chương | Chương 1: Giới thiệu Java |
| description | TEXT | Mô tả | Chương này sẽ giới thiệu... |
| order_num | INT | Thứ tự (1, 2, 3...) | 1 |
| prerequisite_module_ids | UUID[] | Module cần học trước | [uuid1, uuid2] |
| estimated_duration_minutes | INT | Thời lượng ước tính (phút) | 180 |
| created_at | TIMESTAMP | Thời điểm tạo | 2025-10-15 11:00:00 |
| updated_at | TIMESTAMP | Lần cập nhật gần nhất | 2025-11-20 15:00:00 |

**Ví dụ cấu trúc:**
```
Course: "Lập trình Java"
├── Module 1: Giới thiệu Java (order_num = 1)
├── Module 2: OOP cơ bản (order_num = 2, prerequisite = Module 1)
└── Module 3: OOP nâng cao (order_num = 3, prerequisite = Module 2)
```

---

### Bảng 6: Lecture (Bài giảng)
**Mục đích:** Nội dung học tập (video, PDF, assignment...)

| Cột | Kiểu dữ liệu | Ý nghĩa | Ví dụ |
|-----|--------------|---------|-------|
| lecture_id | UUID | Mã bài giảng | 550e8400... |
| module_id | UUID | Thuộc module nào | Link to Module |
| title | VARCHAR(200) | Tiêu đề | Bài 1.1: Cài đặt JDK |
| description | TEXT | Mô tả | Video hướng dẫn cài đặt... |
| type | VARCHAR(20) | Loại bài giảng | VIDEO, PDF, ASSIGNMENT |
| content_url | VARCHAR(1024) | Đường dẫn nội dung | https://s3.../video.mp4 |
| duration_seconds | INT | Thời lượng (giây) | 1800 (30 phút) |
| order_num | INT | Thứ tự trong module | 1 |
| assignment_config | JSON | Cấu hình (nếu type=ASSIGNMENT) | {"max_points": 100, ...} |
| is_preview | BOOLEAN | Cho xem trước không đăng ký? | false |
| is_downloadable | BOOLEAN | Cho phép tải về? | true |
| created_at | TIMESTAMP | Thời điểm tạo | 2025-10-16 09:00:00 |
| updated_at | TIMESTAMP | Lần cập nhật gần nhất | 2025-11-21 10:00:00 |

**Loại bài giảng (type):**
- **VIDEO**: Video bài giảng
- **PDF**: Tài liệu PDF
- **SLIDE**: Slide bài giảng
- **AUDIO**: File âm thanh
- **TEXT**: Nội dung text
- **ASSIGNMENT**: Bài tập (⭐ MỚI - gộp Assignment vào đây)

**Ví dụ assignment_config JSON:**
```json
{
  "max_points": 100,
  "due_date": "2025-12-15T23:59:00Z",
  "submission_types": ["file", "text", "code"],
  "instructions": "Viết chương trình Java in ra Hello World...",
  "rubric": {
    "code_quality": 40,
    "functionality": 40,
    "documentation": 20
  }
}
```

---

### Bảng 7: Resource (Tài liệu đính kèm)
**Mục đích:** File đính kèm cho bài giảng (slide, source code, dataset...)

| Cột | Kiểu dữ liệu | Ý nghĩa | Ví dụ |
|-----|--------------|---------|-------|
| resource_id | UUID | Mã tài liệu | 550e8400... |
| lecture_id | UUID | Đính kèm bài giảng nào | Link to Lecture |
| title | VARCHAR(200) | Tên file | Slide bài 1.1 |
| file_url | VARCHAR(500) | Đường dẫn file | https://s3.../slide.pdf |
| file_type | VARCHAR(100) | Loại file (MIME type) | application/pdf |
| file_size_bytes | BIGINT | Kích thước (bytes) | 2048576 (2MB) |
| created_at | TIMESTAMP | Thời điểm upload | 2025-10-16 10:00:00 |

---

## DOMAIN 3: ĐÁNH GIÁ (5 bảng)

### Bảng 8: Quiz (Bài kiểm tra)
**Mục đích:** Cấu hình bài kiểm tra trắc nghiệm

| Cột | Kiểu dữ liệu | Ý nghĩa | Ví dụ |
|-----|--------------|---------|-------|
| quiz_id | UUID | Mã quiz | 550e8400... |
| course_id | UUID | Thuộc khóa học nào | Link to Course |
| title | VARCHAR(200) | Tên quiz | Kiểm tra giữa kỳ |
| description | TEXT | Mô tả | 20 câu trắc nghiệm... |
| time_limit_minutes | INT | Giới hạn thời gian (phút) | 60 |
| pass_score | DECIMAL(5,2) | Điểm đạt (%) | 70.00 |
| questions | JSON | ⭐ Danh sách câu hỏi | [{"question_id": "...", "points": 10}] |
| shuffle_questions | BOOLEAN | Xáo trộn câu hỏi? | true |
| show_correct_answers | BOOLEAN | Hiện đáp án sau làm? | true |
| is_published | BOOLEAN | Đã public? | false |
| created_by | UUID | Người tạo | Link to User (Instructor) |
| created_at | TIMESTAMP | Thời điểm tạo | 2025-11-01 00:00:00 |
| updated_at | TIMESTAMP | Lần cập nhật gần nhất | 2025-11-20 10:00:00 |

**⭐ THAY ĐỔI LỚN: questions JSON (thay vì bảng QuizQuestion riêng)**

**Cấu trúc JSON:**
```json
[
  {
    "question_id": "uuid-1",
    "points": 10,
    "order": 1
  },
  {
    "question_id": "uuid-2",
    "points": 15,
    "order": 2
  }
]
```

**Lợi ích:**
- Đơn giản hơn (không cần bảng riêng)
- Dễ thêm/xóa câu hỏi
- PostgreSQL hỗ trợ query JSON tốt

---

### Bảng 9: Question (Câu hỏi)
**Mục đích:** Ngân hàng câu hỏi (dùng chung cho nhiều quiz)

| Cột | Kiểu dữ liệu | Ý nghĩa | Ví dụ |
|-----|--------------|---------|-------|
| question_id | UUID | Mã câu hỏi | 550e8400... |
| course_id | UUID | Thuộc khóa học nào | Link to Course |
| text | TEXT | Nội dung câu hỏi | Thủ đô của Việt Nam là? |
| type | VARCHAR(20) | Loại câu hỏi | MCQ, TRUE_FALSE, ESSAY |
| difficulty | VARCHAR(20) | Độ khó | EASY, MEDIUM, HARD |
| max_points | DECIMAL(5,2) | Điểm tối đa | 10.00 |
| explanation | TEXT | Giải thích đáp án | Hà Nội là thủ đô... |
| is_active | BOOLEAN | Còn dùng không? | true |
| created_by | UUID | Người tạo | Link to User |
| created_at | TIMESTAMP | Thời điểm tạo | 2025-11-01 00:00:00 |
| updated_at | TIMESTAMP | Lần cập nhật gần nhất | 2025-11-20 11:00:00 |

**Loại câu hỏi (type):**
- **MCQ**: Multiple Choice Question (chọn 1 hoặc nhiều đáp án)
- **TRUE_FALSE**: Đúng/Sai
- **ESSAY**: Tự luận
- **SHORT_ANSWER**: Câu trả lời ngắn

---

### Bảng 10: Option (Lựa chọn)
**Mục đích:** Các đáp án cho câu hỏi trắc nghiệm

| Cột | Kiểu dữ liệu | Ý nghĩa | Ví dụ |
|-----|--------------|---------|-------|
| option_id | UUID | Mã lựa chọn | 550e8400... |
| question_id | UUID | Thuộc câu hỏi nào | Link to Question |
| option_text | TEXT | Nội dung đáp án | Hà Nội |
| is_correct | BOOLEAN | Đáp án đúng? | true |
| order_num | INT | Thứ tự hiển thị | 1 |
| feedback | TEXT | Giải thích cho đáp án | Hà Nội là thủ đô Việt Nam |

**Ví dụ câu hỏi MCQ:**
```
Question: Thủ đô của Việt Nam là?
├── Option A: Hà Nội (is_correct = true)
├── Option B: TP.HCM (is_correct = false)
├── Option C: Đà Nẵng (is_correct = false)
└── Option D: Cần Thơ (is_correct = false)
```

---

### Bảng 11: Attempt (Lần làm bài quiz)
**Mục đích:** Lưu lịch sử làm quiz của học viên

| Cột | Kiểu dữ liệu | Ý nghĩa | Ví dụ |
|-----|--------------|---------|-------|
| attempt_id | UUID | Mã lần làm bài | 550e8400... |
| quiz_id | UUID | Làm quiz nào | Link to Quiz |
| user_id | UUID | Ai làm | Link to User |
| enrollment_id | UUID | Từ enrollment nào | Link to Enrollment |
| attempt_number | INT | Lần thứ mấy | 1, 2, 3 |
| started_at | TIMESTAMP | Bắt đầu lúc | 2025-11-27 10:00:00 |
| submitted_at | TIMESTAMP | Nộp bài lúc | 2025-11-27 10:45:00 |
| time_spent_seconds | INT | Thời gian làm (giây) | 2700 (45 phút) |
| status | VARCHAR(20) | Trạng thái | IN_PROGRESS, SUBMITTED |
| answers | JSON | ⭐ Câu trả lời | [{"question_id": "...", "score": 10}] |
| total_score | DECIMAL(6,2) | Tổng điểm | 85.00 |
| max_possible_score | DECIMAL(6,2) | Điểm tối đa | 100.00 |
| percentage_score | DECIMAL(5,2) | Điểm % | 85.00 |
| graded_at | TIMESTAMP | Chấm xong lúc | 2025-11-27 11:00:00 |
| graded_by | UUID | Ai chấm | Link to User (Instructor) |

**⭐ THAY ĐỔI LỚN: answers JSON (thay vì bảng QuizSubmission riêng)**

**Cấu trúc JSON:**
```json
[
  {
    "question_id": "uuid-1",
    "answer_text": "Hà Nội",
    "selected_options": ["option-uuid-a"],
    "score": 10,
    "max_score": 10,
    "is_correct": true,
    "feedback": "Chính xác!",
    "graded_at": "2025-11-27T10:45:00Z"
  },
  {
    "question_id": "uuid-2",
    "answer_text": null,
    "selected_options": ["option-uuid-x"],
    "score": 0,
    "max_score": 15,
    "is_correct": false,
    "feedback": "Sai rồi, đáp án đúng là...",
    "graded_at": "2025-11-27T10:45:00Z"
  }
]
```

---

### Bảng 12: AssignmentSubmission (Nộp bài tập)
**Mục đích:** Lưu bài làm của học viên cho Assignment

| Cột | Kiểu dữ liệu | Ý nghĩa | Ví dụ |
|-----|--------------|---------|-------|
| submission_id | UUID | Mã bài nộp | 550e8400... |
| lecture_id | UUID | Nộp cho lecture nào (type=ASSIGNMENT) | Link to Lecture |
| user_id | UUID | Ai nộp | Link to User |
| enrollment_id | UUID | Từ enrollment nào | Link to Enrollment |
| submission_number | INT | Lần nộp thứ mấy | 1, 2 |
| submitted_at | TIMESTAMP | Thời điểm nộp | 2025-11-27 14:00:00 |
| content | TEXT | Nội dung text | Code hoặc văn bản |
| file_urls | JSON | ⭐ File đính kèm | ["https://s3.../file1.zip"] |
| code_submission | TEXT | Code (nếu là bài code) | public class Main { ... } |
| is_late | BOOLEAN | Nộp trễ? | false |
| status | VARCHAR(20) | Trạng thái | SUBMITTED, GRADED |
| score | DECIMAL(6,2) | Điểm | 85.00 |
| max_score | DECIMAL(6,2) | Điểm tối đa | 100.00 |
| feedback | TEXT | Nhận xét | Bài làm tốt, tuy nhiên... |
| graded_at | TIMESTAMP | Chấm xong lúc | 2025-11-28 10:00:00 |
| graded_by | UUID | Ai chấm | Link to User (Instructor) |

**Ví dụ file_urls JSON:**
```json
[
  "https://s3.amazonaws.com/blearning/submissions/user123/assignment1.zip",
  "https://s3.amazonaws.com/blearning/submissions/user123/report.pdf"
]
```

---

## DOMAIN 4: ĐĂNG KÝ & TIẾN ĐỘ (2 bảng)

### Bảng 13: Enrollment (Đăng ký)
**Mục đích:** Đăng ký khóa học (self-paced hoặc theo lớp)

| Cột | Kiểu dữ liệu | Ý nghĩa | Ví dụ |
|-----|--------------|---------|-------|
| enrollment_id | UUID | Mã đăng ký | 550e8400... |
| user_id | UUID | Ai đăng ký | Link to User |
| course_id | UUID | Khóa học nào | Link to Course |
| class_id | UUID | ⭐ Lớp nào (nullable) | Link to Class hoặc NULL |
| role | VARCHAR(20) | Vai trò | STUDENT, INSTRUCTOR, TA |
| status | VARCHAR(20) | Trạng thái | ACTIVE, COMPLETED, DROPPED |
| enrolled_at | TIMESTAMP | Đăng ký lúc | 2025-11-01 09:00:00 |
| completed_at | TIMESTAMP | Hoàn thành lúc | 2025-12-15 16:00:00 |
| final_grade | DECIMAL(5,2) | Điểm cuối khóa | 87.50 |
| completion_percentage | DECIMAL(5,2) | % hoàn thành | 100.00 |
| last_accessed_at | TIMESTAMP | Truy cập gần nhất | 2025-11-27 10:00:00 |

**⭐ THAY ĐỔI LỚN: Gộp CourseEnrollment + ClassEnrollment**

**Cách hoạt động:**
- **Self-paced learning**: class_id = NULL
- **Blended learning**: class_id = UUID của lớp

**Ví dụ:**
```sql
-- Student tự học
INSERT INTO Enrollment (user_id, course_id, class_id, role)
VALUES ('user-uuid', 'course-uuid', NULL, 'STUDENT');

-- Student học theo lớp
INSERT INTO Enrollment (user_id, course_id, class_id, role)
VALUES ('user-uuid', 'course-uuid', 'class-uuid', 'STUDENT');
```

---

### Bảng 14: Progress (Tiến độ)
**Mục đích:** Theo dõi tiến độ học tập (chỉ ở module level)

| Cột | Kiểu dữ liệu | Ý nghĩa | Ví dụ |
|-----|--------------|---------|-------|
| progress_id | UUID | Mã tiến độ | 550e8400... |
| user_id | UUID | Của ai | Link to User |
| course_id | UUID | Khóa học nào | Link to Course |
| module_id | UUID | Module nào | Link to Module |
| status | VARCHAR(20) | Trạng thái | NOT_STARTED, IN_PROGRESS, COMPLETED |
| started_at | TIMESTAMP | Bắt đầu lúc | 2025-11-01 10:00:00 |
| completed_at | TIMESTAMP | Hoàn thành lúc | 2025-11-05 16:00:00 |
| lecture_progress | JSON | ⭐ Tiến độ lecture (optional) | {"lecture-uuid": {"percent": 80}} |

**⭐ THAY ĐỔI: Chỉ tracking ở module level**

**Lý do:**
- Giảm số lượng records (không lưu mỗi lecture)
- Lecture progress có thể lưu ở frontend (localStorage)
- Hoặc lưu trong lecture_progress JSON nếu cần

**Ví dụ lecture_progress JSON:**
```json
{
  "lecture-uuid-1": {
    "status": "COMPLETED",
    "percent": 100,
    "last_position_seconds": 0
  },
  "lecture-uuid-2": {
    "status": "IN_PROGRESS",
    "percent": 45,
    "last_position_seconds": 810
  }
}
```

---

## DOMAIN 5: LỚP HỌC & CHỨNG CHỈ (2 bảng)

### Bảng 15: Class (Lớp học)
**Mục đích:** Quản lý lớp học (blended learning)

| Cột | Kiểu dữ liệu | Ý nghĩa | Ví dụ |
|-----|--------------|---------|-------|
| class_id | UUID | Mã lớp | 550e8400... |
| course_id | UUID | Dựa trên khóa học nào | Link to Course |
| instructor_id | UUID | Giảng viên nào | Link to User |
| name | VARCHAR(100) | Tên lớp | Java K63 - Sáng T2-T4-T6 |
| start_date | DATE | Ngày bắt đầu | 2025-12-01 |
| end_date | DATE | Ngày kết thúc | 2026-02-28 |
| status | VARCHAR(20) | Trạng thái | SCHEDULED, ONGOING, COMPLETED |
| max_students | INT | Sĩ số tối đa | 30 |
| location | VARCHAR(200) | Địa điểm | Phòng 101, Tòa A |
| schedules | JSON | ⭐ Lịch học + điểm danh | [{...}] |
| created_at | TIMESTAMP | Thời điểm tạo | 2025-11-01 00:00:00 |
| updated_at | TIMESTAMP | Lần cập nhật gần nhất | 2025-11-20 10:00:00 |

**⭐ THAY ĐỔI LỚN: schedules JSON (gộp Schedule + Attendance)**

**Cấu trúc JSON:**
```json
[
  {
    "session_id": "uuid-session-1",
    "date": "2025-12-01",
    "start_time": "09:00",
    "end_time": "11:00",
    "topic": "Chương 1: Giới thiệu Java",
    "location": "Phòng 101",
    "type": "IN_PERSON",
    "meeting_url": null,
    "attendances": [
      {
        "user_id": "student-uuid-1",
        "status": "PRESENT",
        "check_in_time": "2025-12-01T09:05:00Z",
        "notes": null
      },
      {
        "user_id": "student-uuid-2",
        "status": "LATE",
        "check_in_time": "2025-12-01T09:15:00Z",
        "notes": "Đi trễ 15 phút"
      },
      {
        "user_id": "student-uuid-3",
        "status": "ABSENT",
        "check_in_time": null,
        "notes": "Xin phép"
      }
    ]
  }
]
```

**Loại buổi học (type):**
- **IN_PERSON**: Học trực tiếp tại lớp
- **ONLINE**: Học online qua video call
- **HYBRID**: Kết hợp (vừa có lớp vừa có online)

---

### Bảng 16: Certificate (Chứng chỉ)
**Mục đích:** Chứng chỉ hoàn thành khóa học

| Cột | Kiểu dữ liệu | Ý nghĩa | Ví dụ |
|-----|--------------|---------|-------|
| certificate_id | UUID | Mã chứng chỉ | 550e8400... |
| user_id | UUID | Cấp cho ai | Link to User |
| course_id | UUID | Khóa học nào | Link to Course |
| certificate_code | VARCHAR(50) | Mã chứng chỉ công khai | BL-2025-000001 |
| verification_code | VARCHAR(100) | Mã xác thực bí mật | a1b2c3d4e5f6... (hash) |
| issue_date | DATE | Ngày cấp | 2025-12-15 |
| final_grade | DECIMAL(5,2) | Điểm cuối khóa | 87.50 |
| pdf_url | VARCHAR(500) | Link PDF chứng chỉ | https://s3.../cert.pdf |
| status | VARCHAR(20) | Trạng thái | ACTIVE, REVOKED |
| created_at | TIMESTAMP | Thời điểm tạo | 2025-12-15 16:00:00 |

**⭐ THAY ĐỔI: Đơn giản hóa từ 3 bảng xuống 1 bảng**

**Đã bỏ:**
- CertificateTemplate → Dùng template file (.html, .docx)
- CertificateVerification → Verify trực tiếp từ bảng Certificate

**Cách verify:**
```
1. User nhập certificate_code (BL-2025-000001)
2. System tìm trong bảng Certificate
3. Nếu tìm thấy và status = ACTIVE → Valid
4. Hiển thị thông tin: Tên người nhận, Khóa học, Điểm, Ngày cấp
```

---

## PHỤ LỤC

### A. So sánh với v2.0 (31 bảng)

| Chức năng | v2.0 (31 bảng) | Core (16 bảng) | Thay đổi |
|-----------|----------------|----------------|----------|
| Quiz Questions | 2 bảng (Quiz + QuizQuestion) | 1 bảng (Quiz.questions JSON) | Gộp |
| Quiz Answers | 2 bảng (Attempt + QuizSubmission) | 1 bảng (Attempt.answers JSON) | Gộp |
| Assignment | 2 bảng (Assignment + AssignmentSubmission) | 1 bảng (Lecture + AssignmentSubmission) | Gộp vào Lecture |
| Enrollment | 2 bảng (CourseEnrollment + ClassEnrollment) | 1 bảng (Enrollment) | Gộp |
| Schedule | 2 bảng (Schedule + Attendance) | 1 bảng (Class.schedules JSON) | Gộp |
| Certificate | 3 bảng (Template + Certificate + Verification) | 1 bảng (Certificate) | Đơn giản hóa |
| Notification | 3 bảng | ❌ Bỏ | Dùng email service |
| Activity Log | 1 bảng | ❌ Bỏ | Dùng app logging |
| File | 1 bảng | ❌ Bỏ | Dùng cloud storage |
| System Settings | 1 bảng | ❌ Bỏ | Dùng config files |
| GradeBook | 1 bảng | ❌ Bỏ | Tính động |

### B. Ưu điểm của Core (16 bảng)

1. **Dễ hiểu hơn**: Giảm 48% số bảng
2. **Query đơn giản hơn**: Ít JOIN hơn
3. **Maintainability cao hơn**: Ít migration
4. **Performance tốt**: PostgreSQL query JSON rất nhanh với GIN index
5. **Flexible**: JSON dễ thêm field mới không cần ALTER TABLE

### C. Nhược điểm và cách giải quyết

| Nhược điểm | Giải pháp |
|------------|-----------|
| Mất normalization | PostgreSQL hỗ trợ JSON tốt, có GIN index |
| Không có FK cho JSON | Validate ở application layer + triggers |
| Khó query JSON | Dùng jsonb operators (@>, ->>, #>, etc.) |
| Mất audit log | Dùng application logging (Winston, Logtail) |

---

**KẾT THÚC TÀI LIỆU GIẢI THÍCH**
```

---

## PHASE 2: IMPLEMENTATION

### Bước 5: Tạo SQL Scripts

#### 5.1. Folder Structure
```
98-B-Learing-Core/sql/
├── 01-schema.sql          # CREATE TABLE với comments tiếng Việt
├── 02-indexes.sql         # CREATE INDEX
├── 03-constraints.sql     # ALTER TABLE ADD CONSTRAINT
└── 04-seed-data.sql       # INSERT sample data
```

**KHÔNG CẦN:**
- triggers.sql (không dùng triggers)
- views.sql (không dùng views)
- drop-all.sql (không cần cleanup script)

---

#### 5.2. 01-schema.sql

**Yêu cầu:**
- CREATE TABLE cho tất cả 16 bảng
- Có COMMENT ON TABLE và COMMENT ON COLUMN bằng tiếng Việt
- UUID primary keys
- CHECK constraints inline
- DEFAULT values

**Ví dụ structure:**

```sql
-- ============================================
-- B-LEARNING CORE DATABASE SCHEMA
-- Version: 1.0 (Core - 16 tables)
-- Database: PostgreSQL 14+
-- Created: 2025-11-27
-- ============================================

-- Extensions
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pg_trgm";
CREATE EXTENSION IF NOT EXISTS "btree_gin";

-- ============================================
-- DOMAIN 1: USER MANAGEMENT (3 bảng)
-- ============================================

CREATE TABLE "User" (
  user_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  email VARCHAR(255) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  first_name VARCHAR(100) NOT NULL,
  last_name VARCHAR(100) NOT NULL,
  avatar_url VARCHAR(500),
  phone VARCHAR(20),
  account_status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
  preferences JSON DEFAULT '{}',
  email_verified_at TIMESTAMP,
  last_login_at TIMESTAMP,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT chk_user_account_status CHECK (account_status IN (
    'PENDING_VERIFICATION', 'ACTIVE', 'SUSPENDED', 'DELETED'
  ))
);

COMMENT ON TABLE "User" IS 'Bảng người dùng - lưu thông tin tài khoản';
COMMENT ON COLUMN "User".user_id IS 'Mã định danh duy nhất của người dùng';
COMMENT ON COLUMN "User".email IS 'Email đăng nhập (duy nhất)';
COMMENT ON COLUMN "User".password_hash IS 'Mật khẩu đã mã hóa (bcrypt)';
COMMENT ON COLUMN "User".first_name IS 'Tên';
COMMENT ON COLUMN "User".last_name IS 'Họ và tên đệm';
COMMENT ON COLUMN "User".account_status IS 'Trạng thái tài khoản: PENDING_VERIFICATION, ACTIVE, SUSPENDED, DELETED';
COMMENT ON COLUMN "User".preferences IS 'Tùy chọn người dùng (JSON): notifications, locale, timezone';

-- ... (Tiếp tục cho 15 bảng còn lại)
```

---

#### 5.3. 02-indexes.sql

**Yêu cầu:**
- Indexes cho tất cả FK
- Performance indexes cho các trường thường query
- GIN indexes cho JSON fields
- Full-text search indexes

**Ví dụ:**

```sql
-- ============================================
-- INDEXES FOR PERFORMANCE
-- ============================================

-- User indexes
CREATE INDEX idx_user_email ON "User"(email);
CREATE INDEX idx_user_status ON "User"(account_status);
CREATE INDEX idx_user_created ON "User"(created_at DESC);

-- Course indexes
CREATE INDEX idx_course_code ON "Course"(code);
CREATE INDEX idx_course_status ON "Course"(status);
CREATE INDEX idx_course_category ON "Course"(category);
CREATE INDEX idx_course_published ON "Course"(published_at DESC)
  WHERE status = 'PUBLISHED';

-- GIN indexes for JSON
CREATE INDEX idx_quiz_questions_gin ON "Quiz" USING GIN (questions);
CREATE INDEX idx_attempt_answers_gin ON "Attempt" USING GIN (answers);
CREATE INDEX idx_class_schedules_gin ON "Class" USING GIN (schedules);
CREATE INDEX idx_user_preferences_gin ON "User" USING GIN (preferences);

-- Full-text search
CREATE INDEX idx_course_search ON "Course" USING GIN(
  to_tsvector('english', COALESCE(title, '') || ' ' || COALESCE(description, ''))
);

-- ... (Tiếp tục cho tất cả indexes)
```

---

#### 5.4. 03-constraints.sql

**Yêu cầu:**
- Foreign Key constraints với CASCADE behaviors
- CHECK constraints phức tạp (nếu không inline được)
- UNIQUE constraints phức tạp

**Ví dụ:**

```sql
-- ============================================
-- FOREIGN KEY CONSTRAINTS
-- ============================================

-- UserRole
ALTER TABLE "UserRole"
  ADD CONSTRAINT fk_userrole_user
  FOREIGN KEY (user_id) REFERENCES "User"(user_id) ON DELETE CASCADE;

ALTER TABLE "UserRole"
  ADD CONSTRAINT fk_userrole_role
  FOREIGN KEY (role_id) REFERENCES "Role"(role_id) ON DELETE CASCADE;

-- Module
ALTER TABLE "Module"
  ADD CONSTRAINT fk_module_course
  FOREIGN KEY (course_id) REFERENCES "Course"(course_id) ON DELETE CASCADE;

-- ... (Tiếp tục cho tất cả FK)

-- ============================================
-- CHECK CONSTRAINTS (nếu phức tạp)
-- ============================================

-- Enrollment: class_id nullable cho self-paced
ALTER TABLE "Enrollment"
  ADD CONSTRAINT chk_enrollment_unique
  UNIQUE (user_id, course_id, COALESCE(class_id, '00000000-0000-0000-0000-000000000000'::UUID));

-- ... (Tiếp tục)
```

---

#### 5.5. 04-seed-data.sql

**Yêu cầu:**
- INSERT sample data để test
- 4 roles (STUDENT, INSTRUCTOR, TA, ADMIN)
- 3 sample users (admin, instructor, student)
- 1 sample course với modules, lectures
- 1 sample quiz với questions

**Ví dụ:**

```sql
-- ============================================
-- SEED DATA FOR TESTING
-- ============================================

-- Roles
INSERT INTO "Role" (name, description) VALUES
('STUDENT', 'Học viên - Có thể học và làm bài'),
('INSTRUCTOR', 'Giảng viên - Có thể tạo khóa học và chấm bài'),
('TA', 'Trợ giảng - Hỗ trợ chấm bài'),
('ADMIN', 'Quản trị viên - Quản lý toàn bộ hệ thống')
ON CONFLICT (name) DO NOTHING;

-- Sample users
-- Password: 'password123' (bcrypt hashed)
INSERT INTO "User" (email, password_hash, first_name, last_name, account_status, email_verified_at) VALUES
('admin@blearning.edu', '$2a$10$rZ8pqBJKB5v7J0YdN4YQy.vQZJ9e1Q7XJ8mK5kJ9N3J8YQy', 'Admin', 'User', 'ACTIVE', CURRENT_TIMESTAMP),
('instructor@blearning.edu', '$2a$10$rZ8pqBJKB5v7J0YdN4YQy.vQZJ9e1Q7XJ8mK5kJ9N3J8YQy', 'Nguyễn', 'Văn Kiệt', 'ACTIVE', CURRENT_TIMESTAMP),
('student@blearning.edu', '$2a$10$rZ8pqBJKB5v7J0YdN4YQy.vQZJ9e1Q7XJ8mK5kJ9N3J8YQy', 'Trần', 'Thị Mai', 'ACTIVE', CURRENT_TIMESTAMP)
ON CONFLICT (email) DO NOTHING;

-- Assign roles
INSERT INTO "UserRole" (user_id, role_id)
SELECT u.user_id, r.role_id
FROM "User" u, "Role" r
WHERE (u.email = 'admin@blearning.edu' AND r.name = 'ADMIN')
   OR (u.email = 'instructor@blearning.edu' AND r.name = 'INSTRUCTOR')
   OR (u.email = 'student@blearning.edu' AND r.name = 'STUDENT')
ON CONFLICT DO NOTHING;

-- Sample course
INSERT INTO "Course" (code, title, description, difficulty_level, status, created_by) VALUES
('JAVA101', 'Lập trình Java cơ bản', 'Khóa học Java từ cơ bản đến nâng cao', 'BEGINNER', 'PUBLISHED',
  (SELECT user_id FROM "User" WHERE email = 'instructor@blearning.edu'));

-- ... (Tiếp tục với sample data)
```

---

### Bước 6: Viết documentation files

Tạo tất cả file .md theo cấu trúc đã nêu ở Bước 4:
- BFD-SPEC.md
- ERD-SPEC.md
- DATABASE-SCHEMA.md
- FUNCTIONAL-REQUIREMENTS.md
- API-ENDPOINTS.md
- TABLES-EXPLANATION-VI.md ⭐ MỚI

---

### Bước 7: Tạo UML files trong StarUML

#### 7.1. BFD.mdj
- Vẽ Business Function Diagram theo Bước 2
- Đơn giản hơn v2.0 (bỏ Notification, File upload use cases)
- Export as BFD.mdj

#### 7.2. ERD.mdj
- Vẽ Entity Relationship Diagram theo Bước 3
- 16 bảng, 5 domains
- 23 relationships
- Export as ERD.mdj

---

## PHASE 3: VALIDATION

### Bước 8: Validation Checklist

```markdown
### ERD Validation
- [ ] All 16 tables defined
- [ ] All PKs are UUID
- [ ] All FKs correctly reference PKs
- [ ] All data types specified
- [ ] All UNIQUE constraints defined
- [ ] All CHECK constraints defined
- [ ] All ON DELETE behaviors specified
- [ ] All relationships correctly drawn in ERD.mdj
- [ ] JSON fields have proper structure

### SQL Validation
- [ ] 01-schema.sql creates all tables without errors
- [ ] 02-indexes.sql creates all indexes without errors
- [ ] 03-constraints.sql adds all constraints without errors
- [ ] 04-seed-data.sql inserts sample data without errors
- [ ] All SQL follows PostgreSQL syntax
- [ ] All comments in Vietnamese

### Documentation Validation
- [ ] BFD-SPEC.md complete
- [ ] ERD-SPEC.md complete with 16 tables
- [ ] DATABASE-SCHEMA.md has full DDL
- [ ] FUNCTIONAL-REQUIREMENTS.md lists all FRs
- [ ] API-ENDPOINTS.md lists all endpoints
- [ ] TABLES-EXPLANATION-VI.md has all 16 tables explained
- [ ] All .md files use proper markdown formatting
- [ ] All diagrams (BFD.mdj, ERD.mdj) included

### Functional Validation
- [ ] All core functions covered
- [ ] JSON structures documented
- [ ] External services integration documented
- [ ] Simplification benefits documented
- [ ] Migration plan from v2.0 available (if needed)
```

---

### Bước 9: Tạo README

Tạo file `98-B-Learing-Core/README.md`:

```markdown
# B-Learning Core Database Design (v1.0)

## Overview
Database tinh giản cho B-Learning (Blended Learning) system với **16 bảng** (giảm 48% từ v2.0), tập trung vào chức năng core, dễ hiểu và dễ bảo trì.

## Key Changes from v2.0
- ✅ Giảm từ 31 bảng → 16 bảng (-48%)
- ✅ Gộp QuizQuestion → Quiz.questions (JSON)
- ✅ Gộp QuizSubmission → Attempt.answers (JSON)
- ✅ Gộp Assignment → Lecture (type='ASSIGNMENT')
- ✅ Gộp Enrollment (Course + Class)
- ✅ Gộp Schedule/Attendance → Class.schedules (JSON)
- ✅ Đơn giản hóa Certificate (3 bảng → 1 bảng)
- ❌ Bỏ Notification → Email service
- ❌ Bỏ ActivityLog → Application logging
- ❌ Bỏ File → Cloud storage
- ❌ Bỏ GradeBook → Dynamic calculation

## Architecture
- **Database**: PostgreSQL 14+
- **Total Tables**: 16
- **Domains**: 5 (User, Course, Assessment, Enrollment, Class)
- **Primary Keys**: UUID
- **JSON Support**: PostgreSQL jsonb với GIN indexes

## Documentation
- `/documents/BFD-SPEC.md` - Business functions
- `/documents/ERD-SPEC.md` - Entity relationships
- `/documents/DATABASE-SCHEMA.md` - Full DDL
- `/documents/FUNCTIONAL-REQUIREMENTS.md` - Requirements
- `/documents/API-ENDPOINTS.md` - API design
- `/documents/TABLES-EXPLANATION-VI.md` - ⭐ Giải thích tiếng Việt

## UML Diagrams
- `/BFD.mdj` - Business Function Diagram (StarUML)
- `/ERD.mdj` - Entity Relationship Diagram (StarUML)

## SQL Scripts
- `/sql/01-schema.sql` - Create tables
- `/sql/02-indexes.sql` - Performance indexes
- `/sql/03-constraints.sql` - Constraints
- `/sql/04-seed-data.sql` - Sample data

## Quick Start
```bash
# Create database
createdb b_learning_core

# Run schema
psql b_learning_core < sql/01-schema.sql
psql b_learning_core < sql/02-indexes.sql
psql b_learning_core < sql/03-constraints.sql
psql b_learning_core < sql/04-seed-data.sql
```

## Features
✅ User management with RBAC
✅ Course with modules and lectures
✅ Quiz with JSON questions
✅ Assignment (as Lecture type)
✅ Progress tracking (module level)
✅ Blended learning (class + schedules)
✅ Certificate issuance
✅ Simplified enrollment (self-paced + class)

## External Services
- **Email**: SendGrid / AWS SES (notifications)
- **File Storage**: AWS S3 / Google Cloud Storage
- **Logging**: Winston / Logtail
- **Config**: Environment variables (.env)

## Comparison

| Metric | v2.0 (31 tables) | Core (16 tables) | Improvement |
|--------|------------------|------------------|-------------|
| Number of tables | 31 | 16 | -48% |
| FK relationships | ~45 | ~23 | -49% |
| JOIN depth | 4-5 | 2-3 | -40% |
| Onboarding time | 2-3 weeks | 3-5 days | -70% |

## Author
Nguyễn Văn Kiệt - CNTT1-K63
Created: 2025-11-27
```

---

## Deliverables Summary

### 📁 Folder Structure
```
98-B-Learing-Core/
├── README.md
├── req-2.md (this file)
│
├── documents/
│   ├── BFD-SPEC.md
│   ├── ERD-SPEC.md
│   ├── DATABASE-SCHEMA.md
│   ├── FUNCTIONAL-REQUIREMENTS.md
│   ├── API-ENDPOINTS.md
│   └── TABLES-EXPLANATION-VI.md ⭐ MỚI
│
├── sql/
│   ├── 01-schema.sql
│   ├── 02-indexes.sql
│   ├── 03-constraints.sql
│   └── 04-seed-data.sql
│
├── BFD.mdj (StarUML)
└── ERD.mdj (StarUML)
```

---

## Tiêu chí đánh giá

### ✅ Thiết kế tốt phải có:
1. **Đầy đủ**: 16 bảng theo đặc tả
2. **Chính xác**: PK, FK, data types, constraints đúng
3. **Đơn giản**: Dễ hiểu hơn v2.0
4. **Có thể triển khai**: SQL chạy được không lỗi
5. **Rõ ràng**: Diagram dễ đọc, comments tiếng Việt
6. **Best practices**: Indexes, constraints hợp lý
7. **JSON hợp lý**: Dùng JSON đúng chỗ (questions, answers, schedules)
8. **Giải thích đầy đủ**: TABLES-EXPLANATION-VI.md chi tiết

### ❌ Tránh các lỗi:
1. Thiếu bảng hoặc field
2. PK/FK sai
3. JSON structure không hợp lý
4. Thiếu indexes cho JSON (GIN)
5. Comments không rõ ràng
6. SQL syntax error
7. Thiếu seed data

---

## Timeline

```
Tuần 1: Design & Documentation
├── Day 1-2: ERD design (StarUML)
├── Day 3: BFD design (StarUML)
└── Day 4-5: Write documentation

Tuần 2: SQL Implementation
├── Day 1-2: 01-schema.sql
├── Day 3: 02-indexes.sql + 03-constraints.sql
├── Day 4: 04-seed-data.sql
└── Day 5: Testing & validation

Tuần 3: Review & Polish
├── Day 1-2: TABLES-EXPLANATION-VI.md
├── Day 3: Review tất cả documents
├── Day 4: Test SQL scripts
└── Day 5: Final review & submit
```

**Total: 3 tuần (15 ngày làm việc)**

---

## Notes
- Tham khảo `99-B-Learing/` cho v2.0 design
- Đọc `99-B-Learing/documents/DATABASE-SIMPLIFICATION-PROPOSAL.md` để hiểu lý do tinh giản
- Focus vào simplicity và maintainability
- JSON phải có structure rõ ràng và documented
- Comments tiếng Việt để dễ hiểu

---

**END OF PLAN TASK**
