# ĐỀ XUẤT TINH GIẢN DATABASE HỆ THỐNG B-LEARNING

**Người thực hiện:** Nguyễn Văn Kiệt - CNTT1-K63
**Ngày tạo:** 2025-11-27
**Phiên bản:** 1.0
**Mục tiêu:** Giảm độ phức tạp database từ 31 bảng xuống còn 15-18 bảng

---

## 📊 TÓM TẮT EXECUTIVE

```
┌─────────────────────────────────────────────────────┐
│  HIỆN TẠI: 31 bảng, 8 domains                      │
│  ĐỀ XUẤT:  16 bảng, 5 domains                      │
│  GIẢM:     48% số lượng bảng                        │
│  THỜI GIAN: 2-3 tuần refactoring                    │
└─────────────────────────────────────────────────────┘
```

### Lợi ích chính
- ✅ Giảm 48% số lượng bảng (31 → 16)
- ✅ Dễ hiểu và bảo trì hơn cho người mới
- ✅ Giữ nguyên 100% chức năng core
- ✅ Giảm độ phức tạp của JOIN queries
- ✅ Vẫn đáp ứng đầy đủ yêu cầu nghiệp vụ

---

## 1. PHÂN TÍCH HIỆN TRẠNG

### 1.1. Thống kê bảng hiện tại

| Domain | Số bảng | % Tổng | Đánh giá |
|--------|---------|--------|----------|
| User Management | 3 | 9.7% | ✅ Core - Giữ nguyên |
| Course Content | 4 | 12.9% | ✅ Core - Có thể tối ưu |
| Assessment | 9 | 29.0% | ⚠️ Quá phức tạp - Cần giảm |
| Enrollment & Progress | 4 | 12.9% | ⚠️ Có thể gộp |
| Class & Blended | 3 | 9.7% | ✅ Core - Giữ nguyên |
| Certificate | 3 | 9.7% | ⚠️ Quá chi tiết - Cần giảm |
| Notification | 3 | 9.7% | ⚠️ Không core - Cần xem xét |
| Audit & System | 3 | 9.7% | ⚠️ Không core - Cần xem xét |
| **TỔNG** | **31** | **100%** | |

### 1.2. Vấn đề hiện tại

#### Vấn đề 1: Domain Assessment quá phức tạp (9 bảng)
- Quiz (1) + Question (1) + Option (1) + QuizQuestion (1) = 4 bảng chỉ cho Quiz
- Assignment (1) + AssignmentSubmission (1) = 2 bảng cho Assignment
- Attempt (1) + QuizSubmission (1) = 2 bảng cho việc làm bài
- GradeBook (1) = 1 bảng tổng hợp

**Phân tích:**
- QuizQuestion có thể gộp vào Quiz (dùng JSON)
- QuizSubmission có thể gộp vào Attempt
- GradeBook có thể tính toán động (không cần lưu)

#### Vấn đề 2: Certificate domain quá chi tiết (3 bảng)
- CertificateTemplate, Certificate, CertificateVerification
- Với hệ thống nhỏ/trung bình, không cần phức tạp đến vậy

#### Vấn đề 3: Notification & Audit không phải core
- Notification (3 bảng): Có thể dùng service bên ngoài
- ActivityLog: Có thể dùng application logging
- File: Có thể dùng cloud storage (S3, GCS)

#### Vấn đề 4: Enrollment có 2 bảng riêng biệt
- CourseEnrollment và ClassEnrollment
- Có thể gộp thành 1 bảng với class_id nullable

---

## 2. PHÂN LOẠI CHỨC NĂNG

### 2.1. CORE - Bắt buộc phải có (70% giá trị)

```
✅ User Management
   - Đăng nhập, phân quyền

✅ Course Management
   - Tạo khóa học, module, lecture

✅ Assessment Basic
   - Quiz, Assignment cơ bản
   - Chấm điểm

✅ Enrollment & Progress
   - Đăng ký khóa học
   - Theo dõi tiến độ

✅ Class Management
   - Quản lý lớp học (blended)
   - Điểm danh
```

### 2.2. OPTIONAL - Nên có nhưng có thể đơn giản hóa (20% giá trị)

```
⚠️ Certificate
   - Có thể đơn giản chỉ 1 bảng

⚠️ Advanced Assessment
   - Rubric, auto-grading
   - Có thể lưu trong JSON
```

### 2.3. NICE-TO-HAVE - Không cần thiết cho MVP (10% giá trị)

```
❌ Notification System
   - Dùng email service (SendGrid, SES)

❌ Activity Logging
   - Dùng application logs

❌ File Management
   - Dùng cloud storage trực tiếp

❌ Certificate Template
   - Dùng template engine (Handlebars, Mustache)

❌ Certificate Verification Log
   - Kiểm tra trực tiếp từ bảng Certificate
```

---

## 3. ĐỀ XUẤT TINH GIẢN

### 3.1. Các bảng ĐỀ XUẤT BỎ HOÀN TOÀN (9 bảng)

| # | Bảng | Lý do bỏ | Giải pháp thay thế |
|---|------|----------|-------------------|
| 1 | **CertificateTemplate** | Không cần database | Dùng template file (.html/.docx) |
| 2 | **CertificateVerification** | Không cần log riêng | Verify trực tiếp từ Certificate |
| 3 | **Notification** | Không core | Dùng email service (SendGrid) |
| 4 | **NotificationPreference** | Không core | Lưu trong User.preferences (JSON) |
| 5 | **NotificationLog** | Không core | Application logs |
| 6 | **ActivityLog** | Không core | Application logs (Winston, Morgan) |
| 7 | **File** | Không cần quản lý | Cloud storage SDK (S3, GCS) |
| 8 | **SystemSettings** | Không cần database | Config file (.env, config.json) |
| 9 | **GradeBook** | Tính toán động | View/Query động |

**Tổng số bỏ: 9 bảng**

### 3.2. Các bảng ĐỀ XUẤT GỘP (6 bảng → 3 bảng)

