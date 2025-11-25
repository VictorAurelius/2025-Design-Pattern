# API Endpoints Specification

**Project**: B-Learning System v2.0
**Author**: Nguyễn Văn Kiệt - CNTT1-K63
**Created**: 2025-11-25
**API Version**: v1
**Base URL**: `https://api.blearning.edu/v1`

---

## 1. OVERVIEW

### 1.1. API Design Principles
- **RESTful**: Resource-based URLs, HTTP verbs
- **JSON**: All requests and responses in JSON format
- **Stateless**: JWT-based authentication
- **Versioned**: API version in URL path (`/v1/`)
- **HATEOAS**: Links to related resources in responses
- **Idempotent**: PUT, DELETE are idempotent

### 1.2. Base URL
- **Production**: `https://api.blearning.edu/v1`
- **Staging**: `https://api-staging.blearning.edu/v1`
- **Development**: `http://localhost:3000/api/v1`

### 1.3. Authentication
All endpoints except public ones require JWT authentication.

**Header**:
```
Authorization: Bearer <jwt_token>
```

**JWT Payload**:
```json
{
  "user_id": "uuid",
  "email": "user@example.com",
  "roles": ["STUDENT", "INSTRUCTOR"],
  "iat": 1701234567,
  "exp": 1701320967
}
```

**Token Lifetime**: 24 hours

### 1.4. Common Response Format

**Success Response**:
```json
{
  "success": true,
  "data": { ... },
  "meta": {
    "timestamp": "2025-11-25T10:30:00Z",
    "request_id": "req_abc123"
  }
}
```

**Error Response**:
```json
{
  "success": false,
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "Invalid email format",
    "details": [
      {
        "field": "email",
        "message": "Must be a valid email address"
      }
    ]
  },
  "meta": {
    "timestamp": "2025-11-25T10:30:00Z",
    "request_id": "req_abc123"
  }
}
```

### 1.5. HTTP Status Codes

| Code | Meaning | Usage |
|------|---------|-------|
| 200 | OK | Successful GET, PUT, PATCH |
| 201 | Created | Successful POST |
| 204 | No Content | Successful DELETE |
| 400 | Bad Request | Validation error |
| 401 | Unauthorized | Missing or invalid token |
| 403 | Forbidden | No permission |
| 404 | Not Found | Resource not found |
| 409 | Conflict | Duplicate resource |
| 422 | Unprocessable Entity | Business logic error |
| 429 | Too Many Requests | Rate limit exceeded |
| 500 | Internal Server Error | Server error |
| 503 | Service Unavailable | Maintenance mode |

### 1.6. Pagination

**Query Parameters**:
- `page`: Page number (default: 1)
- `limit`: Items per page (default: 20, max: 100)
- `sort`: Sort field (e.g., `created_at`)
- `order`: Sort order (`asc` or `desc`)

**Response**:
```json
{
  "success": true,
  "data": [ ... ],
  "pagination": {
    "page": 1,
    "limit": 20,
    "total": 150,
    "pages": 8
  }
}
```

### 1.7. Filtering & Search

**Query Parameters**:
- `filter[field]`: Filter by field value
- `search`: Full-text search
- `status`: Filter by status

**Example**:
```
GET /api/v1/courses?filter[status]=PUBLISHED&search=javascript&sort=created_at&order=desc
```

---

## 2. AUTHENTICATION

### 2.1. Register

**Endpoint**: `POST /auth/register`
**Auth**: No
**Description**: Create new user account

**Request Body**:
```json
{
  "email": "student@example.com",
  "password": "SecureP@ss123",
  "first_name": "Jane",
  "last_name": "Smith",
  "phone": "+84901234567",
  "timezone": "Asia/Ho_Chi_Minh"
}
```

**Response** (201):
```json
{
  "success": true,
  "data": {
    "user_id": "550e8400-e29b-41d4-a716-446655440000",
    "email": "student@example.com",
    "first_name": "Jane",
    "last_name": "Smith",
    "account_status": "PENDING_VERIFICATION",
    "created_at": "2025-11-25T10:30:00Z"
  },
  "message": "Registration successful. Please check your email to verify your account."
}
```

**Errors**:
- `409`: Email already exists
- `400`: Password too weak

---

### 2.2. Verify Email

**Endpoint**: `POST /auth/verify-email`
**Auth**: No
**Description**: Verify email with token from email

**Request Body**:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Response** (200):
```json
{
  "success": true,
  "message": "Email verified successfully. You can now log in."
}
```

**Errors**:
- `400`: Invalid or expired token

---

### 2.3. Login

**Endpoint**: `POST /auth/login`
**Auth**: No
**Description**: Login with email and password

**Request Body**:
```json
{
  "email": "student@example.com",
  "password": "SecureP@ss123"
}
```

**Response** (200):
```json
{
  "success": true,
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "user": {
      "user_id": "550e8400-e29b-41d4-a716-446655440000",
      "email": "student@example.com",
      "first_name": "Jane",
      "last_name": "Smith",
      "avatar_url": "https://cdn.blearning.edu/avatars/user123.jpg",
      "roles": ["STUDENT"]
    }
  }
}
```

**Errors**:
- `401`: Invalid credentials
- `403`: Account not verified or suspended

---

### 2.4. Logout

**Endpoint**: `POST /auth/logout`
**Auth**: Required
**Description**: Logout and invalidate token

