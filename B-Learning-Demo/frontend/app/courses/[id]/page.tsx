/**
 * Course Detail & Module Management Page
 * ========================================
 *
 * Hiển thị chi tiết course và quản lý modules.
 * - View course info
 * - CRUD modules
 * - Click module để quản lý lectures
 *
 * Author: Nguyễn Văn Kiệt - CNTT1-K63
 */

'use client';

import { useState, useEffect } from 'react';
import { useParams, useRouter } from 'next/navigation';
import apiClient from '@/lib/api';
import Link from 'next/link';

interface Module {
  module_id: string;
  course_id: string;
  title: string;
  description: string | null;
  order_num: number;
  created_at: string;
  lecture_count: number;
  assignment_count: number;
}

interface ModuleCreate {
  course_id: string;
  title: string;
  description: string;
  order_num: number;
}

export default function CourseDetailPage() {
  const params = useParams();
  const router = useRouter();
  const courseId = params.id as string;

  const [course, setCourse] = useState<any>(null);
  const [modules, setModules] = useState<Module[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // Module form
  const [showModuleForm, setShowModuleForm] = useState(false);
  const [editingModule, setEditingModule] = useState<Module | null>(null);
  const [moduleForm, setModuleForm] = useState<ModuleCreate>({
    course_id: courseId,
    title: '',
    description: '',
    order_num: 1,
  });

  useEffect(() => {
    if (courseId) {
      fetchCourse();
      fetchModules();
    }
  }, [courseId]);

  const fetchCourse = async () => {
    try {
      const response = await apiClient.get(`/api/courses/${courseId}`);
      setCourse(response.data);
    } catch (err: any) {
      setError(err.response?.data?.detail || 'Lỗi khi tải course');
    }
  };

  const fetchModules = async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await apiClient.get('/api/modules', {
        params: { course_id: courseId },
      });
      setModules(response.data.modules);
    } catch (err: any) {
      setError(err.response?.data?.detail || 'Lỗi khi tải modules');
    } finally {
      setLoading(false);
    }
  };

  const handleCreateModule = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await apiClient.post('/api/modules', moduleForm);
      alert('Tạo module thành công!');
      setShowModuleForm(false);
      resetModuleForm();
      fetchModules();
    } catch (err: any) {
      alert(err.response?.data?.detail || 'Lỗi khi tạo module');
    }
  };

  const handleUpdateModule = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!editingModule) return;
    try {
      await apiClient.put(`/api/modules/${editingModule.module_id}`, {
        title: moduleForm.title,
        description: moduleForm.description,
        order_num: moduleForm.order_num,
      });
      alert('Cập nhật module thành công!');
      setEditingModule(null);
      setShowModuleForm(false);
      resetModuleForm();
      fetchModules();
    } catch (err: any) {
      alert(err.response?.data?.detail || 'Lỗi khi cập nhật module');
    }
  };

  const handleDeleteModule = async (moduleId: string) => {
    if (!confirm('Xóa module này? Tất cả lectures bên trong sẽ bị xóa!')) return;
    try {
      await apiClient.delete(`/api/modules/${moduleId}`);
      alert('Xóa module thành công!');
      fetchModules();
    } catch (err: any) {
      alert(err.response?.data?.detail || 'Lỗi khi xóa module');
    }
  };

  const openEditModule = (module: Module) => {
    setEditingModule(module);
    setModuleForm({
      course_id: module.course_id,
      title: module.title,
      description: module.description || '',
      order_num: module.order_num,
    });
    setShowModuleForm(true);
  };

  const resetModuleForm = () => {
    setModuleForm({
      course_id: courseId,
      title: '',
      description: '',
      order_num: modules.length + 1,
    });
    setEditingModule(null);
  };

  if (!course) {
    return (
      <div className="max-w-7xl mx-auto">
        <div className="bg-white rounded-lg shadow-md p-6">
          <p className="text-center py-8">Đang tải...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="max-w-7xl mx-auto">
      <div className="bg-white rounded-lg shadow-md p-6">
        {/* Header */}
        <div className="mb-6">
          <Link
            href="/courses"
            className="inline-block text-blue-600 hover:text-blue-800 mb-4"
          >
            ← Quay lại Courses
          </Link>
          <h1 className="text-3xl font-bold text-gray-800">
            {course.code} - {course.title}
          </h1>
          <p className="text-gray-600 mt-2">{course.description}</p>
          <div className="mt-4 flex gap-4">
            <span className="px-3 py-1 bg-blue-100 text-blue-700 rounded">
              {course.difficulty_level}
            </span>
            <span className="px-3 py-1 bg-green-100 text-green-700 rounded">
              {course.status}
            </span>
          </div>
        </div>

        {/* Actions */}
        <div className="mb-6 flex justify-between items-center">
          <h2 className="text-2xl font-bold">📚 Modules</h2>
          <div className="space-x-2">
            <Link
              href={`/enrollments?course_id=${courseId}`}
              className="inline-block bg-purple-600 text-white px-4 py-2 rounded hover:bg-purple-700"
            >
              👥 Manage Enrollments
            </Link>
            <button
              onClick={() => {
                resetModuleForm();
                setShowModuleForm(true);
              }}
              className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
            >
              + Thêm Module
            </button>
          </div>
        </div>

        {/* Loading / Error */}
        {loading && <p>Đang tải modules...</p>}
        {error && <p className="text-red-600">{error}</p>}

        {/* Modules List */}
        {!loading && !error && (
          <>
            {modules.length === 0 ? (
              <div className="text-center py-8 text-gray-500">
                Chưa có modules. Click "Thêm Module" để tạo mới.
              </div>
            ) : (
              <div className="space-y-4">
                {modules.map((module) => (
                  <div
                    key={module.module_id}
                    className="border-2 border-gray-200 rounded-lg p-4 hover:border-blue-300 transition"
                  >
                    <div className="flex justify-between items-start">
                      <div className="flex-1">
                        <div className="flex items-center gap-2">
                          <span className="text-sm font-semibold text-gray-500">
                            #{module.order_num}
                          </span>
                          <h3 className="text-xl font-bold text-gray-800">
                            {module.title}
                          </h3>
                        </div>
                        {module.description && (
                          <p className="text-gray-600 mt-2">
                            {module.description}
                          </p>
                        )}
                        <div className="mt-3 flex gap-4 text-sm">
                          <span className="text-gray-600">
                            📄 {module.lecture_count} lectures
                          </span>
                          <span className="text-blue-600 font-semibold">
                            ✏️ {module.assignment_count} assignments
                          </span>
                        </div>
                      </div>
                      <div className="flex flex-col gap-2 ml-4">
                        <Link
                          href={`/modules/${module.module_id}`}
                          className="bg-green-600 text-white px-4 py-2 rounded text-sm hover:bg-green-700 text-center"
                        >
                          Manage Lectures →
                        </Link>
                        <button
                          onClick={() => openEditModule(module)}
                          className="bg-yellow-600 text-white px-4 py-2 rounded text-sm hover:bg-yellow-700"
                        >
                          Edit
                        </button>
                        <button
                          onClick={() => handleDeleteModule(module.module_id)}
                          className="bg-red-600 text-white px-4 py-2 rounded text-sm hover:bg-red-700"
                        >
                          Delete
                        </button>
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </>
        )}
      </div>

      {/* Module Form Modal */}
      {showModuleForm && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
          <div className="bg-white rounded-lg p-6 max-w-2xl w-full">
            <h2 className="text-2xl font-bold mb-4">
              {editingModule ? 'Sửa Module' : 'Thêm Module Mới'}
            </h2>
            <form
              onSubmit={editingModule ? handleUpdateModule : handleCreateModule}
            >
              <div className="space-y-4">
                <div>
                  <label className="block text-sm font-semibold mb-1">
                    Tên Module *
                  </label>
                  <input
                    type="text"
                    required
                    value={moduleForm.title}
                    onChange={(e) =>
                      setModuleForm({ ...moduleForm, title: e.target.value })
                    }
                    className="w-full border px-4 py-2 rounded"
                    placeholder="Ví dụ: Introduction to Java"
                  />
                </div>
                <div>
                  <label className="block text-sm font-semibold mb-1">
                    Mô tả
                  </label>
                  <textarea
                    value={moduleForm.description}
                    onChange={(e) =>
                      setModuleForm({
                        ...moduleForm,
                        description: e.target.value,
                      })
                    }
                    className="w-full border px-4 py-2 rounded"
                    rows={3}
                    placeholder="Mô tả về module này..."
                  />
                </div>
                <div>
                  <label className="block text-sm font-semibold mb-1">
                    Thứ tự hiển thị
                  </label>
                  <input
                    type="number"
                    required
                    min="1"
                    value={moduleForm.order_num}
                    onChange={(e) =>
                      setModuleForm({
                        ...moduleForm,
                        order_num: parseInt(e.target.value),
                      })
                    }
                    className="w-full border px-4 py-2 rounded"
                  />
                </div>
              </div>
              <div className="mt-6 flex justify-end gap-2">
                <button
                  type="button"
                  onClick={() => {
                    setShowModuleForm(false);
                    resetModuleForm();
                  }}
                  className="px-6 py-2 border rounded hover:bg-gray-100"
                >
                  Hủy
                </button>
                <button
                  type="submit"
                  className="px-6 py-2 bg-blue-600 text-white rounded hover:bg-blue-700"
                >
                  {editingModule ? 'Cập nhật' : 'Tạo mới'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
