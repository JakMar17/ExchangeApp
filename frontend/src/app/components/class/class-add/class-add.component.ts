import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CourseApiService } from 'src/app/api/course-api/course-api.service';
import { Course } from 'src/app/models/class-model';
import { User } from 'src/app/models/user-model';
import { CoursesService } from 'src/app/services/courses-service/courses.service';
import { UserServiceService } from 'src/app/services/user-service/user-service.service';
import { SecurityLevel } from './models/security-level';

@Component({
  selector: 'app-class-add',
  templateUrl: './class-add.component.html',
  styleUrls: ['./class-add.component.scss'],
})
export class ClassAddComponent implements OnInit {
  public selectedSecurityLevel: SecurityLevel = null;
  public securityLevels: SecurityLevel[] = [
    {
      key: 'PUBLIC',
      securityLevelTitle: 'javen',
      securityLevelDescription:
        'Do predmeta lahko dostopa in sodeluje vsak prijavljen uporabnik.',
      securityLevelIcon: 'ri-global-line',
    },
    {
      key: 'PASSWORD',
      securityLevelTitle: 'zaščiten z geslom',
      securityLevelDescription:
        'Pred prvim dostopom do predmeta mora uporabnik vpisati prednastavljeno geslo.',
      securityLevelIcon: 'ri-lock-2-line',
    },
    {
      key: 'WHITELIST',
      securityLevelTitle: 'omejen',
      securityLevelDescription:
        'Do predmeta lahko dostopajo samo uporabniki, katerih vpisna številka/email je omogočena na definiranem seznamu (whitelist).',
      securityLevelIcon: 'ri-file-list-3-line',
    },
  ];

  public courseId: number | null = null;
  public course: Course = {
    courseId: null,
    description: null,
    guardianMain: this.userService.userLoggedIn,
    title: null,
  };

  public showAddGuardianRow: boolean = false;
  public guardianAddEmailInput: string = null;

  public showAddWhitelistsRow: boolean = false;
  public whitelistAddEmailInput: string = null;
  public whitelistAddPersonalNumberInput: string = null;

  public showAddBlacklistsRow: boolean = false;
  public blacklistAddEmailInput: string = null;
  public blacklistAddPersonalNumberInput: string = null;

  public showAddFromFileModal: boolean = false;

  public errorMessage: string = null;

  constructor(
    private activatedRoute: ActivatedRoute,
    private courseApi: CourseApiService,
    private courseService: CoursesService,
    private userService: UserServiceService,
    private router: Router
  ) {
    this.getCourseData();
  }

  ngOnInit(): void {
    this.selectedSecurityLevel = this.securityLevels[0];
  }

  private getCourseData(): void {
    this.activatedRoute.params.subscribe((params) => {
      if (params.classID != null) {
        this.courseId = params.classID;

        this.courseApi
          .getCourseDetailedData(this.courseId, this.userService.bearer)
          .subscribe((data) => {
            this.course = data;
            this.selectedSecurityLevel = this.securityLevels.find(
              (e) => e.key == this.course.accessLevel
            );
          });
      }
    });
  }

  public get typeOfList(): typeof TypeOfList {
    return TypeOfList;
  }

  public onSecurityLevelSelected(selected: SecurityLevel): void {
    this.selectedSecurityLevel = selected;
  }

  public onElementListRemove(element: User, typeOfList: TypeOfList): void {
    switch (typeOfList) {
      case TypeOfList.WHITELIST:
        this.course.studentsWhitelisted = this.course.studentsWhitelisted.filter(
          (e) => e !== element
        );
        break;
      case TypeOfList.BLACKLIST:
        this.course.studentsBlacklisted = this.course.studentsBlacklisted.filter(
          (e) => e !== element
        );
        break;
      case TypeOfList.GUARDIANS:
        this.course.guardians = this.course.guardians.filter(
          (e) => e !== element
        );
        break;
    }
  }

