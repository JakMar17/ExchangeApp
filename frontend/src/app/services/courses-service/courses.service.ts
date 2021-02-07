import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CoursesDammyData } from 'src/app/aaa_dummy-data/dummy-courses';
import { CourseApiService } from 'src/app/api/course-api/course-api.service';
import { Course } from 'src/app/models/class-model';
import { User } from 'src/app/models/user-model';
import { UserServiceService } from '../user-service/user-service.service';

@Injectable({
  providedIn: 'root',
})
export class CoursesService {
  constructor(
    private courseApi: CourseApiService,
    private userService: UserServiceService
  ) {}

  public getAllCourses(): Observable<Course[]> {
    return this.courseApi.getAll();
  }

  public getCourse(courseId: number): Observable<Course> {
    return this.courseApi.getCourse(
      courseId,
      this.userService.userLoggedIn.personalNumber
    );
  }

  public checkCoursePassword(
    courseId: number,
    insertedPassword: string,
    user: User
  ): Observable<Course> {
    return this.courseApi.checkPasswordAndGetCourse(
      courseId,
      user.personalNumber,
      insertedPassword
    );
  }

  public saveCourse(
    course: Course,
    personalNumber: string
  ): Observable<Course> {
    return this.courseApi.saveCourse(course, personalNumber);
  }
}
