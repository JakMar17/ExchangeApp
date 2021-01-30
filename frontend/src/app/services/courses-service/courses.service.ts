import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CoursesDammyData } from 'src/app/aaa_dummy-data/dummy-courses';
import { Course } from 'src/app/models/class-model';
import { User } from 'src/app/models/user-model';

@Injectable({
  providedIn: 'root'
})
export class CoursesService {

  private dummy: CoursesDammyData;

  constructor() {
    this.dummy = new CoursesDammyData();
  }

  public getAllCourses(): Observable<Course[]> {
    const dummy = new CoursesDammyData();
    return dummy.getAllCourses();
  }

  public getCourse(courseId: number): Observable<Course>{
    const dummy = new CoursesDammyData();
    return dummy.getCourseById(courseId);
  }

  public checkCoursePassword(courseId: number, insertedPassword: string, user: User): Observable<Course> {
    console.log(courseId, insertedPassword, user);
    return this.dummy.checkPassword(courseId, insertedPassword, user);
  }
}
