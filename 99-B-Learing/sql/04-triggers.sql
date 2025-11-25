-- ============================================
-- B-LEARNING DATABASE TRIGGERS
-- Version: 2.0
-- Purpose: Business logic automation
-- Created: 2025-11-25
-- ============================================

-- ============================================
-- 1. AUTO-UPDATE TIMESTAMPS
-- ============================================

CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
  NEW.updated_at = CURRENT_TIMESTAMP;
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Apply to all tables with updated_at column
CREATE TRIGGER trg_user_updated_at
  BEFORE UPDATE ON "User"
  FOR EACH ROW
  EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER trg_course_updated_at
  BEFORE UPDATE ON "Course"
  FOR EACH ROW
  EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER trg_module_updated_at
  BEFORE UPDATE ON "Module"
  FOR EACH ROW
  EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER trg_lecture_updated_at
  BEFORE UPDATE ON "Lecture"
  FOR EACH ROW
  EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER trg_quiz_updated_at
  BEFORE UPDATE ON "Quiz"
  FOR EACH ROW
  EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER trg_question_updated_at
  BEFORE UPDATE ON "Question"
  FOR EACH ROW
  EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER trg_assignment_updated_at
  BEFORE UPDATE ON "Assignment"
  FOR EACH ROW
  EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER trg_progress_updated_at
  BEFORE UPDATE ON "Progress"
  FOR EACH ROW
  EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER trg_class_updated_at
  BEFORE UPDATE ON "Class"
  FOR EACH ROW
  EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER trg_certificatetemplate_updated_at
  BEFORE UPDATE ON "CertificateTemplate"
  FOR EACH ROW
  EXECUTE FUNCTION update_updated_at_column();

-- ============================================
-- 2. AUTO-ISSUE CERTIFICATE
-- ============================================

-- Create sequence for certificate codes
CREATE SEQUENCE IF NOT EXISTS certificate_code_seq START 1;

CREATE OR REPLACE FUNCTION auto_issue_certificate()
RETURNS TRIGGER AS $$
DECLARE
  v_certificate_code VARCHAR(50);
  v_verification_code VARCHAR(100);
  v_template_id UUID;
  v_course_title VARCHAR(200);
BEGIN
  -- Only issue certificate when enrollment is completed
  IF NEW.enrollment_status = 'COMPLETED' AND
     (OLD IS NULL OR OLD.enrollment_status != 'COMPLETED') AND
     NEW.completion_percentage >= 100 THEN

    -- Check if certificate already exists
    IF NOT EXISTS (
      SELECT 1 FROM "Certificate"
      WHERE user_id = NEW.user_id AND course_id = NEW.course_id
    ) THEN

      -- Generate certificate code
      v_certificate_code := 'BL-' || TO_CHAR(CURRENT_DATE, 'YYYY') || '-' ||
                           LPAD(nextval('certificate_code_seq')::TEXT, 6, '0');

      -- Generate verification code (64 character hex)
      v_verification_code := encode(gen_random_bytes(32), 'hex');

      -- Get default template
      SELECT template_id INTO v_template_id
      FROM "CertificateTemplate"
      WHERE is_default = TRUE AND is_active = TRUE
      LIMIT 1;

      -- Get course title for certificate
      SELECT title INTO v_course_title
      FROM "Course"
      WHERE course_id = NEW.course_id;

      -- Insert certificate
      INSERT INTO "Certificate" (
        user_id, course_id, course_enrollment_id, template_id,
        certificate_code, verification_code, title,
        issue_date, completion_date, final_grade, valid_from
      ) VALUES (
        NEW.user_id,
        NEW.course_id,
        NEW.course_enrollment_id,
        v_template_id,
        v_certificate_code,
        v_verification_code,
        'Certificate of Completion - ' || v_course_title,
        CURRENT_DATE,
        COALESCE(NEW.completed_at::DATE, CURRENT_DATE),
        NEW.final_grade,
        CURRENT_DATE
      );

      -- Send notification
      INSERT INTO "Notification" (
        user_id, notification_type, title, message,
        related_entity_type, related_entity_id, priority
      )
      SELECT
        NEW.user_id,
        'CERTIFICATE_ISSUED',
        'Certificate Issued!',
        'Congratulations! Your certificate for "' || v_course_title || '" has been issued. You can download it now.',
        'Certificate',
        certificate_id,
        'HIGH'
      FROM "Certificate"
      WHERE certificate_code = v_certificate_code;

    END IF;
  END IF;

  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_auto_issue_certificate
  AFTER INSERT OR UPDATE ON "CourseEnrollment"
  FOR EACH ROW
  EXECUTE FUNCTION auto_issue_certificate();

