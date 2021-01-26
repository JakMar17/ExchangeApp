import { Component, OnInit } from '@angular/core';
import { NavigationGlobalService } from 'src/app/services/navigation-global/navigation-global.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {

  constructor(public globalNavigationService: NavigationGlobalService) { }

  ngOnInit(): void {
  }

}