#### Gộp 1: QuizQuestion → Quiz
**Trước:** 2 bảng
```sql
Quiz: quiz_id, title, description, ...
QuizQuestion: quiz_question_id, quiz_id, question_id, points, order_num
```

**Sau:** 1 bảng
```sql
Quiz: quiz_id, title, description, questions (JSON)

-- JSON structure:
{
  "questions": [
    {"question_id": "uuid", "points": 10, "order": 1},
    {"question_id": "uuid", "points": 15, "order": 2}
  ]
}
```

**Lý do:** QuizQuestion chỉ là join table đơn giản, dùng JSON gọn hơn

---

#### Gộp 2: QuizSubmission → Attempt
**Trước:** 2 bảng
```sql
Attempt: attempt_id, quiz_id, user_id, ...
QuizSubmission: submission_id, attempt_id, question_id, answer, score
```

**Sau:** 1 bảng
```sql
Attempt: attempt_id, quiz_id, user_id, answers (JSON), ...

-- JSON structure:
{
  "answers": [
    {
      "question_id": "uuid",
      "answer_text": "...",
      "selected_options": ["uuid1", "uuid2"],
      "score": 10,
      "feedback": "..."
    }
  ]
}
```

**Lý do:** Submissions luôn thuộc về 1 attempt, không cần tách riêng

---

#### Gộp 3: CourseEnrollment + ClassEnrollment → Enrollment
**Trước:** 2 bảng
```sql
CourseEnrollment: enrollment_id, user_id, course_id, ...
ClassEnrollment: class_enrollment_id, user_id, class_id, course_enrollment_id
```

**Sau:** 1 bảng
```sql
Enrollment: enrollment_id, user_id, course_id, class_id (NULLABLE), ...
```

**Lý do:**
- Self-paced: class_id = NULL
- Blended: class_id = UUID
- Không cần 2 bảng riêng

---

### 3.3. Các bảng ĐƠN GIẢN HÓA (3 bảng)

#### 1. Certificate - Bỏ các trường không cần thiết

**Trước:** 16 cột
```sql
Certificate {
  certificate_id, user_id, course_id, course_enrollment_id,
  template_id, certificate_code, verification_code,
  title, issue_date, completion_date,
  final_grade, grade_letter, pdf_url, qr_code_url,
  verification_url, status, valid_from, valid_until,
  revoked_at, revoked_by, revoke_reason, created_at
}
```

**Sau:** 10 cột
```sql
Certificate {
  certificate_id, user_id, course_id,
  certificate_code, verification_code,
  issue_date, final_grade,
  pdf_url, status, created_at
}
```

**Bỏ:**
- course_enrollment_id (dư thừa, có user_id + course_id)
- template_id (dùng file template)
- title, completion_date (tính từ course data)
- grade_letter (tính từ final_grade)
- qr_code_url, verification_url (generate động)
- valid_from, valid_until (không cần cho hệ thống giáo dục)
- revoked_at, revoked_by, revoke_reason (chỉ cần status)

---

#### 2. Assignment - Bỏ các trường advanced

**Trước:** 17 cột
```sql
Assignment {
  ..., rubric (JSON), auto_grading_enabled, test_cases (JSON),
  late_penalty_percent, max_late_days, allow_resubmission, max_submissions
}
```

**Sau:** 10 cột
```sql
Assignment {
  assignment_id, course_id, title, description,
  assignment_type, max_points, due_date,
  created_by, created_at, updated_at
}
```

**Bỏ:**
- rubric, test_cases (feature nâng cao, không dùng ngay)
- late_penalty, max_late_days (business logic trong code)
- allow_resubmission, max_submissions (business logic)

---

#### 3. Progress - Giảm tracking granularity

**Trước:** Tracking từng lecture
```sql
Progress {
  progress_id, user_id, course_id, class_id,
  module_id, lecture_id, status, percent_complete,
  last_position_seconds, ...
}
```

**Sau:** Chỉ tracking module level
```sql
Progress {
  progress_id, user_id, course_id,
  module_id, status, completed_at
}
```

**Lý do:**
- Lecture progress có thể tracking ở frontend (localStorage)
- Chỉ lưu database khi hoàn thành module
- Giảm số lượng records đáng kể

---

## 4. SCHEMA MỚI SAU TINH GIẢN

### 4.1. Danh sách bảng mới (16 bảng)

#### Domain 1: User Management (3 bảng - GIỮ NGUYÊN)
1. **User** - Tài khoản người dùng
2. **Role** - Vai trò hệ thống
3. **UserRole** - Phân quyền

#### Domain 2: Course Content (4 bảng - GIỮ NGUYÊN)
4. **Course** - Khóa học
5. **Module** - Chương học
6. **Lecture** - Bài giảng
7. **Resource** - Tài liệu đính kèm

#### Domain 3: Assessment (5 bảng - TỪ 9 XUỐNG 5)
8. **Quiz** - Bài kiểm tra (gộp QuizQuestion vào JSON)
9. **Question** - Ngân hàng câu hỏi
10. **Option** - Lựa chọn câu hỏi
11. **Attempt** - Lần làm bài (gộp QuizSubmission vào JSON)
12. **AssignmentSubmission** - Nộp bài tập (giữ Assignment riêng trong Course)

**Thay đổi:**
- ❌ Bỏ QuizQuestion → Lưu trong Quiz.questions (JSON)
- ❌ Bỏ QuizSubmission → Lưu trong Attempt.answers (JSON)
- ❌ Bỏ Assignment → Dùng Lecture với type='ASSIGNMENT'
- ❌ Bỏ GradeBook → Tính toán động

#### Domain 4: Enrollment & Progress (2 bảng - TỪ 4 XUỐNG 2)
13. **Enrollment** - Đăng ký (gộp Course + Class enrollment)
14. **Progress** - Tiến độ học (giảm granularity)

**Thay đổi:**
- ❌ Bỏ CourseEnrollment, ClassEnrollment → Gộp thành Enrollment
- ❌ Bỏ Attendance → Lưu trong Schedule.attendances (JSON)

