"""
Modules API Routes
==================

RESTful API endpoints cho quản lý Modules trong Course.

Endpoints:
- GET    /api/modules?course_id=xxx - Lấy danh sách modules của 1 course
- POST   /api/modules - Tạo module mới
- PUT    /api/modules/{id} - Cập nhật module
- DELETE /api/modules/{id} - Xóa module

Author: Nguyễn Văn Kiệt - CNTT1-K63
"""

from fastapi import APIRouter, HTTPException, Query
from typing import Optional
from pydantic import BaseModel

from config.database import execute_query, execute_query_with_returning

router = APIRouter(prefix="/api/modules", tags=["Modules"])


class ModuleCreate(BaseModel):
    course_id: str
    title: str
    description: Optional[str] = None
    order_num: int = 1


class ModuleUpdate(BaseModel):
    title: Optional[str] = None
    description: Optional[str] = None
    order_num: Optional[int] = None


@router.get("")
def get_modules(
    course_id: str = Query(..., description="Course ID to get modules for")
):
    """
    Lấy danh sách modules của 1 course.

    **Returns:**
    - modules: Danh sách modules với lecture count
    """

    query = """
        SELECT
            m.module_id,
            m.course_id,
            m.title,
            m.description,
            m.order_num,
            m.created_at,
            COUNT(l.lecture_id) as lecture_count,
            COUNT(l.lecture_id) FILTER (WHERE l.type = 'ASSIGNMENT') as assignment_count
        FROM "Module" m
        LEFT JOIN "Lecture" l ON m.module_id = l.module_id
        WHERE m.course_id = %s
        GROUP BY m.module_id
        ORDER BY m.order_num, m.created_at
    """

    modules = execute_query(query, (course_id,), fetch='all')

    return {
        "modules": modules
    }


@router.post("")
def create_module(module_data: ModuleCreate):
    """
    Tạo module mới trong course.

    **Returns:**
    - module_id: UUID của module vừa tạo
    """

    query = """
        INSERT INTO "Module" (course_id, title, description, order_num)
        VALUES (%s, %s, %s, %s)
        RETURNING module_id, course_id, title, description, order_num, created_at
    """

    params = (
        module_data.course_id,
        module_data.title,
        module_data.description,
        module_data.order_num
    )

    result = execute_query_with_returning(query, params)

    return result


@router.put("/{module_id}")
def update_module(module_id: str, module_data: ModuleUpdate):
    """
    Cập nhật thông tin module.
    """

    # Build update fields
    update_fields = []
    params = []

    if module_data.title is not None:
        update_fields.append("title = %s")
        params.append(module_data.title)

    if module_data.description is not None:
        update_fields.append("description = %s")
        params.append(module_data.description)

    if module_data.order_num is not None:
        update_fields.append("order_num = %s")
        params.append(module_data.order_num)

    if not update_fields:
        raise HTTPException(status_code=400, detail="No fields to update")

    params.append(module_id)

    query = f"""
        UPDATE "Module"
        SET {", ".join(update_fields)}, updated_at = CURRENT_TIMESTAMP
        WHERE module_id = %s
        RETURNING module_id, course_id, title, description, order_num, updated_at
    """

    result = execute_query_with_returning(query, tuple(params))

    if not result:
        raise HTTPException(status_code=404, detail="Module not found")

    return result


@router.delete("/{module_id}")
def delete_module(module_id: str):
    """
    Xóa module (CASCADE sẽ xóa tất cả lectures bên trong).
    """

    query = """
        DELETE FROM "Module"
        WHERE module_id = %s
        RETURNING module_id
    """

    result = execute_query(query, (module_id,), fetch='one')

    if not result:
        raise HTTPException(status_code=404, detail="Module not found")

    return {
        "message": "Module deleted successfully",
        "module_id": result['module_id']
    }
