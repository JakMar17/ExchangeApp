import { Pipe, PipeTransform } from '@angular/core';
import { User } from 'src/app/models/user-model';

@Pipe({
  name: 'userToString',
})
export class UserToStringPipe implements PipeTransform {
  transform(user: User | null): string {
    if (user == null) return '';
    return user.name + ' ' + user.surname;
  }
}
