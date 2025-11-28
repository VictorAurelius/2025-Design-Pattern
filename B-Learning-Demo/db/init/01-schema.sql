-- ============================================
-- B-LEARNING CORE DATABASE SCHEMA
-- Version: 1.0 (Core - 16 bảng)
-- Database: PostgreSQL 14+
-- Author: Nguyễn Văn Kiệt - CNTT1-K63
-- Created: 2025-11-27
-- ============================================

-- ============================================
-- EXTENSIONS
-- ============================================

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";  -- UUID generation
CREATE EXTENSION IF NOT EXISTS "pg_trgm";    -- Full-text search
CREATE EXTENSION IF NOT EXISTS "btree_gin";  -- GIN indexes cho arrays và JSON

-- ============================================
-- DOMAIN 1: QUẢN LÝ NGƯỜI DÙNG (3 bảng)
-- ============================================

-- Bảng 1: User (Người dùng)
CREATE TABLE "User" (
  user_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  email VARCHAR(255) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  first_name VARCHAR(100) NOT NULL,
  last_name VARCHAR(100) NOT NULL,
  avatar_url VARCHAR(500),
  phone VARCHAR(20),
  account_status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
  preferences JSON DEFAULT '{}',
  email_verified_at TIMESTAMP,
  last_login_at TIMESTAMP,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT chk_user_account_status CHECK (account_status IN (
    'PENDING_VERIFICATION', 'ACTIVE', 'SUSPENDED', 'DELETED'
  ))
);

COMMENT ON TABLE "User" IS 'Bảng người dùng - lưu thông tin tài khoản';
COMMENT ON COLUMN "User".user_id IS 'Mã định danh duy nhất của người dùng';
COMMENT ON COLUMN "User".email IS 'Email đăng nhập (duy nhất)';
COMMENT ON COLUMN "User".password_hash IS 'Mật khẩu đã mã hóa (bcrypt)';
COMMENT ON COLUMN "User".first_name IS 'Tên';
COMMENT ON COLUMN "User".last_name IS 'Họ và tên đệm';
COMMENT ON COLUMN "User".avatar_url IS 'Đường dẫn ảnh đại diện (S3/GCS)';
COMMENT ON COLUMN "User".phone IS 'Số điện thoại';
COMMENT ON COLUMN "User".account_status IS 'Trạng thái tài khoản: PENDING_VERIFICATION, ACTIVE, SUSPENDED, DELETED';
COMMENT ON COLUMN "User".preferences IS 'Tùy chọn người dùng (JSON): notifications, locale, timezone';
COMMENT ON COLUMN "User".email_verified_at IS 'Thời điểm xác thực email';
COMMENT ON COLUMN "User".last_login_at IS 'Lần đăng nhập gần nhất';

-- Bảng 2: Role (Vai trò)
CREATE TABLE "Role" (
  role_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  name VARCHAR(50) NOT NULL UNIQUE,
  description TEXT,
  permissions JSON,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE "Role" IS 'Vai trò hệ thống - RBAC (Role-Based Access Control)';
COMMENT ON COLUMN "Role".name IS 'Tên vai trò: STUDENT, INSTRUCTOR, TA, ADMIN';
COMMENT ON COLUMN "Role".description IS 'Mô tả vai trò';
COMMENT ON COLUMN "Role".permissions IS 'Danh sách quyền hạn (JSON)';

-- Bảng 3: UserRole (Phân quyền)
CREATE TABLE "UserRole" (
  user_role_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID NOT NULL REFERENCES "User"(user_id) ON DELETE CASCADE,
  role_id UUID NOT NULL REFERENCES "Role"(role_id) ON DELETE CASCADE,
  granted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  granted_by UUID REFERENCES "User"(user_id) ON DELETE SET NULL,
  expires_at TIMESTAMP,

  CONSTRAINT uq_user_role UNIQUE(user_id, role_id)
);

COMMENT ON TABLE "UserRole" IS 'Gán vai trò cho người dùng (many-to-many)';
COMMENT ON COLUMN "UserRole".granted_at IS 'Thời điểm cấp quyền';
COMMENT ON COLUMN "UserRole".granted_by IS 'Người cấp quyền (admin)';
COMMENT ON COLUMN "UserRole".expires_at IS 'Thời điểm hết hạn (NULL = vĩnh viễn)';

-- ============================================
-- DOMAIN 2: NỘI DUNG KHÓA HỌC (4 bảng)
-- ============================================

-- Bảng 4: Course (Khóa học)
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
    'BEGINNER', 'INTERMEDIATE', 'ADVANCED'
  )),
  CONSTRAINT chk_course_status CHECK (status IN (
    'DRAFT', 'PUBLISHED', 'ARCHIVED'
  ))
);

