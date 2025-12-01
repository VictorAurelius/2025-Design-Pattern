"""
Assignments API Routes
======================

RESTful API endpoints cho quản lý Assignments (Lectures type=ASSIGNMENT).

Endpoints:
- GET    /api/assignments - Lấy danh sách assignments với stats

Author: Nguyễn Văn Kiệt - CNTT1-K63
"""

from fastapi import APIRouter, Query
from typing import Optional

from config.database import execute_query

router = APIRouter(prefix="/api/assignments", tags=["Assignments"])


@router.get("")
def get_assignments(
    course_id: Optional[str] = Query(None, description="Filter by course")
):
    """
    Lấy danh sách assignments với submission statistics.

    **JOIN Tables:**
    - Lecture (type=ASSIGNMENT)
    - Module
    - Course
    - COUNT(AssignmentSubmission) GROUP BY status

    **Query Parameters:**
    - course_id: Lọc theo khóa học (optional)

    **Returns:**
    - assignments: Danh sách assignments với stats
    """

    where_conditions = ["l.type = 'ASSIGNMENT'"]
    params = []

    if course_id:
        where_conditions.append("c.course_id = %s")
        params.append(course_id)

    query = f"""
        SELECT
            -- From Lecture (Assignment)
            l.lecture_id,
            l.title,
            l.description,
            l.assignment_config->>'max_points' as max_points,
            l.assignment_config->>'due_date' as due_date,

            -- From Course
            c.course_id,
            c.code as course_code,
            c.title as course_title,

            -- From Module
            m.module_id,
            m.title as module_title,

            -- Submission Statistics
            COUNT(asub.submission_id) as total_submissions,
            COUNT(asub.submission_id) FILTER (WHERE asub.status = 'GRADED') as graded_count,
            COUNT(asub.submission_id) FILTER (WHERE asub.status = 'SUBMITTED') as pending_count,
            COUNT(asub.submission_id) FILTER (WHERE asub.status = 'GRADING') as grading_count

        FROM "Lecture" l

        -- JOIN Module
        INNER JOIN "Module" m
            ON l.module_id = m.module_id

        -- JOIN Course
        INNER JOIN "Course" c
            ON m.course_id = c.course_id

        -- LEFT JOIN AssignmentSubmission for stats
        LEFT JOIN "AssignmentSubmission" asub
            ON l.lecture_id = asub.lecture_id

        WHERE {" AND ".join(where_conditions)}

        GROUP BY l.lecture_id, c.course_id, m.module_id
        ORDER BY c.code, m.order_num, l.order_num
    """

    assignments = execute_query(query, tuple(params), fetch='all')

    return {
        "assignments": assignments
    }
