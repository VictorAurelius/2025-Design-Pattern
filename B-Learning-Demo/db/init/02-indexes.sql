-- ============================================
-- 02-indexes.sql - Performance Indexes
-- B-Learning Core v1.0
-- Nguyễn Văn Kiệt - CNTT1-K63
-- Created: 2025-11-27
-- ============================================

-- Mục đích:
-- 1. Indexes cho tất cả Foreign Keys (23 relationships)
-- 2. Performance indexes cho các trường thường query
-- 3. GIN indexes cho JSON fields (PostgreSQL 14+)
-- 4. Full-text search indexes

-- ============================================
-- SECTION 1: FOREIGN KEY INDEXES
-- ============================================
-- Lý do: Tăng tốc JOIN operations và FK constraint checks

-- Domain 1: User Management
CREATE INDEX idx_userrole_user ON "UserRole"(user_id);
CREATE INDEX idx_userrole_role ON "UserRole"(role_id);

-- Domain 2: Course Content
CREATE INDEX idx_course_creator ON "Course"(created_by);
CREATE INDEX idx_module_course ON "Module"(course_id);
CREATE INDEX idx_lecture_module ON "Lecture"(module_id);
CREATE INDEX idx_resource_lecture ON "Resource"(lecture_id);

-- Domain 3: Assessment
CREATE INDEX idx_quiz_course ON "Quiz"(course_id);
CREATE INDEX idx_quiz_creator ON "Quiz"(created_by);
CREATE INDEX idx_question_course ON "Question"(course_id);
CREATE INDEX idx_question_creator ON "Question"(created_by);
CREATE INDEX idx_option_question ON "Option"(question_id);

CREATE INDEX idx_attempt_quiz ON "Attempt"(quiz_id);
CREATE INDEX idx_attempt_user ON "Attempt"(user_id);
CREATE INDEX idx_attempt_enrollment ON "Attempt"(enrollment_id);

CREATE INDEX idx_assignment_lecture ON "AssignmentSubmission"(lecture_id);
CREATE INDEX idx_assignment_user ON "AssignmentSubmission"(user_id);
CREATE INDEX idx_assignment_enrollment ON "AssignmentSubmission"(enrollment_id);

-- Domain 4: Enrollment & Progress
CREATE INDEX idx_enrollment_user ON "Enrollment"(user_id);
CREATE INDEX idx_enrollment_course ON "Enrollment"(course_id);
CREATE INDEX idx_enrollment_class ON "Enrollment"(class_id); -- nullable

CREATE INDEX idx_progress_user ON "Progress"(user_id);
CREATE INDEX idx_progress_course ON "Progress"(course_id);
CREATE INDEX idx_progress_module ON "Progress"(module_id);

-- Domain 5: Class & Certificate
CREATE INDEX idx_class_course ON "Class"(course_id);
CREATE INDEX idx_class_instructor ON "Class"(instructor_id); -- nullable

CREATE INDEX idx_certificate_user ON "Certificate"(user_id);
CREATE INDEX idx_certificate_course ON "Certificate"(course_id);

-- ============================================
-- SECTION 2: PERFORMANCE INDEXES
-- ============================================
-- Indexes cho các query thường xuyên

-- User queries
CREATE INDEX idx_user_email ON "User"(email);
CREATE INDEX idx_user_account_status ON "User"(account_status);
CREATE INDEX idx_user_created_at ON "User"(created_at DESC);

-- Role queries
CREATE INDEX idx_role_name ON "Role"(name);

-- Course queries
CREATE INDEX idx_course_code ON "Course"(code);
CREATE INDEX idx_course_status ON "Course"(status);
CREATE INDEX idx_course_difficulty ON "Course"(difficulty_level);
CREATE INDEX idx_course_created_at ON "Course"(created_at DESC);

-- Module queries
CREATE INDEX idx_module_order ON "Module"(course_id, order_num);

-- Lecture queries
CREATE INDEX idx_lecture_order ON "Lecture"(module_id, order_num);
CREATE INDEX idx_lecture_type ON "Lecture"(type);

-- Quiz queries
CREATE INDEX idx_quiz_status ON "Quiz"(status);
CREATE INDEX idx_quiz_available ON "Quiz"(available_from, available_until);

