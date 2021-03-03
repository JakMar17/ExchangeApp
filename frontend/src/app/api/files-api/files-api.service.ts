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
    Authorization: string,
    assignmentId: number
  ): Observable<Assignment> {
    const headers = new HttpHeaders({ Authorization });
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

  public downloadMySubmissions(
    Authorization: string,
    assignmentId: number
  ): void {
    const headers = new HttpHeaders({ Authorization });
    const options: Object = { headers, responseType: 'blob' };

    this.http
      .get<any>(
        this.baseUrl + '/users-submissions?assignmentId=' + assignmentId,
        options
      )
      .subscribe(
        (data) => {
          this.downloadZip(data);
        },
        (err) => console.error(err)
      );
  }

  public downloadSubmission(
    Authorization: string,
    submissionId: number
  ): void {
    const headers = new HttpHeaders({ Authorization });
    const options: Object = { headers, responseType: 'blob' };

    this.http
      .get<any>(
        this.baseUrl + '/download-submission?submissionId=' + submissionId,
        options
      )
      .subscribe(
        (data) => {
          this.downloadZip(data);
        },
        (err) => console.error(err)
      );
  }

  private downloadZip(data: Response): void {
    const blob = new Blob([data as any], { type: 'application/zip' });
    const anchor = document.createElement('a');
    anchor.download = 'testi.zip';
    anchor.href = window.URL.createObjectURL(blob);
    anchor.click();
  }
}
