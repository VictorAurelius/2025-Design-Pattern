-- ============================================
-- B-LEARNING DATABASE SCHEMA
-- Version: 2.0 (Redesigned)
-- Database: PostgreSQL 14+
-- Created: 2025-11-25
-- Author: Nguyễn Văn Kiệt - CNTT1-K63
-- ============================================

-- Enable required extensions
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

  CONSTRAINT chk_user_account_status CHECK (account_status IN (
    'PENDING_VERIFICATION', 'ACTIVE', 'SUSPENDED', 'DELETED'
  ))
);

COMMENT ON TABLE "User" IS 'User accounts with profile and authentication';
COMMENT ON COLUMN "User".user_id IS 'Unique user identifier (UUID)';
COMMENT ON COLUMN "User".email IS 'Email address (login credential, unique)';
COMMENT ON COLUMN "User".password_hash IS 'Hashed password (bcrypt/scrypt)';

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

-- ============================================
-- 2. COURSE CONTENT (4 tables)
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

-- ============================================
-- 3. CLASS & BLENDED LEARNING (3 tables)
-- Must be created before CourseEnrollment for FK references
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

-- ============================================
-- 4. ENROLLMENT & PROGRESS (4 tables)
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
  
  -- Note: expression (COALESCE) cannot be used inside a table-level
  -- UNIQUE constraint. We will create an equivalent unique index after
  -- the table definition to treat NULL class_id as the zero-UUID.
  CONSTRAINT chk_progress_status CHECK (status IN (
    'NOT_STARTED', 'IN_PROGRESS', 'COMPLETED', 'SKIPPED'
  )),
  CONSTRAINT chk_progress_percent CHECK (
    percent_complete >= 0 AND percent_complete <= 100
  )
);

COMMENT ON TABLE "Progress" IS 'Learning progress tracking (REDESIGNED from v1.0)';

-- Unique index to enforce uniqueness treating NULL class_id as zero-UUID
CREATE UNIQUE INDEX IF NOT EXISTS uq_progress ON "Progress" (
  user_id, course_id,
  COALESCE(class_id, '00000000-0000-0000-0000-000000000000'::UUID),
  module_id, lecture_id
);

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

-- ============================================
-- 5. ASSESSMENT (9 tables)
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
  
  -- Note: expression (COALESCE) cannot be used inside a table-level
  -- UNIQUE constraint. We'll enforce the intended uniqueness with
  -- a unique index created after the table definition.
);

COMMENT ON TABLE "GradeBook" IS 'Aggregated grades per student per course (NEW)';

-- Unique index to treat NULL class_id as zero-UUID for uniqueness
CREATE UNIQUE INDEX IF NOT EXISTS uq_gradebook ON "GradeBook" (
  user_id, course_id,
  COALESCE(class_id, '00000000-0000-0000-0000-000000000000'::UUID)
);

-- ============================================
-- 6. CERTIFICATE (3 tables)
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

-- ============================================
-- 7. NOTIFICATION (3 tables - NEW)
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

-- ============================================
-- 8. AUDIT & SYSTEM (3 tables - NEW)
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

-- ============================================
-- SCHEMA CREATION COMPLETE
-- Total tables: 31
-- ============================================
