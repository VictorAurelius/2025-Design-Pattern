-- ============================================
-- 03-constraints.sql - Foreign Keys & Constraints
-- B-Learning Core v1.0
-- Nguyễn Văn Kiệt - CNTT1-K63
-- Created: 2025-11-27
-- ============================================

-- LƯU Ý QUAN TRỌNG:
-- File này chứa tất cả constraints dưới dạng ALTER TABLE statements.
-- Có 2 cách sử dụng:
--
-- CÁCH 1: Chạy sau 01-schema.sql (nếu 01-schema.sql không có constraints inline)
--   - Tạo tables trống trước
--   - Load data
--   - Sau đó chạy file này để add constraints
--
-- CÁCH 2: Sử dụng độc lập để add/modify constraints
--   - Có thể drop và recreate constraints mà không ảnh hưởng data
--   - Hữu ích cho migration và maintenance
--
-- Hiện tại 01-schema.sql đã có constraints inline, nên file này
-- là OPTIONAL hoặc dùng cho reference/maintenance.

-- ============================================
-- SECTION 1: FOREIGN KEY CONSTRAINTS
-- ============================================

-- --------------------------------------------
-- Domain 1: User Management
-- --------------------------------------------

-- UserRole → User
ALTER TABLE "UserRole"
  ADD CONSTRAINT fk_userrole_user
  FOREIGN KEY (user_id)
  REFERENCES "User"(user_id)
  ON DELETE CASCADE;

-- UserRole → Role
ALTER TABLE "UserRole"
  ADD CONSTRAINT fk_userrole_role
  FOREIGN KEY (role_id)
  REFERENCES "Role"(role_id)
  ON DELETE CASCADE;

-- --------------------------------------------
-- Domain 2: Course Content
-- --------------------------------------------

-- Course → User (creator)
ALTER TABLE "Course"
  ADD CONSTRAINT fk_course_creator
  FOREIGN KEY (created_by)
  REFERENCES "User"(user_id)
  ON DELETE SET NULL;

-- Module → Course
ALTER TABLE "Module"
  ADD CONSTRAINT fk_module_course
  FOREIGN KEY (course_id)
  REFERENCES "Course"(course_id)
  ON DELETE CASCADE;

-- Lecture → Module
ALTER TABLE "Lecture"
  ADD CONSTRAINT fk_lecture_module
  FOREIGN KEY (module_id)
  REFERENCES "Module"(module_id)
  ON DELETE CASCADE;

-- Resource → Lecture
ALTER TABLE "Resource"
  ADD CONSTRAINT fk_resource_lecture
  FOREIGN KEY (lecture_id)
  REFERENCES "Lecture"(lecture_id)
  ON DELETE CASCADE;

-- --------------------------------------------
-- Domain 3: Assessment
-- --------------------------------------------

-- Quiz → Course
ALTER TABLE "Quiz"
  ADD CONSTRAINT fk_quiz_course
  FOREIGN KEY (course_id)
  REFERENCES "Course"(course_id)
  ON DELETE CASCADE;

-- Quiz → User (creator)
ALTER TABLE "Quiz"
  ADD CONSTRAINT fk_quiz_creator
  FOREIGN KEY (created_by)
  REFERENCES "User"(user_id)
  ON DELETE SET NULL;

-- Question → Course
ALTER TABLE "Question"
  ADD CONSTRAINT fk_question_course
  FOREIGN KEY (course_id)
  REFERENCES "Course"(course_id)
  ON DELETE CASCADE;

-- Question → User (creator)
ALTER TABLE "Question"
  ADD CONSTRAINT fk_question_creator
  FOREIGN KEY (created_by)
  REFERENCES "User"(user_id)
  ON DELETE SET NULL;

-- Option → Question
ALTER TABLE "Option"
  ADD CONSTRAINT fk_option_question
  FOREIGN KEY (question_id)
  REFERENCES "Question"(question_id)
  ON DELETE CASCADE;

