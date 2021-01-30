import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { UserDummyLogin } from 'src/app/aaa_dummy-data/dummy-users';
import { User } from 'src/app/models/user-model';

@Injectable({
  providedIn: 'root',
})
export class UserServiceService {
  public userLoggedIn: User | null = null;

  constructor() {
    // TODO odstrani kasneje
    this.loginUserIn('karleto', 'karleto').subscribe(
      (user) => (this.userLoggedIn = user)
    );
  }

  public loginUserIn(email: string, password: string): Observable<User> {
    const userDummy: UserDummyLogin = new UserDummyLogin();
    return userDummy.getUser(email, password);
  }
}