COMMENT ON TABLE "Course" IS 'Khóa học';
COMMENT ON COLUMN "Course".code IS 'Mã khóa học (duy nhất, ví dụ: JAVA101)';
COMMENT ON COLUMN "Course".title IS 'Tên khóa học';
COMMENT ON COLUMN "Course".description IS 'Mô tả chi tiết (HTML/Markdown)';
COMMENT ON COLUMN "Course".short_description IS 'Mô tả ngắn (cho danh sách)';
COMMENT ON COLUMN "Course".thumbnail_url IS 'Ảnh đại diện khóa học';
COMMENT ON COLUMN "Course".category IS 'Danh mục (Programming, Math, Design...)';
COMMENT ON COLUMN "Course".difficulty_level IS 'Độ khó: BEGINNER, INTERMEDIATE, ADVANCED';
COMMENT ON COLUMN "Course".estimated_hours IS 'Thời lượng ước tính (giờ)';
COMMENT ON COLUMN "Course".status IS 'Trạng thái: DRAFT (nháp), PUBLISHED (public), ARCHIVED (lưu trữ)';
COMMENT ON COLUMN "Course".published_at IS 'Thời điểm xuất bản';
COMMENT ON COLUMN "Course".created_by IS 'Người tạo (Instructor)';

-- Bảng 5: Module (Chương/Module)
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

COMMENT ON TABLE "Module" IS 'Chương/Module trong khóa học';
COMMENT ON COLUMN "Module".title IS 'Tên chương (ví dụ: Chương 1: Giới thiệu Java)';
COMMENT ON COLUMN "Module".order_num IS 'Thứ tự hiển thị (1, 2, 3...)';
COMMENT ON COLUMN "Module".prerequisite_module_ids IS 'Mảng UUID của modules cần học trước';
COMMENT ON COLUMN "Module".estimated_duration_minutes IS 'Thời lượng ước tính (phút)';

-- Bảng 6: Lecture (Bài giảng)
CREATE TABLE "Lecture" (
  lecture_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  module_id UUID NOT NULL REFERENCES "Module"(module_id) ON DELETE CASCADE,
  title VARCHAR(200) NOT NULL,
  description TEXT,
  type VARCHAR(20) NOT NULL,
  content_url VARCHAR(1024),
  duration_seconds INT,
  order_num INT NOT NULL,
  assignment_config JSON,
  is_preview BOOLEAN DEFAULT FALSE,
  is_downloadable BOOLEAN DEFAULT TRUE,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT chk_lecture_type CHECK (type IN (
    'VIDEO', 'PDF', 'SLIDE', 'AUDIO', 'TEXT', 'ASSIGNMENT'
  )),
  CONSTRAINT uq_lecture_order UNIQUE(module_id, order_num)
);

COMMENT ON TABLE "Lecture" IS 'Bài giảng (bao gồm cả Assignment)';
COMMENT ON COLUMN "Lecture".title IS 'Tiêu đề bài giảng';
COMMENT ON COLUMN "Lecture".type IS 'Loại: VIDEO, PDF, SLIDE, AUDIO, TEXT, ASSIGNMENT';
COMMENT ON COLUMN "Lecture".content_url IS 'Đường dẫn nội dung (S3/GCS URL)';
COMMENT ON COLUMN "Lecture".duration_seconds IS 'Thời lượng (giây)';
COMMENT ON COLUMN "Lecture".order_num IS 'Thứ tự trong module';
COMMENT ON COLUMN "Lecture".assignment_config IS 'Cấu hình bài tập (JSON) - chỉ dùng khi type=ASSIGNMENT';
COMMENT ON COLUMN "Lecture".is_preview IS 'Cho phép xem trước không cần đăng ký?';
COMMENT ON COLUMN "Lecture".is_downloadable IS 'Cho phép tải về?';