-- ============================================
-- 3. AUTO-GRADE MCQ QUIZ
-- ============================================

CREATE OR REPLACE FUNCTION auto_grade_mcq()
RETURNS TRIGGER AS $$
DECLARE
  v_correct_option_ids UUID[];
  v_question_type VARCHAR(20);
  v_is_correct BOOLEAN;
BEGIN
  -- Get question type
  SELECT type INTO v_question_type
  FROM "Question"
  WHERE question_id = NEW.question_id;

  -- Only auto-grade MCQ and TRUE_FALSE questions
  IF v_question_type IN ('MCQ', 'TRUE_FALSE') THEN

    -- Get correct option IDs
    SELECT ARRAY_AGG(option_id ORDER BY option_id) INTO v_correct_option_ids
    FROM "Option"
    WHERE question_id = NEW.question_id AND is_correct = TRUE;

    -- Sort student's answer for comparison
    IF NEW.selected_option_ids IS NOT NULL THEN
      NEW.selected_option_ids := ARRAY(
        SELECT UNNEST(NEW.selected_option_ids) ORDER BY 1
      );
    END IF;

    -- Check if answer is correct
    v_is_correct := (
      v_correct_option_ids IS NOT NULL AND
      NEW.selected_option_ids IS NOT NULL AND
      v_correct_option_ids = NEW.selected_option_ids
    );

    -- Set auto_score
    IF v_is_correct THEN
      NEW.auto_score := NEW.max_points;
    ELSE
      NEW.auto_score := 0;
    END IF;

    -- Set final_score for auto-graded questions
    NEW.final_score := NEW.auto_score;
    NEW.graded_at := CURRENT_TIMESTAMP;
  END IF;

  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_auto_grade_mcq
  BEFORE INSERT OR UPDATE ON "QuizSubmission"
  FOR EACH ROW
  EXECUTE FUNCTION auto_grade_mcq();

-- ============================================
-- 4. UPDATE ATTEMPT STATUS AFTER ALL QUESTIONS GRADED
-- ============================================

CREATE OR REPLACE FUNCTION update_attempt_status()
RETURNS TRIGGER AS $$
DECLARE
  v_total_questions INT;
  v_graded_questions INT;
  v_total_score DECIMAL(6,2);
  v_max_score DECIMAL(6,2);
BEGIN
  -- Count total questions and graded questions for this attempt
  SELECT
    COUNT(*),
    COUNT(*) FILTER (WHERE final_score IS NOT NULL)
  INTO v_total_questions, v_graded_questions
  FROM "QuizSubmission"
  WHERE attempt_id = NEW.attempt_id;

  -- If all questions are graded, update attempt
  IF v_total_questions > 0 AND v_total_questions = v_graded_questions THEN

    -- Calculate total scores
    SELECT
      COALESCE(SUM(auto_score), 0),
      COALESCE(SUM(COALESCE(manual_score, 0)), 0),
      SUM(max_points)
    INTO
      v_total_score,
      v_max_score,
      v_max_score
    FROM "QuizSubmission"
    WHERE attempt_id = NEW.attempt_id;

    -- Update attempt
    UPDATE "Attempt"
    SET
      status = 'GRADED',
      auto_score = (
        SELECT COALESCE(SUM(auto_score), 0)
        FROM "QuizSubmission"
        WHERE attempt_id = NEW.attempt_id
      ),
      manual_score = (
        SELECT COALESCE(SUM(manual_score), 0)
        FROM "QuizSubmission"
        WHERE attempt_id = NEW.attempt_id
      ),
      final_score = (
        SELECT COALESCE(SUM(final_score), 0)
        FROM "QuizSubmission"
        WHERE attempt_id = NEW.attempt_id
      ),
      percentage_score = CASE
        WHEN max_possible_score > 0 THEN
          ROUND((
            SELECT COALESCE(SUM(final_score), 0)
            FROM "QuizSubmission"
            WHERE attempt_id = NEW.attempt_id
          ) / max_possible_score * 100, 2)
        ELSE 0
      END,
      graded_at = CURRENT_TIMESTAMP
    WHERE attempt_id = NEW.attempt_id
      AND status != 'GRADED';

  END IF;

  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_update_attempt_status
  AFTER INSERT OR UPDATE ON "QuizSubmission"
  FOR EACH ROW
  EXECUTE FUNCTION update_attempt_status();

