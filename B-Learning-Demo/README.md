# B-Learning Demo

Demo 2 giao diện web để thao tác với database B-Learning.

## 📋 Mục Lục

1. [Giới thiệu](#giới-thiệu)
2. [Tech Stack](#tech-stack)
3. [Cấu trúc dự án](#cấu-trúc-dự-án)
4. [Yêu cầu hệ thống](#yêu-cầu-hệ-thống)
5. [Cài đặt](#cài-đặt)
6. [Sử dụng](#sử-dụng)
7. [API Documentation](#api-documentation)
8. [Giải thích Code](#giải-thích-code)

---

## 🎯 Giới thiệu

Dự án demo 2 giao diện web để quản lý database B-Learning:

### **Page 1: Course Management (1 Bảng)**
- ✅ CRUD operations cho bảng `Course`
- ✅ Tìm kiếm và filter
- ✅ Pagination
- ✅ SQL queries trực tiếp (không dùng ORM)

### **Page 2: Submission Management (Nhiều Bảng)**
- ✅ JOIN 5+ bảng: `AssignmentSubmission`, `User`, `Assignment`, `Course`, `GradeBook`
- ✅ Chấm điểm assignment submissions
- ✅ Thống kê (tổng submissions, đã chấm, chưa chấm, điểm TB)
- ✅ Filter nâng cao

---

## 🛠️ Tech Stack

### Backend
- **Framework**: Python 3.11+ / FastAPI
- **Database**: PostgreSQL 14+
- **DB Driver**: psycopg2 (raw SQL queries)
- **API**: RESTful với auto-generated docs (Swagger UI)

### Frontend
- **Framework**: Next.js 14 / React 18
- **Language**: TypeScript
- **Styling**: Tailwind CSS
- **HTTP Client**: Axios

---

## 📁 Cấu trúc dự án

```
B-Learning-Demo/
├── backend/                    # Python FastAPI backend
│   ├── app/
│   │   ├── main.py            # FastAPI app
│   │   ├── models/            # Pydantic schemas
│   │   │   ├── course.py
│   │   │   └── submission.py
│   │   └── routes/            # API endpoints
│   │       ├── courses.py     # Course CRUD
│   │       └── submissions.py # Submission management
│   ├── config/
│   │   └── database.py        # PostgreSQL connection
│   ├── requirements.txt       # Python dependencies
│   └── .env.example           # Environment variables template
│
├── frontend/                   # Next.js frontend
│   ├── app/
│   │   ├── layout.tsx         # Root layout
│   │   ├── page.tsx           # Home page
│   │   ├── courses/           # Page 1: Course Management
│   │   │   └── page.tsx
│   │   └── submissions/       # Page 2: Submission Management
│   │       └── page.tsx
│   ├── lib/
│   │   └── api.ts             # Axios client
│   ├── types/                 # TypeScript types
│   │   ├── course.ts
│   │   └── submission.ts
│   ├── package.json
│   └── .env.local             # Frontend env
│
└── README.md                   # Tài liệu này
```

---

## 💻 Yêu cầu hệ thống

### Phần mềm cần cài đặt:
- **Python**: 3.11 hoặc cao hơn
- **Node.js**: 18.x hoặc cao hơn
- **PostgreSQL**: 14 hoặc cao hơn
- **npm** hoặc **yarn**

### Database:
- Đã setup database B-Learning với schema đầy đủ
- Đã có seed data (courses, users, assignments, submissions)

---

## 🚀 Cài đặt

### 1. Clone repository (hoặc copy folder)

```bash
cd B-Learning-Demo
```

### 2. Setup Backend

```bash
cd backend

# Tạo virtual environment (khuyến nghị)
python -m venv venv

# Activate virtual environment
# Windows:
venv\Scripts\activate
# Linux/Mac:
source venv/bin/activate

# Cài đặt dependencies
pip install -r requirements.txt

# Tạo file .env từ template
cp .env.example .env

# Chỉnh sửa .env với thông tin database của bạn
#Ví dụ:
# DB_HOST=localhost
# DB_PORT=5432
# DB_NAME=b_learning
# DB_USER=postgres
# DB_PASSWORD=your_password
```

### 3. Setup Frontend

```bash
cd ../frontend

# Cài đặt dependencies
npm install

# Tạo file .env.local
echo "NEXT_PUBLIC_API_URL=http://localhost:8000" > .env.local
```

---

## ▶️ Sử dụng

### 1. Chạy Backend

```bash
cd backend

# Activate virtual environment (nếu chưa)
# Windows: venv\Scripts\activate
# Linux/Mac: source venv/bin/activate

# Chạy server
python -m uvicorn app.main:app --reload --host 0.0.0.0 --port 8000
```

Backend sẽ chạy tại: **http://localhost:8000**

**API Docs:**
- Swagger UI: http://localhost:8000/docs
- ReDoc: http://localhost:8000/redoc

### 2. Chạy Frontend

```bash
cd frontend

# Chạy dev server
npm run dev
```

Frontend sẽ chạy tại: **http://localhost:3000**

### 3. Truy cập ứng dụng

1. Mở browser: http://localhost:3000
2. Chọn một trong 2 pages:
   - **Courses**: Quản lý khóa học (1 bảng)
   - **Submissions**: Quản lý bài nộp (nhiều bảng)

---

## 📚 API Documentation

### Course Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/courses` | Lấy danh sách courses |
| GET | `/api/courses/{id}` | Lấy chi tiết 1 course |
| POST | `/api/courses` | Tạo course mới |
| PUT | `/api/courses/{id}` | Cập nhật course |
| DELETE | `/api/courses/{id}` | Xóa course |

**Query Parameters (GET /api/courses):**
- `status`: Filter by status (DRAFT/PUBLISHED/ARCHIVED)
- `category`: Filter by category
- `difficulty`: Filter by difficulty level
- `search`: Search in title and description
- `limit`: Number of results (default 50, max 100)
- `offset`: Pagination offset

**Example Request:**
```bash
# Lấy tất cả courses PUBLISHED
GET http://localhost:8000/api/courses?status=PUBLISHED&limit=10

# Tạo course mới
POST http://localhost:8000/api/courses
Content-Type: application/json

{
  "code": "CS101",
  "title": "Introduction to Computer Science",
  "description": "Basic CS course",
  "difficulty_level": "BEGINNER",
  "status": "DRAFT"
}
```

### Submission Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/submissions` | Lấy danh sách submissions (JOIN nhiều bảng) |
| GET | `/api/submissions/{id}` | Lấy chi tiết submission |
| PUT | `/api/submissions/{id}/grade` | Chấm điểm submission |
| GET | `/api/submissions/stats/overview` | Thống kê submissions |

**Query Parameters (GET /api/submissions):**
- `course_id`: Filter by course
- `assignment_id`: Filter by assignment
- `status`: Filter by status
- `is_late`: Filter late submissions (true/false)
- `student_email`: Search student
- `limit`, `offset`: Pagination

**Example Request:**
```bash
# Lấy submissions chưa chấm
GET http://localhost:8000/api/submissions?status=SUBMITTED&limit=20

# Chấm điểm
PUT http://localhost:8000/api/submissions/{id}/grade
Content-Type: application/json

{
  "manual_score": 85.5,
  "feedback": "Bài làm tốt, nhưng cần cải thiện phần X"
}
```

---

## 💡 Giải thích Code

### Backend Architecture

#### 1. Database Connection (`config/database.py`)

```python
# Sử dụng connection pooling để tối ưu hiệu suất
connection_pool = psycopg2.pool.SimpleConnectionPool(minconn=1, maxconn=10)

# Context manager để tự động trả connection về pool
@contextmanager
def get_db_cursor(commit=True):
    with get_db_connection() as conn:
        cursor = conn.cursor(cursor_factory=RealDictCursor)
        try:
            yield cursor
            if commit:
                conn.commit()
        except Exception as e:
            conn.rollback()
            raise e
```

**Lợi ích:**
- Connection pooling giúp tái sử dụng kết nối → tăng hiệu suất
- RealDictCursor trả về dict thay vì tuple → dễ làm việc
- Auto commit/rollback → an toàn

#### 2. Raw SQL Queries

**Course CRUD (1 bảng):**
```python
# Tạo course mới
query = """
    INSERT INTO "Course" (code, title, description, ...)
    VALUES (%s, %s, %s, ...)
    RETURNING *
"""
new_course = execute_query_with_returning(query, params)
```

**Submission JOIN (nhiều bảng):**
```python
query = """
    SELECT
        asub.*,  -- AssignmentSubmission
        u.email as student_email,  -- User
        a.title as assignment_title,  -- Assignment
        c.code as course_code  -- Course
    FROM "AssignmentSubmission" asub
    INNER JOIN "User" u ON asub.user_id = u.user_id
    INNER JOIN "Assignment" a ON asub.assignment_id = a.assignment_id
    INNER JOIN "Course" c ON a.course_id = c.course_id
    WHERE asub.status = %s
"""
```

**Tại sao dùng raw SQL thay vì ORM?**
- ✅ Control hoàn toàn query → tối ưu performance
- ✅ JOIN nhiều bảng dễ dàng
- ✅ Không bị overhead của ORM
- ✅ Học được SQL thực tế

#### 3. Pydantic Schemas

```python
class CourseCreate(BaseModel):
    code: str = Field(..., min_length=2, max_length=50)
    title: str = Field(..., min_length=3, max_length=200)
    difficulty_level: Optional[str] = Field('BEGINNER')

    @field_validator('difficulty_level')
    def validate_difficulty(cls, v):
        allowed = ['BEGINNER', 'INTERMEDIATE', 'ADVANCED', 'EXPERT']
        if v not in allowed:
            raise ValueError(f'Must be one of: {allowed}')
        return v
```

**Lợi ích:**
- Validation tự động
- Type hints cho TypeScript
- Auto-generate API docs

### Frontend Architecture

#### 1. API Client (`lib/api.ts`)

```typescript
const apiClient = axios.create({
  baseURL: process.env.NEXT_PUBLIC_API_URL,
  timeout: 10000,
});

// Request interceptor (có thể thêm auth token)
apiClient.interceptors.request.use(config => {
  // config.headers.Authorization = `Bearer ${token}`;
  return config;
});

// Response interceptor (xử lý errors)
apiClient.interceptors.response.use(
  response => response,
  error => {
    console.error('API Error:', error.response?.data);
    return Promise.reject(error);
  }
);
```

#### 2. Type Safety với TypeScript

```typescript
// Types tương ứng với Pydantic schemas
interface Course {
  course_id: string;
  code: string;
  title: string;
  status: 'DRAFT' | 'PUBLISHED' | 'ARCHIVED';
  // ...
}

// Type-safe API calls
const response = await apiClient.get<CourseListResponse>('/api/courses');
const courses: Course[] = response.data.courses;
```

#### 3. React Hooks Pattern

```typescript
const [courses, setCourses] = useState<Course[]>([]);
const [loading, setLoading] = useState(false);

const fetchCourses = async () => {
  setLoading(true);
  try {
    const response = await apiClient.get<CourseListResponse>('/api/courses');
    setCourses(response.data.courses);
  } catch (error) {
    // Handle error
  } finally {
    setLoading(false);
  }
};

useEffect(() => {
  fetchCourses();
}, [page, filters]);  // Re-fetch khi page hoặc filters thay đổi
```

---

## 🎓 Điểm nổi bật của demo

### 1. **Course Management (1 Bảng)**
- ✅ **CRUD đầy đủ**: Create, Read, Update, Delete
- ✅ **Validation**: Check unique code, validate input
- ✅ **Search**: Tìm kiếm full-text trong title và description
- ✅ **Filters**: Lọc theo status, difficulty, category
- ✅ **Pagination**: Hỗ trợ phân trang

**SQL Query mẫu:**
```sql
SELECT * FROM "Course"
WHERE title ILIKE '%pattern%'
  AND status = 'PUBLISHED'
ORDER BY created_at DESC
LIMIT 10 OFFSET 0;
```

### 2. **Submission Management (Nhiều Bảng)**
- ✅ **Complex JOIN**: 5 bảng (AssignmentSubmission, User, Assignment, Course, GradeBook)
- ✅ **Aggregation**: Thống kê (COUNT, AVG)
- ✅ **Business Logic**: Tính điểm tự động, penalty cho nộp trễ
- ✅ **Filter cascading**: Filter theo course → assignment → status

**SQL Query mẫu:**
```sql
SELECT
    asub.assignment_submission_id,
    asub.final_score,
    u.email as student_email,
    CONCAT(u.first_name, ' ', u.last_name) as student_name,
    a.title as assignment_title,
    c.code as course_code
FROM "AssignmentSubmission" asub
INNER JOIN "User" u ON asub.user_id = u.user_id
INNER JOIN "Assignment" a ON asub.assignment_id = a.assignment_id
INNER JOIN "Course" c ON a.course_id = c.course_id
LEFT JOIN "User" grader ON asub.graded_by = grader.user_id
WHERE asub.status = 'SUBMITTED'
ORDER BY asub.submitted_at DESC;
```

---

## 🐛 Troubleshooting

### Backend không connect được database?
```bash
# Kiểm tra PostgreSQL đang chạy
# Windows: Check Services
# Linux: sudo systemctl status postgresql

# Test connection
psql -h localhost -U postgres -d b_learning

# Kiểm tra .env file
cat backend/.env
```

### Frontend không gọi được API?
```bash
# Kiểm tra backend đang chạy
curl http://localhost:8000/health

# Kiểm tra CORS
# Backend đã enable CORS cho http://localhost:3000

# Kiểm tra .env.local
cat frontend/.env.local
```

### Port đã được sử dụng?
```bash
# Backend (8000):
# Windows: netstat -ano | findstr :8000
# Linux: lsof -i :8000

# Frontend (3000):
# Thay đổi port: npm run dev -- -p 3001
```

---

## 📝 Notes

- **SQL Injection Prevention**: Tất cả queries đều sử dụng parameterized queries (`%s`)
- **Transaction Safety**: Auto commit/rollback trong context manager
- **Error Handling**: Try-catch ở mọi API calls
- **Type Safety**: TypeScript + Pydantic validation
- **Responsive UI**: Tailwind CSS responsive classes

---

## 👨‍💻 Author

**Nguyễn Văn Kiệt - CNTT1-K63**

📧 Email: [your-email]
📅 Date: 2025-11-28

---

## 📄 License

Educational purpose - B-Learning Database Demo
