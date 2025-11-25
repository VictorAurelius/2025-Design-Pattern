# Business Function Diagram Specification

**Project**: B-Learning System v2.0
**Document Type**: Business Function Specification
**Version**: 2.0
**Date**: 2025-11-25
**Author**: Nguyễn Văn Kiệt - CNTT1-K63

---

## 1. OVERVIEW

### 1.1 Purpose
Mô tả các chức năng nghiệp vụ của hệ thống B-Learning (Blended Learning) thông qua Use Case Diagram. Hệ thống hỗ trợ học tập kết hợp giữa trực tuyến và trực tiếp, tập trung vào Assignment-based learning với chứng chỉ tự động.

### 1.2 Scope
- **Bao gồm**: Course management, Assignment system, Quiz, Progress tracking, Certificate, Notification
- **Không bao gồm**: Forum/Discussion, Social features (removed from v1.0)

### 1.3 System Actors
1. **Student** (Học viên) - Người học
2. **Instructor** (Giảng viên) - Người dạy, tạo nội dung
3. **Teaching Assistant (TA)** - Trợ giảng, hỗ trợ chấm bài
4. **Admin** (Quản trị viên) - Quản lý hệ thống
5. **System** (Hệ thống) - Tự động hóa các tác vụ

---

## 2. ACTORS DESCRIPTION

### 2.1 Student (Học viên)
**Role**: End user của hệ thống, người tham gia học tập
**Primary Goals**:
- Học các khóa học trực tuyến và trực tiếp
- Nộp bài tập và làm bài kiểm tra
- Theo dõi tiến độ học tập
- Nhận chứng chỉ khi hoàn thành

**Characteristics**:
- Có thể đăng ký nhiều khóa học
- Tham gia lớp học trực tiếp (optional)
- Self-paced learning hoặc class-based learning

### 2.2 Instructor (Giảng viên)
**Role**: Người tạo và quản lý nội dung học tập
**Primary Goals**:
- Tạo và quản lý khóa học
- Tạo bài giảng, bài tập, bài kiểm tra
- Chấm bài và đánh giá học viên
- Theo dõi tiến độ lớp học

**Characteristics**:
- Có thể dạy nhiều khóa học/lớp học
- Có quyền cao trong khóa học của mình
- Làm việc với TA để hỗ trợ chấm bài

### 2.3 Teaching Assistant (TA)
**Role**: Hỗ trợ giảng viên
**Primary Goals**:
- Chấm bài tập và bài kiểm tra
- Trả lời câu hỏi học viên
- Theo dõi tiến độ học viên

**Characteristics**:
- Quyền hạn thấp hơn Instructor
- Được assign vào các khóa học cụ thể
- Không thể tạo/sửa nội dung khóa học

### 2.4 Admin (Quản trị viên)
**Role**: Quản lý toàn bộ hệ thống
**Primary Goals**:
- Quản lý người dùng và phân quyền
- Quản lý cấu hình hệ thống
- Theo dõi hoạt động và tạo báo cáo
- Xử lý vấn đề kỹ thuật

**Characteristics**:
- Quyền cao nhất trong hệ thống
- Có thể truy cập mọi dữ liệu
- Quản lý certificate templates

### 2.5 System (Hệ thống tự động)
**Role**: Thực hiện các tác vụ tự động
**Primary Goals**:
- Tự động chấm bài trắc nghiệm
- Tự động cập nhật tiến độ học tập
- Tự động cấp chứng chỉ khi đủ điều kiện
- Gửi thông báo tự động

**Characteristics**:
- Chạy background jobs
- Event-driven triggers
- Không có human interaction

---

## 3. USE CASES BY ACTOR

### 3.1 STUDENT USE CASES

#### UC-STU-001: Register Account
**Actor**: Student (Guest)
**Description**: Đăng ký tài khoản mới trong hệ thống
**Preconditions**: User chưa có tài khoản
**Postconditions**: Tài khoản được tạo, email verification gửi đi
**Main Flow**:
1. User điền form đăng ký (email, password, first_name, last_name)
2. System validate thông tin
3. System tạo tài khoản với status 'PENDING_VERIFICATION'
4. System gửi email verification
5. User click link trong email
6. System activate tài khoản (status = 'ACTIVE')

**Alternative Flows**:
- 2a. Email đã tồn tại → Show error
- 4a. Email gửi thất bại → Show warning, cho phép resend

---

#### UC-STU-002: Login
**Actor**: Student
**Description**: Đăng nhập vào hệ thống
**Preconditions**: User có tài khoản active
**Postconditions**: User được authenticate, session được tạo
**Main Flow**:
1. User nhập email và password
2. System verify credentials
3. System tạo session token
4. System cập nhật last_login_at
5. User được redirect đến dashboard

