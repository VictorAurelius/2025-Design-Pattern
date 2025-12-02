TRƯỜNG ĐẠI HỌC GIAO THÔNG VẬN TẢI
KHOA: CÔNG NGHỆ THÔNG TIN

[Logo trường]

1.1. Giao diện Page và thành phần

- Tên Page: Course Management Page (Web Application)

- Mục tiêu: Cung cấp giao diện cho Instructor/ADMIN để tạo, xem, chỉnh sửa, xuất bản và xóa khóa học (Course-level only).

- Bố cục chính:
  • Canvas chính (Chi tiết khóa học): biểu mẫu hiển thị các trường thông tin khóa học và cho phép tạo/cập nhật.
  • Action Bar: chứa các nút `Create`, `Save`, `Publish`, `Delete`, `Cancel`.
  • Dialog/Modal: xác nhận hành động (ví dụ: xóa), hiển thị lỗi hoặc thành công.

- Chi tiết thành phần (Course-level):
  • ID Khóa học: `Text (Readonly)` → `course_id` (UUID, auto-generated).
  • Mã khóa học: `Text Input` → `code` (VARCHAR(50), unique).
  • Tên khóa học: `Text Input` → `title` (VARCHAR(200)).
  • Mô tả: `Text Area (Rich Text Editor)` → `description` (TEXT, hỗ trợ HTML).
  • Độ khó: `Dropdown` → `difficulty_level` (BEGINNER, INTERMEDIATE, ADVANCED).
  • Hình đại diện: `File Upload` → `thumbnail_url` (URL lưu trữ trên S3/GCS).
  • Trạng thái: `Dropdown` → `status` (DRAFT, PUBLISHED, ARCHIVED).
  • Giảng viên (Created by): `Text (Readonly)` → hiển thị tên giảng viên (từ `User`).
  • Thao tác: `Create` (POST), `Save`/`Update` (PUT), `Publish` (POST /publish), `Delete` (DELETE).

--------------------------------------------------------------------------------

1.2. Truy vấn dữ liệu

- Hiển thị chi tiết Course
  • Bảng: Course
  • Trường: `course_id`, `code`, `title`, `description`, `difficulty_level`, `status`, `thumbnail_url`, `created_by`, `created_at`, `updated_at`.
  • API: `GET /api/courses/{course_id}` — trả về thông tin đầy đủ của khóa học.

- Danh sách khóa học (Instructor/ADMIN)
  • API: `GET /api/courses` — hỗ trợ lọc theo `created_by`, `status`, `search` (title/code), pagination.

- Thông tin Instructor
  • API: `GET /api/users/{user_id}` — hoặc trả kèm trong response `GET /api/courses/{course_id}` dưới dạng embedded author info.

--------------------------------------------------------------------------------

1.3. Cập nhật dữ liệu

