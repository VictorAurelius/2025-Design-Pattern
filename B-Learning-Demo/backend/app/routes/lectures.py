"""
Lectures API Routes
===================

RESTful API endpoints cho quản lý Lectures (VIDEO, READING, ASSIGNMENT).

Endpoints:
- GET    /api/lectures?module_id=xxx - Lấy danh sách lectures của 1 module
- POST   /api/lectures - Tạo lecture mới
- PUT    /api/lectures/{id} - Cập nhật lecture
- DELETE /api/lectures/{id} - Xóa lecture

Author: Nguyễn Văn Kiệt - CNTT1-K63
"""

from fastapi import APIRouter, HTTPException, Query
from typing import Optional
from pydantic import BaseModel
import json

from config.database import execute_query, execute_query_with_returning

router = APIRouter(prefix="/api/lectures", tags=["Lectures"])


class LectureCreate(BaseModel):
    module_id: str
    title: str
    description: Optional[str] = None
    type: str = "VIDEO"  # VIDEO | READING | ASSIGNMENT
    order_num: int = 1
    # For ASSIGNMENT type
    max_points: Optional[int] = None
    due_days: Optional[int] = 7  # Due date = created_at + due_days


class LectureUpdate(BaseModel):
    title: Optional[str] = None
    description: Optional[str] = None
    order_num: Optional[int] = None
    max_points: Optional[int] = None
    due_days: Optional[int] = None


@router.get("")
def get_lectures(
    module_id: str = Query(..., description="Module ID to get lectures for")
):
    """
    Lấy danh sách lectures của 1 module.

    **Returns:**
    - lectures: Danh sách lectures với submission count (nếu là ASSIGNMENT)
    """

    query = """
        SELECT
            l.lecture_id,
            l.module_id,
            l.title,
            l.description,
            l.type,
            l.order_num,
            l.assignment_config,
            l.created_at,
            COUNT(asub.submission_id) as submission_count
        FROM "Lecture" l
        LEFT JOIN "AssignmentSubmission" asub ON l.lecture_id = asub.lecture_id
        WHERE l.module_id = %s
        GROUP BY l.lecture_id
        ORDER BY l.order_num, l.created_at
    """

    lectures = execute_query(query, (module_id,), fetch='all')

    return {
        "lectures": lectures
    }


@router.post("")
def create_lecture(lecture_data: LectureCreate):
    """
    Tạo lecture mới trong module.

    Nếu type = 'ASSIGNMENT', sẽ tạo assignment_config với max_points và due_date.

    **Returns:**
    - lecture_id: UUID của lecture vừa tạo
    """

    # Build assignment_config for ASSIGNMENT type
    assignment_config = None
    if lecture_data.type == "ASSIGNMENT":
        if lecture_data.max_points is None:
            lecture_data.max_points = 100

        assignment_config = json.dumps({
            "max_points": str(lecture_data.max_points),
            "due_date": f"CURRENT_TIMESTAMP + INTERVAL '{lecture_data.due_days or 7} days'",
            "late_submission_allowed": True,
            "late_penalty_percent": "0"
        })

    query = """
        INSERT INTO "Lecture" (module_id, title, description, type, order_num, assignment_config)
        VALUES (%s, %s, %s, %s, %s, %s::jsonb)
        RETURNING lecture_id, module_id, title, description, type, order_num, assignment_config, created_at
    """

    params = (
        lecture_data.module_id,
        lecture_data.title,
        lecture_data.description,
        lecture_data.type,
        lecture_data.order_num,
        assignment_config
    )

    result = execute_query_with_returning(query, params)

    return result


@router.put("/{lecture_id}")
def update_lecture(lecture_id: str, lecture_data: LectureUpdate):
    """
    Cập nhật thông tin lecture.
    """

    # Build update fields
    update_fields = []
    params = []

    if lecture_data.title is not None:
        update_fields.append("title = %s")
        params.append(lecture_data.title)

    if lecture_data.description is not None:
        update_fields.append("description = %s")
        params.append(lecture_data.description)

    if lecture_data.order_num is not None:
        update_fields.append("order_num = %s")
        params.append(lecture_data.order_num)

    # Update assignment_config for ASSIGNMENT type
    if lecture_data.max_points is not None or lecture_data.due_days is not None:
        # Need to get current config and update
        get_query = "SELECT assignment_config FROM \"Lecture\" WHERE lecture_id = %s"
        current = execute_query(get_query, (lecture_id,), fetch='one')

        if current and current['assignment_config']:
            config = current['assignment_config']
            if lecture_data.max_points is not None:
                config['max_points'] = str(lecture_data.max_points)
            if lecture_data.due_days is not None:
                config['due_date'] = f"CURRENT_TIMESTAMP + INTERVAL '{lecture_data.due_days} days'"

            update_fields.append("assignment_config = %s::jsonb")
            params.append(json.dumps(config))

    if not update_fields:
        raise HTTPException(status_code=400, detail="No fields to update")

    params.append(lecture_id)

    query = f"""
        UPDATE "Lecture"
        SET {", ".join(update_fields)}, updated_at = CURRENT_TIMESTAMP
        WHERE lecture_id = %s
        RETURNING lecture_id, module_id, title, description, type, order_num, assignment_config, updated_at
    """

    result = execute_query_with_returning(query, tuple(params))

    if not result:
        raise HTTPException(status_code=404, detail="Lecture not found")

    return result


@router.delete("/{lecture_id}")
def delete_lecture(lecture_id: str):
    """
    Xóa lecture (CASCADE sẽ xóa tất cả submissions nếu là ASSIGNMENT).
    """

    query = """
        DELETE FROM "Lecture"
        WHERE lecture_id = %s
        RETURNING lecture_id
    """

    result = execute_query(query, (lecture_id,), fetch='one')

    if not result:
        raise HTTPException(status_code=404, detail="Lecture not found")

    return {
        "message": "Lecture deleted successfully",
        "lecture_id": result['lecture_id']
    }
