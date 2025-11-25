-- ============================================
-- B-LEARNING DATABASE CONSTRAINTS
-- Version: 2.0
-- Purpose: Additional constraints (most defined in schema)
-- Created: 2025-11-25
-- ============================================

-- NOTE: Most constraints are already defined inline in 01-schema.sql:
-- - PRIMARY KEY constraints
-- - FOREIGN KEY constraints with CASCADE behaviors
-- - UNIQUE constraints
-- - CHECK constraints for status values and ranges
-- - NOT NULL constraints
--
-- This file is reserved for:
-- 1. Additional constraints added after initial deployment
-- 2. Complex multi-column constraints
-- 3. Constraints that improve readability when separated

-- ============================================
-- 1. ADDITIONAL CHECK CONSTRAINTS (if needed)
-- ============================================

-- Example: Ensure end_date is after start_date for Class
-- (This could be added if not already validated in application logic)
/*
ALTER TABLE "Class"
  ADD CONSTRAINT chk_class_dates
  CHECK (end_date IS NULL OR start_date IS NULL OR end_date >= start_date);
*/

-- Example: Ensure quiz time_limit is reasonable
/*
ALTER TABLE "Quiz"
  ADD CONSTRAINT chk_quiz_time_limit
  CHECK (time_limit_minutes IS NULL OR (time_limit_minutes >= 0 AND time_limit_minutes <= 480));
*/

-- Example: Ensure assignment due_date is in future when created
-- (This is better handled in application logic as it's time-dependent)

-- ============================================
-- 2. ADDITIONAL UNIQUE CONSTRAINTS (if needed)
-- ============================================

-- All unique constraints are already defined in schema:
-- - User.email
-- - Role.name
-- - Course.code
-- - Certificate.certificate_code
-- - Certificate.verification_code
-- - CertificateTemplate.name
-- - SystemSettings.setting_key
-- - Multi-column unique constraints (UserRole, CourseEnrollment, etc.)

-- ============================================
-- 3. EXCLUSION CONSTRAINTS (if needed)
-- ============================================

-- Example: Prevent overlapping class schedules for same location
-- (Using PostgreSQL btree_gist extension)
/*
CREATE EXTENSION IF NOT EXISTS btree_gist;

ALTER TABLE "Schedule"
  ADD CONSTRAINT excl_schedule_overlap
  EXCLUDE USING GIST (
    class_id WITH =,
    tsrange(
      (session_date + start_time)::timestamp,
      (session_date + end_time)::timestamp
    ) WITH &&
  )
  WHERE (session_type = 'IN_PERSON' AND location IS NOT NULL);
*/

-- ============================================
-- 4. DEFERRED CONSTRAINTS (if needed)
-- ============================================

-- Deferred constraints are checked at transaction commit rather than immediately
-- Useful for complex data migrations or bulk operations

-- Example: Make some foreign keys deferrable
/*
ALTER TABLE "Progress"
  DROP CONSTRAINT progress_user_id_fkey,
  ADD CONSTRAINT progress_user_id_fkey
    FOREIGN KEY (user_id) REFERENCES "User"(user_id)
    ON DELETE CASCADE
    DEFERRABLE INITIALLY DEFERRED;
*/

-- ============================================
-- 5. VALIDATION QUERIES
-- ============================================

-- Verify all constraints are in place
-- Run these queries to check constraint coverage:

/*
-- Check all CHECK constraints
SELECT
  tc.table_name,
  tc.constraint_name,
  cc.check_clause
FROM information_schema.table_constraints tc
JOIN information_schema.check_constraints cc
  ON tc.constraint_name = cc.constraint_name
WHERE tc.table_schema = 'public'
  AND tc.constraint_type = 'CHECK'
ORDER BY tc.table_name, tc.constraint_name;

-- Check all UNIQUE constraints
SELECT
  tc.table_name,
  tc.constraint_name,
  STRING_AGG(kcu.column_name, ', ' ORDER BY kcu.ordinal_position) as columns
FROM information_schema.table_constraints tc
JOIN information_schema.key_column_usage kcu
  ON tc.constraint_name = kcu.constraint_name
WHERE tc.table_schema = 'public'
  AND tc.constraint_type = 'UNIQUE'
GROUP BY tc.table_name, tc.constraint_name
ORDER BY tc.table_name;

-- Check all FOREIGN KEY constraints
SELECT
  tc.table_name,
  tc.constraint_name,
  kcu.column_name,
  ccu.table_name AS foreign_table_name,
  ccu.column_name AS foreign_column_name,
  rc.delete_rule,
  rc.update_rule
FROM information_schema.table_constraints AS tc
JOIN information_schema.key_column_usage AS kcu
  ON tc.constraint_name = kcu.constraint_name
JOIN information_schema.constraint_column_usage AS ccu
  ON ccu.constraint_name = tc.constraint_name
JOIN information_schema.referential_constraints AS rc
  ON rc.constraint_name = tc.constraint_name
WHERE tc.constraint_type = 'FOREIGN KEY'
  AND tc.table_schema = 'public'
ORDER BY tc.table_name, tc.constraint_name;
*/

-- ============================================
-- CONSTRAINTS COMPLETE
-- Most constraints defined in 01-schema.sql
-- ============================================
