import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { SubmissionApiService } from 'src/app/api/submission-api/submission-api.service';
import { Assignment } from 'src/app/models/assignment-model';
import { Submission } from 'src/app/models/submission-model';
import { UserServiceService } from '../user-service/user-service.service';

@Injectable({
  providedIn: 'root',
})
export class SubmissionService {
  constructor(
    private submissionApi: SubmissionApiService,
    private userService: UserServiceService
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
}
