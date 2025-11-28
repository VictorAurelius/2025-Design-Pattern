-- ============================================
-- 04-seed-data.sql - Sample Data for Testing
-- B-Learning Core v1.0
-- Nguyễn Văn Kiệt - CNTT1-K63
-- Created: 2025-11-27
-- ============================================

-- Mục đích:
-- 1. Sample data để test database schema
-- 2. Demonstrate tất cả features của hệ thống
-- 3. Realistic data với Vietnamese content
-- 4. Test all FK relationships và constraints

-- LƯU Ý:
-- - Sử dụng gen_random_uuid() để tạo UUID
-- - Password được hash với bcrypt (example: 'password123')
-- - JSON structures follow ERD-SPEC.md
-- - Dates sử dụng CURRENT_DATE và interval

-- ============================================
-- SECTION 1: USER MANAGEMENT
-- ============================================

-- --------------------------------------------
-- 1.1. Roles
-- --------------------------------------------

INSERT INTO "Role" (role_id, name, description, permissions, created_at) VALUES
(
  '10000000-0000-0000-0000-000000000001',
  'ADMIN',
  'System Administrator',
  '["user.manage", "course.manage", "system.manage", "report.view"]'::json,
  CURRENT_TIMESTAMP
),
(
  '10000000-0000-0000-0000-000000000002',
  'INSTRUCTOR',
  'Course Instructor',
  '["course.create", "course.edit", "grade.manage", "class.manage"]'::json,
  CURRENT_TIMESTAMP
),
(
  '10000000-0000-0000-0000-000000000003',
  'TA',
  'Teaching Assistant',
  '["grade.manage", "discussion.moderate"]'::json,
  CURRENT_TIMESTAMP
),
(
  '10000000-0000-0000-0000-000000000004',
  'STUDENT',
  'Student',
  '["course.enroll", "quiz.take", "assignment.submit"]'::json,
  CURRENT_TIMESTAMP
);

-- --------------------------------------------
-- 1.2. Users
-- --------------------------------------------

-- Admin
INSERT INTO "User" (user_id, email, password_hash, first_name, last_name, account_status, preferences, created_at) VALUES
(
  '20000000-0000-0000-0000-000000000001',
  'admin@blearning.edu.vn',
  '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', -- password123
  'Nguyễn',
  'Admin',
  'ACTIVE',
  '{
    "notifications": {
      "assignment_due": {"email": true, "push": true},
      "grade_published": {"email": true, "push": true}
    },
    "locale": "vi",
    "timezone": "Asia/Ho_Chi_Minh",
    "theme": "light"
  }'::json,
  CURRENT_TIMESTAMP
);

-- Instructors
INSERT INTO "User" (user_id, email, password_hash, first_name, last_name, account_status, preferences, created_at) VALUES
(
  '20000000-0000-0000-0000-000000000002',
  'kiet.nguyen@blearning.edu.vn',
  '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', -- password123
  'Nguyễn Văn',
  'Kiệt',
  'ACTIVE',
  '{"locale": "vi", "timezone": "Asia/Ho_Chi_Minh"}'::json,
  CURRENT_TIMESTAMP
),
(
  '20000000-0000-0000-0000-000000000003',
  'linh.tran@blearning.edu.vn',
  '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
  'Trần Thị',
  'Linh',
  'ACTIVE',
  '{"locale": "vi", "timezone": "Asia/Ho_Chi_Minh"}'::json,
  CURRENT_TIMESTAMP
);

