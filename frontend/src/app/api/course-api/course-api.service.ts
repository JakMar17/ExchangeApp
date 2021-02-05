import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Course } from 'src/app/models/class-model';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CourseApiService {

  private baseUrl = environment.BASE_API_URL + 'courses/';

  constructor(private http: HttpClient) {}

  public getAll(): Observable<Course[]> {
    return this.http.get<Course[]>(this.baseUrl + 'all');
  }
}
