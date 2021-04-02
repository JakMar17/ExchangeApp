import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import {
  Submission,
  SubmissionSimilarity,
} from 'src/app/models/submission-model';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class SubmissionApiService {
  private baseUrl = environment.BASE_API_URL + 'submissions';

  constructor(private http: HttpClient) {}

  public buySubmissions(
    Authorization: string,
    assignmentId: number,
    noOfSubmissions: number
  ): Observable<Submission[]> {
    const headers = new HttpHeaders({ Authorization });
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
    Authorization: string,
    submissionId: number
  ): Observable<Submission> {
    const headers = new HttpHeaders({ Authorization });
    return this.http.get<Submission>(
      this.baseUrl + '/details?submissionId=' + submissionId,
      { headers }
    );
  }

  public getAllSubmissionsOfAssignment(
    Authorization: string,
    assignmentId: number
  ): Observable<Submission[]> {
    const headers = new HttpHeaders({ Authorization });
    return this.http.get<Submission[]>(
      this.baseUrl + '/all?assignmentId=' + assignmentId,
      { headers }
    );
  }

  public getSubmissionsSimilarity(
    Authorization: string,
    submissionId: number
  ): Observable<SubmissionSimilarity[]> {
    const headers = { Authorization };
    return this.http.get<SubmissionSimilarity[]>(
      this.baseUrl + '/similarity?submissionId=' + submissionId,
      { headers }
    );
  }
}