-- ============================================
-- 5. UPDATE GRADEBOOK
-- ============================================

CREATE OR REPLACE FUNCTION update_gradebook()
RETURNS TRIGGER AS $$
DECLARE
  v_user_id UUID;
  v_course_id UUID;
  v_class_id UUID;
  v_total_quiz_score DECIMAL(6,2);
  v_total_assignment_score DECIMAL(6,2);
  v_total_participation_score DECIMAL(6,2);
BEGIN
  -- Get user_id and course_id from enrollment
  IF TG_TABLE_NAME = 'Attempt' THEN
    SELECT ce.user_id, ce.course_id, NEW.class_id
    INTO v_user_id, v_course_id, v_class_id
    FROM "CourseEnrollment" ce
    WHERE ce.course_enrollment_id = NEW.course_enrollment_id;
  ELSIF TG_TABLE_NAME = 'AssignmentSubmission' THEN
    SELECT ce.user_id, ce.course_id, NULL
    INTO v_user_id, v_course_id, v_class_id
    FROM "CourseEnrollment" ce
    WHERE ce.course_enrollment_id = NEW.course_enrollment_id;
  ELSE
    RETURN NEW;
  END IF;

  -- Calculate quiz scores
  SELECT COALESCE(AVG(percentage_score), 0)
  INTO v_total_quiz_score
  FROM "Attempt" a
  JOIN "CourseEnrollment" ce ON a.course_enrollment_id = ce.course_enrollment_id
  WHERE ce.user_id = v_user_id
    AND ce.course_id = v_course_id
    AND a.status = 'GRADED';

  -- Calculate assignment scores
  SELECT COALESCE(AVG(
    CASE
      WHEN asub.final_score IS NOT NULL AND a.max_points > 0
      THEN (asub.final_score / a.max_points * 100)
      ELSE 0
    END
  ), 0)
  INTO v_total_assignment_score
  FROM "AssignmentSubmission" asub
  JOIN "Assignment" a ON asub.assignment_id = a.assignment_id
  JOIN "CourseEnrollment" ce ON asub.course_enrollment_id = ce.course_enrollment_id
  WHERE ce.user_id = v_user_id
    AND ce.course_id = v_course_id
    AND asub.status = 'GRADED';

  -- Calculate participation score (attendance rate for blended learning)
  IF v_class_id IS NOT NULL THEN
    SELECT COALESCE(
      (COUNT(*) FILTER (WHERE att.status = 'PRESENT')::DECIMAL /
       NULLIF(COUNT(*), 0) * 100),
      0
    )
    INTO v_total_participation_score
    FROM "Schedule" s
    JOIN "Attendance" att ON s.schedule_id = att.schedule_id
    WHERE s.class_id = v_class_id
      AND att.user_id = v_user_id;
  ELSE
    v_total_participation_score := 0;
  END IF;

  -- Upsert gradebook
  INSERT INTO "GradeBook" (
    user_id, course_id, class_id,
    quiz_score, assignment_score, participation_score,
    total_score, weighted_score, last_updated_at
  ) VALUES (
    v_user_id,
    v_course_id,
    v_class_id,
    v_total_quiz_score,
    v_total_assignment_score,
    v_total_participation_score,
    (v_total_quiz_score * 0.4 + v_total_assignment_score * 0.5 + v_total_participation_score * 0.1),
    (v_total_quiz_score * 0.4 + v_total_assignment_score * 0.5 + v_total_participation_score * 0.1),
    CURRENT_TIMESTAMP
  )
  ON CONFLICT (user_id, course_id, COALESCE(class_id, '00000000-0000-0000-0000-000000000000'::UUID))
  DO UPDATE SET
    quiz_score = EXCLUDED.quiz_score,
    assignment_score = EXCLUDED.assignment_score,
    participation_score = EXCLUDED.participation_score,
    total_score = EXCLUDED.total_score,
    weighted_score = EXCLUDED.weighted_score,
    last_updated_at = CURRENT_TIMESTAMP;

  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger for quiz attempts
