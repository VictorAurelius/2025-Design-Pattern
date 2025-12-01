# ANALYSIS_TEMPLATE.md

**Mục đích:** Tài liệu này tổng hợp cấu trúc, format, và style guide cho việc viết báo cáo cơ sở dữ liệu B-Learning dựa trên phân tích tài liệu tham khảo (Tham_khao.pdf).

**Nguồn tham khảo:** Scripts/Tham_khao.pdf - Báo cáo ứng dụng giao đồ ăn (24 trang)

---

## 1. CẤU TRÚC TỔNG QUAN

### 1.1. Thứ tự các phần chính

```
1. LỜI NÓI ĐẦU (Preface)
2. MỤC LỤC (Table of Contents)
3. DANH MỤC HÌNH ẢNH/BẢNG BIỂU (List of Figures/Tables)
4. CHƯƠNG 1: TỔNG QUAN HỆ THỐNG
5. CHƯƠNG 2: PHÂN TÍCH YÊU CẦU
6. CHƯƠNG 3: THIẾT KẾ CƠ SỞ DỮ LIỆU
7. CHƯƠNG 4: TRIỂN KHAI & DEMO
8. CHƯƠNG 5: ĐÁNH GIÁ & CẢI TIẾN
9. KẾT LUẬN
10. TÀI LIỆU THAM KHẢO
```

---

## 2. TEMPLATE CHO TỪNG PHẦN

### 2.1. LỜI NÓI ĐẦU (Preface)

**Độ dài:** 1-2 trang

**Nội dung:**
- Bối cảnh và lý do chọn đề tài
- Mục tiêu của đồ án
- Phạm vi nghiên cứu
- Cấu trúc báo cáo (tóm tắt nội dung từng chương)
- Lời cảm ơn (nếu có)

**Ví dụ cấu trúc đoạn văn:**

```markdown
## LỜI NÓI ĐẦU

Trong bối cảnh giáo dục hiện đại, việc ứng dụng công nghệ thông tin vào quản lý
và tổ chức quá trình học tập đã trở thành xu hướng tất yếu. Hệ thống B-Learning
(Blended Learning) kết hợp giữa học trực tuyến và học trực tiếp tại lớp, mang lại
sự linh hoạt và hiệu quả cao trong giảng dạy.

Đồ án này được thực hiện với mục tiêu thiết kế một cơ sở dữ liệu hoàn chỉnh cho
hệ thống B-Learning, hỗ trợ quản lý courses, assignments, submissions, grading,
và các tính năng tương tác giữa giảng viên và học viên.

**Cấu trúc báo cáo:**
- **Chương 1:** Tổng quan về hệ thống B-Learning và yêu cầu thiết kế
- **Chương 2:** Phân tích yêu cầu chức năng chi tiết
- **Chương 3:** Thiết kế cơ sở dữ liệu với 31 bảng và các mối quan hệ
- **Chương 4:** Triển khai hệ thống và demo thực tế
- **Chương 5:** Đánh giá kết quả và đề xuất cải tiến

Em xin chân thành cảm ơn Thầy [Tên giảng viên] đã hướng dẫn và hỗ trợ em trong
suốt quá trình thực hiện đồ án.
```

---

### 2.2. MỤC LỤC (Table of Contents)

**Format:** Sử dụng hệ thống đánh số thập phân (1, 1.1, 1.1.1, 1.1.1.1)

**Ví dụ:**

