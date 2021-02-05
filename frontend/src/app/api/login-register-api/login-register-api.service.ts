import { HttpClient, HttpHeaders } from '@angular/common/http';
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
  private baseUrl = environment.BASE_API_URL + 'user/';
  private httpJsonHeader: HttpHeaders = new HttpHeaders({ 'Content-Type': 'application/json' });

  constructor(private http: HttpClient) {}

  public login(loginModel: LoginRequestModel): Observable<User> {
    return this.http.post<User>(this.baseUrl + 'login', loginModel);
  }

  public register(registerModel: RegisterRequestModel): Observable<boolean> {
    return this.http.post<boolean>(this.baseUrl + 'register', registerModel);
  }
}
