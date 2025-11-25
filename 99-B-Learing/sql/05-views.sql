-- ============================================
-- B-LEARNING DATABASE VIEWS
-- Version: 2.0
-- Purpose: Materialized views for reporting and analytics
-- Created: 2025-11-25
-- ============================================

-- ============================================
-- 1. COURSE STATISTICS
-- ============================================

CREATE MATERIALIZED VIEW mv_course_statistics AS
SELECT
  c.course_id,
  c.code,
  c.title,
  c.category,
  c.difficulty_level,
  c.status,

  -- Enrollment metrics
  COUNT(DISTINCT ce.course_enrollment_id) AS total_enrollments,
  COUNT(DISTINCT ce.course_enrollment_id) FILTER (
    WHERE ce.enrollment_status = 'ACTIVE'
  ) AS active_enrollments,
  COUNT(DISTINCT ce.course_enrollment_id) FILTER (
    WHERE ce.enrollment_status = 'COMPLETED'
  ) AS completed_enrollments,

  -- Completion rate
  ROUND(
    (COUNT(DISTINCT ce.course_enrollment_id) FILTER (
      WHERE ce.enrollment_status = 'COMPLETED'
    )::DECIMAL /
    NULLIF(COUNT(DISTINCT ce.course_enrollment_id), 0) * 100),
    2
  ) AS completion_rate,

  -- Content metrics
  COUNT(DISTINCT m.module_id) AS total_modules,
  COUNT(DISTINCT l.lecture_id) AS total_lectures,
  COUNT(DISTINCT q.quiz_id) AS total_quizzes,
  COUNT(DISTINCT a.assignment_id) AS total_assignments,

  -- Average grades
  ROUND(AVG(gb.total_score), 2) AS avg_course_score,
  ROUND(AVG(gb.quiz_score), 2) AS avg_quiz_score,
  ROUND(AVG(gb.assignment_score), 2) AS avg_assignment_score,

  -- Certificate metrics
  COUNT(DISTINCT cert.certificate_id) AS certificates_issued,

  -- Time metrics
  AVG(EXTRACT(EPOCH FROM (ce.completed_at - ce.enrolled_at)) / 86400) AS avg_completion_days,

  -- Last updated
  CURRENT_TIMESTAMP AS last_refreshed_at

FROM "Course" c
LEFT JOIN "CourseEnrollment" ce ON c.course_id = ce.course_id
LEFT JOIN "Module" m ON c.course_id = m.course_id
LEFT JOIN "Lecture" l ON m.module_id = l.module_id
LEFT JOIN "Quiz" q ON c.course_id = q.course_id
LEFT JOIN "Assignment" a ON c.course_id = a.course_id
LEFT JOIN "GradeBook" gb ON c.course_id = gb.course_id
LEFT JOIN "Certificate" cert ON c.course_id = cert.course_id

GROUP BY c.course_id, c.code, c.title, c.category, c.difficulty_level, c.status;

-- Indexes for mv_course_statistics
CREATE UNIQUE INDEX idx_mv_course_stats_course_id ON mv_course_statistics(course_id);
CREATE INDEX idx_mv_course_stats_category ON mv_course_statistics(category);
CREATE INDEX idx_mv_course_stats_status ON mv_course_statistics(status);
CREATE INDEX idx_mv_course_stats_completion_rate ON mv_course_statistics(completion_rate DESC);

COMMENT ON MATERIALIZED VIEW mv_course_statistics IS
'Course-level statistics for dashboard and reporting';

-- ============================================
-- 2. USER PROGRESS SUMMARY
-- ============================================

