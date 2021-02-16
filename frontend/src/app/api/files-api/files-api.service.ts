import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Assignment } from 'src/app/models/assignment-model';
import {
  Submission,
  SubmissionFilePair,
  UploadModel,
} from 'src/app/models/submission-model';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class FilesApiService {
  private baseUrl = environment.BASE_API_URL + 'files';

  constructor(private http: HttpClient) {}

  public uploadFiles(
    uploads: SubmissionFilePair[],
    personalNumber: string,
    assignmentId: number
  ): Observable<Assignment> {
    const headers = new HttpHeaders({ 'Personal-Number': personalNumber });
    const formdata = new FormData();
    uploads.forEach((e) => {
      formdata.append('input', e.inputFile);
      formdata.append('output', e.outputFile);
    });

    return this.http.post<Assignment>(
      this.baseUrl + '/upload?assignmentId=' + assignmentId,
      formdata,
      {
        headers,
      }
    );
  }
}
