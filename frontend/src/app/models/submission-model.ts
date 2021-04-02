import { User } from './user-model';

export interface Submission {
  submissionId?: number;
  input?: string;
  output?: string;
  created?: Date;
  status?: SubmissionStatus;
  inputFile?: string;
  outputFile?: string;
  diffOrErrorMessage?: string;
  expectedOutput?: string;
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

export enum SubmissionStatus {
  OK = 'OK',
  NOK = 'NOK',
  TIMEOUT = 'TIMEOUT',
  PENDING_REVIEW = 'PENDING_REVIEW',
  COMPILE_ERROR = 'COMPILE_ERROR',
}

export class SubmissionSimilarity {
  public inputSimilarity: number;
  public outputSimilarity: number;
  public noOfSubmissionsInGroup: number;
  public group: string;

  public objectToArray(): any {
    return [this.group, this.inputSimilarity, this.outputSimilarity, this.noOfSubmissionsInGroup];
  }
}