-- Bảng 7: Resource (Tài liệu đính kèm)
CREATE TABLE "Resource" (
  resource_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  lecture_id UUID NOT NULL REFERENCES "Lecture"(lecture_id) ON DELETE CASCADE,
  title VARCHAR(200) NOT NULL,
  file_url VARCHAR(500) NOT NULL,
  file_type VARCHAR(100),
  file_size_bytes BIGINT,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE "Resource" IS 'Tài liệu đính kèm cho bài giảng';
COMMENT ON COLUMN "Resource".title IS 'Tên file hiển thị';
COMMENT ON COLUMN "Resource".file_url IS 'Đường dẫn file (S3/GCS)';
COMMENT ON COLUMN "Resource".file_type IS 'Loại file (MIME type, ví dụ: application/pdf)';
COMMENT ON COLUMN "Resource".file_size_bytes IS 'Kích thước file (bytes)';

-- ============================================
-- DOMAIN 3: ĐÁNH GIÁ (5 bảng)
-- ============================================

-- Bảng 8: Quiz (Bài kiểm tra)
CREATE TABLE "Quiz" (
  quiz_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  course_id UUID NOT NULL REFERENCES "Course"(course_id) ON DELETE CASCADE,
  title VARCHAR(200) NOT NULL,
  description TEXT,
  time_limit_minutes INT,
  pass_score DECIMAL(5,2),
  questions JSON NOT NULL,
  shuffle_questions BOOLEAN DEFAULT FALSE,
  show_correct_answers BOOLEAN DEFAULT TRUE,
  is_published BOOLEAN DEFAULT FALSE,
  created_by UUID REFERENCES "User"(user_id) ON DELETE SET NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE "Quiz" IS 'Bài kiểm tra trắc nghiệm/tự luận';
COMMENT ON COLUMN "Quiz".title IS 'Tên quiz';
COMMENT ON COLUMN "Quiz".time_limit_minutes IS 'Giới hạn thời gian (phút, 0 = không giới hạn)';
COMMENT ON COLUMN "Quiz".pass_score IS 'Điểm đạt (%, NULL = không yêu cầu)';
COMMENT ON COLUMN "Quiz".questions IS 'Danh sách câu hỏi (JSON): [{"question_id": "uuid", "points": 10, "order": 1}]';
COMMENT ON COLUMN "Quiz".shuffle_questions IS 'Xáo trộn thứ tự câu hỏi?';
COMMENT ON COLUMN "Quiz".show_correct_answers IS 'Hiện đáp án đúng sau khi nộp bài?';
COMMENT ON COLUMN "Quiz".is_published IS 'Đã public cho student làm?';

-- Bảng 9: Question (Câu hỏi)
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
    'MCQ', 'TRUE_FALSE', 'ESSAY', 'SHORT_ANSWER'
  )),
  CONSTRAINT chk_question_difficulty CHECK (difficulty IN (
    'EASY', 'MEDIUM', 'HARD'
  ))
);

COMMENT ON TABLE "Question" IS 'Ngân hàng câu hỏi (dùng chung cho nhiều quiz)';
COMMENT ON COLUMN "Question".text IS 'Nội dung câu hỏi';
COMMENT ON COLUMN "Question".type IS 'Loại câu hỏi: MCQ (trắc nghiệm), TRUE_FALSE (đúng/sai), ESSAY (tự luận), SHORT_ANSWER';
COMMENT ON COLUMN "Question".difficulty IS 'Độ khó: EASY, MEDIUM, HARD';
COMMENT ON COLUMN "Question".max_points IS 'Điểm tối đa mặc định';
COMMENT ON COLUMN "Question".explanation IS 'Giải thích đáp án';
COMMENT ON COLUMN "Question".is_active IS 'Còn sử dụng không?';

-- Bảng 10: Option (Lựa chọn)
CREATE TABLE "Option" (
  option_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  question_id UUID NOT NULL REFERENCES "Question"(question_id) ON DELETE CASCADE,
  option_text TEXT NOT NULL,
  is_correct BOOLEAN NOT NULL DEFAULT FALSE,
  order_num INT NOT NULL,
  feedback TEXT,

  CONSTRAINT uq_option_order UNIQUE(question_id, order_num)
);

