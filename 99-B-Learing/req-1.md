# Plan Task: Thiết Kế Lại Database Hệ Thống B-Learning

## Mục tiêu
Thiết kế lại hoàn toàn database cho hệ thống B-Learning (Blended Learning) với ERD, BFD và đặc tả đầy đủ. Hệ thống mới loại bỏ forum/discussion, tập trung vào Assignment-based learning với chứng chỉ tự động.

## Yêu cầu đầu ra

### 1. Tài liệu đặc tả hệ thống (documents/)
- **BFD-SPEC.md** - Business Function Diagram specification
- **ERD-SPEC.md** - Entity Relationship Diagram specification
- **DATABASE-SCHEMA.md** - Chi tiết schema với DDL đầy đủ
- **FUNCTIONAL-REQUIREMENTS.md** - Yêu cầu chức năng chi tiết
- **API-ENDPOINTS.md** - API endpoints và business logic

### 2. Sơ đồ UML (StarUML format)
- **BFD.mdj** - Business Function Diagram
- **ERD.mdj** - Entity Relationship Diagram với đầy đủ PK, FK, attributes

### 3. SQL Scripts (sql/)
- **01-schema.sql** - Create tables, constraints
- **02-indexes.sql** - Performance indexes
- **03-triggers.sql** - Business logic triggers
- **04-seed-data.sql** - Sample data for testing

---

## Phạm vi hệ thống

### ✅ Chức năng GIỮ LẠI
1. **Course Management** - Quản lý khóa học
2. **Module & Lecture** - Nội dung học theo cấu trúc
3. **Quiz & Assessment** - Đánh giá kiến thức
4. **Progress Tracking** - Theo dõi tiến độ học tập
5. **Class Management** - Quản lý lớp học (blended)
6. **Certificate** - Cấp chứng chỉ tự động
7. **User & Role Management** - Quản lý người dùng và phân quyền
8. **Enrollment** - Đăng ký khóa học/lớp học
9. **Attendance** - Điểm danh (cho học trực tiếp)
10. **Assignment System** - Hệ thống bài tập (MỚI - thay thế Submission)

### ❌ Chức năng BỎ ĐI
1. **Thread/Discussion** - Forum thảo luận
2. **Post/Reply** - Bài viết, trả lời
3. **Vote/Like** - Tương tác social
4. **Off-topic Discussion** - Thảo luận ngoài lề
5. **Post editing/history** - Lịch sử chỉnh sửa bài viết

### 🆕 Chức năng MỚI
1. **Assignment System** - Hệ thống bài tập đầy đủ
   - Assignment types: Essay, Code, File Upload, Problem Set
   - Submission với versioning
   - Grading với rubric
   - Late submission policy
   - Peer review (optional)

2. **Notification System** - Thông báo đa kênh
   - Email, Push, In-app
   - User preferences

3. **Certificate Advanced** - Chứng chỉ nâng cao
   - Template management
   - QR verification
   - Blockchain-ready (optional)

---

## Các bước thực hiện

## PHASE 1: PHÂN TÍCH & THIẾT KẾ (Analysis & Design)

### Bước 1: Phân tích hệ thống cũ

#### 1.1. Đọc tài liệu đánh giá
- Đọc file `documents/DATABASE-DESIGN-EVALUATION.md`
- Đọc file `documents/EXECUTIVE-SUMMARY.md`
- Tóm tắt:
  - 5 vấn đề nghiêm trọng cần fix
  - 8 vấn đề cần cải thiện
  - Các bảng thiếu

#### 1.2. Phân tích ERD hiện tại
- Xem ERD trong `DTPM_B-Learning.pdf` page 12
- Liệt kê 21 bảng hiện tại
- Xác định:
  - Bảng nào giữ lại (Course, Module, Lecture, Quiz, etc.)
  - Bảng nào bỏ đi (Thread, Post, PostVote, etc.)
  - Bảng nào cần redesign (Progress, Attempt, Submission → Assignment)

#### 1.3. Xác định yêu cầu mới
- **Assignment System**: Thay thế Submission
  - Assignment types: Essay, Code, File, Problem Set
  - Versioning và late submission
  - Rubric-based grading
  - Auto-grading cho một số loại

- **Certificate System**: Nâng cấp từ thiết kế đơn giản
  - Certificate templates
  - QR verification
  - Revocation support

- **Notification System**: Mới hoàn toàn
  - Multi-channel (Email, Push, SMS)
  - User preferences
  - Event-driven

---

### Bước 2: Thiết kế Business Function Diagram (BFD)

#### 2.1. Xác định các actors
1. **Student** (Học viên)
2. **Instructor** (Giảng viên)
3. **Teaching Assistant (TA)**
4. **Admin** (Quản trị viên)
5. **System** (Hệ thống tự động)

#### 2.2. Xác định các chức năng chính

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
   - Notification preferences
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
   - Preview settings
```

##### C. Assessment Management (Instructor)
```
1. Quiz Management
   - Create quiz
   - Question bank
   - Auto-grading setup
   - Time limit & attempts

2. Assignment Management (MỚI)
   - Create assignment
   - Set deadline & late policy
   - Create rubric
   - Assign to course/class
```

##### D. Learning (Student)
```
1. Course Enrollment
   - Browse courses
   - Enroll in course
   - View enrolled courses

2. Learning Activities
   - Watch lectures
   - Take quizzes
   - Submit assignments
   - Track progress

3. Results & Certificate
   - View grades
   - Download certificate
```

##### E. Class Management (Blended Learning)
```
1. Class Setup (Instructor/Admin)
   - Create class
   - Set schedule
   - Assign instructor
   - Link to course

2. Class Operations
   - Attendance tracking
   - In-person sessions
   - Video conference (optional)
```

##### F. Grading (Instructor/TA)
```
1. Assignment Grading
   - View submissions
   - Grade with rubric
   - Provide feedback
   - Handle late submissions

2. Quiz Review
   - Manual grading (essay questions)
   - Review flags
   - Grade adjustments
```

##### G. Certificate Management (System/Admin)
```
1. Auto-issuance
   - Check completion criteria
   - Generate certificate
   - Send notification

2. Verification
   - Public verification page
   - QR code scan
   - Revocation check
```

##### H. Notification System (System)
```
1. Event Triggers
   - Assignment due soon
   - Grade published
   - Certificate issued
   - Course update

2. Delivery
   - Email
   - Push notification
   - In-app notification
```

#### 2.3. Vẽ BFD trong StarUML

**Tạo file BFD.mdj** với:

##### Cấu trúc diagram:
```
Actor          →    Use Cases                →    Sub-functions
[Student]      →    [Enroll in Course]       →    [Browse, Search, Enroll]
               →    [Learn Course]           →    [Watch Lecture, Take Quiz, Submit Assignment]
               →    [Track Progress]         →    [View Progress, View Grades]

[Instructor]   →    [Manage Course]          →    [Create, Update, Publish]
               →    [Create Content]         →    [Upload Lecture, Create Quiz, Create Assignment]
               →    [Grade Students]         →    [Grade Assignment, Review Quiz]

[Admin]        →    [Manage Users]           →    [Create User, Assign Role]
               →    [Manage System]          →    [System Settings, Reports]

