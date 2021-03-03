import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Course } from 'src/app/models/class-model';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class CourseApiService {
  private baseUrl = environment.BASE_API_URL + 'courses/';
  private baseUrlManagement = environment.BASE_API_URL + 'course/';

  constructor(private http: HttpClient) {}

  public getAll(Authorization: string): Observable<Course[]> {
    const headers = new HttpHeaders({ Authorization });
    return this.http.get<Course[]>(this.baseUrl + 'all', { headers });
  }

  public getCourse(
    courseId: number,
    Authorization: string
  ): Observable<Course> {
    const headers = new HttpHeaders({ Authorization });
    return this.http.get<Course>(this.baseUrl + 'course?courseId=' + courseId, {
      headers,
    });
  }

  public checkPasswordAndGetCourse(
    courseId: number,
    Authorization: string,
    password: string
  ): Observable<Course> {
    const headers = new HttpHeaders({
      Authorization,
      'Course-Password': password,
    });
    return this.http.get<Course>(
      this.baseUrl + 'course/access?courseId=' + courseId,
      {
        headers,
      }
    );
  }

  public getCourseDetailedData(
    courseId: number,
    Authorization: string
  ): Observable<Course> {
    const headers = new HttpHeaders({ Authorization });
    return this.http.get<Course>(
      this.baseUrlManagement + 'detailed?courseId=' + courseId,
      { headers }
    );
  }

  public saveCourse(course: Course, Authorization: string): Observable<Course> {
    const headers = new HttpHeaders({ Authorization });
    return this.http.post<Course>(this.baseUrlManagement, course, { headers });
  }

  public getUsersCourses(Authorization: string): Observable<Course[]> {
    const headers = new HttpHeaders({ Authorization });
    return this.http.get<Course[]>(this.baseUrl + 'my', { headers });
  }

  public deleteCourse(
    Authorization: string,
    courseId: number
  ): Observable<any> {
    const headers = new HttpHeaders({ Authorization });
    return this.http.delete<any>(
      this.baseUrlManagement + 'delete?courseId=' + courseId,
      {
        headers,
      }
    );
  }
}
