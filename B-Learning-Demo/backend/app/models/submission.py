"""
Assignment Submission Models (Pydantic Schemas)
================================================

Schemas cho Assignment Submissions (liên quan nhiều bảng).

Author: Nguyễn Văn Kiệt - CNTT1-K63
"""

from pydantic import BaseModel, Field
from typing import Optional
from datetime import datetime
from decimal import Decimal


class SubmissionListItem(BaseModel):
    """
    Schema cho item trong danh sách submissions.
    Kết hợp dữ liệu từ nhiều bảng:
    - AssignmentSubmission
    - User (student info)
    - Assignment
    - Course
    """
    # From AssignmentSubmission
    assignment_submission_id: str
    submission_number: int
    submitted_at: datetime
    content: Optional[str] = None
    is_late: bool
    days_late: int
    status: str
    auto_score: Optional[Decimal] = None
    manual_score: Optional[Decimal] = None
    final_score: Optional[Decimal] = None
    penalty_applied: Decimal
    feedback: Optional[str] = None
    graded_at: Optional[datetime] = None

    # From User (student)
    student_id: str
    student_email: str
    student_name: str  # first_name + last_name

    # From Assignment
    assignment_id: str
    assignment_title: str
    assignment_type: str
    max_points: Decimal
    due_date: datetime

    # From Course
    course_id: str
    course_code: str
    course_title: str

    # From User (grader) - optional
    graded_by_name: Optional[str] = None

    class Config:
        from_attributes = True


class SubmissionDetail(SubmissionListItem):
    """
    Schema chi tiết submission (bao gồm thêm thông tin)
    """
    # Additional fields from AssignmentSubmission
    file_urls: Optional[dict] = None
    code_submission: Optional[str] = None
    rubric_scores: Optional[dict] = None

    # Additional fields from Assignment
    assignment_description: str
    assignment_instructions: Optional[str] = None
    late_submission_allowed: bool
    late_penalty_percent: Decimal

    # Additional fields from Course
    course_description: Optional[str] = None


class SubmissionListResponse(BaseModel):
    """Response cho danh sách submissions"""
    total: int
    submissions: list[SubmissionListItem]


class GradeSubmissionRequest(BaseModel):
    """Request body cho việc chấm điểm"""
    manual_score: Decimal = Field(..., ge=0, description="Điểm manual (>= 0)")
    feedback: Optional[str] = Field(None, description="Nhận xét của giảng viên")
    rubric_scores: Optional[dict] = Field(None, description="Điểm theo rubric (JSON)")


class SubmissionStats(BaseModel):
    """Thống kê submission"""
    total_submissions: int
    submitted: int
    graded: int
    pending: int
    average_score: Optional[Decimal] = None
    late_submissions: int
    on_time_submissions: int