[System]       →    [Auto Grade Quiz]        →    [Calculate Score, Update Progress]
               →    [Issue Certificate]      →    [Check Criteria, Generate PDF, Send Email]
               →    [Send Notification]      →    [Trigger Event, Deliver Message]
```

##### Relationships:
- **Association**: Actor → Use Case
- **Include**: Required functionality
- **Extend**: Optional functionality
- **Generalization**: Role hierarchy (User ← Student, Instructor, Admin)

##### StarUML Tips:
1. Create **Use Case Diagram**
2. Add **Actors** (Student, Instructor, Admin, System)
3. Add **Use Cases** (oval shapes)
4. Draw **Associations** (solid lines)
5. Use **<<include>>** and **<<extend>>** stereotypes
6. Group related use cases with **Packages**

---

### Bước 3: Thiết kế Entity Relationship Diagram (ERD)

#### 3.1. Xác định các thực thể (Entities)

##### CORE ENTITIES (13 bảng)

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
  - timezone (VARCHAR 50, DEFAULT 'UTC')
  - locale (VARCHAR 10, DEFAULT 'vi')
  - account_status (VARCHAR 30, DEFAULT 'ACTIVE')
    - Values: PENDING_VERIFICATION, ACTIVE, SUSPENDED, DELETED
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
    - Values: STUDENT, INSTRUCTOR, TA, ADMIN, MODERATOR
  - description (TEXT)
  - permissions (JSON)
  - is_system_role (BOOLEAN, DEFAULT TRUE)
  - created_at (TIMESTAMP)
```

**3. UserRole** - Quan hệ User-Role (Many-to-Many)
```
PK: user_role_id (UUID)
FK: user_id → User.user_id
FK: role_id → Role.role_id
Attributes:
  - granted_at (TIMESTAMP, DEFAULT CURRENT_TIMESTAMP)
  - granted_by (UUID, FK → User.user_id)
  - expires_at (TIMESTAMP)

UNIQUE(user_id, role_id)
```

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
    - Values: BEGINNER, INTERMEDIATE, ADVANCED, EXPERT
  - estimated_hours (DECIMAL 5,2)
  - status (VARCHAR 20, DEFAULT 'DRAFT')
    - Values: DRAFT, PUBLISHED, ARCHIVED
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

**6. Lecture** - Bài giảng
```
PK: lecture_id (UUID)
FK: module_id → Module.module_id (CASCADE)
Attributes:
  - title (VARCHAR 200, NOT NULL)
  - description (TEXT)
  - type (VARCHAR 20, NOT NULL)
    - Values: VIDEO, PDF, SLIDE, AUDIO, TEXT
  - content_url (VARCHAR 1024)
  - duration_seconds (INT)
  - order_num (INT, NOT NULL)
  - is_preview (BOOLEAN, DEFAULT FALSE)
  - is_downloadable (BOOLEAN, DEFAULT TRUE)
  - transcript (TEXT)
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

**8. Quiz** - Bài kiểm tra
```
PK: quiz_id (UUID)
FK: course_id → Course.course_id (CASCADE)
Attributes:
  - title (VARCHAR 200, NOT NULL)
  - description (TEXT)
  - time_limit_minutes (INT)
  - attempt_limit (INT)
  - pass_score (DECIMAL 5,2)
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
    - Values: MCQ, TRUE_FALSE, ESSAY, SHORT_ANSWER, CODE
  - difficulty (VARCHAR 20, DEFAULT 'MEDIUM')
    - Values: EASY, MEDIUM, HARD
  - max_points (DECIMAL 5,2, NOT NULL, DEFAULT 1.00)
  - explanation (TEXT)
  - is_active (BOOLEAN, DEFAULT TRUE)
  - created_by (UUID, FK → User.user_id)
  - created_at (TIMESTAMP)
  - updated_at (TIMESTAMP)
```

**10. Option** - Lựa chọn (cho MCQ, TRUE_FALSE)
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

**11. QuizQuestion** - Liên kết Quiz-Question (Many-to-Many)
```
PK: quiz_question_id (UUID)
FK: quiz_id → Quiz.quiz_id (CASCADE)
FK: question_id → Question.question_id (RESTRICT)
Attributes:
  - points (DECIMAL 5,2, NOT NULL)
  - order_num (INT, NOT NULL)

UNIQUE(quiz_id, question_id)
UNIQUE(quiz_id, order_num)
```

**12. Class** - Lớp học (Blended)
```
PK: class_id (UUID)
FK: course_id → Course.course_id (CASCADE)
FK: instructor_id → User.user_id (SET NULL)
Attributes:
  - name (VARCHAR 100, NOT NULL)
  - start_date (DATE)
  - end_date (DATE)
  - status (VARCHAR 20, DEFAULT 'SCHEDULED')
    - Values: SCHEDULED, ONGOING, COMPLETED, CANCELLED
  - max_students (INT)
  - location (VARCHAR 200)
  - created_at (TIMESTAMP)
  - updated_at (TIMESTAMP)
```

**13. Schedule** - Lịch học
```
PK: schedule_id (UUID)
FK: class_id → Class.class_id (CASCADE)
Attributes:
  - session_date (DATE, NOT NULL)
  - start_time (TIME, NOT NULL)
  - end_time (TIME, NOT NULL)
  - location (VARCHAR 200)
  - topic (VARCHAR 200)
  - session_type (VARCHAR 20, DEFAULT 'IN_PERSON')
    - Values: IN_PERSON, ONLINE, HYBRID
  - meeting_url (VARCHAR 500)
  - created_at (TIMESTAMP)
```

##### ENROLLMENT & PROGRESS (4 bảng)

**14. CourseEnrollment** - Đăng ký khóa học
```
PK: course_enrollment_id (UUID)
FK: user_id → User.user_id (CASCADE)
FK: course_id → Course.course_id (CASCADE)
Attributes:
  - role_in_course (VARCHAR 20, NOT NULL)
    - Values: STUDENT, INSTRUCTOR, TA
  - enrollment_status (VARCHAR 20, DEFAULT 'ACTIVE')
    - Values: ACTIVE, COMPLETED, DROPPED, SUSPENDED
  - enrolled_at (TIMESTAMP, DEFAULT CURRENT_TIMESTAMP)
  - completed_at (TIMESTAMP)
  - final_grade (DECIMAL 5,2)
  - completion_percentage (DECIMAL 5,2, DEFAULT 0.00)
  - last_accessed_at (TIMESTAMP)

UNIQUE(user_id, course_id)
```

**15. ClassEnrollment** - Đăng ký lớp học
```
PK: class_enrollment_id (UUID)
FK: user_id → User.user_id (CASCADE)
FK: class_id → Class.class_id (CASCADE)
FK: course_enrollment_id → CourseEnrollment.course_enrollment_id
Attributes:
  - role_in_class (VARCHAR 20, NOT NULL)
    - Values: STUDENT, INSTRUCTOR, TA
  - enrolled_at (TIMESTAMP, DEFAULT CURRENT_TIMESTAMP)

