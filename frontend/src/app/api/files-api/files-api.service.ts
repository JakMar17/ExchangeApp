import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Submission } from 'src/app/models/submission-model';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class FilesApiService {
  private baseUrl = environment.BASE_API_URL + 'files';

  constructor(private http: HttpClient) {}

  public uploadFilePair(
    input: File,
    output: File,
    assignmentId: number,
    personalNumber: string
  ): Observable<Submission> {
    const formData = new FormData();
    formData.append('input', input);
    formData.append('output', output);
    formData.append('assignmentId', assignmentId.toString());

    const headers = new HttpHeaders({ 'Personal-Number': personalNumber });

    return this.http.post<Submission>(this.baseUrl + '/upload', formData, {
      headers,
    });
  }
}