  public onAllElementsListRemove(typeOfList: TypeOfList): void {
    switch (typeOfList) {
      case TypeOfList.WHITELIST:
        this.course.studentsWhitelisted = null;
        break;
      case TypeOfList.BLACKLIST:
        this.course.studentsBlacklisted = null;
        break;
      case TypeOfList.GUARDIANS:
        this.course.guardians = null;
        break;
    }
  }

  public onGuardianAdd(): void {
    if (
      this.guardianAddEmailInput == null ||
      this.guardianAddEmailInput.length == 0
    )
      return;

    const guardianNew: User = { email: this.guardianAddEmailInput };

    if (this.course.guardians != null) this.course.guardians.push(guardianNew);
    else this.course.guardians = [guardianNew];

    this.showAddGuardianRow = false;
    this.guardianAddEmailInput = null;
  }

  public onStudentWhitelistAdd(): void {
    if (
      this.whitelistAddEmailInput == null ||
      this.whitelistAddEmailInput.length == 0
    )
      if (
        this.whitelistAddPersonalNumberInput == null ||
        this.whitelistAddPersonalNumberInput.length == 0
      )
        return;

    const studentNew: User = {
      email: this.whitelistAddEmailInput,
      personalNumber: this.whitelistAddPersonalNumberInput,
    };

    if (this.course.studentsWhitelisted != null)
      this.course.studentsWhitelisted.push(studentNew);
    else this.course.studentsWhitelisted = [studentNew];

    this.showAddWhitelistsRow = false;
    this.whitelistAddEmailInput = null;
    this.whitelistAddPersonalNumberInput = null;
  }

  public onStudentBlacklistAdd(): void {
    if (
      this.blacklistAddEmailInput == null ||
      this.blacklistAddEmailInput.length == 0
    )
      if (
        this.blacklistAddPersonalNumberInput == null ||
        this.blacklistAddPersonalNumberInput.length == 0
      )
        return;

    const studentNew: User = {
      email: this.blacklistAddEmailInput,
      personalNumber: this.blacklistAddPersonalNumberInput,
    };

    if (this.course.studentsBlacklisted != null)
      this.course.studentsBlacklisted.push(studentNew);
    else this.course.studentsBlacklisted = [studentNew];

    this.showAddBlacklistsRow = false;
    this.blacklistAddEmailInput = null;
    this.blacklistAddPersonalNumberInput = null;
  }

  public onSaveButtonPressed(): void {
    this.errorMessage = null;
    this.course.accessLevel = this.selectedSecurityLevel.key;

    if (this.course.title == null || this.course.title.length === 0)
      this.errorMessage = 'Ime predmeta mora biti izpolnjeno';
    else if (this.course.initialCoins == null)
      this.errorMessage = 'Začetno stanje žetonov mora biti izpolnjeno';
    else if (
      this.course.accessLevel === 'PASSWORD' &&
      (this.course.accessPassword == null ||
        this.course.accessPassword.length === 0)
    )
      this.errorMessage = 'Geslo mora biti izpolnjeno';

    if (this.errorMessage == null)
      this.courseApi.saveCourse(this.course, this.userService.bearer).subscribe(
        (data) => this.router.navigate(['/course/' + data.courseId]),
        (err: HttpErrorResponse) => alert('Napaka: ' + err.error.message)
      );
  }

  public onDeleteButtonPressed(): void {
    this.courseService.deleteCourse(this.course).subscribe(
      (data) => {
        alert('Predmet je bil izbrisan');
        this.router.navigate(['/dashboard']);
      },
      (err: HttpErrorResponse) => {
        this.errorMessage = err.error.message;
      }
    );
  }

  public onWhitelistModalClose(data: User[] | null): void {
    if (data != null) {
      if (this.course.studentsWhitelisted == null)
        this.course.studentsWhitelisted = [];
      data.forEach((e) => this.course.studentsWhitelisted.push(e));
    }
    this.showAddFromFileModal = false;
  }
}

enum TypeOfList {
  WHITELIST,
  BLACKLIST,
  GUARDIANS,
  SIGNED_IN,
}