**Alternative Flows**:
- 2a. Credentials sai → Show error, track failed attempts
- 2b. Account suspended → Show message

---

#### UC-STU-003: Browse Courses
**Actor**: Student
**Description**: Xem danh sách khóa học có sẵn
**Preconditions**: User đã login
**Postconditions**: Danh sách khóa học được hiển thị
**Main Flow**:
1. User truy cập course catalog
2. System load courses với status = 'PUBLISHED'
3. User có thể filter (category, difficulty, search)
4. System hiển thị course cards (title, thumbnail, description, rating)
5. User click vào course để xem chi tiết

**Includes**: Search, Filter

---

#### UC-STU-004: Enroll in Course
**Actor**: Student
**Description**: Đăng ký vào một khóa học
**Preconditions**:
- User đã login
- Course status = 'PUBLISHED'
- User chưa enroll course này
**Postconditions**:
- CourseEnrollment record created
- User có quyền truy cập course content
**Main Flow**:
1. User xem course detail
2. User click "Enroll" button
3. System check prerequisites (if any)
4. System tạo CourseEnrollment (status = 'ACTIVE', role = 'STUDENT')
5. System gửi notification "Enrollment confirmed"
6. User được redirect đến course content

**Alternative Flows**:
- 3a. Không đủ prerequisites → Show message
- 4a. Course đã full (nếu có limit) → Show message

---

#### UC-STU-005: Watch Lecture
**Actor**: Student
**Description**: Xem bài giảng video/slides
**Preconditions**:
- User đã enroll course
- Lecture unlocked (prerequisites met)
**Postconditions**:
- Progress được cập nhật
- Last accessed timestamp được update
**Main Flow**:
1. User click vào lecture
2. System check access permission
3. System load lecture content (video/PDF/slides)
4. User xem nội dung
5. System track viewing progress (% complete, last position for video)
6. System auto-save progress mỗi 30s
7. Khi hoàn thành (percent_complete = 100%), Progress.status = 'COMPLETED'

**Includes**: Track Progress (UC-SYS-002)

---

#### UC-STU-006: Download Resources
**Actor**: Student
**Description**: Tải tài liệu đính kèm bài giảng
**Preconditions**:
- User đã enroll course
- Resource.is_downloadable = TRUE
**Postconditions**: File được download
**Main Flow**:
1. User click download button
2. System check permission
3. System generate secure download link (nếu cần)
4. Browser download file
5. System log download activity

---

#### UC-STU-007: Take Quiz
**Actor**: Student
**Description**: Làm bài kiểm tra trắc nghiệm/tự luận
**Preconditions**:
- User đã enroll course
- Quiz is_published = TRUE
- Chưa vượt quá attempt_limit (nếu có)
- Trong thời gian open_at - close_at (nếu có)
**Postconditions**:
- Attempt được tạo/cập nhật
- Score được tính (auto hoặc chờ manual grading)
**Main Flow**:
1. User click "Start Quiz"
2. System tạo Attempt (status = 'IN_PROGRESS', attempt_number++)
3. System load questions (shuffle nếu quiz.shuffle_questions = TRUE)
4. User trả lời từng câu hỏi
5. System lưu QuizSubmission cho mỗi câu
6. User click "Submit"
7. System update Attempt (status = 'SUBMITTED', submitted_at)
8. System auto-grade MCQ questions (UC-SYS-003)
9. Nếu có essay questions → chờ manual grading
10. User xem kết quả (nếu quiz.show_correct_answers = TRUE)

**Alternative Flows**:
- 2a. Đã hết attempt_limit → Show error
- 7a. Hết thời gian → System tự động submit

**Includes**: Auto Grade Quiz (UC-SYS-003)

---

#### UC-STU-008: Submit Assignment
**Actor**: Student
**Description**: Nộp bài tập (essay, code, file upload)
**Preconditions**:
- User đã enroll course
- Assignment đã được assigned
- Trước deadline hoặc trong late submission period
**Postconditions**:
- AssignmentSubmission được tạo
- Status = 'SUBMITTED'
- Notification gửi đến instructor
**Main Flow**:
1. User truy cập assignment detail
2. User đọc instructions và rubric
3. User viết nội dung hoặc upload file
4. User click "Submit"
5. System validate (file size, format)
6. System tạo AssignmentSubmission
7. System check nếu late:
   - Nếu sau deadline: is_late = TRUE, days_late = số ngày trễ
   - Tính penalty: penalty_applied = late_penalty_percent * max_points
