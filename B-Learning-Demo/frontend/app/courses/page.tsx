/**
 * Course Management Page
 * =======================
 *
 * Page 1: Quản lý Course (1 bảng)
 * - CRUD operations
 * - Search và filter
 * - Pagination
 *
 * Author: Nguyễn Văn Kiệt - CNTT1-K63
 */

'use client';

import { useState, useEffect } from 'react';
import apiClient from '@/lib/api';
import Link from 'next/link';
import type {
  Course,
  CourseCreate,
  CourseUpdate,
  CourseListResponse,
} from '@/types/course';

export default function CoursesPage() {
  // State management
  const [courses, setCourses] = useState<Course[]>([]);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // Filters
  const [search, setSearch] = useState('');
  const [statusFilter, setStatusFilter] = useState('');
  const [difficultyFilter, setDifficultyFilter] = useState('');

  // Pagination
  const [page, setPage] = useState(1);
  const limit = 10;

  // Form state
  const [showForm, setShowForm] = useState(false);
  const [editingCourse, setEditingCourse] = useState<Course | null>(null);
  const [formData, setFormData] = useState<CourseCreate>({
    code: '',
    title: '',
    description: '',
    difficulty_level: 'BEGINNER',
    status: 'DRAFT',
  });

  // Fetch courses
  const fetchCourses = async () => {
    setLoading(true);
    setError(null);
    try {
      const params: any = {
        limit,
        offset: (page - 1) * limit,
      };
      if (search) params.search = search;
      if (statusFilter) params.status = statusFilter;
      if (difficultyFilter) params.difficulty = difficultyFilter;

      const response = await apiClient.get<CourseListResponse>(
        '/api/courses',
        { params }
      );
      setCourses(response.data.courses);
      setTotal(response.data.total);
    } catch (err: any) {
      setError(err.response?.data?.detail || 'Lỗi khi tải courses');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchCourses();
  }, [page, search, statusFilter, difficultyFilter]);

  // Create course
  const handleCreate = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await apiClient.post('/api/courses', formData);
      alert('Tạo course thành công!');
      setShowForm(false);
      resetForm();
      fetchCourses();
    } catch (err: any) {
      alert(err.response?.data?.detail || 'Lỗi khi tạo course');
    }
  };

  // Update course
  const handleUpdate = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!editingCourse) return;
    try {
      await apiClient.put(
        `/api/courses/${editingCourse.course_id}`,
        formData
      );
      alert('Cập nhật course thành công!');
      setShowForm(false);
      setEditingCourse(null);
      resetForm();
      fetchCourses();
    } catch (err: any) {
      alert(err.response?.data?.detail || 'Lỗi khi cập nhật course');
    }
  };

  // Delete course
  const handleDelete = async (courseId: string) => {
    if (!confirm('Bạn có chắc muốn xóa course này?')) return;
    try {
      await apiClient.delete(`/api/courses/${courseId}`);
      alert('Xóa course thành công!');
      fetchCourses();
    } catch (err: any) {
      alert(err.response?.data?.detail || 'Lỗi khi xóa course');
    }
  };

  // Reset form
  const resetForm = () => {
    setFormData({
      code: '',
      title: '',
      description: '',
      difficulty_level: 'BEGINNER',
      status: 'DRAFT',
    });
  };

  // Open edit form
  const handleEdit = (course: Course) => {
    setEditingCourse(course);
    setFormData({
      code: course.code,
      title: course.title,
      description: course.description || '',
      short_description: course.short_description || '',
      category: course.category || '',
      difficulty_level: course.difficulty_level || 'BEGINNER',
      estimated_hours: course.estimated_hours || undefined,
      status: course.status,
    });
    setShowForm(true);
  };

  return (
    <div className="max-w-7xl mx-auto">
      <div className="bg-white rounded-lg shadow-md p-6">
        {/* Header */}
        <div className="flex justify-between items-center mb-6">
          <h1 className="text-3xl font-bold text-gray-800">
            📚 Course Management (1 Bảng)
          </h1>
          <button
            onClick={() => {
              resetForm();
              setEditingCourse(null);
              setShowForm(true);
            }}
            className="bg-primary-600 text-white px-4 py-2 rounded hover:bg-primary-700"
          >
            ➕ Tạo Course Mới
          </button>
        </div>

        {/* Filters */}
        <div className="grid md:grid-cols-3 gap-4 mb-6">
          <input
            type="text"
            placeholder="Tìm kiếm (title, description)..."
            value={search}
            onChange={(e) => {
              setSearch(e.target.value);
              setPage(1);
            }}
            className="border px-4 py-2 rounded"
          />
          <select
            value={statusFilter}
            onChange={(e) => {
              setStatusFilter(e.target.value);
              setPage(1);
            }}
            className="border px-4 py-2 rounded"
          >
            <option value="">Tất cả trạng thái</option>
            <option value="DRAFT">DRAFT</option>
            <option value="PUBLISHED">PUBLISHED</option>
            <option value="ARCHIVED">ARCHIVED</option>
          </select>
          <select
            value={difficultyFilter}
            onChange={(e) => {
              setDifficultyFilter(e.target.value);
              setPage(1);
            }}
            className="border px-4 py-2 rounded"
          >
            <option value="">Tất cả độ khó</option>
            <option value="BEGINNER">BEGINNER</option>
            <option value="INTERMEDIATE">INTERMEDIATE</option>
            <option value="ADVANCED">ADVANCED</option>
            <option value="EXPERT">EXPERT</option>
          </select>
        </div>

        {/* Loading / Error */}
        {loading && <p>Đang tải...</p>}
        {error && <p className="text-red-600">{error}</p>}

        {/* Courses Table */}
        {!loading && !error && (
          <>
            <div className="overflow-x-auto">
              <table className="min-w-full border">
                <thead className="bg-gray-100">
                  <tr>
                    <th className="px-4 py-2 border">Code</th>
                    <th className="px-4 py-2 border">Title</th>
                    <th className="px-4 py-2 border">Category</th>
                    <th className="px-4 py-2 border">Difficulty</th>
                    <th className="px-4 py-2 border">Status</th>
                    <th className="px-4 py-2 border">Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {courses.map((course) => (
                    <tr key={course.course_id} className="hover:bg-gray-50">
                      <td className="px-4 py-2 border font-mono">
                        {course.code}
                      </td>
                      <td className="px-4 py-2 border">{course.title}</td>
                      <td className="px-4 py-2 border">
                        {course.category || '-'}
                      </td>
                      <td className="px-4 py-2 border">
                        <span className="text-xs bg-blue-100 px-2 py-1 rounded">
                          {course.difficulty_level}
                        </span>
                      </td>
                      <td className="px-4 py-2 border">
                        <span
                          className={`text-xs px-2 py-1 rounded ${
                            course.status === 'PUBLISHED'
                              ? 'bg-green-100'
                              : course.status === 'DRAFT'
                              ? 'bg-yellow-100'
                              : 'bg-gray-100'
                          }`}
                        >
                          {course.status}
                        </span>
                      </td>
                      <td className="px-4 py-2 border">
                        <div className="flex flex-col gap-1">
                          <Link
                            href={`/courses/${course.course_id}`}
                            className="text-green-600 hover:underline font-semibold text-sm"
                          >
                            📚 Modules →
                          </Link>
                          <button
                            onClick={() => handleEdit(course)}
                            className="text-blue-600 hover:underline text-sm text-left"
                          >
                            Edit
                          </button>
                          <button
                            onClick={() => handleDelete(course.course_id)}
                            className="text-red-600 hover:underline text-sm text-left"
                          >
                            Delete
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>

            {/* Pagination */}
            <div className="flex justify-between items-center mt-4">
              <p className="text-gray-600">
                Tổng: {total} courses | Trang {page} / {Math.ceil(total / limit)}
              </p>
              <div className="space-x-2">
                <button
                  onClick={() => setPage(page - 1)}
                  disabled={page === 1}
                  className="px-4 py-2 border rounded disabled:opacity-50"
                >
                  ← Trước
                </button>
                <button
                  onClick={() => setPage(page + 1)}
                  disabled={page >= Math.ceil(total / limit)}
                  className="px-4 py-2 border rounded disabled:opacity-50"
                >
                  Sau →
                </button>
              </div>
            </div>
          </>
        )}
      </div>

      {/* Create/Edit Form Modal */}
      {showForm && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4">
          <div className="bg-white rounded-lg p-6 max-w-2xl w-full max-h-[90vh] overflow-y-auto">
            <h2 className="text-2xl font-bold mb-4">
              {editingCourse ? 'Cập Nhật Course' : 'Tạo Course Mới'}
            </h2>
            <form onSubmit={editingCourse ? handleUpdate : handleCreate}>
              <div className="space-y-4">
                <div>
                  <label className="block font-semibold mb-1">Code *</label>
                  <input
                    type="text"
                    required
                    value={formData.code}
                    onChange={(e) =>
                      setFormData({ ...formData, code: e.target.value })
                    }
                    className="w-full border px-3 py-2 rounded"
                  />
                </div>
                <div>
                  <label className="block font-semibold mb-1">Title *</label>
                  <input
                    type="text"
                    required
                    value={formData.title}
                    onChange={(e) =>
                      setFormData({ ...formData, title: e.target.value })
                    }
                    className="w-full border px-3 py-2 rounded"
                  />
                </div>
                <div>
                  <label className="block font-semibold mb-1">
                    Description
                  </label>
                  <textarea
                    value={formData.description}
                    onChange={(e) =>
                      setFormData({ ...formData, description: e.target.value })
                    }
                    className="w-full border px-3 py-2 rounded"
                    rows={3}
                  />
                </div>
                <div className="grid md:grid-cols-2 gap-4">
                  <div>
                    <label className="block font-semibold mb-1">
                      Difficulty
                    </label>
                    <select
                      value={formData.difficulty_level}
                      onChange={(e) =>
                        setFormData({
                          ...formData,
                          difficulty_level: e.target.value as any,
                        })
                      }
                      className="w-full border px-3 py-2 rounded"
                    >
                      <option value="BEGINNER">BEGINNER</option>
                      <option value="INTERMEDIATE">INTERMEDIATE</option>
                      <option value="ADVANCED">ADVANCED</option>
                      <option value="EXPERT">EXPERT</option>
                    </select>
                  </div>
                  <div>
                    <label className="block font-semibold mb-1">Status</label>
                    <select
                      value={formData.status}
                      onChange={(e) =>
                        setFormData({
                          ...formData,
                          status: e.target.value as any,
                        })
                      }
                      className="w-full border px-3 py-2 rounded"
                    >
                      <option value="DRAFT">DRAFT</option>
                      <option value="PUBLISHED">PUBLISHED</option>
                      <option value="ARCHIVED">ARCHIVED</option>
                    </select>
                  </div>
                </div>
                <div className="flex justify-end space-x-2 mt-6">
                  <button
                    type="button"
                    onClick={() => {
                      setShowForm(false);
                      setEditingCourse(null);
                    }}
                    className="px-4 py-2 border rounded"
                  >
                    Hủy
                  </button>
                  <button
                    type="submit"
                    className="px-4 py-2 bg-primary-600 text-white rounded hover:bg-primary-700"
                  >
                    {editingCourse ? 'Cập Nhật' : 'Tạo Mới'}
                  </button>
                </div>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
