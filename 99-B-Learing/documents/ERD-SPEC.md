# Entity Relationship Diagram Specification

**Project**: B-Learning System Database v2.0
**Author**: Nguyễn Văn Kiệt - CNTT1-K63
**Created**: 2025-11-25
**Database**: PostgreSQL 14+
**Total Tables**: 31
**Total Domains**: 8

---

## 1. OVERVIEW

This document specifies the complete Entity Relationship Diagram (ERD) for the redesigned B-Learning system. The database consists of **31 tables** organized into **8 functional domains**, supporting:

- **Assignment-based learning** (replacing simple submission system)
- **Blended learning** (online + in-person classes)
- **Automated certificate issuance** with verification
- **Multi-channel notifications**
- **Comprehensive progress tracking** (course, module, lecture, quiz, assignment)
- **Role-based access control** (Student, Instructor, TA, Admin)

### Key Improvements from v1.0
- ❌ **Removed**: Forum/Discussion system (Thread, Post, PostVote, ThreadParticipant, PostEditHistory)
- ✅ **Added**: Assignment system (5 tables)
- ✅ **Added**: Certificate templates and verification (3 tables)
- ✅ **Added**: Notification system (3 tables)
- ✅ **Fixed**: Progress tracking (now includes course_id, module_id, quiz_id)
- ✅ **Fixed**: Attempt relationship (course_enrollment_id instead of class_id)

---

## 2. DOMAINS

The database is organized into 8 functional domains:

| Domain | Tables | Purpose |
|--------|--------|---------|
| **1. User Management** | 3 | Authentication, roles, permissions |
| **2. Course Content** | 4 | Courses, modules, lectures, resources |
| **3. Assessment** | 9 | Quizzes, questions, assignments, submissions, grades |
| **4. Enrollment & Progress** | 4 | Course/class enrollment, learning progress, attendance |
| **5. Class & Blended Learning** | 3 | Physical classes, schedules, sessions |
| **6. Certificate** | 3 | Certificate templates, issuance, verification |
| **7. Notification** | 3 | Multi-channel notifications, preferences, delivery logs |
| **8. Audit & System** | 3 | Activity logs, file management, system settings |

**Total**: 31 tables

---

## 3. ENTITIES DETAIL

### 3.1. User Management Domain (3 tables)

#### 3.1.1. User

**Purpose**: Stores user accounts with profile information and authentication credentials.

**Table Name**: `User`

| Column | Data Type | Constraints | Description |
|--------|-----------|-------------|-------------|
| user_id | UUID | PRIMARY KEY, DEFAULT gen_random_uuid() | Unique identifier |
| email | VARCHAR(255) | NOT NULL, UNIQUE | User email (login) |
| password_hash | VARCHAR(255) | NOT NULL | Hashed password |
| first_name | VARCHAR(100) | NOT NULL | First name |
| last_name | VARCHAR(100) | NOT NULL | Last name |
| avatar_url | VARCHAR(500) | | Profile picture URL |
| phone | VARCHAR(20) | | Contact phone |
| timezone | VARCHAR(50) | DEFAULT 'UTC' | User timezone |
| locale | VARCHAR(10) | DEFAULT 'vi' | Language preference |
| account_status | VARCHAR(30) | NOT NULL, DEFAULT 'PENDING_VERIFICATION' | Account status |
| email_verified_at | TIMESTAMP | | Email verification timestamp |
| last_login_at | TIMESTAMP | | Last login time |
| created_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Record creation time |
| updated_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Last update time |

**Constraints**:
- `CHECK (account_status IN ('PENDING_VERIFICATION', 'ACTIVE', 'SUSPENDED', 'DELETED'))`

**Indexes**:
- Primary key on `user_id`
- Unique index on `email`
- Index on `account_status`
- Index on `created_at DESC`

---

#### 3.1.2. Role

**Purpose**: Defines system roles with associated permissions.

**Table Name**: `Role`

| Column | Data Type | Constraints | Description |
|--------|-----------|-------------|-------------|
| role_id | UUID | PRIMARY KEY, DEFAULT gen_random_uuid() | Unique identifier |
| name | VARCHAR(50) | NOT NULL, UNIQUE | Role name |
| description | TEXT | | Role description |
| permissions | JSON | | JSON array of permissions |
| is_system_role | BOOLEAN | DEFAULT TRUE | System-defined role flag |
| created_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Record creation time |

**Standard Roles**:
- `STUDENT` - Học viên
- `INSTRUCTOR` - Giảng viên
- `TA` - Trợ giảng
- `ADMIN` - Quản trị viên
- `MODERATOR` - Người kiểm duyệt (optional)

**Indexes**:
- Primary key on `role_id`
- Unique index on `name`

---

#### 3.1.3. UserRole

**Purpose**: Many-to-many relationship between users and roles.

**Table Name**: `UserRole`

| Column | Data Type | Constraints | Description |
|--------|-----------|-------------|-------------|
| user_role_id | UUID | PRIMARY KEY, DEFAULT gen_random_uuid() | Unique identifier |
| user_id | UUID | NOT NULL, FK → User.user_id | User reference |
| role_id | UUID | NOT NULL, FK → Role.role_id | Role reference |
| granted_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | When role was granted |
| granted_by | UUID | FK → User.user_id | Who granted the role |
| expires_at | TIMESTAMP | | Role expiration date (optional) |

**Constraints**:
- `UNIQUE(user_id, role_id)` - User can have a role only once
- `ON DELETE CASCADE` for both user_id and role_id

**Indexes**:
- Primary key on `user_role_id`
- Index on `user_id`
- Index on `role_id`
- Unique index on `(user_id, role_id)`

---

### 3.2. Course Content Domain (4 tables)

#### 3.2.1. Course

**Purpose**: Stores course information and metadata.

**Table Name**: `Course`

| Column | Data Type | Constraints | Description |
|--------|-----------|-------------|-------------|
| course_id | UUID | PRIMARY KEY, DEFAULT gen_random_uuid() | Unique identifier |
| code | VARCHAR(50) | NOT NULL, UNIQUE | Course code (e.g., CS101) |
| title | VARCHAR(200) | NOT NULL | Course title |
| description | TEXT | | Full description |
| short_description | VARCHAR(500) | | Brief summary |
| thumbnail_url | VARCHAR(500) | | Course image |
| category | VARCHAR(100) | | Course category |
| difficulty_level | VARCHAR(20) | | Difficulty level |
| estimated_hours | DECIMAL(5,2) | | Estimated completion time |
| status | VARCHAR(20) | DEFAULT 'DRAFT' | Course status |
| published_at | TIMESTAMP | | Publication timestamp |
| created_by | UUID | FK → User.user_id | Creator user ID |
| created_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Record creation time |
| updated_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Last update time |

**Constraints**:
- `CHECK (difficulty_level IN ('BEGINNER', 'INTERMEDIATE', 'ADVANCED', 'EXPERT'))`
- `CHECK (status IN ('DRAFT', 'PUBLISHED', 'ARCHIVED'))`

**Indexes**:
- Primary key on `course_id`
- Unique index on `code`
- Index on `status`
- Index on `category`
- Index on `published_at DESC` WHERE status = 'PUBLISHED'
- Full-text search index on `title || ' ' || description`

---

#### 3.2.2. Module

**Purpose**: Organizes course content into chapters/modules.

**Table Name**: `Module`

| Column | Data Type | Constraints | Description |
|--------|-----------|-------------|-------------|
| module_id | UUID | PRIMARY KEY, DEFAULT gen_random_uuid() | Unique identifier |
| course_id | UUID | NOT NULL, FK → Course.course_id, CASCADE | Parent course |
| title | VARCHAR(200) | NOT NULL | Module title |
| description | TEXT | | Module description |
| order_num | INT | NOT NULL | Display order |
| prerequisite_module_ids | UUID[] | | Required modules (array) |
| estimated_duration_minutes | INT | | Estimated time to complete |
| created_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Record creation time |
| updated_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Last update time |

**Constraints**:
- `UNIQUE(course_id, order_num)` - Order unique within course
- `ON DELETE CASCADE` when course is deleted

**Indexes**:
- Primary key on `module_id`
- Index on `course_id`
- Unique index on `(course_id, order_num)`

---

#### 3.2.3. Lecture

