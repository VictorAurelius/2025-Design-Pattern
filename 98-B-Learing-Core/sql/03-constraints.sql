-- ============================================
-- 03-constraints.sql - Additional Constraints
-- B-Learning Core v1.0
-- Author: Nguyen Van Kiet - CNTT1-K63
-- Created: 2025-11-27
-- Fixed: UTF-8 Encoding & Column References
-- ============================================

-- Purpose: Additional constraints that complement the schema
-- Note: Most basic constraints are already defined in 01-schema.sql

-- ============================================
-- UNIQUE CONSTRAINTS (Additional)
-- ============================================

-- UserRole - prevent duplicate role assignments
-- (Already handled by uq_user_role in schema)

-- Module - ensure order numbers are sequential within course
-- (Already handled by uq_module_order in schema)

-- Lecture - ensure order numbers are sequential within module  
-- (Already handled by uq_lecture_order in schema)

-- Option - ensure order numbers are sequential within question
-- (Already handled by uq_option_order in schema)

-- Attempt - prevent multiple attempts with same number
-- (Already handled by uq_attempt in schema)

-- AssignmentSubmission - prevent duplicate submissions
-- (Already handled by uq_assignment_submission in schema)

-- Progress - one progress record per user per module
-- (Already handled by uq_progress in schema)

-- Certificate - one certificate per user per course
-- (Already handled by uq_certificate in schema)

-- ============================================
-- CHECK CONSTRAINTS (Additional)
-- ============================================

-- Course constraints
ALTER TABLE "Course"
  ADD CONSTRAINT chk_course_estimated_hours_positive 
  CHECK (estimated_hours IS NULL OR estimated_hours > 0);

-- Module constraints  
ALTER TABLE "Module"
  ADD CONSTRAINT chk_module_order_positive 
  CHECK (order_num > 0);

ALTER TABLE "Module"
  ADD CONSTRAINT chk_module_duration_positive
  CHECK (estimated_duration_minutes IS NULL OR estimated_duration_minutes > 0);

-- Lecture constraints
ALTER TABLE "Lecture" 
  ADD CONSTRAINT chk_lecture_order_positive
  CHECK (order_num > 0);

ALTER TABLE "Lecture"
  ADD CONSTRAINT chk_lecture_duration_positive
  CHECK (duration_seconds IS NULL OR duration_seconds > 0);

-- Quiz constraints
ALTER TABLE "Quiz"
  ADD CONSTRAINT chk_quiz_time_limit_positive
  CHECK (time_limit_minutes IS NULL OR time_limit_minutes > 0);

ALTER TABLE "Quiz" 
  ADD CONSTRAINT chk_quiz_pass_score_range
  CHECK (pass_score IS NULL OR (pass_score >= 0 AND pass_score <= 100));

-- Question constraints
ALTER TABLE "Question"
  ADD CONSTRAINT chk_question_max_points_positive
  CHECK (max_points > 0);

-- Option constraints
ALTER TABLE "Option"
  ADD CONSTRAINT chk_option_order_positive
  CHECK (order_num > 0);

-- Attempt constraints  
ALTER TABLE "Attempt"
  ADD CONSTRAINT chk_attempt_number_positive
  CHECK (attempt_number > 0);

ALTER TABLE "Attempt"
  ADD CONSTRAINT chk_attempt_time_spent_positive
  CHECK (time_spent_seconds >= 0);

ALTER TABLE "Attempt" 
  ADD CONSTRAINT chk_attempt_scores_positive
  CHECK (
    (total_score IS NULL OR total_score >= 0) AND
    (max_possible_score >= 0) AND
    (percentage_score IS NULL OR (percentage_score >= 0 AND percentage_score <= 100))
  );

-- AssignmentSubmission constraints
ALTER TABLE "AssignmentSubmission"
  ADD CONSTRAINT chk_assignment_submission_number_positive
  CHECK (submission_number > 0);

ALTER TABLE "AssignmentSubmission"
  ADD CONSTRAINT chk_assignment_scores_positive
  CHECK (
    (score IS NULL OR score >= 0) AND
    (max_score IS NULL OR max_score >= 0)
  );

