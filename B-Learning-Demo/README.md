# B-Learning Demo

Demo 2 giao diện web để thao tác với database B-Learning.

## 📋 Mục Lục

1. [Giới thiệu](#giới-thiệu)
2. [Tech Stack](#tech-stack)
3. [Cấu trúc dự án](#cấu-trúc-dự-án)
4. [Yêu cầu hệ thống](#yêu-cầu-hệ-thống)
5. [Cài đặt](#cài-đặt)
   - [Option 1: Docker (Khuyến nghị) 🐳](#option-1-docker-khuyến-nghị-)
   - [Option 2: Local Development](#option-2-local-development)
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
- ✅ Filters nâng cao

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

### DevOps
- **Docker**: Multi-container setup
- **Docker Compose**: Orchestration
- **Database Init**: Automated SQL scripts

---

## 📁 Cấu trúc dự án

```
B-Learning-Demo/
├── 🐳 docker-compose.yml       # Docker orchestration
├── 📄 DOCKER.md                # Docker deployment guide
├── 📄 DOCKER-QUICKSTART.md     # Docker quick start
├── 📄 Makefile                 # Quick commands
│
├── backend/                    # Python FastAPI backend
│   ├── 🐳 Dockerfile
│   ├── app/
│   │   ├── main.py            # FastAPI app
│   │   ├── models/            # Pydantic schemas
│   │   └── routes/            # API endpoints
│   ├── config/
│   │   └── database.py        # PostgreSQL connection
│   └── requirements.txt
│
├── frontend/                   # Next.js frontend
│   ├── 🐳 Dockerfile
│   ├── app/
│   │   ├── courses/           # Page 1: Course Management
│   │   └── submissions/       # Page 2: Submission Management
│   ├── lib/
│   ├── types/
│   └── package.json
│
├── db/                         # Database image
│   ├── 🐳 Dockerfile
│   └── init/                   # SQL initialization
│       ├── 01-schema.sql      # Tables
│       ├── 02-indexes.sql     # Indexes
│       ├── 03-constraints.sql # Constraints
│       └── 04-seed-data.sql   # Sample data
│
└── 📚 Documentation
    ├── README.md              # This file
    ├── QUICKSTART.md          # Local setup guide
    └── PROJECT-STRUCTURE.md   # Detailed structure
```

---

## 💻 Yêu cầu hệ thống

### Option 1: Docker (Khuyến nghị)
- **Docker**: 20.10+
- **Docker Compose**: 2.0+
- **RAM**: 4GB free
- **Disk**: 2GB free

### Option 2: Local Development
- **Python**: 3.11+
- **Node.js**: 18+
- **PostgreSQL**: 14+
- **npm** hoặc **yarn**

---

## 🚀 Cài đặt

### Option 1: Docker (Khuyến nghị) 🐳

**Đây là cách nhanh nhất - chỉ cần Docker!**

#### Quick Start (30 giây)

```bash
cd B-Learning-Demo

# Start all services (database + backend + frontend)
docker-compose up -d --build

# Xem logs
docker-compose logs -f
```

**Chờ ~2 phút** để database init và services start.

#### Truy cập:
- 🌐 Frontend: http://localhost:3000
- 🔌 Backend: http://localhost:8000
- 📖 API Docs: http://localhost:8000/docs
- 🗄️ Database: localhost:5432

#### Stop:
```bash
docker-compose down          # Stop (giữ data)
docker-compose down -v       # Stop và xóa data
```

#### Makefile Commands:
```bash
make up          # Start
make logs        # View logs
make test        # Test services
make down        # Stop
make clean       # Remove all
```

**📖 Chi tiết:** Xem [DOCKER.md](./DOCKER.md) và [DOCKER-QUICKSTART.md](./DOCKER-QUICKSTART.md)

---

### Option 2: Local Development

Nếu bạn muốn develop và sửa code, dùng local setup.

#### 1. Setup Database

```bash
# Tạo database
createdb -U postgres -h localhost b_learning

# Chạy SQL scripts (từ 98-B-Learning-Core)
psql -U postgres -d b_learning -f ../98-B-Learing-Core/sql/01-schema.sql
psql -U postgres -d b_learning -f ../98-B-Learing-Core/sql/02-indexes.sql
psql -U postgres -d b_learning -f ../98-B-Learing-Core/sql/03-constraints.sql
psql -U postgres -d b_learning -f ../98-B-Learing-Core/sql/04-seed-data.sql
```

#### 2. Setup Backend

```bash
cd backend

# Virtual environment
python -m venv venv
source venv/bin/activate  # Windows: venv\Scripts\activate

# Install
pip install -r requirements.txt

# Config
cp .env.example .env
# Sửa .env với DB credentials

# Run
python -m uvicorn app.main:app --reload
```

Backend: http://localhost:8000

#### 3. Setup Frontend

```bash
cd frontend

# Install
npm install

# Config
echo "NEXT_PUBLIC_API_URL=http://localhost:8000" > .env.local

# Run
npm run dev
```

Frontend: http://localhost:3000

**📖 Chi tiết:** Xem [QUICKSTART.md](./QUICKSTART.md)

---

## ▶️ Sử dụng

### 1. Truy cập ứng dụng

Mở browser: **http://localhost:3000**

### 2. Course Management (Page 1)

- Click "**Courses**" trên navbar
- Thử các chức năng:
  - ➕ Tạo course mới
  - 🔍 Search và filter
  - ✏️ Edit course
  - 🗑️ Delete course
  - 📄 Pagination

**Đặc điểm:**
- Thao tác với 1 bảng `Course`
- Raw SQL queries
- CRUD đầy đủ

### 3. Submission Management (Page 2)

- Click "**Submissions**" trên navbar
- Xem thống kê ở trên
- Thử các chức năng:
  - 📊 Xem danh sách submissions
  - 🎯 Grade submission (chấm điểm)
  - 🔍 Filter theo course, status, late/on-time
  - 👤 Search student

**Đặc điểm:**
- JOIN 5 bảng
- Complex queries
- Business logic (grading, penalties)

---

## 📚 API Documentation

### Swagger UI (Interactive)

http://localhost:8000/docs

### Course Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/courses` | Lấy danh sách courses |
| GET | `/api/courses/{id}` | Lấy chi tiết 1 course |
| POST | `/api/courses` | Tạo course mới |
| PUT | `/api/courses/{id}` | Cập nhật course |
| DELETE | `/api/courses/{id}` | Xóa course |

**Example:**
```bash
# Get all published courses
curl "http://localhost:8000/api/courses?status=PUBLISHED&limit=10"

# Create course
curl -X POST http://localhost:8000/api/courses \
  -H "Content-Type: application/json" \
  -d '{"code":"CS101","title":"Intro to CS","status":"DRAFT"}'
```

### Submission Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/submissions` | Lấy danh sách (JOIN) |
| GET | `/api/submissions/{id}` | Chi tiết submission |
| PUT | `/api/submissions/{id}/grade` | Chấm điểm |
| GET | `/api/submissions/stats/overview` | Thống kê |

**Example:**
```bash
# Get ungraded submissions
curl "http://localhost:8000/api/submissions?status=SUBMITTED"

# Grade submission
curl -X PUT http://localhost:8000/api/submissions/{id}/grade \
  -H "Content-Type: application/json" \
  -d '{"manual_score":85.5,"feedback":"Good work!"}'
```

---

## 💡 Giải thích Code

### Backend Architecture

#### 1. Connection Pooling

```python
# config/database.py
connection_pool = psycopg2.pool.SimpleConnectionPool(
    minconn=1, maxconn=10
)

@contextmanager
def get_db_cursor(commit=True):
    with get_db_connection() as conn:
        cursor = conn.cursor(cursor_factory=RealDictCursor)
        # Auto commit/rollback
```

**Lợi ích:**
- Reuse connections → faster
- RealDictCursor → returns dicts
- Auto cleanup

#### 2. Raw SQL Queries

**Simple query (1 table):**
```python
courses = execute_query(
    "SELECT * FROM \"Course\" WHERE status = %s",
    ('PUBLISHED',),
    fetch='all'
)
```

**Complex JOIN (nhiều bảng):**
```python
query = """
    SELECT
        asub.*,
        u.email as student_email,
        a.title as assignment_title,
        c.code as course_code
    FROM "AssignmentSubmission" asub
    INNER JOIN "User" u ON asub.user_id = u.user_id
    INNER JOIN "Assignment" a ON asub.assignment_id = a.assignment_id
    INNER JOIN "Course" c ON a.course_id = c.course_id
    WHERE asub.status = %s
"""
submissions = execute_query(query, ('SUBMITTED',), fetch='all')
```

**Tại sao Raw SQL?**
- ✅ Full control → optimize performance
- ✅ Easy to JOIN multiple tables
- ✅ No ORM overhead
- ✅ Learn real SQL

#### 3. Pydantic Validation

```python
class CourseCreate(BaseModel):
    code: str = Field(..., min_length=2, max_length=50)
    title: str = Field(..., min_length=3, max_length=200)

    @field_validator('code')
    def validate_code(cls, v):
        # Custom validation
        return v
```

### Frontend Architecture

#### 1. Type-safe API Calls

```typescript
const response = await apiClient.get<CourseListResponse>(
    '/api/courses'
);
const courses: Course[] = response.data.courses;
```

#### 2. React Hooks Pattern

```typescript
const [courses, setCourses] = useState<Course[]>([]);
const [loading, setLoading] = useState(false);

const fetchCourses = async () => {
    setLoading(true);
    try {
        const response = await apiClient.get<CourseListResponse>('/api/courses');
        setCourses(response.data.courses);
    } catch (error) {
        // Handle
    } finally {
        setLoading(false);
    }
};

useEffect(() => {
    fetchCourses();
}, [page, filters]);  // Re-fetch on change
```

---

## 🎓 Điểm nổi bật

### 1. Docker Support 🐳
- **One-command deployment**: `docker-compose up`
- **Automated database setup**: SQL scripts auto-run
- **Isolated environment**: No conflicts
- **Production-ready**: Health checks, restart policies

### 2. Course Management (1 Bảng)
- ✅ CRUD đầy đủ
- ✅ Search full-text
- ✅ Multiple filters
- ✅ Pagination

### 3. Submission Management (Nhiều Bảng)
- ✅ Complex JOIN (5 tables)
- ✅ Aggregation (stats)
- ✅ Business logic (grading)
- ✅ Filter cascading

---

## 🐛 Troubleshooting

### Docker Issues

```bash
# Port conflicts
docker-compose down
# Change ports in docker-compose.yml

# Container restart loop
docker-compose logs -f <service>

# Reset everything
docker-compose down -v
docker system prune -a
docker-compose up -d --build
```

### Local Issues

```bash
# Backend can't connect to DB
# Check .env file
cat backend/.env

# Frontend can't reach backend
# Check CORS in backend logs
# Check .env.local

# Port already in use
# Kill process or change port
```

**📖 More:** [DOCKER.md](./DOCKER.md) - Troubleshooting section

---

## 📊 Thống kê

- **Backend**: ~980 dòng Python
- **Frontend**: ~1030 dòng TypeScript/TSX
- **Tổng code**: ~2010 dòng
- **Docker configs**: 3 Dockerfiles + docker-compose
- **Documentation**: 6 markdown files

---

## 📝 Documentation Files

| File | Description |
|------|-------------|
| [README.md](./README.md) | Main documentation (this file) |
| [DOCKER.md](./DOCKER.md) | Docker deployment guide (detailed) |
| [DOCKER-QUICKSTART.md](./DOCKER-QUICKSTART.md) | Docker quick start (30 sec) |
| [QUICKSTART.md](./QUICKSTART.md) | Local setup guide (5 min) |
| [PROJECT-STRUCTURE.md](./PROJECT-STRUCTURE.md) | Project structure details |
| [Makefile](./Makefile) | Quick commands |

---

## 👨‍💻 Author

**Nguyễn Văn Kiệt - CNTT1-K63**

📅 Date: 2025-11-28
🐳 Docker support: Yes
📖 Full documentation: Yes

---

## 📄 License

Educational purpose - B-Learning Database Demo
