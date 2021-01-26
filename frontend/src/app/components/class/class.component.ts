import { Component, OnInit } from '@angular/core';
import { ClassViewEnum } from './models/class-view-enum';

@Component({
  selector: 'app-class',
  templateUrl: './class.component.html',
  styleUrls: ['./class.component.scss'],
})
export class ClassComponent implements OnInit {
  public classView: ClassViewEnum = ClassViewEnum.CLASS_UNLOCKED;

  public classPasswordInput: string = '';
  public showClassPasswordError: boolean = false;

  constructor() {}

  ngOnInit(): void {}

  public get classViewEnum(): typeof ClassViewEnum {
    return ClassViewEnum;
  }

  /**
   * TODO create service that checks if password is OK
   * checks if input password is OK and unlock class
   */
  public onPasswordInputClick(): void {
    console.log(this.classPasswordInput);
    if (this.classPasswordInput == 'geslo')
      this.classView = ClassViewEnum.CLASS_UNLOCKED;
    else this.showClassPasswordError = true;
    this.classPasswordInput = '';
  }
}
