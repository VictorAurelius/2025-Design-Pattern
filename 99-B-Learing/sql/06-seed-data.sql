-- ============================================
-- B-LEARNING DATABASE SEED DATA
-- Version: 2.0
-- Purpose: Sample data for testing and development
-- Created: 2025-11-25
-- ============================================

-- IMPORTANT: This script is for development/testing only
-- DO NOT run in production environment

BEGIN;

-- ============================================
-- 1. ROLES
-- ============================================

INSERT INTO "Role" (name, description) VALUES
('ADMIN', 'System administrator with full access'),
('INSTRUCTOR', 'Course creator and instructor'),
('TA', 'Teaching assistant with limited instructor privileges'),
('STUDENT', 'Student with learning access');

-- ============================================
-- 2. USERS
-- ============================================

-- Admin users
INSERT INTO "User" (user_id, email, password_hash, first_name, last_name, phone_number, date_of_birth, gender, account_status, avatar_url, bio, timezone) VALUES
('00000000-0000-0000-0000-000000000001', 'admin@blelearning.com', '$2b$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5UpKKNr0EjEju', 'System', 'Admin', '+84901234567', '1985-01-15', 'OTHER', 'ACTIVE', 'https://i.pravatar.cc/150?img=1', 'System administrator', 'Asia/Ho_Chi_Minh'),
('00000000-0000-0000-0000-000000000002', 'support@blelearning.com', '$2b$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5UpKKNr0EjEju', 'Support', 'Team', '+84901234568', '1990-03-20', 'OTHER', 'ACTIVE', 'https://i.pravatar.cc/150?img=2', 'Customer support', 'Asia/Ho_Chi_Minh');

-- Instructor users
INSERT INTO "User" (user_id, email, password_hash, first_name, last_name, phone_number, date_of_birth, gender, account_status, avatar_url, bio, timezone) VALUES
('10000000-0000-0000-0000-000000000001', 'nguyen.van.a@instructor.com', '$2b$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5UpKKNr0EjEju', 'Nguyen', 'Van A', '+84912345001', '1980-05-10', 'MALE', 'ACTIVE', 'https://i.pravatar.cc/150?img=11', 'Senior software engineer with 15 years of experience in web development', 'Asia/Ho_Chi_Minh'),
('10000000-0000-0000-0000-000000000002', 'tran.thi.b@instructor.com', '$2b$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5UpKKNr0EjEju', 'Tran', 'Thi B', '+84912345002', '1985-08-22', 'FEMALE', 'ACTIVE', 'https://i.pravatar.cc/150?img=12', 'Data science expert and AI researcher', 'Asia/Ho_Chi_Minh'),
('10000000-0000-0000-0000-000000000003', 'le.van.c@instructor.com', '$2b$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5UpKKNr0EjEju', 'Le', 'Van C', '+84912345003', '1982-11-30', 'MALE', 'ACTIVE', 'https://i.pravatar.cc/150?img=13', 'Database architect and backend specialist', 'Asia/Ho_Chi_Minh');

-- TA users
INSERT INTO "User" (user_id, email, password_hash, first_name, last_name, phone_number, date_of_birth, gender, account_status, avatar_url, bio, timezone) VALUES
('20000000-0000-0000-0000-000000000001', 'pham.thi.d@ta.com', '$2b$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5UpKKNr0EjEju', 'Pham', 'Thi D', '+84912345004', '1995-02-14', 'FEMALE', 'ACTIVE', 'https://i.pravatar.cc/150?img=21', 'Teaching assistant for web development courses', 'Asia/Ho_Chi_Minh'),
('20000000-0000-0000-0000-000000000002', 'hoang.van.e@ta.com', '$2b$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5UpKKNr0EjEju', 'Hoang', 'Van E', '+84912345005', '1996-07-18', 'MALE', 'ACTIVE', 'https://i.pravatar.cc/150?img=22', 'Teaching assistant for data science courses', 'Asia/Ho_Chi_Minh');

