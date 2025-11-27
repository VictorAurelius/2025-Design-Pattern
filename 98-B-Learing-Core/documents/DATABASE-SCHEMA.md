# Database Schema Documentation - B-Learning Core v1.0

**Project**: B-Learning Core - Simplified Blended Learning Platform
**Author**: Nguyễn Văn Kiệt - CNTT1-K63
**Database**: PostgreSQL 14+
**Created**: 2025-11-27
**Total Tables**: 16 (reduced from 31 in v2.0 - 48% simplification)

---

## Table of Contents

1. [Schema Overview](#1-schema-overview)
2. [Database Requirements](#2-database-requirements)
3. [Complete Table Definitions](#3-complete-table-definitions)
4. [Relationships](#4-relationships)
5. [Indexes](#5-indexes)
6. [Constraints](#6-constraints)
7. [JSON Structures](#7-json-structures)
8. [Sample Queries](#8-sample-queries)

---

## 1. SCHEMA OVERVIEW

### 1.1. Architecture Summary

The B-Learning Core database is designed with a **5-domain architecture**:

```
┌─────────────────────────────────────────────────────┐
│              B-LEARNING CORE DATABASE               │
├─────────────────────────────────────────────────────┤
│                                                     │
│  Domain 1: USER MANAGEMENT (3 tables)              │
│  ├── User                                           │
│  ├── Role                                           │
│  └── UserRole                                       │
│                                                     │
│  Domain 2: COURSE CONTENT (4 tables)               │
│  ├── Course                                         │
│  ├── Module                                         │
│  ├── Lecture                                        │
│  └── Resource                                       │
│                                                     │
│  Domain 3: ASSESSMENT (5 tables)                   │
│  ├── Quiz                                           │
│  ├── Question                                       │
│  ├── Option                                         │
│  ├── Attempt                                        │
│  └── AssignmentSubmission                           │
│                                                     │
│  Domain 4: ENROLLMENT & PROGRESS (2 tables)        │
│  ├── Enrollment                                     │
│  └── Progress                                       │
│                                                     │
│  Domain 5: CLASS & CERTIFICATE (2 tables)          │
│  ├── Class                                          │
│  └── Certificate                                    │
│                                                     │
└─────────────────────────────────────────────────────┘
```

### 1.2. Key Design Decisions

| Decision | Rationale |
|----------|-----------|
| **UUID Primary Keys** | Distributed-system friendly, avoids ID collision |
| **JSON for Flexible Data** | Quiz questions, attempt answers, class schedules stored as JSON for simplicity |
| **Nullable class_id** | Supports both self-paced (NULL) and class-based (UUID) learning |
| **Module-level Progress** | Simpler than lecture-level, sufficient for most use cases |
| **Inline Constraints** | All constraints in schema for clarity and enforcement |
| **GIN Indexes** | Fast JSON queries with PostgreSQL native support |

### 1.3. Simplification from v2.0

| Component | v2.0 (31 tables) | Core (16 tables) | Change |
|-----------|------------------|------------------|--------|
| Quiz Questions | QuizQuestion table | Quiz.questions JSON | -1 table |
| Quiz Submissions | QuizSubmission table | Attempt.answers JSON | -1 table |
| Assignment | Assignment table | Lecture.assignment_config JSON | -1 table |
| Enrollment | 2 tables (Course/Class) | 1 Enrollment table | -1 table |
| Schedule/Attendance | 2 separate tables | Class.schedules JSON | -2 tables |
| Certificate | 3 tables | 1 Certificate table | -2 tables |
| Notification System | 3 tables | Removed (external) | -3 tables |
| ActivityLog | 1 table | Removed (app logs) | -1 table |
| File Management | 1 table | Removed (S3/GCS) | -1 table |
| GradeBook | 1 table | Removed (calculated) | -1 table |
| System Settings | 1 table | Removed (.env config) | -1 table |

---

## 2. DATABASE REQUIREMENTS

### 2.1. PostgreSQL Version

```sql
-- Minimum version: PostgreSQL 14+
SELECT version();
-- PostgreSQL 14.0 or higher required for:
-- - gen_random_uuid() function
-- - JSONB improvements
-- - GIN index enhancements
```

### 2.2. Required Extensions

```sql
-- UUID generation
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Full-text search
CREATE EXTENSION IF NOT EXISTS "pg_trgm";

-- Composite GIN indexes
CREATE EXTENSION IF NOT EXISTS "btree_gin";
```

### 2.3. Character Encoding

```sql
-- Database encoding
CREATE DATABASE b_learning_core
  WITH ENCODING = 'UTF8'
       LC_COLLATE = 'en_US.UTF-8'
       LC_CTYPE = 'en_US.UTF-8'
       TEMPLATE = template0;
```

---

## 3. COMPLETE TABLE DEFINITIONS

### DOMAIN 1: USER MANAGEMENT

#### Table 1: User

**Purpose**: Stores all user accounts (students, instructors, TAs, admins)

```sql
CREATE TABLE "User" (
  user_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  email VARCHAR(255) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  first_name VARCHAR(100) NOT NULL,
  last_name VARCHAR(100) NOT NULL,
  account_status VARCHAR(20) DEFAULT 'PENDING_VERIFICATION',
  preferences JSON DEFAULT '{}',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT chk_user_account_status CHECK (
    account_status IN ('PENDING_VERIFICATION', 'ACTIVE', 'SUSPENDED', 'DELETED')
  )
);
```

**Key Fields**:
- `email`: Unique login identifier, validated with regex
- `password_hash`: bcrypt hash ($2a$10$...)
- `preferences`: JSON for notification settings, locale, timezone
- `account_status`: Tracks account lifecycle

**Sample preferences JSON**:
```json
{
  "notifications": {
    "assignment_due": {"email": true, "push": true},
    "grade_published": {"email": true, "push": false}
  },
  "locale": "vi",
  "timezone": "Asia/Ho_Chi_Minh",
  "theme": "light"
}
```

---

#### Table 2: Role

**Purpose**: Defines system roles and their permissions

```sql
CREATE TABLE "Role" (
  role_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  name VARCHAR(50) NOT NULL UNIQUE,
  description TEXT,
  permissions JSON DEFAULT '[]',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

**Standard Roles**:
- `ADMIN`: Full system access
- `INSTRUCTOR`: Course creation, grading
- `TA`: Limited grading access
- `STUDENT`: Learning and submission

**Sample permissions JSON**:
```json
["course.create", "course.edit", "grade.manage", "class.manage"]
```

---

#### Table 3: UserRole

**Purpose**: Many-to-many relationship between Users and Roles

```sql
CREATE TABLE "UserRole" (
  user_role_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID NOT NULL REFERENCES "User"(user_id) ON DELETE CASCADE,
  role_id UUID NOT NULL REFERENCES "Role"(role_id) ON DELETE CASCADE,
  assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  expires_at TIMESTAMP,

  CONSTRAINT uq_userrole_user_role UNIQUE(user_id, role_id)
);
```

**Features**:
- **Temporary Roles**: `expires_at` allows time-limited role assignments
- **Multiple Roles**: One user can have multiple active roles

---

### DOMAIN 2: COURSE CONTENT

#### Table 4: Course

**Purpose**: Core course information

```sql
CREATE TABLE "Course" (
  course_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  code VARCHAR(20) NOT NULL UNIQUE,
  title VARCHAR(255) NOT NULL,
  description TEXT,
  difficulty_level VARCHAR(20) DEFAULT 'BEGINNER',
  credits INTEGER,
  status VARCHAR(20) DEFAULT 'DRAFT',
  created_by UUID REFERENCES "User"(user_id) ON DELETE SET NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT chk_course_status CHECK (status IN ('DRAFT', 'PUBLISHED', 'ARCHIVED')),
  CONSTRAINT chk_course_difficulty CHECK (
    difficulty_level IN ('BEGINNER', 'INTERMEDIATE', 'ADVANCED')
  ),
  CONSTRAINT chk_course_code_format CHECK (code ~ '^[A-Z0-9]{3,10}$')
);
```

**Status Lifecycle**:
```
DRAFT → PUBLISHED → ARCHIVED
```

---

#### Table 5: Module

**Purpose**: Course chapters/modules with prerequisite support

```sql
CREATE TABLE "Module" (
  module_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  course_id UUID NOT NULL REFERENCES "Course"(course_id) ON DELETE CASCADE,
  title VARCHAR(255) NOT NULL,
  description TEXT,
  order_num INTEGER NOT NULL,
  estimated_duration_minutes INTEGER,
  prerequisite_module_ids UUID[] DEFAULT '{}',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT uq_module_course_order UNIQUE(course_id, order_num)
);
```

**Features**:
- **Ordering**: `order_num` ensures sequential display
- **Prerequisites**: `prerequisite_module_ids` array enforces learning path
- **Cascade Delete**: Modules deleted when course deleted

---

#### Table 6: Lecture

**Purpose**: Individual learning units (videos, PDFs, assignments, etc.)

```sql
CREATE TABLE "Lecture" (
  lecture_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  module_id UUID NOT NULL REFERENCES "Module"(module_id) ON DELETE CASCADE,
  title VARCHAR(255) NOT NULL,
  description TEXT,
  type VARCHAR(20) NOT NULL,
  order_num INTEGER NOT NULL,
  duration_minutes INTEGER,
  assignment_config JSON,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT uq_lecture_module_order UNIQUE(module_id, order_num),
  CONSTRAINT chk_lecture_type CHECK (
    type IN ('VIDEO', 'PDF', 'SLIDE', 'AUDIO', 'TEXT', 'ASSIGNMENT')
  ),
  CONSTRAINT chk_lecture_assignment_config CHECK (
    (type = 'ASSIGNMENT' AND assignment_config IS NOT NULL) OR
    (type != 'ASSIGNMENT' AND assignment_config IS NULL)
  )
);
```

**Assignment Config JSON** (when type = 'ASSIGNMENT'):
```json
{
  "max_points": 100,
  "due_date": "2025-12-15T23:59:00Z",
  "submission_types": ["file", "text", "code"],
  "allowed_file_types": [".java", ".py", ".pdf"],
  "max_file_size_mb": 10,
  "instructions": "Detailed assignment instructions...",
  "rubric": {
    "code_quality": 40,
    "functionality": 40,
    "documentation": 20
  }
}
```

---

#### Table 7: Resource

**Purpose**: File attachments for lectures (stored in S3/GCS)

```sql
CREATE TABLE "Resource" (
  resource_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  lecture_id UUID NOT NULL REFERENCES "Lecture"(lecture_id) ON DELETE CASCADE,
  file_url VARCHAR(500) NOT NULL,
  file_type VARCHAR(100),
  file_size_bytes BIGINT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

**Usage**:
- `file_url`: Points to external storage (e.g., https://s3.amazonaws.com/...)
- `file_type`: MIME type (video/mp4, application/pdf, etc.)

---

### DOMAIN 3: ASSESSMENT

#### Table 8: Quiz

**Purpose**: Quiz/exam definitions with embedded questions

```sql
CREATE TABLE "Quiz" (
  quiz_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  course_id UUID NOT NULL REFERENCES "Course"(course_id) ON DELETE CASCADE,
  title VARCHAR(255) NOT NULL,
  description TEXT,
  instructions TEXT,
  status VARCHAR(20) DEFAULT 'DRAFT',
  duration_minutes INTEGER,
  total_points DECIMAL(10,2),
  passing_score DECIMAL(10,2),
  max_attempts INTEGER,
  available_from TIMESTAMP,
  available_until TIMESTAMP,
  questions JSON DEFAULT '[]',
  created_by UUID REFERENCES "User"(user_id) ON DELETE SET NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT chk_quiz_status CHECK (status IN ('DRAFT', 'PUBLISHED', 'ARCHIVED'))
);
```

**Questions JSON Structure**:
```json
[
  {"question_id": "uuid-1", "points": 10, "order": 1},
  {"question_id": "uuid-2", "points": 15, "order": 2},
  {"question_id": "uuid-3", "points": 5, "order": 3}
]
```

---

#### Table 9: Question

**Purpose**: Reusable question bank

```sql
CREATE TABLE "Question" (
  question_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  course_id UUID NOT NULL REFERENCES "Course"(course_id) ON DELETE CASCADE,
  question_text TEXT NOT NULL,
  type VARCHAR(20) NOT NULL,
  default_points DECIMAL(10,2),
  created_by UUID REFERENCES "User"(user_id) ON DELETE SET NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT chk_question_type CHECK (
    type IN ('MCQ', 'TRUE_FALSE', 'ESSAY', 'SHORT_ANSWER')
  )
);
```

**Question Types**:
- `MCQ`: Multiple choice (select one or many)
- `TRUE_FALSE`: Binary choice
- `ESSAY`: Long-form text answer
- `SHORT_ANSWER`: Brief text answer

---

#### Table 10: Option

**Purpose**: Answer choices for MCQ/TRUE_FALSE questions

```sql
CREATE TABLE "Option" (
  option_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  question_id UUID NOT NULL REFERENCES "Question"(question_id) ON DELETE CASCADE,
  option_text TEXT NOT NULL,
  is_correct BOOLEAN DEFAULT FALSE,
  order_num INTEGER NOT NULL,

  CONSTRAINT uq_option_question_order UNIQUE(question_id, order_num)
);
```

---

#### Table 11: Attempt

**Purpose**: Student quiz attempts with embedded answers

```sql
CREATE TABLE "Attempt" (
  attempt_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  quiz_id UUID NOT NULL REFERENCES "Quiz"(quiz_id) ON DELETE CASCADE,
  user_id UUID NOT NULL REFERENCES "User"(user_id) ON DELETE CASCADE,
  enrollment_id UUID NOT NULL REFERENCES "Enrollment"(enrollment_id) ON DELETE CASCADE,
  attempt_number INTEGER NOT NULL,
  status VARCHAR(20) DEFAULT 'IN_PROGRESS',
  started_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  submitted_at TIMESTAMP,
  score DECIMAL(10,2),
  max_score DECIMAL(10,2),
  answers JSON DEFAULT '[]',

  CONSTRAINT uq_attempt_user_quiz_number UNIQUE(user_id, quiz_id, attempt_number),
  CONSTRAINT chk_attempt_status CHECK (
    status IN ('IN_PROGRESS', 'SUBMITTED', 'GRADED', 'PENDING_GRADING')
  )
);
```

**Answers JSON Structure**:
```json
[
  {
    "question_id": "uuid-1",
    "answer_text": null,
    "selected_options": ["option-uuid-a"],
    "score": 10,
    "max_score": 10,
    "is_correct": true,
    "graded_at": "2025-11-27T10:30:00Z"
  },
  {
    "question_id": "uuid-2",
    "answer_text": "Student's essay answer...",
    "selected_options": null,
    "score": 12.5,
    "max_score": 15,
    "is_correct": null,
    "graded_at": "2025-11-27T11:00:00Z",
    "feedback": "Good answer but missing key points..."
  }
]
```

---

#### Table 12: AssignmentSubmission

**Purpose**: Student assignment submissions

```sql
CREATE TABLE "AssignmentSubmission" (
  submission_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  lecture_id UUID NOT NULL REFERENCES "Lecture"(lecture_id) ON DELETE CASCADE,
  user_id UUID NOT NULL REFERENCES "User"(user_id) ON DELETE CASCADE,
  enrollment_id UUID NOT NULL REFERENCES "Enrollment"(enrollment_id) ON DELETE CASCADE,
  submission_number INTEGER NOT NULL,
  status VARCHAR(20) DEFAULT 'DRAFT',
  submitted_at TIMESTAMP,
  score DECIMAL(10,2),
  max_score DECIMAL(10,2),
  feedback TEXT,
  file_urls JSON DEFAULT '[]',

  CONSTRAINT uq_assignment_user_lecture_number UNIQUE(lecture_id, user_id, submission_number),
  CONSTRAINT chk_assignment_status CHECK (
    status IN ('DRAFT', 'SUBMITTED', 'GRADED', 'PENDING_GRADING', 'LATE')
  )
);
```

**File URLs JSON**:
```json
["https://s3.amazonaws.com/blearning/submissions/file1.zip",
 "https://s3.amazonaws.com/blearning/submissions/file2.pdf"]
```

---

### DOMAIN 4: ENROLLMENT & PROGRESS

#### Table 13: Enrollment

**Purpose**: Course/class enrollment records (supports both self-paced and blended learning)

```sql
CREATE TABLE "Enrollment" (
  enrollment_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID NOT NULL REFERENCES "User"(user_id) ON DELETE CASCADE,
  course_id UUID NOT NULL REFERENCES "Course"(course_id) ON DELETE CASCADE,
  class_id UUID REFERENCES "Class"(class_id) ON DELETE SET NULL,
  status VARCHAR(20) DEFAULT 'ACTIVE',
  enrolled_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  completed_at TIMESTAMP,

  CONSTRAINT uq_enrollment_user_course_class
    UNIQUE(user_id, course_id, COALESCE(class_id, '00000000-0000-0000-0000-000000000000'::UUID)),
  CONSTRAINT chk_enrollment_status CHECK (
    status IN ('ACTIVE', 'COMPLETED', 'DROPPED', 'SUSPENDED')
  )
);
```

**Key Feature**: `class_id` nullable
- `NULL` = Self-paced learning
- `UUID` = Class-based (blended) learning

---

#### Table 14: Progress

**Purpose**: Module-level learning progress tracking

```sql
CREATE TABLE "Progress" (
  progress_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID NOT NULL REFERENCES "User"(user_id) ON DELETE CASCADE,
  course_id UUID NOT NULL REFERENCES "Course"(course_id) ON DELETE CASCADE,
  module_id UUID NOT NULL REFERENCES "Module"(module_id) ON DELETE CASCADE,
  status VARCHAR(20) DEFAULT 'NOT_STARTED',
  completion_percentage INTEGER DEFAULT 0,
  started_at TIMESTAMP,
  completed_at TIMESTAMP,

  CONSTRAINT uq_progress_user_course_module UNIQUE(user_id, course_id, module_id),
  CONSTRAINT chk_progress_status CHECK (
    status IN ('NOT_STARTED', 'IN_PROGRESS', 'COMPLETED')
  ),
  CONSTRAINT chk_progress_percentage CHECK (
    completion_percentage >= 0 AND completion_percentage <= 100
  )
);
```

---

### DOMAIN 5: CLASS & CERTIFICATE

#### Table 15: Class

**Purpose**: Physical/virtual class management with embedded schedules

```sql
CREATE TABLE "Class" (
  class_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  course_id UUID NOT NULL REFERENCES "Course"(course_id) ON DELETE CASCADE,
  class_name VARCHAR(100) NOT NULL,
  start_date DATE NOT NULL,
  end_date DATE NOT NULL,
  instructor_id UUID REFERENCES "User"(user_id) ON DELETE SET NULL,
  max_students INTEGER,
  status VARCHAR(20) DEFAULT 'SCHEDULED',
  schedules JSON DEFAULT '[]',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT chk_class_status CHECK (
    status IN ('SCHEDULED', 'ONGOING', 'COMPLETED', 'CANCELLED')
  ),
  CONSTRAINT chk_class_dates CHECK (end_date >= start_date)
);
```

**Schedules JSON Structure**:
```json
[
  {
    "session_id": "uuid",
    "date": "2025-12-01",
    "start_time": "09:00",
    "end_time": "11:00",
    "topic": "Introduction to Java",
    "location": "Room A101",
    "type": "IN_PERSON",
    "attendances": [
      {"user_id": "uuid-1", "status": "PRESENT", "check_in": "09:05"},
      {"user_id": "uuid-2", "status": "LATE", "check_in": "09:15"},
      {"user_id": "uuid-3", "status": "ABSENT"}
    ]
  }
]
```

---

#### Table 16: Certificate

**Purpose**: Course completion certificates

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
  status VARCHAR(20) DEFAULT 'ACTIVE',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT uq_certificate_user_course UNIQUE(user_id, course_id),
  CONSTRAINT chk_certificate_status CHECK (status IN ('ACTIVE', 'REVOKED'))
);
```

**Certificate Codes**:
- `certificate_code`: Public display (e.g., BL-2025-000001)
- `verification_code`: Secure verification (UUID-based)

---

## 4. RELATIONSHIPS

### 4.1. Relationship Matrix

| From Table | To Table | Type | FK Column | ON DELETE | Notes |
|------------|----------|------|-----------|-----------|-------|
| UserRole | User | N:1 | user_id | CASCADE | User deleted → roles deleted |
| UserRole | Role | N:1 | role_id | CASCADE | Role deleted → assignments deleted |
| Module | Course | N:1 | course_id | CASCADE | Course deleted → modules deleted |
| Lecture | Module | N:1 | module_id | CASCADE | Module deleted → lectures deleted |
| Resource | Lecture | N:1 | lecture_id | CASCADE | Lecture deleted → resources deleted |
| Quiz | Course | N:1 | course_id | CASCADE | Course deleted → quizzes deleted |
| Question | Course | N:1 | course_id | CASCADE | Course deleted → questions deleted |
| Option | Question | N:1 | question_id | CASCADE | Question deleted → options deleted |
| Attempt | Quiz | N:1 | quiz_id | CASCADE | Quiz deleted → attempts deleted |
| Attempt | User | N:1 | user_id | CASCADE | User deleted → attempts deleted |
| Attempt | Enrollment | N:1 | enrollment_id | CASCADE | Enrollment deleted → attempts deleted |
| AssignmentSubmission | Lecture | N:1 | lecture_id | CASCADE | |
| AssignmentSubmission | User | N:1 | user_id | CASCADE | |
| AssignmentSubmission | Enrollment | N:1 | enrollment_id | CASCADE | |
| Enrollment | User | N:1 | user_id | CASCADE | |
| Enrollment | Course | N:1 | course_id | CASCADE | |
| Enrollment | Class | N:1 | class_id | SET NULL | Class deleted → enrollment kept |
| Progress | User | N:1 | user_id | CASCADE | |
| Progress | Course | N:1 | course_id | CASCADE | |
| Progress | Module | N:1 | module_id | CASCADE | |
| Class | Course | N:1 | course_id | CASCADE | |
| Class | User | N:1 | instructor_id | SET NULL | Instructor deleted → class kept |
| Certificate | User | N:1 | user_id | CASCADE | |
| Certificate | Course | N:1 | course_id | CASCADE | |

**Total**: 23 foreign key relationships

### 4.2. Cascade Behavior Summary

- **CASCADE (19)**: Parent deletion removes children
- **SET NULL (4)**: Parent deletion nullifies FK (optional relationships)
- **RESTRICT (0)**: Not used (too strict for this application)

---

## 5. INDEXES

See `sql/02-indexes.sql` for complete index definitions.

### 5.1. Index Summary

| Index Type | Count | Purpose |
|------------|-------|---------|
| Primary Keys | 16 | Automatic B-tree indexes |
| Foreign Keys | 28 | Speed up JOIN operations |
| Performance | 26 | Common query optimization |
| JSON GIN | 8 | Fast JSON queries |
| Full-Text Search | 5 | tsvector-based search |
| Composite | 7 | Multi-column queries |
| Partial | 6 | Filtered indexes |
| **TOTAL** | **96** | |

### 5.2. Key GIN Indexes

```sql
-- JSON field queries
CREATE INDEX idx_user_preferences ON "User" USING GIN (preferences);
CREATE INDEX idx_quiz_questions ON "Quiz" USING GIN (questions);
CREATE INDEX idx_attempt_answers ON "Attempt" USING GIN (answers);
CREATE INDEX idx_class_schedules ON "Class" USING GIN (schedules);
CREATE INDEX idx_lecture_assignment_config ON "Lecture" USING GIN (assignment_config);
```

---

## 6. CONSTRAINTS

See `sql/03-constraints.sql` for complete constraint definitions.

### 6.1. Constraint Summary

| Constraint Type | Count | Examples |
|-----------------|-------|----------|
| Primary Keys | 16 | All tables |
| Foreign Keys | 27 | All relationships |
| Unique | 14 | email, course.code, certificate.code |
| Check | 47 | Status enums, ranges, formats |
| Not Null | 120+ | Required fields |
| **TOTAL** | **224+** | |

### 6.2. Key Check Constraints

```sql
-- Email format validation
ALTER TABLE "User" ADD CONSTRAINT chk_user_email_format CHECK (
  email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$'
);

-- Score range validation
ALTER TABLE "Attempt" ADD CONSTRAINT chk_attempt_score_range CHECK (
  score IS NULL OR (score >= 0 AND score <= max_score)
);

-- Date logic validation
ALTER TABLE "Quiz" ADD CONSTRAINT chk_quiz_available_dates CHECK (
  available_from IS NULL OR available_until IS NULL OR available_from < available_until
);
```

---

## 7. JSON STRUCTURES

### 7.1. Complete JSON Reference

See **ERD-SPEC.md** Section 5 for full JSON schemas.

**Quick Reference**:
- `User.preferences`: Notification settings, locale, timezone
- `Role.permissions`: Array of permission strings
- `Module.prerequisite_module_ids`: Array of UUID
- `Lecture.assignment_config`: Assignment configuration object
- `Quiz.questions`: Array of question references
- `Attempt.answers`: Array of student responses with scores
- `AssignmentSubmission.file_urls`: Array of S3/GCS URLs
- `Class.schedules`: Array of session objects with attendances

---

## 8. SAMPLE QUERIES

### 8.1. User & Enrollment Queries

```sql
-- Get student's enrolled courses
SELECT
  c.code,
  c.title,
  e.status,
  e.enrolled_at,
  cl.class_name
FROM "Enrollment" e
JOIN "Course" c ON e.course_id = c.course_id
LEFT JOIN "Class" cl ON e.class_id = cl.class_id
WHERE e.user_id = $1 AND e.status = 'ACTIVE';
```

### 8.2. Progress Queries

```sql
-- Calculate course completion percentage
SELECT
  c.title AS course_title,
  COUNT(m.module_id) AS total_modules,
  SUM(CASE WHEN p.status = 'COMPLETED' THEN 1 ELSE 0 END) AS completed_modules,
  ROUND(
    100.0 * SUM(CASE WHEN p.status = 'COMPLETED' THEN 1 ELSE 0 END) / COUNT(m.module_id),
    2
  ) AS completion_percentage
FROM "Course" c
JOIN "Module" m ON c.course_id = m.course_id
LEFT JOIN "Progress" p ON m.module_id = p.module_id AND p.user_id = $1
WHERE c.course_id = $2
GROUP BY c.course_id, c.title;
```

### 8.3. Quiz JSON Queries

```sql
-- Find quizzes containing a specific question
SELECT
  q.title,
  jsonb_pretty(q.questions::jsonb) AS question_structure
FROM "Quiz" q
WHERE q.questions::jsonb @> '[{"question_id": "uuid-here"}]'::jsonb;

-- Get student's quiz results with percentage
SELECT
  q.title AS quiz_title,
  a.attempt_number,
  a.score,
  a.max_score,
  ROUND((a.score / a.max_score * 100)::numeric, 2) AS percentage,
  CASE
    WHEN a.score >= q.passing_score THEN 'PASS'
    ELSE 'FAIL'
  END AS result
FROM "Attempt" a
JOIN "Quiz" q ON a.quiz_id = q.quiz_id
WHERE a.user_id = $1 AND a.status = 'GRADED'
ORDER BY a.submitted_at DESC;
```

### 8.4. Class Attendance Queries

```sql
-- Extract attendance from Class.schedules JSON
SELECT
  c.class_name,
  session ->> 'date' AS session_date,
  session ->> 'topic' AS topic,
  attendance ->> 'user_id' AS user_id,
  attendance ->> 'status' AS attendance_status,
  attendance ->> 'check_in' AS check_in_time
FROM "Class" c,
  jsonb_array_elements(c.schedules::jsonb) AS session,
  jsonb_array_elements(session -> 'attendances') AS attendance
WHERE c.class_id = $1
ORDER BY session ->> 'date';
```

---

## 9. INSTALLATION & USAGE

### 9.1. Database Setup

```bash
# Create database
createdb b_learning_core

# Run schema in order
psql b_learning_core < sql/01-schema.sql
psql b_learning_core < sql/02-indexes.sql
psql b_learning_core < sql/03-constraints.sql
psql b_learning_core < sql/04-seed-data.sql
```

### 9.2. Verification

```sql
-- Count tables
SELECT COUNT(*) FROM information_schema.tables
WHERE table_schema = 'public' AND table_type = 'BASE TABLE';
-- Expected: 16

-- List all indexes
SELECT tablename, indexname
FROM pg_indexes
WHERE schemaname = 'public'
ORDER BY tablename, indexname;

-- Check constraints
SELECT conname, contype, pg_get_constraintdef(oid)
FROM pg_constraint
WHERE connamespace = 'public'::regnamespace
ORDER BY conrelid::regclass::text;
```

---

## 10. MIGRATION FROM v2.0

### 10.1. Data Migration Steps

1. **Export v2.0 data**: Use pg_dump for data-only export
2. **Transform data**:
   - QuizQuestion → Quiz.questions JSON
   - QuizSubmission → Attempt.answers JSON
   - Schedule + Attendance → Class.schedules JSON
3. **Import to Core schema**: Use COPY or INSERT statements
4. **Validate**: Run integrity checks

### 10.2. Not Migrated (External Services)

- Notification records → Email service logs
- ActivityLog → Application logs (Winston, Logtail)
- File records → S3/GCS metadata
- GradeBook → Calculate on-demand

---

## APPENDIX A: Table Size Estimates

| Table | Est. Rows (1000 users) | Storage | Notes |
|-------|------------------------|---------|-------|
| User | 1,000 | ~500 KB | Includes JSON preferences |
| Role | 10 | ~5 KB | Static roles |
| UserRole | 1,000 | ~50 KB | One role per user avg |
| Course | 100 | ~100 KB | |
| Module | 500 | ~200 KB | 5 modules per course |
| Lecture | 2,500 | ~1 MB | 5 lectures per module |
| Resource | 5,000 | ~500 KB | 2 resources per lecture |
| Question | 1,000 | ~500 KB | Reusable question bank |
| Option | 4,000 | ~200 KB | 4 options per MCQ |
| Quiz | 200 | ~2 MB | Includes questions JSON |
| Attempt | 10,000 | ~50 MB | Includes answers JSON |
| AssignmentSubmission | 5,000 | ~2 MB | File URLs only |
| Enrollment | 5,000 | ~500 KB | |
| Progress | 25,000 | ~2 MB | |
| Class | 50 | ~500 KB | Includes schedules JSON |
| Certificate | 500 | ~100 KB | |
| **TOTAL** | **60,360** | **~60 MB** | Without indexes |

---

**END OF DATABASE SCHEMA DOCUMENTATION**
