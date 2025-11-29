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

-- Insert 10 Courses for comprehensive demo
INSERT INTO "Course" (course_id, code, title, description, difficulty_level, status, created_by, published_at) VALUES
('40000000-0000-0000-0000-000000000001', 'JAVA101', 'Lập Trình Java Cơ Bản', 'Học các khái niệm cơ bản về lập trình Java và OOP', 'BEGINNER', 'PUBLISHED', '20000000-0000-0000-0000-000000000102', CURRENT_TIMESTAMP),
('40000000-0000-0000-0000-000000000002', 'WEB201', 'Phát Triển Web Frontend', 'Thành thạo HTML, CSS, JavaScript và React', 'INTERMEDIATE', 'PUBLISHED', '20000000-0000-0000-0000-000000000102', CURRENT_TIMESTAMP),
('40000000-0000-0000-0000-000000000003', 'DB301', 'Cơ Sở Dữ Liệu Nâng Cao', 'PostgreSQL, MongoDB, thiết kế database và tối ưu hóa', 'ADVANCED', 'PUBLISHED', '20000000-0000-0000-0000-000000000102', CURRENT_TIMESTAMP),
('40000000-0000-0000-0000-000000000004', 'ALGO201', 'Cấu Trúc Dữ Liệu & Giải Thuật', 'Arrays, Linked Lists, Trees, Sorting, Searching algorithms', 'INTERMEDIATE', 'PUBLISHED', '20000000-0000-0000-0000-000000000102', CURRENT_TIMESTAMP),
('40000000-0000-0000-0000-000000000005', 'MOBILE101', 'Phát Triển Ứng Dụng Mobile', 'React Native và Flutter cho iOS/Android', 'BEGINNER', 'PUBLISHED', '20000000-0000-0000-0000-000000000102', CURRENT_TIMESTAMP),
('40000000-0000-0000-0000-000000000006', 'CLOUD301', 'Cloud Computing AWS', 'Amazon Web Services, EC2, S3, Lambda, RDS', 'ADVANCED', 'PUBLISHED', '20000000-0000-0000-0000-000000000102', CURRENT_TIMESTAMP),
('40000000-0000-0000-0000-000000000007', 'AI201', 'Trí Tuệ Nhân Tạo Cơ Bản', 'Machine Learning, Neural Networks với Python', 'INTERMEDIATE', 'PUBLISHED', '20000000-0000-0000-0000-000000000102', CURRENT_TIMESTAMP),
('40000000-0000-0000-0000-000000000008', 'SEC301', 'Bảo Mật Ứng Dụng Web', 'OWASP Top 10, Authentication, Authorization, HTTPS', 'ADVANCED', 'PUBLISHED', '20000000-0000-0000-0000-000000000102', CURRENT_TIMESTAMP),
('40000000-0000-0000-0000-000000000009', 'DEVOPS201', 'DevOps và CI/CD', 'Docker, Kubernetes, Jenkins, GitLab CI/CD pipeline', 'INTERMEDIATE', 'PUBLISHED', '20000000-0000-0000-0000-000000000102', CURRENT_TIMESTAMP),
('40000000-0000-0000-0000-000000000010', 'UI101', 'UI/UX Design Fundamentals', 'Figma, Adobe XD, Design principles, User research', 'BEGINNER', 'PUBLISHED', '20000000-0000-0000-0000-000000000102', CURRENT_TIMESTAMP);