-- Students
INSERT INTO "User" (user_id, email, password_hash, first_name, last_name, account_status, preferences, created_at) VALUES
(
  '20000000-0000-0000-0000-000000000101',
  'minh.le@student.blearning.edu.vn',
  '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
  'Lê Quang',
  'Minh',
  'ACTIVE',
  '{
    "notifications": {
      "assignment_due": {"email": true, "push": false},
      "grade_published": {"email": true, "push": true}
    },
    "locale": "vi",
    "timezone": "Asia/Ho_Chi_Minh"
  }'::json,
  CURRENT_TIMESTAMP
),
(
  '20000000-0000-0000-0000-000000000102',
  'huong.pham@student.blearning.edu.vn',
  '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
  'Phạm Thị',
  'Hương',
  'ACTIVE',
  '{"locale": "vi", "timezone": "Asia/Ho_Chi_Minh"}'::json,
  CURRENT_TIMESTAMP
),
(
  '20000000-0000-0000-0000-000000000103',
  'tuan.vo@student.blearning.edu.vn',
  '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
  'Võ Văn',
  'Tuấn',
  'ACTIVE',
  '{"locale": "vi", "timezone": "Asia/Ho_Chi_Minh"}'::json,
  CURRENT_TIMESTAMP
),
(
  '20000000-0000-0000-0000-000000000104',
  'hoa.nguyen@student.blearning.edu.vn',
  '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
  'Nguyễn Thị',
  'Hoa',
  'ACTIVE',
  '{"locale": "vi", "timezone": "Asia/Ho_Chi_Minh"}'::json,
  CURRENT_TIMESTAMP
),
(
  '20000000-0000-0000-0000-000000000105',
  'dung.tran@student.blearning.edu.vn',
  '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
  'Trần Văn',
  'Dũng',
  'ACTIVE',
  '{"locale": "vi", "timezone": "Asia/Ho_Chi_Minh"}'::json,
  CURRENT_TIMESTAMP
);

-- --------------------------------------------
-- 1.3. UserRole Assignments
-- --------------------------------------------

INSERT INTO "UserRole" (user_role_id, user_id, role_id, assigned_at) VALUES
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
-- 2.1. Courses
-- --------------------------------------------

INSERT INTO "Course" (course_id, code, title, description, difficulty_level, credits, status, created_by, created_at) VALUES
(
  '40000000-0000-0000-0000-000000000001',
  'CS101',
  'Lập Trình Java Cơ Bản',
  'Khóa học giới thiệu về lập trình hướng đối tượng với Java. Học viên sẽ nắm vững cú pháp Java, OOP principles, và Design Patterns cơ bản.',
  'BEGINNER',
  3,
  'PUBLISHED',
  '20000000-0000-0000-0000-000000000002', -- Kiet Nguyen
  CURRENT_TIMESTAMP
),
(
  '40000000-0000-0000-0000-000000000002',
  'DB201',
  'Thiết Kế Cơ Sở Dữ Liệu',
  'Khóa học về database design, normalization, SQL, và PostgreSQL. Bao gồm cả NoSQL và best practices.',
  'INTERMEDIATE',
  4,
  'PUBLISHED',
  '20000000-0000-0000-0000-000000000003', -- Linh Tran
  CURRENT_TIMESTAMP
),
(
  '40000000-0000-0000-0000-000000000003',
  'WEB301',
  'Phát Triển Web Full-Stack',
  'Khóa học advanced về web development với React, Node.js, Express, và PostgreSQL. Project-based learning.',
  'ADVANCED',
  5,
  'DRAFT',
  '20000000-0000-0000-0000-000000000002',
  CURRENT_TIMESTAMP
);

-- --------------------------------------------
-- 2.2. Modules
-- --------------------------------------------

-- Course CS101 - Java
INSERT INTO "Module" (module_id, course_id, title, description, order_num, estimated_duration_minutes, prerequisite_module_ids, created_at) VALUES
(
  '50000000-0000-0000-0000-000000000001',
  '40000000-0000-0000-0000-000000000001',
  'Giới thiệu về Java',
  'Cài đặt môi trường, cú pháp cơ bản, biến và kiểu dữ liệu',
  1,
  180,
  NULL,
  CURRENT_TIMESTAMP
),
(
  '50000000-0000-0000-0000-000000000002',
  '40000000-0000-0000-0000-000000000001',
  'Lập trình hướng đối tượng',
  'Class, Object, Inheritance, Polymorphism, Encapsulation',
  2,
  240,
  '["50000000-0000-0000-0000-000000000001"]'::uuid[],
  CURRENT_TIMESTAMP
),
(
  '50000000-0000-0000-0000-000000000003',
  '40000000-0000-0000-0000-000000000001',
  'Design Patterns',
  'Singleton, Factory, Observer, Strategy patterns',
  3,
  300,
  '["50000000-0000-0000-0000-000000000002"]'::uuid[],
  CURRENT_TIMESTAMP
);

