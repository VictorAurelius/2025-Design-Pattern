TRƯỜNG ĐẠI HỌC GIAO THÔNG VẬN TẢI
KHOA: CÔNG NGHỆ THÔNG TIN

[Logo trường]

BÁO CÁO KẾT QUẢ BÀI TẬP LỚN HỌC PHẦN:
ĐẶC TẢ PHẦN MỀM

XÂY DỰNG HỆ THỐNG QUẢN LÝ HỌC TẬP TRỰC TUYẾN
(B-LEARNING CORE v1.0)

Người lập: Nguyễn Văn Kiệt - CNTT1-K63
Ngày lập: 02/12/2025

Người xem xét:...........................................
Ngày xem xét: ……/……./.……….

================================================================================

LỜI NÓI ĐẦU

Trong bối cảnh chuyển đổi số diễn ra mạnh mẽ, giáo dục trực tuyến đã trở thành xu hướng tất yếu của ngành giáo dục hiện đại. Đặc biệt sau đại dịch COVID-19, nhu cầu về các hệ thống quản lý học tập (Learning Management System - LMS) toàn diện, hiệu quả và dễ sử dụng ngày càng tăng cao. Các cơ sở giáo dục, từ trường phổ thông đến đại học và các tổ chức đào tạo doanh nghiệp, đều cần một nền tảng kỹ thuật số để tổ chức, phân phối và theo dõi các hoạt động dạy và học.

Hệ thống B-Learning Core được xây dựng nhằm đáp ứng nhu cầu quản lý học tập trực tuyến với mô hình kết hợp (Blended Learning). Hệ thống tập trung vào hai nhóm người dùng chính: Instructor (Giảng viên) với chức năng Course Management - quản lý khóa học, tạo nội dung bài giảng, thiết lập bài tập và chấm điểm; và Student (Sinh viên) với chức năng Submission Management - đăng ký khóa học, học tập, nộp bài và theo dõi tiến độ.

Báo cáo này trình bày chi tiết đặc tả yêu cầu phần mềm cho hệ thống B-Learning Core, bao gồm: phân tích chức năng thông qua sơ đồ phân rã chức năng (BFD), mô hình hóa dữ liệu với sơ đồ ER và Relational Model, đặc tả chi tiết cơ sở dữ liệu PostgreSQL 14+, và đặc tả yêu cầu chức năng cho hai module chính. Mục tiêu cuối cùng là xây dựng một sản phẩm phần mềm chất lượng, dễ sử dụng, đáp ứng đầy đủ các yêu cầu nghiệp vụ của một hệ thống LMS hiện đại.

================================================================================

MỤC LỤC

CHƯƠNG 1: ĐẶC TẢ YÊU CẦU PHẦN MỀM............................................................ 4
1. Giới thiệu chung ...................................................................................................... 4
   1.1. Mục đích ............................................................................................................ 4
   1.2. Phạm vi .............................................................................................................. 5
2. Sơ đồ phân rã chức năng ........................................................................................ 7
   2.1. Sơ đồ phân rã chức năng dưới góc nhìn của Instructor ..................................... 7
   2.2. Sơ đồ phân rã chức năng dưới góc nhìn của Student ...................................... 10
3. Mô hình hóa dữ liệu .............................................................................................. 13
   3.1. Sơ đồ ER .......................................................................................................... 13
   3.2. Sơ đồ RM......................................................................................................... 14
   3.3. Đặc tả chi tiết cơ sở dữ liệu............................................................................ 15

CHƯƠNG 2: ĐẶC TẢ YÊU CẦU CHỨC NĂNG ....................................................... 25
1. Đặc tả chức năng: Quản lý khóa học (Course Management) .............................. 25
   1.1. Giao diện Page và thành phần ........................................................................ 25
   1.2. Truy vấn dữ liệu.............................................................................................. 26
   1.3. Cập nhật dữ liệu.............................................................................................. 27
   1.4. Tiền và hậu điều kiện ..................................................................................... 27
2. Đặc tả chức năng: Nộp bài tập (Submission Management) ................................ 28
   2.1. Giao diện Page và thành phần........................................................................ 28
   2.2. Truy vấn dữ liệu.............................................................................................. 29
   2.3. Cập nhật dữ liệu.............................................................................................. 29
   2.4. Tiền và hậu điều kiện ..................................................................................... 30

================================================================================

CHƯƠNG 1: ĐẶC TẢ YÊU CẦU PHẦN MỀM