-- Insert Modules for all 10 courses
INSERT INTO "Module" (module_id, course_id, title, description, order_num) VALUES
-- JAVA101 modules
('50000000-0000-0000-0000-000000000001', '40000000-0000-0000-0000-000000000001', 'Cơ Bản Java', 'Cú pháp, biến, vòng lặp, điều kiện', 1),
('50000000-0000-0000-0000-000000000002', '40000000-0000-0000-0000-000000000001', 'Lập Trình Hướng Đối Tượng', 'Class, Object, Inheritance, Polymorphism', 2),
-- WEB201 modules
('50000000-0000-0000-0000-000000000003', '40000000-0000-0000-0000-000000000002', 'HTML & CSS Cơ Bản', 'Cấu trúc HTML và styling với CSS', 1),
('50000000-0000-0000-0000-000000000004', '40000000-0000-0000-0000-000000000002', 'JavaScript & React', 'JS ES6 và React components', 2),
-- DB301 modules
('50000000-0000-0000-0000-000000000005', '40000000-0000-0000-0000-000000000003', 'SQL Nâng Cao', 'Complex queries, indexing, optimization', 1),
('50000000-0000-0000-0000-000000000006', '40000000-0000-0000-0000-000000000003', 'NoSQL & MongoDB', 'Document databases và aggregation', 2),
-- ALGO201 modules
('50000000-0000-0000-0000-000000000007', '40000000-0000-0000-0000-000000000004', 'Cấu Trúc Dữ Liệu', 'Arrays, LinkedList, Stack, Queue, Trees', 1),
('50000000-0000-0000-0000-000000000008', '40000000-0000-0000-0000-000000000004', 'Giải Thuật Sắp Xếp', 'Bubble, Quick, Merge sort algorithms', 2),
-- MOBILE101 modules
('50000000-0000-0000-0000-000000000009', '40000000-0000-0000-0000-000000000005', 'React Native Basics', 'Components, Navigation, State management', 1),
('50000000-0000-0000-0000-000000000010', '40000000-0000-0000-0000-000000000005', 'Flutter Development', 'Dart language và Widget system', 2),
-- CLOUD301 modules
('50000000-0000-0000-0000-000000000011', '40000000-0000-0000-0000-000000000006', 'AWS Core Services', 'EC2, S3, VPC, RDS setup and management', 1),
('50000000-0000-0000-0000-000000000012', '40000000-0000-0000-0000-000000000006', 'Serverless Architecture', 'Lambda functions và API Gateway', 2),
-- AI201 modules
('50000000-0000-0000-0000-000000000013', '40000000-0000-0000-0000-000000000007', 'Machine Learning Cơ Bản', 'Supervised/Unsupervised learning, algorithms', 1),
('50000000-0000-0000-0000-000000000014', '40000000-0000-0000-0000-000000000007', 'Neural Networks', 'Deep learning với TensorFlow/Keras', 2),
-- SEC301 modules
('50000000-0000-0000-0000-000000000015', '40000000-0000-0000-0000-000000000008', 'Web Security Fundamentals', 'OWASP Top 10, XSS, CSRF, SQL Injection', 1),
('50000000-0000-0000-0000-000000000016', '40000000-0000-0000-0000-000000000008', 'Authentication & Authorization', 'JWT, OAuth2, RBAC implementation', 2),
-- DEVOPS201 modules
('50000000-0000-0000-0000-000000000017', '40000000-0000-0000-0000-000000000009', 'Containerization', 'Docker containers và Docker Compose', 1),
('50000000-0000-0000-0000-000000000018', '40000000-0000-0000-0000-000000000009', 'CI/CD Pipelines', 'Jenkins, GitLab CI automated deployment', 2),
-- UI101 modules
('50000000-0000-0000-0000-000000000019', '40000000-0000-0000-0000-000000000010', 'Design Principles', 'Color theory, Typography, Layout principles', 1),
('50000000-0000-0000-0000-000000000020', '40000000-0000-0000-0000-000000000010', 'Prototyping Tools', 'Figma advanced features và user testing', 2);

