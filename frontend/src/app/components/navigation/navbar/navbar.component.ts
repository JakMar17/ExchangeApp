import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { User } from 'src/app/models/user-model';
import { NavigationGlobalService } from 'src/app/services/navigation-global/navigation-global.service';
import { UserServiceService } from 'src/app/services/user-service/user-service.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
})
export class NavbarComponent implements OnInit {
  public user: User | null = null;

  constructor(
    private navigationGlobalService: NavigationGlobalService,
    private router: Router,
    private userService: UserServiceService
  ) {
    const u = userService.userLoggedIn;
    if (u != null) this.user = u;
    else router.navigate(['/']);
  }

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
    this.router.navigate(['/']);
  }

  /**
   * navigates to dashboard
   */
  public onLogoClick(): void {
    this.router.navigate(['/dashboard']);
  }
}