COMMENT ON TABLE "Option" IS 'Lựa chọn cho câu hỏi trắc nghiệm (MCQ, TRUE_FALSE)';
COMMENT ON COLUMN "Option".option_text IS 'Nội dung lựa chọn';
COMMENT ON COLUMN "Option".is_correct IS 'Đây có phải đáp án đúng?';
COMMENT ON COLUMN "Option".order_num IS 'Thứ tự hiển thị (A, B, C, D)';
COMMENT ON COLUMN "Option".feedback IS 'Giải thích cho lựa chọn này';

-- Bảng 11: Attempt (Lần làm bài quiz)
CREATE TABLE "Attempt" (
  attempt_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  quiz_id UUID NOT NULL REFERENCES "Quiz"(quiz_id) ON DELETE CASCADE,
  user_id UUID NOT NULL REFERENCES "User"(user_id) ON DELETE CASCADE,
  enrollment_id UUID NOT NULL REFERENCES "Enrollment"(enrollment_id) ON DELETE CASCADE,
  attempt_number INT NOT NULL,
  started_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  submitted_at TIMESTAMP,
  time_spent_seconds INT DEFAULT 0,
  status VARCHAR(20) NOT NULL DEFAULT 'IN_PROGRESS',
  answers JSON,
  total_score DECIMAL(6,2),
  max_possible_score DECIMAL(6,2) NOT NULL,
  percentage_score DECIMAL(5,2),
  graded_at TIMESTAMP,
  graded_by UUID REFERENCES "User"(user_id) ON DELETE SET NULL,

  CONSTRAINT uq_attempt UNIQUE(user_id, quiz_id, attempt_number),
  CONSTRAINT chk_attempt_status CHECK (status IN (
    'IN_PROGRESS', 'SUBMITTED'
  ))
);

COMMENT ON TABLE "Attempt" IS 'Lần làm bài quiz (bao gồm câu trả lời trong answers JSON)';
COMMENT ON COLUMN "Attempt".attempt_number IS 'Lần thứ mấy (1, 2, 3...)';
COMMENT ON COLUMN "Attempt".started_at IS 'Thời điểm bắt đầu làm';
COMMENT ON COLUMN "Attempt".submitted_at IS 'Thời điểm nộp bài';
COMMENT ON COLUMN "Attempt".time_spent_seconds IS 'Thời gian làm bài (giây)';
COMMENT ON COLUMN "Attempt".status IS 'Trạng thái: IN_PROGRESS (đang làm), SUBMITTED (đã nộp)';
COMMENT ON COLUMN "Attempt".answers IS 'Câu trả lời (JSON): [{"question_id": "uuid", "answer_text": "...", "score": 10}]';
COMMENT ON COLUMN "Attempt".total_score IS 'Tổng điểm';
COMMENT ON COLUMN "Attempt".max_possible_score IS 'Điểm tối đa có thể đạt';
COMMENT ON COLUMN "Attempt".percentage_score IS 'Điểm phần trăm (%)';
COMMENT ON COLUMN "Attempt".graded_at IS 'Thời điểm chấm xong';
COMMENT ON COLUMN "Attempt".graded_by IS 'Người chấm (Instructor)';

-- Bảng 12: AssignmentSubmission (Nộp bài tập)
CREATE TABLE "AssignmentSubmission" (
  submission_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  lecture_id UUID NOT NULL REFERENCES "Lecture"(lecture_id) ON DELETE CASCADE,
  user_id UUID NOT NULL REFERENCES "User"(user_id) ON DELETE CASCADE,
  enrollment_id UUID NOT NULL REFERENCES "Enrollment"(enrollment_id) ON DELETE CASCADE,
  submission_number INT NOT NULL DEFAULT 1,
  submitted_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  content TEXT,
  file_urls JSON,
  code_submission TEXT,
  is_late BOOLEAN DEFAULT FALSE,
  status VARCHAR(20) NOT NULL DEFAULT 'SUBMITTED',
  score DECIMAL(6,2),
  max_score DECIMAL(6,2),
  feedback TEXT,
  graded_at TIMESTAMP,
  graded_by UUID REFERENCES "User"(user_id) ON DELETE SET NULL,

  CONSTRAINT uq_assignment_submission UNIQUE(lecture_id, user_id, submission_number),
  CONSTRAINT chk_submission_status CHECK (status IN (
    'SUBMITTED', 'GRADING', 'GRADED', 'RETURNED'
  ))
);