**Response** (200):
```json
{
  "success": true,
  "message": "Logged out successfully"
}
```

---

### 2.5. Refresh Token

**Endpoint**: `POST /auth/refresh`
**Auth**: Required
**Description**: Refresh JWT token

**Response** (200):
```json
{
  "success": true,
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
}
```

---

### 2.6. Request Password Reset

**Endpoint**: `POST /auth/forgot-password`
**Auth**: No
**Description**: Request password reset email

**Request Body**:
```json
{
  "email": "student@example.com"
}
```

**Response** (200):
```json
{
  "success": true,
  "message": "If this email exists, a password reset link has been sent."
}
```

---

### 2.7. Reset Password

**Endpoint**: `POST /auth/reset-password`
**Auth**: No
**Description**: Reset password with token

**Request Body**:
```json
{
  "token": "reset_token_here",
  "new_password": "NewSecureP@ss456"
}
```

**Response** (200):
```json
{
  "success": true,
  "message": "Password reset successfully. You can now log in."
}
```

**Errors**:
- `400`: Invalid or expired token
- `400`: Password too weak

---

## 3. USER MANAGEMENT

### 3.1. Get Current User Profile

**Endpoint**: `GET /users/me`
**Auth**: Required
**Description**: Get authenticated user's profile

**Response** (200):
```json
{
  "success": true,
  "data": {
    "user_id": "550e8400-e29b-41d4-a716-446655440000",
    "email": "student@example.com",
    "first_name": "Jane",
    "last_name": "Smith",
    "avatar_url": "https://cdn.blearning.edu/avatars/user123.jpg",
    "phone": "+84901234567",
    "timezone": "Asia/Ho_Chi_Minh",
    "locale": "vi",
    "account_status": "ACTIVE",
    "roles": ["STUDENT"],
    "created_at": "2025-11-25T10:30:00Z",
    "last_login_at": "2025-11-26T08:15:00Z"
  }
}
```

---

### 3.2. Update Profile

**Endpoint**: `PUT /users/me`
**Auth**: Required
**Description**: Update user profile

**Request Body**:
```json
{
  "first_name": "Jane",
  "last_name": "Smith",
  "phone": "+84901234567",
  "timezone": "Asia/Ho_Chi_Minh",
  "locale": "vi"
}
```

**Response** (200):
```json
{
  "success": true,
  "data": { ... }
}
```

---

### 3.3. Upload Avatar

**Endpoint**: `POST /users/me/avatar`
**Auth**: Required
**Description**: Upload user avatar
**Content-Type**: `multipart/form-data`

**Request**:
```
POST /users/me/avatar
Content-Type: multipart/form-data

avatar: <file>
```

**Response** (200):
```json
{
  "success": true,
  "data": {
    "avatar_url": "https://cdn.blearning.edu/avatars/user123.jpg"
  }
}
```

**Errors**:
- `400`: File too large (max 5MB)
- `400`: Invalid file type (only JPEG, PNG)

---

### 3.4. List Users (Admin)

**Endpoint**: `GET /users`
**Auth**: Required (Admin)
**Description**: List all users

**Query Params**:
- `page`, `limit` (pagination)
- `filter[account_status]`: Filter by status
- `filter[role]`: Filter by role
- `search`: Search by name or email
- `sort`, `order`: Sort results

**Response** (200):
```json
{
  "success": true,
  "data": [
    {
      "user_id": "...",
      "email": "...",
      "first_name": "...",
      "last_name": "...",
      "account_status": "ACTIVE",
      "roles": ["STUDENT"],
      "created_at": "..."
    }
  ],
  "pagination": { ... }
}
```

---

### 3.5. Assign Role (Admin)

**Endpoint**: `POST /users/{user_id}/roles`
**Auth**: Required (Admin)
**Description**: Assign role to user

**Request Body**:
```json
{
  "role_id": "role-uuid-here"
}
```

**Response** (200):
```json
{
  "success": true,
  "message": "Role assigned successfully"
}
```

---

### 3.6. Revoke Role (Admin)

**Endpoint**: `DELETE /users/{user_id}/roles/{role_id}`
**Auth**: Required (Admin)
**Description**: Revoke role from user

**Response** (200):
```json
{
  "success": true,
  "message": "Role revoked successfully"
}
```

---

## 4. COURSES

### 4.1. List Courses

**Endpoint**: `GET /courses`
**Auth**: Optional
**Description**: List all published courses (public) or all courses (instructor/admin)

**Query Params**:
- `page`, `limit`
- `filter[status]`: DRAFT, PUBLISHED, ARCHIVED
- `filter[category]`: Course category
- `filter[difficulty_level]`: BEGINNER, INTERMEDIATE, ADVANCED, EXPERT
- `search`: Full-text search
- `sort`, `order`

**Response** (200):
```json
{
  "success": true,
  "data": [
    {
      "course_id": "course-uuid",
      "code": "CS101",
      "title": "Introduction to Programming",
      "short_description": "Learn programming basics",
      "thumbnail_url": "https://cdn.blearning.edu/courses/cs101.jpg",
      "category": "Computer Science",
      "difficulty_level": "BEGINNER",
      "estimated_hours": 40,
      "status": "PUBLISHED",
      "published_at": "2025-01-15T10:00:00Z",
      "instructor": {
        "user_id": "...",
        "first_name": "John",
        "last_name": "Doe"
      },
      "stats": {
        "total_students": 1250,
        "total_modules": 8,
        "total_lectures": 45,
        "avg_rating": 4.8
      }
    }
  ],
  "pagination": { ... }
}
```