**Purpose**: Individual learning units within a module (videos, PDFs, etc.).

**Table Name**: `Lecture`

| Column | Data Type | Constraints | Description |
|--------|-----------|-------------|-------------|
| lecture_id | UUID | PRIMARY KEY, DEFAULT gen_random_uuid() | Unique identifier |
| module_id | UUID | NOT NULL, FK → Module.module_id, CASCADE | Parent module |
| title | VARCHAR(200) | NOT NULL | Lecture title |
| description | TEXT | | Lecture description |
| type | VARCHAR(20) | NOT NULL | Content type |
| content_url | VARCHAR(1024) | | URL to content |
| duration_seconds | INT | | Content duration |
| order_num | INT | NOT NULL | Display order |
| is_preview | BOOLEAN | DEFAULT FALSE | Preview without enrollment |
| is_downloadable | BOOLEAN | DEFAULT TRUE | Allow download |
| transcript | TEXT | | Video transcript/notes |
| created_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Record creation time |
| updated_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Last update time |

**Constraints**:
- `CHECK (type IN ('VIDEO', 'PDF', 'SLIDE', 'AUDIO', 'TEXT'))`
- `UNIQUE(module_id, order_num)` - Order unique within module
- `ON DELETE CASCADE` when module is deleted

**Indexes**:
- Primary key on `lecture_id`
- Index on `module_id`
- Unique index on `(module_id, order_num)`

---

#### 3.2.4. Resource

**Purpose**: Supplementary files attached to lectures (downloads, readings).

**Table Name**: `Resource`

| Column | Data Type | Constraints | Description |
|--------|-----------|-------------|-------------|
| resource_id | UUID | PRIMARY KEY, DEFAULT gen_random_uuid() | Unique identifier |
| lecture_id | UUID | NOT NULL, FK → Lecture.lecture_id, CASCADE | Parent lecture |
| title | VARCHAR(200) | NOT NULL | Resource title |
| file_url | VARCHAR(500) | NOT NULL | File URL |
| file_type | VARCHAR(100) | | MIME type |
| file_size_bytes | BIGINT | | File size |
| created_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Record creation time |

**Constraints**:
- `ON DELETE CASCADE` when lecture is deleted

**Indexes**:
- Primary key on `resource_id`
- Index on `lecture_id`

---

### 3.3. Assessment Domain (9 tables)

#### 3.3.1. Quiz

**Purpose**: Quiz/test configuration and settings.

**Table Name**: `Quiz`

| Column | Data Type | Constraints | Description |
|--------|-----------|-------------|-------------|
| quiz_id | UUID | PRIMARY KEY, DEFAULT gen_random_uuid() | Unique identifier |
| course_id | UUID | NOT NULL, FK → Course.course_id, CASCADE | Parent course |
| title | VARCHAR(200) | NOT NULL | Quiz title |
| description | TEXT | | Quiz description/instructions |
| time_limit_minutes | INT | | Time limit (0 = unlimited) |
| attempt_limit | INT | | Max attempts (0 = unlimited) |
| pass_score | DECIMAL(5,2) | | Minimum passing score |
| shuffle_questions | BOOLEAN | DEFAULT FALSE | Randomize question order |
| show_correct_answers | BOOLEAN | DEFAULT TRUE | Show answers after submit |
| is_published | BOOLEAN | DEFAULT FALSE | Published status |
| created_by | UUID | FK → User.user_id | Creator |
| created_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Record creation time |
| updated_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Last update time |

**Indexes**:
- Primary key on `quiz_id`
- Index on `course_id`
- Index on `is_published`

---

#### 3.3.2. Question

**Purpose**: Question bank (shared across quizzes).

**Table Name**: `Question`

| Column | Data Type | Constraints | Description |
|--------|-----------|-------------|-------------|
| question_id | UUID | PRIMARY KEY, DEFAULT gen_random_uuid() | Unique identifier |
| course_id | UUID | NOT NULL, FK → Course.course_id, CASCADE | Parent course |
| text | TEXT | NOT NULL | Question text |
| type | VARCHAR(20) | NOT NULL | Question type |
| difficulty | VARCHAR(20) | DEFAULT 'MEDIUM' | Difficulty level |
| max_points | DECIMAL(5,2) | NOT NULL, DEFAULT 1.00 | Maximum points |
| explanation | TEXT | | Correct answer explanation |
| is_active | BOOLEAN | DEFAULT TRUE | Active status |
| created_by | UUID | FK → User.user_id | Creator |
| created_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Record creation time |
| updated_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Last update time |

**Constraints**:
- `CHECK (type IN ('MCQ', 'TRUE_FALSE', 'ESSAY', 'SHORT_ANSWER', 'CODE'))`
- `CHECK (difficulty IN ('EASY', 'MEDIUM', 'HARD'))`

**Indexes**:
- Primary key on `question_id`
- Index on `course_id`
- Index on `type`
- Index on `is_active`

---

#### 3.3.3. Option

**Purpose**: Answer options for MCQ and TRUE_FALSE questions.

**Table Name**: `Option`

| Column | Data Type | Constraints | Description |
|--------|-----------|-------------|-------------|
| option_id | UUID | PRIMARY KEY, DEFAULT gen_random_uuid() | Unique identifier |
| question_id | UUID | NOT NULL, FK → Question.question_id, CASCADE | Parent question |
| option_text | TEXT | NOT NULL | Option text |
| is_correct | BOOLEAN | NOT NULL, DEFAULT FALSE | Correct answer flag |
| order_num | INT | NOT NULL | Display order |
| feedback | TEXT | | Feedback for this option |

**Constraints**:
- `UNIQUE(question_id, order_num)` - Order unique within question
- `ON DELETE CASCADE` when question is deleted

**Indexes**:
- Primary key on `option_id`
- Index on `question_id`

---

#### 3.3.4. QuizQuestion

**Purpose**: Many-to-many relationship between quizzes and questions.

**Table Name**: `QuizQuestion`

| Column | Data Type | Constraints | Description |
|--------|-----------|-------------|-------------|
| quiz_question_id | UUID | PRIMARY KEY, DEFAULT gen_random_uuid() | Unique identifier |
| quiz_id | UUID | NOT NULL, FK → Quiz.quiz_id, CASCADE | Quiz reference |
| question_id | UUID | NOT NULL, FK → Question.question_id, RESTRICT | Question reference |
| points | DECIMAL(5,2) | NOT NULL | Points for this question in quiz |
| order_num | INT | NOT NULL | Display order in quiz |

**Constraints**:
- `UNIQUE(quiz_id, question_id)` - Question appears once per quiz
- `UNIQUE(quiz_id, order_num)` - Order unique within quiz
- `ON DELETE CASCADE` for quiz_id
- `ON DELETE RESTRICT` for question_id (prevent deletion if used)

**Indexes**:
- Primary key on `quiz_question_id`
- Index on `quiz_id`
- Index on `question_id`

---

#### 3.3.5. Attempt

**Purpose**: Quiz attempt records (REDESIGNED - fixed from v1.0).

**Table Name**: `Attempt`

| Column | Data Type | Constraints | Description |
|--------|-----------|-------------|-------------|
| attempt_id | UUID | PRIMARY KEY, DEFAULT gen_random_uuid() | Unique identifier |
| quiz_id | UUID | NOT NULL, FK → Quiz.quiz_id, CASCADE | Quiz reference |
| user_id | UUID | NOT NULL, FK → User.user_id, CASCADE | Student reference |
| course_enrollment_id | UUID | NOT NULL, FK → CourseEnrollment, CASCADE | Enrollment reference |
| class_id | UUID | FK → Class.class_id, SET NULL | Optional class (blended learning) |
| attempt_number | INT | NOT NULL | Attempt sequence (1, 2, 3...) |
| started_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Start time |
| submitted_at | TIMESTAMP | | Submission time |
| time_spent_seconds | INT | DEFAULT 0 | Time spent |
| status | VARCHAR(20) | NOT NULL, DEFAULT 'IN_PROGRESS' | Attempt status |
| auto_score | DECIMAL(6,2) | DEFAULT 0.00 | Auto-graded score (MCQ) |
| manual_score | DECIMAL(6,2) | | Manual score (ESSAY) |
| final_score | DECIMAL(6,2) | | Total score |
| max_possible_score | DECIMAL(6,2) | NOT NULL | Maximum score |
| percentage_score | DECIMAL(5,2) | | Score percentage |
| graded_at | TIMESTAMP | | Grading completion time |
| graded_by | UUID | FK → User.user_id | Grader (for manual) |
| ip_address | VARCHAR(45) | | IP address |