CREATE MATERIALIZED VIEW mv_user_progress_summary AS
SELECT
  u.user_id,
  u.email,
  u.first_name,
  u.last_name,
  u.account_status,

  -- Enrollment metrics
  COUNT(DISTINCT ce.course_enrollment_id) AS total_enrollments,
  COUNT(DISTINCT ce.course_enrollment_id) FILTER (
    WHERE ce.enrollment_status = 'ACTIVE'
  ) AS active_courses,
  COUNT(DISTINCT ce.course_enrollment_id) FILTER (
    WHERE ce.enrollment_status = 'COMPLETED'
  ) AS completed_courses,

  -- Progress metrics
  ROUND(AVG(ce.completion_percentage), 2) AS avg_completion_percentage,

  -- Assessment metrics
  COUNT(DISTINCT att.attempt_id) AS total_quiz_attempts,
  COUNT(DISTINCT att.attempt_id) FILTER (
    WHERE att.status = 'GRADED'
  ) AS graded_quiz_attempts,
  ROUND(AVG(att.percentage_score) FILTER (
    WHERE att.status = 'GRADED'
  ), 2) AS avg_quiz_score,

  COUNT(DISTINCT asub.submission_id) AS total_assignment_submissions,
  COUNT(DISTINCT asub.submission_id) FILTER (
    WHERE asub.status = 'GRADED'
  ) AS graded_assignments,

  -- Overall performance
  ROUND(AVG(gb.total_score), 2) AS avg_overall_score,

  -- Certificate metrics
  COUNT(DISTINCT cert.certificate_id) AS total_certificates,

  -- Activity metrics
  MAX(al.created_at) AS last_activity_at,
  COUNT(DISTINCT al.log_id) AS total_activities,

  -- Time metrics
  u.created_at AS member_since,
  EXTRACT(EPOCH FROM (CURRENT_TIMESTAMP - u.created_at)) / 86400 AS days_as_member,

  -- Last updated
  CURRENT_TIMESTAMP AS last_refreshed_at

FROM "User" u
LEFT JOIN "CourseEnrollment" ce ON u.user_id = ce.user_id
LEFT JOIN "Attempt" att ON ce.course_enrollment_id = att.course_enrollment_id
LEFT JOIN "AssignmentSubmission" asub ON ce.course_enrollment_id = asub.course_enrollment_id
LEFT JOIN "GradeBook" gb ON u.user_id = gb.user_id
LEFT JOIN "Certificate" cert ON u.user_id = cert.user_id
LEFT JOIN "ActivityLog" al ON u.user_id = al.user_id

WHERE u.account_status != 'DELETED'

GROUP BY u.user_id, u.email, u.first_name, u.last_name, u.account_status, u.created_at;

-- Indexes for mv_user_progress_summary
CREATE UNIQUE INDEX idx_mv_user_progress_user_id ON mv_user_progress_summary(user_id);
CREATE INDEX idx_mv_user_progress_email ON mv_user_progress_summary(email);
CREATE INDEX idx_mv_user_progress_status ON mv_user_progress_summary(account_status);
CREATE INDEX idx_mv_user_progress_score ON mv_user_progress_summary(avg_overall_score DESC);

COMMENT ON MATERIALIZED VIEW mv_user_progress_summary IS
'User learning progress summary across all courses';

-- ============================================
-- 3. INSTRUCTOR DASHBOARD
-- ============================================

CREATE MATERIALIZED VIEW mv_instructor_dashboard AS
SELECT
  u.user_id AS instructor_id,
  u.email AS instructor_email,
  u.first_name || ' ' || u.last_name AS instructor_name,

  -- Course metrics
  COUNT(DISTINCT c.course_id) AS total_courses_created,
  COUNT(DISTINCT c.course_id) FILTER (
    WHERE c.status = 'PUBLISHED'
  ) AS published_courses,

  COUNT(DISTINCT cl.class_id) AS total_classes,
  COUNT(DISTINCT cl.class_id) FILTER (
    WHERE cl.status = 'IN_PROGRESS'
  ) AS active_classes,

  -- Student metrics
  COUNT(DISTINCT ce.user_id) AS total_students,
  COUNT(DISTINCT ce.user_id) FILTER (
    WHERE ce.enrollment_status = 'ACTIVE'
  ) AS active_students,

  -- Assessment metrics
  COUNT(DISTINCT q.quiz_id) AS total_quizzes_created,
  COUNT(DISTINCT a.assignment_id) AS total_assignments_created,

  -- Grading workload
  COUNT(DISTINCT att.attempt_id) FILTER (
    WHERE att.status = 'SUBMITTED'
  ) AS pending_quiz_grading,

  COUNT(DISTINCT asub.submission_id) FILTER (
    WHERE asub.status = 'SUBMITTED'
  ) AS pending_assignment_grading,

  -- Student performance
  ROUND(AVG(gb.total_score), 2) AS avg_student_score,

  -- Certificate metrics
  COUNT(DISTINCT cert.certificate_id) AS certificates_issued_for_courses,

  -- Last updated
  CURRENT_TIMESTAMP AS last_refreshed_at

