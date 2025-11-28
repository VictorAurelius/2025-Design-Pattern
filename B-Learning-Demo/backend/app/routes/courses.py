"""
Course API Routes
=================

RESTful API endpoints cho quản lý Course (1 bảng).

Endpoints:
- GET    /api/courses          - Lấy danh sách courses
- GET    /api/courses/{id}     - Lấy chi tiết 1 course
- POST   /api/courses          - Tạo course mới
- PUT    /api/courses/{id}     - Cập nhật course
- DELETE /api/courses/{id}     - Xóa course

Author: Nguyễn Văn Kiệt - CNTT1-K63
"""

from fastapi import APIRouter, HTTPException, Query
from typing import Optional
from uuid import UUID
import uuid

from app.models.course import (
    CourseCreate,
    CourseUpdate,
    CourseResponse,
    CourseList
)
from config.database import execute_query, execute_query_with_returning

router = APIRouter(prefix="/api/courses", tags=["Courses"])


@router.get("", response_model=CourseList)
def get_courses(
    status: Optional[str] = Query(None, description="Filter by status: DRAFT|PUBLISHED|ARCHIVED"),
    category: Optional[str] = Query(None, description="Filter by category"),
    difficulty: Optional[str] = Query(None, description="Filter by difficulty"),
    search: Optional[str] = Query(None, description="Search in title and description"),
    limit: int = Query(50, ge=1, le=100, description="Number of results (max 100)"),
    offset: int = Query(0, ge=0, description="Offset for pagination")
):
    """
    Lấy danh sách courses với filters và pagination.

    **Query Parameters:**
    - status: Lọc theo trạng thái
    - category: Lọc theo danh mục
    - difficulty: Lọc theo độ khó
    - search: Tìm kiếm trong title và description
    - limit: Số lượng kết quả (mặc định 50, max 100)
    - offset: Vị trí bắt đầu (cho pagination)

    **Returns:**
    - total: Tổng số courses
    - courses: Danh sách courses
    """

    # Build WHERE clause dynamically
    where_conditions = []
    params = []

    if status:
        where_conditions.append("status = %s")
        params.append(status)

    if category:
        where_conditions.append("category = %s")
        params.append(category)

    if difficulty:
        where_conditions.append("difficulty_level = %s")
        params.append(difficulty)

    if search:
        where_conditions.append("(title ILIKE %s OR description ILIKE %s)")
        search_pattern = f"%{search}%"
        params.extend([search_pattern, search_pattern])

    where_clause = "WHERE " + " AND ".join(where_conditions) if where_conditions else ""

    # Count total
    count_query = f"""
        SELECT COUNT(*) as count
        FROM "Course"
        {where_clause}
    """
    count_result = execute_query(count_query, tuple(params), fetch='one')
    total = count_result['count']

    # Fetch courses
    params.extend([limit, offset])
    courses_query = f"""
        SELECT *
        FROM "Course"
        {where_clause}
        ORDER BY created_at DESC
        LIMIT %s OFFSET %s
    """
    courses = execute_query(courses_query, tuple(params), fetch='all')

    return {
        "total": total,
        "courses": courses
    }


@router.get("/{course_id}", response_model=CourseResponse)
def get_course(course_id: str):
    """
    Lấy chi tiết 1 course theo ID.

    **Path Parameters:**
    - course_id: UUID của course

    **Returns:**
    - Course details

    **Raises:**
    - 404: Course không tồn tại
    """
    query = """
        SELECT *
        FROM "Course"
        WHERE course_id = %s
    """
    course = execute_query(query, (course_id,), fetch='one')

    if not course:
        raise HTTPException(status_code=404, detail=f"Course {course_id} không tồn tại")

    return course


