import { Component, Input, OnInit } from '@angular/core';
import { Course } from 'src/app/models/class-model';
import { NotificationsServiceService } from 'src/app/services/notifications-service/notifications-service.service';
import { UserServiceService } from 'src/app/services/user-service/user-service.service';
import { Notification } from './models/notification-interface';

@Component({
  selector: 'app-notifications',
  templateUrl: './notifications.component.html',
  styleUrls: ['./notifications.component.scss'],
})
export class NotificationsComponent implements OnInit {
  @Input() boxTitle = 'Obvestila';
  @Input() notifications: Notification[] | null = null;
  @Input() course: Course | null = null;
  @Input() hasRightToEdit = false;

  public addingNotification = false;

  public newNotificationTitle = '';
  public newNotificationBody = '';

  constructor(
    private userService: UserServiceService,
    private notificationService: NotificationsServiceService
  ) {}

  ngOnInit(): void {
    console.log("course", this.course);
  }

  public openNewNotificationDialog(): void {
    this.addingNotification = true;
    this.cleanInputFields();
  }

  public closeNewNotificationDialog(): void {
    this.addingNotification = false;
    this.cleanInputFields();
  }

  public addNewNotification(): void {
    const notification = {
      author: this.userService.userLoggedIn,
      created: new Date(),
      body: this.newNotificationBody,
      title: this.newNotificationTitle,
    };

    if (this.notifications == null) {
      this.notifications = [];
    }

    this.notificationService
      .saveNotification(notification, this.course)
      .subscribe(
        (data) => {
          this.notifications.push(data);
          this.closeNewNotificationDialog();
        },
        (err) => console.error(err)
      );
  }

  /**
   * TODO: create service that deletes notification
   * creates new array of notifications without given notification and deletes given notification
   * @param notification notification to be deleted
   */
  public deleteNotification(notification: Notification): void {
    if (this.notifications) {
      this.notificationService
        .deleteNotification(notification, this.course)
        .subscribe(
          () =>
            (this.notifications = this.notifications.filter(
              (e) => e !== notification
            )),
          (err) => console.error(err)
        );
    }
  }

  private cleanInputFields(): void {
    this.newNotificationBody = '';
    this.newNotificationTitle = '';
  }
}