```markdown
## MỤC LỤC

**LỜI NÓI ĐẦU** .......................................... 1

**CHƯƠNG 1: TỔNG QUAN HỆ THỐNG** ............................ 3
1.1. Giới thiệu về B-Learning ............................... 3
    1.1.1. Khái niệm B-Learning ............................. 3
    1.1.2. Lợi ích của B-Learning ........................... 4
1.2. Mục tiêu đồ án ......................................... 5
    1.2.1. Mục tiêu tổng quát ............................... 5
    1.2.2. Mục tiêu cụ thể .................................. 5
1.3. Phạm vi nghiên cứu ..................................... 6

**CHƯƠNG 2: PHÂN TÍCH YÊU CẦU** ............................ 7
2.1. Yêu cầu chức năng ...................................... 7
    2.1.1. Quản lý người dùng ............................... 7
    2.1.2. Quản lý khóa học ................................. 8
    2.1.3. Quản lý bài tập và chấm điểm ..................... 9
2.2. Yêu cầu phi chức năng .................................. 10
    2.2.1. Hiệu năng ........................................ 10
    2.2.2. Bảo mật .......................................... 11

**CHƯƠNG 3: THIẾT KẾ CƠ SỞ DỮ LIỆU** ....................... 12
3.1. Sơ đồ ERD (Entity Relationship Diagram) ................ 12
3.2. Sơ đồ RM (Relational Model) ............................ 14
3.3. Đặc tả chi tiết các bảng ............................... 15
    3.3.1. User Management (6 bảng) ......................... 15
    3.3.2. Course Management (7 bảng) ....................... 20
    3.3.3. Class & Enrollment (2 bảng) ...................... 27
    3.3.4. Learning Progress (3 bảng) ....................... 29
    3.3.5. Assessments (4 bảng) ............................. 32
    3.3.6. Assignments (2 bảng) ............................. 36
    3.3.7. Grading (2 bảng) ................................. 38
    3.3.8. Interaction (3 bảng) ............................. 40
    3.3.9. System (1 bảng) .................................. 43

**CHƯƠNG 4: TRIỂN KHAI & DEMO** ............................ 44
4.1. Công nghệ sử dụng ...................................... 44
4.2. Cấu trúc hệ thống ...................................... 45
4.3. Demo các chức năng ..................................... 46

**CHƯƠNG 5: ĐÁNH GIÁ & CẢI TIẾN** .......................... 50
5.1. So sánh với thiết kế cũ ................................ 50
5.2. Ưu điểm của thiết kế mới ............................... 52
5.3. Hạn chế và hướng cải tiến .............................. 53

**KẾT LUẬN** ................................................ 55

**TÀI LIỆU THAM KHẢO** ...................................... 56
```

---

### 2.3. CHƯƠNG (Chapters)

**Quy tắc:**
- Tên chương: IN HOA, đậm
- Đánh số: CHƯƠNG 1, CHƯƠNG 2, ...
- Mỗi chương bắt đầu trang mới
- Độ dài mỗi chương: 8-15 trang

**Template cấu trúc chương:**

```markdown
# CHƯƠNG [Số]: [TÊN CHƯƠNG IN HOA]

## [Số].1. [Mục chính 1]

### [Số].1.1. [Mục con 1.1]

Nội dung mô tả chi tiết với các đoạn văn logic, rõ ràng.

**Ví dụ minh họa:**

[Code/bảng/hình ảnh]

**Giải thích:**
- Điểm 1
- Điểm 2
- Điểm 3

### [Số].1.2. [Mục con 1.2]

...

## [Số].2. [Mục chính 2]

...
```

---

### 2.4. ĐẶC TẢ CHI TIẾT BẢNG (Table Specifications)

**Format chuẩn:**

