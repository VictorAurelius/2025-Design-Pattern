# Database Schema Specification

**Project**: B-Learning System Database v2.0
**Author**: Nguyễn Văn Kiệt - CNTT1-K63
**Created**: 2025-11-25
**Database**: PostgreSQL 14+
**Encoding**: UTF8
**Total Tables**: 31

---

## 1. OVERVIEW

This document contains the complete Data Definition Language (DDL) for the B-Learning database system. All SQL statements are compatible with **PostgreSQL 14+**.

### 1.1. Database Information
- **Database Name**: `b_learning`
- **Version**: 2.0 (Redesigned)
- **Primary Key Type**: UUID
- **Naming Convention**: PascalCase for tables, snake_case for columns
- **Timestamp Type**: TIMESTAMP (assumes UTC)
- **Character Encoding**: UTF8

### 1.2. Features
- ✅ UUID primary keys for all tables
- ✅ Foreign key constraints with appropriate CASCADE behaviors
- ✅ CHECK constraints for data validation
- ✅ UNIQUE constraints for business rules
- ✅ Indexes for performance optimization
- ✅ Full-text search support (GIN indexes)
- ✅ Triggers for automated business logic
- ✅ Materialized views for reporting

---

## 2. EXTENSIONS

```sql
-- ============================================
-- REQUIRED POSTGRESQL EXTENSIONS
-- ============================================

-- UUID generation (for gen_random_uuid())
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Full-text search with trigrams
CREATE EXTENSION IF NOT EXISTS "pg_trgm";

-- GIN indexes for arrays and JSON
CREATE EXTENSION IF NOT EXISTS "btree_gin";
```

---

## 3. TABLE DEFINITIONS

### 3.1. User Management Domain (3 tables)

#### 3.1.1. User

```sql
-- ============================================
-- USER TABLE
-- Purpose: User accounts with authentication
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

  CONSTRAINT chk_user_account_status CHECK (account_status IN (
    'PENDING_VERIFICATION', 'ACTIVE', 'SUSPENDED', 'DELETED'
  ))
);

COMMENT ON TABLE "User" IS 'User accounts with profile and authentication';
COMMENT ON COLUMN "User".user_id IS 'Unique user identifier (UUID)';
COMMENT ON COLUMN "User".email IS 'Email address (login credential, unique)';
COMMENT ON COLUMN "User".password_hash IS 'Hashed password (bcrypt/scrypt)';
COMMENT ON COLUMN "User".account_status IS 'Account status: PENDING_VERIFICATION, ACTIVE, SUSPENDED, DELETED';
```

#### 3.1.2. Role

```sql
-- ============================================
-- ROLE TABLE
-- Purpose: System roles (RBAC)
-- ============================================

CREATE TABLE "Role" (
  role_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  name VARCHAR(50) NOT NULL UNIQUE,
  description TEXT,
  permissions JSON,
  is_system_role BOOLEAN DEFAULT TRUE,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE "Role" IS 'System roles for RBAC';
COMMENT ON COLUMN "Role".name IS 'Role name: STUDENT, INSTRUCTOR, TA, ADMIN';
COMMENT ON COLUMN "Role".permissions IS 'JSON array of permission strings';
COMMENT ON COLUMN "Role".is_system_role IS 'TRUE for built-in roles, FALSE for custom';
```

#### 3.1.3. UserRole

```sql
-- ============================================
-- USERROLE TABLE
-- Purpose: User-Role many-to-many relationship
-- ============================================

CREATE TABLE "UserRole" (
  user_role_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID NOT NULL REFERENCES "User"(user_id) ON DELETE CASCADE,
  role_id UUID NOT NULL REFERENCES "Role"(role_id) ON DELETE CASCADE,
  granted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  granted_by UUID REFERENCES "User"(user_id) ON DELETE SET NULL,
  expires_at TIMESTAMP,

  CONSTRAINT uq_user_role UNIQUE(user_id, role_id)
);

COMMENT ON TABLE "UserRole" IS 'User role assignments';
COMMENT ON COLUMN "UserRole".granted_by IS 'Admin who granted this role';
COMMENT ON COLUMN "UserRole".expires_at IS 'Optional role expiration date';
```

---

### 3.2. Course Content Domain (4 tables)

#### 3.2.1. Course

```sql
-- ============================================
-- COURSE TABLE
-- Purpose: Course information and metadata
-- ============================================

CREATE TABLE "Course" (
  course_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  code VARCHAR(50) NOT NULL UNIQUE,
  title VARCHAR(200) NOT NULL,
  description TEXT,
  short_description VARCHAR(500),
  thumbnail_url VARCHAR(500),
  category VARCHAR(100),
  difficulty_level VARCHAR(20),
  estimated_hours DECIMAL(5,2),
  status VARCHAR(20) DEFAULT 'DRAFT',
  published_at TIMESTAMP,
  created_by UUID REFERENCES "User"(user_id) ON DELETE SET NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT chk_course_difficulty CHECK (difficulty_level IN (
    'BEGINNER', 'INTERMEDIATE', 'ADVANCED', 'EXPERT'
  )),
  CONSTRAINT chk_course_status CHECK (status IN (
    'DRAFT', 'PUBLISHED', 'ARCHIVED'
  ))
);

COMMENT ON TABLE "Course" IS 'Course catalog and metadata';
COMMENT ON COLUMN "Course".code IS 'Unique course code (e.g., CS101)';
COMMENT ON COLUMN "Course".status IS 'Course status: DRAFT, PUBLISHED, ARCHIVED';
```

#### 3.2.2. Module

```sql
-- ============================================
-- MODULE TABLE
-- Purpose: Course modules/chapters
-- ============================================

CREATE TABLE "Module" (
  module_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  course_id UUID NOT NULL REFERENCES "Course"(course_id) ON DELETE CASCADE,
  title VARCHAR(200) NOT NULL,
  description TEXT,
  order_num INT NOT NULL,
  prerequisite_module_ids UUID[],
  estimated_duration_minutes INT,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT uq_module_order UNIQUE(course_id, order_num)
);

COMMENT ON TABLE "Module" IS 'Course modules (chapters)';
COMMENT ON COLUMN "Module".order_num IS 'Display order within course (1, 2, 3...)';
COMMENT ON COLUMN "Module".prerequisite_module_ids IS 'Array of required module UUIDs';
```

#### 3.2.3. Lecture

```sql
-- ============================================
-- LECTURE TABLE
-- Purpose: Learning content (video, PDF, etc.)
-- ============================================

CREATE TABLE "Lecture" (
  lecture_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  module_id UUID NOT NULL REFERENCES "Module"(module_id) ON DELETE CASCADE,
  title VARCHAR(200) NOT NULL,
  description TEXT,
  type VARCHAR(20) NOT NULL,
  content_url VARCHAR(1024),
  duration_seconds INT,
  order_num INT NOT NULL,
  is_preview BOOLEAN DEFAULT FALSE,
  is_downloadable BOOLEAN DEFAULT TRUE,
  transcript TEXT,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT chk_lecture_type CHECK (type IN (
    'VIDEO', 'PDF', 'SLIDE', 'AUDIO', 'TEXT'
  )),
  CONSTRAINT uq_lecture_order UNIQUE(module_id, order_num)
);

COMMENT ON TABLE "Lecture" IS 'Individual learning content units';
COMMENT ON COLUMN "Lecture".type IS 'Content type: VIDEO, PDF, SLIDE, AUDIO, TEXT';
COMMENT ON COLUMN "Lecture".is_preview IS 'Allow preview without enrollment';
COMMENT ON COLUMN "Lecture".transcript IS 'Video transcript or text content';
```

#### 3.2.4. Resource

```sql
-- ============================================
-- RESOURCE TABLE
-- Purpose: Downloadable lecture resources
-- ============================================

CREATE TABLE "Resource" (
  resource_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  lecture_id UUID NOT NULL REFERENCES "Lecture"(lecture_id) ON DELETE CASCADE,
  title VARCHAR(200) NOT NULL,
  file_url VARCHAR(500) NOT NULL,
  file_type VARCHAR(100),
  file_size_bytes BIGINT,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE "Resource" IS 'Supplementary files for lectures';
COMMENT ON COLUMN "Resource".file_url IS 'URL to downloadable file';
COMMENT ON COLUMN "Resource".file_type IS 'MIME type (e.g., application/pdf)';
```

---

### 3.3. Assessment Domain (9 tables)

#### 3.3.1. Quiz