---

### 4.2. Get Course Detail

**Endpoint**: `GET /courses/{course_id}`
**Auth**: Optional
**Description**: Get course details

**Response** (200):
```json
{
  "success": true,
  "data": {
    "course_id": "course-uuid",
    "code": "CS101",
    "title": "Introduction to Programming",
    "description": "Full course description...",
    "short_description": "Learn programming basics",
    "thumbnail_url": "https://cdn.blearning.edu/courses/cs101.jpg",
    "category": "Computer Science",
    "difficulty_level": "BEGINNER",
    "estimated_hours": 40,
    "status": "PUBLISHED",
    "published_at": "2025-01-15T10:00:00Z",
    "created_by": {
      "user_id": "...",
      "first_name": "John",
      "last_name": "Doe",
      "avatar_url": "..."
    },
    "modules": [
      {
        "module_id": "module-uuid",
        "title": "Getting Started",
        "order_num": 1,
        "lecture_count": 5,
        "duration_minutes": 120
      }
    ],
    "stats": {
      "total_students": 1250,
      "total_modules": 8,
      "total_lectures": 45,
      "total_quizzes": 8,
      "total_assignments": 5,
      "avg_rating": 4.8
    }
  }
}
```

---

### 4.3. Create Course

**Endpoint**: `POST /courses`
**Auth**: Required (Instructor, Admin)
**Description**: Create new course

**Request Body**:
```json
{
  "code": "CS102",
  "title": "Advanced Programming",
  "description": "Full description...",
  "short_description": "Learn advanced concepts",
  "category": "Computer Science",
  "difficulty_level": "ADVANCED",
  "estimated_hours": 60
}
```

**Response** (201):
```json
{
  "success": true,
  "data": {
    "course_id": "new-course-uuid",
    "code": "CS102",
    "status": "DRAFT",
    "created_at": "2025-11-25T10:30:00Z"
  }
}
```

---

### 4.4. Update Course

**Endpoint**: `PUT /courses/{course_id}`
**Auth**: Required (Course Instructor, Admin)
**Description**: Update course details

**Request Body**: (same as Create Course)

**Response** (200):
```json
{
  "success": true,
  "data": { ... }
}
```

---

### 4.5. Publish Course

**Endpoint**: `POST /courses/{course_id}/publish`
**Auth**: Required (Course Instructor, Admin)
**Description**: Publish draft course

**Response** (200):
```json
{
  "success": true,
  "data": {
    "course_id": "...",
    "status": "PUBLISHED",
    "published_at": "2025-11-25T10:30:00Z"
  }
}
```

**Errors**:
- `422`: Course incomplete (missing modules or lectures)

---

### 4.6. Archive Course

**Endpoint**: `POST /courses/{course_id}/archive`
**Auth**: Required (Course Instructor, Admin)
**Description**: Archive course

**Response** (200):
```json
{
  "success": true,
  "data": {
    "course_id": "...",
    "status": "ARCHIVED"
  }
}
```

---

### 4.7. Delete Course

**Endpoint**: `DELETE /courses/{course_id}`
**Auth**: Required (Course Instructor, Admin)
**Description**: Delete course (soft delete)

**Response** (204): No content

**Errors**:
- `422`: Cannot delete published course with enrollments

---

## 5. MODULES & LECTURES

### 5.1. List Modules

**Endpoint**: `GET /courses/{course_id}/modules`
**Auth**: Optional (public for published courses)
**Description**: List course modules

**Response** (200):
```json
{
  "success": true,
  "data": [
    {
      "module_id": "module-uuid",
      "title": "Getting Started",
      "description": "Introduction to the course",
      "order_num": 1,
      "prerequisite_modules": [],
      "estimated_duration_minutes": 120,
      "lectures": [
        {
          "lecture_id": "lecture-uuid",
          "title": "Welcome",
          "type": "VIDEO",
          "duration_seconds": 300,
          "order_num": 1,
          "is_preview": true
        }
      ]
    }
  ]
}
```

---

### 5.2. Create Module

**Endpoint**: `POST /courses/{course_id}/modules`
**Auth**: Required (Course Instructor, Admin)
**Description**: Add module to course

**Request Body**:
```json
{
  "title": "Module 2: Variables and Data Types",
  "description": "Learn about variables...",
  "order_num": 2,
  "estimated_duration_minutes": 180
}
```

**Response** (201):
```json
{
  "success": true,
  "data": {
    "module_id": "new-module-uuid",
    "title": "Module 2: Variables and Data Types",
    "order_num": 2
  }
}
```

---

### 5.3. Get Lecture

**Endpoint**: `GET /lectures/{lecture_id}`
**Auth**: Required (must be enrolled or preview lecture)
**Description**: Get lecture details and content

**Response** (200):
```json
{
  "success": true,
  "data": {
    "lecture_id": "lecture-uuid",
    "module_id": "module-uuid",
    "title": "Introduction to Variables",
    "description": "Learn about variables in programming",
    "type": "VIDEO",
    "content_url": "https://streaming.blearning.edu/videos/cs101/m1/l1.m3u8",
    "duration_seconds": 600,
    "order_num": 1,
    "is_preview": false,
    "is_downloadable": true,
    "transcript": "Full transcript text...",
    "resources": [
      {
        "resource_id": "resource-uuid",
        "title": "Lecture Slides",
        "file_url": "https://cdn.blearning.edu/resources/...",
        "file_type": "application/pdf",
        "file_size_bytes": 2048000
      }
    ],
    "user_progress": {
      "status": "IN_PROGRESS",
      "percent_complete": 45.5,
      "last_position_seconds": 273
    }
  }
}
```

