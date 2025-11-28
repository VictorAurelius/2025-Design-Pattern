# Frontend - B-Learning Demo

Next.js + React + TypeScript frontend.

## 📁 Cấu trúc

```
frontend/
├── app/
│   ├── layout.tsx           # Root layout
│   ├── page.tsx             # Home page
│   ├── globals.css          # Global styles
│   ├── courses/             # Page 1: Course Management
│   │   └── page.tsx
│   └── submissions/         # Page 2: Submission Management
│       └── page.tsx
├── lib/
│   └── api.ts               # Axios client
├── types/
│   ├── course.ts            # Course types
│   └── submission.ts        # Submission types
├── components/              # Reusable components (future)
├── package.json
├── tsconfig.json
├── tailwind.config.js
└── .env.local
```

## 🚀 Quick Start

```bash
# 1. Cài dependencies
npm install

# 2. Tạo .env.local
echo "NEXT_PUBLIC_API_URL=http://localhost:8000" > .env.local

# 3. Chạy dev server
npm run dev
```

App sẽ chạy tại: http://localhost:3000

## 🎨 Pages

### 1. Home Page (`/`)
- Giới thiệu 2 pages
- Tech stack overview
- Navigation

### 2. Courses (`/courses`)
- CRUD operations
- Search và filters
- Pagination
- Modal form

### 3. Submissions (`/submissions`)
- List với thông tin từ nhiều bảng
- Grade submission modal
- Statistics cards
- Filters

## 💡 Code Examples

### API Call
```typescript
import apiClient from '@/lib/api';
import type { CourseListResponse } from '@/types/course';

const response = await apiClient.get<CourseListResponse>('/api/courses', {
  params: { status: 'PUBLISHED', limit: 10 }
});
const courses = response.data.courses;
```

### State Management
```typescript
const [courses, setCourses] = useState<Course[]>([]);
const [loading, setLoading] = useState(false);

const fetchCourses = async () => {
  setLoading(true);
  try {
    const response = await apiClient.get<CourseListResponse>('/api/courses');
    setCourses(response.data.courses);
  } catch (error) {
    console.error(error);
  } finally {
    setLoading(false);
  }
};

useEffect(() => {
  fetchCourses();
}, []);
```

## 🎨 Styling

Sử dụng Tailwind CSS:

```tsx
<button className="bg-primary-600 text-white px-4 py-2 rounded hover:bg-primary-700">
  Click me
</button>
```

## 📦 Dependencies

```json
{
  "next": "^14.0.4",
  "react": "^18.2.0",
  "axios": "^1.6.5",
  "typescript": "^5.3.3",
  "tailwindcss": "^3.4.0"
}
```

## 🔧 Build for Production

```bash
# Build
npm run build

# Start production server
npm start
```
