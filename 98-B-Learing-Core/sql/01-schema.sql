-- ============================================
-- B-LEARNING CORE DATABASE SCHEMA
-- Version: 1.0 (Core - 16 tables)
-- Database: PostgreSQL 14+
-- Author: Nguyen Van Kiet - CNTT1-K63
-- Created: 2025-11-27
-- Fixed: UTF-8 Encoding
-- ============================================

-- ============================================
-- EXTENSIONS
-- ============================================

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";  -- UUID generation
CREATE EXTENSION IF NOT EXISTS "pg_trgm";    -- Full-text search
CREATE EXTENSION IF NOT EXISTS "btree_gin";  -- GIN indexes for arrays and JSON

-- ============================================
-- DOMAIN 1: USER MANAGEMENT (3 tables)
-- ============================================

-- Table 1: User
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

COMMENT ON TABLE "User" IS 'User accounts - store account information';
COMMENT ON COLUMN "User".user_id IS 'Unique user identifier';
COMMENT ON COLUMN "User".email IS 'Login email (unique)';
COMMENT ON COLUMN "User".password_hash IS 'Hashed password (bcrypt)';
COMMENT ON COLUMN "User".first_name IS 'First name';
COMMENT ON COLUMN "User".last_name IS 'Last name and middle name';
COMMENT ON COLUMN "User".avatar_url IS 'Avatar image path (S3/GCS)';
COMMENT ON COLUMN "User".phone IS 'Phone number';
COMMENT ON COLUMN "User".account_status IS 'Account status: PENDING_VERIFICATION, ACTIVE, SUSPENDED, DELETED';
COMMENT ON COLUMN "User".preferences IS 'User preferences (JSON): notifications, locale, timezone';
COMMENT ON COLUMN "User".email_verified_at IS 'Email verification time';
COMMENT ON COLUMN "User".last_login_at IS 'Last login time';

-- Table 2: Role
CREATE TABLE "Role" (
  role_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  name VARCHAR(50) NOT NULL UNIQUE,
  description TEXT,
  permissions JSON,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE "Role" IS 'System roles - RBAC (Role-Based Access Control)';
COMMENT ON COLUMN "Role".name IS 'Role name: STUDENT, INSTRUCTOR, TA, ADMIN';
COMMENT ON COLUMN "Role".description IS 'Role description';
COMMENT ON COLUMN "Role".permissions IS 'List of permissions (JSON)';

-- Table 3: UserRole
CREATE TABLE "UserRole" (
  user_role_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID NOT NULL REFERENCES "User"(user_id) ON DELETE CASCADE,
  role_id UUID NOT NULL REFERENCES "Role"(role_id) ON DELETE CASCADE,
  granted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  granted_by UUID REFERENCES "User"(user_id) ON DELETE SET NULL,
  expires_at TIMESTAMP,

  CONSTRAINT uq_user_role UNIQUE(user_id, role_id)
);

COMMENT ON TABLE "UserRole" IS 'Assign roles to users (many-to-many)';
COMMENT ON COLUMN "UserRole".granted_at IS 'Time when permission was granted';
COMMENT ON COLUMN "UserRole".granted_by IS 'Who granted the permission (admin)';
COMMENT ON COLUMN "UserRole".expires_at IS 'Expiration time (NULL = permanent)';

-- ============================================
-- DOMAIN 2: COURSE CONTENT (4 tables)
-- ============================================

-- Table 4: Course
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
    'BEGINNER', 'INTERMEDIATE', 'ADVANCED'
  )),
  CONSTRAINT chk_course_status CHECK (status IN (
    'DRAFT', 'PUBLISHED', 'ARCHIVED'
  ))
);

COMMENT ON TABLE "Course" IS 'Course catalog';
COMMENT ON COLUMN "Course".code IS 'Course code (unique, e.g. JAVA101)';
COMMENT ON COLUMN "Course".title IS 'Course title';
COMMENT ON COLUMN "Course".description IS 'Detailed description (HTML/Markdown)';
COMMENT ON COLUMN "Course".short_description IS 'Short description (for listing)';
COMMENT ON COLUMN "Course".thumbnail_url IS 'Course thumbnail image';
COMMENT ON COLUMN "Course".category IS 'Category (Programming, Math, Design...)';
COMMENT ON COLUMN "Course".difficulty_level IS 'Difficulty: BEGINNER, INTERMEDIATE, ADVANCED';
COMMENT ON COLUMN "Course".estimated_hours IS 'Estimated duration (hours)';
COMMENT ON COLUMN "Course".status IS 'Status: DRAFT (draft), PUBLISHED (public), ARCHIVED (archived)';
COMMENT ON COLUMN "Course".published_at IS 'Publication time';
COMMENT ON COLUMN "Course".created_by IS 'Creator (Instructor)';

