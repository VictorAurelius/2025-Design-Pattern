# 🚀 QUICK START GUIDE - B-Learning Database Redesign

Hướng dẫn nhanh để bắt đầu thực hiện redesign database B-Learning theo **req-1.md**.

---

## 📖 TÀI LIỆU QUAN TRỌNG

### Đọc TRƯỚC KHI BẮT ĐẦU:

1. **req-1.md** (56KB, 1827 dòng) - ⭐ DOCUMENT CHÍNH
   - Plan task chi tiết
   - 31 bảng với full specification
   - SQL examples
   - StarUML guidelines

2. **documents/DATABASE-DESIGN-EVALUATION.md** (85KB)
   - Đánh giá thiết kế cũ
   - 5 vấn đề nghiêm trọng
   - Giải pháp chi tiết

3. **documents/EXECUTIVE-SUMMARY.md** (10KB)
   - Tóm tắt nhanh
   - Top 5 issues
   - Timeline & ROI

4. **CHECKLIST.md**
   - Track tiến độ
   - 3 phases
   - Definition of Done

---

## ⚡ QUICK WORKFLOW

### 🎯 Phase 1: Design (Week 1-2)

```bash
# Step 1: Đọc tài liệu đánh giá
cd 99-B-Learing/documents
cat EXECUTIVE-SUMMARY.md
cat DATABASE-DESIGN-EVALUATION.md (skim sections 4-6)

# Step 2: Vẽ BFD trong StarUML
# - Open StarUML
# - Create Use Case Diagram
# - Add actors: Student, Instructor, TA, Admin, System
# - Add use cases from req-1.md Section "Bước 2.2"
# - Save as BFD.mdj

# Step 3: Vẽ ERD trong StarUML
# - Create Data Model Diagram (ERD)
# - Add 31 tables from req-1.md Section "Bước 3.1"
# - Add all attributes (PK, FK, data types)
# - Draw relationships
# - Save as ERD.mdj

# Step 4: Viết specifications
# Create these files in documents/:
touch documents/BFD-SPEC.md
touch documents/ERD-SPEC.md
touch documents/DATABASE-SCHEMA.md
touch documents/FUNCTIONAL-REQUIREMENTS.md
touch documents/API-ENDPOINTS.md

# Fill content theo template trong req-1.md "Bước 4"
```

---

### 💻 Phase 2: Implementation (Week 3-4)

```bash
# Step 5: Tạo SQL scripts
mkdir -p sql
cd sql

# Create SQL files
touch 01-schema.sql
touch 02-indexes.sql
touch 03-constraints.sql
touch 04-triggers.sql
touch 05-views.sql
touch 06-seed-data.sql
touch 99-drop-all.sql

# Viết SQL theo template trong req-1.md "Bước 5"
# Copy DDL cho 31 bảng từ req-1.md "Bước 3.1"
```

#### Template 01-schema.sql:

```sql
-- Copy this structure
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pg_trgm";
CREATE EXTENSION IF NOT EXISTS "btree_gin";

-- USER MANAGEMENT (3 tables)
CREATE TABLE "User" (
  user_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  email VARCHAR(255) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  first_name VARCHAR(100) NOT NULL,
  last_name VARCHAR(100) NOT NULL,
  -- ... (copy từ req-1.md)
);

CREATE TABLE "Role" (...);
CREATE TABLE "UserRole" (...);

-- COURSE CONTENT (4 tables)
CREATE TABLE "Course" (...);
CREATE TABLE "Module" (...);
CREATE TABLE "Lecture" (...);
CREATE TABLE "Resource" (...);

-- ASSESSMENT (9 tables)
CREATE TABLE "Quiz" (...);
CREATE TABLE "Question" (...);
CREATE TABLE "Option" (...);
CREATE TABLE "QuizQuestion" (...);
CREATE TABLE "Attempt" (...);
CREATE TABLE "QuizSubmission" (...);
CREATE TABLE "Assignment" (...);
CREATE TABLE "AssignmentSubmission" (...);
CREATE TABLE "GradeBook" (...);

-- Tiếp tục cho all 31 tables...
```