---

### 5.4. Create Lecture

**Endpoint**: `POST /modules/{module_id}/lectures`
**Auth**: Required (Course Instructor, Admin)
**Description**: Add lecture to module
**Content-Type**: `multipart/form-data` (if uploading file)

**Request**:
```json
{
  "title": "Variables in Python",
  "description": "Learn Python variables",
  "type": "VIDEO",
  "content_url": "https://youtube.com/watch?v=...",
  "duration_seconds": 600,
  "order_num": 2,
  "is_preview": false,
  "is_downloadable": true,
  "transcript": "Optional transcript..."
}
```

**Response** (201):
```json
{
  "success": true,
  "data": {
    "lecture_id": "new-lecture-uuid",
    "title": "Variables in Python",
    "content_url": "https://cdn.blearning.edu/videos/..."
  }
}
```

---

## 6. QUIZZES & QUESTIONS

### 6.1. List Quizzes

**Endpoint**: `GET /courses/{course_id}/quizzes`
**Auth**: Required (enrolled student)
**Description**: List course quizzes

**Response** (200):
```json
{
  "success": true,
  "data": [
    {
      "quiz_id": "quiz-uuid",
      "title": "Module 1 Quiz",
      "description": "Test your knowledge",
      "time_limit_minutes": 30,
      "attempt_limit": 3,
      "pass_score": 70,
      "is_published": true,
      "question_count": 10,
      "max_score": 100,
      "user_attempts": {
        "attempts_taken": 1,
        "best_score": 85,
        "last_attempt_at": "2025-11-25T10:00:00Z"
      }
    }
  ]
}
```

---

### 6.2. Get Quiz

**Endpoint**: `GET /quizzes/{quiz_id}`
**Auth**: Required (enrolled student)
**Description**: Get quiz details (not questions)

**Response** (200):
```json
{
  "success": true,
  "data": {
    "quiz_id": "quiz-uuid",
    "title": "Module 1 Quiz",
    "description": "Test your knowledge of Module 1",
    "time_limit_minutes": 30,
    "attempt_limit": 3,
    "pass_score": 70,
    "shuffle_questions": true,
    "show_correct_answers": true,
    "question_count": 10,
    "max_score": 100,
    "user_attempts": {
      "attempts_taken": 1,
      "attempts_remaining": 2,
      "best_score": 85
    }
  }
}
```

---

### 6.3. Start Quiz Attempt

**Endpoint**: `POST /quizzes/{quiz_id}/attempts`
**Auth**: Required (enrolled student)
**Description**: Start a new quiz attempt

**Response** (201):
```json
{
  "success": true,
  "data": {
    "attempt_id": "attempt-uuid",
    "quiz_id": "quiz-uuid",
    "attempt_number": 2,
    "started_at": "2025-11-25T10:30:00Z",
    "time_limit_seconds": 1800,
    "questions": [
      {
        "quiz_submission_id": "submission-uuid",
        "question_id": "question-uuid",
        "question_text": "What is a variable?",
        "type": "MCQ",
        "max_points": 10,
        "options": [
          {
            "option_id": "option-uuid-1",
            "option_text": "A container for data",
            "order_num": 1
          },
          {
            "option_id": "option-uuid-2",
            "option_text": "A function",
            "order_num": 2
          }
        ]
      }
    ]
  }
}
```

**Errors**:
- `422`: Attempt limit exceeded
- `422`: Quiz not published

---

### 6.4. Submit Quiz

**Endpoint**: `POST /attempts/{attempt_id}/submit`
**Auth**: Required (attempt owner)
**Description**: Submit quiz answers

**Request Body**:
```json
{
  "answers": [
    {
      "quiz_submission_id": "submission-uuid-1",
      "question_id": "question-uuid-1",
      "selected_option_ids": ["option-uuid-1"]
    },
    {
      "quiz_submission_id": "submission-uuid-2",
      "question_id": "question-uuid-2",
      "answer_text": "A variable is a container..."
    }
  ]
}
```

**Response** (200):
```json
{
  "success": true,
  "data": {
    "attempt_id": "attempt-uuid",
    "status": "GRADED",
    "submitted_at": "2025-11-25T11:00:00Z",
    "auto_score": 80,
    "manual_score": 0,
    "final_score": 80,
    "max_possible_score": 100,
    "percentage_score": 80,
    "passed": true,
    "answers": [
      {
        "question_id": "...",
        "is_correct": true,
        "score": 10,
        "explanation": "Correct! A variable is..."
      }
    ]
  }
}
```

---

### 6.5. Create Quiz (Instructor)

**Endpoint**: `POST /courses/{course_id}/quizzes`
**Auth**: Required (Course Instructor, Admin)
**Description**: Create quiz

**Request Body**:
```json
{
  "title": "Final Exam",
  "description": "Comprehensive final exam",
  "time_limit_minutes": 90,
  "attempt_limit": 1,
  "pass_score": 70,
  "shuffle_questions": true,
  "show_correct_answers": false
}
```