COMMENT ON TABLE "AssignmentSubmission" IS 'Nộp bài tập (Assignment)';
COMMENT ON COLUMN "AssignmentSubmission".lecture_id IS 'Bài giảng nào (type = ASSIGNMENT)';
COMMENT ON COLUMN "AssignmentSubmission".submission_number IS 'Lần nộp thứ mấy';
COMMENT ON COLUMN "AssignmentSubmission".submitted_at IS 'Thời điểm nộp';
COMMENT ON COLUMN "AssignmentSubmission".content IS 'Nội dung text (nếu type = text)';
COMMENT ON COLUMN "AssignmentSubmission".file_urls IS 'Danh sách file đính kèm (JSON array): ["s3://...", "s3://..."]';
COMMENT ON COLUMN "AssignmentSubmission".code_submission IS 'Code (nếu type = code)';
COMMENT ON COLUMN "AssignmentSubmission".is_late IS 'Nộp trễ?';
COMMENT ON COLUMN "AssignmentSubmission".status IS 'Trạng thái: SUBMITTED, GRADING, GRADED, RETURNED';
COMMENT ON COLUMN "AssignmentSubmission".score IS 'Điểm';
COMMENT ON COLUMN "AssignmentSubmission".max_score IS 'Điểm tối đa';
COMMENT ON COLUMN "AssignmentSubmission".feedback IS 'Nhận xét từ giảng viên';
COMMENT ON COLUMN "AssignmentSubmission".graded_by IS 'Người chấm (Instructor)';

-- ============================================
-- DOMAIN 4: ĐĂNG KÝ & TIẾN ĐỘ (2 bảng)
-- ============================================

-- Bảng 13: Enrollment (Đăng ký)
CREATE TABLE "Enrollment" (
  enrollment_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID NOT NULL REFERENCES "User"(user_id) ON DELETE CASCADE,
  course_id UUID NOT NULL REFERENCES "Course"(course_id) ON DELETE CASCADE,
  class_id UUID REFERENCES "Class"(class_id) ON DELETE SET NULL,
  role VARCHAR(20) NOT NULL,
  status VARCHAR(20) DEFAULT 'ACTIVE',
  enrolled_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  completed_at TIMESTAMP,
  final_grade DECIMAL(5,2),
  completion_percentage DECIMAL(5,2) DEFAULT 0,
  last_accessed_at TIMESTAMP,

  CONSTRAINT uq_enrollment UNIQUE(user_id, course_id, COALESCE(class_id, '00000000-0000-0000-0000-000000000000'::UUID)),
  CONSTRAINT chk_enrollment_role CHECK (role IN ('STUDENT', 'INSTRUCTOR', 'TA')),
  CONSTRAINT chk_enrollment_status CHECK (status IN ('ACTIVE', 'COMPLETED', 'DROPPED', 'SUSPENDED'))
);

COMMENT ON TABLE "Enrollment" IS 'Đăng ký khóa học (self-paced hoặc theo lớp)';
COMMENT ON COLUMN "Enrollment".class_id IS 'Lớp học (NULL = self-paced, UUID = blended learning)';
COMMENT ON COLUMN "Enrollment".role IS 'Vai trò trong khóa: STUDENT, INSTRUCTOR, TA';
COMMENT ON COLUMN "Enrollment".status IS 'Trạng thái: ACTIVE, COMPLETED, DROPPED, SUSPENDED';
COMMENT ON COLUMN "Enrollment".final_grade IS 'Điểm cuối khóa';
COMMENT ON COLUMN "Enrollment".completion_percentage IS 'Phần trăm hoàn thành (%)';
COMMENT ON COLUMN "Enrollment".last_accessed_at IS 'Lần truy cập gần nhất';