```sql
-- ============================================
-- QUIZ TABLE
-- Purpose: Quiz/test configuration
-- ============================================

CREATE TABLE "Quiz" (
  quiz_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  course_id UUID NOT NULL REFERENCES "Course"(course_id) ON DELETE CASCADE,
  title VARCHAR(200) NOT NULL,
  description TEXT,
  time_limit_minutes INT,
  attempt_limit INT,
  pass_score DECIMAL(5,2),
  shuffle_questions BOOLEAN DEFAULT FALSE,
  show_correct_answers BOOLEAN DEFAULT TRUE,
  is_published BOOLEAN DEFAULT FALSE,
  created_by UUID REFERENCES "User"(user_id) ON DELETE SET NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE "Quiz" IS 'Quiz configuration and settings';
COMMENT ON COLUMN "Quiz".time_limit_minutes IS 'Time limit (0 = unlimited)';
COMMENT ON COLUMN "Quiz".attempt_limit IS 'Max attempts (0 = unlimited)';
COMMENT ON COLUMN "Quiz".pass_score IS 'Minimum score to pass (percentage)';
```

#### 3.3.2. Question

```sql
-- ============================================
-- QUESTION TABLE
-- Purpose: Question bank
-- ============================================

CREATE TABLE "Question" (
  question_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  course_id UUID NOT NULL REFERENCES "Course"(course_id) ON DELETE CASCADE,
  text TEXT NOT NULL,
  type VARCHAR(20) NOT NULL,
  difficulty VARCHAR(20) DEFAULT 'MEDIUM',
  max_points DECIMAL(5,2) NOT NULL DEFAULT 1.00,
  explanation TEXT,
  is_active BOOLEAN DEFAULT TRUE,
  created_by UUID REFERENCES "User"(user_id) ON DELETE SET NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT chk_question_type CHECK (type IN (
    'MCQ', 'TRUE_FALSE', 'ESSAY', 'SHORT_ANSWER', 'CODE'
  )),
  CONSTRAINT chk_question_difficulty CHECK (difficulty IN (
    'EASY', 'MEDIUM', 'HARD'
  ))
);

COMMENT ON TABLE "Question" IS 'Question bank (reusable across quizzes)';
COMMENT ON COLUMN "Question".type IS 'Question type: MCQ, TRUE_FALSE, ESSAY, SHORT_ANSWER, CODE';
COMMENT ON COLUMN "Question".explanation IS 'Explanation shown after answering';
```

#### 3.3.3. Option

```sql
-- ============================================
-- OPTION TABLE
-- Purpose: Answer options for MCQ/TRUE_FALSE
-- ============================================

CREATE TABLE "Option" (
  option_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  question_id UUID NOT NULL REFERENCES "Question"(question_id) ON DELETE CASCADE,
  option_text TEXT NOT NULL,
  is_correct BOOLEAN NOT NULL DEFAULT FALSE,
  order_num INT NOT NULL,
  feedback TEXT,

  CONSTRAINT uq_option_order UNIQUE(question_id, order_num)
);

COMMENT ON TABLE "Option" IS 'Answer options for MCQ and TRUE_FALSE questions';
COMMENT ON COLUMN "Option".is_correct IS 'TRUE if this is the correct answer';
COMMENT ON COLUMN "Option".feedback IS 'Feedback shown for this option';
```

#### 3.3.4. QuizQuestion

```sql
-- ============================================
-- QUIZQUESTION TABLE
-- Purpose: Quiz-Question many-to-many
-- ============================================

CREATE TABLE "QuizQuestion" (
  quiz_question_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  quiz_id UUID NOT NULL REFERENCES "Quiz"(quiz_id) ON DELETE CASCADE,
  question_id UUID NOT NULL REFERENCES "Question"(question_id) ON DELETE RESTRICT,
  points DECIMAL(5,2) NOT NULL,
  order_num INT NOT NULL,

  CONSTRAINT uq_quiz_question UNIQUE(quiz_id, question_id),
  CONSTRAINT uq_quiz_question_order UNIQUE(quiz_id, order_num)
);

COMMENT ON TABLE "QuizQuestion" IS 'Questions assigned to quizzes';
COMMENT ON COLUMN "QuizQuestion".points IS 'Points for this question in this quiz';
COMMENT ON COLUMN "QuizQuestion".order_num IS 'Question order in quiz';
```

#### 3.3.5. Attempt

```sql
-- ============================================
-- ATTEMPT TABLE
-- Purpose: Quiz attempt records (REDESIGNED)
-- ============================================

CREATE TABLE "Attempt" (
  attempt_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  quiz_id UUID NOT NULL REFERENCES "Quiz"(quiz_id) ON DELETE CASCADE,
  user_id UUID NOT NULL REFERENCES "User"(user_id) ON DELETE CASCADE,
  course_enrollment_id UUID NOT NULL REFERENCES "CourseEnrollment"(course_enrollment_id) ON DELETE CASCADE,
  class_id UUID REFERENCES "Class"(class_id) ON DELETE SET NULL,
  attempt_number INT NOT NULL,
  started_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  submitted_at TIMESTAMP,
  time_spent_seconds INT DEFAULT 0,
  status VARCHAR(20) NOT NULL DEFAULT 'IN_PROGRESS',
  auto_score DECIMAL(6,2) DEFAULT 0.00,
  manual_score DECIMAL(6,2),
  final_score DECIMAL(6,2),
  max_possible_score DECIMAL(6,2) NOT NULL,
  percentage_score DECIMAL(5,2),
  graded_at TIMESTAMP,
  graded_by UUID REFERENCES "User"(user_id) ON DELETE SET NULL,
  ip_address VARCHAR(45),

  CONSTRAINT uq_attempt UNIQUE(user_id, quiz_id, attempt_number),
  CONSTRAINT chk_attempt_status CHECK (status IN (
    'IN_PROGRESS', 'SUBMITTED', 'GRADED', 'ABANDONED'
  ))
);

COMMENT ON TABLE "Attempt" IS 'Quiz attempt records (REDESIGNED from v1.0)';
COMMENT ON COLUMN "Attempt".course_enrollment_id IS 'NEW: Links attempt to enrollment';
COMMENT ON COLUMN "Attempt".class_id IS 'CHANGED: Now optional (supports self-paced)';
COMMENT ON COLUMN "Attempt".auto_score IS 'Score from auto-graded questions (MCQ, TRUE_FALSE)';
COMMENT ON COLUMN "Attempt".manual_score IS 'Score from manually graded questions (ESSAY)';
```

#### 3.3.6. QuizSubmission

```sql
-- ============================================
-- QUIZSUBMISSION TABLE
-- Purpose: Individual question answers (RENAMED from Submission)
-- ============================================

CREATE TABLE "QuizSubmission" (
  quiz_submission_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  attempt_id UUID NOT NULL REFERENCES "Attempt"(attempt_id) ON DELETE CASCADE,
  question_id UUID NOT NULL REFERENCES "Question"(question_id) ON DELETE RESTRICT,
  answer_text TEXT,
  selected_option_ids UUID[],
  submitted_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  auto_score DECIMAL(5,2),
  manual_score DECIMAL(5,2),
  final_score DECIMAL(5,2),
  max_points DECIMAL(5,2) NOT NULL,
  instructor_feedback TEXT,
  graded_at TIMESTAMP,
  graded_by UUID REFERENCES "User"(user_id) ON DELETE SET NULL
);

COMMENT ON TABLE "QuizSubmission" IS 'Individual question answers (RENAMED from Submission)';
COMMENT ON COLUMN "QuizSubmission".answer_text IS 'Text answer for ESSAY, SHORT_ANSWER';
COMMENT ON COLUMN "QuizSubmission".selected_option_ids IS 'Array of selected option UUIDs for MCQ';
```

#### 3.3.7. Assignment

