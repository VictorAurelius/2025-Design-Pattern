# Entity Relationship Diagram Specification - B-Learning Core

**Project**: B-Learning Core v1.0
**Author**: Nguyễn Văn Kiệt - CNTT1-K63
**Created**: 2025-11-27
**Total Tables**: 16 (giảm 48% từ v2.0)

---

## 1. OVERVIEW

### 1.1. Summary

Hệ thống database B-Learning Core được thiết kế với **16 bảng**, giảm 48% so với phiên bản v2.0 (31 bảng), tập trung vào chức năng core và dễ maintain.

### 1.2. Key Changes from v2.0

| Thay đổi | v2.0 | Core | Lý do |
|----------|------|------|-------|
| Quiz Questions | QuizQuestion table | Quiz.questions JSON | Đơn giản hóa, dễ maintain |
| Quiz Submissions | QuizSubmission table | Attempt.answers JSON | Gom data 1 attempt |
| Assignment | Assignment table | Lecture.assignment_config JSON | Logic thống nhất |
| Enrollment | 2 tables | 1 Enrollment table | class_id nullable |
| Schedule/Attendance | 2 tables | Class.schedules JSON | Giảm bảng, linh hoạt |
| Certificate | 3 tables | 1 Certificate table | Đủ dùng, đơn giản |
| Notification | 3 tables | ❌ Bỏ | Email service external |
| GradeBook | 1 table | ❌ Bỏ | Tính động (View/Query) |
| ActivityLog | 1 table | ❌ Bỏ | Application logging |
| File | 1 table | ❌ Bỏ | Cloud storage (S3/GCS) |

---

## 2. DOMAINS & TABLES

### 2.1. Domain Structure

```
┌────────────────────────────────────────┐
│  DOMAIN 1: USER MANAGEMENT (3 bảng)   │
├────────────────────────────────────────┤
│  1. User                               │
│  2. Role                               │
│  3. UserRole                           │
└────────────────────────────────────────┘

┌────────────────────────────────────────┐
│  DOMAIN 2: COURSE CONTENT (4 bảng)    │
├────────────────────────────────────────┤
│  4. Course                             │
│  5. Module                             │
│  6. Lecture                            │
│  7. Resource                           │
└────────────────────────────────────────┘

┌────────────────────────────────────────┐
│  DOMAIN 3: ASSESSMENT (5 bảng)        │
├────────────────────────────────────────┤
│  8. Quiz                               │
│  9. Question                           │
│  10. Option                            │
│  11. Attempt                           │
│  12. AssignmentSubmission              │
└────────────────────────────────────────┘

┌────────────────────────────────────────┐
│  DOMAIN 4: ENROLLMENT & PROGRESS (2)  │
├────────────────────────────────────────┤
│  13. Enrollment                        │
│  14. Progress                          │
└────────────────────────────────────────┘

┌────────────────────────────────────────┐
│  DOMAIN 5: CLASS & CERTIFICATE (2)    │
├────────────────────────────────────────┤
│  15. Class                             │
│  16. Certificate                       │
└────────────────────────────────────────┘
```

---

## 3. TABLE DEFINITIONS

### DOMAIN 1: USER MANAGEMENT

#### 1. User
- **PK**: user_id (UUID)
- **Key Fields**: email (UNIQUE), password_hash, first_name, last_name
- **Special**: preferences (JSON) - lưu notification preferences, locale, timezone
- **Status**: account_status (PENDING_VERIFICATION, ACTIVE, SUSPENDED, DELETED)

#### 2. Role
- **PK**: role_id (UUID)
- **Key Fields**: name (UNIQUE) - STUDENT, INSTRUCTOR, TA, ADMIN
- **Special**: permissions (JSON) - danh sách quyền hạn

#### 3. UserRole
- **PK**: user_role_id (UUID)
- **FK**: user_id → User, role_id → Role
- **UNIQUE**: (user_id, role_id)
- **Special**: expires_at - cho role tạm thời

### DOMAIN 2: COURSE CONTENT

#### 4. Course
- **PK**: course_id (UUID)
- **Key Fields**: code (UNIQUE), title, status (DRAFT, PUBLISHED, ARCHIVED)
- **FK**: created_by → User

#### 5. Module
- **PK**: module_id (UUID)
- **FK**: course_id → Course (CASCADE)
- **UNIQUE**: (course_id, order_num)
- **Special**: prerequisite_module_ids (UUID[]) - modules cần học trước

#### 6. Lecture
- **PK**: lecture_id (UUID)
- **FK**: module_id → Module (CASCADE)
- **UNIQUE**: (module_id, order_num)
- **Type**: VIDEO, PDF, SLIDE, AUDIO, TEXT, **ASSIGNMENT**
- **⭐ Special**: assignment_config (JSON) - chỉ dùng khi type='ASSIGNMENT'

