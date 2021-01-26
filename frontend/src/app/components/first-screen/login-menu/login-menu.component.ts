import { Component, OnInit, Output, EventEmitter  } from '@angular/core';
import { Router } from '@angular/router';
import { LoginPanelEnum } from '../models/login-panel-enum';

@Component({
  selector: 'app-login-menu',
  templateUrl: './login-menu.component.html',
  styleUrls: ['./login-menu.component.scss']
})
export class LoginMenuComponent implements OnInit {

  @Output() registerButtonEvent: EventEmitter<LoginPanelEnum> = new EventEmitter<LoginPanelEnum>();
  public email: string = "";
  public password: string = "";

  constructor(private router: Router) { }

  ngOnInit(): void {
  }

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
    console.log(switchTo);
    this.registerButtonEvent.emit(switchTo);
  }

  /**
   * TODO: create service that checks login informations
   * validates user credentions, login user into system and redirect user to dashboard
   */
  public onLoginButtonPressed(): void {
    //check login credentions
    this.router.navigate(['/dashboard']);
  }
}
