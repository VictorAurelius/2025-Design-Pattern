-- ============================================
-- B-LEARNING DATABASE INDEXES
-- Version: 2.0
-- Purpose: Performance optimization indexes
-- Created: 2025-11-25
-- ============================================

-- ============================================
-- 1. FOREIGN KEY INDEXES
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

-- ============================================
-- 2. PERFORMANCE INDEXES
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

-- ============================================
-- 3. COMPOSITE INDEXES (for common query patterns)
-- ============================================

CREATE INDEX idx_progress_user_course ON "Progress"(user_id, course_id);
CREATE INDEX idx_attempt_user_quiz ON "Attempt"(user_id, quiz_id);
CREATE INDEX idx_activitylog_user_created ON "ActivityLog"(user_id, created_at DESC);
CREATE INDEX idx_notification_user_read ON "Notification"(user_id, is_read);

-- ============================================
-- 4. FULL-TEXT SEARCH INDEXES
-- ============================================

-- Course search (title + description)
CREATE INDEX idx_course_search ON "Course" USING GIN(
  to_tsvector('english', COALESCE(title, '') || ' ' || COALESCE(description, ''))
);

-- Question search
CREATE INDEX idx_question_search ON "Question" USING GIN(
  to_tsvector('english', text)
);

-- ============================================
-- INDEX CREATION COMPLETE
-- Total indexes: ~85
-- ============================================
