# Business Function Diagram Specification - B-Learning Core

**Project**: B-Learning Core v1.0
**Author**: Nguyễn Văn Kiệt - CNTT1-K63
**Created**: 2025-11-27
**Total Functions**: 7 domains với 35+ business functions

---

## 1. OVERVIEW

### 1.1. Purpose

Business Function Diagram (BFD) mô tả các chức năng nghiệp vụ của hệ thống B-Learning Core, bao gồm:
- **Actors**: Admin, Instructor, TA, Student, System
- **Business Functions**: Use cases và workflows chính
- **System Boundaries**: Phân tách core functions vs external services

### 1.2. Actors

| Actor | Mô tả | Vai trò chính |
|-------|-------|---------------|
| **Student** | Học viên | Học tập, làm bài tập/quiz, xem tiến độ |
| **Instructor** | Giảng viên | Tạo khóa học, chấm bài, quản lý lớp |
| **TA** | Teaching Assistant | Hỗ trợ chấm bài, moderator |
| **Admin** | Quản trị viên | Quản lý users, courses, system |
| **System** | Hệ thống tự động | Auto-grading, notifications, certificates |

---

## 2. BUSINESS FUNCTIONS BY DOMAIN

### DOMAIN 1: User Management

#### 1.1. User Registration & Authentication
- **FR-1.1**: Student tự đăng ký tài khoản
  - Input: Email, password, first_name, last_name
  - Validation: Email format, password strength
  - Output: User account (status: PENDING_VERIFICATION)
  - Post-condition: Send verification email (external service)

- **FR-1.2**: User login
  - Input: Email, password
  - Validation: Check credentials, account_status = 'ACTIVE'
  - Output: JWT token, user profile
  - Business Rule: Suspended/Deleted users cannot login

- **FR-1.3**: Password reset
  - Input: Email
  - Output: Reset token (external email service)
  - Business Rule: Token expires in 1 hour

#### 1.2. Role Management (Admin only)
- **FR-1.4**: Admin assigns roles to users
  - Input: user_id, role_id, expires_at (optional)
  - Validation: User exists, Role exists
  - Output: UserRole record
  - Business Rule: One user can have multiple roles

- **FR-1.5**: Admin creates/modifies roles
  - Input: role_name, permissions JSON
  - Output: New Role record
  - Business Rule: Cannot delete role if UserRole references exist

#### 1.3. Profile Management
- **FR-1.6**: User updates profile
  - Input: first_name, last_name, preferences JSON
  - Output: Updated User record
  - Business Rule: Cannot change email (requires verification)

- **FR-1.7**: User configures notification preferences
  - Input: preferences JSON
  - Output: Updated User.preferences
  - Example: `{"notifications": {"assignment_due": {"email": true, "push": false}}}`

---

### DOMAIN 2: Course Content Management

#### 2.1. Course Management (Instructor)
- **FR-2.1**: Instructor creates course
  - Input: code, title, description, difficulty_level, credits
  - Validation: code UNIQUE, code format [A-Z0-9]{3,10}
  - Output: Course record (status: DRAFT)
  - Business Rule: created_by = current instructor

- **FR-2.2**: Instructor publishes course
  - Pre-condition: Course has at least 1 module with lectures
  - Action: Update status = 'PUBLISHED'
  - Post-condition: Course visible in catalog

- **FR-2.3**: Instructor archives course
  - Action: Update status = 'ARCHIVED'
  - Business Rule: Cannot archive if active enrollments exist

#### 2.2. Module Management (Instructor)
- **FR-2.4**: Instructor creates module
  - Input: course_id, title, description, order_num
  - Validation: (course_id, order_num) UNIQUE
  - Output: Module record
  - Business Rule: order_num auto-incremented

- **FR-2.5**: Instructor sets module prerequisites
  - Input: prerequisite_module_ids (UUID[])
  - Validation: All UUIDs must be valid modules in same course
  - Business Rule: No circular dependencies

- **FR-2.6**: Instructor reorders modules
  - Input: Array of {module_id, new_order_num}
  - Action: Batch update order_num
  - Validation: No duplicate order_num

