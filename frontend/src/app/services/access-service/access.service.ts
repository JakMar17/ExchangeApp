import { Injectable } from '@angular/core';
import { Course, CourseAccess } from 'src/app/models/class-model';
import { User, UserType } from 'src/app/models/user-model';

@Injectable({
  providedIn: 'root',
})
export class AccessService {
  constructor() {}

  public adminAccess(user: User): boolean {
    return user.globalUserType === UserType.ADMIN;
  }

  public minimumGlobalProffesorAccess(user: User): boolean {
    return user.globalUserType === UserType.PROFFESOR || this.adminAccess(user);
  }

  public hasEditRightsOnCourse(user: User, course: Course): boolean {
    if (user.globalUserType == UserType.ADMIN) return true;
    if (user.email == course.guardianMain.email) return true;
    if (course.guardians?.some((e) => e.email == user.email)) return true;

    return false;
  }

  public hasAccessToCourse(user: User, course: Course): boolean {
    if (this.hasEditRightsOnCourse(user, course)) return true;
    else if (course.studentBlacklist?.some((e) => e.email == user.email))
      return false;
    else if (course.students?.some((e) => e.email == user.email)) return true;
    else if (course.studentWhitelist?.some((e) => e.email == user.email))
      return true;
    else if (course.accessLevel == CourseAccess.PUBLIC) return true;
    else return false;
  }
}