UNIQUE(user_id, class_id)
```

**16. Progress** - Tiến độ học tập (REDESIGNED)
```
PK: progress_id (UUID)
FK: user_id → User.user_id (CASCADE)
FK: course_id → Course.course_id (CASCADE)
FK: class_id → Class.class_id (SET NULL)
FK: module_id → Module.module_id (CASCADE)
FK: lecture_id → Lecture.lecture_id (CASCADE)
Attributes:
  - status (VARCHAR 20, NOT NULL, DEFAULT 'NOT_STARTED')
    - Values: NOT_STARTED, IN_PROGRESS, COMPLETED, SKIPPED
  - percent_complete (DECIMAL 5,2, DEFAULT 0.00)
  - last_position_seconds (INT, DEFAULT 0)
  - first_accessed_at (TIMESTAMP)
  - last_accessed_at (TIMESTAMP)
  - completed_at (TIMESTAMP)
  - created_at (TIMESTAMP, DEFAULT CURRENT_TIMESTAMP)
  - updated_at (TIMESTAMP, DEFAULT CURRENT_TIMESTAMP)

UNIQUE(user_id, course_id, COALESCE(class_id), module_id, lecture_id)
CHECK(percent_complete >= 0 AND percent_complete <= 100)
```

**17. Attendance** - Điểm danh
```
PK: attendance_id (UUID)
FK: schedule_id → Schedule.schedule_id (CASCADE)
FK: user_id → User.user_id (CASCADE)
Attributes:
  - status (VARCHAR 20, NOT NULL)
    - Values: PRESENT, ABSENT, LATE, EXCUSED
  - check_in_time (TIMESTAMP)
  - notes (TEXT)
  - recorded_at (TIMESTAMP, DEFAULT CURRENT_TIMESTAMP)

UNIQUE(schedule_id, user_id)
```

##### ASSESSMENT (5 bảng - REDESIGNED)

**18. Attempt** - Lần làm bài quiz (REDESIGNED)
```
PK: attempt_id (UUID)
FK: quiz_id → Quiz.quiz_id (CASCADE)
FK: user_id → User.user_id (CASCADE)
FK: course_enrollment_id → CourseEnrollment.course_enrollment_id (CASCADE)
FK: class_id → Class.class_id (SET NULL)
Attributes:
  - attempt_number (INT, NOT NULL)
  - started_at (TIMESTAMP, NOT NULL, DEFAULT CURRENT_TIMESTAMP)
  - submitted_at (TIMESTAMP)
  - time_spent_seconds (INT, DEFAULT 0)
  - status (VARCHAR 20, NOT NULL, DEFAULT 'IN_PROGRESS')
    - Values: IN_PROGRESS, SUBMITTED, GRADED, ABANDONED
  - auto_score (DECIMAL 6,2, DEFAULT 0.00)
  - manual_score (DECIMAL 6,2)
  - final_score (DECIMAL 6,2)
  - max_possible_score (DECIMAL 6,2, NOT NULL)
  - percentage_score (DECIMAL 5,2)
  - graded_at (TIMESTAMP)
  - graded_by (UUID, FK → User.user_id)
  - ip_address (VARCHAR 45)

UNIQUE(user_id, quiz_id, attempt_number)
```

**19. QuizSubmission** - Câu trả lời quiz (RENAMED from Submission)
```
PK: quiz_submission_id (UUID)
FK: attempt_id → Attempt.attempt_id (CASCADE)
FK: question_id → Question.question_id (RESTRICT)
Attributes:
  - answer_text (TEXT)
  - selected_option_ids (UUID[])
  - submitted_at (TIMESTAMP, NOT NULL, DEFAULT CURRENT_TIMESTAMP)
  - auto_score (DECIMAL 5,2)
  - manual_score (DECIMAL 5,2)
  - final_score (DECIMAL 5,2)
  - max_points (DECIMAL 5,2, NOT NULL)
  - instructor_feedback (TEXT)
  - graded_at (TIMESTAMP)
  - graded_by (UUID, FK → User.user_id)
```

**20. Assignment** - Bài tập (MỚI - thay thế Submission)
```
PK: assignment_id (UUID)
FK: course_id → Course.course_id (CASCADE)
FK: class_id → Class.class_id (SET NULL)
Attributes:
  - title (VARCHAR 200, NOT NULL)
  - description (TEXT, NOT NULL)
  - instructions (TEXT)
  - assignment_type (VARCHAR 20, NOT NULL)
    - Values: ESSAY, CODE, FILE_UPLOAD, PROBLEM_SET, PROJECT
  - max_points (DECIMAL 6,2, NOT NULL)
  - due_date (TIMESTAMP, NOT NULL)
  - late_submission_allowed (BOOLEAN, DEFAULT TRUE)
  - late_penalty_percent (DECIMAL 5,2, DEFAULT 10.00)
  - max_late_days (INT, DEFAULT 7)
  - allow_resubmission (BOOLEAN, DEFAULT FALSE)
  - max_submissions (INT, DEFAULT 1)
  - rubric (JSON)
  - auto_grading_enabled (BOOLEAN, DEFAULT FALSE)
  - test_cases (JSON)
  - created_by (UUID, FK → User.user_id)
  - created_at (TIMESTAMP, NOT NULL)
  - updated_at (TIMESTAMP)
```

**21. AssignmentSubmission** - Nộp bài tập (MỚI)
```
PK: assignment_submission_id (UUID)
FK: assignment_id → Assignment.assignment_id (CASCADE)
FK: user_id → User.user_id (CASCADE)
FK: course_enrollment_id → CourseEnrollment.course_enrollment_id (CASCADE)
Attributes:
  - submission_number (INT, NOT NULL, DEFAULT 1)
  - submitted_at (TIMESTAMP, NOT NULL, DEFAULT CURRENT_TIMESTAMP)
  - content (TEXT)
  - file_urls (JSON)
  - code_submission (TEXT)
  - is_late (BOOLEAN, DEFAULT FALSE)
  - days_late (INT, DEFAULT 0)
  - status (VARCHAR 20, NOT NULL, DEFAULT 'SUBMITTED')
    - Values: DRAFT, SUBMITTED, GRADING, GRADED, RETURNED
  - auto_score (DECIMAL 6,2)
  - manual_score (DECIMAL 6,2)
  - final_score (DECIMAL 6,2)
  - penalty_applied (DECIMAL 6,2, DEFAULT 0.00)
  - rubric_scores (JSON)
  - feedback (TEXT)
  - graded_at (TIMESTAMP)
  - graded_by (UUID, FK → User.user_id)
  - version (INT, DEFAULT 1)

UNIQUE(assignment_id, user_id, submission_number)
```

**22. GradeBook** - Sổ điểm tổng hợp (MỚI)
```
PK: gradebook_id (UUID)
FK: user_id → User.user_id (CASCADE)
FK: course_id → Course.course_id (CASCADE)
FK: class_id → Class.class_id (SET NULL)
Attributes:
  - quiz_score (DECIMAL 6,2, DEFAULT 0.00)
  - assignment_score (DECIMAL 6,2, DEFAULT 0.00)
  - participation_score (DECIMAL 6,2, DEFAULT 0.00)
  - total_score (DECIMAL 6,2, DEFAULT 0.00)
  - weighted_score (DECIMAL 6,2, DEFAULT 0.00)
  - letter_grade (VARCHAR 5)
  - last_updated_at (TIMESTAMP)

