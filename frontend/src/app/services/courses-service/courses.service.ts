import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CourseApiService } from 'src/app/api/course-api/course-api.service';
import { NotificationsApiService } from 'src/app/api/notifications-api/notifications-api.service';
import { Notification } from 'src/app/components/notifications/models/notification-interface';
import { Course } from 'src/app/models/class-model';
import { User } from 'src/app/models/user-model';
import { UserServiceService } from '../user-service/user-service.service';

@Injectable({
  providedIn: 'root',
})
export class CoursesService {
  constructor(
    private courseApi: CourseApiService,
    private userService: UserServiceService,
    private notificationApi: NotificationsApiService
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

  public getMyCourses(): Observable<Course[]> {
    return this.courseApi.getUsersCourses(
      this.userService.userLoggedIn.personalNumber
    );
  }

  public getDashboardNotifications(): Observable<Notification[]> {
    return this.notificationApi.getDashoboardNotifications();
  }

  public deleteCourse(course: Course): Observable<any> {
    return this.courseApi.deleteCourse(
      this.userService.userLoggedIn.personalNumber,
      course.courseId
    );
  }
}