-- Question queries
CREATE INDEX idx_question_type ON "Question"(type);

-- Attempt queries
CREATE INDEX idx_attempt_user_quiz ON "Attempt"(user_id, quiz_id);
CREATE INDEX idx_attempt_submitted ON "Attempt"(submitted_at DESC);
CREATE INDEX idx_attempt_status ON "Attempt"(status);

-- AssignmentSubmission queries
CREATE INDEX idx_assignment_submitted ON "AssignmentSubmission"(submitted_at DESC);
CREATE INDEX idx_assignment_status ON "AssignmentSubmission"(status);

-- Enrollment queries
CREATE INDEX idx_enrollment_user_course ON "Enrollment"(user_id, course_id);
CREATE INDEX idx_enrollment_status ON "Enrollment"(status);
CREATE INDEX idx_enrollment_enrolled_at ON "Enrollment"(enrolled_at DESC);

-- Progress queries
CREATE INDEX idx_progress_user_course ON "Progress"(user_id, course_id);
CREATE INDEX idx_progress_status ON "Progress"(status);

-- Class queries
CREATE INDEX idx_class_status ON "Class"(status);
CREATE INDEX idx_class_dates ON "Class"(start_date, end_date);

-- Certificate queries
CREATE INDEX idx_certificate_code ON "Certificate"(certificate_code);
CREATE INDEX idx_certificate_verification ON "Certificate"(verification_code);
CREATE INDEX idx_certificate_status ON "Certificate"(status);
CREATE INDEX idx_certificate_issue_date ON "Certificate"(issue_date DESC);

-- ============================================
-- SECTION 3: JSON GIN INDEXES
-- ============================================
-- GIN indexes cho JSON fields - hỗ trợ @>, ->, ->>, #> operators

-- User.preferences - query notification settings, locale, timezone
CREATE INDEX idx_user_preferences ON "User" USING GIN (preferences);

-- Quiz.questions - query quiz structure, question order, points
CREATE INDEX idx_quiz_questions ON "Quiz" USING GIN (questions);

-- Attempt.answers - query student responses, scores, grading status
CREATE INDEX idx_attempt_answers ON "Attempt" USING GIN (answers);

-- Lecture.assignment_config - query due dates, submission types, rubric
CREATE INDEX idx_lecture_assignment_config ON "Lecture" USING GIN (assignment_config);

-- AssignmentSubmission.file_urls - query submitted files
CREATE INDEX idx_assignment_file_urls ON "AssignmentSubmission" USING GIN (file_urls);

-- Class.schedules - query session dates, attendance records
CREATE INDEX idx_class_schedules ON "Class" USING GIN (schedules);

-- Module.prerequisite_module_ids - query prerequisites
CREATE INDEX idx_module_prerequisites ON "Module" USING GIN (prerequisite_module_ids);

-- Role.permissions - query role permissions
CREATE INDEX idx_role_permissions ON "Role" USING GIN (permissions);

-- ============================================
-- SECTION 4: FULL-TEXT SEARCH INDEXES
-- ============================================
-- Full-text search cho text fields (PostgreSQL tsvector)

-- Course search (title + description)
CREATE INDEX idx_course_search ON "Course" USING GIN(
  to_tsvector('english', COALESCE(title, '') || ' ' || COALESCE(description, ''))
);

-- Module search (title + description)
CREATE INDEX idx_module_search ON "Module" USING GIN(
  to_tsvector('english', COALESCE(title, '') || ' ' || COALESCE(description, ''))
);

-- Lecture search (title + description)
CREATE INDEX idx_lecture_search ON "Lecture" USING GIN(
  to_tsvector('english', COALESCE(title, '') || ' ' || COALESCE(description, ''))
);

-- Quiz search (title + description + instructions)
CREATE INDEX idx_quiz_search ON "Quiz" USING GIN(
  to_tsvector('english',
    COALESCE(title, '') || ' ' ||
    COALESCE(description, '') || ' ' ||
    COALESCE(instructions, '')
  )
);

-- Question search (question_text)
CREATE INDEX idx_question_search ON "Question" USING GIN(
  to_tsvector('english', COALESCE(question_text, ''))
);

-- ============================================
-- SECTION 5: COMPOSITE INDEXES
-- ============================================
-- Multi-column indexes cho queries phức tạp