UNIQUE(user_id, course_id, COALESCE(class_id))
```

##### CERTIFICATE (3 bảng)

**23. CertificateTemplate** - Mẫu chứng chỉ (MỚI)
```
PK: template_id (UUID)
Attributes:
  - name (VARCHAR 100, NOT NULL, UNIQUE)
  - description (TEXT)
  - background_image_url (VARCHAR 500)
  - layout_config (JSON)
  - html_template (TEXT)
  - is_active (BOOLEAN, DEFAULT TRUE)
  - is_default (BOOLEAN, DEFAULT FALSE)
  - created_by (UUID, FK → User.user_id)
  - created_at (TIMESTAMP)
  - updated_at (TIMESTAMP)
```

**24. Certificate** - Chứng chỉ (REDESIGNED)
```
PK: certificate_id (UUID)
FK: user_id → User.user_id (CASCADE)
FK: course_id → Course.course_id (CASCADE)
FK: course_enrollment_id → CourseEnrollment.course_enrollment_id (CASCADE)
FK: template_id → CertificateTemplate.template_id (SET NULL)
Attributes:
  - certificate_code (VARCHAR 50, UNIQUE, NOT NULL)
  - verification_code (VARCHAR 100, UNIQUE, NOT NULL)
  - title (VARCHAR 200, NOT NULL)
  - issue_date (DATE, NOT NULL)
  - completion_date (DATE, NOT NULL)
  - final_grade (DECIMAL 5,2)
  - grade_letter (VARCHAR 5)
  - pdf_url (VARCHAR 500)
  - qr_code_url (VARCHAR 500)
  - verification_url (VARCHAR 500)
  - status (VARCHAR 20, DEFAULT 'ACTIVE')
    - Values: ACTIVE, REVOKED, EXPIRED
  - valid_from (DATE, NOT NULL)
  - valid_until (DATE)
  - revoked_at (TIMESTAMP)
  - revoked_by (UUID, FK → User.user_id)
  - revoke_reason (TEXT)
  - created_at (TIMESTAMP, DEFAULT CURRENT_TIMESTAMP)

UNIQUE(user_id, course_id)
```

**25. CertificateVerification** - Lịch sử xác minh chứng chỉ (MỚI)
```
PK: verification_id (UUID)
FK: certificate_id → Certificate.certificate_id (CASCADE)
Attributes:
  - verified_at (TIMESTAMP, NOT NULL, DEFAULT CURRENT_TIMESTAMP)
  - verified_by_ip (VARCHAR 45)
  - verification_method (VARCHAR 20)
    - Values: CODE, QR, URL
  - verification_result (VARCHAR 20, NOT NULL)
    - Values: VALID, REVOKED, EXPIRED, NOT_FOUND
```

##### NOTIFICATION (3 bảng - MỚI)

**26. Notification** - Thông báo
```
PK: notification_id (UUID)
FK: user_id → User.user_id (CASCADE)
Attributes:
  - notification_type (VARCHAR 50, NOT NULL)
    - Values: ASSIGNMENT_DUE, GRADE_PUBLISHED, CERTIFICATE_ISSUED,
              COURSE_UPDATE, CLASS_REMINDER, ENROLLMENT_CONFIRMED
  - title (VARCHAR 200, NOT NULL)
  - message (TEXT, NOT NULL)
  - related_entity_type (VARCHAR 50)
  - related_entity_id (UUID)
  - action_url (VARCHAR 500)
  - priority (VARCHAR 20, DEFAULT 'NORMAL')
    - Values: LOW, NORMAL, HIGH, URGENT
  - is_read (BOOLEAN, DEFAULT FALSE)
  - read_at (TIMESTAMP)
  - sent_via_email (BOOLEAN, DEFAULT FALSE)
  - sent_via_push (BOOLEAN, DEFAULT FALSE)
  - created_at (TIMESTAMP, NOT NULL, DEFAULT CURRENT_TIMESTAMP)
  - expires_at (TIMESTAMP)
```

**27. NotificationPreference** - Tùy chọn thông báo
```
PK: preference_id (UUID)
FK: user_id → User.user_id (CASCADE)
Attributes:
  - notification_type (VARCHAR 50, NOT NULL)
  - email_enabled (BOOLEAN, DEFAULT TRUE)
  - push_enabled (BOOLEAN, DEFAULT TRUE)
  - sms_enabled (BOOLEAN, DEFAULT FALSE)
  - frequency (VARCHAR 20, DEFAULT 'IMMEDIATE')
    - Values: IMMEDIATE, DAILY_DIGEST, WEEKLY_DIGEST, NEVER
  - updated_at (TIMESTAMP)

UNIQUE(user_id, notification_type)
```

**28. NotificationLog** - Lịch sử gửi thông báo
```
PK: log_id (UUID)
FK: notification_id → Notification.notification_id (CASCADE)
Attributes:
  - channel (VARCHAR 20, NOT NULL)
    - Values: EMAIL, PUSH, SMS, IN_APP
  - status (VARCHAR 20, NOT NULL)
    - Values: PENDING, SENT, FAILED, BOUNCED
  - sent_at (TIMESTAMP)
  - error_message (TEXT)
  - attempts (INT, DEFAULT 1)
  - created_at (TIMESTAMP, DEFAULT CURRENT_TIMESTAMP)
```

##### AUDIT & SYSTEM (3 bảng - MỚI)

**29. ActivityLog** - Nhật ký hoạt động
```
PK: log_id (UUID)
FK: user_id → User.user_id (SET NULL)
Attributes:
  - action (VARCHAR 100, NOT NULL)
  - entity_type (VARCHAR 50, NOT NULL)
  - entity_id (UUID, NOT NULL)
  - description (TEXT)
  - old_values (JSON)
  - new_values (JSON)
  - ip_address (VARCHAR 45)
  - user_agent (TEXT)
  - created_at (TIMESTAMP, NOT NULL, DEFAULT CURRENT_TIMESTAMP)
  - log_date (DATE GENERATED ALWAYS AS (created_at::DATE) STORED)

INDEX on (user_id, created_at DESC)
INDEX on (entity_type, entity_id, created_at DESC)
```

**30. File** - Quản lý file
```
PK: file_id (UUID)
FK: uploaded_by → User.user_id (CASCADE)
Attributes:
  - original_filename (VARCHAR 255, NOT NULL)
  - stored_filename (VARCHAR 255, NOT NULL)
  - file_path (VARCHAR 500, NOT NULL)
  - file_url (VARCHAR 500, NOT NULL)
  - file_size_bytes (BIGINT, NOT NULL)
  - mime_type (VARCHAR 100, NOT NULL)
  - entity_type (VARCHAR 50)
  - entity_id (UUID)
  - storage_type (VARCHAR 20, DEFAULT 'LOCAL')
    - Values: LOCAL, S3, AZURE, GCS
  - is_deleted (BOOLEAN, DEFAULT FALSE)
  - uploaded_at (TIMESTAMP, NOT NULL, DEFAULT CURRENT_TIMESTAMP)
  - deleted_at (TIMESTAMP)
