import { Submission } from "./submission-model";

export interface Assignment {
  title: string;
  assignmentId?: number;
  classromUrl?: string;
  description?: string;
  startDate: Date;
  endDate?: Date;
  maxSubmissionsTotal?: number;
  maxSubmissionsStudent?: number;
  coinsPerSubmission: number;
  coinsPrice: number;
  inputDataType?: string;
  outputDataType?: string;
  submissionCheck: SubmissionCheck;
  submissionCheckUrl?: string;
  submissionNotify: boolean;
  plagiarismWarning?: number;
  plagiarismLevel?: number;
  noOfSubmisions: number;
  studentSubmissions?: Submission[];
  boughtSubmissions?: Submission[];
  status?: AssignmentStatus ;
  visible: boolean;
}

export enum SubmissionCheck {
  NONE,
  MANUAL,
  AUTOMATIC
}

export enum AssignmentStatus {
  ACTIVE,
  DELETED,
  ARCHIVED
}