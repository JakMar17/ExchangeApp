import { User } from "./user-model";

export interface Submission {
  submissionId?: number;
  input?: string;
  output?: string;
  created?: Date;
  status?: string;
  inputFile?: string;
  outputFile?: string;
  author?: User;
}

export interface SubmissionFilePair {
  inputName?: string;
  outputName?: string;
  inputFile?: File;
  outputFile?: File;
  status?: string;
}

export interface UploadModel {
  inputFilename: string;
  outputFilename: string;
  inputFile: string;
  outputFile: string;
}
