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
  public userCanView: boolean = false;
  public userCanEdit: boolean = false;

  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private coursesService: CoursesService,
    private userService: UserServiceService,
    private accessService: AccessService
  ) {
    this.user = userService.userLoggedIn;
  }

  ngOnInit(): void {
    this.getCourseData();
  }

  public get courseAccess(): typeof CourseAccess {
    return CourseAccess;
  }

  /**
   * TODO create service that checks if password is OK
   * checks if input password is OK and unlock class
   */
  public onPasswordInputClick(): void {
    if (this.user == null) return;
    console.log(this.classPasswordInput);
    this.coursesService
      .checkCoursePassword(
        this.course?.courseId ?? -1,
        this.classPasswordInput,
        this.user
      )
      .subscribe(
        (data) => {
          this.course = data;
          this.setAccessRights();
        },
        () => (this.showClassPasswordError = true)
      );
  }

  private getCourseData(): void {
    this.activatedRoute.params.subscribe((params: Params) => {
      const courseId = params.classID;
      if (courseId != null)
        this.coursesService.getCourse(courseId).subscribe((data) => {
          this.course = data;
          if (this.course.notifications == null) this.course.notifications = [];
          this.setAccessRights();
        });
    });
  }

  private setAccessRights(): void {
    console.log(this.user, this.course);
    if (this.user != null && this.course != null) {
      this.userCanView = this.accessService.hasAccessToCourse(
        this.user,
        this.course
      );
      this.userCanEdit = this.accessService.hasEditRightsOnCourse(
        this.user,
        this.course
      );

      console.log(this.userCanView);
    }
  }

  public onAssignmentNewButtonClick(): void {
    this.router.navigate([
      '/course/' + this.course.courseId + '/assignment/add',
    ]);
  }
}