#### Domain 5: Class & Certificate (2 bảng - TỪ 6 XUỐNG 2)
15. **Class** - Lớp học
16. **Certificate** - Chứng chỉ (đơn giản hóa)

**Thay đổi:**
- ❌ Bỏ Schedule → Lưu trong Class.schedules (JSON)
- ❌ Bỏ CertificateTemplate → Dùng file template
- ❌ Bỏ CertificateVerification → Verify trực tiếp

---

### 4.2. So sánh số lượng bảng

```
TRƯỚC (31 bảng)                    SAU (16 bảng)
================                   =============

User Management (3)         →      User Management (3)
├── User                           ├── User
├── Role                           ├── Role
└── UserRole                       └── UserRole

Course Content (4)          →      Course Content (4)
├── Course                         ├── Course
├── Module                         ├── Module
├── Lecture                        ├── Lecture
└── Resource                       └── Resource

Assessment (9)              →      Assessment (5)
├── Quiz                           ├── Quiz (+ questions JSON)
├── Question                       ├── Question
├── Option                         ├── Option
├── QuizQuestion            ❌     ├── Attempt (+ answers JSON)
├── Attempt                        └── AssignmentSubmission
├── QuizSubmission          ❌
├── Assignment              ❌
├── AssignmentSubmission
└── GradeBook               ❌

Enrollment & Progress (4)   →      Enrollment & Progress (2)
├── CourseEnrollment        ❌     ├── Enrollment
├── ClassEnrollment         ❌     └── Progress
├── Progress
└── Attendance              ❌

Class & Blended (3)         →      Class (1)
├── Class                          └── Class (+ schedules JSON)
├── Schedule                ❌
└── (Attendance)

Certificate (3)             →      Certificate (1)
├── CertificateTemplate     ❌     └── Certificate
├── Certificate
└── CertificateVerification ❌

Notification (3)            →      (BỎ HOÀN TOÀN)
├── Notification            ❌
├── NotificationPreference  ❌
└── NotificationLog         ❌

Audit & System (3)          →      (BỎ HOÀN TOÀN)
├── ActivityLog             ❌
├── File                    ❌
└── SystemSettings          ❌
```

---

## 5. CHI TIẾT SCHEMA MỚI

### 5.1. Các bảng thay đổi

#### Quiz (Gộp QuizQuestion)

```sql
CREATE TABLE "Quiz" (
  quiz_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  course_id UUID NOT NULL REFERENCES "Course"(course_id) ON DELETE CASCADE,
  title VARCHAR(200) NOT NULL,
  description TEXT,
  time_limit_minutes INT,
  pass_score DECIMAL(5,2),

  -- GỘP QuizQuestion vào đây
  questions JSON NOT NULL,
  -- Structure: [
  --   {"question_id": "uuid", "points": 10, "order": 1},
  --   {"question_id": "uuid", "points": 15, "order": 2}
  -- ]

  created_by UUID REFERENCES "User"(user_id),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Ví dụ data:
{
  "questions": [
    {
      "question_id": "550e8400-e29b-41d4-a716-446655440000",
      "points": 10,
      "order": 1
    },
    {
      "question_id": "550e8400-e29b-41d4-a716-446655440001",
      "points": 15,
      "order": 2
    }
  ],
  "settings": {
    "shuffle": true,
    "show_answers": false
  }
}
```

---

#### Attempt (Gộp QuizSubmission)

```sql
CREATE TABLE "Attempt" (
  attempt_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  quiz_id UUID NOT NULL REFERENCES "Quiz"(quiz_id) ON DELETE CASCADE,
  user_id UUID NOT NULL REFERENCES "User"(user_id) ON DELETE CASCADE,
  enrollment_id UUID NOT NULL REFERENCES "Enrollment"(enrollment_id) ON DELETE CASCADE,

  attempt_number INT NOT NULL,
  started_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  submitted_at TIMESTAMP,

  -- GỘP QuizSubmission vào đây
  answers JSON,
  -- Structure: [
  --   {
  --     "question_id": "uuid",
  --     "answer_text": "text",
  --     "selected_options": ["uuid1"],
  --     "score": 10,
  --     "feedback": "correct"
  --   }
  -- ]

  total_score DECIMAL(6,2),
  status VARCHAR(20) DEFAULT 'IN_PROGRESS',

  UNIQUE(user_id, quiz_id, attempt_number)
);

-- Ví dụ data:
{
  "answers": [
    {
      "question_id": "550e8400-e29b-41d4-a716-446655440000",
      "answer_text": "Paris",
      "selected_options": ["550e8400-e29b-41d4-a716-446655440010"],
      "score": 10,
      "max_score": 10,
      "is_correct": true,
      "feedback": "Correct answer!",
      "graded_at": "2025-11-27T10:30:00Z"
    }
  ]
}
```

---

#### Enrollment (Gộp Course + Class)

```sql
CREATE TABLE "Enrollment" (
  enrollment_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID NOT NULL REFERENCES "User"(user_id) ON DELETE CASCADE,
  course_id UUID NOT NULL REFERENCES "Course"(course_id) ON DELETE CASCADE,

  -- NULLABLE cho self-paced learning
  class_id UUID REFERENCES "Class"(class_id) ON DELETE SET NULL,

  role VARCHAR(20) NOT NULL, -- STUDENT, INSTRUCTOR, TA
  status VARCHAR(20) DEFAULT 'ACTIVE',

  enrolled_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  completed_at TIMESTAMP,
  completion_percentage DECIMAL(5,2) DEFAULT 0,

  UNIQUE(user_id, course_id, COALESCE(class_id, '00000000-0000-0000-0000-000000000000'::UUID))
);
```

---

#### Progress (Module-level only)

```sql
CREATE TABLE "Progress" (
  progress_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID NOT NULL REFERENCES "User"(user_id) ON DELETE CASCADE,
  course_id UUID NOT NULL REFERENCES "Course"(course_id) ON DELETE CASCADE,
  module_id UUID NOT NULL REFERENCES "Module"(module_id) ON DELETE CASCADE,

  status VARCHAR(20) DEFAULT 'NOT_STARTED',
  -- NOT_STARTED, IN_PROGRESS, COMPLETED

  completed_at TIMESTAMP,

  UNIQUE(user_id, course_id, module_id)
);

-- Lecture progress tracking qua frontend
-- Hoặc thêm optional:
-- lecture_progress JSON: {"lecture_id": "uuid", "percent": 80}
```