```

**31. SystemSettings** - Cấu hình hệ thống
```
PK: setting_id (UUID)
Attributes:
  - setting_key (VARCHAR 100, UNIQUE, NOT NULL)
  - setting_value (TEXT, NOT NULL)
  - data_type (VARCHAR 20, DEFAULT 'STRING')
    - Values: STRING, INTEGER, DECIMAL, BOOLEAN, JSON
  - category (VARCHAR 50)
  - description (TEXT)
  - is_editable (BOOLEAN, DEFAULT TRUE)
  - updated_by (UUID, FK → User.user_id)
  - updated_at (TIMESTAMP, DEFAULT CURRENT_TIMESTAMP)
```

---

#### 3.2. Mối quan hệ giữa các thực thể

##### Relationships Matrix

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
| QuizQuestion | Quiz | Many-to-One | N:1 | quiz_id | CASCADE |
| QuizQuestion | Question | Many-to-One | N:1 | question_id | RESTRICT |
| Class | Course | Many-to-One | N:1 | course_id | CASCADE |
| Class | User (instructor) | Many-to-One | N:1 | instructor_id | SET NULL |
| Schedule | Class | Many-to-One | N:1 | class_id | CASCADE |
| CourseEnrollment | User | Many-to-One | N:1 | user_id | CASCADE |
| CourseEnrollment | Course | Many-to-One | N:1 | course_id | CASCADE |
| ClassEnrollment | User | Many-to-One | N:1 | user_id | CASCADE |
| ClassEnrollment | Class | Many-to-One | N:1 | class_id | CASCADE |
| ClassEnrollment | CourseEnrollment | Many-to-One | N:1 | course_enrollment_id | CASCADE |
| Progress | User | Many-to-One | N:1 | user_id | CASCADE |
| Progress | Course | Many-to-One | N:1 | course_id | CASCADE |
| Progress | Class | Many-to-One | N:1 | class_id | SET NULL |
| Progress | Module | Many-to-One | N:1 | module_id | CASCADE |
| Progress | Lecture | Many-to-One | N:1 | lecture_id | CASCADE |
| Attendance | Schedule | Many-to-One | N:1 | schedule_id | CASCADE |
| Attendance | User | Many-to-One | N:1 | user_id | CASCADE |
| Attempt | Quiz | Many-to-One | N:1 | quiz_id | CASCADE |
| Attempt | User | Many-to-One | N:1 | user_id | CASCADE |
| Attempt | CourseEnrollment | Many-to-One | N:1 | course_enrollment_id | CASCADE |
| Attempt | Class | Many-to-One | N:1 | class_id | SET NULL |
| QuizSubmission | Attempt | Many-to-One | N:1 | attempt_id | CASCADE |
| QuizSubmission | Question | Many-to-One | N:1 | question_id | RESTRICT |
| Assignment | Course | Many-to-One | N:1 | course_id | CASCADE |
| Assignment | Class | Many-to-One | N:1 | class_id | SET NULL |
| AssignmentSubmission | Assignment | Many-to-One | N:1 | assignment_id | CASCADE |
| AssignmentSubmission | User | Many-to-One | N:1 | user_id | CASCADE |
| AssignmentSubmission | CourseEnrollment | Many-to-One | N:1 | course_enrollment_id | CASCADE |
| GradeBook | User | Many-to-One | N:1 | user_id | CASCADE |
| GradeBook | Course | Many-to-One | N:1 | course_id | CASCADE |
| GradeBook | Class | Many-to-One | N:1 | class_id | SET NULL |
| Certificate | User | Many-to-One | N:1 | user_id | CASCADE |
| Certificate | Course | Many-to-One | N:1 | course_id | CASCADE |
| Certificate | CourseEnrollment | Many-to-One | N:1 | course_enrollment_id | CASCADE |
| Certificate | CertificateTemplate | Many-to-One | N:1 | template_id | SET NULL |
| CertificateVerification | Certificate | Many-to-One | N:1 | certificate_id | CASCADE |
| Notification | User | Many-to-One | N:1 | user_id | CASCADE |
| NotificationPreference | User | Many-to-One | N:1 | user_id | CASCADE |
| NotificationLog | Notification | Many-to-One | N:1 | notification_id | CASCADE |
| ActivityLog | User | Many-to-One | N:1 | user_id | SET NULL |
| File | User (uploader) | Many-to-One | N:1 | uploaded_by | CASCADE |

---

#### 3.3. Vẽ ERD trong StarUML

**Tạo file ERD.mdj** với các yêu cầu:

##### A. General Requirements
1. **All 31 tables** must be represented
2. **All PKs, FKs, Attributes** must be shown
3. **Data types** must be specified
4. **Constraints** (UNIQUE, NOT NULL, CHECK) must be indicated
5. **Relationships** must be correctly drawn
6. **Layout** must be clear and organized

##### B. UML Class Representation for Tables

**Format cho mỗi table:**
```
┌─────────────────────────────────┐
│ <<table>>                       │
│ TableName                       │
├─────────────────────────────────┤
│ PK: field_name : DataType       │
│ FK: field_name : DataType       │
│     field_name : DataType       │
│     ...                         │
├─────────────────────────────────┤
│ Constraints:                    │
│ • UNIQUE(field1, field2)        │
│ • CHECK(condition)              │
└─────────────────────────────────┘
```

**Example - User table:**
```
┌─────────────────────────────────────────────┐
│ <<table>>                                   │
│ User                                        │
├─────────────────────────────────────────────┤
│ PK: user_id : UUID                          │
│     email : VARCHAR(255)                    │
│     password_hash : VARCHAR(255)            │
│     first_name : VARCHAR(100)               │
│     last_name : VARCHAR(100)                │
│     avatar_url : VARCHAR(500)               │
│     phone : VARCHAR(20)                     │
│     timezone : VARCHAR(50)                  │
│     locale : VARCHAR(10)                    │
│     account_status : VARCHAR(30)            │
│     email_verified_at : TIMESTAMP           │
│     last_login_at : TIMESTAMP               │
│     created_at : TIMESTAMP                  │
│     updated_at : TIMESTAMP                  │
├─────────────────────────────────────────────┤
│ Constraints:                                │
│ • UNIQUE(email)                             │
│ • CHECK(account_status IN ('PENDING...'))  │
└─────────────────────────────────────────────┘
```

##### C. Relationship Representation

**1. One-to-Many (1:N)**
```
Table A ──────────< Table B
  (1)              (N)

Example:
Course ──────────< Module
  (1)              (N)

Draw: Solid line with crow's foot on "many" side
```

**2. Many-to-Many (M:N) - via junction table**
```
Table A >────────< Junction >────────< Table B
  (N)               (M:N)              (N)

Example:
Quiz >────────< QuizQuestion >────────< Question