#### Test SQL Scripts:

```bash
# Create test database
createdb b_learning_test

# Run scripts
psql b_learning_test < 01-schema.sql
psql b_learning_test < 02-indexes.sql
psql b_learning_test < 03-constraints.sql
psql b_learning_test < 04-triggers.sql
psql b_learning_test < 05-views.sql
psql b_learning_test < 06-seed-data.sql

# Verify
psql b_learning_test -c "\dt"  # List tables (should show 31)
psql b_learning_test -c "\di"  # List indexes
psql b_learning_test -c "SELECT COUNT(*) FROM \"User\";"  # Check data

# Cleanup if needed
psql b_learning_test < 99-drop-all.sql
```

---

### ✅ Phase 3: Validation (Week 5)

```bash
# Check all files exist
ls -lh documents/
ls -lh sql/
ls -lh *.mdj

# Validation checklist
# Open CHECKLIST.md and mark items as done

# Common validation queries:
psql b_learning_test <<EOF
-- Test 1: Check all tables exist
SELECT COUNT(*) FROM information_schema.tables
WHERE table_schema = 'public';  -- Should be 31

-- Test 2: Check FK constraints
SELECT COUNT(*) FROM information_schema.table_constraints
WHERE constraint_type = 'FOREIGN KEY';  -- Should be ~40+

-- Test 3: Check indexes
SELECT COUNT(*) FROM pg_indexes
WHERE schemaname = 'public';  -- Should be 50+

-- Test 4: Sample enrollment
SELECT u.email, c.title, ce.enrollment_status
FROM "CourseEnrollment" ce
JOIN "User" u ON ce.user_id = u.user_id
JOIN "Course" c ON ce.course_id = c.course_id;

-- Test 5: Progress tracking
SELECT u.email, c.title, l.title as lecture, p.status, p.percent_complete
FROM "Progress" p
JOIN "User" u ON p.user_id = u.user_id
JOIN "Course" c ON p.course_id = c.course_id
JOIN "Lecture" l ON p.lecture_id = l.lecture_id;

-- Test 6: Certificate
SELECT u.email, cert.certificate_code, cert.status
FROM "Certificate" cert
JOIN "User" u ON cert.user_id = u.user_id;
EOF
```

---

## 📋 31 TABLES CHECKLIST

Khi viết SQL, đảm bảo tạo đủ 31 bảng theo thứ tự:

### User Management (3)
- [ ] User
- [ ] Role
- [ ] UserRole

### Course Content (4)
- [ ] Course
- [ ] Module
- [ ] Lecture
- [ ] Resource

### Assessment (9)
- [ ] Quiz
- [ ] Question
- [ ] Option
- [ ] QuizQuestion
- [ ] Attempt
- [ ] QuizSubmission
- [ ] Assignment
- [ ] AssignmentSubmission
- [ ] GradeBook

### Enrollment & Progress (4)
- [ ] CourseEnrollment
- [ ] ClassEnrollment
- [ ] Progress
- [ ] Attendance

### Class & Blended (3)
- [ ] Class
- [ ] Schedule
- [ ] Attendance (already in Enrollment group)

### Certificate (3)
- [ ] CertificateTemplate
- [ ] Certificate
- [ ] CertificateVerification

### Notification (3)
- [ ] Notification
- [ ] NotificationPreference
- [ ] NotificationLog

### Audit & System (3)
- [ ] ActivityLog
- [ ] File
- [ ] SystemSettings

**Total: 31 tables**

---

## 🎯 KEY POINTS TO REMEMBER

### ✅ MUST HAVE:
1. **UUID for all PKs** - Use `gen_random_uuid()`
2. **CASCADE behaviors** - ON DELETE CASCADE for child tables
3. **UNIQUE constraints** - email, codes, etc.
4. **CHECK constraints** - status values, percentages
5. **Indexes** - All FKs + performance indexes
6. **Timestamps** - created_at, updated_at
7. **Full DDL** - Complete CREATE TABLE statements

