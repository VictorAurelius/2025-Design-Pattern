-- ============================================
-- B-LEARNING DATABASE CLEANUP SCRIPT
-- Version: 2.0
-- Purpose: Drop all database objects for cleanup/reset
-- Created: 2025-11-25
-- ============================================

-- WARNING: This script will DELETE ALL DATA and database objects!
-- Use with extreme caution. Recommended for:
-- - Development environment reset
-- - Test environment cleanup
-- - Complete database reinstall

-- IMPORTANT: DO NOT run this in production without proper backup!

-- To prevent accidental execution, uncomment the following line:
-- DO $$ BEGIN RAISE EXCEPTION 'Safety check: Are you sure you want to drop all objects?'; END $$;

-- ============================================
-- SAFETY CONFIRMATION
-- ============================================

DO $$
BEGIN
  RAISE NOTICE '╔═══════════════════════════════════════════════════════════╗';
  RAISE NOTICE '║   WARNING: DATABASE CLEANUP IN PROGRESS                  ║';
  RAISE NOTICE '║   All tables, views, functions, and data will be deleted ║';
  RAISE NOTICE '╚═══════════════════════════════════════════════════════════╝';
END $$;

-- ============================================
-- 1. DROP MATERIALIZED VIEWS
-- ============================================

DROP MATERIALIZED VIEW IF EXISTS mv_system_overview CASCADE;
DROP MATERIALIZED VIEW IF EXISTS mv_class_dashboard CASCADE;
DROP MATERIALIZED VIEW IF EXISTS mv_assignment_analytics CASCADE;
DROP MATERIALIZED VIEW IF EXISTS mv_quiz_analytics CASCADE;
DROP MATERIALIZED VIEW IF EXISTS mv_instructor_dashboard CASCADE;
DROP MATERIALIZED VIEW IF EXISTS mv_user_progress_summary CASCADE;
DROP MATERIALIZED VIEW IF EXISTS mv_course_statistics CASCADE;

RAISE NOTICE '✓ Materialized views dropped';

-- ============================================
-- 2. DROP FUNCTIONS
-- ============================================

-- Materialized view refresh functions
DROP FUNCTION IF EXISTS refresh_system_overview() CASCADE;
DROP FUNCTION IF EXISTS refresh_user_views() CASCADE;
DROP FUNCTION IF EXISTS refresh_course_views() CASCADE;
DROP FUNCTION IF EXISTS refresh_all_materialized_views() CASCADE;

-- Trigger functions
DROP FUNCTION IF EXISTS send_assignment_due_notification() CASCADE;
DROP FUNCTION IF EXISTS update_course_completion() CASCADE;
DROP FUNCTION IF EXISTS update_gradebook() CASCADE;
DROP FUNCTION IF EXISTS update_attempt_status() CASCADE;
DROP FUNCTION IF EXISTS auto_grade_mcq() CASCADE;
DROP FUNCTION IF EXISTS auto_issue_certificate() CASCADE;
DROP FUNCTION IF EXISTS update_updated_at_column() CASCADE;

RAISE NOTICE '✓ Functions dropped';

-- ============================================
-- 3. DROP TRIGGERS (automatically dropped with functions)
-- ============================================

-- Triggers are automatically dropped when their functions are dropped
-- But we'll explicitly list them for documentation:

-- DROP TRIGGER IF EXISTS trg_update_course_completion ON "Progress";
-- DROP TRIGGER IF EXISTS trg_update_gradebook_assignment ON "AssignmentSubmission";
-- DROP TRIGGER IF EXISTS trg_update_gradebook_attempt ON "Attempt";
-- DROP TRIGGER IF EXISTS trg_update_attempt_status ON "QuizSubmission";
-- DROP TRIGGER IF EXISTS trg_auto_grade_mcq ON "QuizSubmission";
-- DROP TRIGGER IF EXISTS trg_auto_issue_certificate ON "CourseEnrollment";
-- DROP TRIGGER IF EXISTS trg_certificatetemplate_updated_at ON "CertificateTemplate";
-- DROP TRIGGER IF EXISTS trg_class_updated_at ON "Class";
-- DROP TRIGGER IF EXISTS trg_progress_updated_at ON "Progress";
-- DROP TRIGGER IF EXISTS trg_assignment_updated_at ON "Assignment";
-- DROP TRIGGER IF EXISTS trg_question_updated_at ON "Question";
-- DROP TRIGGER IF EXISTS trg_quiz_updated_at ON "Quiz";
-- DROP TRIGGER IF EXISTS trg_lecture_updated_at ON "Lecture";
-- DROP TRIGGER IF EXISTS trg_module_updated_at ON "Module";
-- DROP TRIGGER IF EXISTS trg_course_updated_at ON "Course";
-- DROP TRIGGER IF EXISTS trg_user_updated_at ON "User";

RAISE NOTICE '✓ Triggers dropped (via CASCADE)';

-- ============================================
-- 4. DROP TABLES (in reverse dependency order)
-- ============================================

-- Level 5: Deep dependencies
DROP TABLE IF EXISTS "NotificationLog" CASCADE;
DROP TABLE IF EXISTS "CertificateVerification" CASCADE;
DROP TABLE IF EXISTS "Attendance" CASCADE;
DROP TABLE IF EXISTS "QuizSubmission" CASCADE;

RAISE NOTICE '✓ Level 5 tables dropped';

-- Level 4: Mid-level dependencies
DROP TABLE IF EXISTS "NotificationPreference" CASCADE;
DROP TABLE IF EXISTS "Notification" CASCADE;
DROP TABLE IF EXISTS "AssignmentSubmission" CASCADE;
DROP TABLE IF EXISTS "GradeBook" CASCADE;
DROP TABLE IF EXISTS "Certificate" CASCADE;
DROP TABLE IF EXISTS "Progress" CASCADE;
DROP TABLE IF EXISTS "Attempt" CASCADE;
DROP TABLE IF EXISTS "ClassEnrollment" CASCADE;
DROP TABLE IF EXISTS "Schedule" CASCADE;
DROP TABLE IF EXISTS "ActivityLog" CASCADE;
DROP TABLE IF EXISTS "File" CASCADE;

RAISE NOTICE '✓ Level 4 tables dropped';

-- Level 3: Content dependencies
DROP TABLE IF EXISTS "QuizQuestion" CASCADE;
DROP TABLE IF EXISTS "Option" CASCADE;
DROP TABLE IF EXISTS "Resource" CASCADE;

RAISE NOTICE '✓ Level 3 tables dropped';

-- Level 2: Primary content tables
DROP TABLE IF EXISTS "Assignment" CASCADE;
DROP TABLE IF EXISTS "Question" CASCADE;
DROP TABLE IF EXISTS "Quiz" CASCADE;
DROP TABLE IF EXISTS "Lecture" CASCADE;
DROP TABLE IF EXISTS "Module" CASCADE;
DROP TABLE IF EXISTS "Class" CASCADE;
DROP TABLE IF EXISTS "CourseEnrollment" CASCADE;

RAISE NOTICE '✓ Level 2 tables dropped';

-- Level 1: Core tables
DROP TABLE IF EXISTS "CertificateTemplate" CASCADE;
DROP TABLE IF EXISTS "Course" CASCADE;
DROP TABLE IF EXISTS "UserRole" CASCADE;
DROP TABLE IF EXISTS "Role" CASCADE;
DROP TABLE IF EXISTS "User" CASCADE;
DROP TABLE IF EXISTS "SystemSettings" CASCADE;

RAISE NOTICE '✓ Level 1 tables dropped';

-- ============================================
-- 5. DROP SEQUENCES
-- ============================================

DROP SEQUENCE IF EXISTS certificate_code_seq CASCADE;