8. System gửi notification đến instructor
9. User xem confirmation

**Alternative Flows**:
- 5a. File quá lớn → Show error
- 7a. Quá deadline và không allow late submission → Block submit

---

#### UC-STU-009: View Progress
**Actor**: Student
**Description**: Xem tiến độ học tập của mình
**Preconditions**: User đã enroll ít nhất 1 course
**Postconditions**: Dashboard hiển thị progress
**Main Flow**:
1. User truy cập dashboard
2. System load tất cả enrolled courses
3. Cho mỗi course, system tính:
   - Completion percentage (lectures, quizzes, assignments)
   - Current grade
   - Next item to complete
4. System hiển thị timeline và statistics
5. User có thể drill down vào từng course

---

#### UC-STU-010: View Grades
**Actor**: Student
**Description**: Xem điểm số của mình
**Preconditions**: User đã enroll course và có ít nhất 1 graded item
**Postconditions**: Gradebook được hiển thị
**Main Flow**:
1. User truy cập Grades section
2. System load GradeBook record
3. System hiển thị:
   - Quiz scores (từ Attempt)
   - Assignment scores (từ AssignmentSubmission)
   - Participation scores
   - Weighted total score
   - Letter grade
4. User có thể click vào từng item để xem chi tiết và feedback

---

#### UC-STU-011: Download Certificate
**Actor**: Student
**Description**: Tải chứng chỉ hoàn thành khóa học
**Preconditions**:
- User đã complete course (CourseEnrollment.status = 'COMPLETED')
- Certificate đã được issue
**Postconditions**: Certificate PDF được download
**Main Flow**:
1. User truy cập My Certificates
2. System load danh sách certificates
3. User click "Download PDF"
4. System generate/retrieve PDF
5. Browser download file
6. System log verification activity

**Includes**: View Certificate, Verify Certificate

---

### 3.2 INSTRUCTOR USE CASES

#### UC-INS-001: Create Course
**Actor**: Instructor
**Description**: Tạo khóa học mới
**Preconditions**: User có role INSTRUCTOR hoặc ADMIN
**Postconditions**: Course được tạo với status = 'DRAFT'
**Main Flow**:
1. Instructor click "Create Course"
2. Instructor điền thông tin:
   - Code, Title, Description
   - Category, Difficulty level
   - Estimated hours
   - Thumbnail
3. System validate (code unique)
4. System tạo Course record (status = 'DRAFT', created_by = instructor_id)
5. Instructor được redirect đến course editor

**Alternative Flows**:
- 3a. Code đã tồn tại → Show error

---

#### UC-INS-002: Create Module
**Actor**: Instructor
**Description**: Tạo module/chương trong course
**Preconditions**: User là instructor/creator của course
**Postconditions**: Module được thêm vào course
**Main Flow**:
1. Instructor truy cập course editor
2. Instructor click "Add Module"
3. Instructor điền:
   - Title, Description
   - Order number
   - Prerequisites (optional)
   - Estimated duration
4. System tạo Module record
5. Module xuất hiện trong course structure

---

#### UC-INS-003: Upload Lecture
**Actor**: Instructor
**Description**: Upload bài giảng (video/PDF/slides)
**Preconditions**: User là instructor của course, đã có module
**Postconditions**: Lecture được thêm vào module
**Main Flow**:
1. Instructor chọn module
2. Instructor click "Add Lecture"
3. Instructor chọn type (VIDEO, PDF, SLIDE, AUDIO)
4. Instructor upload file
5. System upload file lên storage (S3/Azure/Local)
6. Nếu video: System xử lý encoding (background job)
7. System tạo Lecture record (content_url)
8. Instructor thêm thông tin:
   - Title, Description
   - Duration
   - Is preview, Is downloadable
   - Transcript (optional)
9. System save lecture

**Alternative Flows**:
- 4a. File quá lớn → Show error
- 6a. Video encoding failed → Retry hoặc notify admin

---

#### UC-INS-004: Create Quiz
**Actor**: Instructor
**Description**: Tạo bài kiểm tra
**Preconditions**: User là instructor của course
**Postconditions**: Quiz được tạo (chưa published)
**Main Flow**:
1. Instructor click "Create Quiz"
2. Instructor điền thông tin:
   - Title, Description
   - Time limit
   - Attempt limit
   - Pass score
   - Shuffle questions/options
   - Show correct answers settings
3. System tạo Quiz record (is_published = FALSE)
4. Instructor thêm questions (UC-INS-005)

---