#### 7. Resource
- **PK**: resource_id (UUID)
- **FK**: lecture_id → Lecture (CASCADE)
- **Key Fields**: file_url, file_type, file_size_bytes

### DOMAIN 3: ASSESSMENT

#### 8. Quiz
- **PK**: quiz_id (UUID)
- **FK**: course_id → Course (CASCADE), created_by → User
- **⭐ Special**: questions (JSON) - thay vì QuizQuestion table
  ```json
  [{"question_id": "uuid", "points": 10, "order": 1}]
  ```

#### 9. Question
- **PK**: question_id (UUID)
- **FK**: course_id → Course (CASCADE), created_by → User
- **Type**: MCQ, TRUE_FALSE, ESSAY, SHORT_ANSWER

#### 10. Option
- **PK**: option_id (UUID)
- **FK**: question_id → Question (CASCADE)
- **UNIQUE**: (question_id, order_num)
- **Key Fields**: is_correct (BOOLEAN)

#### 11. Attempt
- **PK**: attempt_id (UUID)
- **FK**: quiz_id → Quiz (CASCADE), user_id → User (CASCADE), enrollment_id → Enrollment (CASCADE)
- **UNIQUE**: (user_id, quiz_id, attempt_number)
- **⭐ Special**: answers (JSON) - thay vì QuizSubmission table
  ```json
  [{"question_id": "uuid", "answer_text": "...", "score": 10}]
  ```

#### 12. AssignmentSubmission
- **PK**: submission_id (UUID)
- **FK**: lecture_id → Lecture (CASCADE), user_id → User (CASCADE), enrollment_id → Enrollment (CASCADE)
- **UNIQUE**: (lecture_id, user_id, submission_number)
- **Special**: file_urls (JSON) - array of S3/GCS URLs

### DOMAIN 4: ENROLLMENT & PROGRESS

#### 13. Enrollment
- **PK**: enrollment_id (UUID)
- **FK**: user_id → User (CASCADE), course_id → Course (CASCADE), class_id → Class (SET NULL - **NULLABLE**)
- **UNIQUE**: (user_id, course_id, COALESCE(class_id, UUID_NIL))
- **⭐ Special**: class_id nullable - NULL cho self-paced, UUID cho blended learning

#### 14. Progress
- **PK**: progress_id (UUID)
- **FK**: user_id → User (CASCADE), course_id → Course (CASCADE), module_id → Module (CASCADE)
- **UNIQUE**: (user_id, course_id, module_id)
- **Status**: NOT_STARTED, IN_PROGRESS, COMPLETED
- **⭐ Note**: Chỉ track ở module level (không track từng lecture)

### DOMAIN 5: CLASS & CERTIFICATE

#### 15. Class
- **PK**: class_id (UUID)
- **FK**: course_id → Course (CASCADE), instructor_id → User (SET NULL)
- **Status**: SCHEDULED, ONGOING, COMPLETED, CANCELLED
- **⭐ Special**: schedules (JSON) - thay vì Schedule + Attendance tables
  ```json
  [{
    "date": "2025-12-01",
    "start_time": "09:00",
    "attendances": [{"user_id": "...", "status": "PRESENT"}]
  }]
  ```

#### 16. Certificate
- **PK**: certificate_id (UUID)
- **FK**: user_id → User (CASCADE), course_id → Course (CASCADE)
- **UNIQUE**: (user_id, course_id)
- **Key Fields**: certificate_code (UNIQUE), verification_code (UNIQUE)
- **Status**: ACTIVE, REVOKED

---

## 4. RELATIONSHIPS MATRIX

| From Table | To Table | Relationship | Cardinality | FK Column | ON DELETE |
|------------|----------|--------------|-------------|-----------|-----------|
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

**Total**: 23 relationships (giảm từ 45+ ở v2.0)

---

## 5. JSON STRUCTURES

### 5.1. User.preferences
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

### 5.2. Quiz.questions
```json
[
  {
    "question_id": "550e8400-e29b-41d4-a716-446655440000",
    "points": 10,
    "order": 1
  },
  {
    "question_id": "550e8400-e29b-41d4-a716-446655440001",
    "points": 15,
    "order": 2
  }
]
```

### 5.3. Attempt.answers
```json
[
  {
    "question_id": "550e8400-e29b-41d4-a716-446655440000",
    "answer_text": "Paris",
    "selected_options": ["option-uuid-a"],
    "score": 10,
    "max_score": 10,
    "is_correct": true,
    "graded_at": "2025-11-27T10:45:00Z"
  }
]
```

### 5.4. Lecture.assignment_config
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

