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
  testType?: SubmissionCheck;
  notifyOnEmail?: boolean;
  plagiarismWarning?: number;
  plagiarismLevel?: number;
  archived?: boolean;
  mySubmissions?: Submission[];
  boughtSubmissions?: Submission[];

  sourceId?: number;
  sourceName?: string;
  sourceLanguage?: string;
  sourceTimeout?: number;
}

export enum SubmissionCheck {
  NONE = 'NONE',
  MANUAL = 'MANUAL',
  AUTOMATIC = 'AUTOMATIC',
}

export enum AssignmentStatus {
  ACTIVE,
  DELETED,
  ARCHIVED,
}
