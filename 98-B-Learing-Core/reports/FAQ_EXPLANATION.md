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

### Q4.3: AssignmentSubmission vs Attempt - khác nhau thế nào?

**Trả lời:**

**AssignmentSubmission** và **Attempt** là 2 bảng khác nhau cho 2 types of assessment:

| Aspect | AssignmentSubmission | Attempt |
|--------|---------------------|---------|
| **Purpose** | Student submissions cho assignments | Student quiz attempts |
| **Parent** | Lecture (type='ASSIGNMENT') | Quiz |
| **Content** | Files + text submission | Selected answers (JSONB) |
| **Grading** | Manual (instructor reviews) | Auto-graded (+ manual for short answer) |
| **Multiple Submissions** | Yes (submission_number) | Yes (attempt_number) |
| **Time Limit** | Deadline (due_date) | Time limit (e.g., 60 minutes) |

**AssignmentSubmission Schema:**

```sql
CREATE TABLE "AssignmentSubmission" (
  submission_id UUID PRIMARY KEY,
  lecture_id UUID REFERENCES "Lecture",  -- Assignment lecture
  user_id UUID REFERENCES "User",
  submission_number INT DEFAULT 1,  -- 1st, 2nd, 3rd submission
  submitted_content TEXT,  -- Text submission
  file_urls TEXT[],  -- Array of uploaded file URLs
  submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  score DECIMAL(5,2),
  feedback TEXT,  -- Instructor feedback
  graded_at TIMESTAMP,
  graded_by UUID REFERENCES "User",
  status VARCHAR(20) DEFAULT 'SUBMITTED'  -- SUBMITTED, GRADED, LATE
);
```

**Attempt Schema:**

```sql
CREATE TABLE "Attempt" (
  attempt_id UUID PRIMARY KEY,
  quiz_id UUID REFERENCES "Quiz",
  user_id UUID REFERENCES "User",
  attempt_number INT DEFAULT 1,  -- 1st, 2nd, 3rd attempt
  started_at TIMESTAMP,
  submitted_at TIMESTAMP,
  time_limit_minutes INT,
  answers JSONB,  -- Student answers
  score DECIMAL(5,2),
  status VARCHAR(20)  -- IN_PROGRESS, SUBMITTED, GRADED
);
```

**Use Cases:**

**Assignment Example:**
```
Lecture: "Java Project - Create a Calculator"
Type: ASSIGNMENT
assignment_config: {
  "due_date": "2025-12-15T23:59:59Z",
  "max_score": 100,
  "submission_types": ["FILE"],
  "allowed_file_types": [".zip", ".java"],
  "max_attempts": 3
}

Student Submission:
- submission_number: 1
- file_urls: ["https://s3.../calculator.zip"]
- submitted_content: "Here is my calculator project..."
- submitted_at: 2025-12-10 20:30:00

Instructor Grading:
- score: 85
- feedback: "Good work! Consider adding error handling for division by zero."
- graded_at: 2025-12-12 10:00:00
```

**Quiz Example:**
```
Quiz: "Java OOP Concepts Quiz"
questions: [10 MULTIPLE_CHOICE questions]
time_limit_minutes: 45
pass_percentage: 70

Student Attempt:
- attempt_number: 1
- started_at: 2025-12-10 14:00:00
- submitted_at: 2025-12-10 14:35:00
- answers: [{"question_id": "q1", "selected_option_id": "opt1", "is_correct": true, ...}, ...]
- score: 85 (auto-graded)
- status: GRADED
```

**Multiple Submissions/Attempts:**

**Assignment:**
```sql
-- Student can resubmit (if max_attempts allows)
INSERT INTO "AssignmentSubmission" (lecture_id, user_id, submission_number, ...)
VALUES (:lecture_id, :user_id, 2, ...);  -- 2nd submission

-- Get latest submission
SELECT *
FROM "AssignmentSubmission"
WHERE lecture_id = :lecture_id
  AND user_id = :user_id
ORDER BY submission_number DESC
LIMIT 1;

-- Get best submission
SELECT *
FROM "AssignmentSubmission"
WHERE lecture_id = :lecture_id
  AND user_id = :user_id
ORDER BY score DESC
LIMIT 1;
```

**Quiz:**
```sql
-- Student can retake quiz (if max_attempts allows)
INSERT INTO "Attempt" (quiz_id, user_id, attempt_number, ...)
VALUES (:quiz_id, :user_id, 2, ...);  -- 2nd attempt

-- Get best attempt
SELECT *
FROM "Attempt"
WHERE quiz_id = :quiz_id
  AND user_id = :user_id
  AND status = 'GRADED'
ORDER BY score DESC
LIMIT 1;
```

**Kết luận:**
- AssignmentSubmission = manual grading, file uploads
- Attempt = auto-grading, selected answers
- Both support multiple submissions/attempts
- Different workflows, different tables

---

### Q4.4: Làm sao để enforce max_attempts cho assignments và quizzes?

**Trả lời:**

Max attempts được enforce tại **application level** (không enforce ở database level).

**Assignment max_attempts:**

Stored in `Lecture.assignment_config` JSON:

```json
{
  "max_attempts": 3
}
```

**Application Logic:**

```python
@app.post("/api/assignments/{lecture_id}/submit")
def submit_assignment(lecture_id: UUID, submission_data: dict, user_id: UUID):
    # Get assignment config
    lecture = db.query("SELECT assignment_config FROM Lecture WHERE lecture_id = %s", lecture_id)
    config = json.loads(lecture['assignment_config'])
    max_attempts = config.get('max_attempts', 999)  # Default unlimited

    # Count existing submissions
    submission_count = db.query("""
        SELECT COUNT(*) as count
        FROM "AssignmentSubmission"
        WHERE lecture_id = %s AND user_id = %s
    """, lecture_id, user_id)[0]['count']

    # Check if exceeded
    if submission_count >= max_attempts:
        raise HTTPException(403, f"Maximum {max_attempts} submissions allowed")

    # Create submission
    next_number = submission_count + 1
    db.insert("AssignmentSubmission", {
        'lecture_id': lecture_id,
        'user_id': user_id,
        'submission_number': next_number,
        'submitted_content': submission_data['content'],
        'file_urls': submission_data['file_urls'],
        'status': 'SUBMITTED'
    })

    return {'submission_number': next_number, 'remaining_attempts': max_attempts - next_number}
```

**Quiz max_attempts:**

Stored in `Quiz.max_attempts`:

```sql
CREATE TABLE "Quiz" (
  quiz_id UUID PRIMARY KEY,
  title VARCHAR(200),
  max_attempts INT DEFAULT 3,
  ...
);
```

**Application Logic:**

```python
@app.post("/api/quizzes/{quiz_id}/attempts")
def start_quiz_attempt(quiz_id: UUID, user_id: UUID):
    # Get quiz
    quiz = db.query("SELECT max_attempts FROM Quiz WHERE quiz_id = %s", quiz_id)
    max_attempts = quiz['max_attempts']

    # Count existing attempts
    attempt_count = db.query("""
        SELECT COUNT(*) as count
        FROM "Attempt"
        WHERE quiz_id = %s AND user_id = %s
    """, quiz_id, user_id)[0]['count']

    # Check if exceeded
    if attempt_count >= max_attempts:
        raise HTTPException(403, f"Maximum {max_attempts} attempts allowed")

    # Create new attempt
    attempt_id = db.insert("Attempt", {
        'quiz_id': quiz_id,
        'user_id': user_id,
        'attempt_number': attempt_count + 1,
        'started_at': 'CURRENT_TIMESTAMP',
        'status': 'IN_PROGRESS'
    })

    return {'attempt_id': attempt_id, 'attempt_number': attempt_count + 1, 'remaining_attempts': max_attempts - attempt_count - 1}
```

**Database Constraint (Optional - strict enforcement):**

```sql
-- Could add CHECK constraint (but less flexible)
ALTER TABLE "AssignmentSubmission"
ADD CONSTRAINT chk_max_submissions
CHECK (
  submission_number <= (
    SELECT (assignment_config->>'max_attempts')::INT
    FROM "Lecture"
    WHERE lecture_id = "AssignmentSubmission".lecture_id
  )
);

-- Problem: Less flexible (requires SQL function or trigger)
-- Better: Handle at application level
```

**UI Display:**

```
Assignment: Java Calculator Project
Submission Status: 2/3 attempts used
[Submit Assignment] button
Remaining attempts: 1
```

**Kết luận:**
- max_attempts enforced at application level
- Count existing submissions/attempts
- Reject if exceeded
- More flexible than database constraint

---

## 5. DOMAIN 4: ENROLLMENT & PROGRESS

### Q5.1: Enrollment.class_id nullable - ý nghĩa gì?

**Trả lời:**

**Enrollment.class_id** nullable để support **2 learning modes**:

**1. Self-paced learning (class_id = NULL):**

Student enrolls in course, học theo tốc độ riêng, không có schedule/class

```sql
INSERT INTO "Enrollment" (user_id, course_id, class_id, status)
VALUES (
  :user_id,
  :course_id,
  NULL,  -- Self-paced (no class)
  'ACTIVE'
);
```

**Use case:**
- MOOCs (Massive Open Online Courses)
- Self-study courses
- On-demand training

**Characteristics:**
- No schedule (học bất kỳ lúc nào)
- No instructor-led sessions
- No attendance tracking
- Individual progress

**2. Class-based learning (class_id = UUID):**

Student enrolls in specific class với schedule, instructor, in-person/online sessions

```sql
INSERT INTO "Enrollment" (user_id, course_id, class_id, status)
VALUES (
  :user_id,
  :course_id,
  'class-uuid',  -- Specific class
  'ACTIVE'
);
```

**Use case:**
- University courses (CS101 - Spring 2025)
- Corporate training (Java Bootcamp - Jan cohort)
- Blended learning (online + in-person)

**Characteristics:**
- Fixed schedule (Mon/Wed/Fri 2pm-4pm)
- Instructor-led
- Attendance tracking
- Cohort-based (học cùng classmates)

**Schema:**

```sql
CREATE TABLE "Enrollment" (
  enrollment_id UUID PRIMARY KEY,
  user_id UUID REFERENCES "User",
  course_id UUID REFERENCES "Course",
  class_id UUID REFERENCES "Class",  -- NULLABLE!
  enrolled_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  status VARCHAR(20) DEFAULT 'ACTIVE',
  ...
);

CREATE TABLE "Class" (
  class_id UUID PRIMARY KEY,
  course_id UUID REFERENCES "Course",
  class_name VARCHAR(100),  -- "CS101 - Spring 2025"
  instructor_id UUID REFERENCES "User",
  start_date DATE,
  end_date DATE,
  schedules JSONB,  -- Weekly schedule
  max_students INT,
  status VARCHAR(20)
);
```

**Comparison:**

| Aspect | Self-paced (class_id=NULL) | Class-based (class_id=UUID) |
|--------|----------------------------|----------------------------|
| **Schedule** | Flexible | Fixed (e.g., Mon/Wed 2pm) |
| **Instructor** | N/A | Assigned instructor |
| **Start/End** | Anytime | Specific dates |
| **Attendance** | N/A | Tracked |
| **Interaction** | Forums only | Live sessions + forums |
| **Pacing** | Individual | Cohort (everyone together) |

**Query Examples:**

```sql
-- 1. Get all enrollments for user
SELECT
  e.enrollment_id,
  c.title AS course_title,
  cl.class_name,
  CASE
    WHEN e.class_id IS NULL THEN 'Self-paced'
    ELSE 'Class-based'
  END AS learning_mode
FROM "Enrollment" e
JOIN "Course" c ON e.course_id = c.course_id
LEFT JOIN "Class" cl ON e.class_id = cl.class_id
WHERE e.user_id = :user_id;

-- 2. Get all self-paced enrollments
SELECT e.*, c.title
FROM "Enrollment" e
JOIN "Course" c ON e.course_id = c.course_id
WHERE e.class_id IS NULL;

-- 3. Get all enrollments in a specific class
SELECT e.*, u.email, u.first_name, u.last_name
FROM "Enrollment" e
JOIN "User" u ON e.user_id = u.user_id
WHERE e.class_id = :class_id;

-- 4. Count enrollments by mode
SELECT
  COUNT(*) FILTER (WHERE class_id IS NULL) AS self_paced_count,
  COUNT(*) FILTER (WHERE class_id IS NOT NULL) AS class_based_count
FROM "Enrollment"
WHERE course_id = :course_id;
```

**Business Rules:**

```python
def enroll_student(user_id: UUID, course_id: UUID, class_id: UUID | None = None):
    # If class_id provided, check if class is open and has capacity
    if class_id:
        cls = db.query("SELECT * FROM Class WHERE class_id = %s", class_id)

        # Check status
        if cls['status'] != 'OPEN':
            raise HTTPException(400, "Class is not open for enrollment")

        # Check capacity
        enrolled_count = db.query("""
            SELECT COUNT(*) as count
            FROM "Enrollment"
            WHERE class_id = %s
        """, class_id)[0]['count']

        if enrolled_count >= cls['max_students']:
            raise HTTPException(400, "Class is full")

    # Create enrollment
    db.insert("Enrollment", {
        'user_id': user_id,
        'course_id': course_id,
        'class_id': class_id,  # Can be NULL
        'status': 'ACTIVE'
    })

    return {'success': True}
```

**UI Flow:**

**Self-paced:**
```
Course: "Introduction to Java"
Enrollment Type: Self-paced
[Enroll Now] → Immediate access
```

**Class-based:**
```
Course: "Introduction to Java"

Available Classes:
  ✅ CS101 - Spring 2025 (Mon/Wed 2-4pm) - 15/30 students
  ✅ CS101 - Evening (Tue/Thu 6-8pm) - 8/25 students
  🔒 CS101 - Summer 2025 (Enrollment opens Mar 1)

[Select Class] → [Enroll]
```

**Kết luận:**
- class_id nullable = flexible enrollment
- NULL = self-paced learning
- UUID = class-based learning with schedule
- Same course, different modes

---

### Q5.2: Progress table track gì? Tại sao không track quiz/assignment progress?

**Trả lời:**

**Progress** table tracks **lecture completion** (module-level progress).

**Schema:**

```sql
CREATE TABLE "Progress" (
  progress_id UUID PRIMARY KEY,
  user_id UUID REFERENCES "User",
  enrollment_id UUID REFERENCES "Enrollment",
  module_id UUID REFERENCES "Module",
  completed_lectures INT DEFAULT 0,  -- Số lectures đã complete
  total_lectures INT,  -- Tổng số lectures trong module
  percentage DECIMAL(5,2),  -- % complete (calculated)
  last_accessed_at TIMESTAMP,
  completed_at TIMESTAMP,  -- When module completed
  status VARCHAR(20) DEFAULT 'IN_PROGRESS'  -- IN_PROGRESS, COMPLETED
);
```

**What Progress tracks:**

✅ **Lectures watched/completed:**
- Video lectures watched
- Reading materials read
- Content consumed

❌ **What Progress does NOT track:**
- Quiz attempts (tracked in Attempt table)
- Assignment submissions (tracked in AssignmentSubmission table)

**Why separate tracking?**

**1. Different semantics:**

| Type | Progress Meaning | Completion Criteria |
|------|------------------|---------------------|
| **Lecture** | Watched/read | Viewed content (passive) |
| **Quiz** | Taken + passed | Score ≥ pass_percentage (active) |
| **Assignment** | Submitted + graded | Score ≥ pass_score (active) |

**2. Different tables:**

```
Progress table:
- Tracks: Lectures (VIDEO, READING)
- Granularity: Module-level
- Purpose: Course completion percentage

Attempt table:
- Tracks: Quizzes
- Granularity: Quiz-level (individual attempts)
- Purpose: Assessment scores

AssignmentSubmission table:
- Tracks: Assignments
- Granularity: Assignment-level (individual submissions)
- Purpose: Graded work
```

**3. Aggregation for overall progress:**

```sql
-- Calculate overall course progress (all types)
WITH lecture_progress AS (
  -- Lectures completed
  SELECT
    e.enrollment_id,
    COUNT(DISTINCT l.lecture_id) FILTER (WHERE l.type IN ('VIDEO', 'READING')) AS total_lectures,
    COUNT(DISTINCT p.module_id) AS completed_modules  -- Simplified
  FROM "Enrollment" e
  JOIN "Course" c ON e.course_id = c.course_id
  JOIN "Module" m ON c.course_id = m.course_id
  JOIN "Lecture" l ON m.module_id = l.module_id
  LEFT JOIN "Progress" p ON m.module_id = p.module_id AND p.user_id = e.user_id
  WHERE e.enrollment_id = :enrollment_id
  GROUP BY e.enrollment_id
),
quiz_progress AS (
  -- Quizzes passed
  SELECT
    e.enrollment_id,
    COUNT(DISTINCT q.quiz_id) AS total_quizzes,
    COUNT(DISTINCT a.quiz_id) FILTER (WHERE a.status = 'GRADED' AND a.score >= q.pass_percentage) AS passed_quizzes
  FROM "Enrollment" e
  JOIN "Course" c ON e.course_id = c.course_id
  JOIN "Module" m ON c.course_id = m.course_id
  JOIN "Lecture" l ON m.module_id = l.module_id
  JOIN "Quiz" q ON l.quiz_id = q.quiz_id
  LEFT JOIN "Attempt" a ON q.quiz_id = a.quiz_id AND a.user_id = e.user_id
  WHERE e.enrollment_id = :enrollment_id
  GROUP BY e.enrollment_id
),
assignment_progress AS (
  -- Assignments graded
  SELECT
    e.enrollment_id,
    COUNT(DISTINCT l.lecture_id) FILTER (WHERE l.type = 'ASSIGNMENT') AS total_assignments,
    COUNT(DISTINCT s.lecture_id) FILTER (WHERE s.status = 'GRADED') AS graded_assignments
  FROM "Enrollment" e
  JOIN "Course" c ON e.course_id = c.course_id
  JOIN "Module" m ON c.course_id = m.course_id
  JOIN "Lecture" l ON m.module_id = l.module_id
  LEFT JOIN "AssignmentSubmission" s ON l.lecture_id = s.lecture_id AND s.user_id = e.user_id
  WHERE e.enrollment_id = :enrollment_id
  GROUP BY e.enrollment_id
)
SELECT
  lp.total_lectures,
  lp.completed_modules,
  qp.passed_quizzes || '/' || qp.total_quizzes AS quiz_progress,
  ap.graded_assignments || '/' || ap.total_assignments AS assignment_progress,
  ROUND(
    (
      (lp.completed_modules::DECIMAL / NULLIF(lp.total_lectures, 0) * 40) +
      (qp.passed_quizzes::DECIMAL / NULLIF(qp.total_quizzes, 0) * 30) +
      (ap.graded_assignments::DECIMAL / NULLIF(ap.total_assignments, 0) * 30)
    ),
    2
  ) AS overall_progress_percentage
FROM lecture_progress lp, quiz_progress qp, assignment_progress ap;
```

