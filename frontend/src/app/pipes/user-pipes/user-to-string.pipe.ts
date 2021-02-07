import { Pipe, PipeTransform } from '@angular/core';
import { User } from 'src/app/models/user-model';

@Pipe({
  name: 'userToString',
})
export class UserToStringPipe implements PipeTransform {
  transform(user: User | null): string {
    if (user == null) return '';

    const name = user.name == null ? '' : user.name;
    const surname = user.surname == null ? '' : user.surname;

    return name + ' ' + surname;
  }
}
