import { HttpErrorResponse } from '@angular/common/http';
import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { LoginRegisterApiService } from 'src/app/api/login-register-api/login-register-api.service';
import { ExceptionWrapper } from 'src/app/models/error/http-response-error';
import { LoginPanelEnum } from '../models/login-panel-enum';

@Component({
  selector: 'app-password-reset',
  templateUrl: './password-reset.component.html',
  styleUrls: ['./password-reset.component.scss'],
})
export class PasswordResetComponent implements OnInit {
  @Output()
  backButtonEvent: EventEmitter<LoginPanelEnum> = new EventEmitter<LoginPanelEnum>();
  public email: string = '';
  public error: string | null = null;
  public processing: boolean = false;

  constructor(private userApi: LoginRegisterApiService) {}

  ngOnInit(): void {}

  public onBackButtonClick(): void {
    this.backButtonEvent.emit(LoginPanelEnum.LOGIN);
  }

  public onPasswordResetButtonPressed(): void {
    this.error = null;
    if (this.email.length == 0) {
      this.error = 'Epošta mora biti izpolnjena';
      return;
    }

    this.processing = true;
    this.userApi.resetPassword(this.email).subscribe(
      () => {
        alert(
          'Navodila za ponastavitev gesla so bila poslana na vnešeni epoštni naslov'
        );
        this.onBackButtonClick();
        this.processing = false;
      },
      (error: HttpErrorResponse) => {
        this.error = (error.error as ExceptionWrapper).body;
        this.processing = false;
      }
    );
  }
}
