# 📁 Cấu trúc dự án B-Learning Demo

## 🌳 Sơ đồ thư mục

```
B-Learning-Demo/
│
├── 📄 README.md                    # Tài liệu chính (đầy đủ)
├── 📄 QUICKSTART.md                # Hướng dẫn nhanh (5 phút)
├── 📄 PROJECT-STRUCTURE.md         # File này
│
├── 📁 backend/                     # Python FastAPI Backend
│   ├── 📄 README.md                # Docs backend
│   ├── 📄 requirements.txt         # Python dependencies
│   ├── 📄 .env.example             # Template environment variables
│   │
│   ├── 📁 app/                     # Application code
│   │   ├── 📄 main.py              # FastAPI app entry point
│   │   │
│   │   ├── 📁 models/              # Pydantic schemas (validation)
│   │   │   ├── 📄 course.py        # Course schemas
│   │   │   └── 📄 submission.py    # Submission schemas
│   │   │
│   │   └── 📁 routes/              # API endpoints
│   │       ├── 📄 courses.py       # Course CRUD (1 bảng)
│   │       └── 📄 submissions.py   # Submissions (nhiều bảng JOIN)
│   │
│   └── 📁 config/
│       └── 📄 database.py          # PostgreSQL connection + pooling
│
└── 📁 frontend/                    # Next.js Frontend
    ├── 📄 README.md                # Docs frontend
    ├── 📄 package.json             # npm dependencies
    ├── 📄 tsconfig.json            # TypeScript config
    ├── 📄 tailwind.config.js       # Tailwind CSS config
    ├── 📄 next.config.js           # Next.js config
    ├── 📄 .env.local               # Environment variables
    │
    ├── 📁 app/                     # Next.js App Router
    │   ├── 📄 layout.tsx           # Root layout (header, footer)
    │   ├── 📄 page.tsx             # Home page
    │   ├── 📄 globals.css          # Global CSS
    │   │
    │   ├── 📁 courses/             # Page 1: Course Management
    │   │   └── 📄 page.tsx         # Courses page (CRUD 1 bảng)
    │   │
    │   └── 📁 submissions/         # Page 2: Submission Management
    │       └── 📄 page.tsx         # Submissions page (JOIN nhiều bảng)
    │
    ├── 📁 lib/
    │   └── 📄 api.ts               # Axios client (HTTP requests)
    │
    └── 📁 types/                   # TypeScript type definitions
        ├── 📄 course.ts            # Course types
        └── 📄 submission.ts        # Submission types
```

---

## 📊 File Statistics

### Backend
- **Total files**: 9
- **Python files**: 6
  - `main.py` - FastAPI app
  - `database.py` - DB connection
  - `courses.py` - Course routes
  - `submissions.py` - Submission routes
  - `course.py` - Course models
  - `submission.py` - Submission models

### Frontend
- **Total files**: 10
- **TypeScript/TSX files**: 7
  - `layout.tsx` - Root layout
  - `page.tsx` (home)
  - `courses/page.tsx` - Course management
  - `submissions/page.tsx` - Submission management
  - `api.ts` - API client
  - `course.ts` - Course types
  - `submission.ts` - Submission types

---

## 🗂️ Chi tiết từng file

### Backend Files

| File | Dòng code | Mô tả |
|------|-----------|-------|
| `backend/app/main.py` | ~100 | FastAPI app, CORS, lifespan events |
| `backend/config/database.py` | ~180 | Connection pool, context managers, SQL helpers |
| `backend/app/routes/courses.py` | ~250 | Course CRUD endpoints (1 bảng) |
| `backend/app/routes/submissions.py` | ~280 | Submission endpoints (JOIN nhiều bảng) |
| `backend/app/models/course.py` | ~80 | Pydantic schemas cho Course |
| `backend/app/models/submission.py` | ~90 | Pydantic schemas cho Submission |

**Tổng**: ~980 dòng Python code

### Frontend Files

