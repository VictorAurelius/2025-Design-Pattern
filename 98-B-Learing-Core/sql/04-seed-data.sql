-- ============================================
-- 04-seed-data.sql - Sample Data
-- B-Learning Core v1.0
-- Author: Nguyen Van Kiet - CNTT1-K63
-- Created: 2025-11-27
-- Fixed: UTF-8 Encoding & Column References
-- ============================================

-- Purpose: Insert sample data for testing and demonstration
-- Note: All column names match the corrected schema

-- ============================================
-- DOMAIN 1: USER MANAGEMENT
-- ============================================

-- Insert Roles
INSERT INTO "Role" (role_id, name, description, permissions) VALUES
('10000000-0000-0000-0000-000000000001', 'ADMIN', 'System Administrator', '{"manage_users": true, "manage_courses": true, "manage_system": true}'),
('10000000-0000-0000-0000-000000000002', 'INSTRUCTOR', 'Course Instructor', '{"create_courses": true, "manage_classes": true, "grade_submissions": true}'),
('10000000-0000-0000-0000-000000000003', 'TA', 'Teaching Assistant', '{"assist_classes": true, "grade_assignments": true}'),
('10000000-0000-0000-0000-000000000004', 'STUDENT', 'Student User', '{"enroll_courses": true, "submit_assignments": true, "take_quizzes": true}');

-- Insert Users
INSERT INTO "User" (user_id, email, password_hash, first_name, last_name, account_status) VALUES
('20000000-0000-0000-0000-000000000101', 'admin@blearning.com', '$2b$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewdBPj/3OzjKqBR6', 'System', 'Administrator', 'ACTIVE'),
('20000000-0000-0000-0000-000000000102', 'instructor@blearning.com', '$2b$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewdBPj/3OzjKqBR6', 'John', 'Smith', 'ACTIVE'),
('20000000-0000-0000-0000-000000000103', 'ta@blearning.com', '$2b$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewdBPj/3OzjKqBR6', 'Mary', 'Johnson', 'ACTIVE'),
('20000000-0000-0000-0000-000000000104', 'student1@blearning.com', '$2b$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewdBPj/3OzjKqBR6', 'Alice', 'Wilson', 'ACTIVE'),
('20000000-0000-0000-0000-000000000105', 'student2@blearning.com', '$2b$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewdBPj/3OzjKqBR6', 'Bob', 'Davis', 'ACTIVE');

-- Assign User Roles (fixed column name: granted_at instead of assigned_at)
INSERT INTO "UserRole" (user_role_id, user_id, role_id, granted_at) VALUES
('30000000-0000-0000-0000-000000000001', '20000000-0000-0000-0000-000000000101', '10000000-0000-0000-0000-000000000001', CURRENT_TIMESTAMP),
('30000000-0000-0000-0000-000000000002', '20000000-0000-0000-0000-000000000102', '10000000-0000-0000-0000-000000000002', CURRENT_TIMESTAMP),
('30000000-0000-0000-0000-000000000003', '20000000-0000-0000-0000-000000000103', '10000000-0000-0000-0000-000000000003', CURRENT_TIMESTAMP),
('30000000-0000-0000-0000-000000000004', '20000000-0000-0000-0000-000000000104', '10000000-0000-0000-0000-000000000004', CURRENT_TIMESTAMP),
('30000000-0000-0000-0000-000000000005', '20000000-0000-0000-0000-000000000105', '10000000-0000-0000-0000-000000000004', CURRENT_TIMESTAMP);

-- ============================================
-- DOMAIN 2: COURSE CONTENT  
-- ============================================

-- Insert Courses
INSERT INTO "Course" (course_id, code, title, description, difficulty_level, status, created_by, published_at) VALUES
('40000000-0000-0000-0000-000000000001', 'JAVA101', 'Introduction to Java Programming', 'Learn the fundamentals of Java programming language', 'BEGINNER', 'PUBLISHED', '20000000-0000-0000-0000-000000000102', CURRENT_TIMESTAMP),
('40000000-0000-0000-0000-000000000002', 'WEB201', 'Web Development with HTML/CSS', 'Master frontend web development technologies', 'INTERMEDIATE', 'PUBLISHED', '20000000-0000-0000-0000-000000000102', CURRENT_TIMESTAMP);

-- Insert Modules
INSERT INTO "Module" (module_id, course_id, title, description, order_num) VALUES
('50000000-0000-0000-0000-000000000001', '40000000-0000-0000-0000-000000000001', 'Getting Started with Java', 'Introduction to Java environment and basic syntax', 1),
('50000000-0000-0000-0000-000000000002', '40000000-0000-0000-0000-000000000001', 'Object-Oriented Programming', 'Classes, objects, inheritance and polymorphism', 2),
('50000000-0000-0000-0000-000000000003', '40000000-0000-0000-0000-000000000002', 'HTML Fundamentals', 'HTML structure, elements and semantic markup', 1),
('50000000-0000-0000-0000-000000000004', '40000000-0000-0000-0000-000000000002', 'CSS Styling', 'CSS selectors, properties and responsive design', 2);