Draw: Two one-to-many relationships
```

**3. Foreign Key (FK)**
```
Mark FK fields with "FK:" prefix
Show relationship line from FK to PK
```

##### D. Layout Strategy

**Group tables by domain:**

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
│  [Quiz]  [Question]  [Option]  [QuizQuestion]               │
│  [Assignment]  [Attempt]  [QuizSubmission]                  │
│  [AssignmentSubmission]  [GradeBook]                        │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│                 ENROLLMENT & PROGRESS                       │
│  [CourseEnrollment]  [ClassEnrollment]  [Progress]          │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│                  CLASS & BLENDED LEARNING                   │
│  [Class]  [Schedule]  [Attendance]                          │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│                     CERTIFICATE                             │
│  [CertificateTemplate]  [Certificate]                       │
│  [CertificateVerification]                                  │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│                   NOTIFICATION                              │
│  [Notification]  [NotificationPreference]                   │
│  [NotificationLog]                                          │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│                   AUDIT & SYSTEM                            │
│  [ActivityLog]  [File]  [SystemSettings]                    │
└─────────────────────────────────────────────────────────────┘
```

##### E. StarUML Creation Steps

1. **Create New Project** in StarUML
2. **Add ERD (Data Model Diagram)**
3. **Create Tables** using "Table" element
4. **Add Columns** with:
   - Name
   - Data Type
   - PK checkbox
   - FK checkbox
   - NOT NULL checkbox
   - UNIQUE checkbox
5. **Draw Relationships**:
   - Identifying (solid line, PK-FK)
   - Non-identifying (dashed line, FK only)
6. **Set Multiplicity**: 1, 0..1, 0..*, 1..*
7. **Arrange Layout** by domain groups
8. **Save as ERD.mdj**

##### F. Validation Checklist

```markdown
✅ All 31 tables created
✅ All primary keys defined (UUID)
✅ All foreign keys defined with correct references
✅ All attributes with correct data types
✅ All UNIQUE constraints marked
✅ All CHECK constraints documented
✅ All relationships drawn correctly
✅ Multiplicity (1:N, M:N) indicated
✅ ON DELETE behaviors specified (CASCADE, SET NULL, RESTRICT)
✅ Layout organized by domain
✅ No overlapping elements
✅ Readable font size and spacing
```

---

### Bước 4: Viết tài liệu đặc tả chi tiết

#### 4.1. BFD-SPEC.md

Nội dung:
```markdown
# Business Function Diagram Specification

## 1. Overview
Mô tả tổng quan về các chức năng nghiệp vụ của hệ thống B-Learning

## 2. Actors
- Student (Học viên)
- Instructor (Giảng viên)
- Teaching Assistant (TA)
- Admin (Quản trị viên)
- System (Hệ thống tự động)

## 3. Use Cases by Actor

### Student
(Chi tiết từ Bước 2.2)

### Instructor
(Chi tiết từ Bước 2.2)

### Admin
(Chi tiết từ Bước 2.2)

### System
(Chi tiết từ Bước 2.2)

## 4. Use Case Descriptions
Mô tả chi tiết từng use case:
- Use Case ID
- Name
- Actor
- Description
- Preconditions
- Postconditions
- Main Flow
- Alternative Flows
- Exception Flows

## 5. Business Rules
- Enrollment rules
- Grading policies
- Certificate issuance criteria
- Late submission policies
- etc.

## 6. Diagram
[Link to BFD.mdj or embedded image]
```

#### 4.2. ERD-SPEC.md

Nội dung:
```markdown
# Entity Relationship Diagram Specification

## 1. Overview
31 tables, 8 domains, support for Assignment-based B-Learning

## 2. Domains
1. User Management (3 tables)
2. Course Content (4 tables)
3. Assessment (9 tables)
4. Enrollment & Progress (4 tables)
5. Class & Blended Learning (3 tables)
6. Certificate (3 tables)
7. Notification (3 tables)
8. Audit & System (3 tables)

## 3. Entities Detail
(Chi tiết 31 bảng từ Bước 3.1)

## 4. Relationships
(Chi tiết từ Bước 3.2)

## 5. Indexes Strategy
- Primary keys (UUID)
- Foreign keys
- Performance indexes
- Full-text search indexes

## 6. Constraints
- Primary Key constraints
- Foreign Key constraints with CASCADE behaviors
- UNIQUE constraints
- CHECK constraints
- DEFAULT values

## 7. Diagram
[Link to ERD.mdj or embedded image]
```

#### 4.3. DATABASE-SCHEMA.md

Nội dung:
```markdown
# Database Schema Specification

## 1. Overview
- Database: PostgreSQL 14+
- Total tables: 31
- Naming convention: snake_case
- Primary keys: UUID
- Timestamps: TIMESTAMP WITH TIME ZONE

## 2. Table Definitions

### 2.1 User Management

#### User
CREATE TABLE "User" (
  ...
);

(DDL cho tất cả 31 bảng)

## 3. Indexes

### Performance Indexes
CREATE INDEX idx_user_email ON "User"(email);
...

### Full-text Search
CREATE INDEX idx_course_search ON "Course" USING GIN(...);
...

## 4. Constraints

### Foreign Keys
ALTER TABLE UserRole ADD CONSTRAINT fk_userrole_user...;
...

### Check Constraints
ALTER TABLE Progress ADD CONSTRAINT chk_progress_percent...;
...

## 5. Triggers

### Auto-update timestamps
CREATE TRIGGER trg_user_updated_at...;
...

### Business logic triggers
CREATE TRIGGER trg_auto_issue_certificate...;
...

## 6. Views

### Materialized Views for Reporting
CREATE MATERIALIZED VIEW CourseStatistics...;
...

## 7. Functions

### Helper Functions
CREATE FUNCTION update_progress()...;
...
```

#### 4.4. FUNCTIONAL-REQUIREMENTS.md

Nội dung:
```markdown
# Functional Requirements Specification

## 1. User Management

### FR-UM-001: User Registration
- Actor: Guest
- Description: ...
- Input: email, password, first_name, last_name
- Process: ...
- Output: User account created, verification email sent
- Business Rules: ...

(Chi tiết tất cả functional requirements)

## 2. Course Management

### FR-CM-001: Create Course
...

## 3. Learning Activities

### FR-LA-001: Watch Lecture
...

### FR-LA-002: Submit Assignment
...

## 4. Assessment

### FR-AS-001: Take Quiz
...

### FR-AS-002: Grade Assignment
...

## 5. Certificate

### FR-CT-001: Auto Issue Certificate
...

### FR-CT-002: Verify Certificate
...

## 6. Notification

### FR-NT-001: Send Assignment Due Notification
...
```

#### 4.5. API-ENDPOINTS.md

Nội dung:
```markdown
# API Endpoints Specification

## 1. Authentication
POST /api/auth/register
POST /api/auth/login
POST /api/auth/logout
...

## 2. User Management
GET /api/users/profile
PUT /api/users/profile
...

## 3. Courses
GET /api/courses
GET /api/courses/:id
POST /api/courses (Instructor/Admin)
...

## 4. Enrollment
POST /api/enrollments
GET /api/enrollments/my-courses
...

## 5. Learning
GET /api/courses/:id/modules
GET /api/lectures/:id
POST /api/progress
...

## 6. Assessment
GET /api/quizzes/:id
POST /api/attempts
POST /api/assignments/:id/submit
...

## 7. Grading
GET /api/assignments/:id/submissions (Instructor)
PUT /api/submissions/:id/grade (Instructor)
...

## 8. Certificate
GET /api/certificates/my-certificates
GET /api/certificates/verify/:code
...

## 9. Notification
GET /api/notifications
PUT /api/notifications/:id/read
PUT /api/notification-preferences
...
```

