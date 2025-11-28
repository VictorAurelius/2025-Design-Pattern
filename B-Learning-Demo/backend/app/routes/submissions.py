"""
Assignment Submission API Routes
=================================

RESTful API endpoints cho quản lý Assignment Submissions (nhiều bảng).

Endpoints:
- GET    /api/submissions             - Lấy danh sách submissions (JOIN nhiều bảng)
- GET    /api/submissions/{id}        - Lấy chi tiết 1 submission
- PUT    /api/submissions/{id}/grade  - Chấm điểm submission
- GET    /api/submissions/stats       - Thống kê submissions

Author: Nguyễn Văn Kiệt - CNTT1-K63
"""

from fastapi import APIRouter, HTTPException, Query
from typing import Optional
from decimal import Decimal

from app.models.submission import (
    SubmissionListItem,
    SubmissionDetail,
    SubmissionListResponse,
    GradeSubmissionRequest,
    SubmissionStats
)
from config.database import execute_query, execute_query_with_returning

router = APIRouter(prefix="/api/submissions", tags=["Submissions"])


@router.get("", response_model=SubmissionListResponse)
def get_submissions(
    course_id: Optional[str] = Query(None, description="Filter by course"),
    assignment_id: Optional[str] = Query(None, description="Filter by assignment"),
    status: Optional[str] = Query(None, description="Filter by status: SUBMITTED|GRADED|GRADING|DRAFT|RETURNED"),
    is_late: Optional[bool] = Query(None, description="Filter late submissions"),
    student_email: Optional[str] = Query(None, description="Search student by email"),
    limit: int = Query(50, ge=1, le=100),
    offset: int = Query(0, ge=0)
):
    """
    Lấy danh sách submissions với thông tin từ nhiều bảng.

    **JOIN Tables:**
    - AssignmentSubmission (main)
    - User (student info)
    - Assignment
    - Course
    - User (grader info) - LEFT JOIN

    **Query Parameters:**
    - course_id: Lọc theo khóa học
    - assignment_id: Lọc theo bài tập
    - status: Lọc theo trạng thái
    - is_late: Lọc submission nộp trễ
    - student_email: Tìm kiếm sinh viên
    - limit/offset: Pagination

    **Returns:**
    - total: Tổng số submissions
    - submissions: Danh sách với đầy đủ thông tin
    """

    # Build WHERE conditions
    where_conditions = []
    params = []

    if course_id:
        where_conditions.append("a.course_id = %s")
        params.append(course_id)

    if assignment_id:
        where_conditions.append("asub.assignment_id = %s")
        params.append(assignment_id)

    if status:
        where_conditions.append("asub.status = %s")
        params.append(status)

    if is_late is not None:
        where_conditions.append("asub.is_late = %s")
        params.append(is_late)

    if student_email:
        where_conditions.append("u.email ILIKE %s")
        params.append(f"%{student_email}%")

    where_clause = "WHERE " + " AND ".join(where_conditions) if where_conditions else ""

    # Count total
    count_query = f"""
        SELECT COUNT(*) as count
        FROM "AssignmentSubmission" asub
        INNER JOIN "User" u ON asub.user_id = u.user_id
        INNER JOIN "Assignment" a ON asub.assignment_id = a.assignment_id
        INNER JOIN "Course" c ON a.course_id = c.course_id
        {where_clause}
    """
    count_result = execute_query(count_query, tuple(params), fetch='one')
    total = count_result['count']

    # Fetch submissions with JOIN
    params.extend([limit, offset])
    submissions_query = f"""
        SELECT
            -- From AssignmentSubmission
            asub.assignment_submission_id,
            asub.submission_number,
            asub.submitted_at,
            asub.content,
            asub.is_late,
            asub.days_late,
            asub.status,
            asub.auto_score,
            asub.manual_score,
            asub.final_score,
            asub.penalty_applied,
            asub.feedback,
            asub.graded_at,

            -- From User (student)
            u.user_id as student_id,
            u.email as student_email,
            CONCAT(u.first_name, ' ', u.last_name) as student_name,

            -- From Assignment
            a.assignment_id,
            a.title as assignment_title,
            a.assignment_type,
            a.max_points,
            a.due_date,

            -- From Course
            c.course_id,
            c.code as course_code,
            c.title as course_title,

            -- From User (grader) - LEFT JOIN
            CONCAT(grader.first_name, ' ', grader.last_name) as graded_by_name

        FROM "AssignmentSubmission" asub

        -- JOIN Student
        INNER JOIN "User" u
            ON asub.user_id = u.user_id

        -- JOIN Assignment
        INNER JOIN "Assignment" a
            ON asub.assignment_id = a.assignment_id

        -- JOIN Course
        INNER JOIN "Course" c
            ON a.course_id = c.course_id

        -- LEFT JOIN Grader (có thể chưa chấm)
        LEFT JOIN "User" grader
            ON asub.graded_by = grader.user_id

        {where_clause}

        ORDER BY asub.submitted_at DESC
        LIMIT %s OFFSET %s
    """

    submissions = execute_query(submissions_query, tuple(params), fetch='all')

    return {
        "total": total,
        "submissions": submissions
    }


