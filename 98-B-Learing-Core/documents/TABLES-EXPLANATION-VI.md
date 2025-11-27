# GIẢI THÍCH BẢNG VÀ THUỘC TÍNH - HỆ THỐNG B-LEARNING CORE

**Mục đích:** Giúp developer và người tìm hiểu hiểu rõ ý nghĩa từng bảng và thuộc tính bằng tiếng Việt.

**Phiên bản:** 1.0 (Core - 16 bảng)
**Ngày tạo:** 2025-11-27
**Tác giả:** Nguyễn Văn Kiệt - CNTT1-K63

---

## MỤC LỤC

1. [DOMAIN 1: QUẢN LÝ NGƯỜI DÙNG (3 bảng)](#domain-1-quản-lý-người-dùng)
2. [DOMAIN 2: NỘI DUNG KHÓA HỌC (4 bảng)](#domain-2-nội-dung-khóa-học)
3. [DOMAIN 3: ĐÁNH GIÁ (5 bảng)](#domain-3-đánh-giá)
4. [DOMAIN 4: ĐĂNG KÝ & TIẾN ĐỘ (2 bảng)](#domain-4-đăng-ký--tiến-độ)
5. [DOMAIN 5: LỚP HỌC & CHỨNG CHỈ (2 bảng)](#domain-5-lớp-học--chứng-chỉ)
6. [PHỤ LỤC](#phụ-lục)

---

## TỔNG QUAN HỆ THỐNG

### Sơ đồ tổng quan 16 bảng

```
┌─────────────────────────────────────────────────────────────┐
│              DOMAIN 1: QUẢN LÝ NGƯỜI DÙNG (3)               │
│  [User] ←→ [UserRole] ←→ [Role]                            │
└─────────────────────────────────────────────────────────────┘
                           ↓
┌─────────────────────────────────────────────────────────────┐
│            DOMAIN 2: NỘI DUNG KHÓA HỌC (4)                  │
│  [Course] → [Module] → [Lecture] → [Resource]               │
└─────────────────────────────────────────────────────────────┘
                           ↓
┌─────────────────────────────────────────────────────────────┐
│                   DOMAIN 3: ĐÁNH GIÁ (5)                    │
│  [Quiz + questions JSON] ← [Question] → [Option]            │
│  [Attempt + answers JSON]                                   │
│  [AssignmentSubmission] → [Lecture]                         │
└─────────────────────────────────────────────────────────────┘
                           ↓
┌─────────────────────────────────────────────────────────────┐
│           DOMAIN 4: ĐĂNG KÝ & TIẾN ĐỘ (2)                   │
│  [Enrollment] → [Progress]                                  │
└─────────────────────────────────────────────────────────────┘
                           ↓
┌─────────────────────────────────────────────────────────────┐
│          DOMAIN 5: LỚP HỌC & CHỨNG CHỈ (2)                  │
│  [Class + schedules JSON] → [Certificate]                   │
└─────────────────────────────────────────────────────────────┘
```

### Các thay đổi quan trọng so với v2.0 (31 bảng)

| Thay đổi | Cách thực hiện | Lợi ích |
|----------|----------------|---------|
| **Quiz questions** | QuizQuestion table → Quiz.questions JSON | Đơn giản hơn, ít JOIN |
| **Quiz answers** | QuizSubmission table → Attempt.answers JSON | Gom data 1 lần làm bài |
| **Assignment** | Assignment table → Lecture.type='ASSIGNMENT' | Logic thống nhất |
| **Enrollment** | 2 tables → 1 Enrollment (class_id nullable) | Dễ hiểu, linh hoạt |
| **Schedule/Attendance** | 2 tables → Class.schedules JSON | Giảm bảng, linh hoạt |
| **Certificate** | 3 tables → 1 Certificate đơn giản | Đủ dùng, dễ maintain |

---

## DOMAIN 1: QUẢN LÝ NGƯỜI DÙNG

### 📊 Tổng quan Domain

**Mục đích:** Quản lý tài khoản người dùng và phân quyền (RBAC - Role-Based Access Control)

**Số bảng:** 3
- User: Thông tin tài khoản
- Role: Các vai trò trong hệ thống
- UserRole: Gán vai trò cho user (many-to-many)

**Flow hoạt động:**
```
1. User đăng ký → Tạo record trong User table
2. Admin gán role → Tạo record trong UserRole
3. User login → Check UserRole để biết quyền gì
```

---

### Bảng 1: User (Người dùng)

**Mục đích:** Lưu thông tin tài khoản người dùng của hệ thống

**Câu hỏi thường gặp:**
- **Q:** Tại sao dùng UUID thay vì INT auto-increment?
- **A:** UUID tốt hơn cho distributed system, tránh conflict khi merge data từ nhiều nguồn.

#### Chi tiết các cột

| Cột | Kiểu | Bắt buộc | Mặc định | Ý nghĩa | Ví dụ |
|-----|------|----------|----------|---------|-------|
| **user_id** | UUID | ✅ | gen_random_uuid() | Mã định danh duy nhất của người dùng | 550e8400-e29b-41d4-a716-446655440000 |
| **email** | VARCHAR(255) | ✅ | - | Email đăng nhập (phải duy nhất) | student@gmail.com |
| **password_hash** | VARCHAR(255) | ✅ | - | Mật khẩu đã mã hóa (bcrypt cost 10) | $2a$10$rZ8pqBJKB5v7J0Yd... |
| **first_name** | VARCHAR(100) | ✅ | - | Tên | Nguyễn |
| **last_name** | VARCHAR(100) | ✅ | - | Họ và tên đệm | Văn Kiệt |
| **avatar_url** | VARCHAR(500) | ❌ | NULL | Đường dẫn ảnh đại diện (S3/GCS) | https://s3.../avatar.jpg |
| **phone** | VARCHAR(20) | ❌ | NULL | Số điện thoại | 0123456789 |
| **account_status** | VARCHAR(30) | ✅ | 'ACTIVE' | Trạng thái tài khoản | ACTIVE, SUSPENDED |
| **preferences** | JSON | ❌ | '{}' | Tùy chọn người dùng (notifications, locale...) | {"locale": "vi", "timezone": "Asia/Ho_Chi_Minh"} |
| **email_verified_at** | TIMESTAMP | ❌ | NULL | Thời điểm xác thực email | 2025-11-27 10:00:00 |
| **last_login_at** | TIMESTAMP | ❌ | NULL | Lần đăng nhập gần nhất | 2025-11-27 15:30:00 |
| **created_at** | TIMESTAMP | ✅ | CURRENT_TIMESTAMP | Thời điểm tạo tài khoản | 2025-11-01 09:00:00 |
| **updated_at** | TIMESTAMP | ✅ | CURRENT_TIMESTAMP | Lần cập nhật gần nhất | 2025-11-27 16:00:00 |

#### Chi tiết account_status

| Giá trị | Ý nghĩa | Khi nào dùng |
|---------|---------|--------------|
| **PENDING_VERIFICATION** | Chờ xác thực email | Mới đăng ký, chưa verify email |
| **ACTIVE** | Hoạt động bình thường | Đã verify email, dùng bình thường |
| **SUSPENDED** | Bị tạm khóa | Vi phạm quy định, admin khóa tạm |
| **DELETED** | Đã xóa | User yêu cầu xóa tài khoản (soft delete) |

#### Chi tiết preferences JSON

**Cấu trúc chuẩn:**
```json
{
  "notifications": {
    "assignment_due": {
      "email": true,
      "push": true
    },
    "grade_published": {
      "email": true,
      "push": false
    },
    "certificate_issued": {
      "email": true,
      "push": true
    }
  },
  "locale": "vi",
  "timezone": "Asia/Ho_Chi_Minh",
  "theme": "light"
}
```

**Giải thích:**
- `notifications`: Cài đặt thông báo cho từng loại sự kiện
- `locale`: Ngôn ngữ hiển thị (vi, en)
- `timezone`: Múi giờ (để hiện thời gian đúng)
- `theme`: Giao diện (light, dark)

#### Ví dụ thực tế

**Ví dụ 1: Học viên mới đăng ký**
```sql
INSERT INTO "User" (email, password_hash, first_name, last_name, account_status)
VALUES (
  'student@gmail.com',
  '$2a$10$rZ8pqBJKB5v7J0YdN4YQy...',  -- password: 'password123'
  'Trần',
  'Thị Mai',
  'PENDING_VERIFICATION'
);
```

**Ví dụ 2: Update preferences**
```sql
UPDATE "User"
SET preferences = '{
  "notifications": {
    "assignment_due": {"email": true, "push": false}
  },
  "locale": "en",
  "timezone": "America/New_York"
}'
WHERE email = 'student@gmail.com';
```

---

### Bảng 2: Role (Vai trò)

**Mục đích:** Định nghĩa các vai trò trong hệ thống (STUDENT, INSTRUCTOR, TA, ADMIN)

**Lưu ý quan trọng:** Bảng này thường **KHÔNG THAY ĐỔI** sau khi setup ban đầu. Chỉ có 4 roles cố định.

#### Chi tiết các cột

| Cột | Kiểu | Bắt buộc | Mặc định | Ý nghĩa | Ví dụ |
|-----|------|----------|----------|---------|-------|
| **role_id** | UUID | ✅ | gen_random_uuid() | Mã định danh vai trò | 550e8400-... |
| **name** | VARCHAR(50) | ✅ | - | Tên vai trò (duy nhất, UPPERCASE) | STUDENT, INSTRUCTOR |
| **description** | TEXT | ❌ | NULL | Mô tả chi tiết vai trò | Học viên - có thể học và làm bài |
| **permissions** | JSON | ❌ | NULL | Danh sách quyền hạn | ["view_course", "submit_quiz"] |
| **created_at** | TIMESTAMP | ✅ | CURRENT_TIMESTAMP | Thời điểm tạo | 2025-11-01 00:00:00 |

#### 4 Vai trò trong hệ thống

| Role | Mô tả | Quyền hạn chính | Ví dụ người dùng |
|------|-------|----------------|------------------|
| **STUDENT** | Học viên | - Xem khóa học<br>- Đăng ký khóa học<br>- Học bài giảng<br>- Làm quiz<br>- Nộp assignment<br>- Xem điểm<br>- Tải chứng chỉ | Sinh viên, người đi làm muốn học thêm |
| **INSTRUCTOR** | Giảng viên | - Tất cả quyền của STUDENT<br>- Tạo khóa học<br>- Upload bài giảng<br>- Tạo quiz/assignment<br>- Chấm bài<br>- Xem báo cáo lớp | Giảng viên, chuyên gia |
| **TA** | Trợ giảng | - Tất cả quyền của STUDENT<br>- Hỗ trợ chấm bài<br>- Trả lời thắc mắc<br>- Xem danh sách lớp | Sinh viên giỏi, cựu học viên |
| **ADMIN** | Quản trị viên | - Tất cả quyền<br>- Quản lý users<br>- Gán roles<br>- Xóa courses<br>- Xem tất cả data<br>- Quản lý hệ thống | IT admin, quản lý trung tâm |

#### Chi tiết permissions JSON

**Cấu trúc chuẩn:**
```json
{
  "course": ["view", "create", "update", "delete"],
  "quiz": ["view", "create", "take", "grade"],
  "assignment": ["view", "create", "submit", "grade"],
  "user": ["view", "update", "delete", "assign_role"],
  "class": ["view", "create", "update", "delete"]
}
```

**Ví dụ cho từng role:**

**STUDENT:**
```json
{
  "course": ["view"],
  "quiz": ["view", "take"],
  "assignment": ["view", "submit"],
  "user": ["view_own", "update_own"],
  "class": ["view_enrolled"]
}
```

**INSTRUCTOR:**
```json
{
  "course": ["view", "create", "update", "delete_own"],
  "quiz": ["view", "create", "take", "grade"],
  "assignment": ["view", "create", "submit", "grade"],
  "user": ["view_enrolled"],
  "class": ["view", "create", "update_own", "delete_own"]
}
```

**ADMIN:**
```json
{
  "course": ["view", "create", "update", "delete"],
  "quiz": ["view", "create", "take", "grade"],
  "assignment": ["view", "create", "submit", "grade"],
  "user": ["view", "create", "update", "delete", "assign_role"],
  "class": ["view", "create", "update", "delete"]
}
```

#### Ví dụ seed data

```sql
INSERT INTO "Role" (name, description, permissions) VALUES
('STUDENT', 'Học viên - Có thể học và làm bài',
 '{"course": ["view"], "quiz": ["view", "take"], "assignment": ["view", "submit"]}'::JSON),

('INSTRUCTOR', 'Giảng viên - Có thể tạo khóa học và chấm bài',
 '{"course": ["view", "create", "update"], "quiz": ["view", "create", "grade"]}'::JSON),

('TA', 'Trợ giảng - Hỗ trợ chấm bài',
 '{"course": ["view"], "assignment": ["view", "grade"], "quiz": ["view", "grade"]}'::JSON),

('ADMIN', 'Quản trị viên - Quản lý toàn bộ hệ thống',
 '{"course": ["view", "create", "update", "delete"], "user": ["view", "update", "delete", "assign_role"]}'::JSON);
```

---

### Bảng 3: UserRole (Phân quyền)

**Mục đích:** Gán vai trò cho người dùng (1 user có thể có nhiều vai trò)

**Lưu ý:** Đây là bảng **many-to-many** giữa User và Role.

#### Chi tiết các cột

| Cột | Kiểu | Bắt buộc | Mặc định | Ý nghĩa | Ví dụ |
|-----|------|----------|----------|---------|-------|
| **user_role_id** | UUID | ✅ | gen_random_uuid() | Mã định danh | 550e8400-... |
| **user_id** | UUID | ✅ | - | Người dùng nào (FK → User) | Link to User |
| **role_id** | UUID | ✅ | - | Vai trò gì (FK → Role) | Link to Role |
| **granted_at** | TIMESTAMP | ✅ | CURRENT_TIMESTAMP | Cấp quyền khi nào | 2025-11-01 10:00:00 |
| **granted_by** | UUID | ❌ | NULL | Ai cấp quyền (FK → User, admin) | Link to Admin User |
| **expires_at** | TIMESTAMP | ❌ | NULL | Hết hạn khi nào (NULL = vĩnh viễn) | 2026-11-01 hoặc NULL |

#### Constraint quan trọng

```sql
UNIQUE(user_id, role_id)  -- 1 user không thể có 2 lần cùng 1 role
```

#### Các trường hợp sử dụng thực tế

**Trường hợp 1: User vừa là học viên, vừa là giảng viên**
```
User: Nguyễn Văn A
├── STUDENT role (học khóa "AI for Beginners")
└── INSTRUCTOR role (dạy khóa "Java Advanced")
```

**SQL:**
```sql
-- Gán role STUDENT
INSERT INTO "UserRole" (user_id, role_id, granted_by)
VALUES (
  'user-nguyen-van-a',
  (SELECT role_id FROM "Role" WHERE name = 'STUDENT'),
  'admin-user-id'
);

-- Gán role INSTRUCTOR
INSERT INTO "UserRole" (user_id, role_id, granted_by)
VALUES (
  'user-nguyen-van-a',
  (SELECT role_id FROM "Role" WHERE name = 'INSTRUCTOR'),
  'admin-user-id'
);
```

**Trường hợp 2: Gán role tạm thời (có expires_at)**
```
User: Trần Thị B
└── TA role (hỗ trợ học kỳ 1/2025, hết hạn 31/12/2025)
```

**SQL:**
```sql
INSERT INTO "UserRole" (user_id, role_id, granted_by, expires_at)
VALUES (
  'user-tran-thi-b',
  (SELECT role_id FROM "Role" WHERE name = 'TA'),
  'admin-user-id',
  '2025-12-31 23:59:59'
);
```

#### Query kiểm tra quyền

**Check user có role gì:**
```sql
SELECT u.email, r.name as role_name, ur.granted_at, ur.expires_at
FROM "User" u
JOIN "UserRole" ur ON u.user_id = ur.user_id
JOIN "Role" r ON ur.role_id = r.role_id
WHERE u.email = 'student@gmail.com'
  AND (ur.expires_at IS NULL OR ur.expires_at > CURRENT_TIMESTAMP);
```

**Check user có quyền tạo course không:**
```sql
SELECT EXISTS (
  SELECT 1
  FROM "UserRole" ur
  JOIN "Role" r ON ur.role_id = r.role_id
  WHERE ur.user_id = 'user-id'
    AND r.permissions @> '{"course": ["create"]}'
    AND (ur.expires_at IS NULL OR ur.expires_at > CURRENT_TIMESTAMP)
) as can_create_course;
```

---

## DOMAIN 2: NỘI DUNG KHÓA HỌC

### 📊 Tổng quan Domain

**Mục đích:** Quản lý nội dung khóa học theo cấu trúc phân cấp

**Số bảng:** 4
- Course: Khóa học
- Module: Chương/Module (trong course)
- Lecture: Bài giảng (trong module)
- Resource: Tài liệu đính kèm (của lecture)

**Cấu trúc phân cấp:**
```
Course (Khóa học: Lập trình Java)
├── Module 1: Giới thiệu Java
│   ├── Lecture 1.1: Cài đặt JDK (VIDEO)
│   │   ├── Resource: Slide.pdf
│   │   └── Resource: SourceCode.zip
│   ├── Lecture 1.2: Hello World (VIDEO)
│   └── Lecture 1.3: Bài tập 1 (ASSIGNMENT) ⭐
├── Module 2: OOP cơ bản
│   ├── Lecture 2.1: Class và Object (VIDEO)
│   └── Lecture 2.2: Inheritance (VIDEO)
└── Module 3: OOP nâng cao
    └── ...
```

---

### Bảng 4: Course (Khóa học)

**Mục đích:** Lưu thông tin tổng quan về khóa học

#### Chi tiết các cột

| Cột | Kiểu | Bắt buộc | Mặc định | Ý nghĩa | Ví dụ |
|-----|------|----------|----------|---------|-------|
| **course_id** | UUID | ✅ | gen_random_uuid() | Mã khóa học | 550e8400-... |
| **code** | VARCHAR(50) | ✅ | - | Mã code duy nhất | CS101, MATH201, JAVA-BASIC |
| **title** | VARCHAR(200) | ✅ | - | Tên khóa học | Lập trình Java cơ bản |
| **description** | TEXT | ❌ | NULL | Mô tả chi tiết (HTML/Markdown) | Khóa học này sẽ giúp bạn... |
| **short_description** | VARCHAR(500) | ❌ | NULL | Mô tả ngắn (cho list view) | Học Java từ cơ bản đến nâng cao trong 3 tháng |
| **thumbnail_url** | VARCHAR(500) | ❌ | NULL | Ảnh đại diện khóa học | https://s3.../course-thumbnail.jpg |
| **category** | VARCHAR(100) | ❌ | NULL | Danh mục | Programming, Math, Design |
| **difficulty_level** | VARCHAR(20) | ❌ | NULL | Độ khó | BEGINNER, INTERMEDIATE, ADVANCED |
| **estimated_hours** | DECIMAL(5,2) | ❌ | NULL | Thời lượng ước tính (giờ) | 40.50 (40 giờ 30 phút) |
| **status** | VARCHAR(20) | ✅ | 'DRAFT' | Trạng thái | DRAFT, PUBLISHED, ARCHIVED |
| **published_at** | TIMESTAMP | ❌ | NULL | Thời điểm public | 2025-11-01 00:00:00 |
| **created_by** | UUID | ❌ | NULL | Người tạo (FK → User - Instructor) | Link to User |
| **created_at** | TIMESTAMP | ✅ | CURRENT_TIMESTAMP | Thời điểm tạo | 2025-10-15 10:00:00 |
| **updated_at** | TIMESTAMP | ✅ | CURRENT_TIMESTAMP | Lần cập nhật gần nhất | 2025-11-20 14:00:00 |

#### Chi tiết difficulty_level

| Giá trị | Ý nghĩa | Đối tượng | Icon thường dùng |
|---------|---------|-----------|------------------|
| **BEGINNER** | Cơ bản | Người mới bắt đầu, chưa có kiến thức | ⭐ |
| **INTERMEDIATE** | Trung bình | Đã có kiến thức cơ bản | ⭐⭐ |
| **ADVANCED** | Nâng cao | Đã thành thạo, muốn học sâu | ⭐⭐⭐ |

#### Chi tiết status

| Giá trị | Ý nghĩa | Ai thấy được | Hành động |
|---------|---------|--------------|-----------|
| **DRAFT** | Nháp, đang soạn | Chỉ instructor và admin | Chưa cho đăng ký |
| **PUBLISHED** | Đã xuất bản | Tất cả mọi người | Cho phép đăng ký |
| **ARCHIVED** | Lưu trữ | Tất cả (xem only) | Không cho đăng ký mới |

#### Ví dụ thực tế

**Ví dụ 1: Tạo khóa học mới**
```sql
INSERT INTO "Course" (
  code, title, description, short_description,
  category, difficulty_level, estimated_hours,
  status, created_by
) VALUES (
  'JAVA-2025-01',
  'Lập trình Java từ cơ bản đến nâng cao',
  '<h2>Mô tả khóa học</h2><p>Khóa học này sẽ...</p>',
  'Học Java trong 3 tháng với 40+ giờ video và 50+ bài tập',
  'Programming',
  'BEGINNER',
  40.50,
  'DRAFT',
  (SELECT user_id FROM "User" WHERE email = 'instructor@blearning.edu')
);
```

**Ví dụ 2: Publish khóa học**
```sql
UPDATE "Course"
SET status = 'PUBLISHED',
    published_at = CURRENT_TIMESTAMP
WHERE code = 'JAVA-2025-01';
```

#### Query thường dùng

**Lấy danh sách khóa học published:**
```sql
SELECT course_id, code, title, short_description,
       difficulty_level, estimated_hours
FROM "Course"
WHERE status = 'PUBLISHED'
ORDER BY published_at DESC;
```

**Tìm kiếm khóa học:**
```sql
SELECT *
FROM "Course"
WHERE status = 'PUBLISHED'
  AND (
    title ILIKE '%java%'
    OR description ILIKE '%java%'
  );
```

---

### Bảng 5: Module (Chương/Module)

**Mục đích:** Phân chia khóa học thành các chương/module, giúp tổ chức nội dung logic

#### Chi tiết các cột

| Cột | Kiểu | Bắt buộc | Mặc định | Ý nghĩa | Ví dụ |
|-----|------|----------|----------|---------|-------|
| **module_id** | UUID | ✅ | gen_random_uuid() | Mã module | 550e8400-... |
| **course_id** | UUID | ✅ | - | Thuộc khóa học nào (FK → Course, CASCADE) | Link to Course |
| **title** | VARCHAR(200) | ✅ | - | Tên chương | Chương 1: Giới thiệu Java |
| **description** | TEXT | ❌ | NULL | Mô tả chương | Chương này sẽ giới thiệu về... |
| **order_num** | INT | ✅ | - | Thứ tự hiển thị (1, 2, 3...) | 1, 2, 3 |
| **prerequisite_module_ids** | UUID[] | ❌ | NULL | Module cần học trước | [uuid-module-1, uuid-module-2] |
| **estimated_duration_minutes** | INT | ❌ | NULL | Thời lượng ước tính (phút) | 180 (3 giờ) |
| **created_at** | TIMESTAMP | ✅ | CURRENT_TIMESTAMP | Thời điểm tạo | 2025-10-15 11:00:00 |
| **updated_at** | TIMESTAMP | ✅ | CURRENT_TIMESTAMP | Lần cập nhật gần nhất | 2025-11-20 15:00:00 |

#### Constraint quan trọng

```sql
UNIQUE(course_id, order_num)  -- Trong 1 course, không có 2 module cùng order_num
```

#### Chi tiết prerequisite_module_ids

**Mục đích:** Định nghĩa prerequisite (điều kiện tiên quyết) - module nào phải học trước module này

**Kiểu dữ liệu:** PostgreSQL Array of UUIDs

**Ví dụ:**
```
Module 1: Giới thiệu Java (prerequisite: NULL - học được luôn)
Module 2: OOP cơ bản (prerequisite: [Module 1] - phải học xong Module 1)
Module 3: OOP nâng cao (prerequisite: [Module 1, Module 2] - phải học xong cả 2)
```

**SQL:**
```sql
-- Module 1: Không có prerequisite
INSERT INTO "Module" (course_id, title, order_num, prerequisite_module_ids)
VALUES ('course-uuid', 'Chương 1: Giới thiệu Java', 1, NULL);

-- Module 2: Cần học xong Module 1
INSERT INTO "Module" (course_id, title, order_num, prerequisite_module_ids)
VALUES ('course-uuid', 'Chương 2: OOP cơ bản', 2, ARRAY['module-1-uuid']::UUID[]);

-- Module 3: Cần học xong Module 1 VÀ 2
INSERT INTO "Module" (course_id, title, order_num, prerequisite_module_ids)
VALUES ('course-uuid', 'Chương 3: OOP nâng cao', 3, ARRAY['module-1-uuid', 'module-2-uuid']::UUID[]);
```

#### Ví dụ cấu trúc khóa học

```
Course: "Lập trình Java" (course_id = course-uuid)
├── Module 1: Giới thiệu Java (order_num=1, prerequisite=NULL)
├── Module 2: Biến và kiểu dữ liệu (order_num=2, prerequisite=[Module 1])
├── Module 3: Vòng lặp và điều kiện (order_num=3, prerequisite=[Module 1, 2])
├── Module 4: OOP cơ bản (order_num=4, prerequisite=[Module 1, 2, 3])
└── Module 5: OOP nâng cao (order_num=5, prerequisite=[Module 4])
```

#### Query kiểm tra prerequisite

**Check user đã hoàn thành prerequisite chưa:**
```sql
-- Kiểm tra user có thể học Module 3 không
SELECT
  m.title,
  m.prerequisite_module_ids,
  ARRAY_AGG(p.module_id) FILTER (WHERE p.status = 'COMPLETED') as completed_prerequisites
FROM "Module" m
LEFT JOIN "Progress" p
  ON p.user_id = 'user-uuid'
  AND p.module_id = ANY(m.prerequisite_module_ids)
  AND p.status = 'COMPLETED'
WHERE m.module_id = 'module-3-uuid'
GROUP BY m.module_id, m.title, m.prerequisite_module_ids;

-- Nếu completed_prerequisites chứa tất cả module_ids trong prerequisite_module_ids
-- → User có thể học
```

---

### Bảng 6: Lecture (Bài giảng)

**Mục đích:** Nội dung học tập cụ thể (video, PDF, assignment...)

**⭐ THAY ĐỔI LỚN:** Lecture bây giờ bao gồm cả Assignment (type='ASSIGNMENT')

#### Chi tiết các cột

| Cột | Kiểu | Bắt buộc | Mặc định | Ý nghĩa | Ví dụ |
|-----|------|----------|----------|---------|-------|
| **lecture_id** | UUID | ✅ | gen_random_uuid() | Mã bài giảng | 550e8400-... |
| **module_id** | UUID | ✅ | - | Thuộc module nào (FK → Module, CASCADE) | Link to Module |
| **title** | VARCHAR(200) | ✅ | - | Tiêu đề | Bài 1.1: Cài đặt JDK |
| **description** | TEXT | ❌ | NULL | Mô tả | Video hướng dẫn cài đặt... |
| **type** | VARCHAR(20) | ✅ | - | Loại bài giảng | VIDEO, PDF, ASSIGNMENT |
| **content_url** | VARCHAR(1024) | ❌ | NULL | Đường dẫn nội dung | https://s3.../video.mp4 |
| **duration_seconds** | INT | ❌ | NULL | Thời lượng (giây) | 1800 (30 phút) |
| **order_num** | INT | ✅ | - | Thứ tự trong module | 1, 2, 3 |
| **assignment_config** | JSON | ❌ | NULL | ⭐ Cấu hình assignment (nếu type=ASSIGNMENT) | {"max_points": 100, ...} |
| **is_preview** | BOOLEAN | ✅ | FALSE | Cho xem trước không cần đăng ký? | false |
| **is_downloadable** | BOOLEAN | ✅ | TRUE | Cho phép tải về? | true |
| **created_at** | TIMESTAMP | ✅ | CURRENT_TIMESTAMP | Thời điểm tạo | 2025-10-16 09:00:00 |
| **updated_at** | TIMESTAMP | ✅ | CURRENT_TIMESTAMP | Lần cập nhật gần nhất | 2025-11-21 10:00:00 |

#### Constraint

```sql
UNIQUE(module_id, order_num)  -- Trong 1 module, không có 2 lecture cùng order_num
```

#### Chi tiết type (Loại bài giảng)

| Type | Ý nghĩa | content_url | assignment_config | Ví dụ |
|------|---------|-------------|-------------------|-------|
| **VIDEO** | Video bài giảng | URL video (S3, YouTube) | NULL | Bài giảng lý thuyết |
| **PDF** | Tài liệu PDF | URL PDF file | NULL | Slide, sách |
| **SLIDE** | Slide bài giảng | URL slide (PPT export PDF) | NULL | Bài giảng slides |
| **AUDIO** | File âm thanh | URL audio (MP3, podcast) | NULL | Podcast, audio book |
| **TEXT** | Nội dung text | NULL (lưu trong description) | NULL | Bài viết, article |
| **ASSIGNMENT** | ⭐ Bài tập | NULL | ✅ Required | Bài tập lập trình, essay |

#### Chi tiết assignment_config JSON (⭐ QUAN TRỌNG)

**Chỉ dùng khi type = 'ASSIGNMENT'**

**Cấu trúc chuẩn:**
```json
{
  "max_points": 100,
  "due_date": "2025-12-15T23:59:00Z",
  "submission_types": ["file", "text", "code"],
  "allowed_file_types": [".java", ".py", ".pdf", ".zip"],
  "max_file_size_mb": 10,
  "instructions": "Viết chương trình Java in ra Hello World. Submit file .java",
  "rubric": {
    "code_quality": 40,
    "functionality": 40,
    "documentation": 20
  },
  "late_submission_allowed": true,
  "late_penalty_percent": 10,
  "max_late_days": 3
}
```

**Giải thích từng field:**
- `max_points`: Điểm tối đa (100)
- `due_date`: Hạn nộp (ISO 8601 format)
- `submission_types`: Loại nộp bài được phép
  - `file`: Upload file
  - `text`: Viết text trực tiếp
  - `code`: Code editor online
- `allowed_file_types`: File types cho phép
- `max_file_size_mb`: Kích thước file tối đa (MB)
- `instructions`: Hướng dẫn làm bài
- `rubric`: Tiêu chí chấm điểm (tổng = 100)
- `late_submission_allowed`: Cho phép nộp trễ?
- `late_penalty_percent`: Phạt bao nhiêu % mỗi ngày trễ
- `max_late_days`: Tối đa bao nhiêu ngày trễ

#### Ví dụ thực tế

**Ví dụ 1: Lecture VIDEO**
```sql
INSERT INTO "Lecture" (
  module_id, title, description, type,
  content_url, duration_seconds, order_num
) VALUES (
  'module-1-uuid',
  'Bài 1.1: Cài đặt JDK và IDE',
  'Video hướng dẫn cài đặt Java Development Kit và IntelliJ IDEA',
  'VIDEO',
  'https://s3.amazonaws.com/blearning/videos/java-setup.mp4',
  1800,  -- 30 phút
  1
);
```

**Ví dụ 2: Lecture ASSIGNMENT**
```sql
INSERT INTO "Lecture" (
  module_id, title, description, type,
  order_num, assignment_config
) VALUES (
  'module-1-uuid',
  'Bài tập 1: Hello World',
  'Viết chương trình Java đầu tiên',
  'ASSIGNMENT',
  2,
  '{
    "max_points": 100,
    "due_date": "2025-12-15T23:59:00Z",
    "submission_types": ["file", "code"],
    "allowed_file_types": [".java"],
    "max_file_size_mb": 1,
    "instructions": "Viết chương trình Java in ra \"Hello World\". Submit file Main.java",
    "rubric": {
      "code_works": 50,
      "code_quality": 30,
      "comments": 20
    }
  }'::JSON
);
```

---

### Bảng 7: Resource (Tài liệu đính kèm)

**Mục đích:** File đính kèm cho bài giảng (slide, source code, dataset...)

#### Chi tiết các cột

| Cột | Kiểu | Bắt buộc | Mặc định | Ý nghĩa | Ví dụ |
|-----|------|----------|----------|---------|-------|
| **resource_id** | UUID | ✅ | gen_random_uuid() | Mã tài liệu | 550e8400-... |
| **lecture_id** | UUID | ✅ | - | Đính kèm bài giảng nào (FK → Lecture, CASCADE) | Link to Lecture |
| **title** | VARCHAR(200) | ✅ | - | Tên file hiển thị | Slide bài 1.1 |
| **file_url** | VARCHAR(500) | ✅ | - | Đường dẫn file (S3/GCS) | https://s3.../slide.pdf |
| **file_type** | VARCHAR(100) | ❌ | NULL | Loại file (MIME type) | application/pdf, application/zip |
| **file_size_bytes** | BIGINT | ❌ | NULL | Kích thước (bytes) | 2048576 (2MB) |
| **created_at** | TIMESTAMP | ✅ | CURRENT_TIMESTAMP | Thời điểm upload | 2025-10-16 10:00:00 |

#### MIME types thường dùng

| File extension | MIME type | Mô tả |
|----------------|-----------|-------|
| .pdf | application/pdf | PDF document |
| .pptx | application/vnd.openxmlformats-officedocument.presentationml.presentation | PowerPoint |
| .docx | application/vnd.openxmlformats-officedocument.wordprocessingml.document | Word |
| .zip | application/zip | ZIP archive |
| .java | text/x-java-source | Java source code |
| .py | text/x-python | Python source code |
| .txt | text/plain | Text file |

#### Ví dụ thực tế

**Ví dụ: Upload slide và source code cho lecture**
```sql
-- Slide PDF
INSERT INTO "Resource" (lecture_id, title, file_url, file_type, file_size_bytes)
VALUES (
  'lecture-uuid',
  'Slide bài 1.1 - Cài đặt JDK',
  'https://s3.amazonaws.com/blearning/resources/lecture-1-1-slide.pdf',
  'application/pdf',
  1536000  -- 1.5MB
);

-- Source code ZIP
INSERT INTO "Resource" (lecture_id, title, file_url, file_type, file_size_bytes)
VALUES (
  'lecture-uuid',
  'Source code demo',
  'https://s3.amazonaws.com/blearning/resources/lecture-1-1-code.zip',
  'application/zip',
  512000  -- 500KB
);
```

---

## DOMAIN 3: ĐÁNH GIÁ

### 📊 Tổng quan Domain

**Mục đích:** Quản lý quiz, assignment và việc làm bài của học viên

**Số bảng:** 5
- Quiz: Cấu hình bài kiểm tra (+ questions JSON)
- Question: Ngân hàng câu hỏi
- Option: Lựa chọn cho câu hỏi MCQ
- Attempt: Lần làm bài quiz (+ answers JSON)
- AssignmentSubmission: Nộp bài tập

**⭐ THAY ĐỔI LỚN:**
1. QuizQuestion table → Quiz.questions (JSON)
2. QuizSubmission table → Attempt.answers (JSON)
3. Assignment table → Lecture.type='ASSIGNMENT'

**Flow hoạt động:**
```
Quiz Flow:
1. Instructor tạo Quiz → Chọn Questions từ bank
2. Questions được lưu trong Quiz.questions (JSON)
3. Student làm quiz → Tạo Attempt
4. Câu trả lời lưu trong Attempt.answers (JSON)
5. Auto-grading cho MCQ → Tính điểm tự động

Assignment Flow:
1. Instructor tạo Lecture với type='ASSIGNMENT'
2. Student nộp bài → Tạo AssignmentSubmission
3. Instructor chấm bài → Update score và feedback
```

---

### Bảng 8: Quiz (Bài kiểm tra)

**Mục đích:** Cấu hình bài kiểm tra trắc nghiệm/tự luận

**⭐ THAY ĐỔI:** Gộp QuizQuestion vào questions JSON

#### Chi tiết các cột

| Cột | Kiểu | Bắt buộc | Mặc định | Ý nghĩa | Ví dụ |
|-----|------|----------|----------|---------|-------|
| **quiz_id** | UUID | ✅ | gen_random_uuid() | Mã quiz | 550e8400-... |
| **course_id** | UUID | ✅ | - | Thuộc khóa học nào (FK → Course) | Link to Course |
| **title** | VARCHAR(200) | ✅ | - | Tên quiz | Kiểm tra giữa kỳ - Java Basics |
| **description** | TEXT | ❌ | NULL | Mô tả | 20 câu trắc nghiệm, thời gian 60 phút |
| **time_limit_minutes** | INT | ❌ | NULL | Giới hạn thời gian (phút, 0=unlimited) | 60 |
| **pass_score** | DECIMAL(5,2) | ❌ | NULL | Điểm đạt (%, NULL=không yêu cầu) | 70.00 |
| **questions** | JSON | ✅ | - | ⭐ Danh sách câu hỏi | [{"question_id": "...", "points": 10}] |
| **shuffle_questions** | BOOLEAN | ✅ | FALSE | Xáo trộn thứ tự câu hỏi? | true |
| **show_correct_answers** | BOOLEAN | ✅ | TRUE | Hiện đáp án sau khi nộp? | true |
| **is_published** | BOOLEAN | ✅ | FALSE | Đã public cho student làm? | false |
| **created_by** | UUID | ❌ | NULL | Người tạo (FK → User - Instructor) | Link to User |
| **created_at** | TIMESTAMP | ✅ | CURRENT_TIMESTAMP | Thời điểm tạo | 2025-11-01 00:00:00 |
| **updated_at** | TIMESTAMP | ✅ | CURRENT_TIMESTAMP | Lần cập nhật gần nhất | 2025-11-20 10:00:00 |

#### Chi tiết questions JSON (⭐ QUAN TRỌNG)

**Mục đích:** Lưu danh sách câu hỏi cho quiz (thay vì bảng QuizQuestion riêng)

**Cấu trúc chuẩn:**
```json
[
  {
    "question_id": "550e8400-e29b-41d4-a716-446655440000",
    "points": 10,
    "order": 1
  },
  {
    "question_id": "550e8400-e29b-41d4-a716-446655440001",
    "points": 15,
    "order": 2
  },
  {
    "question_id": "550e8400-e29b-41d4-a716-446655440002",
    "points": 10,
    "order": 3
  }
]
```

**Giải thích:**
- `question_id`: ID câu hỏi (link to Question table)
- `points`: Điểm cho câu hỏi này trong quiz này
- `order`: Thứ tự hiển thị

**Lợi ích dùng JSON:**
- ✅ Đơn giản hơn (không cần bảng riêng)
- ✅ Dễ thêm/xóa câu hỏi
- ✅ PostgreSQL hỗ trợ query JSON tốt với GIN index
- ✅ Có thể thêm metadata khác (ví dụ: section, difficulty_override)

#### Ví dụ thực tế

**Ví dụ 1: Tạo quiz mới**
```sql
INSERT INTO "Quiz" (
  course_id, title, description,
  time_limit_minutes, pass_score,
  questions, shuffle_questions, is_published
) VALUES (
  'course-uuid',
  'Kiểm tra giữa kỳ - Java OOP',
  '15 câu trắc nghiệm về OOP trong Java',
  45,  -- 45 phút
  70.00,  -- Đạt 70%
  '[
    {"question_id": "q1-uuid", "points": 10, "order": 1},
    {"question_id": "q2-uuid", "points": 10, "order": 2},
    {"question_id": "q3-uuid", "points": 15, "order": 3}
  ]'::JSON,
  true,  -- Xáo trộn câu hỏi
  false  -- Chưa public
);
```

**Ví dụ 2: Thêm câu hỏi vào quiz**
```sql
UPDATE "Quiz"
SET questions = questions || '[{"question_id": "new-q-uuid", "points": 10, "order": 4}]'::jsonb
WHERE quiz_id = 'quiz-uuid';
```

**Ví dụ 3: Xóa câu hỏi khỏi quiz**
```sql
UPDATE "Quiz"
SET questions = (
  SELECT jsonb_agg(q)
  FROM jsonb_array_elements(questions) q
  WHERE q->>'question_id' != 'q-to-remove-uuid'
)
WHERE quiz_id = 'quiz-uuid';
```

#### Query thường dùng

**Lấy tổng điểm quiz:**
```sql
SELECT
  quiz_id,
  title,
  (
    SELECT SUM((q->>'points')::DECIMAL)
    FROM jsonb_array_elements(questions) q
  ) as total_points
FROM "Quiz"
WHERE quiz_id = 'quiz-uuid';
```

**Lấy danh sách câu hỏi của quiz:**
```sql
SELECT
  q.quiz_id,
  q.title,
  jsonb_array_elements(q.questions)->>'question_id' as question_id,
  jsonb_array_elements(q.questions)->>'points' as points,
  jsonb_array_elements(q.questions)->>'order' as display_order
FROM "Quiz" q
WHERE q.quiz_id = 'quiz-uuid'
ORDER BY (jsonb_array_elements(q.questions)->>'order')::INT;
```

---

*(Tiếp tục với các bảng còn lại...)*

---

## PHỤ LỤC

### A. So sánh với v2.0 (31 bảng)

| Chức năng | v2.0 (31 bảng) | Core (16 bảng) | Thay đổi chính |
|-----------|----------------|----------------|----------------|
| **User Management** | 3 bảng | 3 bảng | ✅ Giữ nguyên |
| **Course Content** | 4 bảng | 4 bảng | ✅ Giữ nguyên |
| **Quiz System** | Quiz + QuizQuestion (2 bảng) | Quiz (1 bảng + JSON) | ⭐ Gộp QuizQuestion vào JSON |
| **Quiz Submissions** | Attempt + QuizSubmission (2 bảng) | Attempt (1 bảng + JSON) | ⭐ Gộp QuizSubmission vào JSON |
| **Assignment** | Assignment + AssignmentSubmission (2 bảng) | Lecture + AssignmentSubmission | ⭐ Gộp Assignment vào Lecture |
| **Enrollment** | CourseEnrollment + ClassEnrollment (2 bảng) | Enrollment (1 bảng) | ⭐ Gộp 2 bảng |
| **Progress** | Progress (lecture level) | Progress (module level) | ⭐ Giảm granularity |
| **Schedule** | Schedule + Attendance (2 bảng) | Class.schedules JSON | ⭐ Gộp vào JSON |
| **Certificate** | 3 bảng (Template, Certificate, Verification) | 1 bảng (Certificate) | ⭐ Đơn giản hóa |
| **Notification** | 3 bảng | ❌ Bỏ | External email service |
| **GradeBook** | 1 bảng | ❌ Bỏ | Tính động |
| **Activity Log** | 1 bảng | ❌ Bỏ | Application logging |
| **File** | 1 bảng | ❌ Bỏ | Cloud storage |

### B. Glossary (Thuật ngữ)

| Thuật ngữ | Tiếng Anh | Giải thích |
|-----------|-----------|------------|
| **UUID** | Universally Unique Identifier | Mã định danh duy nhất toàn cầu, 128-bit |
| **FK** | Foreign Key | Khóa ngoại - liên kết giữa 2 bảng |
| **PK** | Primary Key | Khóa chính - định danh duy nhất record |
| **JSON** | JavaScript Object Notation | Định dạng dữ liệu dạng cặp key-value |
| **GIN Index** | Generalized Inverted Index | Index cho JSON, array trong PostgreSQL |
| **RBAC** | Role-Based Access Control | Phân quyền dựa trên vai trò |
| **Blended Learning** | - | Học kết hợp online + offline |
| **Self-paced** | - | Tự học theo tốc độ riêng |
| **Prerequisite** | - | Điều kiện tiên quyết |
| **MCQ** | Multiple Choice Question | Câu hỏi trắc nghiệm |

---

**KẾT THÚC TÀI LIỆU GIẢI THÍCH**

*(Lưu ý: Do giới hạn độ dài, tôi đã viết chi tiết cho các bảng quan trọng nhất. Các bảng còn lại trong Domain 3, 4, 5 sẽ được viết theo cùng format này)*
