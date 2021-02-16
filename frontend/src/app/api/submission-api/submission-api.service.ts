import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Submission } from 'src/app/models/submission-model';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class SubmissionApiService {
  private baseUrl = environment.BASE_API_URL + 'submissions';

  constructor(private http: HttpClient) {}

  public buySubmissions(
    personalNumber: string,
    assignmentId: number,
    noOfSubmissions: number
  ): Observable<Submission[]> {
    const headers = new HttpHeaders({ 'Personal-Number': personalNumber });
    return this.http.get<Submission[]>(
      this.baseUrl +
        '/buy?assignmentId=' +
        assignmentId +
        '&noOfSubmissions=' +
        noOfSubmissions,
      { headers }
    );
  }

  public getSubmissionDetails(
    personalNumber: string,
    submissionId: number
  ): Observable<Submission> {
    const headers = new HttpHeaders({ 'Personal-Number': personalNumber });
    return this.http.get<Submission>(
      this.baseUrl + '/details?submissionId=' + submissionId,
      { headers }
    );
  }
}