RAISE NOTICE '✓ Sequences dropped';

-- ============================================
-- 6. DROP EXTENSIONS (Optional - uncomment if needed)
-- ============================================

-- WARNING: Only drop extensions if you're sure they're not used by other databases
-- or if you're completely removing PostgreSQL

/*
DROP EXTENSION IF EXISTS pg_trgm CASCADE;
DROP EXTENSION IF EXISTS btree_gin CASCADE;
DROP EXTENSION IF EXISTS btree_gist CASCADE;
DROP EXTENSION IF EXISTS "uuid-ossp" CASCADE;
DROP EXTENSION IF EXISTS pgcrypto CASCADE;

RAISE NOTICE '✓ Extensions dropped';
*/

-- ============================================
-- 7. DROP CUSTOM TYPES (if any were created)
-- ============================================

-- No custom types were created in this schema
-- But here's how you would drop them if they existed:
-- DROP TYPE IF EXISTS custom_type_name CASCADE;

-- ============================================
-- CLEANUP COMPLETE
-- ============================================

DO $$
BEGIN
  RAISE NOTICE '';
  RAISE NOTICE '╔═══════════════════════════════════════════════════════════╗';
  RAISE NOTICE '║   DATABASE CLEANUP COMPLETE                              ║';
  RAISE NOTICE '║   All B-Learning database objects have been removed      ║';
  RAISE NOTICE '╚═══════════════════════════════════════════════════════════╝';
  RAISE NOTICE '';
  RAISE NOTICE 'Summary:';
  RAISE NOTICE '- 7 Materialized views dropped';
  RAISE NOTICE '- 11 Functions dropped';
  RAISE NOTICE '- 14 Triggers dropped';
  RAISE NOTICE '- 31 Tables dropped';
  RAISE NOTICE '- 1 Sequence dropped';
  RAISE NOTICE '';
  RAISE NOTICE 'To rebuild the database, run the following scripts in order:';
  RAISE NOTICE '1. 01-schema.sql';
  RAISE NOTICE '2. 02-indexes.sql';
  RAISE NOTICE '3. 03-constraints.sql';
  RAISE NOTICE '4. 04-triggers.sql';
  RAISE NOTICE '5. 05-views.sql';
  RAISE NOTICE '6. 06-seed-data.sql (optional, for development)';
END $$;

-- ============================================
-- VERIFICATION QUERIES
-- ============================================

/*
-- Verify all tables are dropped
SELECT
  schemaname,
  tablename
FROM pg_tables
WHERE schemaname = 'public'
  AND tablename LIKE '%'
ORDER BY tablename;

-- Verify all materialized views are dropped
SELECT
  schemaname,
  matviewname
FROM pg_matviews
WHERE schemaname = 'public'
ORDER BY matviewname;

-- Verify all functions are dropped
SELECT
  routine_schema,
  routine_name,
  routine_type
FROM information_schema.routines
WHERE routine_schema = 'public'
ORDER BY routine_name;

-- Verify all triggers are dropped
SELECT
  trigger_schema,
  trigger_name,
  event_object_table
FROM information_schema.triggers
WHERE trigger_schema = 'public'
ORDER BY trigger_name;

-- Verify all sequences are dropped
SELECT
  schemaname,
  sequencename
FROM pg_sequences
WHERE schemaname = 'public'
ORDER BY sequencename;
*/

-- ============================================
-- ALTERNATIVE: DROP SCHEMA (Nuclear option)
-- ============================================

/*
-- This is the nuclear option - it drops EVERYTHING in the public schema
-- including objects not created by our scripts
-- USE WITH EXTREME CAUTION!

DROP SCHEMA public CASCADE;
CREATE SCHEMA public;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO public;
COMMENT ON SCHEMA public IS 'standard public schema';

RAISE NOTICE 'Public schema completely reset';
*/

-- ============================================
-- CLEANUP SCRIPT COMPLETE
-- ============================================