-- Attempt → Quiz
ALTER TABLE "Attempt"
  ADD CONSTRAINT fk_attempt_quiz
  FOREIGN KEY (quiz_id)
  REFERENCES "Quiz"(quiz_id)
  ON DELETE CASCADE;

-- Attempt → User
ALTER TABLE "Attempt"
  ADD CONSTRAINT fk_attempt_user
  FOREIGN KEY (user_id)
  REFERENCES "User"(user_id)
  ON DELETE CASCADE;

-- Attempt → Enrollment
ALTER TABLE "Attempt"
  ADD CONSTRAINT fk_attempt_enrollment
  FOREIGN KEY (enrollment_id)
  REFERENCES "Enrollment"(enrollment_id)
  ON DELETE CASCADE;

-- AssignmentSubmission → Lecture
ALTER TABLE "AssignmentSubmission"
  ADD CONSTRAINT fk_assignment_lecture
  FOREIGN KEY (lecture_id)
  REFERENCES "Lecture"(lecture_id)
  ON DELETE CASCADE;

-- AssignmentSubmission → User
ALTER TABLE "AssignmentSubmission"
  ADD CONSTRAINT fk_assignment_user
  FOREIGN KEY (user_id)
  REFERENCES "User"(user_id)
  ON DELETE CASCADE;

-- AssignmentSubmission → Enrollment
ALTER TABLE "AssignmentSubmission"
  ADD CONSTRAINT fk_assignment_enrollment
  FOREIGN KEY (enrollment_id)
  REFERENCES "Enrollment"(enrollment_id)
  ON DELETE CASCADE;

-- --------------------------------------------
-- Domain 4: Enrollment & Progress
-- --------------------------------------------

-- Enrollment → User
ALTER TABLE "Enrollment"
  ADD CONSTRAINT fk_enrollment_user
  FOREIGN KEY (user_id)
  REFERENCES "User"(user_id)
  ON DELETE CASCADE;

-- Enrollment → Course
ALTER TABLE "Enrollment"
  ADD CONSTRAINT fk_enrollment_course
  FOREIGN KEY (course_id)
  REFERENCES "Course"(course_id)
  ON DELETE CASCADE;

-- Enrollment → Class (nullable - cho blended learning)
ALTER TABLE "Enrollment"
  ADD CONSTRAINT fk_enrollment_class
  FOREIGN KEY (class_id)
  REFERENCES "Class"(class_id)
  ON DELETE SET NULL;

-- Progress → User
ALTER TABLE "Progress"
  ADD CONSTRAINT fk_progress_user
  FOREIGN KEY (user_id)
  REFERENCES "User"(user_id)
  ON DELETE CASCADE;

-- Progress → Course
ALTER TABLE "Progress"
  ADD CONSTRAINT fk_progress_course
  FOREIGN KEY (course_id)
  REFERENCES "Course"(course_id)
  ON DELETE CASCADE;

-- Progress → Module
ALTER TABLE "Progress"
  ADD CONSTRAINT fk_progress_module
  FOREIGN KEY (module_id)
  REFERENCES "Module"(module_id)
  ON DELETE CASCADE;

-- --------------------------------------------
-- Domain 5: Class & Certificate
-- --------------------------------------------

-- Class → Course
ALTER TABLE "Class"
  ADD CONSTRAINT fk_class_course
  FOREIGN KEY (course_id)
  REFERENCES "Course"(course_id)
  ON DELETE CASCADE;

-- Class → User (instructor, nullable)
ALTER TABLE "Class"
  ADD CONSTRAINT fk_class_instructor
  FOREIGN KEY (instructor_id)
  REFERENCES "User"(user_id)
  ON DELETE SET NULL;

-- Certificate → User
ALTER TABLE "Certificate"
  ADD CONSTRAINT fk_certificate_user
  FOREIGN KEY (user_id)
  REFERENCES "User"(user_id)
  ON DELETE CASCADE;