-- Bảng 14: Progress (Tiến độ)
CREATE TABLE "Progress" (
  progress_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID NOT NULL REFERENCES "User"(user_id) ON DELETE CASCADE,
  course_id UUID NOT NULL REFERENCES "Course"(course_id) ON DELETE CASCADE,
  module_id UUID NOT NULL REFERENCES "Module"(module_id) ON DELETE CASCADE,
  status VARCHAR(20) NOT NULL DEFAULT 'NOT_STARTED',
  started_at TIMESTAMP,
  completed_at TIMESTAMP,
  lecture_progress JSON,

  CONSTRAINT uq_progress UNIQUE(user_id, course_id, module_id),
  CONSTRAINT chk_progress_status CHECK (status IN ('NOT_STARTED', 'IN_PROGRESS', 'COMPLETED'))
);

COMMENT ON TABLE "Progress" IS 'Tiến độ học tập (module level)';
COMMENT ON COLUMN "Progress".status IS 'Trạng thái: NOT_STARTED, IN_PROGRESS, COMPLETED';
COMMENT ON COLUMN "Progress".started_at IS 'Thời điểm bắt đầu học module';
COMMENT ON COLUMN "Progress".completed_at IS 'Thời điểm hoàn thành module';
COMMENT ON COLUMN "Progress".lecture_progress IS 'Tiến độ lecture (JSON, optional): {"lecture_uuid": {"percent": 80, "last_position": 1200}}';

-- ============================================
-- DOMAIN 5: LỚP HỌC & CHỨNG CHỈ (2 bảng)
-- ============================================

-- Bảng 15: Class (Lớp học)
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
  schedules JSON,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT chk_class_status CHECK (status IN ('SCHEDULED', 'ONGOING', 'COMPLETED', 'CANCELLED'))
);

COMMENT ON TABLE "Class" IS 'Lớp học (blended learning)';
COMMENT ON COLUMN "Class".name IS 'Tên lớp (ví dụ: Java K63 - Sáng T2-T4-T6)';
COMMENT ON COLUMN "Class".start_date IS 'Ngày bắt đầu';
COMMENT ON COLUMN "Class".end_date IS 'Ngày kết thúc';
COMMENT ON COLUMN "Class".status IS 'Trạng thái: SCHEDULED, ONGOING, COMPLETED, CANCELLED';
COMMENT ON COLUMN "Class".max_students IS 'Sĩ số tối đa';
COMMENT ON COLUMN "Class".location IS 'Địa điểm học';
COMMENT ON COLUMN "Class".schedules IS 'Lịch học + điểm danh (JSON): [{"date": "2025-12-01", "attendances": [...]}]';

-- Bảng 16: Certificate (Chứng chỉ)
CREATE TABLE "Certificate" (
  certificate_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID NOT NULL REFERENCES "User"(user_id) ON DELETE CASCADE,
  course_id UUID NOT NULL REFERENCES "Course"(course_id) ON DELETE CASCADE,
  certificate_code VARCHAR(50) NOT NULL UNIQUE,
  verification_code VARCHAR(100) NOT NULL UNIQUE,
  issue_date DATE NOT NULL,
  final_grade DECIMAL(5,2),
  pdf_url VARCHAR(500),
  status VARCHAR(20) DEFAULT 'ACTIVE',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT uq_certificate UNIQUE(user_id, course_id),
  CONSTRAINT chk_certificate_status CHECK (status IN ('ACTIVE', 'REVOKED'))
);

COMMENT ON TABLE "Certificate" IS 'Chứng chỉ hoàn thành khóa học';
COMMENT ON COLUMN "Certificate".certificate_code IS 'Mã chứng chỉ công khai (ví dụ: BL-2025-000001)';
COMMENT ON COLUMN "Certificate".verification_code IS 'Mã xác thực bí mật (hash)';
COMMENT ON COLUMN "Certificate".issue_date IS 'Ngày cấp chứng chỉ';
COMMENT ON COLUMN "Certificate".final_grade IS 'Điểm cuối khóa';
COMMENT ON COLUMN "Certificate".pdf_url IS 'Đường dẫn file PDF chứng chỉ (S3/GCS)';
COMMENT ON COLUMN "Certificate".status IS 'Trạng thái: ACTIVE (hiệu lực), REVOKED (đã thu hồi)';

-- ============================================
-- END OF SCHEMA
-- ============================================
