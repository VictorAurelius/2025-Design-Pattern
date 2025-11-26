# B-Learning Platform Database v2.0

**Complete Database Redesign for Blended Learning Management System**

[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-14%2B-blue.svg)](https://www.postgresql.org/)
[![License](https://img.shields.io/badge/license-MIT-green.svg)](LICENSE)
[![Status](https://img.shields.io/badge/status-production--ready-success.svg)](.)

## 📋 Table of Contents

- [Overview](#overview)
- [What's New in v2.0](#whats-new-in-v20)
- [Features](#features)
- [Database Architecture](#database-architecture)
- [Installation](#installation)
- [Usage](#usage)
- [Documentation](#documentation)
- [API Reference](#api-reference)
- [Development](#development)
- [Testing](#testing)
- [Contributing](#contributing)
- [License](#license)

## 🎯 Overview

B-Learning Platform Database v2.0 is a complete redesign of the database system for a modern blended learning management platform. It combines online self-paced learning with traditional classroom instruction, supporting courses, quizzes, assignments, certificates, and comprehensive progress tracking.

### Key Statistics

- **31 Tables** across 8 domains
- **~85 Indexes** for optimal performance
- **14 Triggers** for business logic automation
- **7 Materialized Views** for analytics
- **70+ REST API Endpoints**
- **PostgreSQL 14+** with UUID, JSON, full-text search

## 🆕 What's New in v2.0

### Additions

✅ **Assignment System** (5 tables)
- Essay, code, file upload, problem sets
- Rubric-based grading
- Late submission handling
- Auto-grading for code assignments

✅ **Certificate Management** (3 tables)
- Auto-issuance upon course completion
- QR code verification
- PDF generation templates
- Certificate revocation support

✅ **Notification System** (3 tables)
- Multi-channel (Email, Push, SMS)
- User preferences per channel
- Delivery tracking and logging
- Priority-based routing

✅ **Enhanced Progress Tracking**
- Course, module, and lecture level
- Watch time tracking
- Completion percentage calculation
- Last accessed timestamps

### Removals

❌ **Forum/Discussion** (5 tables removed)
- Thread, Post, PostVote, ThreadParticipant, PostEditHistory
- Reason: Shift focus to core learning features

### Improvements

⚡ **Fixed Issues from v1.0**
- Progress table now includes course_id and module_id
- Attempt table uses course_enrollment_id (supports self-paced learning)
- Proper CASCADE behaviors on foreign keys
- Optimized indexes for common query patterns

## ✨ Features

### Core Features

- **User Management**: Role-based access control (Admin, Instructor, TA, Student)
- **Course Management**: Multi-level content (Course → Module → Lecture)
- **Assessment**: Quizzes (MCQ, True/False, Essay) and Assignments
- **Blended Learning**: Online + in-person classes with attendance tracking
- **Grading**: Automated + manual grading with rubrics
- **Certificates**: Automated issuance with verification
- **Progress Tracking**: Comprehensive learning analytics
- **Notifications**: Multi-channel with user preferences

### Advanced Features

- **Auto-Grading**: MCQ and True/False questions
- **Full-Text Search**: Course and content discovery
- **Materialized Views**: Real-time analytics dashboards
- **Activity Logging**: Complete audit trail
- **File Management**: Secure upload/download with soft delete
- **Time-based Features**: Session timeouts, assignment deadlines

## 🏗 Database Architecture

### Domain Structure

```
┌─────────────────────────────────────────────────────────────────┐
│                    B-LEARNING DATABASE v2.0                     │
├─────────────────────────────────────────────────────────────────┤
│ 1. User Management (3 tables)                                   │
│    User, Role, UserRole                                         │
│                                                                  │
│ 2. Course Content (5 tables)                                    │
│    Course, Module, Lecture, Resource, CourseEnrollment          │
│                                                                  │
│ 3. Assessment - Quizzes (6 tables)                              │
│    Quiz, Question, Option, QuizQuestion, Attempt,               │
│    QuizSubmission                                               │
│                                                                  │
│ 4. Assessment - Assignments (2 tables) ★ NEW                    │
│    Assignment, AssignmentSubmission                             │
│                                                                  │
│ 5. Grading (1 table)                                            │
│    GradeBook                                                    │
│                                                                  │
│ 6. Progress Tracking (1 table) ★ ENHANCED                       │
│    Progress                                                     │
│                                                                  │
│ 7. Blended Learning (4 tables)                                  │
│    Class, ClassEnrollment, Schedule, Attendance                 │
│                                                                  │
│ 8. Certificates (3 tables) ★ NEW                                │
│    Certificate, CertificateTemplate, CertificateVerification    │
│                                                                  │
│ 9. Notifications (3 tables) ★ NEW                               │
│    Notification, NotificationPreference, NotificationLog        │
│                                                                  │
│ 10. System (3 tables)                                           │
│     ActivityLog, File, SystemSettings                           │
└─────────────────────────────────────────────────────────────────┘
```

### Entity Relationships

- **User** 1:N **CourseEnrollment** N:1 **Course**
- **Course** 1:N **Module** 1:N **Lecture**
- **User** + **Course** → **Progress**, **Attempt**, **AssignmentSubmission**
- **CourseEnrollment** (COMPLETED) → Auto-generates **Certificate**
- **Quiz/Assignment** (graded) → Updates **GradeBook**

## 📦 Installation

### Prerequisites

- PostgreSQL 14 or higher
- psql command-line tool
- (Optional) pgAdmin 4 for GUI management

### Quick Start

1. **Clone the repository**

```bash
git clone <repository-url>
cd 99-B-Learing
```

2. **Create database**

set PGPASSWORD=YourPass

```bash
createdb -U postgres -h localhost blelearning_db
```

psql -U postgres -h localhost -d blelearning_db -f "sql/run-all.sql"
psql -U postgres -h localhost -d blelearning_db -f "sql/01-schema.sql"
psql -U postgres -h localhost -d blelearning_db -f "sql/02-indexes.sql"
psql -U postgres -h localhost -d blelearning_db -f "sql/03-constraints.sql"

3. **Run SQL scripts in order**

```bash
# 1. Create schema (tables, constraints, comments)
psql -d blelearning_db -f sql/01-schema.sql

# 2. Create indexes for performance
psql -d blelearning_db -f sql/02-indexes.sql

# 3. Create additional constraints (optional)
psql -d blelearning_db -f sql/03-constraints.sql

# 4. Create triggers and functions
psql -d blelearning_db -f sql/04-triggers.sql

# 5. Create materialized views
psql -d blelearning_db -f sql/05-views.sql

# 6. Load seed data (development only)
psql -d blelearning_db -f sql/06-seed-data.sql
```

4. **Verify installation**

```bash
psql -d blelearning_db -c "SELECT COUNT(*) FROM \"User\";"
# Should return: 27 (if seed data was loaded)

psql -d blelearning_db -c "SELECT * FROM mv_system_overview;"
# Shows system-wide statistics
```

### Docker Installation

```bash
# Using Docker Compose
docker-compose up -d

# Access database
docker exec -it blelearning_postgres psql -U blelearning -d blelearning_db
```

## 🚀 Usage

### Basic Operations

#### 1. User Registration

```sql
-- Register new user
INSERT INTO "User" (email, password_hash, first_name, last_name)
VALUES ('student@example.com', '$2b$12$...', 'John', 'Doe')
RETURNING user_id;

-- Assign student role
INSERT INTO "UserRole" (user_id, role_id)
VALUES ('<user_id>', (SELECT role_id FROM "Role" WHERE name = 'STUDENT'));
```

#### 2. Course Enrollment

```sql
-- Enroll user in course
INSERT INTO "CourseEnrollment" (user_id, course_id, role_in_course)
VALUES ('<user_id>', '<course_id>', 'STUDENT');
```

#### 3. Track Progress

```sql
-- Mark lecture as completed
INSERT INTO "Progress" (user_id, course_id, module_id, lecture_id, status, percent_complete)
VALUES ('<user_id>', '<course_id>', '<module_id>', '<lecture_id>', 'COMPLETED', 100);

-- Trigger automatically updates CourseEnrollment.completion_percentage
```

#### 4. Take Quiz

```sql
-- Create attempt
INSERT INTO "Attempt" (quiz_id, user_id, course_enrollment_id, attempt_number)
VALUES ('<quiz_id>', '<user_id>', '<enrollment_id>', 1)
RETURNING attempt_id;

-- Submit answers (auto-graded by trigger)
INSERT INTO "QuizSubmission" (attempt_id, question_id, selected_option_ids, max_points)
VALUES ('<attempt_id>', '<question_id>', ARRAY['<option_id>'], 10);

-- Trigger automatically:
-- 1. Grades MCQ/True-False questions
-- 2. Updates Attempt status when all questions answered
-- 3. Updates GradeBook
```

#### 5. Certificate Issuance

```sql
-- Certificates are auto-issued when enrollment is completed
-- Trigger fires when:
UPDATE "CourseEnrollment"
SET enrollment_status = 'COMPLETED', completion_percentage = 100
WHERE user_id = '<user_id>' AND course_id = '<course_id>';

-- Certificate is automatically created with:
-- - Unique certificate code (BL-YYYY-NNNNNN)
-- - Verification code (64-char hex)
-- - Notification sent to user
```

### Materialized Views

```sql
-- Refresh all views (run periodically)
SELECT refresh_all_materialized_views();

-- View course statistics
SELECT * FROM mv_course_statistics
WHERE status = 'PUBLISHED'
ORDER BY completion_rate DESC;

-- View user progress
SELECT * FROM mv_user_progress_summary
WHERE user_id = '<user_id>';

-- View instructor dashboard
SELECT * FROM mv_instructor_dashboard
WHERE instructor_id = '<instructor_id>';
```

### Data Cleanup

```sql
-- Reset entire database (CAUTION: Deletes all data!)
\i sql/99-drop-all.sql

-- Then rebuild
\i sql/01-schema.sql
\i sql/02-indexes.sql
\i sql/03-constraints.sql
\i sql/04-triggers.sql
\i sql/05-views.sql
```

## 📚 Documentation

### Available Documents

- **[BFD-SPEC.md](documents/BFD-SPEC.md)** - Business Function Diagram (43 use cases)
- **[ERD-SPEC.md](documents/ERD-SPEC.md)** - Entity Relationship Diagram (31 tables)
- **[DATABASE-SCHEMA.md](documents/DATABASE-SCHEMA.md)** - Complete DDL specification
- **[FUNCTIONAL-REQUIREMENTS.md](documents/FUNCTIONAL-REQUIREMENTS.md)** - 36 functional + 26 non-functional requirements
- **[API-ENDPOINTS.md](documents/API-ENDPOINTS.md)** - 70+ REST API endpoints
- **[DATABASE-DESIGN-EVALUATION.md](documents/DATABASE-DESIGN-EVALUATION.md)** - v1.0 evaluation and redesign decisions
- **[EXECUTIVE-SUMMARY.md](documents/EXECUTIVE-SUMMARY.md)** - Project overview and decisions

### Quick Links

- [Installation Guide](QUICK-START.md)
- [Development Checklist](CHECKLIST.md)
- [Project Index](INDEX.md)

## 🔌 API Reference

The database supports a REST API with 70+ endpoints across 14 modules:

### Authentication
- `POST /api/v1/auth/register`
- `POST /api/v1/auth/login`
- `POST /api/v1/auth/refresh-token`

### Courses
- `GET /api/v1/courses` - List courses
- `GET /api/v1/courses/{id}` - Get course details
- `POST /api/v1/courses` - Create course (Instructor only)

### Enrollment
- `POST /api/v1/enrollments` - Enroll in course
- `GET /api/v1/enrollments/my-courses` - Get enrolled courses

### Progress
- `GET /api/v1/progress/course/{courseId}` - Get course progress
- `POST /api/v1/progress/lectures/{lectureId}/complete` - Mark lecture complete

### Quizzes
- `POST /api/v1/quizzes/{quizId}/attempts` - Start quiz attempt
- `POST /api/v1/quizzes/attempts/{attemptId}/submit` - Submit quiz answers

### Assignments
- `POST /api/v1/assignments/{assignmentId}/submissions` - Submit assignment
- `GET /api/v1/assignments/{assignmentId}/submissions/my` - Get my submissions

### Certificates
- `GET /api/v1/certificates/my-certificates` - Get my certificates
- `GET /api/v1/certificates/verify?code={code}` - Verify certificate

[See complete API documentation →](documents/API-ENDPOINTS.md)

## 🛠 Development

### Database Connection

```javascript
// Node.js with pg
const { Pool } = require('pg');

const pool = new Pool({
  host: 'localhost',
  port: 5432,
  database: 'blelearning_db',
  user: 'blelearning',
  password: 'secure_password',
});
```

```python
# Python with psycopg2
import psycopg2

conn = psycopg2.connect(
    host="localhost",
    port=5432,
    database="blelearning_db",
    user="blelearning",
    password="secure_password"
)
```

### Scheduled Tasks

Set up cron jobs for:

1. **Refresh Materialized Views** (every hour)
```sql
SELECT cron.schedule(
  'refresh-views-hourly',
  '0 * * * *',
  'SELECT refresh_all_materialized_views();'
);
```

2. **Send Assignment Due Notifications** (daily at 8 AM)
```sql
SELECT cron.schedule(
  'assignment-due-notifications',
  '0 8 * * *',
  'SELECT send_assignment_due_notification();'
);
```

3. **Cleanup Old Activity Logs** (weekly)
```sql
DELETE FROM "ActivityLog"
WHERE created_at < CURRENT_TIMESTAMP - INTERVAL '90 days';
```

## 🧪 Testing

### Test Accounts (from seed data)

| Role | Email | Password |
|------|-------|----------|
| Admin | admin@blelearning.com | Password123! |
| Instructor | nguyen.van.a@instructor.com | Password123! |
| TA | pham.thi.d@ta.com | Password123! |
| Student | student01@example.com | Password123! |

### Run Tests

```sql
-- Test user registration
SELECT COUNT(*) FROM "User" WHERE account_status = 'ACTIVE';

-- Test course enrollment
SELECT u.email, c.title, ce.enrollment_status
FROM "CourseEnrollment" ce
JOIN "User" u ON ce.user_id = u.user_id
JOIN "Course" c ON ce.course_id = c.course_id
LIMIT 5;

-- Test auto-grading
-- (Submit quiz answers and verify auto_score is calculated)

-- Test certificate issuance
-- (Complete a course and verify certificate is auto-generated)

-- Test materialized views
SELECT * FROM mv_course_statistics LIMIT 5;
SELECT * FROM mv_user_progress_summary LIMIT 5;
```

## 📊 Performance

### Optimization Features

- **Indexes**: ~85 indexes on foreign keys, search fields, and composite columns
- **Materialized Views**: Pre-computed aggregations for dashboards
- **Full-Text Search**: GIN indexes on course and question content
- **Partitioning**: Ready for time-based partitioning on ActivityLog and Notification tables
- **Connection Pooling**: Recommended for production deployments

### Monitoring Queries

```sql
-- Check table sizes
SELECT
  schemaname,
  tablename,
  pg_size_pretty(pg_total_relation_size(schemaname||'.'||tablename)) AS size
FROM pg_tables
WHERE schemaname = 'public'
ORDER BY pg_total_relation_size(schemaname||'.'||tablename) DESC;

-- Check slow queries (requires pg_stat_statements)
SELECT
  query,
  calls,
  total_time,
  mean_time,
  max_time
FROM pg_stat_statements
ORDER BY mean_time DESC
LIMIT 10;

-- Check index usage
SELECT
  schemaname,
  tablename,
  indexname,
  idx_scan,
  idx_tup_read,
  idx_tup_fetch
FROM pg_stat_user_indexes
WHERE schemaname = 'public'
ORDER BY idx_scan DESC;
```

## 🤝 Contributing

Contributions are welcome! Please follow these guidelines:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Development Workflow

1. Make changes to SQL scripts
2. Test in local PostgreSQL database
3. Update documentation if needed
4. Run verification queries
5. Submit PR with detailed description

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👥 Authors

- **Database Design Team** - Initial work and v2.0 redesign

## 🙏 Acknowledgments

- PostgreSQL community for excellent documentation
- StarUML for UML diagram tools
- All contributors and testers

## 📞 Support

For questions, issues, or feature requests:

- **Email**: support@blelearning.com
- **Issues**: [GitHub Issues](https://github.com/yourusername/blelearning-db/issues)
- **Documentation**: [Wiki](https://github.com/yourusername/blelearning-db/wiki)

## 🗺 Roadmap

### v2.1 (Planned)

- [ ] Add gamification tables (Badges, Points, Leaderboard)
- [ ] Add discussion forum (simplified version)
- [ ] Add live streaming support
- [ ] Add payment and subscription management
- [ ] Add recommendation engine tables

### v3.0 (Future)

- [ ] Multi-tenant support
- [ ] Advanced analytics with ML predictions
- [ ] Real-time collaboration features
- [ ] Mobile app specific optimizations

---

**Built with ❤️ for educators and learners worldwide**

**Version**: 2.0.0
**Last Updated**: 2025-11-25
**PostgreSQL**: 14+
