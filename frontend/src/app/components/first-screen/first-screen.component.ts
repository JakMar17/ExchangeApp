import { Component, OnInit } from '@angular/core';
import { LoginPanelEnum } from './models/login-panel-enum';

@Component({
  selector: 'app-first-screen',
  templateUrl: './first-screen.component.html',
  styleUrls: ['./first-screen.component.scss']
})
export class FirstScreenComponent implements OnInit {

  public loginPanel: LoginPanelEnum = LoginPanelEnum.LOGIN;

  constructor() { }

  ngOnInit(): void {
  }

  /**
   * enum selector for html template
   */
  public get loginPanelEnum(): typeof LoginPanelEnum {
    return LoginPanelEnum;
  }

  /**
   * TODO: write comment
   * @param $event 
   */
  public onRegisterButtonClick($event: any): void {
    this.loginPanel = $event;
  }

}