---

#### Class (Gộp Schedule)

```sql
CREATE TABLE "Class" (
  class_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  course_id UUID NOT NULL REFERENCES "Course"(course_id) ON DELETE CASCADE,
  instructor_id UUID REFERENCES "User"(user_id),

  name VARCHAR(100) NOT NULL,
  start_date DATE,
  end_date DATE,
  status VARCHAR(20) DEFAULT 'SCHEDULED',

  -- GỘP Schedule vào đây
  schedules JSON,
  -- Structure: [
  --   {
  --     "date": "2025-12-01",
  --     "start_time": "09:00",
  --     "end_time": "11:00",
  --     "topic": "Introduction",
  --     "location": "Room 101",
  --     "attendances": [
  --       {"user_id": "uuid", "status": "PRESENT", "check_in": "09:05"}
  --     ]
  --   }
  -- ]

  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Ví dụ data:
{
  "schedules": [
    {
      "session_id": "uuid",
      "date": "2025-12-01",
      "start_time": "09:00",
      "end_time": "11:00",
      "topic": "Introduction to Programming",
      "location": "Room 101",
      "type": "IN_PERSON",
      "attendances": [
        {
          "user_id": "uuid1",
          "status": "PRESENT",
          "check_in_time": "2025-12-01T09:05:00Z"
        },
        {
          "user_id": "uuid2",
          "status": "LATE",
          "check_in_time": "2025-12-01T09:15:00Z"
        }
      ]
    }
  ]
}
```

---

#### Certificate (Đơn giản hóa)

```sql
CREATE TABLE "Certificate" (
  certificate_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID NOT NULL REFERENCES "User"(user_id) ON DELETE CASCADE,
  course_id UUID NOT NULL REFERENCES "Course"(course_id) ON DELETE CASCADE,

  certificate_code VARCHAR(50) NOT NULL UNIQUE,
  verification_code VARCHAR(100) NOT NULL UNIQUE,

  issue_date DATE NOT NULL,
  final_grade DECIMAL(5,2),

  pdf_url VARCHAR(500),
  status VARCHAR(20) DEFAULT 'ACTIVE', -- ACTIVE, REVOKED

  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

  UNIQUE(user_id, course_id)
);

COMMENT ON TABLE "Certificate" IS 'Simplified certificate - template in files';
COMMENT ON COLUMN "Certificate".certificate_code IS 'Public code: BL-2025-000001';
COMMENT ON COLUMN "Certificate".verification_code IS 'Secret hash for verification';
```

---

#### Lecture (Thêm ASSIGNMENT type)

```sql
CREATE TABLE "Lecture" (
  lecture_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  module_id UUID NOT NULL REFERENCES "Module"(module_id) ON DELETE CASCADE,

  title VARCHAR(200) NOT NULL,
  description TEXT,

  -- Thêm ASSIGNMENT vào type
  type VARCHAR(20) NOT NULL,
  -- VIDEO, PDF, SLIDE, AUDIO, TEXT, ASSIGNMENT

  content_url VARCHAR(1024),

  -- Nếu type = ASSIGNMENT
  assignment_config JSON,
  -- {
  --   "max_points": 100,
  --   "due_date": "2025-12-15T23:59:00Z",
  --   "submission_types": ["file", "text"],
  --   "rubric": {...}
  -- }

  order_num INT NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

  UNIQUE(module_id, order_num)
);
```

---

### 5.2. Giải pháp thay thế cho các bảng bỏ

#### 1. Notification → Email Service

**Thay vì:**
```sql
-- 3 bảng: Notification, NotificationPreference, NotificationLog
```

**Dùng:**
```javascript
// Backend service
import sendgrid from '@sendgrid/mail';
import twilio from 'twilio';

class NotificationService {
  sendEmail(userId, type, message) {
    const user = getUserById(userId);
    const preferences = user.preferences.notifications; // JSON in User table

    if (preferences[type]?.email) {
      sendgrid.send({
        to: user.email,
        subject: message.title,
        text: message.body
      });
    }
  }

  sendPush(userId, message) {
    // Firebase Cloud Messaging
    fcm.send(message);
  }
}
```

**Lưu preferences trong User table:**
```sql
ALTER TABLE "User" ADD COLUMN preferences JSON DEFAULT '{}';

-- Example:
{
  "notifications": {
    "assignment_due": {"email": true, "push": true},
    "grade_published": {"email": true, "push": false}
  },
  "locale": "vi",
  "timezone": "Asia/Ho_Chi_Minh"
}
```

---

#### 2. ActivityLog → Application Logging

**Thay vì:**
```sql
CREATE TABLE ActivityLog (...)
```

**Dùng:**
```javascript
// Winston logger
import winston from 'winston';

const logger = winston.createLogger({
  level: 'info',
  format: winston.format.json(),
  transports: [
    new winston.transports.File({ filename: 'activity.log' }),
    new winston.transports.Console()
  ]
});

// Usage
logger.info('User enrolled in course', {
  userId: 'uuid',
  courseId: 'uuid',
  action: 'ENROLLMENT_CREATED',
  timestamp: new Date()
});

// Hoặc dùng service như Logtail, Datadog
```

---

#### 3. File → Cloud Storage SDK

**Thay vì:**
```sql
CREATE TABLE File (
  file_id UUID,
  uploaded_by UUID,
  file_path VARCHAR(500),
  ...
)
```

**Dùng:**
```javascript
// AWS S3
import AWS from 'aws-sdk';
const s3 = new AWS.S3();

async function uploadFile(file, metadata) {
  const key = `uploads/${userId}/${Date.now()}-${file.name}`;

  const result = await s3.upload({
    Bucket: 'blearning-files',
    Key: key,
    Body: file,
    Metadata: {
      uploadedBy: metadata.userId,
      entityType: metadata.entityType,
      entityId: metadata.entityId
    }
  }).promise();

  return result.Location; // URL
}

// Lưu URL vào AssignmentSubmission.file_urls (JSON)
```

