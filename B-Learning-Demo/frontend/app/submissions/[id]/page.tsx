/**
 * Submission Detail Page
 * ======================
 *
 * Hiển thị chi tiết 1 submission với FULL DATA từ nhiều bảng.
 * Mục đích: Chứng minh tất cả JOINs hoạt động đúng.
 *
 * JOIN Tables:
 * - AssignmentSubmission (main)
 * - User (student)
 * - User (grader)
 * - Lecture (Assignment)
 * - Module
 * - Course
 * - Enrollment
 *
 * Author: Nguyễn Văn Kiệt - CNTT1-K63
 */

'use client';

import { useState, useEffect } from 'react';
import { useParams, useRouter } from 'next/navigation';
import apiClient from '@/lib/api';
import Link from 'next/link';

interface SubmissionDetail {
  // AssignmentSubmission table
  assignment_submission_id: string;
  submission_number: number;
  submitted_at: string;
  content: string;
  file_urls: string[] | null;
  code_submission: any | null;
  is_late: boolean;
  days_late: number;
  status: string;
  auto_score: number;
  manual_score: number;
  final_score: number;
  penalty_applied: number;
  feedback: string | null;
  graded_at: string | null;

  // User table (student)
  student_id: string;
  student_email: string;
  student_name: string;

  // User table (grader)
  graded_by_name: string | null;

  // Lecture table (Assignment)
  assignment_id: string;
  assignment_title: string;
  assignment_description: string;
  assignment_instructions: string;
  assignment_type: string;
  max_points: number;
  due_date: string;
  late_submission_allowed: boolean;
  late_penalty_percent: number;

  // Course table
  course_id: string;
  course_code: string;
  course_title: string;
  course_description: string;
}

