import { Observable, of, throwError } from 'rxjs';
import {
  Assignment,
  AssignmentStatus,
  SubmissionCheck,
} from '../models/assignment-model';
import { Course, CourseAccess, CourseCategory } from '../models/class-model';
import { User, UserType } from '../models/user-model';

const courses: Course[] = [
  {
    accessLevel: CourseAccess.SECURED_PASSWORD,
    accessPassword: 'super geslo',
    courseId: 745,
    description:
      'To je opisek predmeta, ki je zaklenjen z zelo močnim geslom. Ob prvi prijavi ga mora vpisati vsak študent in pedagoški delavec, ki ni skrbnik (ali soskrbnik) predmeta. Geslo predmeta je super geslo.',
    guardianMain: {
      email: 'jakob.marusic',
      globalUserType: UserType.PROFFESOR,
      name: 'Jakob',
      surname: 'Marušič',
    },
    initialCoins: 4,
    title: 'Predmet zaklenjen z geslom',
    assignments: [
      {
        title: '1. domača naloga',
        assignmentId: 457,
        classromUrl: 'www.google.com',
        description: 'To je prva domača naloga pri predmetu',
        startDate: new Date(Date.parse('2021-01-01')),
        endDate: new Date(Date.parse('2021-04-01')),
        maxSubmissionsTotal: 30,
        coinsPerSubmission: 2,
        coinsPrice: 2,
        inputDataType: '.txt',
        outputDataType: '.txt',
        submissionCheck: SubmissionCheck.NONE,
        submissionNotify: false,
        status: AssignmentStatus.ACTIVE,
        visible: true,
        noOfSubmisions: 13,
      },
      {
        title: '2. domača naloga',
        assignmentId: 47,
        classromUrl: 'www.google.com',
        description: 'To je prva domača naloga pri predmetu',
        startDate: new Date(Date.parse('2021-02-01')),
        maxSubmissionsTotal: 30,
        coinsPerSubmission: 2,
        coinsPrice: 2,
        inputDataType: '.txt',
        outputDataType: '.txt',
        submissionCheck: SubmissionCheck.NONE,
        submissionNotify: false,
        status: AssignmentStatus.ACTIVE,
        visible: false,
        noOfSubmisions: 154,
      },
    ],
  },
  {
    accessLevel: CourseAccess.PUBLIC,
    courseId: 888,
    description: 'To je opisek predmeta do katerega lahko dostopa čisto vsak.',
    guardianMain: {
      email: 'tilka',
      globalUserType: UserType.PROFFESOR,
      name: 'Tilka',
      surname: 'Matilka',
    },
    initialCoins: 4,
    title: 'Javen predmet',
    notifications: [
      {
        author: 'Karleto Špacapan',
        dateCreated: new Date(),
        body: 'To je opozorilna pesem',
        title: 'Opozorilna pesem',
      },
    ],
    studentBlacklist: [
      {
        email: 'student1',
        name: 'Bertolin',
        surname: 'Špacapan',
        globalUserType: UserType.STUDENT,
      },
    ],
  },
  {
    accessLevel: CourseAccess.SECURED_WHITELIST,
    courseId: 1254,
    description:
      'To je opisek predmeta do katerega lahko dostopajo samo tisti, ki so na beli listi.',
    guardianMain: {
      email: 'karleto',
      name: 'Karleto',
      surname: 'Špacapan',
      globalUserType: UserType.ADMIN,
    },
    initialCoins: 4,
    title: 'Zaprt predmet',
    studentWhitelist: [
      {
        email: 'student',
        name: 'Štefana',
        surname: 'Špacapan',
        globalUserType: UserType.STUDENT,
        password: 'student',
      },
    ],
  },
];

export class CoursesDammyData {
  public getAllCourses(): Observable<Course[]> {
    console.log(courses);
    return of(courses);
  }

  public getCourseById(id: number): Observable<Course> {
    const course = courses.find((e) => e.courseId == id);
    console.log(id, course);
    if (course != null) return of(course);
    else return throwError('napaka');
  }

  public checkPassword(
    id: number,
    password: string,
    user: User
  ): Observable<Course> {
    const course = courses.find((e) => e.courseId == id);

    console.log(id, course);

    if (course == null) return throwError('404');
    else if (course.accessPassword == password) {
      if (course.students != null) course.students?.push(user);
      else course.students = [user];

      console.log('Ok');

      return of(course);
    } else return throwError('403');
  }

  public getAssignemtn(
    courseId: number,
    assignmentId: number
  ): Observable<Assignment> {
    const course = courses.find((e) => e.courseId == courseId);

    if (courseId == null) return throwError('ne najdem predmeta');

    const assignment = course?.assignments?.find(
      (e) => e.assignmentId == assignmentId
    );

    if (assignment != null) return of(assignment);

    return throwError('Ne najdem naloge');
  }

  saveAssignment(courseId: any, assignment: Assignment) {
    const course = courses.find((e) => e.courseId == courseId);

    if (course == null) return throwError('ne najdem predmeta');

    if (assignment.assignmentId == null) {
      if (course?.assignments == null) {
        assignment.assignmentId = 1;
        course.assignments = [assignment];
      } else {
        let max: number = 0;
        for (const element of course.assignments)
          if (element.assignmentId)
            if (max < element.assignmentId) max = element.assignmentId;

        assignment.assignmentId = max + 1;

        course.assignments.push(assignment);
      }
    }

    return of(assignment);
  }
}