---

#### 4. SystemSettings → Config File

**Thay vì:**
```sql
CREATE TABLE SystemSettings (
  setting_key VARCHAR(100),
  setting_value TEXT,
  ...
)
```

**Dùng:**
```javascript
// config/settings.js
export const settings = {
  MAX_FILE_SIZE_MB: 10,
  QUIZ_TIME_LIMIT_DEFAULT: 60,
  PASS_SCORE_DEFAULT: 70,
  CERTIFICATE_TEMPLATE: './templates/certificate.html',

  grading: {
    weights: {
      quiz: 0.3,
      assignment: 0.5,
      participation: 0.2
    }
  }
};

// Hoặc .env
MAX_FILE_SIZE_MB=10
QUIZ_TIME_LIMIT_DEFAULT=60
```

---

#### 5. GradeBook → Dynamic View/Query

**Thay vì:**
```sql
CREATE TABLE GradeBook (
  user_id UUID,
  course_id UUID,
  quiz_score DECIMAL,
  assignment_score DECIMAL,
  total_score DECIMAL
)
```

**Dùng View:**
```sql
CREATE VIEW GradeBook AS
SELECT
  e.user_id,
  e.course_id,

  -- Quiz score
  COALESCE(SUM(a.total_score) FILTER (WHERE a.status = 'SUBMITTED'), 0) as quiz_score,

  -- Assignment score
  COALESCE(SUM(asub.final_score) FILTER (WHERE asub.status = 'GRADED'), 0) as assignment_score,

  -- Total
  COALESCE(SUM(a.total_score), 0) + COALESCE(SUM(asub.final_score), 0) as total_score

FROM "Enrollment" e
LEFT JOIN "Attempt" a ON e.enrollment_id = a.enrollment_id
LEFT JOIN "AssignmentSubmission" asub ON e.enrollment_id = asub.enrollment_id
GROUP BY e.user_id, e.course_id;

-- Truy vấn như bảng bình thường
SELECT * FROM GradeBook WHERE user_id = '...';
```

**Hoặc dùng API endpoint:**
```javascript
GET /api/grades/user/:userId/course/:courseId

// Backend tính toán real-time
async function getGradeBook(userId, courseId) {
  const quizScore = await calculateQuizScore(userId, courseId);
  const assignmentScore = await calculateAssignmentScore(userId, courseId);

  return {
    quiz_score: quizScore,
    assignment_score: assignmentScore,
    total_score: quizScore + assignmentScore,
    letter_grade: calculateLetterGrade(total)
  };
}
```

---

## 6. SO SÁNH TRƯỚC/SAU

### 6.1. Metrics

| Metric | Trước | Sau | Thay đổi |
|--------|-------|-----|----------|
| **Số bảng** | 31 | 16 | -48% ⬇️ |
| **FK relationships** | ~45 | ~20 | -56% ⬇️ |
| **JOIN depth trung bình** | 4-5 | 2-3 | -40% ⬇️ |
| **Số trường trung bình/bảng** | 12 | 8 | -33% ⬇️ |
| **Độ phức tạp ERD** | Phức tạp | Đơn giản | ⬇️⬇️⬇️ |
| **Thời gian onboarding** | 2-3 tuần | 3-5 ngày | -70% ⬇️ |

### 6.2. Query Performance

#### Trước: Lấy progress của user

```sql
-- JOIN 6 bảng
SELECT
  ce.completion_percentage,
  p.percent_complete,
  gb.total_score
FROM CourseEnrollment ce
LEFT JOIN Progress p ON ce.course_enrollment_id = p.course_enrollment_id
LEFT JOIN Attempt a ON ce.course_enrollment_id = a.course_enrollment_id
LEFT JOIN AssignmentSubmission asub ON ce.course_enrollment_id = asub.course_enrollment_id
LEFT JOIN GradeBook gb ON ce.user_id = gb.user_id AND ce.course_id = gb.course_id
LEFT JOIN Certificate cert ON ce.user_id = cert.user_id AND ce.course_id = cert.course_id
WHERE ce.user_id = ? AND ce.course_id = ?;
```

#### Sau: Lấy progress của user

```sql
-- JOIN 3 bảng + 1 View
SELECT
  e.completion_percentage,
  COUNT(p.progress_id) FILTER (WHERE p.status = 'COMPLETED') as modules_completed,
  gb.total_score
FROM Enrollment e
LEFT JOIN Progress p ON e.user_id = p.user_id AND e.course_id = p.course_id
LEFT JOIN GradeBook gb ON e.user_id = gb.user_id AND e.course_id = gb.course_id
WHERE e.user_id = ? AND e.course_id = ?
GROUP BY e.enrollment_id, gb.total_score;
```

**Cải thiện: -50% joins, query đơn giản hơn**

---

### 6.3. Code Complexity

#### Trước: Tạo quiz với questions

```javascript
// Phải insert 3 bảng: Quiz, QuizQuestion, Notification
async function createQuiz(quizData) {
  const quiz = await Quiz.create(quizData);

  // Insert junction table
  for (const q of quizData.questions) {
    await QuizQuestion.create({
      quiz_id: quiz.quiz_id,
      question_id: q.question_id,
      points: q.points,
      order_num: q.order
    });
  }

  // Notify students
  const students = await getEnrolledStudents(quizData.course_id);
  for (const student of students) {
    await Notification.create({
      user_id: student.user_id,
      type: 'QUIZ_PUBLISHED',
      message: `New quiz: ${quiz.title}`
    });
  }

  return quiz;
}
```

#### Sau: Tạo quiz với questions

```javascript
// Chỉ insert 1 bảng + send email
async function createQuiz(quizData) {
  const quiz = await Quiz.create({
    ...quizData,
    questions: quizData.questions // JSON
  });

  // Send notification via email service
  const students = await getEnrolledStudents(quizData.course_id);
  await emailService.sendBulk({
    to: students.map(s => s.email),
    subject: `New quiz: ${quiz.title}`,
    template: 'quiz-published'
  });

  return quiz;
}
```