-- Insert Lectures
INSERT INTO "Lecture" (lecture_id, module_id, title, description, type, order_num) VALUES
('60000000-0000-0000-0000-000000000001', '50000000-0000-0000-0000-000000000001', 'Java Installation and Setup', 'Setting up Java development environment', 'VIDEO', 1),
('60000000-0000-0000-0000-000000000002', '50000000-0000-0000-0000-000000000001', 'Hello World Program', 'Your first Java program', 'TEXT', 2),
('60000000-0000-0000-0000-000000000003', '50000000-0000-0000-0000-000000000002', 'Classes and Objects', 'Understanding OOP concepts', 'VIDEO', 1),
('60000000-0000-0000-0000-000000000004', '50000000-0000-0000-0000-000000000003', 'HTML Document Structure', 'Basic HTML document layout', 'TEXT', 1);

-- Insert Resources
INSERT INTO "Resource" (resource_id, lecture_id, title, file_url, file_type) VALUES
('70000000-0000-0000-0000-000000000001', '60000000-0000-0000-0000-000000000001', 'Java Installation Guide', 'https://s3.amazonaws.com/blearning/docs/java-install.pdf', 'application/pdf'),
('70000000-0000-0000-0000-000000000002', '60000000-0000-0000-0000-000000000002', 'Hello World Source Code', 'https://s3.amazonaws.com/blearning/code/HelloWorld.java', 'text/plain');

-- ============================================
-- DOMAIN 3: ASSESSMENT
-- ============================================

-- Insert Quizzes
INSERT INTO "Quiz" (quiz_id, course_id, title, description, questions, created_by) VALUES
('80000000-0000-0000-0000-000000000001', '40000000-0000-0000-0000-000000000001', 'Java Basics Quiz', 'Test your knowledge of Java fundamentals', '[]', '20000000-0000-0000-0000-000000000102'),
('80000000-0000-0000-0000-000000000002', '40000000-0000-0000-0000-000000000002', 'HTML/CSS Quiz', 'Web development knowledge check', '[]', '20000000-0000-0000-0000-000000000102');

-- Insert Questions (fixed column name: text instead of question_text)
INSERT INTO "Question" (question_id, course_id, text, type, max_points, created_by) VALUES
('90000000-0000-0000-0000-000000000001', '40000000-0000-0000-0000-000000000001', 'What is the main method signature in Java?', 'MCQ', 2.0, '20000000-0000-0000-0000-000000000102'),
('90000000-0000-0000-0000-000000000002', '40000000-0000-0000-0000-000000000001', 'Java is a compiled language.', 'TRUE_FALSE', 1.0, '20000000-0000-0000-0000-000000000102'),
('90000000-0000-0000-0000-000000000003', '40000000-0000-0000-0000-000000000002', 'What does HTML stand for?', 'SHORT_ANSWER', 1.5, '20000000-0000-0000-0000-000000000102');

-- Insert Options
INSERT INTO "Option" (option_id, question_id, option_text, is_correct, order_num) VALUES
('a0000000-0000-0000-0000-000000000001', '90000000-0000-0000-0000-000000000001', 'public static void main(String[] args)', true, 1),
('a0000000-0000-0000-0000-000000000002', '90000000-0000-0000-0000-000000000001', 'public void main(String[] args)', false, 2),
('a0000000-0000-0000-0000-000000000003', '90000000-0000-0000-0000-000000000001', 'static void main(String[] args)', false, 3),
('a0000000-0000-0000-0000-000000000004', '90000000-0000-0000-0000-000000000001', 'public main(String[] args)', false, 4),
('a0000000-0000-0000-0000-000000000005', '90000000-0000-0000-0000-000000000002', 'True', true, 1),
('a0000000-0000-0000-0000-000000000006', '90000000-0000-0000-0000-000000000002', 'False', false, 2);

-- ============================================
-- DOMAIN 5: CLASS & CERTIFICATES
-- ============================================

-- Insert Classes
INSERT INTO "Class" (class_id, course_id, instructor_id, name, status, start_date, end_date) VALUES
('b0000000-0000-0000-0000-000000000001', '40000000-0000-0000-0000-000000000001', '20000000-0000-0000-0000-000000000102', 'Java 101 - Morning Class', 'ONGOING', '2025-11-01', '2025-12-31'),
('b0000000-0000-0000-0000-000000000002', '40000000-0000-0000-0000-000000000002', '20000000-0000-0000-0000-000000000102', 'Web Dev - Evening Class', 'SCHEDULED', '2025-12-01', '2026-01-31');

-- ============================================
-- DOMAIN 4: ENROLLMENT & PROGRESS
-- ============================================

