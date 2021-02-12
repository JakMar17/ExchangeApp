import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { FilesApiService } from 'src/app/api/files-api/files-api.service';
import { Assignment } from 'src/app/models/assignment-model';
import { Submission } from 'src/app/models/submission-model';
import { UserServiceService } from '../user-service/user-service.service';

@Injectable({
  providedIn: 'root',
})
export class FilesService {
  constructor(
    private filesApi: FilesApiService,
    private userService: UserServiceService
  ) {}

  public uploadFilePair(
    input: File,
    output: File,
    assignment: Assignment
  ): Observable<Submission> | null {
    return this.filesApi.uploadFilePair(
      input,
      output,
      assignment.assignmentId,
      this.userService.userLoggedIn.personalNumber
    );
  }
}
