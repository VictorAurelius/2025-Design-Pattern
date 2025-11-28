"""
Course Models (Pydantic Schemas)
=================================

Định nghĩa các schema để validate request/response data.

Author: Nguyễn Văn Kiệt - CNTT1-K63
"""

from pydantic import BaseModel, Field, field_validator
from typing import Optional
from datetime import datetime
from decimal import Decimal


class CourseBase(BaseModel):
    """Schema cơ bản cho Course (dùng chung cho Create và Update)"""
    code: str = Field(..., min_length=2, max_length=50, description="Mã khóa học (VD: CS101)")
    title: str = Field(..., min_length=3, max_length=200, description="Tên khóa học")
    description: Optional[str] = Field(None, description="Mô tả chi tiết")
    short_description: Optional[str] = Field(None, max_length=500, description="Mô tả ngắn")
    thumbnail_url: Optional[str] = Field(None, max_length=500, description="URL ảnh đại diện")
    category: Optional[str] = Field(None, max_length=100, description="Danh mục")
    difficulty_level: Optional[str] = Field('BEGINNER', description="Độ khó: BEGINNER | INTERMEDIATE | ADVANCED | EXPERT")
    estimated_hours: Optional[Decimal] = Field(None, ge=0, description="Số giờ ước tính")
    status: Optional[str] = Field('DRAFT', description="Trạng thái: DRAFT | PUBLISHED | ARCHIVED")

    @field_validator('difficulty_level')
    @classmethod
    def validate_difficulty(cls, v):
        """Kiểm tra difficulty_level hợp lệ"""
        allowed = ['BEGINNER', 'INTERMEDIATE', 'ADVANCED', 'EXPERT']
        if v and v not in allowed:
            raise ValueError(f'difficulty_level phải là một trong: {allowed}')
        return v

    @field_validator('status')
    @classmethod
    def validate_status(cls, v):
        """Kiểm tra status hợp lệ"""
        allowed = ['DRAFT', 'PUBLISHED', 'ARCHIVED']
        if v and v not in allowed:
            raise ValueError(f'status phải là một trong: {allowed}')
        return v


class CourseCreate(CourseBase):
    """Schema cho API tạo Course mới"""
    pass


class CourseUpdate(BaseModel):
    """Schema cho API update Course (tất cả fields đều optional)"""
    code: Optional[str] = Field(None, min_length=2, max_length=50)
    title: Optional[str] = Field(None, min_length=3, max_length=200)
    description: Optional[str] = None
    short_description: Optional[str] = Field(None, max_length=500)
    thumbnail_url: Optional[str] = Field(None, max_length=500)
    category: Optional[str] = Field(None, max_length=100)
    difficulty_level: Optional[str] = None
    estimated_hours: Optional[Decimal] = Field(None, ge=0)
    status: Optional[str] = None

    @field_validator('difficulty_level')
    @classmethod
    def validate_difficulty(cls, v):
        if v:
            allowed = ['BEGINNER', 'INTERMEDIATE', 'ADVANCED', 'EXPERT']
            if v not in allowed:
                raise ValueError(f'difficulty_level phải là một trong: {allowed}')
        return v

    @field_validator('status')
    @classmethod
    def validate_status(cls, v):
        if v:
            allowed = ['DRAFT', 'PUBLISHED', 'ARCHIVED']
            if v not in allowed:
                raise ValueError(f'status phải là một trong: {allowed}')
        return v


class CourseResponse(CourseBase):
    """Schema cho response trả về từ API"""
    course_id: str
    published_at: Optional[datetime] = None
    created_by: Optional[str] = None
    created_at: datetime
    updated_at: datetime

    class Config:
        from_attributes = True  # Cho phép chuyển đổi từ dict


class CourseList(BaseModel):
    """Schema cho danh sách courses"""
    total: int
    courses: list[CourseResponse]