-- Student users (20 students)
INSERT INTO "User" (user_id, email, password_hash, first_name, last_name, phone_number, date_of_birth, gender, account_status, avatar_url, timezone) VALUES
('30000000-0000-0000-0000-000000000001', 'student01@example.com', '$2b$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5UpKKNr0EjEju', 'Student', 'One', '+84912345101', '2000-01-01', 'MALE', 'ACTIVE', 'https://i.pravatar.cc/150?img=31', 'Asia/Ho_Chi_Minh'),
('30000000-0000-0000-0000-000000000002', 'student02@example.com', '$2b$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5UpKKNr0EjEju', 'Student', 'Two', '+84912345102', '2000-02-02', 'FEMALE', 'ACTIVE', 'https://i.pravatar.cc/150?img=32', 'Asia/Ho_Chi_Minh'),
('30000000-0000-0000-0000-000000000003', 'student03@example.com', '$2b$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5UpKKNr0EjEju', 'Student', 'Three', '+84912345103', '2000-03-03', 'MALE', 'ACTIVE', 'https://i.pravatar.cc/150?img=33', 'Asia/Ho_Chi_Minh'),
('30000000-0000-0000-0000-000000000004', 'student04@example.com', '$2b$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5UpKKNr0EjEju', 'Student', 'Four', '+84912345104', '2000-04-04', 'FEMALE', 'ACTIVE', 'https://i.pravatar.cc/150?img=34', 'Asia/Ho_Chi_Minh'),
('30000000-0000-0000-0000-000000000005', 'student05@example.com', '$2b$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5UpKKNr0EjEju', 'Student', 'Five', '+84912345105', '2000-05-05', 'MALE', 'ACTIVE', 'https://i.pravatar.cc/150?img=35', 'Asia/Ho_Chi_Minh'),
('30000000-0000-0000-0000-000000000006', 'student06@example.com', '$2b$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5UpKKNr0EjEju', 'Student', 'Six', '+84912345106', '2000-06-06', 'FEMALE', 'ACTIVE', 'https://i.pravatar.cc/150?img=36', 'Asia/Ho_Chi_Minh'),
('30000000-0000-0000-0000-000000000007', 'student07@example.com', '$2b$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5UpKKNr0EjEju', 'Student', 'Seven', '+84912345107', '2000-07-07', 'MALE', 'ACTIVE', 'https://i.pravatar.cc/150?img=37', 'Asia/Ho_Chi_Minh'),
('30000000-0000-0000-0000-000000000008', 'student08@example.com', '$2b$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5UpKKNr0EjEju', 'Student', 'Eight', '+84912345108', '2000-08-08', 'FEMALE', 'ACTIVE', 'https://i.pravatar.cc/150?img=38', 'Asia/Ho_Chi_Minh'),
('30000000-0000-0000-0000-000000000009', 'student09@example.com', '$2b$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5UpKKNr0EjEju', 'Student', 'Nine', '+84912345109', '2000-09-09', 'MALE', 'ACTIVE', 'https://i.pravatar.cc/150?img=39', 'Asia/Ho_Chi_Minh'),
('30000000-0000-0000-0000-000000000010', 'student10@example.com', '$2b$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5UpKKNr0EjEju', 'Student', 'Ten', '+84912345110', '2000-10-10', 'FEMALE', 'ACTIVE', 'https://i.pravatar.cc/150?img=40', 'Asia/Ho_Chi_Minh'),
('30000000-0000-0000-0000-000000000011', 'student11@example.com', '$2b$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5UpKKNr0EjEju', 'Student', 'Eleven', '+84912345111', '2001-01-11', 'MALE', 'ACTIVE', 'https://i.pravatar.cc/150?img=41', 'Asia/Ho_Chi_Minh'),
('30000000-0000-0000-0000-000000000012', 'student12@example.com', '$2b$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5UpKKNr0EjEju', 'Student', 'Twelve', '+84912345112', '2001-02-12', 'FEMALE', 'ACTIVE', 'https://i.pravatar.cc/150?img=42', 'Asia/Ho_Chi_Minh'),
('30000000-0000-0000-0000-000000000013', 'student13@example.com', '$2b$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5UpKKNr0EjEju', 'Student', 'Thirteen', '+84912345113', '2001-03-13', 'MALE', 'ACTIVE', 'https://i.pravatar.cc/150?img=43', 'Asia/Ho_Chi_Minh'),
('30000000-0000-0000-0000-000000000014', 'student14@example.com', '$2b$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5UpKKNr0EjEju', 'Student', 'Fourteen', '+84912345114', '2001-04-14', 'FEMALE', 'ACTIVE', 'https://i.pravatar.cc/150?img=44', 'Asia/Ho_Chi_Minh'),
('30000000-0000-0000-0000-000000000015', 'student15@example.com', '$2b$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5UpKKNr0EjEju', 'Student', 'Fifteen', '+84912345115', '2001-05-15', 'MALE', 'ACTIVE', 'https://i.pravatar.cc/150?img=45', 'Asia/Ho_Chi_Minh'),
('30000000-0000-0000-0000-000000000016', 'student16@example.com', '$2b$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5UpKKNr0EjEju', 'Student', 'Sixteen', '+84912345116', '2001-06-16', 'FEMALE', 'ACTIVE', 'https://i.pravatar.cc/150?img=46', 'Asia/Ho_Chi_Minh'),
('30000000-0000-0000-0000-000000000017', 'student17@example.com', '$2b$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5UpKKNr0EjEju', 'Student', 'Seventeen', '+84912345117', '2001-07-17', 'MALE', 'ACTIVE', 'https://i.pravatar.cc/150?img=47', 'Asia/Ho_Chi_Minh'),
('30000000-0000-0000-0000-000000000018', 'student18@example.com', '$2b$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5UpKKNr0EjEju', 'Student', 'Eighteen', '+84912345118', '2001-08-18', 'FEMALE', 'ACTIVE', 'https://i.pravatar.cc/150?img=48', 'Asia/Ho_Chi_Minh'),
('30000000-0000-0000-0000-000000000019', 'student19@example.com', '$2b$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5UpKKNr0EjEju', 'Student', 'Nineteen', '+84912345119', '2001-09-19', 'MALE', 'ACTIVE', 'https://i.pravatar.cc/150?img=49', 'Asia/Ho_Chi_Minh'),
('30000000-0000-0000-0000-000000000020', 'student20@example.com', '$2b$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5UpKKNr0EjEju', 'Student', 'Twenty', '+84912345120', '2001-10-20', 'FEMALE', 'ACTIVE', 'https://i.pravatar.cc/150?img=50', 'Asia/Ho_Chi_Minh');