#### 2.3. Lecture Management (Instructor)
- **FR-2.7**: Instructor creates lecture
  - Input: module_id, title, type, order_num
  - Types: VIDEO, PDF, SLIDE, AUDIO, TEXT, ASSIGNMENT
  - Validation: (module_id, order_num) UNIQUE
  - Output: Lecture record

- **FR-2.8**: Instructor creates assignment lecture
  - Input: Lecture fields + assignment_config JSON
  - assignment_config structure:
    ```json
    {
      "max_points": 100,
      "due_date": "2025-12-15T23:59:00Z",
      "submission_types": ["file", "text", "code"],
      "allowed_file_types": [".java", ".py"],
      "max_file_size_mb": 10,
      "rubric": {"code_quality": 40, "functionality": 40, "docs": 20}
    }
    ```
  - Business Rule: assignment_config only when type = 'ASSIGNMENT'

#### 2.4. Resource Management (Instructor)
- **FR-2.9**: Instructor uploads lecture resources
  - Input: lecture_id, file (upload to S3/GCS)
  - Output: Resource record with file_url, file_type, file_size_bytes
  - Business Rule: file_url points to external storage

---

### DOMAIN 3: Assessment

#### 3.1. Question Bank Management (Instructor)
- **FR-3.1**: Instructor creates question
  - Input: course_id, question_text, type, default_points
  - Types: MCQ, TRUE_FALSE, ESSAY, SHORT_ANSWER
  - Output: Question record
  - Business Rule: Questions belong to course, reusable across quizzes

- **FR-3.2**: Instructor creates MCQ options
  - Input: question_id, option_text, is_correct, order_num
  - Validation: At least 2 options, at least 1 correct
  - Business Rule: For TRUE_FALSE, exactly 2 options

#### 3.2. Quiz Management (Instructor)
- **FR-3.3**: Instructor creates quiz
  - Input: course_id, title, duration_minutes, total_points, passing_score
  - Output: Quiz record (status: DRAFT)
  - Business Rule: passing_score <= total_points

- **FR-3.4**: Instructor adds questions to quiz
  - Input: quiz_id, questions JSON array
  - Structure: `[{"question_id": "uuid", "points": 10, "order": 1}]`
  - Validation: All question_ids must exist
  - Business Rule: Sum of points should equal total_points

- **FR-3.5**: Instructor publishes quiz
  - Pre-condition: Quiz has at least 1 question
  - Action: Update status = 'PUBLISHED'
  - Business Rule: Can set available_from, available_until

#### 3.3. Quiz Taking (Student)
- **FR-3.6**: Student starts quiz attempt
  - Pre-condition: Enrolled in course, quiz published, within available dates
  - Input: quiz_id
  - Output: Attempt record (status: IN_PROGRESS, attempt_number auto-incremented)
  - Business Rule: Check max_attempts limit

- **FR-3.7**: Student answers questions
  - Input: answers JSON array
  - Structure for MCQ:
    ```json
    {
      "question_id": "uuid",
      "selected_options": ["option-uuid-a"],
      "answer_text": null
    }
    ```
  - Structure for ESSAY:
    ```json
    {
      "question_id": "uuid",
      "selected_options": null,
      "answer_text": "Student's essay answer..."
    }
    ```
  - Action: Update Attempt.answers (JSON)

- **FR-3.8**: Student submits quiz
  - Pre-condition: Within time limit (started_at + duration_minutes)
  - Action: Update status = 'SUBMITTED', submitted_at = NOW()
  - Post-condition: System auto-grades MCQ/TRUE_FALSE
  - Business Rule: Cannot edit after submission

#### 3.4. Quiz Grading
- **FR-3.9**: System auto-grades MCQ/TRUE_FALSE
  - Trigger: Quiz submission
  - Process: Compare selected_options with correct options
  - Action: Update answers JSON with score, is_correct
  - Output: Total score calculated

- **FR-3.10**: Instructor/TA grades ESSAY/SHORT_ANSWER
  - Input: attempt_id, question_id, score, feedback
  - Action: Update answers JSON with manual score
  - Business Rule: score <= max_score

- **FR-3.11**: Finalize attempt grading
  - Pre-condition: All questions graded
  - Action: Update status = 'GRADED', calculate total score
  - Post-condition: Student can view results