**Constraints**:
- `UNIQUE(user_id, quiz_id, attempt_number)` - Unique attempt number per user per quiz
- `CHECK (status IN ('IN_PROGRESS', 'SUBMITTED', 'GRADED', 'ABANDONED'))`
- `ON DELETE CASCADE` for quiz_id, user_id, course_enrollment_id
- `ON DELETE SET NULL` for class_id

**Key Changes from v1.0**:
- ✅ Added `course_enrollment_id` (proper relationship)
- ✅ Changed `class_id` to optional (supports self-paced learning)
- ✅ Separated auto_score and manual_score

**Indexes**:
- Primary key on `attempt_id`
- Index on `(user_id, quiz_id)`
- Index on `course_enrollment_id`
- Index on `status`

---

#### 3.3.6. QuizSubmission

**Purpose**: Individual question answers within a quiz attempt (RENAMED from Submission).

**Table Name**: `QuizSubmission`

| Column | Data Type | Constraints | Description |
|--------|-----------|-------------|-------------|
| quiz_submission_id | UUID | PRIMARY KEY, DEFAULT gen_random_uuid() | Unique identifier |
| attempt_id | UUID | NOT NULL, FK → Attempt.attempt_id, CASCADE | Attempt reference |
| question_id | UUID | NOT NULL, FK → Question.question_id, RESTRICT | Question reference |
| answer_text | TEXT | | Text answer (ESSAY, SHORT_ANSWER) |
| selected_option_ids | UUID[] | | Selected options (MCQ) |
| submitted_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Submission time |
| auto_score | DECIMAL(5,2) | | Auto-graded score |
| manual_score | DECIMAL(5,2) | | Manual score |
| final_score | DECIMAL(5,2) | | Final score |
| max_points | DECIMAL(5,2) | NOT NULL | Maximum points |
| instructor_feedback | TEXT | | Instructor comments |
| graded_at | TIMESTAMP | | Grading time |
| graded_by | UUID | FK → User.user_id | Grader |

**Constraints**:
- `ON DELETE CASCADE` when attempt is deleted
- `ON DELETE RESTRICT` for question_id

**Indexes**:
- Primary key on `quiz_submission_id`
- Index on `attempt_id`
- Index on `question_id`

---

#### 3.3.7. Assignment

**Purpose**: Assignment configuration (NEW - replaces old Submission table).

**Table Name**: `Assignment`

| Column | Data Type | Constraints | Description |
|--------|-----------|-------------|-------------|
| assignment_id | UUID | PRIMARY KEY, DEFAULT gen_random_uuid() | Unique identifier |
| course_id | UUID | NOT NULL, FK → Course.course_id, CASCADE | Parent course |
| class_id | UUID | FK → Class.class_id, SET NULL | Optional class (blended) |
| title | VARCHAR(200) | NOT NULL | Assignment title |
| description | TEXT | NOT NULL | Assignment description |
| instructions | TEXT | | Detailed instructions |
| assignment_type | VARCHAR(20) | NOT NULL | Assignment type |
| max_points | DECIMAL(6,2) | NOT NULL | Maximum points |
| due_date | TIMESTAMP | NOT NULL | Due date |
| late_submission_allowed | BOOLEAN | DEFAULT TRUE | Allow late submissions |
| late_penalty_percent | DECIMAL(5,2) | DEFAULT 10.00 | Late penalty (%) |
| max_late_days | INT | DEFAULT 7 | Maximum days late |
| allow_resubmission | BOOLEAN | DEFAULT FALSE | Allow multiple submissions |
| max_submissions | INT | DEFAULT 1 | Max submission count |
| rubric | JSON | | Grading rubric (JSON) |
| auto_grading_enabled | BOOLEAN | DEFAULT FALSE | Auto-grade flag (for CODE) |
| test_cases | JSON | | Test cases for auto-grading |
| created_by | UUID | FK → User.user_id | Creator |
| created_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Record creation time |
| updated_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Last update time |

**Constraints**:
- `CHECK (assignment_type IN ('ESSAY', 'CODE', 'FILE_UPLOAD', 'PROBLEM_SET', 'PROJECT'))`
- `CHECK (late_penalty_percent >= 0 AND late_penalty_percent <= 100)`

**Indexes**:
- Primary key on `assignment_id`
- Index on `course_id`
- Index on `class_id`
- Index on `due_date`

---

#### 3.3.8. AssignmentSubmission

**Purpose**: Student assignment submissions with versioning (NEW).

**Table Name**: `AssignmentSubmission`

| Column | Data Type | Constraints | Description |
|--------|-----------|-------------|-------------|
| assignment_submission_id | UUID | PRIMARY KEY, DEFAULT gen_random_uuid() | Unique identifier |
| assignment_id | UUID | NOT NULL, FK → Assignment.assignment_id, CASCADE | Assignment reference |
| user_id | UUID | NOT NULL, FK → User.user_id, CASCADE | Student reference |
| course_enrollment_id | UUID | NOT NULL, FK → CourseEnrollment, CASCADE | Enrollment reference |
| submission_number | INT | NOT NULL, DEFAULT 1 | Submission version |
| submitted_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Submission time |
| content | TEXT | | Text content (ESSAY) |
| file_urls | JSON | | Uploaded files (array) |
| code_submission | TEXT | | Code content (CODE type) |
| is_late | BOOLEAN | DEFAULT FALSE | Late submission flag |
| days_late | INT | DEFAULT 0 | Days late |
| status | VARCHAR(20) | NOT NULL, DEFAULT 'SUBMITTED' | Submission status |
| auto_score | DECIMAL(6,2) | | Auto-graded score |
| manual_score | DECIMAL(6,2) | | Manual score |
| final_score | DECIMAL(6,2) | | Final score (after penalty) |
| penalty_applied | DECIMAL(6,2) | DEFAULT 0.00 | Late penalty deducted |
| rubric_scores | JSON | | Rubric scores (JSON) |
| feedback | TEXT | | Instructor feedback |
| graded_at | TIMESTAMP | | Grading time |
| graded_by | UUID | FK → User.user_id | Grader |
| version | INT | DEFAULT 1 | Version number |

**Constraints**:
- `UNIQUE(assignment_id, user_id, submission_number)` - Unique submission per user
- `CHECK (status IN ('DRAFT', 'SUBMITTED', 'GRADING', 'GRADED', 'RETURNED'))`
- `ON DELETE CASCADE` for assignment_id, user_id, course_enrollment_id

**Indexes**:
- Primary key on `assignment_submission_id`
- Index on `assignment_id`
- Index on `user_id`
- Index on `course_enrollment_id`
- Index on `status`

---

#### 3.3.9. GradeBook

**Purpose**: Aggregated grades for a student in a course (NEW).

**Table Name**: `GradeBook`

| Column | Data Type | Constraints | Description |
|--------|-----------|-------------|-------------|
| gradebook_id | UUID | PRIMARY KEY, DEFAULT gen_random_uuid() | Unique identifier |
| user_id | UUID | NOT NULL, FK → User.user_id, CASCADE | Student reference |
| course_id | UUID | NOT NULL, FK → Course.course_id, CASCADE | Course reference |
| class_id | UUID | FK → Class.class_id, SET NULL | Optional class |
| quiz_score | DECIMAL(6,2) | DEFAULT 0.00 | Total quiz score |
| assignment_score | DECIMAL(6,2) | DEFAULT 0.00 | Total assignment score |
| participation_score | DECIMAL(6,2) | DEFAULT 0.00 | Participation score |
| total_score | DECIMAL(6,2) | DEFAULT 0.00 | Total raw score |
| weighted_score | DECIMAL(6,2) | DEFAULT 0.00 | Weighted final score |
| letter_grade | VARCHAR(5) | | Letter grade (A, B+, etc.) |
| last_updated_at | TIMESTAMP | | Last grade update |