-- ============================================
-- 3. USER ROLES
-- ============================================

-- Admin roles
INSERT INTO "UserRole" (user_id, role_id)
SELECT u.user_id, r.role_id
FROM "User" u
CROSS JOIN "Role" r
WHERE u.email IN ('admin@blelearning.com', 'support@blelearning.com')
  AND r.name = 'ADMIN';

-- Instructor roles
INSERT INTO "UserRole" (user_id, role_id)
SELECT u.user_id, r.role_id
FROM "User" u
CROSS JOIN "Role" r
WHERE u.email LIKE '%@instructor.com'
  AND r.name = 'INSTRUCTOR';

-- TA roles
INSERT INTO "UserRole" (user_id, role_id)
SELECT u.user_id, r.role_id
FROM "User" u
CROSS JOIN "Role" r
WHERE u.email LIKE '%@ta.com'
  AND r.name = 'TA';

-- Student roles
INSERT INTO "UserRole" (user_id, role_id)
SELECT u.user_id, r.role_id
FROM "User" u
CROSS JOIN "Role" r
WHERE u.email LIKE 'student%@example.com'
  AND r.name = 'STUDENT';

-- ============================================
-- 4. COURSES
-- ============================================

INSERT INTO "Course" (course_id, code, title, description, short_description, category, difficulty_level, duration_weeks, status, thumbnail_url, language, prerequisites, learning_outcomes, created_by, published_at) VALUES
('40000000-0000-0000-0000-000000000001', 'WEB101', 'Introduction to Web Development',
'Learn the fundamentals of web development including HTML, CSS, and JavaScript. This course covers responsive design, modern web standards, and best practices for building professional websites.',
'Master HTML, CSS, and JavaScript basics',
'PROGRAMMING', 'BEGINNER', 8, 'PUBLISHED',
'https://images.unsplash.com/photo-1498050108023-c5249f4df085',
'en',
'["Basic computer skills", "Familiarity with internet browsing"]'::JSON,
'["Build responsive websites", "Understand HTML5 and CSS3", "Write JavaScript code", "Use developer tools"]'::JSON,
'10000000-0000-0000-0000-000000000001',
CURRENT_TIMESTAMP - INTERVAL '60 days'),

('40000000-0000-0000-0000-000000000002', 'DS101', 'Data Science Fundamentals',
'Dive into the world of data science with Python. Learn data analysis, visualization, statistics, and machine learning basics. Perfect for beginners interested in data careers.',
'Python-based data science introduction',
'DATA_SCIENCE', 'BEGINNER', 10, 'PUBLISHED',
'https://images.unsplash.com/photo-1551288049-bebda4e38f71',
'en',
'["Basic programming knowledge", "High school mathematics"]'::JSON,
'["Analyze data with pandas", "Create visualizations", "Apply statistical methods", "Build simple ML models"]'::JSON,
'10000000-0000-0000-0000-000000000002',
CURRENT_TIMESTAMP - INTERVAL '45 days'),

('40000000-0000-0000-0000-000000000003', 'DB201', 'Database Design and SQL',
'Master relational database design, SQL queries, and database optimization. Learn to design efficient schemas, write complex queries, and optimize database performance.',
'Complete SQL and database design course',
'DATABASE', 'INTERMEDIATE', 6, 'PUBLISHED',
'https://images.unsplash.com/photo-1544383835-bda2bc66a55d',
'en',
'["Basic programming knowledge", "Understanding of data structures"]'::JSON,
'["Design normalized databases", "Write complex SQL queries", "Optimize query performance", "Implement database security"]'::JSON,
'10000000-0000-0000-0000-000000000003',
CURRENT_TIMESTAMP - INTERVAL '30 days'),

