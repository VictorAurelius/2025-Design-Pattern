/**
 * Student Submit Assignment Page
 * ===============================
 *
 * Trang để student submit assignment.
 * - Nhập student ID, assignment ID
 * - Nhập content
 * - Submit assignment
 *
 * Author: Nguyễn Văn Kiệt - CNTT1-K63
 */

'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import apiClient from '@/lib/api';
import Link from 'next/link';

interface SubmitForm {
  user_id: string;
  lecture_id: string;
  content: string;
  file_urls: string[];
}

export default function StudentSubmitPage() {
  const router = useRouter();

  const [submitForm, setSubmitForm] = useState<SubmitForm>({
    user_id: '',
    lecture_id: '',
    content: '',
    file_urls: [],
  });

  const [fileUrlInput, setFileUrlInput] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState(false);
  const [submissionId, setSubmissionId] = useState<string | null>(null);

  const handleAddFileUrl = () => {
    if (fileUrlInput.trim()) {
      setSubmitForm({
        ...submitForm,
        file_urls: [...submitForm.file_urls, fileUrlInput.trim()],
      });
      setFileUrlInput('');
    }
  };

  const handleRemoveFileUrl = (index: number) => {
    setSubmitForm({
      ...submitForm,
      file_urls: submitForm.file_urls.filter((_, i) => i !== index),
    });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    setSuccess(false);

    try {
      const response = await apiClient.post('/api/submissions', {
        user_id: submitForm.user_id,
        lecture_id: submitForm.lecture_id,
        content: submitForm.content,
        file_urls: submitForm.file_urls.length > 0 ? submitForm.file_urls : null,
      });

      setSuccess(true);
      setSubmissionId(response.data.assignment_submission_id);
      alert('Submit thành công! 🎉');

      // Reset form
      setSubmitForm({
        user_id: '',
        lecture_id: '',
        content: '',
        file_urls: [],
      });
    } catch (err: any) {
      setError(err.response?.data?.detail || 'Lỗi khi submit assignment');
      alert(err.response?.data?.detail || 'Lỗi khi submit assignment');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="max-w-4xl mx-auto">
      <div className="bg-white rounded-lg shadow-md p-6">
        {/* Header */}
        <h1 className="text-3xl font-bold text-gray-800 mb-2">
          📝 Submit Assignment
        </h1>
        <p className="text-gray-600 mb-6">
          Trang dành cho students submit assignment vào hệ thống
        </p>

        {/* Success Message */}
        {success && submissionId && (
          <div className="mb-6 bg-green-50 border-2 border-green-300 rounded-lg p-4">
            <h3 className="text-lg font-bold text-green-800 mb-2">
              ✅ Submit thành công!
            </h3>
            <p className="text-green-700 mb-3">
              Submission ID:{' '}
              <span className="font-mono text-sm">{submissionId}</span>
            </p>
            <div className="flex gap-2">
              <Link
                href={`/submissions/${submissionId}`}
                className="inline-block bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700"
              >
                View Submission Detail
              </Link>
              <Link
                href="/submissions"
                className="inline-block bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
              >
                View All Submissions
              </Link>
            </div>
          </div>
        )}

        {/* Error Message */}
        {error && (
          <div className="mb-6 bg-red-50 border-2 border-red-300 rounded-lg p-4">
            <h3 className="text-lg font-bold text-red-800 mb-2">❌ Lỗi</h3>
            <p className="text-red-700">{error}</p>
          </div>
        )}

        {/* Submit Form */}
        <form onSubmit={handleSubmit} className="space-y-6">
          {/* Student ID */}
          <div>
            <label className="block text-sm font-semibold mb-2">
              Student ID * (UUID)
            </label>
            <input
              type="text"
              required
              value={submitForm.user_id}
              onChange={(e) =>
                setSubmitForm({ ...submitForm, user_id: e.target.value })
              }
              className="w-full border-2 border-gray-300 px-4 py-3 rounded-lg font-mono text-sm focus:border-blue-500 focus:outline-none"
              placeholder="20000000-0000-0000-0000-000000000101"
            />
            <p className="text-xs text-gray-500 mt-1">
              💡 Tip: Lấy từ{' '}
              <Link href="/students" className="text-blue-600 hover:underline">
                Students page
              </Link>
            </p>
          </div>

          {/* Assignment ID */}
          <div>
            <label className="block text-sm font-semibold mb-2">
              Assignment ID * (UUID)
            </label>
            <input
              type="text"
              required
              value={submitForm.lecture_id}
              onChange={(e) =>
                setSubmitForm({ ...submitForm, lecture_id: e.target.value })
              }
              className="w-full border-2 border-gray-300 px-4 py-3 rounded-lg font-mono text-sm focus:border-blue-500 focus:outline-none"
              placeholder="60000000-0000-0000-0000-000000000002"
            />
            <p className="text-xs text-gray-500 mt-1">
              💡 Tip: Lấy từ{' '}
              <Link
                href="/assignments"
                className="text-blue-600 hover:underline"
              >
                Assignments page
              </Link>
            </p>
          </div>

          {/* Content */}
          <div>
            <label className="block text-sm font-semibold mb-2">
              Submission Content * (Answer/Solution)
            </label>
            <textarea
              required
              value={submitForm.content}
              onChange={(e) =>
                setSubmitForm({ ...submitForm, content: e.target.value })
              }
              className="w-full border-2 border-gray-300 px-4 py-3 rounded-lg focus:border-blue-500 focus:outline-none"
              rows={10}
              placeholder="Nhập câu trả lời, code, hoặc solution của bạn ở đây...

Ví dụ:
public class HelloWorld {
    public static void main(String[] args) {
        System.out.println(\"Hello, World!\");
    }
}
"
            />
            <p className="text-xs text-gray-500 mt-1">
              Nhập câu trả lời, code, hoặc giải pháp của bạn cho assignment này
            </p>
          </div>

          {/* File URLs (Optional) */}
          <div>
            <label className="block text-sm font-semibold mb-2">
              Attached Files (Optional)
            </label>
            <div className="flex gap-2 mb-2">
              <input
                type="url"
                value={fileUrlInput}
                onChange={(e) => setFileUrlInput(e.target.value)}
                className="flex-1 border-2 border-gray-300 px-4 py-3 rounded-lg text-sm focus:border-blue-500 focus:outline-none"
                placeholder="https://example.com/file.pdf"
              />
              <button
                type="button"
                onClick={handleAddFileUrl}
                className="bg-gray-600 text-white px-6 py-3 rounded-lg hover:bg-gray-700"
              >
                + Add URL
              </button>
            </div>

            {/* File URLs List */}
            {submitForm.file_urls.length > 0 && (
              <div className="border-2 border-gray-200 rounded-lg p-3 bg-gray-50">
                <p className="text-xs font-semibold text-gray-600 mb-2">
                  Attached Files ({submitForm.file_urls.length}):
                </p>
                <ul className="space-y-2">
                  {submitForm.file_urls.map((url, index) => (
                    <li
                      key={index}
                      className="flex items-center justify-between bg-white p-2 rounded border"
                    >
                      <a
                        href={url}
                        target="_blank"
                        rel="noopener noreferrer"
                        className="text-blue-600 hover:underline text-sm truncate flex-1"
                      >
                        {url}
                      </a>
                      <button
                        type="button"
                        onClick={() => handleRemoveFileUrl(index)}
                        className="ml-2 text-red-600 hover:text-red-800 text-sm"
                      >
                        Remove
                      </button>
                    </li>
                  ))}
                </ul>
              </div>
            )}
          </div>

          {/* Submit Button */}
          <div className="pt-4 flex gap-4">
            <button
              type="submit"
              disabled={loading}
              className="flex-1 bg-blue-600 text-white px-8 py-4 rounded-lg hover:bg-blue-700 disabled:opacity-50 disabled:cursor-not-allowed font-semibold text-lg"
            >
              {loading ? 'Đang submit...' : '📤 Submit Assignment'}
            </button>
            <Link
              href="/assignments"
              className="bg-gray-600 text-white px-8 py-4 rounded-lg hover:bg-gray-700 font-semibold text-lg flex items-center justify-center"
            >
              Hủy
            </Link>
          </div>
        </form>

        {/* Instructions */}
        <div className="mt-8 border-t-2 pt-6">
          <h3 className="text-lg font-bold text-gray-800 mb-3">
            📋 Hướng dẫn sử dụng
          </h3>
          <ol className="list-decimal list-inside space-y-2 text-sm text-gray-700">
            <li>
              <strong>Lấy Student ID:</strong> Vào{' '}
              <Link href="/students" className="text-blue-600 hover:underline">
                Students page
              </Link>{' '}
              để lấy UUID của student
            </li>
            <li>
              <strong>Lấy Assignment ID:</strong> Vào{' '}
              <Link
                href="/assignments"
                className="text-blue-600 hover:underline"
              >
                Assignments page
              </Link>{' '}
              để lấy UUID của assignment cần submit
            </li>
            <li>
              <strong>Nhập Content:</strong> Gõ câu trả lời, code, hoặc solution
              của bạn vào ô Content
            </li>
            <li>
              <strong>Thêm Files (Optional):</strong> Nếu có file đính kèm, nhập
              URL của file và click "Add URL"
            </li>
            <li>
              <strong>Submit:</strong> Click "Submit Assignment" để nộp bài
            </li>
          </ol>

          <div className="mt-4 bg-yellow-50 border-2 border-yellow-300 rounded-lg p-4">
            <h4 className="font-semibold text-yellow-800 mb-2">
              ⚠️ Lưu ý quan trọng:
            </h4>
            <ul className="list-disc list-inside space-y-1 text-sm text-yellow-800">
              <li>Student phải đã enroll vào course chứa assignment này</li>
              <li>Assignment phải có type = "ASSIGNMENT" (không phải VIDEO hay READING)</li>
              <li>Có thể submit nhiều lần cho cùng 1 assignment (submission_number sẽ tăng dần)</li>
              <li>Instructor có thể grade submission sau khi student submit</li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  );
}
