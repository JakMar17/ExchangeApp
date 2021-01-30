import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { User } from 'src/app/models/user-model';
import { AccessService } from 'src/app/services/access-service/access.service';
import { NavigationGlobalService } from 'src/app/services/navigation-global/navigation-global.service';
import { UserServiceService } from 'src/app/services/user-service/user-service.service';

@Component({
  selector: 'app-sidemenu',
  templateUrl: './sidemenu.component.html',
  styleUrls: ['./sidemenu.component.scss'],
})
export class SidemenuComponent implements OnInit {
  public user: User | null;

  public showClassManagmentFields: boolean = false;

  constructor(
    public globalNavigationService: NavigationGlobalService,
    private router: Router,
    private userService: UserServiceService,
    private accessService: AccessService
  ) {
    this.user = userService.userLoggedIn;
  }

  ngOnInit(): void {
    this.setAccesFields();
  }

  /**
   * navigates to page for creating new class
   */
  public onAddNewClassButtonClick(): void {
    this.router.navigate(['/course/add']);
  }

  private setAccesFields(): void {
    if (this.user == null) return;

    this.showClassManagmentFields = this.accessService.minimumGlobalProffesorAccess(
      this.user
    );
  }
}
