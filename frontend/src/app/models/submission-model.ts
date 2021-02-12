export interface Submission {
  submissionId?: number;
  input?: string;
  output?: string;
  created?: Date;
  status?: string;
}

export interface SubmissionFilePair {
  inputName?: string;
  outputName?: string;
  inputFile?: File;
  outputFile?: File;
  status?: string;
}
