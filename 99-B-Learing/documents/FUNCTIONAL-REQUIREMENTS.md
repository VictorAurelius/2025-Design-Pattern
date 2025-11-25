# Functional Requirements Specification

**Project**: B-Learning System v2.0
**Author**: Nguyễn Văn Kiệt - CNTT1-K63
**Created**: 2025-11-25
**Status**: Draft

---

## 1. INTRODUCTION

### 1.1. Purpose
This document specifies the functional requirements for the B-Learning (Blended Learning) system v2.0, redesigned to focus on assignment-based learning with automated assessment and certificate issuance.

### 1.2. Scope
The system supports:
- **Online learning**: Self-paced video courses with quizzes and assignments
- **Blended learning**: Combination of online and in-person classes
- **Assessment**: Automated and manual grading
- **Certification**: Automated certificate issuance with verification
- **Notifications**: Multi-channel (email, push, SMS)

### 1.3. Requirements Notation

**Format**: `FR-[DOMAIN]-[NUMBER]: [Requirement Name]`

**Domains**:
- **UM**: User Management
- **CM**: Course Management
- **AS**: Assessment
- **EP**: Enrollment & Progress
- **CL**: Class & Blended Learning
- **CT**: Certificate
- **NT**: Notification
- **AD**: Audit & System

**Priority Levels**:
- 🔴 **CRITICAL** (P1): Must have for MVP
- 🟡 **HIGH** (P2): Should have for launch
- 🟢 **MEDIUM** (P3): Nice to have
- ⚪ **LOW** (P4): Future enhancement

---

## 2. USER MANAGEMENT (UM)

### FR-UM-001: User Registration
**Actor**: Guest
**Priority**: 🔴 CRITICAL (P1)

**Description**: Guest users can create a new account by providing email, password, and profile information.

**Input**:
- Email (unique, valid format)
- Password (min 8 characters, complexity requirements)
- First name
- Last name
- Phone (optional)
- Timezone (optional)

**Process**:
1. Validate email format and uniqueness
2. Validate password strength
3. Hash password using bcrypt (cost 12)
4. Create User record with status 'PENDING_VERIFICATION'
5. Generate email verification token
6. Send verification email
7. Return success message

**Output**:
- User account created
- Verification email sent
- User redirected to "Check your email" page

**Business Rules**:
- Email must be unique
- Password must be >= 8 characters with uppercase, lowercase, number, symbol
- Default timezone is UTC
- Default locale is 'vi'
- Account cannot log in until email verified

**Status**: ✅ Implemented

---

### FR-UM-002: Email Verification
**Actor**: User
**Priority**: 🔴 CRITICAL (P1)

**Description**: User verifies email address by clicking link in verification email.

**Input**:
- Verification token (from email link)

**Process**:
1. Validate token (not expired, not used)
2. Find user by token
3. Update User.email_verified_at = CURRENT_TIMESTAMP
4. Update User.account_status = 'ACTIVE'
5. Invalidate token
6. Send welcome email
7. Auto-assign 'STUDENT' role

**Output**:
- Email verified
- Account status = 'ACTIVE'
- User can now log in

**Business Rules**:
- Token expires after 24 hours
- Token can only be used once
- Users with unverified email cannot log in

**Status**: ✅ Implemented

---

### FR-UM-003: User Login
**Actor**: User
**Priority**: 🔴 CRITICAL (P1)

**Description**: User logs in with email and password to access the system.

**Input**:
- Email
- Password

**Process**:
1. Validate email exists
2. Validate account_status = 'ACTIVE'
3. Validate email_verified_at is not null
4. Compare password hash
5. Generate JWT token (expires 24h)
6. Update last_login_at
7. Create session
8. Log activity (ActivityLog)

**Output**:
- JWT token
- User profile
- User roles

**Business Rules**:
- Only ACTIVE accounts can log in
- Email must be verified
- Failed login attempts tracked (max 5 in 15 minutes → temporary lock)
- Session expires after 24 hours

**Status**: ✅ Implemented

---

### FR-UM-004: Password Reset
**Actor**: User
**Priority**: 🔴 CRITICAL (P1)

**Description**: User can reset password if forgotten.

**Input**:
- Email (for request)
- Reset token + new password (for reset)

**Process**:
1. **Request Reset**:
   - Validate email exists
   - Generate reset token
   - Send reset email with link
2. **Complete Reset**:
   - Validate token (not expired, not used)
   - Validate new password strength
   - Hash new password
   - Update User.password_hash
   - Invalidate token
   - Send confirmation email

**Output**:
- Password updated
- User can log in with new password

**Business Rules**:
- Reset token expires after 1 hour
- Token can only be used once
- New password cannot be same as old password
- All active sessions invalidated after reset

**Status**: ✅ Implemented

---

### FR-UM-005: Update Profile
**Actor**: User
**Priority**: 🟡 HIGH (P2)

**Description**: User can update their profile information.

**Input**:
- First name
- Last name
- Phone
- Avatar image (upload)
- Timezone
- Locale