-- Certificate → Course
ALTER TABLE "Certificate"
  ADD CONSTRAINT fk_certificate_course
  FOREIGN KEY (course_id)
  REFERENCES "Course"(course_id)
  ON DELETE CASCADE;

-- ============================================
-- SECTION 2: UNIQUE CONSTRAINTS
-- ============================================

-- User
ALTER TABLE "User"
  ADD CONSTRAINT uq_user_email UNIQUE (email);

-- Role
ALTER TABLE "Role"
  ADD CONSTRAINT uq_role_name UNIQUE (name);

-- UserRole - một user không thể có cùng role 2 lần
ALTER TABLE "UserRole"
  ADD CONSTRAINT uq_userrole_user_role UNIQUE (user_id, role_id);

-- Course
ALTER TABLE "Course"
  ADD CONSTRAINT uq_course_code UNIQUE (code);

-- Module - order_num phải unique trong 1 course
ALTER TABLE "Module"
  ADD CONSTRAINT uq_module_course_order UNIQUE (course_id, order_num);

-- Lecture - order_num phải unique trong 1 module
ALTER TABLE "Lecture"
  ADD CONSTRAINT uq_lecture_module_order UNIQUE (module_id, order_num);

-- Option - order_num phải unique trong 1 question
ALTER TABLE "Option"
  ADD CONSTRAINT uq_option_question_order UNIQUE (question_id, order_num);

-- Attempt - một user không thể có 2 attempts cùng attempt_number cho 1 quiz
ALTER TABLE "Attempt"
  ADD CONSTRAINT uq_attempt_user_quiz_number UNIQUE (user_id, quiz_id, attempt_number);

-- AssignmentSubmission - một user không thể có 2 submissions cùng số cho 1 lecture
ALTER TABLE "AssignmentSubmission"
  ADD CONSTRAINT uq_assignment_user_lecture_number UNIQUE (lecture_id, user_id, submission_number);

-- Enrollment - một user không thể enroll vào cùng course+class 2 lần
-- Dùng COALESCE để handle nullable class_id
ALTER TABLE "Enrollment"
  ADD CONSTRAINT uq_enrollment_user_course_class
  UNIQUE (user_id, course_id, COALESCE(class_id, '00000000-0000-0000-0000-000000000000'::UUID));

-- Progress - một user chỉ có 1 progress record cho mỗi module
ALTER TABLE "Progress"
  ADD CONSTRAINT uq_progress_user_course_module UNIQUE (user_id, course_id, module_id);

-- Certificate - một user chỉ có 1 certificate cho mỗi course
ALTER TABLE "Certificate"
  ADD CONSTRAINT uq_certificate_user_course UNIQUE (user_id, course_id);

ALTER TABLE "Certificate"
  ADD CONSTRAINT uq_certificate_code UNIQUE (certificate_code);

ALTER TABLE "Certificate"
  ADD CONSTRAINT uq_certificate_verification UNIQUE (verification_code);

-- ============================================
-- SECTION 3: CHECK CONSTRAINTS
-- ============================================

-- --------------------------------------------
-- User Table
-- --------------------------------------------

ALTER TABLE "User"
  ADD CONSTRAINT chk_user_account_status CHECK (
    account_status IN ('PENDING_VERIFICATION', 'ACTIVE', 'SUSPENDED', 'DELETED')
  );

ALTER TABLE "User"
  ADD CONSTRAINT chk_user_email_format CHECK (
    email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$'
  );

ALTER TABLE "User"
  ADD CONSTRAINT chk_user_name_not_empty CHECK (
    LENGTH(TRIM(first_name)) > 0 AND LENGTH(TRIM(last_name)) > 0
  );

-- --------------------------------------------
-- UserRole Table
-- --------------------------------------------

ALTER TABLE "UserRole"
  ADD CONSTRAINT chk_userrole_expires_future CHECK (
    expires_at IS NULL OR expires_at > assigned_at
  );

-- --------------------------------------------
-- Course Table
-- --------------------------------------------