-- Enrollment constraints
ALTER TABLE "Enrollment"
  ADD CONSTRAINT chk_enrollment_completion_percentage
  CHECK (completion_percentage >= 0 AND completion_percentage <= 100);

ALTER TABLE "Enrollment"
  ADD CONSTRAINT chk_enrollment_final_grade_positive
  CHECK (final_grade IS NULL OR final_grade >= 0);

-- Class constraints
ALTER TABLE "Class"
  ADD CONSTRAINT chk_class_max_students_positive
  CHECK (max_students IS NULL OR max_students > 0);

ALTER TABLE "Class"
  ADD CONSTRAINT chk_class_date_order
  CHECK (end_date IS NULL OR start_date IS NULL OR end_date >= start_date);

-- Certificate constraints
ALTER TABLE "Certificate" 
  ADD CONSTRAINT chk_certificate_final_grade_positive
  CHECK (final_grade IS NULL OR final_grade >= 0);

-- ============================================
-- FUNCTIONAL CONSTRAINTS
-- ============================================

-- Ensure submitted attempts have submission time
ALTER TABLE "Attempt"
  ADD CONSTRAINT chk_attempt_submitted_has_time
  CHECK (
    (status != 'SUBMITTED') OR 
    (status = 'SUBMITTED' AND submitted_at IS NOT NULL)
  );

-- Ensure graded attempts have grade information
ALTER TABLE "Attempt"
  ADD CONSTRAINT chk_attempt_graded_has_info
  CHECK (
    (graded_at IS NULL AND graded_by IS NULL) OR
    (graded_at IS NOT NULL AND graded_by IS NOT NULL)
  );

-- Ensure assignment grades have grader info
ALTER TABLE "AssignmentSubmission"
  ADD CONSTRAINT chk_assignment_graded_has_info  
  CHECK (
    (graded_at IS NULL AND graded_by IS NULL) OR
    (graded_at IS NOT NULL AND graded_by IS NOT NULL)
  );

-- Ensure completed enrollments have completion time
ALTER TABLE "Enrollment"
  ADD CONSTRAINT chk_enrollment_completed_has_time
  CHECK (
    (status != 'COMPLETED') OR
    (status = 'COMPLETED' AND completed_at IS NOT NULL)
  );

-- Ensure completed progress has completion time  
ALTER TABLE "Progress"
  ADD CONSTRAINT chk_progress_completed_has_time
  CHECK (
    (status != 'COMPLETED') OR
    (status = 'COMPLETED' AND completed_at IS NOT NULL)
  );

-- ============================================
-- COMMENTS ON CONSTRAINTS  
-- ============================================

COMMENT ON CONSTRAINT chk_course_estimated_hours_positive ON "Course" 
  IS 'Ensure course estimated hours is positive when provided';

COMMENT ON CONSTRAINT chk_quiz_pass_score_range ON "Quiz"
  IS 'Ensure pass score is between 0 and 100 percent when provided';

COMMENT ON CONSTRAINT chk_attempt_scores_positive ON "Attempt"
  IS 'Ensure all score fields are non-negative and percentage is 0-100';

COMMENT ON CONSTRAINT chk_class_date_order ON "Class"
  IS 'Ensure end date is not before start date';

COMMENT ON CONSTRAINT chk_attempt_submitted_has_time ON "Attempt"
  IS 'Ensure submitted attempts have submission timestamp';

COMMENT ON CONSTRAINT chk_enrollment_completed_has_time ON "Enrollment"
  IS 'Ensure completed enrollments have completion timestamp';

-- ============================================
-- CONSTRAINT SUMMARY
-- ============================================

-- Total Additional Constraints: ~25
-- - Check constraints for positive values: 12
-- - Check constraints for ranges: 4  
-- - Check constraints for logical consistency: 6
-- - Check constraints for data integrity: 3

-- Note: Many constraints are already defined in the schema file:
-- - Primary keys, foreign keys, and basic check constraints
-- - Unique constraints for business rules
-- - NOT NULL constraints for required fields

-- These additional constraints provide:
-- - Data validation beyond basic schema rules
-- - Business logic enforcement
-- - Data integrity for calculated fields
-- - Consistency checks for related fields

-- ============================================
-- END OF CONSTRAINTS
-- ============================================