import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from 'src/app/models/user-model';
import { environment } from 'src/environments/environment';
import { LoginRequestModel } from './models/login-request-model';

@Injectable({
  providedIn: 'root',
})
export class LoginRegisterApiService {
  private baseUrl = environment.BASE_API_URL + 'user/';

  constructor(private http: HttpClient) {}

  public login(loginModel: LoginRequestModel): Observable<User> {
    return this.http.post<User>(this.baseUrl + 'login', loginModel);
  }
}