#### UC-INS-005: Create Question
**Actor**: Instructor
**Description**: Tạo câu hỏi cho quiz hoặc question bank
**Preconditions**: User là instructor của course
**Postconditions**: Question được tạo
**Main Flow**:
1. Instructor chọn question type (MCQ, TRUE_FALSE, ESSAY, SHORT_ANSWER, CODE)
2. Instructor điền:
   - Question text
   - Difficulty level
   - Max points
   - Explanation
3. Nếu MCQ/TRUE_FALSE:
   - Instructor thêm options
   - Mark correct answer(s)
   - Thêm feedback cho mỗi option (optional)
4. System tạo Question và Options
5. Question được thêm vào question bank

---

#### UC-INS-006: Create Assignment
**Actor**: Instructor
**Description**: Tạo bài tập cho học viên
**Preconditions**: User là instructor của course
**Postconditions**: Assignment được tạo
**Main Flow**:
1. Instructor click "Create Assignment"
2. Instructor điền thông tin:
   - Title, Description, Instructions
   - Assignment type (ESSAY, CODE, FILE_UPLOAD, PROBLEM_SET, PROJECT)
   - Max points
   - Due date
   - Late submission policy (allow/penalty/max days)
   - Allow resubmission (yes/no, max submissions)
3. Nếu có rubric:
   - Instructor tạo rubric criteria (JSON)
4. Nếu CODE type và auto-grading:
   - Instructor thêm test cases
5. System tạo Assignment record
6. Assignment được assign đến course hoặc specific class

**Alternative Flows**:
- 4a. Test cases invalid → Show validation error

---

#### UC-INS-007: Grade Assignment
**Actor**: Instructor hoặc TA
**Description**: Chấm bài tập của học viên
**Preconditions**:
- User là instructor hoặc TA của course
- Assignment đã có submissions
**Postconditions**:
- AssignmentSubmission.status = 'GRADED'
- Score và feedback được lưu
**Main Flow**:
1. Instructor truy cập "Grade Submissions"
2. System load danh sách submissions (status = 'SUBMITTED')
3. Instructor chọn một submission
4. Instructor xem:
   - Student info
   - Submission content/files
   - Rubric criteria
5. Instructor điền:
   - Rubric scores (JSON) hoặc manual_score
   - Feedback text
6. System tính final_score (manual_score - penalty_applied)
7. Instructor click "Save Grade"
8. System update AssignmentSubmission:
   - manual_score, final_score, feedback
   - status = 'GRADED'
   - graded_at, graded_by
9. System update GradeBook
10. System gửi notification đến student

**Includes**: Update GradeBook (UC-SYS-004)

---

#### UC-INS-008: Grade Quiz (Essay Questions)
**Actor**: Instructor hoặc TA
**Description**: Chấm câu hỏi tự luận trong quiz
**Preconditions**: Quiz có essay questions, đã có attempts submitted
**Postconditions**: QuizSubmission được chấm, Attempt score updated
**Main Flow**:
1. Instructor truy cập "Grade Quizzes"
2. System filter attempts có essay questions chưa chấm
3. Instructor chọn attempt
4. Instructor xem từng essay question
5. Instructor điền manual_score và feedback
6. System save QuizSubmission
7. System recalculate Attempt.final_score (auto_score + manual_score)
8. System update Attempt.status = 'GRADED'
9. System gửi notification đến student

**Includes**: Update GradeBook (UC-SYS-004)

---

#### UC-INS-009: Publish Course
**Actor**: Instructor
**Description**: Publish course để học viên có thể enroll
**Preconditions**:
- User là instructor của course
- Course có ít nhất 1 module với lectures
- Course status = 'DRAFT'
**Postconditions**: Course.status = 'PUBLISHED', học viên có thể enroll
**Main Flow**:
1. Instructor click "Publish Course"
2. System validate:
   - Có modules và lectures
   - Thông tin course đầy đủ
3. System update Course.status = 'PUBLISHED'
4. System set published_at = CURRENT_TIMESTAMP
5. Course xuất hiện trong catalog

**Alternative Flows**:
- 2a. Validation failed → Show errors, cannot publish

---

#### UC-INS-010: Manage Class
**Actor**: Instructor
**Description**: Tạo và quản lý lớp học trực tiếp (blended learning)
**Preconditions**: User là instructor, đã có course
**Postconditions**: Class được tạo, có lịch học
**Main Flow**:
1. Instructor click "Create Class"
2. Instructor điền:
   - Class name
   - Link to course
   - Start/End date
   - Max students
   - Location
3. System tạo Class record
4. Instructor tạo Schedule (UC-INS-011)
5. Instructor invite students (manual hoặc allow self-enroll)

