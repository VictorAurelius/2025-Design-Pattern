# ĐÁNH GIÁ CHI TIẾT THIẾT KẾ DATABASE HỆ THỐNG B-LEARNING

**Người đánh giá:** Claude AI
**Ngày đánh giá:** 25/11/2025
**Phiên bản tài liệu:** 1.0
**Đối tượng đánh giá:** Hệ thống B-Learning (Blended Learning) - Nguyễn Văn Kiệt

---

## MỤC LỤC

1. [Tổng quan đánh giá](#1-tổng-quan-đánh-giá)
2. [Điểm mạnh của thiết kế](#2-điểm-mạnh-của-thiết-kế)
3. [Phân tích chi tiết các thực thể](#3-phân-tích-chi-tiết-các-thực-thể)
4. [Vấn đề nghiêm trọng](#4-vấn-đề-nghiêm-trọng)
5. [Vấn đề cần cải thiện](#5-vấn-đề-cần-cải-thiện)
6. [Thiếu sót về mối quan hệ](#6-thiếu-sót-về-mối-quan-hệ)
7. [Thiếu các bảng hỗ trợ](#7-thiếu-các-bảng-hỗ-trợ)
8. [Vấn đề về indexes và constraints](#8-vấn-đề-về-indexes-và-constraints)
9. [Đề xuất cải tiến](#9-đề-xuất-cải-tiến)
10. [SQL Scripts đề xuất](#10-sql-scripts-đề-xuất)
11. [Tổng kết](#11-tổng-kết)

---

## 1. TỔNG QUAN ĐÁNH GIÁ

### 1.1 Thống kê thiết kế hiện tại

| Chỉ số | Giá trị |
|--------|---------|
| Tổng số bảng | 21 |
| Bảng chính (Core entities) | 15 |
| Bảng trung gian (Junction tables) | 6 |
| Kiểu dữ liệu Primary Key | UUID |
| Số lượng ENUM được sử dụng | 8+ |

### 1.2 Xếp hạng tổng thể

```
┌─────────────────────────────────────────────┐
│  ĐIỂM TỔNG THỂ: 6.5/10                      │
├─────────────────────────────────────────────┤
│  ✅ Cấu trúc cơ bản      : 8/10             │
│  ⚠️  Tính đầy đủ         : 6/10             │
│  ⚠️  Tính mở rộng        : 6/10             │
│  ❌ Constraints/Indexes  : 4/10             │
│  ⚠️  Data integrity      : 6/10             │
└─────────────────────────────────────────────┘
```

### 1.3 Phân loại vấn đề

| Mức độ | Số lượng | Mô tả |
|--------|----------|-------|
| 🔴 Nghiêm trọng | 5 | Ảnh hưởng trực tiếp đến chức năng core |
| 🟡 Trung bình | 8 | Cần cải thiện để nâng cao chất lượng |
| 🟢 Nhỏ | 12 | Tối ưu hóa và best practices |

---

## 2. ĐIỂM MẠNH CỦA THIẾT KẾ

### ✅ 2.1 Sử dụng UUID cho Primary Keys

**Ưu điểm:**
```sql
-- Ví dụ định nghĩa
User {
  + user_id : UUID PRIMARY KEY
  ...
}
```

- ✅ Tính duy nhất toàn cục - không xung đột khi merge data
- ✅ Bảo mật tốt hơn - không dễ đoán được ID
- ✅ Phù hợp với kiến trúc microservices/phân tán
- ✅ Tránh race condition khi insert đồng thời

**Lưu ý:**
- ⚠️ Kích thước lớn hơn INT/BIGINT (16 bytes vs 4/8 bytes)
- ⚠️ Index performance có thể kém hơn sequential ID

### ✅ 2.2 Tách biệt Course và Class

**Thiết kế hợp lý:**

```
Course (Nội dung học)
  ↓
  - Module
  - Lecture
  - Quiz
  - Question Bank

Class (Buổi học thực tế)
  ↓
  - Schedule
  - Attendance
  - Video Conference
```

**Lợi ích:**
- ✅ Tái sử dụng nội dung Course cho nhiều Class khác nhau
- ✅ Cho phép self-paced learning (học không qua Class)
- ✅ Dễ dàng quản lý lịch học và điểm danh riêng biệt

### ✅ 2.3 Cấu trúc phân cấp rõ ràng

```
Course
  └── Module
       └── Lecture
            ├── Resource
            ├── Progress
            └── Thread (discussion)

Quiz
  └── QuizQuestion (junction)
       └── Question
            ├── Option
            └── Submission
```

- ✅ Dễ hiểu, logic nghiệp vụ rõ ràng
- ✅ Cascade delete dễ kiểm soát
- ✅ Query performance tốt với proper indexing

### ✅ 2.4 Phân quyền linh hoạt (RBAC)

```
User ←→ UserRole ←→ Role
```

- ✅ Một user có thể có nhiều role
- ✅ Dễ mở rộng thêm role mới
- ✅ Phù hợp với yêu cầu: STUDENT, INSTRUCTOR, TA, ADMIN, MODERATOR

---

## 3. PHÂN TÍCH CHI TIẾT CÁC THỰC THỂ

### 3.1 Nhóm User Management

#### Table: User

**Thiết kế hiện tại:**
```sql
User {
  + user_id : UUID PRIMARY KEY
  + email : VARCHAR(255)
  + password_hash : VARCHAR(255)
  + created_at : DATETIME
}
```

**⚠️ Vấn đề:**
1. Thiếu thông tin cơ bản: first_name, last_name
2. Không có trạng thái tài khoản (active, suspended, deleted)
3. Không tracking email verification
4. Thiếu avatar, phone, timezone
5. Không lưu last_login_at

**✅ Đề xuất cải thiện:**
```sql
CREATE TABLE User (
  user_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

  -- Authentication
  email VARCHAR(255) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  email_verified_at TIMESTAMP,

  -- Personal Info
  first_name VARCHAR(100) NOT NULL,
  last_name VARCHAR(100) NOT NULL,
  avatar_url VARCHAR(500),
  phone VARCHAR(20),
  bio TEXT,

  -- Status
  account_status VARCHAR(30) NOT NULL DEFAULT 'PENDING_VERIFICATION',
    -- PENDING_VERIFICATION, ACTIVE, SUSPENDED, DELETED

  -- Preferences
  timezone VARCHAR(50) DEFAULT 'UTC',
  locale VARCHAR(10) DEFAULT 'vi',
  notification_enabled BOOLEAN DEFAULT TRUE,

  -- Tracking
  last_login_at TIMESTAMP,
  login_count INT DEFAULT 0,

  -- Audit
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  deleted_at TIMESTAMP,

  -- Constraints
  CONSTRAINT chk_email_format CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Z|a-z]{2,}$'),
  CONSTRAINT chk_account_status CHECK (account_status IN ('PENDING_VERIFICATION', 'ACTIVE', 'SUSPENDED', 'DELETED'))
);

-- Indexes
CREATE INDEX idx_user_email ON User(email);
CREATE INDEX idx_user_status ON User(account_status);
CREATE INDEX idx_user_created ON User(created_at DESC);
```

#### Table: Role

**Thiết kế hiện tại:** ✅ Tốt

```sql
Role {
  + role_id : UUID PRIMARY KEY
  + name : VARCHAR(50)
  + permissions : JSON
}
```

**💡 Đề xuất bổ sung:**
```sql
ALTER TABLE Role ADD COLUMN description TEXT;
ALTER TABLE Role ADD COLUMN is_system_role BOOLEAN DEFAULT FALSE;
ALTER TABLE Role ADD COLUMN priority INT DEFAULT 0;
```

#### Table: UserRole

**Thiết kế hiện tại:** ✅ Tốt (bảng trung gian)

**💡 Đề xuất thêm:**
```sql
ALTER TABLE UserRole ADD COLUMN granted_by UUID REFERENCES User(user_id);
ALTER TABLE UserRole ADD COLUMN granted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE UserRole ADD COLUMN expires_at TIMESTAMP;
```

---

### 3.2 Nhóm Course Content

#### Table: Course

**Thiết kế hiện tại:**
```sql
Course {
  + course_id : UUID PRIMARY KEY
  + title : VARCHAR(200)
  + description : TEXT
  + code : VARCHAR(50)
  + published_at : DATETIME
  + status : ENUM('DRAFT', 'PUBLISHED')
}
```

**✅ Thiết kế tốt, có thể bổ sung:**

```sql
CREATE TABLE Course (
  course_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

  -- Basic Info
  code VARCHAR(50) NOT NULL UNIQUE,
  title VARCHAR(200) NOT NULL,
  description TEXT,
  short_description VARCHAR(500),

  -- Content
  learning_objectives TEXT[],
  prerequisites TEXT[],
  target_audience TEXT,

  -- Media
  thumbnail_url VARCHAR(500),
  intro_video_url VARCHAR(500),

  -- Metadata
  category VARCHAR(100),
  tags VARCHAR(500)[],
  difficulty_level VARCHAR(20), -- BEGINNER, INTERMEDIATE, ADVANCED, EXPERT
  estimated_hours DECIMAL(5,2),
  language VARCHAR(10) DEFAULT 'vi',

  -- Status
  status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
  published_at TIMESTAMP,
  archived_at TIMESTAMP,

  -- Tracking
  enrollment_count INT DEFAULT 0,
  completion_count INT DEFAULT 0,
  average_rating DECIMAL(3,2),

  -- Audit
  created_by UUID REFERENCES User(user_id),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT chk_course_status CHECK (status IN ('DRAFT', 'PUBLISHED', 'ARCHIVED')),
  CONSTRAINT chk_difficulty CHECK (difficulty_level IN ('BEGINNER', 'INTERMEDIATE', 'ADVANCED', 'EXPERT'))
);

CREATE INDEX idx_course_status ON Course(status);
CREATE INDEX idx_course_category ON Course(category);
CREATE INDEX idx_course_published ON Course(published_at DESC);
CREATE INDEX idx_course_code ON Course(code);
```

#### Table: Module

**Thiết kế hiện tại:**
```sql
Module {
  + module_id : UUID PRIMARY KEY
  - course_id : UUID FK
  + title : VARCHAR(200)
  + order_num : INT
}
```

**⚠️ Vấn đề:** Thiếu thông tin chi tiết

**✅ Đề xuất:**
```sql
CREATE TABLE Module (
  module_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  course_id UUID NOT NULL REFERENCES Course(course_id) ON DELETE CASCADE,

  -- Content
  title VARCHAR(200) NOT NULL,
  description TEXT,
  learning_objectives TEXT[],

  -- Structure
  order_num INT NOT NULL,
  parent_module_id UUID REFERENCES Module(module_id), -- For nested modules

  -- Prerequisites
  prerequisite_module_ids UUID[],

  -- Metadata
  estimated_duration_minutes INT,
  is_optional BOOLEAN DEFAULT FALSE,

  -- Audit
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT uq_course_module_order UNIQUE(course_id, order_num)
);

CREATE INDEX idx_module_course ON Module(course_id, order_num);
CREATE INDEX idx_module_parent ON Module(parent_module_id);
```

#### Table: Lecture

**Thiết kế hiện tại:**
```sql
Lecture {
  + lecture_id : UUID PRIMARY KEY
  - module_id : UUID FK
  + title : VARCHAR(200)
  + duration_sec : INT
  + content_url : VARCHAR(1024)
  + type : ENUM('VIDEO', 'PDF', 'AUDIO', 'SLIDE')
}
```

**⚠️ Vấn đề:**
1. `content_url` quá đơn giản - không hỗ trợ multiple files
2. Thiếu transcript cho video
3. Không tracking upload status
4. Thiếu thứ tự lecture trong module

**✅ Đề xuất:**
```sql
CREATE TABLE Lecture (
  lecture_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  module_id UUID NOT NULL REFERENCES Module(module_id) ON DELETE CASCADE,

  -- Content
  title VARCHAR(200) NOT NULL,
  description TEXT,
  type VARCHAR(20) NOT NULL, -- VIDEO, PDF, AUDIO, SLIDE, TEXT, CODE
  order_num INT NOT NULL,

  -- Media (primary content)
  content_url VARCHAR(1024),
  duration_seconds INT,
  file_size_bytes BIGINT,

  -- Additional content
  transcript TEXT,
  subtitle_urls JSON, -- {"en": "url", "vi": "url"}

  -- Settings
  is_preview BOOLEAN DEFAULT FALSE, -- Free preview for non-enrolled users
  is_downloadable BOOLEAN DEFAULT TRUE,

  -- Processing status (for video encoding)
  processing_status VARCHAR(20) DEFAULT 'PENDING',
    -- PENDING, PROCESSING, COMPLETED, FAILED
  processing_progress INT DEFAULT 0, -- 0-100

  -- Tracking
  view_count INT DEFAULT 0,
  average_completion_rate DECIMAL(5,2),

  -- Audit
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT uq_module_lecture_order UNIQUE(module_id, order_num),
  CONSTRAINT chk_lecture_type CHECK (type IN ('VIDEO', 'PDF', 'AUDIO', 'SLIDE', 'TEXT', 'CODE')),
  CONSTRAINT chk_processing_status CHECK (processing_status IN ('PENDING', 'PROCESSING', 'COMPLETED', 'FAILED'))
);

CREATE INDEX idx_lecture_module ON Lecture(module_id, order_num);
CREATE INDEX idx_lecture_type ON Lecture(type);
CREATE INDEX idx_lecture_status ON Lecture(processing_status);
```

#### Table: Resource

**Thiết kế hiện tại:** ✅ Tốt

**💡 Đề xuất thêm tracking:**
```sql
ALTER TABLE Resource ADD COLUMN download_count INT DEFAULT 0;
ALTER TABLE Resource ADD COLUMN file_size_bytes BIGINT;
ALTER TABLE Resource ADD COLUMN mime_type VARCHAR(100);
```

---

### 3.3 Nhóm Assessment

#### Table: Question

**Thiết kế hiện tại:**
```sql
Question {
  + question_id : UUID PRIMARY KEY
  - course_id : UUID FK
  + text : TEXT
  + type : ENUM('MCQ', 'TF', 'ESSAY', 'CODE', 'FILE')
  + order_qn : INT
  + max_points : DECIMAL(5,2)
}
```

**⚠️ Vấn đề:**
1. Thiếu difficulty level
2. Không có explanation (giải thích đáp án)
3. Thiếu tags để phân loại
4. `order_qn` không cần thiết (order nên ở QuizQuestion)

**✅ Đề xuất:**
```sql
CREATE TABLE Question (
  question_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  course_id UUID NOT NULL REFERENCES Course(course_id) ON DELETE CASCADE,

  -- Content
  text TEXT NOT NULL,
  question_text_html TEXT, -- Rich text version
  type VARCHAR(20) NOT NULL,
    -- MCQ (Multiple Choice), TRUE_FALSE, ESSAY, SHORT_ANSWER, CODE, FILE_UPLOAD, MATCHING

  -- Settings
  difficulty VARCHAR(20) DEFAULT 'MEDIUM',
    -- EASY, MEDIUM, HARD, EXPERT
  max_points DECIMAL(5,2) NOT NULL DEFAULT 1.00,
  time_limit_seconds INT, -- NULL = no limit

  -- Metadata
  topic VARCHAR(100),
  tags VARCHAR(100)[],
  bloom_taxonomy VARCHAR(50), -- REMEMBER, UNDERSTAND, APPLY, ANALYZE, EVALUATE, CREATE

  -- Answer & Explanation
  correct_answer_explanation TEXT,
  hints TEXT[],

  -- Media
  image_url VARCHAR(500),
  code_template TEXT, -- For CODE type

  -- Usage tracking
  times_used INT DEFAULT 0,
  average_score DECIMAL(5,2),

  -- Status
  is_active BOOLEAN DEFAULT TRUE,

  -- Audit
  created_by UUID REFERENCES User(user_id),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT chk_question_type CHECK (type IN ('MCQ', 'TRUE_FALSE', 'ESSAY', 'SHORT_ANSWER', 'CODE', 'FILE_UPLOAD', 'MATCHING')),
  CONSTRAINT chk_difficulty CHECK (difficulty IN ('EASY', 'MEDIUM', 'HARD', 'EXPERT')),
  CONSTRAINT chk_max_points CHECK (max_points > 0)
);

CREATE INDEX idx_question_course ON Question(course_id);
CREATE INDEX idx_question_type ON Question(type);
CREATE INDEX idx_question_difficulty ON Question(difficulty);
CREATE INDEX idx_question_tags ON Question USING GIN(tags);
CREATE INDEX idx_question_active ON Question(is_active);
```

#### Table: Option

**Thiết kế hiện tại:**
```sql
Option {
  + option_id : UUID PRIMARY KEY
  - question_id : UUID FK
  + text : VARCHAR(500)
  + is_correct : BOOLEAN
}
```

**⚠️ Vấn đề:** Thiếu order, explanation

**✅ Đề xuất:**
```sql
CREATE TABLE Option (
  option_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  question_id UUID NOT NULL REFERENCES Question(question_id) ON DELETE CASCADE,

  -- Content
  option_text TEXT NOT NULL,
  option_text_html TEXT,

  -- Settings
  is_correct BOOLEAN NOT NULL DEFAULT FALSE,
  order_num INT NOT NULL,

  -- Media
  image_url VARCHAR(500),

  -- Feedback
  feedback_if_selected TEXT, -- Explain why this option is correct/incorrect

  -- Audit
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT uq_question_option_order UNIQUE(question_id, order_num)
);

CREATE INDEX idx_option_question ON Option(question_id, order_num);

-- Constraint: At least one correct answer for MCQ
CREATE OR REPLACE FUNCTION check_question_has_correct_answer()
RETURNS TRIGGER AS $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM Option
    WHERE question_id = NEW.question_id
    AND is_correct = TRUE
  ) THEN
    RAISE EXCEPTION 'Question must have at least one correct answer';
  END IF;
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_check_correct_answer
AFTER INSERT OR UPDATE ON Option
FOR EACH ROW
EXECUTE FUNCTION check_question_has_correct_answer();
```

#### Table: Quiz

**Thiết kế hiện tại:**
```sql
Quiz {
  + quiz_id : UUID PRIMARY KEY
  - course_id : UUID FK
  + title : VARCHAR(200)
  + attempt_limit : INT
  + open_at : DATETIME
  + close_at : DATETIME
  + pass_score : DECIMAL(5,2)
}
```

**✅ Thiết kế tốt, đề xuất bổ sung:**

```sql
CREATE TABLE Quiz (
  quiz_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  course_id UUID NOT NULL REFERENCES Course(course_id) ON DELETE CASCADE,

  -- Basic info
  title VARCHAR(200) NOT NULL,
  description TEXT,
  instructions TEXT,

  -- Type & Settings
  quiz_type VARCHAR(20) DEFAULT 'ASSESSMENT',
    -- PRACTICE, ASSESSMENT, EXAM, SURVEY

  -- Timing
  time_limit_minutes INT, -- NULL = no limit
  open_at TIMESTAMP,
  close_at TIMESTAMP,

  -- Attempts
  attempt_limit INT, -- NULL = unlimited
  allow_review BOOLEAN DEFAULT TRUE,
  show_correct_answers BOOLEAN DEFAULT TRUE,
  show_correct_answers_after TIMESTAMP, -- NULL = immediately

  -- Scoring
  pass_score DECIMAL(5,2),
  total_points DECIMAL(6,2), -- Auto-calculated from questions
  grading_method VARCHAR(20) DEFAULT 'HIGHEST',
    -- HIGHEST, AVERAGE, FIRST, LAST

  -- Question settings
  shuffle_questions BOOLEAN DEFAULT FALSE,
  shuffle_options BOOLEAN DEFAULT FALSE,
  questions_per_page INT DEFAULT 1,

  -- Access control
  require_password BOOLEAN DEFAULT FALSE,
  password_hash VARCHAR(255),
  ip_restriction VARCHAR(500), -- Comma-separated IPs or CIDR

  -- Proctoring (future feature)
  require_webcam BOOLEAN DEFAULT FALSE,
  require_screen_recording BOOLEAN DEFAULT FALSE,

  -- Status
  is_published BOOLEAN DEFAULT FALSE,

  -- Tracking
  attempt_count INT DEFAULT 0,
  average_score DECIMAL(5,2),

  -- Audit
  created_by UUID REFERENCES User(user_id),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT chk_quiz_type CHECK (quiz_type IN ('PRACTICE', 'ASSESSMENT', 'EXAM', 'SURVEY')),
  CONSTRAINT chk_grading_method CHECK (grading_method IN ('HIGHEST', 'AVERAGE', 'FIRST', 'LAST')),
  CONSTRAINT chk_quiz_dates CHECK (open_at IS NULL OR close_at IS NULL OR open_at < close_at)
);

CREATE INDEX idx_quiz_course ON Quiz(course_id);
CREATE INDEX idx_quiz_published ON Quiz(is_published);
CREATE INDEX idx_quiz_dates ON Quiz(open_at, close_at);
```

---

## 4. VẤN ĐỀ NGHIÊM TRỌNG

### 🔴 4.1 Bảng Progress - Thiết kế thiếu sót nghiêm trọng

**Thiết kế hiện tại:**
```sql
Progress {
  + progress_id : UUID PRIMARY KEY
  - user_id : UUID FK
  - lecture_id : UUID FK
  - updated_at : DATETIME
  + percent_complete : DECIMAL(5,2)
}
```

**❌ Vấn đề nghiêm trọng:**

1. **Không có `course_id`** → Không biết progress thuộc khóa học nào
2. **Không có `class_id`** → Không phân biệt progress giữa các lớp
3. **Chỉ track lecture** → Không track module, quiz completion
4. **Thiếu trạng thái** → Không biết NOT_STARTED, IN_PROGRESS, COMPLETED
5. **Thiếu timestamp chi tiết** → Không biết khi nào bắt đầu, khi nào hoàn thành

**✅ Giải pháp đề xuất:**

```sql
-- DROP existing table (in migration)
-- DROP TABLE IF EXISTS Progress CASCADE;

-- Create improved Progress tracking
CREATE TABLE Progress (
  progress_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

  -- Who & Where
  user_id UUID NOT NULL REFERENCES User(user_id) ON DELETE CASCADE,
  course_id UUID NOT NULL REFERENCES Course(course_id) ON DELETE CASCADE,
  class_id UUID REFERENCES Class(class_id) ON DELETE SET NULL,
    -- NULL = self-paced learning

  -- What (can track multiple levels)
  module_id UUID REFERENCES Module(module_id) ON DELETE CASCADE,
  lecture_id UUID REFERENCES Lecture(lecture_id) ON DELETE CASCADE,
  quiz_id UUID REFERENCES Quiz(quiz_id) ON DELETE CASCADE,

  -- Progress details
  status VARCHAR(20) NOT NULL DEFAULT 'NOT_STARTED',
    -- NOT_STARTED, IN_PROGRESS, COMPLETED, SKIPPED
  percent_complete DECIMAL(5,2) DEFAULT 0.00,

  -- For video lectures
  last_position_seconds INT DEFAULT 0,
  total_watch_time_seconds INT DEFAULT 0,

  -- Timestamps
  first_accessed_at TIMESTAMP,
  last_accessed_at TIMESTAMP,
  completed_at TIMESTAMP,

  -- Audit
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

  -- Constraints
  CONSTRAINT chk_progress_status CHECK (status IN ('NOT_STARTED', 'IN_PROGRESS', 'COMPLETED', 'SKIPPED')),
  CONSTRAINT chk_progress_percent CHECK (percent_complete >= 0 AND percent_complete <= 100),
  CONSTRAINT chk_progress_entity CHECK (
    -- Must have at least one of module, lecture, or quiz
    module_id IS NOT NULL OR lecture_id IS NOT NULL OR quiz_id IS NOT NULL
  ),

  -- Unique constraint: one progress record per user per entity per class/course
  CONSTRAINT uq_progress_user_entity UNIQUE(
    user_id,
    course_id,
    COALESCE(class_id, '00000000-0000-0000-0000-000000000000'::UUID),
    COALESCE(module_id, '00000000-0000-0000-0000-000000000000'::UUID),
    COALESCE(lecture_id, '00000000-0000-0000-0000-000000000000'::UUID),
    COALESCE(quiz_id, '00000000-0000-0000-0000-000000000000'::UUID)
  )
);

-- Indexes for performance
CREATE INDEX idx_progress_user_course ON Progress(user_id, course_id);
CREATE INDEX idx_progress_user_class ON Progress(user_id, class_id) WHERE class_id IS NOT NULL;
CREATE INDEX idx_progress_lecture ON Progress(lecture_id) WHERE lecture_id IS NOT NULL;
CREATE INDEX idx_progress_module ON Progress(module_id) WHERE module_id IS NOT NULL;
CREATE INDEX idx_progress_quiz ON Progress(quiz_id) WHERE quiz_id IS NOT NULL;
CREATE INDEX idx_progress_status ON Progress(status);
CREATE INDEX idx_progress_updated ON Progress(updated_at DESC);

-- Function to auto-update course progress when lecture completed
CREATE OR REPLACE FUNCTION update_course_progress()
RETURNS TRIGGER AS $$
DECLARE
  total_lectures INT;
  completed_lectures INT;
  course_completion DECIMAL(5,2);
BEGIN
  IF NEW.status = 'COMPLETED' AND NEW.lecture_id IS NOT NULL THEN
    -- Count total lectures in course
    SELECT COUNT(*) INTO total_lectures
    FROM Lecture l
    JOIN Module m ON l.module_id = m.module_id
    WHERE m.course_id = NEW.course_id;

    -- Count completed lectures by this user
    SELECT COUNT(DISTINCT lecture_id) INTO completed_lectures
    FROM Progress
    WHERE user_id = NEW.user_id
      AND course_id = NEW.course_id
      AND status = 'COMPLETED'
      AND lecture_id IS NOT NULL;

    -- Calculate completion percentage
    IF total_lectures > 0 THEN
      course_completion := (completed_lectures::DECIMAL / total_lectures) * 100;

      -- Update or insert course-level progress
      INSERT INTO Progress (
        user_id, course_id, class_id, status, percent_complete, updated_at
      ) VALUES (
        NEW.user_id,
        NEW.course_id,
        NEW.class_id,
        CASE WHEN course_completion >= 100 THEN 'COMPLETED' ELSE 'IN_PROGRESS' END,
        course_completion,
        CURRENT_TIMESTAMP
      )
      ON CONFLICT ON CONSTRAINT uq_progress_user_entity
      DO UPDATE SET
        percent_complete = course_completion,
        status = CASE WHEN course_completion >= 100 THEN 'COMPLETED' ELSE 'IN_PROGRESS' END,
        updated_at = CURRENT_TIMESTAMP,
        completed_at = CASE WHEN course_completion >= 100 THEN CURRENT_TIMESTAMP ELSE NULL END;
    END IF;
  END IF;

  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_update_course_progress
AFTER INSERT OR UPDATE ON Progress
FOR EACH ROW
EXECUTE FUNCTION update_course_progress();
```

**📊 So sánh trước và sau:**

| Khía cạnh | Trước | Sau |
|-----------|-------|-----|
| Track course progress | ❌ Không | ✅ Có |
| Track module progress | ❌ Không | ✅ Có |
| Track quiz progress | ❌ Không | ✅ Có |
| Phân biệt class | ❌ Không | ✅ Có |
| Trạng thái chi tiết | ❌ Không | ✅ Có 4 trạng thái |
| Timestamp đầy đủ | ❌ Chỉ updated_at | ✅ Đầy đủ |
| Video tracking | ❌ Không | ✅ Có position, watch time |
| Auto-aggregation | ❌ Không | ✅ Tự động tính course progress |

---

### 🔴 4.2 Bảng Attempt - Quan hệ không hợp lý

**Thiết kế hiện tại:**
```sql
Attempt {
  + attempt_id : UUID PRIMARY KEY
  - quiz_id : UUID FK
  - class_id : UUID FK    -- ❌ Vấn đề ở đây
  - user_id : UUID FK
  + status : ENUM('IN_PROGRESS', 'SUBMITTED', 'GRADED')
  + started_at : DATETIME
  + submitted_at : DATETIME
  + graded_at : DATETIME
}
```

**❌ Vấn đề:**

1. **`class_id` bắt buộc** → User tự học (self-paced) không làm quiz được
2. **Thiếu `course_enrollment_id`** → Không liên kết với enrollment
3. **Thiếu tracking attempt number** → Không biết đây là lần thử thứ mấy
4. **Thiếu time tracking** → Không biết user dùng bao nhiêu thời gian
5. **Thiếu IP address, browser** → Không có audit trail cho chống gian lận

**✅ Giải pháp:**

```sql
CREATE TABLE Attempt (
  attempt_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

  -- References
  quiz_id UUID NOT NULL REFERENCES Quiz(quiz_id) ON DELETE CASCADE,
  user_id UUID NOT NULL REFERENCES User(user_id) ON DELETE CASCADE,
  course_enrollment_id UUID NOT NULL REFERENCES CourseEnrollment(courseenroll_id) ON DELETE CASCADE,
  class_id UUID REFERENCES Class(class_id) ON DELETE SET NULL,
    -- NULL = self-paced, not NULL = in-class quiz

  -- Attempt info
  attempt_number INT NOT NULL,
    -- Auto-increment per user per quiz

  -- Timing
  started_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  submitted_at TIMESTAMP,
  time_limit_minutes INT,
    -- Copied from quiz at attempt start (in case quiz settings change later)
  time_spent_seconds INT DEFAULT 0,
    -- Actual time spent

  -- Status
  status VARCHAR(20) NOT NULL DEFAULT 'IN_PROGRESS',
    -- IN_PROGRESS, SUBMITTED, GRADED, ABANDONED, EXPIRED

  -- Scoring
  auto_score DECIMAL(6,2) DEFAULT 0.00,
    -- Auto-graded questions (MCQ, True/False)
  manual_score DECIMAL(6,2),
    -- Manually graded questions (Essay, Code)
  final_score DECIMAL(6,2),
    -- Total score
  max_possible_score DECIMAL(6,2) NOT NULL,
  percentage_score DECIMAL(5,2),
    -- (final_score / max_possible_score) * 100

  -- Grading
  graded_at TIMESTAMP,
  graded_by UUID REFERENCES User(user_id),
  instructor_feedback TEXT,

  -- Security & Audit
  ip_address VARCHAR(45),
  user_agent TEXT,
  browser_fingerprint VARCHAR(255),

  -- Flags
  is_late_submission BOOLEAN DEFAULT FALSE,
  flagged_for_review BOOLEAN DEFAULT FALSE,
  flag_reason VARCHAR(500),

  -- Audit
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

  -- Constraints
  CONSTRAINT chk_attempt_status CHECK (status IN ('IN_PROGRESS', 'SUBMITTED', 'GRADED', 'ABANDONED', 'EXPIRED')),
  CONSTRAINT chk_attempt_scores CHECK (
    final_score IS NULL OR
    (final_score >= 0 AND final_score <= max_possible_score)
  ),
  CONSTRAINT uq_attempt_user_quiz_number UNIQUE(user_id, quiz_id, attempt_number)
);

-- Indexes
CREATE INDEX idx_attempt_user_quiz ON Attempt(user_id, quiz_id);
CREATE INDEX idx_attempt_quiz ON Attempt(quiz_id);
CREATE INDEX idx_attempt_status ON Attempt(status);
CREATE INDEX idx_attempt_grading ON Attempt(graded_at, graded_by) WHERE status = 'SUBMITTED';
CREATE INDEX idx_attempt_flagged ON Attempt(flagged_for_review) WHERE flagged_for_review = TRUE;

-- Function to validate attempt limit
CREATE OR REPLACE FUNCTION check_attempt_limit()
RETURNS TRIGGER AS $$
DECLARE
  max_attempts INT;
  current_attempts INT;
BEGIN
  -- Get quiz attempt limit
  SELECT attempt_limit INTO max_attempts
  FROM Quiz
  WHERE quiz_id = NEW.quiz_id;

  -- If no limit, allow
  IF max_attempts IS NULL THEN
    RETURN NEW;
  END IF;

  -- Count existing attempts
  SELECT COUNT(*) INTO current_attempts
  FROM Attempt
  WHERE user_id = NEW.user_id
    AND quiz_id = NEW.quiz_id
    AND status IN ('SUBMITTED', 'GRADED');

  -- Check limit
  IF current_attempts >= max_attempts THEN
    RAISE EXCEPTION 'Attempt limit (%) exceeded for this quiz', max_attempts;
  END IF;

  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_check_attempt_limit
BEFORE INSERT ON Attempt
FOR EACH ROW
EXECUTE FUNCTION check_attempt_limit();

-- Function to auto-calculate final score
CREATE OR REPLACE FUNCTION calculate_final_score()
RETURNS TRIGGER AS $$
BEGIN
  IF NEW.status = 'GRADED' THEN
    NEW.final_score := COALESCE(NEW.auto_score, 0) + COALESCE(NEW.manual_score, 0);
    NEW.percentage_score := (NEW.final_score / NULLIF(NEW.max_possible_score, 0)) * 100;
    NEW.graded_at := CURRENT_TIMESTAMP;
  END IF;

  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_calculate_final_score
BEFORE UPDATE ON Attempt
FOR EACH ROW
WHEN (NEW.status = 'GRADED' AND OLD.status != 'GRADED')
EXECUTE FUNCTION calculate_final_score();
```

**📊 Improvement Summary:**

| Feature | Before | After |
|---------|--------|-------|
| Self-paced support | ❌ | ✅ |
| Attempt tracking | ❌ | ✅ Number + limit validation |
| Time tracking | Basic | ✅ Detailed with time limit |
| Audit trail | ❌ | ✅ IP, browser, fingerprint |
| Auto-scoring | ❌ | ✅ Auto + manual + final |
| Cheating detection | ❌ | ✅ Flagging system |

---

### 🔴 4.3 Bảng Thread - Phân loại không rõ ràng

**Thiết kế hiện tại:**
```sql
Thread {
  + thread_id : UUID PRIMARY KEY
  - class_id : UUID FK
  - lecture_id : UUID FK
  - user_id : UUID FK (creator)
  + title : VARCHAR(200)
}
```

**❌ Vấn đề:**

1. **Cả `class_id` và `lecture_id` đều nullable** → Không rõ thread thuộc về đâu
2. **Không phân loại thread type** → Không phân biệt in-class vs off-topic
3. **Thiếu cơ chế pin, lock** → Không quản lý được thread quan trọng
4. **Thiếu tags** → Khó tìm kiếm và phân loại
5. **Không track views, replies** → Không biết mức độ hoạt động

**✅ Giải pháp:**

```sql
CREATE TABLE Thread (
  thread_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

  -- Creator
  user_id UUID NOT NULL REFERENCES User(user_id) ON DELETE CASCADE,

  -- Context (at least one must be set)
  course_id UUID REFERENCES Course(course_id) ON DELETE CASCADE,
  class_id UUID REFERENCES Class(class_id) ON DELETE CASCADE,
  lecture_id UUID REFERENCES Lecture(lecture_id) ON DELETE CASCADE,

  -- Type & Category
  thread_type VARCHAR(30) NOT NULL,
    -- CLASS_DISCUSSION: Thảo luận trong lớp về nội dung học
    -- LECTURE_QA: Hỏi đáp về bài giảng cụ thể
    -- OFF_TOPIC: Thảo luận ngoài lề (career, news, resources)
    -- ANNOUNCEMENT: Thông báo từ giảng viên
    -- STUDY_GROUP: Tổ chức nhóm học

  -- Content
  title VARCHAR(200) NOT NULL,
  content TEXT,
  tags VARCHAR(100)[],
    -- e.g., ['javascript', 'async', 'promises']

  -- Status & Moderation
  status VARCHAR(20) DEFAULT 'OPEN',
    -- OPEN, CLOSED, LOCKED, DELETED
  is_pinned BOOLEAN DEFAULT FALSE,
  is_announcement BOOLEAN DEFAULT FALSE,

  -- Access control
  visibility VARCHAR(20) DEFAULT 'CLASS',
    -- PUBLIC: Anyone can view
    -- CLASS: Only class members
    -- PRIVATE: Only specific users (via ThreadParticipant)

  -- Tracking
  view_count INT DEFAULT 0,
  reply_count INT DEFAULT 0,
  last_activity_at TIMESTAMP,
  last_post_id UUID,
    -- For quick access to latest post

  -- Best answer (for Q&A threads)
  accepted_answer_post_id UUID,

  -- Audit
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  deleted_at TIMESTAMP,

  -- Constraints
  CONSTRAINT chk_thread_type CHECK (thread_type IN (
    'CLASS_DISCUSSION', 'LECTURE_QA', 'OFF_TOPIC',
    'ANNOUNCEMENT', 'STUDY_GROUP'
  )),
  CONSTRAINT chk_thread_status CHECK (status IN ('OPEN', 'CLOSED', 'LOCKED', 'DELETED')),
  CONSTRAINT chk_thread_visibility CHECK (visibility IN ('PUBLIC', 'CLASS', 'PRIVATE')),
  CONSTRAINT chk_thread_context CHECK (
    -- Must have at least one context
    course_id IS NOT NULL OR class_id IS NOT NULL OR lecture_id IS NOT NULL
  )
);

-- Indexes
CREATE INDEX idx_thread_course ON Thread(course_id) WHERE course_id IS NOT NULL;
CREATE INDEX idx_thread_class ON Thread(class_id) WHERE class_id IS NOT NULL;
CREATE INDEX idx_thread_lecture ON Thread(lecture_id) WHERE lecture_id IS NOT NULL;
CREATE INDEX idx_thread_user ON Thread(user_id);
CREATE INDEX idx_thread_type ON Thread(thread_type);
CREATE INDEX idx_thread_tags ON Thread USING GIN(tags);
CREATE INDEX idx_thread_activity ON Thread(last_activity_at DESC);
CREATE INDEX idx_thread_pinned ON Thread(is_pinned, created_at DESC) WHERE is_pinned = TRUE;
CREATE INDEX idx_thread_status ON Thread(status);

-- Full-text search index
CREATE INDEX idx_thread_search ON Thread USING GIN(
  to_tsvector('english', title || ' ' || COALESCE(content, ''))
);

-- Thread participants (for private threads)
CREATE TABLE ThreadParticipant (
  participant_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  thread_id UUID NOT NULL REFERENCES Thread(thread_id) ON DELETE CASCADE,
  user_id UUID NOT NULL REFERENCES User(user_id) ON DELETE CASCADE,
  role VARCHAR(20) DEFAULT 'MEMBER',
    -- OWNER, MODERATOR, MEMBER
  can_post BOOLEAN DEFAULT TRUE,
  joined_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT uq_thread_participant UNIQUE(thread_id, user_id)
);

CREATE INDEX idx_thread_participant ON ThreadParticipant(thread_id, user_id);
```

**📊 Comparison:**

| Feature | Before | After |
|---------|--------|-------|
| Thread classification | ❌ Unclear | ✅ 5 clear types |
| Context tracking | ⚠️ Ambiguous | ✅ Clear hierarchy |
| Moderation | ❌ No | ✅ Pin, lock, close |
| Access control | ❌ No | ✅ 3 visibility levels |
| Engagement metrics | ❌ No | ✅ Views, replies |
| Search | ❌ No | ✅ Full-text + tags |
| Private discussions | ❌ No | ✅ ThreadParticipant |

---

### 🔴 4.4 Bảng Submission - Thiếu thông tin quan trọng

**Thiết kế hiện tại:**
```sql
Submission {
  + submission_id : UUID PRIMARY KEY
  - attempt_id : UUID FK
  - question_id : UUID FK
  - option_id : UUID FK (nullable - for MCQ)
  + answer_text : TEXT
  + auto_score : DECIMAL(5,2)
  + manual_score : DECIMAL(5,2)
  + final_score : DECIMAL(5,2)
}
```

**❌ Vấn đề:**

1. **Không lưu file upload** → Câu hỏi yêu cầu upload không làm được
2. **Thiếu timestamp** → Không biết khi nào submit
3. **Không có instructor feedback** → Không giải thích điểm
4. **Multiple options support** → MCQ nhiều đáp án đúng không xử lý được
5. **Không lưu original question** → Nếu question bị sửa/xóa thì mất context

**✅ Giải pháp:**

```sql
CREATE TABLE Submission (
  submission_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

  -- References
  attempt_id UUID NOT NULL REFERENCES Attempt(attempt_id) ON DELETE CASCADE,
  question_id UUID NOT NULL REFERENCES Question(question_id) ON DELETE RESTRICT,
    -- RESTRICT to prevent deleting questions with submissions

  -- Question snapshot (in case question is edited later)
  question_snapshot JSON,
    -- Store question text, options, correct answer at time of submission

  -- Answer (different types)
  answer_text TEXT,
    -- For ESSAY, SHORT_ANSWER

  selected_option_ids UUID[],
    -- For MCQ, TRUE_FALSE (can be multiple for multi-select MCQ)

  uploaded_files JSON,
    -- For FILE_UPLOAD: [{"filename": "...", "url": "...", "size": ...}]

  code_submission TEXT,
    -- For CODE questions

  code_output TEXT,
    -- Result of code execution

  -- Timing
  submitted_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  time_spent_seconds INT DEFAULT 0,

  -- Scoring
  max_points DECIMAL(5,2) NOT NULL,
    -- Copied from question at submission time

  auto_score DECIMAL(5,2),
    -- Auto-graded (MCQ, TRUE_FALSE, CODE with test cases)

  manual_score DECIMAL(5,2),
    -- Manually graded (ESSAY, CODE review)

  final_score DECIMAL(5,2),
    -- final_score = COALESCE(manual_score, auto_score)

  -- Feedback
  auto_feedback TEXT,
    -- System-generated feedback

  instructor_feedback TEXT,
    -- Manual feedback from instructor

  rubric_scores JSON,
    -- For rubric-based grading: {"criteria1": 3, "criteria2": 5, ...}

  -- Grading
  graded_at TIMESTAMP,
  graded_by UUID REFERENCES User(user_id),

  -- Flags
  flagged_for_review BOOLEAN DEFAULT FALSE,
  flag_reason VARCHAR(500),

  -- Audit
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

  -- Constraints
  CONSTRAINT chk_submission_scores CHECK (
    final_score IS NULL OR
    (final_score >= 0 AND final_score <= max_points)
  ),
  CONSTRAINT chk_submission_answer CHECK (
    -- Must have at least one type of answer
    answer_text IS NOT NULL OR
    selected_option_ids IS NOT NULL OR
    uploaded_files IS NOT NULL OR
    code_submission IS NOT NULL
  )
);

-- Indexes
CREATE INDEX idx_submission_attempt ON Submission(attempt_id);
CREATE INDEX idx_submission_question ON Submission(question_id);
CREATE INDEX idx_submission_grading ON Submission(graded_at, graded_by)
  WHERE manual_score IS NULL AND instructor_feedback IS NULL;
CREATE INDEX idx_submission_flagged ON Submission(flagged_for_review)
  WHERE flagged_for_review = TRUE;

-- Junction table for selected options (alternative to array)
CREATE TABLE SubmissionOption (
  submission_option_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  submission_id UUID NOT NULL REFERENCES Submission(submission_id) ON DELETE CASCADE,
  option_id UUID NOT NULL REFERENCES Option(option_id) ON DELETE RESTRICT,

  CONSTRAINT uq_submission_option UNIQUE(submission_id, option_id)
);

CREATE INDEX idx_submission_option ON SubmissionOption(submission_id);

-- Function to auto-grade MCQ
CREATE OR REPLACE FUNCTION auto_grade_mcq()
RETURNS TRIGGER AS $$
DECLARE
  question_type VARCHAR(20);
  correct_option_ids UUID[];
  is_correct BOOLEAN;
BEGIN
  -- Get question type
  SELECT type INTO question_type
  FROM Question
  WHERE question_id = NEW.question_id;

  -- Auto-grade only for MCQ and TRUE_FALSE
  IF question_type IN ('MCQ', 'TRUE_FALSE') THEN
    -- Get correct option IDs
    SELECT ARRAY_AGG(option_id) INTO correct_option_ids
    FROM Option
    WHERE question_id = NEW.question_id
      AND is_correct = TRUE;

    -- Check if selected options match correct options
    IF NEW.selected_option_ids @> correct_option_ids
       AND NEW.selected_option_ids <@ correct_option_ids THEN
      -- All correct, no extra selections
      NEW.auto_score := NEW.max_points;
      NEW.auto_feedback := 'Correct!';
    ELSE
      NEW.auto_score := 0;
      NEW.auto_feedback := 'Incorrect. Please review the material.';
    END IF;

    -- Set final score if no manual grading needed
    NEW.final_score := NEW.auto_score;
  END IF;

  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_auto_grade_mcq
BEFORE INSERT ON Submission
FOR EACH ROW
EXECUTE FUNCTION auto_grade_mcq();

-- Function to update attempt score when submissions are graded
CREATE OR REPLACE FUNCTION update_attempt_score()
RETURNS TRIGGER AS $$
DECLARE
  total_score DECIMAL(6,2);
  max_score DECIMAL(6,2);
BEGIN
  -- Calculate total score for this attempt
  SELECT
    COALESCE(SUM(COALESCE(final_score, 0)), 0),
    COALESCE(SUM(max_points), 0)
  INTO total_score, max_score
  FROM Submission
  WHERE attempt_id = NEW.attempt_id;

  -- Update attempt
  UPDATE Attempt
  SET
    final_score = total_score,
    max_possible_score = max_score,
    percentage_score = CASE
      WHEN max_score > 0 THEN (total_score / max_score) * 100
      ELSE 0
    END,
    updated_at = CURRENT_TIMESTAMP
  WHERE attempt_id = NEW.attempt_id;

  -- Check if all submissions are graded
  IF NOT EXISTS (
    SELECT 1 FROM Submission
    WHERE attempt_id = NEW.attempt_id
      AND final_score IS NULL
  ) THEN
    UPDATE Attempt
    SET status = 'GRADED',
        graded_at = CURRENT_TIMESTAMP
    WHERE attempt_id = NEW.attempt_id
      AND status = 'SUBMITTED';
  END IF;

  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_update_attempt_score
AFTER INSERT OR UPDATE OF final_score ON Submission
FOR EACH ROW
EXECUTE FUNCTION update_attempt_score();
```

**💡 Key Improvements:**

1. ✅ Support all question types (MCQ, Essay, Code, File upload)
2. ✅ Multiple correct answers for MCQ
3. ✅ Question snapshot for historical accuracy
4. ✅ Auto-grading with feedback
5. ✅ Rubric-based grading support
6. ✅ Code execution results
7. ✅ File upload support
8. ✅ Automatic attempt score calculation

---

### 🔴 4.5 Thiếu bảng Certificate

**Vấn đề:**
Tài liệu yêu cầu "Cấp chứng chỉ tự động khi hoàn thành đầy đủ yêu cầu" nhưng không có bảng Certificate trong ERD.

**✅ Giải pháp:**

```sql
CREATE TABLE Certificate (
  certificate_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

  -- References
  user_id UUID NOT NULL REFERENCES User(user_id) ON DELETE CASCADE,
  course_id UUID NOT NULL REFERENCES Course(course_id) ON DELETE CASCADE,
  course_enrollment_id UUID NOT NULL REFERENCES CourseEnrollment(courseenroll_id) ON DELETE CASCADE,

  -- Certificate info
  certificate_code VARCHAR(50) NOT NULL UNIQUE,
    -- e.g., "CERT-2025-ABC123"

  title VARCHAR(200) NOT NULL,
    -- e.g., "Certificate of Completion: Advanced JavaScript"

  description TEXT,

  -- Template & Design
  template_id UUID,
    -- Reference to certificate template (if you have template system)

  -- Achievement details
  final_grade DECIMAL(5,2),
  grade_letter VARCHAR(5),
    -- A+, A, B+, B, etc.

  completion_date DATE NOT NULL,
  issue_date DATE NOT NULL DEFAULT CURRENT_DATE,

  -- Files
  pdf_url VARCHAR(500),
    -- Generated PDF certificate

  image_url VARCHAR(500),
    -- Certificate as image (for sharing on social media)

  -- Verification
  verification_code VARCHAR(100) UNIQUE,
    -- For external verification: https://yoursite.com/verify/{code}

  verification_url VARCHAR(500),

  qr_code_url VARCHAR(500),
    -- QR code for mobile verification

  -- Validity
  valid_from DATE NOT NULL DEFAULT CURRENT_DATE,
  valid_until DATE,
    -- NULL =永久有效

  -- Status
  status VARCHAR(20) DEFAULT 'ACTIVE',
    -- ACTIVE, REVOKED, EXPIRED

  revoked_at TIMESTAMP,
  revoked_by UUID REFERENCES User(user_id),
  revoke_reason TEXT,

  -- Metadata
  metadata JSON,
    -- Additional info: skills earned, hours completed, etc.

  -- Audit
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

  -- Constraints
  CONSTRAINT chk_certificate_status CHECK (status IN ('ACTIVE', 'REVOKED', 'EXPIRED')),
  CONSTRAINT uq_certificate_user_course UNIQUE(user_id, course_id)
    -- One certificate per user per course
);

-- Indexes
CREATE INDEX idx_certificate_user ON Certificate(user_id);
CREATE INDEX idx_certificate_course ON Certificate(course_id);
CREATE INDEX idx_certificate_code ON Certificate(certificate_code);
CREATE INDEX idx_certificate_verification ON Certificate(verification_code);
CREATE INDEX idx_certificate_issued ON Certificate(issue_date DESC);
CREATE INDEX idx_certificate_status ON Certificate(status);

-- Certificate templates
CREATE TABLE CertificateTemplate (
  template_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

  -- Template info
  name VARCHAR(100) NOT NULL,
  description TEXT,

  -- Design
  template_type VARCHAR(20) DEFAULT 'COMPLETION',
    -- COMPLETION, EXCELLENCE, PARTICIPATION

  background_image_url VARCHAR(500),
  logo_url VARCHAR(500),

  -- Layout (JSON with position, font, color info)
  layout_config JSON,
    -- {"title_position": {"x": 100, "y": 200}, "font": "Arial", ...}

  -- Template files
  html_template TEXT,
  css_styles TEXT,

  -- Status
  is_active BOOLEAN DEFAULT TRUE,
  is_default BOOLEAN DEFAULT FALSE,

  -- Audit
  created_by UUID REFERENCES User(user_id),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Function to auto-issue certificate when course completed
CREATE OR REPLACE FUNCTION auto_issue_certificate()
RETURNS TRIGGER AS $$
DECLARE
  cert_code VARCHAR(50);
  verify_code VARCHAR(100);
  course_title VARCHAR(200);
  student_name VARCHAR(200);
BEGIN
  -- Only proceed if enrollment just became COMPLETED
  IF NEW.enrollment_status = 'COMPLETED' AND
     (OLD.enrollment_status IS NULL OR OLD.enrollment_status != 'COMPLETED') THEN

    -- Generate unique codes
    cert_code := 'CERT-' || TO_CHAR(CURRENT_DATE, 'YYYY') || '-' ||
                 UPPER(SUBSTR(MD5(RANDOM()::TEXT), 1, 8));
    verify_code := UPPER(SUBSTR(MD5(RANDOM()::TEXT || NEW.user_id::TEXT), 1, 16));

    -- Get course title
    SELECT title INTO course_title
    FROM Course
    WHERE course_id = NEW.course_id;

    -- Get student name
    SELECT first_name || ' ' || last_name INTO student_name
    FROM User
    WHERE user_id = NEW.user_id;

    -- Create certificate
    INSERT INTO Certificate (
      user_id,
      course_id,
      course_enrollment_id,
      certificate_code,
      verification_code,
      title,
      final_grade,
      completion_date,
      verification_url
    ) VALUES (
      NEW.user_id,
      NEW.course_id,
      NEW.courseenroll_id,
      cert_code,
      verify_code,
      'Certificate of Completion: ' || course_title,
      NEW.final_grade,
      NEW.completed_at::DATE,
      'https://yoursite.com/verify/' || verify_code
    );

    -- TODO: Trigger PDF generation and email notification

  END IF;

  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_auto_issue_certificate
AFTER UPDATE ON CourseEnrollment
FOR EACH ROW
EXECUTE FUNCTION auto_issue_certificate();
```

---

## 5. VẤN ĐỀ CẦN CẢI THIỆN

### 🟡 5.1 CourseEnrollment - Thiếu tracking chi tiết

**Thiết kế hiện tại:**
```sql
CourseEnrollment {
  + courseenroll_id : UUID
  - course_id : UUID
  - user_id : UUID
  - enrolled_at : DATETIME
  + role_in_course : ENUM('STUDENT', 'TA', 'INSTRUCTOR')
}
```

**⚠️ Thiếu:**
- Enrollment status (active, completed, dropped)
- Completion tracking
- Final grade
- Drop reason

**✅ Đề xuất bổ sung:**

```sql
ALTER TABLE CourseEnrollment
ADD COLUMN enrollment_status VARCHAR(20) DEFAULT 'ACTIVE',
ADD COLUMN completed_at TIMESTAMP,
ADD COLUMN completion_percentage DECIMAL(5,2) DEFAULT 0.00,
ADD COLUMN final_grade DECIMAL(5,2),
ADD COLUMN grade_letter VARCHAR(5),
ADD COLUMN certificate_issued_at TIMESTAMP,
ADD COLUMN dropped_at TIMESTAMP,
ADD COLUMN drop_reason TEXT,
ADD COLUMN last_accessed_at TIMESTAMP,
ADD COLUMN total_time_spent_minutes INT DEFAULT 0,
ADD CONSTRAINT chk_enrollment_status CHECK (
  enrollment_status IN ('ACTIVE', 'COMPLETED', 'DROPPED', 'SUSPENDED', 'EXPIRED')
);

CREATE INDEX idx_enrollment_status ON CourseEnrollment(enrollment_status);
CREATE INDEX idx_enrollment_user_active ON CourseEnrollment(user_id, enrollment_status)
  WHERE enrollment_status = 'ACTIVE';
```

### 🟡 5.2 ClassEnrollment - Quan hệ với CourseEnrollment

**Vấn đề:**
Có cả CourseEnrollment và ClassEnrollment nhưng không rõ mối quan hệ:
- User phải enroll Course trước khi enroll Class?
- Hay enroll Class tự động enroll Course?

**✅ Đề xuất:**

```sql
ALTER TABLE ClassEnrollment
ADD COLUMN course_enrollment_id UUID REFERENCES CourseEnrollment(courseenroll_id);

-- Constraint: Must enroll in course before enrolling in class
ALTER TABLE ClassEnrollment
ADD CONSTRAINT chk_class_enrollment_course
CHECK (
  EXISTS (
    SELECT 1 FROM CourseEnrollment ce
    JOIN Class c ON c.course_id = ce.course_id
    WHERE ce.courseenroll_id = course_enrollment_id
      AND c.class_id = ClassEnrollment.class_id
  )
);
```

### 🟡 5.3 Post - Thiếu tính năng social

**Thiết kế hiện tại:**
```sql
Post {
  + post_id : UUID
  - thread_id : UUID
  - author_id : UUID
  + content : TEXT
  + votes : INT
  + is_answer : BOOLEAN
}
```

**⚠️ Vấn đề:**
1. `votes` là INT → Không biết ai đã vote
2. Không có nested replies
3. Không có edit history
4. Thiếu mention/tagging

**✅ Đề xuất:**

```sql
-- Bảng Post cải tiến
ALTER TABLE Post DROP COLUMN votes;
ALTER TABLE Post
ADD COLUMN parent_post_id UUID REFERENCES Post(post_id),
ADD COLUMN reply_count INT DEFAULT 0,
ADD COLUMN upvote_count INT DEFAULT 0,
ADD COLUMN downvote_count INT DEFAULT 0,
ADD COLUMN is_edited BOOLEAN DEFAULT FALSE,
ADD COLUMN edited_at TIMESTAMP,
ADD COLUMN mentioned_users UUID[],
ADD COLUMN attachments JSON;

-- Bảng PostVote riêng
CREATE TABLE PostVote (
  vote_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  post_id UUID NOT NULL REFERENCES Post(post_id) ON DELETE CASCADE,
  user_id UUID NOT NULL REFERENCES User(user_id) ON DELETE CASCADE,
  vote_type VARCHAR(10) NOT NULL CHECK (vote_type IN ('UPVOTE', 'DOWNVOTE')),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT uq_post_vote UNIQUE(post_id, user_id)
);

CREATE INDEX idx_post_vote ON PostVote(post_id);
CREATE INDEX idx_user_vote ON PostVote(user_id);

-- Trigger to update vote counts
CREATE OR REPLACE FUNCTION update_post_votes()
RETURNS TRIGGER AS $$
BEGIN
  IF TG_OP = 'INSERT' THEN
    IF NEW.vote_type = 'UPVOTE' THEN
      UPDATE Post SET upvote_count = upvote_count + 1 WHERE post_id = NEW.post_id;
    ELSE
      UPDATE Post SET downvote_count = downvote_count + 1 WHERE post_id = NEW.post_id;
    END IF;
  ELSIF TG_OP = 'DELETE' THEN
    IF OLD.vote_type = 'UPVOTE' THEN
      UPDATE Post SET upvote_count = upvote_count - 1 WHERE post_id = OLD.post_id;
    ELSE
      UPDATE Post SET downvote_count = downvote_count - 1 WHERE post_id = OLD.post_id;
    END IF;
  ELSIF TG_OP = 'UPDATE' AND NEW.vote_type != OLD.vote_type THEN
    IF NEW.vote_type = 'UPVOTE' THEN
      UPDATE Post
      SET upvote_count = upvote_count + 1, downvote_count = downvote_count - 1
      WHERE post_id = NEW.post_id;
    ELSE
      UPDATE Post
      SET upvote_count = upvote_count - 1, downvote_count = downvote_count + 1
      WHERE post_id = NEW.post_id;
    END IF;
  END IF;

  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_update_post_votes
AFTER INSERT OR UPDATE OR DELETE ON PostVote
FOR EACH ROW
EXECUTE FUNCTION update_post_votes();

-- Edit history
CREATE TABLE PostEditHistory (
  edit_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  post_id UUID NOT NULL REFERENCES Post(post_id) ON DELETE CASCADE,
  old_content TEXT NOT NULL,
  edited_by UUID NOT NULL REFERENCES User(user_id),
  edited_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  edit_reason VARCHAR(500)
);

CREATE INDEX idx_post_edit_history ON PostEditHistory(post_id, edited_at DESC);
```

---

## 6. THIẾU SÓT VỀ MỐI QUAN HỆ

### 6.1 Quan hệ Class ↔ Course

**Vấn đề hiện tại:**
```
Class (n) → (1) Course
```

Một Class chỉ có thể sử dụng 1 Course. Nếu muốn kết hợp nhiều Course trong 1 Class (ví dụ: lớp "Full-stack Development" kết hợp Course "Frontend" và "Backend") thì không được.

**💡 Đề xuất:**

Nếu cần linh hoạt hơn, tạo bảng trung gian:

```sql
CREATE TABLE ClassCourse (
  class_course_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  class_id UUID NOT NULL REFERENCES Class(class_id) ON DELETE CASCADE,
  course_id UUID NOT NULL REFERENCES Course(course_id) ON DELETE CASCADE,
  order_num INT NOT NULL,
  is_primary BOOLEAN DEFAULT FALSE,

  CONSTRAINT uq_class_course UNIQUE(class_id, course_id),
  CONSTRAINT uq_class_course_order UNIQUE(class_id, order_num)
);
```

Tuy nhiên, nếu logic nghiệp vụ là **1 Class = 1 Course**, thì thiết kế hiện tại là hợp lý.

### 6.2 Thiếu quan hệ Schedule ↔ Lecture

**Vấn đề:**
Schedule chỉ liên kết với Class, không biết Schedule đó dạy Lecture/Module nào.

**✅ Đề xuất:**

```sql
ALTER TABLE Schedule
ADD COLUMN module_id UUID REFERENCES Module(module_id),
ADD COLUMN lecture_id UUID REFERENCES Lecture(lecture_id),
ADD COLUMN topics_covered TEXT[],
ADD COLUMN homework_assigned TEXT;
```

---

## 7. THIẾU CÁC BẢNG HỖ TRỢ

### 7.1 Bảng Notification - Thiếu hoàn toàn

**Yêu cầu:** "Thông báo về thread mới, câu trả lời cho bài viết của mình"

**✅ Giải pháp:**

```sql
CREATE TABLE Notification (
  notification_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

  -- Recipient
  user_id UUID NOT NULL REFERENCES User(user_id) ON DELETE CASCADE,

  -- Type
  notification_type VARCHAR(50) NOT NULL,
    -- THREAD_REPLY, POST_MENTION, QUIZ_GRADED, COURSE_UPDATE,
    -- CLASS_REMINDER, ASSIGNMENT_DUE, CERTIFICATE_ISSUED, etc.

  -- Content
  title VARCHAR(200) NOT NULL,
  message TEXT NOT NULL,

  -- Related entity (polymorphic association)
  related_entity_type VARCHAR(50),
    -- Thread, Post, Quiz, Attempt, Certificate, etc.
  related_entity_id UUID,

  -- Action
  action_url VARCHAR(500),
    -- Deep link to the related content
  action_label VARCHAR(100),
    -- e.g., "View Thread", "See Results", "Download Certificate"

  -- Status
  is_read BOOLEAN DEFAULT FALSE,
  read_at TIMESTAMP,

  -- Priority
  priority VARCHAR(20) DEFAULT 'NORMAL',
    -- HIGH, NORMAL, LOW

  -- Channels
  sent_via_email BOOLEAN DEFAULT FALSE,
  sent_via_push BOOLEAN DEFAULT FALSE,
  sent_via_sms BOOLEAN DEFAULT FALSE,

  -- Audit
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  expires_at TIMESTAMP,

  CONSTRAINT chk_notification_priority CHECK (priority IN ('HIGH', 'NORMAL', 'LOW'))
);

-- Indexes
CREATE INDEX idx_notification_user ON Notification(user_id, is_read, created_at DESC);
CREATE INDEX idx_notification_unread ON Notification(user_id, created_at DESC)
  WHERE is_read = FALSE;
CREATE INDEX idx_notification_entity ON Notification(related_entity_type, related_entity_id);

-- User notification preferences
CREATE TABLE NotificationPreference (
  preference_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID NOT NULL REFERENCES User(user_id) ON DELETE CASCADE,

  notification_type VARCHAR(50) NOT NULL,

  -- Channels enabled
  email_enabled BOOLEAN DEFAULT TRUE,
  push_enabled BOOLEAN DEFAULT TRUE,
  sms_enabled BOOLEAN DEFAULT FALSE,

  -- Frequency
  frequency VARCHAR(20) DEFAULT 'IMMEDIATE',
    -- IMMEDIATE, DAILY_DIGEST, WEEKLY_DIGEST, NEVER

  CONSTRAINT uq_user_notification_type UNIQUE(user_id, notification_type)
);
```

### 7.2 Bảng ActivityLog - Audit Trail

**Mục đích:** Track mọi hành động quan trọng trong hệ thống

```sql
CREATE TABLE ActivityLog (
  log_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

  -- Who
  user_id UUID REFERENCES User(user_id) ON DELETE SET NULL,
    -- NULL if system action

  -- What
  action VARCHAR(100) NOT NULL,
    -- CREATE_COURSE, UPDATE_MODULE, DELETE_QUESTION,
    -- SUBMIT_QUIZ, GRADE_SUBMISSION, etc.

  -- Where (entity affected)
  entity_type VARCHAR(50) NOT NULL,
  entity_id UUID NOT NULL,

  -- Details
  description TEXT,

  -- Changes (for updates)
  old_values JSON,
  new_values JSON,

  -- Context
  ip_address VARCHAR(45),
  user_agent TEXT,
  session_id UUID,

  -- Metadata
  metadata JSON,

  -- Audit
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

  -- Partitioning hint
  log_date DATE GENERATED ALWAYS AS (created_at::DATE) STORED
);

-- Indexes
CREATE INDEX idx_activity_log_user ON ActivityLog(user_id, created_at DESC);
CREATE INDEX idx_activity_log_entity ON ActivityLog(entity_type, entity_id, created_at DESC);
CREATE INDEX idx_activity_log_action ON ActivityLog(action, created_at DESC);
CREATE INDEX idx_activity_log_date ON ActivityLog(log_date DESC);

-- Partition by month for better performance
-- (Implementation depends on database - this is PostgreSQL syntax)
CREATE TABLE ActivityLog_2025_01 PARTITION OF ActivityLog
  FOR VALUES FROM ('2025-01-01') TO ('2025-02-01');
```

### 7.3 Bảng File - Media Management

**Mục đích:** Quản lý tập trung tất cả files upload

```sql
CREATE TABLE File (
  file_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

  -- Uploader
  uploaded_by UUID NOT NULL REFERENCES User(user_id) ON DELETE CASCADE,

  -- File info
  original_filename VARCHAR(255) NOT NULL,
  stored_filename VARCHAR(255) NOT NULL,
    -- e.g., "abc123-original-name.pdf"

  file_path VARCHAR(500) NOT NULL,
    -- Relative or absolute path

  file_url VARCHAR(500) NOT NULL,
    -- Full URL for access

  -- Metadata
  file_size_bytes BIGINT NOT NULL,
  mime_type VARCHAR(100) NOT NULL,
  file_extension VARCHAR(20),

  -- Checksums
  md5_hash VARCHAR(32),
  sha256_hash VARCHAR(64),

  -- Related entity (polymorphic)
  entity_type VARCHAR(50),
    -- Lecture, Resource, Submission, Post, etc.
  entity_id UUID,

  -- Storage
  storage_type VARCHAR(20) DEFAULT 'LOCAL',
    -- LOCAL, S3, AZURE, GCS
  storage_bucket VARCHAR(100),

  -- Processing (for videos/images)
  processing_status VARCHAR(20) DEFAULT 'COMPLETED',
    -- PENDING, PROCESSING, COMPLETED, FAILED
  thumbnail_url VARCHAR(500),

  -- Access control
  is_public BOOLEAN DEFAULT FALSE,
  access_token VARCHAR(255),
    -- For private file access

  -- Lifecycle
  is_deleted BOOLEAN DEFAULT FALSE,
  deleted_at TIMESTAMP,

  -- Audit
  uploaded_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  last_accessed_at TIMESTAMP,
  access_count INT DEFAULT 0,

  CONSTRAINT chk_file_storage CHECK (storage_type IN ('LOCAL', 'S3', 'AZURE', 'GCS'))
);

-- Indexes
CREATE INDEX idx_file_uploaded_by ON File(uploaded_by);
CREATE INDEX idx_file_entity ON File(entity_type, entity_id);
CREATE INDEX idx_file_deleted ON File(is_deleted) WHERE is_deleted = FALSE;
CREATE INDEX idx_file_hash ON File(md5_hash);
```

### 7.4 Bảng SystemSettings - Configuration

**Mục đích:** Lưu cấu hình hệ thống có thể thay đổi

```sql
CREATE TABLE SystemSettings (
  setting_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

  -- Key
  setting_key VARCHAR(100) NOT NULL UNIQUE,
    -- e.g., "max_upload_size_mb", "session_timeout_minutes"

  -- Value
  setting_value TEXT NOT NULL,

  -- Type hint for parsing
  data_type VARCHAR(20) DEFAULT 'STRING',
    -- STRING, INTEGER, DECIMAL, BOOLEAN, JSON, DATE

  -- Metadata
  category VARCHAR(50),
    -- GENERAL, SECURITY, EMAIL, STORAGE, etc.

  description TEXT,
  default_value TEXT,

  -- Validation
  validation_regex VARCHAR(500),
  min_value DECIMAL(20,2),
  max_value DECIMAL(20,2),
  allowed_values TEXT[],

  -- Status
  is_editable BOOLEAN DEFAULT TRUE,
  is_sensitive BOOLEAN DEFAULT FALSE,
    -- Hide value in UI (passwords, API keys)

  -- Audit
  updated_by UUID REFERENCES User(user_id),
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT chk_setting_data_type CHECK (
    data_type IN ('STRING', 'INTEGER', 'DECIMAL', 'BOOLEAN', 'JSON', 'DATE')
  )
);

-- Sample data
INSERT INTO SystemSettings (setting_key, setting_value, data_type, category, description) VALUES
('max_upload_size_mb', '100', 'INTEGER', 'STORAGE', 'Maximum file upload size in MB'),
('session_timeout_minutes', '30', 'INTEGER', 'SECURITY', 'User session timeout in minutes'),
('allow_self_registration', 'true', 'BOOLEAN', 'GENERAL', 'Allow users to self-register'),
('default_quiz_time_limit', '60', 'INTEGER', 'ASSESSMENT', 'Default quiz time limit in minutes');
```

---

## 8. VẤN ĐỀ VỀ INDEXES VÀ CONSTRAINTS

### 8.1 Foreign Key Constraints

**Vấn đề:** Tài liệu không đề cập đến ON DELETE, ON UPDATE behavior

**✅ Đề xuất:**

```sql
-- Example: Proper cascade behavior
ALTER TABLE Progress
ADD CONSTRAINT fk_progress_user
  FOREIGN KEY (user_id)
  REFERENCES User(user_id)
  ON DELETE CASCADE;  -- Delete progress when user deleted

ALTER TABLE Progress
ADD CONSTRAINT fk_progress_lecture
  FOREIGN KEY (lecture_id)
  REFERENCES Lecture(lecture_id)
  ON DELETE CASCADE;  -- Delete progress when lecture deleted

ALTER TABLE Submission
ADD CONSTRAINT fk_submission_question
  FOREIGN KEY (question_id)
  REFERENCES Question(question_id)
  ON DELETE RESTRICT;  -- Prevent deleting questions with submissions

ALTER TABLE Class
ADD CONSTRAINT fk_class_instructor
  FOREIGN KEY (instructor_id)
  REFERENCES User(user_id)
  ON DELETE SET NULL;  -- Keep class if instructor deleted
```

**Guideline:**
- `ON DELETE CASCADE`: Khi xóa parent, xóa luôn children (Progress, Resource)
- `ON DELETE RESTRICT`: Không cho xóa parent nếu còn children (Question có Submission)
- `ON DELETE SET NULL`: Giữ record nhưng clear foreign key (Instructor bị xóa)

### 8.2 Unique Constraints

```sql
-- User email must be unique
ALTER TABLE User ADD CONSTRAINT uq_user_email UNIQUE(email);

-- Course code must be unique
ALTER TABLE Course ADD CONSTRAINT uq_course_code UNIQUE(code);

-- Certificate code must be unique
ALTER TABLE Certificate ADD CONSTRAINT uq_certificate_code UNIQUE(certificate_code);

-- User can't enroll in same course twice (unless dropped and re-enrolled)
ALTER TABLE CourseEnrollment
ADD CONSTRAINT uq_active_enrollment
UNIQUE(user_id, course_id, enrollment_status)
WHERE enrollment_status IN ('ACTIVE', 'COMPLETED');

-- User can't vote twice on same post
-- (Already handled in PostVote table)

-- Module order must be unique within course
ALTER TABLE Module
ADD CONSTRAINT uq_module_order
UNIQUE(course_id, order_num);
```

### 8.3 Check Constraints

```sql
-- Progress percentage must be 0-100
ALTER TABLE Progress
ADD CONSTRAINT chk_progress_percent
CHECK (percent_complete >= 0 AND percent_complete <= 100);

-- Score must be within valid range
ALTER TABLE Submission
ADD CONSTRAINT chk_submission_score
CHECK (final_score IS NULL OR (final_score >= 0 AND final_score <= max_points));

-- Quiz attempt limit must be positive
ALTER TABLE Quiz
ADD CONSTRAINT chk_quiz_attempts
CHECK (attempt_limit IS NULL OR attempt_limit > 0);

-- Lecture duration must be positive
ALTER TABLE Lecture
ADD CONSTRAINT chk_lecture_duration
CHECK (duration_seconds IS NULL OR duration_seconds > 0);

-- Email format validation
ALTER TABLE User
ADD CONSTRAINT chk_user_email
CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Z|a-z]{2,}$');

-- Date range validation
ALTER TABLE Quiz
ADD CONSTRAINT chk_quiz_dates
CHECK (open_at IS NULL OR close_at IS NULL OR open_at < close_at);
```

### 8.4 Performance Indexes

```sql
-- User queries
CREATE INDEX idx_user_email ON User(email);
CREATE INDEX idx_user_status ON User(account_status);
CREATE INDEX idx_user_created ON User(created_at DESC);
CREATE INDEX idx_user_last_login ON User(last_login_at DESC);

-- Course queries
CREATE INDEX idx_course_status ON Course(status);
CREATE INDEX idx_course_published ON Course(published_at DESC) WHERE status = 'PUBLISHED';
CREATE INDEX idx_course_category ON Course(category);
CREATE INDEX idx_course_code ON Course(code);

-- Enrollment queries
CREATE INDEX idx_enrollment_user ON CourseEnrollment(user_id, enrollment_status);
CREATE INDEX idx_enrollment_course ON CourseEnrollment(course_id, enrollment_status);
CREATE INDEX idx_enrollment_active ON CourseEnrollment(enrollment_status, enrolled_at DESC)
  WHERE enrollment_status = 'ACTIVE';

-- Progress tracking
CREATE INDEX idx_progress_user_course ON Progress(user_id, course_id);
CREATE INDEX idx_progress_lecture ON Progress(lecture_id, status);
CREATE INDEX idx_progress_updated ON Progress(updated_at DESC);

-- Assessment queries
CREATE INDEX idx_attempt_user_quiz ON Attempt(user_id, quiz_id);
CREATE INDEX idx_attempt_grading ON Attempt(status, submitted_at DESC)
  WHERE status = 'SUBMITTED';
CREATE INDEX idx_submission_attempt ON Submission(attempt_id);

-- Discussion queries
CREATE INDEX idx_thread_class ON Thread(class_id, last_activity_at DESC);
CREATE INDEX idx_thread_lecture ON Thread(lecture_id, created_at DESC);
CREATE INDEX idx_post_thread ON Post(thread_id, created_at DESC);
CREATE INDEX idx_post_user ON Post(user_id, created_at DESC);

-- Full-text search indexes
CREATE INDEX idx_course_search ON Course USING GIN(
  to_tsvector('english', title || ' ' || description)
);
CREATE INDEX idx_thread_search ON Thread USING GIN(
  to_tsvector('english', title || ' ' || COALESCE(content, ''))
);
CREATE INDEX idx_question_search ON Question USING GIN(
  to_tsvector('english', text)
);

-- Array indexes (PostgreSQL)
CREATE INDEX idx_question_tags ON Question USING GIN(tags);
CREATE INDEX idx_thread_tags ON Thread USING GIN(tags);
```

---

## 9. ĐỀ XUẤT CẢI TIẾN

### 9.1 Soft Delete Pattern

**Lợi ích:**
- Không mất dữ liệu vĩnh viễn
- Có thể khôi phục
- Audit trail tốt hơn

**Implementation:**

```sql
-- Add to all major tables
ALTER TABLE User
ADD COLUMN is_deleted BOOLEAN DEFAULT FALSE,
ADD COLUMN deleted_at TIMESTAMP,
ADD COLUMN deleted_by UUID REFERENCES User(user_id);

ALTER TABLE Course
ADD COLUMN is_deleted BOOLEAN DEFAULT FALSE,
ADD COLUMN deleted_at TIMESTAMP,
ADD COLUMN deleted_by UUID REFERENCES User(user_id);

-- Similar for other tables...

-- Helper function
CREATE OR REPLACE FUNCTION soft_delete()
RETURNS TRIGGER AS $$
BEGIN
  -- Prevent actual deletion
  -- Instead, mark as deleted
  UPDATE User
  SET is_deleted = TRUE,
      deleted_at = CURRENT_TIMESTAMP
  WHERE user_id = OLD.user_id;

  -- Cancel the DELETE
  RETURN NULL;
END;
$$ LANGUAGE plpgsql;

-- Apply trigger (example for User)
CREATE TRIGGER trg_user_soft_delete
BEFORE DELETE ON User
FOR EACH ROW
EXECUTE FUNCTION soft_delete();

-- Create view for active records
CREATE VIEW ActiveUser AS
SELECT * FROM User WHERE is_deleted = FALSE OR is_deleted IS NULL;

-- Use view in queries
SELECT * FROM ActiveUser WHERE email = 'user@example.com';
```

### 9.2 Audit Timestamps

**Best practice:** Tất cả bảng nên có:

```sql
-- Standard audit columns
created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
created_by UUID REFERENCES User(user_id),
updated_by UUID REFERENCES User(user_id),
deleted_at TIMESTAMP,
deleted_by UUID REFERENCES User(user_id)

-- Auto-update trigger
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
  NEW.updated_at = CURRENT_TIMESTAMP;
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Apply to all tables
CREATE TRIGGER trg_user_updated_at
BEFORE UPDATE ON User
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();
```

### 9.3 Versioning cho Content

**Use case:** Track changes to Course, Lecture, Question content

```sql
CREATE TABLE LectureVersion (
  version_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  lecture_id UUID NOT NULL REFERENCES Lecture(lecture_id) ON DELETE CASCADE,

  -- Version info
  version_number INT NOT NULL,

  -- Snapshot
  content_snapshot JSON NOT NULL,
    -- Store entire lecture state at this version

  -- Changes
  change_summary VARCHAR(500),
  change_details JSON,

  -- Metadata
  created_by UUID REFERENCES User(user_id),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT uq_lecture_version UNIQUE(lecture_id, version_number)
);

CREATE INDEX idx_lecture_version ON LectureVersion(lecture_id, version_number DESC);

-- Trigger to auto-create version
CREATE OR REPLACE FUNCTION create_lecture_version()
RETURNS TRIGGER AS $$
DECLARE
  next_version INT;
BEGIN
  IF TG_OP = 'UPDATE' AND (
    NEW.title != OLD.title OR
    NEW.content_url != OLD.content_url OR
    NEW.description != OLD.description
  ) THEN
    -- Get next version number
    SELECT COALESCE(MAX(version_number), 0) + 1 INTO next_version
    FROM LectureVersion
    WHERE lecture_id = NEW.lecture_id;

    -- Create version snapshot
    INSERT INTO LectureVersion (
      lecture_id, version_number, content_snapshot, created_by
    ) VALUES (
      NEW.lecture_id,
      next_version,
      jsonb_build_object(
        'title', OLD.title,
        'description', OLD.description,
        'content_url', OLD.content_url,
        'duration_seconds', OLD.duration_seconds,
        'type', OLD.type
      ),
      NEW.updated_by
    );
  END IF;

  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_lecture_versioning
BEFORE UPDATE ON Lecture
FOR EACH ROW
EXECUTE FUNCTION create_lecture_version();
```

### 9.4 Caching Strategy

**Materialized Views cho reporting:**

```sql
-- Course statistics
CREATE MATERIALIZED VIEW CourseStatistics AS
SELECT
  c.course_id,
  c.title,
  c.status,
  COUNT(DISTINCT ce.user_id) AS enrollment_count,
  COUNT(DISTINCT CASE WHEN ce.enrollment_status = 'COMPLETED' THEN ce.user_id END) AS completion_count,
  AVG(ce.final_grade) AS average_grade,
  COUNT(DISTINCT m.module_id) AS module_count,
  COUNT(DISTINCT l.lecture_id) AS lecture_count,
  COUNT(DISTINCT q.quiz_id) AS quiz_count,
  SUM(l.duration_seconds) / 3600.0 AS total_hours
FROM Course c
LEFT JOIN CourseEnrollment ce ON c.course_id = ce.course_id
LEFT JOIN Module m ON c.course_id = m.course_id
LEFT JOIN Lecture l ON m.module_id = l.module_id
LEFT JOIN Quiz q ON c.course_id = q.course_id
WHERE c.is_deleted = FALSE OR c.is_deleted IS NULL
GROUP BY c.course_id, c.title, c.status;

CREATE UNIQUE INDEX idx_course_stats ON CourseStatistics(course_id);

-- Refresh periodically
REFRESH MATERIALIZED VIEW CONCURRENTLY CourseStatistics;

-- User progress summary
CREATE MATERIALIZED VIEW UserProgressSummary AS
SELECT
  u.user_id,
  u.email,
  u.first_name,
  u.last_name,
  COUNT(DISTINCT ce.course_id) AS enrolled_courses,
  COUNT(DISTINCT CASE WHEN ce.enrollment_status = 'COMPLETED' THEN ce.course_id END) AS completed_courses,
  AVG(ce.final_grade) AS average_grade,
  SUM(p.total_time_spent_minutes) / 60.0 AS total_hours_learned,
  COUNT(DISTINCT a.attempt_id) AS quizzes_taken,
  AVG(a.percentage_score) AS average_quiz_score
FROM User u
LEFT JOIN CourseEnrollment ce ON u.user_id = ce.user_id
LEFT JOIN Progress p ON u.user_id = p.user_id
LEFT JOIN Attempt a ON u.user_id = a.user_id AND a.status = 'GRADED'
WHERE u.is_deleted = FALSE OR u.is_deleted IS NULL
GROUP BY u.user_id, u.email, u.first_name, u.last_name;

CREATE UNIQUE INDEX idx_user_progress ON UserProgressSummary(user_id);
```

### 9.5 Data Partitioning

**For large tables like ActivityLog, Notification:**

```sql
-- Partition by date (PostgreSQL example)
CREATE TABLE ActivityLog (
  log_id UUID NOT NULL,
  user_id UUID,
  action VARCHAR(100) NOT NULL,
  entity_type VARCHAR(50) NOT NULL,
  entity_id UUID NOT NULL,
  created_at TIMESTAMP NOT NULL,
  log_date DATE GENERATED ALWAYS AS (created_at::DATE) STORED,
  PRIMARY KEY (log_id, log_date)
) PARTITION BY RANGE (log_date);

-- Create partitions for each month
CREATE TABLE ActivityLog_2025_01 PARTITION OF ActivityLog
  FOR VALUES FROM ('2025-01-01') TO ('2025-02-01');

CREATE TABLE ActivityLog_2025_02 PARTITION OF ActivityLog
  FOR VALUES FROM ('2025-02-01') TO ('2025-03-01');

-- Auto-create future partitions with a maintenance job
CREATE OR REPLACE FUNCTION create_monthly_partition()
RETURNS void AS $$
DECLARE
  partition_name TEXT;
  start_date DATE;
  end_date DATE;
BEGIN
  -- Create partition for next month
  start_date := DATE_TRUNC('month', CURRENT_DATE + INTERVAL '1 month');
  end_date := start_date + INTERVAL '1 month';
  partition_name := 'ActivityLog_' || TO_CHAR(start_date, 'YYYY_MM');

  EXECUTE FORMAT(
    'CREATE TABLE IF NOT EXISTS %I PARTITION OF ActivityLog FOR VALUES FROM (%L) TO (%L)',
    partition_name, start_date, end_date
  );
END;
$$ LANGUAGE plpgsql;
```

---

## 10. SQL SCRIPTS ĐỀ XUẤT

### 10.1 Complete Schema Creation Script

```sql
-- ============================================
-- B-LEARNING DATABASE SCHEMA - IMPROVED VERSION
-- Created: 2025-11-25
-- Database: PostgreSQL 14+
-- ============================================

-- Extensions
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pg_trgm";  -- For fuzzy text search
CREATE EXTENSION IF NOT EXISTS "btree_gin";  -- For multi-column GIN indexes

-- ============================================
-- 1. USER MANAGEMENT
-- ============================================

CREATE TABLE "User" (
  user_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

  -- Authentication
  email VARCHAR(255) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  email_verified_at TIMESTAMP,

  -- Personal Info
  first_name VARCHAR(100) NOT NULL,
  last_name VARCHAR(100) NOT NULL,
  avatar_url VARCHAR(500),
  phone VARCHAR(20),
  bio TEXT,

  -- Status
  account_status VARCHAR(30) NOT NULL DEFAULT 'PENDING_VERIFICATION',

  -- Preferences
  timezone VARCHAR(50) DEFAULT 'UTC',
  locale VARCHAR(10) DEFAULT 'vi',
  notification_enabled BOOLEAN DEFAULT TRUE,

  -- Tracking
  last_login_at TIMESTAMP,
  login_count INT DEFAULT 0,

  -- Audit
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_deleted BOOLEAN DEFAULT FALSE,
  deleted_at TIMESTAMP,
  deleted_by UUID,

  CONSTRAINT chk_account_status CHECK (account_status IN (
    'PENDING_VERIFICATION', 'ACTIVE', 'SUSPENDED', 'DELETED'
  ))
);

CREATE TABLE "Role" (
  role_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  name VARCHAR(50) NOT NULL UNIQUE,
  description TEXT,
  permissions JSON,
  is_system_role BOOLEAN DEFAULT FALSE,
  priority INT DEFAULT 0,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE "UserRole" (
  userrole_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID NOT NULL REFERENCES "User"(user_id) ON DELETE CASCADE,
  role_id UUID NOT NULL REFERENCES "Role"(role_id) ON DELETE CASCADE,
  granted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  granted_by UUID REFERENCES "User"(user_id),
  expires_at TIMESTAMP,
  CONSTRAINT uq_user_role UNIQUE(user_id, role_id)
);

-- ============================================
-- 2. COURSE CONTENT
-- ============================================

CREATE TABLE "Course" (
  course_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  code VARCHAR(50) NOT NULL UNIQUE,
  title VARCHAR(200) NOT NULL,
  description TEXT,
  short_description VARCHAR(500),
  learning_objectives TEXT[],
  prerequisites TEXT[],
  target_audience TEXT,
  thumbnail_url VARCHAR(500),
  intro_video_url VARCHAR(500),
  category VARCHAR(100),
  tags VARCHAR(500)[],
  difficulty_level VARCHAR(20),
  estimated_hours DECIMAL(5,2),
  language VARCHAR(10) DEFAULT 'vi',
  status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
  published_at TIMESTAMP,
  archived_at TIMESTAMP,
  enrollment_count INT DEFAULT 0,
  completion_count INT DEFAULT 0,
  average_rating DECIMAL(3,2),
  created_by UUID REFERENCES "User"(user_id),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_deleted BOOLEAN DEFAULT FALSE,

  CONSTRAINT chk_course_status CHECK (status IN ('DRAFT', 'PUBLISHED', 'ARCHIVED')),
  CONSTRAINT chk_difficulty CHECK (difficulty_level IN ('BEGINNER', 'INTERMEDIATE', 'ADVANCED', 'EXPERT'))
);

-- Add more tables: Module, Lecture, Resource, Quiz, Question, etc.
-- (See previous sections for complete definitions)

-- ============================================
-- 3. INDEXES
-- ============================================

-- User indexes
CREATE INDEX idx_user_email ON "User"(email);
CREATE INDEX idx_user_status ON "User"(account_status);
CREATE INDEX idx_user_created ON "User"(created_at DESC);

-- Course indexes
CREATE INDEX idx_course_status ON "Course"(status);
CREATE INDEX idx_course_published ON "Course"(published_at DESC) WHERE status = 'PUBLISHED';
CREATE INDEX idx_course_category ON "Course"(category);
CREATE INDEX idx_course_tags ON "Course" USING GIN(tags);

-- Full-text search
CREATE INDEX idx_course_search ON "Course" USING GIN(
  to_tsvector('english', title || ' ' || description)
);

-- ============================================
-- 4. TRIGGERS
-- ============================================

-- Auto-update updated_at
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
  NEW.updated_at = CURRENT_TIMESTAMP;
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_user_updated_at BEFORE UPDATE ON "User"
  FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER trg_course_updated_at BEFORE UPDATE ON "Course"
  FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Add more triggers as needed...

-- ============================================
-- 5. INITIAL DATA
-- ============================================

INSERT INTO "Role" (name, description, is_system_role, priority) VALUES
('ADMIN', 'System Administrator', TRUE, 100),
('INSTRUCTOR', 'Course Instructor', TRUE, 50),
('TA', 'Teaching Assistant', TRUE, 30),
('STUDENT', 'Student', TRUE, 10),
('MODERATOR', 'Forum Moderator', TRUE, 20);

INSERT INTO "SystemSettings" (setting_key, setting_value, data_type, category) VALUES
('max_upload_size_mb', '100', 'INTEGER', 'STORAGE'),
('session_timeout_minutes', '30', 'INTEGER', 'SECURITY'),
('allow_self_registration', 'true', 'BOOLEAN', 'GENERAL');
```

### 10.2 Migration Script (Old → New)

```sql
-- ============================================
-- MIGRATION: OLD SCHEMA → IMPROVED SCHEMA
-- ============================================

BEGIN;

-- 1. Backup existing data
CREATE TABLE User_backup AS SELECT * FROM "User";
CREATE TABLE Progress_backup AS SELECT * FROM Progress;

-- 2. Add new columns to existing tables
ALTER TABLE "User"
  ADD COLUMN IF NOT EXISTS first_name VARCHAR(100),
  ADD COLUMN IF NOT EXISTS last_name VARCHAR(100),
  ADD COLUMN IF NOT EXISTS account_status VARCHAR(30) DEFAULT 'ACTIVE',
  ADD COLUMN IF NOT EXISTS timezone VARCHAR(50) DEFAULT 'UTC';

-- 3. Add Progress improvements
ALTER TABLE Progress
  ADD COLUMN IF NOT EXISTS course_id UUID REFERENCES "Course"(course_id),
  ADD COLUMN IF NOT EXISTS class_id UUID REFERENCES "Class"(class_id),
  ADD COLUMN IF NOT EXISTS module_id UUID REFERENCES "Module"(module_id),
  ADD COLUMN IF NOT EXISTS quiz_id UUID REFERENCES "Quiz"(quiz_id),
  ADD COLUMN IF NOT EXISTS status VARCHAR(20) DEFAULT 'IN_PROGRESS',
  ADD COLUMN IF NOT EXISTS first_accessed_at TIMESTAMP,
  ADD COLUMN IF NOT EXISTS completed_at TIMESTAMP;

-- 4. Migrate data
-- Extract first_name from email
UPDATE "User"
SET first_name = SPLIT_PART(email, '@', 1),
    last_name = 'User'
WHERE first_name IS NULL;

-- Backfill Progress.course_id from Lecture → Module → Course
UPDATE Progress p
SET course_id = m.course_id
FROM Lecture l
JOIN Module m ON l.module_id = m.module_id
WHERE p.lecture_id = l.lecture_id
  AND p.course_id IS NULL;

-- 5. Add constraints
ALTER TABLE Progress
  ADD CONSTRAINT chk_progress_status
  CHECK (status IN ('NOT_STARTED', 'IN_PROGRESS', 'COMPLETED', 'SKIPPED'));

-- 6. Create new tables
CREATE TABLE IF NOT EXISTS Certificate (
  -- (See previous section for complete definition)
);

CREATE TABLE IF NOT EXISTS Notification (
  -- (See previous section)
);

-- 7. Create indexes
CREATE INDEX IF NOT EXISTS idx_progress_course ON Progress(course_id);
CREATE INDEX IF NOT EXISTS idx_progress_status ON Progress(status);

COMMIT;
```

---

## 11. TỔNG KẾT

### 11.1 Scorecard Chi Tiết

| Khía cạnh | Điểm hiện tại | Điểm sau cải tiến | Ghi chú |
|-----------|---------------|-------------------|---------|
| **Cấu trúc tổng thể** | 8/10 | 9/10 | Tốt, chỉ cần bổ sung vài bảng |
| **User Management** | 5/10 | 9/10 | Thiếu nhiều thông tin cơ bản |
| **Course Content** | 7/10 | 9/10 | Cần bổ sung metadata |
| **Progress Tracking** | 3/10 | 9/10 | Vấn đề nghiêm trọng nhất |
| **Assessment** | 6/10 | 9/10 | Attempt design có vấn đề |
| **Discussion** | 5/10 | 9/10 | Thread phân loại mơ hồ |
| **Notification** | 0/10 | 9/10 | Thiếu hoàn toàn |
| **Certificate** | 0/10 | 9/10 | Thiếu hoàn toàn |
| **Audit Trail** | 2/10 | 9/10 | Chỉ có timestamps cơ bản |
| **Indexes** | 3/10 | 9/10 | Không đề cập trong tài liệu |
| **Constraints** | 4/10 | 9/10 | Thiếu nhiều business rules |
| **Scalability** | 6/10 | 9/10 | Cần partitioning, caching |

**ĐIỂM TRUNG BÌNH:**
- **Trước cải tiến:** 4.1/10
- **Sau cải tiến:** 8.9/10
- **Cải thiện:** +4.8 điểm (+117%)

### 11.2 Ưu tiên thực hiện

#### 🔴 Urgent (Phải làm ngay)

1. **Fix bảng Progress** - Thêm course_id, class_id, module_id, quiz_id, status
2. **Fix bảng Attempt** - Thay class_id bằng course_enrollment_id
3. **Fix bảng Thread** - Thêm thread_type, visibility, status
4. **Thêm bảng Certificate** - Required cho tính năng cấp chứng chỉ
5. **Thêm bảng Notification** - Required cho thông báo

#### 🟡 Important (Nên làm sớm)

6. Bổ sung thông tin User (first_name, last_name, account_status)
7. Cải thiện Submission (file upload, feedback)
8. Tách PostVote ra khỏi Post
9. Thêm indexes cho performance
10. Thêm foreign key constraints với ON DELETE behavior

#### 🟢 Nice to have (Có thể làm sau)

11. ActivityLog cho audit trail
12. File management table
13. SystemSettings table
14. Versioning cho content
15. Materialized views cho reporting
16. Data partitioning
17. Soft delete implementation

### 11.3 Checklist triển khai

```markdown
### Phase 1: Critical Fixes (Week 1-2)
- [ ] Redesign Progress table
- [ ] Fix Attempt table relationships
- [ ] Improve Thread categorization
- [ ] Add Certificate table
- [ ] Add Notification table
- [ ] Add essential indexes

### Phase 2: Data Quality (Week 3-4)
- [ ] Add User details fields
- [ ] Improve Submission structure
- [ ] Create PostVote table
- [ ] Add all foreign key constraints
- [ ] Add check constraints
- [ ] Add unique constraints

### Phase 3: Features (Week 5-6)
- [ ] Implement ActivityLog
- [ ] Create File management
- [ ] Add SystemSettings
- [ ] Implement auto-grading triggers
- [ ] Implement progress calculation triggers
- [ ] Add certificate auto-issue trigger

### Phase 4: Optimization (Week 7-8)
- [ ] Add all performance indexes
- [ ] Create materialized views
- [ ] Implement caching strategy
- [ ] Add full-text search
- [ ] Optimize slow queries
- [ ] Add monitoring

### Phase 5: Advanced (Week 9+)
- [ ] Implement soft delete
- [ ] Add content versioning
- [ ] Set up data partitioning
- [ ] Add multi-language support
- [ ] Implement advanced analytics
```

### 11.4 Rủi ro và giảm thiểu

| Rủi ro | Tác động | Khả năng | Giảm thiểu |
|--------|----------|----------|------------|
| Migration data loss | Cao | Thấp | Full backup + test migration script |
| Breaking changes | Cao | Trung bình | Gradual migration, backward compatibility |
| Performance regression | Trung bình | Thấp | Benchmark before/after, proper indexing |
| Downtime during migration | Trung bình | Cao | Blue-green deployment, migration in off-hours |
| Data inconsistency | Cao | Thấp | Transaction wrapping, validation scripts |

### 11.5 Kết luận cuối cùng

**Điểm mạnh của thiết kế hiện tại:**
1. ✅ Nền tảng tốt với UUID và cấu trúc phân cấp rõ ràng
2. ✅ Tách biệt Course/Class hợp lý
3. ✅ Hỗ trợ RBAC linh hoạt
4. ✅ Đa dạng loại câu hỏi và bài kiểm tra

**Điểm yếu nghiêm trọng:**
1. ❌ Progress tracking không đầy đủ (thiếu course_id, module_id, quiz_id)
2. ❌ Attempt design có vấn đề (class_id mandatory)
3. ❌ Thread phân loại mơ hồ
4. ❌ Submission thiếu nhiều loại answer
5. ❌ Thiếu Certificate và Notification tables

**Khuyến nghị:**
- **Cần refactor 4-5 bảng chính** (Progress, Attempt, Thread, Submission, User)
- **Thêm 5-7 bảng mới** (Certificate, Notification, ActivityLog, File, PostVote, etc.)
- **Bổ sung 50+ indexes** cho performance
- **Thêm 30+ constraints** cho data integrity
- **Implement 10+ triggers** cho business logic tự động

**Thời gian ước tính:** 6-8 tuần cho complete refactor

**ROI:** Cao - Cải thiện maintainability, performance, và user experience đáng kể

---

**END OF REPORT**

*Nếu cần chi tiết hơn về bất kỳ phần nào, vui lòng yêu cầu.*
