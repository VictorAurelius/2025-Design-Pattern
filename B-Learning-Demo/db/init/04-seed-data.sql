-- ============================================
-- 04-seed-data.sql - Sample Data for Testing
-- B-Learning Core v1.0
-- Author: Nguyen Van Kiet - CNTT1-K63
-- Created: 2025-11-27
-- Fixed: Complete rewrite based on correct schema
-- ============================================

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
  'Quản trị viên hệ thống - có toàn quyền quản lý',
  '["user.manage", "course.manage", "system.config", "report.view"]'::json,
  CURRENT_TIMESTAMP
),
(
  '10000000-0000-0000-0000-000000000002',
  'INSTRUCTOR',
  'Giảng viên - tạo và quản lý khóa học, chấm bài',
  '["course.create", "course.edit", "grade.manage", "class.manage"]'::json,
  CURRENT_TIMESTAMP
),
(
  '10000000-0000-0000-0000-000000000003',
  'TA',
  'Trợ giảng - hỗ trợ chấm bài và quản lý lớp',
  '["grade.manage", "discussion.moderate", "class.assist"]'::json,
  CURRENT_TIMESTAMP
),
(
  '10000000-0000-0000-0000-000000000004',
  'STUDENT',
  'Học viên - đăng ký khóa học, học bài, làm quiz và assignment',
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
  'Hệ Thống',
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
  'Kiệt',
  'Nguyễn Văn',
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
  'Trần Thị',
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
  'Lê Quang',
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
  'Hương',
  'Phạm Thị',
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
  'Tuấn',
  'Võ Văn',
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
  'Nguyễn Thị',
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
  'Dũng',
  'Trần Văn',
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
  'Lập Trình Java Cơ Bản',
  'Khóa học giới thiệu về lập trình hướng đối tượng với Java. Học viên sẽ nắm vững cú pháp Java, OOP principles, và Design Patterns cơ bản.',
  'Học Java từ cơ bản đến nâng cao với 23 design patterns',
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
  'Phát Triển Web Frontend',
  'Khóa học về phát triển web frontend với HTML, CSS, JavaScript và React. Bao gồm responsive design, state management, và best practices.',
  'Thành thạo HTML, CSS, JavaScript và React',
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
  'Cơ Sở Dữ Liệu Nâng Cao',
  'PostgreSQL, MongoDB, thiết kế database, normalization, indexing và query optimization. Bao gồm cả NoSQL và best practices.',
  'Database design, SQL, NoSQL và performance tuning',
  'ADVANCED',
  45.00,
  'PUBLISHED',
  '20000000-0000-0000-0000-000000000003',
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP
),
(
  '40000000-0000-0000-0000-000000000004',
  'ALGO201',
  'Cấu Trúc Dữ Liệu và Giải Thuật',
  'Khóa học về các cấu trúc dữ liệu cơ bản và giải thuật: Arrays, Linked Lists, Trees, Sorting, Searching algorithms.',
  'Data structures và algorithms với Java',
  'INTERMEDIATE',
  55.00,
  'PUBLISHED',
  '20000000-0000-0000-0000-000000000002',
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP
),
(
  '40000000-0000-0000-0000-000000000005',
  'MOBILE101',
  'Phát Triển Ứng Dụng Mobile',
  'React Native và Flutter cho iOS/Android. State management, navigation, API integration, và deployment.',
  'Xây dựng mobile app với React Native và Flutter',
  'BEGINNER',
  60.00,
  'PUBLISHED',
  '20000000-0000-0000-0000-000000000002',
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP
),
(
  '40000000-0000-0000-0000-000000000006',
  'CLOUD301',
  'Cloud Computing với AWS',
  'Amazon Web Services: EC2, S3, Lambda, RDS, CloudFront. Infrastructure as Code với Terraform.',
  'AWS cloud services và serverless architecture',
  'ADVANCED',
  70.00,
  'PUBLISHED',
  '20000000-0000-0000-0000-000000000003',
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP
),
(
  '40000000-0000-0000-0000-000000000007',
  'AI201',
  'Trí Tuệ Nhân Tạo Cơ Bản',
  'Machine Learning, Neural Networks với Python. Scikit-learn, TensorFlow, và Keras.',
  'Machine Learning và Deep Learning với Python',
  'INTERMEDIATE',
  65.00,
  'PUBLISHED',
  '20000000-0000-0000-0000-000000000003',
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP
),
(
  '40000000-0000-0000-0000-000000000008',
  'SEC301',
  'Bảo Mật Ứng Dụng Web',
  'OWASP Top 10, Authentication, Authorization, HTTPS, JWT, OAuth2. Penetration testing và secure coding.',
  'Web security và ethical hacking',
  'ADVANCED',
  48.00,
  'PUBLISHED',
  '20000000-0000-0000-0000-000000000003',
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP
),
(
  '40000000-0000-0000-0000-000000000009',
  'DEVOPS201',
  'DevOps và CI/CD',
  'Docker, Kubernetes, Jenkins, GitLab CI/CD pipeline. Monitoring với Prometheus và Grafana.',
  'DevOps practices và automation',
  'INTERMEDIATE',
  52.00,
  'PUBLISHED',
  '20000000-0000-0000-0000-000000000002',
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP
),
(
  '40000000-0000-0000-0000-000000000010',
  'UI101',
  'UI/UX Design Fundamentals',
  'Figma, Adobe XD, Design principles, User research, Prototyping, Usability testing.',
  'Thiết kế giao diện và trải nghiệm người dùng',
  'BEGINNER',
  35.00,
  'PUBLISHED',
  '20000000-0000-0000-0000-000000000003',
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP
);