---

#### UC-INS-011: Create Schedule
**Actor**: Instructor
**Description**: Tạo lịch học cho Class
**Preconditions**: User là instructor của class
**Postconditions**: Schedule được tạo
**Main Flow**:
1. Instructor chọn class
2. Instructor click "Add Schedule"
3. Instructor điền:
   - Session date, start time, end time
   - Location (hoặc meeting URL nếu online)
   - Topic/description
   - Session type (IN_PERSON, ONLINE, HYBRID)
4. System tạo Schedule record
5. Schedule xuất hiện trong class calendar
6. System gửi notification đến enrolled students

---

#### UC-INS-012: Take Attendance
**Actor**: Instructor hoặc TA
**Description**: Điểm danh cho buổi học trực tiếp
**Preconditions**: Class có schedule, đã đến session_date
**Postconditions**: Attendance records được tạo
**Main Flow**:
1. Instructor truy cập schedule
2. System load danh sách enrolled students
3. Instructor mark từng student:
   - PRESENT, ABSENT, LATE, EXCUSED
4. System tạo/update Attendance records
5. Attendance data được lưu

---

#### UC-INS-013: View Class Analytics
**Actor**: Instructor
**Description**: Xem thống kê lớp học
**Preconditions**: User là instructor của course/class
**Postconditions**: Dashboard hiển thị analytics
**Main Flow**:
1. Instructor truy cập Analytics
2. System tính toán:
   - Enrollment count
   - Completion rate
   - Average quiz scores
   - Average assignment scores
   - Attendance rate (for classes)
   - Student engagement metrics
3. System hiển thị charts và tables
4. Instructor có thể export reports

---

### 3.3 ADMIN USE CASES

#### UC-ADM-001: Manage Users
**Actor**: Admin
**Description**: Quản lý tài khoản người dùng
**Preconditions**: User có role ADMIN
**Postconditions**: User account được quản lý
**Main Flow**:
1. Admin truy cập User Management
2. Admin có thể:
   - View danh sách users
   - Search/Filter users
   - Edit user info
   - Assign/Remove roles
   - Suspend/Activate accounts
   - Reset passwords
3. System thực hiện action
4. System log activity (ActivityLog)

---

#### UC-ADM-002: Manage Roles
**Actor**: Admin
**Description**: Quản lý roles và permissions
**Preconditions**: User có role ADMIN
**Postconditions**: Roles/Permissions được cập nhật
**Main Flow**:
1. Admin truy cập Role Management
2. Admin có thể:
   - Create new role
   - Edit role permissions (JSON)
   - Delete custom roles (không delete system roles)
3. System validate và save
4. Changes apply immediately

---

#### UC-ADM-003: Manage Certificate Templates
**Actor**: Admin
**Description**: Quản lý mẫu chứng chỉ
**Preconditions**: User có role ADMIN
**Postconditions**: Template được tạo/cập nhật
**Main Flow**:
1. Admin truy cập Certificate Templates
2. Admin click "Create Template"
3. Admin điền:
   - Name, Description
   - Background image
   - Layout config (JSON: positions, fonts, colors)
   - HTML template
4. Admin preview template
5. System save template
6. Template có thể set là default

---

#### UC-ADM-004: System Settings
**Actor**: Admin
**Description**: Cấu hình hệ thống
**Preconditions**: User có role ADMIN
**Postconditions**: Settings được update
**Main Flow**:
1. Admin truy cập System Settings
2. Admin xem/sửa settings:
   - max_upload_size_mb
   - session_timeout_minutes
   - allow_self_registration
   - default_quiz_time_limit
   - email_smtp_config
   - storage_config
3. System validate values
4. System save settings
5. Changes apply (may need restart for some settings)

---

#### UC-ADM-005: View Activity Logs
**Actor**: Admin
**Description**: Xem nhật ký hoạt động hệ thống
**Preconditions**: User có role ADMIN
**Postconditions**: Logs được hiển thị
**Main Flow**:
1. Admin truy cập Activity Logs
2. Admin filter:
   - User
   - Action type
   - Entity type
   - Date range
3. System query ActivityLog table
4. System hiển thị results (with pagination)
5. Admin có thể export logs

---

#### UC-ADM-006: Manage Files
**Actor**: Admin
**Description**: Quản lý files đã upload
**Preconditions**: User có role ADMIN
**Postconditions**: Files được quản lý
**Main Flow**:
1. Admin truy cập File Management
2. System hiển thị files (with storage info)
3. Admin có thể:
   - View file details
   - Delete unused files
   - View storage usage
