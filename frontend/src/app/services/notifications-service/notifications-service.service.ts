import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { NotificationsApiService } from 'src/app/api/notifications-api/notifications-api.service';
import { Notification } from 'src/app/components/notifications/models/notification-interface';
import { Course } from 'src/app/models/class-model';
import { UserServiceService } from '../user-service/user-service.service';

@Injectable({
  providedIn: 'root',
})
export class NotificationsServiceService {
  constructor(
    private userService: UserServiceService,
    private notificationApi: NotificationsApiService
  ) {}

  public saveNotification(
    notification: Notification,
    course: Course | null
  ): Observable<Notification> {
    console.log(course);
    return this.notificationApi.saveNotification(
      notification,
      this.userService.userLoggedIn.personalNumber ?? '',
      course == null ? null : course.courseId
    );
  }

  public deleteNotification(
    notification: Notification,
    course: Course | null
  ): Observable<any> {
    console.log(notification, course);
    return this.notificationApi.deleteNotification(
      notification.notificationId,
      this.userService.userLoggedIn.personalNumber,
      course == null ? null : course.courseId
    );
  }
}