ALTER TABLE "Course"
  ADD CONSTRAINT chk_course_status CHECK (
    status IN ('DRAFT', 'PUBLISHED', 'ARCHIVED')
  );

ALTER TABLE "Course"
  ADD CONSTRAINT chk_course_difficulty CHECK (
    difficulty_level IN ('BEGINNER', 'INTERMEDIATE', 'ADVANCED')
  );

ALTER TABLE "Course"
  ADD CONSTRAINT chk_course_credits_positive CHECK (
    credits IS NULL OR credits > 0
  );

ALTER TABLE "Course"
  ADD CONSTRAINT chk_course_code_format CHECK (
    code ~ '^[A-Z0-9]{3,10}$'
  );

-- --------------------------------------------
-- Module Table
-- --------------------------------------------

ALTER TABLE "Module"
  ADD CONSTRAINT chk_module_order_positive CHECK (
    order_num > 0
  );

ALTER TABLE "Module"
  ADD CONSTRAINT chk_module_duration_positive CHECK (
    estimated_duration_minutes IS NULL OR estimated_duration_minutes > 0
  );

-- --------------------------------------------
-- Lecture Table
-- --------------------------------------------

ALTER TABLE "Lecture"
  ADD CONSTRAINT chk_lecture_type CHECK (
    type IN ('VIDEO', 'PDF', 'SLIDE', 'AUDIO', 'TEXT', 'ASSIGNMENT')
  );

ALTER TABLE "Lecture"
  ADD CONSTRAINT chk_lecture_order_positive CHECK (
    order_num > 0
  );

ALTER TABLE "Lecture"
  ADD CONSTRAINT chk_lecture_duration_positive CHECK (
    duration_minutes IS NULL OR duration_minutes > 0
  );

-- Assignment config chỉ cho type = 'ASSIGNMENT'
ALTER TABLE "Lecture"
  ADD CONSTRAINT chk_lecture_assignment_config CHECK (
    (type = 'ASSIGNMENT' AND assignment_config IS NOT NULL) OR
    (type != 'ASSIGNMENT' AND assignment_config IS NULL)
  );

-- --------------------------------------------
-- Resource Table
-- --------------------------------------------

ALTER TABLE "Resource"
  ADD CONSTRAINT chk_resource_filesize_positive CHECK (
    file_size_bytes IS NULL OR file_size_bytes > 0
  );

-- --------------------------------------------
-- Quiz Table
-- --------------------------------------------

ALTER TABLE "Quiz"
  ADD CONSTRAINT chk_quiz_status CHECK (
    status IN ('DRAFT', 'PUBLISHED', 'ARCHIVED')
  );

ALTER TABLE "Quiz"
  ADD CONSTRAINT chk_quiz_duration_positive CHECK (
    duration_minutes IS NULL OR duration_minutes > 0
  );

ALTER TABLE "Quiz"
  ADD CONSTRAINT chk_quiz_points_positive CHECK (
    total_points IS NULL OR total_points > 0
  );

ALTER TABLE "Quiz"
  ADD CONSTRAINT chk_quiz_passing_positive CHECK (
    passing_score IS NULL OR passing_score >= 0
  );

ALTER TABLE "Quiz"
  ADD CONSTRAINT chk_quiz_passing_max CHECK (
    passing_score IS NULL OR total_points IS NULL OR passing_score <= total_points
  );

ALTER TABLE "Quiz"
  ADD CONSTRAINT chk_quiz_attempts_positive CHECK (
    max_attempts IS NULL OR max_attempts > 0
  );

ALTER TABLE "Quiz"
  ADD CONSTRAINT chk_quiz_available_dates CHECK (
    available_from IS NULL OR available_until IS NULL OR available_from < available_until
  );

-- --------------------------------------------
-- Question Table
-- --------------------------------------------

ALTER TABLE "Question"
  ADD CONSTRAINT chk_question_type CHECK (
    type IN ('MCQ', 'TRUE_FALSE', 'ESSAY', 'SHORT_ANSWER')
  );