```markdown
### 3.3.1. Bảng USERS (Người dùng)

**Mục đích:** Lưu trữ thông tin về tất cả người dùng trong hệ thống (students, instructors, admins).

**Khóa chính:** user_id (UUID)

**Khóa ngoại:** Không có

**Đặc tả chi tiết:**

| Tên trường | Kiểu dữ liệu | Ràng buộc | Mô tả |
|------------|--------------|-----------|-------|
| user_id | UUID | PK, NOT NULL | Mã định danh duy nhất của người dùng |
| email | VARCHAR(255) | UNIQUE, NOT NULL | Email đăng nhập, phải duy nhất |
| password_hash | VARCHAR(255) | NOT NULL | Mật khẩu đã được băm (hashed) |
| full_name | VARCHAR(255) | NOT NULL | Họ và tên đầy đủ |
| avatar_url | TEXT | NULL | URL ảnh đại diện |
| bio | TEXT | NULL | Tiểu sử, giới thiệu bản thân |
| date_of_birth | DATE | NULL | Ngày sinh |
| phone | VARCHAR(20) | NULL | Số điện thoại |
| address | TEXT | NULL | Địa chỉ |
| status | VARCHAR(20) | NOT NULL, DEFAULT 'ACTIVE' | Trạng thái: ACTIVE, INACTIVE, SUSPENDED |
| created_at | TIMESTAMP | NOT NULL, DEFAULT NOW() | Thời gian tạo tài khoản |
| updated_at | TIMESTAMP | NOT NULL, DEFAULT NOW() | Thời gian cập nhật cuối |

**Ví dụ dữ liệu:**

| user_id | email | full_name | status |
|---------|-------|-----------|--------|
| 20000001-... | nvkiet@example.com | Nguyễn Văn Kiệt | ACTIVE |
| 20000002-... | lthanh@example.com | Lê Thị Thanh | ACTIVE |

**Quan hệ:**
- 1 USER có nhiều ENROLLMENTS (1:N với bảng Enrollments)
- 1 USER có nhiều SUBMISSIONS (1:N với bảng AssignmentSubmissions)
- 1 USER có nhiều THREADS (1:N với bảng DiscussionThreads)

**Indexes:**
- PRIMARY KEY trên user_id
- UNIQUE INDEX trên email
- INDEX trên status (để filter nhanh)

**Trigger/Stored Procedure:**
- Trigger `update_users_updated_at`: Tự động cập nhật updated_at khi có thay đổi
```

---

### 2.5. SƠ ĐỒ ERD (Entity Relationship Diagram)