**Cải thiện: Code ngắn hơn 50%, ít transaction hơn**

---

## 7. LỢI ÍCH & RỦI RO

### 7.1. Lợi ích

#### ✅ Lợi ích kỹ thuật

| Lợi ích | Mô tả | Mức độ |
|---------|-------|--------|
| **Đơn giản hơn** | Giảm 48% số bảng | ⭐⭐⭐⭐⭐ |
| **Query nhanh hơn** | Ít JOIN, ít lock | ⭐⭐⭐⭐ |
| **Dễ maintain** | Ít migration, ít bug | ⭐⭐⭐⭐⭐ |
| **Onboarding nhanh** | Dev mới hiểu nhanh hơn | ⭐⭐⭐⭐⭐ |
| **Flexible hơn** | JSON dễ mở rộng | ⭐⭐⭐⭐ |

#### ✅ Lợi ích nghiệp vụ

- Giữ 100% chức năng core
- Giảm time-to-market
- Giảm chi phí vận hành
- Dễ scale horizontal

---

### 7.2. Rủi ro & Giải pháp

#### ⚠️ Rủi ro 1: Mất tính chuẩn hóa (Normalization)

**Vấn đề:** Dùng JSON thay vì bảng riêng
- QuizQuestion → Quiz.questions (JSON)
- QuizSubmission → Attempt.answers (JSON)

**Rủi ro:**
- Khó query JSON (PostgreSQL hỗ trợ tốt nhưng không bằng JOIN)
- Không có FK constraint cho JSON fields

**Giải pháp:**
```sql
-- PostgreSQL hỗ trợ query JSON tốt
SELECT * FROM Quiz
WHERE questions @> '[{"question_id": "uuid"}]';

-- Có thể tạo index cho JSON
CREATE INDEX idx_quiz_questions ON Quiz USING GIN (questions);

-- Validate JSON với CHECK constraint
ALTER TABLE Quiz ADD CONSTRAINT chk_questions_format
CHECK (jsonb_typeof(questions) = 'array');

-- Trigger để validate structure
CREATE TRIGGER validate_quiz_questions
BEFORE INSERT OR UPDATE ON Quiz
FOR EACH ROW EXECUTE FUNCTION validate_questions_json();
```

---

#### ⚠️ Rủi ro 2: Mất audit trail chi tiết

**Vấn đề:** Bỏ ActivityLog table

**Rủi ro:** Khó debug, không có audit log

**Giải pháp:**
```javascript
// Dùng application logging + external service
import winston from 'winston';
import { LogtailTransport } from '@logtail/winston';

const logger = winston.createLogger({
  transports: [
    new winston.transports.File({ filename: 'audit.log' }),
    new LogtailTransport(logtailToken) // Cloud logging
  ]
});

// Middleware audit
app.use((req, res, next) => {
  logger.info('API Request', {
    userId: req.user?.id,
    method: req.method,
    path: req.path,
    body: req.body,
    timestamp: new Date()
  });
  next();
});

// Hoặc dùng PostgreSQL audit extension
CREATE EXTENSION IF NOT EXISTS pgaudit;
```

---

#### ⚠️ Rủi ro 3: Performance với JSON query

**Vấn đề:** Query JSON chậm hơn JOIN

**Rủi ro:** Performance giảm khi data lớn

**Giải pháp:**
```sql
-- 1. Tạo GIN index cho JSON fields
CREATE INDEX idx_quiz_questions ON Quiz USING GIN (questions);
CREATE INDEX idx_attempt_answers ON Attempt USING GIN (answers);

-- 2. Dùng materialized view nếu cần
CREATE MATERIALIZED VIEW QuizQuestionFlat AS
SELECT
  q.quiz_id,
  (jsonb_array_elements(q.questions)->>'question_id')::UUID as question_id,
  (jsonb_array_elements(q.questions)->>'points')::DECIMAL as points
FROM Quiz q;

REFRESH MATERIALIZED VIEW QuizQuestionFlat;

-- 3. Monitor performance
EXPLAIN ANALYZE
SELECT * FROM Quiz WHERE questions @> '[{"question_id": "uuid"}]';
```

---

#### ⚠️ Rủi ro 4: Mất notification history

**Vấn đề:** Bỏ Notification table

**Rủi ro:** User không thấy lịch sử thông báo

**Giải pháp:**
```javascript
// Option 1: Lưu trong User table
ALTER TABLE "User" ADD COLUMN notification_history JSON DEFAULT '[]';

// Structure:
{
  "notifications": [
    {
      "id": "uuid",
      "type": "ASSIGNMENT_DUE",
      "title": "Assignment due soon",
      "message": "...",
      "read": false,
      "created_at": "2025-11-27T10:00:00Z"
    }
  ]
}

// Option 2: Dùng Redis cho real-time notifications
import Redis from 'ioredis';
const redis = new Redis();

// Lưu notifications trong Redis (TTL 30 days)
await redis.setex(
  `notifications:${userId}`,
  30 * 24 * 60 * 60,
  JSON.stringify(notifications)
);

// Option 3: Giữ lại Notification table nhưng đơn giản
CREATE TABLE "Notification" (
  notification_id UUID PRIMARY KEY,
  user_id UUID NOT NULL,
  type VARCHAR(50),
  message TEXT,
  is_read BOOLEAN DEFAULT FALSE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
-- Không cần NotificationPreference, NotificationLog
```

---

## 8. KẾ HOẠCH TRIỂN KHAI

### 8.1. Timeline

```
Week 1: Preparation & Design
├── Day 1-2: Review & approve proposal
├── Day 3-4: Design new schema details
└── Day 5: Create migration scripts

Week 2: Implementation
├── Day 1-2: Create new tables
├── Day 3: Migrate data (Phase 1: Core tables)
└── Day 4-5: Migrate data (Phase 2: JSON consolidation)

Week 3: Testing & Rollout
├── Day 1-2: Integration testing
├── Day 3: Performance testing
├── Day 4: Staging deployment
└── Day 5: Production deployment
```