-- Course DB201 - Database
INSERT INTO "Module" (module_id, course_id, title, description, order_num, estimated_duration_minutes, created_at) VALUES
(
  '50000000-0000-0000-0000-000000000011',
  '40000000-0000-0000-0000-000000000002',
  'Database Fundamentals',
  'ERD, Normalization, Keys, Relationships',
  1,
  200,
  CURRENT_TIMESTAMP
),
(
  '50000000-0000-0000-0000-000000000012',
  '40000000-0000-0000-0000-000000000002',
  'SQL và PostgreSQL',
  'DDL, DML, Queries, Joins, Indexes',
  2,
  280,
  CURRENT_TIMESTAMP
);

-- --------------------------------------------
-- 2.3. Lectures
-- --------------------------------------------

-- Module 1: Giới thiệu về Java
INSERT INTO "Lecture" (lecture_id, module_id, title, description, type, order_num, duration_minutes, created_at) VALUES
(
  '60000000-0000-0000-0000-000000000001',
  '50000000-0000-0000-0000-000000000001',
  'Giới thiệu về Java và JDK',
  'Lịch sử Java, cài đặt JDK, IDE setup (IntelliJ IDEA)',
  'VIDEO',
  1,
  45,
  CURRENT_TIMESTAMP
),
(
  '60000000-0000-0000-0000-000000000002',
  '50000000-0000-0000-0000-000000000001',
  'Cú pháp Java cơ bản',
  'Variables, data types, operators, control flow',
  'VIDEO',
  2,
  60,
  CURRENT_TIMESTAMP
),
(
  '60000000-0000-0000-0000-000000000003',
  '50000000-0000-0000-0000-000000000001',
  'Bài tập: Hello World và Calculator',
  'Viết chương trình Hello World và Simple Calculator',
  'ASSIGNMENT',
  3,
  NULL,
  CURRENT_TIMESTAMP
);

-- Assignment config cho bài tập
UPDATE "Lecture" SET assignment_config = '{
  "max_points": 100,
  "due_date": "2025-12-15T23:59:00Z",
  "submission_types": ["file", "code"],
  "allowed_file_types": [".java", ".zip"],
  "max_file_size_mb": 5,
  "instructions": "Viết chương trình Java:\n1. Hello World (30 điểm)\n2. Simple Calculator với 4 phép tính (70 điểm)\n\nNộp file .java hoặc .zip",
  "rubric": {
    "code_quality": 30,
    "functionality": 50,
    "documentation": 20
  }
}'::json
WHERE lecture_id = '60000000-0000-0000-0000-000000000003';

-- Module 2: OOP
INSERT INTO "Lecture" (lecture_id, module_id, title, description, type, order_num, duration_minutes, created_at) VALUES
(
  '60000000-0000-0000-0000-000000000011',
  '50000000-0000-0000-0000-000000000002',
  'Class và Object trong Java',
  'Định nghĩa class, constructor, methods, this keyword',
  'VIDEO',
  1,
  50,
  CURRENT_TIMESTAMP
),
(
  '60000000-0000-0000-0000-000000000012',
  '50000000-0000-0000-0000-000000000002',
  'Inheritance và Polymorphism',
  'Extends, super, method overriding, abstract class',
  'SLIDE',
  2,
  45,
  CURRENT_TIMESTAMP
);

-- --------------------------------------------
-- 2.4. Resources
-- --------------------------------------------