FROM "User" u
INNER JOIN "UserRole" ur ON u.user_id = ur.user_id
INNER JOIN "Role" r ON ur.role_id = r.role_id
LEFT JOIN "Course" c ON u.user_id = c.created_by
LEFT JOIN "Class" cl ON u.user_id = cl.instructor_id
LEFT JOIN "CourseEnrollment" ce ON c.course_id = ce.course_id
LEFT JOIN "Quiz" q ON c.course_id = q.course_id
LEFT JOIN "Assignment" a ON c.course_id = a.course_id
LEFT JOIN "Attempt" att ON q.quiz_id = att.quiz_id
LEFT JOIN "AssignmentSubmission" asub ON a.assignment_id = asub.assignment_id
LEFT JOIN "GradeBook" gb ON c.course_id = gb.course_id
LEFT JOIN "Certificate" cert ON c.course_id = cert.course_id

WHERE r.name IN ('INSTRUCTOR', 'TA')
  AND u.account_status = 'ACTIVE'

GROUP BY u.user_id, u.email, u.first_name, u.last_name;

-- Indexes for mv_instructor_dashboard
CREATE UNIQUE INDEX idx_mv_instructor_dash_id ON mv_instructor_dashboard(instructor_id);
CREATE INDEX idx_mv_instructor_dash_email ON mv_instructor_dashboard(instructor_email);

COMMENT ON MATERIALIZED VIEW mv_instructor_dashboard IS
'Instructor dashboard with course and student metrics';

-- ============================================
-- 4. QUIZ ANALYTICS
-- ============================================

CREATE MATERIALIZED VIEW mv_quiz_analytics AS
SELECT
  q.quiz_id,
  q.title AS quiz_title,
  c.course_id,
  c.title AS course_title,
  q.quiz_type,
  q.difficulty_level,
  q.is_published,

  -- Question metrics
  COUNT(DISTINCT qq.question_id) AS total_questions,
  SUM(qq.points) AS total_points,

  -- Attempt metrics
  COUNT(DISTINCT att.attempt_id) AS total_attempts,
  COUNT(DISTINCT att.user_id) AS unique_students,
  COUNT(DISTINCT att.attempt_id) FILTER (
    WHERE att.status = 'GRADED'
  ) AS graded_attempts,

  -- Score metrics
  ROUND(AVG(att.percentage_score) FILTER (
    WHERE att.status = 'GRADED'
  ), 2) AS avg_score,
  ROUND(MIN(att.percentage_score) FILTER (
    WHERE att.status = 'GRADED'
  ), 2) AS min_score,
  ROUND(MAX(att.percentage_score) FILTER (
    WHERE att.status = 'GRADED'
  ), 2) AS max_score,
  ROUND(STDDEV(att.percentage_score) FILTER (
    WHERE att.status = 'GRADED'
  ), 2) AS score_stddev,

  -- Pass rate (assuming 60% is passing)
  ROUND(
    (COUNT(DISTINCT att.attempt_id) FILTER (
      WHERE att.status = 'GRADED' AND att.percentage_score >= 60
    )::DECIMAL /
    NULLIF(COUNT(DISTINCT att.attempt_id) FILTER (
      WHERE att.status = 'GRADED'
    ), 0) * 100),
    2
  ) AS pass_rate,

  -- Time metrics
  ROUND(AVG(EXTRACT(EPOCH FROM (att.ended_at - att.started_at)) / 60), 2) AS avg_time_minutes,

  -- Last updated
  CURRENT_TIMESTAMP AS last_refreshed_at

FROM "Quiz" q
INNER JOIN "Course" c ON q.course_id = c.course_id
LEFT JOIN "QuizQuestion" qq ON q.quiz_id = qq.quiz_id
LEFT JOIN "Attempt" att ON q.quiz_id = att.quiz_id

