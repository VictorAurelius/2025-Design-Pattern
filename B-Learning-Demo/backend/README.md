# Backend - B-Learning Demo API

RESTful API backend sử dụng Python FastAPI và PostgreSQL.

## 📁 Cấu trúc

```
backend/
├── app/
│   ├── main.py              # FastAPI application
│   ├── models/              # Pydantic schemas
│   │   ├── course.py        # Course schemas
│   │   └── submission.py    # Submission schemas
│   └── routes/              # API endpoints
│       ├── courses.py       # /api/courses
│       └── submissions.py   # /api/submissions
├── config/
│   └── database.py          # PostgreSQL connection
├── requirements.txt         # Dependencies
└── .env.example            # Environment template
```

## 🚀 Quick Start

```bash
# 1. Tạo virtual environment
python -m venv venv
source venv/bin/activate  # Linux/Mac
# venv\Scripts\activate   # Windows

# 2. Cài dependencies
pip install -r requirements.txt

# 3. Tạo .env file
cp .env.example .env
# Chỉnh sửa .env với thông tin database

# 4. Chạy server
python -m uvicorn app.main:app --reload
```

API sẽ chạy tại: http://localhost:8000

**Docs:**
- Swagger UI: http://localhost:8000/docs
- ReDoc: http://localhost:8000/redoc

## 📝 Environment Variables

```env
# Database
DB_HOST=localhost
DB_PORT=5432
DB_NAME=b_learning
DB_USER=postgres
DB_PASSWORD=your_password

# API
API_HOST=0.0.0.0
API_PORT=8000
API_RELOAD=true

# CORS
CORS_ORIGINS=http://localhost:3000
```

## 📚 API Endpoints

### Courses

- `GET /api/courses` - List courses
- `GET /api/courses/{id}` - Get course
- `POST /api/courses` - Create course
- `PUT /api/courses/{id}` - Update course
- `DELETE /api/courses/{id}` - Delete course

### Submissions

- `GET /api/submissions` - List submissions (JOIN)
- `GET /api/submissions/{id}` - Get submission detail
- `PUT /api/submissions/{id}/grade` - Grade submission
- `GET /api/submissions/stats/overview` - Statistics

## 💡 Code Examples

### Raw SQL Query
```python
from config.database import execute_query

# Fetch courses
courses = execute_query(
    "SELECT * FROM \"Course\" WHERE status = %s",
    ('PUBLISHED',),
    fetch='all'
)

# Insert with RETURNING
new_course = execute_query_with_returning(
    """
    INSERT INTO "Course" (code, title)
    VALUES (%s, %s)
    RETURNING *
    """,
    ('CS101', 'Introduction to CS')
)
```

### JOIN Query
```python
query = """
    SELECT
        asub.*,
        u.email as student_email,
        a.title as assignment_title
    FROM "AssignmentSubmission" asub
    INNER JOIN "User" u ON asub.user_id = u.user_id
    INNER JOIN "Assignment" a ON asub.assignment_id = a.assignment_id
    WHERE asub.status = %s
"""
submissions = execute_query(query, ('SUBMITTED',), fetch='all')
```

## 🔒 Security

- ✅ SQL Injection prevention (parameterized queries)
- ✅ CORS enabled
- ✅ Input validation (Pydantic)
- ✅ Error handling
- ✅ Connection pooling

## 🐛 Debugging

```bash
# Test database connection
python -c "from config.database import init_connection_pool, execute_query; \
           init_connection_pool(); \
           print(execute_query('SELECT version()', fetch='one'))"

# Check API health
curl http://localhost:8000/health
```