INSERT INTO "Resource" (resource_id, lecture_id, file_url, file_type, file_size_bytes, created_at) VALUES
(
  '70000000-0000-0000-0000-000000000001',
  '60000000-0000-0000-0000-000000000001',
  'https://s3.amazonaws.com/blearning/videos/java-intro.mp4',
  'video/mp4',
  125829120,
  CURRENT_TIMESTAMP
),
(
  '70000000-0000-0000-0000-000000000002',
  '60000000-0000-0000-0000-000000000002',
  'https://s3.amazonaws.com/blearning/videos/java-syntax.mp4',
  'video/mp4',
  158920704,
  CURRENT_TIMESTAMP
),
(
  '70000000-0000-0000-0000-000000000003',
  '60000000-0000-0000-0000-000000000012',
  'https://s3.amazonaws.com/blearning/slides/oop-inheritance.pdf',
  'application/pdf',
  2458624,
  CURRENT_TIMESTAMP
);

-- ============================================
-- SECTION 3: ASSESSMENT
-- ============================================

-- --------------------------------------------
-- 3.1. Questions (Question Bank)
-- --------------------------------------------

-- MCQ Questions
INSERT INTO "Question" (question_id, course_id, question_text, type, default_points, created_by, created_at) VALUES
(
  '80000000-0000-0000-0000-000000000001',
  '40000000-0000-0000-0000-000000000001',
  'Java là ngôn ngữ lập trình thuộc loại nào?',
  'MCQ',
  10,
  '20000000-0000-0000-0000-000000000002',
  CURRENT_TIMESTAMP
),
(
  '80000000-0000-0000-0000-000000000002',
  '40000000-0000-0000-0000-000000000001',
  'Keyword nào dùng để kế thừa class trong Java?',
  'MCQ',
  10,
  '20000000-0000-0000-0000-000000000002',
  CURRENT_TIMESTAMP
),
(
  '80000000-0000-0000-0000-000000000003',
  '40000000-0000-0000-0000-000000000001',
  'Java hỗ trợ đa kế thừa (multiple inheritance)?',
  'TRUE_FALSE',
  5,
  '20000000-0000-0000-0000-000000000002',
  CURRENT_TIMESTAMP
),
(
  '80000000-0000-0000-0000-000000000004',
  '40000000-0000-0000-0000-000000000001',
  'Giải thích sự khác biệt giữa abstract class và interface trong Java.',
  'ESSAY',
  20,
  '20000000-0000-0000-0000-000000000002',
  CURRENT_TIMESTAMP
);

-- --------------------------------------------
-- 3.2. Options (for MCQ questions)
-- --------------------------------------------

-- Question 1 options
INSERT INTO "Option" (option_id, question_id, option_text, is_correct, order_num) VALUES
('90000000-0000-0000-0000-000000000001', '80000000-0000-0000-0000-000000000001', 'Hướng đối tượng (OOP)', true, 1),
('90000000-0000-0000-0000-000000000002', '80000000-0000-0000-0000-000000000001', 'Thủ tục (Procedural)', false, 2),
('90000000-0000-0000-0000-000000000003', '80000000-0000-0000-0000-000000000001', 'Hàm (Functional)', false, 3),
('90000000-0000-0000-0000-000000000004', '80000000-0000-0000-0000-000000000001', 'Logic', false, 4);

-- Question 2 options
INSERT INTO "Option" (option_id, question_id, option_text, is_correct, order_num) VALUES
('90000000-0000-0000-0000-000000000011', '80000000-0000-0000-0000-000000000002', 'extends', true, 1),
('90000000-0000-0000-0000-000000000012', '80000000-0000-0000-0000-000000000002', 'implements', false, 2),
('90000000-0000-0000-0000-000000000013', '80000000-0000-0000-0000-000000000002', 'inherits', false, 3),
('90000000-0000-0000-0000-000000000014', '80000000-0000-0000-0000-000000000002', 'super', false, 4);

-- Question 3 options (TRUE_FALSE)
INSERT INTO "Option" (option_id, question_id, option_text, is_correct, order_num) VALUES
('90000000-0000-0000-0000-000000000021', '80000000-0000-0000-0000-000000000003', 'True', false, 1),
('90000000-0000-0000-0000-000000000022', '80000000-0000-0000-0000-000000000003', 'False', true, 2);