-- Insert Enrollments (fixed: added required role column)
INSERT INTO "Enrollment" (enrollment_id, user_id, course_id, class_id, role, status, enrolled_at) VALUES
('c0000000-0000-0000-0000-000000000001', '20000000-0000-0000-0000-000000000104', '40000000-0000-0000-0000-000000000001', 'b0000000-0000-0000-0000-000000000001', 'STUDENT', 'ACTIVE', '2025-10-30'),
('c0000000-0000-0000-0000-000000000002', '20000000-0000-0000-0000-000000000105', '40000000-0000-0000-0000-000000000001', 'b0000000-0000-0000-0000-000000000001', 'STUDENT', 'ACTIVE', '2025-11-01'),
('c0000000-0000-0000-0000-000000000003', '20000000-0000-0000-0000-000000000104', '40000000-0000-0000-0000-000000000002', NULL, 'STUDENT', 'ACTIVE', '2025-11-14');

-- Insert Progress (added completed_at for COMPLETED status)
INSERT INTO "Progress" (progress_id, user_id, course_id, module_id, status, completed_at) VALUES
('d0000000-0000-0000-0000-000000000001', '20000000-0000-0000-0000-000000000104', '40000000-0000-0000-0000-000000000001', '50000000-0000-0000-0000-000000000001', 'COMPLETED', CURRENT_TIMESTAMP),
('d0000000-0000-0000-0000-000000000002', '20000000-0000-0000-0000-000000000104', '40000000-0000-0000-0000-000000000001', '50000000-0000-0000-0000-000000000002', 'IN_PROGRESS', NULL),
('d0000000-0000-0000-0000-000000000003', '20000000-0000-0000-0000-000000000105', '40000000-0000-0000-0000-000000000001', '50000000-0000-0000-0000-000000000001', 'IN_PROGRESS', NULL);

-- Insert Quiz Attempts (added submitted_at for SUBMITTED status)
INSERT INTO "Attempt" (attempt_id, quiz_id, user_id, enrollment_id, attempt_number, max_possible_score, status, submitted_at) VALUES
('e0000000-0000-0000-0000-000000000001', '80000000-0000-0000-0000-000000000001', '20000000-0000-0000-0000-000000000104', 'c0000000-0000-0000-0000-000000000001', 1, 10.0, 'SUBMITTED', CURRENT_TIMESTAMP),
('e0000000-0000-0000-0000-000000000002', '80000000-0000-0000-0000-000000000001', '20000000-0000-0000-0000-000000000105', 'c0000000-0000-0000-0000-000000000002', 1, 10.0, 'IN_PROGRESS', NULL);

-- Insert Assignment Submissions (using existing lecture IDs)
INSERT INTO "AssignmentSubmission" (submission_id, lecture_id, user_id, enrollment_id, content, status) VALUES
('f0000000-0000-0000-0000-000000000001', '60000000-0000-0000-0000-000000000002', '20000000-0000-0000-0000-000000000104', 'c0000000-0000-0000-0000-000000000001', 'public class HelloWorld { public static void main(String[] args) { System.out.println("Hello World!"); } }', 'GRADED'),
('f0000000-0000-0000-0000-000000000002', '60000000-0000-0000-0000-000000000002', '20000000-0000-0000-0000-000000000105', 'c0000000-0000-0000-0000-000000000002', 'public class Hello { public static void main(String[] args) { System.out.println("Hello!"); } }', 'SUBMITTED');

-- Insert Certificates (fixed UUID format)
INSERT INTO "Certificate" (certificate_id, user_id, course_id, certificate_code, verification_code, issue_date, final_grade, status) VALUES
('f1000000-0000-0000-0000-000000000001', '20000000-0000-0000-0000-000000000104', '40000000-0000-0000-0000-000000000001', 'BL-2025-000001', 'CERT-VERIFY-001', '2025-11-29', 85.5, 'ACTIVE');

-- ============================================
-- COMMENTS  
-- ============================================

COMMENT ON TABLE "Role" IS 'System roles with proper permissions structure';
COMMENT ON TABLE "User" IS 'Sample users representing different role types';
COMMENT ON TABLE "Course" IS 'Example courses for Java and Web Development';
COMMENT ON TABLE "Enrollment" IS 'Student enrollments in courses and classes';

-- ============================================
-- SEED DATA SUMMARY
-- ============================================

-- Total Records Inserted:
-- - Roles: 4 (ADMIN, INSTRUCTOR, TA, STUDENT)  
-- - Users: 5 (1 admin, 1 instructor, 1 TA, 2 students)
-- - UserRoles: 5 role assignments
-- - Courses: 2 (Java, Web Development)
-- - Modules: 4 (2 per course)
-- - Lectures: 4 (various types)
-- - Resources: 2 supplementary materials
-- - Quizzes: 2 assessment tools
-- - Questions: 3 different question types
-- - Options: 6 answer choices
-- - Classes: 2 scheduled classes
-- - Enrollments: 3 student enrollments
-- - Progress: 3 learning progress records
-- - Attempts: 2 quiz attempts
-- - Submissions: 2 assignment submissions  
-- - Certificates: 1 completion certificate

-- Data demonstrates:
-- - Complete user management workflow
-- - Course content structure
-- - Assessment and grading system
-- - Class-based and self-paced learning
-- - Progress tracking and certification

-- ============================================
-- END OF SEED DATA
-- ============================================