('40000000-0000-0000-0000-000000000004', 'WEB201', 'Advanced React Development',
'Advanced React course covering hooks, context, Redux, performance optimization, testing, and modern React patterns. Build production-ready applications.',
'Master modern React development',
'PROGRAMMING', 'ADVANCED', 12, 'PUBLISHED',
'https://images.unsplash.com/photo-1633356122544-f134324a6cee',
'en',
'["JavaScript ES6+", "Basic React knowledge", "HTML/CSS proficiency"]'::JSON,
'["Build complex React applications", "Implement state management", "Write unit tests", "Optimize React performance"]'::JSON,
'10000000-0000-0000-0000-000000000001',
CURRENT_TIMESTAMP - INTERVAL '15 days'),

('40000000-0000-0000-0000-000000000005', 'ML101', 'Machine Learning with Python',
'Comprehensive machine learning course covering supervised/unsupervised learning, neural networks, and deep learning fundamentals using Python and popular libraries.',
'Complete ML course with hands-on projects',
'DATA_SCIENCE', 'INTERMEDIATE', 14, 'DRAFT',
'https://images.unsplash.com/photo-1555949963-aa79dcee981c',
'en',
'["Python programming", "Statistics basics", "Linear algebra basics"]'::JSON,
'["Implement ML algorithms", "Train neural networks", "Evaluate model performance", "Deploy ML models"]'::JSON,
'10000000-0000-0000-0000-000000000002',
NULL);

-- ============================================
-- 5. MODULES & LECTURES
-- ============================================

-- Course 1: Web Development - Modules
INSERT INTO "Module" (module_id, course_id, title, description, order_index) VALUES
('50000000-0000-0000-0000-000000000101', '40000000-0000-0000-0000-000000000001', 'HTML Fundamentals', 'Learn HTML5 structure and semantic elements', 1),
('50000000-0000-0000-0000-000000000102', '40000000-0000-0000-0000-000000000001', 'CSS Styling', 'Master CSS3 and responsive design', 2),
('50000000-0000-0000-0000-000000000103', '40000000-0000-0000-0000-000000000001', 'JavaScript Basics', 'Introduction to JavaScript programming', 3);

-- Course 1: Web Development - Lectures
INSERT INTO "Lecture" (lecture_id, module_id, title, content, lecture_type, duration_minutes, order_index, video_url) VALUES
('51000000-0000-0000-0000-000000000101', '50000000-0000-0000-0000-000000000101', 'Introduction to HTML', 'HTML structure, tags, and elements', 'VIDEO', 30, 1, 'https://www.youtube.com/watch?v=example1'),
('51000000-0000-0000-0000-000000000102', '50000000-0000-0000-0000-000000000101', 'HTML Forms', 'Creating interactive forms', 'VIDEO', 45, 2, 'https://www.youtube.com/watch?v=example2'),
('51000000-0000-0000-0000-000000000103', '50000000-0000-0000-0000-000000000102', 'CSS Selectors', 'Understanding CSS selectors', 'VIDEO', 35, 1, 'https://www.youtube.com/watch?v=example3'),
('51000000-0000-0000-0000-000000000104', '50000000-0000-0000-0000-000000000102', 'Flexbox Layout', 'Modern layout with Flexbox', 'VIDEO', 50, 2, 'https://www.youtube.com/watch?v=example4'),
('51000000-0000-0000-0000-000000000105', '50000000-0000-0000-0000-000000000103', 'JavaScript Variables', 'Variables and data types', 'VIDEO', 40, 1, 'https://www.youtube.com/watch?v=example5');

-- Course 2: Data Science - Modules
INSERT INTO "Module" (module_id, course_id, title, description, order_index) VALUES
('50000000-0000-0000-0000-000000000201', '40000000-0000-0000-0000-000000000002', 'Python Basics', 'Python fundamentals for data science', 1),
('50000000-0000-0000-0000-000000000202', '40000000-0000-0000-0000-000000000002', 'Data Analysis', 'Working with pandas and numpy', 2);

-- Course 2: Data Science - Lectures
INSERT INTO "Lecture" (lecture_id, module_id, title, content, lecture_type, duration_minutes, order_index, video_url) VALUES
('51000000-0000-0000-0000-000000000201', '50000000-0000-0000-0000-000000000201', 'Python Setup', 'Installing Python and tools', 'VIDEO', 25, 1, 'https://www.youtube.com/watch?v=example6'),
('51000000-0000-0000-0000-000000000202', '50000000-0000-0000-0000-000000000201', 'Python Syntax', 'Basic Python syntax', 'VIDEO', 40, 2, 'https://www.youtube.com/watch?v=example7'),
('51000000-0000-0000-0000-000000000203', '50000000-0000-0000-0000-000000000202', 'Intro to Pandas', 'DataFrames and Series', 'VIDEO', 55, 1, 'https://www.youtube.com/watch?v=example8');

-- ============================================
-- 6. RESOURCES
-- ============================================