```sql
-- ============================================
-- ASSIGNMENT TABLE
-- Purpose: Assignment configuration (NEW)
-- ============================================

CREATE TABLE "Assignment" (
  assignment_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  course_id UUID NOT NULL REFERENCES "Course"(course_id) ON DELETE CASCADE,
  class_id UUID REFERENCES "Class"(class_id) ON DELETE SET NULL,
  title VARCHAR(200) NOT NULL,
  description TEXT NOT NULL,
  instructions TEXT,
  assignment_type VARCHAR(20) NOT NULL,
  max_points DECIMAL(6,2) NOT NULL,
  due_date TIMESTAMP NOT NULL,
  late_submission_allowed BOOLEAN DEFAULT TRUE,
  late_penalty_percent DECIMAL(5,2) DEFAULT 10.00,
  max_late_days INT DEFAULT 7,
  allow_resubmission BOOLEAN DEFAULT FALSE,
  max_submissions INT DEFAULT 1,
  rubric JSON,
  auto_grading_enabled BOOLEAN DEFAULT FALSE,
  test_cases JSON,
  created_by UUID REFERENCES "User"(user_id) ON DELETE SET NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT chk_assignment_type CHECK (assignment_type IN (
    'ESSAY', 'CODE', 'FILE_UPLOAD', 'PROBLEM_SET', 'PROJECT'
  )),
  CONSTRAINT chk_late_penalty CHECK (late_penalty_percent >= 0 AND late_penalty_percent <= 100)
);

COMMENT ON TABLE "Assignment" IS 'Assignment configuration (NEW - replaces old Submission)';
COMMENT ON COLUMN "Assignment".rubric IS 'JSON grading rubric';
COMMENT ON COLUMN "Assignment".test_cases IS 'JSON test cases for auto-grading (CODE type)';
```

#### 3.3.8. AssignmentSubmission

```sql
-- ============================================
-- ASSIGNMENTSUBMISSION TABLE
-- Purpose: Assignment submissions (NEW)
-- ============================================

CREATE TABLE "AssignmentSubmission" (
  assignment_submission_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  assignment_id UUID NOT NULL REFERENCES "Assignment"(assignment_id) ON DELETE CASCADE,
  user_id UUID NOT NULL REFERENCES "User"(user_id) ON DELETE CASCADE,
  course_enrollment_id UUID NOT NULL REFERENCES "CourseEnrollment"(course_enrollment_id) ON DELETE CASCADE,
  submission_number INT NOT NULL DEFAULT 1,
  submitted_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  content TEXT,
  file_urls JSON,
  code_submission TEXT,
  is_late BOOLEAN DEFAULT FALSE,
  days_late INT DEFAULT 0,
  status VARCHAR(20) NOT NULL DEFAULT 'SUBMITTED',
  auto_score DECIMAL(6,2),
  manual_score DECIMAL(6,2),
  final_score DECIMAL(6,2),
  penalty_applied DECIMAL(6,2) DEFAULT 0.00,
  rubric_scores JSON,
  feedback TEXT,
  graded_at TIMESTAMP,
  graded_by UUID REFERENCES "User"(user_id) ON DELETE SET NULL,
  version INT DEFAULT 1,

  CONSTRAINT uq_assignment_submission UNIQUE(assignment_id, user_id, submission_number),
  CONSTRAINT chk_submission_status CHECK (status IN (
    'DRAFT', 'SUBMITTED', 'GRADING', 'GRADED', 'RETURNED'
  ))
);

COMMENT ON TABLE "AssignmentSubmission" IS 'Student assignment submissions (NEW)';
COMMENT ON COLUMN "AssignmentSubmission".submission_number IS 'Submission attempt number';
COMMENT ON COLUMN "AssignmentSubmission".file_urls IS 'JSON array of uploaded file URLs';
COMMENT ON COLUMN "AssignmentSubmission".rubric_scores IS 'JSON scores for each rubric criterion';
```

#### 3.3.9. GradeBook

```sql
-- ============================================
-- GRADEBOOK TABLE
-- Purpose: Aggregated student grades (NEW)
-- ============================================

CREATE TABLE "GradeBook" (
  gradebook_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID NOT NULL REFERENCES "User"(user_id) ON DELETE CASCADE,
  course_id UUID NOT NULL REFERENCES "Course"(course_id) ON DELETE CASCADE,
  class_id UUID REFERENCES "Class"(class_id) ON DELETE SET NULL,
  quiz_score DECIMAL(6,2) DEFAULT 0.00,
  assignment_score DECIMAL(6,2) DEFAULT 0.00,
  participation_score DECIMAL(6,2) DEFAULT 0.00,
  total_score DECIMAL(6,2) DEFAULT 0.00,
  weighted_score DECIMAL(6,2) DEFAULT 0.00,
  letter_grade VARCHAR(5),
  last_updated_at TIMESTAMP,

  CONSTRAINT uq_gradebook UNIQUE(user_id, course_id, COALESCE(class_id, '00000000-0000-0000-0000-000000000000'::UUID))
);

COMMENT ON TABLE "GradeBook" IS 'Aggregated grades per student per course (NEW)';
COMMENT ON COLUMN "GradeBook".weighted_score IS 'Final weighted score based on course grading policy';
COMMENT ON COLUMN "GradeBook".letter_grade IS 'Letter grade (A, B+, B, etc.)';
```

---

### 3.4. Enrollment & Progress Domain (4 tables)

#### 3.4.1. CourseEnrollment

```sql
-- ============================================
-- COURSEENROLLMENT TABLE
-- Purpose: Course enrollment records
-- ============================================

CREATE TABLE "CourseEnrollment" (
  course_enrollment_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID NOT NULL REFERENCES "User"(user_id) ON DELETE CASCADE,
  course_id UUID NOT NULL REFERENCES "Course"(course_id) ON DELETE CASCADE,
  role_in_course VARCHAR(20) NOT NULL,
  enrollment_status VARCHAR(20) DEFAULT 'ACTIVE',
  enrolled_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  completed_at TIMESTAMP,
  final_grade DECIMAL(5,2),
  completion_percentage DECIMAL(5,2) DEFAULT 0.00,
  last_accessed_at TIMESTAMP,

  CONSTRAINT uq_course_enrollment UNIQUE(user_id, course_id),
  CONSTRAINT chk_role_in_course CHECK (role_in_course IN (
    'STUDENT', 'INSTRUCTOR', 'TA'
  )),
  CONSTRAINT chk_enrollment_status CHECK (enrollment_status IN (
    'ACTIVE', 'COMPLETED', 'DROPPED', 'SUSPENDED'
  )),
  CONSTRAINT chk_completion_percentage CHECK (
    completion_percentage >= 0 AND completion_percentage <= 100
  )
);

COMMENT ON TABLE "CourseEnrollment" IS 'Course enrollment records';
COMMENT ON COLUMN "CourseEnrollment".role_in_course IS 'User role: STUDENT, INSTRUCTOR, TA';
COMMENT ON COLUMN "CourseEnrollment".completion_percentage IS 'Overall course progress (0-100)';
```

#### 3.4.2. ClassEnrollment

```sql
-- ============================================
-- CLASSENROLLMENT TABLE
-- Purpose: Class enrollment (blended learning)
-- ============================================

CREATE TABLE "ClassEnrollment" (
  class_enrollment_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID NOT NULL REFERENCES "User"(user_id) ON DELETE CASCADE,
  class_id UUID NOT NULL REFERENCES "Class"(class_id) ON DELETE CASCADE,
  course_enrollment_id UUID REFERENCES "CourseEnrollment"(course_enrollment_id) ON DELETE CASCADE,
  role_in_class VARCHAR(20) NOT NULL,
  enrolled_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT uq_class_enrollment UNIQUE(user_id, class_id),
  CONSTRAINT chk_role_in_class CHECK (role_in_class IN (
    'STUDENT', 'INSTRUCTOR', 'TA'
  ))
);

COMMENT ON TABLE "ClassEnrollment" IS 'Class enrollment for blended learning';
COMMENT ON COLUMN "ClassEnrollment".course_enrollment_id IS 'Link to course enrollment';
```

#### 3.4.3. Progress

```sql
-- ============================================
-- PROGRESS TABLE
-- Purpose: Learning progress tracking (REDESIGNED)
-- ============================================

CREATE TABLE "Progress" (
  progress_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID NOT NULL REFERENCES "User"(user_id) ON DELETE CASCADE,
  course_id UUID NOT NULL REFERENCES "Course"(course_id) ON DELETE CASCADE,
  class_id UUID REFERENCES "Class"(class_id) ON DELETE SET NULL,
  module_id UUID NOT NULL REFERENCES "Module"(module_id) ON DELETE CASCADE,
  lecture_id UUID NOT NULL REFERENCES "Lecture"(lecture_id) ON DELETE CASCADE,
  status VARCHAR(20) NOT NULL DEFAULT 'NOT_STARTED',
  percent_complete DECIMAL(5,2) DEFAULT 0.00,
  last_position_seconds INT DEFAULT 0,
  first_accessed_at TIMESTAMP,
  last_accessed_at TIMESTAMP,
  completed_at TIMESTAMP,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT uq_progress UNIQUE(
    user_id, course_id,
    COALESCE(class_id, '00000000-0000-0000-0000-000000000000'::UUID),
    module_id, lecture_id
  ),
  CONSTRAINT chk_progress_status CHECK (status IN (
    'NOT_STARTED', 'IN_PROGRESS', 'COMPLETED', 'SKIPPED'
  )),
  CONSTRAINT chk_progress_percent CHECK (
    percent_complete >= 0 AND percent_complete <= 100
  )
);

COMMENT ON TABLE "Progress" IS 'Learning progress tracking (REDESIGNED from v1.0)';
COMMENT ON COLUMN "Progress".course_id IS 'NEW: Course-level tracking';
COMMENT ON COLUMN "Progress".module_id IS 'NEW: Module-level tracking';
COMMENT ON COLUMN "Progress".last_position_seconds IS 'Video resume position';
```