**Response** (201):
```json
{
  "success": true,
  "data": {
    "quiz_id": "new-quiz-uuid",
    "title": "Final Exam",
    "is_published": false
  }
}
```

---

### 6.6. Add Question to Quiz (Instructor)

**Endpoint**: `POST /quizzes/{quiz_id}/questions`
**Auth**: Required (Course Instructor, Admin)
**Description**: Add existing question to quiz

**Request Body**:
```json
{
  "question_id": "question-uuid",
  "points": 10,
  "order_num": 1
}
```

**Response** (200):
```json
{
  "success": true,
  "message": "Question added to quiz"
}
```

---

## 7. ASSIGNMENTS

### 7.1. List Assignments

**Endpoint**: `GET /courses/{course_id}/assignments`
**Auth**: Required (enrolled student)
**Description**: List course assignments

**Response** (200):
```json
{
  "success": true,
  "data": [
    {
      "assignment_id": "assignment-uuid",
      "title": "Build a Calculator",
      "assignment_type": "CODE",
      "max_points": 100,
      "due_date": "2025-12-01T23:59:59Z",
      "late_submission_allowed": true,
      "late_penalty_percent": 10,
      "max_late_days": 7,
      "user_submission": {
        "submitted": true,
        "submitted_at": "2025-11-28T15:30:00Z",
        "is_late": false,
        "status": "GRADED",
        "final_score": 95
      }
    }
  ]
}
```

---

### 7.2. Get Assignment

**Endpoint**: `GET /assignments/{assignment_id}`
**Auth**: Required (enrolled student)
**Description**: Get assignment details

**Response** (200):
```json
{
  "success": true,
  "data": {
    "assignment_id": "assignment-uuid",
    "title": "Build a Calculator",
    "description": "Create a calculator app with basic operations",
    "instructions": "Detailed instructions...",
    "assignment_type": "CODE",
    "max_points": 100,
    "due_date": "2025-12-01T23:59:59Z",
    "late_submission_allowed": true,
    "late_penalty_percent": 10,
    "max_late_days": 7,
    "allow_resubmission": false,
    "max_submissions": 1,
    "rubric": {
      "criteria": [
        {"name": "Functionality", "points": 50},
        {"name": "Code Quality", "points": 30},
        {"name": "Documentation", "points": 20}
      ]
    },
    "user_submission": {
      "submitted": true,
      "submission_number": 1,
      "submitted_at": "2025-11-28T15:30:00Z",
      "is_late": false,
      "status": "GRADED",
      "final_score": 95,
      "feedback": "Excellent work!"
    }
  }
}
```

---

### 7.3. Submit Assignment

**Endpoint**: `POST /assignments/{assignment_id}/submissions`
**Auth**: Required (enrolled student)
**Description**: Submit assignment
**Content-Type**: `multipart/form-data`

**Request**:
```
POST /assignments/{assignment_id}/submissions
Content-Type: multipart/form-data

content: "Essay content for ESSAY type"
code_submission: "Python code for CODE type"
files: <file1>, <file2> (for FILE_UPLOAD type)
```

**Response** (201):
```json
{
  "success": true,
  "data": {
    "assignment_submission_id": "submission-uuid",
    "assignment_id": "assignment-uuid",
    "submission_number": 1,
    "submitted_at": "2025-11-28T15:30:00Z",
    "is_late": false,
    "days_late": 0,
    "status": "SUBMITTED",
    "auto_score": null,
    "file_urls": [
      "https://cdn.blearning.edu/submissions/file1.pdf",
      "https://cdn.blearning.edu/submissions/file2.zip"
    ]
  }
}
```

**Errors**:
- `422`: Submission limit exceeded
- `422`: Late submission not allowed
- `422`: Too late (exceeds max_late_days)
- `400`: File size exceeded

---

### 7.4. Create Assignment (Instructor)

**Endpoint**: `POST /courses/{course_id}/assignments`
**Auth**: Required (Course Instructor, Admin)
**Description**: Create assignment

**Request Body**:
```json
{
  "title": "Build a Calculator",
  "description": "Create a calculator app",
  "instructions": "Detailed instructions...",
  "assignment_type": "CODE",
  "max_points": 100,
  "due_date": "2025-12-01T23:59:59Z",
  "late_submission_allowed": true,
  "late_penalty_percent": 10,
  "max_late_days": 7,
  "allow_resubmission": false,
  "max_submissions": 1,
  "rubric": {
    "criteria": [
      {"name": "Functionality", "points": 50},
      {"name": "Code Quality", "points": 30},
      {"name": "Documentation", "points": 20}
    ]
  },
  "auto_grading_enabled": false
}
```

**Response** (201):
```json
{
  "success": true,
  "data": {
    "assignment_id": "new-assignment-uuid",
    "title": "Build a Calculator",
    "created_at": "2025-11-25T10:30:00Z"
  }
}
```

---

### 7.5. Grade Assignment (Instructor)

**Endpoint**: `PUT /submissions/{submission_id}/grade`
**Auth**: Required (Course Instructor, TA, Admin)
**Description**: Grade assignment submission

**Request Body**:
```json
{
  "manual_score": 95,
  "rubric_scores": {
    "Functionality": 48,
    "Code Quality": 28,
    "Documentation": 19
  },
  "feedback": "Excellent work! Minor improvements in documentation."
}
```