**Constraints**:
- `UNIQUE(user_id, course_id, COALESCE(class_id, '00000000-0000-0000-0000-000000000000'))` - One gradebook per student per course/class
- `ON DELETE CASCADE` for user_id, course_id
- `ON DELETE SET NULL` for class_id

**Indexes**:
- Primary key on `gradebook_id`
- Index on `(user_id, course_id)`
- Index on `course_id`

---

### 3.4. Enrollment & Progress Domain (4 tables)

#### 3.4.1. CourseEnrollment

**Purpose**: Course enrollment records.

**Table Name**: `CourseEnrollment`

| Column | Data Type | Constraints | Description |
|--------|-----------|-------------|-------------|
| course_enrollment_id | UUID | PRIMARY KEY, DEFAULT gen_random_uuid() | Unique identifier |
| user_id | UUID | NOT NULL, FK → User.user_id, CASCADE | Student reference |
| course_id | UUID | NOT NULL, FK → Course.course_id, CASCADE | Course reference |
| role_in_course | VARCHAR(20) | NOT NULL | User role in course |
| enrollment_status | VARCHAR(20) | DEFAULT 'ACTIVE' | Enrollment status |
| enrolled_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Enrollment date |
| completed_at | TIMESTAMP | | Completion date |
| final_grade | DECIMAL(5,2) | | Final grade (0-100) |
| completion_percentage | DECIMAL(5,2) | DEFAULT 0.00 | Progress percentage |
| last_accessed_at | TIMESTAMP | | Last access time |

**Constraints**:
- `UNIQUE(user_id, course_id)` - One enrollment per user per course
- `CHECK (role_in_course IN ('STUDENT', 'INSTRUCTOR', 'TA'))`
- `CHECK (enrollment_status IN ('ACTIVE', 'COMPLETED', 'DROPPED', 'SUSPENDED'))`
- `CHECK (completion_percentage >= 0 AND completion_percentage <= 100)`
- `ON DELETE CASCADE` for user_id, course_id

**Indexes**:
- Primary key on `course_enrollment_id`
- Index on `user_id`
- Index on `course_id`
- Index on `enrollment_status`
- Unique index on `(user_id, course_id)`

---

#### 3.4.2. ClassEnrollment

**Purpose**: Class enrollment (for blended learning).

**Table Name**: `ClassEnrollment`

| Column | Data Type | Constraints | Description |
|--------|-----------|-------------|-------------|
| class_enrollment_id | UUID | PRIMARY KEY, DEFAULT gen_random_uuid() | Unique identifier |
| user_id | UUID | NOT NULL, FK → User.user_id, CASCADE | Student reference |
| class_id | UUID | NOT NULL, FK → Class.class_id, CASCADE | Class reference |
| course_enrollment_id | UUID | FK → CourseEnrollment, CASCADE | Related course enrollment |
| role_in_class | VARCHAR(20) | NOT NULL | User role in class |
| enrolled_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Enrollment date |

**Constraints**:
- `UNIQUE(user_id, class_id)` - One enrollment per user per class
- `CHECK (role_in_class IN ('STUDENT', 'INSTRUCTOR', 'TA'))`
- `ON DELETE CASCADE` for user_id, class_id, course_enrollment_id

**Indexes**:
- Primary key on `class_enrollment_id`
- Index on `user_id`
- Index on `class_id`
- Index on `course_enrollment_id`

---

#### 3.4.3. Progress

**Purpose**: Learning progress tracking (REDESIGNED - fixed from v1.0).

**Table Name**: `Progress`

| Column | Data Type | Constraints | Description |
|--------|-----------|-------------|-------------|
| progress_id | UUID | PRIMARY KEY, DEFAULT gen_random_uuid() | Unique identifier |
| user_id | UUID | NOT NULL, FK → User.user_id, CASCADE | Student reference |
| course_id | UUID | NOT NULL, FK → Course.course_id, CASCADE | Course reference |
| class_id | UUID | FK → Class.class_id, SET NULL | Optional class |
| module_id | UUID | NOT NULL, FK → Module.module_id, CASCADE | Module reference |
| lecture_id | UUID | NOT NULL, FK → Lecture.lecture_id, CASCADE | Lecture reference |
| status | VARCHAR(20) | NOT NULL, DEFAULT 'NOT_STARTED' | Progress status |
| percent_complete | DECIMAL(5,2) | DEFAULT 0.00 | Completion percentage |
| last_position_seconds | INT | DEFAULT 0 | Video position (for resume) |
| first_accessed_at | TIMESTAMP | | First access time |
| last_accessed_at | TIMESTAMP | | Last access time |
| completed_at | TIMESTAMP | | Completion time |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Record creation time |
| updated_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Last update time |

**Constraints**:
- `UNIQUE(user_id, course_id, COALESCE(class_id, '00000000-0000-0000-0000-000000000000'), module_id, lecture_id)` - One progress per user per lecture
- `CHECK (status IN ('NOT_STARTED', 'IN_PROGRESS', 'COMPLETED', 'SKIPPED'))`
- `CHECK (percent_complete >= 0 AND percent_complete <= 100)`
- `ON DELETE CASCADE` for user_id, course_id, module_id, lecture_id
- `ON DELETE SET NULL` for class_id

**Key Changes from v1.0**:
- ✅ Added `course_id` (track at course level)
- ✅ Added `module_id` (track at module level)
- ✅ Lecture-level tracking (already existed)
- ✅ Can also track quiz_id, assignment_id via separate logic

**Indexes**:
- Primary key on `progress_id`
- Index on `(user_id, course_id)`
- Index on `status`
- Index on `lecture_id`

---

#### 3.4.4. Attendance

**Purpose**: In-person class attendance tracking.

**Table Name**: `Attendance`

| Column | Data Type | Constraints | Description |
|--------|-----------|-------------|-------------|
| attendance_id | UUID | PRIMARY KEY, DEFAULT gen_random_uuid() | Unique identifier |
| schedule_id | UUID | NOT NULL, FK → Schedule.schedule_id, CASCADE | Session reference |
| user_id | UUID | NOT NULL, FK → User.user_id, CASCADE | Student reference |
| status | VARCHAR(20) | NOT NULL | Attendance status |
| check_in_time | TIMESTAMP | | Actual check-in time |
| notes | TEXT | | Attendance notes |
| recorded_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Record time |

**Constraints**:
- `UNIQUE(schedule_id, user_id)` - One attendance per student per session
- `CHECK (status IN ('PRESENT', 'ABSENT', 'LATE', 'EXCUSED'))`
- `ON DELETE CASCADE` for schedule_id, user_id

**Indexes**:
- Primary key on `attendance_id`
- Index on `schedule_id`
- Index on `user_id`
- Index on `status`

---

### 3.5. Class & Blended Learning Domain (3 tables)

#### 3.5.1. Class

**Purpose**: Physical/hybrid class information.

**Table Name**: `Class`

| Column | Data Type | Constraints | Description |
|--------|-----------|-------------|-------------|
| class_id | UUID | PRIMARY KEY, DEFAULT gen_random_uuid() | Unique identifier |
| course_id | UUID | NOT NULL, FK → Course.course_id, CASCADE | Parent course |
| instructor_id | UUID | FK → User.user_id, SET NULL | Instructor |
| name | VARCHAR(100) | NOT NULL | Class name |
| start_date | DATE | | Class start date |
| end_date | DATE | | Class end date |
| status | VARCHAR(20) | DEFAULT 'SCHEDULED' | Class status |
| max_students | INT | | Maximum enrollment |
| location | VARCHAR(200) | | Physical location |
| created_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Record creation time |
| updated_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Last update time |

**Constraints**:
- `CHECK (status IN ('SCHEDULED', 'ONGOING', 'COMPLETED', 'CANCELLED'))`
- `ON DELETE CASCADE` for course_id
- `ON DELETE SET NULL` for instructor_id

**Indexes**:
- Primary key on `class_id`
- Index on `course_id`
- Index on `instructor_id`
- Index on `status`

---

#### 3.5.2. Schedule

**Purpose**: Class session schedule.

**Table Name**: `Schedule`

