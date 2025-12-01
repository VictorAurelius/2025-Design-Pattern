# Hướng Dẫn Demo Quản Lý Bài Nộp - Tài Liệu Kỹ Thuật

**Tác giả:** Nguyễn Văn Kiệt - CNTT1-K63
**Dự án:** Nền Tảng B-Learning
**Phiên bản:** 1.0
**Cập nhật:** 1 tháng 12, 2025

---

## Mục Lục

1. [Tổng Quan](#tổng-quan)
2. [Kiến Trúc Hệ Thống](#kiến-trúc-hệ-thống)
3. [Cơ Sở Dữ Liệu](#cơ-sở-dữ-liệu)
4. [API Backend](#api-backend)
5. [Giao Diện Frontend](#giao-diện-frontend)
6. [Luồng Dữ Liệu](#luồng-dữ-liệu)
7. [Dữ Liệu Mẫu](#dữ-liệu-mẫu)
8. [Hướng Dẫn Kiểm Thử](#hướng-dẫn-kiểm-thử)
9. [Xử Lý Lỗi Thường Gặp](#xử-lý-lỗi-thường-gặp)
10. [Phát Triển Tương Lai](#phát-triển-tương-lai)

---

## Tổng Quan

### Demo Quản Lý Bài Nộp Là Gì?

Tính năng Quản Lý Bài Nộp minh họa cho việc **JOIN nhiều bảng** trong hệ thống quản lý học tập (LMS). Cho phép:

- **Sinh viên** nộp bài tập
- **Giảng viên** chấm điểm bài nộp
- **Hệ thống** theo dõi tiến độ, tính điểm và tạo thống kê

### Tính Năng Chính

✅ **JOIN Nhiều Bảng** - Kết hợp dữ liệu từ 5+ bảng
✅ **Thao Tác CRUD** - Tạo, Đọc, Cập nhật bài nộp
✅ **Hệ Thống Chấm Điểm** - Chấm thủ công với feedback
✅ **Dashboard Thống Kê** - Phân tích theo thời gian thực
✅ **Lọc & Tìm Kiếm** - Theo trạng thái, sinh viên, khóa học, bài tập
✅ **Theo Dõi Nộp Trễ** - Tính toán penalty
✅ **Type-safe API** - Pydantic models + TypeScript types

---

## Kiến Trúc Hệ Thống

### Công Nghệ Sử Dụng

```
┌─────────────────────────────────────────────────────┐
│                Frontend (Next.js)                   │
│  ┌─────────────────────────────────────────────┐   │
│  │  submissions/page.tsx (TypeScript + React)  │   │
│  │  - Hiển thị danh sách bài nộp               │   │
│  │  - Modal chấm điểm                          │   │
│  │  - Thẻ thống kê                             │   │
│  └─────────────────────────────────────────────┘   │
│                      ↓ HTTP (Axios)                 │
└─────────────────────────────────────────────────────┘
                       ↓
┌─────────────────────────────────────────────────────┐
│              Backend (FastAPI + Python)             │
│  ┌─────────────────────────────────────────────┐   │
│  │  routes/submissions.py                      │   │
│  │  - GET /api/submissions                     │   │
│  │  - GET /api/submissions/{id}                │   │
│  │  - PUT /api/submissions/{id}/grade          │   │
│  │  - GET /api/submissions/stats/overview      │   │
│  └─────────────────────────────────────────────┘   │
│                      ↓ SQL Queries                  │
└─────────────────────────────────────────────────────┘
                       ↓
┌─────────────────────────────────────────────────────┐
│             Cơ Sở Dữ Liệu (PostgreSQL 14+)         │
│  Các bảng:                                          │
│  - AssignmentSubmission (chính)                     │
│  - User (sinh viên + giảng viên)                    │
│  - Lecture (bài tập)                                │
│  - Module (chương học)                              │
│  - Course (khóa học)                                │
│  - Enrollment (đăng ký học)                         │
└─────────────────────────────────────────────────────┘
```

### Cấu Trúc Thư Mục

```
B-Learning-Demo/
├── backend/
│   ├── app/
│   │   ├── main.py                    # Ứng dụng FastAPI
│   │   ├── routes/
│   │   │   └── submissions.py         # Các endpoint API
│   │   └── models/
│   │       └── submission.py          # Pydantic schemas
│   └── config/
│       └── database.py                # Kết nối DB
├── frontend/
│   ├── app/
│   │   └── submissions/
│   │       └── page.tsx               # Trang bài nộp
│   ├── lib/
│   │   └── api.ts                     # Axios client
│   └── types/
│       └── submission.ts              # TypeScript types
└── db/
    └── init/
        ├── 01-schema.sql              # Schema database
        ├── 02-indexes.sql             # Indexes
        ├── 03-constraints.sql         # Constraints
        └── 04-seed-data.sql           # Dữ liệu mẫu
```

---

## Cơ Sở Dữ Liệu

### Sơ Đồ Quan Hệ Thực Thể (ERD)

```
┌─────────────────┐
│      User       │
│─────────────────│
│ user_id (PK)    │──┐
│ email           │  │
│ first_name      │  │
│ last_name       │  │
│ ...             │  │
└─────────────────┘  │
         │           │
         │ 1         │ 1
         │           │
         ↓ *         ↓ *
┌──────────────────────────┐      ┌─────────────────┐
│  AssignmentSubmission    │      │   Enrollment    │
│──────────────────────────│      │─────────────────│
│ submission_id (PK)       │      │ enrollment_id   │
│ user_id (FK) ────────────┼──────┤ user_id (FK)    │
│ lecture_id (FK)          │      │ course_id (FK)  │
│ enrollment_id (FK)       │──────┤                 │
│ submission_number        │      └─────────────────┘
│ submitted_at             │               │
│ content                  │               │
│ file_urls (JSON)         │               │
│ code_submission          │               │
│ is_late                  │               ↓
│ status                   │      ┌─────────────────┐
│ score                    │      │     Course      │
│ max_score                │      │─────────────────│
│ feedback                 │      │ course_id (PK)  │
│ graded_at                │      │ code            │
│ graded_by (FK) ──────────┼──┐   │ title           │
└──────────────────────────┘  │   │ ...             │
         │                    │   └─────────────────┘
         │                    │            │
         ↓                    │            ↓ 1
┌─────────────────┐           │   ┌─────────────────┐
│    Lecture      │           │   │     Module      │
│─────────────────│           │   │─────────────────│
│ lecture_id (PK) │           │   │ module_id (PK)  │
│ module_id (FK)  │───────────┼───┤ course_id (FK)  │
│ title           │           │   │ title           │
│ type            │           │   │ order_num       │
│ assignment_...  │           │   └─────────────────┘
└─────────────────┘           │
                              │
                              └──→ User (người chấm)
```

### Các Bảng Chính

#### 1. AssignmentSubmission (Bảng Chính)

```sql
CREATE TABLE "AssignmentSubmission" (
  submission_id UUID PRIMARY KEY,
  lecture_id UUID NOT NULL REFERENCES "Lecture"(lecture_id),
  user_id UUID NOT NULL REFERENCES "User"(user_id),
  enrollment_id UUID NOT NULL REFERENCES "Enrollment"(enrollment_id),
  submission_number INT NOT NULL DEFAULT 1,
  submitted_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  content TEXT,
  file_urls JSON,                    -- Mảng URL files
  code_submission TEXT,
  is_late BOOLEAN DEFAULT FALSE,
  status VARCHAR(20) NOT NULL DEFAULT 'SUBMITTED',
  score DECIMAL(6,2),                -- Điểm cuối cùng
  max_score DECIMAL(6,2),
  feedback TEXT,
  graded_at TIMESTAMP,
  graded_by UUID REFERENCES "User"(user_id)
);
```

**Các Giá Trị Status:**
- `DRAFT` - Chưa nộp
- `SUBMITTED` - Đã nộp, đang chờ chấm
- `GRADING` - Đang được chấm
- `GRADED` - Đã chấm xong có điểm và feedback
- `RETURNED` - Đã trả lại cho sinh viên kèm feedback

#### 2. Bảng User

```sql
CREATE TABLE "User" (
  user_id UUID PRIMARY KEY,
  email VARCHAR(255) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  first_name VARCHAR(100) NOT NULL,
  last_name VARCHAR(100) NOT NULL,
  account_status VARCHAR(30) DEFAULT 'ACTIVE'
);
```

**Vai trò:** Student, Instructor, TA, Admin (qua bảng UserRole)

#### 3. Bảng Lecture (Bài Tập)

```sql
CREATE TABLE "Lecture" (
  lecture_id UUID PRIMARY KEY,
  module_id UUID NOT NULL REFERENCES "Module"(module_id),
  title VARCHAR(200) NOT NULL,
  description TEXT,
  type VARCHAR(20) NOT NULL,  -- 'ASSIGNMENT', 'VIDEO', 'PDF', v.v.
  assignment_config JSON
);
```

### Ràng Buộc Quan Trọng

#### Ràng Buộc Tính Toàn Vẹn Chấm Điểm

```sql
-- Đảm bảo bài nộp đã chấm có thông tin người chấm
ALTER TABLE "AssignmentSubmission"
  ADD CONSTRAINT chk_assignment_graded_has_info
  CHECK (
    (graded_at IS NULL AND graded_by IS NULL) OR
    (graded_at IS NOT NULL AND graded_by IS NOT NULL)
  );
```

**Tại sao?** Ngăn chặn dữ liệu chấm điểm bị mồ côi - nếu bài nộp được chấm (`graded_at` được set), nó BẮT BUỘC phải có người chấm (`graded_by`).

---

## API Backend

### Các Endpoint API

#### 1. GET /api/submissions

**Mục đích:** Lấy danh sách bài nộp với các bộ lọc

**Query Parameters:**
```typescript
{
  course_id?: string,
  assignment_id?: string,
  status?: 'SUBMITTED' | 'GRADED' | 'GRADING' | 'DRAFT' | 'RETURNED',
  is_late?: boolean,
  student_email?: string,
  limit?: number,    // Mặc định: 50, Tối đa: 100
  offset?: number    // Mặc định: 0
}
```

**SQL Query (Đơn giản hóa):**
```sql
SELECT
  -- Các trường AssignmentSubmission
  asub.submission_id as assignment_submission_id,
  asub.submission_number,
  asub.submitted_at,
  asub.status,
  asub.score as final_score,
  asub.feedback,

  -- Thông tin sinh viên (JOIN User)
  u.email as student_email,
  CONCAT(u.first_name, ' ', u.last_name) as student_name,

  -- Thông tin bài tập (JOIN Lecture)
  l.title as assignment_title,

  -- Thông tin khóa học (JOIN Module + Course)
  c.code as course_code,
  c.title as course_title,

  -- Thông tin người chấm (LEFT JOIN User)
  CONCAT(grader.first_name, ' ', grader.last_name) as graded_by_name

FROM "AssignmentSubmission" asub
INNER JOIN "User" u ON asub.user_id = u.user_id
INNER JOIN "Lecture" l ON asub.lecture_id = l.lecture_id
INNER JOIN "Module" m ON l.module_id = m.module_id
INNER JOIN "Course" c ON m.course_id = c.course_id
LEFT JOIN "User" grader ON asub.graded_by = grader.user_id
WHERE l.type = 'ASSIGNMENT'
ORDER BY asub.submitted_at DESC
LIMIT ? OFFSET ?
```

**Response:**
```json
{
  "total": 2,
  "submissions": [
    {
      "assignment_submission_id": "f0000000-0000-0000-0000-000000000001",
      "student_name": "Minh Le Quang",
      "student_email": "minh.le@student.blearning.edu.vn",
      "assignment_title": "Bai Tap: Hello World",
      "course_title": "Lap Trinh Java Co Ban",
      "status": "GRADED",
      "final_score": 85.00,
      "max_points": 100.00,
      "submitted_at": "2025-11-19T10:52:57.783179",
      "graded_by_name": "Kiet Nguyen Van"
    }
  ]
}
```

#### 2. GET /api/submissions/{submission_id}

**Mục đích:** Lấy thông tin chi tiết về một bài nộp

**Response:**
```json
{
  "assignment_submission_id": "f0000000-0000-0000-0000-000000000001",
  "submission_number": 1,
  "submitted_at": "2025-11-19T10:52:57.783179",
  "content": null,
  "file_urls": [
    "https://s3.amazonaws.com/blearning/submissions/minh-le-hello-world.zip"
  ],
  "code_submission": null,
  "is_late": false,
  "status": "GRADED",
  "score": 85.00,
  "feedback": "Code chay tot, logic dung. Can improve code documentation.",
  "student_name": "Minh Le Quang",
  "assignment_title": "Bai Tap: Hello World",
  "course_title": "Lap Trinh Java Co Ban"
}
```

#### 3. PUT /api/submissions/{submission_id}/grade

**Mục đích:** Chấm điểm một bài nộp

**Request Body:**
```json
{
  "manual_score": 85.0,
  "feedback": "Làm tốt lắm! Code sạch và có cấu trúc."
}
```

**Quy trình:**
1. Kiểm tra bài nộp tồn tại và status là `SUBMITTED` hoặc `GRADING`
2. Kiểm tra `manual_score <= max_points`
3. Tính `final_score = auto_score + manual_score - penalty`
4. Cập nhật bài nộp với:
   - `score = final_score`
   - `feedback = feedback được cung cấp`
   - `status = 'GRADED'`
   - `graded_at = CURRENT_TIMESTAMP`
   - `graded_by = instructor_id` (hardcode cho demo)

**SQL:**
```sql
UPDATE "AssignmentSubmission"
SET
  score = ?,
  feedback = ?,
  status = 'GRADED',
  graded_at = CURRENT_TIMESTAMP,
  graded_by = '20000000-0000-0000-0000-000000000002'
WHERE submission_id = ?
```

#### 4. GET /api/submissions/stats/overview

**Mục đích:** Lấy thống kê bài nộp

**Response:**
```json
{
  "total_submissions": 2,
  "submitted": 1,
  "graded": 1,
  "pending": 1,
  "average_score": 85.00,
  "late_submissions": 0,
  "on_time_submissions": 2
}
```

**SQL:**
```sql
SELECT
  COUNT(*) as total_submissions,
  COUNT(*) FILTER (WHERE status = 'SUBMITTED') as submitted,
  COUNT(*) FILTER (WHERE status = 'GRADED') as graded,
  COUNT(*) FILTER (WHERE status IN ('SUBMITTED', 'GRADING')) as pending,
  AVG(score) FILTER (WHERE status = 'GRADED') as average_score,
  COUNT(*) FILTER (WHERE is_late = TRUE) as late_submissions,
  COUNT(*) FILTER (WHERE is_late = FALSE) as on_time_submissions
FROM "AssignmentSubmission" asub
INNER JOIN "Lecture" l ON asub.lecture_id = l.lecture_id
WHERE l.type = 'ASSIGNMENT'
```

---

## Giao Diện Frontend

### Trang Submissions (`frontend/app/submissions/page.tsx`)

#### Cấu Trúc Component

```tsx
export default function SubmissionsPage() {
  // Quản lý State
  const [submissions, setSubmissions] = useState<SubmissionListItem[]>([]);
  const [stats, setStats] = useState<SubmissionStats | null>(null);
  const [filters, setFilters] = useState({...});

  // Lấy Dữ Liệu
  useEffect(() => {
    fetchSubmissions();
    fetchStats();
  }, [page, statusFilter, ...]);

  // Render
  return (
    <>
      {/* Thẻ Thống Kê */}
      <StatsCards stats={stats} />

      {/* Bộ Lọc */}
      <FilterBar filters={filters} onChange={setFilters} />

      {/* Bảng Bài Nộp */}
      <SubmissionsTable
        submissions={submissions}
        onGrade={handleGrade}
      />

      {/* Modal Chấm Điểm */}
      {gradingSubmission && (
        <GradingModal
          submission={gradingSubmission}
          onSubmit={handleGradeSubmit}
        />
      )}
    </>
  );
}
```

#### Tính Năng Chính

**1. Dashboard Thống Kê**
```tsx
<div className="grid grid-cols-4 gap-4">
  <StatCard title="Tổng" value={stats.total_submissions} />
  <StatCard title="Đã Chấm" value={stats.graded} />
  <StatCard title="Chờ Chấm" value={stats.pending} />
  <StatCard title="Điểm TB" value={stats.average_score?.toFixed(2)} />
</div>
```

**2. Thanh Lọc**
```tsx
<select value={statusFilter} onChange={(e) => setStatusFilter(e.target.value)}>
  <option value="">Tất Cả Trạng Thái</option>
  <option value="SUBMITTED">Đã Nộp</option>
  <option value="GRADED">Đã Chấm</option>
  <option value="GRADING">Đang Chấm</option>
</select>
```

**3. Bảng Bài Nộp**
```tsx
<table>
  <thead>
    <tr>
      <th>Sinh Viên</th>
      <th>Bài Tập</th>
      <th>Khóa Học</th>
      <th>Ngày Nộp</th>
      <th>Trạng Thái</th>
      <th>Điểm</th>
      <th>Thao Tác</th>
    </tr>
  </thead>
  <tbody>
    {submissions.map(sub => (
      <tr key={sub.assignment_submission_id}>
        <td>{sub.student_name}</td>
        <td>{sub.assignment_title}</td>
        <td>{sub.course_code}</td>
        <td>{formatDate(sub.submitted_at)}</td>
        <td>
          <StatusBadge status={sub.status} />
        </td>
        <td>
          {sub.final_score !== null
            ? Number(sub.final_score).toFixed(2)
            : '-'}
        </td>
        <td>
          <button onClick={() => handleGrade(sub.assignment_submission_id)}>
            {sub.status === 'GRADED' ? 'Xem' : 'Chấm'}
          </button>
        </td>
      </tr>
    ))}
  </tbody>
</table>
```

**4. Modal Chấm Điểm**
```tsx
<Modal>
  <h2>Chấm Bài Nộp</h2>

  {/* Thông Tin Sinh Viên & Bài Tập */}
  <div>Sinh viên: {submission.student_name}</div>
  <div>Bài tập: {submission.assignment_title}</div>

  {/* Files Đã Nộp */}
  <div>
    Files:
    {submission.file_urls?.map(url => (
      <a href={url} target="_blank">{url}</a>
    ))}
  </div>

  {/* Form Chấm Điểm */}
  <form onSubmit={handleSubmit}>
    <input
      type="number"
      value={gradeData.manual_score}
      max={submission.max_points}
    />
    <textarea
      placeholder="Nhận xét..."
      value={gradeData.feedback}
    />
    <button type="submit">Gửi Điểm</button>
  </form>
</Modal>
```

---

## Luồng Dữ Liệu

### 1. Luồng Tải Trang

```
User mở /submissions
    ↓
Component mount (useEffect)
    ↓
┌───────────────────────────────────┐
│ fetchSubmissions()                │
│ - GET /api/submissions?limit=10   │
└───────────────────────────────────┘
    ↓
Backend: submissions.py
    ↓
Thực thi SQL với JOINs:
  AssignmentSubmission
  → User (sinh viên)
  → Lecture (bài tập)
  → Module
  → Course
  → User (người chấm)
    ↓
Trả về JSON response
    ↓
Frontend cập nhật state
    ↓
Render bảng với dữ liệu
```

### 2. Luồng Chấm Bài Nộp

```
User click nút "Chấm"
    ↓
fetchSubmissionDetail(id)
    ↓
GET /api/submissions/{id}
    ↓
Backend trả về chi tiết đầy đủ
    ↓
Mở modal với form
    ↓
User nhập điểm & nhận xét
    ↓
User click "Gửi Điểm"
    ↓
PUT /api/submissions/{id}/grade
  Body: {
    manual_score: 85,
    feedback: "Làm tốt lắm!"
  }
    ↓
Backend kiểm tra:
  - Bài nộp tồn tại?
  - Status là SUBMITTED hoặc GRADING?
  - Điểm <= max_points?
    ↓
Tính final_score
    ↓
UPDATE AssignmentSubmission
SET
  score = final_score,
  feedback = feedback,
  status = 'GRADED',
  graded_at = NOW(),
  graded_by = instructor_id
    ↓
Trả về bài nộp đã cập nhật
    ↓
Frontend làm mới danh sách
    ↓
Hiển thị thông báo thành công
```

### 3. Luồng Type Safety

```
Database (PostgreSQL)
  ↓
  DECIMAL, UUID, TIMESTAMP, JSON
  ↓
Backend (Python/Pydantic)
  ↓
  class SubmissionListItem(BaseModel):
    final_score: Optional[Decimal]
    submitted_at: datetime
    file_urls: Optional[list]
  ↓
API Response (JSON)
  ↓
  {
    "final_score": "85.00",  // String từ Decimal
    "submitted_at": "2025-11-19T10:52:57",
    "file_urls": ["url1", "url2"]
  }
  ↓
Frontend (TypeScript)
  ↓
  interface SubmissionListItem {
    final_score?: number | null;
    submitted_at: string;
    file_urls?: any | null;
  }
  ↓
Chuyển đổi runtime
  ↓
  Number(sub.final_score).toFixed(2)
  new Date(sub.submitted_at).toLocaleDateString()
```

---

## Dữ Liệu Mẫu

### Tổng Quan

Dữ liệu mẫu trong `04-seed-data.sql` tạo môi trường demo thực tế:

```sql
-- 8 Users: 1 admin, 2 giảng viên, 5 sinh viên
-- 3 Courses: Java, Web Frontend, Database
-- 6 Modules: 2 mỗi khóa học
-- 6 Lectures: Bao gồm 4 bài tập
-- 3 Enrollments: 3 sinh viên đăng ký khóa Java
-- 2 Submissions: 1 đã chấm, 1 đang chờ
```

### Các Bản Ghi Dữ Liệu Mẫu Chính

#### Sinh Viên
```sql
INSERT INTO "User" VALUES
('20000000-0000-0000-0000-000000000101', 'minh.le@student.blearning.edu.vn',
 '$2a$10$...', 'Minh', 'Le Quang', 'ACTIVE'),
('20000000-0000-0000-0000-000000000102', 'huong.pham@student.blearning.edu.vn',
 '$2a$10$...', 'Huong', 'Pham Thi', 'ACTIVE');
```

#### Giảng Viên
```sql
INSERT INTO "User" VALUES
('20000000-0000-0000-0000-000000000002', 'kiet.nguyen@blearning.edu.vn',
 '$2a$10$...', 'Kiet', 'Nguyen Van', 'ACTIVE');
```

#### Bài Tập
```sql
INSERT INTO "Lecture" VALUES
('60000000-0000-0000-0000-000000000002',
 '50000000-0000-0000-0000-000000000001', -- module_id
 'Bai Tap: Hello World',
 'Viet chuong trinh Java dau tien',
 'ASSIGNMENT',
 2, -- order_num
 NULL, -- duration_seconds
 '{"max_points": 100, "due_date": "2025-12-31T23:59:00Z"}'::json);
```

#### Bài Nộp

**Bài Nộp Đã Chấm:**
```sql
INSERT INTO "AssignmentSubmission" VALUES
('f0000000-0000-0000-0000-000000000001',
 '60000000-0000-0000-0000-000000000002', -- lecture_id (Hello World)
 '20000000-0000-0000-0000-000000000101', -- user_id (Minh)
 'c0000000-0000-0000-0000-000000000001', -- enrollment_id
 1, -- submission_number
 'GRADED',
 CURRENT_TIMESTAMP - INTERVAL '12 days',
 85.00, -- score
 100.00, -- max_score
 'Code chay tot, logic dung. Can improve code documentation.', -- feedback
 CURRENT_TIMESTAMP - INTERVAL '11 days', -- graded_at
 '20000000-0000-0000-0000-000000000002', -- graded_by (Kiet)
 '["https://s3.amazonaws.com/blearning/submissions/minh-le-hello-world.zip"]'::json);
```

**Bài Nộp Đang Chờ:**
```sql
INSERT INTO "AssignmentSubmission" VALUES
('f0000000-0000-0000-0000-000000000002',
 '60000000-0000-0000-0000-000000000002',
 '20000000-0000-0000-0000-000000000102', -- user_id (Huong)
 'c0000000-0000-0000-0000-000000000002',
 1,
 'SUBMITTED', -- Chưa chấm
 CURRENT_TIMESTAMP - INTERVAL '2 days',
 NULL, -- score
 NULL, -- max_score
 NULL, -- feedback
 NULL, -- graded_at
 NULL, -- graded_by
 '["https://s3.amazonaws.com/blearning/submissions/huong-pham-hello.java"]'::json);
```

---

## Hướng Dẫn Kiểm Thử

### Thiết Lập

1. **Khởi động Database**
```bash
cd B-Learning-Demo
.\setup-database.bat
```

2. **Khởi động Backend**
```bash
cd backend
python -m uvicorn app.main:app --reload
```

3. **Khởi động Frontend**
```bash
cd frontend
npm run dev
```

4. **Mở Trình Duyệt**
```
http://localhost:3000/submissions
```

### Các Test Case

#### Test 1: Xem Danh Sách Bài Nộp

**Các bước:**
1. Truy cập http://localhost:3000/submissions
2. Kiểm tra thấy 2 bài nộp
3. Kiểm tra thống kê:
   - Tổng: 2
   - Đã Chấm: 1
   - Chờ Chấm: 1
   - Điểm Trung Bình: 85.00

**Kết quả mong đợi:**
```
┌─────────────────┬──────────────────────┬────────┬────────┐
│ Sinh Viên       │ Bài Tập              │ Status │ Điểm   │
├─────────────────┼──────────────────────┼────────┼────────┤
│ Minh Le Quang   │ Bai Tap: Hello World │ GRADED │ 85.00  │
│ Huong Pham Thi  │ Bai Tap: Hello World │ SUBMIT │ -      │
└─────────────────┴──────────────────────┴────────┴────────┘
```

#### Test 2: Xem Bài Nộp Đã Chấm

**Các bước:**
1. Click "Xem" ở bài nộp của Minh
2. Kiểm tra modal hiển thị:
   - Tên sinh viên
   - Tiêu đề bài tập
   - Điểm: 85/100
   - Nhận xét: "Code chay tot, logic dung..."
   - File URL

#### Test 3: Chấm Bài Nộp Đang Chờ

**Các bước:**
1. Click "Chấm" ở bài nộp của Hương
2. Nhập điểm: 90
3. Nhập nhận xét: "Làm xuất sắc!"
4. Click "Gửi Điểm"

**Kết quả mong đợi:**
- Modal đóng
- Danh sách làm mới
- Status của Hương đổi thành "GRADED"
- Điểm hiển thị 90.00
- Thống kê cập nhật:
  - Đã Chấm: 2
  - Chờ Chấm: 0
  - Điểm TB: 87.50

**Kiểm tra trong Database:**
```sql
SELECT
  submission_id,
  status,
  score,
  feedback,
  graded_at,
  graded_by
FROM "AssignmentSubmission"
WHERE submission_id = 'f0000000-0000-0000-0000-000000000002';
```

Kết quả:
```
status: GRADED
score: 90.00
feedback: Làm xuất sắc!
graded_at: 2025-12-01 12:34:56
graded_by: 20000000-0000-0000-0000-000000000002
```

#### Test 4: Lọc Theo Trạng Thái

**Các bước:**
1. Chọn bộ lọc "Trạng thái: GRADED"
2. Kiểm tra chỉ hiển thị bài nộp đã chấm
3. Chọn "Trạng thái: SUBMITTED"
4. Kiểm tra chỉ hiển thị bài nộp đang chờ

#### Test 5: Tìm Kiếm Theo Email Sinh Viên

**Các bước:**
1. Nhập "minh.le" vào ô tìm kiếm
2. Kiểm tra chỉ hiển thị bài nộp của Minh
3. Xóa tìm kiếm
4. Nhập "pham"
5. Kiểm tra chỉ hiển thị bài nộp của Hương

---

## Xử Lý Lỗi Thường Gặp

### Lỗi 1: `toFixed is not a function`

**Lỗi:**
```
TypeError: sub.final_score.toFixed is not a function
```

**Nguyên nhân:** PostgreSQL DECIMAL trả về dạng string, không phải number

**Giải pháp:**
```typescript
// ❌ Sai
{sub.final_score.toFixed(2)}

// ✅ Đúng
{Number(sub.final_score).toFixed(2)}
```

### Lỗi 2: CORS Error

**Lỗi:**
```
Access to XMLHttpRequest blocked by CORS policy
```

**Nguyên nhân:** Frontend (localhost:3000) gọi Backend (localhost:8000)

**Giải pháp:** Backend đã cấu hình CORS trong `main.py`:
```python
app.add_middleware(
    CORSMiddleware,
    allow_origins=["http://localhost:3000"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)
```

### Lỗi 3: Vi Phạm Constraint `chk_assignment_graded_has_info`

**Lỗi:**
```
new row violates check constraint "chk_assignment_graded_has_info"
```

**Nguyên nhân:** `graded_at` được set nhưng `graded_by` là NULL

**Giải pháp:**
```sql
-- ❌ Sai
UPDATE "AssignmentSubmission"
SET graded_at = CURRENT_TIMESTAMP
WHERE submission_id = ?;

-- ✅ Đúng
UPDATE "AssignmentSubmission"
SET
  graded_at = CURRENT_TIMESTAMP,
  graded_by = '20000000-0000-0000-0000-000000000002'
WHERE submission_id = ?;
```

### Lỗi 4: Validation Error - Thiếu Trường

**Lỗi:**
```
Field 'assignment_submission_id' required
Field 'days_late' required
Field 'penalty_applied' required
```

**Nguyên nhân:** SQL query trả về `submission_id` thay vì `assignment_submission_id`

**Giải pháp:**
```sql
-- ❌ Sai
SELECT asub.* FROM "AssignmentSubmission" asub

-- ✅ Đúng
SELECT
  asub.submission_id as assignment_submission_id,
  CASE WHEN asub.is_late THEN 1 ELSE 0 END as days_late,
  0 as penalty_applied
FROM "AssignmentSubmission" asub
```

### Lỗi 5: Lỗi Encoding PostgreSQL

**Lỗi:**
```
character with byte sequence 0x81 in encoding "WIN1252" has no equivalent in "UTF8"
```

**Nguyên nhân:** Ký tự tiếng Việt trong file SQL với encoding sai

**Giải pháp:**
```sql
-- Thêm vào đầu file SQL
SET CLIENT_ENCODING = 'UTF8';

-- Thay tất cả dấu tiếng Việt:
-- Kiệt → Kiet
-- Phạm → Pham
-- Nguyễn → Nguyen
```

---

## Tài Liệu API

### Base URL
```
http://localhost:8000
```

### Tóm Tắt Endpoints

| Method | Endpoint                               | Mô tả                      |
|--------|----------------------------------------|----------------------------|
| GET    | `/api/submissions`                     | Danh sách bài nộp          |
| GET    | `/api/submissions/{id}`                | Chi tiết bài nộp           |
| PUT    | `/api/submissions/{id}/grade`          | Chấm bài nộp               |
| GET    | `/api/submissions/stats/overview`      | Lấy thống kê               |

### Response Models

#### SubmissionListItem
```typescript
{
  assignment_submission_id: string;
  submission_number: number;
  submitted_at: string;
  content?: string | null;
  is_late: boolean;
  days_late: number;
  status: string;
  auto_score?: number | null;
  manual_score?: number | null;
  final_score?: number | null;
  penalty_applied: number;
  feedback?: string | null;
  graded_at?: string | null;
  student_id: string;
  student_email: string;
  student_name: string;
  assignment_id: string;
  assignment_title: string;
  assignment_type: string;
  max_points: number;
  due_date: string;
  course_id: string;
  course_code: string;
  course_title: string;
  graded_by_name?: string | null;
}
```

---

## Cân Nhắc Hiệu Năng

### Indexes Database

Các index quan trọng cho hiệu năng:

```sql
-- Indexes AssignmentSubmission
CREATE INDEX idx_assignment_submission_user ON "AssignmentSubmission"(user_id);
CREATE INDEX idx_assignment_submission_lecture ON "AssignmentSubmission"(lecture_id);
CREATE INDEX idx_assignment_submission_status ON "AssignmentSubmission"(status);
CREATE INDEX idx_assignment_submission_submitted_at ON "AssignmentSubmission"(submitted_at DESC);

-- Composite index cho các filter phổ biến
CREATE INDEX idx_assignment_submission_status_late
  ON "AssignmentSubmission"(status, is_late);
```

### Tối Ưu Query

Query danh sách chính sử dụng INNER JOINs hiệu quả:

```sql
-- Execution plan hiển thị:
-- 1. Index scan trên AssignmentSubmission (filter status)
-- 2. Nested loop joins trên foreign keys
-- 3. Tổng cost: ~50-100ms cho 1000 bài nộp
```

### Phân Trang

Luôn sử dụng LIMIT/OFFSET cho dataset lớn:

```sql
LIMIT 50 OFFSET 0  -- Trang đầu
LIMIT 50 OFFSET 50 -- Trang thứ hai
```

---

## Phát Triển Tương Lai

### Giai Đoạn 1: Xác Thực
- Thêm JWT authentication
- Lấy `graded_by` từ auth token thay vì hardcode
- Kiểm soát truy cập theo vai trò

### Giai Đoạn 2: Tự Động Chấm
- Sandbox thực thi code
- Chạy test case
- Tính điểm tự động

### Giai Đoạn 3: Upload File
- Upload file trực tiếp lên S3
- Quét virus
- Giới hạn kích thước file

### Giai Đoạn 4: Cập Nhật Thời Gian Thực
- WebSocket cho thông báo chấm điểm live
- Cộng tác thời gian thực

---

## Kết Luận

Demo bài nộp này minh họa:

✅ **SQL JOINs Phức Tạp** - Kết hợp 5+ bảng
✅ **Thiết Kế RESTful API** - Endpoints sạch, dễ đoán
✅ **Type Safety** - Pydantic + TypeScript
✅ **Tính Toàn Vẹn Dữ Liệu** - Database constraints
✅ **Kịch Bản Thực Tế** - Quy trình chấm điểm hoàn chỉnh

Hệ thống minh họa các mẫu thiết kế production-ready cho tính năng nộp bài tập của hệ thống quản lý học tập.

---

**Câu hỏi?**
Liên hệ: Nguyễn Văn Kiệt - CNTT1-K63
Email: kiet.nguyen@blearning.edu.vn
