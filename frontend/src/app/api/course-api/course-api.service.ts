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

  public getAll(): Observable<Course[]> {
    return this.http.get<Course[]>(this.baseUrl + 'all');
  }

  public getCourse(
    courseId: number,
    personalNumber: string
  ): Observable<Course> {
    const headers = new HttpHeaders({ 'Personal-Number': personalNumber });
    return this.http.get<Course>(this.baseUrl + 'course?courseId=' + courseId, {
      headers,
    });
  }

  public checkPasswordAndGetCourse(
    courseId: number,
    personalNumber: string,
    password: string
  ): Observable<Course> {
    const headers = new HttpHeaders({
      'Personal-Number': personalNumber,
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
    personalNumber: string
  ): Observable<Course> {
    const headers = new HttpHeaders({ 'Personal-Number': personalNumber });
    return this.http.get<Course>(this.baseUrlManagement + 'detailed?courseId=' + courseId, { headers });
  }

  public saveCourse(
    course: Course,
    personalNumber: string
  ): Observable<Course> {
    const headers = new HttpHeaders({ 'Personal-Number': personalNumber });
    return this.http.post<Course>(this.baseUrlManagement, course,  { headers });
  }
}