@router.get("/{submission_id}", response_model=SubmissionDetail)
def get_submission_detail(submission_id: str):
    """
    Lấy chi tiết 1 submission với đầy đủ thông tin từ các bảng liên quan.

    **Path Parameters:**
    - submission_id: UUID của submission

    **Returns:**
    - Chi tiết submission với thông tin đầy đủ

    **Raises:**
    - 404: Submission không tồn tại
    """

    query = """
        SELECT
            -- From AssignmentSubmission (all fields)
            asub.*,

            -- From User (student)
            u.user_id as student_id,
            u.email as student_email,
            CONCAT(u.first_name, ' ', u.last_name) as student_name,

            -- From Assignment (all important fields)
            a.assignment_id,
            a.title as assignment_title,
            a.description as assignment_description,
            a.instructions as assignment_instructions,
            a.assignment_type,
            a.max_points,
            a.due_date,
            a.late_submission_allowed,
            a.late_penalty_percent,

            -- From Course
            c.course_id,
            c.code as course_code,
            c.title as course_title,
            c.description as course_description,

            -- From User (grader)
            CONCAT(grader.first_name, ' ', grader.last_name) as graded_by_name

        FROM "AssignmentSubmission" asub

        INNER JOIN "User" u ON asub.user_id = u.user_id
        INNER JOIN "Assignment" a ON asub.assignment_id = a.assignment_id
        INNER JOIN "Course" c ON a.course_id = c.course_id
        LEFT JOIN "User" grader ON asub.graded_by = grader.user_id

        WHERE asub.assignment_submission_id = %s
    """

    submission = execute_query(query, (submission_id,), fetch='one')

    if not submission:
        raise HTTPException(
            status_code=404,
            detail=f"Submission {submission_id} không tồn tại"
        )

    return submission