**Process**:
1. Validate user is authenticated
2. Validate input (names not empty, phone format, etc.)
3. If avatar uploaded:
   - Validate file size (<5MB)
   - Validate file type (image/jpeg, image/png)
   - Upload to storage
   - Generate URL
   - Delete old avatar
4. Update User record
5. Return updated profile

**Output**:
- Profile updated
- New avatar URL (if uploaded)

**Business Rules**:
- Email cannot be changed via this function (use separate "Change Email" flow)
- Avatar max size: 5MB
- Supported formats: JPEG, PNG
- Old avatar deleted from storage

**Status**: ✅ Implemented

---

### FR-UM-006: Manage Roles (Admin)
**Actor**: Admin
**Priority**: 🔴 CRITICAL (P1)

**Description**: Admin can assign or revoke roles for users.

**Input**:
- User ID
- Role ID
- Action (GRANT or REVOKE)

**Process**:
1. Validate requester has ADMIN role
2. Validate target user exists
3. Validate role exists
4. **If GRANT**:
   - Check if user already has role
   - Insert UserRole record
   - Log activity
5. **If REVOKE**:
   - Check if user has role
   - Delete UserRole record
   - Log activity
6. Return updated user roles

**Output**:
- User roles updated
- Activity logged

**Business Rules**:
- Only ADMIN can manage roles
- Cannot revoke own ADMIN role (must be another admin)
- System roles (STUDENT, INSTRUCTOR, TA, ADMIN) cannot be deleted
- User must have at least one role

**Status**: ✅ Implemented

---

## 3. COURSE MANAGEMENT (CM)

### FR-CM-001: Create Course
**Actor**: Instructor, Admin
**Priority**: 🔴 CRITICAL (P1)

**Description**: Instructor or Admin can create a new course.

**Input**:
- Code (unique, e.g., CS101)
- Title
- Description
- Short description
- Thumbnail image
- Category
- Difficulty level
- Estimated hours

**Process**:
1. Validate requester has INSTRUCTOR or ADMIN role
2. Validate code is unique
3. Validate required fields
4. Upload thumbnail if provided
5. Create Course record with status = 'DRAFT'
6. Set created_by = current user
7. Auto-enroll creator as INSTRUCTOR
8. Log activity

**Output**:
- Course created (status = DRAFT)
- Creator enrolled as INSTRUCTOR
- Course ID returned

**Business Rules**:
- Course code must be unique
- Only INSTRUCTOR or ADMIN can create courses
- New courses start in DRAFT status
- Creator auto-enrolled as INSTRUCTOR

**Status**: ✅ Implemented

---

### FR-CM-002: Publish Course
**Actor**: Instructor, Admin
**Priority**: 🔴 CRITICAL (P1)

**Description**: Instructor publishes a draft course to make it available for enrollment.

**Input**:
- Course ID

**Process**:
1. Validate requester is course instructor or admin
2. Validate course status = 'DRAFT'
3. Validate course completeness:
   - Has at least 1 module
   - Each module has at least 1 lecture
   - Has course description
4. Update Course.status = 'PUBLISHED'
5. Set Course.published_at = CURRENT_TIMESTAMP
6. Send notification to followers (if any)
7. Log activity

**Output**:
- Course published
- Course visible in catalog
- Notifications sent

**Business Rules**:
- Only course instructor or admin can publish
- Course must have content (modules + lectures)
- Published courses appear in public catalog
- Cannot unpublish if students enrolled (must archive)

**Status**: ✅ Implemented

---

### FR-CM-003: Create Module
**Actor**: Instructor, Admin
**Priority**: 🔴 CRITICAL (P1)

**Description**: Instructor adds a module (chapter) to a course.

**Input**:
- Course ID
- Title
- Description
- Order number
- Prerequisite module IDs (optional)
- Estimated duration (minutes)

**Process**:
1. Validate requester is course instructor or admin
2. Validate course exists
3. Validate order_num unique within course
4. Create Module record
5. Log activity

**Output**:
- Module created
- Module ID returned

**Business Rules**:
- Modules ordered sequentially (1, 2, 3...)
- Module order unique within course
- Can set prerequisite modules (students must complete prerequisites first)

**Status**: ✅ Implemented

---

### FR-CM-004: Upload Lecture
**Actor**: Instructor, Admin
**Priority**: 🔴 CRITICAL (P1)

**Description**: Instructor uploads lecture content (video, PDF, slides, etc.) to a module.

**Input**:
- Module ID
- Title
- Description
- Type (VIDEO, PDF, SLIDE, AUDIO, TEXT)
- Content file (upload) or URL
- Duration (for VIDEO/AUDIO)
- Order number
- is_preview (boolean)
- is_downloadable (boolean)
- Transcript (optional)

**Process**:
1. Validate requester is course instructor or admin
2. Validate module exists
3. Validate file if uploaded:
   - Check file size (<500MB for video, <50MB for PDF)
   - Check file type matches declared type
4. Upload file to storage OR save URL
5. Generate streaming URL if video
6. Create Lecture record
7. Log activity

**Output**:
- Lecture created
- Content accessible via URL
- Lecture ID returned

**Business Rules**:
- Video max size: 500MB
- PDF/Slide max size: 50MB
- Lectures ordered within module
- Preview lectures visible without enrollment
- Downloadable flag controls download button

