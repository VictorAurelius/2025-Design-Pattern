/**
 * Students List Page
 * ==================
 *
 * Hiển thị danh sách students để demo filter student_email
 * JOIN: User, UserRole, Role, COUNT(AssignmentSubmission)
 *
 * Author: Nguyễn Văn Kiệt - CNTT1-K63
 */

'use client';

import { useState, useEffect } from 'react';
import apiClient from '@/lib/api';
import Link from 'next/link';

interface Student {
  user_id: string;
  email: string;
  full_name: string;
  first_name: string;
  last_name: string;
  account_status: string;
  total_submissions: number;
  graded_count: number;
  pending_count: number;
  avg_score: number | null;
  late_count: number;
  enrolled_courses: number;
}

export default function StudentsPage() {
  const [students, setStudents] = useState<Student[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [searchEmail, setSearchEmail] = useState('');

  useEffect(() => {
    fetchStudents();
  }, []);

  const fetchStudents = async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await apiClient.get('/api/students');
      setStudents(response.data.students);
    } catch (err: any) {
      setError(err.response?.data?.detail || 'Lỗi khi tải students');
    } finally {
      setLoading(false);
    }
  };

  // Filter students by search
  const filteredStudents = students.filter((student) =>
    student.email.toLowerCase().includes(searchEmail.toLowerCase()) ||
    student.full_name.toLowerCase().includes(searchEmail.toLowerCase())
  );

  return (
    <div className="max-w-7xl mx-auto">
      <div className="bg-white rounded-lg shadow-md p-6">
        {/* Header */}
        <h1 className="text-3xl font-bold text-gray-800 mb-2">
          👨‍🎓 Students List
        </h1>
        <p className="text-gray-600 mb-6">
          Click student để filter Submissions theo student_email
        </p>

        {/* Search */}
        <div className="mb-6">
          <input
            type="text"
            placeholder="Tìm kiếm theo email hoặc tên..."
            value={searchEmail}
            onChange={(e) => setSearchEmail(e.target.value)}
            className="w-full md:w-1/2 border px-4 py-2 rounded"
          />
        </div>

        {/* Loading / Error */}
        {loading && <p>Đang tải...</p>}
        {error && <p className="text-red-600">{error}</p>}

        {/* Students Table */}
        {!loading && !error && (
          <>
            <div className="overflow-x-auto">
              <table className="min-w-full border text-sm">
                <thead className="bg-gray-100">
                  <tr>
                    <th className="px-4 py-3 border text-left">Student</th>
                    <th className="px-4 py-3 border text-left">Email</th>
                    <th className="px-4 py-3 border text-center">Courses</th>
                    <th className="px-4 py-3 border text-center">Total Subs</th>
                    <th className="px-4 py-3 border text-center">Graded</th>
                    <th className="px-4 py-3 border text-center">Pending</th>
                    <th className="px-4 py-3 border text-center">Avg Score</th>
                    <th className="px-4 py-3 border text-center">Late</th>
                    <th className="px-4 py-3 border text-center">Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredStudents.map((student) => (
                    <tr key={student.user_id} className="hover:bg-gray-50">
                      <td className="px-4 py-3 border">
                        <div className="font-semibold">{student.full_name}</div>
                        <div className="text-xs text-gray-500">
                          ID: {student.user_id.substring(0, 8)}...
                        </div>
                      </td>
                      <td className="px-4 py-3 border">
                        <div className="text-xs">{student.email}</div>
                        <div className="text-xs">
                          <span
                            className={`px-2 py-1 rounded ${
                              student.account_status === 'ACTIVE'
                                ? 'bg-green-100 text-green-700'
                                : 'bg-gray-100 text-gray-700'
                            }`}
                          >
                            {student.account_status}
                          </span>
                        </div>
                      </td>
                      <td className="px-4 py-3 border text-center">
                        <span className="font-semibold text-blue-600">
                          {student.enrolled_courses}
                        </span>
                      </td>
                      <td className="px-4 py-3 border text-center">
                        <span className="font-bold">
                          {student.total_submissions}
                        </span>
                      </td>
                      <td className="px-4 py-3 border text-center">
                        <span className="text-green-600 font-semibold">
                          {student.graded_count}
                        </span>
                      </td>
                      <td className="px-4 py-3 border text-center">
                        <span className="text-yellow-600 font-semibold">
                          {student.pending_count}
                        </span>
                      </td>
                      <td className="px-4 py-3 border text-center">
                        {student.avg_score !== null ? (
                          <span className="font-semibold text-purple-600">
                            {Number(student.avg_score).toFixed(2)}
                          </span>
                        ) : (
                          <span className="text-gray-400">-</span>
                        )}
                      </td>
                      <td className="px-4 py-3 border text-center">
                        {student.late_count > 0 ? (
                          <span className="text-red-600 font-semibold">
                            {student.late_count}
                          </span>
                        ) : (
                          <span className="text-gray-400">0</span>
                        )}
                      </td>
                      <td className="px-4 py-3 border text-center">
                        <Link
                          href={`/submissions?student_email=${encodeURIComponent(
                            student.email
                          )}`}
                          className="inline-block bg-blue-600 text-white px-3 py-1 rounded text-xs hover:bg-blue-700"
                        >
                          View Submissions
                        </Link>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>

              {filteredStudents.length === 0 && (
                <div className="text-center py-8 text-gray-500">
                  Không tìm thấy students
                </div>
              )}
            </div>

            {/* Summary Stats */}
            {filteredStudents.length > 0 && (
              <div className="mt-6 grid grid-cols-4 gap-4">
                <div className="bg-blue-50 rounded-lg p-4">
                  <p className="text-sm text-gray-600">Total Students</p>
                  <p className="text-2xl font-bold text-blue-600">
                    {filteredStudents.length}
                  </p>
                </div>
                <div className="bg-green-50 rounded-lg p-4">
                  <p className="text-sm text-gray-600">Total Submissions</p>
                  <p className="text-2xl font-bold text-green-600">
                    {filteredStudents.reduce(
                      (sum, s) => sum + s.total_submissions,
                      0
                    )}
                  </p>
                </div>
                <div className="bg-purple-50 rounded-lg p-4">
                  <p className="text-sm text-gray-600">Avg Score (All)</p>
                  <p className="text-2xl font-bold text-purple-600">
                    {(
                      filteredStudents
                        .filter((s) => s.avg_score !== null)
                        .reduce((sum, s) => sum + (s.avg_score || 0), 0) /
                      filteredStudents.filter((s) => s.avg_score !== null)
                        .length
                    ).toFixed(2)}
                  </p>
                </div>
                <div className="bg-red-50 rounded-lg p-4">
                  <p className="text-sm text-gray-600">Late Submissions</p>
                  <p className="text-2xl font-bold text-red-600">
                    {filteredStudents.reduce((sum, s) => sum + s.late_count, 0)}
                  </p>
                </div>
              </div>
            )}
          </>
        )}
      </div>
    </div>
  );
}