### 5.5. Class.schedules
```json
[
  {
    "session_id": "uuid",
    "date": "2025-12-01",
    "start_time": "09:00",
    "end_time": "11:00",
    "topic": "Chương 1",
    "location": "Phòng 101",
    "type": "IN_PERSON",
    "attendances": [
      {"user_id": "uuid1", "status": "PRESENT", "check_in": "09:05"},
      {"user_id": "uuid2", "status": "LATE", "check_in": "09:15"}
    ]
  }
]
```

---

## 6. INDEXES STRATEGY

### 6.1. Primary Key Indexes
Tự động tạo cho tất cả UUID primary keys.

### 6.2. Foreign Key Indexes
```sql
CREATE INDEX idx_userrole_user ON "UserRole"(user_id);
CREATE INDEX idx_module_course ON "Module"(course_id);
CREATE INDEX idx_lecture_module ON "Lecture"(module_id);
-- ... (tất cả FK indexes)
```

### 6.3. Performance Indexes
```sql
CREATE INDEX idx_user_email ON "User"(email);
CREATE INDEX idx_course_status ON "Course"(status);
CREATE INDEX idx_enrollment_user_course ON "Enrollment"(user_id, course_id);
```

### 6.4. JSON GIN Indexes
```sql
CREATE INDEX idx_quiz_questions ON "Quiz" USING GIN (questions);
CREATE INDEX idx_attempt_answers ON "Attempt" USING GIN (answers);
CREATE INDEX idx_class_schedules ON "Class" USING GIN (schedules);
CREATE INDEX idx_user_preferences ON "User" USING GIN (preferences);
```

### 6.5. Full-Text Search
```sql
CREATE INDEX idx_course_search ON "Course" USING GIN(
  to_tsvector('english', COALESCE(title, '') || ' ' || COALESCE(description, ''))
);
```

---

## 7. CONSTRAINTS

### 7.1. CHECK Constraints
```sql
-- User
CONSTRAINT chk_user_account_status CHECK (account_status IN (
  'PENDING_VERIFICATION', 'ACTIVE', 'SUSPENDED', 'DELETED'
))

-- Course
CONSTRAINT chk_course_status CHECK (status IN ('DRAFT', 'PUBLISHED', 'ARCHIVED'))
CONSTRAINT chk_difficulty CHECK (difficulty_level IN ('BEGINNER', 'INTERMEDIATE', 'ADVANCED'))

-- Lecture
CONSTRAINT chk_lecture_type CHECK (type IN (
  'VIDEO', 'PDF', 'SLIDE', 'AUDIO', 'TEXT', 'ASSIGNMENT'
))

-- Progress
CONSTRAINT chk_progress_status CHECK (status IN ('NOT_STARTED', 'IN_PROGRESS', 'COMPLETED'))
```

### 7.2. UNIQUE Constraints
```sql
UNIQUE(user_id, role_id)  -- UserRole
UNIQUE(course_id, order_num)  -- Module
UNIQUE(module_id, order_num)  -- Lecture
UNIQUE(question_id, order_num)  -- Option
UNIQUE(user_id, quiz_id, attempt_number)  -- Attempt
UNIQUE(user_id, course_id, COALESCE(class_id, UUID_NIL))  -- Enrollment
UNIQUE(user_id, course_id, module_id)  -- Progress
UNIQUE(user_id, course_id)  -- Certificate
```

---

## 8. DATA TYPES

### 8.1. Primary Keys
- **UUID**: Tất cả primary keys
- **Lợi ích**: Distributed-friendly, không conflict

### 8.2. Timestamps
- **TIMESTAMP**: created_at, updated_at, etc.
- **DATE**: issue_date, start_date, end_date

### 8.3. Text Fields
- **VARCHAR(n)**: Giới hạn độ dài (email, code, name)
- **TEXT**: Không giới hạn (description, feedback)

### 8.4. JSON Fields
- **JSON/JSONB**: PostgreSQL native JSON
- **Lợi ích**: Flexible, có thể query với operators (@>, ->>, #>)

---

## 9. VALIDATION

### 9.1. Data Integrity
- ✅ Tất cả FK có ON DELETE behavior hợp lý
- ✅ UNIQUE constraints đảm bảo không duplicate
- ✅ CHECK constraints validate enum values
- ✅ NOT NULL cho các trường bắt buộc

### 9.2. JSON Validation
- ⚠️ JSON structure validation ở application layer
- ⚠️ Có thể dùng triggers để validate JSON format

### 9.3. Business Rules
- ✅ Prerequisite modules validate ở application
- ✅ Assignment due date validation ở application
- ✅ Enrollment permissions check ở application

---

**END OF ERD SPECIFICATION**