---

### 8.2. Migration Steps

#### Step 1: Backup hiện tại

```bash
# Backup toàn bộ database
pg_dump b_learning > backup_$(date +%Y%m%d).sql

# Backup specific tables
pg_dump -t "QuizQuestion" -t "QuizSubmission" b_learning > backup_before_merge.sql
```

---

#### Step 2: Tạo tables mới

```sql
-- 1. Thêm cột JSON vào Quiz
ALTER TABLE "Quiz" ADD COLUMN questions JSON;

-- 2. Migrate data từ QuizQuestion
UPDATE "Quiz" q
SET questions = (
  SELECT json_agg(
    json_build_object(
      'question_id', qq.question_id,
      'points', qq.points,
      'order', qq.order_num
    ) ORDER BY qq.order_num
  )
  FROM "QuizQuestion" qq
  WHERE qq.quiz_id = q.quiz_id
);

-- 3. Verify
SELECT quiz_id, questions FROM "Quiz" LIMIT 5;
```

---

#### Step 3: Migrate Attempt + QuizSubmission

```sql
-- 1. Thêm cột JSON vào Attempt
ALTER TABLE "Attempt" ADD COLUMN answers JSON;

-- 2. Migrate data
UPDATE "Attempt" a
SET answers = (
  SELECT json_agg(
    json_build_object(
      'question_id', qs.question_id,
      'answer_text', qs.answer_text,
      'selected_options', qs.selected_option_ids,
      'score', qs.final_score,
      'max_score', qs.max_points,
      'feedback', qs.instructor_feedback
    )
  )
  FROM "QuizSubmission" qs
  WHERE qs.attempt_id = a.attempt_id
);

-- 3. Verify
SELECT attempt_id, answers FROM "Attempt" LIMIT 5;
```

---

#### Step 4: Merge CourseEnrollment + ClassEnrollment

```sql
-- 1. Tạo bảng mới
CREATE TABLE "Enrollment" (
  enrollment_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID NOT NULL,
  course_id UUID NOT NULL,
  class_id UUID, -- NULLABLE
  role VARCHAR(20) NOT NULL,
  status VARCHAR(20) DEFAULT 'ACTIVE',
  enrolled_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  completed_at TIMESTAMP,
  completion_percentage DECIMAL(5,2) DEFAULT 0
);

-- 2. Migrate từ CourseEnrollment
INSERT INTO "Enrollment" (
  enrollment_id, user_id, course_id, class_id,
  role, status, enrolled_at, completed_at, completion_percentage
)
SELECT
  ce.course_enrollment_id,
  ce.user_id,
  ce.course_id,
  cle.class_id, -- NULL nếu không có
  ce.role_in_course,
  ce.enrollment_status,
  ce.enrolled_at,
  ce.completed_at,
  ce.completion_percentage
FROM "CourseEnrollment" ce
LEFT JOIN "ClassEnrollment" cle ON ce.course_enrollment_id = cle.course_enrollment_id;

-- 3. Verify
SELECT COUNT(*) FROM "Enrollment";
SELECT COUNT(*) FROM "CourseEnrollment";
-- Should match
```

---

#### Step 5: Drop old tables

```sql
-- Sau khi verify kỹ, drop old tables
DROP TABLE IF EXISTS "QuizQuestion" CASCADE;
DROP TABLE IF EXISTS "QuizSubmission" CASCADE;
DROP TABLE IF EXISTS "ClassEnrollment" CASCADE;
DROP TABLE IF EXISTS "CourseEnrollment" CASCADE;
DROP TABLE IF EXISTS "GradeBook" CASCADE; -- Replaced by view
DROP TABLE IF EXISTS "Notification" CASCADE;
DROP TABLE IF EXISTS "NotificationPreference" CASCADE;
DROP TABLE IF EXISTS "NotificationLog" CASCADE;
DROP TABLE IF EXISTS "ActivityLog" CASCADE;
DROP TABLE IF EXISTS "File" CASCADE;
DROP TABLE IF EXISTS "SystemSettings" CASCADE;
DROP TABLE IF EXISTS "CertificateTemplate" CASCADE;
DROP TABLE IF EXISTS "CertificateVerification" CASCADE;
```

---

#### Step 6: Create views

```sql
-- GradeBook view
CREATE VIEW GradeBook AS
SELECT
  e.user_id,
  e.course_id,
  COALESCE(SUM(a.total_score), 0) as quiz_score,
  COALESCE(SUM(asub.final_score), 0) as assignment_score,
  COALESCE(SUM(a.total_score), 0) + COALESCE(SUM(asub.final_score), 0) as total_score
FROM "Enrollment" e
LEFT JOIN "Attempt" a ON e.enrollment_id = a.enrollment_id AND a.status = 'SUBMITTED'
LEFT JOIN "AssignmentSubmission" asub ON e.enrollment_id = asub.enrollment_id AND asub.status = 'GRADED'
GROUP BY e.user_id, e.course_id;
```

---

### 8.3. Testing Checklist

```markdown
Phase 1: Data Integrity
- [ ] Verify all CourseEnrollment migrated to Enrollment
- [ ] Verify Quiz.questions JSON format correct
- [ ] Verify Attempt.answers JSON format correct
- [ ] Verify no data loss during migration
- [ ] Verify FK constraints still work

Phase 2: Application Testing
- [ ] Test quiz creation with questions
- [ ] Test quiz taking and submission
- [ ] Test enrollment (self-paced and class-based)
- [ ] Test progress tracking
- [ ] Test certificate generation
- [ ] Test grade calculation

Phase 3: Performance Testing
- [ ] Benchmark Quiz queries with JSON
- [ ] Benchmark Attempt queries with JSON
- [ ] Compare query performance before/after
- [ ] Check index usage
- [ ] Load testing with 1000+ users

Phase 4: Integration Testing
- [ ] Test all API endpoints
- [ ] Test frontend integration
- [ ] Test notification emails
- [ ] Test file uploads
- [ ] Test edge cases
```

