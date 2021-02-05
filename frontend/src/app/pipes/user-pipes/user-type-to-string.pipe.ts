import { Pipe, PipeTransform } from '@angular/core';
import { User, UserType } from 'src/app/models/user-model';

@Pipe({
  name: 'userTypeToString',
})
export class UserTypeToStringPipe implements PipeTransform {
  transform(user: User | null): string {
    if (user == null) return '';

    switch (user.userType) {
      case 'ADMIN':
        return 'administrator sistema';
      case 'PROFESSOR':
        return 'pedagoški delavec';
      default:
        return 'študent';
    }
  }
}
