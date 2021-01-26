import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NavigationGlobalService } from 'src/app/services/navigation-global/navigation-global.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
})
export class NavbarComponent implements OnInit {
  constructor(private navigationGlobalService: NavigationGlobalService, private router: Router) {}

  ngOnInit(): void {}

  /**
   * open or close side menu
   */
  public onMenuBarsButtonClick(): void {
    this.navigationGlobalService.showSideBar = !this.navigationGlobalService
      .showSideBar;
  }

  /**
   * TODO: create service that logs off user and deletes cookies
   * logs off user
   */
  public onLogOffButtonClick(): void {
    //logoff user
    this.router.navigate(["/"]);
  }

  /**
   * navigates to dashboard
   */
  public onLogoClick(): void {
    this.router.navigate(["/dashboard"]);
  }
}