--------------------------------------------------------------------------------
1. Giới thiệu chung
--------------------------------------------------------------------------------

1.1. Mục đích

Hệ thống B-Learning Core là một nền tảng quản lý học tập trực tuyến (Learning Management System - LMS) được thiết kế để hỗ trợ mô hình học tập kết hợp (Blended Learning). Mục đích chính của hệ thống là cung cấp một giải pháp toàn diện cho việc tổ chức, quản lý và theo dõi các hoạt động dạy và học trong môi trường giáo dục hiện đại.

Hệ thống được xây dựng với các mục tiêu cụ thể sau:

- Đối với Instructor (Giảng viên):
  • Cung cấp công cụ tạo và quản lý khóa học với cấu trúc phân cấp: Course → Module → Lecture.
  • Hỗ trợ tạo các loại nội dung đa dạng: video, PDF, slide, audio, text và assignment.
  • Cho phép tạo Quiz với câu hỏi đa dạng: MCQ, True/False, Essay, Short Answer.
  • Cung cấp chức năng chấm điểm Assignment và Quiz, gửi feedback cho sinh viên.
  • Theo dõi tiến độ học tập của từng sinh viên trong khóa học.

- Đối với Student (Sinh viên):
  • Đăng ký và truy cập các khóa học đã xuất bản.
  • Học tập theo cấu trúc Module, xem video và tài liệu bài giảng.
  • Nộp bài Assignment qua upload file hoặc nhập text.
  • Làm Quiz trực tuyến và nhận kết quả tự động (cho MCQ/True-False).
  • Theo dõi tiến độ học tập và nhận Certificate khi hoàn thành khóa học.

- Yêu cầu phi chức năng:
  • Bảo mật: Mã hóa password với bcrypt, xác thực qua token, phân quyền theo role.
  • Hiệu năng: Tối ưu truy vấn với indexes, hỗ trợ GIN index cho JSON queries.
  • Độ tin cậy: Backup dữ liệu định kỳ, transaction support với PostgreSQL.
  • Khả năng mở rộng: Sử dụng UUID làm primary key, JSON fields linh hoạt.

- Công nghệ sử dụng:
  • Database: PostgreSQL 14+ với hỗ trợ JSONB, UUID, Full-text search.
  • Backend: RESTful API (Python/FastAPI hoặc Node.js/Express).
  • Frontend: Web-based platform (React/Next.js).
  • Storage: S3/GCS cho file upload.

--------------------------------------------------------------------------------

1.2. Phạm vi

Phần mềm này là một hệ thống quản lý học tập trực tuyến (LMS), bao gồm ứng dụng web cho sinh viên và nền tảng web quản trị cho giảng viên. Hệ thống được thiết kế với kiến trúc 5 domain, 16 bảng dữ liệu, hỗ trợ cả mô hình học tự điều khiển (self-paced) và học theo lớp (blended learning).

Phạm vi của ứng dụng tập trung vào hai bên liên quan chính:

- Instructor (Giảng viên):
  • Quản lý tài khoản: Đăng nhập, cập nhật thông tin cá nhân, đăng xuất.
  • Quản lý nội dung khóa học: Tạo/cập nhật/xuất bản Course, Module, Lecture, Resource.
  • Quản lý đánh giá: Tạo Quiz, Question, chấm điểm AssignmentSubmission và Attempt.
  • Quản lý lớp học: Tạo Class, xem Enrollment, theo dõi Progress, cấp Certificate.

- Student (Sinh viên):
  • Quản lý tài khoản: Đăng ký, đăng nhập, cập nhật thông tin, đăng xuất.
  • Đăng ký và học tập: Enrollment khóa học, xem Lecture, học Module.
  • Làm bài tập và kiểm tra: Nộp AssignmentSubmission, làm Quiz (Attempt).
  • Theo dõi tiến độ: Xem Progress, điểm số, tải Certificate.

- Cấu trúc cơ sở dữ liệu (16 tables):
  • Domain 1 - User Management (3 bảng): User, Role, UserRole
  • Domain 2 - Course Content (4 bảng): Course, Module, Lecture, Resource
  • Domain 3 - Assessment (5 bảng): Quiz, Question, Option, Attempt, AssignmentSubmission
  • Domain 4 - Enrollment & Progress (2 bảng): Enrollment, Progress
  • Domain 5 - Class & Certificate (2 bảng): Class, Certificate