ALTER TABLE "Question"
  ADD CONSTRAINT chk_question_points_positive CHECK (
    default_points IS NULL OR default_points > 0
  );

-- --------------------------------------------
-- Option Table
-- --------------------------------------------

ALTER TABLE "Option"
  ADD CONSTRAINT chk_option_order_positive CHECK (
    order_num > 0
  );

-- --------------------------------------------
-- Attempt Table
-- --------------------------------------------

ALTER TABLE "Attempt"
  ADD CONSTRAINT chk_attempt_status CHECK (
    status IN ('IN_PROGRESS', 'SUBMITTED', 'GRADED', 'PENDING_GRADING')
  );

ALTER TABLE "Attempt"
  ADD CONSTRAINT chk_attempt_number_positive CHECK (
    attempt_number > 0
  );

ALTER TABLE "Attempt"
  ADD CONSTRAINT chk_attempt_score_range CHECK (
    score IS NULL OR (score >= 0 AND score <= max_score)
  );

ALTER TABLE "Attempt"
  ADD CONSTRAINT chk_attempt_dates CHECK (
    submitted_at IS NULL OR submitted_at >= started_at
  );

-- --------------------------------------------
-- AssignmentSubmission Table
-- --------------------------------------------

ALTER TABLE "AssignmentSubmission"
  ADD CONSTRAINT chk_assignment_status CHECK (
    status IN ('DRAFT', 'SUBMITTED', 'GRADED', 'PENDING_GRADING', 'LATE')
  );

ALTER TABLE "AssignmentSubmission"
  ADD CONSTRAINT chk_assignment_number_positive CHECK (
    submission_number > 0
  );

ALTER TABLE "AssignmentSubmission"
  ADD CONSTRAINT chk_assignment_score_range CHECK (
    score IS NULL OR (score >= 0 AND score <= max_score)
  );

-- --------------------------------------------
-- Enrollment Table
-- --------------------------------------------

ALTER TABLE "Enrollment"
  ADD CONSTRAINT chk_enrollment_status CHECK (
    status IN ('ACTIVE', 'COMPLETED', 'DROPPED', 'SUSPENDED')
  );

ALTER TABLE "Enrollment"
  ADD CONSTRAINT chk_enrollment_dates CHECK (
    completed_at IS NULL OR completed_at >= enrolled_at
  );

-- --------------------------------------------
-- Progress Table
-- --------------------------------------------

ALTER TABLE "Progress"
  ADD CONSTRAINT chk_progress_status CHECK (
    status IN ('NOT_STARTED', 'IN_PROGRESS', 'COMPLETED')
  );

ALTER TABLE "Progress"
  ADD CONSTRAINT chk_progress_percentage CHECK (
    completion_percentage >= 0 AND completion_percentage <= 100
  );

ALTER TABLE "Progress"
  ADD CONSTRAINT chk_progress_dates CHECK (
    completed_at IS NULL OR completed_at >= started_at
  );

-- --------------------------------------------
-- Class Table
-- --------------------------------------------

ALTER TABLE "Class"
  ADD CONSTRAINT chk_class_status CHECK (
    status IN ('SCHEDULED', 'ONGOING', 'COMPLETED', 'CANCELLED')
  );

ALTER TABLE "Class"
  ADD CONSTRAINT chk_class_dates CHECK (
    end_date >= start_date
  );

ALTER TABLE "Class"
  ADD CONSTRAINT chk_class_capacity_positive CHECK (
    max_students IS NULL OR max_students > 0
  );

-- --------------------------------------------
-- Certificate Table
-- --------------------------------------------

ALTER TABLE "Certificate"
  ADD CONSTRAINT chk_certificate_status CHECK (
    status IN ('ACTIVE', 'REVOKED')
  );

ALTER TABLE "Certificate"
  ADD CONSTRAINT chk_certificate_grade_range CHECK (
    final_grade IS NULL OR (final_grade >= 0 AND final_grade <= 100)
  );