-- --------------------------------------------
-- 3.3. Quizzes
-- --------------------------------------------

INSERT INTO "Quiz" (quiz_id, course_id, title, description, instructions, status, duration_minutes, total_points, passing_score, max_attempts, questions, created_by, created_at) VALUES
(
  'a0000000-0000-0000-0000-000000000001',
  '40000000-0000-0000-0000-000000000001',
  'Kiểm tra giữa kỳ - Java Basics',
  'Bài kiểm tra kiến thức cơ bản về Java và OOP',
  'Bài kiểm tra gồm 4 câu hỏi. Thời gian: 30 phút. Điểm đạt: 70%. Được làm tối đa 2 lần.',
  'PUBLISHED',
  30,
  45,
  31.5,
  2,
  '[
    {"question_id": "80000000-0000-0000-0000-000000000001", "points": 10, "order": 1},
    {"question_id": "80000000-0000-0000-0000-000000000002", "points": 10, "order": 2},
    {"question_id": "80000000-0000-0000-0000-000000000003", "points": 5, "order": 3},
    {"question_id": "80000000-0000-0000-0000-000000000004", "points": 20, "order": 4}
  ]'::json,
  '20000000-0000-0000-0000-000000000002',
  CURRENT_TIMESTAMP
);

-- ============================================
-- SECTION 4: ENROLLMENT & PROGRESS
-- ============================================

-- --------------------------------------------
-- 4.1. Classes
-- --------------------------------------------

INSERT INTO "Class" (class_id, course_id, class_name, start_date, end_date, instructor_id, max_students, status, schedules, created_at) VALUES
(
  'b0000000-0000-0000-0000-000000000001',
  '40000000-0000-0000-0000-000000000001',
  'CS101-K63-01',
  '2025-01-10',
  '2025-05-15',
  '20000000-0000-0000-0000-000000000002',
  30,
  'ONGOING',
  '[
    {
      "session_id": "session-001",
      "date": "2025-01-10",
      "start_time": "09:00",
      "end_time": "11:00",
      "topic": "Giới thiệu khóa học và Java",
      "location": "Phòng A101",
      "type": "IN_PERSON",
      "attendances": [
        {"user_id": "20000000-0000-0000-0000-000000000101", "status": "PRESENT", "check_in": "09:05"},
        {"user_id": "20000000-0000-0000-0000-000000000102", "status": "PRESENT", "check_in": "08:58"},
        {"user_id": "20000000-0000-0000-0000-000000000103", "status": "LATE", "check_in": "09:15"}
      ]
    },
    {
      "session_id": "session-002",
      "date": "2025-01-17",
      "start_time": "09:00",
      "end_time": "11:00",
      "topic": "Cú pháp Java cơ bản",
      "location": "Phòng A101",
      "type": "IN_PERSON",
      "attendances": [
        {"user_id": "20000000-0000-0000-0000-000000000101", "status": "PRESENT", "check_in": "09:02"},
        {"user_id": "20000000-0000-0000-0000-000000000102", "status": "ABSENT"},
        {"user_id": "20000000-0000-0000-0000-000000000103", "status": "PRESENT", "check_in": "09:01"}
      ]
    }
  ]'::json,
  CURRENT_TIMESTAMP
);

-- --------------------------------------------
-- 4.2. Enrollments
-- --------------------------------------------

-- Class-based enrollments (blended learning)
INSERT INTO "Enrollment" (enrollment_id, user_id, course_id, class_id, status, enrolled_at) VALUES
('c0000000-0000-0000-0000-000000000001', '20000000-0000-0000-0000-000000000101', '40000000-0000-0000-0000-000000000001', 'b0000000-0000-0000-0000-000000000001', 'ACTIVE', CURRENT_TIMESTAMP - INTERVAL '30 days'),
('c0000000-0000-0000-0000-000000000002', '20000000-0000-0000-0000-000000000102', '40000000-0000-0000-0000-000000000001', 'b0000000-0000-0000-0000-000000000001', 'ACTIVE', CURRENT_TIMESTAMP - INTERVAL '30 days'),
('c0000000-0000-0000-0000-000000000003', '20000000-0000-0000-0000-000000000103', '40000000-0000-0000-0000-000000000001', 'b0000000-0000-0000-0000-000000000001', 'ACTIVE', CURRENT_TIMESTAMP - INTERVAL '30 days');

