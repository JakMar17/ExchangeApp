import { Injectable, OnDestroy } from '@angular/core';
import { interval, Observable, of, Subscription } from 'rxjs';
import { LoginRegisterApiService } from 'src/app/api/login-register-api/login-register-api.service';
import { User } from 'src/app/models/user-model';

@Injectable({
  providedIn: 'root',
})
export class UserServiceService implements OnDestroy {
  public userLoggedIn: User | null = null;
  private _bearer: string | null = null;
  private _refreshTokenInterval: number | null = null;

  private refreshTokenSubscription: Subscription;

  private static authorizationHeader: string = 'Authorization';
  private static refreshTimeHeader: string = 'Refresh-Token-After';

  constructor(private loginRegisterApi: LoginRegisterApiService) {}

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

    console.log(this.userLoggedIn);

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

  private refreshToken(): void {
    console.log('Refreshing token');
  }

  ngOnDestroy(): void {
    this.logUserOff();
  }
}
