/**
 * Submission Management Page
 * ===========================
 *
 * Page 2: Quản lý Assignment Submissions (nhiều bảng)
 * - JOIN nhiều bảng: AssignmentSubmission, User, Assignment, Course
 * - Chấm điểm
 * - Thống kê
 * - Filter nâng cao
 *
 * Author: Nguyễn Văn Kiệt - CNTT1-K63
 */

'use client';

import { useState, useEffect } from 'react';
import apiClient from '@/lib/api';
import type {
  SubmissionListItem,
  SubmissionDetail,
  SubmissionListResponse,
  GradeSubmissionRequest,
  SubmissionStats,
} from '@/types/submission';

export default function SubmissionsPage() {
  // State
  const [submissions, setSubmissions] = useState<SubmissionListItem[]>([]);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // Stats
  const [stats, setStats] = useState<SubmissionStats | null>(null);

  // Filters
  const [statusFilter, setStatusFilter] = useState('');
  const [isLateFilter, setIsLateFilter] = useState('');
  const [studentSearch, setStudentSearch] = useState('');

  // Pagination
  const [page, setPage] = useState(1);
  const limit = 10;

  // Grading modal
  const [gradingSubmission, setGradingSubmission] = useState<
    SubmissionDetail | null
  >(null);
  const [gradeData, setGradeData] = useState<GradeSubmissionRequest>({
    manual_score: 0,
    feedback: '',
  });

  // Fetch submissions
  const fetchSubmissions = async () => {
    setLoading(true);
    setError(null);
    try {
      const params: any = {
        limit,
        offset: (page - 1) * limit,
      };
      if (statusFilter) params.status = statusFilter;
      if (isLateFilter) params.is_late = isLateFilter === 'true';
      if (studentSearch) params.student_email = studentSearch;

      const response = await apiClient.get<SubmissionListResponse>(
        '/api/submissions',
        { params }
      );
      setSubmissions(response.data.submissions);
      setTotal(response.data.total);
    } catch (err: any) {
      setError(err.response?.data?.detail || 'Lỗi khi tải submissions');
    } finally {
      setLoading(false);
    }
  };

  // Fetch stats
  const fetchStats = async () => {
    try {
      const response = await apiClient.get<SubmissionStats>(
        '/api/submissions/stats/overview'
      );
      setStats(response.data);
    } catch (err) {
      console.error('Error fetching stats:', err);
    }
  };

  useEffect(() => {
    fetchSubmissions();
    fetchStats();
  }, [page, statusFilter, isLateFilter, studentSearch]);

  // Open grading modal
  const handleGrade = async (submissionId: string) => {
    try {
      const response = await apiClient.get<SubmissionDetail>(
        `/api/submissions/${submissionId}`
      );
      setGradingSubmission(response.data);
      setGradeData({
        manual_score: response.data.manual_score || 0,
        feedback: response.data.feedback || '',
      });
    } catch (err: any) {
      alert(err.response?.data?.detail || 'Lỗi khi tải submission detail');
    }
  };

  // Submit grade
  const handleSubmitGrade = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!gradingSubmission) return;

    try {
      await apiClient.put(
        `/api/submissions/${gradingSubmission.assignment_submission_id}/grade`,
        gradeData
      );
      alert('Chấm điểm thành công!');
      setGradingSubmission(null);
      fetchSubmissions();
      fetchStats();
    } catch (err: any) {
      alert(err.response?.data?.detail || 'Lỗi khi chấm điểm');
    }
  };

  return (
    <div className="max-w-7xl mx-auto">
      {/* Stats Cards */}
      {stats && (
        <div className="grid md:grid-cols-4 gap-4 mb-6">
          <div className="bg-white rounded-lg shadow p-4">
            <p className="text-gray-600 text-sm">Tổng Submissions</p>
            <p className="text-2xl font-bold">{stats.total_submissions}</p>
          </div>
          <div className="bg-green-50 rounded-lg shadow p-4">
            <p className="text-gray-600 text-sm">Đã Chấm</p>
            <p className="text-2xl font-bold text-green-600">
              {stats.graded}
            </p>
          </div>
          <div className="bg-yellow-50 rounded-lg shadow p-4">
            <p className="text-gray-600 text-sm">Chưa Chấm</p>
            <p className="text-2xl font-bold text-yellow-600">
              {stats.pending}
            </p>
          </div>
          <div className="bg-blue-50 rounded-lg shadow p-4">
            <p className="text-gray-600 text-sm">Điểm TB</p>
            <p className="text-2xl font-bold text-blue-600">
              {stats.average_score !== null && stats.average_score !== undefined
                ? Number(stats.average_score).toFixed(2)
                : '-'}
            </p>
          </div>
        </div>
      )}

      <div className="bg-white rounded-lg shadow-md p-6">
        {/* Header */}
        <h1 className="text-3xl font-bold text-gray-800 mb-2">
          📝 Submission Management (Nhiều Bảng)
        </h1>
        <p className="text-gray-600 mb-6">
          JOIN: AssignmentSubmission, User, Assignment, Course, GradeBook
        </p>

        {/* Filters */}
        <div className="grid md:grid-cols-3 gap-4 mb-6">
          <input
            type="text"
            placeholder="Tìm kiếm sinh viên (email)..."
            value={studentSearch}
            onChange={(e) => {
              setStudentSearch(e.target.value);
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
            <option value="SUBMITTED">SUBMITTED</option>
            <option value="GRADING">GRADING</option>
            <option value="GRADED">GRADED</option>
            <option value="RETURNED">RETURNED</option>
          </select>
          <select
            value={isLateFilter}
            onChange={(e) => {
              setIsLateFilter(e.target.value);
              setPage(1);
            }}
            className="border px-4 py-2 rounded"
          >
            <option value="">Tất cả</option>
            <option value="true">Nộp trễ</option>
            <option value="false">Đúng hạn</option>
          </select>
        </div>

        {/* Loading / Error */}
        {loading && <p>Đang tải...</p>}
        {error && <p className="text-red-600">{error}</p>}

        {/* Submissions Table */}
        {!loading && !error && (
          <>
            <div className="overflow-x-auto">
              <table className="min-w-full border text-sm">
                <thead className="bg-gray-100">
                  <tr>
                    <th className="px-3 py-2 border">Student</th>
                    <th className="px-3 py-2 border">Course</th>
                    <th className="px-3 py-2 border">Assignment</th>
                    <th className="px-3 py-2 border">Submitted</th>
                    <th className="px-3 py-2 border">Status</th>
                    <th className="px-3 py-2 border">Score</th>
                    <th className="px-3 py-2 border">Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {submissions.map((sub) => (
                    <tr key={sub.assignment_submission_id} className="hover:bg-gray-50">
                      <td className="px-3 py-2 border">
                        <div className="font-semibold">{sub.student_name}</div>
                        <div className="text-xs text-gray-500">
                          {sub.student_email}
                        </div>
                      </td>
                      <td className="px-3 py-2 border">
                        <div className="font-mono text-xs">
                          {sub.course_code}
                        </div>
                        <div className="text-xs">{sub.course_title}</div>
                      </td>
                      <td className="px-3 py-2 border">
                        <div>{sub.assignment_title}</div>
                        <div className="text-xs">
                          <span className="bg-purple-100 px-1 rounded">
                            {sub.assignment_type}
                          </span>
                        </div>
                      </td>
                      <td className="px-3 py-2 border">
                        <div>
                          {new Date(sub.submitted_at).toLocaleDateString()}
                        </div>
                        {sub.is_late && (
                          <span className="text-xs text-red-600">
                            ⚠️ Trễ {sub.days_late} ngày
                          </span>
                        )}
                      </td>
                      <td className="px-3 py-2 border">
                        <span
                          className={`text-xs px-2 py-1 rounded ${
                            sub.status === 'GRADED'
                              ? 'bg-green-100'
                              : sub.status === 'SUBMITTED'
                              ? 'bg-yellow-100'
                              : 'bg-gray-100'
                          }`}
                        >
                          {sub.status}
                        </span>
                      </td>
                      <td className="px-3 py-2 border text-center">
                        {sub.final_score !== null && sub.final_score !== undefined ? (
                          <div>
                            <div className="font-bold">
                              {Number(sub.final_score).toFixed(2)}
                            </div>
                            <div className="text-xs text-gray-500">
                              / {sub.max_points}
                            </div>
                          </div>
                        ) : (
                          <span className="text-gray-400">-</span>
                        )}
                      </td>
                      <td className="px-3 py-2 border">
                        <button
                          onClick={() =>
                            handleGrade(sub.assignment_submission_id)
                          }
                          className="text-blue-600 hover:underline text-sm"
                        >
                          {sub.status === 'GRADED' ? 'View' : 'Grade'}
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>

            {/* Pagination */}
            <div className="flex justify-between items-center mt-4">
              <p className="text-gray-600">
                Tổng: {total} submissions | Trang {page} /{' '}
                {Math.ceil(total / limit)}
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

      {/* Grading Modal */}
      {gradingSubmission && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4">
          <div className="bg-white rounded-lg p-6 max-w-3xl w-full max-h-[90vh] overflow-y-auto">
            <h2 className="text-2xl font-bold mb-4">Chấm Điểm Submission</h2>

            {/* Submission Info */}
            <div className="bg-gray-50 p-4 rounded mb-4 space-y-2">
              <div className="grid md:grid-cols-2 gap-2 text-sm">
                <div>
                  <strong>Student:</strong> {gradingSubmission.student_name}
                </div>
                <div>
                  <strong>Email:</strong> {gradingSubmission.student_email}
                </div>
                <div>
                  <strong>Course:</strong> {gradingSubmission.course_code} -{' '}
                  {gradingSubmission.course_title}
                </div>
                <div>
                  <strong>Assignment:</strong>{' '}
                  {gradingSubmission.assignment_title}
                </div>
                <div>
                  <strong>Type:</strong> {gradingSubmission.assignment_type}
                </div>
                <div>
                  <strong>Max Points:</strong> {gradingSubmission.max_points}
                </div>
                <div>
                  <strong>Submitted:</strong>{' '}
                  {new Date(gradingSubmission.submitted_at).toLocaleString()}
                </div>
                <div>
                  <strong>Late:</strong>{' '}
                  {gradingSubmission.is_late
                    ? `Yes (${gradingSubmission.days_late} days)`
                    : 'No'}
                </div>
              </div>
            </div>

            {/* Content */}
            {gradingSubmission.content && (
              <div className="mb-4">
                <strong className="block mb-2">Nội dung:</strong>
                <div className="border p-3 rounded bg-gray-50 text-sm max-h-40 overflow-y-auto">
                  {gradingSubmission.content}
                </div>
              </div>
            )}

            {/* Grading Form */}
            <form onSubmit={handleSubmitGrade}>
              <div className="space-y-4">
                <div>
                  <label className="block font-semibold mb-1">
                    Manual Score * (Max: {gradingSubmission.max_points})
                  </label>
                  <input
                    type="number"
                    step="0.01"
                    min="0"
                    max={gradingSubmission.max_points}
                    required
                    value={gradeData.manual_score}
                    onChange={(e) =>
                      setGradeData({
                        ...gradeData,
                        manual_score: parseFloat(e.target.value),
                      })
                    }
                    className="w-full border px-3 py-2 rounded"
                  />
                </div>
                <div>
                  <label className="block font-semibold mb-1">Feedback</label>
                  <textarea
                    value={gradeData.feedback}
                    onChange={(e) =>
                      setGradeData({ ...gradeData, feedback: e.target.value })
                    }
                    className="w-full border px-3 py-2 rounded"
                    rows={4}
                    placeholder="Nhận xét về bài làm của sinh viên..."
                  />
                </div>

                <div className="flex justify-end space-x-2 mt-6">
                  <button
                    type="button"
                    onClick={() => setGradingSubmission(null)}
                    className="px-4 py-2 border rounded"
                  >
                    Đóng
                  </button>
                  <button
                    type="submit"
                    className="px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700"
                  >
                    Lưu Điểm
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