**Response** (200):
```json
{
  "success": true,
  "data": {
    "assignment_submission_id": "submission-uuid",
    "status": "GRADED",
    "auto_score": 0,
    "manual_score": 95,
    "penalty_applied": 0,
    "final_score": 95,
    "graded_at": "2025-11-30T14:00:00Z",
    "graded_by": {
      "user_id": "instructor-uuid",
      "first_name": "John",
      "last_name": "Doe"
    }
  }
}
```

---

## 8. ENROLLMENT & PROGRESS

### 8.1. Enroll in Course

**Endpoint**: `POST /enrollments`
**Auth**: Required (Student)
**Description**: Enroll in a course

**Request Body**:
```json
{
  "course_id": "course-uuid"
}
```

**Response** (201):
```json
{
  "success": true,
  "data": {
    "course_enrollment_id": "enrollment-uuid",
    "user_id": "user-uuid",
    "course_id": "course-uuid",
    "role_in_course": "STUDENT",
    "enrollment_status": "ACTIVE",
    "enrolled_at": "2025-11-25T10:30:00Z"
  }
}
```

**Errors**:
- `409`: Already enrolled
- `422`: Course not published

---

### 8.2. Get My Enrollments

**Endpoint**: `GET /enrollments/me`
**Auth**: Required
**Description**: List user's enrolled courses

**Query Params**:
- `status`: ACTIVE, COMPLETED, DROPPED
- `role`: STUDENT, INSTRUCTOR, TA

**Response** (200):
```json
{
  "success": true,
  "data": [
    {
      "course_enrollment_id": "enrollment-uuid",
      "course": {
        "course_id": "course-uuid",
        "code": "CS101",
        "title": "Introduction to Programming",
        "thumbnail_url": "..."
      },
      "role_in_course": "STUDENT",
      "enrollment_status": "ACTIVE",
      "enrolled_at": "2025-09-01T00:00:00Z",
      "completion_percentage": 65.5,
      "last_accessed_at": "2025-11-25T08:00:00Z"
    }
  ]
}
```

---

### 8.3. Update Lecture Progress

**Endpoint**: `PUT /progress/lectures/{lecture_id}`
**Auth**: Required (enrolled student)
**Description**: Update lecture progress

**Request Body**:
```json
{
  "percent_complete": 75.5,
  "last_position_seconds": 453
}
```

**Response** (200):
```json
{
  "success": true,
  "data": {
    "progress_id": "progress-uuid",
    "lecture_id": "lecture-uuid",
    "status": "IN_PROGRESS",
    "percent_complete": 75.5,
    "last_position_seconds": 453,
    "updated_at": "2025-11-25T11:00:00Z"
  }
}
```

---

### 8.4. Get Course Progress

**Endpoint**: `GET /courses/{course_id}/progress`
**Auth**: Required (enrolled user)
**Description**: Get user's progress for a course

**Response** (200):
```json
{
  "success": true,
  "data": {
    "course_id": "course-uuid",
    "overall_completion": 65.5,
    "lectures_completed": 15,
    "lectures_total": 45,
    "quizzes_completed": 3,
    "quizzes_total": 8,
    "assignments_completed": 2,
    "assignments_total": 5,
    "modules": [
      {
        "module_id": "module-uuid",
        "title": "Module 1",
        "completion": 100,
        "lectures": [
          {
            "lecture_id": "lecture-uuid",
            "title": "Lecture 1",
            "status": "COMPLETED",
            "percent_complete": 100,
            "completed_at": "2025-09-15T10:00:00Z"
          }
        ]
      }
    ]
  }
}
```

---

### 8.5. Get GradeBook

**Endpoint**: `GET /courses/{course_id}/gradebook`
**Auth**: Required (enrolled student)
**Description**: Get gradebook for a course

**Response** (200):
```json
{
  "success": true,
  "data": {
    "gradebook_id": "gradebook-uuid",
    "course_id": "course-uuid",
    "quiz_score": 85.5,
    "assignment_score": 92.0,
    "participation_score": 95.0,
    "total_score": 90.5,
    "weighted_score": 88.5,
    "letter_grade": "A-",
    "last_updated_at": "2025-11-25T14:00:00Z",
    "breakdown": {
      "quizzes": [
        {
          "quiz_id": "quiz-uuid",
          "title": "Module 1 Quiz",
          "score": 85,
          "max_score": 100,
          "weight": 0.2,
          "completed_at": "2025-09-20T10:00:00Z"
        }
      ],
      "assignments": [
        {
          "assignment_id": "assignment-uuid",
          "title": "Build a Calculator",
          "score": 95,
          "max_score": 100,
          "weight": 0.3,
          "submitted_at": "2025-10-15T15:00:00Z"
        }
      ]
    }
  }
}
```

---

## 9. CERTIFICATES

### 9.1. Get My Certificates

**Endpoint**: `GET /certificates/me`
**Auth**: Required
**Description**: List user's certificates

**Response** (200):
```json
{
  "success": true,
  "data": [
    {
      "certificate_id": "cert-uuid",
      "course": {
        "course_id": "course-uuid",
        "code": "CS101",
        "title": "Introduction to Programming"
      },
      "certificate_code": "BL-2025-000123",
      "title": "Certificate of Completion",
      "issue_date": "2025-11-20",
      "completion_date": "2025-11-15",
      "final_grade": 88.5,
      "grade_letter": "A-",
      "pdf_url": "https://cdn.blearning.edu/certificates/cert123.pdf",
      "qr_code_url": "https://cdn.blearning.edu/certificates/qr123.png",
      "verification_url": "https://blearning.edu/verify/cert-code",
      "status": "ACTIVE"
    }
  ]
}
```

