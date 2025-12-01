"""
B-Learning Demo API
===================

FastAPI application cho demo database B-Learning.

Features:
- RESTful API cho Course (1 bảng)
- RESTful API cho Assignment Submissions (nhiều bảng với JOIN)
- CORS enabled
- Auto-generated API docs (Swagger UI)

Author: Nguyễn Văn Kiệt - CNTT1-K63
Date: 2025-11-28

Chạy server:
    uvicorn app.main:app --reload --host 0.0.0.0 --port 8000

API Docs:
    - Swagger UI: http://localhost:8000/docs
    - ReDoc: http://localhost:8000/redoc
"""

from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from contextlib import asynccontextmanager
import os

from config.database import init_connection_pool, close_connection_pool
from app.routes import courses, submissions, assignments, students, modules, lectures, enrollments


# Lifespan context manager (startup/shutdown events)
@asynccontextmanager
async def lifespan(app: FastAPI):
    """
    Quản lý lifecycle của application.
    - startup: Khởi tạo database connection pool
    - shutdown: Đóng connection pool
    """
    # Startup
    print("\n🚀 Starting B-Learning Demo API...")
    init_connection_pool()
    yield
    # Shutdown
    print("\n🛑 Shutting down B-Learning Demo API...")
    close_connection_pool()


# Create FastAPI app
app = FastAPI(
    title="B-Learning Demo API",
    description="""
    ## REST API cho demo database B-Learning

    ### Features:
    - ✅ **Course Management** - CRUD operations cho bảng Course
    - ✅ **Submission Management** - Quản lý Assignment Submissions với JOIN nhiều bảng
    - ✅ **SQL Queries** - Sử dụng raw SQL thay vì ORM
    - ✅ **CORS Enabled** - Cho phép gọi từ Next.js frontend

    ### Database Schema:
    - PostgreSQL 14+
    - 31 tables
    - UUID primary keys
    - Foreign key constraints

    ### Technologies:
    - **Backend**: Python 3.11+ / FastAPI
    - **Database**: PostgreSQL 14+ / psycopg2
    - **Frontend**: Next.js 14 / React 18 / TypeScript

    ### Author:
    Nguyễn Văn Kiệt - CNTT1-K63
    """,
    version="1.0.0",
    lifespan=lifespan,
    docs_url="/docs",
    redoc_url="/redoc"
)

# CORS Configuration
# Cho phép frontend (Next.js) gọi API từ domain khác
CORS_ORIGINS = os.getenv('CORS_ORIGINS', 'http://localhost:3000').split(',')

app.add_middleware(
    CORSMiddleware,
    allow_origins=CORS_ORIGINS,
    allow_credentials=True,
    allow_methods=["*"],  # Cho phép tất cả methods: GET, POST, PUT, DELETE, ...
    allow_headers=["*"],  # Cho phép tất cả headers
)


# Include routers
app.include_router(courses.router)
app.include_router(modules.router)
app.include_router(lectures.router)
app.include_router(assignments.router)
app.include_router(students.router)
app.include_router(enrollments.router)
app.include_router(submissions.router)


# Root endpoint
@app.get("/", tags=["Root"])
def root():
    """
    Root endpoint - Health check và thông tin API.
    """
    return {
        "message": "B-Learning Demo API",
        "version": "1.0.0",
        "status": "running",
        "docs": {
            "swagger": "/docs",
            "redoc": "/redoc"
        },
        "endpoints": {
            "courses": "/api/courses",
            "submissions": "/api/submissions"
        }
    }


# Health check endpoint
@app.get("/health", tags=["Health"])
def health_check():
    """
    Health check endpoint.
    Kiểm tra xem API có hoạt động không.
    """
    return {
        "status": "healthy",
        "database": "connected"
    }


if __name__ == "__main__":
    import uvicorn

    # Chạy server
    uvicorn.run(
        "app.main:app",
        host=os.getenv('API_HOST', '0.0.0.0'),
        port=int(os.getenv('API_PORT', 8000)),
        reload=os.getenv('API_RELOAD', 'true').lower() == 'true'
    )