#### 3.4.4. Attendance

```sql
-- ============================================
-- ATTENDANCE TABLE
-- Purpose: In-person class attendance
-- ============================================

CREATE TABLE "Attendance" (
  attendance_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  schedule_id UUID NOT NULL REFERENCES "Schedule"(schedule_id) ON DELETE CASCADE,
  user_id UUID NOT NULL REFERENCES "User"(user_id) ON DELETE CASCADE,
  status VARCHAR(20) NOT NULL,
  check_in_time TIMESTAMP,
  notes TEXT,
  recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT uq_attendance UNIQUE(schedule_id, user_id),
  CONSTRAINT chk_attendance_status CHECK (status IN (
    'PRESENT', 'ABSENT', 'LATE', 'EXCUSED'
  ))
);

COMMENT ON TABLE "Attendance" IS 'In-person class attendance tracking';
COMMENT ON COLUMN "Attendance".check_in_time IS 'Actual check-in time';
```

---

### 3.5. Class & Blended Learning Domain (3 tables)

#### 3.5.1. Class

```sql
-- ============================================
-- CLASS TABLE
-- Purpose: Physical/hybrid class sessions
-- ============================================

CREATE TABLE "Class" (
  class_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  course_id UUID NOT NULL REFERENCES "Course"(course_id) ON DELETE CASCADE,
  instructor_id UUID REFERENCES "User"(user_id) ON DELETE SET NULL,
  name VARCHAR(100) NOT NULL,
  start_date DATE,
  end_date DATE,
  status VARCHAR(20) DEFAULT 'SCHEDULED',
  max_students INT,
  location VARCHAR(200),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT chk_class_status CHECK (status IN (
    'SCHEDULED', 'ONGOING', 'COMPLETED', 'CANCELLED'
  ))
);

COMMENT ON TABLE "Class" IS 'Physical or hybrid classes (blended learning)';
COMMENT ON COLUMN "Class".instructor_id IS 'Primary instructor for this class';
COMMENT ON COLUMN "Class".max_students IS 'Maximum enrollment capacity';
```

#### 3.5.2. Schedule

```sql
-- ============================================
-- SCHEDULE TABLE
-- Purpose: Class session schedule
-- ============================================

CREATE TABLE "Schedule" (
  schedule_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  class_id UUID NOT NULL REFERENCES "Class"(class_id) ON DELETE CASCADE,
  session_date DATE NOT NULL,
  start_time TIME NOT NULL,
  end_time TIME NOT NULL,
  location VARCHAR(200),
  topic VARCHAR(200),
  session_type VARCHAR(20) DEFAULT 'IN_PERSON',
  meeting_url VARCHAR(500),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT chk_session_type CHECK (session_type IN (
    'IN_PERSON', 'ONLINE', 'HYBRID'
  ))
);

COMMENT ON TABLE "Schedule" IS 'Class session schedule';
COMMENT ON COLUMN "Schedule".session_type IS 'IN_PERSON, ONLINE, or HYBRID';
COMMENT ON COLUMN "Schedule".meeting_url IS 'Video conference URL (for ONLINE/HYBRID)';
```

---

### 3.6. Certificate Domain (3 tables)

#### 3.6.1. CertificateTemplate

```sql
-- ============================================
-- CERTIFICATETEMPLATE TABLE
-- Purpose: Certificate design templates (NEW)
-- ============================================

CREATE TABLE "CertificateTemplate" (
  template_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  name VARCHAR(100) NOT NULL UNIQUE,
  description TEXT,
  background_image_url VARCHAR(500),
  layout_config JSON,
  html_template TEXT,
  is_active BOOLEAN DEFAULT TRUE,
  is_default BOOLEAN DEFAULT FALSE,
  created_by UUID REFERENCES "User"(user_id) ON DELETE SET NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE "CertificateTemplate" IS 'Certificate design templates (NEW)';
COMMENT ON COLUMN "CertificateTemplate".layout_config IS 'JSON layout configuration';
COMMENT ON COLUMN "CertificateTemplate".html_template IS 'HTML template with placeholders';
```

#### 3.6.2. Certificate

```sql
-- ============================================
-- CERTIFICATE TABLE
-- Purpose: Issued certificates (REDESIGNED)
-- ============================================

CREATE TABLE "Certificate" (
  certificate_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID NOT NULL REFERENCES "User"(user_id) ON DELETE CASCADE,
  course_id UUID NOT NULL REFERENCES "Course"(course_id) ON DELETE CASCADE,
  course_enrollment_id UUID NOT NULL REFERENCES "CourseEnrollment"(course_enrollment_id) ON DELETE CASCADE,
  template_id UUID REFERENCES "CertificateTemplate"(template_id) ON DELETE SET NULL,
  certificate_code VARCHAR(50) NOT NULL UNIQUE,
  verification_code VARCHAR(100) NOT NULL UNIQUE,
  title VARCHAR(200) NOT NULL,
  issue_date DATE NOT NULL,
  completion_date DATE NOT NULL,
  final_grade DECIMAL(5,2),
  grade_letter VARCHAR(5),
  pdf_url VARCHAR(500),
  qr_code_url VARCHAR(500),
  verification_url VARCHAR(500),
  status VARCHAR(20) DEFAULT 'ACTIVE',
  valid_from DATE NOT NULL,
  valid_until DATE,
  revoked_at TIMESTAMP,
  revoked_by UUID REFERENCES "User"(user_id) ON DELETE SET NULL,
  revoke_reason TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT uq_certificate UNIQUE(user_id, course_id),
  CONSTRAINT chk_certificate_status CHECK (status IN (
    'ACTIVE', 'REVOKED', 'EXPIRED'
  ))
);

COMMENT ON TABLE "Certificate" IS 'Issued certificates (REDESIGNED from v1.0)';
COMMENT ON COLUMN "Certificate".certificate_code IS 'Public certificate code (e.g., BL-2025-0001)';
COMMENT ON COLUMN "Certificate".verification_code IS 'Secret verification code';
COMMENT ON COLUMN "Certificate".qr_code_url IS 'QR code image for quick verification';
```

#### 3.6.3. CertificateVerification

```sql
-- ============================================
-- CERTIFICATEVERIFICATION TABLE
-- Purpose: Certificate verification audit log (NEW)
-- ============================================

CREATE TABLE "CertificateVerification" (
  verification_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  certificate_id UUID NOT NULL REFERENCES "Certificate"(certificate_id) ON DELETE CASCADE,
  verified_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  verified_by_ip VARCHAR(45),
  verification_method VARCHAR(20),
  verification_result VARCHAR(20) NOT NULL,

  CONSTRAINT chk_verification_method CHECK (verification_method IN (
    'CODE', 'QR', 'URL'
  )),
  CONSTRAINT chk_verification_result CHECK (verification_result IN (
    'VALID', 'REVOKED', 'EXPIRED', 'NOT_FOUND'
  ))
);

COMMENT ON TABLE "CertificateVerification" IS 'Certificate verification audit log (NEW)';
COMMENT ON COLUMN "CertificateVerification".verification_method IS 'How verified: CODE, QR, URL';
```

---

### 3.7. Notification Domain (3 tables - NEW)

#### 3.7.1. Notification