**Simplified Progress Calculation (Core v1.0):**

```sql
-- Module progress
UPDATE "Progress"
SET completed_lectures = (
      SELECT COUNT(*)
      FROM "Lecture" l
      WHERE l.module_id = :module_id
        AND l.type IN ('VIDEO', 'READING')
        -- Assume completed if user accessed (simplified)
    ),
    percentage = (completed_lectures::DECIMAL / total_lectures * 100)
WHERE progress_id = :progress_id;

-- Enrollment overall progress
UPDATE "Enrollment"
SET completion_percentage = (
  SELECT AVG(p.percentage)
  FROM "Progress" p
  WHERE p.enrollment_id = :enrollment_id
)
WHERE enrollment_id = :enrollment_id;
```

**UI Display:**

```
Course Progress Dashboard:

📚 Lectures: 15/20 completed (75%)
  ✅ Module 1: Introduction (100%)
  ✅ Module 2: Basics (100%)
  🔄 Module 3: Advanced (50%)
  ⬜ Module 4: Final Project (0%)

📝 Quizzes: 2/3 passed (67%)
  ✅ Quiz 1: Basics (Score: 85%)
  ✅ Quiz 2: OOP (Score: 90%)
  ❌ Quiz 3: Advanced (Score: 55% - Retake required)

📄 Assignments: 2/3 graded (67%)
  ✅ Assignment 1: Calculator (Score: 85/100)
  ✅ Assignment 2: OOP Project (Score: 90/100)
  ⏳ Assignment 3: Final Project (Submitted, pending grading)

Overall Progress: 70%
Certificate: Not yet eligible (need 80%)
```

**Kết luận:**
- Progress = lecture completion tracking
- Quiz/Assignment tracked separately (Attempt, AssignmentSubmission)
- Overall progress = aggregate of all types
- Module-level granularity (simpler than lecture-level)

---

### Q5.3: Enrollment.completion_percentage vs Progress.percentage - khác nhau thế nào?

**Trả lời:**

**Two different levels of completion tracking:**

**1. Progress.percentage (Module-level):**

Tracks completion percentage **within a module**

```sql
CREATE TABLE "Progress" (
  progress_id UUID PRIMARY KEY,
  module_id UUID REFERENCES "Module",  -- Specific module
  user_id UUID REFERENCES "User",
  completed_lectures INT,
  total_lectures INT,
  percentage DECIMAL(5,2),  -- % complete for THIS MODULE
  ...
);

-- Example: Module 2 progress
module_id = "module-2-uuid"
completed_lectures = 3
total_lectures = 5
percentage = 60.00  -- 3/5 * 100
```

**2. Enrollment.completion_percentage (Course-level):**

Tracks completion percentage **for entire course** (aggregate of all modules)

```sql
CREATE TABLE "Enrollment" (
  enrollment_id UUID PRIMARY KEY,
  course_id UUID REFERENCES "Course",  -- Entire course
  user_id UUID REFERENCES "User",
  completion_percentage DECIMAL(5,2),  -- % complete for ENTIRE COURSE
  ...
);

-- Example: Course progress
completion_percentage = 45.00  -- Average/weighted average of all modules
```

**Hierarchy:**

```
Course (Enrollment.completion_percentage = 45%)
  ├── Module 1 (Progress.percentage = 100%)
  ├── Module 2 (Progress.percentage = 60%)
  ├── Module 3 (Progress.percentage = 20%)
  └── Module 4 (Progress.percentage = 0%)

Course completion = Average(100, 60, 20, 0) = 45%
```

**Calculation:**

**Option 1: Simple Average**

```sql
-- Calculate course completion as average of module progress
UPDATE "Enrollment"
SET completion_percentage = (
  SELECT AVG(p.percentage)
  FROM "Progress" p
  WHERE p.enrollment_id = :enrollment_id
)
WHERE enrollment_id = :enrollment_id;
```

**Option 2: Weighted Average (based on module size)**

```sql
-- Weighted by number of lectures in each module
UPDATE "Enrollment"
SET completion_percentage = (
  SELECT
    SUM(p.percentage * p.total_lectures) / SUM(p.total_lectures)
  FROM "Progress" p
  WHERE p.enrollment_id = :enrollment_id
)
WHERE enrollment_id = :enrollment_id;

-- Example:
-- Module 1: 100% * 3 lectures = 300
-- Module 2:  60% * 5 lectures = 300
-- Module 3:  20% * 2 lectures =  40
-- Module 4:   0% * 10 lectures = 0
-- Total: 640 / 20 lectures = 32%
```

**When to update:**

**Progress.percentage updated:**
- When student completes a lecture
- Real-time (every lecture completion)

**Enrollment.completion_percentage updated:**
- After Progress.percentage changes
- Can be calculated on-demand (not stored) or cached

**Query Examples:**

```sql
-- 1. Get course progress with module breakdown
SELECT
  e.enrollment_id,
  e.completion_percentage AS course_progress,
  json_agg(json_build_object(
    'module_id', m.module_id,
    'module_title', m.title,
    'progress_percentage', COALESCE(p.percentage, 0),
    'completed_lectures', COALESCE(p.completed_lectures, 0),
    'total_lectures', COALESCE(p.total_lectures, 0)
  ) ORDER BY m.order_num) AS module_progress
FROM "Enrollment" e
JOIN "Course" c ON e.course_id = c.course_id
JOIN "Module" m ON c.course_id = m.course_id
LEFT JOIN "Progress" p ON m.module_id = p.module_id AND p.enrollment_id = e.enrollment_id
WHERE e.enrollment_id = :enrollment_id
GROUP BY e.enrollment_id, e.completion_percentage;

-- 2. Find students who completed >80% of course
SELECT u.email, e.completion_percentage
FROM "Enrollment" e
JOIN "User" u ON e.user_id = u.user_id
WHERE e.course_id = :course_id
  AND e.completion_percentage >= 80
ORDER BY e.completion_percentage DESC;

-- 3. Module with lowest average completion
SELECT
  m.title AS module_title,
  AVG(p.percentage) AS avg_completion
FROM "Module" m
JOIN "Progress" p ON m.module_id = p.module_id
WHERE m.course_id = :course_id
GROUP BY m.module_id, m.title
ORDER BY avg_completion ASC
LIMIT 1;

-- Result: "Module 3: Advanced Concepts" has 35% avg completion
-- → Students struggling with this module
```

**Application Logic:**

```python
def update_course_progress(enrollment_id: UUID):
    # Recalculate course completion
    result = db.query("""
        UPDATE "Enrollment"
        SET completion_percentage = (
          SELECT COALESCE(AVG(p.percentage), 0)
          FROM "Progress" p
          WHERE p.enrollment_id = %s
        )
        WHERE enrollment_id = %s
        RETURNING completion_percentage
    """, enrollment_id, enrollment_id)

    new_percentage = result[0]['completion_percentage']

    # Check if eligible for certificate (e.g., 80% threshold)
    if new_percentage >= 80:
        check_certificate_eligibility(enrollment_id)

    return new_percentage

def mark_lecture_completed(user_id: UUID, lecture_id: UUID):
    # Get enrollment and module
    lecture = db.query("SELECT module_id FROM Lecture WHERE lecture_id = %s", lecture_id)
    module_id = lecture['module_id']

    enrollment = db.query("""
        SELECT e.enrollment_id
        FROM "Enrollment" e
        JOIN "Course" c ON e.course_id = c.course_id
        JOIN "Module" m ON c.course_id = m.course_id
        WHERE m.module_id = %s AND e.user_id = %s
    """, module_id, user_id)

    # Update progress
    db.execute("""
        INSERT INTO "Progress" (enrollment_id, module_id, user_id, completed_lectures, total_lectures)
        VALUES (%s, %s, %s,
          (SELECT COUNT(*) FROM Lecture WHERE module_id = %s AND type IN ('VIDEO', 'READING')),  -- Assume all completed
          (SELECT COUNT(*) FROM Lecture WHERE module_id = %s AND type IN ('VIDEO', 'READING'))
        )
        ON CONFLICT (enrollment_id, module_id)
        DO UPDATE SET
          completed_lectures = EXCLUDED.completed_lectures,
          percentage = (EXCLUDED.completed_lectures::DECIMAL / EXCLUDED.total_lectures * 100),
          last_accessed_at = CURRENT_TIMESTAMP
    """, enrollment['enrollment_id'], module_id, user_id, module_id, module_id)

    # Recalculate course progress
    update_course_progress(enrollment['enrollment_id'])
```

**Kết luận:**
- Progress.percentage = module-level (per module)
- Enrollment.completion_percentage = course-level (aggregate)
- Course completion = average/weighted average of modules
- Update course progress when module progress changes

---

## 6. DOMAIN 5: CLASS & CERTIFICATE

### Q6.1: Class.schedules JSONB - cấu trúc như thế nào?

**Trả lời:**

**Class.schedules** lưu trữ weekly schedule cho blended learning classes.

**Schema:**

```sql
CREATE TABLE "Class" (
  class_id UUID PRIMARY KEY,
  course_id UUID REFERENCES "Course",
  class_name VARCHAR(100),  -- "CS101 - Spring 2025"
  instructor_id UUID REFERENCES "User",
  start_date DATE,
  end_date DATE,
  schedules JSONB,  -- Weekly schedule
  max_students INT,
  status VARCHAR(20) DEFAULT 'DRAFT'
);
```

**schedules JSONB Structure:**

```json
[
  {
    "day": "MONDAY",
    "start_time": "14:00",
    "end_time": "16:00",
    "location": "Room 301",
    "session_type": "LECTURE",
    "online_meeting_url": null
  },
  {
    "day": "WEDNESDAY",
    "start_time": "14:00",
    "end_time": "16:00",
    "location": "Room 301",
    "session_type": "LECTURE",
    "online_meeting_url": null
  },
  {
    "day": "FRIDAY",
    "start_time": "15:00",
    "end_time": "17:00",
    "location": "Lab 205",
    "session_type": "LAB",
    "online_meeting_url": null
  }
]
```

**For online/hybrid classes:**

```json
[
  {
    "day": "TUESDAY",
    "start_time": "18:00",
    "end_time": "20:00",
    "location": "Online",
    "session_type": "LECTURE",
    "online_meeting_url": "https://zoom.us/j/123456789"
  },
  {
    "day": "THURSDAY",
    "start_time": "18:00",
    "end_time": "20:00",
    "location": "Room 401",
    "session_type": "WORKSHOP",
    "online_meeting_url": null
  }
]
```

**Fields:**

- **day:** MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
- **start_time:** HH:MM (24-hour format)
- **end_time:** HH:MM
- **location:** Physical location or "Online"
- **session_type:** LECTURE, LAB, WORKSHOP, DISCUSSION, EXAM
- **online_meeting_url:** Zoom/Teams/Meet link (nullable)

**Why JSONB instead of separate Schedule table?**

**Option 1: Separate Schedule table (❌ Rejected)**

```sql
CREATE TABLE "ClassSchedule" (
  schedule_id UUID PRIMARY KEY,
  class_id UUID REFERENCES "Class",
  day VARCHAR(10),
  start_time TIME,
  end_time TIME,
  location VARCHAR(200),
  session_type VARCHAR(20)
);

-- Need JOIN to get schedules
SELECT * FROM "ClassSchedule" WHERE class_id = '...';
```

**Option 2: JSONB schedules (✅ Current design)**

```sql
-- Single query, no JOIN
SELECT schedules FROM "Class" WHERE class_id = '...';
```

**Benefits:**
- ✅ Simple: 1 table, 1 query
- ✅ Atomic: All schedules loaded together
- ✅ Flexible: Easy to add fields (recording_url, notes)

**Query Examples:**

```sql
-- 1. Get class schedule
SELECT
  class_name,
  schedules
FROM "Class"
WHERE class_id = :class_id;

-- 2. Find classes on Monday
SELECT class_id, class_name
FROM "Class"
WHERE schedules @> '[{"day": "MONDAY"}]';

-- 3. Find online classes
SELECT class_id, class_name
FROM "Class"
WHERE schedules::text ILIKE '%"location": "Online"%';

-- 4. Expand schedule for display
SELECT
  c.class_name,
  s->>'day' AS day,
  s->>'start_time' AS start_time,
  s->>'end_time' AS end_time,
  s->>'location' AS location,
  s->>'session_type' AS session_type
FROM "Class" c,
     jsonb_array_elements(c.schedules) s
WHERE c.class_id = :class_id
ORDER BY
  CASE s->>'day'
    WHEN 'MONDAY' THEN 1
    WHEN 'TUESDAY' THEN 2
    WHEN 'WEDNESDAY' THEN 3
    WHEN 'THURSDAY' THEN 4
    WHEN 'FRIDAY' THEN 5
    WHEN 'SATURDAY' THEN 6
    WHEN 'SUNDAY' THEN 7
  END,
  s->>'start_time';
```

**UI Display:**

```
Class: CS101 - Spring 2025
Instructor: Dr. Jane Smith
Duration: Jan 15, 2025 - May 15, 2025
Enrolled: 25/30 students

Weekly Schedule:
  📅 Monday 2:00 PM - 4:00 PM
     📍 Room 301
     🎓 Lecture

  📅 Wednesday 2:00 PM - 4:00 PM
     📍 Room 301
     🎓 Lecture

  📅 Friday 3:00 PM - 5:00 PM
     📍 Lab 205
     🔬 Lab Session

[Enroll in Class]
```

**Attendance Tracking (optional - future):**

Could add `attendance` JSONB field:

```json
{
  "2025-01-15": {
    "session_type": "LECTURE",
    "attendees": ["user-uuid-1", "user-uuid-2", ...],
    "absences": ["user-uuid-5"]
  },
  "2025-01-17": {
    "session_type": "LECTURE",
    "attendees": [...],
    "absences": [...]
  }
}
```

**Kết luận:**
- schedules JSONB = weekly class schedule
- Flexible, simple (no separate table)
- Supports in-person, online, hybrid modes
- Easy to query and display

---

### Q6.2: Certificate có trigger tự động tạo không?

**Trả lời:**

Certificate **KHÔNG** tự động tạo bởi database trigger. Tạo bởi **application logic** khi student meets requirements.

**Why not trigger?**

**❌ Database trigger approach:**

```sql
-- BAD: Trigger on Enrollment update
CREATE TRIGGER auto_issue_certificate
AFTER UPDATE ON "Enrollment"
FOR EACH ROW
WHEN (NEW.completion_percentage >= 80 AND NEW.status = 'COMPLETED')
EXECUTE FUNCTION issue_certificate_func();

CREATE FUNCTION issue_certificate_func()
RETURNS TRIGGER AS $$
BEGIN
  INSERT INTO "Certificate" (user_id, course_id, enrollment_id, ...)
  VALUES (NEW.user_id, NEW.course_id, NEW.enrollment_id, ...);
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;
```

**Problems:**
- ❌ Business logic in database (hard to test, maintain)
- ❌ Can't send email notification from trigger
- ❌ Can't generate PDF certificate file
- ❌ Can't check additional requirements (payment, etc.)
- ❌ Harder to debug

**✅ Application logic approach (Current):**

```python
@app.post("/api/enrollments/{enrollment_id}/complete")
def complete_course(enrollment_id: UUID, user_id: UUID):
    # Get enrollment
    enrollment = db.query("SELECT * FROM Enrollment WHERE enrollment_id = %s", enrollment_id)

    # Check completion requirements
    if enrollment['completion_percentage'] < 80:
        raise HTTPException(400, "Must complete at least 80% of course")

    # Additional checks (optional)
    # - All quizzes passed?
    # - All assignments graded?
    # - Payment completed? (if paid course)
    # - No violations/plagiarism?

    # Update enrollment status
    db.update("Enrollment", enrollment_id, {
        'status': 'COMPLETED',
        'completed_at': 'CURRENT_TIMESTAMP'
    })

    # Issue certificate
    certificate_id = issue_certificate(enrollment)

    # Send notification email
    send_certificate_email(user_id, certificate_id)

    return {'certificate_id': certificate_id}

def issue_certificate(enrollment):
    # Generate certificate number (unique)
    cert_number = generate_certificate_number()

    # Create certificate record
    cert_id = db.insert("Certificate", {
        'user_id': enrollment['user_id'],
        'course_id': enrollment['course_id'],
        'enrollment_id': enrollment['enrollment_id'],
        'certificate_number': cert_number,
        'issued_at': 'CURRENT_TIMESTAMP',
        'valid': True
    })

    # Generate PDF certificate file (async job)
    generate_certificate_pdf.delay(cert_id)

    return cert_id
```

**Certificate Schema:**

```sql
CREATE TABLE "Certificate" (
  certificate_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID NOT NULL REFERENCES "User"(user_id),
  course_id UUID NOT NULL REFERENCES "Course"(course_id),
  enrollment_id UUID NOT NULL REFERENCES "Enrollment"(enrollment_id),
  certificate_number VARCHAR(50) NOT NULL UNIQUE,  -- "CERT-2025-001234"
  issued_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  certificate_url VARCHAR(500),  -- PDF file URL
  valid BOOLEAN DEFAULT TRUE,  -- Can be revoked
  revoked_at TIMESTAMP,
  revoked_reason TEXT
);
```

**Certificate Number Generation:**

```python
def generate_certificate_number():
    # Format: CERT-YYYY-NNNNNN
    year = datetime.now().year
    # Get last certificate number for this year
    last_cert = db.query("""
        SELECT certificate_number
        FROM "Certificate"
        WHERE certificate_number LIKE %s
        ORDER BY issued_at DESC
        LIMIT 1
    """, f"CERT-{year}-%")

    if last_cert:
        # Extract number: CERT-2025-001234 → 1234
        last_num = int(last_cert['certificate_number'].split('-')[-1])
        next_num = last_num + 1
    else:
        next_num = 1

    # Format: CERT-2025-000001
    return f"CERT-{year}-{next_num:06d}"
```

**Certificate Verification:**