4. System thực hiện actions
5. System update File table

---

### 3.4 SYSTEM USE CASES (Automated)

#### UC-SYS-001: Auto Issue Certificate
**Actor**: System
**Description**: Tự động cấp chứng chỉ khi học viên hoàn thành khóa học
**Trigger**: CourseEnrollment.status = 'COMPLETED'
**Postconditions**: Certificate được tạo, notification gửi
**Main Flow**:
1. Trigger: CourseEnrollment updated to 'COMPLETED'
2. System check criteria:
   - All required modules completed
   - All quizzes passed (score >= pass_score)
   - All assignments submitted và graded
   - Final grade >= course requirement
3. System generate certificate:
   - Create unique certificate_code
   - Create unique verification_code
   - Get default CertificateTemplate
   - Generate PDF from template
   - Upload PDF to storage
   - Generate QR code with verification_url
4. System tạo Certificate record
5. System gửi notification (email + in-app)
6. Student có thể download certificate

**Alternative Flows**:
- 2a. Criteria not met → Do nothing (wait)
- 3a. PDF generation failed → Retry or notify admin

---

#### UC-SYS-002: Track Progress
**Actor**: System
**Description**: Tự động cập nhật tiến độ học tập
**Trigger**: User interaction (watch lecture, complete quiz, submit assignment)
**Postconditions**: Progress và GradeBook được update
**Main Flow**:
1. Trigger: User completes an activity
2. System update/create Progress record:
   - lecture completed: Progress.status = 'COMPLETED', percent_complete = 100
   - quiz completed: Create Progress for quiz
   - assignment submitted: Create Progress for assignment
3. System aggregate progress at module level:
   - Calculate module completion % based on lectures/quizzes/assignments
4. System aggregate progress at course level:
   - Calculate course completion % based on modules
5. System update CourseEnrollment.completion_percentage
6. If completion_percentage = 100% → Trigger UC-SYS-001 (Auto Issue Certificate)

---

#### UC-SYS-003: Auto Grade Quiz
**Actor**: System
**Description**: Tự động chấm câu hỏi trắc nghiệm
**Trigger**: Attempt submitted
**Postconditions**: MCQ/TRUE_FALSE questions được chấm
**Main Flow**:
1. Trigger: Attempt.status = 'SUBMITTED'
2. System load all QuizSubmissions for this attempt
3. For each submission where question type is MCQ or TRUE_FALSE:
   a. Load correct option_ids from Options (is_correct = TRUE)
   b. Compare with QuizSubmission.selected_option_ids
   c. If match → auto_score = max_points
   d. Else → auto_score = 0
   e. Set auto_feedback
4. System sum auto_scores → Attempt.auto_score
5. System check nếu có essay questions:
   - Yes → Attempt.status = 'SUBMITTED' (chờ manual grading)
   - No → Attempt.status = 'GRADED', final_score = auto_score
6. System update GradeBook (UC-SYS-004)
7. System gửi notification nếu fully graded

---

#### UC-SYS-004: Update GradeBook
**Actor**: System
**Description**: Tự động cập nhật sổ điểm tổng hợp
**Trigger**: Quiz graded hoặc Assignment graded
**Postconditions**: GradeBook được update
**Main Flow**:
1. Trigger: Score updated (Attempt or AssignmentSubmission)
2. System find/create GradeBook record (user_id, course_id, class_id)
3. System calculate:
   - quiz_score = AVG(Attempt.percentage_score) hoặc SUM based on policy
   - assignment_score = AVG(AssignmentSubmission.final_score)
   - participation_score = from Attendance + Discussion (if any)
4. System calculate weighted_score based on course policy:
   - E.g., quiz 40%, assignment 50%, participation 10%
5. System determine letter_grade based on weighted_score
6. System update GradeBook
7. System update CourseEnrollment.final_grade
8. If criteria met → Trigger UC-SYS-001 (certificate)

---

#### UC-SYS-005: Send Notifications
**Actor**: System
**Description**: Gửi thông báo tự động
**Trigger**: Various events
**Postconditions**: Notification được gửi qua channels (email, push, in-app)
**Main Flow**:
1. Trigger: Event occurs (assignment due, grade published, etc.)
2. System create Notification record:
   - notification_type
   - title, message
   - related_entity_type, related_entity_id
   - action_url
3. System check NotificationPreference của user:
   - email_enabled?
   - push_enabled?
   - frequency (IMMEDIATE, DAILY_DIGEST, WEEKLY_DIGEST)
4. If IMMEDIATE:
   a. System gửi qua enabled channels
   b. Log to NotificationLog
