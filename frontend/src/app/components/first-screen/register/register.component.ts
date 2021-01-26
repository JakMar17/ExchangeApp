import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { LoginPanelEnum } from '../models/login-panel-enum';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {

  @Input() typeOfRegistration: LoginPanelEnum | null = null;
  @Output() backButtonEvent: EventEmitter<LoginPanelEnum> = new EventEmitter<LoginPanelEnum>();
  public emailDomain: string | null = null;

  public typeOfOtherRegistration: LoginPanelEnum | null = null;

  constructor() { }

  ngOnInit(): void {
    this.selectRegistrationFormOnInit();
  }

  /**
   * create view depending on type of registration
   */
  private selectRegistrationFormOnInit() {
    switch(this.typeOfRegistration) {
      case LoginPanelEnum.REGISTER_STUDENT:
        this.emailDomain = '@student.uni-lj.si';
        break;
      case LoginPanelEnum.REGISTER_PROF:
        this.emailDomain = '@fri1.uni-lj.si';
        break;
      default:
        this.setOtherRegistrationType(LoginPanelEnum.REGISTER_PROF);
    }
  }

  /**
   * enum selector for html template
   */
  public get loginPanelEnum(): typeof LoginPanelEnum {
    return LoginPanelEnum;
  }

  /**
   * redirects user back to login view
   */
  public onBackButtonClick(): void {
    this.backButtonEvent.emit(LoginPanelEnum.LOGIN);
  }

  /**
   * switch view to other type of registration form
   * @param switchTo which register view to switch to
   */
  public setOtherRegistrationType(switchTo: LoginPanelEnum): void {
    this.typeOfOtherRegistration = switchTo;
  }

}
