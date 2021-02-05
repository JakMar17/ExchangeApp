import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CoursesDammyData } from 'src/app/aaa_dummy-data/dummy-courses';
import { CourseApiService } from 'src/app/api/course-api/course-api.service';
import { Course } from 'src/app/models/class-model';
import { User } from 'src/app/models/user-model';

@Injectable({
  providedIn: 'root'
})
export class CoursesService {

  private dummy: CoursesDammyData;

  constructor(private courseApi: CourseApiService) {
    this.dummy = new CoursesDammyData();
  }

  public getAllCourses(): Observable<Course[]> {
    return this.courseApi.getAll();
  }

  public getCourse(courseId: number): Observable<Course>{
    const dummy = new CoursesDammyData();
    return dummy.getCourseById(courseId);
  }

  public checkCoursePassword(courseId: number, insertedPassword: string, user: User): Observable<Course> {
    return this.dummy.checkPassword(courseId, insertedPassword, user);
  }
}
