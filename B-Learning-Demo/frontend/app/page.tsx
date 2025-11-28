/**
 * Home Page
 * ==========
 */

import Link from 'next/link';

export default function Home() {
  return (
    <div className="max-w-4xl mx-auto">
      <div className="bg-white rounded-lg shadow-md p-8">
        <h1 className="text-4xl font-bold text-gray-800 mb-4">
          Chào mừng đến B-Learning Demo
        </h1>
        <p className="text-lg text-gray-600 mb-8">
          Ứng dụng demo database B-Learning với 2 giao diện web:
        </p>

        <div className="grid md:grid-cols-2 gap-6 mb-8">
          {/* Card 1: Courses */}
          <Link href="/courses">
            <div className="border-2 border-primary-300 rounded-lg p-6 hover:shadow-lg transition cursor-pointer">
              <h2 className="text-2xl font-bold text-primary-600 mb-3">
                📚 Course Management
              </h2>
              <p className="text-gray-700 mb-4">
                Quản lý khóa học (1 bảng)
              </p>
              <ul className="text-sm text-gray-600 space-y-1">
                <li>✅ CRUD operations</li>
                <li>✅ Tìm kiếm và lọc</li>
                <li>✅ Pagination</li>
                <li>✅ SQL queries trực tiếp</li>
              </ul>
            </div>
          </Link>

          {/* Card 2: Submissions */}
          <Link href="/submissions">
            <div className="border-2 border-green-300 rounded-lg p-6 hover:shadow-lg transition cursor-pointer">
              <h2 className="text-2xl font-bold text-green-600 mb-3">
                📝 Submission Management
              </h2>
              <p className="text-gray-700 mb-4">
                Quản lý bài nộp (nhiều bảng với JOIN)
              </p>
              <ul className="text-sm text-gray-600 space-y-1">
                <li>✅ JOIN 5+ bảng</li>
                <li>✅ Chấm điểm</li>
                <li>✅ Thống kê</li>
                <li>✅ Filter nâng cao</li>
              </ul>
            </div>
          </Link>
        </div>

        {/* Tech Stack */}
        <div className="bg-gray-50 rounded-lg p-6">
          <h3 className="text-xl font-bold text-gray-800 mb-3">
            🛠️ Tech Stack
          </h3>
          <div className="grid md:grid-cols-2 gap-4">
            <div>
              <h4 className="font-semibold text-gray-700">Backend:</h4>
              <ul className="text-sm text-gray-600">
                <li>• Python 3.11+ / FastAPI</li>
                <li>• PostgreSQL 14+</li>
                <li>• psycopg2 (raw SQL)</li>
                <li>• RESTful API</li>
              </ul>
            </div>
            <div>
              <h4 className="font-semibold text-gray-700">Frontend:</h4>
              <ul className="text-sm text-gray-600">
                <li>• Next.js 14 / React 18</li>
                <li>• TypeScript</li>
                <li>• Tailwind CSS</li>
                <li>• Axios</li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
