import { Component, Input, OnInit } from '@angular/core';
import { NotificationInterface } from './models/notification-interface';

@Component({
  selector: 'app-notifications',
  templateUrl: './notifications.component.html',
  styleUrls: ['./notifications.component.scss'],
})
export class NotificationsComponent implements OnInit {
  @Input() boxTitle: string = 'Obvestila';
  @Input() notifications: NotificationInterface[] = [
    {
      title: 'Testni sistem',
      body:
        'Trenutno se nahajate na testnem sistemu. Zaradi velikega števila mogočih plagiatorstev, ki jih je zasledil avtomatski sistem za plagiatorstvo, bomo do nadaljnega vse oddaje ročno preverjali, plagiatorje pa brezmilostno kaznovali.',
      author: 'Karleto Špacapan',
      dateCreated: new Date(),
    },
  ];
  @Input() hasRightToEdit: boolean = false;

  public addingNotification: boolean = false;

  public newNotificationTitle: string = '';
  public newNotificationBody: string = '';

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
  public deleteNotification(notification: NotificationInterface): void {
    this.notifications = this.notifications.filter((e) => e != notification);
  }

  private cleanInputFields(): void {
    this.newNotificationBody = '';
    this.newNotificationTitle = '';
  }
}