**Status**: ✅ Implemented

---

### FR-CM-005: Create Quiz
**Actor**: Instructor, Admin
**Priority**: 🔴 CRITICAL (P1)

**Description**: Instructor creates a quiz for a course.

**Input**:
- Course ID
- Title
- Description
- Time limit (minutes, 0 = unlimited)
- Attempt limit (0 = unlimited)
- Pass score (percentage)
- Shuffle questions (boolean)
- Show correct answers (boolean)

**Process**:
1. Validate requester is course instructor or admin
2. Validate course exists
3. Create Quiz record (is_published = FALSE)
4. Log activity

**Output**:
- Quiz created (unpublished)
- Quiz ID returned

**Business Rules**:
- Quiz starts unpublished
- Must add questions before publishing
- Time limit of 0 means unlimited
- Attempt limit of 0 means unlimited attempts

**Status**: ✅ Implemented

---

### FR-CM-006: Create Question
**Actor**: Instructor, Admin
**Priority**: 🔴 CRITICAL (P1)

**Description**: Instructor creates a question for the question bank.

**Input**:
- Course ID
- Question text
- Type (MCQ, TRUE_FALSE, ESSAY, SHORT_ANSWER, CODE)
- Difficulty (EASY, MEDIUM, HARD)
- Max points
- Options (for MCQ, TRUE_FALSE)
- Correct answer (for MCQ, TRUE_FALSE)
- Explanation

**Process**:
1. Validate requester is course instructor or admin
2. Validate course exists
3. Create Question record
4. If MCQ or TRUE_FALSE:
   - Create Option records
   - Mark correct options
5. Log activity

**Output**:
- Question created
- Options created (if applicable)
- Question ID returned

**Business Rules**:
- MCQ must have 2-6 options
- MCQ can have multiple correct answers
- TRUE_FALSE has exactly 2 options (True, False)
- ESSAY and SHORT_ANSWER have no options
- Questions can be reused across multiple quizzes

**Status**: ✅ Implemented

---

### FR-CM-007: Add Question to Quiz
**Actor**: Instructor, Admin
**Priority**: 🔴 CRITICAL (P1)

**Description**: Instructor adds a question from question bank to a quiz.

**Input**:
- Quiz ID
- Question ID
- Points (for this quiz)
- Order number

**Process**:
1. Validate requester is course instructor or admin
2. Validate quiz and question exist
3. Validate question not already in quiz
4. Create QuizQuestion record
5. Log activity

**Output**:
- Question added to quiz
- Order and points set

**Business Rules**:
- Question can appear only once per quiz
- Points can differ from question's default max_points
- Order determines question sequence in quiz

**Status**: ✅ Implemented

---

### FR-CM-008: Create Assignment
**Actor**: Instructor, Admin
**Priority**: 🔴 CRITICAL (P1)

**Description**: Instructor creates an assignment for students.

**Input**:
- Course ID (or Class ID for blended)
- Title
- Description
- Instructions
- Type (ESSAY, CODE, FILE_UPLOAD, PROBLEM_SET, PROJECT)
- Max points
- Due date
- Late submission allowed (boolean)
- Late penalty percentage
- Max late days
- Allow resubmission (boolean)
- Max submissions
- Rubric (JSON)
- Auto-grading enabled (boolean, for CODE)
- Test cases (JSON, for CODE)

**Process**:
1. Validate requester is course instructor or admin
2. Validate course or class exists
3. Validate due date is in future
4. Create Assignment record
5. Send notification to enrolled students
6. Log activity

**Output**:
- Assignment created
- Students notified
- Assignment ID returned

**Business Rules**:
- Due date must be in future
- Late penalty 0-100%
- Rubric is JSON with criteria and points
- CODE assignments can have auto-grading with test cases
- Assignments can be course-level or class-level

**Status**: ✅ Implemented

---

## 4. ASSESSMENT (AS)

### FR-AS-001: Take Quiz
**Actor**: Student
**Priority**: 🔴 CRITICAL (P1)

**Description**: Student takes a quiz (creates an attempt).

**Input**:
- Quiz ID

**Process**:
1. Validate student is enrolled in course
2. Validate quiz is published
3. Check attempt limit not exceeded
4. Create Attempt record (status = IN_PROGRESS)
5. Fetch quiz questions (shuffled if configured)
6. Start timer if time limit set
7. Return questions (without correct answers)

**Output**:
- Attempt created
- Questions displayed
- Timer started (if applicable)

**Business Rules**:
- Student must be enrolled in course
- Quiz must be published
- Attempt limit enforced
- Questions shuffled if quiz.shuffle_questions = TRUE
- Timer enforced on client and server

**Status**: ✅ Implemented

---

### FR-AS-002: Submit Quiz
**Actor**: Student
**Priority**: 🔴 CRITICAL (P1)

**Description**: Student submits quiz answers.

**Input**:
- Attempt ID
- Answers: array of {question_id, answer_text, selected_option_ids}

