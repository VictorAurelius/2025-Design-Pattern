/**
 * Enrollment Management Page
 * ===========================
 *
 * Quản lý enrollments - enroll students vào courses.
 * - View enrollments (filter by course or user)
 * - Enroll student vào course
 * - Unenroll student khỏi course
 *
 * Author: Nguyễn Văn Kiệt - CNTT1-K63
 */

'use client';

import { useState, useEffect } from 'react';
import { useSearchParams } from 'next/navigation';
import apiClient from '@/lib/api';
import Link from 'next/link';

interface Enrollment {
  enrollment_id: string;
  user_id: string;
  course_id: string;
  role: string;
  status: string;
  enrolled_at: string;
  final_grade: number | null;
  student_email: string;
  student_name: string;
  course_code: string;
  course_title: string;
  submission_count: number;
  avg_score: number | null;
}

interface EnrollmentCreate {
  user_id: string;
  course_id: string;
  role: string;
}

interface Student {
  user_id: string;
  email: string;
  full_name: string;
}

interface Course {
  course_id: string;
  code: string;
  title: string;
  status: string;
}

export default function EnrollmentsPage() {
  const searchParams = useSearchParams();

  const [enrollments, setEnrollments] = useState<Enrollment[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // Filters
  const [courseFilter, setCourseFilter] = useState('');
  const [userFilter, setUserFilter] = useState('');

  // Enroll form
  const [showEnrollForm, setShowEnrollForm] = useState(false);
  const [enrollForm, setEnrollForm] = useState<EnrollmentCreate>({
    user_id: '',
    course_id: '',
    role: 'STUDENT',
  });

  // Dropdown data
  const [students, setStudents] = useState<Student[]>([]);
  const [courses, setCourses] = useState<Course[]>([]);
  const [loadingDropdowns, setLoadingDropdowns] = useState(false);

  // Initialize from URL params
  useEffect(() => {
    const courseIdParam = searchParams.get('course_id');
    const userIdParam = searchParams.get('user_id');

    if (courseIdParam) {
      setCourseFilter(courseIdParam);
      setEnrollForm({ ...enrollForm, course_id: courseIdParam });
    }
    if (userIdParam) {
      setUserFilter(userIdParam);
    }
  }, []);

  useEffect(() => {
    fetchEnrollments();
  }, [courseFilter, userFilter]);

  const fetchEnrollments = async () => {
    setLoading(true);
    setError(null);
    try {
      const params: any = {};
      if (courseFilter) params.course_id = courseFilter;
      if (userFilter) params.user_id = userFilter;

      const response = await apiClient.get('/api/enrollments', { params });
      setEnrollments(response.data.enrollments);
    } catch (err: any) {
      setError(err.response?.data?.detail || 'Lỗi khi tải enrollments');
    } finally {
      setLoading(false);
    }
  };

  const handleEnroll = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await apiClient.post('/api/enrollments', enrollForm);
      alert('Enroll thành công!');
      setShowEnrollForm(false);
      resetEnrollForm();
      fetchEnrollments();
    } catch (err: any) {
      alert(err.response?.data?.detail || 'Lỗi khi enroll');
    }
  };

  const handleUnenroll = async (enrollmentId: string) => {
    if (
      !confirm(
        'Unenroll student này? Tất cả submissions của student trong course này sẽ bị xóa!'
      )
    )
      return;
    try {
      await apiClient.delete(`/api/enrollments/${enrollmentId}`);
      alert('Unenroll thành công!');
      fetchEnrollments();
    } catch (err: any) {
      alert(err.response?.data?.detail || 'Lỗi khi unenroll');
    }
  };

  const resetEnrollForm = () => {
    setEnrollForm({
      user_id: '',
      course_id: courseFilter || '',
      role: 'STUDENT',
    });
  };

  const fetchStudents = async () => {
    setLoadingDropdowns(true);
    try {
      const response = await apiClient.get('/api/students');
      setStudents(response.data.students);
    } catch (err: any) {
      console.error('Error loading students:', err);
    } finally {
      setLoadingDropdowns(false);
    }
  };

  const fetchCourses = async () => {
    try {
      const response = await apiClient.get('/api/courses', {
        params: { limit: 100 },
      });
      setCourses(response.data.courses);
    } catch (err: any) {
      console.error('Error loading courses:', err);
    }
  };

  const openEnrollForm = () => {
    setShowEnrollForm(true);
    fetchStudents();
    fetchCourses();
  };

  return (
    <div className="max-w-7xl mx-auto">
      <div className="bg-white rounded-lg shadow-md p-6">
        {/* Header */}
        <h1 className="text-3xl font-bold text-gray-800 mb-2">
          👥 Enrollment Management
        </h1>
        <p className="text-gray-600 mb-6">Quản lý enrollments - enroll students vào courses</p>

        {/* Filters */}
        <div className="grid md:grid-cols-3 gap-4 mb-6">
          <input
            type="text"
            placeholder="Course ID (40000000-...)"
            value={courseFilter}
            onChange={(e) => setCourseFilter(e.target.value)}
            className="border px-4 py-2 rounded"
          />
          <input
            type="text"
            placeholder="User ID (20000000-...)"
            value={userFilter}
            onChange={(e) => setUserFilter(e.target.value)}
            className="border px-4 py-2 rounded"
          />
          <button
            onClick={openEnrollForm}
            className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
          >
            + Enroll Student
          </button>
        </div>

        {/* Loading / Error */}
        {loading && <p>Đang tải...</p>}
        {error && <p className="text-red-600">{error}</p>}

        {/* Enrollments Table */}
        {!loading && !error && (
          <>
            {enrollments.length === 0 ? (
              <div className="text-center py-8 text-gray-500">
                Không tìm thấy enrollments. Click "Enroll Student" để tạo mới.
              </div>
            ) : (
              <div className="overflow-x-auto">
                <table className="min-w-full border text-sm">
                  <thead className="bg-gray-100">
                    <tr>
                      <th className="px-4 py-3 border text-left">Student</th>
                      <th className="px-4 py-3 border text-left">Course</th>
                      <th className="px-4 py-3 border text-center">Role</th>
                      <th className="px-4 py-3 border text-center">Status</th>
                      <th className="px-4 py-3 border text-center">Submissions</th>
                      <th className="px-4 py-3 border text-center">Avg Score</th>
                      <th className="px-4 py-3 border text-center">Final Grade</th>
                      <th className="px-4 py-3 border text-center">Enrolled At</th>
                      <th className="px-4 py-3 border text-center">Actions</th>
                    </tr>
                  </thead>
                  <tbody>
                    {enrollments.map((enrollment) => (
                      <tr key={enrollment.enrollment_id} className="hover:bg-gray-50">
                        <td className="px-4 py-3 border">
                          <div className="font-semibold">{enrollment.student_name}</div>
                          <div className="text-xs text-gray-500">{enrollment.student_email}</div>
                        </td>
                        <td className="px-4 py-3 border">
                          <div className="font-mono text-xs font-semibold">
                            {enrollment.course_code}
                          </div>
                          <div className="text-xs">{enrollment.course_title}</div>
                        </td>
                        <td className="px-4 py-3 border text-center">
                          <span className="text-xs px-2 py-1 rounded bg-purple-100 text-purple-700">
                            {enrollment.role}
                          </span>
                        </td>
                        <td className="px-4 py-3 border text-center">
                          <span
                            className={`text-xs px-2 py-1 rounded ${
                              enrollment.status === 'ACTIVE'
                                ? 'bg-green-100 text-green-700'
                                : 'bg-gray-100 text-gray-700'
                            }`}
                          >
                            {enrollment.status}
                          </span>
                        </td>
                        <td className="px-4 py-3 border text-center">
                          <span className="font-bold">{enrollment.submission_count}</span>
                        </td>
                        <td className="px-4 py-3 border text-center">
                          {enrollment.avg_score !== null ? (
                            <span className="font-semibold text-blue-600">
                              {Number(enrollment.avg_score).toFixed(2)}
                            </span>
                          ) : (
                            <span className="text-gray-400">-</span>
                          )}
                        </td>
                        <td className="px-4 py-3 border text-center">
                          {enrollment.final_grade !== null ? (
                            <span className="font-bold text-green-600">
                              {Number(enrollment.final_grade).toFixed(2)}
                            </span>
                          ) : (
                            <span className="text-gray-400">-</span>
                          )}
                        </td>
                        <td className="px-4 py-3 border text-center text-xs">
                          {new Date(enrollment.enrolled_at).toLocaleDateString('vi-VN')}
                        </td>
                        <td className="px-4 py-3 border text-center">
                          <button
                            onClick={() => handleUnenroll(enrollment.enrollment_id)}
                            className="text-red-600 hover:underline text-sm"
                          >
                            Unenroll
                          </button>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}

            {/* Summary */}
            {enrollments.length > 0 && (
              <div className="mt-6 grid grid-cols-3 gap-4">
                <div className="bg-blue-50 rounded-lg p-4">
                  <p className="text-sm text-gray-600">Total Enrollments</p>
                  <p className="text-2xl font-bold text-blue-600">
                    {enrollments.length}
                  </p>
                </div>
                <div className="bg-green-50 rounded-lg p-4">
                  <p className="text-sm text-gray-600">Total Submissions</p>
                  <p className="text-2xl font-bold text-green-600">
                    {enrollments.reduce((sum, e) => sum + e.submission_count, 0)}
                  </p>
                </div>
                <div className="bg-purple-50 rounded-lg p-4">
                  <p className="text-sm text-gray-600">Avg Score (All)</p>
                  <p className="text-2xl font-bold text-purple-600">
                    {enrollments.filter((e) => e.avg_score !== null).length > 0
                      ? (
                          enrollments
                            .filter((e) => e.avg_score !== null)
                            .reduce((sum, e) => sum + (e.avg_score || 0), 0) /
                          enrollments.filter((e) => e.avg_score !== null).length
                        ).toFixed(2)
                      : '-'}
                  </p>
                </div>
              </div>
            )}
          </>
        )}
      </div>

      {/* Enroll Form Modal */}
      {showEnrollForm && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
          <div className="bg-white rounded-lg p-6 max-w-2xl w-full">
            <h2 className="text-2xl font-bold mb-4">Enroll Student vào Course</h2>
            <form onSubmit={handleEnroll}>
              <div className="space-y-4">
                {/* Student Selection */}
                <div>
                  <label className="block text-sm font-semibold mb-1">
                    Student *
                  </label>
                  {loadingDropdowns ? (
                    <div className="w-full border px-4 py-2 rounded text-gray-500">
                      Đang tải students...
                    </div>
                  ) : (
                    <select
                      required
                      value={enrollForm.user_id}
                      onChange={(e) =>
                        setEnrollForm({ ...enrollForm, user_id: e.target.value })
                      }
                      className="w-full border px-4 py-2 rounded"
                    >
                      <option value="">-- Chọn Student --</option>
                      {students.map((student) => (
                        <option key={student.user_id} value={student.user_id}>
                          {student.full_name} ({student.email})
                        </option>
                      ))}
                    </select>
                  )}
                  <p className="text-xs text-gray-500 mt-1">
                    💡 Chọn student từ danh sách
                  </p>
                </div>

                {/* Course Selection */}
                <div>
                  <label className="block text-sm font-semibold mb-1">
                    Course *
                  </label>
                  {loadingDropdowns ? (
                    <div className="w-full border px-4 py-2 rounded text-gray-500">
                      Đang tải courses...
                    </div>
                  ) : (
                    <select
                      required
                      value={enrollForm.course_id}
                      onChange={(e) =>
                        setEnrollForm({ ...enrollForm, course_id: e.target.value })
                      }
                      className="w-full border px-4 py-2 rounded"
                    >
                      <option value="">-- Chọn Course --</option>
                      {courses.map((course) => (
                        <option key={course.course_id} value={course.course_id}>
                          {course.code} - {course.title} ({course.status})
                        </option>
                      ))}
                    </select>
                  )}
                  <p className="text-xs text-gray-500 mt-1">
                    💡 Chọn course từ danh sách
                  </p>
                </div>
                <div>
                  <label className="block text-sm font-semibold mb-1">
                    Role *
                  </label>
                  <select
                    required
                    value={enrollForm.role}
                    onChange={(e) =>
                      setEnrollForm({ ...enrollForm, role: e.target.value })
                    }
                    className="w-full border px-4 py-2 rounded"
                  >
                    <option value="STUDENT">STUDENT</option>
                    <option value="TA">TA</option>
                    <option value="INSTRUCTOR">INSTRUCTOR</option>
                  </select>
                </div>
              </div>
              <div className="mt-6 flex justify-end gap-2">
                <button
                  type="button"
                  onClick={() => {
                    setShowEnrollForm(false);
                    resetEnrollForm();
                  }}
                  className="px-6 py-2 border rounded hover:bg-gray-100"
                >
                  Hủy
                </button>
                <button
                  type="submit"
                  className="px-6 py-2 bg-blue-600 text-white rounded hover:bg-blue-700"
                >
                  Enroll
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