GROUP BY q.quiz_id, q.title, c.course_id, c.title, q.quiz_type, q.difficulty_level, q.is_published;

-- Indexes for mv_quiz_analytics
CREATE UNIQUE INDEX idx_mv_quiz_analytics_quiz_id ON mv_quiz_analytics(quiz_id);
CREATE INDEX idx_mv_quiz_analytics_course_id ON mv_quiz_analytics(course_id);
CREATE INDEX idx_mv_quiz_analytics_avg_score ON mv_quiz_analytics(avg_score DESC);
CREATE INDEX idx_mv_quiz_analytics_pass_rate ON mv_quiz_analytics(pass_rate DESC);

COMMENT ON MATERIALIZED VIEW mv_quiz_analytics IS
'Quiz performance analytics and statistics';

-- ============================================
-- 5. ASSIGNMENT ANALYTICS
-- ============================================

CREATE MATERIALIZED VIEW mv_assignment_analytics AS
SELECT
  a.assignment_id,
  a.title AS assignment_title,
  c.course_id,
  c.title AS course_title,
  a.assignment_type,
  a.max_points,
  a.due_date,
  a.late_submission_allowed,

  -- Submission metrics
  COUNT(DISTINCT asub.submission_id) AS total_submissions,
  COUNT(DISTINCT asub.user_id) AS unique_students,
  COUNT(DISTINCT asub.submission_id) FILTER (
    WHERE asub.status = 'SUBMITTED'
  ) AS pending_grading,
  COUNT(DISTINCT asub.submission_id) FILTER (
    WHERE asub.status = 'GRADED'
  ) AS graded_submissions,

  -- Submission timing
  COUNT(DISTINCT asub.submission_id) FILTER (
    WHERE asub.submitted_at <= a.due_date
  ) AS on_time_submissions,
  COUNT(DISTINCT asub.submission_id) FILTER (
    WHERE asub.submitted_at > a.due_date
  ) AS late_submissions,

  -- On-time rate
  ROUND(
    (COUNT(DISTINCT asub.submission_id) FILTER (
      WHERE asub.submitted_at <= a.due_date
    )::DECIMAL /
    NULLIF(COUNT(DISTINCT asub.submission_id), 0) * 100),
    2
  ) AS on_time_rate,

  -- Score metrics (percentage of max_points)
  ROUND(AVG(
    (asub.final_score / a.max_points * 100)
  ) FILTER (
    WHERE asub.status = 'GRADED'
  ), 2) AS avg_score_percentage,

  ROUND(MIN(
    (asub.final_score / a.max_points * 100)
  ) FILTER (
    WHERE asub.status = 'GRADED'
  ), 2) AS min_score_percentage,

  ROUND(MAX(
    (asub.final_score / a.max_points * 100)
  ) FILTER (
    WHERE asub.status = 'GRADED'
  ), 2) AS max_score_percentage,

  -- Pass rate (assuming 60% is passing)
  ROUND(
    (COUNT(DISTINCT asub.submission_id) FILTER (
      WHERE asub.status = 'GRADED' AND (asub.final_score / a.max_points * 100) >= 60
    )::DECIMAL /
    NULLIF(COUNT(DISTINCT asub.submission_id) FILTER (
      WHERE asub.status = 'GRADED'
    ), 0) * 100),
    2
  ) AS pass_rate,

  -- Grading turnaround time (days)
  ROUND(AVG(
    EXTRACT(EPOCH FROM (asub.graded_at - asub.submitted_at)) / 86400
  ) FILTER (
    WHERE asub.status = 'GRADED'
  ), 2) AS avg_grading_days,

  -- Last updated
  CURRENT_TIMESTAMP AS last_refreshed_at

FROM "Assignment" a
INNER JOIN "Course" c ON a.course_id = c.course_id
LEFT JOIN "AssignmentSubmission" asub ON a.assignment_id = asub.assignment_id

GROUP BY
  a.assignment_id, a.title, c.course_id, c.title,
  a.assignment_type, a.max_points, a.due_date, a.late_submission_allowed;