**Process**:
1. Validate attempt exists and status = IN_PROGRESS
2. Validate all questions answered (or allow partial)
3. Create QuizSubmission records for each answer
4. Update Attempt.submitted_at = CURRENT_TIMESTAMP
5. Update Attempt.status = 'SUBMITTED'
6. Calculate time_spent
7. Auto-grade MCQ and TRUE_FALSE questions (trigger)
8. If all auto-gradable: set status = 'GRADED'
9. Send notification to instructor

**Output**:
- Quiz submitted
- MCQ/TRUE_FALSE auto-graded
- Status = SUBMITTED or GRADED
- Score displayed (if all auto-graded)

**Business Rules**:
- Cannot submit after attempt abandoned or submitted
- Time limit enforced (auto-submit if expired)
- MCQ and TRUE_FALSE auto-graded immediately
- ESSAY and SHORT_ANSWER require manual grading
- Student cannot see correct answers until graded (if quiz.show_correct_answers = FALSE)

**Status**: ✅ Implemented

---

### FR-AS-003: Grade Quiz (Manual)
**Actor**: Instructor, TA
**Priority**: 🔴 CRITICAL (P1)

**Description**: Instructor manually grades ESSAY and SHORT_ANSWER questions.

**Input**:
- Quiz Submission ID
- Manual score
- Feedback

**Process**:
1. Validate requester is course instructor or TA
2. Validate submission exists
3. Update QuizSubmission.manual_score
4. Update QuizSubmission.instructor_feedback
5. Update QuizSubmission.graded_at = CURRENT_TIMESTAMP
6. Update QuizSubmission.graded_by = current user
7. Calculate QuizSubmission.final_score = auto_score + manual_score
8. Check if all questions graded:
   - If yes: Update Attempt.status = 'GRADED'
   - Calculate Attempt.final_score
   - Update GradeBook (trigger)
   - Send notification to student
9. Log activity

**Output**:
- Submission graded
- Attempt final score calculated
- Student notified
- GradeBook updated

**Business Rules**:
- Only ESSAY and SHORT_ANSWER need manual grading
- Manual score cannot exceed max_points
- Feedback optional but recommended
- Student gets notification when all questions graded

**Status**: ✅ Implemented

---

### FR-AS-004: Submit Assignment
**Actor**: Student
**Priority**: 🔴 CRITICAL (P1)

**Description**: Student submits an assignment.

**Input**:
- Assignment ID
- Content (text, for ESSAY)
- Files (upload, for FILE_UPLOAD, CODE, PROJECT)
- Code (text, for CODE)

**Process**:
1. Validate student is enrolled in course
2. Check submission count within max_submissions
3. Calculate if late:
   - If current_time > due_date:
     - is_late = TRUE
     - days_late = CEIL((current_time - due_date) / 86400)
     - If days_late > max_late_days: reject
4. Upload files if provided
5. Create AssignmentSubmission record
6. If auto_grading_enabled (CODE):
   - Run test cases
   - Calculate auto_score
   - Set status = 'GRADED'
7. Else: status = 'SUBMITTED'
8. Send notification to instructor
9. Log activity

**Output**:
- Submission created
- Files uploaded
- Auto-graded (if CODE with auto-grading)
- Instructor notified

**Business Rules**:
- Must be enrolled in course
- Cannot exceed max_submissions
- Late submissions rejected if days_late > max_late_days
- Late penalty applied: penalty = (days_late > 0) ? (late_penalty_percent * max_points) : 0
- CODE assignments can be auto-graded
- File size limits: 50MB per file, 200MB total

**Status**: ✅ Implemented

---

### FR-AS-005: Grade Assignment
**Actor**: Instructor, TA
**Priority**: 🔴 CRITICAL (P1)

**Description**: Instructor grades a student's assignment submission.

**Input**:
- Assignment Submission ID
- Manual score
- Rubric scores (JSON)
- Feedback

**Process**:
1. Validate requester is course instructor or TA
2. Validate submission exists
3. Update AssignmentSubmission.manual_score
4. Update AssignmentSubmission.rubric_scores
5. Update AssignmentSubmission.feedback
6. Calculate final_score = auto_score + manual_score - penalty_applied
7. Update AssignmentSubmission.status = 'GRADED'
8. Update AssignmentSubmission.graded_at = CURRENT_TIMESTAMP
9. Update AssignmentSubmission.graded_by = current user
10. Update GradeBook (trigger)
11. Send notification to student
12. Log activity

**Output**:
- Assignment graded
- Final score calculated
- GradeBook updated
- Student notified

**Business Rules**:
- Manual score cannot exceed max_points
- Rubric scores must match rubric criteria
- Penalty already calculated on submission
- Final score = manual_score + auto_score - penalty
- Student notification includes score and feedback

**Status**: ✅ Implemented

---

### FR-AS-006: View GradeBook
**Actor**: Student, Instructor
**Priority**: 🟡 HIGH (P2)

**Description**: User views gradebook for a course.

**Input**:
- Course ID
- User ID (if instructor viewing student grades)

**Process**:
1. If student: show own gradebook
2. If instructor: show all students' gradebooks
3. Fetch GradeBook record
4. Fetch detailed scores:
   - All quiz attempts with scores
   - All assignment submissions with scores
   - Participation score (attendance)