================================================================================

--------------------------------------------------------------------------------
2. Sơ đồ phân rã chức năng
--------------------------------------------------------------------------------

2.1. Sơ đồ phân rã chức năng dưới góc nhìn của Instructor (Course Management)

2.1.1. Sơ đồ phân rã chức năng

[Hình 1: Sơ đồ phân rã chức năng - Instructor]

Đặc tả chức năng:

- Quản lý Tài khoản:
  • Mục đích: Cho phép giảng viên đăng nhập và quản lý thông tin cá nhân.
  • Ngữ cảnh: Được sử dụng bởi giảng viên trên nền tảng web.
  • Điều kiện tiên quyết: Thiết bị đã kết nối Internet, có tài khoản với role Instructor.
  • Mô tả:
    o Đăng nhập: Giảng viên nhập email và password để truy cập hệ thống. Hệ thống xác thực và trả về JWT token.
    o Cập nhật thông tin: Cập nhật User.first_name, last_name, preferences (JSON).
    o Đăng xuất: Invalidate token và thoát khỏi hệ thống.
  • Kết quả: Giảng viên có thể truy cập các chức năng của hệ thống. Thông tin cá nhân được cập nhật chính xác.

- Quản lý Nội dung Khóa học:
  • Mục đích: Giúp giảng viên tạo, cập nhật, và tổ chức nội dung khóa học.
  • Ngữ cảnh: Giảng viên sử dụng trên nền tảng web để quản lý Course, Module, Lecture.
  • Điều kiện tiên quyết: Giảng viên đã đăng nhập thành công vào hệ thống.
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
  • Người dùng phải đăng nhập với role INSTRUCTOR hoặc ADMIN.
  • JWT token hợp lệ và chưa hết hạn.
  • course_id của khóa học cần chỉnh sửa phải tồn tại trong bảng Course.
  • Khóa học đó phải thuộc về giảng viên này (Course.created_by = User.user_id) hoặc user có role ADMIN.
  • Khi xuất bản, Course.status phải là DRAFT (không thể xuất bản khóa học đã PUBLISHED hoặc ARCHIVED).
  • code mới (nếu thay đổi) phải chưa tồn tại trong hệ thống (UNIQUE constraint).

- Hậu điều kiện:
  • Nếu thành công:
    o Các trường dữ liệu trong bảng Course được cập nhật.
    o Course.updated_at được cập nhật thành thời gian hiện tại.
    o Nếu xuất bản, status = 'PUBLISHED'.
    o Module được thêm/sửa/xóa theo yêu cầu.
    o Response trả về HTTP 200 OK với Course data mới.
    o Frontend hiển thị toast notification: "Khóa học đã được cập nhật thành công."
  • Nếu thất bại:
    o Course không thay đổi (transaction rollback).
    o Response trả về HTTP 400/403/404/500 với error message.
    o Frontend hiển thị error toast.

- Ràng buộc nghiệp vụ:
  • Không thể xóa Course nếu đã có Enrollment (cần soft delete hoặc archive).
  • Khi xóa Module, tất cả Lecture, Resource thuộc Module đó cũng bị xóa (CASCADE).
  • prerequisite_module_ids phải chứa các module_id hợp lệ thuộc cùng course_id.
  • order_num của Module phải unique trong cùng Course.
  • Chỉ Course có status = PUBLISHED mới cho phép sinh viên enroll.

================================================================================

--------------------------------------------------------------------------------
2. Đặc tả chức năng: Nộp bài tập (Submission Management)
--------------------------------------------------------------------------------

2.1. Giao diện Page và thành phần

- Tên Page: Assignment Submission Page (Web Application)

- Canvas 1 (Chi tiết bài tập):
  Hiển thị thông tin Assignment (title, instructions, due_date, max_points).

- Canvas 2 (Nộp bài):
  Cho phép sinh viên nhập text_content và upload file.

- Canvas 3 (Kết quả):
  Hiển thị score và feedback sau khi được chấm.

- Dialog/Modal:
  Pop-up xác nhận nộp bài, lỗi upload file.

Chi tiết thành phần:

- Tên bài tập
  • Loại điều khiển: Text (Readonly)
  • Trường dữ liệu: title
  • Bảng và kiểu dữ liệu: Lecture (VARCHAR(200))
  • Thao tác: Hiển thị tên bài tập.

