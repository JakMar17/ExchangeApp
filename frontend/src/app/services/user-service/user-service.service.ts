import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { UserDummyLogin } from 'src/app/aaa_dummy-data/dummy-users';
import { LoginRegisterApiService } from 'src/app/api/login-register-api/login-register-api.service';
import { User } from 'src/app/models/user-model';

@Injectable({
  providedIn: 'root',
})
export class UserServiceService {
  public userLoggedIn: User | null = null;

  constructor(private loginRegisterApi: LoginRegisterApiService) {
  }

  public loginUserIn(email: string, password: string): Observable<User> {
    return this.loginRegisterApi.login({ email, password });
  }
}