```sql
-- ============================================
-- NOTIFICATION TABLE
-- Purpose: User notifications (multi-channel) (NEW)
-- ============================================

CREATE TABLE "Notification" (
  notification_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID NOT NULL REFERENCES "User"(user_id) ON DELETE CASCADE,
  notification_type VARCHAR(50) NOT NULL,
  title VARCHAR(200) NOT NULL,
  message TEXT NOT NULL,
  related_entity_type VARCHAR(50),
  related_entity_id UUID,
  action_url VARCHAR(500),
  priority VARCHAR(20) DEFAULT 'NORMAL',
  is_read BOOLEAN DEFAULT FALSE,
  read_at TIMESTAMP,
  sent_via_email BOOLEAN DEFAULT FALSE,
  sent_via_push BOOLEAN DEFAULT FALSE,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  expires_at TIMESTAMP,

  CONSTRAINT chk_notification_type CHECK (notification_type IN (
    'ASSIGNMENT_DUE', 'GRADE_PUBLISHED', 'CERTIFICATE_ISSUED',
    'COURSE_UPDATE', 'CLASS_REMINDER', 'ENROLLMENT_CONFIRMED'
  )),
  CONSTRAINT chk_notification_priority CHECK (priority IN (
    'LOW', 'NORMAL', 'HIGH', 'URGENT'
  ))
);

COMMENT ON TABLE "Notification" IS 'User notifications (NEW)';
COMMENT ON COLUMN "Notification".related_entity_id IS 'ID of related course, assignment, etc.';
COMMENT ON COLUMN "Notification".action_url IS 'Deep link to related content';
```

#### 3.7.2. NotificationPreference

```sql
-- ============================================
-- NOTIFICATIONPREFERENCE TABLE
-- Purpose: User notification preferences (NEW)
-- ============================================

CREATE TABLE "NotificationPreference" (
  preference_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID NOT NULL REFERENCES "User"(user_id) ON DELETE CASCADE,
  notification_type VARCHAR(50) NOT NULL,
  email_enabled BOOLEAN DEFAULT TRUE,
  push_enabled BOOLEAN DEFAULT TRUE,
  sms_enabled BOOLEAN DEFAULT FALSE,
  frequency VARCHAR(20) DEFAULT 'IMMEDIATE',
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT uq_notification_preference UNIQUE(user_id, notification_type),
  CONSTRAINT chk_notification_frequency CHECK (frequency IN (
    'IMMEDIATE', 'DAILY_DIGEST', 'WEEKLY_DIGEST', 'NEVER'
  ))
);

COMMENT ON TABLE "NotificationPreference" IS 'User notification preferences (NEW)';
COMMENT ON COLUMN "NotificationPreference".frequency IS 'Delivery frequency';
```

#### 3.7.3. NotificationLog

```sql
-- ============================================
-- NOTIFICATIONLOG TABLE
-- Purpose: Notification delivery log (NEW)
-- ============================================

CREATE TABLE "NotificationLog" (
  log_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  notification_id UUID NOT NULL REFERENCES "Notification"(notification_id) ON DELETE CASCADE,
  channel VARCHAR(20) NOT NULL,
  status VARCHAR(20) NOT NULL,
  sent_at TIMESTAMP,
  error_message TEXT,
  attempts INT DEFAULT 1,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT chk_notification_channel CHECK (channel IN (
    'EMAIL', 'PUSH', 'SMS', 'IN_APP'
  )),
  CONSTRAINT chk_notification_log_status CHECK (status IN (
    'PENDING', 'SENT', 'FAILED', 'BOUNCED'
  ))
);

COMMENT ON TABLE "NotificationLog" IS 'Notification delivery log (NEW)';
COMMENT ON COLUMN "NotificationLog".error_message IS 'Error details if delivery failed';
```

---

### 3.8. Audit & System Domain (3 tables - NEW)

#### 3.8.1. ActivityLog

```sql
-- ============================================
-- ACTIVITYLOG TABLE
-- Purpose: Audit log for all system activities (NEW)
-- ============================================

CREATE TABLE "ActivityLog" (
  log_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID REFERENCES "User"(user_id) ON DELETE SET NULL,
  action VARCHAR(100) NOT NULL,
  entity_type VARCHAR(50) NOT NULL,
  entity_id UUID NOT NULL,
  description TEXT,
  old_values JSON,
  new_values JSON,
  ip_address VARCHAR(45),
  user_agent TEXT,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  log_date DATE GENERATED ALWAYS AS (created_at::DATE) STORED
);

COMMENT ON TABLE "ActivityLog" IS 'Audit log for all system activities (NEW)';
COMMENT ON COLUMN "ActivityLog".action IS 'Action performed (CREATE, UPDATE, DELETE, etc.)';
COMMENT ON COLUMN "ActivityLog".entity_type IS 'Entity type (Course, Assignment, etc.)';
COMMENT ON COLUMN "ActivityLog".old_values IS 'Previous values (JSON)';
COMMENT ON COLUMN "ActivityLog".new_values IS 'New values (JSON)';
COMMENT ON COLUMN "ActivityLog".log_date IS 'Generated column for partitioning';
```

#### 3.8.2. File

```sql
-- ============================================
-- FILE TABLE
-- Purpose: File upload management (NEW)
-- ============================================

CREATE TABLE "File" (
  file_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  uploaded_by UUID NOT NULL REFERENCES "User"(user_id) ON DELETE CASCADE,
  original_filename VARCHAR(255) NOT NULL,
  stored_filename VARCHAR(255) NOT NULL,
  file_path VARCHAR(500) NOT NULL,
  file_url VARCHAR(500) NOT NULL,
  file_size_bytes BIGINT NOT NULL,
  mime_type VARCHAR(100) NOT NULL,
  entity_type VARCHAR(50),
  entity_id UUID,
  storage_type VARCHAR(20) DEFAULT 'LOCAL',
  is_deleted BOOLEAN DEFAULT FALSE,
  uploaded_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  deleted_at TIMESTAMP,

  CONSTRAINT chk_storage_type CHECK (storage_type IN (
    'LOCAL', 'S3', 'AZURE', 'GCS'
  ))
);

COMMENT ON TABLE "File" IS 'File upload management (NEW)';
COMMENT ON COLUMN "File".stored_filename IS 'Unique filename on server';
COMMENT ON COLUMN "File".entity_type IS 'Related entity (Assignment, Lecture, etc.)';
COMMENT ON COLUMN "File".storage_type IS 'Storage backend: LOCAL, S3, AZURE, GCS';
```

#### 3.8.3. SystemSettings

```sql
-- ============================================
-- SYSTEMSETTINGS TABLE
-- Purpose: System configuration (NEW)
-- ============================================

CREATE TABLE "SystemSettings" (
  setting_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  setting_key VARCHAR(100) NOT NULL UNIQUE,
  setting_value TEXT NOT NULL,
  data_type VARCHAR(20) DEFAULT 'STRING',
  category VARCHAR(50),
  description TEXT,
  is_editable BOOLEAN DEFAULT TRUE,
  updated_by UUID REFERENCES "User"(user_id) ON DELETE SET NULL,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT chk_data_type CHECK (data_type IN (
    'STRING', 'INTEGER', 'DECIMAL', 'BOOLEAN', 'JSON'
  ))
);

COMMENT ON TABLE "SystemSettings" IS 'System configuration key-value store (NEW)';
COMMENT ON COLUMN "SystemSettings".setting_key IS 'Unique setting key (e.g., MAX_FILE_SIZE)';
COMMENT ON COLUMN "SystemSettings".data_type IS 'Value type for parsing';
```

---

## 4. INDEXES

### 4.1. Primary Key Indexes
All primary key indexes are automatically created by PostgreSQL.

### 4.2. Foreign Key Indexes