-- Indexes for mv_assignment_analytics
CREATE UNIQUE INDEX idx_mv_assignment_analytics_id ON mv_assignment_analytics(assignment_id);
CREATE INDEX idx_mv_assignment_analytics_course_id ON mv_assignment_analytics(course_id);
CREATE INDEX idx_mv_assignment_analytics_avg_score ON mv_assignment_analytics(avg_score_percentage DESC);
CREATE INDEX idx_mv_assignment_analytics_due_date ON mv_assignment_analytics(due_date DESC);

COMMENT ON MATERIALIZED VIEW mv_assignment_analytics IS
'Assignment submission and performance analytics';

-- ============================================
-- 6. CLASS DASHBOARD
-- ============================================

CREATE MATERIALIZED VIEW mv_class_dashboard AS
SELECT
  cl.class_id,
  cl.class_name,
  cl.class_code,
  cl.class_type,
  cl.status AS class_status,
  c.course_id,
  c.title AS course_title,
  u.user_id AS instructor_id,
  u.first_name || ' ' || u.last_name AS instructor_name,

  -- Enrollment metrics
  cl.max_students,
  cl.enrolled_students AS current_enrollments,
  cl.max_students - cl.enrolled_students AS available_spots,
  ROUND(
    (cl.enrolled_students::DECIMAL / NULLIF(cl.max_students, 0) * 100),
    2
  ) AS enrollment_percentage,

  -- Schedule metrics
  COUNT(DISTINCT s.schedule_id) AS total_sessions,
  COUNT(DISTINCT s.schedule_id) FILTER (
    WHERE s.session_date < CURRENT_DATE
  ) AS completed_sessions,
  COUNT(DISTINCT s.schedule_id) FILTER (
    WHERE s.session_date >= CURRENT_DATE
  ) AS upcoming_sessions,

  -- Attendance metrics
  ROUND(AVG(
    (COUNT(att.attendance_id) FILTER (WHERE att.status = 'PRESENT')::DECIMAL /
     NULLIF(COUNT(att.attendance_id), 0) * 100)
  ), 2) AS avg_attendance_rate,

  -- Performance metrics
  ROUND(AVG(gb.total_score), 2) AS avg_class_score,

  -- Time metrics
  cl.start_date,
  cl.end_date,
  CASE
    WHEN cl.end_date < CURRENT_DATE THEN 100
    WHEN cl.start_date > CURRENT_DATE THEN 0
    ELSE ROUND(
      (EXTRACT(EPOCH FROM (CURRENT_DATE - cl.start_date)) /
       NULLIF(EXTRACT(EPOCH FROM (cl.end_date - cl.start_date)), 0) * 100),
      2
    )
  END AS class_progress_percentage,

  -- Last updated
  CURRENT_TIMESTAMP AS last_refreshed_at

FROM "Class" cl
INNER JOIN "Course" c ON cl.course_id = c.course_id
LEFT JOIN "User" u ON cl.instructor_id = u.user_id
LEFT JOIN "Schedule" s ON cl.class_id = s.class_id
LEFT JOIN "Attendance" att ON s.schedule_id = att.schedule_id
LEFT JOIN "GradeBook" gb ON cl.class_id = gb.class_id

GROUP BY
  cl.class_id, cl.class_name, cl.class_code, cl.class_type, cl.status,
  c.course_id, c.title, u.user_id, u.first_name, u.last_name,
  cl.max_students, cl.enrolled_students, cl.start_date, cl.end_date;

-- Indexes for mv_class_dashboard
CREATE UNIQUE INDEX idx_mv_class_dash_class_id ON mv_class_dashboard(class_id);
CREATE INDEX idx_mv_class_dash_course_id ON mv_class_dashboard(course_id);
CREATE INDEX idx_mv_class_dash_instructor_id ON mv_class_dashboard(instructor_id);
CREATE INDEX idx_mv_class_dash_status ON mv_class_dashboard(class_status);

COMMENT ON MATERIALIZED VIEW mv_class_dashboard IS
'Class dashboard with enrollment, attendance, and performance metrics';

-- ============================================
-- 7. SYSTEM OVERVIEW
-- ============================================