-- Table 5: Module
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

COMMENT ON TABLE "Module" IS 'Modules/Chapters in course';
COMMENT ON COLUMN "Module".title IS 'Module title (e.g. Chapter 1: Java Introduction)';
COMMENT ON COLUMN "Module".order_num IS 'Display order (1, 2, 3...)';
COMMENT ON COLUMN "Module".prerequisite_module_ids IS 'Array of prerequisite module UUIDs';
COMMENT ON COLUMN "Module".estimated_duration_minutes IS 'Estimated duration (minutes)';

-- Table 6: Lecture
CREATE TABLE "Lecture" (
  lecture_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  module_id UUID NOT NULL REFERENCES "Module"(module_id) ON DELETE CASCADE,
  title VARCHAR(200) NOT NULL,
  description TEXT,
  type VARCHAR(20) NOT NULL,
  content_url VARCHAR(1024),
  duration_seconds INT,
  order_num INT NOT NULL,
  assignment_config JSON,
  is_preview BOOLEAN DEFAULT FALSE,
  is_downloadable BOOLEAN DEFAULT TRUE,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT chk_lecture_type CHECK (type IN (
    'VIDEO', 'PDF', 'SLIDE', 'AUDIO', 'TEXT', 'ASSIGNMENT'
  )),
  CONSTRAINT uq_lecture_order UNIQUE(module_id, order_num)
);

COMMENT ON TABLE "Lecture" IS 'Lectures (including Assignments)';
COMMENT ON COLUMN "Lecture".title IS 'Lecture title';
COMMENT ON COLUMN "Lecture".type IS 'Type: VIDEO, PDF, SLIDE, AUDIO, TEXT, ASSIGNMENT';
COMMENT ON COLUMN "Lecture".content_url IS 'Content URL (S3/GCS URL)';
COMMENT ON COLUMN "Lecture".duration_seconds IS 'Duration (seconds)';
COMMENT ON COLUMN "Lecture".order_num IS 'Order in module';
COMMENT ON COLUMN "Lecture".assignment_config IS 'Assignment configuration (JSON) - only when type=ASSIGNMENT';
COMMENT ON COLUMN "Lecture".is_preview IS 'Allow preview without registration?';
COMMENT ON COLUMN "Lecture".is_downloadable IS 'Allow download?';

-- Table 7: Resource
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
COMMENT ON COLUMN "Resource".title IS 'Display file name';
COMMENT ON COLUMN "Resource".file_url IS 'File path (S3/GCS)';
COMMENT ON COLUMN "Resource".file_type IS 'File type (MIME type, e.g. application/pdf)';
COMMENT ON COLUMN "Resource".file_size_bytes IS 'File size (bytes)';

-- ============================================
-- DOMAIN 3: ASSESSMENT (5 tables)
-- ============================================

-- Table 8: Quiz
CREATE TABLE "Quiz" (
  quiz_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  course_id UUID NOT NULL REFERENCES "Course"(course_id) ON DELETE CASCADE,
  title VARCHAR(200) NOT NULL,
  description TEXT,
  time_limit_minutes INT,
  pass_score DECIMAL(5,2),
  questions JSON NOT NULL,
  shuffle_questions BOOLEAN DEFAULT FALSE,
  show_correct_answers BOOLEAN DEFAULT TRUE,
  is_published BOOLEAN DEFAULT FALSE,
  created_by UUID REFERENCES "User"(user_id) ON DELETE SET NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE "Quiz" IS 'Quiz/assessment configuration';
COMMENT ON COLUMN "Quiz".title IS 'Quiz title';
COMMENT ON COLUMN "Quiz".time_limit_minutes IS 'Time limit (minutes, 0 = unlimited)';
COMMENT ON COLUMN "Quiz".pass_score IS 'Passing score (%, NULL = no requirement)';
COMMENT ON COLUMN "Quiz".questions IS 'Question list (JSON): [{"question_id": "uuid", "points": 10, "order": 1}]';
COMMENT ON COLUMN "Quiz".shuffle_questions IS 'Shuffle question order?';
COMMENT ON COLUMN "Quiz".show_correct_answers IS 'Show correct answers after submission?';
COMMENT ON COLUMN "Quiz".is_published IS 'Published for students?';

-- Table 9: Question
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
    'MCQ', 'TRUE_FALSE', 'ESSAY', 'SHORT_ANSWER'
  )),
  CONSTRAINT chk_question_difficulty CHECK (difficulty IN (
    'EASY', 'MEDIUM', 'HARD'
  ))
);

