/**
 * Root Layout
 * ===========
 *
 * Layout chính cho toàn bộ app.
 */

import type { Metadata } from 'next';
import Link from 'next/link';
import './globals.css';

export const metadata: Metadata = {
  title: 'B-Learning Demo',
  description: 'Demo database B-Learning với Next.js và FastAPI',
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="vi">
      <body>
        <div className="min-h-screen bg-gray-50">
          {/* Header */}
          <header className="bg-primary-600 text-white shadow-lg">
            <div className="container mx-auto px-4 py-4">
              <div className="flex items-center justify-between">
                <h1 className="text-2xl font-bold">
                  <Link href="/">B-Learning Demo</Link>
                </h1>
                <nav className="space-x-6">
                  <Link
                    href="/courses"
                    className="hover:text-primary-200 transition"
                  >
                    Courses
                  </Link>
                  <Link
                    href="/assignments"
                    className="hover:text-primary-200 transition"
                  >
                    Assignments
                  </Link>
                  <Link
                    href="/students"
                    className="hover:text-primary-200 transition"
                  >
                    Students
                  </Link>
                  <Link
                    href="/submissions"
                    className="hover:text-primary-200 transition"
                  >
                    Submissions
                  </Link>
                </nav>
              </div>
            </div>
          </header>

          {/* Main Content */}
          <main className="container mx-auto px-4 py-8">{children}</main>

          {/* Footer */}
          <footer className="bg-gray-800 text-white mt-16">
            <div className="container mx-auto px-4 py-6 text-center">
              <p>B-Learning Demo - Nguyễn Văn Kiệt - CNTT1-K63</p>
              <p className="text-gray-400 text-sm mt-2">
                Backend: Python FastAPI | Frontend: Next.js + React + TypeScript
              </p>
            </div>
          </footer>
        </div>
      </body>
    </html>
  );
}