| Column | Data Type | Constraints | Description |
|--------|-----------|-------------|-------------|
| schedule_id | UUID | PRIMARY KEY, DEFAULT gen_random_uuid() | Unique identifier |
| class_id | UUID | NOT NULL, FK → Class.class_id, CASCADE | Parent class |
| session_date | DATE | NOT NULL | Session date |
| start_time | TIME | NOT NULL | Start time |
| end_time | TIME | NOT NULL | End time |
| location | VARCHAR(200) | | Session location |
| topic | VARCHAR(200) | | Session topic |
| session_type | VARCHAR(20) | DEFAULT 'IN_PERSON' | Session type |
| meeting_url | VARCHAR(500) | | Online meeting URL |
| created_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Record creation time |

**Constraints**:
- `CHECK (session_type IN ('IN_PERSON', 'ONLINE', 'HYBRID'))`
- `ON DELETE CASCADE` when class is deleted

**Indexes**:
- Primary key on `schedule_id`
- Index on `class_id`
- Index on `session_date`

---

### 3.6. Certificate Domain (3 tables)

#### 3.6.1. CertificateTemplate

**Purpose**: Certificate design templates (NEW).

**Table Name**: `CertificateTemplate`

| Column | Data Type | Constraints | Description |
|--------|-----------|-------------|-------------|
| template_id | UUID | PRIMARY KEY, DEFAULT gen_random_uuid() | Unique identifier |
| name | VARCHAR(100) | NOT NULL, UNIQUE | Template name |
| description | TEXT | | Template description |
| background_image_url | VARCHAR(500) | | Background image |
| layout_config | JSON | | Layout configuration (JSON) |
| html_template | TEXT | | HTML template |
| is_active | BOOLEAN | DEFAULT TRUE | Active status |
| is_default | BOOLEAN | DEFAULT FALSE | Default template flag |
| created_by | UUID | FK → User.user_id | Creator |
| created_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Record creation time |
| updated_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Last update time |

**Indexes**:
- Primary key on `template_id`
- Unique index on `name`
- Index on `is_active`
- Index on `is_default`

---

#### 3.6.2. Certificate

**Purpose**: Issued certificates (REDESIGNED).

**Table Name**: `Certificate`

| Column | Data Type | Constraints | Description |
|--------|-----------|-------------|-------------|
| certificate_id | UUID | PRIMARY KEY, DEFAULT gen_random_uuid() | Unique identifier |
| user_id | UUID | NOT NULL, FK → User.user_id, CASCADE | Student reference |
| course_id | UUID | NOT NULL, FK → Course.course_id, CASCADE | Course reference |
| course_enrollment_id | UUID | NOT NULL, FK → CourseEnrollment, CASCADE | Enrollment reference |
| template_id | UUID | FK → CertificateTemplate, SET NULL | Template used |
| certificate_code | VARCHAR(50) | NOT NULL, UNIQUE | Unique certificate code |
| verification_code | VARCHAR(100) | NOT NULL, UNIQUE | Verification code |
| title | VARCHAR(200) | NOT NULL | Certificate title |
| issue_date | DATE | NOT NULL | Issuance date |
| completion_date | DATE | NOT NULL | Course completion date |
| final_grade | DECIMAL(5,2) | | Final grade |
| grade_letter | VARCHAR(5) | | Letter grade |
| pdf_url | VARCHAR(500) | | PDF file URL |
| qr_code_url | VARCHAR(500) | | QR code image URL |
| verification_url | VARCHAR(500) | | Public verification URL |
| status | VARCHAR(20) | DEFAULT 'ACTIVE' | Certificate status |
| valid_from | DATE | NOT NULL | Validity start date |
| valid_until | DATE | | Validity end date (optional) |
| revoked_at | TIMESTAMP | | Revocation time |
| revoked_by | UUID | FK → User.user_id | Who revoked |
| revoke_reason | TEXT | | Revocation reason |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Record creation time |

**Constraints**:
- `UNIQUE(user_id, course_id)` - One certificate per user per course
- `UNIQUE(certificate_code)` - Unique code for verification
- `UNIQUE(verification_code)` - Unique verification code
- `CHECK (status IN ('ACTIVE', 'REVOKED', 'EXPIRED'))`
- `ON DELETE CASCADE` for user_id, course_id, course_enrollment_id
- `ON DELETE SET NULL` for template_id

**Indexes**:
- Primary key on `certificate_id`
- Unique index on `certificate_code`
- Unique index on `verification_code`
- Index on `user_id`
- Index on `course_id`
- Index on `status`

---

#### 3.6.3. CertificateVerification

**Purpose**: Certificate verification log (NEW).

**Table Name**: `CertificateVerification`

| Column | Data Type | Constraints | Description |
|--------|-----------|-------------|-------------|
| verification_id | UUID | PRIMARY KEY, DEFAULT gen_random_uuid() | Unique identifier |
| certificate_id | UUID | NOT NULL, FK → Certificate.certificate_id, CASCADE | Certificate reference |
| verified_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Verification time |
| verified_by_ip | VARCHAR(45) | | IP address |
| verification_method | VARCHAR(20) | | Verification method |
| verification_result | VARCHAR(20) | NOT NULL | Verification result |

**Constraints**:
- `CHECK (verification_method IN ('CODE', 'QR', 'URL'))`
- `CHECK (verification_result IN ('VALID', 'REVOKED', 'EXPIRED', 'NOT_FOUND'))`
- `ON DELETE CASCADE` when certificate is deleted

**Indexes**:
- Primary key on `verification_id`
- Index on `certificate_id`
- Index on `verified_at DESC`

---

### 3.7. Notification Domain (3 tables - NEW)

#### 3.7.1. Notification

**Purpose**: User notifications (multi-channel).

**Table Name**: `Notification`

| Column | Data Type | Constraints | Description |
|--------|-----------|-------------|-------------|
| notification_id | UUID | PRIMARY KEY, DEFAULT gen_random_uuid() | Unique identifier |
| user_id | UUID | NOT NULL, FK → User.user_id, CASCADE | Recipient |
| notification_type | VARCHAR(50) | NOT NULL | Notification type |
| title | VARCHAR(200) | NOT NULL | Notification title |
| message | TEXT | NOT NULL | Notification message |
| related_entity_type | VARCHAR(50) | | Related entity type |
| related_entity_id | UUID | | Related entity ID |
| action_url | VARCHAR(500) | | Action URL |
| priority | VARCHAR(20) | DEFAULT 'NORMAL' | Priority level |
| is_read | BOOLEAN | DEFAULT FALSE | Read status |
| read_at | TIMESTAMP | | Read time |
| sent_via_email | BOOLEAN | DEFAULT FALSE | Email sent flag |
| sent_via_push | BOOLEAN | DEFAULT FALSE | Push sent flag |
| created_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Record creation time |
| expires_at | TIMESTAMP | | Expiration time |

**Constraints**:
- `CHECK (notification_type IN ('ASSIGNMENT_DUE', 'GRADE_PUBLISHED', 'CERTIFICATE_ISSUED', 'COURSE_UPDATE', 'CLASS_REMINDER', 'ENROLLMENT_CONFIRMED'))`
- `CHECK (priority IN ('LOW', 'NORMAL', 'HIGH', 'URGENT'))`
- `ON DELETE CASCADE` when user is deleted

**Indexes**:
- Primary key on `notification_id`
- Index on `user_id`
- Index on `notification_type`
- Index on `is_read`
- Index on `created_at DESC`

---

#### 3.7.2. NotificationPreference

**Purpose**: User notification preferences.

**Table Name**: `NotificationPreference`

| Column | Data Type | Constraints | Description |
|--------|-----------|-------------|-------------|
| preference_id | UUID | PRIMARY KEY, DEFAULT gen_random_uuid() | Unique identifier |
| user_id | UUID | NOT NULL, FK → User.user_id, CASCADE | User reference |
| notification_type | VARCHAR(50) | NOT NULL | Notification type |
| email_enabled | BOOLEAN | DEFAULT TRUE | Enable email |
| push_enabled | BOOLEAN | DEFAULT TRUE | Enable push |
| sms_enabled | BOOLEAN | DEFAULT FALSE | Enable SMS |
| frequency | VARCHAR(20) | DEFAULT 'IMMEDIATE' | Delivery frequency |
| updated_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Last update time |

**Constraints**:
- `UNIQUE(user_id, notification_type)` - One preference per user per type
- `CHECK (frequency IN ('IMMEDIATE', 'DAILY_DIGEST', 'WEEKLY_DIGEST', 'NEVER'))`
- `ON DELETE CASCADE` when user is deleted