-- Insert Lectures (including ASSIGNMENT type for submissions)
INSERT INTO "Lecture" (lecture_id, module_id, title, description, type, order_num, assignment_config) VALUES
-- JAVA101 lectures
('60000000-0000-0000-0000-000000000001', '50000000-0000-0000-0000-000000000001', 'Cài Đặt Môi Trường Java', 'Hướng dẫn cài đặt JDK và IDE', 'VIDEO', 1, NULL),
('60000000-0000-0000-0000-000000000002', '50000000-0000-0000-0000-000000000001', 'Bài Tập: Hello World', 'Viết chương trình Java đầu tiên', 'ASSIGNMENT', 2, '{"max_points": 100, "due_date": "2025-12-31", "instructions": "Viết chương trình Hello World bằng Java"}'),
('60000000-0000-0000-0000-000000000003', '50000000-0000-0000-0000-000000000002', 'Bài Tập: OOP Calculator', 'Tạo máy tính sử dụng OOP', 'ASSIGNMENT', 1, '{"max_points": 150, "due_date": "2025-12-31", "instructions": "Tạo lớp Calculator với các phương thức +, -, *, /"}'),
-- WEB201 lectures
('60000000-0000-0000-0000-000000000004', '50000000-0000-0000-0000-000000000003', 'HTML Cơ Bản', 'Cấu trúc HTML và semantic tags', 'VIDEO', 1, NULL),
('60000000-0000-0000-0000-000000000005', '50000000-0000-0000-0000-000000000003', 'Bài Tập: Personal Website', 'Tạo website cá nhân với HTML/CSS', 'ASSIGNMENT', 2, '{"max_points": 120, "due_date": "2025-12-31", "instructions": "Tạo website cá nhân responsive với HTML5 và CSS3"}'),
('60000000-0000-0000-0000-000000000006', '50000000-0000-0000-0000-000000000004', 'Bài Tập: Todo App React', 'Xây dựng ứng dụng Todo với React', 'ASSIGNMENT', 1, '{"max_points": 180, "due_date": "2025-12-31", "instructions": "Tạo Todo App với React hooks, CRUD operations"}'),
-- DB301 lectures
('60000000-0000-0000-0000-000000000007', '50000000-0000-0000-0000-000000000005', 'Bài Tập: Database Design', 'Thiết kế ERD cho hệ thống bán hàng', 'ASSIGNMENT', 1, '{"max_points": 140, "due_date": "2025-12-31", "instructions": "Thiết kế ERD và implement database cho hệ thống e-commerce"}'),
('60000000-0000-0000-0000-000000000008', '50000000-0000-0000-0000-000000000006', 'Bài Tập: MongoDB Queries', 'Viết queries phức tạp với MongoDB', 'ASSIGNMENT', 1, '{"max_points": 130, "due_date": "2025-12-31", "instructions": "Viết aggregation pipeline và complex queries với MongoDB"}'),
-- ALGO201 lectures
('60000000-0000-0000-0000-000000000009', '50000000-0000-0000-0000-000000000007', 'Bài Tập: Binary Tree Implementation', 'Cài đặt cây nhị phân và các thuật toán duyệt', 'ASSIGNMENT', 1, '{"max_points": 160, "due_date": "2025-12-31", "instructions": "Implement Binary Tree với insert, delete, search, và các phương thức duyệt"}'),
('60000000-0000-0000-0000-000000000010', '50000000-0000-0000-0000-000000000008', 'Bài Tập: Sorting Algorithms', 'So sánh hiệu suất các thuật toán sắp xếp', 'ASSIGNMENT', 1, '{"max_points": 140, "due_date": "2025-12-31", "instructions": "Implement và benchmark Quick Sort, Merge Sort, Heap Sort"}'),
-- MOBILE101 lectures
('60000000-0000-0000-0000-000000000011', '50000000-0000-0000-0000-000000000009', 'Bài Tập: Weather App React Native', 'Ứng dụng thời tiết với API integration', 'ASSIGNMENT', 1, '{"max_points": 170, "due_date": "2025-12-31", "instructions": "Tạo ứng dụng thời tiết với React Native, API call, navigation"}'),
('60000000-0000-0000-0000-000000000012', '50000000-0000-0000-0000-000000000010', 'Bài Tập: Flutter Quiz App', 'Quiz app với Flutter và local storage', 'ASSIGNMENT', 1, '{"max_points": 160, "due_date": "2025-12-31", "instructions": "Tạo quiz app với Flutter, SQLite local database"}'),
-- CLOUD301 lectures
('60000000-0000-0000-0000-000000000013', '50000000-0000-0000-0000-000000000011', 'Bài Tập: AWS Infrastructure Setup', 'Deploy web app lên AWS với EC2, RDS', 'ASSIGNMENT', 1, '{"max_points": 200, "due_date": "2025-12-31", "instructions": "Deploy full-stack app lên AWS sử dụng EC2, RDS, S3"}'),
('60000000-0000-0000-0000-000000000014', '50000000-0000-0000-0000-000000000012', 'Bài Tập: Serverless API', 'Xây dựng API serverless với Lambda', 'ASSIGNMENT', 1, '{"max_points": 180, "due_date": "2025-12-31", "instructions": "Tạo REST API với AWS Lambda, API Gateway, DynamoDB"}'),
-- AI201 lectures
('60000000-0000-0000-0000-000000000015', '50000000-0000-0000-0000-000000000013', 'Bài Tập: Linear Regression Model', 'Xây dựng mô hình dự đoán giá nhà', 'ASSIGNMENT', 1, '{"max_points": 150, "due_date": "2025-12-31", "instructions": "Implement linear regression từ scratch và với scikit-learn"}'),
('60000000-0000-0000-0000-000000000016', '50000000-0000-0000-0000-000000000014', 'Bài Tập: Image Classification CNN', 'Phân loại ảnh với Convolutional Neural Network', 'ASSIGNMENT', 1, '{"max_points": 190, "due_date": "2025-12-31", "instructions": "Xây dựng CNN model cho image classification với TensorFlow"}'),
-- SEC301 lectures
('60000000-0000-0000-0000-000000000017', '50000000-0000-0000-0000-000000000015', 'Bài Tập: Secure Web App', 'Implement bảo mật cho web application', 'ASSIGNMENT', 1, '{"max_points": 170, "due_date": "2025-12-31", "instructions": "Secure web app: implement authentication, authorization, input validation"}'),
('60000000-0000-0000-0000-000000000018', '50000000-0000-0000-0000-000000000016', 'Bài Tập: JWT Authentication System', 'Hệ thống xác thực với JWT và refresh tokens', 'ASSIGNMENT', 1, '{"max_points": 160, "due_date": "2025-12-31", "instructions": "Implement JWT auth system với access/refresh tokens, role-based access"}'),
-- DEVOPS201 lectures
('60000000-0000-0000-0000-000000000019', '50000000-0000-0000-0000-000000000017', 'Bài Tập: Docker Multi-Container App', 'Containerize full-stack application', 'ASSIGNMENT', 1, '{"max_points": 150, "due_date": "2025-12-31", "instructions": "Dockerize frontend, backend, database với Docker Compose"}'),
('60000000-0000-0000-0000-000000000020', '50000000-0000-0000-0000-000000000018', 'Bài Tập: CI/CD Pipeline Setup', 'Thiết lập pipeline tự động deploy', 'ASSIGNMENT', 1, '{"max_points": 180, "due_date": "2025-12-31", "instructions": "Setup GitLab CI/CD pipeline: test, build, deploy to staging/production"}'),
-- UI101 lectures
('60000000-0000-0000-0000-000000000021', '50000000-0000-0000-0000-000000000019', 'Bài Tập: Mobile App UI Design', 'Thiết kế giao diện ứng dụng mobile', 'ASSIGNMENT', 1, '{"max_points": 130, "due_date": "2025-12-31", "instructions": "Design complete mobile app UI/UX với Figma: wireframes, prototypes, style guide"}'),
('60000000-0000-0000-0000-000000000022', '50000000-0000-0000-0000-000000000020', 'Bài Tập: User Experience Research', 'Nghiên cứu và phân tích trải nghiệm người dùng', 'ASSIGNMENT', 1, '{"max_points": 140, "due_date": "2025-12-31", "instructions": "Conduct user research, create personas, user journey maps, usability testing report"}');

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
('f0000000-0000-0000-0000-000000000002', '60000000-0000-0000-0000-000000000002', '20000000-0000-0000-0000-000000000105', 'c0000000-0000-0000-0000-000000000002', 'public class Hello { public static void main(String[] args) { System.out.println("Hello!"); } }', 'SUBMITTED'),
('f0000000-0000-0000-0000-000000000003', '60000000-0000-0000-0000-000000000003', '20000000-0000-0000-0000-000000000104', 'c0000000-0000-0000-0000-000000000001', 'class Calculator { ... }', 'SUBMITTED'),
('f0000000-0000-0000-0000-000000000004', '60000000-0000-0000-0000-000000000005', '20000000-0000-0000-0000-000000000105', 'c0000000-0000-0000-0000-000000000002', '<html><body>My Website</body></html>', 'GRADED'),
('f0000000-0000-0000-0000-000000000005', '60000000-0000-0000-0000-000000000006', '20000000-0000-0000-0000-000000000104', 'c0000000-0000-0000-0000-000000000001', 'function TodoApp() { ... }', 'SUBMITTED'),
('f0000000-0000-0000-0000-000000000006', '60000000-0000-0000-0000-000000000008', '20000000-0000-0000-0000-000000000105', 'c0000000-0000-0000-0000-000000000002', 'db.orders.aggregate([...])', 'GRADED'),
('f0000000-0000-0000-0000-000000000007', '60000000-0000-0000-0000-000000000010', '20000000-0000-0000-0000-000000000104', 'c0000000-0000-0000-0000-000000000001', 'def quick_sort(arr): ...', 'SUBMITTED'),
('f0000000-0000-0000-0000-000000000008', '60000000-0000-0000-0000-000000000012', '20000000-0000-0000-0000-000000000105', 'c0000000-0000-0000-0000-000000000002', 'Flutter quiz app code', 'GRADED'),
('f0000000-0000-0000-0000-000000000009', '60000000-0000-0000-0000-000000000014', '20000000-0000-0000-0000-000000000104', 'c0000000-0000-0000-0000-000000000001', 'AWS Lambda handler code', 'SUBMITTED'),
('f0000000-0000-0000-0000-000000000010', '60000000-0000-0000-0000-000000000016', '20000000-0000-0000-0000-000000000105', 'c0000000-0000-0000-0000-000000000002', 'CNN model code', 'GRADED');

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

-- Demo Courses
-- Demo data below removed due to schema mismatch (wrong column names and values).
