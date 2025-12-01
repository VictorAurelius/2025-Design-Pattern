-- ============================================
-- 04-seed-data.sql - Sample Data for Testing
-- B-Learning Core v1.0
-- Author: Nguyen Van Kiet - CNTT1-K63
-- Created: 2025-11-27
-- Fixed: UTF-8 encoding, schema alignment, foreign keys
-- ============================================

-- Set client encoding to UTF-8
SET CLIENT_ENCODING = 'UTF8';

-- Purpose:
-- 1. Comprehensive seed data for testing all features
-- 2. Realistic Vietnamese content for demo
-- 3. Test all FK relationships and constraints
-- 4. Enable immediate frontend/backend testing

-- Note: All UUIDs are hardcoded for reproducibility
-- Password for all users: 'password123' (bcrypt hash)

-- ============================================
-- SECTION 1: USER MANAGEMENT
-- ============================================

-- --------------------------------------------
-- 1.1. Roles (4 roles)
-- --------------------------------------------

INSERT INTO "Role" (role_id, name, description, permissions, created_at) VALUES
(
  '10000000-0000-0000-0000-000000000001',
  'ADMIN',
  'Quan tri vien he thong - co toan quyen quan ly',
  '["user.manage", "course.manage", "system.config", "report.view"]'::json,
  CURRENT_TIMESTAMP
),
(
  '10000000-0000-0000-0000-000000000002',
  'INSTRUCTOR',
  'Giang vien - tao va quan ly khoa hoc, cham bai',
  '["course.create", "course.edit", "grade.manage", "class.manage"]'::json,
  CURRENT_TIMESTAMP
),
(
  '10000000-0000-0000-0000-000000000003',
  'TA',
  'Tro giang - ho tro cham bai va quan ly lop',
  '["grade.manage", "discussion.moderate", "class.assist"]'::json,
  CURRENT_TIMESTAMP
),
(
  '10000000-0000-0000-0000-000000000004',
  'STUDENT',
  'Hoc vien - dang ky khoa hoc, hoc bai, lam quiz va assignment',
  '["course.enroll", "quiz.take", "assignment.submit", "progress.view"]'::json,
  CURRENT_TIMESTAMP
);

-- --------------------------------------------
-- 1.2. Users (1 admin, 2 instructors, 5 students)
-- --------------------------------------------

-- Admin user
INSERT INTO "User" (user_id, email, password_hash, first_name, last_name, account_status, preferences, email_verified_at, created_at, updated_at) VALUES
(
  '20000000-0000-0000-0000-000000000001',
  'admin@blearning.edu.vn',
  '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', -- password123
  'Admin',
  'He Thong',
  'ACTIVE',
  '{"notifications": {"email": true, "push": true}, "locale": "vi", "timezone": "Asia/Ho_Chi_Minh", "theme": "dark"}'::json,
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP
);

-- Instructors
INSERT INTO "User" (user_id, email, password_hash, first_name, last_name, account_status, preferences, email_verified_at, created_at, updated_at) VALUES
(
  '20000000-0000-0000-0000-000000000002',
  'kiet.nguyen@blearning.edu.vn',
  '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
  'Kiet',
  'Nguyen Van',
  'ACTIVE',
  '{"locale": "vi", "timezone": "Asia/Ho_Chi_Minh"}'::json,
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP
),
(
  '20000000-0000-0000-0000-000000000003',
  'linh.tran@blearning.edu.vn',
  '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
  'Linh',
  'Tran Thi',
  'ACTIVE',
  '{"locale": "vi", "timezone": "Asia/Ho_Chi_Minh"}'::json,
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP
);