CREATE MATERIALIZED VIEW mv_system_overview AS
SELECT
  -- User metrics
  (SELECT COUNT(*) FROM "User" WHERE account_status = 'ACTIVE') AS total_active_users,
  (SELECT COUNT(*) FROM "User" WHERE account_status = 'PENDING_VERIFICATION') AS pending_verifications,
  (SELECT COUNT(DISTINCT ur.user_id) FROM "UserRole" ur
   JOIN "Role" r ON ur.role_id = r.role_id WHERE r.name = 'STUDENT') AS total_students,
  (SELECT COUNT(DISTINCT ur.user_id) FROM "UserRole" ur
   JOIN "Role" r ON ur.role_id = r.role_id WHERE r.name = 'INSTRUCTOR') AS total_instructors,

  -- Course metrics
  (SELECT COUNT(*) FROM "Course" WHERE status = 'PUBLISHED') AS published_courses,
  (SELECT COUNT(*) FROM "Course" WHERE status = 'DRAFT') AS draft_courses,
  (SELECT COUNT(*) FROM "CourseEnrollment" WHERE enrollment_status = 'ACTIVE') AS active_enrollments,
  (SELECT COUNT(*) FROM "CourseEnrollment" WHERE enrollment_status = 'COMPLETED') AS completed_enrollments,

  -- Assessment metrics
  (SELECT COUNT(*) FROM "Quiz" WHERE is_published = TRUE) AS published_quizzes,
  (SELECT COUNT(*) FROM "Assignment") AS total_assignments,
  (SELECT COUNT(*) FROM "Attempt" WHERE status = 'SUBMITTED') AS pending_quiz_grading,
  (SELECT COUNT(*) FROM "AssignmentSubmission" WHERE status = 'SUBMITTED') AS pending_assignment_grading,

  -- Certificate metrics
  (SELECT COUNT(*) FROM "Certificate" WHERE status = 'ACTIVE') AS active_certificates,
  (SELECT COUNT(*) FROM "CertificateVerification") AS total_verifications,

  -- Class metrics
  (SELECT COUNT(*) FROM "Class" WHERE status = 'IN_PROGRESS') AS active_classes,
  (SELECT COUNT(*) FROM "ClassEnrollment") AS total_class_enrollments,

  -- Content metrics
  (SELECT COUNT(*) FROM "Module") AS total_modules,
  (SELECT COUNT(*) FROM "Lecture") AS total_lectures,
  (SELECT COUNT(*) FROM "Resource") AS total_resources,
  (SELECT COUNT(*) FROM "Question") AS total_questions,

  -- Activity metrics (last 24 hours)
  (SELECT COUNT(*) FROM "ActivityLog" WHERE created_at >= CURRENT_TIMESTAMP - INTERVAL '24 hours') AS activities_last_24h,
  (SELECT COUNT(*) FROM "Notification" WHERE created_at >= CURRENT_TIMESTAMP - INTERVAL '24 hours') AS notifications_last_24h,

  -- Storage metrics
  (SELECT COUNT(*) FROM "File" WHERE is_deleted = FALSE) AS total_files,
  (SELECT COALESCE(SUM(file_size), 0) FROM "File" WHERE is_deleted = FALSE) AS total_storage_bytes,

  -- Last updated
  CURRENT_TIMESTAMP AS last_refreshed_at;

COMMENT ON MATERIALIZED VIEW mv_system_overview IS
'System-wide overview metrics for admin dashboard';

-- ============================================
-- 8. REFRESH FUNCTIONS
-- ============================================

-- Refresh all materialized views
CREATE OR REPLACE FUNCTION refresh_all_materialized_views()
RETURNS void AS $$
BEGIN
  REFRESH MATERIALIZED VIEW CONCURRENTLY mv_course_statistics;
  REFRESH MATERIALIZED VIEW CONCURRENTLY mv_user_progress_summary;
  REFRESH MATERIALIZED VIEW CONCURRENTLY mv_instructor_dashboard;
  REFRESH MATERIALIZED VIEW CONCURRENTLY mv_quiz_analytics;
  REFRESH MATERIALIZED VIEW CONCURRENTLY mv_assignment_analytics;
  REFRESH MATERIALIZED VIEW CONCURRENTLY mv_class_dashboard;
  REFRESH MATERIALIZED VIEW mv_system_overview;

  RAISE NOTICE 'All materialized views refreshed at %', CURRENT_TIMESTAMP;