-- Self-paced enrollments (class_id = NULL)
INSERT INTO "Enrollment" (enrollment_id, user_id, course_id, class_id, status, enrolled_at) VALUES
('c0000000-0000-0000-0000-000000000011', '20000000-0000-0000-0000-000000000104', '40000000-0000-0000-0000-000000000002', NULL, 'ACTIVE', CURRENT_TIMESTAMP - INTERVAL '15 days'),
('c0000000-0000-0000-0000-000000000012', '20000000-0000-0000-0000-000000000105', '40000000-0000-0000-0000-000000000002', NULL, 'ACTIVE', CURRENT_TIMESTAMP - INTERVAL '20 days');

-- --------------------------------------------
-- 4.3. Progress Tracking
-- --------------------------------------------

-- Student 1 (Minh) progress in CS101
INSERT INTO "Progress" (progress_id, user_id, course_id, module_id, status, completion_percentage, started_at, completed_at) VALUES
('d0000000-0000-0000-0000-000000000001', '20000000-0000-0000-0000-000000000101', '40000000-0000-0000-0000-000000000001', '50000000-0000-0000-0000-000000000001', 'COMPLETED', 100, CURRENT_TIMESTAMP - INTERVAL '25 days', CURRENT_TIMESTAMP - INTERVAL '18 days'),
('d0000000-0000-0000-0000-000000000002', '20000000-0000-0000-0000-000000000101', '40000000-0000-0000-0000-000000000001', '50000000-0000-0000-0000-000000000002', 'IN_PROGRESS', 60, CURRENT_TIMESTAMP - INTERVAL '18 days', NULL);

-- Student 2 (Huong) progress in CS101
INSERT INTO "Progress" (progress_id, user_id, course_id, module_id, status, completion_percentage, started_at) VALUES
('d0000000-0000-0000-0000-000000000011', '20000000-0000-0000-0000-000000000102', '40000000-0000-0000-0000-000000000001', '50000000-0000-0000-0000-000000000001', 'COMPLETED', 100, CURRENT_TIMESTAMP - INTERVAL '25 days'),
('d0000000-0000-0000-0000-000000000012', '20000000-0000-0000-0000-000000000102', '40000000-0000-0000-0000-000000000001', '50000000-0000-0000-0000-000000000002', 'IN_PROGRESS', 40, CURRENT_TIMESTAMP - INTERVAL '15 days');

-- ============================================
-- SECTION 5: QUIZ ATTEMPTS & ASSIGNMENTS
-- ============================================

-- --------------------------------------------
-- 5.1. Quiz Attempts
-- --------------------------------------------

