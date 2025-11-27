# B-Learning Core v1.0

**Simplified Blended Learning Platform Database**

Simplified database design for a blended learning management system with **16 tables** (reduced from 31 - 48% simplification), focusing on core functionality and maintainability.

---

## Overview

**B-Learning Core** is a streamlined database schema for a modern blended learning platform that supports both online self-paced learning and traditional classroom instruction.

### Key Features

✅ **User Management** with Role-Based Access Control (RBAC)
✅ **Course Management** with hierarchical structure (Course → Module → Lecture)
✅ **Assessment System** (Quizzes + Assignments) with auto-grading
✅ **Progress Tracking** at module level
✅ **Blended Learning** (online + classroom) support
✅ **Certificate Generation** for course completion
✅ **JSON-based** flexible data structures

### Architecture

- **Database**: PostgreSQL 14+
- **Tables**: 16 tables across 5 domains
- **Indexes**: 96 indexes (B-tree, GIN for JSON)
- **Constraints**: 224+ constraints (FK, CHECK, UNIQUE)
- **Storage**: ~60 MB for 1000 users (estimated)

---

## Quick Start

### Prerequisites

- PostgreSQL 14 or higher
- `uuid-ossp` extension

### Installation

```bash
# 1. Create database
createdb b_learning_core

# 2. Run SQL scripts in order
cd 98-B-Learing-Core/sql

psql b_learning_core < 01-schema.sql
psql b_learning_core < 02-indexes.sql
psql b_learning_core < 03-constraints.sql
psql b_learning_core < 04-seed-data.sql

# 3. Verify installation
psql b_learning_core -c "SELECT COUNT(*) FROM \"User\";"
```

### Verify Setup

```sql
-- Count tables (should be 16)
SELECT COUNT(*) FROM information_schema.tables
WHERE table_schema = 'public' AND table_type = 'BASE TABLE';

-- List all tables
\dt

-- Check sample data
SELECT email, first_name, last_name FROM "User" LIMIT 5;
```

---

## Project Structure

```
98-B-Learing-Core/
├── documents/
│   ├── BFD-SPEC.md              # Business Function Diagram
│   ├── ERD-SPEC.md              # Entity Relationship Diagram
│   ├── DATABASE-SCHEMA.md       # Complete schema documentation
│   ├── FUNCTIONAL-REQUIREMENTS.md
│   ├── API-ENDPOINTS.md         # REST API specifications
│   └── TABLES-EXPLANATION-VI.md # Vietnamese table explanations
│
├── sql/
│   ├── 01-schema.sql            # CREATE TABLE statements
│   ├── 02-indexes.sql           # Performance indexes
│   ├── 03-constraints.sql       # Foreign keys & constraints
│   └── 04-seed-data.sql         # Sample data for testing
│
├── req-2.md                     # Implementation plan
└── README.md                    # This file
```

---

## Database Schema

### 16 Tables Across 5 Domains

#### Domain 1: User Management (3 tables)
- **User** - User accounts with JSON preferences
- **Role** - System roles (ADMIN, INSTRUCTOR, TA, STUDENT)
- **UserRole** - User-Role assignments (M:N)

#### Domain 2: Course Content (4 tables)
- **Course** - Course catalog
- **Module** - Course chapters with prerequisites
- **Lecture** - Learning units (VIDEO, PDF, ASSIGNMENT, etc.)
- **Resource** - File attachments (stored in S3/GCS)

#### Domain 3: Assessment (5 tables)
- **Quiz** - Quizzes with embedded questions JSON
- **Question** - Reusable question bank
- **Option** - MCQ answer choices
- **Attempt** - Quiz attempts with answers JSON
- **AssignmentSubmission** - Assignment submissions

#### Domain 4: Enrollment & Progress (2 tables)
- **Enrollment** - Course/class enrollment (supports self-paced + blended)
- **Progress** - Module completion tracking

#### Domain 5: Class & Certificate (2 tables)
- **Class** - Physical/virtual classes with schedules JSON
- **Certificate** - Course completion certificates

---

## Key Design Features

### JSON Data Structures

The schema uses PostgreSQL JSON fields for flexible data:

