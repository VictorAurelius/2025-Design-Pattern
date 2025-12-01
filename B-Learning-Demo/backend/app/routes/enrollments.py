"""
Enrollments API Routes
======================

RESTful API endpoints cho quản lý Enrollments (enroll students vào courses).

Endpoints:
- GET    /api/enrollments?course_id=xxx - Lấy danh sách enrollments của 1 course
- POST   /api/enrollments - Enroll student vào course
- DELETE /api/enrollments/{id} - Xóa enrollment

Author: Nguyễn Văn Kiệt - CNTT1-K63
"""

from fastapi import APIRouter, HTTPException, Query
from typing import Optional
from pydantic import BaseModel

from config.database import execute_query, execute_query_with_returning

router = APIRouter(prefix="/api/enrollments", tags=["Enrollments"])


class EnrollmentCreate(BaseModel):
    user_id: str  # Student ID
    course_id: str
    role: str = "STUDENT"  # STUDENT | TA | INSTRUCTOR


@router.get("")
def get_enrollments(
    course_id: Optional[str] = Query(None, description="Filter by course"),
    user_id: Optional[str] = Query(None, description="Filter by user")
):
    """
    Lấy danh sách enrollments.

    **Query Parameters:**
    - course_id: Lọc theo course (optional)
    - user_id: Lọc theo user (optional)

    **Returns:**
    - enrollments: Danh sách với user và course info
    """

    where_conditions = []
    params = []

    if course_id:
        where_conditions.append("e.course_id = %s")
        params.append(course_id)

    if user_id:
        where_conditions.append("e.user_id = %s")
        params.append(user_id)

    where_clause = ""
    if where_conditions:
        where_clause = "WHERE " + " AND ".join(where_conditions)

    query = f"""
        SELECT
            e.enrollment_id,
            e.user_id,
            e.course_id,
            e.role,
            e.status,
            e.enrolled_at,
            e.final_grade,

            -- User info
            u.email as student_email,
            CONCAT(u.first_name, ' ', u.last_name) as student_name,

            -- Course info
            c.code as course_code,
            c.title as course_title,

            -- Stats
            COUNT(asub.submission_id) as submission_count,
            AVG(asub.score) FILTER (WHERE asub.status = 'GRADED') as avg_score

        FROM "Enrollment" e
        INNER JOIN "User" u ON e.user_id = u.user_id
        INNER JOIN "Course" c ON e.course_id = c.course_id
        LEFT JOIN "AssignmentSubmission" asub ON e.enrollment_id = asub.enrollment_id
        {where_clause}
        GROUP BY e.enrollment_id, u.user_id, u.email, u.first_name, u.last_name,
                 c.course_id, c.code, c.title
        ORDER BY e.enrolled_at DESC
    """

    enrollments = execute_query(query, tuple(params), fetch='all')

    return {
        "enrollments": enrollments
    }


@router.post("")
def create_enrollment(enrollment_data: EnrollmentCreate):
    """
    Enroll student vào course.

    **Request Body:**
    - user_id: Student ID
    - course_id: Course ID
    - role: STUDENT | TA | INSTRUCTOR (default: STUDENT)

    **Returns:**
    - enrollment_id: UUID của enrollment vừa tạo

    **Raises:**
    - 400: User đã enrolled vào course này rồi
    """

    # Check if already enrolled
    check_query = """
        SELECT enrollment_id
        FROM "Enrollment"
        WHERE user_id = %s AND course_id = %s
    """
    existing = execute_query(
        check_query,
        (enrollment_data.user_id, enrollment_data.course_id),
        fetch='one'
    )

    if existing:
        raise HTTPException(
            status_code=400,
            detail="User đã enrolled vào course này rồi"
        )

    # Create enrollment
    query = """
        INSERT INTO "Enrollment" (user_id, course_id, role, status, enrolled_at)
        VALUES (%s, %s, %s, 'ACTIVE', CURRENT_TIMESTAMP)
        RETURNING enrollment_id, user_id, course_id, role, status, enrolled_at
    """

    params = (
        enrollment_data.user_id,
        enrollment_data.course_id,
        enrollment_data.role
    )

    result = execute_query_with_returning(query, params)

    return result


@router.delete("/{enrollment_id}")
def delete_enrollment(enrollment_id: str):
    """
    Xóa enrollment (unenroll student khỏi course).

    **Warning:** CASCADE sẽ xóa tất cả submissions của student trong course này!
    """

    query = """
        DELETE FROM "Enrollment"
        WHERE enrollment_id = %s
        RETURNING enrollment_id
    """

    result = execute_query(query, (enrollment_id,), fetch='one')

    if not result:
        raise HTTPException(status_code=404, detail="Enrollment not found")

    return {
        "message": "Enrollment deleted successfully",
        "enrollment_id": result['enrollment_id']
    }