-- Students
INSERT INTO "User" (user_id, email, password_hash, first_name, last_name, account_status, preferences, email_verified_at, created_at, updated_at) VALUES
(
  '20000000-0000-0000-0000-000000000101',
  'minh.le@student.blearning.edu.vn',
  '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
  'Minh',
  'Le Quang',
  'ACTIVE',
  '{"notifications": {"assignment_due": {"email": true, "push": false}, "grade_published": {"email": true, "push": true}}, "locale": "vi", "timezone": "Asia/Ho_Chi_Minh"}'::json,
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP
),
(
  '20000000-0000-0000-0000-000000000102',
  'huong.pham@student.blearning.edu.vn',
  '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
  'Huong',
  'Pham Thi',
  'ACTIVE',
  '{"locale": "vi", "timezone": "Asia/Ho_Chi_Minh"}'::json,
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP
),
(
  '20000000-0000-0000-0000-000000000103',
  'tuan.vo@student.blearning.edu.vn',
  '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
  'Tuan',
  'Vo Van',
  'ACTIVE',
  '{"locale": "vi", "timezone": "Asia/Ho_Chi_Minh"}'::json,
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP
),
(
  '20000000-0000-0000-0000-000000000104',
  'hoa.nguyen@student.blearning.edu.vn',
  '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
  'Hoa',
  'Nguyen Thi',
  'ACTIVE',
  '{"locale": "vi", "timezone": "Asia/Ho_Chi_Minh"}'::json,
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP
),
(
  '20000000-0000-0000-0000-000000000105',
  'dung.tran@student.blearning.edu.vn',
  '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
  'Dung',
  'Tran Van',
  'ACTIVE',
  '{"locale": "vi", "timezone": "Asia/Ho_Chi_Minh"}'::json,
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP
);

-- --------------------------------------------
-- 1.3. UserRole Assignments
-- --------------------------------------------

INSERT INTO "UserRole" (user_role_id, user_id, role_id, granted_at) VALUES
-- Admin
('30000000-0000-0000-0000-000000000001', '20000000-0000-0000-0000-000000000001', '10000000-0000-0000-0000-000000000001', CURRENT_TIMESTAMP),

-- Instructors
('30000000-0000-0000-0000-000000000002', '20000000-0000-0000-0000-000000000002', '10000000-0000-0000-0000-000000000002', CURRENT_TIMESTAMP),
('30000000-0000-0000-0000-000000000003', '20000000-0000-0000-0000-000000000003', '10000000-0000-0000-0000-000000000002', CURRENT_TIMESTAMP),

-- Students
('30000000-0000-0000-0000-000000000101', '20000000-0000-0000-0000-000000000101', '10000000-0000-0000-0000-000000000004', CURRENT_TIMESTAMP),
('30000000-0000-0000-0000-000000000102', '20000000-0000-0000-0000-000000000102', '10000000-0000-0000-0000-000000000004', CURRENT_TIMESTAMP),
('30000000-0000-0000-0000-000000000103', '20000000-0000-0000-0000-000000000103', '10000000-0000-0000-0000-000000000004', CURRENT_TIMESTAMP),
('30000000-0000-0000-0000-000000000104', '20000000-0000-0000-0000-000000000104', '10000000-0000-0000-0000-000000000004', CURRENT_TIMESTAMP),
('30000000-0000-0000-0000-000000000105', '20000000-0000-0000-0000-000000000105', '10000000-0000-0000-0000-000000000004', CURRENT_TIMESTAMP);

-- ============================================
-- SECTION 2: COURSE CONTENT
-- ============================================

-- --------------------------------------------
-- 2.1. Courses (10 courses for comprehensive demo)
-- --------------------------------------------

INSERT INTO "Course" (course_id, code, title, description, short_description, difficulty_level, estimated_hours, status, created_by, published_at, created_at, updated_at) VALUES
(
  '40000000-0000-0000-0000-000000000001',
  'JAVA101',
  'Lap Trinh Java Co Ban',
  'Khoa hoc gioi thieu ve lap trinh huong doi tuong voi Java. Hoc vien se nam vung cu phap Java, OOP principles, va Design Patterns co ban.',
  'Hoc Java tu co ban den nang cao voi 23 design patterns',
  'BEGINNER',
  40.00,
  'PUBLISHED',
  '20000000-0000-0000-0000-000000000002',
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP
),
(
  '40000000-0000-0000-0000-000000000002',
  'WEB201',
  'Phat Trien Web Frontend',
  'Khoa hoc ve phat trien web frontend voi HTML, CSS, JavaScript va React. Bao gom responsive design, state management, va best practices.',
  'Thanh thao HTML, CSS, JavaScript va React',
  'INTERMEDIATE',
  50.00,
  'PUBLISHED',
  '20000000-0000-0000-0000-000000000002',
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP
),
(
  '40000000-0000-0000-0000-000000000003',
  'DB301',
  'Co So Du Lieu Nang Cao',
  'PostgreSQL, MongoDB, thiet ke database, normalization, indexing va query optimization. Bao gom ca NoSQL va best practices.',
  'Database design, SQL, NoSQL va performance tuning',
  'ADVANCED',
  45.00,
  'PUBLISHED',
  '20000000-0000-0000-0000-000000000003',
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP
);

