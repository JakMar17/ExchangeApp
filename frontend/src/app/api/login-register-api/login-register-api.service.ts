import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from 'src/app/models/user-model';
import { environment } from 'src/environments/environment';
import { LoginRequestModel } from './models/login-request-model';
import { RegisterRequestModel } from './models/register-request-model';

@Injectable({
  providedIn: 'root',
})
export class LoginRegisterApiService {
  private baseUrl = environment.BASE_API_URL;

  constructor(private http: HttpClient) {}

  public login(loginModel: LoginRequestModel): Observable<HttpResponse<any>> {
    console.log(this.baseUrl);
    return this.http.post<any>(this.baseUrl + 'login', loginModel, {
      observe: 'response',
    });
  }

  public getUserData(Authorization: string) {
    const headers = {Authorization};
    return this.http.get<User>(this.baseUrl + 'user', {headers});
  }

  public register(registerModel: RegisterRequestModel): Observable<boolean> {
    return this.http.post<boolean>(
      this.baseUrl + 'user/register',
      registerModel
    );
  }

  public resetPassword(email: string): Observable<any> {
    const headers = { email };
    return this.http.get<any>(this.baseUrl + 'user/reset', { headers });
  }
}
