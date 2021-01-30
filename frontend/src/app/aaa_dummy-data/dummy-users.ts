import { Observable, of, throwError } from 'rxjs';
import { User, UserType } from '../models/user-model';

const users: User[] = [
  {
    email: 'karleto',
    name: 'Karleto',
    surname: 'Špacapan',
    globalUserType: UserType.ADMIN,
    password: 'karleto',
  },
  {
    email: 'student',
    name: 'Štefana',
    surname: 'Špacapan',
    globalUserType: UserType.STUDENT,
    password: 'student',
  },
  {
    email: 'student1',
    name: 'Bertolin',
    surname: 'Špacapan',
    globalUserType: UserType.STUDENT,
    password: 'student',
  },
  {
    email: 'tilka',
    globalUserType: UserType.PROFFESOR,
    name: 'Tilka',
    surname: 'Matilka',
    password: 'matilka',
  },
];

export class UserDummyLogin {
  public getUser(email: string, password: string): Observable<User> {
    console.log(email, password);
    const user =
      users.find((e) => e.email == email && e.password == password) ?? null;

    if (user == null) return throwError('ni uporabnika');
    else return of(user);
  }
}
