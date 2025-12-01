/**
 * Module Detail & Lecture Management Page
 * =========================================
 *
 * Hiển thị chi tiết module và quản lý lectures/assignments.
 * - View module info
 * - CRUD lectures (VIDEO, READING, ASSIGNMENT)
 * - Click assignment để view submissions
 *
 * Author: Nguyễn Văn Kiệt - CNTT1-K63
 */

'use client';

import { useState, useEffect } from 'react';
import { useParams, useRouter } from 'next/navigation';
import apiClient from '@/lib/api';
import Link from 'next/link';

interface Lecture {
  lecture_id: string;
  module_id: string;
  title: string;
  description: string | null;
  type: string; // VIDEO | READING | ASSIGNMENT
  order_num: number;
  assignment_config: any;
  created_at: string;
  submission_count: number;
}

interface LectureCreate {
  module_id: string;
  title: string;
  description: string;
  type: string;
  order_num: number;
  max_points: number;
  due_days: number;
}

export default function ModuleDetailPage() {
  const params = useParams();
  const router = useRouter();
  const moduleId = params.id as string;

  const [module, setModule] = useState<any>(null);
  const [lectures, setLectures] = useState<Lecture[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // Lecture form
  const [showLectureForm, setShowLectureForm] = useState(false);
  const [editingLecture, setEditingLecture] = useState<Lecture | null>(null);
  const [lectureForm, setLectureForm] = useState<LectureCreate>({
    module_id: moduleId,
    title: '',
    description: '',
    type: 'VIDEO',
    order_num: 1,
    max_points: 100,
    due_days: 7,
  });

  useEffect(() => {
    if (moduleId) {
      fetchModule();
      fetchLectures();
    }
  }, [moduleId]);

  const fetchModule = async () => {
    try {
      // Get first module from the list
      const response = await apiClient.get('/api/modules', {
        params: { course_id: 'temp' }, // Will be replaced
      });
      // This is a workaround - in production, should have GET /api/modules/:id
      // For now, just set a placeholder
      setModule({ title: 'Module', module_id: moduleId });
    } catch (err: any) {
      console.error('Error loading module:', err);
    }
  };

  const fetchLectures = async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await apiClient.get('/api/lectures', {
        params: { module_id: moduleId },
      });
      setLectures(response.data.lectures);
    } catch (err: any) {
      setError(err.response?.data?.detail || 'Lỗi khi tải lectures');
    } finally {
      setLoading(false);
    }
  };

  const handleCreateLecture = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await apiClient.post('/api/lectures', lectureForm);
      alert('Tạo lecture thành công!');
      setShowLectureForm(false);
      resetLectureForm();
      fetchLectures();
    } catch (err: any) {
      alert(err.response?.data?.detail || 'Lỗi khi tạo lecture');
    }
  };

  const handleUpdateLecture = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!editingLecture) return;
    try {
      await apiClient.put(`/api/lectures/${editingLecture.lecture_id}`, {
        title: lectureForm.title,
        description: lectureForm.description,
        order_num: lectureForm.order_num,
        max_points: lectureForm.max_points,
        due_days: lectureForm.due_days,
      });
      alert('Cập nhật lecture thành công!');
      setEditingLecture(null);
      setShowLectureForm(false);
      resetLectureForm();
      fetchLectures();
    } catch (err: any) {
      alert(err.response?.data?.detail || 'Lỗi khi cập nhật lecture');
    }
  };

  const handleDeleteLecture = async (lectureId: string) => {
    if (!confirm('Xóa lecture này? Nếu là assignment, tất cả submissions sẽ bị xóa!')) return;
    try {
      await apiClient.delete(`/api/lectures/${lectureId}`);
      alert('Xóa lecture thành công!');
      fetchLectures();
    } catch (err: any) {
      alert(err.response?.data?.detail || 'Lỗi khi xóa lecture');
    }
  };

  const openEditLecture = (lecture: Lecture) => {
    setEditingLecture(lecture);
    setLectureForm({
      module_id: lecture.module_id,
      title: lecture.title,
      description: lecture.description || '',
      type: lecture.type,
      order_num: lecture.order_num,
      max_points: lecture.assignment_config?.max_points || 100,
      due_days: 7,
    });
    setShowLectureForm(true);
  };

  const resetLectureForm = () => {
    setLectureForm({
      module_id: moduleId,
      title: '',
      description: '',
      type: 'VIDEO',
      order_num: lectures.length + 1,
      max_points: 100,
      due_days: 7,
    });
    setEditingLecture(null);
  };

  const getLectureIcon = (type: string) => {
    switch (type) {
      case 'VIDEO':
        return '🎥';
      case 'READING':
        return '📖';
      case 'ASSIGNMENT':
        return '✏️';
      default:
        return '📄';
    }
  };

  return (
    <div className="max-w-7xl mx-auto">
      <div className="bg-white rounded-lg shadow-md p-6">
        {/* Header */}
        <div className="mb-6">
          <button
            onClick={() => router.back()}
            className="inline-block text-blue-600 hover:text-blue-800 mb-4"
          >
            ← Quay lại Course
          </button>
          <h1 className="text-3xl font-bold text-gray-800">
            {module?.title || 'Module'}
          </h1>
          <p className="text-gray-600 mt-2">Quản lý lectures và assignments</p>
        </div>

        {/* Actions */}
        <div className="mb-6 flex justify-between items-center">
          <h2 className="text-2xl font-bold">📚 Lectures & Assignments</h2>
          <button
            onClick={() => {
              resetLectureForm();
              setShowLectureForm(true);
            }}
            className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
          >
            + Thêm Lecture/Assignment
          </button>
        </div>

        {/* Loading / Error */}
        {loading && <p>Đang tải lectures...</p>}
        {error && <p className="text-red-600">{error}</p>}

        {/* Lectures List */}
        {!loading && !error && (
          <>
            {lectures.length === 0 ? (
              <div className="text-center py-8 text-gray-500">
                Chưa có lectures. Click "Thêm Lecture/Assignment" để tạo mới.
              </div>
            ) : (
              <div className="space-y-4">
                {lectures.map((lecture) => (
                  <div
                    key={lecture.lecture_id}
                    className={`border-2 rounded-lg p-4 transition ${
                      lecture.type === 'ASSIGNMENT'
                        ? 'border-blue-300 bg-blue-50'
                        : 'border-gray-200 hover:border-gray-300'
                    }`}
                  >
                    <div className="flex justify-between items-start">
                      <div className="flex-1">
                        <div className="flex items-center gap-3">
                          <span className="text-sm font-semibold text-gray-500">
                            #{lecture.order_num}
                          </span>
                          <span className="text-2xl">
                            {getLectureIcon(lecture.type)}
                          </span>
                          <h3 className="text-xl font-bold text-gray-800">
                            {lecture.title}
                          </h3>
                          <span
                            className={`text-xs px-2 py-1 rounded font-semibold ${
                              lecture.type === 'ASSIGNMENT'
                                ? 'bg-blue-600 text-white'
                                : lecture.type === 'VIDEO'
                                ? 'bg-purple-100 text-purple-700'
                                : 'bg-green-100 text-green-700'
                            }`}
                          >
                            {lecture.type}
                          </span>
                        </div>
                        {lecture.description && (
                          <p className="text-gray-600 mt-2 ml-16">
                            {lecture.description}
                          </p>
                        )}
                        {lecture.type === 'ASSIGNMENT' && (
                          <div className="mt-3 ml-16 flex gap-4 text-sm">
                            <span className="text-blue-700 font-semibold">
                              Max Points:{' '}
                              {lecture.assignment_config?.max_points || 100}
                            </span>
                            <span className="text-gray-600">
                              📝 {lecture.submission_count} submissions
                            </span>
                          </div>
                        )}
                      </div>
                      <div className="flex flex-col gap-2 ml-4">
                        {lecture.type === 'ASSIGNMENT' && (
                          <Link
                            href={`/submissions?assignment_id=${lecture.lecture_id}`}
                            className="bg-purple-600 text-white px-4 py-2 rounded text-sm hover:bg-purple-700 text-center"
                          >
                            View Submissions →
                          </Link>
                        )}
                        <button
                          onClick={() => openEditLecture(lecture)}
                          className="bg-yellow-600 text-white px-4 py-2 rounded text-sm hover:bg-yellow-700"
                        >
                          Edit
                        </button>
                        <button
                          onClick={() => handleDeleteLecture(lecture.lecture_id)}
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

      {/* Lecture Form Modal */}
      {showLectureForm && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
          <div className="bg-white rounded-lg p-6 max-w-2xl w-full max-h-[90vh] overflow-y-auto">
            <h2 className="text-2xl font-bold mb-4">
              {editingLecture ? 'Sửa Lecture' : 'Thêm Lecture/Assignment Mới'}
            </h2>
            <form
              onSubmit={
                editingLecture ? handleUpdateLecture : handleCreateLecture
              }
            >
              <div className="space-y-4">
                <div>
                  <label className="block text-sm font-semibold mb-1">
                    Loại *
                  </label>
                  <select
                    required
                    value={lectureForm.type}
                    onChange={(e) =>
                      setLectureForm({ ...lectureForm, type: e.target.value })
                    }
                    className="w-full border px-4 py-2 rounded"
                    disabled={!!editingLecture}
                  >
                    <option value="VIDEO">VIDEO</option>
                    <option value="READING">READING</option>
                    <option value="ASSIGNMENT">ASSIGNMENT</option>
                  </select>
                </div>
                <div>
                  <label className="block text-sm font-semibold mb-1">
                    Tiêu đề *
                  </label>
                  <input
                    type="text"
                    required
                    value={lectureForm.title}
                    onChange={(e) =>
                      setLectureForm({ ...lectureForm, title: e.target.value })
                    }
                    className="w-full border px-4 py-2 rounded"
                    placeholder="Ví dụ: Introduction to Variables"
                  />
                </div>
                <div>
                  <label className="block text-sm font-semibold mb-1">
                    Mô tả
                  </label>
                  <textarea
                    value={lectureForm.description}
                    onChange={(e) =>
                      setLectureForm({
                        ...lectureForm,
                        description: e.target.value,
                      })
                    }
                    className="w-full border px-4 py-2 rounded"
                    rows={3}
                    placeholder="Mô tả về lecture này..."
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
                    value={lectureForm.order_num}
                    onChange={(e) =>
                      setLectureForm({
                        ...lectureForm,
                        order_num: parseInt(e.target.value),
                      })
                    }
                    className="w-full border px-4 py-2 rounded"
                  />
                </div>
                {lectureForm.type === 'ASSIGNMENT' && (
                  <>
                    <div>
                      <label className="block text-sm font-semibold mb-1">
                        Max Points (điểm tối đa)
                      </label>
                      <input
                        type="number"
                        required
                        min="1"
                        value={lectureForm.max_points}
                        onChange={(e) =>
                          setLectureForm({
                            ...lectureForm,
                            max_points: parseInt(e.target.value),
                          })
                        }
                        className="w-full border px-4 py-2 rounded"
                      />
                    </div>
                    <div>
                      <label className="block text-sm font-semibold mb-1">
                        Due Days (số ngày để hoàn thành)
                      </label>
                      <input
                        type="number"
                        required
                        min="1"
                        value={lectureForm.due_days}
                        onChange={(e) =>
                          setLectureForm({
                            ...lectureForm,
                            due_days: parseInt(e.target.value),
                          })
                        }
                        className="w-full border px-4 py-2 rounded"
                      />
                    </div>
                  </>
                )}
              </div>
              <div className="mt-6 flex justify-end gap-2">
                <button
                  type="button"
                  onClick={() => {
                    setShowLectureForm(false);
                    resetLectureForm();
                  }}
                  className="px-6 py-2 border rounded hover:bg-gray-100"
                >
                  Hủy
                </button>
                <button
                  type="submit"
                  className="px-6 py-2 bg-blue-600 text-white rounded hover:bg-blue-700"
                >
                  {editingLecture ? 'Cập nhật' : 'Tạo mới'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