INSERT INTO "Resource" (lecture_id, resource_type, title, description, url, file_size) VALUES
('51000000-0000-0000-0000-000000000101', 'PDF', 'HTML Cheat Sheet', 'Quick reference for HTML tags', 'https://example.com/html-cheatsheet.pdf', 524288),
('51000000-0000-0000-0000-000000000102', 'DOCUMENT', 'Form Examples', 'Sample HTML forms', 'https://example.com/forms.html', 102400),
('51000000-0000-0000-0000-000000000103', 'CODE', 'CSS Selector Examples', 'Code examples for selectors', 'https://example.com/selectors.zip', 204800),
('51000000-0000-0000-0000-000000000201', 'VIDEO', 'Python Installation Guide', 'Step-by-step installation', 'https://example.com/python-install.mp4', 10485760);

-- ============================================
-- 7. QUIZZES & QUESTIONS
-- ============================================

-- Quiz 1: HTML Basics
INSERT INTO "Quiz" (quiz_id, course_id, title, description, quiz_type, difficulty_level, total_points, passing_score, time_limit_minutes, max_attempts, shuffle_questions, show_correct_answers, is_published) VALUES
('60000000-0000-0000-0000-000000000001', '40000000-0000-0000-0000-000000000001', 'HTML Fundamentals Quiz', 'Test your knowledge of HTML basics', 'FORMATIVE', 'BEGINNER', 100, 60, 30, 3, TRUE, TRUE, TRUE);

-- Questions for Quiz 1
INSERT INTO "Question" (question_id, course_id, text, type, difficulty_level, points, explanation, is_active) VALUES
('61000000-0000-0000-0000-000000000001', '40000000-0000-0000-0000-000000000001', 'What does HTML stand for?', 'MCQ', 'BEGINNER', 10, 'HTML stands for HyperText Markup Language', TRUE),
('61000000-0000-0000-0000-000000000002', '40000000-0000-0000-0000-000000000001', 'Which tag is used for the largest heading?', 'MCQ', 'BEGINNER', 10, 'The <h1> tag represents the largest heading', TRUE),
('61000000-0000-0000-0000-000000000003', '40000000-0000-0000-0000-000000000001', 'Is HTML case-sensitive?', 'TRUE_FALSE', 'BEGINNER', 10, 'HTML is not case-sensitive, but lowercase is recommended', TRUE);

-- Options for Question 1
INSERT INTO "Option" (question_id, option_text, is_correct, order_index) VALUES
('61000000-0000-0000-0000-000000000001', 'HyperText Markup Language', TRUE, 1),
('61000000-0000-0000-0000-000000000001', 'High Tech Modern Language', FALSE, 2),
('61000000-0000-0000-0000-000000000001', 'Home Tool Markup Language', FALSE, 3),
('61000000-0000-0000-0000-000000000001', 'Hyperlinks and Text Markup Language', FALSE, 4);

-- Options for Question 2
INSERT INTO "Option" (question_id, option_text, is_correct, order_index) VALUES
('61000000-0000-0000-0000-000000000002', '<h1>', TRUE, 1),
('61000000-0000-0000-0000-000000000002', '<h6>', FALSE, 2),
('61000000-0000-0000-0000-000000000002', '<heading>', FALSE, 3),
('61000000-0000-0000-0000-000000000002', '<head>', FALSE, 4);

-- Options for Question 3
INSERT INTO "Option" (question_id, option_text, is_correct, order_index) VALUES
('61000000-0000-0000-0000-000000000003', 'False', TRUE, 1),
('61000000-0000-0000-0000-000000000003', 'True', FALSE, 2);

-- Link questions to quiz
INSERT INTO "QuizQuestion" (quiz_id, question_id, points, order_index) VALUES
('60000000-0000-0000-0000-000000000001', '61000000-0000-0000-0000-000000000001', 10, 1),
('60000000-0000-0000-0000-000000000001', '61000000-0000-0000-0000-000000000002', 10, 2),
('60000000-0000-0000-0000-000000000001', '61000000-0000-0000-0000-000000000003', 10, 3);

-- Quiz 2: Data Science Basics
INSERT INTO "Quiz" (quiz_id, course_id, title, description, quiz_type, difficulty_level, total_points, passing_score, time_limit_minutes, max_attempts, is_published) VALUES
('60000000-0000-0000-0000-000000000002', '40000000-0000-0000-0000-000000000002', 'Python Basics Quiz', 'Test your Python knowledge', 'FORMATIVE', 'BEGINNER', 50, 30, 20, 5, TRUE);

-- ============================================
-- 8. ASSIGNMENTS
-- ============================================

INSERT INTO "Assignment" (assignment_id, course_id, title, description, assignment_type, max_points, due_date, late_submission_allowed, rubric) VALUES
('70000000-0000-0000-0000-000000000001', '40000000-0000-0000-0000-000000000001', 'Build a Personal Website',
'Create a personal portfolio website using HTML and CSS. Include home, about, and contact pages.',
'PROJECT', 100, CURRENT_TIMESTAMP + INTERVAL '14 days', TRUE,
'{"criteria": [
  {"name": "HTML Structure", "points": 30, "description": "Proper HTML5 structure and semantic tags"},
  {"name": "CSS Styling", "points": 30, "description": "Professional styling and layout"},
  {"name": "Responsiveness", "points": 20, "description": "Mobile-friendly design"},
  {"name": "Code Quality", "points": 20, "description": "Clean, well-commented code"}
]}'::JSON),