```json
// Quiz.questions
[
  {"question_id": "uuid", "points": 10, "order": 1},
  {"question_id": "uuid", "points": 15, "order": 2}
]

// Attempt.answers
[
  {
    "question_id": "uuid",
    "answer_text": "Student answer...",
    "score": 12.5,
    "is_correct": true,
    "graded_at": "2025-11-27T10:30:00Z"
  }
]

// Class.schedules
[
  {
    "date": "2025-12-01",
    "start_time": "09:00",
    "topic": "Introduction",
    "location": "Room A101",
    "attendances": [
      {"user_id": "uuid", "status": "PRESENT", "check_in": "09:05"}
    ]
  }
]
```

### Blended Learning Support

The `Enrollment` table supports both learning modes:
- **Self-paced**: `class_id = NULL`
- **Class-based**: `class_id = UUID` (blended learning)

### Simplified from v2.0

| Feature | v2.0 Approach | Core Approach | Benefit |
|---------|---------------|---------------|---------|
| Quiz Questions | Separate QuizQuestion table | Quiz.questions JSON | -1 table, simpler queries |
| Quiz Submissions | Separate QuizSubmission table | Attempt.answers JSON | -1 table, atomic data |
| Assignments | Separate Assignment table | Lecture.assignment_config JSON | -1 table, unified model |
| Schedules | Schedule + Attendance tables | Class.schedules JSON | -2 tables, embedded data |
| Notifications | 3 tables | External service | -3 tables, better separation |

---

## Documentation

### Core Documents

1. **[ERD-SPEC.md](documents/ERD-SPEC.md)** - Complete ERD with 16 tables, relationships, JSON structures
2. **[DATABASE-SCHEMA.md](documents/DATABASE-SCHEMA.md)** - Full schema with DDL, indexes, constraints
3. **[BFD-SPEC.md](documents/BFD-SPEC.md)** - Business functions and workflows
4. **[TABLES-EXPLANATION-VI.md](documents/TABLES-EXPLANATION-VI.md)** - Vietnamese explanations

### SQL Files

1. **[01-schema.sql](sql/01-schema.sql)** - All 16 tables with Vietnamese comments
2. **[02-indexes.sql](sql/02-indexes.sql)** - 96 indexes (B-tree, GIN, full-text)
3. **[03-constraints.sql](sql/03-constraints.sql)** - 224+ constraints
4. **[04-seed-data.sql](sql/04-seed-data.sql)** - Sample data for testing

### API & Requirements

- **[API-ENDPOINTS.md](documents/API-ENDPOINTS.md)** - 58+ REST API endpoints
- **[FUNCTIONAL-REQUIREMENTS.md](documents/FUNCTIONAL-REQUIREMENTS.md)** - 45 functional requirements

---

## Sample Queries

### Student Progress

```sql
-- Get student's course progress
SELECT
  c.title,
  COUNT(m.module_id) AS total_modules,
  SUM(CASE WHEN p.status = 'COMPLETED' THEN 1 ELSE 0 END) AS completed,
  ROUND(100.0 * SUM(CASE WHEN p.status = 'COMPLETED' THEN 1 ELSE 0 END) / COUNT(m.module_id), 2) AS percentage
FROM "Course" c
JOIN "Module" m ON c.course_id = m.course_id
LEFT JOIN "Progress" p ON m.module_id = p.module_id AND p.user_id = $1
WHERE c.course_id = $2
GROUP BY c.course_id, c.title;
```

### Class Attendance

```sql
-- Extract attendance from JSON
SELECT
  c.class_name,
  session ->> 'date' AS session_date,
  attendance ->> 'user_id' AS user_id,
  attendance ->> 'status' AS status
FROM "Class" c,
  jsonb_array_elements(c.schedules::jsonb) AS session,
  jsonb_array_elements(session -> 'attendances') AS attendance
WHERE c.class_id = $1;
```

### Quiz Performance