### ❌ MUST NOT:
1. ❌ Thread, Post, Discussion tables (REMOVED)
2. ❌ Vote, Like tables (REMOVED)
3. ❌ Any forum-related features

### 🆕 NEW FEATURES:
1. ✅ Assignment system (vs old Submission)
2. ✅ Certificate templates
3. ✅ Notification system
4. ✅ GradeBook table
5. ✅ Enhanced Progress tracking

---

## 🔧 TOOLS NEEDED

### Required:
- **StarUML** - For BFD.mdj and ERD.mdj
- **PostgreSQL 14+** - Database
- **Text Editor** - For .md and .sql files

### Optional:
- **DBeaver** - Visual database tool
- **pgAdmin** - PostgreSQL admin
- **Draw.io** - Alternative diagram tool

---

## 📊 ESTIMATES

| Task | Time | Priority |
|------|------|----------|
| Read req-1.md | 2h | 🔴 HIGH |
| Design BFD | 4h | 🔴 HIGH |
| Design ERD | 8h | 🔴 HIGH |
| Write 01-schema.sql | 6h | 🔴 HIGH |
| Write 02-indexes.sql | 2h | 🟡 MED |
| Write 03-constraints.sql | 2h | 🟡 MED |
| Write 04-triggers.sql | 4h | 🟡 MED |
| Write 05-views.sql | 2h | 🟢 LOW |
| Write 06-seed-data.sql | 3h | 🔴 HIGH |
| Write specifications | 8h | 🔴 HIGH |
| Testing & validation | 6h | 🔴 HIGH |
| Documentation | 3h | 🟡 MED |

**Total: ~50 hours** (1-2 weeks full-time)

---

## 🆘 TROUBLESHOOTING

### Error: "relation does not exist"
**Cause**: Creating tables in wrong order (FK references not exist yet)
**Fix**: Create parent tables before child tables

### Error: "duplicate key value violates unique constraint"
**Cause**: Inserting duplicate data in seed script
**Fix**: Add `ON CONFLICT DO NOTHING` or check existing data

### Error: "syntax error at or near"
**Cause**: SQL syntax error
**Fix**: Check PostgreSQL 14 syntax, use proper quoting for table names ("User")

### StarUML: Can't see attributes
**Cause**: View settings
**Fix**: View → Show → Attributes, Operations

### StarUML: Relationships not showing
**Cause**: Not properly created
**Fix**: Use proper relationship types (InterfaceRealization, Association, Dependency)

---

## 📞 SUPPORT

### Resources:
- **req-1.md** - Main reference document
- **DATABASE-DESIGN-EVALUATION.md** - Detailed analysis
- **PostgreSQL Docs**: https://www.postgresql.org/docs/14/
- **StarUML Docs**: https://docs.staruml.io/

### Questions:
- Check CHECKLIST.md for status
- Reference req-1.md sections
- Review example SQL in documents/

---

## ✨ FINAL DELIVERABLES

When done, you should have:

```
99-B-Learing/
├── README.md ✅
├── req-1.md ✅
├── CHECKLIST.md ✅
├── QUICK-START.md ✅
│
├── documents/
│   ├── BFD-SPEC.md ⬜
│   ├── ERD-SPEC.md ⬜
│   ├── DATABASE-SCHEMA.md ⬜
│   ├── FUNCTIONAL-REQUIREMENTS.md ⬜
│   └── API-ENDPOINTS.md ⬜
│
├── sql/
│   ├── 01-schema.sql ⬜
│   ├── 02-indexes.sql ⬜
│   ├── 03-constraints.sql ⬜
│   ├── 04-triggers.sql ⬜
│   ├── 05-views.sql ⬜
│   ├── 06-seed-data.sql ⬜
│   └── 99-drop-all.sql ⬜
│
├── BFD.mdj ⬜
└── ERD.mdj ⬜
```

---

**Good luck! 🚀**

*Last updated: 2025-11-25*
