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
  public email: string = '';
  public password: string = '';

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
    this.userService
      .loginUserIn(this.email, this.password)
      .then((user) => {
        this.router.navigate(['/dashboard']);
      })
      .catch((err) => {
        this.userLoginError = err;
        console.error(err);
      });
  }

  public onPasswordResetButtonClick(): void {
    this.registerButtonEvent.emit(LoginPanelEnum.PASSWORD_RESET);
  }
}