```python
@app.get("/api/certificates/verify/{certificate_number}")
def verify_certificate(certificate_number: str):
    cert = db.query("""
        SELECT
          c.certificate_id,
          c.certificate_number,
          c.issued_at,
          c.valid,
          u.first_name,
          u.last_name,
          u.email,
          co.title AS course_title
        FROM "Certificate" c
        JOIN "User" u ON c.user_id = u.user_id
        JOIN "Course" co ON c.course_id = co.course_id
        WHERE c.certificate_number = %s
    """, certificate_number)

    if not cert:
        raise HTTPException(404, "Certificate not found")

    if not cert['valid']:
        return {
            'valid': False,
            'message': 'Certificate has been revoked',
            'revoked_at': cert.get('revoked_at')
        }

    return {
        'valid': True,
        'certificate_number': cert['certificate_number'],
        'issued_to': f"{cert['first_name']} {cert['last_name']}",
        'email': cert['email'],
        'course': cert['course_title'],
        'issued_at': cert['issued_at']
    }
```

**Certificate Revocation:**

```python
@app.post("/api/certificates/{certificate_id}/revoke")
def revoke_certificate(certificate_id: UUID, reason: str, admin_user_id: UUID):
    # Check permission (only admin)
    if not has_permission(admin_user_id, 'revoke_certificate'):
        raise HTTPException(403)

    # Revoke
    db.update("Certificate", certificate_id, {
        'valid': False,
        'revoked_at': 'CURRENT_TIMESTAMP',
        'revoked_reason': reason
    })

    # Send notification to user
    send_revocation_email(certificate_id, reason)

    return {'success': True}
```

**Auto-check for certificate eligibility:**

```python
# Background job (runs daily)
def check_certificate_eligibility():
    # Find completed enrollments without certificates
    enrollments = db.query("""
        SELECT e.*
        FROM "Enrollment" e
        LEFT JOIN "Certificate" c ON e.enrollment_id = c.enrollment_id
        WHERE e.status = 'COMPLETED'
          AND e.completion_percentage >= 80
          AND c.certificate_id IS NULL  -- No certificate yet
    """)

    for enrollment in enrollments:
        # Issue certificate
        cert_id = issue_certificate(enrollment)
        print(f"Issued certificate {cert_id} for enrollment {enrollment['enrollment_id']}")
```

**Kết luận:**
- Certificates issued by application logic (NOT database trigger)
- Allows complex business rules, email notifications, PDF generation
- Can be revoked (valid=false)
- Verified via public API (certificate_number)

---

## 7. RELATIONSHIPS & CONSTRAINTS

### Q7.1: Foreign keys có ON DELETE CASCADE hay SET NULL? Quyết định như thế nào?

**Trả lời:**

ON DELETE behavior phụ thuộc vào **relationship semantics**:

**1. CASCADE (Cascade delete):**

**When:** Child records không có ý nghĩa without parent

**Examples:**

```sql
-- Module → Lecture
CREATE TABLE "Lecture" (
  lecture_id UUID PRIMARY KEY,
  module_id UUID REFERENCES "Module"(module_id) ON DELETE CASCADE
);
-- Delete module → Delete all lectures (lectures meaningless without module)

-- Lecture → Resource
CREATE TABLE "Resource" (
  resource_id UUID PRIMARY KEY,
  lecture_id UUID REFERENCES "Lecture"(lecture_id) ON DELETE CASCADE
);
-- Delete lecture → Delete all resources

-- Quiz → Attempt
CREATE TABLE "Attempt" (
  attempt_id UUID PRIMARY KEY,
  quiz_id UUID REFERENCES "Quiz"(quiz_id) ON DELETE CASCADE
);
-- Delete quiz → Delete all attempts (attempts meaningless without quiz)

-- User → UserRole
CREATE TABLE "UserRole" (
  user_role_id UUID PRIMARY KEY,
  user_id UUID REFERENCES "User"(user_id) ON DELETE CASCADE
);
-- Delete user → Delete all role assignments
```

**2. SET NULL (Nullify FK):**

**When:** Child records có ý nghĩa, nhưng reference optional

**Examples:**

```sql
-- User → Course (created_by)
CREATE TABLE "Course" (
  course_id UUID PRIMARY KEY,
  created_by UUID REFERENCES "User"(user_id) ON DELETE SET NULL
);
-- Delete user → Course remains, created_by = NULL (course still exists)

-- User → AssignmentSubmission (graded_by)
CREATE TABLE "AssignmentSubmission" (
  submission_id UUID PRIMARY KEY,
  graded_by UUID REFERENCES "User"(user_id) ON DELETE SET NULL
);
-- Delete instructor → Submissions remain, graded_by = NULL (history preserved)
```

**3. RESTRICT (Prevent delete):**

**When:** Parent cannot be deleted if children exist

**Examples:**

```sql
-- Course → Enrollment
CREATE TABLE "Enrollment" (
  enrollment_id UUID PRIMARY KEY,
  course_id UUID REFERENCES "Course"(course_id) ON DELETE RESTRICT
);
-- Cannot delete course if enrollments exist (protect student data)

-- User → Enrollment
CREATE TABLE "Enrollment" (
  enrollment_id UUID PRIMARY KEY,
  user_id UUID REFERENCES "User"(user_id) ON DELETE RESTRICT
);
-- Cannot delete user if enrollments exist (protect learning history)
```

**Decision Matrix:**

| Parent → Child | Relationship | ON DELETE | Reasoning |
|----------------|--------------|-----------|-----------|
| Course → Module | Composition | **CASCADE** | Modules belong to course |
| Module → Lecture | Composition | **CASCADE** | Lectures belong to module |
| Lecture → Resource | Composition | **CASCADE** | Resources belong to lecture |
| Quiz → Attempt | Composition | **CASCADE** | Attempts belong to quiz |
| User → UserRole | Association | **CASCADE** | Roles assigned to user |
| User → Course (creator) | Attribution | **SET NULL** | Course survives creator deletion |
| User → Submission (grader) | Attribution | **SET NULL** | Submission survives grader deletion |
| Course → Enrollment | Historical | **RESTRICT** | Protect student enrollment history |
| User → Enrollment | Historical | **RESTRICT** | Protect user learning history |

**In B-Learning Core:**

```sql
-- CASCADE: Parent-child composition
CREATE TABLE "Module" (
  course_id UUID REFERENCES "Course"(course_id) ON DELETE CASCADE
);

CREATE TABLE "Lecture" (
  module_id UUID REFERENCES "Module"(module_id) ON DELETE CASCADE
);

CREATE TABLE "Resource" (
  lecture_id UUID REFERENCES "Lecture"(lecture_id) ON DELETE CASCADE
);

-- SET NULL: Optional attribution
CREATE TABLE "Course" (
  created_by UUID REFERENCES "User"(user_id) ON DELETE SET NULL
);

CREATE TABLE "AssignmentSubmission" (
  graded_by UUID REFERENCES "User"(user_id) ON DELETE SET NULL
);

-- No explicit RESTRICT in Core v1.0 (application handles)
-- But conceptually: Course/User should not be deleted if enrollments exist
```

**Application-level protection:**

```python
def delete_course(course_id: UUID, user_id: UUID):
    # Check permission
    if not has_permission(user_id, 'delete_course'):
        raise HTTPException(403)

    # Check if enrollments exist
    enrollment_count = db.query("""
        SELECT COUNT(*) as count
        FROM "Enrollment"
        WHERE course_id = %s
    """, course_id)[0]['count']

    if enrollment_count > 0:
        raise HTTPException(400, f"Cannot delete course with {enrollment_count} enrollments")

    # Safe to delete (will CASCADE to modules, lectures, resources)
    db.delete("Course", course_id)

    return {'success': True}
```

**Kết luận:**
- CASCADE: Composition (child meaningless without parent)
- SET NULL: Attribution (child survives, reference optional)
- RESTRICT: Historical data protection (application-enforced)
- Choose based on relationship semantics

---

### Q7.2: Tại sao dùng UUID thay vì composite primary key cho junction tables?

**Trả lời:**

Junction tables (many-to-many) trong B-Learning Core dùng **UUID primary key** thay vì composite key.

**Example: UserRole (User ↔ Role)**

**Option 1: Composite PK (❌ Not used)**

```sql
CREATE TABLE "UserRole" (
  user_id UUID REFERENCES "User",
  role_id UUID REFERENCES "Role",
  granted_at TIMESTAMP,

  PRIMARY KEY (user_id, role_id)  -- Composite PK
);
```

**Option 2: UUID PK + Unique constraint (✅ Current design)**

```sql
CREATE TABLE "UserRole" (
  user_role_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),  -- Surrogate PK
  user_id UUID REFERENCES "User",
  role_id UUID REFERENCES "Role",
  granted_at TIMESTAMP,
  granted_by UUID,
  expires_at TIMESTAMP,

  CONSTRAINT uq_user_role UNIQUE(user_id, role_id)  -- Natural key as unique constraint
);
```

**Why UUID PK?**

**1. Consistency:**

All tables have same PK structure (single UUID column)

```sql
-- Consistent pattern across all tables
User: user_id UUID PRIMARY KEY
Role: role_id UUID PRIMARY KEY
UserRole: user_role_id UUID PRIMARY KEY  -- Consistent!
Course: course_id UUID PRIMARY KEY
Enrollment: enrollment_id UUID PRIMARY KEY  -- Consistent!
```

vs

```sql
-- Inconsistent
User: user_id UUID PRIMARY KEY
Role: role_id UUID PRIMARY KEY
UserRole: PRIMARY KEY (user_id, role_id)  -- Different!
```

**2. Easier to reference:**

If other tables need to reference UserRole:

```sql
-- With UUID PK: Easy
CREATE TABLE "UserRoleAudit" (
  audit_id UUID PRIMARY KEY,
  user_role_id UUID REFERENCES "UserRole",  -- Simple FK
  action VARCHAR(20),
  changed_at TIMESTAMP
);

-- With composite PK: Complex
CREATE TABLE "UserRoleAudit" (
  audit_id UUID PRIMARY KEY,
  user_id UUID,
  role_id UUID,
  action VARCHAR(20),
  changed_at TIMESTAMP,

  FOREIGN KEY (user_id, role_id) REFERENCES "UserRole"(user_id, role_id)  -- Complex FK
);
```

**3. Additional metadata:**

Junction table có thể có metadata riêng (granted_at, granted_by, expires_at)

```sql
CREATE TABLE "UserRole" (
  user_role_id UUID PRIMARY KEY,  -- Identity for this assignment
  user_id UUID,
  role_id UUID,
  granted_at TIMESTAMP,    -- When role granted
  granted_by UUID,         -- Who granted (admin)
  expires_at TIMESTAMP,    -- Role expiration
  notes TEXT               -- Optional notes

  CONSTRAINT uq_user_role UNIQUE(user_id, role_id)
);

-- Queries on specific assignment
SELECT * FROM "UserRole" WHERE user_role_id = '...';
UPDATE "UserRole" SET expires_at = '2026-01-01' WHERE user_role_id = '...';
DELETE FROM "UserRole" WHERE user_role_id = '...';
```

**4. ORM-friendly:**

Most ORMs prefer single-column PK:

```python
# SQLAlchemy (Python ORM)
class UserRole(Base):
    __tablename__ = 'UserRole'

    user_role_id = Column(UUID, primary_key=True)  # Simple PK
    user_id = Column(UUID, ForeignKey('User.user_id'))
    role_id = Column(UUID, ForeignKey('Role.role_id'))
    granted_at = Column(DateTime)

    __table_args__ = (
        UniqueConstraint('user_id', 'role_id', name='uq_user_role'),
    )

# vs composite PK (more complex)
class UserRole(Base):
    __tablename__ = 'UserRole'

    user_id = Column(UUID, ForeignKey('User.user_id'), primary_key=True)
    role_id = Column(UUID, ForeignKey('Role.role_id'), primary_key=True)
    granted_at = Column(DateTime)
```

**5. API simplicity:**

```python
# With UUID PK: Simple REST API
DELETE /api/user-roles/{user_role_id}

# With composite PK: Complex
DELETE /api/user-roles?user_id=xxx&role_id=yyy
# or
DELETE /api/users/{user_id}/roles/{role_id}
```

**Trade-offs:**

**Cons of UUID PK:**
- ❌ Extra 16 bytes per row (storage overhead)
- ❌ Need UNIQUE constraint on (user_id, role_id) to prevent duplicates

**Pros outweigh cons:**
- ✅ Consistency across schema
- ✅ Easier to reference
- ✅ Supports rich metadata
- ✅ ORM-friendly
- ✅ Simple APIs

**When to use composite PK:**

✅ Use composite PK if:
- Pure junction table (no metadata)
- Never referenced by other tables
- Want to save storage

Example:
```sql
-- Simple tag assignment (no metadata needed)
CREATE TABLE "CourseTag" (
  course_id UUID REFERENCES "Course",
  tag_id UUID REFERENCES "Tag",
  PRIMARY KEY (course_id, tag_id)  -- Composite PK OK here
);
```

**In B-Learning Core:**

Most junction tables use **UUID PK** for consistency:

```sql
-- UserRole: UUID PK
user_role_id UUID PRIMARY KEY

-- Could add in future:
-- EnrollmentProgress: UUID PK (if needed)
-- CourseInstructor: UUID PK (if multiple instructors per course)
```

**Kết luận:**
- UUID PK + UNIQUE constraint = best practice for junction tables with metadata
- Composite PK = acceptable for pure junction tables without metadata
- B-Learning Core uses UUID PK for consistency and flexibility

---

## 8. PERFORMANCE & OPTIMIZATION

### Q8.1: Database có bao nhiêu indexes? Tại sao cần nhiều indexes?

**Trả lời:**

B-Learning Core có **96+ indexes** across 16 tables:

**Index Categories:**

**1. Primary Key Indexes (16 indexes)**
- Automatically created for PRIMARY KEY constraints
- 1 per table × 16 tables = 16 indexes

**2. Foreign Key Indexes (23 indexes)**
- Created for foreign key columns to speed up JOINs
- Examples:
  ```sql
  CREATE INDEX idx_enrollment_user ON "Enrollment"(user_id);
  CREATE INDEX idx_enrollment_course ON "Enrollment"(course_id);
  CREATE INDEX idx_module_course ON "Module"(course_id);
  ```

**3. Unique Indexes (10+ indexes)**
- Created for UNIQUE constraints
- Examples:
  ```sql
  CREATE UNIQUE INDEX uq_user_email ON "User"(email);
  CREATE UNIQUE INDEX uq_course_code ON "Course"(code);
  CREATE UNIQUE INDEX uq_user_role ON "UserRole"(user_id, role_id);
  ```

**4. Performance Indexes (30+ indexes)**
- Created for common queries
- Examples:
  ```sql
  CREATE INDEX idx_user_status ON "User"(account_status);
  CREATE INDEX idx_course_status ON "Course"(status);
  CREATE INDEX idx_enrollment_status ON "Enrollment"(status);
  CREATE INDEX idx_attempt_user_quiz ON "Attempt"(user_id, quiz_id);
  ```

**5. GIN Indexes for JSON (10+ indexes)**
- For fast JSON queries
- Examples:
  ```sql
  CREATE INDEX idx_quiz_questions ON "Quiz" USING GIN (questions);
  CREATE INDEX idx_attempt_answers ON "Attempt" USING GIN (answers);
  CREATE INDEX idx_user_preferences ON "User" USING GIN (preferences);
  ```

**6. GIN Indexes for Arrays (5+ indexes)**
- For array containment queries
- Examples:
  ```sql
  CREATE INDEX idx_module_prerequisites ON "Module" USING GIN (prerequisite_module_ids);
  CREATE INDEX idx_submission_files ON "AssignmentSubmission" USING GIN (file_urls);
  ```

**7. Composite Indexes (12+ indexes)**
- For multi-column queries
- Examples:
  ```sql
  CREATE INDEX idx_progress_enrollment_module ON "Progress"(enrollment_id, module_id);
  CREATE INDEX idx_attempt_quiz_user_status ON "Attempt"(quiz_id, user_id, status);
  ```

**Total: 96+ indexes**

**Tại sao cần nhiều indexes?**

**1. Speed up JOINs:**

```sql
-- Without index on Enrollment.course_id: Slow (sequential scan)
SELECT e.*, c.title
FROM "Enrollment" e
JOIN "Course" c ON e.course_id = c.course_id
WHERE e.user_id = '...';

-- With index: Fast (index scan)
CREATE INDEX idx_enrollment_course ON "Enrollment"(course_id);
```

**2. Speed up WHERE clauses:**

```sql
-- Without index on User.account_status: Slow
SELECT * FROM "User" WHERE account_status = 'ACTIVE';

-- With index: Fast
CREATE INDEX idx_user_status ON "User"(account_status);
```

**3. Speed up ORDER BY:**

```sql
-- Without index on Course.created_at: Slow (sort needed)
SELECT * FROM "Course"
ORDER BY created_at DESC
LIMIT 10;

-- With index: Fast (index already sorted)
CREATE INDEX idx_course_created ON "Course"(created_at DESC);
```

**4. Unique constraints:**

```sql
-- Prevent duplicate emails
CREATE UNIQUE INDEX uq_user_email ON "User"(email);

-- Try to insert duplicate → Error
INSERT INTO "User" (email, ...) VALUES ('existing@example.com', ...);
-- ERROR: duplicate key value violates unique constraint "uq_user_email"
```

**5. JSON queries:**

```sql
-- Without GIN index: Slow (full table scan)
SELECT * FROM "Quiz"
WHERE questions @> '[{"question_type": "MULTIPLE_CHOICE"}]';

-- With GIN index: Fast
CREATE INDEX idx_quiz_questions ON "Quiz" USING GIN (questions);
```

**Index Size vs Performance:**

**Trade-offs:**
- ✅ Faster SELECT queries
- ❌ Slower INSERT/UPDATE/DELETE (indexes must be updated)
- ❌ More disk space (indexes ~30-50% of table size)
- ❌ More RAM (indexes cached in memory)

**Example:**

```
Table: Enrollment (10,000 rows)
- Data size: 2 MB
- Indexes:
  - PK index (enrollment_id): 0.5 MB
  - FK index (user_id): 0.3 MB
  - FK index (course_id): 0.3 MB
  - FK index (class_id): 0.3 MB
  - Status index: 0.2 MB
  Total indexes: 1.6 MB (80% of data size)

Total: 3.6 MB (data + indexes)
```

**For 16 tables × average 6 indexes per table = 96 indexes**

**When to add index:**

✅ **Add index if:**
- Column used in WHERE clause frequently
- Column used in JOIN frequently
- Column used in ORDER BY frequently
- Unique constraint needed
- Query slow (EXPLAIN shows sequential scan)

❌ **Don't add index if:**
- Column rarely queried
- Table very small (<1000 rows)
- Column has low cardinality (few distinct values, e.g., boolean)
- Too many indexes (INSERT performance suffers)