-- Student 1 (Minh) - Attempt 1 (completed and graded)
INSERT INTO "Attempt" (attempt_id, quiz_id, user_id, enrollment_id, attempt_number, status, started_at, submitted_at, score, max_score, answers) VALUES
(
  'e0000000-0000-0000-0000-000000000001',
  'a0000000-0000-0000-0000-000000000001',
  '20000000-0000-0000-0000-000000000101',
  'c0000000-0000-0000-0000-000000000001',
  1,
  'GRADED',
  CURRENT_TIMESTAMP - INTERVAL '10 days',
  CURRENT_TIMESTAMP - INTERVAL '10 days' + INTERVAL '25 minutes',
  38.5,
  45,
  '[
    {
      "question_id": "80000000-0000-0000-0000-000000000001",
      "answer_text": null,
      "selected_options": ["90000000-0000-0000-0000-000000000001"],
      "score": 10,
      "max_score": 10,
      "is_correct": true,
      "graded_at": "2025-11-20T10:30:00Z"
    },
    {
      "question_id": "80000000-0000-0000-0000-000000000002",
      "answer_text": null,
      "selected_options": ["90000000-0000-0000-0000-000000000011"],
      "score": 10,
      "max_score": 10,
      "is_correct": true,
      "graded_at": "2025-11-20T10:30:00Z"
    },
    {
      "question_id": "80000000-0000-0000-0000-000000000003",
      "answer_text": null,
      "selected_options": ["90000000-0000-0000-0000-000000000022"],
      "score": 5,
      "max_score": 5,
      "is_correct": true,
      "graded_at": "2025-11-20T10:30:00Z"
    },
    {
      "question_id": "80000000-0000-0000-0000-000000000004",
      "answer_text": "Abstract class có thể có methods với implementation, trong khi interface chỉ có method signatures (trước Java 8). Abstract class có thể có constructor, interface không. Một class chỉ extend được 1 abstract class nhưng implement nhiều interfaces.",
      "selected_options": null,
      "score": 13.5,
      "max_score": 20,
      "is_correct": null,
      "graded_at": "2025-11-20T11:00:00Z",
      "feedback": "Câu trả lời tốt nhưng thiếu một số chi tiết về default methods trong Java 8+"
    }
  ]'::json
);

-- Student 2 (Huong) - Attempt 1 (submitted, pending grading)
INSERT INTO "Attempt" (attempt_id, quiz_id, user_id, enrollment_id, attempt_number, status, started_at, submitted_at, score, max_score, answers) VALUES
(
  'e0000000-0000-0000-0000-000000000002',
  'a0000000-0000-0000-0000-000000000001',
  '20000000-0000-0000-0000-000000000102',
  'c0000000-0000-0000-0000-000000000002',
  1,
  'PENDING_GRADING',
  CURRENT_TIMESTAMP - INTERVAL '1 day',
  CURRENT_TIMESTAMP - INTERVAL '1 day' + INTERVAL '28 minutes',
  NULL,
  45,
  '[
    {
      "question_id": "80000000-0000-0000-0000-000000000001",
      "answer_text": null,
      "selected_options": ["90000000-0000-0000-0000-000000000001"],
      "score": null,
      "max_score": 10,
      "is_correct": null
    },
    {
      "question_id": "80000000-0000-0000-0000-000000000002",
      "answer_text": null,
      "selected_options": ["90000000-0000-0000-0000-000000000012"],
      "score": null,
      "max_score": 10,
      "is_correct": null
    },
    {
      "question_id": "80000000-0000-0000-0000-000000000003",
      "answer_text": null,
      "selected_options": ["90000000-0000-0000-0000-000000000021"],
      "score": null,
      "max_score": 5,
      "is_correct": null
    },
    {
      "question_id": "80000000-0000-0000-0000-000000000004",
      "answer_text": "Abstract class và interface đều dùng để tạo abstraction trong Java.",
      "selected_options": null,
      "score": null,
      "max_score": 20,
      "is_correct": null
    }
  ]'::json
);

-- --------------------------------------------
-- 5.2. Assignment Submissions
-- --------------------------------------------

-- Student 1 (Minh) - Assignment submission (graded)
INSERT INTO "AssignmentSubmission" (submission_id, lecture_id, user_id, enrollment_id, submission_number, status, submitted_at, score, max_score, feedback, file_urls) VALUES
(
  'f0000000-0000-0000-0000-000000000001',
  '60000000-0000-0000-0000-000000000003',
  '20000000-0000-0000-0000-000000000101',
  'c0000000-0000-0000-0000-000000000001',
  1,
  'GRADED',
  CURRENT_TIMESTAMP - INTERVAL '12 days',
  85,
  100,
  'Code chạy tốt, logic đúng. Cần improve code documentation và error handling.',
  '["https://s3.amazonaws.com/blearning/submissions/minh-le-java-assignment1.zip"]'::json
);

