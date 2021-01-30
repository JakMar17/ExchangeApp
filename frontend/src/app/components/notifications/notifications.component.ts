import { Component, Input, OnInit } from '@angular/core';
import { Notification } from './models/notification-interface';

@Component({
  selector: 'app-notifications',
  templateUrl: './notifications.component.html',
  styleUrls: ['./notifications.component.scss'],
})
export class NotificationsComponent implements OnInit {
  @Input() boxTitle = 'Obvestila';
  @Input() notifications: Notification[] | null = null;
  @Input() hasRightToEdit = false;

  public addingNotification = false;

  public newNotificationTitle = '';
  public newNotificationBody = '';

  constructor() {}

  ngOnInit(): void {}

  public openNewNotificationDialog(): void {
    this.addingNotification = true;
    this.cleanInputFields();
  }

  public closeNewNotificationDialog(): void {
    this.addingNotification = false;
    this.cleanInputFields();
  }

  public addNewNotification(): void {
    if (this.notifications == null) {
      this.notifications = [];
    }
    this.notifications.push({
      author: 'Karleto Špacapan',
      dateCreated: new Date(),
      body: this.newNotificationBody,
      title: this.newNotificationTitle,
    });
    this.closeNewNotificationDialog();
  }

  /**
   * TODO: create service that deletes notification
   * creates new array of notifications without given notification and deletes given notification
   * @param notification notification to be deleted
   */
  public deleteNotification(notification: Notification): void {
    if (this.notifications) {
      this.notifications = this.notifications.filter((e) => e != notification);
    }
  }

  private cleanInputFields(): void {
    this.newNotificationBody = '';
    this.newNotificationTitle = '';
  }
}