COMMENT ON TABLE "Question" IS 'Question bank (reusable across quizzes)';
COMMENT ON COLUMN "Question".text IS 'Question content';
COMMENT ON COLUMN "Question".type IS 'Question type: MCQ (multiple choice), TRUE_FALSE, ESSAY, SHORT_ANSWER';
COMMENT ON COLUMN "Question".difficulty IS 'Difficulty: EASY, MEDIUM, HARD';
COMMENT ON COLUMN "Question".max_points IS 'Default maximum points';
COMMENT ON COLUMN "Question".explanation IS 'Answer explanation';
COMMENT ON COLUMN "Question".is_active IS 'Is still in use?';

-- Table 10: Option
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
COMMENT ON COLUMN "Option".option_text IS 'Option content';
COMMENT ON COLUMN "Option".is_correct IS 'Is this the correct answer?';
COMMENT ON COLUMN "Option".order_num IS 'Display order (A, B, C, D)';
COMMENT ON COLUMN "Option".feedback IS 'Explanation for this option';

-- ============================================
-- DOMAIN 5: CLASS & CERTIFICATES (2 tables)
-- Must be created BEFORE Enrollment table
-- ============================================

-- Table 15: Class
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
  schedules JSON,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT chk_class_status CHECK (status IN ('SCHEDULED', 'ONGOING', 'COMPLETED', 'CANCELLED'))
);

COMMENT ON TABLE "Class" IS 'Class (blended learning)';
COMMENT ON COLUMN "Class".name IS 'Class name (e.g. Java K63 - Morning T2-T4-T6)';
COMMENT ON COLUMN "Class".start_date IS 'Start date';
COMMENT ON COLUMN "Class".end_date IS 'End date';
COMMENT ON COLUMN "Class".status IS 'Status: SCHEDULED, ONGOING, COMPLETED, CANCELLED';
COMMENT ON COLUMN "Class".max_students IS 'Maximum capacity';
COMMENT ON COLUMN "Class".location IS 'Learning location';
COMMENT ON COLUMN "Class".schedules IS 'Schedule + attendance (JSON): [{"date": "2025-12-01", "attendances": [...]}]';

-- ============================================
-- DOMAIN 4: ENROLLMENT & PROGRESS (2 tables)
-- ============================================

-- Table 13: Enrollment
CREATE TABLE "Enrollment" (
  enrollment_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID NOT NULL REFERENCES "User"(user_id) ON DELETE CASCADE,
  course_id UUID NOT NULL REFERENCES "Course"(course_id) ON DELETE CASCADE,
  class_id UUID REFERENCES "Class"(class_id) ON DELETE SET NULL,
  role VARCHAR(20) NOT NULL,
  status VARCHAR(20) DEFAULT 'ACTIVE',
  enrolled_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  completed_at TIMESTAMP,
  final_grade DECIMAL(5,2),
  completion_percentage DECIMAL(5,2) DEFAULT 0,
  last_accessed_at TIMESTAMP,

  CONSTRAINT chk_enrollment_role CHECK (role IN ('STUDENT', 'INSTRUCTOR', 'TA')),
  CONSTRAINT chk_enrollment_status CHECK (status IN ('ACTIVE', 'COMPLETED', 'DROPPED', 'SUSPENDED'))
);