| File | Dòng code | Mô tả |
|------|-----------|-------|
| `frontend/app/layout.tsx` | ~60 | Header, footer, navigation |
| `frontend/app/page.tsx` | ~100 | Home page với cards |
| `frontend/app/courses/page.tsx` | ~340 | Course management UI + logic |
| `frontend/app/submissions/page.tsx` | ~380 | Submission management UI + logic |
| `frontend/lib/api.ts` | ~40 | Axios client setup |
| `frontend/types/course.ts` | ~50 | Course TypeScript types |
| `frontend/types/submission.ts` | ~60 | Submission TypeScript types |

**Tổng**: ~1030 dòng TypeScript/TSX code

---

## 🔗 Dependencies

### Backend (`requirements.txt`)
```
fastapi==0.109.0           # Web framework
uvicorn[standard]==0.27.0  # ASGI server
psycopg2-binary==2.9.9     # PostgreSQL driver
python-dotenv==1.0.0       # Environment variables
pydantic==2.5.3            # Data validation
```

### Frontend (`package.json`)
```json
{
  "next": "^14.0.4",      // React framework
  "react": "^18.2.0",     // UI library
  "axios": "^1.6.5",      // HTTP client
  "typescript": "^5.3.3", // Type safety
  "tailwindcss": "^3.4.0" // CSS framework
}
```

---

## 🎯 Key Features by File

### `backend/config/database.py`
- ✅ Connection pooling (1-10 connections)
- ✅ Context managers (auto cleanup)
- ✅ RealDictCursor (dict results)
- ✅ Parameterized queries (SQL injection safe)

### `backend/app/routes/courses.py`
- ✅ GET /api/courses - List with filters
- ✅ POST /api/courses - Create new
- ✅ PUT /api/courses/{id} - Update
- ✅ DELETE /api/courses/{id} - Delete
- ✅ Dynamic WHERE clause building

### `backend/app/routes/submissions.py`
- ✅ Complex JOIN (5 tables)
- ✅ Aggregation (stats)
- ✅ Grade submission
- ✅ Filter cascading

### `frontend/app/courses/page.tsx`
- ✅ CRUD operations
- ✅ Search + filters
- ✅ Pagination
- ✅ Modal form (create/edit)
- ✅ State management

### `frontend/app/submissions/page.tsx`
- ✅ Stats cards
- ✅ Multi-table data display
- ✅ Grading modal
- ✅ Complex filters

---

## 📈 Lines of Code Summary

| Component | Python | TypeScript/TSX | Total |
|-----------|--------|----------------|-------|
| Backend | 980 | 0 | 980 |
| Frontend | 0 | 1030 | 1030 |
| **Total** | **980** | **1030** | **2010** |

---

## 🚀 Execution Flow

### 1. Course Creation Flow
```
Frontend (courses/page.tsx)
  ↓ POST /api/courses
Backend (routes/courses.py)
  ↓ Validate (Pydantic)
Database (config/database.py)
  ↓ INSERT INTO "Course"
  ← RETURNING *
Backend
  ← 201 Created
Frontend
  ← Refresh list
```

### 2. Submission Grading Flow
```
Frontend (submissions/page.tsx)
  ↓ PUT /api/submissions/{id}/grade
Backend (routes/submissions.py)
  ↓ Validate score <= max_points
Database
  ↓ UPDATE "AssignmentSubmission"
  ↓ TRIGGER update_gradebook()
  ← Updated submission
Backend
  ← 200 OK
Frontend
  ← Refresh list + stats
```

---

## 🔐 Security Layers

1. **SQL Injection Prevention**
   - File: `database.py`
   - Method: Parameterized queries (`%s`)

2. **Input Validation**
   - File: `models/*.py`
   - Method: Pydantic validators

3. **CORS Protection**
   - File: `main.py`
   - Method: FastAPI CORS middleware

4. **Type Safety**
   - Files: `types/*.ts`
   - Method: TypeScript strict mode

---

**Author**: Nguyễn Văn Kiệt - CNTT1-K63
**Date**: 2025-11-28
