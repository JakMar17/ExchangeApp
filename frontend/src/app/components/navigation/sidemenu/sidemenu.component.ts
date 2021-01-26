import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NavigationGlobalService } from 'src/app/services/navigation-global/navigation-global.service';

@Component({
  selector: 'app-sidemenu',
  templateUrl: './sidemenu.component.html',
  styleUrls: ['./sidemenu.component.scss']
})
export class SidemenuComponent implements OnInit {

  constructor(public globalNavigationService: NavigationGlobalService, private router: Router) { }

  ngOnInit(): void {
  }

  /**
   * navigates to page for creating new class
   */
  public onAddNewClassButtonClick(): void {
    this.router.navigate(['/class/add']);
  }
}
