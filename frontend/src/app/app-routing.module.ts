import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AssignmentAddComponent } from './components/assignment/assignment-add/assignment-add.component';
import { AssignmentDetailedComponent } from './components/assignment/assignment-detailed/assignment-detailed.component';
import { ClassAddComponent } from './components/class/class-add/class-add.component';
import { ClassComponent } from './components/class/class.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { FirstScreenComponent } from './components/first-screen/first-screen.component';
import { SubmissionAddComponent } from './components/submissions/submission-add/submission-add.component';
import { SubmissionViewComponent } from './components/submissions/submission-view/submission-view.component';
import { UserProfileComponent } from './components/user-profile/user-profile.component';

const routes: Routes = [
  {
    path: '',
    component: FirstScreenComponent,
  },
  {
    path: 'dashboard',
    component: DashboardComponent,
  },
  {
    path: 'course/add',
    component: ClassAddComponent,
  },
  {
    path: 'course/edit/:classID',
    component: ClassAddComponent,
  },
  {
    path: 'course/:classID',
    component: ClassComponent,
  },
  {
    path: 'course/:courseId/assignment/add',
    component: AssignmentAddComponent,
  },
  {
    path: 'course/:courseId/assignment/edit/:assignmentId',
    component: AssignmentAddComponent,
  },
  {
    path: 'course/:courseId/assignment/:assignmentId',
    component: AssignmentDetailedComponent
  },
  {
    path: 'user/:userId/profile',
    component: UserProfileComponent
  },
  {
    path: 'course/:courseId/assignment/:assignmentId/submission/new',
    component: SubmissionAddComponent
  },
  {
    path: 'course/:courseId/assignment/:assignmentId/submission/:submissionId',
    component: SubmissionViewComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