-- --------------------------------------------
-- 2.2. Modules (20 modules - 2 per course)
-- --------------------------------------------

INSERT INTO "Module" (module_id, course_id, title, description, order_num, estimated_duration_minutes, prerequisite_module_ids, created_at, updated_at) VALUES
-- JAVA101 modules
('50000000-0000-0000-0000-000000000001', '40000000-0000-0000-0000-000000000001', 'Cơ Bản Java', 'Cú pháp, biến, vòng lặp, điều kiện', 1, 180, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('50000000-0000-0000-0000-000000000002', '40000000-0000-0000-0000-000000000001', 'Lập Trình Hướng Đối Tượng', 'Class, Object, Inheritance, Polymorphism', 2, 240, '{"50000000-0000-0000-0000-000000000001"}'::uuid[], CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- WEB201 modules
('50000000-0000-0000-0000-000000000003', '40000000-0000-0000-0000-000000000002', 'HTML & CSS Cơ Bản', 'Cấu trúc HTML và styling với CSS', 1, 200, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('50000000-0000-0000-0000-000000000004', '40000000-0000-0000-0000-000000000002', 'JavaScript & React', 'JS ES6 và React components', 2, 280, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- DB301 modules
('50000000-0000-0000-0000-000000000005', '40000000-0000-0000-0000-000000000003', 'SQL Nâng Cao', 'Complex queries, indexing, optimization', 1, 220, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('50000000-0000-0000-0000-000000000006', '40000000-0000-0000-0000-000000000003', 'NoSQL & MongoDB', 'Document databases và aggregation', 2, 200, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- ALGO201 modules
('50000000-0000-0000-0000-000000000007', '40000000-0000-0000-0000-000000000004', 'Cấu Trúc Dữ Liệu', 'Arrays, LinkedList, Stack, Queue, Trees', 1, 250, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('50000000-0000-0000-0000-000000000008', '40000000-0000-0000-0000-000000000004', 'Giải Thuật Sắp Xếp', 'Bubble, Quick, Merge sort algorithms', 2, 230, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- MOBILE101 modules
('50000000-0000-0000-0000-000000000009', '40000000-0000-0000-0000-000000000005', 'React Native Basics', 'Components, Navigation, State management', 1, 260, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('50000000-0000-0000-0000-000000000010', '40000000-0000-0000-0000-000000000005', 'Flutter Development', 'Dart language và Widget system', 2, 280, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- CLOUD301 modules
('50000000-0000-0000-0000-000000000011', '40000000-0000-0000-0000-000000000006', 'AWS Core Services', 'EC2, S3, VPC, RDS setup and management', 1, 300, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('50000000-0000-0000-0000-000000000012', '40000000-0000-0000-0000-000000000006', 'Serverless Architecture', 'Lambda functions và API Gateway', 2, 270, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- AI201 modules
('50000000-0000-0000-0000-000000000013', '40000000-0000-0000-0000-000000000007', 'Machine Learning Cơ Bản', 'Supervised/Unsupervised learning, algorithms', 1, 290, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('50000000-0000-0000-0000-000000000014', '40000000-0000-0000-0000-000000000007', 'Neural Networks', 'Deep learning với TensorFlow/Keras', 2, 320, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- SEC301 modules
('50000000-0000-0000-0000-000000000015', '40000000-0000-0000-0000-000000000008', 'Web Security Fundamentals', 'OWASP Top 10, XSS, CSRF, SQL Injection', 1, 240, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('50000000-0000-0000-0000-000000000016', '40000000-0000-0000-0000-000000000008', 'Authentication & Authorization', 'JWT, OAuth2, RBAC implementation', 2, 250, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- DEVOPS201 modules
('50000000-0000-0000-0000-000000000017', '40000000-0000-0000-0000-000000000009', 'Containerization', 'Docker containers và Docker Compose', 1, 220, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('50000000-0000-0000-0000-000000000018', '40000000-0000-0000-0000-000000000009', 'CI/CD Pipelines', 'Jenkins, GitLab CI automated deployment', 2, 260, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- UI101 modules
('50000000-0000-0000-0000-000000000019', '40000000-0000-0000-0000-000000000010', 'Design Principles', 'Color theory, Typography, Layout principles', 1, 180, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('50000000-0000-0000-0000-000000000020', '40000000-0000-0000-0000-000000000010', 'Prototyping Tools', 'Figma advanced features và user testing', 2, 200, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- --------------------------------------------
-- 2.3. Lectures (22 lectures including assignments)
-- --------------------------------------------

INSERT INTO "Lecture" (lecture_id, module_id, title, description, type, order_num, duration_seconds, assignment_config, created_at, updated_at) VALUES
-- JAVA101 lectures
('60000000-0000-0000-0000-000000000001', '50000000-0000-0000-0000-000000000001', 'Giới Thiệu Java và JDK', 'Lịch sử Java, cài đặt JDK, IDE setup', 'VIDEO', 1, 2700, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('60000000-0000-0000-0000-000000000002', '50000000-0000-0000-0000-000000000001', 'Bài Tập: Hello World', 'Viết chương trình Java đầu tiên', 'ASSIGNMENT', 2, NULL, '{"max_points": 100, "due_date": "2025-12-31T23:59:00Z", "submission_types": ["file", "code"], "instructions": "Viết chương trình Hello World bằng Java"}'::json, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('60000000-0000-0000-0000-000000000003', '50000000-0000-0000-0000-000000000002', 'Bài Tập: OOP Calculator', 'Tạo máy tính sử dụng OOP', 'ASSIGNMENT', 1, NULL, '{"max_points": 150, "due_date": "2025-12-31T23:59:00Z", "submission_types": ["file"], "instructions": "Tạo lớp Calculator với các phương thức +, -, *, /"}'::json, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- WEB201 lectures
('60000000-0000-0000-0000-000000000004', '50000000-0000-0000-0000-000000000003', 'HTML Cơ Bản', 'Cấu trúc HTML và semantic tags', 'VIDEO', 1, 3000, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('60000000-0000-0000-0000-000000000005', '50000000-0000-0000-0000-000000000003', 'Bài Tập: Personal Website', 'Tạo website cá nhân với HTML/CSS', 'ASSIGNMENT', 2, NULL, '{"max_points": 120, "due_date": "2025-12-31T23:59:00Z", "submission_types": ["file"], "instructions": "Tạo website cá nhân responsive với HTML5 và CSS3"}'::json, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('60000000-0000-0000-0000-000000000006', '50000000-0000-0000-0000-000000000004', 'Bài Tập: Todo App React', 'Xây dựng ứng dụng Todo với React', 'ASSIGNMENT', 1, NULL, '{"max_points": 180, "due_date": "2025-12-31T23:59:00Z", "submission_types": ["file"], "instructions": "Tạo Todo App với React hooks, CRUD operations"}'::json, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- DB301 lectures
('60000000-0000-0000-0000-000000000007', '50000000-0000-0000-0000-000000000005', 'Bài Tập: Database Design', 'Thiết kế ERD cho hệ thống bán hàng', 'ASSIGNMENT', 1, NULL, '{"max_points": 140, "due_date": "2025-12-31T23:59:00Z", "submission_types": ["file"], "instructions": "Thiết kế ERD và implement database cho hệ thống e-commerce"}'::json, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('60000000-0000-0000-0000-000000000008', '50000000-0000-0000-0000-000000000006', 'Bài Tập: MongoDB Queries', 'Viết queries phức tạp với MongoDB', 'ASSIGNMENT', 1, NULL, '{"max_points": 130, "due_date": "2025-12-31T23:59:00Z", "submission_types": ["code"], "instructions": "Viết aggregation pipeline và complex queries với MongoDB"}'::json, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- ALGO201 lectures
('60000000-0000-0000-0000-000000000009', '50000000-0000-0000-0000-000000000007', 'Bài Tập: Binary Tree', 'Cài đặt cây nhị phân và các thuật toán duyệt', 'ASSIGNMENT', 1, NULL, '{"max_points": 160, "due_date": "2025-12-31T23:59:00Z", "submission_types": ["code"], "instructions": "Implement Binary Tree với insert, delete, search"}'::json, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('60000000-0000-0000-0000-000000000010', '50000000-0000-0000-0000-000000000008', 'Bài Tập: Sorting Algorithms', 'So sánh hiệu suất các thuật toán sắp xếp', 'ASSIGNMENT', 1, NULL, '{"max_points": 140, "due_date": "2025-12-31T23:59:00Z", "submission_types": ["code"], "instructions": "Implement và benchmark Quick Sort, Merge Sort, Heap Sort"}'::json, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- MOBILE101 lectures
('60000000-0000-0000-0000-000000000011', '50000000-0000-0000-0000-000000000009', 'Bài Tập: Weather App', 'Ứng dụng thời tiết với API integration', 'ASSIGNMENT', 1, NULL, '{"max_points": 170, "due_date": "2025-12-31T23:59:00Z", "submission_types": ["file"], "instructions": "Tạo ứng dụng thời tiết với React Native, API call, navigation"}'::json, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('60000000-0000-0000-0000-000000000012', '50000000-0000-0000-0000-000000000010', 'Bài Tập: Flutter Quiz App', 'Quiz app với Flutter và local storage', 'ASSIGNMENT', 1, NULL, '{"max_points": 160, "due_date": "2025-12-31T23:59:00Z", "submission_types": ["file"], "instructions": "Tạo quiz app với Flutter, SQLite local database"}'::json, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- CLOUD301 lectures
('60000000-0000-0000-0000-000000000013', '50000000-0000-0000-0000-000000000011', 'Bài Tập: AWS Infrastructure', 'Deploy web app lên AWS với EC2, RDS', 'ASSIGNMENT', 1, NULL, '{"max_points": 200, "due_date": "2025-12-31T23:59:00Z", "submission_types": ["file"], "instructions": "Deploy full-stack app lên AWS sử dụng EC2, RDS, S3"}'::json, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('60000000-0000-0000-0000-000000000014', '50000000-0000-0000-0000-000000000012', 'Bài Tập: Serverless API', 'Xây dựng API serverless với Lambda', 'ASSIGNMENT', 1, NULL, '{"max_points": 180, "due_date": "2025-12-31T23:59:00Z", "submission_types": ["code"], "instructions": "Tạo REST API với AWS Lambda, API Gateway, DynamoDB"}'::json, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- AI201 lectures
('60000000-0000-0000-0000-000000000015', '50000000-0000-0000-0000-000000000013', 'Bài Tập: Linear Regression', 'Xây dựng mô hình dự đoán giá nhà', 'ASSIGNMENT', 1, NULL, '{"max_points": 150, "due_date": "2025-12-31T23:59:00Z", "submission_types": ["code"], "instructions": "Implement linear regression từ scratch và với scikit-learn"}'::json, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('60000000-0000-0000-0000-000000000016', '50000000-0000-0000-0000-000000000014', 'Bài Tập: Image Classification CNN', 'Phân loại ảnh với Convolutional Neural Network', 'ASSIGNMENT', 1, NULL, '{"max_points": 190, "due_date": "2025-12-31T23:59:00Z", "submission_types": ["code"], "instructions": "Xây dựng CNN model cho image classification với TensorFlow"}'::json, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- SEC301 lectures
('60000000-0000-0000-0000-000000000017', '50000000-0000-0000-0000-000000000015', 'Bài Tập: Secure Web App', 'Implement bảo mật cho web application', 'ASSIGNMENT', 1, NULL, '{"max_points": 170, "due_date": "2025-12-31T23:59:00Z", "submission_types": ["file"], "instructions": "Secure web app: implement authentication, authorization, input validation"}'::json, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('60000000-0000-0000-0000-000000000018', '50000000-0000-0000-0000-000000000016', 'Bài Tập: JWT Authentication', 'Hệ thống xác thực với JWT và refresh tokens', 'ASSIGNMENT', 1, NULL, '{"max_points": 160, "due_date": "2025-12-31T23:59:00Z", "submission_types": ["code"], "instructions": "Implement JWT auth system với access/refresh tokens, role-based access"}'::json, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- DEVOPS201 lectures
('60000000-0000-0000-0000-000000000019', '50000000-0000-0000-0000-000000000017', 'Bài Tập: Docker Multi-Container', 'Containerize full-stack application', 'ASSIGNMENT', 1, NULL, '{"max_points": 150, "due_date": "2025-12-31T23:59:00Z", "submission_types": ["file"], "instructions": "Dockerize frontend, backend, database với Docker Compose"}'::json, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('60000000-0000-0000-0000-000000000020', '50000000-0000-0000-0000-000000000018', 'Bài Tập: CI/CD Pipeline', 'Thiết lập pipeline tự động deploy', 'ASSIGNMENT', 1, NULL, '{"max_points": 180, "due_date": "2025-12-31T23:59:00Z", "submission_types": ["file"], "instructions": "Setup GitLab CI/CD pipeline: test, build, deploy to staging/production"}'::json, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- UI101 lectures
('60000000-0000-0000-0000-000000000021', '50000000-0000-0000-0000-000000000019', 'Bài Tập: Mobile App UI Design', 'Thiết kế giao diện ứng dụng mobile', 'ASSIGNMENT', 1, NULL, '{"max_points": 130, "due_date": "2025-12-31T23:59:00Z", "submission_types": ["file"], "instructions": "Design complete mobile app UI/UX với Figma: wireframes, prototypes, style guide"}'::json, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('60000000-0000-0000-0000-000000000022', '50000000-0000-0000-0000-000000000020', 'Bài Tập: User Experience Research', 'Nghiên cứu và phân tích trải nghiệm người dùng', 'ASSIGNMENT', 1, NULL, '{"max_points": 140, "due_date": "2025-12-31T23:59:00Z", "submission_types": ["file"], "instructions": "Conduct user research, create personas, user journey maps, usability testing report"}'::json, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- --------------------------------------------
-- 2.4. Resources (3 sample resources)
-- --------------------------------------------

INSERT INTO "Resource" (resource_id, lecture_id, title, file_url, file_type, file_size_bytes, created_at) VALUES
('70000000-0000-0000-0000-000000000001', '60000000-0000-0000-0000-000000000001', 'Java Installation Guide', 'https://s3.amazonaws.com/blearning/docs/java-install.pdf', 'application/pdf', 2458624, CURRENT_TIMESTAMP),
('70000000-0000-0000-0000-000000000002', '60000000-0000-0000-0000-000000000001', 'JDK Setup Video', 'https://s3.amazonaws.com/blearning/videos/jdk-setup.mp4', 'video/mp4', 125829120, CURRENT_TIMESTAMP),
('70000000-0000-0000-0000-000000000003', '60000000-0000-0000-0000-000000000004', 'HTML5 Reference', 'https://s3.amazonaws.com/blearning/docs/html5-ref.pdf', 'application/pdf', 1835008, CURRENT_TIMESTAMP);

-- ============================================
-- SECTION 3: ASSESSMENT
-- ============================================

-- --------------------------------------------
-- 3.1. Questions (6 questions for quiz)
-- --------------------------------------------

INSERT INTO "Question" (question_id, course_id, text, type, difficulty, max_points, explanation, is_active, created_by, created_at, updated_at) VALUES
(
  '80000000-0000-0000-0000-000000000001',
  '40000000-0000-0000-0000-000000000001',
  'Java là ngôn ngữ lập trình thuộc loại nào?',
  'MCQ',
  'EASY',
  10,
  'Java là ngôn ngữ lập trình hướng đối tượng (OOP) phổ biến nhất thế giới',
  true,
  '20000000-0000-0000-0000-000000000002',
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP
),
(
  '80000000-0000-0000-0000-000000000002',
  '40000000-0000-0000-0000-000000000001',
  'Keyword nào dùng để kế thừa class trong Java?',
  'MCQ',
  'EASY',
  10,
  'Keyword "extends" được sử dụng để kế thừa class trong Java',
  true,
  '20000000-0000-0000-0000-000000000002',
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP
),
(
  '80000000-0000-0000-0000-000000000003',
  '40000000-0000-0000-0000-000000000001',
  'Java hỗ trợ đa kế thừa (multiple inheritance) qua class?',
  'TRUE_FALSE',
  'MEDIUM',
  5,
  'Java không hỗ trợ đa kế thừa qua class để tránh Diamond Problem. Thay vào đó dùng interface',
  true,
  '20000000-0000-0000-0000-000000000002',
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP
),
(
  '80000000-0000-0000-0000-000000000004',
  '40000000-0000-0000-0000-000000000001',
  'Giải thích sự khác biệt giữa abstract class và interface trong Java (Java 8+)',
  'ESSAY',
  'HARD',
  20,
  'Từ Java 8, cả abstract class và interface đều có thể chứa implementation. Tuy nhiên abstract class có thể có constructor, state (fields), và một class chỉ extend được 1 abstract class nhưng implement nhiều interfaces',
  true,
  '20000000-0000-0000-0000-000000000002',
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP
),
(
  '80000000-0000-0000-0000-000000000002',
  '40000000-0000-0000-0000-000000000002',
  'HTML là viết tắt của gì?',
  'SHORT_ANSWER',
  'EASY',
  5,
  'HyperText Markup Language',
  true,
  '20000000-0000-0000-0000-000000000002',
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP
),
(
  '80000000-0000-0000-0000-000000000006',
  '40000000-0000-0000-0000-000000000002',
  'CSS Grid tốt hơn Flexbox trong mọi tình huống?',
  'TRUE_FALSE',
  'MEDIUM',
  5,
  'Sai. Grid tốt cho 2D layout, Flexbox tốt cho 1D layout. Tùy use case',
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
('90000000-0000-0000-0000-000000000001', '80000000-0000-0000-0000-000000000001', 'Hướng đối tượng (OOP)', true, 1, 'Chính xác! Java là ngôn ngữ OOP'),
('90000000-0000-0000-0000-000000000002', '80000000-0000-0000-0000-000000000001', 'Thủ tục (Procedural)', false, 2, 'Sai. Java không phải procedural language'),
('90000000-0000-0000-0000-000000000003', '80000000-0000-0000-0000-000000000001', 'Hàm (Functional)', false, 3, 'Java có hỗ trợ functional programming từ Java 8 nhưng không phải functional language'),
('90000000-0000-0000-0000-000000000004', '80000000-0000-0000-0000-000000000001', 'Logic', false, 4, 'Sai. Prolog mới là logic programming language');

-- Question 2 options
INSERT INTO "Option" (option_id, question_id, option_text, is_correct, order_num, feedback) VALUES
('90000000-0000-0000-0000-000000000011', '80000000-0000-0000-0000-000000000002', 'extends', true, 1, 'Đúng! extends dùng để kế thừa class'),
('90000000-0000-0000-0000-000000000012', '80000000-0000-0000-0000-000000000002', 'implements', false, 2, 'implements dùng cho interface, không phải class'),
('90000000-0000-0000-0000-000000000013', '80000000-0000-0000-0000-000000000002', 'inherits', false, 3, 'Không có keyword này trong Java'),
('90000000-0000-0000-0000-000000000014', '80000000-0000-0000-0000-000000000002', 'super', false, 4, 'super dùng để gọi parent class, không phải keyword kế thừa');

-- Question 3 options (TRUE_FALSE)
INSERT INTO "Option" (option_id, question_id, option_text, is_correct, order_num, feedback) VALUES
('90000000-0000-0000-0000-000000000021', '80000000-0000-0000-0000-000000000003', 'True', false, 1, 'Sai. Java không hỗ trợ multiple inheritance qua class'),
('90000000-0000-0000-0000-000000000022', '80000000-0000-0000-0000-000000000003', 'False', true, 2, 'Đúng! Java dùng interface thay vì multiple inheritance');

-- Question 6 options (TRUE_FALSE)
INSERT INTO "Option" (option_id, question_id, option_text, is_correct, order_num, feedback) VALUES
('90000000-0000-0000-0000-000000000031', '80000000-0000-0000-0000-000000000006', 'True', false, 1, 'Sai'),
('90000000-0000-0000-0000-000000000032', '80000000-0000-0000-0000-000000000006', 'False', true, 2, 'Đúng!');

-- --------------------------------------------
-- 3.3. Quizzes (2 quizzes)
-- --------------------------------------------

INSERT INTO "Quiz" (quiz_id, course_id, title, description, time_limit_minutes, pass_score, questions, shuffle_questions, show_correct_answers, is_published, created_by, created_at, updated_at) VALUES
(
  'a0000000-0000-0000-0000-000000000001',
  '40000000-0000-0000-0000-000000000001',
  'Kiểm tra giữa kỳ - Java Basics',
  'Bài kiểm tra kiến thức cơ bản về Java và OOP. Thời gian: 30 phút. Điểm đạt: 70%',
  30,
  70.00,
  '[
    {"question_id": "80000000-0000-0000-0000-000000000001", "points": 10, "order": 1},
    {"question_id": "80000000-0000-0000-0000-000000000002", "points": 10, "order": 2},
    {"question_id": "80000000-0000-0000-0000-000000000003", "points": 5, "order": 3},
    {"question_id": "80000000-0000-0000-0000-000000000004", "points": 20, "order": 4}
  ]'::json,
  false,
  true,
  true,
  '20000000-0000-0000-0000-000000000002',
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP
),
(
  'a0000000-0000-0000-0000-000000000002',
  '40000000-0000-0000-0000-000000000002',
  'HTML/CSS Fundamentals Quiz',
  'Kiểm tra kiến thức HTML và CSS cơ bản',
  20,
  60.00,
  '[
    {"question_id": "80000000-0000-0000-0000-000000000005", "points": 5, "order": 1},
    {"question_id": "80000000-0000-0000-0000-000000000006", "points": 5, "order": 2}
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
-- 4.1. Classes (2 classes)
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
  'Phòng A101',
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
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP
),
(
  'b0000000-0000-0000-0000-000000000002',
  '40000000-0000-0000-0000-000000000002',
  '20000000-0000-0000-0000-000000000002',
  'WEB201-K63-02',
  '2025-02-01',
  '2025-06-30',
  'SCHEDULED',
  25,
  'Phòng B202',
  '[]'::json,
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP
);

-- --------------------------------------------
-- 4.2. Enrollments (5 enrollments - mix of class-based and self-paced)
-- --------------------------------------------

-- Class-based enrollments (blended learning)
INSERT INTO "Enrollment" (enrollment_id, user_id, course_id, class_id, role, status, enrolled_at, created_at) VALUES
('c0000000-0000-0000-0000-000000000001', '20000000-0000-0000-0000-000000000101', '40000000-0000-0000-0000-000000000001', 'b0000000-0000-0000-0000-000000000001', 'STUDENT', 'ACTIVE', CURRENT_TIMESTAMP - INTERVAL '30 days', CURRENT_TIMESTAMP),
('c0000000-0000-0000-0000-000000000002', '20000000-0000-0000-0000-000000000102', '40000000-0000-0000-0000-000000000001', 'b0000000-0000-0000-0000-000000000001', 'STUDENT', 'ACTIVE', CURRENT_TIMESTAMP - INTERVAL '30 days', CURRENT_TIMESTAMP),
('c0000000-0000-0000-0000-000000000003', '20000000-0000-0000-0000-000000000103', '40000000-0000-0000-0000-000000000001', 'b0000000-0000-0000-0000-000000000001', 'STUDENT', 'ACTIVE', CURRENT_TIMESTAMP - INTERVAL '30 days', CURRENT_TIMESTAMP);

-- Self-paced enrollments (class_id = NULL)
INSERT INTO "Enrollment" (enrollment_id, user_id, course_id, class_id, role, status, enrolled_at, created_at) VALUES
('c0000000-0000-0000-0000-000000000011', '20000000-0000-0000-0000-000000000104', '40000000-0000-0000-0000-000000000002', NULL, 'STUDENT', 'ACTIVE', CURRENT_TIMESTAMP - INTERVAL '15 days', CURRENT_TIMESTAMP),
('c0000000-0000-0000-0000-000000000012', '20000000-0000-0000-0000-000000000105', '40000000-0000-0000-0000-000000000003', NULL, 'STUDENT', 'ACTIVE', CURRENT_TIMESTAMP - INTERVAL '20 days', CURRENT_TIMESTAMP),
('c0000000-0000-0000-0000-000000000013', '20000000-0000-0000-0000-000000000101', '40000000-0000-0000-0000-000000000002', NULL, 'STUDENT', 'ACTIVE', CURRENT_TIMESTAMP - INTERVAL '25 days', CURRENT_TIMESTAMP),
('c0000000-0000-0000-0000-000000000014', '20000000-0000-0000-0000-000000000102', '40000000-0000-0000-0000-000000000002', NULL, 'STUDENT', 'ACTIVE', CURRENT_TIMESTAMP - INTERVAL '22 days', CURRENT_TIMESTAMP),
('c0000000-0000-0000-0000-000000000015', '20000000-0000-0000-0000-000000000103', '40000000-0000-0000-0000-000000000004', NULL, 'STUDENT', 'ACTIVE', CURRENT_TIMESTAMP - INTERVAL '18 days', CURRENT_TIMESTAMP);

-- --------------------------------------------
-- 4.3. Progress Tracking (6 progress records)
-- --------------------------------------------

-- Student 1 (Minh) progress in CS101
INSERT INTO "Progress" (progress_id, user_id, course_id, module_id, status, started_at, completed_at, created_at) VALUES
('d0000000-0000-0000-0000-000000000001', '20000000-0000-0000-0000-000000000101', '40000000-0000-0000-0000-000000000001', '50000000-0000-0000-0000-000000000001', 'COMPLETED', CURRENT_TIMESTAMP - INTERVAL '25 days', CURRENT_TIMESTAMP - INTERVAL '18 days', CURRENT_TIMESTAMP),
('d0000000-0000-0000-0000-000000000002', '20000000-0000-0000-0000-000000000101', '40000000-0000-0000-0000-000000000001', '50000000-0000-0000-0000-000000000002', 'IN_PROGRESS', CURRENT_TIMESTAMP - INTERVAL '18 days', NULL, CURRENT_TIMESTAMP);

-- Student 2 (Huong) progress in CS101
INSERT INTO "Progress" (progress_id, user_id, course_id, module_id, status, started_at, completed_at, created_at) VALUES
('d0000000-0000-0000-0000-000000000011', '20000000-0000-0000-0000-000000000102', '40000000-0000-0000-0000-000000000001', '50000000-0000-0000-0000-000000000001', 'COMPLETED', CURRENT_TIMESTAMP - INTERVAL '25 days', CURRENT_TIMESTAMP - INTERVAL '20 days', CURRENT_TIMESTAMP),
('d0000000-0000-0000-0000-000000000012', '20000000-0000-0000-0000-000000000102', '40000000-0000-0000-0000-000000000001', '50000000-0000-0000-0000-000000000002', 'IN_PROGRESS', CURRENT_TIMESTAMP - INTERVAL '15 days', NULL, CURRENT_TIMESTAMP);

-- Student 4 (Hoa) progress in WEB201
INSERT INTO "Progress" (progress_id, user_id, course_id, module_id, status, started_at, created_at) VALUES
('d0000000-0000-0000-0000-000000000021', '20000000-0000-0000-0000-000000000104', '40000000-0000-0000-0000-000000000002', '50000000-0000-0000-0000-000000000003', 'IN_PROGRESS', CURRENT_TIMESTAMP - INTERVAL '10 days', CURRENT_TIMESTAMP);

-- Student 5 (Dung) progress in DB301
INSERT INTO "Progress" (progress_id, user_id, course_id, module_id, status, started_at, created_at) VALUES
('d0000000-0000-0000-0000-000000000031', '20000000-0000-0000-0000-000000000105', '40000000-0000-0000-0000-000000000003', '50000000-0000-0000-0000-000000000005', 'NOT_STARTED', NULL, CURRENT_TIMESTAMP);

-- ============================================
-- SECTION 5: QUIZ ATTEMPTS & ASSIGNMENTS
-- ============================================

-- --------------------------------------------
-- 5.1. Quiz Attempts (2 attempts)
-- --------------------------------------------

-- Student 1 (Minh) - Attempt 1 (completed and graded)
INSERT INTO "Attempt" (attempt_id, quiz_id, user_id, enrollment_id, attempt_number, status, started_at, submitted_at, total_score, max_possible_score, percentage_score, graded_at, answers, created_at) VALUES
(
  'e0000000-0000-0000-0000-000000000001',
  'a0000000-0000-0000-0000-000000000001',
  '20000000-0000-0000-0000-000000000101',
  'c0000000-0000-0000-0000-000000000001',
  1,
  'SUBMITTED',
  CURRENT_TIMESTAMP - INTERVAL '10 days',
  CURRENT_TIMESTAMP - INTERVAL '10 days' + INTERVAL '25 minutes',
  38.5,
  45.00,
  85.56,
  CURRENT_TIMESTAMP - INTERVAL '10 days' + INTERVAL '1 hour',
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
    },
    {
      "question_id": "80000000-0000-0000-0000-000000000004",
      "answer_text": "Abstract class có thể có methods với implementation, trong khi interface trước Java 8 chỉ có method signatures. Abstract class có thể có constructor, interface không. Một class chỉ extend được 1 abstract class nhưng implement nhiều interfaces.",
      "selected_options": null,
      "score": 13.5,
      "max_score": 20,
      "is_correct": null
    }
  ]'::json,
  CURRENT_TIMESTAMP
);

-- Student 2 (Huong) - Attempt 1 (in progress)
INSERT INTO "Attempt" (attempt_id, quiz_id, user_id, enrollment_id, attempt_number, status, started_at, submitted_at, total_score, max_possible_score, answers, created_at) VALUES
(
  'e0000000-0000-0000-0000-000000000002',
  'a0000000-0000-0000-0000-000000000001',
  '20000000-0000-0000-0000-000000000102',
  'c0000000-0000-0000-0000-000000000002',
  1,
  'IN_PROGRESS',
  CURRENT_TIMESTAMP - INTERVAL '1 hour',
  NULL,
  NULL,
  45.00,
  NULL,
  CURRENT_TIMESTAMP
);

-- --------------------------------------------
-- 5.2. Assignment Submissions (10 submissions)
-- --------------------------------------------

INSERT INTO "AssignmentSubmission" (submission_id, lecture_id, user_id, enrollment_id, submission_number, status, submitted_at, score, max_score, feedback, file_urls, created_at) VALUES
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
  'Code chạy tốt, logic đúng. Cần improve code documentation.',
  '["https://s3.amazonaws.com/blearning/submissions/minh-le-hello-world.zip"]'::json,
  CURRENT_TIMESTAMP
),

-- Student 2 (Huong) - Java Hello World (submitted)
(
  'f0000000-0000-0000-0000-000000000002',
  '60000000-0000-0000-0000-000000000002',
  '20000000-0000-0000-0000-000000000102',
  'c0000000-0000-0000-0000-000000000002',
  1,
  'SUBMITTED',
  CURRENT_TIMESTAMP - INTERVAL '2 days',
  NULL,
  NULL,
  NULL,
  '["https://s3.amazonaws.com/blearning/submissions/huong-pham-hello.java"]'::json,
  CURRENT_TIMESTAMP
),

-- Student 3 (Tuan) - Java OOP Calculator (graded)
(
  'f0000000-0000-0000-0000-000000000003',
  '60000000-0000-0000-0000-000000000003',
  '20000000-0000-0000-0000-000000000103',
  'c0000000-0000-0000-0000-000000000003',
  1,
  'GRADED',
  CURRENT_TIMESTAMP - INTERVAL '5 days',
  142.00,
  150.00,
  'Xuất sắc! Code clean và có error handling tốt',
  '["https://s3.amazonaws.com/blearning/submissions/tuan-vo-calculator.zip"]'::json,
  CURRENT_TIMESTAMP
),

-- Student 4 (Hoa) - Personal Website (graded)
(
  'f0000000-0000-0000-0000-000000000004',
  '60000000-0000-0000-0000-000000000005',
  '20000000-0000-0000-0000-000000000104',
  'c0000000-0000-0000-0000-000000000011',
  1,
  'GRADED',
  CURRENT_TIMESTAMP - INTERVAL '8 days',
  110.00,
  120.00,
  'Website đẹp và responsive. Cần cải thiện semantic HTML',
  '["https://s3.amazonaws.com/blearning/submissions/hoa-nguyen-website.zip"]'::json,
  CURRENT_TIMESTAMP
),

-- Student 5 (Dung) - Database Design (submitted)
(
  'f0000000-0000-0000-0000-000000000005',
  '60000000-0000-0000-0000-000000000007',
  '20000000-0000-0000-0000-000000000105',
  'c0000000-0000-0000-0000-000000000012',
  1,
  'SUBMITTED',
  CURRENT_TIMESTAMP - INTERVAL '3 days',
  NULL,
  NULL,
  NULL,
  '["https://s3.amazonaws.com/blearning/submissions/dung-tran-erd.pdf"]'::json,
  CURRENT_TIMESTAMP
),

-- Student 1 (Minh) - Personal Website (graded)
(
  'f0000000-0000-0000-0000-000000000006',
  '60000000-0000-0000-0000-000000000005',
  '20000000-0000-0000-0000-000000000101',
  'c0000000-0000-0000-0000-000000000013',
  1,
  'GRADED',
  CURRENT_TIMESTAMP - INTERVAL '15 days',
  115.00,
  120.00,
  'Website đẹp, responsive tốt. Cần thêm accessibility features.',
  '["https://s3.amazonaws.com/blearning/submissions/minh-le-portfolio.zip"]'::json,
  CURRENT_TIMESTAMP
),

-- Student 2 (Huong) - Personal Website (submitted - late)
(
  'f0000000-0000-0000-0000-000000000007',
  '60000000-0000-0000-0000-000000000005',
  '20000000-0000-0000-0000-000000000102',
  'c0000000-0000-0000-0000-000000000014',
  1,
  'SUBMITTED',
  CURRENT_TIMESTAMP - INTERVAL '1 day',
  NULL,
  NULL,
  NULL,
  '["https://s3.amazonaws.com/blearning/submissions/huong-pham-website.zip"]'::json,
  CURRENT_TIMESTAMP
),

-- Student 1 (Minh) - Todo App React (graded - excellent)
(
  'f0000000-0000-0000-0000-000000000008',
  '60000000-0000-0000-0000-000000000006',
  '20000000-0000-0000-0000-000000000101',
  'c0000000-0000-0000-0000-000000000013',
  1,
  'GRADED',
  CURRENT_TIMESTAMP - INTERVAL '10 days',
  175.00,
  180.00,
  'Xuất sắc! Code structure tốt, sử dụng hooks đúng cách, có unit tests.',
  '["https://s3.amazonaws.com/blearning/submissions/minh-le-todo-app.zip"]'::json,
  CURRENT_TIMESTAMP
),

-- Student 4 (Hoa) - Todo App React (grading)
(
  'f0000000-0000-0000-0000-000000000009',
  '60000000-0000-0000-0000-000000000006',
  '20000000-0000-0000-0000-000000000104',
  'c0000000-0000-0000-0000-000000000011',
  1,
  'GRADING',
  CURRENT_TIMESTAMP - INTERVAL '4 days',
  NULL,
  NULL,
  NULL,
  '["https://s3.amazonaws.com/blearning/submissions/hoa-nguyen-todo.zip"]'::json,
  CURRENT_TIMESTAMP
),

-- Student 3 (Tuan) - Data Structures Assignment (graded)
(
  'f0000000-0000-0000-0000-000000000010',
  '60000000-0000-0000-0000-000000000008',
  '20000000-0000-0000-0000-000000000103',
  'c0000000-0000-0000-0000-000000000015',
  1,
  'GRADED',
  CURRENT_TIMESTAMP - INTERVAL '7 days',
  88.00,
  100.00,
  'Implement Binary Tree tốt. Cần optimize time complexity ở search function.',
  '["https://s3.amazonaws.com/blearning/submissions/tuan-vo-binary-tree.java"]'::json,
  CURRENT_TIMESTAMP
),

-- Student 2 (Huong) - Java OOP Calculator (submitted)
(
  'f0000000-0000-0000-0000-000000000011',
  '60000000-0000-0000-0000-000000000003',
  '20000000-0000-0000-0000-000000000102',
  'c0000000-0000-0000-0000-000000000002',
  1,
  'SUBMITTED',
  CURRENT_TIMESTAMP - INTERVAL '1 day',
  NULL,
  NULL,
  NULL,
  '["https://s3.amazonaws.com/blearning/submissions/huong-pham-calculator.zip"]'::json,
  CURRENT_TIMESTAMP
),

-- Student 1 (Minh) - Java OOP Calculator (graded)
(
  'f0000000-0000-0000-0000-000000000012',
  '60000000-0000-0000-0000-000000000003',
  '20000000-0000-0000-0000-000000000101',
  'c0000000-0000-0000-0000-000000000001',
  1,
  'GRADED',
  CURRENT_TIMESTAMP - INTERVAL '14 days',
  148.00,
  150.00,
  'Tốt lắm! Có exception handling và validation đầy đủ.',
  '["https://s3.amazonaws.com/blearning/submissions/minh-le-calculator.zip"]'::json,
  CURRENT_TIMESTAMP
);

-- ============================================
-- SECTION 6: CERTIFICATES
-- ============================================

-- Student 1 completed course and earned certificate
INSERT INTO "Certificate" (certificate_id, user_id, course_id, certificate_code, verification_code, issue_date, final_grade, pdf_url, status, created_at) VALUES
(
  'g0000000-0000-0000-0000-000000000001',
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
  RAISE NOTICE '- 10 diverse courses with realistic content';
  RAISE NOTICE '- 22 lectures including assignments';
  RAISE NOTICE '- Quiz system with multiple question types';
  RAISE NOTICE '- Both class-based and self-paced learning';
  RAISE NOTICE '- Progress tracking and grading';
  RAISE NOTICE '- Certificate issuance';
  RAISE NOTICE '========================================';
END $$;

-- ============================================
-- END OF SEED DATA
-- ============================================