export default function SubmissionDetailPage() {
  const params = useParams();
  const router = useRouter();
  const submissionId = params.id as string;

  const [submission, setSubmission] = useState<SubmissionDetail | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (submissionId) {
      fetchSubmissionDetail();
    }
  }, [submissionId]);

  const fetchSubmissionDetail = async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await apiClient.get(`/api/submissions/${submissionId}`);
      setSubmission(response.data);
    } catch (err: any) {
      setError(
        err.response?.data?.detail || 'Lỗi khi tải submission detail'
      );
    } finally {
      setLoading(false);
    }
  };

  // Loading state
  if (loading) {
    return (
      <div className="max-w-7xl mx-auto">
        <div className="bg-white rounded-lg shadow-md p-6">
          <p className="text-center py-8">Đang tải...</p>
        </div>
      </div>
    );
  }

  // Error state
  if (error) {
    return (
      <div className="max-w-7xl mx-auto">
        <div className="bg-white rounded-lg shadow-md p-6">
          <p className="text-red-600 text-center py-8">{error}</p>
          <div className="text-center mt-4">
            <Link
              href="/submissions"
              className="inline-block bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
            >
              ← Quay lại danh sách
            </Link>
          </div>
        </div>
      </div>
    );
  }

  // No data state
  if (!submission) {
    return null;
  }

  return (
    <div className="max-w-7xl mx-auto">
      <div className="bg-white rounded-lg shadow-md p-6">
        {/* Header */}
        <div className="mb-6">
          <Link
            href="/submissions"
            className="inline-block text-blue-600 hover:text-blue-800 mb-4"
          >
            ← Quay lại danh sách
          </Link>
          <h1 className="text-3xl font-bold text-gray-800">
            📝 Submission Detail
          </h1>
          <p className="text-gray-600 mt-2">
            Hiển thị FULL DATA từ các bảng: AssignmentSubmission, User,
            Lecture, Course, Enrollment
          </p>
        </div>

        {/* Status Badge */}
        <div className="mb-6">
          <span
            className={`inline-block px-4 py-2 rounded-lg font-semibold text-sm ${
              submission.status === 'GRADED'
                ? 'bg-green-100 text-green-700'
                : submission.status === 'SUBMITTED'
                ? 'bg-yellow-100 text-yellow-700'
                : submission.status === 'GRADING'
                ? 'bg-blue-100 text-blue-700'
                : 'bg-gray-100 text-gray-700'
            }`}
          >
            {submission.status}
          </span>
          {submission.is_late && (
            <span className="ml-2 inline-block px-4 py-2 rounded-lg font-semibold text-sm bg-red-100 text-red-700">
              ⚠ LATE ({submission.days_late} day{submission.days_late !== 1 ? 's' : ''})
            </span>
          )}
        </div>

        {/* Main Content Grid */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          {/* LEFT COLUMN */}
          <div className="space-y-6">
            {/* 1. AssignmentSubmission Table */}
            <div className="border-2 border-purple-200 rounded-lg p-4 bg-purple-50">
              <h2 className="text-lg font-bold text-purple-800 mb-3">
                📊 AssignmentSubmission Table
              </h2>
              <div className="space-y-2 text-sm">
                <div className="grid grid-cols-2 gap-2">
                  <span className="text-gray-600">submission_id:</span>
                  <span className="font-mono text-xs">
                    {submission.assignment_submission_id.substring(0, 20)}...
                  </span>
                </div>
                <div className="grid grid-cols-2 gap-2">
                  <span className="text-gray-600">submission_number:</span>
                  <span className="font-semibold">
                    #{submission.submission_number}
                  </span>
                </div>
                <div className="grid grid-cols-2 gap-2">
                  <span className="text-gray-600">submitted_at:</span>
                  <span className="font-mono text-xs">
                    {new Date(submission.submitted_at).toLocaleString('vi-VN')}
                  </span>
                </div>
                <div className="grid grid-cols-2 gap-2">
                  <span className="text-gray-600">status:</span>
                  <span className="font-semibold">{submission.status}</span>
                </div>
                <div className="grid grid-cols-2 gap-2">
                  <span className="text-gray-600">is_late:</span>
                  <span className="font-semibold">
                    {submission.is_late ? '✅ TRUE' : '❌ FALSE'}
                  </span>
                </div>
                <div className="grid grid-cols-2 gap-2">
                  <span className="text-gray-600">auto_score:</span>
                  <span className="font-semibold">{submission.auto_score}</span>
                </div>
                <div className="grid grid-cols-2 gap-2">
                  <span className="text-gray-600">manual_score:</span>
                  <span className="font-semibold">
                    {submission.manual_score}
                  </span>
                </div>
                <div className="grid grid-cols-2 gap-2">
                  <span className="text-gray-600">final_score:</span>
                  <span className="font-bold text-lg text-purple-700">
                    {submission.final_score} / {submission.max_points}
                  </span>
                </div>
                <div className="grid grid-cols-2 gap-2">
                  <span className="text-gray-600">penalty_applied:</span>
                  <span className="font-semibold">
                    {submission.penalty_applied}
                  </span>
                </div>
                {submission.graded_at && (
                  <div className="grid grid-cols-2 gap-2">
                    <span className="text-gray-600">graded_at:</span>
                    <span className="font-mono text-xs">
                      {new Date(submission.graded_at).toLocaleString('vi-VN')}
                    </span>
                  </div>
                )}
              </div>
            </div>

            {/* 2. User Table (Student) */}
            <div className="border-2 border-blue-200 rounded-lg p-4 bg-blue-50">
              <h2 className="text-lg font-bold text-blue-800 mb-3">
                👨‍🎓 User Table (Student)
              </h2>
              <div className="space-y-2 text-sm">
                <div className="grid grid-cols-2 gap-2">
                  <span className="text-gray-600">user_id:</span>
                  <span className="font-mono text-xs">
                    {submission.student_id.substring(0, 20)}...
                  </span>
                </div>
                <div className="grid grid-cols-2 gap-2">
                  <span className="text-gray-600">email:</span>
                  <span className="font-semibold">
                    {submission.student_email}
                  </span>
                </div>
                <div className="grid grid-cols-2 gap-2">
                  <span className="text-gray-600">full_name:</span>
                  <span className="font-semibold text-blue-700">
                    {submission.student_name}
                  </span>
                </div>
              </div>
            </div>

            {/* 3. User Table (Grader) */}
            <div className="border-2 border-green-200 rounded-lg p-4 bg-green-50">
              <h2 className="text-lg font-bold text-green-800 mb-3">
                👨‍🏫 User Table (Grader)
              </h2>
              <div className="space-y-2 text-sm">
                <div className="grid grid-cols-2 gap-2">
                  <span className="text-gray-600">graded_by_name:</span>
                  <span className="font-semibold text-green-700">
                    {submission.graded_by_name || '(Chưa chấm)'}
                  </span>
                </div>
              </div>

              {submission.feedback && (
                <div className="mt-4 pt-4 border-t border-green-300">
                  <h3 className="font-semibold text-green-800 mb-2">
                    Feedback:
                  </h3>
                  <p className="text-sm text-gray-700 bg-white p-3 rounded">
                    {submission.feedback}
                  </p>
                </div>
              )}
            </div>
          </div>

          {/* RIGHT COLUMN */}
          <div className="space-y-6">
            {/* 4. Lecture Table (Assignment) */}
            <div className="border-2 border-orange-200 rounded-lg p-4 bg-orange-50">
              <h2 className="text-lg font-bold text-orange-800 mb-3">
                📚 Lecture Table (Assignment)
              </h2>
              <div className="space-y-2 text-sm">
                <div className="grid grid-cols-2 gap-2">
                  <span className="text-gray-600">lecture_id:</span>
                  <span className="font-mono text-xs">
                    {submission.assignment_id.substring(0, 20)}...
                  </span>
                </div>
                <div className="grid grid-cols-2 gap-2">
                  <span className="text-gray-600">title:</span>
                  <span className="font-semibold text-orange-700">
                    {submission.assignment_title}
                  </span>
                </div>
                <div className="grid grid-cols-2 gap-2">
                  <span className="text-gray-600">type:</span>
                  <span className="font-semibold">
                    {submission.assignment_type}
                  </span>
                </div>
                <div className="grid grid-cols-2 gap-2">
                  <span className="text-gray-600">max_points:</span>
                  <span className="font-bold text-orange-700">
                    {submission.max_points}
                  </span>
                </div>
                <div className="grid grid-cols-2 gap-2">
                  <span className="text-gray-600">due_date:</span>
                  <span className="font-mono text-xs">
                    {new Date(submission.due_date).toLocaleString('vi-VN')}
                  </span>
                </div>
                <div className="grid grid-cols-2 gap-2">
                  <span className="text-gray-600">late_submission:</span>
                  <span className="font-semibold">
                    {submission.late_submission_allowed ? '✅ Allowed' : '❌ Not Allowed'}
                  </span>
                </div>
                <div className="grid grid-cols-2 gap-2">
                  <span className="text-gray-600">late_penalty:</span>
                  <span className="font-semibold">
                    {submission.late_penalty_percent}%
                  </span>
                </div>
              </div>

              {submission.assignment_description && (
                <div className="mt-4 pt-4 border-t border-orange-300">
                  <h3 className="font-semibold text-orange-800 mb-2">
                    Description:
                  </h3>
                  <p className="text-sm text-gray-700 bg-white p-3 rounded">
                    {submission.assignment_description}
                  </p>
                </div>
              )}
            </div>

            {/* 5. Course Table */}
            <div className="border-2 border-red-200 rounded-lg p-4 bg-red-50">
              <h2 className="text-lg font-bold text-red-800 mb-3">
                🎓 Course Table
              </h2>
              <div className="space-y-2 text-sm">
                <div className="grid grid-cols-2 gap-2">
                  <span className="text-gray-600">course_id:</span>
                  <span className="font-mono text-xs">
                    {submission.course_id.substring(0, 20)}...
                  </span>
                </div>
                <div className="grid grid-cols-2 gap-2">
                  <span className="text-gray-600">code:</span>
                  <span className="font-mono font-bold text-red-700">
                    {submission.course_code}
                  </span>
                </div>
                <div className="grid grid-cols-2 gap-2">
                  <span className="text-gray-600">title:</span>
                  <span className="font-semibold text-red-700">
                    {submission.course_title}
                  </span>
                </div>
              </div>

              {submission.course_description && (
                <div className="mt-4 pt-4 border-t border-red-300">
                  <h3 className="font-semibold text-red-800 mb-2">
                    Description:
                  </h3>
                  <p className="text-sm text-gray-700 bg-white p-3 rounded">
                    {submission.course_description}
                  </p>
                </div>
              )}
            </div>

            {/* 6. Submission Content */}
            <div className="border-2 border-gray-200 rounded-lg p-4 bg-gray-50">
              <h2 className="text-lg font-bold text-gray-800 mb-3">
                📄 Submission Content
              </h2>
              <div className="bg-white p-4 rounded text-sm">
                <pre className="whitespace-pre-wrap text-gray-700">
                  {submission.content}
                </pre>
              </div>

              {submission.file_urls && submission.file_urls.length > 0 && (
                <div className="mt-4">
                  <h3 className="font-semibold text-gray-800 mb-2">
                    Attached Files:
                  </h3>
                  <ul className="list-disc list-inside text-sm">
                    {submission.file_urls.map((url, index) => (
                      <li key={index}>
                        <a
                          href={url}
                          target="_blank"
                          rel="noopener noreferrer"
                          className="text-blue-600 hover:underline"
                        >
                          {url}
                        </a>
                      </li>
                    ))}
                  </ul>
                </div>
              )}

              {submission.code_submission && (
                <div className="mt-4">
                  <h3 className="font-semibold text-gray-800 mb-2">
                    Code Submission:
                  </h3>
                  <pre className="bg-gray-900 text-green-400 p-4 rounded text-xs overflow-x-auto">
                    {JSON.stringify(submission.code_submission, null, 2)}
                  </pre>
                </div>
              )}
            </div>
          </div>
        </div>

        {/* Summary Stats */}
        <div className="mt-8 grid grid-cols-4 gap-4">
          <div className="bg-purple-50 rounded-lg p-4 border-2 border-purple-200">
            <p className="text-sm text-gray-600">Score Percentage</p>
            <p className="text-2xl font-bold text-purple-600">
              {((submission.final_score / submission.max_points) * 100).toFixed(
                1
              )}
              %
            </p>
          </div>
          <div className="bg-blue-50 rounded-lg p-4 border-2 border-blue-200">
            <p className="text-sm text-gray-600">Submission #</p>
            <p className="text-2xl font-bold text-blue-600">
              #{submission.submission_number}
            </p>
          </div>
          <div className="bg-orange-50 rounded-lg p-4 border-2 border-orange-200">
            <p className="text-sm text-gray-600">Max Points</p>
            <p className="text-2xl font-bold text-orange-600">
              {submission.max_points}
            </p>
          </div>
          <div
            className={`rounded-lg p-4 border-2 ${
              submission.is_late
                ? 'bg-red-50 border-red-200'
                : 'bg-green-50 border-green-200'
            }`}
          >
            <p className="text-sm text-gray-600">Timeliness</p>
            <p
              className={`text-2xl font-bold ${
                submission.is_late ? 'text-red-600' : 'text-green-600'
              }`}
            >
              {submission.is_late ? 'LATE' : 'ON TIME'}
            </p>
          </div>
        </div>

        {/* Actions */}
        <div className="mt-8 flex justify-between items-center">
          <Link
            href="/submissions"
            className="inline-block bg-gray-600 text-white px-6 py-3 rounded-lg hover:bg-gray-700 font-semibold"
          >
            ← Quay lại danh sách
          </Link>

          <div className="space-x-2">
            <Link
              href={`/assignments`}
              className="inline-block bg-orange-600 text-white px-6 py-3 rounded-lg hover:bg-orange-700 font-semibold"
            >
              View Assignment
            </Link>
            <Link
              href={`/students`}
              className="inline-block bg-blue-600 text-white px-6 py-3 rounded-lg hover:bg-blue-700 font-semibold"
            >
              View Student
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
}