---

### 9.2. Download Certificate

**Endpoint**: `GET /certificates/{certificate_id}/download`
**Auth**: Required (certificate owner)
**Description**: Download certificate PDF

**Response** (302): Redirect to PDF URL

---

### 9.3. Verify Certificate (Public)

**Endpoint**: `GET /certificates/verify`
**Auth**: No
**Description**: Verify certificate by code

**Query Params**:
- `code`: Certificate code (e.g., BL-2025-000123)

**Response** (200):
```json
{
  "success": true,
  "data": {
    "certificate_id": "cert-uuid",
    "certificate_code": "BL-2025-000123",
    "status": "ACTIVE",
    "valid": true,
    "holder": {
      "full_name": "Jane Smith"
    },
    "course": {
      "title": "Introduction to Programming",
      "code": "CS101"
    },
    "issue_date": "2025-11-20",
    "final_grade": 88.5,
    "grade_letter": "A-"
  }
}
```

**Response** (200) - Invalid:
```json
{
  "success": true,
  "data": {
    "valid": false,
    "reason": "REVOKED",
    "message": "This certificate has been revoked."
  }
}
```

---

## 10. NOTIFICATIONS

### 10.1. Get My Notifications

**Endpoint**: `GET /notifications/me`
**Auth**: Required
**Description**: List user's notifications

**Query Params**:
- `is_read`: true/false
- `type`: Notification type
- `page`, `limit`

**Response** (200):
```json
{
  "success": true,
  "data": [
    {
      "notification_id": "notif-uuid",
      "notification_type": "ASSIGNMENT_DUE",
      "title": "Assignment Due Soon",
      "message": "Your assignment 'Build a Calculator' is due in 24 hours.",
      "related_entity_type": "Assignment",
      "related_entity_id": "assignment-uuid",
      "action_url": "/assignments/assignment-uuid",
      "priority": "HIGH",
      "is_read": false,
      "created_at": "2025-11-24T10:00:00Z"
    }
  ],
  "meta": {
    "unread_count": 5
  },
  "pagination": { ... }
}
```

---

### 10.2. Mark as Read

**Endpoint**: `PUT /notifications/{notification_id}/read`
**Auth**: Required (notification owner)
**Description**: Mark notification as read

**Response** (200):
```json
{
  "success": true,
  "message": "Notification marked as read"
}
```

---

### 10.3. Mark All as Read

**Endpoint**: `PUT /notifications/me/read-all`
**Auth**: Required
**Description**: Mark all notifications as read

**Response** (200):
```json
{
  "success": true,
  "message": "All notifications marked as read"
}
```

---

### 10.4. Get Notification Preferences

**Endpoint**: `GET /notifications/preferences`
**Auth**: Required
**Description**: Get user's notification preferences

**Response** (200):
```json
{
  "success": true,
  "data": [
    {
      "notification_type": "ASSIGNMENT_DUE",
      "email_enabled": true,
      "push_enabled": true,
      "sms_enabled": false,
      "frequency": "IMMEDIATE"
    },
    {
      "notification_type": "GRADE_PUBLISHED",
      "email_enabled": true,
      "push_enabled": true,
      "sms_enabled": false,
      "frequency": "IMMEDIATE"
    }
  ]
}
```

---

### 10.5. Update Notification Preferences

**Endpoint**: `PUT /notifications/preferences`
**Auth**: Required
**Description**: Update notification preferences

**Request Body**:
```json
{
  "preferences": [
    {
      "notification_type": "ASSIGNMENT_DUE",
      "email_enabled": true,
      "push_enabled": true,
      "sms_enabled": false,
      "frequency": "IMMEDIATE"
    },
    {
      "notification_type": "COURSE_UPDATE",
      "email_enabled": true,
      "push_enabled": false,
      "sms_enabled": false,
      "frequency": "DAILY_DIGEST"
    }
  ]
}
```

**Response** (200):
```json
{
  "success": true,
  "message": "Preferences updated successfully"
}
```

---

## 11. FILE UPLOADS

### 11.1. Upload File

**Endpoint**: `POST /files/upload`
**Auth**: Required
**Description**: Upload file (generic endpoint)
**Content-Type**: `multipart/form-data`

**Request**:
```
POST /files/upload
Content-Type: multipart/form-data

file: <file>
entity_type: "Assignment"
entity_id: "assignment-uuid"
```

**Response** (201):
```json
{
  "success": true,
  "data": {
    "file_id": "file-uuid",
    "original_filename": "report.pdf",
    "file_url": "https://cdn.blearning.edu/files/report.pdf",
    "file_size_bytes": 2048000,
    "mime_type": "application/pdf",
    "uploaded_at": "2025-11-25T11:00:00Z"
  }
}
```

**Errors**:
- `400`: File too large
- `400`: Invalid file type

---

## 12. ADMIN ENDPOINTS

### 12.1. System Statistics

**Endpoint**: `GET /admin/statistics`
**Auth**: Required (Admin)
**Description**: Get system-wide statistics

