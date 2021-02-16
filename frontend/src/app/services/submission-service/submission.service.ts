import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { FilesApiService } from 'src/app/api/files-api/files-api.service';
import { SubmissionApiService } from 'src/app/api/submission-api/submission-api.service';
import { Assignment } from 'src/app/models/assignment-model';
import {
  Submission,
  SubmissionFilePair,
  UploadModel,
} from 'src/app/models/submission-model';
import { UserServiceService } from '../user-service/user-service.service';

@Injectable({
  providedIn: 'root',
})
export class SubmissionService {
  constructor(
    private submissionApi: SubmissionApiService,
    private userService: UserServiceService,
    private fileApi: FilesApiService
  ) {}

  public buySubmissions(
    assignment: Assignment,
    noOfSubmissions: number
  ): Observable<Submission[]> {
    return this.submissionApi.buySubmissions(
      this.userService.userLoggedIn.personalNumber,
      assignment.assignmentId,
      noOfSubmissions
    );
  }

  public getDetailedSubmission(submission: Submission): Observable<Submission> {
    return this.submissionApi.getSubmissionDetails(
      this.userService.userLoggedIn.personalNumber,
      submission.submissionId
    );
  }

  public async uploadFiles(
    uploadPairs: SubmissionFilePair[],
    assignment: Assignment
  ): Promise<Observable<Assignment>> {
    /*const uploads: UploadModel[] = [];

    for (const element of uploadPairs) {
      const inputFile = await this.readFileContent(element.inputFile);
      const outputFile = await this.readFileContent(element.outputFile);

      uploads.push({
        inputFilename: element.inputName,
        outputFilename: element.outputName,
        inputFile,
        outputFile,
      });
    }*/

    return this.fileApi.uploadFiles(uploadPairs, this.userService.userLoggedIn.personalNumber, assignment.assignmentId);
  }

  private readFileContent(file: File): Promise<string> {
    return new Promise<string>((resolve, reject) => {
      if (!file) {
        resolve('');
      }

      const reader = new FileReader();

      reader.onload = (e) => {
        const text = reader.result.toString();
        resolve(text);
      };

      reader.readAsText(file);
    });
  }
}