**Yêu cầu:**
- Sử dụng ký hiệu chuẩn (crow's foot notation hoặc UML)
- Hiển thị tất cả entities và relationships
- Đánh dấu rõ khóa chính (PK), khóa ngoại (FK)
- Phân loại relationships: 1:1, 1:N, N:N

**Chú thích hình ảnh:**

```markdown
**Hình 3.1:** Sơ đồ ERD tổng quan hệ thống B-Learning (31 entities)

[Hình ảnh ERD]

**Giải thích:**
- Hệ thống gồm 9 nhóm chức năng chính
- User Management: Quản lý người dùng và phân quyền
- Course Management: Quản lý khóa học, modules, lectures
- Assessments: Quản lý quiz, câu hỏi, bài thi
- ...
```

---

### 2.6. DANH SÁCH BẢNG (Table Summary)

**Template:**

```markdown
## 3.2. Tổng quan các bảng trong hệ thống

Hệ thống B-Learning được thiết kế với **31 bảng** được phân loại thành 9 nhóm chức năng:

### 3.2.1. User Management (Quản lý người dùng) - 6 bảng

| STT | Tên bảng | Mô tả | Số trường |
|-----|----------|-------|-----------|
| 1 | Users | Lưu thông tin người dùng | 12 |
| 2 | Roles | Định nghĩa các vai trò | 5 |
| 3 | UserRoles | Gán vai trò cho người dùng | 4 |
| 4 | Permissions | Quyền hạn trong hệ thống | 5 |
| 5 | RolePermissions | Gán quyền cho vai trò | 4 |
| 6 | UserSettings | Cài đặt cá nhân | 6 |

### 3.2.2. Course Management (Quản lý khóa học) - 7 bảng

| STT | Tên bảng | Mô tả | Số trường |
|-----|----------|-------|-----------|
| 7 | Categories | Danh mục khóa học | 6 |
| 8 | Tags | Tags cho khóa học | 5 |
| 9 | Courses | Thông tin khóa học | 15 |
| 10 | CourseTags | Gán tags cho khóa học | 3 |
| 11 | Modules | Modules trong khóa học | 8 |
| 12 | Lectures | Lectures/assignments trong module | 10 |
| 13 | Resources | Tài nguyên học tập | 10 |

**Tổng cộng:** 31 bảng với hơn 250 trường dữ liệu
```

---

## 3. STYLE GUIDE (Hướng dẫn viết)

### 3.1. Ngôn ngữ

**Tiếng Việt:**
- Sử dụng tiếng Việt chính thức, học thuật
- Dùng "chúng tôi" hoặc câu bị động (ví dụ: "Hệ thống được thiết kế...")
- KHÔNG dùng "tôi", "mình", "em"

**Thuật ngữ tiếng Anh:**
- Giữ nguyên thuật ngữ kỹ thuật: database, primary key, foreign key, trigger, stored procedure
- Thêm giải thích tiếng Việt trong ngoặc khi xuất hiện lần đầu
  - Ví dụ: "Primary Key (khóa chính)"
  - Ví dụ: "Trigger (trình kích hoạt tự động)"

### 3.2. Cấu trúc câu

**Tốt:**
```
Bảng Users lưu trữ thông tin của tất cả người dùng trong hệ thống, bao gồm
students (học viên), instructors (giảng viên), và admins (quản trị viên).
```

**Không tốt:**
```
Bảng Users này em dùng để lưu thông tin người dùng.
```

### 3.3. Định dạng văn bản

**Chữ đậm (Bold):**
- Tên chương: **CHƯƠNG 1: TỔNG QUAN HỆ THỐNG**
- Từ khóa quan trọng: **Khóa chính**, **Ràng buộc NOT NULL**

**Chữ nghiêng (Italic):**
- Thuật ngữ tiếng Anh lần đầu: *Primary Key*
- Nhấn mạnh: *Lưu ý: Trường này không được NULL*

**Monospace (code):**
- Tên bảng: `Users`, `Courses`, `Enrollments`
- Tên trường: `user_id`, `email`, `created_at`
- Câu lệnh SQL: `CREATE TABLE ...`

**Danh sách:**
- Sử dụng gạch đầu dòng (-) hoặc số (1., 2., 3.)
- Thụt lề nhất quán

### 3.4. Bảng (Tables)

**Format Markdown:**
```markdown
| Tên trường | Kiểu dữ liệu | Ràng buộc | Mô tả |
|------------|--------------|-----------|-------|
| user_id | UUID | PK, NOT NULL | Mã định danh duy nhất |
| email | VARCHAR(255) | UNIQUE, NOT NULL | Email đăng nhập |
```

**Yêu cầu:**
- Căn lề đều
- Header đậm tự động (Markdown)
- Border đầy đủ

### 3.5. Code Blocks

**SQL:**
````markdown
```sql
CREATE TABLE Users (
    user_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    status VARCHAR(20) DEFAULT 'ACTIVE' NOT NULL,
    created_at TIMESTAMP DEFAULT NOW() NOT NULL,
    updated_at TIMESTAMP DEFAULT NOW() NOT NULL
);

CREATE INDEX idx_users_email ON Users(email);
CREATE INDEX idx_users_status ON Users(status);
```
````

**Giải thích code:**
- Luôn thêm comment giải thích sau mỗi code block
- Giải thích mục đích, tham số, kết quả

---

## 4. EXAMPLES (Ví dụ mẫu)

### 4.1. Ví dụ viết đoạn văn giới thiệu

```markdown
## 1.1. Giới thiệu về B-Learning

B-Learning (Blended Learning - Học tập kết hợp) là mô hình giáo dục kết hợp
giữa học trực tuyến (online) và học trực tiếp tại lớp (in-person). Mô hình
này mang lại sự linh hoạt cao cho học viên, cho phép họ học tập mọi lúc, mọi
nơi thông qua các bài giảng video, tài liệu đọc, và bài tập trực tuyến, đồng
thời vẫn duy trì tương tác trực tiếp với giảng viên và bạn học tại lớp.

Theo nghiên cứu của Garrison & Kanuka (2004), B-Learning không chỉ đơn thuần
là sự kết hợp giữa hai hình thức học tập, mà còn là sự tích hợp sâu sắc các
công nghệ giáo dục nhằm tối ưu hóa trải nghiệm học tập. Trong bối cảnh đại
dịch COVID-19, mô hình B-Learning đã chứng minh hiệu quả vượt trội trong việc
duy trì hoạt động giảng dạy liên tục.

**Các thành phần chính của B-Learning:**
- **Online Learning:** Video lectures, e-books, quizzes, assignments
- **In-person Learning:** Classroom discussions, labs, group projects
- **Learning Management System (LMS):** Nền tảng quản lý và theo dõi tiến độ
```

### 4.2. Ví dụ đặc tả yêu cầu chức năng

```markdown
## 2.1.3. Quản lý bài tập và chấm điểm

### 2.1.3.1. Yêu cầu chức năng

**RF-03.01: Tạo assignment**
- **Actor:** Instructor
- **Mô tả:** Instructor có thể tạo assignment mới cho một lecture trong course
- **Input:**
  - Tiêu đề assignment
  - Mô tả chi tiết (content, requirements)
  - Điểm tối đa (max_points)
  - Thời hạn nộp (due_date)
- **Output:** Assignment được tạo thành công, hiển thị cho students enrolled
- **Ràng buộc:**
  - Instructor phải có quyền INSTRUCTOR trong course
  - max_points > 0
  - due_date phải sau thời gian hiện tại

**RF-03.02: Submit assignment**
- **Actor:** Student
- **Mô tả:** Student nộp bài làm cho assignment
- **Input:**
  - Content (text, code, hoặc link)
  - File đính kèm (nếu có)
- **Output:** Submission được lưu, submission_number tự động tăng
- **Ràng buộc:**
  - Student phải enrolled vào course
  - Lecture phải có type = 'ASSIGNMENT'
  - Có thể submit nhiều lần (submission_number tăng dần)

**RF-03.03: Grade submission**
- **Actor:** Instructor
- **Mô tả:** Instructor chấm điểm submission của student
- **Input:**
  - Điểm (score)
  - Feedback (nhận xét)
- **Output:** Submission được cập nhật với điểm và feedback
- **Ràng buộc:**
  - 0 ≤ score ≤ max_points
  - Chỉ có thể grade submission đã nộp (status = 'SUBMITTED')
```

### 4.3. Ví dụ so sánh (Old vs New)

```markdown
## 5.1.2. So sánh cấu trúc bảng Users

| Tiêu chí | Thiết kế cũ | Thiết kế mới | Cải tiến |
|----------|-------------|--------------|----------|
| Số trường | 8 | 12 | +4 trường (avatar_url, bio, phone, address) |
| Khóa chính | INT (auto-increment) | UUID | UUID an toàn hơn, phân tán tốt hơn |
| Email validation | Không có | UNIQUE constraint + CHECK | Đảm bảo tính duy nhất |
| Status | Không có | ENUM (ACTIVE, INACTIVE, SUSPENDED) | Quản lý trạng thái rõ ràng |
| Timestamps | created_at | created_at, updated_at | Theo dõi lịch sử thay đổi |

**Nhận xét:**
- Thiết kế mới bổ sung thông tin cá nhân chi tiết hơn (avatar, bio, phone, address)
- Chuyển từ INT sang UUID giúp tránh lộ thông tin số lượng users
- Thêm status giúp quản lý tài khoản linh hoạt (suspend, deactivate)
- Thêm updated_at giúp audit trail tốt hơn
```

---

## 5. CHECKLIST KIỂM TRA CHẤT LƯỢNG

Trước khi hoàn thành báo cáo, kiểm tra các tiêu chí sau:

### 5.1. Nội dung

- [ ] Tất cả 31 bảng đều được mô tả chi tiết
- [ ] Mỗi bảng có: mục đích, khóa chính, khóa ngoại, đặc tả trường, ví dụ, quan hệ
- [ ] Có sơ đồ ERD và RM rõ ràng
- [ ] Có ít nhất 3-5 ví dụ dữ liệu cho mỗi bảng quan trọng
- [ ] Giải thích đầy đủ các relationships (1:1, 1:N, N:N)

### 5.2. Định dạng

- [ ] Sử dụng heading đúng cấp (H1, H2, H3, H4)
- [ ] Đánh số nhất quán (1, 1.1, 1.1.1)
- [ ] Bảng có border đầy đủ, căn lề đều
- [ ] Code blocks có syntax highlighting
- [ ] Hình ảnh có caption và số thứ tự

### 5.3. Ngôn ngữ

- [ ] Không có lỗi chính tả
- [ ] Sử dụng "chúng tôi" hoặc câu bị động
- [ ] Thuật ngữ tiếng Anh có giải thích tiếng Việt lần đầu
- [ ] Câu văn mạch lạc, rõ ràng
- [ ] Không dùng ngôn ngữ thân mật (tôi, mình, em)

### 5.4. Độ dài

- [ ] Báo cáo chính: 40-60 trang
- [ ] Mỗi chương: 8-15 trang
- [ ] Lời nói đầu: 1-2 trang
- [ ] Kết luận: 1-2 trang

### 5.5. Tính đầy đủ

- [ ] Có tài liệu tham khảo (ít nhất 5 nguồn)
- [ ] Có danh mục hình ảnh/bảng biểu
- [ ] Có FAQ (50-70 câu hỏi)
- [ ] Có so sánh với thiết kế cũ
- [ ] Có đề xuất cải tiến/mở rộng

---

## 6. TIMELINE ƯỚC TÍNH

| Giai đoạn | Thời gian | Nội dung |
|-----------|-----------|----------|
| Week 1 | 7 ngày | Viết CHƯƠNG 1 + CHƯƠNG 2 (20 trang) |
| Week 2 | 7 ngày | Viết CHƯƠNG 3 - Đặc tả 31 bảng (25 trang) |
| Week 3 | 7 ngày | Viết CHƯƠNG 4 + CHƯƠNG 5 + FAQ (20 trang) |
| Week 4 | 7 ngày | Review, chỉnh sửa, hoàn thiện hình ảnh |

**Tổng thời gian:** 3-4 tuần (20-28 ngày)

---

## 7. TÀI LIỆU THAM KHẢO MẪU

**Format APA:**

```
[1] Garrison, D. R., & Kanuka, H. (2004). Blended learning: Uncovering its
    transformative potential in higher education. The Internet and Higher
    Education, 7(2), 95-105.

[2] PostgreSQL Documentation. (2024). PostgreSQL 14 Official Documentation.
    Retrieved from https://www.postgresql.org/docs/14/

[3] Elmasri, R., & Navathe, S. B. (2016). Fundamentals of Database Systems
    (7th ed.). Pearson.

[4] Date, C. J. (2003). An Introduction to Database Systems (8th ed.).
    Addison-Wesley.

[5] Connolly, T., & Begg, C. (2014). Database Systems: A Practical Approach
    to Design, Implementation, and Management (6th ed.). Pearson.
```

---

**KẾT LUẬN:**

Template này cung cấp framework hoàn chỉnh để viết báo cáo cơ sở dữ liệu
B-Learning theo chuẩn học thuật Việt Nam. Tuân thủ template này sẽ đảm bảo
báo cáo có cấu trúc rõ ràng, nội dung đầy đủ, và định dạng chuyên nghiệp.

---

**Ngày tạo:** 2025-12-01
**Tác giả:** Nguyễn Văn Kiệt - CNTT1-K63
**Nguồn tham khảo:** Scripts/Tham_khao.pdf