5. If DIGEST:
   a. Queue notification
   b. Send batch vào scheduled time
6. System mark is_read = FALSE
7. User xem notification và mark as read

**Event Types**:
- ASSIGNMENT_DUE (2 days before, 1 day before)
- GRADE_PUBLISHED (quiz/assignment graded)
- CERTIFICATE_ISSUED
- COURSE_UPDATE
- CLASS_REMINDER (before scheduled session)
- ENROLLMENT_CONFIRMED

---

#### UC-SYS-006: Clean Up Expired Data
**Actor**: System
**Description**: Dọn dẹp dữ liệu hết hạn
**Trigger**: Daily cron job
**Postconditions**: Expired data được xử lý
**Main Flow**:
1. Trigger: Scheduled job (daily at 2 AM)
2. System check:
   - Expired notifications (expires_at < NOW) → Delete
   - Expired sessions → Delete
   - Old activity logs (> 1 year) → Archive hoặc delete
   - Unused files (is_deleted = TRUE && deleted_at < 30 days ago) → Delete from storage
3. System execute cleanup operations
4. System log summary to ActivityLog

---

## 4. USE CASE RELATIONSHIPS

### 4.1 Include Relationships
- UC-STU-005 (Watch Lecture) **includes** UC-SYS-002 (Track Progress)
- UC-STU-007 (Take Quiz) **includes** UC-SYS-003 (Auto Grade Quiz)
- UC-INS-007 (Grade Assignment) **includes** UC-SYS-004 (Update GradeBook)
- UC-INS-008 (Grade Quiz) **includes** UC-SYS-004 (Update GradeBook)

### 4.2 Extend Relationships
- UC-STU-004 (Enroll in Course) **extends** UC-STU-003 (Browse Courses)
- UC-STU-006 (Download Resources) **extends** UC-STU-005 (Watch Lecture)
- UC-STU-011 (Download Certificate) **extends** UC-STU-010 (View Grades)

### 4.3 Generalization
- Student, Instructor, TA, Admin **are types of** User (authentication/authorization)

---

## 5. BUSINESS RULES

### 5.1 Enrollment Rules
1. User phải verify email trước khi enroll courses
2. User chỉ có thể enroll course có status = 'PUBLISHED'
3. User không thể enroll cùng course 2 lần (trừ khi dropped trước đó)
4. Prerequisites phải hoàn thành trước khi enroll course cao hơn

### 5.2 Progress Rules
1. Lectures phải xem tuần tự theo order_num (nếu có prerequisites)
2. Quiz phải unlock sau khi complete lectures required
3. Assignment phải complete trước khi issue certificate
4. Completion percentage tính dựa trên: lectures (40%), quizzes (30%), assignments (30%)

### 5.3 Assessment Rules
1. Quiz attempt_limit: Nếu NULL = unlimited, else max N attempts
2. Quiz time_limit: Nếu NULL = no limit, else auto-submit khi hết giờ
3. Quiz pass_score: Phải đạt >= pass_score để consider "passed"
4. Assignment late submission:
   - Nếu allow = TRUE → accept với penalty
   - penalty = late_penalty_percent * max_points * days_late (capped at max_late_days)
5. Assignment resubmission:
   - Nếu allow = TRUE và submission_number < max_submissions → allow resubmit
   - Instructor grade submission mới nhất

### 5.4 Grading Rules
1. Quiz auto_score: MCQ và TRUE_FALSE tự động chấm
2. Quiz manual_score: Essay, SHORT_ANSWER, CODE chờ manual grading
3. Assignment với rubric: Score tính từ rubric_scores (sum of criteria)
4. GradeBook weighted score:
   - Quizzes: 40%
   - Assignments: 50%
   - Participation: 10%
5. Letter grade mapping:
   - A: 90-100
   - B: 80-89
   - C: 70-79
   - D: 60-69
   - F: <60

### 5.5 Certificate Rules
1. Criteria to issue certificate:
   - CourseEnrollment.completion_percentage = 100%
   - CourseEnrollment.final_grade >= course requirement (e.g., 70%)
   - All required quizzes passed
   - All required assignments submitted và graded
2. Certificate verification:
   - verification_code valid
   - Certificate.status = 'ACTIVE'
   - Not expired (nếu có valid_until)
3. Certificate revocation:
   - Admin có thể revoke với lý do
   - Revoked certificates không verify được

### 5.6 Notification Rules
1. Notification frequency:
   - IMMEDIATE: Gửi ngay
   - DAILY_DIGEST: Gộp và gửi 1 lần/ngày (8 AM)
   - WEEKLY_DIGEST: Gộp và gửi 1 lần/tuần (Monday 8 AM)
   - NEVER: Không gửi
