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
  public coursesCopy: Course[] | null = null;
  public showMyCourses: boolean;

  public searchInput: string | null = null;

  constructor(
    public globalNavigationService: NavigationGlobalService,
    public userService: UserServiceService,
    public accessService: AccessService,
    private coursesService: CoursesService
  ) {
    this.user = userService.userLoggedIn;
    console.log(this.user);
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
      this.coursesCopy = this.courses;
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

  public searchFilter(): void {
    if (this.searchInput == null) return;
    this.courses = [...this.coursesCopy].filter((e) =>
      e.title.toLowerCase().includes(this.searchInput.toLowerCase())
    );
  }
}