END;
$$ LANGUAGE plpgsql;

COMMENT ON FUNCTION refresh_all_materialized_views() IS
'Refresh all materialized views concurrently (requires unique indexes)';

-- Refresh course-related views
CREATE OR REPLACE FUNCTION refresh_course_views()
RETURNS void AS $$
BEGIN
  REFRESH MATERIALIZED VIEW CONCURRENTLY mv_course_statistics;
  REFRESH MATERIALIZED VIEW CONCURRENTLY mv_quiz_analytics;
  REFRESH MATERIALIZED VIEW CONCURRENTLY mv_assignment_analytics;

  RAISE NOTICE 'Course-related views refreshed at %', CURRENT_TIMESTAMP;
END;
$$ LANGUAGE plpgsql;

COMMENT ON FUNCTION refresh_course_views() IS
'Refresh course-related materialized views';

-- Refresh user-related views
CREATE OR REPLACE FUNCTION refresh_user_views()
RETURNS void AS $$
BEGIN
  REFRESH MATERIALIZED VIEW CONCURRENTLY mv_user_progress_summary;
  REFRESH MATERIALIZED VIEW CONCURRENTLY mv_instructor_dashboard;

  RAISE NOTICE 'User-related views refreshed at %', CURRENT_TIMESTAMP;
END;
$$ LANGUAGE plpgsql;

COMMENT ON FUNCTION refresh_user_views() IS
'Refresh user-related materialized views';

-- Refresh system overview
CREATE OR REPLACE FUNCTION refresh_system_overview()
RETURNS void AS $$
BEGIN
  REFRESH MATERIALIZED VIEW mv_system_overview;

  RAISE NOTICE 'System overview refreshed at %', CURRENT_TIMESTAMP;
END;
$$ LANGUAGE plpgsql;

COMMENT ON FUNCTION refresh_system_overview() IS
'Refresh system overview materialized view';

-- ============================================
-- 9. SCHEDULED REFRESH (Example with pg_cron)
-- ============================================

-- NOTE: Requires pg_cron extension
-- To enable: CREATE EXTENSION pg_cron;

-- Schedule refresh every hour
/*
SELECT cron.schedule(
  'refresh-all-views-hourly',
  '0 * * * *',
  'SELECT refresh_all_materialized_views();'
);

-- Schedule system overview refresh every 15 minutes
SELECT cron.schedule(
  'refresh-system-overview-15min',
  '*/15 * * * *',
  'SELECT refresh_system_overview();'
);
*/

-- ============================================
-- 10. VIEW USAGE EXAMPLES
-- ============================================

/*
-- Query course statistics
SELECT * FROM mv_course_statistics
WHERE status = 'PUBLISHED'
ORDER BY completion_rate DESC
LIMIT 10;

-- Query user progress
SELECT * FROM mv_user_progress_summary
WHERE active_courses > 0
ORDER BY avg_overall_score DESC;

-- Query instructor dashboard
SELECT * FROM mv_instructor_dashboard
WHERE instructor_id = '...'::UUID;

-- Query quiz analytics
SELECT * FROM mv_quiz_analytics
WHERE course_id = '...'::UUID
ORDER BY avg_score DESC;

-- Query assignment analytics
SELECT * FROM mv_assignment_analytics
WHERE due_date >= CURRENT_DATE
ORDER BY on_time_rate DESC;

-- Query class dashboard
SELECT * FROM mv_class_dashboard
WHERE class_status = 'IN_PROGRESS'
ORDER BY avg_attendance_rate DESC;

-- Query system overview
SELECT * FROM mv_system_overview;

-- Manual refresh examples
SELECT refresh_all_materialized_views();
SELECT refresh_course_views();
SELECT refresh_user_views();
SELECT refresh_system_overview();
*/

-- ============================================
-- VIEWS COMPLETE
-- Total materialized views: 7
-- Total refresh functions: 4
-- ============================================