-- --------------------------------------------
-- 2.2. Modules (6 modules - 2 per course)
-- --------------------------------------------

INSERT INTO "Module" (module_id, course_id, title, description, order_num, estimated_duration_minutes, prerequisite_module_ids, created_at, updated_at) VALUES
-- JAVA101 modules
('50000000-0000-0000-0000-000000000001', '40000000-0000-0000-0000-000000000001', 'Co Ban Java', 'Cu phap, bien, vong lap, dieu kien', 1, 180, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('50000000-0000-0000-0000-000000000002', '40000000-0000-0000-0000-000000000001', 'Lap Trinh Huong Doi Tuong', 'Class, Object, Inheritance, Polymorphism', 2, 240, '{"50000000-0000-0000-0000-000000000001"}'::uuid[], CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- WEB201 modules
('50000000-0000-0000-0000-000000000003', '40000000-0000-0000-0000-000000000002', 'HTML & CSS Co Ban', 'Cau truc HTML va styling voi CSS', 1, 200, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('50000000-0000-0000-0000-000000000004', '40000000-0000-0000-0000-000000000002', 'JavaScript & React', 'JS ES6 va React components', 2, 280, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- DB301 modules
('50000000-0000-0000-0000-000000000005', '40000000-0000-0000-0000-000000000003', 'SQL Nang Cao', 'Complex queries, indexing, optimization', 1, 220, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('50000000-0000-0000-0000-000000000006', '40000000-0000-0000-0000-000000000003', 'NoSQL & MongoDB', 'Document databases va aggregation', 2, 200, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- --------------------------------------------
-- 2.3. Lectures (6 lectures including assignments)
-- --------------------------------------------

INSERT INTO "Lecture" (lecture_id, module_id, title, description, type, order_num, duration_seconds, assignment_config, created_at, updated_at) VALUES
-- JAVA101 lectures
('60000000-0000-0000-0000-000000000001', '50000000-0000-0000-0000-000000000001', 'Gioi Thieu Java va JDK', 'Lich su Java, cai dat JDK, IDE setup', 'VIDEO', 1, 2700, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('60000000-0000-0000-0000-000000000002', '50000000-0000-0000-0000-000000000001', 'Bai Tap: Hello World', 'Viet chuong trinh Java dau tien', 'ASSIGNMENT', 2, NULL, '{"max_points": 100, "due_date": "2025-12-31T23:59:00Z", "submission_types": ["file", "code"], "instructions": "Viet chuong trinh Hello World bang Java"}'::json, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- WEB201 lectures
('60000000-0000-0000-0000-000000000003', '50000000-0000-0000-0000-000000000003', 'HTML Co Ban', 'Cau truc HTML va semantic tags', 'VIDEO', 1, 3000, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('60000000-0000-0000-0000-000000000004', '50000000-0000-0000-0000-000000000003', 'Bai Tap: Personal Website', 'Tao website ca nhan voi HTML/CSS', 'ASSIGNMENT', 2, NULL, '{"max_points": 120, "due_date": "2025-12-31T23:59:00Z", "submission_types": ["file"], "instructions": "Tao website ca nhan responsive voi HTML5 va CSS3"}'::json, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- DB301 lectures
('60000000-0000-0000-0000-000000000005', '50000000-0000-0000-0000-000000000005', 'SQL Advanced Queries', 'Complex joins, subqueries, CTEs', 'VIDEO', 1, 3600, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('60000000-0000-0000-0000-000000000006', '50000000-0000-0000-0000-000000000005', 'Bai Tap: Database Design', 'Thiet ke ERD cho he thong ban hang', 'ASSIGNMENT', 2, NULL, '{"max_points": 140, "due_date": "2025-12-31T23:59:00Z", "submission_types": ["file"], "instructions": "Thiet ke ERD va implement database cho he thong e-commerce"}'::json, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- --------------------------------------------
-- 2.4. Resources (2 sample resources)
-- --------------------------------------------

INSERT INTO "Resource" (resource_id, lecture_id, title, file_url, file_type, file_size_bytes, created_at) VALUES
('70000000-0000-0000-0000-000000000001', '60000000-0000-0000-0000-000000000001', 'Java Installation Guide', 'https://s3.amazonaws.com/blearning/docs/java-install.pdf', 'application/pdf', 2458624, CURRENT_TIMESTAMP),
('70000000-0000-0000-0000-000000000002', '60000000-0000-0000-0000-000000000003', 'HTML5 Reference', 'https://s3.amazonaws.com/blearning/docs/html5-ref.pdf', 'application/pdf', 1835008, CURRENT_TIMESTAMP);

-- ============================================
-- SECTION 3: ASSESSMENT
-- ============================================

-- --------------------------------------------
-- 3.1. Questions (4 questions for quiz)
-- --------------------------------------------

INSERT INTO "Question" (question_id, course_id, text, type, difficulty, max_points, explanation, is_active, created_by, created_at, updated_at) VALUES
(
  '80000000-0000-0000-0000-000000000001',
  '40000000-0000-0000-0000-000000000001',
  'Java la ngon ngu lap trinh thuoc loai nao?',
  'MCQ',
  'EASY',
  10,
  'Java la ngon ngu lap trinh huong doi tuong (OOP) pho bien nhat the gioi',
  true,
  '20000000-0000-0000-0000-000000000002',
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP
),
(
  '80000000-0000-0000-0000-000000000002',
  '40000000-0000-0000-0000-000000000001',
  'Keyword nao dung de ke thua class trong Java?',
  'MCQ',
  'EASY',
  10,
  'Keyword "extends" duoc su dung de ke thua class trong Java',
  true,
  '20000000-0000-0000-0000-000000000002',
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP
),
(
  '80000000-0000-0000-0000-000000000003',
  '40000000-0000-0000-0000-000000000001',
  'Java ho tro da ke thua (multiple inheritance) qua class?',
  'TRUE_FALSE',
  'MEDIUM',
  5,
  'Java khong ho tro da ke thua qua class de tranh Diamond Problem. Thay vao do dung interface',
  true,
  '20000000-0000-0000-0000-000000000002',
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP
),
(
  '80000000-0000-0000-0000-000000000004',
  '40000000-0000-0000-0000-000000000002',
  'HTML la viet tat cua gi?',
  'SHORT_ANSWER',
  'EASY',
  5,
  'HyperText Markup Language',
  true,
  '20000000-0000-0000-0000-000000000002',
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP
);

-- --------------------------------------------
-- 3.2. Options (for MCQ and TRUE_FALSE questions)
-- --------------------------------------------

-- Question 1 options
INSERT INTO "Option" (option_id, question_id, option_text, is_correct, order_num, feedback) VALUES
('90000000-0000-0000-0000-000000000001', '80000000-0000-0000-0000-000000000001', 'Huong doi tuong (OOP)', true, 1, 'Chinh xac! Java la ngon ngu OOP'),
('90000000-0000-0000-0000-000000000002', '80000000-0000-0000-0000-000000000001', 'Thu tuc (Procedural)', false, 2, 'Sai. Java khong phai procedural language'),
('90000000-0000-0000-0000-000000000003', '80000000-0000-0000-0000-000000000001', 'Ham (Functional)', false, 3, 'Java co ho tro functional programming tu Java 8 nhung khong phai functional language'),
('90000000-0000-0000-0000-000000000004', '80000000-0000-0000-0000-000000000001', 'Logic', false, 4, 'Sai. Prolog moi la logic programming language');

-- Question 2 options
INSERT INTO "Option" (option_id, question_id, option_text, is_correct, order_num, feedback) VALUES
('90000000-0000-0000-0000-000000000011', '80000000-0000-0000-0000-000000000002', 'extends', true, 1, 'Dung! extends dung de ke thua class'),
('90000000-0000-0000-0000-000000000012', '80000000-0000-0000-0000-000000000002', 'implements', false, 2, 'implements dung cho interface, khong phai class'),
('90000000-0000-0000-0000-000000000013', '80000000-0000-0000-0000-000000000002', 'inherits', false, 3, 'Khong co keyword nay trong Java'),
('90000000-0000-0000-0000-000000000014', '80000000-0000-0000-0000-000000000002', 'super', false, 4, 'super dung de goi parent class, khong phai keyword ke thua');

-- Question 3 options (TRUE_FALSE)
INSERT INTO "Option" (option_id, question_id, option_text, is_correct, order_num, feedback) VALUES
('90000000-0000-0000-0000-000000000021', '80000000-0000-0000-0000-000000000003', 'True', false, 1, 'Sai. Java khong ho tro multiple inheritance qua class'),
('90000000-0000-0000-0000-000000000022', '80000000-0000-0000-0000-000000000003', 'False', true, 2, 'Dung! Java dung interface thay vi multiple inheritance');

-- --------------------------------------------
-- 3.3. Quizzes (1 quiz)
-- --------------------------------------------

INSERT INTO "Quiz" (quiz_id, course_id, title, description, time_limit_minutes, pass_score, questions, shuffle_questions, show_correct_answers, is_published, created_by, created_at, updated_at) VALUES
(
  'a0000000-0000-0000-0000-000000000001',
  '40000000-0000-0000-0000-000000000001',
  'Kiem tra giua ky - Java Basics',
  'Bai kiem tra kien thuc co ban ve Java va OOP. Thoi gian: 30 phut. Diem dat: 70%',
  30,
  70.00,
  '[
    {"question_id": "80000000-0000-0000-0000-000000000001", "points": 10, "order": 1},
    {"question_id": "80000000-0000-0000-0000-000000000002", "points": 10, "order": 2},
    {"question_id": "80000000-0000-0000-0000-000000000003", "points": 5, "order": 3}
  ]'::json,
  false,
  true,
  true,
  '20000000-0000-0000-0000-000000000002',
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP
);

-- ============================================
-- SECTION 4: CLASS & ENROLLMENT
-- ============================================

-- --------------------------------------------
-- 4.1. Classes (1 class)
-- --------------------------------------------

INSERT INTO "Class" (class_id, course_id, instructor_id, name, start_date, end_date, status, max_students, location, schedules, created_at, updated_at) VALUES
(
  'b0000000-0000-0000-0000-000000000001',
  '40000000-0000-0000-0000-000000000001',
  '20000000-0000-0000-0000-000000000002',
  'CS101-K63-01',
  '2025-01-10',
  '2025-05-15',
  'ONGOING',
  30,
  'Phong A101',
  '[
    {
      "session_id": "session-001",
      "date": "2025-01-10",
      "start_time": "09:00",
      "end_time": "11:00",
      "topic": "Gioi thieu khoa hoc va Java",
      "location": "Phong A101",
      "type": "IN_PERSON",
      "attendances": [
        {"user_id": "20000000-0000-0000-0000-000000000101", "status": "PRESENT", "check_in": "09:05"},
        {"user_id": "20000000-0000-0000-0000-000000000102", "status": "PRESENT", "check_in": "08:58"},
        {"user_id": "20000000-0000-0000-0000-000000000103", "status": "LATE", "check_in": "09:15"}
      ]
    }
  ]'::json,
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP
);

-- --------------------------------------------
-- 4.2. Enrollments (3 enrollments)
-- Note: Enrollment table does NOT have created_at column (schema mismatch fix)
-- --------------------------------------------

-- Class-based enrollments
INSERT INTO "Enrollment" (enrollment_id, user_id, course_id, class_id, role, status, enrolled_at) VALUES
('c0000000-0000-0000-0000-000000000001', '20000000-0000-0000-0000-000000000101', '40000000-0000-0000-0000-000000000001', 'b0000000-0000-0000-0000-000000000001', 'STUDENT', 'ACTIVE', CURRENT_TIMESTAMP - INTERVAL '30 days'),
('c0000000-0000-0000-0000-000000000002', '20000000-0000-0000-0000-000000000102', '40000000-0000-0000-0000-000000000001', 'b0000000-0000-0000-0000-000000000001', 'STUDENT', 'ACTIVE', CURRENT_TIMESTAMP - INTERVAL '30 days'),
('c0000000-0000-0000-0000-000000000003', '20000000-0000-0000-0000-000000000103', '40000000-0000-0000-0000-000000000001', 'b0000000-0000-0000-0000-000000000001', 'STUDENT', 'ACTIVE', CURRENT_TIMESTAMP - INTERVAL '30 days');

-- --------------------------------------------
-- 4.3. Progress Tracking (2 progress records)
-- Note: Progress table does NOT have created_at column (schema mismatch fix)
-- --------------------------------------------

-- Student 1 (Minh) progress in CS101
INSERT INTO "Progress" (progress_id, user_id, course_id, module_id, status, started_at, completed_at) VALUES
('d0000000-0000-0000-0000-000000000001', '20000000-0000-0000-0000-000000000101', '40000000-0000-0000-0000-000000000001', '50000000-0000-0000-0000-000000000001', 'COMPLETED', CURRENT_TIMESTAMP - INTERVAL '25 days', CURRENT_TIMESTAMP - INTERVAL '18 days'),
('d0000000-0000-0000-0000-000000000002', '20000000-0000-0000-0000-000000000101', '40000000-0000-0000-0000-000000000001', '50000000-0000-0000-0000-000000000002', 'IN_PROGRESS', CURRENT_TIMESTAMP - INTERVAL '18 days', NULL);

-- ============================================
-- SECTION 5: QUIZ ATTEMPTS & ASSIGNMENTS
-- ============================================

-- --------------------------------------------
-- 5.1. Quiz Attempts (1 attempt)
-- Note: Attempt table does NOT have created_at column (schema mismatch fix)
-- --------------------------------------------

-- Student 1 (Minh) - Attempt 1 (completed and graded)
INSERT INTO "Attempt" (attempt_id, quiz_id, user_id, enrollment_id, attempt_number, status, started_at, submitted_at, total_score, max_possible_score, percentage_score, graded_at, graded_by, answers) VALUES
(
  'e0000000-0000-0000-0000-000000000001',
  'a0000000-0000-0000-0000-000000000001',
  '20000000-0000-0000-0000-000000000101',
  'c0000000-0000-0000-0000-000000000001',
  1,
  'SUBMITTED',
  CURRENT_TIMESTAMP - INTERVAL '10 days',
  CURRENT_TIMESTAMP - INTERVAL '10 days' + INTERVAL '25 minutes',
  25.00,
  25.00,
  100.00,
  CURRENT_TIMESTAMP - INTERVAL '10 days' + INTERVAL '1 hour',
  '20000000-0000-0000-0000-000000000002',  -- Graded by instructor Kiet
  '[
    {
      "question_id": "80000000-0000-0000-0000-000000000001",
      "answer_text": null,
      "selected_options": ["90000000-0000-0000-0000-000000000001"],
      "score": 10,
      "max_score": 10,
      "is_correct": true
    },
    {
      "question_id": "80000000-0000-0000-0000-000000000002",
      "answer_text": null,
      "selected_options": ["90000000-0000-0000-0000-000000000011"],
      "score": 10,
      "max_score": 10,
      "is_correct": true
    },
    {
      "question_id": "80000000-0000-0000-0000-000000000003",
      "answer_text": null,
      "selected_options": ["90000000-0000-0000-0000-000000000022"],
      "score": 5,
      "max_score": 5,
      "is_correct": true
    }
  ]'::json
);

-- --------------------------------------------
-- 5.2. Assignment Submissions (2 submissions)
-- Note: AssignmentSubmission table does NOT have created_at column (schema mismatch fix)
-- --------------------------------------------

INSERT INTO "AssignmentSubmission" (submission_id, lecture_id, user_id, enrollment_id, submission_number, status, submitted_at, score, max_score, feedback, graded_at, graded_by, file_urls) VALUES
-- Student 1 (Minh) - Java Hello World (graded)
(
  'f0000000-0000-0000-0000-000000000001',
  '60000000-0000-0000-0000-000000000002',
  '20000000-0000-0000-0000-000000000101',
  'c0000000-0000-0000-0000-000000000001',
  1,
  'GRADED',
  CURRENT_TIMESTAMP - INTERVAL '12 days',
  85.00,
  100.00,
  'Code chay tot, logic dung. Can improve code documentation.',
  CURRENT_TIMESTAMP - INTERVAL '11 days',
  '20000000-0000-0000-0000-000000000002',  -- Graded by instructor Kiet
  '["https://s3.amazonaws.com/blearning/submissions/minh-le-hello-world.zip"]'::json
),

-- Student 2 (Huong) - Java Hello World (submitted, not graded yet)
(
  'f0000000-0000-0000-0000-000000000002',
  '60000000-0000-0000-0000-000000000002',
  '20000000-0000-0000-0000-000000000102',
  'c0000000-0000-0000-0000-000000000002',
  1,
  'SUBMITTED',
  CURRENT_TIMESTAMP - INTERVAL '2 days',
  NULL,  -- score (not graded yet)
  NULL,  -- max_score
  NULL,  -- feedback
  NULL,  -- graded_at (not graded yet)
  NULL,  -- graded_by (not graded yet)
  '["https://s3.amazonaws.com/blearning/submissions/huong-pham-hello.java"]'::json
);

-- ============================================
-- SECTION 6: CERTIFICATES
-- ============================================

-- Certificate for Student 1
INSERT INTO "Certificate" (certificate_id, user_id, course_id, certificate_code, verification_code, issue_date, final_grade, pdf_url, status, created_at) VALUES
(
  'ff000000-0000-0000-0000-000000000001',
  '20000000-0000-0000-0000-000000000101',
  '40000000-0000-0000-0000-000000000002',
  'BL-2025-000001',
  'VERIFY-550e8400-e29b-41d4-a716-446655440000',
  CURRENT_DATE - INTERVAL '60 days',
  88.50,
  'https://s3.amazonaws.com/blearning/certificates/BL-2025-000001.pdf',
  'ACTIVE',
  CURRENT_TIMESTAMP - INTERVAL '60 days'
);

-- ============================================
-- DATA VERIFICATION & STATISTICS
-- ============================================

DO $$
DECLARE
  rec RECORD;
  v_count INTEGER;
BEGIN
  RAISE NOTICE '========================================';
  RAISE NOTICE 'SEED DATA STATISTICS';
  RAISE NOTICE '========================================';

  FOR rec IN
    SELECT table_name
    FROM information_schema.tables
    WHERE table_schema = 'public' AND table_type = 'BASE TABLE'
    ORDER BY table_name
  LOOP
    EXECUTE format('SELECT COUNT(*) FROM %I', rec.table_name) INTO v_count;
    RAISE NOTICE '%-30s : %s records', rec.table_name, v_count;
  END LOOP;

  RAISE NOTICE '========================================';
  RAISE NOTICE 'Data demonstrates:';
  RAISE NOTICE '- Complete user management workflow';
  RAISE NOTICE '- 3 diverse courses with realistic content';
  RAISE NOTICE '- 6 lectures including assignments';
  RAISE NOTICE '- Quiz system with multiple question types';
  RAISE NOTICE '- Both class-based and self-paced learning';
  RAISE NOTICE '- Progress tracking and grading';
  RAISE NOTICE '- Certificate issuance';
  RAISE NOTICE '========================================';
END $$;

-- ============================================
-- END OF SEED DATA
-- ============================================
