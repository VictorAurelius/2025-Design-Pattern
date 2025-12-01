/**
 * Assignments List Page
 * =====================
 *
 * Hiển thị danh sách assignments để demo filter assignment_id
 * JOIN: Lecture, Module, Course, COUNT(AssignmentSubmission)
 *
 * Author: Nguyễn Văn Kiệt - CNTT1-K63
 */

'use client';

import { useState, useEffect } from 'react';
import apiClient from '@/lib/api';
import Link from 'next/link';

interface Assignment {
  lecture_id: string;
  title: string;
  description: string;
  course_code: string;
  course_title: string;
  course_id: string;
  module_title: string;
  max_points: number;
  due_date: string;
  total_submissions: number;
  graded_count: number;
  pending_count: number;
  grading_count: number;
}

export default function AssignmentsPage() {
  const [assignments, setAssignments] = useState<Assignment[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    fetchAssignments();
  }, []);

  const fetchAssignments = async () => {
    setLoading(true);
    setError(null);
    try {
      // Gọi API endpoint mới để lấy assignments
      const response = await apiClient.get('/api/assignments');
      setAssignments(response.data.assignments);
    } catch (err: any) {
      setError(err.response?.data?.detail || 'Lỗi khi tải assignments');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="max-w-7xl mx-auto">
      <div className="bg-white rounded-lg shadow-md p-6">
        {/* Header */}
        <h1 className="text-3xl font-bold text-gray-800 mb-2">
          📚 Assignments List
        </h1>
        <p className="text-gray-600 mb-6">
          Click assignment để filter Submissions theo assignment_id
        </p>

        {/* Loading / Error */}
        {loading && <p>Đang tải...</p>}
        {error && <p className="text-red-600">{error}</p>}

        {/* Assignments Table */}
        {!loading && !error && (
          <div className="overflow-x-auto">
            <table className="min-w-full border text-sm">
              <thead className="bg-gray-100">
                <tr>
                  <th className="px-4 py-3 border text-left">Assignment</th>
                  <th className="px-4 py-3 border text-left">Course</th>
                  <th className="px-4 py-3 border text-left">Module</th>
                  <th className="px-4 py-3 border text-center">Max Points</th>
                  <th className="px-4 py-3 border text-center">Total</th>
                  <th className="px-4 py-3 border text-center">Graded</th>
                  <th className="px-4 py-3 border text-center">Pending</th>
                  <th className="px-4 py-3 border text-center">Actions</th>
                </tr>
              </thead>
              <tbody>
                {assignments.map((assignment) => (
                  <tr key={assignment.lecture_id} className="hover:bg-gray-50">
                    <td className="px-4 py-3 border">
                      <div className="font-semibold">{assignment.title}</div>
                      {assignment.description && (
                        <div className="text-xs text-gray-500 mt-1">
                          {assignment.description.substring(0, 60)}...
                        </div>
                      )}
                    </td>
                    <td className="px-4 py-3 border">
                      <div className="font-mono text-xs font-semibold">
                        {assignment.course_code}
                      </div>
                      <div className="text-xs text-gray-600">
                        {assignment.course_title}
                      </div>
                    </td>
                    <td className="px-4 py-3 border">
                      <div className="text-xs">{assignment.module_title}</div>
                    </td>
                    <td className="px-4 py-3 border text-center">
                      <span className="font-semibold text-blue-600">
                        {assignment.max_points}
                      </span>
                    </td>
                    <td className="px-4 py-3 border text-center">
                      <span className="font-bold">{assignment.total_submissions}</span>
                    </td>
                    <td className="px-4 py-3 border text-center">
                      <span className="text-green-600 font-semibold">
                        {assignment.graded_count}
                      </span>
                    </td>
                    <td className="px-4 py-3 border text-center">
                      <span className="text-yellow-600 font-semibold">
                        {assignment.pending_count}
                      </span>
                    </td>
                    <td className="px-4 py-3 border text-center">
                      <Link
                        href={`/submissions?assignment_id=${assignment.lecture_id}`}
                        className="inline-block bg-blue-600 text-white px-3 py-1 rounded text-xs hover:bg-blue-700"
                      >
                        View Submissions
                      </Link>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>

            {assignments.length === 0 && (
              <div className="text-center py-8 text-gray-500">
                Không có assignments
              </div>
            )}
          </div>
        )}

        {/* Summary Stats */}
        {!loading && !error && assignments.length > 0 && (
          <div className="mt-6 grid grid-cols-3 gap-4">
            <div className="bg-blue-50 rounded-lg p-4">
              <p className="text-sm text-gray-600">Total Assignments</p>
              <p className="text-2xl font-bold text-blue-600">
                {assignments.length}
              </p>
            </div>
            <div className="bg-green-50 rounded-lg p-4">
              <p className="text-sm text-gray-600">Total Submissions</p>
              <p className="text-2xl font-bold text-green-600">
                {assignments.reduce((sum, a) => sum + a.total_submissions, 0)}
              </p>
            </div>
            <div className="bg-yellow-50 rounded-lg p-4">
              <p className="text-sm text-gray-600">Pending Grading</p>
              <p className="text-2xl font-bold text-yellow-600">
                {assignments.reduce((sum, a) => sum + a.pending_count, 0)}
              </p>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}