5. Calculate grade distribution (for instructor)

**Output**:
- Quiz scores (total and breakdown)
- Assignment scores (total and breakdown)
- Participation score
- Total score
- Weighted score
- Letter grade
- Course progress percentage

**Business Rules**:
- Students see only their own grades
- Instructors see all students' grades
- Grade breakdown shows individual quiz/assignment scores
- Letter grade based on weighted_score

**Status**: ✅ Implemented

---

## 5. ENROLLMENT & PROGRESS (EP)

### FR-EP-001: Enroll in Course
**Actor**: Student
**Priority**: 🔴 CRITICAL (P1)

**Description**: Student enrolls in a course.

**Input**:
- Course ID

**Process**:
1. Validate course is published
2. Validate student not already enrolled
3. Create CourseEnrollment record:
   - user_id = student
   - course_id = course
   - role_in_course = 'STUDENT'
   - enrollment_status = 'ACTIVE'
4. Send notification to student
5. Log activity

**Output**:
- Student enrolled
- Course appears in "My Courses"
- Notification sent

**Business Rules**:
- Course must be published
- Cannot enroll twice in same course
- Free courses: instant enrollment
- Paid courses: enrollment after payment (future feature)

**Status**: ✅ Implemented

---

### FR-EP-002: Track Lecture Progress
**Actor**: Student (automatic)
**Priority**: 🔴 CRITICAL (P1)

**Description**: System tracks student's progress as they watch lectures.

**Input**:
- User ID
- Course ID
- Module ID
- Lecture ID
- Percent complete (0-100)
- Current position (seconds, for video)

**Process**:
1. Upsert Progress record:
   - If first access: create with status = 'IN_PROGRESS'
   - If percent_complete >= 80: status = 'COMPLETED'
2. Update last_accessed_at
3. Update last_position_seconds (for video resume)
4. If lecture completed: check if module completed
5. If module completed: check if course completed
6. If course completed:
   - Update CourseEnrollment.completion_percentage = 100
   - Update CourseEnrollment.enrollment_status = 'COMPLETED'
   - Trigger certificate issuance

**Output**:
- Progress saved
- Status updated
- Certificate issued (if course completed)

**Business Rules**:
- Progress tracked at lecture level
- Lecture considered "completed" at 80% progress
- Module completed when all lectures completed
- Course completed when all modules + all quizzes + all assignments completed with passing grade

**Status**: ✅ Implemented

---

### FR-EP-003: View Course Progress
**Actor**: Student, Instructor
**Priority**: 🟡 HIGH (P2)

**Description**: User views progress for a course.

**Input**:
- Course ID
- User ID (if instructor viewing student progress)

**Process**:
1. If student: show own progress
2. If instructor: show specified student's progress
3. Fetch Progress records for all lectures
4. Fetch Attempt records for all quizzes
5. Fetch AssignmentSubmission records for all assignments
6. Calculate completion percentages:
   - Lectures: completed / total
   - Quizzes: completed / total
   - Assignments: completed / total
   - Overall: weighted average

**Output**:
- Module-by-module progress
- Lectures completed / total
- Quizzes completed / total
- Assignments completed / total
- Overall completion percentage
- Time spent
- Last accessed

**Business Rules**:
- Students see only own progress
- Instructors can view any enrolled student's progress
- Progress updated in real-time
- Visual progress bars for each module

**Status**: ✅ Implemented

---

## 6. CLASS & BLENDED LEARNING (CL)

### FR-CL-001: Create Class
**Actor**: Instructor, Admin
**Priority**: 🟡 HIGH (P2)

**Description**: Instructor creates a physical or hybrid class for a course.

**Input**:
- Course ID
- Class name
- Start date
- End date
- Max students
- Location

**Process**:
1. Validate requester is instructor or admin
2. Validate course exists
3. Create Class record
4. Set instructor_id = current user
5. Set status = 'SCHEDULED'
6. Log activity

**Output**:
- Class created
- Instructor assigned
- Class ID returned

**Business Rules**:
- Class linked to a course
- Start date must be in future
- End date must be after start date
- Max students for capacity planning

**Status**: ✅ Implemented

---

### FR-CL-002: Create Schedule
**Actor**: Instructor, Admin
**Priority**: 🟡 HIGH (P2)

**Description**: Instructor creates session schedule for a class.

**Input**:
- Class ID
- Session date
- Start time
- End time
- Location
- Topic
- Session type (IN_PERSON, ONLINE, HYBRID)
- Meeting URL (for ONLINE/HYBRID)

**Process**:
1. Validate requester is class instructor or admin
2. Validate class exists
3. Validate session_date within class date range
4. Create Schedule record
5. Send notification to enrolled students
6. Log activity

**Output**:
- Session scheduled
- Students notified
- Schedule ID returned

**Business Rules**:
- Session date must be within class start/end dates
- End time must be after start time
- Online sessions require meeting URL
- Students get notification 24h before session

**Status**: ✅ Implemented

---

### FR-CL-003: Take Attendance
**Actor**: Instructor, TA
**Priority**: 🟡 HIGH (P2)

**Description**: Instructor takes attendance for a class session.