('70000000-0000-0000-0000-000000000002', '40000000-0000-0000-0000-000000000002', 'Data Analysis Project',
'Analyze a provided dataset and create visualizations. Submit a Jupyter notebook with your analysis.',
'CODE', 100, CURRENT_TIMESTAMP + INTERVAL '21 days', TRUE,
'{"criteria": [
  {"name": "Data Cleaning", "points": 25, "description": "Proper data preprocessing"},
  {"name": "Analysis", "points": 35, "description": "Insightful data analysis"},
  {"name": "Visualizations", "points": 25, "description": "Clear and informative charts"},
  {"name": "Documentation", "points": 15, "description": "Well-documented code and findings"}
]}'::JSON);

-- ============================================
-- 9. COURSE ENROLLMENTS
-- ============================================

-- Enroll students 1-10 in Course 1 (Web Development)
INSERT INTO "CourseEnrollment" (user_id, course_id, role_in_course, enrollment_status, enrolled_at, completion_percentage)
SELECT
  u.user_id,
  '40000000-0000-0000-0000-000000000001',
  'STUDENT',
  CASE
    WHEN u.email IN ('student01@example.com', 'student02@example.com') THEN 'COMPLETED'
    WHEN u.email IN ('student03@example.com', 'student04@example.com', 'student05@example.com') THEN 'ACTIVE'
    ELSE 'ACTIVE'
  END,
  CURRENT_TIMESTAMP - INTERVAL '30 days',
  CASE
    WHEN u.email IN ('student01@example.com', 'student02@example.com') THEN 100
    WHEN u.email IN ('student03@example.com') THEN 75
    WHEN u.email IN ('student04@example.com') THEN 60
    WHEN u.email IN ('student05@example.com') THEN 45
    ELSE 30
  END
FROM "User" u
WHERE u.email IN (
  'student01@example.com', 'student02@example.com', 'student03@example.com',
  'student04@example.com', 'student05@example.com', 'student06@example.com',
  'student07@example.com', 'student08@example.com', 'student09@example.com',
  'student10@example.com'
);

-- Enroll students 5-15 in Course 2 (Data Science)
INSERT INTO "CourseEnrollment" (user_id, course_id, role_in_course, enrollment_status, enrolled_at, completion_percentage)
SELECT
  u.user_id,
  '40000000-0000-0000-0000-000000000002',
  'STUDENT',
  'ACTIVE',
  CURRENT_TIMESTAMP - INTERVAL '20 days',
  CASE
    WHEN u.email IN ('student05@example.com') THEN 80
    WHEN u.email IN ('student06@example.com') THEN 65
    ELSE 40
  END
FROM "User" u
WHERE u.email LIKE 'student%@example.com'
  AND u.email >= 'student05@example.com'
  AND u.email <= 'student15@example.com';

-- Enroll students 10-20 in Course 3 (Database)
INSERT INTO "CourseEnrollment" (user_id, course_id, role_in_course, enrollment_status, enrolled_at, completion_percentage)
SELECT
  u.user_id,
  '40000000-0000-0000-0000-000000000003',
  'STUDENT',
  'ACTIVE',
  CURRENT_TIMESTAMP - INTERVAL '10 days',
  25
FROM "User" u
WHERE u.email LIKE 'student%@example.com'
  AND u.email >= 'student10@example.com'
  AND u.email <= 'student20@example.com';

-- ============================================
-- 10. PROGRESS TRACKING
-- ============================================

-- Progress for Student 1 (100% complete in Course 1)
INSERT INTO "Progress" (user_id, course_id, module_id, lecture_id, status, percent_complete, last_accessed_at)
SELECT
  '30000000-0000-0000-0000-000000000001',
  '40000000-0000-0000-0000-000000000001',
  l.module_id,
  l.lecture_id,
  'COMPLETED',
  100,
  CURRENT_TIMESTAMP - INTERVAL '5 days'
FROM "Lecture" l
JOIN "Module" m ON l.module_id = m.module_id
WHERE m.course_id = '40000000-0000-0000-0000-000000000001';

-- Progress for Student 3 (75% complete in Course 1)
INSERT INTO "Progress" (user_id, course_id, module_id, lecture_id, status, percent_complete, last_accessed_at)
SELECT
  '30000000-0000-0000-0000-000000000003',
  '40000000-0000-0000-0000-000000000001',
  l.module_id,
  l.lecture_id,
  CASE WHEN l.order_index <= 2 THEN 'COMPLETED' ELSE 'IN_PROGRESS' END,
  CASE WHEN l.order_index <= 2 THEN 100 ELSE 50 END,
  CURRENT_TIMESTAMP - INTERVAL '2 days'
