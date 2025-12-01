"""
Students API Routes
===================

RESTful API endpoints cho quản lý Students.

Endpoints:
- GET    /api/students - Lấy danh sách students với submission stats

Author: Nguyễn Văn Kiệt - CNTT1-K63
"""

from fastapi import APIRouter

from config.database import execute_query

router = APIRouter(prefix="/api/students", tags=["Students"])


@router.get("")
def get_students():
    """
    Lấy danh sách students với submission statistics.

    **JOIN Tables:**
    - User
    - UserRole
    - Role (name='STUDENT')
    - COUNT(AssignmentSubmission)
    - COUNT(Enrollment)

    **Returns:**
    - students: Danh sách students với stats
    """

    query = """
        SELECT
            -- From User
            u.user_id,
            u.email,
            CONCAT(u.first_name, ' ', u.last_name) as full_name,
            u.first_name,
            u.last_name,
            u.account_status,

            -- Submission Statistics
            COUNT(DISTINCT asub.submission_id) as total_submissions,
            COUNT(DISTINCT asub.submission_id) FILTER (WHERE asub.status = 'GRADED') as graded_count,
            COUNT(DISTINCT asub.submission_id) FILTER (WHERE asub.status IN ('SUBMITTED', 'GRADING')) as pending_count,
            AVG(asub.score) FILTER (WHERE asub.status = 'GRADED') as avg_score,
            COUNT(DISTINCT asub.submission_id) FILTER (WHERE asub.is_late = true) as late_count,

            -- Enrollment count
            COUNT(DISTINCT e.enrollment_id) as enrolled_courses

        FROM "User" u

        -- JOIN UserRole
        INNER JOIN "UserRole" ur
            ON u.user_id = ur.user_id

        -- JOIN Role (filter STUDENT only)
        INNER JOIN "Role" r
            ON ur.role_id = r.role_id

        -- LEFT JOIN AssignmentSubmission for stats
        LEFT JOIN "AssignmentSubmission" asub
            ON u.user_id = asub.user_id

        -- LEFT JOIN Enrollment for course count
        LEFT JOIN "Enrollment" e
            ON u.user_id = e.user_id

        WHERE r.name = 'STUDENT'

        GROUP BY u.user_id
        ORDER BY u.last_name, u.first_name
    """

    students = execute_query(query, fetch='all')

    return {
        "students": students
    }