-- Student 2 (Huong) - Assignment submission (submitted)
INSERT INTO "AssignmentSubmission" (submission_id, lecture_id, user_id, enrollment_id, submission_number, status, submitted_at, file_urls) VALUES
(
  'f0000000-0000-0000-0000-000000000002',
  '60000000-0000-0000-0000-000000000003',
  '20000000-0000-0000-0000-000000000102',
  'c0000000-0000-0000-0000-000000000002',
  1,
  'SUBMITTED',
  CURRENT_TIMESTAMP - INTERVAL '2 days',
  '["https://s3.amazonaws.com/blearning/submissions/huong-pham-java-assignment1.java"]'::json
);

-- ============================================
-- SECTION 6: CERTIFICATES
-- ============================================

-- Student 1 completed a course and earned certificate
-- (Giả sử có course khác đã hoàn thành trước đó)
INSERT INTO "Certificate" (certificate_id, user_id, course_id, certificate_code, verification_code, issue_date, final_grade, pdf_url, status, created_at) VALUES
(
  'g0000000-0000-0000-0000-000000000001',
  '20000000-0000-0000-0000-000000000101',
  '40000000-0000-0000-0000-000000000002',
  'BL-2025-000001',
  'VERIFY-550e8400-e29b-41d4-a716',
  CURRENT_DATE - INTERVAL '60 days',
  88.5,
  'https://s3.amazonaws.com/blearning/certificates/BL-2025-000001.pdf',
  'ACTIVE',
  CURRENT_TIMESTAMP - INTERVAL '60 days'
);

-- ============================================
-- STATISTICS & VERIFICATION
-- ============================================

-- Count records in each table
DO $$
DECLARE
  rec RECORD;
  v_count INTEGER;
BEGIN
  RAISE NOTICE '=== SEED DATA STATISTICS ===';

  FOR rec IN
    SELECT tablename
    FROM pg_tables
    WHERE schemaname = 'public'
    ORDER BY tablename
  LOOP
    EXECUTE format('SELECT COUNT(*) FROM %I', rec.tablename) INTO v_count;
    RAISE NOTICE '% : % records', rec.tablename, v_count;
  END LOOP;

  RAISE NOTICE '============================';
END $$;

-- ============================================
-- SAMPLE QUERIES FOR TESTING
-- ============================================

-- Query 1: List all students in a class with their progress
/*
SELECT
  u.first_name || ' ' || u.last_name AS student_name,
  e.status AS enrollment_status,
  COUNT(p.progress_id) AS modules_started,
  SUM(CASE WHEN p.status = 'COMPLETED' THEN 1 ELSE 0 END) AS modules_completed
FROM "Enrollment" e
JOIN "User" u ON e.user_id = u.user_id
LEFT JOIN "Progress" p ON e.user_id = p.user_id AND e.course_id = p.course_id
WHERE e.class_id = 'b0000000-0000-0000-0000-000000000001'
GROUP BY u.first_name, u.last_name, e.status;
*/

-- Query 2: Quiz results for a student
/*
SELECT
  q.title AS quiz_title,
  a.attempt_number,
  a.score,
  a.max_score,
  ROUND((a.score / a.max_score * 100)::numeric, 2) AS percentage,
  CASE WHEN a.score >= q.passing_score THEN 'PASS' ELSE 'FAIL' END AS result
FROM "Attempt" a
JOIN "Quiz" q ON a.quiz_id = q.quiz_id
WHERE a.user_id = '20000000-0000-0000-0000-000000000101'
  AND a.status = 'GRADED';
*/

-- Query 3: Class attendance report
/*
SELECT
  c.class_name,
  jsonb_array_elements(c.schedules::jsonb) ->> 'date' AS session_date,
  jsonb_array_elements(c.schedules::jsonb) ->> 'topic' AS topic,
  jsonb_array_length((jsonb_array_elements(c.schedules::jsonb) -> 'attendances')::jsonb) AS total_attendances
FROM "Class" c
WHERE c.class_id = 'b0000000-0000-0000-0000-000000000001';
*/

-- ============================================
-- END OF SEED DATA
-- ============================================

COMMENT ON EXTENSION "uuid-ossp" IS 'Seed data created with gen_random_uuid()';