FROM "Lecture" l
JOIN "Module" m ON l.module_id = m.module_id
WHERE m.course_id = '40000000-0000-0000-0000-000000000001'
LIMIT 4;

-- ============================================
-- 11. QUIZ ATTEMPTS & SUBMISSIONS
-- ============================================

-- Student 1 takes HTML quiz (perfect score)
INSERT INTO "Attempt" (quiz_id, user_id, course_enrollment_id, attempt_number, status, started_at, ended_at, max_possible_score, auto_score, final_score, percentage_score, graded_at)
SELECT
  '60000000-0000-0000-0000-000000000001',
  '30000000-0000-0000-0000-000000000001',
  ce.course_enrollment_id,
  1,
  'GRADED',
  CURRENT_TIMESTAMP - INTERVAL '10 days',
  CURRENT_TIMESTAMP - INTERVAL '10 days' + INTERVAL '15 minutes',
  30,
  30,
  30,
  100,
  CURRENT_TIMESTAMP - INTERVAL '10 days' + INTERVAL '15 minutes'
FROM "CourseEnrollment" ce
WHERE ce.user_id = '30000000-0000-0000-0000-000000000001'
  AND ce.course_id = '40000000-0000-0000-0000-000000000001';

-- ============================================
-- 12. CERTIFICATE TEMPLATES
-- ============================================

INSERT INTO "CertificateTemplate" (name, description, template_design, border_style, signature_fields, is_default, is_active) VALUES
('Standard Certificate', 'Default certificate template for course completion',
'{"backgroundColor": "#FFFFFF", "primaryColor": "#1e40af", "secondaryColor": "#3b82f6", "fontFamily": "Georgia"}',
'{"style": "ornate", "color": "#1e40af", "width": 5}',
'{"signatories": [
  {"name": "Course Instructor", "title": "Instructor", "position": "left"},
  {"name": "Academic Director", "title": "Director", "position": "right"}
]}'::JSON,
TRUE, TRUE);

-- ============================================
-- 13. CERTIFICATES (auto-issued by trigger)
-- ============================================

-- Update completed enrollments to trigger certificate issuance
UPDATE "CourseEnrollment"
SET
  enrollment_status = 'COMPLETED',
  completion_percentage = 100,
  completed_at = CURRENT_TIMESTAMP - INTERVAL '3 days',
  final_grade = 95.5
WHERE user_id IN ('30000000-0000-0000-0000-000000000001', '30000000-0000-0000-0000-000000000002')
  AND course_id = '40000000-0000-0000-0000-000000000001';

-- ============================================
-- 14. CLASSES (Blended Learning)
-- ============================================

INSERT INTO "Class" (class_id, course_id, class_name, class_code, class_type, instructor_id, start_date, end_date, max_students, enrolled_students, status, location, meeting_link) VALUES
('80000000-0000-0000-0000-000000000001', '40000000-0000-0000-0000-000000000001', 'Web Development - Spring 2025', 'WEB101-S25-01', 'BLENDED',
  '10000000-0000-0000-0000-000000000001',
  CURRENT_DATE,
  CURRENT_DATE + INTERVAL '56 days',
  30, 10, 'IN_PROGRESS',
  'Room 101, Building A',
  'https://meet.google.com/abc-defg-hij');

-- ============================================
-- 15. CLASS ENROLLMENTS
-- ============================================

INSERT INTO "ClassEnrollment" (user_id, class_id, enrollment_status, enrolled_at)
SELECT
  u.user_id,
  '80000000-0000-0000-0000-000000000001',
  'ACTIVE',
  CURRENT_TIMESTAMP - INTERVAL '25 days'
FROM "User" u
WHERE u.email IN (
  'student01@example.com', 'student02@example.com', 'student03@example.com',
  'student04@example.com', 'student05@example.com', 'student06@example.com',
  'student07@example.com', 'student08@example.com', 'student09@example.com',
  'student10@example.com'
);

-- ============================================
-- 16. SCHEDULES
-- ============================================

INSERT INTO "Schedule" (class_id, session_date, start_time, end_time, session_type, topic, location) VALUES
('80000000-0000-0000-0000-000000000001', CURRENT_DATE - INTERVAL '7 days', '09:00:00', '11:00:00', 'IN_PERSON', 'HTML Fundamentals Workshop', 'Room 101, Building A'),
('80000000-0000-0000-0000-000000000001', CURRENT_DATE - INTERVAL '5 days', '09:00:00', '11:00:00', 'IN_PERSON', 'CSS Flexbox Practice', 'Room 101, Building A'),
('80000000-0000-0000-0000-000000000001', CURRENT_DATE + INTERVAL '2 days', '09:00:00', '11:00:00', 'ONLINE', 'JavaScript Basics', NULL),
('80000000-0000-0000-0000-000000000001', CURRENT_DATE + INTERVAL '7 days', '09:00:00', '11:00:00', 'IN_PERSON', 'Project Workshop', 'Room 101, Building A');