COMMENT ON TABLE "Enrollment" IS 'Course enrollment (self-paced or class-based)';
COMMENT ON COLUMN "Enrollment".class_id IS 'Class (NULL = self-paced, UUID = blended learning)';
COMMENT ON COLUMN "Enrollment".role IS 'Role in course: STUDENT, INSTRUCTOR, TA';
COMMENT ON COLUMN "Enrollment".status IS 'Status: ACTIVE, COMPLETED, DROPPED, SUSPENDED';
COMMENT ON COLUMN "Enrollment".final_grade IS 'Final course grade';
COMMENT ON COLUMN "Enrollment".completion_percentage IS 'Completion percentage (%)';
COMMENT ON COLUMN "Enrollment".last_accessed_at IS 'Last access time';
-- Create unique index for enrollment (handles NULL class_id as zero-UUID)
CREATE UNIQUE INDEX uq_enrollment_idx ON "Enrollment" (
  user_id, course_id, COALESCE(class_id, '00000000-0000-0000-0000-000000000000'::UUID)
);

-- Table 11: Attempt
CREATE TABLE "Attempt" (
  attempt_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  quiz_id UUID NOT NULL REFERENCES "Quiz"(quiz_id) ON DELETE CASCADE,
  user_id UUID NOT NULL REFERENCES "User"(user_id) ON DELETE CASCADE,
  enrollment_id UUID NOT NULL REFERENCES "Enrollment"(enrollment_id) ON DELETE CASCADE,
  attempt_number INT NOT NULL,
  started_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  submitted_at TIMESTAMP,
  time_spent_seconds INT DEFAULT 0,
  status VARCHAR(20) NOT NULL DEFAULT 'IN_PROGRESS',
  answers JSON,
  total_score DECIMAL(6,2),
  max_possible_score DECIMAL(6,2) NOT NULL,
  percentage_score DECIMAL(5,2),
  graded_at TIMESTAMP,
  graded_by UUID REFERENCES "User"(user_id) ON DELETE SET NULL,

  CONSTRAINT uq_attempt UNIQUE(user_id, quiz_id, attempt_number),
  CONSTRAINT chk_attempt_status CHECK (status IN (
    'IN_PROGRESS', 'SUBMITTED'
  ))
);

COMMENT ON TABLE "Attempt" IS 'Quiz attempts (includes answers in JSON)';
COMMENT ON COLUMN "Attempt".attempt_number IS 'Attempt number (1, 2, 3...)';
COMMENT ON COLUMN "Attempt".started_at IS 'Start time';
COMMENT ON COLUMN "Attempt".submitted_at IS 'Submission time';
COMMENT ON COLUMN "Attempt".time_spent_seconds IS 'Time spent (seconds)';
COMMENT ON COLUMN "Attempt".status IS 'Status: IN_PROGRESS, SUBMITTED';
COMMENT ON COLUMN "Attempt".answers IS 'Answers (JSON): [{"question_id": "uuid", "answer_text": "...", "score": 10}]';
COMMENT ON COLUMN "Attempt".total_score IS 'Total score';
COMMENT ON COLUMN "Attempt".max_possible_score IS 'Maximum possible score';
COMMENT ON COLUMN "Attempt".percentage_score IS 'Percentage score (%)';
COMMENT ON COLUMN "Attempt".graded_at IS 'Grading completion time';
COMMENT ON COLUMN "Attempt".graded_by IS 'Grader (Instructor)';

-- Table 12: AssignmentSubmission
CREATE TABLE "AssignmentSubmission" (
  submission_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  lecture_id UUID NOT NULL REFERENCES "Lecture"(lecture_id) ON DELETE CASCADE,
  user_id UUID NOT NULL REFERENCES "User"(user_id) ON DELETE CASCADE,
  enrollment_id UUID NOT NULL REFERENCES "Enrollment"(enrollment_id) ON DELETE CASCADE,
  submission_number INT NOT NULL DEFAULT 1,
  submitted_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  content TEXT,
  file_urls JSON,
  code_submission TEXT,
  is_late BOOLEAN DEFAULT FALSE,
  status VARCHAR(20) NOT NULL DEFAULT 'SUBMITTED',
  score DECIMAL(6,2),
  max_score DECIMAL(6,2),
  feedback TEXT,
  graded_at TIMESTAMP,
  graded_by UUID REFERENCES "User"(user_id) ON DELETE SET NULL,

  CONSTRAINT uq_assignment_submission UNIQUE(lecture_id, user_id, submission_number),
  CONSTRAINT chk_submission_status CHECK (status IN (
    'SUBMITTED', 'GRADING', 'GRADED', 'RETURNED'
  ))
);

