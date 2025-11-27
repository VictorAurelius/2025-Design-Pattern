# API Endpoints - B-Learning Core v1.0

**Project**: B-Learning Core API
**Base URL**: `https://api.blearning.edu.vn/v1`
**Authentication**: JWT Bearer Token
**Created**: 2025-11-27

---

## Authentication Endpoints

### POST /auth/register
**Purpose**: Student self-registration
**Access**: Public
**Request**:
```json
{
  "email": "student@example.com",
  "password": "SecurePass123",
  "first_name": "John",
  "last_name": "Doe"
}
```
**Response**: `201 Created` + User object

### POST /auth/login
**Purpose**: User login
**Access**: Public
**Request**:
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```
**Response**: `200 OK` + JWT token

### POST /auth/password-reset
**Purpose**: Request password reset
**Access**: Public

---

## User Management (8 endpoints)

- `GET /users/me` - Get current user profile
- `PUT /users/me` - Update profile
- `GET /users/{id}/roles` - Get user roles
- `POST /users/{id}/roles` - Assign role (Admin)
- `DELETE /users/{id}/roles/{role_id}` - Remove role (Admin)

---

## Course Management (12 endpoints)

- `GET /courses` - List published courses
- `GET /courses/{id}` - Get course details
- `POST /courses` - Create course (Instructor)
- `PUT /courses/{id}` - Update course (Instructor)
- `POST /courses/{id}/publish` - Publish course
- `POST /courses/{id}/modules` - Create module
- `PUT /modules/{id}` - Update module
- `POST /modules/{id}/lectures` - Create lecture
- `PUT /lectures/{id}` - Update lecture
- `POST /lectures/{id}/resources` - Upload resource
- `GET /courses/{id}/structure` - Get full course structure

---

## Assessment (15 endpoints)

### Quiz Management
- `POST /courses/{id}/quizzes` - Create quiz
- `PUT /quizzes/{id}` - Update quiz
- `POST /quizzes/{id}/publish` - Publish quiz
- `POST /questions` - Create question
- `PUT /questions/{id}` - Update question

### Quiz Taking
- `POST /quizzes/{id}/attempts` - Start attempt
- `PUT /attempts/{id}/answers` - Save answers
- `POST /attempts/{id}/submit` - Submit quiz
- `GET /attempts/{id}/results` - View results

### Assignment
- `POST /lectures/{id}/submissions` - Submit assignment
- `GET /submissions/{id}` - Get submission
- `PUT /submissions/{id}/grade` - Grade submission (Instructor)

### Grading
- `GET /quizzes/{id}/pending` - Get pending attempts
- `PUT /attempts/{id}/grade` - Grade essay questions
- `POST /attempts/{id}/finalize` - Finalize grading

---

## Enrollment & Progress (6 endpoints)

- `POST /enrollments` - Enroll in course
- `GET /enrollments/me` - My enrollments
- `DELETE /enrollments/{id}` - Drop enrollment
- `GET /courses/{id}/progress` - My progress
- `PUT /progress/{module_id}` - Update progress
- `GET /users/{id}/transcript` - Student transcript

---

## Class Management (8 endpoints)

- `POST /classes` - Create class (Instructor)
- `GET /classes/{id}` - Get class details
- `PUT /classes/{id}` - Update class
- `POST /classes/{id}/schedule` - Add session
- `PUT /classes/{id}/sessions/{session_id}/attendance` - Mark attendance
- `GET /classes/{id}/attendance-report` - Attendance report
- `POST /classes/{id}/cancel` - Cancel class

---

## Certificate (4 endpoints)

- `GET /certificates/me` - My certificates
- `GET /certificates/{id}/download` - Download PDF
- `GET /certificates/verify/{code}` - Public verification
- `POST /certificates/{id}/revoke` - Revoke certificate (Admin)

---

## Reporting & Analytics (5 endpoints)

- `GET /analytics/courses/{id}/stats` - Course statistics
- `GET /analytics/quizzes/{id}/performance` - Quiz analytics
- `GET /analytics/students/{id}/performance` - Student performance
- `GET /analytics/classes/{id}/progress` - Class progress
- `GET /analytics/dashboard` - Admin dashboard

---

**Total**: 58+ REST API endpoints

For complete API documentation with request/response schemas, see Swagger/OpenAPI spec.