-- ============================================
-- 17. NOTIFICATIONS
-- ============================================

INSERT INTO "Notification" (user_id, notification_type, title, message, related_entity_type, related_entity_id, priority, is_read)
SELECT
  u.user_id,
  'COURSE_ENROLLMENT',
  'Welcome to Web Development!',
  'You have successfully enrolled in the Web Development course. Start learning now!',
  'Course',
  '40000000-0000-0000-0000-000000000001',
  'MEDIUM',
  TRUE
FROM "User" u
WHERE u.email IN ('student01@example.com', 'student02@example.com', 'student03@example.com');

-- ============================================
-- 18. NOTIFICATION PREFERENCES
-- ============================================

INSERT INTO "NotificationPreference" (user_id, channel, notification_type, is_enabled)
SELECT
  u.user_id,
  channel,
  notif_type,
  TRUE
FROM "User" u
CROSS JOIN (VALUES ('EMAIL'), ('PUSH'), ('SMS')) AS channels(channel)
CROSS JOIN (VALUES
  ('COURSE_ENROLLMENT'),
  ('ASSIGNMENT_DUE'),
  ('QUIZ_AVAILABLE'),
  ('GRADE_POSTED'),
  ('CERTIFICATE_ISSUED')
) AS types(notif_type)
WHERE u.email LIKE 'student%@example.com'
LIMIT 100;

-- ============================================
-- 19. SYSTEM SETTINGS
-- ============================================

INSERT INTO "SystemSettings" (setting_key, setting_value, data_type, description) VALUES
('site_name', '"B-Learning Platform"', 'STRING', 'Platform name'),
('site_url', '"https://blelearning.com"', 'STRING', 'Platform URL'),
('support_email', '"support@blelearning.com"', 'STRING', 'Support email address'),
('max_file_size_mb', '100', 'NUMBER', 'Maximum file upload size in MB'),
('session_timeout_minutes', '60', 'NUMBER', 'Session timeout duration'),
('enable_notifications', 'true', 'BOOLEAN', 'Enable notification system'),
('enable_certificates', 'true', 'BOOLEAN', 'Enable certificate issuance'),
('maintenance_mode', 'false', 'BOOLEAN', 'System maintenance mode'),
('default_language', '"en"', 'STRING', 'Default platform language'),
('default_timezone', '"Asia/Ho_Chi_Minh"', 'STRING', 'Default timezone');

-- ============================================
-- 20. ACTIVITY LOGS (Sample)
-- ============================================

INSERT INTO "ActivityLog" (user_id, action, entity_type, entity_id, ip_address, user_agent, log_date)
SELECT
  u.user_id,
  'LOGIN',
  'User',
  u.user_id,
  '192.168.1.' || (RANDOM() * 255)::INT,
  'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36',
  CURRENT_DATE
FROM "User" u
WHERE u.email LIKE 'student%@example.com'
LIMIT 20;

COMMIT;

-- ============================================
-- SEED DATA COMPLETE
-- Total records: ~200+
-- - 27 Users (2 admins, 3 instructors, 2 TAs, 20 students)
-- - 5 Courses (4 published, 1 draft)
-- - 5 Modules, 8 Lectures, 4 Resources
-- - 2 Quizzes, 3 Questions, 10 Options
-- - 2 Assignments
-- - 28 Enrollments
-- - Multiple progress records
-- - 1 Class, 10 class enrollments, 4 schedules
-- - Notifications and preferences
-- - System settings
-- ============================================

-- ============================================
-- POST-SEED VERIFICATION QUERIES
-- ============================================

/*
-- Verify user counts by role
SELECT r.name, COUNT(ur.user_id) as user_count
FROM "Role" r
LEFT JOIN "UserRole" ur ON r.role_id = ur.role_id
GROUP BY r.name
ORDER BY r.name;

-- Verify course statistics
SELECT
  c.code,
  c.title,
  c.status,
  COUNT(DISTINCT ce.user_id) as enrolled_students,
  COUNT(DISTINCT m.module_id) as modules,
  COUNT(DISTINCT l.lecture_id) as lectures
FROM "Course" c
LEFT JOIN "CourseEnrollment" ce ON c.course_id = ce.course_id
LEFT JOIN "Module" m ON c.course_id = m.course_id
LEFT JOIN "Lecture" l ON m.module_id = l.module_id
GROUP BY c.course_id, c.code, c.title, c.status
ORDER BY c.code;

-- Verify enrollments
SELECT
  enrollment_status,
  COUNT(*) as count
FROM "CourseEnrollment"
GROUP BY enrollment_status;

-- Verify certificates issued
SELECT COUNT(*) as certificates_issued
FROM "Certificate";

-- Refresh materialized views
SELECT refresh_all_materialized_views();
*/