**Monitoring indexes:**

```sql
-- Find unused indexes
SELECT
  schemaname,
  tablename,
  indexname,
  idx_scan,
  idx_tup_read,
  idx_tup_fetch
FROM pg_stat_user_indexes
WHERE idx_scan = 0  -- Never used
ORDER BY tablename, indexname;

-- Index size
SELECT
  tablename,
  indexname,
  pg_size_pretty(pg_relation_size(indexrelid)) AS index_size
FROM pg_stat_user_indexes
ORDER BY pg_relation_size(indexrelid) DESC;
```

**Kết luận:**
- 96+ indexes across 16 tables
- Speed up queries (JOINs, WHERE, ORDER BY)
- Essential for performance at scale
- Trade-off: Storage + slower writes
- Monitor and remove unused indexes

---

### Q8.2: GIN index hoạt động như thế nào? Khi nào dùng GIN?

**Trả lời:**

**GIN (Generalized Inverted Index)** là index type trong PostgreSQL cho **multi-value data** (arrays, JSON, full-text search).

**How GIN works:**

**Normal B-tree index:**
```
user_id (UUID) → row location
550e8400-... → row 1
660e8400-... → row 2
```

**GIN index (for arrays):**
```
Array element → rows containing this element
module-1-uuid → rows [5, 12, 23]  (modules with prerequisite module-1)
module-2-uuid → rows [12, 23, 45]
module-3-uuid → rows [23]
```

**Example: Module prerequisites**

```sql
-- Table
CREATE TABLE "Module" (
  module_id UUID PRIMARY KEY,
  title VARCHAR(200),
  prerequisite_module_ids UUID[]  -- Array
);

-- Sample data
INSERT INTO "Module" (module_id, title, prerequisite_module_ids) VALUES
  ('mod-1', 'Intro', NULL),
  ('mod-2', 'Basics', ARRAY['mod-1']),
  ('mod-3', 'Advanced', ARRAY['mod-1', 'mod-2']),
  ('mod-4', 'Expert', ARRAY['mod-2', 'mod-3']);

-- GIN index
CREATE INDEX idx_module_prerequisites ON "Module"
USING GIN (prerequisite_module_ids);
```

**GIN inverted index:**
```
mod-1 → [mod-2, mod-3]  (mod-2 and mod-3 require mod-1)
mod-2 → [mod-3, mod-4]  (mod-3 and mod-4 require mod-2)
mod-3 → [mod-4]         (mod-4 requires mod-3)
```

**Query using GIN:**

```sql
-- Find all modules that require mod-1
SELECT module_id, title
FROM "Module"
WHERE 'mod-1'::UUID = ANY(prerequisite_module_ids);

-- Without GIN: Sequential scan (slow)
-- With GIN: Index scan (fast)
-- Look up 'mod-1' in inverted index → [mod-2, mod-3]
```

**JSONB GIN index:**

```sql
-- Table
CREATE TABLE "Quiz" (
  quiz_id UUID PRIMARY KEY,
  questions JSONB
);

-- GIN index
CREATE INDEX idx_quiz_questions ON "Quiz" USING GIN (questions);

-- Query: Find quizzes with MULTIPLE_CHOICE questions
SELECT quiz_id, title
FROM "Quiz"
WHERE questions @> '[{"question_type": "MULTIPLE_CHOICE"}]';

-- GIN index builds inverted index on JSON paths:
-- questions[*].question_type → quiz_ids
-- "MULTIPLE_CHOICE" → [quiz-1, quiz-5, quiz-9]
-- "TRUE_FALSE" → [quiz-2, quiz-3]
-- "SHORT_ANSWER" → [quiz-4, quiz-6]
```

**When to use GIN:**

✅ **Use GIN for:**

**1. Array contains queries:**
```sql
-- Check if array contains element
WHERE 'element'::UUID = ANY(array_column)

-- Check if array contains all elements
WHERE array_column @> ARRAY['elem1', 'elem2']

-- Check if arrays overlap
WHERE array_column && ARRAY['elem1', 'elem2']
```

**2. JSONB queries:**
```sql
-- Contains
WHERE jsonb_column @> '{"key": "value"}'

-- Key exists
WHERE jsonb_column ? 'key'

-- Path exists
WHERE jsonb_column @> '{"nested": {"key": "value"}}'
```

**3. Full-text search:**
```sql
-- tsvector for full-text search
CREATE INDEX idx_course_search ON "Course"
USING GIN (to_tsvector('english', title || ' ' || description));

-- Search
SELECT * FROM "Course"
WHERE to_tsvector('english', title || ' ' || description) @@ to_tsquery('java & programming');
```

❌ **Don't use GIN for:**

**1. Exact equality:**
```sql
-- Use B-tree, not GIN
WHERE user_id = '...'
```

**2. Range queries:**
```sql
-- Use B-tree, not GIN
WHERE created_at BETWEEN '2025-01-01' AND '2025-12-31'
```

**3. Small tables:**
- GIN overhead not worth it for <1000 rows

**GIN vs B-tree:**

| Feature | GIN | B-tree |
|---------|-----|--------|
| **Use case** | Arrays, JSON, full-text | Scalars (int, UUID, text) |
| **Operators** | @>, &&, @@ | =, <, >, BETWEEN |
| **Size** | Larger (2-3x) | Smaller |
| **Build time** | Slower | Faster |
| **Query speed** | Fast for containment | Fast for equality/range |
| **Update speed** | Slower | Faster |

**GIN index maintenance:**

```sql
-- GIN indexes can get bloated over time
-- Rebuild index
REINDEX INDEX idx_quiz_questions;

-- Or recreate
DROP INDEX idx_quiz_questions;
CREATE INDEX idx_quiz_questions ON "Quiz" USING GIN (questions);
```

**Examples in B-Learning Core:**

```sql
-- 1. Module prerequisites (array)
CREATE INDEX idx_module_prerequisites ON "Module"
USING GIN (prerequisite_module_ids);

SELECT * FROM "Module"
WHERE 'prereq-uuid'::UUID = ANY(prerequisite_module_ids);

-- 2. Quiz questions (JSONB)
CREATE INDEX idx_quiz_questions ON "Quiz"
USING GIN (questions);

SELECT * FROM "Quiz"
WHERE questions @> '[{"question_type": "MULTIPLE_CHOICE"}]';

-- 3. Attempt answers (JSONB)
CREATE INDEX idx_attempt_answers ON "Attempt"
USING GIN (answers);

SELECT * FROM "Attempt"
WHERE answers @> '[{"is_correct": true}]';

-- 4. User preferences (JSON)
CREATE INDEX idx_user_preferences ON "User"
USING GIN (preferences);

SELECT * FROM "User"
WHERE preferences @> '{"locale": "vi"}';

-- 5. Assignment file URLs (array)
CREATE INDEX idx_submission_files ON "AssignmentSubmission"
USING GIN (file_urls);

SELECT * FROM "AssignmentSubmission"
WHERE file_urls && ARRAY['https://s3.../file.pdf'];
```

**Performance:**

```sql
-- EXPLAIN ANALYZE to verify GIN index used
EXPLAIN ANALYZE
SELECT * FROM "Quiz"
WHERE questions @> '[{"question_type": "MULTIPLE_CHOICE"}]';

-- Result (with GIN index):
-- Bitmap Index Scan on idx_quiz_questions  (cost=0.00..12.00 rows=10 width=500)
--   Index Cond: (questions @> '[{"question_type": "MULTIPLE_CHOICE"}]'::jsonb)

-- Result (without GIN index):
-- Seq Scan on "Quiz"  (cost=0.00..5000.00 rows=10 width=500)
--   Filter: (questions @> '[{"question_type": "MULTIPLE_CHOICE"}]'::jsonb)
-- → Much slower for large tables
```

**Kết luận:**
- GIN = inverted index for multi-value data
- Use for arrays, JSONB, full-text search
- Essential for fast containment queries (@>, &&)
- Larger and slower to maintain than B-tree
- Worth it for complex queries on JSON/arrays

---

### Q8.3: Database có cache không? Làm sao để optimize query performance?

**Trả lời:**

PostgreSQL có **built-in caching**, và có nhiều strategies để optimize performance.

**1. PostgreSQL Built-in Cache:**

**Shared Buffers:**
- PostgreSQL caches frequently accessed data in RAM
- Default: 128 MB (too small for production)
- Recommended: 25% of total RAM

```sql
-- Check current setting
SHOW shared_buffers;

-- Set in postgresql.conf
shared_buffers = 2GB  -- For 8GB RAM server
```

**Page Cache (OS-level):**
- OS caches disk reads in RAM
- Automatic (no configuration needed)
- Helps all queries

**2. Query Optimization Strategies:**

**A. Use EXPLAIN ANALYZE:**

```sql
-- Analyze query performance
EXPLAIN ANALYZE
SELECT e.*, c.title
FROM "Enrollment" e
JOIN "Course" c ON e.course_id = c.course_id
WHERE e.user_id = '...';

-- Result shows:
-- 1. Execution time
-- 2. Index usage (Index Scan vs Seq Scan)
-- 3. Number of rows processed
-- 4. Cost estimation
```

**B. Add indexes for slow queries:**

```sql
-- Slow query (Seq Scan)
EXPLAIN ANALYZE
SELECT * FROM "User" WHERE account_status = 'ACTIVE';
-- → Seq Scan on "User" (cost=0..1000 rows=5000)

-- Add index
CREATE INDEX idx_user_status ON "User"(account_status);

-- Fast query (Index Scan)
EXPLAIN ANALYZE
SELECT * FROM "User" WHERE account_status = 'ACTIVE';
-- → Index Scan using idx_user_status (cost=0..50 rows=5000)
```

**C. Use composite indexes for multi-column queries:**

```sql
-- Slow: Two separate indexes
CREATE INDEX idx_attempt_quiz ON "Attempt"(quiz_id);
CREATE INDEX idx_attempt_user ON "Attempt"(user_id);

SELECT * FROM "Attempt"
WHERE quiz_id = '...' AND user_id = '...';
-- → Uses one index, then filters

-- Fast: Composite index
CREATE INDEX idx_attempt_quiz_user ON "Attempt"(quiz_id, user_id);

SELECT * FROM "Attempt"
WHERE quiz_id = '...' AND user_id = '...';
-- → Uses composite index (faster)
```

**D. Avoid SELECT *:**

```sql
-- Slow: Load all columns
SELECT * FROM "User";

-- Fast: Load only needed columns
SELECT user_id, email, first_name, last_name FROM "User";
```

**E. Use LIMIT for pagination:**

```sql
-- Don't load all rows
SELECT * FROM "Course" WHERE status = 'PUBLISHED'
ORDER BY created_at DESC
LIMIT 20 OFFSET 0;  -- Page 1

LIMIT 20 OFFSET 20;  -- Page 2
```

**F. Use JOINs instead of multiple queries:**

```sql
-- Slow: N+1 queries
enrollments = db.query("SELECT * FROM Enrollment WHERE user_id = %s", user_id)
for e in enrollments:
    course = db.query("SELECT * FROM Course WHERE course_id = %s", e['course_id'])

-- Fast: 1 query with JOIN
SELECT e.*, c.title, c.description
FROM "Enrollment" e
JOIN "Course" c ON e.course_id = c.course_id
WHERE e.user_id = %s
```

**3. Application-Level Caching:**

**A. Redis cache:**

```python
import redis
r = redis.Redis()

def get_user(user_id):
    # Check cache first
    cached = r.get(f"user:{user_id}")
    if cached:
        return json.loads(cached)

    # Cache miss → Query database
    user = db.query("SELECT * FROM User WHERE user_id = %s", user_id)

    # Store in cache (expire after 1 hour)
    r.setex(f"user:{user_id}", 3600, json.dumps(user))

    return user
```

**B. Memoization (in-memory cache):**

```python
from functools import lru_cache

@lru_cache(maxsize=1000)
def get_course(course_id):
    return db.query("SELECT * FROM Course WHERE course_id = %s", course_id)

# First call: Query database
course = get_course('course-uuid')

# Second call: Return cached result (no database query)
course = get_course('course-uuid')
```

**4. Database Connection Pooling:**

```python
# Don't create new connection for every query (slow)
import psycopg2
conn = psycopg2.connect(...)  # New connection
cursor = conn.cursor()
cursor.execute("SELECT ...")
conn.close()

# Use connection pool (fast)
from psycopg2 import pool
connection_pool = pool.SimpleConnectionPool(5, 20, ...)  # 5 min, 20 max

conn = connection_pool.getconn()
cursor = conn.cursor()
cursor.execute("SELECT ...")
connection_pool.putconn(conn)  # Return to pool
```

**5. Materialized Views for complex queries:**

```sql
-- Expensive query (JOINs, aggregations)
SELECT
  c.course_id,
  c.title,
  COUNT(DISTINCT e.enrollment_id) AS enrollment_count,
  AVG(e.completion_percentage) AS avg_completion
FROM "Course" c
LEFT JOIN "Enrollment" e ON c.course_id = e.course_id
GROUP BY c.course_id, c.title;

-- Create materialized view (precomputed)
CREATE MATERIALIZED VIEW course_stats AS
SELECT
  c.course_id,
  c.title,
  COUNT(DISTINCT e.enrollment_id) AS enrollment_count,
  AVG(e.completion_percentage) AS avg_completion
FROM "Course" c
LEFT JOIN "Enrollment" e ON c.course_id = e.course_id
GROUP BY c.course_id, c.title;

-- Query materialized view (fast)
SELECT * FROM course_stats;

-- Refresh periodically (daily cron job)
REFRESH MATERIALIZED VIEW course_stats;
```

**6. Vacuum and Analyze:**

```sql
-- Remove dead rows (UPDATE/DELETE leaves dead rows)
VACUUM "User";

-- Update statistics for query planner
ANALYZE "User";

-- Or both
VACUUM ANALYZE "User";

-- Auto-vacuum (enabled by default)
-- Runs automatically when needed
```

**7. Partitioning for large tables:**

```sql
-- If Attempt table grows to millions of rows
-- Partition by month
CREATE TABLE "Attempt_2025_01" PARTITION OF "Attempt"
FOR VALUES FROM ('2025-01-01') TO ('2025-02-01');

CREATE TABLE "Attempt_2025_02" PARTITION OF "Attempt"
FOR VALUES FROM ('2025-02-01') TO ('2025-03-01');

-- Queries automatically use correct partition
SELECT * FROM "Attempt"
WHERE started_at BETWEEN '2025-01-01' AND '2025-01-31';
-- → Only scans Attempt_2025_01 partition (fast)
```

**8. Monitor slow queries:**

```sql
-- Enable slow query log
ALTER DATABASE b_learning_core SET log_min_duration_statement = 1000;  -- Log queries >1s

-- Check pg_stat_statements
SELECT
  query,
  calls,
  total_time,
  mean_time,
  max_time
FROM pg_stat_statements
ORDER BY mean_time DESC
LIMIT 10;

-- Find slow queries and optimize
```

**Kết luận:**
- PostgreSQL has built-in caching (shared_buffers)
- Optimize queries: indexes, EXPLAIN ANALYZE, avoid SELECT *
- Application-level cache: Redis, memoization
- Connection pooling: Reuse connections
- Materialized views: Precompute complex queries
- Monitor and optimize slow queries

---

## 9. JSON FIELDS & DESIGN PATTERNS

### Q9.1: Khi nào nên dùng JSON field, khi nào nên dùng separate table?

**Trả lời:**

**Decision matrix for JSON vs Separate Table:**

✅ **Use JSON when:**

**1. Schema is flexible/evolving:**
```sql
-- User preferences: Different users have different settings
CREATE TABLE "User" (
  preferences JSON  -- Can have any structure
);

-- User A:
{"theme": "dark", "locale": "vi"}

-- User B:
{"theme": "light", "locale": "en", "dashboard": {"show_progress": true}}
```

**2. Data always loaded/saved together (atomic):**
```sql
-- Quiz questions: Always load all questions when loading quiz
CREATE TABLE "Quiz" (
  questions JSONB  -- All questions in 1 field
);

-- vs separate table:
CREATE TABLE "Question" (...);  -- Need JOIN to load
```

**3. No complex queries on nested data:**
```sql
-- Rarely query individual questions
-- Usually: Load quiz → Display all questions → Student answers

-- If needed deep query:
SELECT * FROM "Quiz"
WHERE questions @> '[{"question_type": "MULTIPLE_CHOICE"}]';
-- → Works, but slow
```

**4. Versioning important:**
```sql
-- Easy to snapshot quiz (archive)
INSERT INTO "Quiz_Archive" SELECT * FROM "Quiz" WHERE quiz_id = '...';
-- → Entire quiz with all questions saved
```

**5. Small to medium data (<100 nested items):**
```sql
-- Quiz with 20 questions: OK
-- Quiz with 1000 questions: Use separate table
```

---

❌ **Use Separate Table when:**

**1. Schema is stable/well-defined:**
```sql
-- Module structure is stable
CREATE TABLE "Module" (
  module_id UUID PRIMARY KEY,
  title VARCHAR(200),
  order_num INT,
  ...
);

-- NOT:
CREATE TABLE "Course" (
  modules JSON  -- BAD!
);
```

**2. Need complex queries on individual items:**
```sql
-- Need to query individual modules
SELECT * FROM "Module"
WHERE title LIKE '%Introduction%';

-- vs JSON:
SELECT * FROM "Course"
WHERE modules::text LIKE '%Introduction%';  -- Slow, ugly
```

**3. Need foreign key constraints:**
```sql
-- Lecture references Module
CREATE TABLE "Lecture" (
  module_id UUID REFERENCES "Module"(module_id)
);

-- vs JSON: Can't have FK to JSON element
```

**4. Data updated individually (not atomic):**
```sql
-- Update single module title
UPDATE "Module" SET title = '...' WHERE module_id = '...';

-- vs JSON: Must update entire array
UPDATE "Course" SET modules = '[...]'::JSONB WHERE course_id = '...';
```

**5. Large number of items (>100):**
```sql
-- Course with 100+ modules: Use separate table
-- Performance: Query specific module without loading all
```

**6. Need to reference from other tables:**
```sql
-- Progress references Module
CREATE TABLE "Progress" (
  module_id UUID REFERENCES "Module"
);

-- vs JSON: Can't reference JSON element
```

---

**Examples from B-Learning Core:**

✅ **JSON used correctly:**

**1. Quiz.questions:**
- Schema flexible (different question types)
- Always loaded together
- No complex queries (just load quiz)
- Small-medium size (10-50 questions)

