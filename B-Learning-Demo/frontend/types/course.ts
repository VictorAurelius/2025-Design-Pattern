/**
 * Course Types
 * =============
 *
 * TypeScript types cho Course (tương ứng với Pydantic schemas).
 */

export interface Course {
  course_id: string;
  code: string;
  title: string;
  description?: string | null;
  short_description?: string | null;
  thumbnail_url?: string | null;
  category?: string | null;
  difficulty_level?: 'BEGINNER' | 'INTERMEDIATE' | 'ADVANCED' | 'EXPERT';
  estimated_hours?: number | null;
  status: 'DRAFT' | 'PUBLISHED' | 'ARCHIVED';
  published_at?: string | null;
  created_by?: string | null;
  created_at: string;
  updated_at: string;
}

export interface CourseCreate {
  code: string;
  title: string;
  description?: string;
  short_description?: string;
  thumbnail_url?: string;
  category?: string;
  difficulty_level?: 'BEGINNER' | 'INTERMEDIATE' | 'ADVANCED' | 'EXPERT';
  estimated_hours?: number;
  status?: 'DRAFT' | 'PUBLISHED' | 'ARCHIVED';
}

export interface CourseUpdate {
  code?: string;
  title?: string;
  description?: string;
  short_description?: string;
  thumbnail_url?: string;
  category?: string;
  difficulty_level?: 'BEGINNER' | 'INTERMEDIATE' | 'ADVANCED' | 'EXPERT';
  estimated_hours?: number;
  status?: 'DRAFT' | 'PUBLISHED' | 'ARCHIVED';
}

export interface CourseListResponse {
  total: number;
  courses: Course[];
}