**Input**:
- Schedule ID
- Array of {user_id, status, check_in_time, notes}

**Process**:
1. Validate requester is class instructor or TA
2. Validate schedule exists and session_date is today
3. For each student:
   - Upsert Attendance record
   - Set status (PRESENT, ABSENT, LATE, EXCUSED)
   - Record check_in_time
4. Calculate attendance rate
5. Log activity

**Output**:
- Attendance recorded
- Attendance summary
- Attendance rate calculated

**Business Rules**:
- Can only take attendance on session date
- Late if check_in_time > start_time + 15 minutes
- Attendance affects participation score in GradeBook

**Status**: ✅ Implemented

---

## 7. CERTIFICATE (CT)

### FR-CT-001: Auto Issue Certificate
**Actor**: System
**Priority**: 🔴 CRITICAL (P1)

**Description**: System automatically issues certificate when student completes course.

**Input**:
- Course Enrollment ID (when enrollment_status = 'COMPLETED')

**Process**:
1. Trigger fires when CourseEnrollment updated to 'COMPLETED'
2. Validate completion_percentage >= 100
3. Check if certificate already exists
4. If not exists:
   - Generate unique certificate_code (e.g., BL-2025-000001)
   - Generate verification_code (random 64-char hex)
   - Get default certificate template
   - Create Certificate record:
     - user_id, course_id, course_enrollment_id
     - certificate_code, verification_code
     - issue_date = today
     - completion_date = enrollment.completed_at
     - final_grade = gradebook.weighted_score
     - status = 'ACTIVE'
   - Generate PDF (background job)
   - Generate QR code
   - Upload PDF and QR to storage
   - Update Certificate.pdf_url and qr_code_url
5. Send notification to student

**Output**:
- Certificate issued
- PDF generated
- QR code generated
- Student notified

**Business Rules**:
- Certificate issued only once per student per course
- Student must complete 100% of course
- Certificate code is unique
- Verification code is secret
- PDF generation is async (background job)
- Student can download PDF from notification

**Status**: ✅ Implemented

---

### FR-CT-002: Verify Certificate
**Actor**: Anyone (public)
**Priority**: 🔴 CRITICAL (P1)

**Description**: Anyone can verify certificate authenticity via public verification page.

**Input**:
- Certificate code OR Verification code

**Process**:
1. Validate input (code or QR scan)
2. Find Certificate by certificate_code or verification_code
3. Check status:
   - If not found: result = 'NOT_FOUND'
   - If status = 'REVOKED': result = 'REVOKED'
   - If valid_until < today: result = 'EXPIRED'
   - Else: result = 'VALID'
4. Log verification (CertificateVerification)
5. Return certificate details if valid

**Output**:
- Verification result
- If valid: show certificate details (holder name, course, issue date, grade)
- If invalid: show reason

**Business Rules**:
- Verification is public (no login required)
- Certificate code is public (can share)
- Verification code is secret (more secure)
- QR code contains verification URL
- All verifications logged for audit

**Status**: ✅ Implemented

---

### FR-CT-003: Revoke Certificate
**Actor**: Admin
**Priority**: 🟢 MEDIUM (P3)

**Description**: Admin can revoke a certificate (e.g., if fraud detected).

**Input**:
- Certificate ID
- Revoke reason

**Process**:
1. Validate requester is ADMIN
2. Validate certificate exists
3. Update Certificate:
   - status = 'REVOKED'
   - revoked_at = CURRENT_TIMESTAMP
   - revoked_by = current user
   - revoke_reason = reason
4. Send notification to certificate holder
5. Log activity

**Output**:
- Certificate revoked
- Verification returns 'REVOKED'
- Holder notified

**Business Rules**:
- Only ADMIN can revoke
- Revoked certificates fail verification
- Revoke reason required
- Holder gets notification
- Cannot un-revoke (permanent)

**Status**: ✅ Implemented

---

## 8. NOTIFICATION (NT)

### FR-NT-001: Send Notification
**Actor**: System
**Priority**: 🔴 CRITICAL (P1)

**Description**: System sends notifications to users for various events.

**Input**:
- User ID
- Notification type
- Title
- Message
- Related entity (type + ID)
- Action URL
- Priority

**Process**:
1. Create Notification record
2. Check NotificationPreference for user + type
3. If email_enabled: send email (background job)
4. If push_enabled: send push notification (background job)
5. If sms_enabled: send SMS (background job)
6. Create NotificationLog records for each channel
7. Update Notification flags (sent_via_email, sent_via_push)

**Output**:
- Notification created
- Delivery queued
- User sees notification in app

**Business Rules**:
- Respect user preferences
- High priority notifications bypass digest settings
- Notifications expire after 30 days
- In-app notifications always shown regardless of preferences

**Notification Types**:
- ASSIGNMENT_DUE (3 days before, 1 day before, on due date)
- GRADE_PUBLISHED (immediate)
- CERTIFICATE_ISSUED (immediate)
- COURSE_UPDATE (immediate or digest)
- CLASS_REMINDER (24h before session)
- ENROLLMENT_CONFIRMED (immediate)

**Status**: ✅ Implemented

---