**2. Attempt.answers:**
- Snapshot of student attempt
- Never update individual answer
- Atomic data

**3. User.preferences:**
- Schema evolves (new settings added)
- Always loaded with user
- No complex queries

**4. Class.schedules:**
- Flexible (different schedule patterns)
- Always loaded with class
- Small size (1-10 sessions per week)

**5. Lecture.assignment_config:**
- Different assignment types have different configs
- Loaded with lecture
- No complex queries

❌ **Separate tables used correctly:**

**1. Module (not in Course.modules JSON):**
- Stable schema
- Need to query individual modules
- Referenced by Lecture, Progress
- Large number per course

**2. User (not in some aggregate JSON):**
- Stable schema
- Complex queries (filter by status, email)
- Referenced by many tables
- Large table

**3. Enrollment (not in User.enrollments JSON):**
- Stable schema
- Query enrollments independently
- Referenced by Progress, Certificate
- Large number of enrollments

---

**Migration path:**

**Start with JSON (MVP):**
```sql
-- MVP: Simple, fast to develop
CREATE TABLE "Quiz" (
  quiz_id UUID PRIMARY KEY,
  questions JSONB
);
```

**Migrate to separate table (scale):**
```sql
-- When queries become complex/slow
CREATE TABLE "Question" (
  question_id UUID PRIMARY KEY,
  quiz_id UUID REFERENCES "Quiz",
  question_text TEXT,
  question_type VARCHAR(20),
  ...
);

-- Migrate data
INSERT INTO "Question" (quiz_id, question_text, ...)
SELECT
  q.quiz_id,
  question->>'question_text',
  ...
FROM "Quiz" q,
     jsonb_array_elements(q.questions) question;

-- Drop JSON column
ALTER TABLE "Quiz" DROP COLUMN questions;
```

**Kết luận:**
- JSON: Flexible, simple, atomic data
- Separate table: Structured, queryable, constrained
- Choose based on: schema stability, query patterns, data size
- Can migrate JSON → table later if needed

---

### Q9.2: Tại sao Class.schedules dùng JSONB array thay vì bảng ClassSchedule riêng?

**Trả lời:**

**Quyết định thiết kế:**

Class.schedules lưu lịch học dưới dạng JSONB array vì:

**1. Tính chất của dữ liệu:**
```json
{
  "schedules": [
    {
      "day_of_week": "Monday",
      "start_time": "09:00",
      "end_time": "11:00",
      "room": "A101"
    },
    {
      "day_of_week": "Wednesday",
      "start_time": "14:00",
      "end_time": "16:00",
      "room": "B205"
    }
  ]
}
```

**Lý do chọn JSON:**
- Schedule luôn được truy vấn **toàn bộ cùng Class** (không query riêng lẻ từng schedule)
- Không cần search/filter theo day_of_week hay start_time riêng biệt
- Số lượng schedules nhỏ (thường 1-3 buổi/tuần)
- Schema đơn giản, ít thay đổi

**2. So sánh với bảng riêng:**

| Tiêu chí | JSONB Array | Separate Table |
|----------|-------------|----------------|
| Queries | Simple: `SELECT schedules FROM "Class"` | Complex: JOIN required |
| Inserts | Single INSERT | Multiple INSERTs + transaction |
| Updates | Atomic update entire schedule | Update multiple rows |
| Storage | Compact (single row) | 3-4 rows per class |
| Flexibility | Easy to add fields | Need ALTER TABLE |

**3. Truy vấn ví dụ:**

**Với JSONB (hiện tại):**
```sql
-- Lấy lịch học của class
SELECT
  class_id,
  class_name,
  schedules
FROM "Class"
WHERE class_id = 'xxx';

-- Tìm classes có lịch học Monday
SELECT class_id, class_name
FROM "Class"
WHERE schedules @> '[{"day_of_week": "Monday"}]';

-- Đếm số buổi học/tuần
SELECT
  class_id,
  class_name,
  jsonb_array_length(schedules) as sessions_per_week
FROM "Class";
```

**Với bảng riêng (nếu dùng):**
```sql
-- Cần JOIN phức tạp
SELECT
  c.class_id,
  c.class_name,
  json_agg(
    json_build_object(
      'day_of_week', cs.day_of_week,
      'start_time', cs.start_time,
      'end_time', cs.end_time,
      'room', cs.room
    )
  ) as schedules
FROM "Class" c
LEFT JOIN "ClassSchedule" cs ON c.class_id = cs.class_id
WHERE c.class_id = 'xxx'
GROUP BY c.class_id, c.class_name;
```

**4. Business logic trong application:**

```python
# Flask/FastAPI example
@app.get("/classes/{class_id}")
def get_class_detail(class_id: str):
    conn = get_db_connection()
    cur = conn.cursor()

    cur.execute("""
        SELECT class_id, class_name, start_date, end_date, schedules
        FROM "Class"
        WHERE class_id = %s
    """, (class_id,))

    class_data = cur.fetchone()

    return {
        "class_id": class_data[0],
        "class_name": class_data[1],
        "start_date": class_data[2],
        "end_date": class_data[3],
        "schedules": class_data[4]  # Already JSON, no processing needed
    }

# Update schedules
@app.put("/classes/{class_id}/schedules")
def update_schedules(class_id: str, schedules: List[dict]):
    conn = get_db_connection()
    cur = conn.cursor()

    # Validate schedule format
    for schedule in schedules:
        if not all(k in schedule for k in ['day_of_week', 'start_time', 'end_time']):
            return {"error": "Invalid schedule format"}, 400

    # Single atomic update
    cur.execute("""
        UPDATE "Class"
        SET schedules = %s::jsonb,
            updated_at = CURRENT_TIMESTAMP
        WHERE class_id = %s
    """, (json.dumps(schedules), class_id))

    conn.commit()
    return {"message": "Schedules updated successfully"}
```

**5. Index support:**

```sql
-- GIN index cho JSON queries
CREATE INDEX idx_class_schedules_gin ON "Class" USING gin(schedules);

-- Query nhanh cho day_of_week
SELECT * FROM "Class"
WHERE schedules @> '[{"day_of_week": "Monday"}]';
-- Uses idx_class_schedules_gin
```

**6. Khi nào cần bảng riêng?**

Nên tách ClassSchedule table nếu:
- Cần query phức tạp theo time ranges (find all classes between 9-11am)
- Cần foreign keys từ schedule đến Room table (room management system)
- Cần track schedule changes history
- Schedule có nhiều fields phức tạp (instructor, resources, capacity)

**Ví dụ thiết kế nâng cao:**
```sql
CREATE TABLE "ClassSchedule" (
  schedule_id UUID PRIMARY KEY,
  class_id UUID REFERENCES "Class"(class_id) ON DELETE CASCADE,
  day_of_week VARCHAR(10),
  start_time TIME,
  end_time TIME,
  room_id UUID REFERENCES "Room"(room_id),  -- FK to Room
  instructor_id UUID REFERENCES "User"(user_id),  -- FK to Instructor
  created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

-- Find all classes using room A101
SELECT DISTINCT c.*
FROM "Class" c
JOIN "ClassSchedule" cs ON c.class_id = cs.class_id
JOIN "Room" r ON cs.room_id = r.room_id
WHERE r.room_code = 'A101';
```

**Kết luận:**
- **Core v1.0 dùng JSONB** vì đơn giản, đủ cho use case hiện tại
- Schedules luôn được xem toàn bộ, không cần query riêng lẻ
- Atomic updates, compact storage
- Có thể migrate sang separate table nếu cần trong tương lai

---

### Q9.3: Quiz.questions và Attempt.answers có cấu trúc JSONB như thế nào?

**Trả lời:**

**1. Quiz.questions structure:**

```json
{
  "questions": [
    {
      "id": "q1",
      "type": "multiple_choice",
      "question_text": "What is the capital of France?",
      "points": 10,
      "options": [
        {"id": "a", "text": "London"},
        {"id": "b", "text": "Paris"},
        {"id": "c", "text": "Berlin"},
        {"id": "d", "text": "Madrid"}
      ],
      "correct_answer": "b",
      "explanation": "Paris is the capital and largest city of France."
    },
    {
      "id": "q2",
      "type": "true_false",
      "question_text": "PostgreSQL supports JSONB data type.",
      "points": 5,
      "correct_answer": true,
      "explanation": "JSONB is a native PostgreSQL data type for efficient JSON storage."
    },
    {
      "id": "q3",
      "type": "short_answer",
      "question_text": "Name three JavaScript frameworks.",
      "points": 15,
      "sample_answers": ["React", "Vue", "Angular"],
      "grading": "manual"
    }
  ]
}
```

**Field explanations:**
- `id`: Unique identifier trong quiz (q1, q2, q3...)
- `type`: Loại câu hỏi (multiple_choice, true_false, short_answer, essay)
- `question_text`: Nội dung câu hỏi
- `points`: Điểm số cho câu hỏi này
- `options`: Các lựa chọn (chỉ có với multiple_choice)
- `correct_answer`: Đáp án đúng (string cho MC, boolean cho T/F)
- `explanation`: Giải thích đáp án (hiển thị sau khi submit)
- `sample_answers`: Đáp án mẫu (cho short answer)
- `grading`: auto hoặc manual

**2. Attempt.answers structure:**

```json
{
  "answers": [
    {
      "question_id": "q1",
      "answer": "b",
      "is_correct": true,
      "points_earned": 10,
      "time_spent_seconds": 15
    },
    {
      "question_id": "q2",
      "answer": true,
      "is_correct": true,
      "points_earned": 5,
      "time_spent_seconds": 8
    },
    {
      "question_id": "q3",
      "answer": "React, Vue, Angular",
      "is_correct": null,  // Chưa chấm
      "points_earned": null,
      "time_spent_seconds": 120,
      "requires_grading": true
    }
  ]
}
```

**Field explanations:**
- `question_id`: Map với Quiz.questions[].id
- `answer`: Câu trả lời của student (type tùy loại câu hỏi)
- `is_correct`: true/false/null (null = chưa chấm)
- `points_earned`: Điểm thực tế nhận được
- `time_spent_seconds`: Thời gian làm câu này
- `requires_grading`: true nếu cần instructor chấm tay

**3. Auto-grading logic (Python example):**

```python
import json
from typing import Dict, List

def auto_grade_quiz(quiz_questions: List[Dict], student_answers: List[Dict]) -> Dict:
    """
    Auto-grade quiz based on questions and student answers.
    Returns updated answers with scores.
    """
    results = {
        "answers": [],
        "total_score": 0,
        "max_score": 0,
        "auto_graded_count": 0,
        "manual_grading_required": 0
    }

    # Create question lookup
    questions_map = {q['id']: q for q in quiz_questions}

    for student_answer in student_answers:
        qid = student_answer['question_id']
        question = questions_map.get(qid)

        if not question:
            continue

        result = {
            "question_id": qid,
            "answer": student_answer['answer'],
            "time_spent_seconds": student_answer.get('time_spent_seconds', 0)
        }

        # Auto-grading logic
        if question['type'] == 'multiple_choice':
            is_correct = student_answer['answer'] == question['correct_answer']
            result['is_correct'] = is_correct
            result['points_earned'] = question['points'] if is_correct else 0
            results['auto_graded_count'] += 1

        elif question['type'] == 'true_false':
            is_correct = student_answer['answer'] == question['correct_answer']
            result['is_correct'] = is_correct
            result['points_earned'] = question['points'] if is_correct else 0
            results['auto_graded_count'] += 1

        elif question['type'] in ['short_answer', 'essay']:
            # Requires manual grading
            result['is_correct'] = None
            result['points_earned'] = None
            result['requires_grading'] = True
            results['manual_grading_required'] += 1

        results['answers'].append(result)
        results['max_score'] += question['points']
        if result.get('points_earned') is not None:
            results['total_score'] += result['points_earned']

    return results

# Usage example
quiz_data = conn.execute("""
    SELECT questions FROM "Quiz" WHERE quiz_id = %s
""", (quiz_id,)).fetchone()[0]

student_answers_data = request.json['answers']  # From frontend

grading_results = auto_grade_quiz(
    quiz_data['questions'],
    student_answers_data
)

# Save to Attempt table
conn.execute("""
    INSERT INTO "Attempt" (
        attempt_id, enrollment_id, quiz_id,
        answers, score, status
    ) VALUES (%s, %s, %s, %s, %s, %s)
""", (
    uuid.uuid4(),
    enrollment_id,
    quiz_id,
    json.dumps(grading_results['answers']),
    grading_results['total_score'],
    'requires_grading' if grading_results['manual_grading_required'] > 0 else 'graded'
))
```

**4. Manual grading workflow:**

```python
@app.post("/attempts/{attempt_id}/grade")
def manual_grade_attempt(attempt_id: str, grading_data: dict):
    """
    Instructor manually grades short answer/essay questions.
    """
    conn = get_db_connection()
    cur = conn.cursor()

    # Get current attempt
    cur.execute("""
        SELECT answers, quiz_id FROM "Attempt"
        WHERE attempt_id = %s
    """, (attempt_id,))

    attempt = cur.fetchone()
    answers = attempt[0]
    quiz_id = attempt[1]

    # Get quiz questions for max points
    cur.execute("""
        SELECT questions FROM "Quiz" WHERE quiz_id = %s
    """, (quiz_id,))
    questions = cur.fetchone()[0]['questions']
    questions_map = {q['id']: q for q in questions}

    # Update answers with manual grades
    total_score = 0
    for answer in answers['answers']:
        qid = answer['question_id']

        # If this question was manually graded
        if qid in grading_data:
            answer['points_earned'] = grading_data[qid]['points']
            answer['is_correct'] = grading_data[qid]['points'] > 0
            answer['instructor_feedback'] = grading_data[qid].get('feedback', '')
            answer['requires_grading'] = False

        # Sum up total score
        if answer.get('points_earned') is not None:
            total_score += answer['points_earned']

    # Update Attempt
    cur.execute("""
        UPDATE "Attempt"
        SET answers = %s::jsonb,
            score = %s,
            status = 'graded',
            graded_at = CURRENT_TIMESTAMP
        WHERE attempt_id = %s
    """, (json.dumps(answers), total_score, attempt_id))

    conn.commit()

    return {
        "message": "Grading completed",
        "total_score": total_score,
        "max_score": sum(q['points'] for q in questions)
    }
```

**5. Truy vấn JSON trong SQL:**

```sql
-- Tìm attempts có câu q1 trả lời đúng
SELECT
  attempt_id,
  enrollment_id,
  score
FROM "Attempt"
WHERE answers @> '{"answers": [{"question_id": "q1", "is_correct": true}]}'::jsonb;

-- Đếm số câu đúng/sai của một attempt
SELECT
  attempt_id,
  jsonb_array_length(answers->'answers') as total_questions,
  (
    SELECT COUNT(*)
    FROM jsonb_array_elements(answers->'answers') AS ans
    WHERE (ans->>'is_correct')::boolean = true
  ) as correct_count
FROM "Attempt"
WHERE attempt_id = 'xxx';

-- Lấy điểm từng câu của attempt
SELECT
  attempt_id,
  ans->>'question_id' as question_id,
  ans->>'answer' as student_answer,
  (ans->>'is_correct')::boolean as is_correct,
  (ans->>'points_earned')::int as points
FROM "Attempt",
     jsonb_array_elements(answers->'answers') AS ans
WHERE attempt_id = 'xxx';

-- Tìm các attempts cần chấm tay
SELECT
  a.attempt_id,
  u.full_name as student_name,
  q.title as quiz_title
FROM "Attempt" a
JOIN "Enrollment" e ON a.enrollment_id = e.enrollment_id
JOIN "User" u ON e.user_id = u.user_id
JOIN "Quiz" q ON a.quiz_id = q.quiz_id
WHERE EXISTS (
  SELECT 1
  FROM jsonb_array_elements(a.answers->'answers') AS ans
  WHERE (ans->>'requires_grading')::boolean = true
);
```

**6. Schema validation với JSON Schema:**

```python
from jsonschema import validate

# Quiz questions schema
quiz_schema = {
    "type": "object",
    "properties": {
        "questions": {
            "type": "array",
            "items": {
                "type": "object",
                "properties": {
                    "id": {"type": "string"},
                    "type": {"type": "string", "enum": ["multiple_choice", "true_false", "short_answer", "essay"]},
                    "question_text": {"type": "string"},
                    "points": {"type": "number", "minimum": 0}
                },
                "required": ["id", "type", "question_text", "points"]
            }
        }
    },
    "required": ["questions"]
}

# Validate before insert
try:
    validate(instance=quiz_data, schema=quiz_schema)
    # Insert into database
except Exception as e:
    return {"error": f"Invalid quiz format: {str(e)}"}, 400
```

**Kết luận:**
- Quiz.questions: Structured JSON array chứa câu hỏi, đáp án, điểm số
- Attempt.answers: Student answers map với questions, chứa kết quả chấm
- Auto-grading: MC và T/F tự động, short answer/essay cần manual
- JSON queries: Dùng `@>`, `jsonb_array_elements()` để search/analyze
- Validation: Dùng JSON Schema để đảm bảo data integrity

---

## Section 10: Normalization & Data Integrity

### Q10.1: Database có tuân thủ Third Normal Form (3NF) không? Các trường hợp denormalization?

**Trả lời:**

**1. Third Normal Form (3NF) requirements:**