**Indexes**:
- Primary key on `preference_id`
- Index on `user_id`
- Unique index on `(user_id, notification_type)`

---

#### 3.7.3. NotificationLog

**Purpose**: Notification delivery log.

**Table Name**: `NotificationLog`

| Column | Data Type | Constraints | Description |
|--------|-----------|-------------|-------------|
| log_id | UUID | PRIMARY KEY, DEFAULT gen_random_uuid() | Unique identifier |
| notification_id | UUID | NOT NULL, FK → Notification.notification_id, CASCADE | Notification reference |
| channel | VARCHAR(20) | NOT NULL | Delivery channel |
| status | VARCHAR(20) | NOT NULL | Delivery status |
| sent_at | TIMESTAMP | | Sent time |
| error_message | TEXT | | Error details (if failed) |
| attempts | INT | DEFAULT 1 | Delivery attempts |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Record creation time |

**Constraints**:
- `CHECK (channel IN ('EMAIL', 'PUSH', 'SMS', 'IN_APP'))`
- `CHECK (status IN ('PENDING', 'SENT', 'FAILED', 'BOUNCED'))`
- `ON DELETE CASCADE` when notification is deleted

**Indexes**:
- Primary key on `log_id`
- Index on `notification_id`
- Index on `channel`
- Index on `status`
- Index on `sent_at DESC`

---

### 3.8. Audit & System Domain (3 tables - NEW)

#### 3.8.1. ActivityLog

**Purpose**: Audit log for all system activities.

**Table Name**: `ActivityLog`

| Column | Data Type | Constraints | Description |
|--------|-----------|-------------|-------------|
| log_id | UUID | PRIMARY KEY, DEFAULT gen_random_uuid() | Unique identifier |
| user_id | UUID | FK → User.user_id, SET NULL | User who performed action |
| action | VARCHAR(100) | NOT NULL | Action performed |
| entity_type | VARCHAR(50) | NOT NULL | Entity type |
| entity_id | UUID | NOT NULL | Entity ID |
| description | TEXT | | Action description |
| old_values | JSON | | Previous values (JSON) |
| new_values | JSON | | New values (JSON) |
| ip_address | VARCHAR(45) | | IP address |
| user_agent | TEXT | | Browser user agent |
| created_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Record creation time |
| log_date | DATE | GENERATED ALWAYS AS (created_at::DATE) STORED | Partition key |

**Constraints**:
- `ON DELETE SET NULL` for user_id (keep logs even if user deleted)

**Indexes**:
- Primary key on `log_id`
- Index on `(user_id, created_at DESC)`
- Index on `(entity_type, entity_id, created_at DESC)`
- Index on `log_date` (for partitioning)

---

#### 3.8.2. File

**Purpose**: File upload management.

**Table Name**: `File`

| Column | Data Type | Constraints | Description |
|--------|-----------|-------------|-------------|
| file_id | UUID | PRIMARY KEY, DEFAULT gen_random_uuid() | Unique identifier |
| uploaded_by | UUID | NOT NULL, FK → User.user_id, CASCADE | Uploader |
| original_filename | VARCHAR(255) | NOT NULL | Original file name |
| stored_filename | VARCHAR(255) | NOT NULL | Stored file name |
| file_path | VARCHAR(500) | NOT NULL | File path |
| file_url | VARCHAR(500) | NOT NULL | Accessible URL |
| file_size_bytes | BIGINT | NOT NULL | File size |
| mime_type | VARCHAR(100) | NOT NULL | MIME type |
| entity_type | VARCHAR(50) | | Related entity type |
| entity_id | UUID | | Related entity ID |
| storage_type | VARCHAR(20) | DEFAULT 'LOCAL' | Storage type |
| is_deleted | BOOLEAN | DEFAULT FALSE | Soft delete flag |
| uploaded_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Upload time |
| deleted_at | TIMESTAMP | | Deletion time |

**Constraints**:
- `CHECK (storage_type IN ('LOCAL', 'S3', 'AZURE', 'GCS'))`
- `ON DELETE CASCADE` when uploader is deleted

**Indexes**:
- Primary key on `file_id`
- Index on `uploaded_by`
- Index on `(entity_type, entity_id)`
- Index on `is_deleted`
- Index on `uploaded_at DESC`

---

#### 3.8.3. SystemSettings

**Purpose**: System configuration key-value store.

**Table Name**: `SystemSettings`

| Column | Data Type | Constraints | Description |
|--------|-----------|-------------|-------------|
| setting_id | UUID | PRIMARY KEY, DEFAULT gen_random_uuid() | Unique identifier |
| setting_key | VARCHAR(100) | NOT NULL, UNIQUE | Setting key |
| setting_value | TEXT | NOT NULL | Setting value |
| data_type | VARCHAR(20) | DEFAULT 'STRING' | Value data type |
| category | VARCHAR(50) | | Setting category |
| description | TEXT | | Setting description |
| is_editable | BOOLEAN | DEFAULT TRUE | Editable flag |
| updated_by | UUID | FK → User.user_id | Last updater |
| updated_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Last update time |

**Constraints**:
- `UNIQUE(setting_key)` - Unique setting key
- `CHECK (data_type IN ('STRING', 'INTEGER', 'DECIMAL', 'BOOLEAN', 'JSON'))`

**Indexes**:
- Primary key on `setting_id`
- Unique index on `setting_key`
- Index on `category`

---

## 4. RELATIONSHIPS

### 4.1. Relationship Matrix