```sql
-- Student quiz results
SELECT
  q.title,
  a.score,
  a.max_score,
  ROUND((a.score / a.max_score * 100)::numeric, 2) AS percentage,
  CASE WHEN a.score >= q.passing_score THEN 'PASS' ELSE 'FAIL' END AS result
FROM "Attempt" a
JOIN "Quiz" q ON a.quiz_id = q.quiz_id
WHERE a.user_id = $1 AND a.status = 'GRADED';
```

---

## Use Cases

### 1. Self-Paced Online Learning

```sql
-- Student enrolls in course (no class)
INSERT INTO "Enrollment" (user_id, course_id, class_id, status)
VALUES ('user-uuid', 'course-uuid', NULL, 'ACTIVE');

-- System tracks progress per module
INSERT INTO "Progress" (user_id, course_id, module_id, status, completion_percentage)
VALUES ('user-uuid', 'course-uuid', 'module-uuid', 'COMPLETED', 100);
```

### 2. Blended Learning (Online + Classroom)

```sql
-- Instructor creates class
INSERT INTO "Class" (course_id, class_name, start_date, end_date, instructor_id)
VALUES ('course-uuid', 'CS101-K63-01', '2025-01-10', '2025-05-15', 'instructor-uuid');

-- Student enrolls in class
INSERT INTO "Enrollment" (user_id, course_id, class_id, status)
VALUES ('user-uuid', 'course-uuid', 'class-uuid', 'ACTIVE');

-- Instructor marks attendance in JSON
UPDATE "Class"
SET schedules = jsonb_set(
  schedules::jsonb,
  '{0,attendances}',
  schedules::jsonb -> 0 -> 'attendances' || '[{"user_id": "uuid", "status": "PRESENT"}]'::jsonb
)
WHERE class_id = 'class-uuid';
```

### 3. Quiz Auto-Grading

```sql
-- Student submits quiz
UPDATE "Attempt"
SET status = 'SUBMITTED', submitted_at = NOW()
WHERE attempt_id = 'attempt-uuid';

-- System auto-grades MCQ (application logic updates answers JSON)
-- Instructor grades essay questions manually
```

---

## Performance

### Index Strategy

- **Foreign Keys**: All 27 FKs indexed
- **GIN Indexes**: JSON fields for fast @> queries
- **Partial Indexes**: Active users, published courses only
- **Full-Text Search**: Course/module/lecture titles

### Estimated Performance

| Operation | Rows | Time (est.) |
|-----------|------|-------------|
| User login | 1K users | < 5ms |
| Course catalog | 100 courses | < 10ms |
| Student progress | 25K progress records | < 50ms |
| Quiz attempt load | 10K attempts | < 20ms |
| JSON attendance query | 50 classes | < 30ms |

---

## Migration from v2.0

See **[DATABASE-SCHEMA.md](documents/DATABASE-SCHEMA.md)** Section 10 for detailed migration steps.

**Key Changes**:
1. QuizQuestion → Quiz.questions JSON
2. QuizSubmission → Attempt.answers JSON
3. CourseEnrollment + ClassEnrollment → Enrollment (with nullable class_id)
4. Schedule + Attendance → Class.schedules JSON
5. Remove: Notification, ActivityLog, File, GradeBook tables

---

## Technologies

- **Database**: PostgreSQL 14+
- **Extensions**: uuid-ossp, pg_trgm, btree_gin
- **Primary Keys**: UUID (distributed-friendly)
- **JSON Storage**: JSONB with GIN indexes
- **Character Set**: UTF-8

---

## License

Educational project - Nguyễn Văn Kiệt, CNTT1-K63

---

## Author

**Nguyễn Văn Kiệt**
Student ID: CNTT1-K63
Email: kiet.nguyen@blearning.edu.vn
Created: 2025-11-27

---

## Version History

- **v1.0** (2025-11-27): Initial release - 16 tables, 48% reduction from v2.0
- **v2.0** (Reference): Original design - 31 tables (see 99-B-Learing/)

---

## Resources

- **PostgreSQL Documentation**: https://www.postgresql.org/docs/14/
- **JSON/JSONB**: https://www.postgresql.org/docs/14/datatype-json.html
- **GIN Indexes**: https://www.postgresql.org/docs/14/gin.html

---

**For detailed technical documentation, see the `documents/` folder.**