COMMENT ON TABLE "AssignmentSubmission" IS 'Assignment submissions';
COMMENT ON COLUMN "AssignmentSubmission".lecture_id IS 'Which lecture (type = ASSIGNMENT)';
COMMENT ON COLUMN "AssignmentSubmission".submission_number IS 'Submission attempt number';
COMMENT ON COLUMN "AssignmentSubmission".submitted_at IS 'Submission time';
COMMENT ON COLUMN "AssignmentSubmission".content IS 'Text content (if text type)';
COMMENT ON COLUMN "AssignmentSubmission".file_urls IS 'Attached files list (JSON array): ["s3://...", "s3://..."]';
COMMENT ON COLUMN "AssignmentSubmission".code_submission IS 'Code (if code type)';
COMMENT ON COLUMN "AssignmentSubmission".is_late IS 'Submitted late?';
COMMENT ON COLUMN "AssignmentSubmission".status IS 'Status: SUBMITTED, GRADING, GRADED, RETURNED';
COMMENT ON COLUMN "AssignmentSubmission".score IS 'Score';
COMMENT ON COLUMN "AssignmentSubmission".max_score IS 'Maximum score';
COMMENT ON COLUMN "AssignmentSubmission".feedback IS 'Instructor feedback';
COMMENT ON COLUMN "AssignmentSubmission".graded_by IS 'Grader (Instructor)';

-- Table 14: Progress
CREATE TABLE "Progress" (
  progress_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID NOT NULL REFERENCES "User"(user_id) ON DELETE CASCADE,
  course_id UUID NOT NULL REFERENCES "Course"(course_id) ON DELETE CASCADE,
  module_id UUID NOT NULL REFERENCES "Module"(module_id) ON DELETE CASCADE,
  status VARCHAR(20) NOT NULL DEFAULT 'NOT_STARTED',
  started_at TIMESTAMP,
  completed_at TIMESTAMP,
  lecture_progress JSON,

  CONSTRAINT uq_progress UNIQUE(user_id, course_id, module_id),
  CONSTRAINT chk_progress_status CHECK (status IN ('NOT_STARTED', 'IN_PROGRESS', 'COMPLETED'))
);

COMMENT ON TABLE "Progress" IS 'Learning progress (module level)';
COMMENT ON COLUMN "Progress".status IS 'Status: NOT_STARTED, IN_PROGRESS, COMPLETED';
COMMENT ON COLUMN "Progress".started_at IS 'Module start time';
COMMENT ON COLUMN "Progress".completed_at IS 'Module completion time';
COMMENT ON COLUMN "Progress".lecture_progress IS 'Lecture progress (JSON, optional): {"lecture_uuid": {"percent": 80, "last_position": 1200}}';

-- Table 16: Certificate
CREATE TABLE "Certificate" (
  certificate_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID NOT NULL REFERENCES "User"(user_id) ON DELETE CASCADE,
  course_id UUID NOT NULL REFERENCES "Course"(course_id) ON DELETE CASCADE,
  certificate_code VARCHAR(50) NOT NULL UNIQUE,
  verification_code VARCHAR(100) NOT NULL UNIQUE,
  issue_date DATE NOT NULL,
  final_grade DECIMAL(5,2),
  pdf_url VARCHAR(500),
  status VARCHAR(20) DEFAULT 'ACTIVE',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT uq_certificate UNIQUE(user_id, course_id),
  CONSTRAINT chk_certificate_status CHECK (status IN ('ACTIVE', 'REVOKED'))
);

COMMENT ON TABLE "Certificate" IS 'Course completion certificates';
COMMENT ON COLUMN "Certificate".certificate_code IS 'Public certificate code (e.g. BL-2025-000001)';
COMMENT ON COLUMN "Certificate".verification_code IS 'Secret verification code (hash)';
COMMENT ON COLUMN "Certificate".issue_date IS 'Certificate issue date';
COMMENT ON COLUMN "Certificate".final_grade IS 'Final course grade';
COMMENT ON COLUMN "Certificate".pdf_url IS 'Certificate PDF file path (S3/GCS)';
COMMENT ON COLUMN "Certificate".status IS 'Status: ACTIVE (valid), REVOKED (revoked)';

-- ============================================
-- END OF SCHEMA
-- ============================================