### FR-NT-002: Manage Notification Preferences
**Actor**: User
**Priority**: 🟡 HIGH (P2)

**Description**: User can configure notification preferences for each notification type.

**Input**:
- Notification type
- Email enabled (boolean)
- Push enabled (boolean)
- SMS enabled (boolean)
- Frequency (IMMEDIATE, DAILY_DIGEST, WEEKLY_DIGEST, NEVER)

**Process**:
1. Validate user is authenticated
2. Upsert NotificationPreference record
3. Return updated preferences

**Output**:
- Preferences saved
- Future notifications respect preferences

**Business Rules**:
- Default: email = TRUE, push = TRUE, sms = FALSE, frequency = IMMEDIATE
- HIGH priority notifications always immediate
- NEVER means no notifications of that type
- Daily digest sent at 8 AM user timezone
- Weekly digest sent Monday 8 AM user timezone

**Status**: ✅ Implemented

---

### FR-NT-003: Mark Notification as Read
**Actor**: User
**Priority**: 🟡 HIGH (P2)

**Description**: User marks notification as read.

**Input**:
- Notification ID

**Process**:
1. Validate notification belongs to user
2. Update Notification:
   - is_read = TRUE
   - read_at = CURRENT_TIMESTAMP
3. Return success

**Output**:
- Notification marked read
- Unread count decreased

**Business Rules**:
- Can only mark own notifications
- Marking as read is permanent
- Unread count shown in badge

**Status**: ✅ Implemented

---

## 9. AUDIT & SYSTEM (AD)

### FR-AD-001: Log Activity
**Actor**: System
**Priority**: 🟢 MEDIUM (P3)

**Description**: System logs all important user actions for audit.

**Input**:
- User ID
- Action (CREATE, UPDATE, DELETE, VIEW, etc.)
- Entity type
- Entity ID
- Old values (JSON, for UPDATE/DELETE)
- New values (JSON, for CREATE/UPDATE)
- IP address
- User agent

**Process**:
1. Create ActivityLog record
2. Store as JSON for flexibility
3. log_date auto-generated for partitioning

**Output**:
- Activity logged
- Queryable for audit

**Business Rules**:
- Log all CREATE, UPDATE, DELETE operations
- Log sensitive operations (role assignments, certificate revocation, etc.)
- Retain logs for 1 year (configurable)
- Partition by log_date for performance

**Actions Logged**:
- User: LOGIN, LOGOUT, REGISTER, UPDATE_PROFILE, PASSWORD_RESET
- Course: CREATE, UPDATE, DELETE, PUBLISH, ARCHIVE
- Assignment: CREATE, UPDATE, DELETE, SUBMIT, GRADE
- Quiz: CREATE, UPDATE, DELETE, TAKE, SUBMIT, GRADE
- Certificate: ISSUE, VERIFY, REVOKE
- Role: GRANT, REVOKE
- Enrollment: ENROLL, DROP, COMPLETE

**Status**: ✅ Implemented

---

### FR-AD-002: Upload File
**Actor**: User
**Priority**: 🔴 CRITICAL (P1)

**Description**: User uploads file (assignment submission, lecture content, avatar, etc.).

**Input**:
- File (binary)
- Entity type (Assignment, Lecture, User)
- Entity ID

**Process**:
1. Validate file size (<= limit for entity type)
2. Validate file type (allowed MIME types)
3. Generate unique stored_filename
4. Upload to storage (LOCAL, S3, AZURE, or GCS)
5. Create File record
6. Return file URL

**Output**:
- File uploaded
- URL returned

**Business Rules**:
- File size limits:
  - Avatar: 5MB
  - Assignment submission: 50MB per file, 200MB total
  - Lecture video: 500MB
  - Lecture PDF/Slide: 50MB
