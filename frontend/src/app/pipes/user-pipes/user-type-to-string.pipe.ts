import { Pipe, PipeTransform } from '@angular/core';
import { User, UserType } from 'src/app/models/user-model';

@Pipe({
  name: 'userTypeToString',
})
export class UserTypeToStringPipe implements PipeTransform {
  transform(user: User | null): string {
    if (user == null) return '';

    switch (user.globalUserType) {
      case UserType.ADMIN:
        return 'administrator sistema';
      case UserType.PROFFESOR:
        return 'pedagoški delavec';
      case UserType.STUDENT:
        return 'študent';
      default:
        return '';
    }
  }
}