---

## PHASE 2: IMPLEMENTATION (Viết SQL & Documentation)

### Bước 5: Tạo SQL Scripts

#### 5.1. Folder Structure
```
99-B-Learing/sql/
├── 01-schema.sql          # CREATE TABLE statements
├── 02-indexes.sql         # CREATE INDEX statements
├── 03-constraints.sql     # ALTER TABLE ... ADD CONSTRAINT
├── 04-triggers.sql        # CREATE TRIGGER and FUNCTION
├── 05-views.sql          # CREATE VIEW and MATERIALIZED VIEW
├── 06-seed-data.sql      # INSERT sample data
└── 99-drop-all.sql       # DROP statements for cleanup
```

#### 5.2. 01-schema.sql

```sql
-- ============================================
-- B-LEARNING DATABASE SCHEMA
-- Version: 2.0 (Redesigned)
-- Database: PostgreSQL 14+
-- Created: 2025-11-25
-- ============================================

-- Extensions
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pg_trgm";
CREATE EXTENSION IF NOT EXISTS "btree_gin";

-- ============================================
-- 1. USER MANAGEMENT (3 tables)
-- ============================================

CREATE TABLE "User" (
  user_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  email VARCHAR(255) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  first_name VARCHAR(100) NOT NULL,
  last_name VARCHAR(100) NOT NULL,
  avatar_url VARCHAR(500),
  phone VARCHAR(20),
  timezone VARCHAR(50) DEFAULT 'UTC',
  locale VARCHAR(10) DEFAULT 'vi',
  account_status VARCHAR(30) NOT NULL DEFAULT 'PENDING_VERIFICATION',
  email_verified_at TIMESTAMP,
  last_login_at TIMESTAMP,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT chk_account_status CHECK (account_status IN (
    'PENDING_VERIFICATION', 'ACTIVE', 'SUSPENDED', 'DELETED'
  ))
);

CREATE TABLE "Role" (
  role_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  name VARCHAR(50) NOT NULL UNIQUE,
  description TEXT,
  permissions JSON,
  is_system_role BOOLEAN DEFAULT TRUE,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE "UserRole" (
  user_role_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID NOT NULL REFERENCES "User"(user_id) ON DELETE CASCADE,
  role_id UUID NOT NULL REFERENCES "Role"(role_id) ON DELETE CASCADE,
  granted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  granted_by UUID REFERENCES "User"(user_id),
  expires_at TIMESTAMP,

  CONSTRAINT uq_user_role UNIQUE(user_id, role_id)
);

-- ============================================
-- 2. COURSE CONTENT (4 tables)
-- ============================================

CREATE TABLE "Course" (
  -- (Full definition from Bước 3.1)
);

CREATE TABLE "Module" (
  -- (Full definition from Bước 3.1)
);

CREATE TABLE "Lecture" (
  -- (Full definition from Bước 3.1)
);

CREATE TABLE "Resource" (
  -- (Full definition from Bước 3.1)
);

-- ============================================
-- (Continue for all 31 tables)
-- ============================================
```

#### 5.3. 02-indexes.sql

```sql
-- ============================================
-- INDEXES FOR PERFORMANCE
-- ============================================

-- User indexes
CREATE INDEX idx_user_email ON "User"(email);
CREATE INDEX idx_user_status ON "User"(account_status);
CREATE INDEX idx_user_created ON "User"(created_at DESC);

-- Course indexes
CREATE INDEX idx_course_status ON "Course"(status);
CREATE INDEX idx_course_published ON "Course"(published_at DESC)
  WHERE status = 'PUBLISHED';
CREATE INDEX idx_course_category ON "Course"(category);

-- Full-text search
CREATE INDEX idx_course_search ON "Course" USING GIN(
  to_tsvector('english', title || ' ' || description)
);

-- Progress tracking
CREATE INDEX idx_progress_user_course ON "Progress"(user_id, course_id);
CREATE INDEX idx_progress_status ON "Progress"(status);

-- Assignment indexes
CREATE INDEX idx_assignment_due ON "Assignment"(due_date);
CREATE INDEX idx_assignment_course ON "Assignment"(course_id);
CREATE INDEX idx_assignment_submission_user ON "AssignmentSubmission"(user_id);

-- (Continue for all relevant indexes)
```

#### 5.4. 03-constraints.sql

```sql
-- ============================================
-- CONSTRAINTS
-- ============================================

-- Check constraints
ALTER TABLE "Progress"
  ADD CONSTRAINT chk_progress_percent
  CHECK (percent_complete >= 0 AND percent_complete <= 100);

ALTER TABLE "Assignment"
  ADD CONSTRAINT chk_late_penalty
  CHECK (late_penalty_percent >= 0 AND late_penalty_percent <= 100);

-- (Continue for all check constraints)
```

#### 5.5. 04-triggers.sql

```sql
-- ============================================
-- TRIGGERS AND FUNCTIONS
-- ============================================

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

-- Auto-issue certificate when course completed
CREATE OR REPLACE FUNCTION auto_issue_certificate()
RETURNS TRIGGER AS $$
-- (Implementation from DATABASE-DESIGN-EVALUATION.md)
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_auto_issue_certificate
  AFTER UPDATE ON "CourseEnrollment"
  FOR EACH ROW
  EXECUTE FUNCTION auto_issue_certificate();

-- Auto-grade MCQ quiz
CREATE OR REPLACE FUNCTION auto_grade_mcq()
RETURNS TRIGGER AS $$
-- (Implementation)
END;
$$ LANGUAGE plpgsql;

-- Update course progress
CREATE OR REPLACE FUNCTION update_course_progress()
RETURNS TRIGGER AS $$
-- (Implementation)
END;
$$ LANGUAGE plpgsql;

-- Send notification on assignment due
CREATE OR REPLACE FUNCTION send_assignment_due_notification()
RETURNS TRIGGER AS $$
-- (Implementation)
END;
$$ LANGUAGE plpgsql;

-- (Continue for all triggers)
```

#### 5.6. 06-seed-data.sql

```sql
-- ============================================
-- SEED DATA FOR TESTING
-- ============================================

-- Roles
INSERT INTO "Role" (name, description, is_system_role) VALUES
('STUDENT', 'Student role', TRUE),
('INSTRUCTOR', 'Instructor role', TRUE),
('TA', 'Teaching Assistant role', TRUE),
('ADMIN', 'Administrator role', TRUE);

-- Sample users
INSERT INTO "User" (email, password_hash, first_name, last_name, account_status) VALUES
('admin@example.com', '$2a$10$...', 'Admin', 'User', 'ACTIVE'),
('instructor@example.com', '$2a$10$...', 'John', 'Doe', 'ACTIVE'),
('student@example.com', '$2a$10$...', 'Jane', 'Smith', 'ACTIVE');

-- Sample course
INSERT INTO "Course" (code, title, description, difficulty_level, status, created_by) VALUES
('CS101', 'Introduction to Programming', '...', 'BEGINNER', 'PUBLISHED',
  (SELECT user_id FROM "User" WHERE email = 'instructor@example.com'));

-- (Continue with sample data)
```

