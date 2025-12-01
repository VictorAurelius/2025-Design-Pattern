# FAQ & GIẢI THÍCH CHI TIẾT
# DATABASE B-LEARNING CORE

**Mục đích:** Tài liệu này cung cấp câu hỏi và giải đáp chi tiết về thiết kế database B-Learning Core, giúp người đọc hiểu sâu về các quyết định thiết kế, relationships, constraints, và optimization strategies.

**Phiên bản:** 1.0 (Core - 16 bảng)

**Tác giả:** Nguyễn Văn Kiệt - CNTT1-K63

**Ngày tạo:** 01/12/2025

---

## MỤC LỤC

1. [Câu hỏi chung về thiết kế](#1-câu-hỏi-chung-về-thiết-kế)
2. [Domain 1: User Management](#2-domain-1-user-management)
3. [Domain 2: Course Content](#3-domain-2-course-content)
4. [Domain 3: Assessment](#4-domain-3-assessment)
5. [Domain 4: Enrollment & Progress](#5-domain-4-enrollment--progress)
6. [Domain 5: Class & Certificate](#6-domain-5-class--certificate)
7. [Relationships & Constraints](#7-relationships--constraints)
8. [Performance & Optimization](#8-performance--optimization)
9. [JSON Fields & Design Patterns](#9-json-fields--design-patterns)
10. [Normalization & Data Integrity](#10-normalization--data-integrity)

---

## 1. CÂU HỎI CHUNG VỀ THIẾT KẾ

### Q1.1: Tại sao chọn PostgreSQL thay vì MySQL hay MongoDB?

**Trả lời:**

PostgreSQL được chọn vì các lý do sau:

**1. ACID Compliance mạnh mẽ:**
- Hệ thống LMS cần đảm bảo data consistency cao (điểm số, submissions, enrollments)
- PostgreSQL có transaction support tốt hơn MySQL (especially với MVCC - Multi-Version Concurrency Control)
- Ví dụ: Khi instructor grading 50 submissions đồng thời, PostgreSQL đảm bảo không có race condition

**2. Advanced Data Types:**
- **JSON/JSONB:** Lưu trữ flexible data như quiz questions, attempt answers, class schedules
  ```sql
  -- Quiz questions stored as JSONB
  SELECT questions->0->>'text' FROM "Quiz" WHERE quiz_id = '...';
  ```
- **Array types:** Prerequisites, file URLs
  ```sql
  -- Array of prerequisite module IDs
  prerequisite_module_ids UUID[]
  ```
- **UUID native support:** Gen random UUID mà không cần external library
  ```sql
  user_id UUID PRIMARY KEY DEFAULT gen_random_uuid()
  ```

**3. Full-text Search:**
- Tìm kiếm courses, lectures, discussions hiệu quả
- PostgreSQL có **pg_trgm extension** mạnh cho fuzzy search
- Ví dụ:
  ```sql
  SELECT * FROM "Course"
  WHERE title ILIKE '%java%'
  OR description ILIKE '%java%';
  ```

**4. Performance:**
- Better query optimizer (cost-based optimizer)
- Parallel query execution (PostgreSQL 9.6+)
- Materialized views cho reporting
- Better handling của complex JOINs

**5. Extensibility:**
- Custom functions, triggers
- Support nhiều extensions (uuid-ossp, pg_trgm, btree_gin)
- PostGIS cho geo-location (nếu cần location-based learning)

**Tại sao KHÔNG chọn MongoDB:**
- ❌ LMS có schema rõ ràng, không cần schema-less
- ❌ Cần ACID transactions cho grading, enrollment (MongoDB transactions phức tạp hơn)
- ❌ Relationships phức tạp, SQL phù hợp hơn NoSQL
- ❌ Strong typing và constraints quan trọng để data integrity

**Trade-offs:**
- PostgreSQL phức tạp hơn MySQL (setup, tuning)
- Nhưng hiệu quả cao hơn cho LMS use case

---

### Q1.2: Database có bao nhiêu bảng? Tại sao lại cần nhiều bảng như vậy?

**Trả lời:**

Database có **16 bảng**, chia thành **5 domains** chức năng:

**Domain 1: User Management (3 bảng)**
- User, Role, UserRole

**Domain 2: Course Content (4 bảng)**
- Course, Module, Lecture, Resource

**Domain 3: Assessment (5 bảng)**
- Quiz, Question, Option, Attempt, AssignmentSubmission

**Domain 4: Enrollment & Progress (2 bảng)**
- Enrollment, Progress

**Domain 5: Class & Certificate (2 bảng)**
- Class, Certificate

**Lý do cần 16 bảng:**

**1. Normalization (3NF - Third Normal Form):**
- Tránh data redundancy
- Mỗi fact chỉ lưu 1 lần
- Ví dụ: Thông tin course (title, description) lưu trong Course table, không duplicate trong Enrollment

**2. Separation of Concerns:**
- Mỗi bảng có 1 responsibility rõ ràng
- User table: authentication
- UserRole table: authorization
- Progress table: learning progress

**3. Scalability:**
- Dễ mở rộng từng phần mà không ảnh hưởng toàn bộ
- Ví dụ: Thêm bảng Discussion sau này không cần modify User table

**4. Performance:**
- Index riêng cho từng entity
- Query chỉ load data cần thiết (không load toàn bộ)
- Ví dụ: Login chỉ query User table (fast), không cần load Profile, Enrollments

**5. Maintainability:**
- Dễ maintain và debug
- Clear schema, dễ onboard developer mới
- Unit testing từng domain độc lập

**So sánh với alternative (1 big table):**
```sql
-- BAD: One huge User table with all info
CREATE TABLE "User" (
  user_id UUID,
  email VARCHAR(255),
  -- ... user info
  role_name VARCHAR(50),
  -- ... role info
  enrolled_courses TEXT[],
  -- ... enrollment info
  quiz_scores JSON
  -- ... assessment info
);
```
❌ Massive table, slow queries
❌ Data redundancy (role info repeated for every user)
❌ Difficult to maintain
❌ Impossible to enforce referential integrity

---

### Q1.3: Database có follow Normalization không? Đến level nào?

**Trả lời:**

Database follow **Third Normal Form (3NF)** với một số exceptions có chủ đích.

**1NF (First Normal Form):**

✅ **Tất cả columns đều atomic** (không có multi-valued attributes)
```sql
-- GOOD: Atomic values
first_name VARCHAR(100),
last_name VARCHAR(100)

-- BAD (would violate 1NF):
full_name VARCHAR(200)  -- "Nguyễn Văn A, Trần Thị B" (multiple values)
```

✅ **Mỗi table có primary key**
```sql
user_id UUID PRIMARY KEY
```

✅ **No repeating groups**
```sql
-- GOOD: Separate Module table
Module: module_id, course_id, title, order_num

-- BAD (would violate 1NF):
Course: course_id, title, module1_title, module2_title, module3_title
```

**2NF (Second Normal Form):**

✅ **Satisfy 1NF**

✅ **No partial dependencies** (tất cả non-key attributes phụ thuộc hoàn toàn vào PK)

Ví dụ trong bảng **Enrollment**:
```sql
CREATE TABLE "Enrollment" (
  enrollment_id UUID PRIMARY KEY,  -- PK
  user_id UUID,                    -- FK to User
  course_id UUID,                  -- FK to Course
  enrolled_at TIMESTAMP,           -- Depends on enrollment_id (full PK)
  completion_percentage DECIMAL    -- Depends on enrollment_id (full PK)
);
```
- `enrolled_at` phụ thuộc vào `enrollment_id` (PK), KHÔNG phụ thuộc riêng `user_id` hay `course_id`
- Nếu phụ thuộc riêng `user_id` → tách sang User table
- Nếu phụ thuộc riêng `course_id` → tách sang Course table

**3NF (Third Normal Form):**

✅ **Satisfy 2NF**

✅ **No transitive dependencies** (non-key attributes không phụ thuộc vào non-key attributes khác)

Ví dụ:
```sql
-- GOOD: Course info in Course table
CREATE TABLE "Course" (
  course_id UUID PRIMARY KEY,
  title VARCHAR(200),
  created_by UUID  -- FK to User
);

CREATE TABLE "Enrollment" (
  enrollment_id UUID PRIMARY KEY,
  course_id UUID  -- FK to Course
  -- course_title KHÔNG lưu ở đây (would be transitive dependency)
);
```

Nếu lưu `course_title` trong Enrollment:
- `course_title` phụ thuộc vào `course_id` (non-key)
- `course_id` phụ thuộc vào `enrollment_id` (PK)
- → Transitive dependency: `enrollment_id` → `course_id` → `course_title`
- → Violates 3NF

**Exceptions (có chủ đích - Denormalization for Performance):**

**1. JSON fields:**
```sql
-- Quiz.questions JSONB
-- Instead of separate QuizQuestion table
```
- **Lý do:** Simplify schema, reduce JOINs
- **Trade-off:** Harder to query individual questions

**2. Array fields:**
```sql
-- Module.prerequisite_module_ids UUID[]
-- Instead of separate PrerequisiteModule table
```
- **Lý do:** Performance, simple queries
- **Trade-off:** Limited array operations

**3. Calculated fields (rare):**
```sql
-- Enrollment.completion_percentage
-- Could be calculated from Progress table
```
- **Lý do:** Avoid expensive JOINs for common queries
- **Trade-off:** Need to update when Progress changes (handled by application logic)

**Tổng kết:**
- **Core structure:** 3NF
- **Flexible data:** JSON/Array for simplicity
- **Performance:** Strategic denormalization where needed

---

### Q1.4: Tại sao dùng UUID thay vì INT auto-increment?

**Trả lời:**

Sử dụng **UUID (Universally Unique Identifier)** thay vì INT auto-increment vì các lý do sau:

**Ưu điểm của UUID:**

**1. Distributed-System Friendly:**
- UUID generated client-side hoặc multiple servers mà không conflict
- INT auto-increment cần central database để generate (bottleneck)
- Ví dụ: Có 3 application servers, tất cả có thể insert User đồng thời mà không cần coordinate

**2. Security:**
- INT sequential: dễ guess (user_id = 1, 2, 3, ...)
  ```
  https://api.example.com/users/1  → Guess user 2, 3, 4...
  ```
- UUID random: không guess được
  ```
  https://api.example.com/users/550e8400-e29b-41d4-a716-446655440000
  ```
- Prevents enumeration attacks

**3. Merging Data:**
- Khi merge data từ nhiều sources (staging → production, backup restore)
- UUID không conflict
- INT có thể conflict (user_id = 1 ở cả 2 databases)

**4. Decoupling:**
- Frontend có thể generate UUID trước khi call API
- Optimistic UI updates (không cần đợi server response để có ID)

**5. Sharding:**
- Khi scale horizontal (nhiều database servers)
- UUID works across shards
- INT auto-increment phức tạp (cần global sequence generator)

**Nhược điểm của UUID (và cách giải quyết):**

**1. Storage:**
- UUID: 16 bytes (128 bits)
- INT: 4 bytes (32 bits)
- BIGINT: 8 bytes (64 bits)
- → UUID chiếm 2-4x storage

**Giải pháp:** PostgreSQL stores UUID efficiently, modern hardware có đủ disk space

**2. Index Size:**
- UUID indexes lớn hơn INT indexes
- Impacts performance (index scan slower)

**Giải pháp:**
- Use BIGINT cho bảng có millions of rows + high query frequency
- Use UUID cho bảng có moderate size (User, Course, Enrollment)

**3. Readability:**
- INT: 1, 2, 3 (easy to remember, debug)
- UUID: 550e8400-e29b-41d4-a716-446655440000 (hard to remember)

**Giải pháp:** Use email, code (Course.code) cho human-readable identifiers

**4. Ordering:**
- INT auto-increment: naturally ordered by insertion time
- UUID random: no natural order

**Giải pháp:** Add `created_at TIMESTAMP` column for ordering

**So sánh Performance:**

```sql
-- INT auto-increment
CREATE TABLE "User" (
  user_id SERIAL PRIMARY KEY  -- SERIAL = INT auto-increment
);
-- INSERT: Fast (sequential)
-- SELECT: Fast (small index)
-- JOIN: Fast

-- UUID
CREATE TABLE "User" (
  user_id UUID PRIMARY KEY DEFAULT gen_random_uuid()
);
-- INSERT: Slightly slower (random)
-- SELECT: Slightly slower (larger index)
-- JOIN: Slightly slower
```

**Benchmark (PostgreSQL 14, 1M rows):**
- INT: 100% (baseline)
- UUID: ~95% (5% slower, negligible for LMS use case)

**Kết luận:**
- **Use UUID:** For main entities (User, Course, Enrollment) → Better for distributed systems, security
- **Use INT/BIGINT:** For high-volume logs, analytics (nếu có millions of rows)
- **Use SERIAL:** For simple internal tables (không expose ra API)

Trong B-Learning Core:
✅ All primary keys = UUID (consistency, future-proof for scaling)

---

### Q1.5: Tại sao simplify từ 31 bảng (v2.0) xuống 16 bảng (Core)?

**Trả lời:**

Simplification từ 31 bảng xuống 16 bảng (48% reduction) nhằm:

**1. Reduce Complexity:**
- 31 tables quá phức tạp cho startup/SME
- Khó onboard developer mới
- Maintenance overhead cao

**2. Improve Performance:**
- Ít tables → ít JOINs
- Faster queries
- Smaller database size

**3. Use PostgreSQL Strengths:**
- JSON/JSONB cho flexible data
- Arrays cho simple relationships
- Native support, không cần separate tables

**Chi tiết Simplification:**

| Component | v2.0 (31 tables) | Core (16 tables) | Change | Benefit |
|-----------|------------------|------------------|--------|---------|
| **Quiz Questions** | Separate `QuizQuestion` table | `Quiz.questions` JSONB | -1 table | Atomic quiz loading, no JOINs |
| **Quiz Submissions** | Separate `QuizSubmission` table | `Attempt.answers` JSONB | -1 table | All attempt data in 1 row |
| **Assignment** | Separate `Assignment` table | `Lecture.type='ASSIGNMENT'` + `assignment_config` JSON | -1 table | Unified lecture model |
| **Enrollment** | 2 tables (CourseEnrollment, ClassEnrollment) | 1 `Enrollment` with nullable `class_id` | -1 table | Simpler, supports both modes |
| **Schedule/Attendance** | 2 tables (Schedule, Attendance) | `Class.schedules` JSON + `attendance` JSON | -2 tables | Flexible scheduling |
| **Certificate** | 3 tables (Certificate, Template, Verification) | 1 `Certificate` table | -2 tables | Sufficient for MVP |
| **Notification** | 3 tables (Notification, NotificationTemplate, UserNotification) | Removed (use external email service) | -3 tables | Not core database concern |
| **ActivityLog** | 1 table | Removed (use application logs) | -1 table | Better handled by logging framework |
| **File Management** | 1 table (FileStorage) | Removed (use S3/GCS) | -1 table | Cloud storage better |
| **GradeBook** | 1 table (separate from Enrollment) | Removed (calculated from Attempt + AssignmentSubmission) | -1 table | Avoid redundancy |
| **System Settings** | 1 table (SystemConfig) | Removed (use .env config) | -1 table | Simpler config management |

**Example 1: Quiz Questions**

**v2.0 (31 tables):**
```sql
-- 2 tables
CREATE TABLE "Quiz" (
  quiz_id UUID PRIMARY KEY,
  title VARCHAR(200),
  ...
);

CREATE TABLE "QuizQuestion" (
  question_id UUID PRIMARY KEY,
  quiz_id UUID REFERENCES "Quiz",
  question_text TEXT,
  question_type VARCHAR(20),
  points DECIMAL,
  order_num INT
);

-- Query: Need JOIN
SELECT q.title, qq.question_text
FROM "Quiz" q
JOIN "QuizQuestion" qq ON q.quiz_id = qq.quiz_id
WHERE q.quiz_id = '...';
```

**Core (16 tables):**
```sql
-- 1 table
CREATE TABLE "Quiz" (
  quiz_id UUID PRIMARY KEY,
  title VARCHAR(200),
  questions JSONB,  -- Array of question objects
  ...
);

-- Query: No JOIN
SELECT title, questions
FROM "Quiz"
WHERE quiz_id = '...';
```

**Benefits:**
- ✅ 1 query instead of JOIN
- ✅ Atomic load (all questions at once)
- ✅ Simpler schema

**Trade-offs:**
- ❌ Harder to query individual questions
- ❌ No foreign key constraints on questions
- But: Acceptable for LMS use case (always load all questions together)

**Example 2: Assignment**

**v2.0:**
```sql
-- Separate tables for Lecture and Assignment
CREATE TABLE "Lecture" (
  lecture_id UUID PRIMARY KEY,
  title VARCHAR(200),
  type VARCHAR(20)  -- VIDEO, READING
);

CREATE TABLE "Assignment" (
  assignment_id UUID PRIMARY KEY,
  lecture_id UUID REFERENCES "Lecture",
  title VARCHAR(200),
  instructions TEXT,
  due_date TIMESTAMP,
  max_score DECIMAL
);
```

**Core:**
```sql
-- Unified model
CREATE TABLE "Lecture" (
  lecture_id UUID PRIMARY KEY,
  title VARCHAR(200),
  type VARCHAR(20),  -- VIDEO, READING, ASSIGNMENT, QUIZ
  assignment_config JSONB  -- For ASSIGNMENT type
);

-- assignment_config JSON:
{
  "instructions": "Complete the Java project...",
  "due_date": "2025-12-15T23:59:59Z",
  "max_score": 100,
  "submission_types": ["FILE", "TEXT"],
  "allowed_file_types": [".pdf", ".zip"]
}
```

**Benefits:**
- ✅ Simpler: 1 table instead of 2
- ✅ Easier to manage: All lecture types in same table
- ✅ Flexible: JSON can have any assignment-specific fields

**Trade-offs:**
- ❌ No schema enforcement on assignment_config
- But: Validation at application level

**Kết luận:**
- Core (16 tables) suitable for **MVP, startup, SME**
- v2.0 (31 tables) suitable for **enterprise, high scale**
- Can migrate Core → v2.0 later if needed

---

### Q1.6: JSON fields có ưu nhược điểm gì so với separate tables?

**Trả lời:**

JSON fields (JSONB trong PostgreSQL) có pros và cons rõ ràng:

**Ưu điểm của JSON:**

**1. Schema Flexibility:**
```sql
-- Quiz questions có thể có different structures
Quiz 1: Multiple choice (4 options)
Quiz 2: True/False (2 options)
Quiz 3: Short answer (no options)

-- JSON handles all cases:
{
  "type": "MULTIPLE_CHOICE",
  "text": "What is Java?",
  "options": ["A", "B", "C", "D"]
}

{
  "type": "TRUE_FALSE",
  "text": "Java is OOP?",
  "options": ["True", "False"]
}

{
  "type": "SHORT_ANSWER",
  "text": "Explain Java OOP",
  "max_length": 500
}
```

**2. Atomic Operations:**
```sql
-- Load all quiz questions in 1 query
SELECT questions FROM "Quiz" WHERE quiz_id = '...';

-- vs separate table (need JOIN + loop)
SELECT * FROM "QuizQuestion" WHERE quiz_id = '...' ORDER BY order_num;
```

**3. No JOINs:**
- Faster for read-heavy operations
- Simple queries

**4. Version Control:**
```sql
-- Easy to add new fields without ALTER TABLE
Quiz.questions:
[
  {
    "text": "...",
    "points": 10,
    "explanation": "..."  -- New field added
  }
]

-- vs separate table:
ALTER TABLE "QuizQuestion" ADD COLUMN explanation TEXT;  -- Risky
```

**5. Transactional:**
```sql
-- All questions updated together
UPDATE "Quiz" SET questions = '[...]' WHERE quiz_id = '...';

-- vs separate table (multiple INSERTs, need transaction)
BEGIN;
DELETE FROM "QuizQuestion" WHERE quiz_id = '...';
INSERT INTO "QuizQuestion" VALUES (...), (...), (...);
COMMIT;
```

**Nhược điểm của JSON:**

**1. No Schema Enforcement:**
```sql
-- Can insert invalid data
INSERT INTO "Quiz" (questions) VALUES ('[
  {
    "txt": "Wrong field name",  -- Should be "text"
    "ponts": 10                  -- Should be "points"
  }
]');
```
- Solution: Validation at application level (Pydantic, Joi)

**2. Harder to Query:**
```sql
-- Find all quizzes with question containing "Java"
SELECT * FROM "Quiz"
WHERE questions @> '[{"text": "Java"}]';  -- Complex syntax

-- vs separate table:
SELECT DISTINCT quiz_id FROM "QuizQuestion"
WHERE question_text LIKE '%Java%';  -- Simple
```

**3. No Foreign Key Constraints:**
```sql
-- JSON can reference non-existent IDs
{
  "question_id": "fake-uuid-does-not-exist"
}

-- vs separate table:
question_id UUID REFERENCES "Question"(question_id)  -- Enforced
```

**4. Index Limitations:**
```sql
-- Can create GIN index, but slower than B-tree
CREATE INDEX idx_quiz_questions ON "Quiz" USING GIN (questions);

-- vs separate table:
CREATE INDEX idx_question_text ON "QuizQuestion"(question_text);  -- Faster
```

**5. No Partial Updates:**
```sql
-- Must update entire JSON
UPDATE "Quiz" SET questions = '[
  {...},  -- Question 1
  {...},  -- Question 2 (unchanged)
  {...}   -- Question 3 (unchanged)
]' WHERE quiz_id = '...';

-- vs separate table:
UPDATE "QuizQuestion" SET points = 15 WHERE question_id = '...';  -- Only 1 row
```

**When to use JSON:**

✅ **Use JSON when:**
- Schema is flexible (different structures)
- Data is always loaded/saved together (atomic)
- No complex queries on nested data
- Version changes frequently
- Examples:
  - Quiz.questions (always load all questions)
  - User.preferences (notification settings, UI preferences)
  - Class.schedules (array of schedule objects)

❌ **DON'T use JSON when:**
- Need complex queries on nested data
- Need foreign key constraints
- Need to update individual items frequently
- Schema is stable and well-defined
- Examples:
  - User profiles → Separate UserProfile table
  - Course modules → Separate Module table

**PostgreSQL JSONB vs JSON:**

| Feature | JSONB (Binary) | JSON (Text) |
|---------|----------------|-------------|
| **Storage** | Binary (compressed) | Text (raw) |
| **Parse** | Pre-parsed (fast) | Parse on read (slow) |
| **Index** | GIN index (fast) | No index |
| **Query** | Fast (jsonb_path) | Slow |
| **Size** | Slightly larger | Smaller |
| **Recommendation** | ✅ Use JSONB | ❌ Rarely use JSON |

**Example in B-Learning Core:**

```sql
-- GOOD: JSON for flexible data
CREATE TABLE "Quiz" (
  questions JSONB  -- Different question types
);

CREATE TABLE "User" (
  preferences JSON  -- Flexible settings
);

-- GOOD: Separate table for structured data
CREATE TABLE "Module" (
  module_id UUID PRIMARY KEY,
  course_id UUID REFERENCES "Course",
  title VARCHAR(200),
  order_num INT
);
```

**Kết luận:**
- JSON = Flexibility, simplicity, atomic operations
- Separate tables = Structure, constraints, complex queries
- Use both strategically based on use case

---

### Q1.7: Database có hỗ trợ soft delete không?

**Trả lời:**

Có, database hỗ trợ **soft delete** thông qua `account_status` và status fields.

**Soft Delete là gì:**

Soft delete = Mark record as deleted, không xóa physical data

```sql
-- Hard delete (BAD for most cases)
DELETE FROM "User" WHERE user_id = '...';  -- Data lost forever

-- Soft delete (GOOD)
UPDATE "User" SET account_status = 'DELETED' WHERE user_id = '...';
```

**Ưu điểm của Soft Delete:**

**1. Data Recovery:**
- Có thể restore deleted records
- Ví dụ: User accidentally delete account → Admin can restore

**2. Audit Trail:**
- Biết ai đã delete gì, khi nào
- Ví dụ: "User X deleted at 2025-12-01 15:30:00"

**3. Referential Integrity:**
- Foreign key references vẫn valid
- Ví dụ: User deleted, nhưng Enrollment history vẫn còn

**4. Analytics:**
- Analyze deleted data (churn analysis)
- Ví dụ: "Why do users delete accounts? What features they used?"

**5. Compliance:**
- GDPR: User có right to be forgotten (phải xóa PII)
- Nhưng có thể keep anonymized data cho analytics

**Implementation trong B-Learning Core:**

**1. User table:**
```sql
CREATE TABLE "User" (
  user_id UUID PRIMARY KEY,
  email VARCHAR(255) NOT NULL UNIQUE,
  account_status VARCHAR(30) DEFAULT 'ACTIVE',

  CONSTRAINT chk_user_account_status CHECK (account_status IN (
    'PENDING_VERIFICATION',
    'ACTIVE',
    'SUSPENDED',
    'DELETED'  -- Soft delete
  ))
);

-- Soft delete user
UPDATE "User"
SET account_status = 'DELETED',
    updated_at = CURRENT_TIMESTAMP
WHERE user_id = '...';

-- Restore user
UPDATE "User"
SET account_status = 'ACTIVE',
    updated_at = CURRENT_TIMESTAMP
WHERE user_id = '...';

-- Query active users only
SELECT * FROM "User" WHERE account_status = 'ACTIVE';
```

**2. Course table:**
```sql
CREATE TABLE "Course" (
  course_id UUID PRIMARY KEY,
  status VARCHAR(20) DEFAULT 'DRAFT',

  CONSTRAINT chk_course_status CHECK (status IN (
    'DRAFT',
    'PUBLISHED',
    'ARCHIVED'  -- Soft delete
  ))
);

-- Archive (soft delete) course
UPDATE "Course"
SET status = 'ARCHIVED',
    updated_at = CURRENT_TIMESTAMP
WHERE course_id = '...';

-- Query published courses only
SELECT * FROM "Course" WHERE status = 'PUBLISHED';
```

**3. Enrollment table:**
```sql
CREATE TABLE "Enrollment" (
  enrollment_id UUID PRIMARY KEY,
  status VARCHAR(20) DEFAULT 'ACTIVE',

  CONSTRAINT chk_enrollment_status CHECK (status IN (
    'ACTIVE',
    'COMPLETED',
    'DROPPED',      -- Soft delete
    'WITHDRAWN'     -- Soft delete
  ))
);

-- Drop enrollment (soft delete)
UPDATE "Enrollment"
SET status = 'DROPPED',
    updated_at = CURRENT_TIMESTAMP
WHERE enrollment_id = '...';
```

**Best Practices:**

**1. Always use soft delete for:**
- User accounts
- Course data
- Enrollment history
- Submissions (for audit trail)

**2. Can use hard delete for:**
- Temporary data (sessions, cache)
- Duplicate data (data migration errors)
- Test data

**3. Add deleted_at timestamp:**
```sql
ALTER TABLE "User" ADD COLUMN deleted_at TIMESTAMP;

-- Soft delete with timestamp
UPDATE "User"
SET account_status = 'DELETED',
    deleted_at = CURRENT_TIMESTAMP
WHERE user_id = '...';

-- Query: Find all users deleted in last 30 days
SELECT * FROM "User"
WHERE account_status = 'DELETED'
  AND deleted_at > CURRENT_TIMESTAMP - INTERVAL '30 days';
```

**4. Automatic filtering:**
```sql
-- Create VIEW to filter active records
CREATE VIEW "ActiveUser" AS
SELECT * FROM "User" WHERE account_status = 'ACTIVE';

-- Use VIEW in queries
SELECT * FROM "ActiveUser" WHERE email LIKE '%@gmail.com';
```

**5. GDPR Compliance:**
```sql
-- Full delete (GDPR right to be forgotten)
-- 1. Anonymize PII
UPDATE "User"
SET email = 'deleted-' || user_id || '@example.com',
    first_name = 'DELETED',
    last_name = 'USER',
    phone = NULL,
    avatar_url = NULL,
    account_status = 'DELETED',
    deleted_at = CURRENT_TIMESTAMP
WHERE user_id = '...';

-- 2. Keep enrollment/progress data for analytics (no PII)
-- Enrollments, Progress, Attempts remain (reference deleted user)
```

**Cascade Behavior:**

```sql
-- Foreign key with ON DELETE behavior
CREATE TABLE "UserRole" (
  user_id UUID REFERENCES "User"(user_id) ON DELETE CASCADE,
  role_id UUID REFERENCES "Role"(role_id) ON DELETE CASCADE
);
```

**Options:**
- `ON DELETE CASCADE`: Delete child records when parent deleted
- `ON DELETE SET NULL`: Set FK to NULL when parent deleted
- `ON DELETE RESTRICT`: Prevent delete if child records exist
- `ON DELETE NO ACTION`: Same as RESTRICT (default)

**In B-Learning Core:**
- User soft delete → Keep all related records (Enrollment, Submissions)
- Course cascade delete → Delete all Modules, Lectures (clean up)

**Kết luận:**
- ✅ Use soft delete for user-facing data (recovery, audit)
- ✅ Use cascade delete for tightly-coupled data (cleanup)
- ✅ Use hard delete sparingly (only when safe)

---

## 2. DOMAIN 1: USER MANAGEMENT

### Q2.1: Tại sao lại có bảng Role và UserRole riêng biệt? Tại sao không lưu role trực tiếp trong User?

**Trả lời:**

Thiết kế **Role-Based Access Control (RBAC)** với 3 bảng: User, Role, UserRole.

**Lý do tách Role và UserRole:**

**1. Many-to-Many Relationship:**
- Một user có thể có nhiều roles
- Ví dụ thực tế:
  ```
  User: John Doe
  Roles: STUDENT (học course A, B)
         INSTRUCTOR (dạy course C)
         TA (assistant cho course D)
  ```
- Không thể lưu multiple roles trong 1 field của User table

**2. Role Management:**
```sql
-- Roles được manage centrally
CREATE TABLE "Role" (
  role_id UUID PRIMARY KEY,
  name VARCHAR(50) UNIQUE,  -- STUDENT, INSTRUCTOR, TA, ADMIN
  description TEXT,
  permissions JSON  -- ["create_course", "grade_submission", ...]
);

-- Easy to add new role
INSERT INTO "Role" (name, description, permissions)
VALUES ('MODERATOR', 'Forum moderator', '["moderate_discussion"]');

-- Easy to modify permissions for all users with role
UPDATE "Role"
SET permissions = '["create_course", "grade_submission", "export_grades"]'
WHERE name = 'INSTRUCTOR';
```

**3. Audit Trail:**
```sql
CREATE TABLE "UserRole" (
  user_role_id UUID PRIMARY KEY,
  user_id UUID REFERENCES "User",
  role_id UUID REFERENCES "Role",
  granted_at TIMESTAMP,      -- When role was assigned
  granted_by UUID,           -- Who assigned the role (admin)
  expires_at TIMESTAMP       -- Role expiration (e.g., TA for 1 semester)
);

-- Audit: When did John become INSTRUCTOR?
SELECT granted_at, granted_by
FROM "UserRole" ur
JOIN "Role" r ON ur.role_id = r.role_id
WHERE ur.user_id = '...' AND r.name = 'INSTRUCTOR';
```

**4. Temporary Roles:**
```sql
-- Assign TA role for 1 semester
INSERT INTO "UserRole" (user_id, role_id, expires_at)
VALUES (
  'john-uuid',
  'ta-role-uuid',
  '2025-06-30 23:59:59'  -- Expires end of semester
);

-- Query: Find expired roles
SELECT * FROM "UserRole"
WHERE expires_at < CURRENT_TIMESTAMP;
```

**5. Flexibility:**
```sql
-- User có thể có different roles trong different contexts
-- (Mặc dù Core v1.0 chưa implement course-specific roles,
--  nhưng schema support future extension)

-- Example (future):
ALTER TABLE "UserRole" ADD COLUMN context_type VARCHAR(50);
ALTER TABLE "UserRole" ADD COLUMN context_id UUID;

-- John = STUDENT in course A
context_type='COURSE', context_id='course-A-uuid', role='STUDENT'

-- John = TA in course B
context_type='COURSE', context_id='course-B-uuid', role='TA'
```

**Alternative Design (bị loại bỏ):**

**Option 1: Role as string in User table**
```sql
-- BAD: Single role
CREATE TABLE "User" (
  user_id UUID PRIMARY KEY,
  email VARCHAR(255),
  role VARCHAR(50)  -- 'STUDENT', 'INSTRUCTOR', ...
);
```
❌ Only 1 role per user
❌ No audit trail
❌ No expiration
❌ No role metadata (permissions, description)

**Option 2: Role as array in User table**
```sql
-- BAD: Multiple roles as array
CREATE TABLE "User" (
  user_id UUID PRIMARY KEY,
  email VARCHAR(255),
  roles VARCHAR(50)[]  -- ARRAY['STUDENT', 'INSTRUCTOR']
);
```
❌ No audit trail (when role assigned?)
❌ No expiration
❌ No role metadata
❌ Hard to query ("find all users with INSTRUCTOR role")

**Query Examples:**

```sql
-- 1. Get all roles for a user
SELECT r.name, r.description, ur.granted_at
FROM "UserRole" ur
JOIN "Role" r ON ur.role_id = r.role_id
WHERE ur.user_id = '...'
  AND (ur.expires_at IS NULL OR ur.expires_at > CURRENT_TIMESTAMP);

-- 2. Check if user has specific role
SELECT EXISTS (
  SELECT 1
  FROM "UserRole" ur
  JOIN "Role" r ON ur.role_id = r.role_id
  WHERE ur.user_id = '...'
    AND r.name = 'INSTRUCTOR'
    AND (ur.expires_at IS NULL OR ur.expires_at > CURRENT_TIMESTAMP)
) AS has_instructor_role;

-- 3. Get all users with ADMIN role
SELECT u.email, u.first_name, u.last_name
FROM "User" u
JOIN "UserRole" ur ON u.user_id = ur.user_id
JOIN "Role" r ON ur.role_id = r.role_id
WHERE r.name = 'ADMIN'
  AND u.account_status = 'ACTIVE';

-- 4. Find all instructors who teach course X
SELECT DISTINCT u.email, u.first_name, u.last_name
FROM "User" u
JOIN "UserRole" ur ON u.user_id = ur.user_id
JOIN "Role" r ON ur.role_id = r.role_id
JOIN "Course" c ON c.created_by = u.user_id
WHERE r.name = 'INSTRUCTOR'
  AND c.course_id = '...';
```

**Performance Considerations:**

```sql
-- Indexes for fast role checks
CREATE INDEX idx_userrole_user ON "UserRole"(user_id);
CREATE INDEX idx_userrole_role ON "UserRole"(role_id);
CREATE INDEX idx_userrole_expires ON "UserRole"(expires_at);
CREATE INDEX idx_role_name ON "Role"(name);

-- Composite index for common query
CREATE INDEX idx_userrole_user_role ON "UserRole"(user_id, role_id);
```

**Kết luận:**
- **3-table design (User, Role, UserRole)** là chuẩn cho RBAC
- Flexible, auditable, maintainable
- Supports complex role management (expiration, context-specific roles)

---

### Q2.2: Bảng User có những status nào? Ý nghĩa từng status?

**Trả lời:**

Bảng **User** có 4 account statuses, theo lifecycle của user account:

**1. PENDING_VERIFICATION**

**Ý nghĩa:** User mới đăng ký, chưa xác thực email

**Khi nào:**
- User complete registration form
- Email verification link sent
- Chưa click link verification

**Hành động được phép:**
- ❌ Login (blocked until verified)
- ❌ Enroll courses
- ✅ Resend verification email
- ✅ Update profile (limited)

**Flow:**
```sql
-- Step 1: User register
INSERT INTO "User" (email, password_hash, first_name, last_name, account_status)
VALUES ('new@example.com', '$2a$10$...', 'John', 'Doe', 'PENDING_VERIFICATION');

-- Step 2: Send email verification link
-- (Application logic)

-- Step 3: User clicks link → Update status
UPDATE "User"
SET account_status = 'ACTIVE',
    email_verified_at = CURRENT_TIMESTAMP
WHERE user_id = '...';
```

**2. ACTIVE**

**Ý nghĩa:** User đã xác thực, account hoạt động bình thường

**Khi nào:**
- Email verified
- No violations
- Account in good standing

**Hành động được phép:**
- ✅ Login
- ✅ Enroll courses
- ✅ Submit assignments
- ✅ Take quizzes
- ✅ View grades
- ✅ Get certificates

**Flow:**
```sql
-- Most common status
SELECT * FROM "User" WHERE account_status = 'ACTIVE';

-- Business logic: Only active users can enroll
INSERT INTO "Enrollment" (user_id, course_id)
SELECT '...', '...'
WHERE EXISTS (
  SELECT 1 FROM "User"
  WHERE user_id = '...'
    AND account_status = 'ACTIVE'
);
```

**3. SUSPENDED**

**Ý nghĩa:** Account bị tạm khóa do vi phạm policy

**Khi nào:**
- User violates terms of service
- Spam/abuse detected
- Admin manually suspends
- Payment issues (nếu có paid courses)

**Hành động được phép:**
- ❌ Login (blocked)
- ❌ New enrollments
- ❌ Submit assignments
- ✅ View suspension reason (via email/support)
- ✅ Appeal suspension

**Flow:**
```sql
-- Admin suspends user
UPDATE "User"
SET account_status = 'SUSPENDED',
    updated_at = CURRENT_TIMESTAMP
WHERE user_id = '...';

-- Log suspension reason (in application logs or separate AuditLog table)
-- "User suspended for spam behavior on 2025-12-01"

-- Login attempt blocked
SELECT user_id, account_status
FROM "User"
WHERE email = '...'
  AND password_hash = '...';
-- Result: account_status = 'SUSPENDED'
-- Application logic: Return "Account suspended. Contact support."
```

**Restore:**
```sql
-- Admin restores account
UPDATE "User"
SET account_status = 'ACTIVE',
    updated_at = CURRENT_TIMESTAMP
WHERE user_id = '...';
```

**4. DELETED**

**Ý nghĩa:** User đã xóa account (soft delete)

**Khi nào:**
- User requests account deletion
- GDPR right to be forgotten
- Admin deletes inactive accounts (cleanup)

**Hành động được phép:**
- ❌ Login (blocked)
- ❌ All actions blocked
- ✅ Admin can view deleted accounts (audit)
- ✅ Possible to restore within X days (grace period)

**Flow:**
```sql
-- User requests deletion
UPDATE "User"
SET account_status = 'DELETED',
    updated_at = CURRENT_TIMESTAMP
WHERE user_id = '...';

-- Optional: Add deleted_at timestamp
ALTER TABLE "User" ADD COLUMN deleted_at TIMESTAMP;

UPDATE "User"
SET account_status = 'DELETED',
    deleted_at = CURRENT_TIMESTAMP
WHERE user_id = '...';

-- GDPR compliance: Anonymize PII after 30 days
UPDATE "User"
SET email = 'deleted-' || user_id || '@example.com',
    first_name = 'DELETED',
    last_name = 'USER',
    phone = NULL,
    avatar_url = NULL
WHERE account_status = 'DELETED'
  AND deleted_at < CURRENT_TIMESTAMP - INTERVAL '30 days';
```

**Related Data:**
- Enrollments: Keep for analytics (anonymized user)
- Submissions: Keep for instructor records
- Grades: Keep for transcript purposes

**Status Transition Diagram:**

```
PENDING_VERIFICATION
    ↓ (email verified)
ACTIVE
    ↓ (violation)
SUSPENDED
    ↓ (appeal approved)
ACTIVE
    ↓ (user requests deletion)
DELETED

Or:
ACTIVE → DELETED (direct)
```

**Query Examples:**

```sql
-- 1. Count users by status
SELECT account_status, COUNT(*)
FROM "User"
GROUP BY account_status;

-- Result:
-- PENDING_VERIFICATION | 150
-- ACTIVE               | 8500
-- SUSPENDED            | 25
-- DELETED              | 325

-- 2. Find all suspended users
SELECT user_id, email, first_name, last_name, updated_at
FROM "User"
WHERE account_status = 'SUSPENDED'
ORDER BY updated_at DESC;

-- 3. Find users who never verified email (> 7 days)
SELECT user_id, email, created_at
FROM "User"
WHERE account_status = 'PENDING_VERIFICATION'
  AND created_at < CURRENT_TIMESTAMP - INTERVAL '7 days';

-- 4. Login validation
SELECT user_id, account_status
FROM "User"
WHERE email = 'user@example.com'
  AND password_hash = '$2a$10$...';

-- Application logic:
IF account_status = 'PENDING_VERIFICATION' THEN
  RETURN "Please verify your email first"
ELSIF account_status = 'SUSPENDED' THEN
  RETURN "Account suspended. Contact support."
ELSIF account_status = 'DELETED' THEN
  RETURN "Account not found"
ELSIF account_status = 'ACTIVE' THEN
  RETURN "Login successful"
END IF
```

**Kết luận:**
- 4 statuses cover full user lifecycle
- PENDING_VERIFICATION: email verification flow
- ACTIVE: normal operations
- SUSPENDED: temporary ban (reversible)
- DELETED: soft delete (GDPR compliance)

---

### Q2.3: User.preferences JSON có cấu trúc như thế nào?

**Trả lời:**

**User.preferences** là JSON field lưu trữ user settings và preferences.

**Cấu trúc chuẩn:**

```json
{
  "notifications": {
    "assignment_due": {
      "email": true,
      "push": true
    },
    "grade_published": {
      "email": true,
      "push": false
    },
    "course_update": {
      "email": false,
      "push": true
    },
    "certificate_issued": {
      "email": true,
      "push": true
    },
    "discussion_reply": {
      "email": false,
      "push": false
    }
  },
  "locale": "vi",
  "timezone": "Asia/Ho_Chi_Minh",
  "theme": "light",
  "dashboard": {
    "default_view": "my_courses",
    "show_progress": true,
    "show_upcoming_assignments": true
  },
  "privacy": {
    "profile_visibility": "PUBLIC",
    "show_email": false,
    "show_progress": true
  }
}
```

**Chi tiết từng section:**

**1. notifications:**

```json
{
  "notifications": {
    "<event_type>": {
      "email": boolean,  // Send email notification?
      "push": boolean    // Send push notification?
    }
  }
}
```

**Event types:**
- `assignment_due`: Assignment deadline approaching (24h, 1h before)
- `grade_published`: Instructor published grade
- `course_update`: Course content updated (new lecture, announcement)
- `certificate_issued`: Certificate generated
- `discussion_reply`: Someone replied to your discussion post

**Example:**
```sql
-- User wants email for grades, but no push
UPDATE "User"
SET preferences = jsonb_set(
  preferences,
  '{notifications,grade_published}',
  '{"email": true, "push": false}'
)
WHERE user_id = '...';

-- Query: Get notification settings for grade_published
SELECT preferences->'notifications'->'grade_published' AS grade_notif
FROM "User"
WHERE user_id = '...';

-- Result: {"email": true, "push": false}
```

**2. locale:**

```json
{
  "locale": "vi"  // or "en", "zh", "ja", ...
}
```

**Supported locales:**
- `vi`: Tiếng Việt
- `en`: English
- `zh`: 中文 (Chinese)
- `ja`: 日本語 (Japanese)

**Usage:**
```sql
-- Set locale
UPDATE "User"
SET preferences = jsonb_set(preferences, '{locale}', '"en"')
WHERE user_id = '...';

-- Query users by locale (for analytics)
SELECT
  preferences->>'locale' AS locale,
  COUNT(*) AS user_count
FROM "User"
WHERE account_status = 'ACTIVE'
GROUP BY preferences->>'locale';

-- Result:
-- vi | 5000
-- en | 3000
-- zh | 500
```

**3. timezone:**

```json
{
  "timezone": "Asia/Ho_Chi_Minh"  // IANA timezone
}
```

**Purpose:** Display times in user's local timezone

**Examples:**
- `America/New_York`: US East Coast
- `Europe/London`: UK
- `Asia/Tokyo`: Japan
- `Australia/Sydney`: Australia

**Usage:**
```sql
-- Assignment due date: 2025-12-15 23:59:59 UTC
-- User timezone: Asia/Ho_Chi_Minh (UTC+7)
-- Display: 2025-12-16 06:59:59 (user's local time)

-- Query: Get user timezone
SELECT preferences->>'timezone' AS timezone
FROM "User"
WHERE user_id = '...';

-- Application converts UTC → user timezone
```

**4. theme:**

```json
{
  "theme": "light"  // or "dark", "auto"
}
```

**Options:**
- `light`: Light theme (default)
- `dark`: Dark theme
- `auto`: Follow system preference

**Usage:**
```sql
-- Toggle theme
UPDATE "User"
SET preferences = jsonb_set(preferences, '{theme}', '"dark"')
WHERE user_id = '...';
```

**5. dashboard:**

```json
{
  "dashboard": {
    "default_view": "my_courses",        // or "calendar", "progress"
    "show_progress": true,               // Show progress charts
    "show_upcoming_assignments": true    // Show upcoming assignments widget
  }
}
```

**Purpose:** Customize dashboard layout

**6. privacy:**

```json
{
  "privacy": {
    "profile_visibility": "PUBLIC",  // or "PRIVATE", "FRIENDS_ONLY"
    "show_email": false,             // Show email on public profile
    "show_progress": true            // Show learning progress on profile
  }
}
```

**Purpose:** Control what others can see

**Default Values:**

Khi user mới đăng ký, set default preferences:

```sql
INSERT INTO "User" (email, password_hash, first_name, last_name, preferences)
VALUES (
  'new@example.com',
  '$2a$10$...',
  'John',
  'Doe',
  '{
    "notifications": {
      "assignment_due": {"email": true, "push": true},
      "grade_published": {"email": true, "push": true},
      "certificate_issued": {"email": true, "push": true}
    },
    "locale": "vi",
    "timezone": "Asia/Ho_Chi_Minh",
    "theme": "light"
  }'::JSON
);
```

**Query Examples:**

```sql
-- 1. Get all preferences
SELECT preferences
FROM "User"
WHERE user_id = '...';

-- 2. Get specific setting
SELECT preferences->'notifications'->'assignment_due'->>'email' AS email_notif
FROM "User"
WHERE user_id = '...';

-- 3. Update specific setting (deep merge)
UPDATE "User"
SET preferences = jsonb_set(
  preferences,
  '{notifications,grade_published,email}',
  'false'
)
WHERE user_id = '...';

-- 4. Find users who want email for assignments
SELECT user_id, email
FROM "User"
WHERE preferences->'notifications'->'assignment_due'->>'email' = 'true'
  AND account_status = 'ACTIVE';

-- 5. Users using dark theme
SELECT COUNT(*)
FROM "User"
WHERE preferences->>'theme' = 'dark';
```

**Migration / Evolution:**

Khi add new preference:

```sql
-- Add new notification type: "quiz_available"
UPDATE "User"
SET preferences = jsonb_set(
  preferences,
  '{notifications,quiz_available}',
  '{"email": true, "push": true}'::jsonb,
  true  -- create_missing = true
)
WHERE preferences->'notifications'->'quiz_available' IS NULL;
```

**Validation (Application Level):**

```python
# Pydantic model for validation
from pydantic import BaseModel

class NotificationSetting(BaseModel):
    email: bool = True
    push: bool = True

class Preferences(BaseModel):
    notifications: dict[str, NotificationSetting]
    locale: str = "vi"
    timezone: str = "Asia/Ho_Chi_Minh"
    theme: str = "light"  # light, dark, auto

    class Config:
        extra = "allow"  # Allow additional fields
```

**Kết luận:**
- preferences JSON = flexible user settings
- Easy to add new settings without ALTER TABLE
- Validated at application level (Pydantic, Joi)
- Default values on user registration

---

### Q2.4: Làm sao để check permissions của user khi họ thực hiện action?

**Trả lời:**

Permission check flow trong B-Learning Core:

**Step 1: Get user roles**

```sql
-- Query: Get all roles for user
SELECT r.name, r.permissions
FROM "UserRole" ur
JOIN "Role" r ON ur.role_id = r.role_id
WHERE ur.user_id = :user_id
  AND (ur.expires_at IS NULL OR ur.expires_at > CURRENT_TIMESTAMP);
```

**Result:**
```json
[
  {
    "name": "STUDENT",
    "permissions": [
      "view_course",
      "enroll_course",
      "submit_assignment",
      "take_quiz",
      "view_own_grades"
    ]
  },
  {
    "name": "INSTRUCTOR",
    "permissions": [
      "create_course",
      "update_course",
      "publish_course",
      "create_module",
      "create_lecture",
      "create_quiz",
      "grade_submission",
      "view_all_grades"
    ]
  }
]
```

**Step 2: Merge permissions**

```python
# Application logic (Python example)
def get_user_permissions(user_id):
    rows = db.query("""
        SELECT r.permissions
        FROM "UserRole" ur
        JOIN "Role" r ON ur.role_id = r.role_id
        WHERE ur.user_id = %s
          AND (ur.expires_at IS NULL OR ur.expires_at > CURRENT_TIMESTAMP)
    """, user_id)

    # Merge all permissions
    all_permissions = set()
    for row in rows:
        all_permissions.update(row['permissions'])

    return list(all_permissions)

# Result:
# ['view_course', 'enroll_course', 'submit_assignment', 'take_quiz',
#  'view_own_grades', 'create_course', 'update_course', 'publish_course',
#  'create_module', 'create_lecture', 'create_quiz', 'grade_submission',
#  'view_all_grades']
```

**Step 3: Check permission**

```python
def has_permission(user_id, required_permission):
    permissions = get_user_permissions(user_id)
    return required_permission in permissions

# Usage:
if has_permission(user_id, 'create_course'):
    # Allow course creation
    create_course(...)
else:
    raise PermissionError("You don't have permission to create courses")
```

**Example Permissions by Role:**

**1. STUDENT:**
```json
{
  "name": "STUDENT",
  "permissions": [
    "view_published_courses",
    "enroll_course",
    "view_enrolled_courses",
    "view_module",
    "view_lecture",
    "download_resource",
    "submit_assignment",
    "take_quiz",
    "view_attempt",
    "view_own_grades",
    "view_own_progress",
    "view_own_certificate",
    "post_discussion",
    "reply_discussion"
  ]
}
```

**2. INSTRUCTOR:**
```json
{
  "name": "INSTRUCTOR",
  "permissions": [
    // All STUDENT permissions +
    "create_course",
    "update_course",
    "delete_course",
    "publish_course",
    "archive_course",
    "create_module",
    "update_module",
    "delete_module",
    "create_lecture",
    "update_lecture",
    "delete_lecture",
    "upload_resource",
    "create_quiz",
    "update_quiz",
    "delete_quiz",
    "grade_submission",
    "view_all_submissions",
    "view_all_attempts",
    "view_course_analytics",
    "export_grades",
    "issue_certificate"
  ]
}
```

**3. TA (Teaching Assistant):**
```json
{
  "name": "TA",
  "permissions": [
    // All STUDENT permissions +
    "view_enrolled_courses",  // Only courses they assist
    "grade_submission",       // Limited to assigned courses
    "view_all_submissions",   // Limited
    "view_all_attempts",      // Limited
    "reply_discussion"        // Moderate discussions
  ]
}
```

**4. ADMIN:**
```json
{
  "name": "ADMIN",
  "permissions": [
    "*"  // All permissions
    // Or explicitly list all:
    "manage_users",
    "manage_roles",
    "view_all_courses",
    "delete_any_course",
    "view_system_logs",
    "manage_system_settings",
    "export_data",
    "...all other permissions..."
  ]
}
```

**Permission Check Examples:**

**Example 1: Create Course**

```python
@app.post("/api/courses")
def create_course(course_data, user_id):
    # Check permission
    if not has_permission(user_id, 'create_course'):
        raise HTTPException(403, "Permission denied")

    # Check account status
    user = db.query("SELECT account_status FROM User WHERE user_id = %s", user_id)
    if user['account_status'] != 'ACTIVE':
        raise HTTPException(403, "Account not active")

    # Create course
    course = db.insert("Course", {
        'title': course_data['title'],
        'created_by': user_id,
        ...
    })

    return course
```

**Example 2: Grade Submission**

```python
@app.post("/api/submissions/{submission_id}/grade")
def grade_submission(submission_id, grade_data, user_id):
    # Check permission
    if not has_permission(user_id, 'grade_submission'):
        raise HTTPException(403, "Permission denied")

    # Additional check: Is user instructor/TA of this course?
    submission = db.query("""
        SELECT s.*, l.module_id, m.course_id, c.created_by
        FROM "AssignmentSubmission" s
        JOIN "Lecture" l ON s.lecture_id = l.lecture_id
        JOIN "Module" m ON l.module_id = m.module_id
        JOIN "Course" c ON m.course_id = c.course_id
        WHERE s.submission_id = %s
    """, submission_id)

    # User must be course instructor
    if submission['created_by'] != user_id:
        # Or check if user is TA for this course (via separate logic)
        raise HTTPException(403, "You are not instructor of this course")

    # Grade submission
    db.update("AssignmentSubmission", submission_id, {
        'grade': grade_data['grade'],
        'feedback': grade_data['feedback'],
        'graded_at': 'CURRENT_TIMESTAMP',
        'graded_by': user_id
    })

    return {'success': True}
```

**Example 3: View Grades**

```python
@app.get("/api/courses/{course_id}/grades")
def get_course_grades(course_id, user_id):
    # Check roles
    roles = get_user_roles(user_id)

    if 'INSTRUCTOR' in roles or 'ADMIN' in roles:
        # View all grades
        grades = db.query("""
            SELECT e.user_id, u.email, e.final_grade
            FROM "Enrollment" e
            JOIN "User" u ON e.user_id = u.user_id
            WHERE e.course_id = %s
        """, course_id)

    elif 'STUDENT' in roles:
        # View only own grade
        grades = db.query("""
            SELECT final_grade
            FROM "Enrollment"
            WHERE course_id = %s AND user_id = %s
        """, course_id, user_id)

    else:
        raise HTTPException(403, "Permission denied")

    return grades
```

**Optimization: Permission Caching**

```python
# Cache permissions in session/JWT token
import jwt

def generate_token(user_id):
    permissions = get_user_permissions(user_id)

    token = jwt.encode({
        'user_id': user_id,
        'permissions': permissions,
        'exp': datetime.utcnow() + timedelta(hours=24)
    }, secret_key)

    return token

# Verify token and check permission
def check_permission_from_token(token, required_permission):
    payload = jwt.decode(token, secret_key)
    return required_permission in payload['permissions']

# No database query needed for permission check!
```

**Kết luận:**
- Permissions stored in Role.permissions JSON
- User gets permissions from all their roles (merged)
- Application checks permission before action
- Can cache permissions in JWT token for performance

---

## 3. DOMAIN 2: COURSE CONTENT

### Q3.1: Tại sao cần bảng Module? Tại sao không lưu Lectures trực tiếp trong Course?

**Trả lời:**

Module là layer trung gian giữa Course và Lecture, tạo ra **3-level hierarchy**:

```
Course (Khóa học)
  └── Module (Chương/Mô đun)
      └── Lecture (Bài giảng)
```

**Lý do cần Module:**

**1. Organization & Structure:**

Course có thể có 50-100 lectures → Cần group thành modules để dễ navigate

**Ví dụ thực tế:**
```
Course: "Introduction to Java Programming"
  ├── Module 1: Getting Started (3 lectures)
  │   ├── Lecture 1.1: What is Java?
  │   ├── Lecture 1.2: Install JDK
  │   └── Lecture 1.3: Hello World
  │
  ├── Module 2: Java Basics (5 lectures)
  │   ├── Lecture 2.1: Variables and Data Types
  │   ├── Lecture 2.2: Operators
  │   ├── Lecture 2.3: Control Flow
  │   ├── Lecture 2.4: Loops
  │   └── Lecture 2.5: Arrays
  │
  ├── Module 3: OOP Concepts (7 lectures)
  │   ├── Lecture 3.1: Classes and Objects
  │   ├── Lecture 3.2: Constructors
  │   ├── Lecture 3.3: Methods
  │   ├── Lecture 3.4: Inheritance
  │   ├── Lecture 3.5: Polymorphism
  │   ├── Lecture 3.6: Abstraction
  │   └── Lecture 3.7: Encapsulation
  │
  └── Module 4: Advanced Topics (8 lectures)
      ├── ...
```

**Without modules:**
```
Course: "Introduction to Java Programming"
  ├── Lecture 1: What is Java?
  ├── Lecture 2: Install JDK
  ├── Lecture 3: Hello World
  ├── Lecture 4: Variables
  ├── ...
  └── Lecture 23: Final Project
```
❌ Flat list of 23 lectures → Hard to navigate
❌ No logical grouping
❌ Can't see "big picture"

**2. Sequential Learning Path:**

```sql
CREATE TABLE "Module" (
  module_id UUID PRIMARY KEY,
  course_id UUID REFERENCES "Course",
  title VARCHAR(200),
  order_num INT,  -- 1, 2, 3, ...
  prerequisite_module_ids UUID[],  -- Must complete before accessing
  ...
);

-- Example: Module 3 requires Module 1, 2
UPDATE "Module"
SET prerequisite_module_ids = ARRAY[
  'module-1-uuid',
  'module-2-uuid'
]::UUID[]
WHERE module_id = 'module-3-uuid';
```

**Business logic:**
- Student must complete Module 1 before accessing Module 2
- Lock Module 2 until Module 1 completed ≥ 80%

**Without modules:**
- Hard to enforce sequential learning
- Can't group related lectures as prerequisite

**3. Progress Tracking:**

```sql
-- Module-level progress
SELECT
  m.title AS module_title,
  COUNT(l.lecture_id) AS total_lectures,
  COUNT(p.progress_id) FILTER (WHERE p.status = 'COMPLETED') AS completed_lectures,
  ROUND(
    COUNT(p.progress_id) FILTER (WHERE p.status = 'COMPLETED')::NUMERIC /
    COUNT(l.lecture_id) * 100,
    2
  ) AS module_progress_percentage
FROM "Module" m
JOIN "Lecture" l ON m.module_id = l.module_id
LEFT JOIN "Progress" p ON l.lecture_id = p.lecture_id AND p.user_id = :user_id
WHERE m.course_id = :course_id
GROUP BY m.module_id, m.title, m.order_num
ORDER BY m.order_num;
```

**Result:**
```
Module 1: Getting Started       | 3 lectures | 3 completed | 100.00%
Module 2: Java Basics           | 5 lectures | 2 completed |  40.00%
Module 3: OOP Concepts          | 7 lectures | 0 completed |   0.00%
```

**UI Display:**
```
Course Progress: 21.74%

✅ Module 1: Getting Started (100%)
    ✅ Lecture 1.1: What is Java?
    ✅ Lecture 1.2: Install JDK
    ✅ Lecture 1.3: Hello World

🔄 Module 2: Java Basics (40%)
    ✅ Lecture 2.1: Variables
    ✅ Lecture 2.2: Operators
    ⬜ Lecture 2.3: Control Flow
    ⬜ Lecture 2.4: Loops
    ⬜ Lecture 2.5: Arrays

🔒 Module 3: OOP Concepts (Locked - Complete Module 2 first)
```

**Without modules:**
- Only course-level progress (e.g., "35% complete")
- Can't see progress per topic
- Harder to motivate students ("you completed 1 module!")

**4. Reusability:**

```sql
-- Copy entire module to another course
INSERT INTO "Module" (course_id, title, description, order_num)
SELECT
  :new_course_id,  -- Different course
  title,
  description,
  order_num
FROM "Module"
WHERE module_id = :module_to_copy;

-- Copy all lectures in module
INSERT INTO "Lecture" (module_id, title, type, content, order_num)
SELECT
  :new_module_id,  -- New module
  title,
  type,
  content,
  order_num
FROM "Lecture"
WHERE module_id = :original_module_id;
```

**Use case:**
- "Introduction to Programming" module → Use in both Java course and Python course
- "Basic Math" module → Use in multiple engineering courses

**Without modules:**
- Have to copy lectures one by one
- No logical unit to reuse

**5. Estimated Duration:**

```sql
CREATE TABLE "Module" (
  ...
  estimated_duration_minutes INT
);

-- Calculate total course duration
SELECT
  SUM(estimated_duration_minutes) AS total_minutes,
  SUM(estimated_duration_minutes) / 60.0 AS total_hours
FROM "Module"
WHERE course_id = :course_id;

-- Result: 15.5 hours
```

**Display:**
```
Course: "Introduction to Java" (15.5 hours total)
  ├── Module 1: Getting Started (2 hours)
  ├── Module 2: Java Basics (3.5 hours)
  ├── Module 3: OOP Concepts (5 hours)
  └── Module 4: Advanced Topics (5 hours)
```

**Without modules:**
- Only course-level duration
- Can't break down time commitment

**6. Flexible Delivery:**

```sql
-- Blended learning: Module 1-2 online, Module 3-4 in-class
UPDATE "Module"
SET delivery_mode = 'ONLINE'
WHERE module_id IN ('module-1', 'module-2');

UPDATE "Module"
SET delivery_mode = 'IN_CLASS'
WHERE module_id IN ('module-3', 'module-4');
```

**Without modules:**
- Hard to specify which lectures are online vs in-class

**Schema Comparison:**

**With Modules (✅ Current design):**
```sql
Course (1) → Module (N) → Lecture (N)

-- 3 tables
CREATE TABLE "Course" (
  course_id UUID PRIMARY KEY,
  title VARCHAR(200)
);

CREATE TABLE "Module" (
  module_id UUID PRIMARY KEY,
  course_id UUID REFERENCES "Course",
  title VARCHAR(200),
  order_num INT
);

CREATE TABLE "Lecture" (
  lecture_id UUID PRIMARY KEY,
  module_id UUID REFERENCES "Module",
  title VARCHAR(200),
  order_num INT
);
```

**Without Modules (❌ Alternative):**
```sql
Course (1) → Lecture (N)

-- 2 tables
CREATE TABLE "Course" (
  course_id UUID PRIMARY KEY,
  title VARCHAR(200)
);

CREATE TABLE "Lecture" (
  lecture_id UUID PRIMARY KEY,
  course_id UUID REFERENCES "Course",  -- Direct reference
  title VARCHAR(200),
  order_num INT,
  group_name VARCHAR(200)  -- "Module 1", "Module 2", ... (string)
);
```
❌ No structure (group_name is just string)
❌ Can't enforce prerequisites at module level
❌ Hard to query module-level progress
❌ No module metadata (duration, description)

**Kết luận:**
- Module = essential for course organization
- Provides hierarchy, sequential learning, progress tracking
- Small overhead (1 extra JOIN) but huge benefits for UX

---

### Q3.2: Bảng Lecture có type gì? Tại sao Assignment là type của Lecture thay vì bảng riêng?

**Trả lời:**

Bảng **Lecture** có 4 types:

```sql
CREATE TABLE "Lecture" (
  lecture_id UUID PRIMARY KEY,
  module_id UUID REFERENCES "Module",
  title VARCHAR(200),
  type VARCHAR(20),  -- VIDEO, READING, ASSIGNMENT, QUIZ
  ...

  CONSTRAINT chk_lecture_type CHECK (type IN (
    'VIDEO', 'READING', 'ASSIGNMENT', 'QUIZ'
  ))
);
```

**1. VIDEO**

**Purpose:** Video lecture (YouTube, Vimeo, self-hosted)

**Fields used:**
```sql
type = 'VIDEO'
content = 'https://youtube.com/watch?v=xxx'  -- Video URL
video_duration_minutes = 45
```

**UI:**
- Video player
- Playback controls (play, pause, speed, subtitles)
- Progress tracking (e.g., "Watched 30/45 minutes")

**2. READING**

**Purpose:** Text content, article, tutorial

**Fields used:**
```sql
type = 'READING'
content = '<h1>Introduction to Java</h1><p>Java is...</p>'  -- HTML/Markdown
estimated_duration_minutes = 15
```

**UI:**
- Rich text display
- Scrollable content
- Mark as complete button

**3. ASSIGNMENT**

**Purpose:** Homework, project, coding assignment

**Fields used:**
```sql
type = 'ASSIGNMENT'
assignment_config = '{
  "instructions": "Complete the Java project...",
  "due_date": "2025-12-15T23:59:59Z",
  "max_score": 100,
  "submission_types": ["FILE", "TEXT"],
  "allowed_file_types": [".pdf", ".zip", ".docx"],
  "max_file_size_mb": 10,
  "max_attempts": 3
}'::JSONB
```

**UI:**
- Assignment instructions
- File upload form
- Submission history
- Grade display (after graded)

**4. QUIZ**

**Purpose:** Quiz, test, assessment

**Fields used:**
```sql
type = 'QUIZ'
quiz_id = '<quiz-uuid>'  -- FK to Quiz table
```

**UI:**
- Start quiz button
- Quiz questions (from Quiz table)
- Submit answers
- View results

---

**Tại sao Assignment là type của Lecture thay vì bảng riêng?**

**Design Decision: Unified Lecture Model**

**Option 1: Separate Assignment table (❌ Rejected)**

```sql
-- Separate tables
CREATE TABLE "Lecture" (
  lecture_id UUID PRIMARY KEY,
  module_id UUID REFERENCES "Module",
  title VARCHAR(200),
  type VARCHAR(20),  -- VIDEO, READING
  content TEXT
);

CREATE TABLE "Assignment" (
  assignment_id UUID PRIMARY KEY,
  module_id UUID REFERENCES "Module",  -- Where to put assignment?
  title VARCHAR(200),
  instructions TEXT,
  due_date TIMESTAMP,
  max_score DECIMAL
);
```

**Problems:**
- ❌ Assignments và Lectures ở different levels → Confusing
- ❌ Ordering: How to order lectures and assignments together?
  ```
  Module 1:
    1. Lecture: Introduction
    2. Assignment: Setup
    3. Lecture: Variables
    4. Lecture: Operators
    5. Assignment: Practice Problems
  ```
  → Need `order_num` across 2 tables → Complex

- ❌ Progress tracking: Have to query 2 tables
  ```sql
  -- Count completed items in module
  SELECT
    COUNT(*) FILTER (WHERE type = 'LECTURE' AND completed) AS lectures_done,
    COUNT(*) FILTER (WHERE type = 'ASSIGNMENT' AND submitted) AS assignments_done
  FROM (
    SELECT lecture_id AS id, 'LECTURE' AS type, completed FROM "Lecture" ...
    UNION ALL
    SELECT assignment_id AS id, 'ASSIGNMENT' AS type, submitted FROM "Assignment" ...
  );
  ```
  → Ugly UNION query

- ❌ UI complexity: Have to handle 2 different entities

**Option 2: Assignment as Lecture type (✅ Current design)**

```sql
-- Unified model
CREATE TABLE "Lecture" (
  lecture_id UUID PRIMARY KEY,
  module_id UUID REFERENCES "Module",
  title VARCHAR(200),
  type VARCHAR(20),  -- VIDEO, READING, ASSIGNMENT, QUIZ
  order_num INT,
  assignment_config JSONB  -- Only for ASSIGNMENT type
);

CREATE TABLE "AssignmentSubmission" (
  submission_id UUID PRIMARY KEY,
  lecture_id UUID REFERENCES "Lecture",  -- Assignment lecture
  user_id UUID REFERENCES "User",
  submitted_content TEXT,
  file_urls TEXT[],
  score DECIMAL,
  status VARCHAR(20)
);
```

**Benefits:**
- ✅ **Consistent ordering:** All content types in 1 table
  ```sql
  SELECT title, type, order_num
  FROM "Lecture"
  WHERE module_id = :module_id
  ORDER BY order_num;

  -- Result:
  -- 1. Introduction (VIDEO)
  -- 2. Setup Guide (READING)
  -- 3. Assignment: Setup (ASSIGNMENT)
  -- 4. Variables (VIDEO)
  -- 5. Assignment: Practice (ASSIGNMENT)
  ```

- ✅ **Simple progress tracking:**
  ```sql
  -- Count total items
  SELECT COUNT(*) FROM "Lecture" WHERE module_id = :module_id;

  -- Count completed items
  SELECT COUNT(*)
  FROM "Lecture" l
  JOIN "Progress" p ON l.lecture_id = p.lecture_id
  WHERE l.module_id = :module_id
    AND p.user_id = :user_id
    AND p.status = 'COMPLETED';
  ```

- ✅ **Unified API:**
  ```
  GET /api/modules/:moduleId/lectures
  → Returns all content (videos, readings, assignments, quizzes)
  → Frontend handles each type differently based on `type` field
  ```

- ✅ **Flexible schema:**
  - VIDEO → `content` = URL, `video_duration_minutes`
  - READING → `content` = HTML/Markdown
  - ASSIGNMENT → `assignment_config` = JSON
  - QUIZ → `quiz_id` = FK to Quiz

  → Each type uses different fields, no problem

**Trade-offs:**

**Cons of unified model:**
- ❌ `assignment_config` NULL for non-assignment lectures (waste space?)
  - **Mitigation:** JSON is flexible, NULL is OK
- ❌ No schema enforcement for assignment fields
  - **Mitigation:** Validate at application level (Pydantic, Joi)
- ❌ Some fields irrelevant for some types
  - **Mitigation:** Application logic handles each type differently

**Pros outweigh cons → Unified model better**

**assignment_config JSON Structure:**

```json
{
  "instructions": "Complete the following tasks...",
  "due_date": "2025-12-15T23:59:59Z",
  "max_score": 100,
  "pass_score": 60,
  "submission_types": ["FILE", "TEXT", "URL"],
  "allowed_file_types": [".pdf", ".docx", ".zip", ".java", ".py"],
  "max_file_size_mb": 10,
  "max_attempts": 3,
  "auto_grade": false,
  "rubric": [
    {
      "criterion": "Code Quality",
      "points": 30,
      "description": "Clean, readable code"
    },
    {
      "criterion": "Functionality",
      "points": 50,
      "description": "All features work correctly"
    },
    {
      "criterion": "Documentation",
      "points": 20,
      "description": "README and comments"
    }
  ]
}
```

**Query Examples:**

```sql
-- 1. Get all assignments in course
SELECT l.lecture_id, l.title, l.assignment_config
FROM "Lecture" l
JOIN "Module" m ON l.module_id = m.module_id
WHERE m.course_id = :course_id
  AND l.type = 'ASSIGNMENT';

-- 2. Get assignments due this week
SELECT l.lecture_id, l.title,
       l.assignment_config->>'due_date' AS due_date
FROM "Lecture" l
JOIN "Module" m ON l.module_id = m.module_id
WHERE m.course_id = :course_id
  AND l.type = 'ASSIGNMENT'
  AND (l.assignment_config->>'due_date')::TIMESTAMP
      BETWEEN CURRENT_TIMESTAMP AND CURRENT_TIMESTAMP + INTERVAL '7 days';

-- 3. Check if assignment allows file submission
SELECT
  l.title,
  l.assignment_config->'submission_types' @> '["FILE"]' AS allows_file
FROM "Lecture" l
WHERE l.lecture_id = :lecture_id;
```

**Kết luận:**
- Lecture.type = unified content model
- Assignment = special type of Lecture (not separate entity)
- Simplifies ordering, progress tracking, API design
- assignment_config JSON = flexible assignment metadata

---

### Q3.3: Bảng Resource có vai trò gì? Khác với Attachment như thế nào?

**Trả lời:**

**Resource** là bảng lưu trữ **supplementary files** cho lectures (slides, PDF, code samples, datasets).

**Schema:**

```sql
CREATE TABLE "Resource" (
  resource_id UUID PRIMARY KEY,
  lecture_id UUID REFERENCES "Lecture" ON DELETE CASCADE,
  resource_name VARCHAR(200) NOT NULL,
  resource_type VARCHAR(50),  -- PDF, PPT, ZIP, TXT, etc.
  file_url VARCHAR(500) NOT NULL,
  file_size_bytes BIGINT,
  uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  uploaded_by UUID REFERENCES "User" ON DELETE SET NULL
);
```

**Purpose:**

**1. Supplementary Materials:**

```
Lecture: "Introduction to OOP"
  └── Resources:
      ├── slides.pdf (2.5 MB)
      ├── sample_code.zip (500 KB)
      ├── reading_material.docx (1.2 MB)
      └── dataset.csv (3.1 MB)
```

**2. Downloadable Files:**

Students can download resources for offline study

```sql
-- Get all resources for a lecture
SELECT resource_name, file_url, file_size_bytes
FROM "Resource"
WHERE lecture_id = :lecture_id
ORDER BY uploaded_at;
```

**3. Multiple Files per Lecture:**

1 lecture có thể có 0, 1, hoặc nhiều resources

```sql
-- One-to-Many relationship
Lecture (1) → Resource (N)
```

**Comparison: Resource vs Attachment (trong v2.0)**

| Aspect | Resource (Core v1.0) | Attachment (v2.0) |
|--------|---------------------|-------------------|
| **Purpose** | Supplementary files for lectures | Same |
| **Table** | Separate `Resource` table | Same (different name) |
| **FK** | `lecture_id` | Same |
| **Usage** | Download before/after lecture | Same |

→ **Resource = Attachment** (same thing, different name)
→ Trong v1.0 Core, đổi tên thành "Resource" cho clear hơn

**Example Usage:**

**Lecture with video + resources:**

```sql
-- Lecture record
INSERT INTO "Lecture" (lecture_id, module_id, title, type, content)
VALUES (
  'lecture-uuid',
  'module-uuid',
  'Introduction to Java OOP',
  'VIDEO',
  'https://youtube.com/watch?v=xxx'
);

-- Resources for lecture
INSERT INTO "Resource" (lecture_id, resource_name, resource_type, file_url, file_size_bytes)
VALUES
  ('lecture-uuid', 'Lecture_Slides.pdf', 'PDF', 'https://s3.../slides.pdf', 2621440),
  ('lecture-uuid', 'Sample_Code.zip', 'ZIP', 'https://s3.../code.zip', 512000),
  ('lecture-uuid', 'Reading_Material.docx', 'DOCX', 'https://s3.../reading.docx', 1258291);
```

**UI Display:**

```
📹 Lecture: Introduction to Java OOP (45 minutes)

[Video Player]

📎 Resources (3 files):
  📄 Lecture_Slides.pdf (2.5 MB) [Download]
  📦 Sample_Code.zip (500 KB) [Download]
  📝 Reading_Material.docx (1.2 MB) [Download]
```

**Query Examples:**

```sql
-- 1. Get lecture with resources
SELECT
  l.title AS lecture_title,
  l.type,
  l.content AS video_url,
  r.resource_name,
  r.file_url,
  r.file_size_bytes
FROM "Lecture" l
LEFT JOIN "Resource" r ON l.lecture_id = r.lecture_id
WHERE l.lecture_id = :lecture_id;

-- 2. Count resources per lecture
SELECT
  l.lecture_id,
  l.title,
  COUNT(r.resource_id) AS resource_count,
  SUM(r.file_size_bytes) AS total_size_bytes
FROM "Lecture" l
LEFT JOIN "Resource" r ON l.lecture_id = r.lecture_id
WHERE l.module_id = :module_id
GROUP BY l.lecture_id, l.title;

-- 3. Find lectures with PDF slides
SELECT DISTINCT l.title
FROM "Lecture" l
JOIN "Resource" r ON l.lecture_id = r.lecture_id
WHERE r.resource_type = 'PDF'
  AND r.resource_name LIKE '%slide%';

-- 4. Total storage used by course
SELECT
  c.title AS course_title,
  SUM(r.file_size_bytes) / 1024.0 / 1024.0 AS total_mb
FROM "Course" c
JOIN "Module" m ON c.course_id = m.course_id
JOIN "Lecture" l ON m.module_id = l.module_id
JOIN "Resource" r ON l.lecture_id = r.lecture_id
WHERE c.course_id = :course_id
GROUP BY c.course_id, c.title;
```

**File Storage:**

Resources không lưu binary data trong database (too large, poor performance)

```sql
-- ❌ BAD: Store file content in database
CREATE TABLE "Resource" (
  resource_id UUID PRIMARY KEY,
  file_content BYTEA  -- Binary data (BAD!)
);

-- ✅ GOOD: Store file URL (reference to S3/GCS)
CREATE TABLE "Resource" (
  resource_id UUID PRIMARY KEY,
  file_url VARCHAR(500)  -- 'https://s3.amazonaws.com/bucket/file.pdf'
);
```

**File Upload Flow:**

```
1. User uploads file → Frontend
2. Frontend uploads to S3/GCS → Get URL
3. Frontend sends URL to backend
4. Backend saves URL to Resource table

-- API endpoint
POST /api/lectures/:lectureId/resources
{
  "resource_name": "Lecture_Slides.pdf",
  "resource_type": "PDF",
  "file_url": "https://s3.../slides.pdf",
  "file_size_bytes": 2621440
}
```

**Cascade Delete:**

```sql
CREATE TABLE "Resource" (
  lecture_id UUID REFERENCES "Lecture" ON DELETE CASCADE
);

-- When lecture deleted → All resources deleted
DELETE FROM "Lecture" WHERE lecture_id = '...';
-- → All Resource records with this lecture_id automatically deleted
-- → S3 files NOT deleted (need separate cleanup job)
```

**Kết luận:**
- Resource = supplementary files for lectures
- One-to-Many relationship (1 lecture → N resources)
- Store file URL, not binary content
- Cascade delete when lecture deleted
- Same concept as "Attachment" in other LMS

---

### Q3.4: Module.prerequisite_module_ids là array UUID - tại sao không dùng bảng riêng?

**Trả lời:**

**prerequisite_module_ids** là PostgreSQL **array field** lưu list of module UUIDs phải complete trước khi access module này.

**Schema:**

```sql
CREATE TABLE "Module" (
  module_id UUID PRIMARY KEY,
  course_id UUID REFERENCES "Course",
  title VARCHAR(200),
  order_num INT,
  prerequisite_module_ids UUID[],  -- Array of module UUIDs
  ...
);
```

**Example:**

```sql
-- Module 1: No prerequisites
INSERT INTO "Module" (title, order_num, prerequisite_module_ids)
VALUES ('Introduction', 1, NULL);

-- Module 2: Requires Module 1
INSERT INTO "Module" (title, order_num, prerequisite_module_ids)
VALUES ('Basic Concepts', 2, ARRAY['module-1-uuid']::UUID[]);

-- Module 3: Requires Module 1 AND Module 2
INSERT INTO "Module" (title, order_num, prerequisite_module_ids)
VALUES ('Advanced Topics', 3, ARRAY['module-1-uuid', 'module-2-uuid']::UUID[]);
```

**Tại sao dùng Array thay vì bảng riêng?**

**Option 1: Separate PrerequisiteModule table (❌ Rejected)**

```sql
-- Junction table for M-N relationship
CREATE TABLE "ModulePrerequisite" (
  prerequisite_id UUID PRIMARY KEY,
  module_id UUID REFERENCES "Module",  -- Target module
  prerequisite_module_id UUID REFERENCES "Module",  -- Required module
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT uq_module_prerequisite UNIQUE(module_id, prerequisite_module_id)
);

-- Insert prerequisites
INSERT INTO "ModulePrerequisite" (module_id, prerequisite_module_id)
VALUES
  ('module-2-uuid', 'module-1-uuid'),  -- Module 2 requires 1
  ('module-3-uuid', 'module-1-uuid'),  -- Module 3 requires 1
  ('module-3-uuid', 'module-2-uuid');  -- Module 3 requires 2
```

**Problems:**
- ❌ Need separate table → More complexity
- ❌ Need JOIN to get prerequisites
  ```sql
  SELECT mp.prerequisite_module_id
  FROM "ModulePrerequisite" mp
  WHERE mp.module_id = :module_id;
  ```
- ❌ Extra table to maintain
- ❌ More foreign key constraints

**Option 2: Array field (✅ Current design)**

```sql
CREATE TABLE "Module" (
  module_id UUID PRIMARY KEY,
  prerequisite_module_ids UUID[]  -- Array
);

-- Get prerequisites (no JOIN needed)
SELECT prerequisite_module_ids
FROM "Module"
WHERE module_id = :module_id;

-- Result: {module-1-uuid, module-2-uuid}
```

**Benefits:**
- ✅ Simple: All data in 1 table
- ✅ Fast: No JOIN needed
- ✅ Atomic: Load all prerequisites in 1 query

**When is Array better than Junction Table?**

✅ **Use Array when:**
- Relationship is **one-to-many** (module → prerequisite modules)
- Small number of items (< 100)
- Always load all items together
- No complex queries on relationships
- **Example:** Module prerequisites (typically 1-3 prerequisites)

❌ **Use Junction Table when:**
- Relationship is **many-to-many** with metadata
- Large number of items (> 100)
- Need to query relationships independently
- Need additional fields on relationship
- **Example:** CourseTag (course ↔ tag, with `created_at`, `created_by`)

**Array Operations:**

```sql
-- 1. Check if module has prerequisites
SELECT module_id, title
FROM "Module"
WHERE prerequisite_module_ids IS NOT NULL
  AND array_length(prerequisite_module_ids, 1) > 0;

-- 2. Check if specific module is prerequisite
SELECT module_id, title
FROM "Module"
WHERE 'target-module-uuid'::UUID = ANY(prerequisite_module_ids);

-- 3. Add prerequisite
UPDATE "Module"
SET prerequisite_module_ids = array_append(
  COALESCE(prerequisite_module_ids, ARRAY[]::UUID[]),
  'new-prerequisite-uuid'::UUID
)
WHERE module_id = :module_id;

-- 4. Remove prerequisite
UPDATE "Module"
SET prerequisite_module_ids = array_remove(
  prerequisite_module_ids,
  'prerequisite-to-remove-uuid'::UUID
)
WHERE module_id = :module_id;

-- 5. Get module with prerequisite details
SELECT
  m.module_id,
  m.title,
  (
    SELECT json_agg(json_build_object(
      'module_id', pm.module_id,
      'title', pm.title
    ))
    FROM "Module" pm
    WHERE pm.module_id = ANY(m.prerequisite_module_ids)
  ) AS prerequisites
FROM "Module" m
WHERE m.module_id = :module_id;
```

**Business Logic: Check Prerequisites Completion**

```sql
-- Check if user completed all prerequisites for module
WITH user_completed_modules AS (
  SELECT DISTINCT m.module_id
  FROM "Module" m
  JOIN "Lecture" l ON m.module_id = l.module_id
  JOIN "Progress" p ON l.lecture_id = p.lecture_id
  WHERE p.user_id = :user_id
    AND p.status = 'COMPLETED'
  GROUP BY m.module_id
  HAVING COUNT(p.progress_id) = (
    SELECT COUNT(*) FROM "Lecture" WHERE module_id = m.module_id
  )
)
SELECT
  m.module_id,
  m.title,
  CASE
    WHEN m.prerequisite_module_ids IS NULL THEN TRUE
    WHEN m.prerequisite_module_ids <@ ARRAY(SELECT module_id FROM user_completed_modules)::UUID[]
      THEN TRUE
    ELSE FALSE
  END AS can_access
FROM "Module" m
WHERE m.course_id = :course_id;
```

**UI Flow:**

```
Course: Introduction to Java

✅ Module 1: Getting Started (No prerequisites)
    Status: Completed ✅

✅ Module 2: Java Basics (Requires: Module 1)
    Status: In Progress 🔄
    Prerequisites: ✅ Module 1

🔒 Module 3: OOP Concepts (Requires: Module 1, Module 2)
    Status: Locked 🔒
    Prerequisites: ✅ Module 1, ⬜ Module 2
    Message: "Complete Module 2 to unlock"
```

**Index on Array:**

```sql
-- GIN index for fast array contains queries
CREATE INDEX idx_module_prerequisites ON "Module"
USING GIN (prerequisite_module_ids);

-- Fast query: Find modules that require Module X
EXPLAIN ANALYZE
SELECT module_id, title
FROM "Module"
WHERE 'module-x-uuid'::UUID = ANY(prerequisite_module_ids);

-- Uses index scan (fast)
```

**Validation:**

```python
# Application-level validation
def add_prerequisite(module_id, prerequisite_id):
    # Check circular dependency
    if has_circular_dependency(module_id, prerequisite_id):
        raise ValueError("Circular dependency detected")

    # Check prerequisite is in same course
    module_course = get_module_course(module_id)
    prereq_course = get_module_course(prerequisite_id)
    if module_course != prereq_course:
        raise ValueError("Prerequisite must be in same course")

    # Add prerequisite
    db.execute("""
        UPDATE "Module"
        SET prerequisite_module_ids = array_append(
          COALESCE(prerequisite_module_ids, ARRAY[]::UUID[]),
          %s::UUID
        )
        WHERE module_id = %s
    """, prerequisite_id, module_id)

def has_circular_dependency(module_id, new_prerequisite_id):
    # Check if new_prerequisite already depends on module (directly or indirectly)
    # BFS/DFS to detect cycle
    ...
```

**Kết luận:**
- prerequisite_module_ids = PostgreSQL array
- Simpler than junction table for this use case
- Fast (no JOIN), atomic (load all at once)
- GIN index for performance
- Application validates circular dependencies

---

## 4. DOMAIN 3: ASSESSMENT

### Q4.1: Quiz.questions là JSONB - cấu trúc như thế nào? Tại sao không dùng bảng Question riêng?

**Trả lời:**

**Quiz.questions** là JSONB array chứa tất cả questions của quiz.

**Cấu trúc:**

```sql
CREATE TABLE "Quiz" (
  quiz_id UUID PRIMARY KEY,
  lecture_id UUID REFERENCES "Lecture",
  title VARCHAR(200),
  time_limit_minutes INT,
  pass_percentage DECIMAL(5,2),
  questions JSONB,  -- Array of question objects
  ...
);
```

**questions JSONB Structure:**

```json
[
  {
    "question_id": "q1-uuid",
    "question_text": "What is Java?",
    "question_type": "MULTIPLE_CHOICE",
    "points": 10,
    "options": [
      {
        "option_id": "opt1",
        "option_text": "A programming language",
        "is_correct": true
      },
      {
        "option_id": "opt2",
        "option_text": "A coffee brand",
        "is_correct": false
      },
      {
        "option_id": "opt3",
        "option_text": "An island",
        "is_correct": false
      },
      {
        "option_id": "opt4",
        "option_text": "A database",
        "is_correct": false
      }
    ],
    "explanation": "Java is a high-level programming language.",
    "order_num": 1
  },
  {
    "question_id": "q2-uuid",
    "question_text": "Is Java OOP?",
    "question_type": "TRUE_FALSE",
    "points": 5,
    "options": [
      {
        "option_id": "opt1",
        "option_text": "True",
        "is_correct": true
      },
      {
        "option_id": "opt2",
        "option_text": "False",
        "is_correct": false
      }
    ],
    "explanation": "Java is object-oriented programming language.",
    "order_num": 2
  },
  {
    "question_id": "q3-uuid",
    "question_text": "Explain polymorphism in Java",
    "question_type": "SHORT_ANSWER",
    "points": 15,
    "options": [],  // No options for short answer
    "max_length": 500,
    "explanation": null,  // Graded manually
    "order_num": 3
  }
]
```

**Question Types:**

**1. MULTIPLE_CHOICE:**
- 4+ options
- 1 correct answer
- Auto-graded

**2. TRUE_FALSE:**
- 2 options (True/False)
- 1 correct answer
- Auto-graded

**3. SHORT_ANSWER:**
- Free text input
- Manual grading (instructor reviews)

**4. MULTIPLE_ANSWER:** (optional)
- Multiple correct answers
- Auto-graded (all correct options must be selected)

---

**Tại sao dùng JSONB thay vì bảng Question riêng?**

**Option 1: Separate Question + Option tables (❌ Rejected for Core v1.0)**

```sql
-- 3 tables
CREATE TABLE "Quiz" (
  quiz_id UUID PRIMARY KEY,
  title VARCHAR(200),
  time_limit_minutes INT
);

CREATE TABLE "Question" (
  question_id UUID PRIMARY KEY,
  quiz_id UUID REFERENCES "Quiz",
  question_text TEXT,
  question_type VARCHAR(20),
  points DECIMAL,
  order_num INT
);

CREATE TABLE "Option" (
  option_id UUID PRIMARY KEY,
  question_id UUID REFERENCES "Question",
  option_text TEXT,
  is_correct BOOLEAN,
  order_num INT
);

-- To load quiz: Need 3 queries + JOINs
SELECT * FROM "Quiz" WHERE quiz_id = '...';
SELECT * FROM "Question" WHERE quiz_id = '...' ORDER BY order_num;
SELECT * FROM "Option" WHERE question_id IN (...) ORDER BY order_num;
```

**Problems:**
- ❌ **3 tables** (complexity)
- ❌ **Multiple queries** to load quiz (slow)
- ❌ **Complex INSERTs** when creating quiz:
  ```sql
  BEGIN;
  INSERT INTO "Quiz" ...;
  INSERT INTO "Question" ... (10 questions);
  INSERT INTO "Option" ... (40 options for 10 questions);
  COMMIT;
  ```
- ❌ **Quiz editing** difficult (update/delete cascades)

**Option 2: JSONB questions (✅ Current design)**

```sql
-- 1 table
CREATE TABLE "Quiz" (
  quiz_id UUID PRIMARY KEY,
  title VARCHAR(200),
  time_limit_minutes INT,
  questions JSONB  -- All questions + options in 1 field
);

-- To load quiz: 1 query, no JOINs
SELECT * FROM "Quiz" WHERE quiz_id = '...';
```

**Benefits:**
- ✅ **Simple:** 1 table, 1 query
- ✅ **Atomic:** All quiz data loaded together (always need all questions)
- ✅ **Fast:** No JOINs
- ✅ **Easy to edit:**
  ```sql
  UPDATE "Quiz"
  SET questions = :new_questions_json
  WHERE quiz_id = '...';
  ```
- ✅ **Version control:** Easy to snapshot quiz (entire JSON saved)
- ✅ **Flexible:** Different question types, different structures

**Trade-offs:**

**Cons of JSONB:**
- ❌ **No schema enforcement**
  - Can insert invalid question structure
  - **Mitigation:** Validate at application level (Pydantic, Joi)

- ❌ **No foreign key constraints**
  - question_id in JSON not enforced
  - **Mitigation:** Application generates UUIDs

- ❌ **Harder to query individual questions**
  - "Find all quizzes with question about 'Java'"
  - **Mitigation:** Not common query, acceptable trade-off

- ❌ **Larger data size** (JSON overhead)
  - **Mitigation:** JSONB is compressed, minimal overhead

**Pros outweigh cons for quiz use case:**
- Quizzes always loaded atomically (need all questions)
- Editing quiz = replace entire questions array (no partial updates)
- Simple > Complex for MVP

**When to use JSONB vs Separate Tables:**

✅ **Use JSONB when:**
- Data always loaded/saved together (atomic)
- No complex queries on nested data
- Schema flexible (different question types)
- Version control important
- **Example:** Quiz questions

❌ **Use Separate Tables when:**
- Need complex queries on individual items
- Need foreign key constraints
- Data updated individually (not atomic)
- Large number of items (1000s+)
- **Example:** User accounts, Courses

---

**Query Examples:**

```sql
-- 1. Load entire quiz with questions
SELECT quiz_id, title, questions
FROM "Quiz"
WHERE quiz_id = :quiz_id;

-- 2. Get question count
SELECT
  quiz_id,
  title,
  jsonb_array_length(questions) AS question_count
FROM "Quiz";

-- 3. Get total points
SELECT
  quiz_id,
  title,
  (
    SELECT SUM((q->>'points')::DECIMAL)
    FROM jsonb_array_elements(questions) q
  ) AS total_points
FROM "Quiz";

-- 4. Find quizzes with >10 questions
SELECT quiz_id, title
FROM "Quiz"
WHERE jsonb_array_length(questions) > 10;

-- 5. Search question text (using @@ operator)
SELECT quiz_id, title
FROM "Quiz"
WHERE questions::text ILIKE '%Java%';

-- 6. Get specific question
SELECT
  quiz_id,
  title,
  q AS question
FROM "Quiz",
     jsonb_array_elements(questions) WITH ORDINALITY AS q
WHERE quiz_id = :quiz_id
  AND ordinality = :question_number;  -- 1-indexed
```

**Creating Quiz:**

```python
# Application code (Python + FastAPI)
from pydantic import BaseModel

class QuizOption(BaseModel):
    option_id: str
    option_text: str
    is_correct: bool

class QuizQuestion(BaseModel):
    question_id: str
    question_text: str
    question_type: str  # MULTIPLE_CHOICE, TRUE_FALSE, SHORT_ANSWER
    points: float
    options: list[QuizOption]
    explanation: str | None
    order_num: int

class QuizCreate(BaseModel):
    title: str
    time_limit_minutes: int
    pass_percentage: float
    questions: list[QuizQuestion]

@app.post("/api/quizzes")
def create_quiz(quiz: QuizCreate, user_id: UUID):
    # Validate
    if len(quiz.questions) == 0:
        raise HTTPException(400, "Quiz must have at least 1 question")

    # Convert to JSON
    questions_json = json.dumps([q.dict() for q in quiz.questions])

    # Insert
    result = db.execute("""
        INSERT INTO "Quiz" (lecture_id, title, time_limit_minutes, pass_percentage, questions)
        VALUES (%s, %s, %s, %s, %s::JSONB)
        RETURNING quiz_id
    """, lecture_id, quiz.title, quiz.time_limit_minutes, quiz.pass_percentage, questions_json)

    return {"quiz_id": result[0]['quiz_id']}
```

**Taking Quiz (Student):**

```python
@app.post("/api/quizzes/{quiz_id}/attempts")
def start_quiz(quiz_id: UUID, user_id: UUID):
    # Get quiz
    quiz = db.query("SELECT * FROM Quiz WHERE quiz_id = %s", quiz_id)

    # Create attempt
    attempt_id = db.insert("Attempt", {
        'quiz_id': quiz_id,
        'user_id': user_id,
        'started_at': 'CURRENT_TIMESTAMP',
        'time_limit_minutes': quiz['time_limit_minutes'],
        'answers': json.dumps([])  # Empty initially
    })

    # Return quiz questions (without correct answers!)
    questions = json.loads(quiz['questions'])
    for q in questions:
        for opt in q['options']:
            del opt['is_correct']  # Hide correct answer

    return {
        'attempt_id': attempt_id,
        'questions': questions,
        'time_limit_minutes': quiz['time_limit_minutes']
    }
```

**Submitting Quiz:**

```python
@app.post("/api/attempts/{attempt_id}/submit")
def submit_quiz(attempt_id: UUID, answers: list[dict]):
    # Get attempt
    attempt = db.query("SELECT * FROM Attempt WHERE attempt_id = %s", attempt_id)

    # Get quiz with correct answers
    quiz = db.query("SELECT * FROM Quiz WHERE quiz_id = %s", attempt['quiz_id'])
    questions = json.loads(quiz['questions'])

    # Grade answers
    total_points = 0
    earned_points = 0

    for question in questions:
        total_points += question['points']

        # Find student answer
        student_answer = next((a for a in answers if a['question_id'] == question['question_id']), None)

        if question['question_type'] in ['MULTIPLE_CHOICE', 'TRUE_FALSE']:
            # Auto-grade
            correct_option_id = next((o['option_id'] for o in question['options'] if o['is_correct']), None)
            if student_answer and student_answer['selected_option_id'] == correct_option_id:
                earned_points += question['points']

    # Calculate score
    score_percentage = (earned_points / total_points * 100) if total_points > 0 else 0

    # Update attempt
    db.update("Attempt", attempt_id, {
        'answers': json.dumps(answers),
        'submitted_at': 'CURRENT_TIMESTAMP',
        'score': score_percentage,
        'status': 'GRADED' if score_percentage >= quiz['pass_percentage'] else 'FAILED'
    })

    return {
        'score': score_percentage,
        'passed': score_percentage >= quiz['pass_percentage'],
        'earned_points': earned_points,
        'total_points': total_points
    }
```

**GIN Index for JSONB:**

```sql
-- Create GIN index for fast JSON queries
CREATE INDEX idx_quiz_questions ON "Quiz" USING GIN (questions);

-- Fast query: Find quizzes with MULTIPLE_CHOICE questions
SELECT quiz_id, title
FROM "Quiz"
WHERE questions @> '[{"question_type": "MULTIPLE_CHOICE"}]';
```

**Kết luận:**
- Quiz.questions = JSONB array of question objects
- Simpler than separate Question + Option tables
- Atomic loading (all questions at once)
- Easy quiz editing (replace entire JSON)
- Acceptable trade-offs for LMS use case

---

### Q4.2: Attempt.answers là JSONB - lưu trữ answers của student như thế nào?

**Trả lời:**

**Attempt.answers** là JSONB field lưu trữ tất cả answers của student cho 1 quiz attempt.

**Schema:**

```sql
CREATE TABLE "Attempt" (
  attempt_id UUID PRIMARY KEY,
  quiz_id UUID REFERENCES "Quiz",
  user_id UUID REFERENCES "User",
  started_at TIMESTAMP,
  submitted_at TIMESTAMP,
  time_limit_minutes INT,
  answers JSONB,  -- Student answers
  score DECIMAL(5,2),
  status VARCHAR(20),  -- IN_PROGRESS, SUBMITTED, GRADED
  ...
);
```

**answers JSONB Structure:**

```json
[
  {
    "question_id": "q1-uuid",
    "question_text": "What is Java?",
    "question_type": "MULTIPLE_CHOICE",
    "selected_option_id": "opt1",
    "selected_option_text": "A programming language",
    "is_correct": true,
    "points_earned": 10,
    "points_possible": 10
  },
  {
    "question_id": "q2-uuid",
    "question_text": "Is Java OOP?",
    "question_type": "TRUE_FALSE",
    "selected_option_id": "opt1",
    "selected_option_text": "True",
    "is_correct": true,
    "points_earned": 5,
    "points_possible": 5
  },
  {
    "question_id": "q3-uuid",
    "question_text": "Explain polymorphism",
    "question_type": "SHORT_ANSWER",
    "answer_text": "Polymorphism means objects of different types...",
    "is_correct": null,  // Not auto-graded
    "points_earned": null,  // Graded manually by instructor
    "points_possible": 15,
    "instructor_feedback": null  // Added after manual grading
  }
]
```

**Tại sao lưu answers trong Attempt thay vì bảng riêng?**

**Same reasoning as Quiz.questions:**

**Option 1: Separate Answer table (❌ Rejected)**
```sql
CREATE TABLE "Answer" (
  answer_id UUID PRIMARY KEY,
  attempt_id UUID REFERENCES "Attempt",
  question_id UUID,  -- References Quiz.questions JSON (no FK)
  selected_option_id UUID,
  answer_text TEXT,
  is_correct BOOLEAN,
  points_earned DECIMAL
);

-- Load attempt answers: Need JOIN
SELECT * FROM "Answer" WHERE attempt_id = '...';
```

**Option 2: JSONB answers (✅ Current design)**
```sql
-- 1 table, 1 query
SELECT * FROM "Attempt" WHERE attempt_id = '...';
```

**Benefits:**
- ✅ Atomic: All answers loaded together
- ✅ Fast: No JOINs
- ✅ Simple: 1 table

---

**Quiz Taking Flow:**

**Step 1: Student starts quiz**

```sql
INSERT INTO "Attempt" (quiz_id, user_id, started_at, time_limit_minutes, answers, status)
VALUES (
  :quiz_id,
  :user_id,
  CURRENT_TIMESTAMP,
  45,  -- From Quiz.time_limit_minutes
  '[]'::JSONB,  -- Empty answers initially
  'IN_PROGRESS'
);
```

**Step 2: Student answers questions (live save - optional)**

```sql
-- Update answers as student progresses
UPDATE "Attempt"
SET answers = :answers_json
WHERE attempt_id = :attempt_id;

-- Example: Student answered 2/10 questions
answers = [
  {"question_id": "q1", "selected_option_id": "opt1"},
  {"question_id": "q2", "selected_option_id": "opt2"}
]
```

**Step 3: Student submits quiz**

```sql
-- Finalize answers
UPDATE "Attempt"
SET answers = :final_answers_json,
    submitted_at = CURRENT_TIMESTAMP,
    status = 'SUBMITTED'
WHERE attempt_id = :attempt_id;
```

**Step 4: Auto-grading**

```python
def grade_attempt(attempt_id):
    # Get attempt and quiz
    attempt = db.query("SELECT * FROM Attempt WHERE attempt_id = %s", attempt_id)
    quiz = db.query("SELECT * FROM Quiz WHERE quiz_id = %s", attempt['quiz_id'])

    # Parse JSON
    questions = json.loads(quiz['questions'])
    answers = json.loads(attempt['answers'])

    # Grade each answer
    total_points = 0
    earned_points = 0

    graded_answers = []

    for question in questions:
        total_points += question['points']

        # Find student answer
        student_answer = next((a for a in answers if a['question_id'] == question['question_id']), None)

        if question['question_type'] in ['MULTIPLE_CHOICE', 'TRUE_FALSE']:
            # Auto-grade
            correct_option = next((o for o in question['options'] if o['is_correct']), None)

            is_correct = False
            points_earned_for_question = 0

            if student_answer and student_answer['selected_option_id'] == correct_option['option_id']:
                is_correct = True
                points_earned_for_question = question['points']
                earned_points += points_earned_for_question

            graded_answers.append({
                'question_id': question['question_id'],
                'question_text': question['question_text'],
                'question_type': question['question_type'],
                'selected_option_id': student_answer['selected_option_id'] if student_answer else None,
                'correct_option_id': correct_option['option_id'],
                'is_correct': is_correct,
                'points_earned': points_earned_for_question,
                'points_possible': question['points'],
                'explanation': question.get('explanation')
            })

        elif question['question_type'] == 'SHORT_ANSWER':
            # Manual grading required
            graded_answers.append({
                'question_id': question['question_id'],
                'question_text': question['question_text'],
                'question_type': question['question_type'],
                'answer_text': student_answer.get('answer_text') if student_answer else None,
                'is_correct': None,  // Not graded yet
                'points_earned': None,  // Graded by instructor
                'points_possible': question['points'],
                'instructor_feedback': None
            })

    # Calculate score
    score_percentage = (earned_points / total_points * 100) if total_points > 0 else 0

    # Update attempt
    db.update("Attempt", attempt_id, {
        'answers': json.dumps(graded_answers),
        'score': score_percentage,
        'status': 'GRADED' if all(q['question_type'] != 'SHORT_ANSWER' for q in questions) else 'PENDING_MANUAL_GRADING'
    })

    return {
        'score': score_percentage,
        'passed': score_percentage >= quiz['pass_percentage'],
        'earned_points': earned_points,
        'total_points': total_points
    }
```

**Step 5: Manual grading (for SHORT_ANSWER)**

```python
@app.post("/api/attempts/{attempt_id}/grade-question")
def grade_short_answer(attempt_id: UUID, question_id: str, points_earned: float, feedback: str, user_id: UUID):
    # Check permission (must be instructor)
    if not has_permission(user_id, 'grade_submission'):
        raise HTTPException(403)

    # Get attempt
    attempt = db.query("SELECT * FROM Attempt WHERE attempt_id = %s", attempt_id)
    answers = json.loads(attempt['answers'])

    # Find question answer
    for answer in answers:
        if answer['question_id'] == question_id:
            answer['points_earned'] = points_earned
            answer['is_correct'] = points_earned > 0
            answer['instructor_feedback'] = feedback
            answer['graded_by'] = str(user_id)
            answer['graded_at'] = datetime.utcnow().isoformat()
            break

    # Recalculate score
    total_earned = sum(a.get('points_earned', 0) or 0 for a in answers)
    total_possible = sum(a['points_possible'] for a in answers)
    score_percentage = (total_earned / total_possible * 100) if total_possible > 0 else 0

    # Update attempt
    db.update("Attempt", attempt_id, {
        'answers': json.dumps(answers),
        'score': score_percentage,
        'status': 'GRADED'
    })

    return {'score': score_percentage}
```

**Query Examples:**

```sql
-- 1. Get attempt with answers
SELECT
  a.attempt_id,
  a.submitted_at,
  a.score,
  a.status,
  a.answers
FROM "Attempt" a
WHERE a.attempt_id = :attempt_id;

-- 2. Get all attempts for user+quiz
SELECT
  a.attempt_id,
  a.submitted_at,
  a.score,
  a.status,
  jsonb_array_length(a.answers) AS questions_answered
FROM "Attempt" a
WHERE a.user_id = :user_id
  AND a.quiz_id = :quiz_id
ORDER BY a.started_at DESC;

-- 3. Find attempts with SHORT_ANSWER needing manual grading
SELECT
  a.attempt_id,
  u.email,
  a.submitted_at
FROM "Attempt" a
JOIN "User" u ON a.user_id = u.user_id
WHERE a.status = 'PENDING_MANUAL_GRADING'
  AND EXISTS (
    SELECT 1
    FROM jsonb_array_elements(a.answers) answer
    WHERE answer->>'question_type' = 'SHORT_ANSWER'
      AND answer->>'points_earned' IS NULL
  );

-- 4. Average score for quiz
SELECT
  q.title,
  AVG(a.score) AS average_score,
  COUNT(a.attempt_id) AS total_attempts
FROM "Quiz" q
JOIN "Attempt" a ON q.quiz_id = a.quiz_id
WHERE q.quiz_id = :quiz_id
  AND a.status = 'GRADED'
GROUP BY q.quiz_id, q.title;

-- 5. Questions most students get wrong
WITH question_stats AS (
  SELECT
    answer->>'question_id' AS question_id,
    answer->>'question_text' AS question_text,
    SUM(CASE WHEN (answer->>'is_correct')::BOOLEAN THEN 1 ELSE 0 END) AS correct_count,
    COUNT(*) AS total_count
  FROM "Attempt" a,
       jsonb_array_elements(a.answers) answer
  WHERE a.quiz_id = :quiz_id
    AND a.status = 'GRADED'
    AND answer->>'question_type' IN ('MULTIPLE_CHOICE', 'TRUE_FALSE')
  GROUP BY answer->>'question_id', answer->>'question_text'
)
SELECT
  question_id,
  question_text,
  correct_count,
  total_count,
  ROUND((correct_count::NUMERIC / total_count * 100), 2) AS correct_percentage
FROM question_stats
ORDER BY correct_percentage ASC
LIMIT 10;
```

**Time Limit Enforcement:**

```sql
-- Check if attempt exceeded time limit
SELECT
  attempt_id,
  started_at,
  submitted_at,
  time_limit_minutes,
  EXTRACT(EPOCH FROM (submitted_at - started_at)) / 60 AS actual_minutes,
  CASE
    WHEN submitted_at IS NULL THEN FALSE
    WHEN EXTRACT(EPOCH FROM (submitted_at - started_at)) / 60 <= time_limit_minutes THEN TRUE
    ELSE FALSE
  END AS submitted_on_time
FROM "Attempt"
WHERE attempt_id = :attempt_id;

-- Auto-submit expired attempts (cron job)
UPDATE "Attempt"
SET submitted_at = started_at + (time_limit_minutes || ' minutes')::INTERVAL,
    status = 'SUBMITTED'
WHERE status = 'IN_PROGRESS'
  AND started_at + (time_limit_minutes || ' minutes')::INTERVAL < CURRENT_TIMESTAMP;
```

**Kết luận:**
- Attempt.answers = JSONB array of answer objects
- Includes student selections + grading results
- Auto-graded for MULTIPLE_CHOICE, TRUE_FALSE
- Manual grading for SHORT_ANSWER
- All attempt data in 1 row (atomic, fast)

---

I'll continue generating the FAQ content. Would you like me to proceed with the remaining sections (Q4.3 onwards), or would you like to review what we have so far first?