@router.post("", response_model=CourseResponse, status_code=201)
def create_course(course_data: CourseCreate):
    """
    Tạo course mới.

    **Request Body:**
    - code: Mã khóa học (unique, required)
    - title: Tên khóa học (required)
    - description: Mô tả chi tiết (optional)
    - ... (xem CourseCreate schema)

    **Returns:**
    - Course đã tạo

    **Raises:**
    - 400: Code đã tồn tại
    - 422: Validation error
    """

    # Check if code already exists
    existing = execute_query(
        'SELECT course_id FROM "Course" WHERE code = %s',
        (course_data.code,),
        fetch='one'
    )
    if existing:
        raise HTTPException(
            status_code=400,
            detail=f"Course code '{course_data.code}' đã tồn tại"
        )

    # Insert new course
    query = """
        INSERT INTO "Course" (
            code, title, description, short_description, thumbnail_url,
            category, difficulty_level, estimated_hours, status
        )
        VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s)
        RETURNING *
    """
    params = (
        course_data.code,
        course_data.title,
        course_data.description,
        course_data.short_description,
        course_data.thumbnail_url,
        course_data.category,
        course_data.difficulty_level,
        course_data.estimated_hours,
        course_data.status
    )

    new_course = execute_query_with_returning(query, params)
    return new_course


@router.put("/{course_id}", response_model=CourseResponse)
def update_course(course_id: str, course_data: CourseUpdate):
    """
    Cập nhật thông tin course.

    **Path Parameters:**
    - course_id: UUID của course

    **Request Body:**
    - Các field muốn update (tất cả đều optional)

    **Returns:**
    - Course đã update

    **Raises:**
    - 404: Course không tồn tại
    - 400: Code mới đã tồn tại (nếu update code)
    """

    # Check if course exists
    existing = execute_query(
        'SELECT course_id FROM "Course" WHERE course_id = %s',
        (course_id,),
        fetch='one'
    )
    if not existing:
        raise HTTPException(status_code=404, detail=f"Course {course_id} không tồn tại")

    # Build UPDATE query dynamically (chỉ update các field được provide)
    update_fields = []
    params = []

    update_data = course_data.model_dump(exclude_unset=True)

    # Check if new code conflicts with existing
    if 'code' in update_data:
        code_conflict = execute_query(
            'SELECT course_id FROM "Course" WHERE code = %s AND course_id != %s',
            (update_data['code'], course_id),
            fetch='one'
        )
        if code_conflict:
            raise HTTPException(
                status_code=400,
                detail=f"Course code '{update_data['code']}' đã tồn tại"
            )

    for field, value in update_data.items():
        update_fields.append(f"{field} = %s")
        params.append(value)

    if not update_fields:
        # No fields to update
        return get_course(course_id)

    params.append(course_id)

    query = f"""
        UPDATE "Course"
        SET {', '.join(update_fields)}
        WHERE course_id = %s
        RETURNING *
    """

    updated_course = execute_query_with_returning(query, tuple(params))
    return updated_course


@router.delete("/{course_id}", status_code=204)
def delete_course(course_id: str):
    """
    Xóa course.

    **Path Parameters:**
    - course_id: UUID của course

    **Returns:**
    - 204 No Content (thành công)

    **Raises:**
    - 404: Course không tồn tại
    - 409: Không thể xóa course có enrollments

    **Note:**
    - Nên check xem course có enrollments không trước khi xóa
    - Hoặc sử dụng soft delete (set status = 'ARCHIVED')
    """

    # Check if course exists
    existing = execute_query(
        'SELECT course_id FROM "Course" WHERE course_id = %s',
        (course_id,),
        fetch='one'
    )
    if not existing:
        raise HTTPException(status_code=404, detail=f"Course {course_id} không tồn tại")

    # Check if course has enrollments
    enrollments = execute_query(
        'SELECT COUNT(*) as count FROM "CourseEnrollment" WHERE course_id = %s',
        (course_id,),
        fetch='one'
    )
    if enrollments['count'] > 0:
        raise HTTPException(
            status_code=409,
            detail=f"Không thể xóa course vì có {enrollments['count']} enrollments. "
                   "Hãy archive course thay vì xóa."
        )

    # Delete course (CASCADE will delete related records)
    execute_query(
        'DELETE FROM "Course" WHERE course_id = %s',
        (course_id,),
        fetch='none'
    )

    return None  # 204 No Content