- Hướng dẫn
  • Loại điều khiển: HTML Content (Readonly)
  • Trường dữ liệu: assignment_config.instructions
  • Bảng và kiểu dữ liệu: Lecture.assignment_config (JSONB)
  • Thao tác: Hiển thị hướng dẫn làm bài (rich text).

- Hạn nộp
  • Loại điều khiển: Text (Readonly)
  • Trường dữ liệu: assignment_config.due_date
  • Bảng và kiểu dữ liệu: Lecture.assignment_config (JSONB)
  • Thao tác: Hiển thị deadline (format: DD/MM/YYYY HH:MM). Highlight màu đỏ nếu đã quá hạn.

- Điểm tối đa
  • Loại điều khiển: Text (Readonly)
  • Trường dữ liệu: assignment_config.max_points
  • Bảng và kiểu dữ liệu: Lecture.assignment_config (JSONB)
  • Thao tác: Hiển thị điểm tối đa.

- Loại file cho phép
  • Loại điều khiển: Text (Readonly)
  • Trường dữ liệu: assignment_config.allowed_file_types
  • Bảng và kiểu dữ liệu: Lecture.assignment_config (JSONB)
  • Thao tác: Hiển thị các định dạng file được phép (VD: .java, .py, .pdf).

- Nội dung bài làm
  • Loại điều khiển: Text Area (Rich Text Editor)
  • Trường dữ liệu: text_content
  • Bảng và kiểu dữ liệu: AssignmentSubmission (TEXT)
  • Thao tác: Nhập nội dung bài làm dạng text (tùy chọn, có thể để trống nếu chỉ upload file).

- File đính kèm
  • Loại điều khiển: File Upload (Multiple)
  • Trường dữ liệu: file_urls
  • Bảng và kiểu dữ liệu: AssignmentSubmission (JSONB - array of URLs)
  • Thao tác: Chọn và upload file. Validate file type và size theo assignment_config. Upload lên S3/GCS.

- Trạng thái nộp
  • Loại điều khiển: Badge/Label (Readonly)
  • Trường dữ liệu: derived from submitted_at vs due_date
  • Thao tác: Hiển thị "On Time" (xanh) hoặc "Late" (đỏ).

- Điểm số
  • Loại điều khiển: Text (Readonly)
  • Trường dữ liệu: score
  • Bảng và kiểu dữ liệu: AssignmentSubmission (DECIMAL(5,2))
  • Thao tác: Hiển thị điểm (nếu đã chấm) hoặc "Chưa chấm".

- Phản hồi
  • Loại điều khiển: HTML Content (Readonly)
  • Trường dữ liệu: feedback
  • Bảng và kiểu dữ liệu: AssignmentSubmission (TEXT)
  • Thao tác: Hiển thị feedback của giảng viên (nếu có).

- Nút Nộp bài
  • Loại điều khiển: Button
  • Thao tác: Gọi API POST /api/assignments/{lecture_id}/submissions để tạo submission.

- Nút Nộp lại
  • Loại điều khiển: Button
  • Thao tác: Gọi API PUT /api/submissions/{submission_id} để cập nhật submission. Chỉ hiển thị nếu chưa được chấm điểm.

--------------------------------------------------------------------------------

2.2. Truy vấn dữ liệu

- Thông tin Assignment
  • Bảng: Lecture
  • Trường: lecture_id, title, type, assignment_config
  • Điều kiện: type = 'ASSIGNMENT'
  • Mục đích: Lấy thông tin bài tập để hiển thị.
  • API: GET /api/lectures/{lecture_id}

- Kiểm tra Enrollment
  • Bảng: Enrollment
  • Trường: enrollment_id, user_id, course_id, status
  • Mục đích: Xác nhận sinh viên đã enroll khóa học chứa Assignment này.
  • API: Thực hiện nội bộ (authorization middleware)
  • SQL tương đương:
    SELECT e.* FROM "Enrollment" e
    JOIN "Module" m ON m.course_id = e.course_id
    JOIN "Lecture" l ON l.module_id = m.module_id
    WHERE l.lecture_id = :lecture_id
      AND e.user_id = :user_id
      AND e.status = 'ACTIVE'

- Submission hiện tại
  • Bảng: AssignmentSubmission
  • Trường: submission_id, text_content, file_urls, submitted_at, score, feedback, graded_at
  • Mục đích: Kiểm tra xem sinh viên đã nộp bài chưa. Nếu có, hiển thị thông tin submission.
  • API: GET /api/assignments/{lecture_id}/submissions/me

