import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserServiceService } from 'src/app/services/user-service/user-service.service';

@Component({
  selector: 'app-app-description',
  templateUrl: './app-description.component.html',
  styleUrls: ['./app-description.component.scss'],
})
export class AppDescriptionComponent implements OnInit {
  constructor(
    private userService: UserServiceService,
    private router: Router
  ) {}

  ngOnInit(): void {}

  public adminLogin(): void {
    this.userService.loginUserIn('admin', 'admin').subscribe(
      (user) => {
        this.userService.userLoggedIn = user;
        this.router.navigate(['/dashboard']);
      },
      (err: HttpErrorResponse) => {
        console.error(err);
      }
    );
  }

  public profLogin(): void {
    this.userService.loginUserIn('prof', 'prof').subscribe(
      (user) => {
        this.userService.userLoggedIn = user;
        this.router.navigate(['/dashboard']);
      },
      (err: HttpErrorResponse) => {
        console.error(err);
      }
    );
  }

  public studentLogin(): void {
    this.userService.loginUserIn('student', 'student').subscribe(
      (user) => {
        this.userService.userLoggedIn = user;
        this.router.navigate(['/dashboard']);
      },
      (err: HttpErrorResponse) => {
        console.error(err);
      }
    );
  }
}