CREATE TRIGGER trg_update_gradebook_attempt
  AFTER INSERT OR UPDATE ON "Attempt"
  FOR EACH ROW
  WHEN (NEW.status = 'GRADED')
  EXECUTE FUNCTION update_gradebook();

-- Trigger for assignment submissions
CREATE TRIGGER trg_update_gradebook_assignment
  AFTER INSERT OR UPDATE ON "AssignmentSubmission"
  FOR EACH ROW
  WHEN (NEW.status = 'GRADED')
  EXECUTE FUNCTION update_gradebook();

-- ============================================
-- 6. UPDATE COURSE COMPLETION
-- ============================================

CREATE OR REPLACE FUNCTION update_course_completion()
RETURNS TRIGGER AS $$
DECLARE
  v_total_lectures INT;
  v_completed_lectures INT;
  v_completion_pct DECIMAL(5,2);
BEGIN
  -- Count total and completed lectures
  SELECT
    COUNT(DISTINCT l.lecture_id),
    COUNT(DISTINCT p.lecture_id) FILTER (WHERE p.status = 'COMPLETED')
  INTO v_total_lectures, v_completed_lectures
  FROM "Lecture" l
  JOIN "Module" m ON l.module_id = m.module_id
  LEFT JOIN "Progress" p ON l.lecture_id = p.lecture_id
    AND p.user_id = NEW.user_id
    AND p.course_id = NEW.course_id
  WHERE m.course_id = NEW.course_id;

  -- Calculate completion percentage
  IF v_total_lectures > 0 THEN
    v_completion_pct := ROUND((v_completed_lectures::DECIMAL / v_total_lectures * 100), 2);
  ELSE
    v_completion_pct := 0;
  END IF;

  -- Update enrollment
  UPDATE "CourseEnrollment"
  SET
    completion_percentage = v_completion_pct,
    enrollment_status = CASE
      WHEN v_completion_pct >= 100 THEN 'COMPLETED'
      ELSE enrollment_status
    END,
    completed_at = CASE
      WHEN v_completion_pct >= 100 AND completed_at IS NULL THEN CURRENT_TIMESTAMP
      ELSE completed_at
    END
  WHERE user_id = NEW.user_id
    AND course_id = NEW.course_id;

  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_update_course_completion
  AFTER INSERT OR UPDATE ON "Progress"
  FOR EACH ROW
  WHEN (NEW.status = 'COMPLETED')
  EXECUTE FUNCTION update_course_completion();

-- ============================================
-- 7. SEND ASSIGNMENT DUE NOTIFICATION
-- ============================================

CREATE OR REPLACE FUNCTION send_assignment_due_notification()
RETURNS void AS $$
BEGIN
  -- Send notifications for assignments due in 24 hours
  INSERT INTO "Notification" (
    user_id, notification_type, title, message,
    related_entity_type, related_entity_id, priority, action_url
  )
  SELECT DISTINCT
    ce.user_id,
    'ASSIGNMENT_DUE',
    'Assignment Due Soon',
    'Your assignment "' || a.title || '" is due in 24 hours.',
    'Assignment',
    a.assignment_id,
    'HIGH',
    '/assignments/' || a.assignment_id::TEXT
  FROM "Assignment" a
  JOIN "Course" c ON a.course_id = c.course_id
  JOIN "CourseEnrollment" ce ON c.course_id = ce.course_id
  WHERE ce.role_in_course = 'STUDENT'
    AND ce.enrollment_status = 'ACTIVE'
    AND a.due_date BETWEEN NOW() + INTERVAL '23 hours' AND NOW() + INTERVAL '25 hours'
    AND NOT EXISTS (
      SELECT 1 FROM "AssignmentSubmission" asub
      WHERE asub.assignment_id = a.assignment_id
        AND asub.user_id = ce.user_id
        AND asub.status IN ('SUBMITTED', 'GRADED')
    );
END;
$$ LANGUAGE plpgsql;

-- This function would be called by a scheduled job (cron or pg_cron)
-- Example: SELECT send_assignment_due_notification();

-- ============================================
-- TRIGGERS COMPLETE
-- Total triggers: 14 active triggers
-- Total functions: 7 functions
-- ============================================
