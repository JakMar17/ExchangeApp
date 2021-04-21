import { Injectable, OnDestroy } from '@angular/core';
import { interval, Observable, of, Subscription } from 'rxjs';
import { LoginRegisterApiService } from 'src/app/api/login-register-api/login-register-api.service';
import { User } from 'src/app/models/user-model';
import { JwtHelperService } from '@auth0/angular-jwt';
import {
  ActivatedRouteSnapshot,
  CanActivate,
  Router,
  RouterStateSnapshot,
  UrlTree,
} from '@angular/router';
import Swal from 'sweetalert2';
import { Styles } from 'src/styles';

@Injectable({
  providedIn: 'root',
})
export class UserServiceService implements OnDestroy, CanActivate {
  public userLoggedIn: User | null = null;
  private _bearer: string | null = null;
  private _refreshTokenInterval: number | null = null;

  private refreshTokenSubscription: Subscription;

  private static authorizationHeader: string = 'Authorization';
  private static refreshTimeHeader: string = 'Refresh-Token-After';

  constructor(
    private router: Router,
    private loginRegisterApi: LoginRegisterApiService,
    private jwtHelper: JwtHelperService
  ) {}

  public async loginUserIn(email: string, password: string): Promise<User> {
    const response = await this.loginRegisterApi
      .login({ email, password })
      .toPromise()
      .catch((err) => {
        throw err.error?.message ?? 'Prijava ni bila uspešna';
      });

    this._bearer = response.headers.get(UserServiceService.authorizationHeader);
    this._refreshTokenInterval = +response.headers.get(
      UserServiceService.refreshTimeHeader
    );

    this.userLoggedIn = await this.loginRegisterApi
      .getUserData(this._bearer)
      .toPromise()
      .catch(() => {
        throw 'Napaka pri pridobivanju podatkov uporabnika';
      });

    this.createTokenRefresher();
    return response.body;
  }

  public logUserOff(): void {
    this.userLoggedIn = null;
    this._bearer = null;
    this._refreshTokenInterval = null;
    this.refreshTokenSubscription.unsubscribe();
  }

  public get bearer() {
    return this._bearer;
  }

  private createTokenRefresher() {
    const inter = interval(this._refreshTokenInterval);
    this.refreshTokenSubscription = inter.subscribe(() => this.refreshToken());
  }

  private async refreshToken(): Promise<void> {
    const response = await this.loginRegisterApi
      .refreshToken(this._bearer)
      .toPromise()
      .catch(() => {
        throw 'Napaka pri posodabljanju ';
      });

    this._bearer = response.headers.get(UserServiceService.authorizationHeader);
  }

  private isJwtExpired(): boolean {
    const jwt = this._bearer.replace('Bearer ', '');
    return this.jwtHelper.isTokenExpired(jwt);
  }

  public canActivate(): boolean {
    if (this._bearer == null) {
      this.router.navigate(['']);
      return false;
    } else if (this.isJwtExpired()) {
      Swal.fire({
        title: 'Seja je potekla',
        text: 'Potrebna je ponovna prijava',
        icon: 'warning',
        confirmButtonColor: Styles.info,
      });
      this.router.navigate(['']);
      return false;
    } else {
      return true;
    }
  }

  public resetUsersPassword(
    oldPassword: string,
    newPassword: string
  ): Observable<any> {
    const formdata = new FormData();
    formdata.append('oldPassword', oldPassword);
    formdata.append('newPassword', newPassword);
    return this.loginRegisterApi.updatePassword(this.bearer, formdata);
  }

  ngOnDestroy(): void {
    this.logUserOff();
  }
}