---

### 8.4. Rollback Plan

```sql
-- Nếu có vấn đề, rollback
-- Step 1: Restore từ backup
psql b_learning < backup_20251127.sql

-- Step 2: Hoặc chỉ restore specific tables
pg_restore -t "QuizQuestion" backup_before_merge.sql

-- Step 3: Verify
SELECT COUNT(*) FROM "QuizQuestion";
```

---

## 9. KẾT LUẬN & KHUYẾN NGHỊ

### 9.1. Tóm tắt

Đề xuất tinh giản database từ **31 bảng → 16 bảng** (-48%) bằng cách:

1. **Bỏ 9 bảng** không core (Notification, ActivityLog, File, SystemSettings, v.v.)
2. **Gộp 6 bảng → 3 bảng** (QuizQuestion, QuizSubmission, Enrollment)
3. **Đơn giản hóa 3 bảng** (Certificate, Assignment, Progress)
4. **Dùng JSON** cho data có cấu trúc linh hoạt
5. **Dùng external services** cho Notification, Logging, File storage

---

### 9.2. Khuyến nghị

#### ✅ NÊN THỰC HIỆN

| Khuyến nghị | Lý do | Ưu tiên |
|-------------|-------|---------|
| **Gộp QuizQuestion → Quiz.questions** | Đơn giản hóa đáng kể | 🔴 Cao |
| **Gộp QuizSubmission → Attempt.answers** | Giảm joins | 🔴 Cao |
| **Gộp CourseEnrollment + ClassEnrollment** | Logic rõ ràng hơn | 🔴 Cao |
| **Bỏ Notification tables** | Dùng email service | 🟡 Trung bình |
| **Bỏ ActivityLog** | Dùng app logging | 🟡 Trung bình |
| **Bỏ File table** | Dùng cloud storage | 🟢 Thấp |
| **GradeBook → View** | Tính động chính xác hơn | 🔴 Cao |

---

#### ⚠️ CẦN CÂN NHẮC

| Vấn đề | Cân nhắc | Quyết định |
|--------|----------|------------|
| **JSON vs Normalization** | Trade-off giữa đơn giản và chuẩn hóa | ✅ Chấp nhận - PostgreSQL hỗ trợ JSON tốt |
| **Mất Notification history** | User có thấy lịch sử không? | ⚠️ Có thể giữ lại 1 bảng Notification đơn giản |
| **Performance với JSON** | Query có chậm không? | ✅ OK - Dùng GIN index |
| **Migration complexity** | Có rủi ro data loss không? | ✅ OK - Có rollback plan |

---

### 9.3. Quyết định cuối cùng

#### Option A: Tinh giản Mạnh (16 bảng) - KHUYẾN NGHỊ

**Pros:**
- ✅ Đơn giản nhất
- ✅ Dễ hiểu nhất
- ✅ Phù hợp với hệ thống nhỏ/trung bình

**Cons:**
- ⚠️ Mất một số audit capabilities
- ⚠️ Dùng JSON nhiều hơn

**Phù hợp:** Hệ thống startup, MVP, < 10,000 users

---

#### Option B: Tinh giản Vừa phải (20 bảng)

**Thêm vào Option A:**
- Giữ lại Notification (1 bảng đơn giản)
- Giữ lại ActivityLog (đơn giản hóa)

**Pros:**
- ✅ Vẫn đơn giản
- ✅ Có audit trail cơ bản
- ✅ Có notification history

**Cons:**
- ⚠️ Phức tạp hơn chút

**Phù hợp:** Hệ thống vừa, 10,000 - 100,000 users

---

#### Option C: Giữ nguyên (31 bảng)

**Không khuyến nghị** vì:
- ❌ Quá phức tạp cho người mới
- ❌ Quá nhiều bảng cho chức năng hiện tại
- ❌ Nhiều bảng chưa dùng đến (over-engineering)

---

### 9.4. Next Steps

1. **Week 1:** Review & approve proposal này
2. **Week 2:** Tạo migration scripts chi tiết
3. **Week 3:** Thực hiện migration trên staging
4. **Week 4:** Test & deploy production

---

## PHỤ LỤC

### A. Danh sách đầy đủ 16 bảng sau tinh giản

```
1. User           - Người dùng
2. Role           - Vai trò
3. UserRole       - Phân quyền

4. Course         - Khóa học
5. Module         - Chương học
6. Lecture        - Bài giảng (+ Assignment)
7. Resource       - Tài liệu

8. Quiz           - Quiz (+ questions JSON)
9. Question       - Câu hỏi
10. Option        - Lựa chọn
11. Attempt       - Làm bài (+ answers JSON)
12. AssignmentSubmission - Nộp bài

13. Enrollment    - Đăng ký (Course + Class)
14. Progress      - Tiến độ

15. Class         - Lớp học (+ schedules JSON)
16. Certificate   - Chứng chỉ
```

### B. Các bảng đã bỏ (15 bảng)

```
❌ QuizQuestion              → Gộp vào Quiz.questions
❌ QuizSubmission            → Gộp vào Attempt.answers
❌ Assignment                → Lecture với type='ASSIGNMENT'
❌ GradeBook                 → View/Query động
❌ CourseEnrollment          → Gộp vào Enrollment
❌ ClassEnrollment           → Gộp vào Enrollment
❌ Attendance                → Class.schedules[].attendances
❌ Schedule                  → Class.schedules JSON
❌ CertificateTemplate       → File template
❌ CertificateVerification   → Verify trực tiếp
❌ Notification              → Email service
❌ NotificationPreference    → User.preferences JSON
❌ NotificationLog           → Application logs
❌ ActivityLog               → Application logs
❌ File                      → Cloud storage
❌ SystemSettings            → Config files
```

---

**KẾT THÚC ĐỀ XUẤT**

---

**Người duyệt:** ___________________
**Ngày duyệt:** ___________________
**Quyết định:** [ ] Option A (16 bảng) [ ] Option B (20 bảng) [ ] Từ chối
