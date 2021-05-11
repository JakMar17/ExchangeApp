import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AssignmentAddComponent } from './components/assignment/assignment-add/assignment-add.component';
import { AssignmentDetailedComponent } from './components/assignment/assignment-detailed/assignment-detailed.component';
import { ClassAddComponent } from './components/class/class-add/class-add.component';
import { ClassComponent } from './components/class/class.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { FirstScreenComponent } from './components/first-screen/first-screen.component';
import { SubmissionViewComponent } from './components/submissions/submission-view/submission-view.component';
import { UserProfileComponent } from './components/user-profile/user-profile.component';
import { UserServiceService } from './services/user-service/user-service.service';

const routes: Routes = [
  {
    path: '',
    component: FirstScreenComponent,
  },
  {
    path: 'dashboard',
    component: DashboardComponent,
    canActivate: [UserServiceService],
  },
  {
    path: 'course/add',
    component: ClassAddComponent,
    canActivate: [UserServiceService],
  },
  {
    path: 'course/edit/:classID',
    component: ClassAddComponent,
    canActivate: [UserServiceService],
  },
  {
    path: 'course/:classID',
    component: ClassComponent,
    canActivate: [UserServiceService],
  },
  {
    path: 'course/:courseId/assignment/add',
    component: AssignmentAddComponent,
    canActivate: [UserServiceService],
  },
  {
    path: 'course/:courseId/assignment/edit/:assignmentId',
    component: AssignmentAddComponent,
    canActivate: [UserServiceService],
  },
  {
    path: 'course/:courseId/assignment/:assignmentId',
    component: AssignmentDetailedComponent,
    canActivate: [UserServiceService],
  },
  {
    path: 'user/profile',
    component: UserProfileComponent,
    canActivate: [UserServiceService],
  },
  {
    path: 'course/:courseId/assignment/:assignmentId/submission/:submissionId',
    component: SubmissionViewComponent,
    canActivate: [UserServiceService],
  },
  {
    path: '**',
    component: FirstScreenComponent,
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