--------------------------------------------------------------------------------

2.3. Cập nhật dữ liệu

- Nộp bài lần đầu
  • Bảng: AssignmentSubmission
  • API: POST /api/assignments/{lecture_id}/submissions
  • Payload: { text_content, files (multipart) }
  • SQL tương đương:
    INSERT INTO "AssignmentSubmission"
    (submission_id, lecture_id, user_id, enrollment_id, submission_number, text_content, file_urls, submitted_at)
    VALUES (gen_random_uuid(), :lecture_id, :user_id, :enrollment_id, 1, :text_content, :file_urls, CURRENT_TIMESTAMP)

- Nộp lại (Re-submit)
  • Bảng: AssignmentSubmission
  • API: PUT /api/submissions/{submission_id}
  • Điều kiện: score IS NULL (chưa được chấm)
  • SQL tương đương:
    UPDATE "AssignmentSubmission"
    SET text_content = :text_content,
        file_urls = :file_urls,
        submitted_at = CURRENT_TIMESTAMP,
        submission_number = submission_number + 1
    WHERE submission_id = :submission_id
      AND user_id = :user_id
      AND score IS NULL

- Upload File
  • Service: S3/GCS Upload Service
  • Process:
    1. Validate file type theo assignment_config.allowed_file_types
    2. Validate file size theo assignment_config.max_file_size_mb
    3. Upload file lên S3/GCS
    4. Trả về URL để lưu vào file_urls JSONB array

--------------------------------------------------------------------------------

2.4. Tiền và hậu điều kiện

- Tiền điều kiện:
  • Người dùng phải đăng nhập với role STUDENT.
  • JWT token hợp lệ và chưa hết hạn.
  • lecture_id phải tồn tại trong bảng Lecture với type = 'ASSIGNMENT'.
  • Sinh viên phải đã enroll khóa học chứa Assignment này:
    o Enrollment.user_id = current_user_id
    o Enrollment.course_id = Course của Lecture
    o Enrollment.status = 'ACTIVE'
  • Nếu đã nộp bài trước đó, chỉ cho phép nộp lại nếu chưa được chấm (AssignmentSubmission.score IS NULL).
  • File upload (nếu có) phải đúng định dạng cho phép (assignment_config.allowed_file_types).
  • File upload không được vượt quá kích thước tối đa (assignment_config.max_file_size_mb).

- Hậu điều kiện:
  • Nếu thành công:
    o Bảng AssignmentSubmission được INSERT (lần đầu) hoặc UPDATE (nộp lại).
    o File được upload lên S3/GCS và URL được lưu vào file_urls.
    o Response trả về HTTP 201 Created (lần đầu) hoặc 200 OK (nộp lại).
    o Frontend hiển thị toast: "Bài tập đã được nộp thành công."
    o Nếu nộp trễ (submitted_at > due_date), hiển thị warning: "Bạn đã nộp trễ."
  • Nếu thất bại:
    o Không có dữ liệu nào được INSERT/UPDATE (transaction rollback).
    o File không được lưu lên S3/GCS.
    o Response trả về HTTP 400/403/404/500 với error message.
    o Frontend hiển thị error toast tương ứng.

- Ràng buộc nghiệp vụ:
  • Một sinh viên có thể có nhiều submission cho mỗi Assignment (submission_number tăng dần).
  • Không thể xóa AssignmentSubmission sau khi đã nộp (chỉ giảng viên/admin có quyền xóa).
  • Khi giảng viên chấm điểm (UPDATE score, feedback), graded_at = CURRENT_TIMESTAMP và graded_by = instructor_id.
  • Sau khi được chấm điểm, sinh viên không thể nộp lại (score IS NOT NULL).

- Validation (Application Layer):
  1. Check enrollment: Sinh viên phải đã enroll và status = ACTIVE.
  2. Check assignment exists: Lecture phải tồn tại với type = ASSIGNMENT.
  3. Check if already graded: Nếu đã có submission với score IS NOT NULL thì không cho nộp lại.
  4. Validate file type: Kiểm tra extension của file phải nằm trong allowed_file_types.
  5. Validate file size: Kiểm tra size <= max_file_size_mb * 1024 * 1024.
  6. Check deadline: Nếu quá hạn, vẫn cho nộp nhưng đánh dấu warning.

================================================================================

KẾT THÚC BÁO CÁO

================================================================================
