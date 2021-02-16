import { Component, OnInit } from '@angular/core';
import { Course } from 'src/app/models/class-model';
import { User, UserType } from 'src/app/models/user-model';
import { AccessService } from 'src/app/services/access-service/access.service';
import { CoursesService } from 'src/app/services/courses-service/courses.service';
import { NavigationGlobalService } from 'src/app/services/navigation-global/navigation-global.service';
import { UserServiceService } from 'src/app/services/user-service/user-service.service';
import { Notification } from '../notifications/models/notification-interface';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
})
export class DashboardComponent implements OnInit {
  public notifications: Notification[] | null = null;

  public user: User | null;
  public courses: Course[] | null = null;
  public showMyCourses: boolean;

  public searchInput: string = '';

  constructor(
    public globalNavigationService: NavigationGlobalService,
    public userService: UserServiceService,
    public accessService: AccessService,
    private coursesService: CoursesService
  ) {
    this.user = userService.userLoggedIn;
    this.showMyCourses = (this.user?.myCourses?.length ?? 0) > 0;
  }

  ngOnInit(): void {
    this.getAllCourses();
    this.getUserCourses();
    this.getNotifications();
  }

  private getAllCourses(): void {
    this.coursesService.getAllCourses().subscribe((data) => {
      this.courses = data;
    });
  }

  private getUserCourses(): void {
    this.coursesService
      .getMyCourses()
      .subscribe((data) => (this.userService.userLoggedIn.myCourses = data));
  }

  private getNotifications(): void {
    this.coursesService
      .getDashboardNotifications()
      .subscribe((data) => (this.notifications = data));
  }


  public filteringFuntion(element: Course, filter: string): boolean {
    return element.title.toLowerCase().includes(filter.toLowerCase());
  }
}