2. Notification channels:
   - Email: Always for important events (certificate, grade)
   - Push: Optional, user preference
   - In-app: Always

---

## 6. NON-FUNCTIONAL REQUIREMENTS

### 6.1 Performance
- Page load time < 2s
- Video streaming smooth (adaptive bitrate)
- Quiz submission response < 500ms
- Notification delivery < 5s

### 6.2 Scalability
- Support 10,000+ concurrent users
- Support 1,000+ courses
- Support 100,000+ students

### 6.3 Security
- HTTPS for all connections
- Password hashing (bcrypt/argon2)
- JWT for authentication
- RBAC for authorization
- CSRF protection
- XSS protection
- SQL injection prevention

### 6.4 Availability
- 99.9% uptime
- Database backup daily
- Disaster recovery plan

### 6.5 Usability
- Mobile responsive (support phone, tablet)
- Multi-language support (vi, en)
- Accessibility (WCAG 2.1 Level AA)

---

## 7. SEQUENCE DIAGRAMS (High-Level)

### 7.1 Student Takes Quiz

```
Student → System: Start Quiz
System → Database: Create Attempt (IN_PROGRESS)
System → Student: Show Questions
Student → System: Submit Answers
System → Database: Save QuizSubmissions
System → Database: Update Attempt (SUBMITTED)
System → Auto-Grader: Grade MCQ
Auto-Grader → Database: Update scores
Auto-Grader → System: Grading complete
System → Student: Show Results
System → Notification: Send grade notification
```

### 7.2 Instructor Grades Assignment

```
Instructor → System: View Submissions
System → Database: Query AssignmentSubmissions
Database → System: Return submissions
System → Instructor: Display list
Instructor → System: Select submission
Instructor → System: Enter score & feedback
System → Database: Update AssignmentSubmission
System → GradeBook: Update gradebook
System → Notification: Notify student
Notification → Student: Email & in-app
```

### 7.3 System Auto-Issues Certificate

```
System → Trigger: CourseEnrollment.status = COMPLETED
System → Database: Check criteria (progress, grades)
Database → System: Criteria met
System → Certificate Generator: Generate certificate
Certificate Generator → Template: Use default template
Certificate Generator → PDF Generator: Create PDF
PDF Generator → Storage: Upload PDF
Storage → System: Return PDF URL
System → Database: Create Certificate record
System → QR Generator: Generate QR code
QR Generator → Storage: Upload QR image
System → Notification: Send certificate issued
Notification → Student: Email with download link
```

---

## 8. STATE DIAGRAMS

### 8.1 Course Status States
```
DRAFT → PUBLISHED → ARCHIVED
  ↑         ↓
  ←─────────┘ (Unpublish)
```

### 8.2 Assignment Submission Status States
```
DRAFT → SUBMITTED → GRADING → GRADED → RETURNED
  ↓                              ↓
  └──────────────────────────────┘ (Resubmit if allowed)
```

### 8.3 Attempt Status States
```
IN_PROGRESS → SUBMITTED → GRADED
     ↓
  ABANDONED (if timeout/leave)
```

### 8.4 Certificate Status States
```
ACTIVE → REVOKED
  ↓
EXPIRED (if valid_until passed)
```

---

## 9. SUMMARY

### 9.1 Total Use Cases: 43

| Actor | Count |
|-------|-------|
| Student | 11 |
| Instructor | 13 |
| Admin | 6 |
| TA | 2 (share with Instructor) |
| System | 6 |

### 9.2 Key Features
- ✅ Complete course management
- ✅ Assignment-based assessment (replacing submission)
- ✅ Quiz with auto-grading
- ✅ Progress tracking (lecture, module, quiz, assignment)
- ✅ Blended learning (online + in-person class)
- ✅ Automated certificate issuance
- ✅ Multi-channel notifications
- ✅ Rubric-based grading
- ✅ Late submission handling
- ✅ Comprehensive analytics

### 9.3 Removed from v1.0
- ❌ Forum/Discussion threads
- ❌ Post/Reply system
- ❌ Vote/Like features

---

## 10. REFERENCES

- req-1.md - Main plan task
- DATABASE-DESIGN-EVALUATION.md - Design analysis
- ERD-SPEC.md - Entity specifications
- FUNCTIONAL-REQUIREMENTS.md - Detailed requirements

---

**Document Status**: ✅ Complete
**Next Steps**: Create ERD-SPEC.md and DATABASE-SCHEMA.md
**Date**: 2025-11-25
