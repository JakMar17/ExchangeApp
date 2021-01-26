import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class NavigationGlobalService {

  private _showSideBar: boolean = true;

  constructor() { }

  public get showSideBar(): boolean {
    return this._showSideBar;
  }

  public set showSideBar(_showSideBar: boolean) {
    this._showSideBar = _showSideBar;
  }
}
