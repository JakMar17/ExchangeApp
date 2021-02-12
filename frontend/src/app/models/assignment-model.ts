import { Submission } from './submission-model';

export interface Assignment {
  title?: string;
  assignmentId?: number;
  classroomUrl?: string;
  description?: string;
  startDate?: Date;
  endDate?: Date;
  maxSubmissionsTotal?: number;
  maxSubmissionsPerStudent?: number;
  coinsPerSubmission?: number;
  coinsPrice?: number;
  noOfSubmissionsTotal?: number;
  noOfSubmissionsStudent?: number;
  visible?: boolean;
  inputExtension?: string;
  outputExtension?: string;
  testType?: string;
  notifyOnEmail?: boolean;
  plagiarismWarning?: number;
  plagiarismLevel?: number;
  archived?: boolean;
  mySubmissions?: Submission[];
  boughtSubmissions?: Submission[];
}

export enum SubmissionCheck {
  NONE,
  MANUAL,
  AUTOMATIC,
}

export enum AssignmentStatus {
  ACTIVE,
  DELETED,
  ARCHIVED,
}