- Tạo khóa học
  • API: `POST /api/courses`
  • Payload: `{ code, title, description, difficulty_level, thumbnail_url, created_by }`
  • SQL tương đương:
    INSERT INTO "Course" (course_id, code, title, description, difficulty_level, status, thumbnail_url, created_by, created_at, updated_at)
    VALUES (gen_random_uuid(), :code, :title, :description, :difficulty_level, 'DRAFT', :thumbnail_url, :created_by, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
  • Response: HTTP 201 Created + Course data.

- Cập nhật khóa học
  • API: `PUT /api/courses/{course_id}`
  • Payload: `{ code, title, description, difficulty_level, thumbnail_url }`
  • SQL tương đương:
    UPDATE "Course"
    SET code = :code, title = :title, description = :description,
        difficulty_level = :difficulty_level, thumbnail_url = :thumbnail_url,
        updated_at = CURRENT_TIMESTAMP
    WHERE course_id = :course_id AND created_by = :instructor_id;
  • Response: HTTP 200 OK + updated Course data.

- Xuất bản khóa học
  • API: `POST /api/courses/{course_id}/publish`
  • Kiểm tra tiền điều kiện: hiện trạng `status = 'DRAFT'` và user có quyền (creator hoặc ADMIN).
  • SQL tương đương:
    UPDATE "Course"
    SET status = 'PUBLISHED', updated_at = CURRENT_TIMESTAMP
    WHERE course_id = :course_id AND created_by = :instructor_id AND status = 'DRAFT';
  • Response: HTTP 200 OK + Course data với `status = 'PUBLISHED'`.

- Xóa khóa học (recommend soft-delete)
  • API: `DELETE /api/courses/{course_id}`
  • Hai lựa chọn thiết kế:
    1. Soft-delete (recommended):
       UPDATE "Course" SET status = 'ARCHIVED', updated_at = CURRENT_TIMESTAMP WHERE course_id = :course_id AND (created_by = :instructor_id OR :is_admin = TRUE);
       Response: HTTP 200 OK.
    2. Physical delete (cần quyền cao và xác nhận):
       DELETE FROM "Course" WHERE course_id = :course_id AND (created_by = :instructor_id OR :is_admin = TRUE);
       Response: HTTP 204 No Content.

  • Lưu ý nghiệp vụ: trước khi xóa vật lý, kiểm tra điều kiện (ví dụ: không có enrollment active), hoặc bắt buộc archive nếu có liên kết quan trọng.

  • Mô tả:
    o Tạo khóa học: Nhập thông tin Course (code, title, description, difficulty_level). Trạng thái mặc định là DRAFT.
    o Cập nhật khóa học: Chỉnh sửa thông tin Course, thay đổi trạng thái (DRAFT → PUBLISHED → ARCHIVED).
    o Xuất bản khóa học: Thay đổi status thành PUBLISHED để sinh viên có thể đăng ký.
    o Xóa khóa học: Xóa Course (CASCADE sẽ xóa tất cả Module, Lecture liên quan).
    o Tạo Module: Tạo Module thuộc Course, thiết lập order_num và prerequisite_module_ids.
    o Tạo Lecture: Tạo Lecture trong Module với type (VIDEO, PDF, SLIDE, AUDIO, TEXT, ASSIGNMENT).
    o Upload tài liệu: Upload file Resource và lưu file_url, file_type, file_size_bytes.
  • Kết quả: Nội dung khóa học được tổ chức rõ ràng theo cấu trúc Course → Module → Lecture.

- Quản lý Đánh giá:
  • Mục đích: Cho phép giảng viên tạo Quiz, Question và chấm điểm sinh viên.
  • Ngữ cảnh: Sử dụng để đánh giá kết quả học tập của sinh viên.
  • Điều kiện tiên quyết: Giảng viên đã tạo Course.
  • Mô tả:
    o Tạo Quiz: Tạo Quiz với questions (JSON array chứa question_id, points, order), thiết lập passing_score, duration_minutes, max_attempts.
    o Tạo Question: Tạo Question với type (MCQ, TRUE_FALSE, ESSAY, SHORT_ANSWER), question_text, points.
    o Tạo Option: Tạo Option cho Question với option_text, is_correct, order_num.
    o Chấm điểm Assignment: Xem AssignmentSubmission, nhập score và feedback.
    o Chấm điểm Quiz: Chấm điểm ESSAY/SHORT_ANSWER trong Attempt.answers (JSON).
    o Xem báo cáo: Xem thống kê điểm số, tỷ lệ đạt, thời gian làm bài.
  • Kết quả: Quiz và Question được tạo. Sinh viên được chấm điểm và nhận feedback.

- Quản lý Lớp học & Sinh viên:
  • Mục đích: Theo dõi sinh viên đã đăng ký, xem tiến độ và cấp chứng chỉ.
  • Ngữ cảnh: Sử dụng để quản lý Enrollment và Progress của sinh viên.
  • Điều kiện tiên quyết: Có sinh viên đã enroll vào khóa học.
  • Mô tả:
    o Tạo Class: Tạo Class với class_name, start_date, end_date, max_students, schedules (JSON).
    o Xem danh sách Enrollment: Xem tất cả sinh viên đã đăng ký khóa học, lọc theo class_id.
    o Theo dõi tiến độ: Xem Progress của từng sinh viên theo Module (status: NOT_STARTED, IN_PROGRESS, COMPLETED).
    o Cấp Certificate: Khi sinh viên hoàn thành khóa học, hệ thống tạo Certificate với certificate_code, verification_code.
  • Kết quả: Giảng viên nắm rõ tình hình học tập của từng sinh viên. Sinh viên nhận được Certificate khi hoàn thành.

--------------------------------------------------------------------------------

2.2. Sơ đồ phân rã chức năng dưới góc nhìn của Student (Submission Management)

2.2.1. Sơ đồ phân rã chức năng

[Hình 2: Sơ đồ phân rã chức năng - Student]

Đặc tả chức năng:

- Quản lý Tài khoản:
  • Mục đích: Cho phép sinh viên tạo tài khoản, đăng nhập và quản lý thông tin cá nhân.
  • Ngữ cảnh: Sử dụng bởi sinh viên trên nền tảng web.
  • Điều kiện tiên quyết: Thiết bị đã kết nối Internet.
  • Mô tả:
    o Đăng ký: Sinh viên cung cấp email, password, first_name, last_name. Hệ thống tạo User với role STUDENT, account_status = PENDING_VERIFICATION.
    o Xác thực email: Click link xác thực, account_status chuyển thành ACTIVE.
    o Đăng nhập: Nhập email và password để truy cập hệ thống.
    o Cập nhật thông tin: Cập nhật first_name, last_name, preferences (notifications, locale, timezone, theme).
    o Đăng xuất: Thoát khỏi hệ thống.
  • Kết quả: Sinh viên có tài khoản và có thể truy cập các chức năng học tập.

- Đăng ký & Học tập:
  • Mục đích: Cho phép sinh viên đăng ký khóa học và học các Lecture.
  • Ngữ cảnh: Sinh viên tìm và đăng ký khóa học quan tâm, sau đó học tập.
  • Điều kiện tiên quyết: Sinh viên đã đăng nhập. Khóa học có status = PUBLISHED.
  • Mô tả:
    o Tìm kiếm khóa học: Tìm Course theo title, category, difficulty_level.
    o Đăng ký khóa học: Tạo Enrollment với user_id, course_id, class_id (nullable cho self-paced).
    o Xem danh sách Module: Xem tất cả Module trong Course theo order_num.
    o Học Lecture: Xem Lecture.content, video (file_url từ Resource), download tài liệu.
    o Đánh dấu hoàn thành: Cập nhật Progress với status = COMPLETED khi hoàn thành Module.
  • Kết quả: Sinh viên đã enroll và có thể học Lecture. Progress được tracking.

- Làm bài tập & Kiểm tra:
  • Mục đích: Cho phép sinh viên làm Assignment và Quiz.
  • Ngữ cảnh: Sinh viên làm bài tập và kiểm tra để đánh giá kiến thức.
  • Điều kiện tiên quyết: Sinh viên đã enroll khóa học.
  • Mô tả:
    o Xem Assignment: Xem Lecture với type = ASSIGNMENT, đọc assignment_config (JSON) để biết max_points, due_date, instructions.
    o Nộp bài tập: Tạo AssignmentSubmission với file_urls (JSON array của S3/GCS URLs), text_content.
    o Làm Quiz: Tạo Attempt với answers (JSON array: question_id, answer_text, selected_options).
    o Xem điểm và feedback: Xem AssignmentSubmission.score, feedback và Attempt.score sau khi giảng viên chấm.
  • Kết quả: Bài tập và quiz được nộp thành công. Sinh viên nhận điểm và feedback.

- Theo dõi Tiến độ:
  • Mục đích: Cho phép sinh viên theo dõi tiến độ học tập của mình.
  • Ngữ cảnh: Sinh viên muốn biết đã hoàn thành bao nhiêu % khóa học.
  • Điều kiện tiên quyết: Sinh viên đã enroll khóa học.
  • Mô tả:
    o Xem tiến độ Module: Xem Progress cho từng Module (NOT_STARTED, IN_PROGRESS, COMPLETED).
    o Xem điểm số: Xem danh sách AssignmentSubmission và Attempt với điểm số.
    o Tính completion: Hệ thống tính tỷ lệ hoàn thành dựa trên Progress của các Module.
    o Tải Certificate: Khi hoàn thành khóa học, tải Certificate với certificate_code để verify.
  • Kết quả: Sinh viên nắm rõ tiến độ học tập của mình. Nhận được Certificate khi hoàn thành.

================================================================================

--------------------------------------------------------------------------------
3. Mô hình hóa dữ liệu
--------------------------------------------------------------------------------

3.1. Sơ đồ ER

[Hình 3: Sơ đồ ER - Tổng quan hệ thống B-Learning Core]

Mô tả sơ đồ ER:

Hệ thống gồm 16 entities được chia thành 5 domains:

- Domain 1: User Management
  • User (1) --- (*) UserRole (1) --- (*) Role
  • Quan hệ Many-to-Many giữa User và Role thông qua UserRole

- Domain 2: Course Content
  • Course (1) --- (*) Module (1) --- (*) Lecture (1) --- (*) Resource
  • Quan hệ 1-N với ON DELETE CASCADE

- Domain 3: Assessment
  • Course (1) --- (*) Quiz
  • Course (1) --- (*) Question (1) --- (*) Option
  • Quiz (*) --- (1) Attempt (*) --- (1) Enrollment
  • Lecture (ASSIGNMENT) (1) --- (*) AssignmentSubmission (*) --- (1) Enrollment

- Domain 4: Enrollment & Progress
  • User (1) --- (*) Enrollment (*) --- (1) Course
  • Enrollment (1) --- (*) Progress (*) --- (1) Module

- Domain 5: Class & Certificate
  • Course (1) --- (*) Class (1) --- (*) Enrollment (nullable)
  • User (1) --- (*) Certificate (*) --- (1) Course

--------------------------------------------------------------------------------

3.2. Sơ đồ RM (Relational Model)

[Hình 4: Sơ đồ RM - Quan hệ giữa các bảng]

Mô tả sơ đồ RM:

USER
• user_id (PK) - UUID
• email - VARCHAR(255) - UNIQUE NOT NULL
• password_hash - VARCHAR(255) - NOT NULL
• first_name - VARCHAR(100) - NOT NULL
• last_name - VARCHAR(100) - NOT NULL
• account_status - VARCHAR(30) - NOT NULL
• preferences - JSONB
• created_at - TIMESTAMPTZ - NOT NULL
• updated_at - TIMESTAMPTZ

ROLE
• role_id (PK) - UUID
• name - VARCHAR(50) - UNIQUE NOT NULL
• permissions - JSONB
• created_at - TIMESTAMPTZ - NOT NULL

USERROLE
• user_role_id (PK) - UUID
• user_id (FK → USER) - UUID - NOT NULL
• role_id (FK → ROLE) - UUID - NOT NULL
• expires_at - TIMESTAMPTZ
• UNIQUE(user_id, role_id)

COURSE
• course_id (PK) - UUID
• code - VARCHAR(50) - UNIQUE NOT NULL
• title - VARCHAR(200) - NOT NULL
• description - TEXT
• difficulty_level - VARCHAR(20) - NOT NULL
• status - VARCHAR(20) - NOT NULL DEFAULT 'DRAFT'
• thumbnail_url - VARCHAR(500)
• created_by (FK → USER) - UUID - NOT NULL
• created_at - TIMESTAMPTZ - NOT NULL
• updated_at - TIMESTAMPTZ

MODULE
• module_id (PK) - UUID
• course_id (FK → COURSE) - UUID - NOT NULL - ON DELETE CASCADE
• title - VARCHAR(200) - NOT NULL
• description - TEXT
• order_num - INT - NOT NULL
• prerequisite_module_ids - UUID[] - DEFAULT '{}'
• UNIQUE(course_id, order_num)
• created_at - TIMESTAMPTZ - NOT NULL

LECTURE
• lecture_id (PK) - UUID
• module_id (FK → MODULE) - UUID - NOT NULL - ON DELETE CASCADE
• title - VARCHAR(200) - NOT NULL
• type - VARCHAR(20) - NOT NULL (VIDEO, PDF, SLIDE, AUDIO, TEXT, ASSIGNMENT)
• content - TEXT
• duration_minutes - INT
• order_num - INT - NOT NULL
• assignment_config - JSONB (chỉ dùng khi type = ASSIGNMENT)
• UNIQUE(module_id, order_num)
• created_at - TIMESTAMPTZ - NOT NULL

RESOURCE
• resource_id (PK) - UUID
• lecture_id (FK → LECTURE) - UUID - NOT NULL - ON DELETE CASCADE
• file_url - VARCHAR(500) - NOT NULL
• file_type - VARCHAR(50) - NOT NULL
• file_size_bytes - BIGINT - NOT NULL
• created_at - TIMESTAMPTZ - NOT NULL

QUIZ
• quiz_id (PK) - UUID
• course_id (FK → COURSE) - UUID - NOT NULL - ON DELETE CASCADE
• title - VARCHAR(200) - NOT NULL
• description - TEXT
• questions - JSONB - NOT NULL (array of {question_id, points, order})
• passing_score - DECIMAL(5,2) - NOT NULL
• duration_minutes - INT
• max_attempts - INT
• shuffle_questions - BOOLEAN - DEFAULT FALSE
• created_by (FK → USER) - UUID - NOT NULL
• created_at - TIMESTAMPTZ - NOT NULL

QUESTION
• question_id (PK) - UUID
• course_id (FK → COURSE) - UUID - NOT NULL - ON DELETE CASCADE
• type - VARCHAR(20) - NOT NULL (MCQ, TRUE_FALSE, ESSAY, SHORT_ANSWER)
• question_text - TEXT - NOT NULL
• points - DECIMAL(5,2) - NOT NULL DEFAULT 1
• explanation - TEXT
• created_by (FK → USER) - UUID - NOT NULL
• created_at - TIMESTAMPTZ - NOT NULL

OPTION
• option_id (PK) - UUID
• question_id (FK → QUESTION) - UUID - NOT NULL - ON DELETE CASCADE
• option_text - TEXT - NOT NULL
• is_correct - BOOLEAN - NOT NULL DEFAULT FALSE
• order_num - INT - NOT NULL
• UNIQUE(question_id, order_num)

ATTEMPT
• attempt_id (PK) - UUID
• quiz_id (FK → QUIZ) - UUID - NOT NULL - ON DELETE CASCADE
• user_id (FK → USER) - UUID - NOT NULL - ON DELETE CASCADE
• enrollment_id (FK → ENROLLMENT) - UUID - NOT NULL - ON DELETE CASCADE
• attempt_number - INT - NOT NULL
• answers - JSONB - NOT NULL (array of {question_id, answer_text, score})
• total_score - DECIMAL(5,2)
• status - VARCHAR(20) - NOT NULL DEFAULT 'IN_PROGRESS'
• started_at - TIMESTAMPTZ - NOT NULL
• submitted_at - TIMESTAMPTZ
• graded_at - TIMESTAMPTZ
• UNIQUE(user_id, quiz_id, attempt_number)

ASSIGNMENTSUBMISSION
• submission_id (PK) - UUID
• lecture_id (FK → LECTURE) - UUID - NOT NULL - ON DELETE CASCADE
• user_id (FK → USER) - UUID - NOT NULL - ON DELETE CASCADE
• enrollment_id (FK → ENROLLMENT) - UUID - NOT NULL - ON DELETE CASCADE
• submission_number - INT - NOT NULL DEFAULT 1
• text_content - TEXT
• file_urls - JSONB (array of S3/GCS URLs)
• score - DECIMAL(5,2)
• feedback - TEXT
• graded_by (FK → USER) - UUID
• submitted_at - TIMESTAMPTZ - NOT NULL
• graded_at - TIMESTAMPTZ
• UNIQUE(lecture_id, user_id, submission_number)

ENROLLMENT
• enrollment_id (PK) - UUID
• user_id (FK → USER) - UUID - NOT NULL - ON DELETE CASCADE
• course_id (FK → COURSE) - UUID - NOT NULL - ON DELETE CASCADE
• class_id (FK → CLASS) - UUID - ON DELETE SET NULL (nullable cho self-paced)
• status - VARCHAR(20) - NOT NULL DEFAULT 'ACTIVE'
• enrolled_at - TIMESTAMPTZ - NOT NULL
• completed_at - TIMESTAMPTZ
• UNIQUE(user_id, course_id, COALESCE(class_id, UUID_NIL))

PROGRESS
• progress_id (PK) - UUID
• user_id (FK → USER) - UUID - NOT NULL - ON DELETE CASCADE
• course_id (FK → COURSE) - UUID - NOT NULL - ON DELETE CASCADE
• module_id (FK → MODULE) - UUID - NOT NULL - ON DELETE CASCADE
• status - VARCHAR(20) - NOT NULL DEFAULT 'NOT_STARTED'
• started_at - TIMESTAMPTZ
• completed_at - TIMESTAMPTZ
• UNIQUE(user_id, course_id, module_id)

CLASS
• class_id (PK) - UUID
• course_id (FK → COURSE) - UUID - NOT NULL - ON DELETE CASCADE
• class_name - VARCHAR(100) - NOT NULL
• instructor_id (FK → USER) - UUID - ON DELETE SET NULL
• start_date - DATE - NOT NULL
• end_date - DATE - NOT NULL
• max_students - INT
• status - VARCHAR(20) - NOT NULL DEFAULT 'SCHEDULED'
• schedules - JSONB (array of {date, start_time, end_time, attendances})
• created_at - TIMESTAMPTZ - NOT NULL

CERTIFICATE
• certificate_id (PK) - UUID
• user_id (FK → USER) - UUID - NOT NULL - ON DELETE CASCADE
• course_id (FK → COURSE) - UUID - NOT NULL - ON DELETE CASCADE
• certificate_code - VARCHAR(50) - UNIQUE NOT NULL
• verification_code - VARCHAR(100) - UNIQUE NOT NULL
• status - VARCHAR(20) - NOT NULL DEFAULT 'ACTIVE'
• issued_at - TIMESTAMPTZ - NOT NULL
• revoked_at - TIMESTAMPTZ
• UNIQUE(user_id, course_id)

--------------------------------------------------------------------------------

3.3. Đặc tả chi tiết cơ sở dữ liệu

3.3.1. Domain 1: User Management

- Bảng: User
  • user_id: PK, UUID, gen_random_uuid(), Khóa chính.
  • email: VARCHAR(255), UNIQUE NOT NULL, Email đăng nhập (dùng để xác thực).
  • password_hash: VARCHAR(255), NOT NULL, Mật khẩu đã mã hóa bcrypt.
  • first_name: VARCHAR(100), NOT NULL, Tên.
  • last_name: VARCHAR(100), NOT NULL, Họ.
  • account_status: VARCHAR(30), NOT NULL DEFAULT 'PENDING_VERIFICATION', Trạng thái tài khoản (PENDING_VERIFICATION, ACTIVE, SUSPENDED, DELETED).
  • preferences: JSONB, Cấu hình cá nhân (notifications, locale, timezone, theme).
  • created_at: TIMESTAMPTZ, NOT NULL DEFAULT CURRENT_TIMESTAMP, Thời gian tạo.
  • updated_at: TIMESTAMPTZ, DEFAULT CURRENT_TIMESTAMP, Thời gian cập nhật cuối.

- Bảng: Role
  • role_id: PK, UUID, Khóa chính.
  • name: VARCHAR(50), UNIQUE NOT NULL, Tên role (STUDENT, INSTRUCTOR, TA, ADMIN).
  • permissions: JSONB, Danh sách quyền hạn dạng JSON array.
  • created_at: TIMESTAMPTZ, NOT NULL DEFAULT CURRENT_TIMESTAMP.

- Bảng: UserRole
  • user_role_id: PK, UUID, Khóa chính.
  • user_id: FK (→ User), UUID, NOT NULL, ON DELETE CASCADE.
  • role_id: FK (→ Role), UUID, NOT NULL, ON DELETE CASCADE.
  • expires_at: TIMESTAMPTZ, Thời hạn role (cho role tạm thời như TA).
  • UNIQUE(user_id, role_id): Một user không có role trùng.

3.3.2. Domain 2: Course Content

- Bảng: Course
  • course_id: PK, UUID, Khóa chính.
  • code: VARCHAR(50), UNIQUE NOT NULL, Mã khóa học (VD: CS101, WEB201).
  • title: VARCHAR(200), NOT NULL, Tên khóa học.
  • description: TEXT, Mô tả chi tiết (tùy chọn).
  • difficulty_level: VARCHAR(20), NOT NULL, Độ khó (BEGINNER, INTERMEDIATE, ADVANCED).
  • status: VARCHAR(20), NOT NULL DEFAULT 'DRAFT', Trạng thái (DRAFT, PUBLISHED, ARCHIVED).
  • thumbnail_url: VARCHAR(500), URL hình đại diện (tùy chọn).
  • created_by: FK (→ User), UUID, NOT NULL, Giảng viên tạo khóa học.
  • created_at: TIMESTAMPTZ, NOT NULL DEFAULT CURRENT_TIMESTAMP.
  • updated_at: TIMESTAMPTZ, DEFAULT CURRENT_TIMESTAMP.

- Bảng: Module
  • module_id: PK, UUID, Khóa chính.
  • course_id: FK (→ Course), UUID, NOT NULL, ON DELETE CASCADE.
  • title: VARCHAR(200), NOT NULL, Tên module.
  • description: TEXT, Mô tả (tùy chọn).
  • order_num: INT, NOT NULL, Thứ tự hiển thị trong khóa học.
  • prerequisite_module_ids: UUID[], DEFAULT '{}', Danh sách module tiên quyết.
  • created_at: TIMESTAMPTZ, NOT NULL DEFAULT CURRENT_TIMESTAMP.
  • UNIQUE(course_id, order_num): Không trùng thứ tự trong cùng khóa học.

- Bảng: Lecture
  • lecture_id: PK, UUID, Khóa chính.
  • module_id: FK (→ Module), UUID, NOT NULL, ON DELETE CASCADE.
  • title: VARCHAR(200), NOT NULL, Tiêu đề bài giảng.
  • type: VARCHAR(20), NOT NULL, Loại nội dung (VIDEO, PDF, SLIDE, AUDIO, TEXT, ASSIGNMENT).
  • content: TEXT, Nội dung text/HTML (tùy chọn).
  • duration_minutes: INT, Thời lượng video (phút, tùy chọn).
  • order_num: INT, NOT NULL, Thứ tự trong module.
  • assignment_config: JSONB, Cấu hình assignment (max_points, due_date, submission_types, allowed_file_types, max_file_size_mb, instructions, rubric). Chỉ dùng khi type = ASSIGNMENT.
  • created_at: TIMESTAMPTZ, NOT NULL DEFAULT CURRENT_TIMESTAMP.
  • UNIQUE(module_id, order_num): Không trùng thứ tự trong cùng module.

- Bảng: Resource
  • resource_id: PK, UUID, Khóa chính.
  • lecture_id: FK (→ Lecture), UUID, NOT NULL, ON DELETE CASCADE.
  • file_url: VARCHAR(500), NOT NULL, URL file trên S3/GCS.
  • file_type: VARCHAR(50), NOT NULL, Loại file (video/mp4, application/pdf, ...).
  • file_size_bytes: BIGINT, NOT NULL, Kích thước file (bytes).
  • created_at: TIMESTAMPTZ, NOT NULL DEFAULT CURRENT_TIMESTAMP.

3.3.3. Domain 3: Assessment

- Bảng: Quiz
  • quiz_id: PK, UUID, Khóa chính.
  • course_id: FK (→ Course), UUID, NOT NULL, ON DELETE CASCADE.
  • title: VARCHAR(200), NOT NULL, Tiêu đề quiz.
  • description: TEXT, Mô tả (tùy chọn).
  • questions: JSONB, NOT NULL, Danh sách câu hỏi với cấu trúc: [{question_id, points, order}].
  • passing_score: DECIMAL(5,2), NOT NULL, Điểm đạt (0-100).
  • duration_minutes: INT, Thời gian làm bài (phút, NULL = không giới hạn).
  • max_attempts: INT, Số lần làm tối đa (NULL = không giới hạn).
  • shuffle_questions: BOOLEAN, DEFAULT FALSE, Xáo trộn câu hỏi.
  • created_by: FK (→ User), UUID, NOT NULL.
  • created_at: TIMESTAMPTZ, NOT NULL DEFAULT CURRENT_TIMESTAMP.

- Bảng: Question
  • question_id: PK, UUID, Khóa chính.
  • course_id: FK (→ Course), UUID, NOT NULL, ON DELETE CASCADE.
  • type: VARCHAR(20), NOT NULL, Loại câu hỏi (MCQ, TRUE_FALSE, ESSAY, SHORT_ANSWER).
  • question_text: TEXT, NOT NULL, Nội dung câu hỏi.
  • points: DECIMAL(5,2), NOT NULL DEFAULT 1, Điểm của câu hỏi.
  • explanation: TEXT, Giải thích đáp án (tùy chọn).
  • created_by: FK (→ User), UUID, NOT NULL.
  • created_at: TIMESTAMPTZ, NOT NULL DEFAULT CURRENT_TIMESTAMP.

- Bảng: Option
  • option_id: PK, UUID, Khóa chính.
  • question_id: FK (→ Question), UUID, NOT NULL, ON DELETE CASCADE.
  • option_text: TEXT, NOT NULL, Nội dung đáp án.
  • is_correct: BOOLEAN, NOT NULL DEFAULT FALSE, Đáp án đúng hay không.
  • order_num: INT, NOT NULL, Thứ tự hiển thị.
  • UNIQUE(question_id, order_num): Không trùng thứ tự trong cùng câu hỏi.

- Bảng: Attempt
  • attempt_id: PK, UUID, Khóa chính.
  • quiz_id: FK (→ Quiz), UUID, NOT NULL, ON DELETE CASCADE.
  • user_id: FK (→ User), UUID, NOT NULL, ON DELETE CASCADE.
  • enrollment_id: FK (→ Enrollment), UUID, NOT NULL, ON DELETE CASCADE.
  • attempt_number: INT, NOT NULL, Lần thử thứ mấy (1, 2, 3...).
  • answers: JSONB, NOT NULL, Câu trả lời: [{question_id, answer_text, selected_options, score, max_score, is_correct, graded_at}].
  • total_score: DECIMAL(5,2), Tổng điểm (NULL nếu chưa chấm xong).
  • status: VARCHAR(20), NOT NULL DEFAULT 'IN_PROGRESS', Trạng thái (IN_PROGRESS, SUBMITTED, GRADED, REQUIRES_MANUAL_GRADING).
  • started_at: TIMESTAMPTZ, NOT NULL DEFAULT CURRENT_TIMESTAMP.
  • submitted_at: TIMESTAMPTZ, Thời gian nộp.
  • graded_at: TIMESTAMPTZ, Thời gian chấm.
  • UNIQUE(user_id, quiz_id, attempt_number): Không trùng lần thử.

- Bảng: AssignmentSubmission
  • submission_id: PK, UUID, Khóa chính.
  • lecture_id: FK (→ Lecture), UUID, NOT NULL, ON DELETE CASCADE (Lecture có type = ASSIGNMENT).
  • user_id: FK (→ User), UUID, NOT NULL, ON DELETE CASCADE.
  • enrollment_id: FK (→ Enrollment), UUID, NOT NULL, ON DELETE CASCADE.
  • submission_number: INT, NOT NULL DEFAULT 1, Lần nộp thứ mấy.
  • text_content: TEXT, Nội dung text (tùy chọn).
  • file_urls: JSONB, Array URLs file đã upload lên S3/GCS.
  • score: DECIMAL(5,2), Điểm số (NULL nếu chưa chấm).
  • feedback: TEXT, Phản hồi của giảng viên.
  • graded_by: FK (→ User), UUID, Người chấm.
  • submitted_at: TIMESTAMPTZ, NOT NULL DEFAULT CURRENT_TIMESTAMP.
  • graded_at: TIMESTAMPTZ, Thời gian chấm.
  • UNIQUE(lecture_id, user_id, submission_number): Không trùng lần nộp.

3.3.4. Domain 4: Enrollment & Progress

- Bảng: Enrollment
  • enrollment_id: PK, UUID, Khóa chính.
  • user_id: FK (→ User), UUID, NOT NULL, ON DELETE CASCADE.
  • course_id: FK (→ Course), UUID, NOT NULL, ON DELETE CASCADE.
  • class_id: FK (→ Class), UUID, ON DELETE SET NULL (NULL = self-paced learning).
  • status: VARCHAR(20), NOT NULL DEFAULT 'ACTIVE', Trạng thái (ACTIVE, COMPLETED, DROPPED).
  • enrolled_at: TIMESTAMPTZ, NOT NULL DEFAULT CURRENT_TIMESTAMP.
  • completed_at: TIMESTAMPTZ, Thời gian hoàn thành.
  • UNIQUE(user_id, course_id, COALESCE(class_id, '00000000-0000-0000-0000-000000000000')): Một sinh viên chỉ enroll một khóa học một lần (hoặc một lần cho mỗi class).

- Bảng: Progress
  • progress_id: PK, UUID, Khóa chính.
  • user_id: FK (→ User), UUID, NOT NULL, ON DELETE CASCADE.
  • course_id: FK (→ Course), UUID, NOT NULL, ON DELETE CASCADE.
  • module_id: FK (→ Module), UUID, NOT NULL, ON DELETE CASCADE.
  • status: VARCHAR(20), NOT NULL DEFAULT 'NOT_STARTED', Trạng thái (NOT_STARTED, IN_PROGRESS, COMPLETED).
  • started_at: TIMESTAMPTZ, Thời gian bắt đầu.
  • completed_at: TIMESTAMPTZ, Thời gian hoàn thành.
  • UNIQUE(user_id, course_id, module_id): Không trùng progress cho cùng module.

3.3.5. Domain 5: Class & Certificate

- Bảng: Class
  • class_id: PK, UUID, Khóa chính.
  • course_id: FK (→ Course), UUID, NOT NULL, ON DELETE CASCADE.
  • class_name: VARCHAR(100), NOT NULL, Tên lớp (VD: CNTT1-K63).
  • instructor_id: FK (→ User), UUID, ON DELETE SET NULL.
  • start_date: DATE, NOT NULL, Ngày bắt đầu.
  • end_date: DATE, NOT NULL, Ngày kết thúc (CHECK: end_date >= start_date).
  • max_students: INT, Số sinh viên tối đa (NULL = không giới hạn).
  • status: VARCHAR(20), NOT NULL DEFAULT 'SCHEDULED', Trạng thái (SCHEDULED, ONGOING, COMPLETED, CANCELLED).
  • schedules: JSONB, Lịch học: [{session_id, date, start_time, end_time, topic, location, type, attendances: [{user_id, status, check_in}]}].
  • created_at: TIMESTAMPTZ, NOT NULL DEFAULT CURRENT_TIMESTAMP.

- Bảng: Certificate
  • certificate_id: PK, UUID, Khóa chính.
  • user_id: FK (→ User), UUID, NOT NULL, ON DELETE CASCADE.
  • course_id: FK (→ Course), UUID, NOT NULL, ON DELETE CASCADE.
  • certificate_code: VARCHAR(50), UNIQUE NOT NULL, Mã chứng chỉ.
  • verification_code: VARCHAR(100), UNIQUE NOT NULL, Mã xác minh.
  • status: VARCHAR(20), NOT NULL DEFAULT 'ACTIVE', Trạng thái (ACTIVE, REVOKED).
  • issued_at: TIMESTAMPTZ, NOT NULL DEFAULT CURRENT_TIMESTAMP.
  • revoked_at: TIMESTAMPTZ, Thời gian thu hồi (nếu có).
  • UNIQUE(user_id, course_id): Một sinh viên chỉ có một certificate cho mỗi khóa học.

================================================================================

CHƯƠNG 2: ĐẶC TẢ YÊU CẦU CHỨC NĂNG

--------------------------------------------------------------------------------
1. Đặc tả chức năng: Quản lý khóa học (Course Management)
--------------------------------------------------------------------------------

1.1. Giao diện Page và thành phần

- Tên Page: Course Management Page (Web Application)

- Canvas chính (Chi tiết khóa học):
  Chứa các trường nhập/chỉnh sửa thông tin Course và Module.

- Canvas phụ (Danh sách Module):
  Hiển thị danh sách Module của khóa học, cho phép thêm/sửa/xóa Module.

- Dialog/Modal:
  Pop-up thông báo lỗi/thành công khi lưu dữ liệu.

Chi tiết thành phần:

- ID Khóa học
  • Loại điều khiển: Text (Readonly)
  • Trường dữ liệu: course_id
  • Bảng và kiểu dữ liệu: Course (UUID)
  • Thao tác: Hiển thị mã khóa học (UUID), auto-generated.

- Mã khóa học
  • Loại điều khiển: Text Input
  • Trường dữ liệu: code
  • Bảng và kiểu dữ liệu: Course (VARCHAR(50))
  • Thao tác: Nhập/Sửa mã khóa học (VD: CS101). Unique.

- Tên khóa học
  • Loại điều khiển: Text Input
  • Trường dữ liệu: title
  • Bảng và kiểu dữ liệu: Course (VARCHAR(200))
  • Thao tác: Nhập/Sửa tên khóa học.

- Mô tả
  • Loại điều khiển: Text Area (Rich Text Editor)
  • Trường dữ liệu: description
  • Bảng và kiểu dữ liệu: Course (TEXT)
  • Thao tác: Nhập/Sửa mô tả chi tiết. Hỗ trợ HTML formatting.

- Độ khó
  • Loại điều khiển: Dropdown/Select
  • Trường dữ liệu: difficulty_level
  • Bảng và kiểu dữ liệu: Course (VARCHAR(20))
  • Thao tác: Chọn BEGINNER, INTERMEDIATE, hoặc ADVANCED.

- Hình đại diện
  • Loại điều khiển: File Upload
  • Trường dữ liệu: thumbnail_url
  • Bảng và kiểu dữ liệu: Course (VARCHAR(500))
  • Thao tác: Upload file ảnh. Hệ thống upload lên S3/GCS và lưu URL.

- Trạng thái
  • Loại điều khiển: Dropdown/Select
  • Trường dữ liệu: status
  • Bảng và kiểu dữ liệu: Course (VARCHAR(20))
  • Thao tác: Chọn DRAFT, PUBLISHED, hoặc ARCHIVED.

- Giảng viên
  • Loại điều khiển: Text (Readonly)
  • Trường dữ liệu: created_by
  • Bảng và kiểu dữ liệu: Course (UUID) → User
  • Thao tác: Hiển thị tên giảng viên (lấy từ User.first_name + last_name).

- Danh sách Module
  • Loại điều khiển: Data Table/Grid
  • Trường dữ liệu: N/A
  • Bảng và kiểu dữ liệu: Module
  • Thao tác: Hiển thị danh sách Module với các cột: title, order_num. Cho phép thêm/sửa/xóa Module. Drag-drop để sắp xếp order_num.

- Nút Lưu
  • Loại điều khiển: Button
  • Thao tác: Gọi API PUT /api/courses/{course_id} để UPDATE Course.

- Nút Xuất bản
  • Loại điều khiển: Button
  • Thao tác: Gọi API POST /api/courses/{course_id}/publish để thay đổi status thành PUBLISHED.

--------------------------------------------------------------------------------

1.2. Truy vấn dữ liệu

- Hiển thị chính
  • Bảng: Course
  • Trường: course_id, code, title, description, difficulty_level, status, thumbnail_url, created_by
  • Mục đích: Hiển thị dữ liệu hiện tại của khóa học để Instructor chỉnh sửa.
  • API: GET /api/courses/{course_id}

- Hiển thị Module
  • Bảng: Module
  • Trường: module_id, course_id, title, description, order_num, prerequisite_module_ids
  • Mục đích: Hiển thị danh sách Module của khóa học.
  • API: GET /api/courses/{course_id}/modules

- Thông tin Instructor
  • Bảng: User
  • Trường: user_id, first_name, last_name
  • Mục đích: Lấy tên giảng viên để hiển thị.
  • API: GET /api/users/{user_id} (hoặc embedded trong Course response)

--------------------------------------------------------------------------------

1.3. Cập nhật dữ liệu

- Cập nhật Course
  • Bảng: Course
  • API: PUT /api/courses/{course_id}
  • Payload: { code, title, description, difficulty_level, thumbnail_url }
  • SQL tương đương:
    UPDATE "Course"
    SET code = :code, title = :title, description = :description,
        difficulty_level = :difficulty_level, thumbnail_url = :thumbnail_url,
        updated_at = CURRENT_TIMESTAMP
    WHERE course_id = :course_id AND created_by = :instructor_id

- Xuất bản Course
  • Bảng: Course
  • API: POST /api/courses/{course_id}/publish
  • SQL tương đương:
    UPDATE "Course"
    SET status = 'PUBLISHED', updated_at = CURRENT_TIMESTAMP
    WHERE course_id = :course_id AND created_by = :instructor_id AND status = 'DRAFT'

- Thêm Module
  • Bảng: Module
  • API: POST /api/courses/{course_id}/modules
  • Payload: { title, description, order_num, prerequisite_module_ids }
  • SQL tương đương:
    INSERT INTO "Module" (module_id, course_id, title, description, order_num, prerequisite_module_ids, created_at)
    VALUES (gen_random_uuid(), :course_id, :title, :description, :order_num, :prerequisite_module_ids, CURRENT_TIMESTAMP)

- Cập nhật Module
  • Bảng: Module
  • API: PUT /api/modules/{module_id}
  • SQL tương đương:
    UPDATE "Module"
    SET title = :title, description = :description, order_num = :order_num,
        prerequisite_module_ids = :prerequisite_module_ids
    WHERE module_id = :module_id

- Xóa Module
  • Bảng: Module
  • API: DELETE /api/modules/{module_id}
  • SQL tương đương:
    DELETE FROM "Module" WHERE module_id = :module_id
    -- CASCADE sẽ xóa Lecture, Resource liên quan

--------------------------------------------------------------------------------

1.4. Tiền và hậu điều kiện

- Tiền điều kiện:
  • Người dùng phải đăng nhập với role `INSTRUCTOR` hoặc `ADMIN`.
  • JWT token hợp lệ và chưa hết hạn.
  • Với thao tác **cập nhật / xuất bản / xóa**: `course_id` phải tồn tại và khóa học phải thuộc về giảng viên đang thực hiện thao tác (hoặc người dùng có role `ADMIN`).
  • Với thao tác **xuất bản**: trạng thái hiện tại của khóa học phải là `DRAFT` (không cho xuất bản từ `PUBLISHED` hoặc `ARCHIVED`).
  • Với thao tác **tạo mới**: trường `code` phải hợp lệ và chưa được sử dụng (tuân thủ ràng buộc UNIQUE).
  • Với thao tác **xóa**: kiểm tra điều kiện nghiệp vụ (ví dụ: không còn học viên đang active/enrolled hoặc phải thực hiện archive thay vì xóa thực sự) — chính sách xóa phải tuân theo quy định tổ chức.

- Hậu điều kiện:
  • Nếu tạo khóa học thành công:
    o Khóa học được tạo với `status = 'DRAFT'`, `course_id` được sinh tự động và `created_at`/`updated_at` được thiết lập.
    o API trả về HTTP 201 Created cùng dữ liệu khóa học mới.
  • Nếu cập nhật khóa học thành công:
    o Các trường dữ liệu (ví dụ: `code`, `title`, `description`, `difficulty_level`, `thumbnail_url`) được lưu thay đổi.
    o `updated_at` được cập nhật về thời điểm hiện tại.
    o API trả về HTTP 200 OK cùng dữ liệu khóa học đã cập nhật.
  • Nếu xuất bản thành công:
    o `status` chuyển thành `PUBLISHED` và `updated_at` được cập nhật.
    o API trả về HTTP 200 OK; frontend hiển thị thông báo: "Khóa học đã được xuất bản.".
  • Nếu xóa thành công:
    o Khóa học bị xóa vật lý hoặc được chuyển sang trạng thái `ARCHIVED` tùy theo chính sách (soft-delete recommended).
    o API trả về HTTP 200 OK hoặc 204 No Content theo thiết kế.
  • Nếu xảy ra lỗi nghiệp vụ hoặc xác thực (ví dụ: quyền hạn, ràng buộc UNIQUE, course không tồn tại):
    o Không có thay đổi được ghi vào hệ thống (transaction rollback).
    o API trả về mã lỗi thích hợp (400/403/404/409/500) kèm thông điệp rõ ràng.

- Ràng buộc nghiệp vụ & validation (áp dụng cho thao tác tạo/cập nhật/xuất bản/xóa):
  1. `code` phải tuân theo định dạng quy định và **unique** trong hệ thống.
  2. Chỉ `INSTRUCTOR` là người tạo/cập nhật khóa học của mình; `ADMIN` có quyền trên toàn hệ thống.
  3. Không cho phép xuất bản khi thông tin bắt buộc (title, description, difficulty_level) thiếu hoặc không hợp lệ.
  4. Khi xuất bản, hệ thống có thể kiểm tra các điều kiện bổ sung theo chính sách (ví dụ: tối thiểu một nội dung giới thiệu, thumbnail hợp lệ).
  5. Xóa khóa học phải tuân thủ chính sách bảo toàn dữ liệu: nếu có học viên đã đăng ký thì ưu tiên `ARCHIVE` thay vì xóa thực sự; nếu cho phép xóa vật lý thì cần có xác nhận cấp cao hơn (quyền ADMIN + xác nhận bổ sung).
  6. Chỉ khóa học có `status = 'PUBLISHED'` mới mở cho sinh viên đăng ký (enroll).

    Ghi chú: Các kiểm tra chi tiết (ví dụ: có học viên đang đăng ký hay không) xử lý ở tầng dịch vụ/ứng dụng; ở tài liệu này chỉ nêu rõ quy tắc nghiệp vụ, không mô tả cấu trúc bảng cụ thể.

================================================================================

--------------------------------------------------------------------------------
2. Đặc tả chức năng: Submission Management (Chi tiết & Chấm điểm)
--------------------------------------------------------------------------------

2.1. Giao diện Page và thành phần (Submission Detail & Grade)

- Mục tiêu: Cung cấp giao diện để:
  • Sinh viên xem chi tiết submission của chính họ (Submission Detail).
  • Giảng viên/ADMIN xem submission của sinh viên và thực hiện chấm điểm (Grade Submission).

- Page: Submission Detail (student view)
  • Canvas chính: hiển thị thông tin submission (submission_id, lecture_id, user_id, submission_number, text_content, file_urls (liệt kê đường dẫn), submitted_at, status (ON_TIME/LATE)).
  • Actions: `View` (download files), `Print`, `Request Regrade` (tùy chọn business rule).

- Page: Grade Submission (instructor view)
  • Canvas chính: hiển thị submission content cùng metadata (student, enrolled course/class, submitted_at).
  • Grade Panel: trường nhập `score` (decimal), `feedback` (rich text), và `private_note` (nội bộ, optional).
  • Buttons: `Save Draft` (lưu tạm), `Submit Grade` (chấm chính thức), `Override` (khi cần cập nhật điểm đã chấm; yêu cầu quyền nâng cao).
  • Audit: hiển thị `graded_by`, `graded_at`, và lịch sử chấm (nếu có).

- Dialog/Modal:
  • Confirm Submit Grade: xác nhận hành động chấm điểm chính thức.
  • Confirm Override: xác nhận khi ghi đè điểm đã chấm.

--------------------------------------------------------------------------------

2.2. Truy vấn dữ liệu (Submission-centric)

- Lấy chi tiết submission
  • API: `GET /api/submissions/{submission_id}`
  • Response: `{ submission_id, lecture_id, user_id, enrollment_id, submission_number, text_content, file_urls, submitted_at, score, feedback, graded_by, graded_at, status }`

- Lấy submission hiện của sinh viên cho lecture
  • API: `GET /api/lectures/{lecture_id}/submissions/me` — trả về submission(s) của current user cho lecture đó.

- Lấy danh sách submission cho giảng viên (để chấm)
  • API: `GET /api/lectures/{lecture_id}/submissions?status=SUBMITTED&sort=submitted_at` — hỗ trợ phân trang, lọc theo status (SUBMITTED, PENDING_REVIEW), tìm kiếm theo student.

--------------------------------------------------------------------------------

2.3. Cập nhật dữ liệu (Grade Submission)

- Chấm điểm (lần đầu)
  • API: `POST /api/submissions/{submission_id}/grade`
  • Payload: `{ score, feedback }`
  • SQL tương đương:
    UPDATE "AssignmentSubmission"
    SET score = :score, feedback = :feedback, graded_by = :grader_id, graded_at = CURRENT_TIMESTAMP
    WHERE submission_id = :submission_id
      AND (score IS NULL OR :force_override = TRUE);
  • Response: HTTP 200 OK + updated submission data (including graded_by, graded_at).

- Lưu nháp chấm điểm
  • API: `PUT /api/submissions/{submission_id}/grade-draft`
  • Payload: `{ score, feedback }` (lưu nhưng không khóa việc nộp lại hoặc finalize)
  • Response: HTTP 200 OK.

- Override / Regrade
  • API: `POST /api/submissions/{submission_id}/regrade`
  • Payload: `{ score, feedback, reason }` (chỉ giảng viên có quyền hoặc ADMIN, cần audit)
  • SQL tương đương: như trên nhưng lưu audit trail trong bảng `SubmissionGradeHistory` (recommended).

--------------------------------------------------------------------------------

2.4. Tiền và hậu điều kiện (Submission & Grading)

- Tiền điều kiện:
  • Authentication: JWT token hợp lệ.
  • View submission (student): user phải là chủ sở hữu của submission (user_id match) hoặc có quyền (INSTRUCTOR/ADMIN).
  • Grade submission (instructor): user phải có role `INSTRUCTOR` hoặc `ADMIN` và có quyền chấm cho khóa học/lecture này (creator hoặc assigned grader).
  • Submission phải tồn tại và có trạng thái phù hợp (ví dụ `SUBMITTED` hoặc `PENDING_REVIEW`) để chấm.

- Hậu điều kiện:
  • Khi chấm xong (Submit Grade): `AssignmentSubmission.score`, `feedback`, `graded_by`, `graded_at` được cập nhật; nếu chấm chính thức thì submission được khóa khỏi việc submit lại (business rule).
  • Khi lưu nháp: các trường grade được lưu nhưng không thay đổi trạng thái khóa nộp.
  • Khi override/regrade: lưu entry audit (grader_id, previous_score, new_score, reason, timestamp) và cập nhật `graded_by`/`graded_at`.
  • API trả về mã phù hợp: 200 OK (thành công), 400/403/404/409/500 (lỗi hoặc vi phạm quyền/validation).

- Ràng buộc nghiệp vụ & validation:
  1. `score` phải nằm trong khoảng hợp lệ (0 .. lecture.assignment_config.max_points) hoặc theo quy ước course-level.
  2. Chỉ grader hợp lệ mới được submit grade; override phải được ghi nhận với lý do và audit.
  3. Sau khi grade finalized, sinh viên không được nộp lại (nếu policy cấm); nếu cho phép re-submit thì ghi chú rõ điều kiện.
  4. Nếu grader muốn chỉnh sửa grade đã finalize, cần flag `force_override` và audit reason.
  5. Tất cả thao tác chấm điểm cần được ghi log/audit để phục vụ khiếu nại và lịch sử điểm.

    Ghi chú: Tài liệu này tập trung vào chi tiết submission và chấm điểm — các chi tiết upload file, kiểm tra file, và luồng nộp bài đã được loại bỏ theo yêu cầu.
================================================================================

KẾT THÚC BÁO CÁO

================================================================================