-- Enrollment: query active enrollments by user
CREATE INDEX idx_enrollment_user_status ON "Enrollment"(user_id, status);

-- Enrollment: query class enrollments
CREATE INDEX idx_enrollment_class_status ON "Enrollment"(class_id, status) WHERE class_id IS NOT NULL;

-- Progress: query user's course progress
CREATE INDEX idx_progress_user_course_status ON "Progress"(user_id, course_id, status);

-- Attempt: query user's quiz attempts
CREATE INDEX idx_attempt_user_quiz_status ON "Attempt"(user_id, quiz_id, status);

-- AssignmentSubmission: query user's assignments
CREATE INDEX idx_assignment_user_status ON "AssignmentSubmission"(user_id, status);

-- Class: query active classes for a course
CREATE INDEX idx_class_course_status ON "Class"(course_id, status);

-- Certificate: query active certificates
CREATE INDEX idx_certificate_user_status ON "Certificate"(user_id, status);

-- ============================================
-- SECTION 6: PARTIAL INDEXES
-- ============================================
-- Partial indexes cho các điều kiện query thường gặp

-- Active users only
CREATE INDEX idx_user_active ON "User"(email) WHERE account_status = 'ACTIVE';

-- Published courses only
CREATE INDEX idx_course_published ON "Course"(code, title) WHERE status = 'PUBLISHED';

-- Pending/graded attempts
CREATE INDEX idx_attempt_pending ON "Attempt"(quiz_id, user_id) WHERE status = 'PENDING_GRADING';

-- Pending assignments
CREATE INDEX idx_assignment_pending ON "AssignmentSubmission"(lecture_id, user_id) WHERE status = 'PENDING_GRADING';

-- Ongoing classes
CREATE INDEX idx_class_ongoing ON "Class"(course_id, instructor_id) WHERE status = 'ONGOING';

-- Active certificates
CREATE INDEX idx_certificate_active ON "Certificate"(certificate_code) WHERE status = 'ACTIVE';

-- ============================================
-- COMMENTS
-- ============================================

COMMENT ON INDEX idx_user_email IS 'Tăng tốc login và email lookup';
COMMENT ON INDEX idx_user_preferences IS 'GIN index cho JSON preferences - query notification settings';

COMMENT ON INDEX idx_course_code IS 'Tăng tốc search course by code (unique identifier)';
COMMENT ON INDEX idx_course_search IS 'Full-text search cho course title và description';

COMMENT ON INDEX idx_quiz_questions IS 'GIN index cho Quiz.questions JSON - query quiz structure';
COMMENT ON INDEX idx_attempt_answers IS 'GIN index cho Attempt.answers JSON - query student responses';

COMMENT ON INDEX idx_lecture_assignment_config IS 'GIN index cho assignment configuration JSON';
COMMENT ON INDEX idx_class_schedules IS 'GIN index cho Class.schedules JSON - query sessions và attendance';

COMMENT ON INDEX idx_enrollment_user_course ON "Enrollment" IS 'Composite index cho lookup enrollment by user và course';
COMMENT ON INDEX idx_progress_user_course_status IS 'Composite index cho track learning progress';

COMMENT ON INDEX idx_user_active IS 'Partial index - chỉ index active users để tăng tốc login';
COMMENT ON INDEX idx_course_published IS 'Partial index - chỉ index published courses cho catalog';

-- ============================================
-- STATISTICS
-- ============================================

-- Total indexes created:
-- - Foreign Key Indexes: 28
-- - Performance Indexes: 26
-- - JSON GIN Indexes: 8
-- - Full-Text Search: 5
-- - Composite Indexes: 7
-- - Partial Indexes: 6
-- TOTAL: 80 indexes

-- Lưu ý:
-- 1. GIN indexes tốn nhiều storage nhưng rất nhanh cho JSON queries
-- 2. Partial indexes tiết kiệm storage và tăng performance cho filtered queries
-- 3. Full-text search cần maintain tsvector khi data update
-- 4. Nên monitor index usage với pg_stat_user_indexes
-- 5. Drop unused indexes để tiết kiệm storage và write performance

-- ============================================
-- END OF INDEXES
-- ============================================