Một table ở 3NF khi:
1. **1NF**: Atomic values (không có repeating groups)
2. **2NF**: Không có partial dependencies (non-key attributes fully depend on primary key)
3. **3NF**: Không có transitive dependencies (non-key attributes don't depend on other non-key attributes)

**2. Kiểm tra từng table:**

**✅ Domain 1: User Management**

```sql
-- ✅ User table: 3NF compliant
CREATE TABLE "User" (
  user_id UUID PRIMARY KEY,           -- PK
  email VARCHAR(255) UNIQUE NOT NULL, -- Depends on user_id, không depend lẫn nhau
  password_hash VARCHAR(255),         -- Depends on user_id
  full_name VARCHAR(200),             -- Depends on user_id
  role VARCHAR(20),                   -- Depends on user_id
  ...
);
-- Không có transitive dependencies: role không depend on full_name, etc.

-- ✅ UserProfile: 3NF compliant
CREATE TABLE "UserProfile" (
  profile_id UUID PRIMARY KEY,
  user_id UUID REFERENCES "User",
  bio TEXT,
  preferences JSONB,
  ...
);
-- Tách riêng khỏi User để tránh NULL values nhiều
-- Profile data depends only on user_id

-- ✅ Notification: 3NF compliant
CREATE TABLE "Notification" (
  notification_id UUID PRIMARY KEY,
  user_id UUID REFERENCES "User",
  message TEXT,
  is_read BOOLEAN,
  ...
);
```

**✅ Domain 2: Course Content**

```sql
-- ✅ Course: 3NF compliant
CREATE TABLE "Course" (
  course_id UUID PRIMARY KEY,
  instructor_id UUID REFERENCES "User",
  title VARCHAR(200),
  description TEXT,
  status VARCHAR(20),
  ...
);

-- ✅ Module: 3NF compliant
CREATE TABLE "Module" (
  module_id UUID PRIMARY KEY,
  course_id UUID REFERENCES "Course",
  title VARCHAR(200),
  order_num INT,
  prerequisite_module_ids UUID[],  -- Array, still atomic (no repeating groups in same column)
  ...
);

-- ✅ Lecture: 3NF compliant
CREATE TABLE "Lecture" (
  lecture_id UUID PRIMARY KEY,
  module_id UUID REFERENCES "Module",
  title VARCHAR(200),
  content TEXT,
  ...
);

-- ✅ Assignment: 3NF compliant
CREATE TABLE "Assignment" (
  assignment_id UUID PRIMARY KEY,
  module_id UUID REFERENCES "Module",
  title VARCHAR(200),
  config JSONB,  -- JSONB is atomic (single value)
  ...
);
```

**⚠️ Strategic Denormalization Cases:**

**Case 1: Enrollment.completion_percentage**

```sql
-- Enrollment table có denormalization
CREATE TABLE "Enrollment" (
  enrollment_id UUID PRIMARY KEY,
  user_id UUID REFERENCES "User",
  course_id UUID REFERENCES "Course",
  completion_percentage DECIMAL(5,2),  -- ⚠️ DENORMALIZED
  ...
);
```

**Tại sao denormalize?**
- `completion_percentage` có thể tính từ Progress table
- **Transitive dependency**: completion_percentage → Progress records → enrollment_id
- Nhưng được giữ lại vì:
  - **Performance**: Tránh phải COUNT Progress records mỗi lần query
  - **Frequent access**: Hiển thị trên dashboard, course lists
  - **Read-heavy**: Đọc nhiều hơn update

**Trade-off:**
```sql
-- ✅ Normalized version (chậm)
SELECT
  e.enrollment_id,
  e.user_id,
  COUNT(p.progress_id) * 100.0 / (
    SELECT COUNT(*) FROM "Lecture" l
    JOIN "Module" m ON l.module_id = m.module_id
    WHERE m.course_id = e.course_id
  ) as completion_percentage
FROM "Enrollment" e
LEFT JOIN "Progress" p ON e.enrollment_id = p.enrollment_id
WHERE e.user_id = 'xxx'
GROUP BY e.enrollment_id;
-- Slow: JOIN + subquery + COUNT

-- ⚠️ Denormalized version (nhanh)
SELECT enrollment_id, user_id, completion_percentage
FROM "Enrollment"
WHERE user_id = 'xxx';
-- Fast: Direct read, no joins
```

**Maintain consistency:**
```python
# Update completion_percentage khi Progress changes
def mark_lecture_complete(enrollment_id, lecture_id):
    conn = get_db_connection()
    cur = conn.cursor()

    # Insert Progress record
    cur.execute("""
        INSERT INTO "Progress" (progress_id, enrollment_id, lecture_id, percentage)
        VALUES (%s, %s, %s, 100)
    """, (uuid.uuid4(), enrollment_id, lecture_id))

    # Recalculate completion_percentage
    cur.execute("""
        UPDATE "Enrollment" e
        SET completion_percentage = (
            SELECT COUNT(p.progress_id) * 100.0 / (
                SELECT COUNT(*) FROM "Lecture" l
                JOIN "Module" m ON l.module_id = m.module_id
                WHERE m.course_id = e.course_id
            )
            FROM "Progress" p
            WHERE p.enrollment_id = e.enrollment_id
        )
        WHERE e.enrollment_id = %s
    """, (enrollment_id,))

    conn.commit()
```

**Case 2: User.role trong User table**

```sql
-- User table có denormalization nhẹ
CREATE TABLE "User" (
  user_id UUID PRIMARY KEY,
  email VARCHAR(255),
  role VARCHAR(20),  -- ⚠️ Could be separate Role table
  ...
);
```

**Tại sao không tách Role table?**
- Roles ít (chỉ có: student, instructor, admin)
- Không cần permissions phức tạp
- Không cần role hierarchy
- **Simplicity > perfect normalization**

**Khi nào cần Role table?**
```sql
-- Nếu có RBAC phức tạp
CREATE TABLE "Role" (
  role_id UUID PRIMARY KEY,
  role_name VARCHAR(50),
  permissions JSONB  -- Detailed permissions
);

CREATE TABLE "User" (
  user_id UUID PRIMARY KEY,
  role_id UUID REFERENCES "Role"  -- FK to Role
);

CREATE TABLE "RolePermission" (
  role_id UUID,
  resource VARCHAR(50),
  actions VARCHAR(20)[]
);
```

**Case 3: JSONB fields (Quiz.questions, Attempt.answers)**

```sql
-- Quiz table
CREATE TABLE "Quiz" (
  quiz_id UUID PRIMARY KEY,
  questions JSONB  -- ⚠️ Array of questions (không normalized)
);
```

**Đây có phải denormalization?**
- **Không phải** traditional denormalization
- **Design choice**: JSON for flexibility vs separate Question table
- Đã phân tích ở Q9.1

**3. Normalization summary table:**

| Table | Normal Form | Denormalization | Justification |
|-------|-------------|-----------------|---------------|
| User | ✅ 3NF | Không | Clean structure |
| UserProfile | ✅ 3NF | Không | Separated from User |
| Notification | ✅ 3NF | Không | Simple notifications |
| Course | ✅ 3NF | Không | Core entity |
| Module | ✅ 3NF | Không | Course structure |
| Lecture | ✅ 3NF | Không | Content unit |
| Assignment | ✅ 3NF | JSONB config | Flexibility |
| Quiz | ✅ 3NF | JSONB questions | Atomic quiz unit |
| Question | N/A | Merged into Quiz | Design choice |
| Attempt | ✅ 3NF | JSONB answers | Atomic attempt |
| Enrollment | ⚠️ 2.5NF | completion_percentage | Performance |
| Progress | ✅ 3NF | Không | Tracking table |
| Class | ✅ 3NF | JSONB schedules | Simple schedules |
| Certificate | ✅ 3NF | Không | Credential record |
| AssignmentSubmission | ✅ 3NF | Không | Submission record |

**4. Denormalization best practices:**

```sql
-- ✅ Good: Denormalize for performance with maintenance plan
CREATE TABLE "Enrollment" (
  completion_percentage DECIMAL(5,2),  -- Cached value
  last_progress_update TIMESTAMPTZ     -- Track when updated
);

-- Trigger to auto-update (optional)
CREATE OR REPLACE FUNCTION update_completion_percentage()
RETURNS TRIGGER AS $$
BEGIN
  UPDATE "Enrollment"
  SET completion_percentage = (
    SELECT COUNT(*) * 100.0 / (
      SELECT COUNT(*) FROM "Lecture" l
      JOIN "Module" m ON l.module_id = m.module_id
      WHERE m.course_id = NEW.course_id
    )
    FROM "Progress"
    WHERE enrollment_id = NEW.enrollment_id
  ),
  last_progress_update = CURRENT_TIMESTAMP
  WHERE enrollment_id = NEW.enrollment_id;

  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_update_completion
AFTER INSERT OR DELETE ON "Progress"
FOR EACH ROW EXECUTE FUNCTION update_completion_percentage();
```

**❌ Bad: Denormalize without consistency maintenance**
```sql
-- Missing: How to update completion_percentage?
-- Missing: What if Progress deleted?
-- Missing: Handling race conditions
```

**Kết luận:**
- **Core v1.0 chủ yếu tuân thủ 3NF**
- **Strategic denormalization**: Enrollment.completion_percentage (performance)
- **Design choices**: JSONB fields (flexibility vs normalization)
- **Trade-off**: Complexity vs Performance vs Maintainability
- **Maintenance plan**: Triggers/application logic để maintain consistency

---

### Q10.2: Làm thế nào để đảm bảo referential integrity với ON DELETE CASCADE, SET NULL, RESTRICT?

**Trả lời:**

**1. ON DELETE behaviors overview:**

PostgreSQL hỗ trợ 5 loại ON DELETE:
1. **CASCADE**: Tự động xóa child records
2. **SET NULL**: Set FK thành NULL khi parent deleted
3. **SET DEFAULT**: Set FK về default value
4. **RESTRICT**: Ngăn delete parent nếu có children (default)
5. **NO ACTION**: Giống RESTRICT nhưng check cuối transaction

**2. ON DELETE CASCADE - Cascading deletes:**

**Use cases: Parent-child relationship mạnh**

```sql
-- Course → Module → Lecture: CASCADE chain
CREATE TABLE "Module" (
  module_id UUID PRIMARY KEY,
  course_id UUID REFERENCES "Course"(course_id) ON DELETE CASCADE
);

CREATE TABLE "Lecture" (
  lecture_id UUID PRIMARY KEY,
  module_id UUID REFERENCES "Module"(module_id) ON DELETE CASCADE
);

CREATE TABLE "Assignment" (
  assignment_id UUID PRIMARY KEY,
  module_id UUID REFERENCES "Module"(module_id) ON DELETE CASCADE
);
```

**Behavior:**
```sql
-- Delete course
DELETE FROM "Course" WHERE course_id = 'course-123';

-- Automatically deletes:
-- 1. All Modules in course-123
-- 2. All Lectures in those modules
-- 3. All Assignments in those modules
-- CASCADE propagates through FK chain
```

**Benefits:**
- **Data integrity**: Không còn orphan records
- **Simplicity**: Không cần manual cleanup
- **Atomic**: All deletes trong một transaction

**Risks & mitigation:**
```python
# ⚠️ Risk: Accidental mass deletion
def delete_course(course_id):
    # ❌ Dangerous: No confirmation, no backup
    conn.execute("DELETE FROM \"Course\" WHERE course_id = %s", (course_id,))

# ✅ Better: Soft delete + archival
def archive_course(course_id):
    # Soft delete instead of hard delete
    conn.execute("""
        UPDATE "Course"
        SET status = 'archived',
            archived_at = CURRENT_TIMESTAMP
        WHERE course_id = %s
    """, (course_id,))

    # Later: Hard delete with confirmation
    # Backup first: pg_dump specific course data

# ✅ Best: Multi-step process
def delete_course_safely(course_id):
    # 1. Check dependencies
    stats = conn.execute("""
        SELECT
            (SELECT COUNT(*) FROM "Module" WHERE course_id = %s) as modules,
            (SELECT COUNT(*) FROM "Enrollment" WHERE course_id = %s) as enrollments
    """, (course_id, course_id)).fetchone()

    # 2. Require confirmation if has data
    if stats['modules'] > 0 or stats['enrollments'] > 0:
        return {
            "error": "Course has dependencies",
            "stats": stats,
            "action_required": "Archive or force delete"
        }

    # 3. Backup before delete
    backup_course_data(course_id)

    # 4. Delete with transaction
    conn.execute("DELETE FROM \"Course\" WHERE course_id = %s", (course_id,))
    conn.commit()
```

**3. ON DELETE SET NULL - Preserve children:**

**Use cases: Optional relationships**

```sql
-- Enrollment.class_id: Optional (self-paced courses)
CREATE TABLE "Enrollment" (
  enrollment_id UUID PRIMARY KEY,
  class_id UUID REFERENCES "Class"(class_id) ON DELETE SET NULL
);

-- Notification.user_id: Keep notifications even if user deleted
CREATE TABLE "Notification" (
  notification_id UUID PRIMARY KEY,
  user_id UUID REFERENCES "User"(user_id) ON DELETE SET NULL,
  message TEXT NOT NULL  -- Message preserved
);
```

**Behavior:**
```sql
-- Delete class
DELETE FROM "Class" WHERE class_id = 'class-456';

-- Result:
SELECT * FROM "Enrollment" WHERE class_id IS NULL;
-- Enrollments preserved but class_id = NULL
-- Now self-paced mode
```

**Benefits:**
- **Data preservation**: Children not deleted
- **Flexibility**: Children can exist independently
- **Audit trail**: Know that parent existed but was removed

**Handling NULL values:**
```python
# Application must handle NULL FKs
@app.get("/enrollments/{enrollment_id}")
def get_enrollment(enrollment_id):
    enrollment = conn.execute("""
        SELECT e.*, c.class_name, c.start_date
        FROM "Enrollment" e
        LEFT JOIN "Class" c ON e.class_id = c.class_id
        WHERE e.enrollment_id = %s
    """, (enrollment_id,)).fetchone()

    return {
        "enrollment_id": enrollment['enrollment_id'],
        "mode": "class-based" if enrollment['class_id'] else "self-paced",
        "class": {
            "class_id": enrollment['class_id'],
            "class_name": enrollment['class_name'],
            "start_date": enrollment['start_date']
        } if enrollment['class_id'] else None
    }
```

**4. ON DELETE RESTRICT - Prevent deletion:**

**Use cases: Critical data protection**

```sql
-- Cannot delete User if has Enrollments
CREATE TABLE "Enrollment" (
  enrollment_id UUID PRIMARY KEY,
  user_id UUID REFERENCES "User"(user_id) ON DELETE RESTRICT
);

-- Cannot delete Course if has Enrollments
CREATE TABLE "Enrollment" (
  enrollment_id UUID PRIMARY KEY,
  course_id UUID REFERENCES "Course"(course_id) ON DELETE RESTRICT
);
```

**Behavior:**
```sql
-- Try to delete user
DELETE FROM "User" WHERE user_id = 'user-789';

-- Error:
-- ERROR:  update or delete on table "User" violates foreign key constraint
-- DETAIL:  Key (user_id)=(user-789) is still referenced from table "Enrollment"
```

**Benefits:**
- **Data safety**: Prevent accidental data loss
- **Business rules**: Enforce that users with enrollments can't be deleted
- **Explicit workflow**: Force proper cleanup process

**Proper deletion workflow:**
```python
def delete_user(user_id):
    """
    Cannot delete user directly if has enrollments.
    Must unenroll first or transfer enrollments.
    """
    # Check dependencies
    has_enrollments = conn.execute("""
        SELECT EXISTS(
            SELECT 1 FROM "Enrollment" WHERE user_id = %s
        )
    """, (user_id,)).fetchone()[0]

    if has_enrollments:
        return {
            "error": "Cannot delete user with active enrollments",
            "action": "Unenroll user first or use deactivate_user()"
        }

    # Safe to delete
    conn.execute("DELETE FROM \"User\" WHERE user_id = %s", (user_id,))
    conn.commit()

def deactivate_user(user_id):
    """
    Soft delete: Deactivate instead of delete.
    """
    conn.execute("""
        UPDATE "User"
        SET status = 'inactive',
            deactivated_at = CURRENT_TIMESTAMP
        WHERE user_id = %s
    """, (user_id,))
```

**5. Decision matrix - Chọn ON DELETE behavior:**

| Relationship | Parent | Child | ON DELETE | Reasoning |
|--------------|--------|-------|-----------|-----------|
| Course → Module | Course | Module | CASCADE | Modules không exist without course |
| Module → Lecture | Module | Lecture | CASCADE | Lectures part of module structure |
| Module → Assignment | Module | Assignment | CASCADE | Assignments belong to module |
| Quiz → Attempt | Quiz | Attempt | RESTRICT | Keep quiz if students took it |
| User → Enrollment | User | Enrollment | RESTRICT | Don't lose enrollment history |
| Course → Enrollment | Course | Enrollment | RESTRICT | Course with enrollments can't delete |
| Class → Enrollment | Class | Enrollment | SET NULL | Enrollment becomes self-paced |
| User → Notification | User | Notification | SET NULL | Keep notification for audit |
| Enrollment → Progress | Enrollment | Progress | CASCADE | Progress meaningless without enrollment |
| Enrollment → Attempt | Enrollment | Attempt | CASCADE | Attempts belong to enrollment |
| Enrollment → Certificate | Enrollment | Certificate | RESTRICT | Certificate is permanent credential |

**6. Complex scenarios:**

**Scenario 1: Soft delete + ON DELETE behaviors**

```sql
-- User table với soft delete
CREATE TABLE "User" (
  user_id UUID PRIMARY KEY,
  email VARCHAR(255),
  status VARCHAR(20) DEFAULT 'active',
  deleted_at TIMESTAMPTZ
);

-- Enrollment vẫn reference User
CREATE TABLE "Enrollment" (
  enrollment_id UUID PRIMARY KEY,
  user_id UUID REFERENCES "User"(user_id) ON DELETE RESTRICT
);

-- Soft delete user (không trigger ON DELETE)
UPDATE "User" SET status = 'deleted', deleted_at = CURRENT_TIMESTAMP
WHERE user_id = 'xxx';

-- FK constraint không affected vì row vẫn tồn tại
-- Enrollments vẫn reference user_id

-- Application must filter:
SELECT * FROM "User" WHERE status = 'active';  -- Exclude soft-deleted
```

**Scenario 2: Transfer ownership before delete**

```python
def transfer_course_ownership(course_id, old_instructor_id, new_instructor_id):
    """
    Transfer course to new instructor before deleting old instructor.
    """
    with conn.transaction():
        # Transfer courses
        conn.execute("""
            UPDATE "Course"
            SET instructor_id = %s,
                updated_at = CURRENT_TIMESTAMP
            WHERE instructor_id = %s
        """, (new_instructor_id, old_instructor_id))

        # Now safe to delete old instructor
        conn.execute("""
            DELETE FROM "User" WHERE user_id = %s
        """, (old_instructor_id,))
```

**Scenario 3: Conditional CASCADE with triggers**

```sql
-- Custom logic: Only CASCADE if course has no enrollments
CREATE OR REPLACE FUNCTION prevent_delete_if_enrolled()
RETURNS TRIGGER AS $$
BEGIN
  IF EXISTS (SELECT 1 FROM "Enrollment" WHERE course_id = OLD.course_id) THEN
    RAISE EXCEPTION 'Cannot delete course with active enrollments';
  END IF;
  RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_prevent_course_delete
BEFORE DELETE ON "Course"
FOR EACH ROW EXECUTE FUNCTION prevent_delete_if_enrolled();
```

**7. Testing referential integrity:**

```sql
-- Test CASCADE
BEGIN;
  DELETE FROM "Course" WHERE course_id = 'test-course';
  -- Check modules deleted
  SELECT COUNT(*) FROM "Module" WHERE course_id = 'test-course';  -- Should be 0
ROLLBACK;

-- Test RESTRICT
BEGIN;
  -- Create enrollment
  INSERT INTO "Enrollment" (enrollment_id, user_id, course_id)
  VALUES (gen_random_uuid(), 'user-1', 'course-1');

  -- Try delete course (should fail)
  DELETE FROM "Course" WHERE course_id = 'course-1';
  -- ERROR: violates foreign key constraint
ROLLBACK;

-- Test SET NULL
BEGIN;
  DELETE FROM "Class" WHERE class_id = 'class-1';
  -- Check enrollment class_id set to NULL
  SELECT class_id FROM "Enrollment" WHERE enrollment_id = 'enrollment-1';  -- NULL
ROLLBACK;
```

**Kết luận:**
- **CASCADE**: Parent-child mạnh (Course→Module→Lecture)
- **SET NULL**: Optional relationships (Enrollment.class_id)
- **RESTRICT**: Critical data (User/Course với Enrollments)
- **Soft delete**: Preferred over hard delete cho nhiều use cases
- **Testing**: Luôn test FK behaviors với transactions
- **Application logic**: Handle NULLs, implement proper deletion workflows

---

### Q10.3: Các constraints (UNIQUE, CHECK, NOT NULL) được sử dụng như thế nào để enforce business rules?

**Trả lời:**

**1. UNIQUE constraints:**

**Use case 1: Email uniqueness**

```sql
CREATE TABLE "User" (
  user_id UUID PRIMARY KEY,
  email VARCHAR(255) UNIQUE NOT NULL,  -- ✅ One email per account
  ...
);

-- Partial unique index: Case-insensitive email
CREATE UNIQUE INDEX idx_user_email_lower
ON "User" (LOWER(email));
-- Prevents: user@example.com vs USER@EXAMPLE.COM
```

**Benefits:**
- Database-level enforcement (không thể bypass)
- Automatic error on duplicate insert
- Index for fast lookup

**Application handling:**
```python
from psycopg2 import IntegrityError

@app.post("/register")
def register_user(email, password):
    try:
        conn.execute("""
            INSERT INTO "User" (user_id, email, password_hash, role)
            VALUES (%s, %s, %s, %s)
        """, (uuid.uuid4(), email.lower(), hash_password(password), 'student'))
        conn.commit()
        return {"message": "User registered successfully"}

    except IntegrityError as e:
        if 'unique constraint' in str(e):
            return {"error": "Email already registered"}, 409
        raise
```

**Use case 2: Composite unique constraint**

```sql
-- Một user chỉ enroll một course một lần
CREATE TABLE "Enrollment" (
  enrollment_id UUID PRIMARY KEY,
  user_id UUID REFERENCES "User",
  course_id UUID REFERENCES "Course",
  UNIQUE(user_id, course_id)  -- ✅ Composite unique
);

-- Error nếu enroll lại:
INSERT INTO "Enrollment" (enrollment_id, user_id, course_id)
VALUES (gen_random_uuid(), 'user-1', 'course-1');
-- Second insert: ERROR: duplicate key value violates unique constraint
```

**Use case 3: Conditional uniqueness**

```sql
-- Certificate number unique per course (not globally)
CREATE TABLE "Certificate" (
  certificate_id UUID PRIMARY KEY,
  enrollment_id UUID REFERENCES "Enrollment",
  certificate_number VARCHAR(50),
  UNIQUE(certificate_number, course_id)  -- Unique within course
);

-- Partial unique index: Only for active certificates
CREATE UNIQUE INDEX idx_certificate_number_active
ON "Certificate" (certificate_number)
WHERE status = 'active';
-- Allows duplicate cert numbers if one is revoked
```

**2. CHECK constraints:**

**Use case 1: Enum-like values**

```sql
CREATE TABLE "User" (
  user_id UUID PRIMARY KEY,
  role VARCHAR(20) CHECK (role IN ('student', 'instructor', 'admin')),
  status VARCHAR(20) CHECK (status IN ('active', 'inactive', 'suspended')),
  ...
);

-- Error nếu invalid role:
INSERT INTO "User" (user_id, role) VALUES (gen_random_uuid(), 'superuser');
-- ERROR: new row violates check constraint "user_role_check"
```

**Use case 2: Numeric ranges**

```sql
CREATE TABLE "Quiz" (
  quiz_id UUID PRIMARY KEY,
  passing_score DECIMAL(5,2) CHECK (passing_score >= 0 AND passing_score <= 100),
  time_limit_minutes INT CHECK (time_limit_minutes > 0),
  ...
);

CREATE TABLE "Attempt" (
  attempt_id UUID PRIMARY KEY,
  score DECIMAL(5,2) CHECK (score >= 0),  -- Score cannot be negative
  ...
);

CREATE TABLE "Enrollment" (
  enrollment_id UUID PRIMARY KEY,
  completion_percentage DECIMAL(5,2) CHECK (
    completion_percentage >= 0 AND completion_percentage <= 100
  ),
  ...
);
```

**Use case 3: Date validations**

```sql
CREATE TABLE "Class" (
  class_id UUID PRIMARY KEY,
  start_date DATE NOT NULL,
  end_date DATE NOT NULL,
  CHECK (end_date >= start_date),  -- ✅ End after start
  ...
);

CREATE TABLE "Quiz" (
  quiz_id UUID PRIMARY KEY,
  available_from TIMESTAMPTZ,
  available_until TIMESTAMPTZ,
  CHECK (available_until > available_from),
  ...
);
```

**Use case 4: Business logic constraints**

```sql
-- AssignmentSubmission: Score không exceed max_score
CREATE TABLE "AssignmentSubmission" (
  submission_id UUID PRIMARY KEY,
  score DECIMAL(5,2),
  max_score DECIMAL(5,2),
  CHECK (score IS NULL OR score <= max_score),  -- ✅ Score ≤ max_score
  ...
);

-- Attempt: Cannot exceed max_attempts
-- (This requires application logic, can't enforce with CHECK alone)
```

**Complex CHECK constraints:**

```sql
-- Status transitions
CREATE TABLE "Course" (
  course_id UUID PRIMARY KEY,
  status VARCHAR(20) CHECK (status IN ('draft', 'published', 'archived')),
  published_at TIMESTAMPTZ,
  CHECK (
    (status = 'published' AND published_at IS NOT NULL) OR
    (status != 'published' AND published_at IS NULL)
  )  -- ✅ Published courses must have published_at
);
```

**3. NOT NULL constraints:**

**Use case 1: Required fields**

```sql
CREATE TABLE "User" (
  user_id UUID PRIMARY KEY,
  email VARCHAR(255) UNIQUE NOT NULL,      -- ✅ Required
  password_hash VARCHAR(255) NOT NULL,     -- ✅ Required
  full_name VARCHAR(200) NOT NULL,         -- ✅ Required
  role VARCHAR(20) NOT NULL,               -- ✅ Required
  created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

  -- Optional fields
  bio TEXT,                                -- ⚠️ Nullable
  avatar_url VARCHAR(500),                 -- ⚠️ Nullable
  deactivated_at TIMESTAMPTZ               -- ⚠️ Nullable
);
```

**Use case 2: Conditional NOT NULL (with CHECK)**

```sql
CREATE TABLE "Certificate" (
  certificate_id UUID PRIMARY KEY,
  enrollment_id UUID NOT NULL,
  issued_at TIMESTAMPTZ NOT NULL,
  revoked_at TIMESTAMPTZ,
  revocation_reason TEXT,
  CHECK (
    (revoked_at IS NULL AND revocation_reason IS NULL) OR
    (revoked_at IS NOT NULL AND revocation_reason IS NOT NULL)
  )  -- ✅ If revoked, must have reason
);
```

**Use case 3: Application-level required fields**

```python
# Backend validation before database
from pydantic import BaseModel, EmailStr, Field

class UserCreate(BaseModel):
    email: EmailStr  # Auto validates email format
    password: str = Field(min_length=8)  # Min length
    full_name: str = Field(min_length=1, max_length=200)
    role: str = Field(pattern='^(student|instructor|admin)$')

@app.post("/users")
def create_user(user: UserCreate):
    # Pydantic already validated required fields + formats
    # Database constraints as second layer of defense
    conn.execute("""
        INSERT INTO "User" (user_id, email, password_hash, full_name, role)
        VALUES (%s, %s, %s, %s, %s)
    """, (uuid.uuid4(), user.email, hash(user.password), user.full_name, user.role))
```

**4. Combining constraints for business rules:**

**Example 1: Quiz availability rules**

```sql
CREATE TABLE "Quiz" (
  quiz_id UUID PRIMARY KEY,
  title VARCHAR(200) NOT NULL,
  passing_score DECIMAL(5,2) NOT NULL CHECK (passing_score >= 0 AND passing_score <= 100),
  time_limit_minutes INT CHECK (time_limit_minutes IS NULL OR time_limit_minutes > 0),
  max_attempts INT CHECK (max_attempts IS NULL OR max_attempts > 0),
  available_from TIMESTAMPTZ,
  available_until TIMESTAMPTZ,
  CHECK (available_until IS NULL OR available_from IS NULL OR available_until > available_from),
  status VARCHAR(20) NOT NULL CHECK (status IN ('draft', 'published', 'archived')) DEFAULT 'draft'
);
```

**Business rules enforced:**
1. Passing score must be 0-100
2. Time limit must be positive (if set)
3. Max attempts must be positive (if set)
4. Available period must be valid (end > start)
5. Status must be valid enum value

**Example 2: Enrollment business rules**

```sql
CREATE TABLE "Enrollment" (
  enrollment_id UUID PRIMARY KEY,
  user_id UUID NOT NULL REFERENCES "User"(user_id) ON DELETE RESTRICT,
  course_id UUID NOT NULL REFERENCES "Course"(course_id) ON DELETE RESTRICT,
  class_id UUID REFERENCES "Class"(class_id) ON DELETE SET NULL,
  enrolled_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
  completion_percentage DECIMAL(5,2) DEFAULT 0 CHECK (
    completion_percentage >= 0 AND completion_percentage <= 100
  ),
  status VARCHAR(20) NOT NULL CHECK (status IN ('active', 'completed', 'dropped')) DEFAULT 'active',
  UNIQUE(user_id, course_id)  -- One enrollment per user per course
);
```

**Business rules enforced:**
1. User and Course required (NOT NULL + FK)
2. One enrollment per user-course pair (UNIQUE)
3. Completion percentage 0-100
4. Status must be valid enum
5. Enrolled timestamp required

**5. Limitations and workarounds:**

**Limitation 1: CHECK cannot reference other tables**

```sql
-- ❌ Cannot do this:
CREATE TABLE "Attempt" (
  attempt_id UUID PRIMARY KEY,
  quiz_id UUID REFERENCES "Quiz",
  score DECIMAL(5,2),
  CHECK (score <= (SELECT passing_score FROM "Quiz" WHERE quiz_id = Attempt.quiz_id))
);
-- ERROR: cannot use subquery in check constraint
```

**Workaround: Application logic or triggers**

```sql
-- ✅ Use trigger instead
CREATE OR REPLACE FUNCTION validate_attempt_score()
RETURNS TRIGGER AS $$
DECLARE
  max_score DECIMAL;
BEGIN
  SELECT passing_score INTO max_score
  FROM "Quiz"
  WHERE quiz_id = NEW.quiz_id;

  IF NEW.score > max_score THEN
    RAISE EXCEPTION 'Score % exceeds max score %', NEW.score, max_score;
  END IF;

  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_validate_score
BEFORE INSERT OR UPDATE ON "Attempt"
FOR EACH ROW EXECUTE FUNCTION validate_attempt_score();
```

**Limitation 2: Complex business rules**

```python
# Application-level validation for complex rules
def enroll_student(user_id, course_id, class_id=None):
    """
    Business rules:
    1. User must be 'student' role
    2. Course must be 'published'
    3. If class_id provided, class must not be full
    4. If class_id provided, class must not have started
    """
    # Validation queries
    user = conn.execute("""
        SELECT role FROM "User" WHERE user_id = %s
    """, (user_id,)).fetchone()

    if user['role'] != 'student':
        return {"error": "Only students can enroll"}, 403

    course = conn.execute("""
        SELECT status FROM "Course" WHERE course_id = %s
    """, (course_id,)).fetchone()

    if course['status'] != 'published':
        return {"error": "Course not available"}, 400

    if class_id:
        class_data = conn.execute("""
            SELECT
                c.max_students,
                c.start_date,
                COUNT(e.enrollment_id) as current_students
            FROM "Class" c
            LEFT JOIN "Enrollment" e ON c.class_id = e.class_id
            WHERE c.class_id = %s
            GROUP BY c.class_id
        """, (class_id,)).fetchone()

        if class_data['current_students'] >= class_data['max_students']:
            return {"error": "Class is full"}, 400

        if class_data['start_date'] < date.today():
            return {"error": "Class already started"}, 400

    # All validations passed, create enrollment
    conn.execute("""
        INSERT INTO "Enrollment" (enrollment_id, user_id, course_id, class_id)
        VALUES (%s, %s, %s, %s)
    """, (uuid.uuid4(), user_id, course_id, class_id))
    conn.commit()

    return {"message": "Enrolled successfully"}
```

**6. Testing constraints:**

```sql
-- Test UNIQUE
BEGIN;
  INSERT INTO "User" (user_id, email, password_hash, full_name, role)
  VALUES (gen_random_uuid(), 'test@example.com', 'hash', 'Test User', 'student');

  -- Should fail
  INSERT INTO "User" (user_id, email, password_hash, full_name, role)
  VALUES (gen_random_uuid(), 'test@example.com', 'hash2', 'Test User 2', 'student');
  -- ERROR: duplicate key value
ROLLBACK;

-- Test CHECK
BEGIN;
  INSERT INTO "Quiz" (quiz_id, title, passing_score)
  VALUES (gen_random_uuid(), 'Test Quiz', 150);
  -- ERROR: violates check constraint (passing_score must be 0-100)
ROLLBACK;

-- Test NOT NULL
BEGIN;
  INSERT INTO "User" (user_id, email, role)
  VALUES (gen_random_uuid(), NULL, 'student');
  -- ERROR: null value in column "email" violates not-null constraint
ROLLBACK;
```

**7. Constraint summary table:**

| Constraint Type | Purpose | Example | Enforcement Level |
|----------------|---------|---------|-------------------|
| UNIQUE | No duplicates | User.email | Database |
| CHECK | Value validation | Quiz.passing_score 0-100 | Database |
| NOT NULL | Required fields | User.email NOT NULL | Database |
| FOREIGN KEY | Referential integrity | Module.course_id → Course | Database |
| Composite UNIQUE | Multi-column uniqueness | (user_id, course_id) | Database |
| Partial UNIQUE | Conditional uniqueness | email WHERE status='active' | Database (index) |
| Complex business rules | Multi-table validation | Max attempts, class capacity | Application + Triggers |

**Kết luận:**
- **Database constraints**: First line of defense (UNIQUE, CHECK, NOT NULL, FK)
- **Application validation**: Complex business rules, user-friendly errors
- **Triggers**: Cross-table validations, computed fields
- **Layered approach**: Database + Application + UI validation
- **Testing**: Test all constraint violations to ensure proper error handling

---

## Section 11: Additional Design Questions

### Q11.1: Tại sao sử dụng UUID thay vì auto-increment INTEGER cho primary keys?

**Trả lời:**

**Đã được trả lời chi tiết ở Q1.2** (Section 1: General Design Questions).

**Tóm tắt lý do:**
1. **Distributed systems**: UUID có thể generate ở nhiều servers không cần coordination
2. **Security**: Không predictable như INT (1, 2, 3...)
3. **Merging data**: Dễ dàng merge từ nhiều sources không conflict
4. **Globally unique**: Có thể dùng làm external IDs
5. **No sequence bottleneck**: Không có lock contention trên sequence

**Trade-offs:**
- ✅ **Pros**: Security, scalability, flexibility
- ⚠️ **Cons**: Storage (16 bytes vs 4 bytes), index size, harder to debug

**Kết luận**: UUID phù hợp với LMS system vì security và potential scaling requirements.

---

### Q11.2: Database có support multi-tenancy (nhiều organizations) không?

**Trả lời:**

**Hiện tại: Core v1.0 KHÔNG support multi-tenancy**

Database được thiết kế cho **single organization** (một trường học, một công ty).

**Nếu cần multi-tenancy, có 3 approaches:**

**Approach 1: Separate Database per Tenant**

```sql
-- Database: tenant_org1
CREATE DATABASE tenant_org1;
\c tenant_org1;
-- Create all tables...

-- Database: tenant_org2
CREATE DATABASE tenant_org2;
\c tenant_org2;
-- Create all tables...
```

**Pros:**
- ✅ Complete isolation
- ✅ Easy backup/restore per tenant
- ✅ Different schema versions possible
- ✅ No cross-tenant data leakage

**Cons:**
- ❌ Resource overhead (one DB per tenant)
- ❌ Difficult to query across tenants
- ❌ Schema migrations need to run on all DBs

**Approach 2: Separate Schema per Tenant**

```sql
-- Schema: tenant_org1
CREATE SCHEMA tenant_org1;
CREATE TABLE tenant_org1."User" (...);
CREATE TABLE tenant_org1."Course" (...);

-- Schema: tenant_org2
CREATE SCHEMA tenant_org2;
CREATE TABLE tenant_org2."User" (...);
CREATE TABLE tenant_org2."Course" (...);
```

**Pros:**
- ✅ Good isolation
- ✅ Shared database connection pool
- ✅ Easier than separate DBs

**Cons:**
- ❌ Still have schema management complexity
- ❌ Need to SET search_path for each query

**Approach 3: Shared Schema with tenant_id column**

```sql
-- Add tenant_id to all tables
CREATE TABLE "User" (
  user_id UUID PRIMARY KEY,
  tenant_id UUID NOT NULL REFERENCES "Tenant"(tenant_id),
  email VARCHAR(255),
  ...,
  UNIQUE(tenant_id, email)  -- Email unique per tenant
);

CREATE TABLE "Course" (
  course_id UUID PRIMARY KEY,
  tenant_id UUID NOT NULL REFERENCES "Tenant"(tenant_id),
  title VARCHAR(200),
  ...
);

CREATE TABLE "Tenant" (
  tenant_id UUID PRIMARY KEY,
  tenant_name VARCHAR(200),
  subdomain VARCHAR(100) UNIQUE,
  settings JSONB,
  created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

-- Row Level Security
ALTER TABLE "User" ENABLE ROW LEVEL SECURITY;

CREATE POLICY tenant_isolation_policy ON "User"
USING (tenant_id = current_setting('app.current_tenant_id')::UUID);
```

**Pros:**
- ✅ Simplest schema
- ✅ Easy analytics across tenants
- ✅ Efficient resource usage

**Cons:**
- ❌ Must include tenant_id in every query
- ❌ Risk of data leakage if tenant_id forgotten
- ❌ Shared indexes (larger size)

**Application implementation with tenant_id:**

```python
# Middleware to set tenant context
from flask import Flask, request, g

app = Flask(__name__)

@app.before_request
def set_tenant_context():
    # Extract tenant from subdomain or header
    subdomain = request.host.split('.')[0]  # org1.example.com → org1

    # Or from JWT token
    # tenant_id = decode_jwt(request.headers['Authorization'])['tenant_id']

    conn = get_db_connection()
    cur = conn.cursor()
    cur.execute("""
        SELECT tenant_id FROM "Tenant" WHERE subdomain = %s
    """, (subdomain,))
    tenant = cur.fetchone()

    if not tenant:
        abort(404, "Tenant not found")

    g.tenant_id = tenant['tenant_id']

    # Set PostgreSQL session variable for RLS
    cur.execute("""
        SET app.current_tenant_id = %s
    """, (str(g.tenant_id),))

# All queries automatically filtered by RLS
@app.get("/courses")
def get_courses():
    cur = get_db_connection().cursor()

    # RLS automatically adds: WHERE tenant_id = current_setting('app.current_tenant_id')
    cur.execute("SELECT * FROM \"Course\"")

    return jsonify(cur.fetchall())

# Explicit tenant_id in queries (without RLS)
@app.get("/courses")
def get_courses_explicit():
    cur = get_db_connection().cursor()
    cur.execute("""
        SELECT * FROM "Course" WHERE tenant_id = %s
    """, (g.tenant_id,))

    return jsonify(cur.fetchall())
```

**Recommendation:**
- **Small scale (< 100 tenants)**: Approach 3 (tenant_id column + RLS)
- **Medium scale (100-1000 tenants)**: Approach 2 (separate schemas)
- **Large scale (> 1000 tenants)**: Approach 1 (separate databases) hoặc shard by tenant

**Core v1.0**: Single-tenant design, có thể migrate sang multi-tenant nếu cần.

---

### Q11.3: Làm thế nào để handle file uploads (videos, documents, images)?

**Trả lời:**

**Core v1.0 design: Store file URLs, NOT binary data**

**1. Table structures cho files:**

```sql
-- Lecture có video/materials
CREATE TABLE "Lecture" (
  lecture_id UUID PRIMARY KEY,
  title VARCHAR(200),
  content TEXT,
  video_url VARCHAR(500),      -- ✅ URL to video file
  duration_seconds INT,
  resources JSONB,             -- ✅ Array of file URLs
  ...
);

-- resources JSONB structure:
{
  "resources": [
    {
      "type": "pdf",
      "filename": "lecture-notes.pdf",
      "url": "https://cdn.example.com/courses/course-123/lecture-notes.pdf",
      "size_bytes": 2048576
    },
    {
      "type": "video",
      "filename": "demo.mp4",
      "url": "https://cdn.example.com/courses/course-123/demo.mp4",
      "size_bytes": 104857600,
      "duration_seconds": 320
    }
  ]
}

-- Assignment submissions với files
CREATE TABLE "AssignmentSubmission" (
  submission_id UUID PRIMARY KEY,
  file_url VARCHAR(500),       -- ✅ URL to submitted file
  ...
);

-- User avatar
CREATE TABLE "User" (
  user_id UUID PRIMARY KEY,
  avatar_url VARCHAR(500),     -- ✅ URL to avatar image
  ...
);
```

**2. Storage approaches:**

**Option A: Cloud Storage (AWS S3, Google Cloud Storage, Azure Blob)**

```python
import boto3
from botocore.exceptions import ClientError
import uuid
import mimetypes

# AWS S3 configuration
s3_client = boto3.client('s3',
    aws_access_key_id='your-access-key',
    aws_secret_access_key='your-secret-key',
    region_name='us-east-1'
)

BUCKET_NAME = 'b-learning-files'
CDN_URL = 'https://cdn.example.com'  # CloudFront URL

@app.post("/lectures/{lecture_id}/upload-video")
async def upload_lecture_video(lecture_id: str, file: UploadFile):
    """
    Upload video to S3 and save URL to database.
    """
    # Generate unique filename
    file_ext = file.filename.split('.')[-1]
    unique_filename = f"lectures/{lecture_id}/{uuid.uuid4()}.{file_ext}"

    # Upload to S3
    try:
        s3_client.upload_fileobj(
            file.file,
            BUCKET_NAME,
            unique_filename,
            ExtraArgs={
                'ContentType': file.content_type,
                'ACL': 'public-read',  # Or use signed URLs
                'CacheControl': 'max-age=31536000'  # 1 year cache
            }
        )
    except ClientError as e:
        return {"error": f"Upload failed: {str(e)}"}, 500

    # Generate CDN URL
    file_url = f"{CDN_URL}/{unique_filename}"

    # Save to database
    conn = get_db_connection()
    conn.execute("""
        UPDATE "Lecture"
        SET video_url = %s,
            updated_at = CURRENT_TIMESTAMP
        WHERE lecture_id = %s
    """, (file_url, lecture_id))
    conn.commit()

    return {
        "message": "Video uploaded successfully",
        "url": file_url
    }

# Get video with signed URL (for private content)
@app.get("/lectures/{lecture_id}/video")
def get_lecture_video(lecture_id: str, current_user: User):
    """
    Return signed URL for private video access.
    """
    # Check authorization
    enrollment = conn.execute("""
        SELECT e.enrollment_id
        FROM "Enrollment" e
        JOIN "Module" m ON m.course_id = e.course_id
        JOIN "Lecture" l ON l.module_id = m.module_id
        WHERE l.lecture_id = %s AND e.user_id = %s
    """, (lecture_id, current_user.user_id)).fetchone()

    if not enrollment:
        return {"error": "Not enrolled in this course"}, 403

    # Get video URL
    lecture = conn.execute("""
        SELECT video_url FROM "Lecture" WHERE lecture_id = %s
    """, (lecture_id,)).fetchone()

    # Generate signed URL (valid for 1 hour)
    s3_key = lecture['video_url'].replace(CDN_URL + '/', '')
    signed_url = s3_client.generate_presigned_url(
        'get_object',
        Params={'Bucket': BUCKET_NAME, 'Key': s3_key},
        ExpiresIn=3600  # 1 hour
    )

    return {"video_url": signed_url}
```

**Option B: Local filesystem (for development/small scale)**

```python
import os
import shutil
from pathlib import Path

UPLOAD_DIR = Path("/var/www/uploads")
STATIC_URL = "https://example.com/static/uploads"

@app.post("/lectures/{lecture_id}/upload-video")
async def upload_lecture_video_local(lecture_id: str, file: UploadFile):
    """
    Upload video to local filesystem.
    """
    # Create directory structure
    lecture_dir = UPLOAD_DIR / "lectures" / lecture_id
    lecture_dir.mkdir(parents=True, exist_ok=True)

    # Save file
    file_ext = file.filename.split('.')[-1]
    unique_filename = f"{uuid.uuid4()}.{file_ext}"
    file_path = lecture_dir / unique_filename

    with file_path.open("wb") as buffer:
        shutil.copyfileobj(file.file, buffer)

    # Generate URL
    file_url = f"{STATIC_URL}/lectures/{lecture_id}/{unique_filename}"

    # Save to database (same as S3 approach)
    conn.execute("""
        UPDATE "Lecture"
        SET video_url = %s
        WHERE lecture_id = %s
    """, (file_url, lecture_id))

    return {"message": "Uploaded", "url": file_url}
```

**3. File upload best practices:**

**Validation:**
```python
from fastapi import UploadFile, HTTPException

MAX_FILE_SIZE = 500 * 1024 * 1024  # 500MB
ALLOWED_VIDEO_TYPES = ['video/mp4', 'video/webm', 'video/ogg']
ALLOWED_DOCUMENT_TYPES = ['application/pdf', 'application/msword',
                          'application/vnd.openxmlformats-officedocument.wordprocessingml.document']
ALLOWED_IMAGE_TYPES = ['image/jpeg', 'image/png', 'image/gif', 'image/webp']

def validate_video_upload(file: UploadFile):
    # Check content type
    if file.content_type not in ALLOWED_VIDEO_TYPES:
        raise HTTPException(400, "Invalid video format. Allowed: MP4, WebM, OGG")

    # Check file size
    file.file.seek(0, 2)  # Seek to end
    file_size = file.file.tell()
    file.file.seek(0)  # Reset

    if file_size > MAX_FILE_SIZE:
        raise HTTPException(400, f"File too large. Max: {MAX_FILE_SIZE / 1024 / 1024}MB")

    # Check extension matches content type
    ext = file.filename.split('.')[-1].lower()
    if ext == 'mp4' and file.content_type != 'video/mp4':
        raise HTTPException(400, "File extension doesn't match content type")

    return True

@app.post("/lectures/{lecture_id}/upload-video")
async def upload_with_validation(lecture_id: str, file: UploadFile):
    validate_video_upload(file)
    # Continue with upload...
```

**Progress tracking (chunked upload):**
```python
# Frontend: Upload in chunks with progress
@app.post("/upload/initiate")
def initiate_multipart_upload(filename: str, content_type: str):
    """
    Initiate S3 multipart upload.
    """
    upload = s3_client.create_multipart_upload(
        Bucket=BUCKET_NAME,
        Key=f"temp/{uuid.uuid4()}/{filename}",
        ContentType=content_type
    )

    return {
        "upload_id": upload['UploadId'],
        "key": upload['Key']
    }

@app.post("/upload/chunk")
def upload_chunk(upload_id: str, key: str, part_number: int, chunk: UploadFile):
    """
    Upload single chunk.
    """
    response = s3_client.upload_part(
        Bucket=BUCKET_NAME,
        Key=key,
        UploadId=upload_id,
        PartNumber=part_number,
        Body=chunk.file
    )

    return {"ETag": response['ETag']}

@app.post("/upload/complete")
def complete_multipart_upload(upload_id: str, key: str, parts: List[dict]):
    """
    Complete multipart upload.
    """
    s3_client.complete_multipart_upload(
        Bucket=BUCKET_NAME,
        Key=key,
        UploadId=upload_id,
        MultipartUpload={'Parts': parts}
    )

    return {"url": f"{CDN_URL}/{key}"}
```

**4. Video processing pipeline:**

```python
# After upload, process video (transcoding, thumbnail generation)
from celery import Celery

celery_app = Celery('tasks', broker='redis://localhost:6379')

@celery_app.task
def process_video(lecture_id: str, original_url: str):
    """
    Background task to process uploaded video.
    """
    # 1. Generate thumbnail
    thumbnail_url = generate_thumbnail(original_url)

    # 2. Transcode to multiple qualities (480p, 720p, 1080p)
    video_variants = transcode_video(original_url)

    # 3. Generate subtitles (if enabled)
    subtitles_url = generate_subtitles(original_url)

    # 4. Update database
    conn.execute("""
        UPDATE "Lecture"
        SET video_url = %s,
            thumbnail_url = %s,
            video_variants = %s::jsonb,
            subtitles_url = %s,
            processing_status = 'completed'
        WHERE lecture_id = %s
    """, (
        video_variants['1080p'],  # Main video URL
        thumbnail_url,
        json.dumps(video_variants),
        subtitles_url,
        lecture_id
    ))

# Trigger after upload
@app.post("/lectures/{lecture_id}/upload-video")
async def upload_and_process(lecture_id: str, file: UploadFile):
    # Upload to S3
    file_url = await upload_to_s3(file)

    # Update status to processing
    conn.execute("""
        UPDATE "Lecture"
        SET video_url = %s,
            processing_status = 'processing'
        WHERE lecture_id = %s
    """, (file_url, lecture_id))

    # Trigger background processing
    process_video.delay(lecture_id, file_url)

    return {
        "message": "Video uploaded, processing started",
        "status": "processing"
    }
```

**5. File management:**

```python
# Delete files when lecture deleted
@app.delete("/lectures/{lecture_id}")
def delete_lecture(lecture_id: str):
    conn = get_db_connection()

    # Get file URLs
    lecture = conn.execute("""
        SELECT video_url, resources FROM "Lecture" WHERE lecture_id = %s
    """, (lecture_id,)).fetchone()

    # Delete from S3
    if lecture['video_url']:
        s3_key = lecture['video_url'].replace(CDN_URL + '/', '')
        s3_client.delete_object(Bucket=BUCKET_NAME, Key=s3_key)

    if lecture['resources']:
        for resource in lecture['resources']['resources']:
            s3_key = resource['url'].replace(CDN_URL + '/', '')
            s3_client.delete_object(Bucket=BUCKET_NAME, Key=s3_key)

    # Delete from database
    conn.execute("DELETE FROM \"Lecture\" WHERE lecture_id = %s", (lecture_id,))
    conn.commit()

    return {"message": "Lecture and files deleted"}
```

**Kết luận:**
- **Store URLs, not binary data** in database
- **Cloud storage (S3)** for production (scalable, CDN, backups)
- **Local filesystem** for development only
- **Validation**: File type, size, content
- **Processing**: Async transcoding, thumbnails, subtitles
- **Security**: Signed URLs for private content, access control
- **Lifecycle**: Delete files when records deleted

---

## Kết luận & Tổng kết

### Tổng quan thiết kế B-Learning Core v1.0

**B-Learning Core Database** là một hệ thống quản lý học tập (LMS) được thiết kế với **16 tables** tổ chức theo **5 domains** chính, tuân thủ các nguyên tắc thiết kế cơ sở dữ liệu hiện đại và best practices của PostgreSQL.

**Điểm mạnh của thiết kế:**

1. **Modular Architecture (5 domains)**
   - Domain 1: User Management (3 tables)
   - Domain 2: Course Content (4 tables)
   - Domain 3: Assessment (5 tables)
   - Domain 4: Enrollment & Progress (2 tables)
   - Domain 5: Class & Certificate (2 tables)

2. **Modern Technologies**
   - PostgreSQL 14+ với JSONB, arrays, UUID
   - 96+ indexes (B-tree, GIN, composite)
   - Foreign keys với ON DELETE behaviors
   - Row Level Security ready

3. **Scalability & Performance**
   - UUID primary keys (distributed-friendly)
   - Strategic denormalization (completion_percentage)
   - Comprehensive indexing strategy
   - JSON for flexible data structures

4. **Data Integrity**
   - UNIQUE constraints (email, enrollments)
   - CHECK constraints (scores, dates, enums)
   - NOT NULL for required fields
   - Foreign key cascades and restrictions

5. **Flexibility vs Structure Balance**
   - JSONB cho Quiz questions và Attempt answers
   - JSONB cho Class schedules và Assignment configs
   - Structured tables cho core entities
   - Migration path từ JSON → separate tables

**Key Design Decisions:**

| Decision | Reasoning | Trade-off |
|----------|-----------|-----------|
| UUID vs INT | Security, scalability | Storage overhead |
| JSONB vs tables | Flexibility for quizzes | Query limitations |
| Soft delete (status) | Data preservation | Application complexity |
| 16 tables (not 31) | Simplicity for v1.0 | May need expansion |
| ON DELETE CASCADE | Auto-cleanup | Risk of mass deletion |
| completion_percentage cached | Performance | Must maintain consistency |
| GIN indexes | Fast JSON/array queries | Index size |

**Tài liệu đã hoàn thành:**

✅ **MAIN_REPORT.md** (2377 lines)
- Lời mở đầu
- Chapter 1: Tổng quan kiến trúc
- Chapter 2: Domain deep-dives
- Chapter 3: Relationships & indexes
- Chapter 4: Business logic & workflows
- Chapter 5: Performance & future scaling

✅ **FAQ_EXPLANATION.md** (hiện tại)
- 60+ câu hỏi chi tiết
- 11 sections covering all aspects
- SQL examples, Python code, decision matrices
- Best practices và anti-patterns

**Use cases được support:**

1. ✅ User registration & authentication (RBAC)
2. ✅ Course creation & publishing (instructor workflow)
3. ✅ Module & lecture organization (prerequisites)
4. ✅ Quiz creation & auto-grading (MC, T/F, short answer)
5. ✅ Assignment submissions & grading
6. ✅ Student enrollment (class-based & self-paced)
7. ✅ Progress tracking (lecture completion)
8. ✅ Class scheduling & management
9. ✅ Certificate issuance (completion-based)
10. ✅ Notifications & user preferences

**Production Readiness:**

✅ **Ready for deployment:**
- Complete schema với constraints
- Comprehensive indexes
- FK relationships properly defined
- Seed data available

⚠️ **Cần bổ sung trước production:**
- Authentication & authorization layer
- File upload/storage integration
- Email service integration
- Backup & recovery procedures
- Monitoring & logging
- Rate limiting & security
- Load testing

**Recommended Tech Stack:**

- **Database**: PostgreSQL 14+ (AWS RDS hoặc self-hosted)
- **Backend**: FastAPI (Python) hoặc Express (Node.js)
- **Frontend**: Next.js (React) hoặc Vue.js
- **File Storage**: AWS S3 + CloudFront
- **Caching**: Redis (sessions, query caching)
- **Background Jobs**: Celery (video processing, email)
- **Deployment**: Docker + Kubernetes hoặc Heroku

**Next Steps:**

1. **Phase 1**: Implement core backend APIs
2. **Phase 2**: Build admin & instructor dashboards
3. **Phase 3**: Student learning interface
4. **Phase 4**: Video processing pipeline
5. **Phase 5**: Analytics & reporting features
6. **Phase 6**: Mobile apps (iOS/Android)

---

**Tác giả**: Nguyễn Văn Kiệt - CNTT1-K63
**Project**: B-Learning Core v1.0
**Database**: PostgreSQL 14+
**Tables**: 16 tables across 5 domains
**Documentation**: 2377 lines (MAIN_REPORT) + 6300+ lines (FAQ)
**Last Updated**: 2025-11-27

---

*Tài liệu này cung cấp câu trả lời chi tiết cho hơn 60 câu hỏi về thiết kế cơ sở dữ liệu B-Learning Core, bao gồm quyết định thiết kế, trade-offs, SQL examples, Python code, và best practices. Sử dụng tài liệu này như một reference guide khi phát triển và maintain LMS system.*