#### 3.5. Assignment Submission (Student)
- **FR-3.12**: Student submits assignment
  - Pre-condition: Enrolled in course, lecture type = 'ASSIGNMENT'
  - Input: lecture_id, files (upload to S3/GCS), submission_text
  - Output: AssignmentSubmission record
  - Business Rule: Check due_date, file types, file size from assignment_config

- **FR-3.13**: Instructor/TA grades assignment
  - Input: submission_id, score, feedback
  - Validation: score <= max_score from assignment_config
  - Action: Update status = 'GRADED', score, feedback
  - Business Rule: Can grade based on rubric in assignment_config

---

### DOMAIN 4: Enrollment & Progress

#### 4.1. Enrollment (Student)
- **FR-4.1**: Student enrolls in self-paced course
  - Input: course_id
  - Pre-condition: Course status = 'PUBLISHED'
  - Output: Enrollment record (class_id = NULL, status: ACTIVE)
  - Business Rule: Cannot enroll in same course twice (unless dropped)

- **FR-4.2**: Student enrolls in class (blended learning)
  - Input: course_id, class_id
  - Pre-condition: Class not full (count < max_students)
  - Output: Enrollment record (status: ACTIVE)
  - Business Rule: (user_id, course_id, class_id) UNIQUE

- **FR-4.3**: Student/Admin drops enrollment
  - Action: Update status = 'DROPPED'
  - Business Rule: Cannot drop if final grade issued

#### 4.2. Progress Tracking (System)
- **FR-4.4**: System tracks module completion
  - Trigger: Student completes last lecture in module
  - Action: Create/Update Progress record
  - Output: status = 'COMPLETED', completion_percentage = 100
  - Business Rule: Progress at module level (not lecture level)

- **FR-4.5**: System calculates course completion
  - Logic: All modules completed → enrollment status = 'COMPLETED'
  - Trigger: Last module marked completed
  - Post-condition: Trigger certificate generation

#### 4.3. Progress Viewing
- **FR-4.6**: Student views own progress
  - Output: List of modules with status, completion_percentage
  - Business Rule: Can see overall course progress

- **FR-4.7**: Instructor views class progress
  - Input: class_id
  - Output: Aggregated progress for all students in class
  - Business Rule: Can identify struggling students

---

### DOMAIN 5: Class Management

#### 5.1. Class Creation (Instructor/Admin)
- **FR-5.1**: Instructor creates class
  - Input: course_id, class_name, start_date, end_date, max_students
  - Validation: end_date >= start_date
  - Output: Class record (status: SCHEDULED)
  - Business Rule: created_by = current instructor

- **FR-5.2**: Instructor adds class schedule
  - Input: schedules JSON array
  - Structure:
    ```json
    [{
      "session_id": "uuid",
      "date": "2025-12-01",
      "start_time": "09:00",
      "end_time": "11:00",
      "topic": "Chương 1",
      "location": "Phòng 101",
      "type": "IN_PERSON",
      "attendances": []
    }]
    ```
  - Business Rule: Sessions must be within start_date and end_date

#### 5.2. Attendance Tracking
- **FR-5.3**: Instructor marks attendance
  - Input: class_id, session_id, attendances array
  - Structure:
    ```json
    {
      "user_id": "uuid",
      "status": "PRESENT|LATE|ABSENT",
      "check_in": "09:05"
    }
    ```
  - Action: Update Class.schedules JSON
  - Business Rule: Only students enrolled in class can be marked

- **FR-5.4**: Student views attendance report
  - Output: List of sessions with attendance status
  - Business Rule: Can only view own attendance

#### 5.3. Class Status Management
- **FR-5.5**: System auto-updates class status
  - Logic:
    - start_date reached → status = 'ONGOING'
    - end_date passed → status = 'COMPLETED'
  - Trigger: Daily cron job

- **FR-5.6**: Instructor cancels class
  - Pre-condition: status = 'SCHEDULED'
  - Action: Update status = 'CANCELLED'
  - Post-condition: Notify enrolled students (external service)

---

### DOMAIN 6: Certificate Management

#### 6.1. Certificate Generation (System)
- **FR-6.1**: System generates certificate
  - Trigger: Enrollment status = 'COMPLETED' AND final_grade >= passing_score
  - Input: user_id, course_id
  - Process:
    1. Calculate final_grade from quiz/assignment scores
    2. Generate certificate_code (format: BL-YYYY-NNNNNN)
    3. Generate verification_code (UUID-based)
    4. Generate PDF (external service)
  - Output: Certificate record (status: ACTIVE)
  - Business Rule: One certificate per (user_id, course_id)

