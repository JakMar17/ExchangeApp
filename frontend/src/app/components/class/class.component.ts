import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { Course, CourseAccess } from 'src/app/models/class-model';
import { User } from 'src/app/models/user-model';
import { AccessService } from 'src/app/services/access-service/access.service';
import { CoursesService } from 'src/app/services/courses-service/courses.service';
import { UserServiceService } from 'src/app/services/user-service/user-service.service';
import { ClassViewEnum } from './models/class-view-enum';

@Component({
  selector: 'app-class',
  templateUrl: './class.component.html',
  styleUrls: ['./class.component.scss'],
})
export class ClassComponent implements OnInit {
  public classView: ClassViewEnum = ClassViewEnum.CLASS_UNLOCKED;

  public classPasswordInput: string = '';
  public showClassPasswordError: boolean = false;

  public course: Course | null = null;
  public user: User | null = null;
  public userCanEdit: boolean = false;

  public courseVisibility: CourseVisibilityToUser =
    CourseVisibilityToUser.LOADING;

  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private coursesService: CoursesService,
    private userService: UserServiceService
  ) {
    this.user = userService.userLoggedIn;
  }

  ngOnInit(): void {
    this.getCourseData();
  }

  public get courseAccess(): typeof CourseVisibilityToUser {
    return CourseVisibilityToUser;
  }

  /**
   * TODO create service that checks if password is OK
   * checks if input password is OK and unlock class
   */
  public onPasswordInputClick(): void {
    if (this.user == null) return;
    this.coursesService
      .checkCoursePassword(
        this.course?.courseId ?? -1,
        this.classPasswordInput,
        this.user
      )
      .subscribe(
        (data) => {
          this.course = data;
          this.course.notifications = this.course.notifications ?? [];
          this.courseVisibility = CourseVisibilityToUser.VISIBLE;
          //this.setAccessRights();
        },
        (err: HttpErrorResponse) => {
          switch (err.status) {
            case 401:
              this.showClassPasswordError = true;
              this.course = err.error;
              break;
          }
        }
      );
  }

  private getCourseData(): void {
    this.activatedRoute.params.subscribe((params: Params) => {
      const courseId = params.classID;
      if (courseId != null)
        this.coursesService.getCourse(courseId).subscribe(
          (data) => {
            this.course = data;
            this.courseVisibility = CourseVisibilityToUser.VISIBLE;
            if (this.course.notifications == null)
              this.course.notifications = [];
            console.log(this.course);
            //this.setAccessRights();
          },
          (err: HttpErrorResponse) => {
            console.error(err);
            this.course = err.error;
            switch (err.status) {
              case 401:
                this.courseVisibility = CourseVisibilityToUser.PASSWORD;
                break;
              case 403:
                this.courseVisibility = CourseVisibilityToUser.BLOCKED;
                break;
            }
          }
        );
    });
  }

  public onAssignmentNewButtonClick(): void {
    this.router.navigate([
      '/course/' + this.course.courseId + '/assignment/add',
    ]);
  }

  public onEditCourseButtonPressed(): void {
    this.router.navigate(['/course/edit/' + this.course.courseId]);
  }
}

export enum CourseVisibilityToUser {
  LOADING,
  VISIBLE,
  BLOCKED,
  PASSWORD,
}
