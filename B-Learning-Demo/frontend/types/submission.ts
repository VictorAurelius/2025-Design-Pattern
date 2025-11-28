/**
 * Submission Types
 * =================
 *
 * TypeScript types cho Assignment Submissions.
 */

export interface SubmissionListItem {
  // From AssignmentSubmission
  assignment_submission_id: string;
  submission_number: number;
  submitted_at: string;
  content?: string | null;
  is_late: boolean;
  days_late: number;
  status: 'DRAFT' | 'SUBMITTED' | 'GRADING' | 'GRADED' | 'RETURNED';
  auto_score?: number | null;
  manual_score?: number | null;
  final_score?: number | null;
  penalty_applied: number;
  feedback?: string | null;
  graded_at?: string | null;

  // From User (student)
  student_id: string;
  student_email: string;
  student_name: string;

  // From Assignment
  assignment_id: string;
  assignment_title: string;
  assignment_type: 'ESSAY' | 'CODE' | 'FILE_UPLOAD' | 'PROBLEM_SET' | 'PROJECT';
  max_points: number;
  due_date: string;

  // From Course
  course_id: string;
  course_code: string;
  course_title: string;

  // From User (grader)
  graded_by_name?: string | null;
}

export interface SubmissionDetail extends SubmissionListItem {
  // Additional fields
  file_urls?: any | null;
  code_submission?: string | null;
  rubric_scores?: any | null;
  assignment_description: string;
  assignment_instructions?: string | null;
  late_submission_allowed: boolean;
  late_penalty_percent: number;
  course_description?: string | null;
}

export interface SubmissionListResponse {
  total: number;
  submissions: SubmissionListItem[];
}

export interface GradeSubmissionRequest {
  manual_score: number;
  feedback?: string;
  rubric_scores?: any;
}

export interface SubmissionStats {
  total_submissions: number;
  submitted: number;
  graded: number;
  pending: number;
  average_score?: number | null;
  late_submissions: number;
  on_time_submissions: number;
}