- **FR-6.2**: Student downloads certificate
  - Input: certificate_id
  - Pre-condition: Owns certificate
  - Output: PDF download from pdf_url
  - Business Rule: Only active certificates can be downloaded

#### 6.2. Certificate Verification
- **FR-6.3**: Public verifies certificate
  - Input: certificate_code OR verification_code
  - Output: Certificate details (user_name, course_title, issue_date, grade)
  - Business Rule: Public API, no authentication required

- **FR-6.4**: Admin revokes certificate
  - Input: certificate_id, reason
  - Action: Update status = 'REVOKED'
  - Business Rule: Cannot download revoked certificates

---

### DOMAIN 7: Reporting & Analytics (Admin/Instructor)

#### 7.1. Course Analytics
- **FR-7.1**: View course enrollment stats
  - Output: Total enrollments, active/completed/dropped counts
  - Grouping: By course, by class, by time period

- **FR-7.2**: View course completion rate
  - Calculation: (Completed enrollments / Total enrollments) * 100%
  - Business Rule: Only published courses

#### 7.2. Quiz Analytics
- **FR-7.3**: View quiz performance
  - Output: Average score, pass rate, attempt distribution
  - Business Rule: Only graded attempts

- **FR-7.4**: View question difficulty
  - Calculation: % students answered correctly
  - Business Rule: Helps identify problematic questions

#### 7.3. Student Performance
- **FR-7.5**: View student transcript
  - Input: user_id
  - Output: All courses, grades, certificates
  - Business Rule: Student can view own, Instructor can view class students

---

## 3. SYSTEM BOUNDARIES

### 3.1. Core Functions (In-Scope)
✅ User Management (RBAC)
✅ Course Content Management
✅ Assessment (Quiz + Assignment)
✅ Progress Tracking
✅ Class Management (Blended Learning)
✅ Enrollment
✅ Certificate Generation
✅ Basic Reporting

### 3.2. External Services (Out-of-Scope)
❌ **Email Service**: Use SendGrid, AWS SES
  - User verification emails
  - Notification emails
  - Password reset emails

❌ **File Storage**: Use S3, GCS
  - Video files (lecture resources)
  - PDF files (slides, certificates)
  - Assignment submissions

❌ **Payment Processing**: Use Stripe, PayPal
  - Course enrollment fees
  - Subscription management

❌ **Video Streaming**: Use Vimeo, YouTube
  - Live class streaming
  - Recorded lecture playback

❌ **Discussion Forum**: Use Discourse, Flarum
  - Student Q&A
  - Peer discussion

❌ **Real-time Notifications**: Use Firebase, Pusher
  - Push notifications
  - In-app notifications

---

## 4. BUSINESS RULES SUMMARY

### 4.1. Data Integrity Rules
1. **Email Uniqueness**: One email = one user account
2. **Course Code Uniqueness**: Course codes must be unique
3. **Role Assignments**: One user can have multiple roles
4. **Enrollment Uniqueness**: Cannot enroll in same (course, class) twice

### 4.2. Status Transitions

#### User Account Status
```
PENDING_VERIFICATION → ACTIVE → SUSPENDED → DELETED
                    ↑_________________↓
```

#### Course Status
```
DRAFT → PUBLISHED → ARCHIVED
```

#### Enrollment Status
```
ACTIVE → COMPLETED
   ↓
DROPPED / SUSPENDED
```

#### Quiz Attempt Status
```
IN_PROGRESS → SUBMITTED → PENDING_GRADING → GRADED
```

### 4.3. Access Control Rules

| Function | Student | Instructor | TA | Admin |
|----------|---------|------------|-----|-------|
| Create Course | ❌ | ✅ | ❌ | ✅ |
| Enroll in Course | ✅ | ❌ | ❌ | ✅ (on behalf) |
| Take Quiz | ✅ | ❌ | ❌ | ❌ |
| Grade Quiz | ❌ | ✅ | ✅ | ✅ |
| View All Users | ❌ | ❌ | ❌ | ✅ |
| Issue Certificate | ❌ | ❌ | ❌ | ✅ (manual) |