- Allowed types:
  - Avatar: image/jpeg, image/png
  - Assignment: all types (configurable)
  - Lecture: video/*, application/pdf, application/vnd.ms-powerpoint, etc.
- Files soft-deleted (is_deleted flag) for recovery
- Storage type configurable via SystemSettings

**Status**: ✅ Implemented

---

### FR-AD-003: Manage System Settings
**Actor**: Admin
**Priority**: 🟢 MEDIUM (P3)

**Description**: Admin can view and update system settings.

**Input**:
- Setting key
- Setting value

**Process**:
1. Validate requester is ADMIN
2. Validate setting exists and is_editable = TRUE
3. Validate value matches data_type
4. Update SystemSettings record
5. Update updated_by = current user
6. Update updated_at = CURRENT_TIMESTAMP
7. Log activity

**Output**:
- Setting updated
- System behavior changes

**Business Rules**:
- Only ADMIN can change settings
- System settings (is_editable = FALSE) cannot be changed
- Settings validated by data_type before saving
- Changes logged for audit

**Common Settings**:
- MAX_FILE_SIZE_MB (INTEGER)
- CERTIFICATE_AUTO_ISSUE (BOOLEAN)
- LATE_SUBMISSION_DEFAULT_PENALTY (DECIMAL)
- SESSION_TIMEOUT_HOURS (INTEGER)
- PASSWORD_MIN_LENGTH (INTEGER)
- STORAGE_TYPE (STRING: LOCAL, S3, AZURE, GCS)
- EMAIL_FROM_ADDRESS (STRING)
- DEFAULT_TIMEZONE (STRING)

**Status**: ✅ Implemented

---

## 10. SUMMARY

### 10.1. Requirements by Priority

| Priority | Count | Percentage |
|----------|-------|------------|
| 🔴 CRITICAL (P1) | 25 | 69% |
| 🟡 HIGH (P2) | 7 | 19% |
| 🟢 MEDIUM (P3) | 4 | 11% |
| ⚪ LOW (P4) | 0 | 0% |
| **TOTAL** | **36** | **100%** |

### 10.2. Requirements by Domain

| Domain | Requirements | Critical | High | Medium | Low |
|--------|-------------|----------|------|--------|-----|
| UM - User Management | 6 | 5 | 1 | 0 | 0 |
| CM - Course Management | 8 | 7 | 1 | 0 | 0 |
| AS - Assessment | 6 | 5 | 1 | 0 | 0 |
| EP - Enrollment & Progress | 3 | 2 | 1 | 0 | 0 |
| CL - Class & Blended Learning | 3 | 0 | 3 | 0 | 0 |
| CT - Certificate | 3 | 2 | 0 | 1 | 0 |
| NT - Notification | 3 | 1 | 2 | 0 | 0 |
| AD - Audit & System | 3 | 1 | 0 | 2 | 0 |
| **TOTAL** | **36** | **25** | **7** | **4** | **0** |

### 10.3. Implementation Status

| Status | Count | Percentage |
|--------|-------|------------|
| ✅ Implemented | 36 | 100% |
| 🔄 In Progress | 0 | 0% |
| ⏳ Planned | 0 | 0% |
| **TOTAL** | **36** | **100%** |

---

## 11. NON-FUNCTIONAL REQUIREMENTS

### 11.1. Performance
- NFR-PF-001: API response time < 200ms (p95)
- NFR-PF-002: Page load time < 2s (p95)
- NFR-PF-003: Video streaming starts < 3s
- NFR-PF-004: Support 1000 concurrent users
- NFR-PF-005: Database queries < 100ms (p95)

### 11.2. Security
- NFR-SC-001: All passwords hashed with bcrypt (cost >= 12)
- NFR-SC-002: JWT tokens expire after 24h
- NFR-SC-003: HTTPS only in production
- NFR-SC-004: SQL injection prevention (parameterized queries)
- NFR-SC-005: XSS prevention (input sanitization)
- NFR-SC-006: CSRF tokens for all mutations
- NFR-SC-007: Rate limiting (100 requests/minute per IP)
- NFR-SC-008: File upload virus scanning

### 11.3. Scalability
- NFR-SC-001: Horizontal scaling for web servers
- NFR-SC-002: Database read replicas
- NFR-SC-003: CDN for video content
- NFR-SC-004: File storage on S3/Azure/GCS
- NFR-SC-005: Redis caching for frequent queries

### 11.4. Availability
- NFR-AV-001: 99.9% uptime (max 8.76h downtime/year)
- NFR-AV-002: Automated backups every 6 hours
- NFR-AV-003: Disaster recovery plan (RPO < 1h, RTO < 4h)
- NFR-AV-004: Health checks every 30s
- NFR-AV-005: Graceful degradation on service failures

### 11.5. Usability
- NFR-US-001: Mobile responsive design
- NFR-US-002: Accessibility (WCAG 2.1 AA)
- NFR-US-003: Multi-language support (i18n)
- NFR-US-004: Keyboard navigation
- NFR-US-005: Progress indicators for long operations

### 11.6. Maintainability
- NFR-MT-001: Code coverage >= 80%
- NFR-MT-002: API documentation (OpenAPI/Swagger)
- NFR-MT-003: Database migrations versioned
- NFR-MT-004: Logging at INFO level minimum
- NFR-MT-005: Error tracking (Sentry or similar)

---

## 12. APPENDIX

### 12.1. Related Documents
- [BFD-SPEC.md](./BFD-SPEC.md) - Business Function Diagram
- [ERD-SPEC.md](./ERD-SPEC.md) - Entity Relationship Diagram
- [DATABASE-SCHEMA.md](./DATABASE-SCHEMA.md) - Complete DDL
- [API-ENDPOINTS.md](./API-ENDPOINTS.md) - REST API specification

### 12.2. Glossary
- **Blended Learning**: Combination of online and in-person learning
- **Attempt**: A single attempt to take a quiz
- **Submission**: Student's answer to a quiz question or assignment
- **Enrollment**: Registration in a course or class
- **Progress**: Tracking of lecture completion
- **Rubric**: Grading criteria with points for each criterion
- **Auto-grading**: Automated scoring of MCQ, TRUE_FALSE, and CODE questions
- **Certificate**: Digital certificate issued upon course completion
- **Verification**: Public check of certificate authenticity

---

**Document Status**: ✅ Complete
**Last Updated**: 2025-11-25
**Total Requirements**: 36 functional + 26 non-functional

---

**END OF FUNCTIONAL REQUIREMENTS SPECIFICATION**
