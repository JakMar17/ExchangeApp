import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Assignment } from 'src/app/models/assignment-model';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class AssignmentsApiService {
  private baseUrl = environment.BASE_API_URL + 'assignment';

  constructor(private http: HttpClient) {}

  public getAssignments(
    courseId: number,
    Authorization: string
  ): Observable<Assignment[]> {
    const headers = { Authorization };
    return this.http.get<Assignment[]>(
      this.baseUrl + '/all?courseId=' + courseId,
      { headers }
    );
  }

  public setAssignmentsVisibility(
    assignmentId: number,
    Authorization: string,
    visibility: boolean
  ): Observable<Assignment> {
    const headers = { Authorization };
    return this.http.put<Assignment>(
      this.baseUrl +
        '/set-visibility?assignmentId=' +
        assignmentId +
        '&visibility=' +
        visibility,
      null,
      { headers }
    );
  }

  public getAssignmentById(
    assignmentId: number,
    Authorization: string
  ): Observable<Assignment> {
    const headers = { Authorization };
    return this.http.get<Assignment>(
      this.baseUrl + '?assignmentId=' + assignmentId,
      { headers }
    );
  }

  public postAssignment(
    courseId: number,
    assignment: Assignment,
    Authorization: string
  ): Observable<Assignment> {
    const headers = { Authorization };
    return this.http.post<Assignment>(
      this.baseUrl + '/save?courseId=' + courseId,
      assignment,
      { headers }
    );
  }

  public archiveAssignment(
    assignment: Assignment,
    Authorization: string
  ): Observable<Assignment> {
    const headers = { Authorization };
    return this.http.put<Assignment>(this.baseUrl + '/archive', assignment, {
      headers,
    });
  }

  public deleteAssignment(
    assignmentId: number,
    Authorization: string
  ): Observable<boolean> {
    const headers = { Authorization };
    return this.http.delete<boolean>(
      this.baseUrl + '/delete?assignmentId=' + assignmentId,
      { headers }
    );
  }

  public getAssignmentWithSubmissions(
    assignmentId: number,
    Authorization: string
  ): Observable<Assignment> {
    const headers = { Authorization };
    return this.http.get<Assignment>(
      this.baseUrl + '/detailed?assignmentId=' + assignmentId,
      { headers }
    );
  }
}