```sql
-- ============================================
-- FOREIGN KEY INDEXES
-- ============================================

-- User Management
CREATE INDEX idx_userrole_user_id ON "UserRole"(user_id);
CREATE INDEX idx_userrole_role_id ON "UserRole"(role_id);

-- Course Content
CREATE INDEX idx_module_course_id ON "Module"(course_id);
CREATE INDEX idx_lecture_module_id ON "Lecture"(module_id);
CREATE INDEX idx_resource_lecture_id ON "Resource"(lecture_id);

-- Assessment
CREATE INDEX idx_quiz_course_id ON "Quiz"(course_id);
CREATE INDEX idx_question_course_id ON "Question"(course_id);
CREATE INDEX idx_option_question_id ON "Option"(question_id);
CREATE INDEX idx_quizquestion_quiz_id ON "QuizQuestion"(quiz_id);
CREATE INDEX idx_quizquestion_question_id ON "QuizQuestion"(question_id);
CREATE INDEX idx_attempt_quiz_id ON "Attempt"(quiz_id);
CREATE INDEX idx_attempt_user_id ON "Attempt"(user_id);
CREATE INDEX idx_attempt_course_enrollment_id ON "Attempt"(course_enrollment_id);
CREATE INDEX idx_quizsubmission_attempt_id ON "QuizSubmission"(attempt_id);
CREATE INDEX idx_quizsubmission_question_id ON "QuizSubmission"(question_id);
CREATE INDEX idx_assignment_course_id ON "Assignment"(course_id);
CREATE INDEX idx_assignment_class_id ON "Assignment"(class_id);
CREATE INDEX idx_assignmentsubmission_assignment_id ON "AssignmentSubmission"(assignment_id);
CREATE INDEX idx_assignmentsubmission_user_id ON "AssignmentSubmission"(user_id);
CREATE INDEX idx_assignmentsubmission_enrollment_id ON "AssignmentSubmission"(course_enrollment_id);
CREATE INDEX idx_gradebook_user_id ON "GradeBook"(user_id);
CREATE INDEX idx_gradebook_course_id ON "GradeBook"(course_id);

-- Enrollment & Progress
CREATE INDEX idx_courseenrollment_user_id ON "CourseEnrollment"(user_id);
CREATE INDEX idx_courseenrollment_course_id ON "CourseEnrollment"(course_id);
CREATE INDEX idx_classenrollment_user_id ON "ClassEnrollment"(user_id);
CREATE INDEX idx_classenrollment_class_id ON "ClassEnrollment"(class_id);
CREATE INDEX idx_progress_user_id ON "Progress"(user_id);
CREATE INDEX idx_progress_course_id ON "Progress"(course_id);
CREATE INDEX idx_progress_module_id ON "Progress"(module_id);
CREATE INDEX idx_progress_lecture_id ON "Progress"(lecture_id);
CREATE INDEX idx_attendance_schedule_id ON "Attendance"(schedule_id);
CREATE INDEX idx_attendance_user_id ON "Attendance"(user_id);

-- Class & Blended Learning
CREATE INDEX idx_class_course_id ON "Class"(course_id);
CREATE INDEX idx_class_instructor_id ON "Class"(instructor_id);
CREATE INDEX idx_schedule_class_id ON "Schedule"(class_id);

-- Certificate
CREATE INDEX idx_certificate_user_id ON "Certificate"(user_id);
CREATE INDEX idx_certificate_course_id ON "Certificate"(course_id);
CREATE INDEX idx_certificate_enrollment_id ON "Certificate"(course_enrollment_id);
CREATE INDEX idx_certificate_template_id ON "Certificate"(template_id);
CREATE INDEX idx_certificateverification_cert_id ON "CertificateVerification"(certificate_id);

-- Notification
CREATE INDEX idx_notification_user_id ON "Notification"(user_id);
CREATE INDEX idx_notificationpreference_user_id ON "NotificationPreference"(user_id);
CREATE INDEX idx_notificationlog_notification_id ON "NotificationLog"(notification_id);

-- Audit & System
CREATE INDEX idx_activitylog_user_id ON "ActivityLog"(user_id);
CREATE INDEX idx_file_uploaded_by ON "File"(uploaded_by);
```

### 4.3. Performance Indexes

```sql
-- ============================================
-- PERFORMANCE INDEXES
-- ============================================

-- User
CREATE INDEX idx_user_email ON "User"(email);
CREATE INDEX idx_user_status ON "User"(account_status);
CREATE INDEX idx_user_created_at ON "User"(created_at DESC);

-- Course
CREATE INDEX idx_course_code ON "Course"(code);
CREATE INDEX idx_course_status ON "Course"(status);
CREATE INDEX idx_course_category ON "Course"(category);
CREATE INDEX idx_course_published_at ON "Course"(published_at DESC)
  WHERE status = 'PUBLISHED';

-- Quiz & Assessment
CREATE INDEX idx_quiz_published ON "Quiz"(is_published);
CREATE INDEX idx_question_type ON "Question"(type);
CREATE INDEX idx_question_active ON "Question"(is_active);
CREATE INDEX idx_attempt_status ON "Attempt"(status);
CREATE INDEX idx_assignment_due_date ON "Assignment"(due_date);
CREATE INDEX idx_assignmentsubmission_status ON "AssignmentSubmission"(status);

-- Enrollment & Progress
CREATE INDEX idx_courseenrollment_status ON "CourseEnrollment"(enrollment_status);
CREATE INDEX idx_progress_status ON "Progress"(status);
CREATE INDEX idx_attendance_status ON "Attendance"(status);

-- Certificate
CREATE INDEX idx_certificate_code ON "Certificate"(certificate_code);
CREATE INDEX idx_certificate_verification_code ON "Certificate"(verification_code);
CREATE INDEX idx_certificate_status ON "Certificate"(status);

-- Notification
CREATE INDEX idx_notification_type ON "Notification"(notification_type);
CREATE INDEX idx_notification_is_read ON "Notification"(is_read);
CREATE INDEX idx_notification_created_at ON "Notification"(created_at DESC);
CREATE INDEX idx_notificationlog_channel ON "NotificationLog"(channel);
CREATE INDEX idx_notificationlog_status ON "NotificationLog"(status);

-- Audit & System
CREATE INDEX idx_activitylog_entity ON "ActivityLog"(entity_type, entity_id);
CREATE INDEX idx_activitylog_created_at ON "ActivityLog"(created_at DESC);
CREATE INDEX idx_activitylog_log_date ON "ActivityLog"(log_date);
CREATE INDEX idx_file_entity ON "File"(entity_type, entity_id);
CREATE INDEX idx_file_deleted ON "File"(is_deleted);
```

### 4.4. Composite Indexes

```sql
-- ============================================
-- COMPOSITE INDEXES (for common query patterns)
-- ============================================

CREATE INDEX idx_progress_user_course ON "Progress"(user_id, course_id);
CREATE INDEX idx_attempt_user_quiz ON "Attempt"(user_id, quiz_id);
CREATE INDEX idx_activitylog_user_created ON "ActivityLog"(user_id, created_at DESC);
CREATE INDEX idx_notification_user_read ON "Notification"(user_id, is_read);
```

### 4.5. Full-Text Search Indexes

```sql
-- ============================================
-- FULL-TEXT SEARCH INDEXES
-- ============================================

-- Course search (title + description)
CREATE INDEX idx_course_search ON "Course" USING GIN(
  to_tsvector('english', COALESCE(title, '') || ' ' || COALESCE(description, ''))
);

-- Question search
CREATE INDEX idx_question_search ON "Question" USING GIN(
  to_tsvector('english', text)
);
```

---

## 5. TRIGGERS

### 5.1. Auto-Update Timestamps

```sql
-- ============================================
-- AUTO-UPDATE UPDATED_AT TRIGGER
-- ============================================

CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
  NEW.updated_at = CURRENT_TIMESTAMP;
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Apply to all tables with updated_at column
CREATE TRIGGER trg_user_updated_at
  BEFORE UPDATE ON "User"
  FOR EACH ROW
  EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER trg_course_updated_at
  BEFORE UPDATE ON "Course"
  FOR EACH ROW
  EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER trg_module_updated_at
  BEFORE UPDATE ON "Module"
  FOR EACH ROW
  EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER trg_lecture_updated_at
  BEFORE UPDATE ON "Lecture"
  FOR EACH ROW
  EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER trg_quiz_updated_at
  BEFORE UPDATE ON "Quiz"
  FOR EACH ROW
  EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER trg_question_updated_at
  BEFORE UPDATE ON "Question"
  FOR EACH ROW
  EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER trg_assignment_updated_at
  BEFORE UPDATE ON "Assignment"
  FOR EACH ROW
  EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER trg_progress_updated_at
  BEFORE UPDATE ON "Progress"
  FOR EACH ROW
  EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER trg_class_updated_at
  BEFORE UPDATE ON "Class"
  FOR EACH ROW
  EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER trg_certificatetemplate_updated_at
  BEFORE UPDATE ON "CertificateTemplate"
  FOR EACH ROW
  EXECUTE FUNCTION update_updated_at_column();
```

### 5.2. Auto-Issue Certificate