**Response** (200):
```json
{
  "success": true,
  "data": {
    "users": {
      "total": 10523,
      "active": 8945,
      "new_this_month": 456
    },
    "courses": {
      "total": 145,
      "published": 98,
      "draft": 47
    },
    "enrollments": {
      "total": 45678,
      "active": 32456,
      "completed": 13222
    },
    "certificates_issued": 8934
  }
}
```

---

### 12.2. Activity Logs

**Endpoint**: `GET /admin/activity-logs`
**Auth**: Required (Admin)
**Description**: Query activity logs

**Query Params**:
- `user_id`: Filter by user
- `action`: Filter by action
- `entity_type`: Filter by entity type
- `start_date`, `end_date`: Date range
- `page`, `limit`

**Response** (200):
```json
{
  "success": true,
  "data": [
    {
      "log_id": "log-uuid",
      "user": {
        "user_id": "user-uuid",
        "email": "student@example.com"
      },
      "action": "CREATE",
      "entity_type": "Course",
      "entity_id": "course-uuid",
      "description": "Created course CS102",
      "ip_address": "192.168.1.1",
      "created_at": "2025-11-25T10:30:00Z"
    }
  ],
  "pagination": { ... }
}
```

---

### 12.3. System Settings

**Endpoint**: `GET /admin/settings`
**Auth**: Required (Admin)
**Description**: Get system settings

**Response** (200):
```json
{
  "success": true,
  "data": [
    {
      "setting_key": "MAX_FILE_SIZE_MB",
      "setting_value": "50",
      "data_type": "INTEGER",
      "category": "UPLOADS",
      "description": "Maximum file upload size in MB"
    }
  ]
}
```

---

### 12.4. Update System Setting

**Endpoint**: `PUT /admin/settings/{setting_key}`
**Auth**: Required (Admin)
**Description**: Update system setting

**Request Body**:
```json
{
  "setting_value": "100"
}
```

**Response** (200):
```json
{
  "success": true,
  "message": "Setting updated successfully"
}
```

---

## 13. WEBHOOKS

### 13.1. Certificate Issued

**Event**: `certificate.issued`
**Payload**:
```json
{
  "event": "certificate.issued",
  "timestamp": "2025-11-25T10:30:00Z",
  "data": {
    "certificate_id": "cert-uuid",
    "user_id": "user-uuid",
    "course_id": "course-uuid",
    "certificate_code": "BL-2025-000123",
    "pdf_url": "https://cdn.blearning.edu/certificates/cert123.pdf"
  }
}
```

### 13.2. Assignment Submitted

**Event**: `assignment.submitted`
**Payload**:
```json
{
  "event": "assignment.submitted",
  "timestamp": "2025-11-25T11:00:00Z",
  "data": {
    "assignment_submission_id": "submission-uuid",
    "assignment_id": "assignment-uuid",
    "user_id": "user-uuid",
    "course_id": "course-uuid",
    "is_late": false
  }
}
```

---

## 14. RATE LIMITS

| Endpoint Pattern | Rate Limit | Window |
|------------------|------------|--------|
| `/auth/login` | 5 requests | 5 minutes |
| `/auth/register` | 3 requests | 1 hour |
| `/files/upload` | 20 requests | 1 minute |
| All other endpoints | 100 requests | 1 minute |

**Rate Limit Headers**:
```
X-RateLimit-Limit: 100
X-RateLimit-Remaining: 87
X-RateLimit-Reset: 1701234567
```

**Error Response** (429):
```json
{
  "success": false,
  "error": {
    "code": "RATE_LIMIT_EXCEEDED",
    "message": "Too many requests. Please try again in 45 seconds.",
    "retry_after": 45
  }
}
```

---

## 15. APPENDIX

### 15.1. Error Codes

| Code | Description |
|------|-------------|
| `VALIDATION_ERROR` | Request validation failed |
| `AUTHENTICATION_REQUIRED` | Missing or invalid token |
| `FORBIDDEN` | No permission for this action |
| `NOT_FOUND` | Resource not found |
| `CONFLICT` | Duplicate resource (e.g., already enrolled) |
| `BUSINESS_LOGIC_ERROR` | Business rule violation |
| `RATE_LIMIT_EXCEEDED` | Too many requests |
| `FILE_TOO_LARGE` | File size exceeds limit |
| `INVALID_FILE_TYPE` | File type not allowed |
| `QUIZ_ALREADY_SUBMITTED` | Cannot submit quiz twice |
| `ATTEMPT_LIMIT_EXCEEDED` | No attempts remaining |
| `LATE_SUBMISSION_NOT_ALLOWED` | Past deadline |

### 15.2. Date/Time Format
All timestamps use **ISO 8601** format in **UTC**:
```
2025-11-25T10:30:00Z
```

### 15.3. UUID Format
All IDs are UUID v4:
```
550e8400-e29b-41d4-a716-446655440000
```

### 15.4. Related Documents
- [BFD-SPEC.md](./BFD-SPEC.md) - Business functions
- [ERD-SPEC.md](./ERD-SPEC.md) - Database entities
- [DATABASE-SCHEMA.md](./DATABASE-SCHEMA.md) - Complete DDL
- [FUNCTIONAL-REQUIREMENTS.md](./FUNCTIONAL-REQUIREMENTS.md) - Functional requirements

---

**Document Status**: ✅ Complete
**Last Updated**: 2025-11-25
**Total Endpoints**: 70+

---

**END OF API ENDPOINTS SPECIFICATION**
