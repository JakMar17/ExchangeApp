import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Assignment } from 'src/app/models/assignment-model';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class AssignmentsApiService {
  private baseUrl = environment.BASE_API_URL + 'assignment/';

  constructor(private http: HttpClient) {}

  public getAssignments(
    courseId: number,
    personalNumber: string
  ): Observable<Assignment[]> {
    const headers = new HttpHeaders({ 'Personal-Number': personalNumber });
    return this.http.get<Assignment[]>(
      this.baseUrl + 'all?courseId=' + courseId,
      { headers }
    );
  }

  public setAssignmentsVisibility(
    assignmentId: number,
    personalNumber: string,
    visibility: boolean
  ): Observable<Assignment> {
    console.log(assignmentId, personalNumber, visibility);
    const headers = new HttpHeaders({ 'Personal-Number': personalNumber });
    return this.http.put<Assignment>(
      this.baseUrl +
        'set-visibility?assignmentId=' +
        assignmentId +
        '&visibility=' +
        visibility,
        null,
      { headers }
    );
  }
}