---

### Bước 6: Viết documentation files

Tạo tất cả file .md theo cấu trúc đã nêu ở Bước 4.

---

### Bước 7: Tạo UML files trong StarUML

#### 7.1. BFD.mdj
- Vẽ Business Function Diagram theo Bước 2
- Export as BFD.mdj

#### 7.2. ERD.mdj
- Vẽ Entity Relationship Diagram theo Bước 3
- Đầy đủ 31 bảng, PK, FK, attributes, relationships
- Export as ERD.mdj

---

## PHASE 3: VALIDATION & DOCUMENTATION

### Bước 8: Validation Checklist

```markdown
### ERD Validation
- [ ] All 31 tables defined
- [ ] All PKs are UUID
- [ ] All FKs correctly reference PKs
- [ ] All data types specified
- [ ] All UNIQUE constraints defined
- [ ] All CHECK constraints defined
- [ ] All ON DELETE behaviors specified
- [ ] All relationships correctly drawn in ERD.mdj

### SQL Validation
- [ ] 01-schema.sql creates all tables without errors
- [ ] 02-indexes.sql creates all indexes without errors
- [ ] 03-constraints.sql adds all constraints without errors
- [ ] 04-triggers.sql creates all triggers without errors
- [ ] 06-seed-data.sql inserts sample data without errors
- [ ] All SQL follows PostgreSQL syntax

### Documentation Validation
- [ ] BFD-SPEC.md complete with all use cases
- [ ] ERD-SPEC.md complete with all 31 tables
- [ ] DATABASE-SCHEMA.md has full DDL
- [ ] FUNCTIONAL-REQUIREMENTS.md lists all FRs
- [ ] API-ENDPOINTS.md lists all endpoints
- [ ] All .md files use proper markdown formatting
- [ ] All diagrams (BFD.mdj, ERD.mdj) are included

### Functional Validation
- [ ] Forum/Discussion removed completely
- [ ] Assignment system fully designed
- [ ] Certificate system enhanced
- [ ] Notification system added
- [ ] All relationships make sense
- [ ] No orphaned tables
- [ ] All business rules enforced by constraints/triggers
```

---

### Bước 9: Tạo README cho hệ thống

Tạo file `99-B-Learing/README.md`:

```markdown
# B-Learning System Database Design (v2.0)

## Overview
Redesigned database for B-Learning (Blended Learning) system with focus on Assignment-based learning and automated certificate issuance.

## Key Changes from v1.0
- ❌ Removed: Forum, Discussion, Thread, Post
- ✅ Added: Assignment System (5 tables)
- ✅ Enhanced: Certificate with templates and verification
- ✅ Added: Notification system (3 tables)
- ✅ Fixed: Progress tracking with course_id, module_id, quiz_id
- ✅ Fixed: Attempt with proper enrollment relationship

## Architecture
- **Database**: PostgreSQL 14+
- **Total Tables**: 31
- **Domains**: 8 (User, Course, Assessment, Enrollment, Class, Certificate, Notification, System)
- **Primary Keys**: UUID
- **Constraints**: UNIQUE, CHECK, FK with CASCADE

## Documentation
- `/documents/BFD-SPEC.md` - Business functions
- `/documents/ERD-SPEC.md` - Entity relationships
- `/documents/DATABASE-SCHEMA.md` - Full DDL
- `/documents/FUNCTIONAL-REQUIREMENTS.md` - Requirements
- `/documents/API-ENDPOINTS.md` - API design

## UML Diagrams
- `/BFD.mdj` - Business Function Diagram (StarUML)
- `/ERD.mdj` - Entity Relationship Diagram (StarUML)

## SQL Scripts
- `/sql/01-schema.sql` - Create tables
- `/sql/02-indexes.sql` - Performance indexes
- `/sql/03-constraints.sql` - Constraints
- `/sql/04-triggers.sql` - Business logic
- `/sql/06-seed-data.sql` - Sample data

## Quick Start
```bash
# Create database
createdb b_learning

# Run schema
psql b_learning < sql/01-schema.sql
psql b_learning < sql/02-indexes.sql
psql b_learning < sql/03-constraints.sql
psql b_learning < sql/04-triggers.sql
psql b_learning < sql/06-seed-data.sql
```

## Features
✅ Course management with modules and lectures
✅ Assignment-based assessment
✅ Quiz with auto-grading
✅ Progress tracking (lecture, module, quiz, assignment)
✅ Blended learning (online + in-person class)
✅ Automated certificate issuance
✅ Multi-channel notifications
✅ Rubric-based grading
✅ Late submission handling
✅ Audit logging

## Removed Features (from v1.0)
❌ Forum/Discussion threads
❌ Post/Reply system
❌ Vote/Like features
❌ Off-topic discussions

## Author
Nguyễn Văn Kiệt - CNTT1-K63
Redesigned: 2025-11-25
```

---

## Deliverables Summary

### 📁 Folder Structure
```
99-B-Learing/
├── README.md
├── req-1.md (this file)
│
├── documents/
│   ├── BFD-SPEC.md
│   ├── ERD-SPEC.md
│   ├── DATABASE-SCHEMA.md
│   ├── FUNCTIONAL-REQUIREMENTS.md
│   └── API-ENDPOINTS.md
│
├── sql/
│   ├── 01-schema.sql
│   ├── 02-indexes.sql
│   ├── 03-constraints.sql
│   ├── 04-triggers.sql
│   ├── 05-views.sql
│   ├── 06-seed-data.sql
│   └── 99-drop-all.sql
│
├── BFD.mdj (StarUML)
└── ERD.mdj (StarUML)
```

---

## Tiêu chí đánh giá

### ✅ Thiết kế tốt phải có:
1. **Đầy đủ**: 31 bảng theo đặc tả
2. **Chính xác**: PK, FK, data types, constraints đúng
3. **Nhất quán**: Naming convention, format
4. **Hoàn chỉnh**: ERD, BFD, SQL, documentation
5. **Có thể triển khai**: SQL chạy được không lỗi
6. **Rõ ràng**: Diagram dễ đọc, layout logic
7. **Đúng yêu cầu**: Không có forum, có Assignment, có Certificate
8. **Best practices**: Indexes, constraints, triggers

### ❌ Tránh các lỗi:
1. Thiếu bảng hoặc field
2. PK/FK sai
3. Data types không phù hợp
4. Thiếu constraints quan trọng
5. Relationships sai
6. SQL syntax error
7. Documentation không đầy đủ
8. Diagram không rõ ràng hoặc sai

---

## Notes
- Tham khảo `documents/DATABASE-DESIGN-EVALUATION.md` để hiểu các vấn đề của thiết kế cũ
- Focus vào Assignment System (thay Submission)
- Certificate phải có verification và template
- Notification phải đa kênh
- Bỏ hoàn toàn Thread, Post, Discussion
- Progress tracking phải tracking đầy đủ (course, module, lecture, quiz, assignment)

---

**END OF PLAN TASK**