| From Table | To Table | Type | Cardinality | FK Field | Cascade | Description |
|------------|----------|------|-------------|----------|---------|-------------|
| **User Management** |
| UserRole | User | Many-to-One | N:1 | user_id | CASCADE | User has roles |
| UserRole | Role | Many-to-One | N:1 | role_id | CASCADE | Role assigned to users |
| **Course Content** |
| Module | Course | Many-to-One | N:1 | course_id | CASCADE | Course contains modules |
| Lecture | Module | Many-to-One | N:1 | module_id | CASCADE | Module contains lectures |
| Resource | Lecture | Many-to-One | N:1 | lecture_id | CASCADE | Lecture has resources |
| **Assessment** |
| Quiz | Course | Many-to-One | N:1 | course_id | CASCADE | Course has quizzes |
| Question | Course | Many-to-One | N:1 | course_id | CASCADE | Course has questions |
| Option | Question | Many-to-One | N:1 | question_id | CASCADE | Question has options |
| QuizQuestion | Quiz | Many-to-One | N:1 | quiz_id | CASCADE | Quiz contains questions |
| QuizQuestion | Question | Many-to-One | N:1 | question_id | RESTRICT | Question used in quizzes |
| Attempt | Quiz | Many-to-One | N:1 | quiz_id | CASCADE | Quiz has attempts |
| Attempt | User | Many-to-One | N:1 | user_id | CASCADE | User makes attempts |
| Attempt | CourseEnrollment | Many-to-One | N:1 | course_enrollment_id | CASCADE | Attempt linked to enrollment |
| Attempt | Class | Many-to-One | N:1 | class_id | SET NULL | Optional class context |
| QuizSubmission | Attempt | Many-to-One | N:1 | attempt_id | CASCADE | Attempt has submissions |
| QuizSubmission | Question | Many-to-One | N:1 | question_id | RESTRICT | Submission for question |
| Assignment | Course | Many-to-One | N:1 | course_id | CASCADE | Course has assignments |
| Assignment | Class | Many-to-One | N:1 | class_id | SET NULL | Optional class assignment |
| AssignmentSubmission | Assignment | Many-to-One | N:1 | assignment_id | CASCADE | Assignment has submissions |
| AssignmentSubmission | User | Many-to-One | N:1 | user_id | CASCADE | User submits assignment |
| AssignmentSubmission | CourseEnrollment | Many-to-One | N:1 | course_enrollment_id | CASCADE | Submission linked to enrollment |
| GradeBook | User | Many-to-One | N:1 | user_id | CASCADE | User has gradebook |
| GradeBook | Course | Many-to-One | N:1 | course_id | CASCADE | Gradebook for course |
| GradeBook | Class | Many-to-One | N:1 | class_id | SET NULL | Optional class gradebook |
| **Enrollment & Progress** |
| CourseEnrollment | User | Many-to-One | N:1 | user_id | CASCADE | User enrolls in course |
| CourseEnrollment | Course | Many-to-One | N:1 | course_id | CASCADE | Course has enrollments |
| ClassEnrollment | User | Many-to-One | N:1 | user_id | CASCADE | User enrolls in class |
| ClassEnrollment | Class | Many-to-One | N:1 | class_id | CASCADE | Class has enrollments |
| ClassEnrollment | CourseEnrollment | Many-to-One | N:1 | course_enrollment_id | CASCADE | Class enrollment linked to course |
| Progress | User | Many-to-One | N:1 | user_id | CASCADE | User has progress |
| Progress | Course | Many-to-One | N:1 | course_id | CASCADE | Progress for course |
| Progress | Class | Many-to-One | N:1 | class_id | SET NULL | Optional class progress |
| Progress | Module | Many-to-One | N:1 | module_id | CASCADE | Progress for module |
| Progress | Lecture | Many-to-One | N:1 | lecture_id | CASCADE | Progress for lecture |
| Attendance | Schedule | Many-to-One | N:1 | schedule_id | CASCADE | Attendance for session |
| Attendance | User | Many-to-One | N:1 | user_id | CASCADE | User attendance |
| **Class & Blended Learning** |
| Class | Course | Many-to-One | N:1 | course_id | CASCADE | Course has classes |
| Class | User (instructor) | Many-to-One | N:1 | instructor_id | SET NULL | Instructor teaches class |
| Schedule | Class | Many-to-One | N:1 | class_id | CASCADE | Class has sessions |
| **Certificate** |
| Certificate | User | Many-to-One | N:1 | user_id | CASCADE | User receives certificate |
| Certificate | Course | Many-to-One | N:1 | course_id | CASCADE | Certificate for course |
| Certificate | CourseEnrollment | Many-to-One | N:1 | course_enrollment_id | CASCADE | Certificate from enrollment |
| Certificate | CertificateTemplate | Many-to-One | N:1 | template_id | SET NULL | Certificate uses template |
| CertificateVerification | Certificate | Many-to-One | N:1 | certificate_id | CASCADE | Certificate verification log |
| **Notification** |
| Notification | User | Many-to-One | N:1 | user_id | CASCADE | User receives notifications |
| NotificationPreference | User | Many-to-One | N:1 | user_id | CASCADE | User notification preferences |
| NotificationLog | Notification | Many-to-One | N:1 | notification_id | CASCADE | Notification delivery log |
| **Audit & System** |
| ActivityLog | User | Many-to-One | N:1 | user_id | SET NULL | User activity log |
| File | User (uploader) | Many-to-One | N:1 | uploaded_by | CASCADE | User uploads files |
| SystemSettings | User (updater) | Many-to-One | N:1 | updated_by | SET NULL | User updates settings |

**Total Relationships**: 48

---

### 4.2. Key Relationship Types

#### One-to-Many (1:N)
Most relationships in the system are one-to-many:
- Course → Module → Lecture → Resource (content hierarchy)
- Quiz → Attempt → QuizSubmission (quiz taking flow)
- Assignment → AssignmentSubmission (assignment submissions)
- User → CourseEnrollment → Progress (learning tracking)

#### Many-to-Many (M:N)
Implemented via junction tables:
- User ↔ Role (via UserRole)
- Quiz ↔ Question (via QuizQuestion)

#### Optional Relationships (nullable FK)
- Attempt.class_id → Class (optional for self-paced learning)
- Progress.class_id → Class (optional for online-only courses)
- Assignment.class_id → Class (optional for course-level assignments)

---

## 5. INDEXES STRATEGY

### 5.1. Index Categories

#### Primary Key Indexes
- All tables have UUID primary keys with automatic indexes
- Format: `[table_name]_pkey` (auto-created)

#### Foreign Key Indexes
- All foreign keys should have indexes for JOIN performance
- Format: `idx_[table]_[fk_field]`
- Example: `idx_module_course_id`

#### Unique Constraints
- Implemented as unique indexes
- Examples:
  - `User.email` - unique index
  - `Course.code` - unique index
  - `Certificate.certificate_code` - unique index

#### Performance Indexes
For frequently queried fields:
- `User`: account_status, created_at
- `Course`: status, category, published_at
- `Progress`: (user_id, course_id), status
- `Attempt`: (user_id, quiz_id), status
- `Assignment`: due_date, course_id
- `Notification`: (user_id, created_at), is_read

#### Full-Text Search Indexes
Using PostgreSQL GIN indexes:
- `Course`: title + description (for course search)
- `Question`: text (for question search)

#### Composite Indexes
For common query patterns:
- `(user_id, course_id)` on Progress, CourseEnrollment
- `(course_id, order_num)` on Module, Lecture
- `(entity_type, entity_id)` on ActivityLog, File

---

### 5.2. Index Implementation

**Example SQL**:
```sql
-- Foreign key indexes
CREATE INDEX idx_module_course_id ON "Module"(course_id);
CREATE INDEX idx_lecture_module_id ON "Lecture"(module_id);
CREATE INDEX idx_attempt_user_id ON "Attempt"(user_id);
CREATE INDEX idx_attempt_quiz_id ON "Attempt"(quiz_id);

-- Performance indexes
CREATE INDEX idx_user_status ON "User"(account_status);
CREATE INDEX idx_course_status ON "Course"(status);
CREATE INDEX idx_progress_status ON "Progress"(status);

-- Partial indexes (with WHERE clause)
CREATE INDEX idx_course_published ON "Course"(published_at DESC)
  WHERE status = 'PUBLISHED';

-- Composite indexes
CREATE INDEX idx_progress_user_course ON "Progress"(user_id, course_id);
CREATE INDEX idx_activitylog_entity ON "ActivityLog"(entity_type, entity_id, created_at DESC);

-- Full-text search
CREATE INDEX idx_course_search ON "Course" USING GIN(
  to_tsvector('english', title || ' ' || description)
);

CREATE INDEX idx_question_search ON "Question" USING GIN(
  to_tsvector('english', text)
);
```

---

## 6. CONSTRAINTS

### 6.1. Primary Key Constraints
All tables use UUID primary keys:
```sql
[table_name]_id UUID PRIMARY KEY DEFAULT gen_random_uuid()
```

### 6.2. Foreign Key Constraints
With appropriate CASCADE behaviors:

**ON DELETE CASCADE**:
- Parent-child relationships (delete children when parent deleted)
- Examples: Module → Course, Lecture → Module, Attempt → Quiz

**ON DELETE SET NULL**:
- Optional references (preserve child, remove reference)
- Examples: Attempt.class_id, Certificate.template_id