```sql
-- ============================================
-- AUTO-ISSUE CERTIFICATE TRIGGER
-- ============================================

CREATE OR REPLACE FUNCTION auto_issue_certificate()
RETURNS TRIGGER AS $$
DECLARE
  v_certificate_code VARCHAR(50);
  v_verification_code VARCHAR(100);
  v_template_id UUID;
BEGIN
  -- Only issue certificate when enrollment is completed
  IF NEW.enrollment_status = 'COMPLETED' AND
     OLD.enrollment_status != 'COMPLETED' AND
     NEW.completion_percentage >= 100 THEN

    -- Check if certificate already exists
    IF NOT EXISTS (
      SELECT 1 FROM "Certificate"
      WHERE user_id = NEW.user_id AND course_id = NEW.course_id
    ) THEN

      -- Generate certificate code
      v_certificate_code := 'BL-' || TO_CHAR(CURRENT_DATE, 'YYYY') || '-' ||
                           LPAD(nextval('certificate_code_seq')::TEXT, 6, '0');

      -- Generate verification code
      v_verification_code := encode(gen_random_bytes(32), 'hex');

      -- Get default template
      SELECT template_id INTO v_template_id
      FROM "CertificateTemplate"
      WHERE is_default = TRUE AND is_active = TRUE
      LIMIT 1;

      -- Insert certificate
      INSERT INTO "Certificate" (
        user_id, course_id, course_enrollment_id, template_id,
        certificate_code, verification_code, title,
        issue_date, completion_date, final_grade, valid_from
      ) VALUES (
        NEW.user_id, NEW.course_id, NEW.course_enrollment_id, v_template_id,
        v_certificate_code, v_verification_code,
        'Certificate of Completion',
        CURRENT_DATE, NEW.completed_at::DATE, NEW.final_grade, CURRENT_DATE
      );

      -- Send notification
      INSERT INTO "Notification" (
        user_id, notification_type, title, message,
        related_entity_type, related_entity_id, priority
      ) VALUES (
        NEW.user_id, 'CERTIFICATE_ISSUED',
        'Certificate Issued!',
        'Congratulations! Your certificate for this course has been issued.',
        'Certificate', (SELECT certificate_id FROM "Certificate"
                       WHERE certificate_code = v_certificate_code),
        'HIGH'
      );
    END IF;
  END IF;

  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Create sequence for certificate codes
CREATE SEQUENCE IF NOT EXISTS certificate_code_seq START 1;

-- Create trigger
CREATE TRIGGER trg_auto_issue_certificate
  AFTER UPDATE ON "CourseEnrollment"
  FOR EACH ROW
  EXECUTE FUNCTION auto_issue_certificate();
```

### 5.3. Auto-Grade MCQ Quiz

```sql
-- ============================================
-- AUTO-GRADE MCQ TRIGGER
-- ============================================

CREATE OR REPLACE FUNCTION auto_grade_mcq()
RETURNS TRIGGER AS $$
DECLARE
  v_correct_option_ids UUID[];
  v_is_correct BOOLEAN;
BEGIN
  -- Only auto-grade MCQ and TRUE_FALSE questions
  IF EXISTS (
    SELECT 1 FROM "Question" q
    WHERE q.question_id = NEW.question_id
    AND q.type IN ('MCQ', 'TRUE_FALSE')
  ) THEN

    -- Get correct option IDs
    SELECT ARRAY_AGG(option_id) INTO v_correct_option_ids
    FROM "Option"
    WHERE question_id = NEW.question_id AND is_correct = TRUE;

    -- Check if answer is correct
    v_is_correct := (NEW.selected_option_ids = v_correct_option_ids);

    -- Set auto_score
    IF v_is_correct THEN
      NEW.auto_score := NEW.max_points;
    ELSE
      NEW.auto_score := 0;
    END IF;

    NEW.final_score := NEW.auto_score;
  END IF;

  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_auto_grade_mcq
  BEFORE INSERT OR UPDATE ON "QuizSubmission"
  FOR EACH ROW
  EXECUTE FUNCTION auto_grade_mcq();
```

### 5.4. Update GradeBook on Assignment Grade

```sql
-- ============================================
-- UPDATE GRADEBOOK TRIGGER
-- ============================================

CREATE OR REPLACE FUNCTION update_gradebook()
RETURNS TRIGGER AS $$
DECLARE
  v_total_quiz_score DECIMAL(6,2);
  v_total_assignment_score DECIMAL(6,2);
BEGIN
  -- Calculate total quiz score
  SELECT COALESCE(SUM(a.final_score), 0) INTO v_total_quiz_score
  FROM "Attempt" a
  WHERE a.user_id = NEW.user_id
  AND a.course_enrollment_id = NEW.course_enrollment_id
  AND a.status = 'GRADED';

  -- Calculate total assignment score
  SELECT COALESCE(SUM(asub.final_score), 0) INTO v_total_assignment_score
  FROM "AssignmentSubmission" asub
  WHERE asub.user_id = NEW.user_id
  AND asub.course_enrollment_id = NEW.course_enrollment_id
  AND asub.status = 'GRADED';

  -- Upsert gradebook
  INSERT INTO "GradeBook" (
    user_id, course_id, quiz_score, assignment_score,
    total_score, last_updated_at
  )
  SELECT
    NEW.user_id,
    ce.course_id,
    v_total_quiz_score,
    v_total_assignment_score,
    v_total_quiz_score + v_total_assignment_score,
    CURRENT_TIMESTAMP
  FROM "CourseEnrollment" ce
  WHERE ce.course_enrollment_id = NEW.course_enrollment_id
  ON CONFLICT (user_id, course_id, COALESCE(class_id, '00000000-0000-0000-0000-000000000000'::UUID))
  DO UPDATE SET
    quiz_score = EXCLUDED.quiz_score,
    assignment_score = EXCLUDED.assignment_score,
    total_score = EXCLUDED.total_score,
    last_updated_at = CURRENT_TIMESTAMP;

  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_update_gradebook_assignment
  AFTER INSERT OR UPDATE ON "AssignmentSubmission"
  FOR EACH ROW
  WHEN (NEW.status = 'GRADED')
  EXECUTE FUNCTION update_gradebook();

CREATE TRIGGER trg_update_gradebook_quiz
  AFTER INSERT OR UPDATE ON "Attempt"
  FOR EACH ROW
  WHEN (NEW.status = 'GRADED')
  EXECUTE FUNCTION update_gradebook();
```

---

## 6. VIEWS

### 6.1. Materialized Views for Reporting

```sql
-- ============================================
-- COURSE STATISTICS VIEW
-- ============================================

CREATE MATERIALIZED VIEW CourseStatistics AS
SELECT
  c.course_id,
  c.code,
  c.title,
  c.status,
  COUNT(DISTINCT ce.user_id) FILTER (WHERE ce.role_in_course = 'STUDENT') as total_students,
  COUNT(DISTINCT ce.user_id) FILTER (WHERE ce.enrollment_status = 'COMPLETED') as completed_students,
  AVG(ce.final_grade) FILTER (WHERE ce.enrollment_status = 'COMPLETED') as avg_final_grade,
  AVG(ce.completion_percentage) as avg_completion_percentage,
  COUNT(DISTINCT a.assignment_id) as total_assignments,
  COUNT(DISTINCT q.quiz_id) as total_quizzes,
  COUNT(DISTINCT m.module_id) as total_modules,
  COUNT(DISTINCT l.lecture_id) as total_lectures
FROM "Course" c
LEFT JOIN "CourseEnrollment" ce ON c.course_id = ce.course_id
LEFT JOIN "Assignment" a ON c.course_id = a.course_id
LEFT JOIN "Quiz" q ON c.course_id = q.course_id
LEFT JOIN "Module" m ON c.course_id = m.course_id
LEFT JOIN "Lecture" l ON m.module_id = l.module_id
GROUP BY c.course_id, c.code, c.title, c.status;

CREATE UNIQUE INDEX idx_coursestats_course_id ON CourseStatistics(course_id);

COMMENT ON MATERIALIZED VIEW CourseStatistics IS 'Course statistics for reporting';

-- Refresh function
CREATE OR REPLACE FUNCTION refresh_course_statistics()
RETURNS void AS $$
BEGIN
  REFRESH MATERIALIZED VIEW CONCURRENTLY CourseStatistics;
END;
$$ LANGUAGE plpgsql;
```

```sql
-- ============================================
-- USER PROGRESS SUMMARY VIEW
-- ============================================

CREATE MATERIALIZED VIEW UserProgressSummary AS
SELECT
  u.user_id,
  u.email,
  u.first_name,
  u.last_name,
  ce.course_id,
  c.title as course_title,
  ce.enrollment_status,
  ce.completion_percentage,
  COUNT(DISTINCT p.lecture_id) FILTER (WHERE p.status = 'COMPLETED') as lectures_completed,
  COUNT(DISTINCT a.attempt_id) FILTER (WHERE a.status = 'GRADED') as quizzes_completed,
  COUNT(DISTINCT asub.assignment_submission_id) FILTER (WHERE asub.status = 'GRADED') as assignments_completed,
  gb.total_score,
  gb.letter_grade,
  ce.last_accessed_at
FROM "User" u
INNER JOIN "CourseEnrollment" ce ON u.user_id = ce.user_id
INNER JOIN "Course" c ON ce.course_id = c.course_id
LEFT JOIN "Progress" p ON u.user_id = p.user_id AND ce.course_id = p.course_id
LEFT JOIN "Attempt" a ON u.user_id = a.user_id AND ce.course_enrollment_id = a.course_enrollment_id
LEFT JOIN "AssignmentSubmission" asub ON u.user_id = asub.user_id AND ce.course_enrollment_id = asub.course_enrollment_id
LEFT JOIN "GradeBook" gb ON u.user_id = gb.user_id AND ce.course_id = gb.course_id
WHERE ce.role_in_course = 'STUDENT'
GROUP BY u.user_id, u.email, u.first_name, u.last_name,
         ce.course_id, c.title, ce.enrollment_status, ce.completion_percentage,
         gb.total_score, gb.letter_grade, ce.last_accessed_at;

CREATE UNIQUE INDEX idx_userprogresssummary_user_course ON UserProgressSummary(user_id, course_id);

COMMENT ON MATERIALIZED VIEW UserProgressSummary IS 'User learning progress summary';
```

