import { Component, OnInit } from '@angular/core';
import { NavigationGlobalService } from 'src/app/services/navigation-global/navigation-global.service';
import { NotificationInterface } from '../notifications/models/notification-interface';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
})
export class DashboardComponent implements OnInit {
  public notifications: NotificationInterface[] = [
    {
      title: 'Testni sistem',
      body: 'Trenutno se nahajate na testnem sistemu.',
      author: 'Karleto Špacapan',
      dateCreated: new Date(),
    },
  ];

  constructor(public globalNavigationService: NavigationGlobalService) {}

  ngOnInit(): void {}
}