**ON DELETE RESTRICT**:
- Prevent deletion if referenced (protect shared resources)
- Example: QuizQuestion.question_id (can't delete question used in quiz)

### 6.3. UNIQUE Constraints

**Single-column UNIQUE**:
- `User.email`
- `Role.name`
- `Course.code`
- `Certificate.certificate_code`
- `Certificate.verification_code`
- `CertificateTemplate.name`
- `SystemSettings.setting_key`

**Multi-column UNIQUE**:
- `UNIQUE(user_id, role_id)` on UserRole
- `UNIQUE(user_id, course_id)` on CourseEnrollment
- `UNIQUE(user_id, class_id)` on ClassEnrollment
- `UNIQUE(course_id, order_num)` on Module
- `UNIQUE(module_id, order_num)` on Lecture
- `UNIQUE(quiz_id, question_id)` on QuizQuestion
- `UNIQUE(assignment_id, user_id, submission_number)` on AssignmentSubmission
- `UNIQUE(user_id, quiz_id, attempt_number)` on Attempt

### 6.4. CHECK Constraints

**Status/Enum checks**:
```sql
-- User
CHECK (account_status IN ('PENDING_VERIFICATION', 'ACTIVE', 'SUSPENDED', 'DELETED'))

-- Course
CHECK (difficulty_level IN ('BEGINNER', 'INTERMEDIATE', 'ADVANCED', 'EXPERT'))
CHECK (status IN ('DRAFT', 'PUBLISHED', 'ARCHIVED'))

-- Lecture
CHECK (type IN ('VIDEO', 'PDF', 'SLIDE', 'AUDIO', 'TEXT'))

-- Question
CHECK (type IN ('MCQ', 'TRUE_FALSE', 'ESSAY', 'SHORT_ANSWER', 'CODE'))
CHECK (difficulty IN ('EASY', 'MEDIUM', 'HARD'))

-- Attempt
CHECK (status IN ('IN_PROGRESS', 'SUBMITTED', 'GRADED', 'ABANDONED'))

-- Assignment
CHECK (assignment_type IN ('ESSAY', 'CODE', 'FILE_UPLOAD', 'PROBLEM_SET', 'PROJECT'))

-- AssignmentSubmission
CHECK (status IN ('DRAFT', 'SUBMITTED', 'GRADING', 'GRADED', 'RETURNED'))

-- Progress
CHECK (status IN ('NOT_STARTED', 'IN_PROGRESS', 'COMPLETED', 'SKIPPED'))

-- Certificate
CHECK (status IN ('ACTIVE', 'REVOKED', 'EXPIRED'))

-- Notification
CHECK (priority IN ('LOW', 'NORMAL', 'HIGH', 'URGENT'))
```

**Range checks**:
```sql
-- Progress
CHECK (percent_complete >= 0 AND percent_complete <= 100)

-- Assignment
CHECK (late_penalty_percent >= 0 AND late_penalty_percent <= 100)

-- CourseEnrollment
CHECK (completion_percentage >= 0 AND completion_percentage <= 100)
```

### 6.5. NOT NULL Constraints
Critical fields that must have values:
- All primary keys
- All foreign keys (except optional ones)
- User: email, password_hash, first_name, last_name
- Course: code, title, status
- Assignment: title, description, due_date, max_points
- Certificate: certificate_code, verification_code

### 6.6. DEFAULT Values
Commonly used defaults:
- Timestamps: `DEFAULT CURRENT_TIMESTAMP`
- UUIDs: `DEFAULT gen_random_uuid()`
- Status fields: `DEFAULT 'ACTIVE'` or appropriate initial status
- Booleans: `DEFAULT FALSE` or `DEFAULT TRUE`
- Numeric: `DEFAULT 0` or `DEFAULT 0.00`

---

## 7. DIAGRAM

### 7.1. ERD File
The complete Entity Relationship Diagram is available in:
- **File**: `ERD.mdj`
- **Format**: StarUML Data Model Diagram
- **Tool**: StarUML 5.0+

### 7.2. Diagram Layout
The ERD is organized by domain groups:

```
┌─────────────────────────────────────────────────────────────┐
│                     USER MANAGEMENT                         │
│  [User]  [Role]  [UserRole]                                 │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│                    COURSE CONTENT                           │
│  [Course]  [Module]  [Lecture]  [Resource]                  │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│                     ASSESSMENT                              │
│  Row 1: [Quiz]  [Question]  [Option]  [QuizQuestion]        │
│  Row 2: [Attempt]  [QuizSubmission]                         │
│  Row 3: [Assignment]  [AssignmentSubmission]  [GradeBook]   │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│                 ENROLLMENT & PROGRESS                       │
│  [CourseEnrollment]  [ClassEnrollment]                      │
│  [Progress]  [Attendance]                                   │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│                  CLASS & BLENDED LEARNING                   │
│  [Class]  [Schedule]  [Attendance]                          │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│                     CERTIFICATE                             │
│  [CertificateTemplate]  [Certificate]                       │
│  [CertificateVerification]                                  │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│                   NOTIFICATION                              │
│  [Notification]  [NotificationPreference]                   │
│  [NotificationLog]                                          │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│                   AUDIT & SYSTEM                            │
│  [ActivityLog]  [File]  [SystemSettings]                    │
└─────────────────────────────────────────────────────────────┘
```

### 7.3. ERD Notation
**StarUML notation used**:
- **Solid line**: Identifying relationship (FK is part of PK)
- **Dashed line**: Non-identifying relationship (FK is not part of PK)
- **Crow's foot**: Many side (N)
- **Single line**: One side (1)
- **Circle**: Optional (0)
- **Hash**: Mandatory (1)

**Multiplicity examples**:
- `1` - Exactly one
- `0..1` - Zero or one (optional)
- `0..*` - Zero or many
- `1..*` - One or many

---

## 8. VALIDATION CHECKLIST

### 8.1. Completeness
- [x] All 31 tables defined
- [x] All primary keys specified (UUID)
- [x] All foreign keys specified with target tables
- [x] All attributes with data types
- [x] All relationships documented
- [x] All constraints specified

### 8.2. Correctness
- [x] PKs are unique and non-null
- [x] FKs reference correct PKs
- [x] Data types appropriate for data
- [x] ON DELETE behaviors correct
- [x] UNIQUE constraints where needed
- [x] CHECK constraints for enums and ranges

### 8.3. Consistency
- [x] Naming convention: snake_case for columns, PascalCase for tables
- [x] All PKs named `[table]_id`
- [x] All timestamps use TIMESTAMP type
- [x] All UUIDs use UUID type
- [x] All created_at/updated_at present where needed

### 8.4. Business Rules
- [x] User can enroll in course only once
- [x] User can have certificate only once per course
- [x] Module order unique within course
- [x] Lecture order unique within module
- [x] Question appears only once per quiz
- [x] Progress tracking at lecture level

### 8.5. Removed Features
- [x] No Thread table
- [x] No Post table
- [x] No PostVote table
- [x] No ThreadParticipant table
- [x] No PostEditHistory table
- [x] Forum completely removed

### 8.6. New Features
- [x] Assignment system (5 tables)
- [x] Certificate templates and verification (3 tables)
- [x] Notification system (3 tables)
- [x] GradeBook table
- [x] Enhanced Progress tracking

---

## 9. IMPLEMENTATION NOTES

### 9.1. Database Requirements
- **PostgreSQL 14+** (for gen_random_uuid(), JSON, generated columns)
- **Extensions**: uuid-ossp, pg_trgm, btree_gin
- **Encoding**: UTF8
- **Collation**: en_US.UTF-8

### 9.2. Performance Considerations
- Use connection pooling (PgBouncer)
- Index all foreign keys
- Partition ActivityLog by log_date
- Use materialized views for reporting
- Implement query caching

### 9.3. Security Considerations
- Never store plain passwords (use bcrypt/scrypt)
- Use parameterized queries (prevent SQL injection)
- Implement row-level security (RLS) for multi-tenancy
- Encrypt sensitive data at rest
- Use SSL/TLS for connections

### 9.4. Migration from v1.0
If migrating from v1.0, follow this sequence:
1. Create new tables (Certificate, Notification, Assignment)
2. Migrate data from old Submission to AssignmentSubmission
3. Drop forum tables (Thread, Post, etc.)
4. Update Progress table structure
5. Update Attempt table structure
6. Re-create all foreign keys
7. Re-create all indexes

---

## 10. REFERENCES

### 10.1. Related Documents
- [BFD-SPEC.md](./BFD-SPEC.md) - Business Function Diagram
- [DATABASE-SCHEMA.md](./DATABASE-SCHEMA.md) - Complete DDL
- [FUNCTIONAL-REQUIREMENTS.md](./FUNCTIONAL-REQUIREMENTS.md) - Requirements
- [API-ENDPOINTS.md](./API-ENDPOINTS.md) - API design
- [DATABASE-DESIGN-EVALUATION.md](./DATABASE-DESIGN-EVALUATION.md) - v1.0 evaluation

### 10.2. External Resources
- PostgreSQL 14 Documentation: https://www.postgresql.org/docs/14/
- StarUML User Guide: https://docs.staruml.io/
- UML Database Modeling: http://www.uml-diagrams.org/
- Database Normalization: https://en.wikipedia.org/wiki/Database_normalization

---

## 11. CHANGE LOG

| Version | Date | Changes | Author |
|---------|------|---------|--------|
| 2.0 | 2025-11-25 | Complete redesign with 31 tables | Nguyễn Văn Kiệt |
| 1.0 | 2025-XX-XX | Original design (21 tables) | Nguyễn Văn Kiệt |

---

**Document Status**: ✅ Complete
**Last Updated**: 2025-11-25
**Next Review**: Before SQL implementation

---

**END OF ERD SPECIFICATION**
