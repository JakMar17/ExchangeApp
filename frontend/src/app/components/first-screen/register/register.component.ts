import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { LoginRegisterApiService } from 'src/app/api/login-register-api/login-register-api.service';
import { RegisterRequestModel } from 'src/app/api/login-register-api/models/register-request-model';
import { User } from 'src/app/models/user-model';
import { LoginPanelEnum } from '../models/login-panel-enum';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
})
export class RegisterComponent implements OnInit {
  @Input() typeOfRegistration: LoginPanelEnum | null = null;
  @Output()
  backButtonEvent: EventEmitter<LoginPanelEnum> = new EventEmitter<LoginPanelEnum>();
  public emailDomain: string | null = null;

  public typeOfOtherRegistration: LoginPanelEnum | null = null;

  public errorMessage: string | null = null;
  public nameInput: string = '';
  public surnameInput: string = '';
  public emailWithoutDomainInput: string = '';
  public passwordInput: string = '';
  public passwordRepeatInput: string = '';
  public studentNumberInput: string = '';
  public description: string = '';

  public registrationInProgress: boolean = false;

  constructor(private loginRegisterApi: LoginRegisterApiService) {}

  ngOnInit(): void {
    this.selectRegistrationFormOnInit();
  }

  /**
   * create view depending on type of registration
   */
  private selectRegistrationFormOnInit(): void {
    switch (this.typeOfRegistration) {
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

  public onRegisterButtonPressed(): void {
    if (!this.checkInputedData()) return;
    const registerModel: RegisterRequestModel = {
      email:
        this.typeOfRegistration != LoginPanelEnum.REGISTER_OTHER
          ? this.emailWithoutDomainInput + this.emailDomain
          : this.emailWithoutDomainInput,
      name: this.nameInput,
      surname: this.surnameInput,
      password: this.passwordInput,
      studentNumber:
        this.studentNumberInput.length == 8 ? this.studentNumberInput : null,
    };

    this.registrationInProgress = true;
    this.loginRegisterApi.register(registerModel).subscribe(
      (data) => {
        alert(
          'Registracija je bila uspešna, na vnešeni epoštni naslov je bil poslan potrditveni email'
        );
        this.registrationInProgress = false;
        this.onBackButtonClick();
      },
      (err) => (this.errorMessage = err.error.message)
    );
  }

  private checkInputedData(): boolean {
    this.errorMessage = null;
    if (this.nameInput.length === 0)
      this.errorMessage = 'Ime mora biti izpolnjeno';
    else if (this.surnameInput.length === 0)
      this.errorMessage = 'Priimek mora biti izpolnjen';
    else if (this.emailWithoutDomainInput.length === 0)
      this.errorMessage = 'Epošta mora biti izpolnjena';
    else if (
      this.passwordInput.length === 0 ||
      this.passwordRepeatInput.length === 0
    )
      this.errorMessage = 'Geslo ne sme biti prazno';
    else if (this.passwordRepeatInput !== this.passwordInput)
      this.errorMessage = 'Gesli se ne ujemata';
    else if (
      this.typeOfRegistration === LoginPanelEnum.REGISTER_OTHER &&
      this.typeOfOtherRegistration === LoginPanelEnum.REGISTER_STUDENT &&
      this.studentNumberInput.length !== 8
    )
      this.errorMessage = 'Vpisna številka mora biti izpolnjena';
    else if (
      this.typeOfRegistration === LoginPanelEnum.REGISTER_STUDENT &&
      this.studentNumberInput.length !== 8
    )
      this.errorMessage = 'Vpisna številka mora biti izpolnjena';

    return this.errorMessage == null;
  }
}