@router.put("/{submission_id}/grade", response_model=SubmissionDetail)
def grade_submission(submission_id: str, grade_data: GradeSubmissionRequest):
    """
    Chấm điểm cho submission.

    **Path Parameters:**
    - submission_id: UUID của submission

    **Request Body:**
    - manual_score: Điểm chấm tay (>= 0)
    - feedback: Nhận xét (optional)
    - rubric_scores: Điểm theo rubric (optional, JSON)

    **Process:**
    1. Validate submission exists và status = 'SUBMITTED' hoặc 'GRADING'
    2. Validate manual_score <= max_points
    3. Calculate final_score = auto_score + manual_score - penalty
    4. Update submission với status = 'GRADED'
    5. Update GradeBook (trigger sẽ tự động chạy)

    **Returns:**
    - Submission đã được chấm điểm

    **Raises:**
    - 404: Submission không tồn tại
    - 400: Validation errors
    """

    # Get submission and assignment info
    check_query = """
        SELECT
            asub.assignment_submission_id,
            asub.status,
            asub.auto_score,
            asub.penalty_applied,
            a.max_points
        FROM "AssignmentSubmission" asub
        INNER JOIN "Assignment" a ON asub.assignment_id = a.assignment_id
        WHERE asub.assignment_submission_id = %s
    """
    submission = execute_query(check_query, (submission_id,), fetch='one')

    if not submission:
        raise HTTPException(status_code=404, detail="Submission không tồn tại")

    # Validate status
    if submission['status'] not in ['SUBMITTED', 'GRADING']:
        raise HTTPException(
            status_code=400,
            detail=f"Không thể chấm submission có status '{submission['status']}'. "
                   "Chỉ chấm được SUBMITTED hoặc GRADING."
        )

    # Validate manual_score
    max_points = submission['max_points']
    if grade_data.manual_score > max_points:
        raise HTTPException(
            status_code=400,
            detail=f"manual_score ({grade_data.manual_score}) không được vượt quá "
                   f"max_points ({max_points})"
        )

    # Calculate final_score
    auto_score = submission['auto_score'] or Decimal('0')
    penalty = submission['penalty_applied'] or Decimal('0')
    final_score = auto_score + grade_data.manual_score - penalty

    # Ensure final_score >= 0
    if final_score < 0:
        final_score = Decimal('0')

    # Update submission
    update_query = """
        UPDATE "AssignmentSubmission"
        SET
            manual_score = %s,
            final_score = %s,
            feedback = %s,
            rubric_scores = %s,
            status = 'GRADED',
            graded_at = CURRENT_TIMESTAMP
        WHERE assignment_submission_id = %s
        RETURNING *
    """

    params = (
        grade_data.manual_score,
        final_score,
        grade_data.feedback,
        grade_data.rubric_scores,
        submission_id
    )

    execute_query_with_returning(update_query, params)

    # Return full detail
    return get_submission_detail(submission_id)


@router.get("/stats/overview", response_model=SubmissionStats)
def get_submission_stats(
    course_id: Optional[str] = Query(None, description="Stats for specific course"),
    assignment_id: Optional[str] = Query(None, description="Stats for specific assignment")
):
    """
    Lấy thống kê tổng quan về submissions.

    **Query Parameters:**
    - course_id: Thống kê cho 1 course
    - assignment_id: Thống kê cho 1 assignment

    **Returns:**
    - total_submissions: Tổng số submissions
    - submitted: Số đã nộp
    - graded: Số đã chấm
    - pending: Số chưa chấm
    - average_score: Điểm trung bình
    - late_submissions: Số nộp trễ
    - on_time_submissions: Số nộp đúng hạn
    """

    where_conditions = []
    params = []

    if course_id:
        where_conditions.append("a.course_id = %s")
        params.append(course_id)

    if assignment_id:
        where_conditions.append("asub.assignment_id = %s")
        params.append(assignment_id)

    where_clause = "WHERE " + " AND ".join(where_conditions) if where_conditions else ""

    stats_query = f"""
        SELECT
            COUNT(*) as total_submissions,
            COUNT(*) FILTER (WHERE asub.status = 'SUBMITTED') as submitted,
            COUNT(*) FILTER (WHERE asub.status = 'GRADED') as graded,
            COUNT(*) FILTER (WHERE asub.status IN ('SUBMITTED', 'GRADING')) as pending,
            AVG(asub.final_score) FILTER (WHERE asub.status = 'GRADED') as average_score,
            COUNT(*) FILTER (WHERE asub.is_late = TRUE) as late_submissions,
            COUNT(*) FILTER (WHERE asub.is_late = FALSE) as on_time_submissions
        FROM "AssignmentSubmission" asub
        INNER JOIN "Assignment" a ON asub.assignment_id = a.assignment_id
        {where_clause}
    """

    stats = execute_query(stats_query, tuple(params), fetch='one')

    return stats
