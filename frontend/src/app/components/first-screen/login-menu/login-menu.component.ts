import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { Router } from '@angular/router';
import { UserServiceService } from 'src/app/services/user-service/user-service.service';
import { LoginPanelEnum } from '../models/login-panel-enum';

@Component({
  selector: 'app-login-menu',
  templateUrl: './login-menu.component.html',
  styleUrls: ['./login-menu.component.scss'],
})
export class LoginMenuComponent implements OnInit {
  @Output()
  registerButtonEvent: EventEmitter<LoginPanelEnum> = new EventEmitter<LoginPanelEnum>();
  public email: string = 'karleto.spacapan';
  public password: string = 'geslo';

  public userLoginError: string | null = null;

  constructor(
    private router: Router,
    private userService: UserServiceService
  ) {}

  ngOnInit(): void {}

  /**
   * template selector for enum
   */
  public get loginPanelEnum(): typeof LoginPanelEnum {
    return LoginPanelEnum;
  }

  /**
   * switch template view to registarttion page
   * @param switchTo defines which type of template should be shown (student, proffesor or other)
   */
  public onRegisterButtonClick(switchTo: LoginPanelEnum): void {
    this.registerButtonEvent.emit(switchTo);
  }

  /**
   * TODO: create service that checks login informations
   * validates user credentions, login user into system and redirect user to dashboard
   */
  public onLoginButtonPressed(): void {
    this.userLoginError = null;
    this.userService.loginUserIn(this.email, this.password).subscribe(
      (user) => {
        this.userService.userLoggedIn = user;
        this.router.navigate(['/dashboard']);
      },
      (err: HttpErrorResponse) => {
        (this.userLoginError = err.error.message);
        console.error(err);
      }
    );
  }
}