-- ============================================
-- SECTION 4: NOT NULL CONSTRAINTS
-- ============================================
-- (Đã được định nghĩa trong 01-schema.sql với NOT NULL)
-- Section này dành cho reference nếu cần modify

-- ============================================
-- SECTION 5: DEFAULT CONSTRAINTS
-- ============================================
-- (Đã được định nghĩa trong 01-schema.sql với DEFAULT)
-- Section này dành cho reference nếu cần modify

-- ============================================
-- COMMENTS
-- ============================================

COMMENT ON CONSTRAINT fk_enrollment_class ON "Enrollment" IS
'Foreign key nullable - NULL cho self-paced learning, UUID cho blended learning';

COMMENT ON CONSTRAINT uq_enrollment_user_course_class ON "Enrollment" IS
'Unique constraint với COALESCE - user không thể enroll vào cùng course+class 2 lần';

COMMENT ON CONSTRAINT chk_lecture_assignment_config ON "Lecture" IS
'Assignment config chỉ có giá trị khi lecture type = ASSIGNMENT';

COMMENT ON CONSTRAINT chk_quiz_passing_max ON "Quiz" IS
'Passing score không được vượt quá total points';

COMMENT ON CONSTRAINT chk_quiz_available_dates ON "Quiz" IS
'Available from phải trước available until';

COMMENT ON CONSTRAINT chk_user_email_format ON "User" IS
'Validate email format với regex';

-- ============================================
-- STATISTICS
-- ============================================

-- Total constraints:
-- - Foreign Keys: 27
-- - Unique: 14
-- - Check: 47
-- TOTAL: 88 constraints

-- Cascade Behaviors:
-- - ON DELETE CASCADE: 23 (majority - khi parent xóa thì xóa children)
-- - ON DELETE SET NULL: 4 (cho relationships không bắt buộc)
-- - ON DELETE RESTRICT: 0 (không dùng - quá strict)

-- ============================================
-- USAGE NOTES
-- ============================================

/*
## Cách sử dụng file này:

### 1. Add constraints sau khi create tables:
```sql
-- Bước 1: Tạo tables (có thể bỏ constraints inline)
\i 01-schema.sql

-- Bước 2: Load data (nếu cần)

-- Bước 3: Add constraints
\i 03-constraints.sql
```

### 2. Drop và recreate constraints:
```sql
-- Drop một constraint
ALTER TABLE "Enrollment" DROP CONSTRAINT fk_enrollment_class;

-- Recreate
ALTER TABLE "Enrollment"
  ADD CONSTRAINT fk_enrollment_class
  FOREIGN KEY (class_id) REFERENCES "Class"(class_id)
  ON DELETE SET NULL;
```

### 3. Disable/Enable constraints (PostgreSQL):
```sql
-- Disable triggers (bao gồm FK checks)
ALTER TABLE "Enrollment" DISABLE TRIGGER ALL;

-- Load data...

-- Enable lại
ALTER TABLE "Enrollment" ENABLE TRIGGER ALL;
```

### 4. Kiểm tra constraints:
```sql
-- List all constraints của 1 table
SELECT conname, contype, pg_get_constraintdef(oid)
FROM pg_constraint
WHERE conrelid = '"Enrollment"'::regclass;

-- contype: 'p' = primary key, 'f' = foreign key, 'u' = unique, 'c' = check
```

## Best Practices:

1. ✅ Luôn đặt tên constraints (dễ drop/modify sau này)
2. ✅ Dùng CASCADE cho relationships chặt chẽ (Quiz → Question)
3. ✅ Dùng SET NULL cho optional relationships (Class → Instructor)
4. ✅ Validate data logic với CHECK constraints
5. ⚠️  Cẩn thận với CASCADE - có thể xóa nhiều data không mong muốn
6. ⚠️  Test constraints với sample data trước khi production

*/

-- ============================================
-- END OF CONSTRAINTS
-- ============================================