### 4.4. Validation Rules
1. **Email Format**: Must match regex `^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$`
2. **Course Code**: Must match `^[A-Z0-9]{3,10}$`
3. **Password**: Min 8 chars, at least 1 uppercase, 1 lowercase, 1 number
4. **Quiz Score**: Must be between 0 and max_score
5. **Passing Score**: Must be <= total_points
6. **File Size**: Check against assignment_config.max_file_size_mb

---

## 5. WORKFLOWS

### 5.1. Student Learning Journey

```
1. Register Account
   ↓
2. Verify Email (external)
   ↓
3. Browse Course Catalog
   ↓
4. Enroll in Course (self-paced OR class-based)
   ↓
5. Study Lectures (watch videos, read PDFs)
   ↓
6. Complete Assignments
   ↓
7. Take Quizzes
   ↓
8. Track Progress
   ↓
9. Complete Course
   ↓
10. Receive Certificate
```

### 5.2. Instructor Course Creation

```
1. Create Course (status: DRAFT)
   ↓
2. Create Modules
   ↓
3. Create Lectures (VIDEO, PDF, ASSIGNMENT, etc.)
   ↓
4. Upload Resources (to S3/GCS)
   ↓
5. Create Quizzes
   ↓
6. Add Questions from Question Bank
   ↓
7. Publish Course (status: PUBLISHED)
   ↓
8. (Optional) Create Class for Blended Learning
   ↓
9. Monitor Student Progress
   ↓
10. Grade Assignments & Essays
```

### 5.3. Quiz Workflow

```
Student Side:
1. Student enrolls in course
2. Student starts quiz attempt
3. Student answers questions
4. Student submits quiz
5. Student views results (after grading)

System Side:
1. Auto-grade MCQ/TRUE_FALSE
2. Update attempt.answers JSON with scores
3. Set status = PENDING_GRADING (if has ESSAY)

Instructor Side:
1. View pending attempts
2. Grade ESSAY/SHORT_ANSWER questions
3. Add feedback
4. Finalize grading (status = GRADED)
```

### 5.4. Blended Learning Workflow

```
1. Instructor creates Class
   ↓
2. Instructor sets Class Schedule (JSON)
   ↓
3. Students enroll in Class (enrollment.class_id = class_id)
   ↓
4. Class starts (status = ONGOING)
   ↓
5. In-person sessions (Instructor marks attendance)
   ↓
6. Students complete online modules (self-paced between sessions)
   ↓
7. Class ends (status = COMPLETED)
   ↓
8. Final grades calculated
   ↓
9. Certificates issued
```

---

## 6. API ENDPOINTS SUMMARY

See **API-ENDPOINTS.md** for detailed API specifications.

### By Domain:
- **User Management**: 8 endpoints (register, login, profile, roles)
- **Course Management**: 12 endpoints (CRUD courses, modules, lectures)
- **Assessment**: 15 endpoints (quizzes, questions, attempts, assignments)
- **Enrollment**: 6 endpoints (enroll, drop, view progress)
- **Class Management**: 8 endpoints (create class, schedules, attendance)
- **Certificate**: 4 endpoints (generate, download, verify, revoke)
- **Reporting**: 5 endpoints (analytics, stats)

**Total**: 58+ REST API endpoints

---

## 7. VALIDATION CHECKLIST

### Business Logic Validation
- [ ] All enrollment rules enforced (max_students, published courses only)
- [ ] Quiz attempt limits respected (max_attempts)
- [ ] Assignment due dates validated
- [ ] Module prerequisites checked before access
- [ ] Certificate only issued if passing_score met
- [ ] Cannot grade already graded attempts
- [ ] Attendance only for enrolled students

### Data Consistency
- [ ] Enrollment.class_id NULL for self-paced, UUID for blended
- [ ] Lecture.assignment_config only when type = 'ASSIGNMENT'
- [ ] Quiz.questions JSON references valid question_ids
- [ ] Attempt.answers JSON matches quiz questions
- [ ] Class.schedules JSON has valid attendances

### Access Control
- [ ] Students cannot access other students' data
- [ ] Instructors can only access own courses
- [ ] TAs have limited grading permissions
- [ ] Admins have full access

---

**END OF BFD SPECIFICATION**