---

## 7. SEED DATA

### 7.1. Initial Roles

```sql
-- ============================================
-- SEED DATA: ROLES
-- ============================================

INSERT INTO "Role" (name, description, is_system_role) VALUES
('STUDENT', 'Student role - can enroll in courses and submit assignments', TRUE),
('INSTRUCTOR', 'Instructor role - can create courses and grade assignments', TRUE),
('TA', 'Teaching Assistant role - can assist with grading', TRUE),
('ADMIN', 'Administrator role - full system access', TRUE)
ON CONFLICT (name) DO NOTHING;
```

### 7.2. Sample Users (for testing only)

```sql
-- ============================================
-- SEED DATA: SAMPLE USERS (Testing Only)
-- ============================================

-- Note: Password is 'password123' hashed with bcrypt
-- In production, users must register and set their own passwords

INSERT INTO "User" (email, password_hash, first_name, last_name, account_status, email_verified_at) VALUES
('admin@bleaching.edu', '$2a$10$rZ8pqBJKB5v7J0YdN4YQy.vQZJ9e1Q7XJ8mK5kJ9N3J8YQy', 'Admin', 'User', 'ACTIVE', CURRENT_TIMESTAMP),
('instructor@blearning.edu', '$2a$10$rZ8pqBJKB5v7J0YdN4YQy.vQZJ9e1Q7XJ8mK5kJ9N3J8YQy', 'John', 'Doe', 'ACTIVE', CURRENT_TIMESTAMP),
('student@blearning.edu', '$2a$10$rZ8pqBJKB5v7J0YdN4YQy.vQZJ9e1Q7XJ8mK5kJ9N3J8YQy', 'Jane', 'Smith', 'ACTIVE', CURRENT_TIMESTAMP)
ON CONFLICT (email) DO NOTHING;

-- Assign roles
INSERT INTO "UserRole" (user_id, role_id)
SELECT u.user_id, r.role_id
FROM "User" u, "Role" r
WHERE (u.email = 'admin@blearning.edu' AND r.name = 'ADMIN')
   OR (u.email = 'instructor@blearning.edu' AND r.name = 'INSTRUCTOR')
   OR (u.email = 'student@blearning.edu' AND r.name = 'STUDENT')
ON CONFLICT (user_id, role_id) DO NOTHING;
```

---

## 8. VALIDATION QUERIES

```sql
-- ============================================
-- VALIDATION QUERIES
-- ============================================

-- Check all tables exist
SELECT COUNT(*) as total_tables
FROM information_schema.tables
WHERE table_schema = 'public'
AND table_type = 'BASE TABLE';
-- Expected: 31

-- Check all foreign keys
SELECT COUNT(*) as total_foreign_keys
FROM information_schema.table_constraints
WHERE constraint_type = 'FOREIGN KEY'
AND table_schema = 'public';
-- Expected: 40+

-- Check all indexes
SELECT COUNT(*) as total_indexes
FROM pg_indexes
WHERE schemaname = 'public';
-- Expected: 80+

-- Check all triggers
SELECT COUNT(*) as total_triggers
FROM pg_trigger
WHERE tgname NOT LIKE 'pg_%';
-- Expected: 10+
```

---

## 9. CLEANUP SCRIPT

```sql
-- ============================================
-- DROP ALL TABLES (USE WITH CAUTION!)
-- ============================================

-- Drop all tables in reverse dependency order
DROP TABLE IF EXISTS "CertificateVerification" CASCADE;
DROP TABLE IF EXISTS "Certificate" CASCADE;
DROP TABLE IF EXISTS "CertificateTemplate" CASCADE;
DROP TABLE IF EXISTS "NotificationLog" CASCADE;
DROP TABLE IF EXISTS "NotificationPreference" CASCADE;
DROP TABLE IF EXISTS "Notification" CASCADE;
DROP TABLE IF EXISTS "ActivityLog" CASCADE;
DROP TABLE IF EXISTS "File" CASCADE;
DROP TABLE IF EXISTS "SystemSettings" CASCADE;
DROP TABLE IF EXISTS "GradeBook" CASCADE;
DROP TABLE IF EXISTS "AssignmentSubmission" CASCADE;
DROP TABLE IF EXISTS "Assignment" CASCADE;
DROP TABLE IF EXISTS "QuizSubmission" CASCADE;
DROP TABLE IF EXISTS "Attempt" CASCADE;
DROP TABLE IF EXISTS "QuizQuestion" CASCADE;
DROP TABLE IF EXISTS "Option" CASCADE;
DROP TABLE IF EXISTS "Question" CASCADE;
DROP TABLE IF EXISTS "Quiz" CASCADE;
DROP TABLE IF EXISTS "Attendance" CASCADE;
DROP TABLE IF EXISTS "Schedule" CASCADE;
DROP TABLE IF EXISTS "Progress" CASCADE;
DROP TABLE IF EXISTS "ClassEnrollment" CASCADE;
DROP TABLE IF EXISTS "CourseEnrollment" CASCADE;
DROP TABLE IF EXISTS "Class" CASCADE;
DROP TABLE IF EXISTS "Resource" CASCADE;
DROP TABLE IF EXISTS "Lecture" CASCADE;
DROP TABLE IF EXISTS "Module" CASCADE;
DROP TABLE IF EXISTS "Course" CASCADE;
DROP TABLE IF EXISTS "UserRole" CASCADE;
DROP TABLE IF EXISTS "Role" CASCADE;
DROP TABLE IF EXISTS "User" CASCADE;

-- Drop materialized views
DROP MATERIALIZED VIEW IF EXISTS CourseStatistics;
DROP MATERIALIZED VIEW IF EXISTS UserProgressSummary;

-- Drop sequences
DROP SEQUENCE IF EXISTS certificate_code_seq;

-- Drop functions
DROP FUNCTION IF EXISTS update_updated_at_column() CASCADE;
DROP FUNCTION IF EXISTS auto_issue_certificate() CASCADE;
DROP FUNCTION IF EXISTS auto_grade_mcq() CASCADE;
DROP FUNCTION IF EXISTS update_gradebook() CASCADE;
DROP FUNCTION IF EXISTS refresh_course_statistics();

-- Drop extensions (optional - only if no other databases use them)
-- DROP EXTENSION IF EXISTS "uuid-ossp";
-- DROP EXTENSION IF EXISTS "pg_trgm";
-- DROP EXTENSION IF EXISTS "btree_gin";
```

---

## 10. IMPLEMENTATION NOTES

### 10.1. Installation Order
1. Install PostgreSQL 14+
2. Create database: `createdb b_learning`
3. Run extensions (Section 2)
4. Run table definitions (Section 3)
5. Run indexes (Section 4)
6. Run triggers (Section 5)
7. Run views (Section 6)
8. Run seed data (Section 7)
9. Validate (Section 8)

### 10.2. Performance Tips
- Use connection pooling (PgBouncer, pgpool-II)
- Partition large tables (ActivityLog by log_date)
- Regular VACUUM and ANALYZE
- Monitor slow queries with pg_stat_statements
- Use EXPLAIN ANALYZE for query optimization

### 10.3. Security Recommendations
- Use strong passwords (bcrypt with cost >= 12)
- Enable SSL/TLS for connections
- Implement row-level security (RLS) if needed
- Regular backups (pg_dump, pg_basebackup)
- Audit log retention policy

---

**Document Status**: ✅ Complete
**Last Updated**: 2025-11-25
**SQL Dialect**: PostgreSQL 14+
**Total Lines**: ~1200

---

**END OF DATABASE SCHEMA SPECIFICATION**
