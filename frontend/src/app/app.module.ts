import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { FirstScreenComponent } from './components/first-screen/first-screen.component';
import { LoginMenuComponent } from './components/first-screen/login-menu/login-menu.component';
import { AppDescriptionComponent } from './components/first-screen/app-description/app-description.component';
import { RegisterComponent } from './components/first-screen/register/register.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { NavbarComponent } from './components/navigation/navbar/navbar.component';
import { SidemenuComponent } from './components/navigation/sidemenu/sidemenu.component';
import { DashboardClassComponent } from './components/dashboard/dashboard-class/dashboard-class.component';
import { LoadingComponent } from './components/loading/loading.component';
import { ClassComponent } from './components/class/class.component';
import { AssignmentComponent } from './components/class/assignment/assignment.component';
import { FormsModule } from '@angular/forms';
import { ClassAddComponent } from './components/class/class-add/class-add.component';
import { FooterComponent } from './components/navigation/footer/footer.component';
import { AssignmentAddComponent } from './components/assignment/assignment-add/assignment-add.component';
import { NotificationsComponent } from './components/notifications/notifications.component';
import { UserToStringPipe } from './pipes/user-pipes/user-to-string.pipe';
import { AssignmentStatusPipe } from './pipes/assignment-status/assignment-status.pipe';
import { AssignmentLimitSubmissionsPipe } from './pipes/assignment-limit-submissions/assignment-limit-submissions.pipe';
import { UserProfileComponent } from './components/user-profile/user-profile.component';
import { UserTypeToStringPipe } from './pipes/user-pipes/user-type-to-string.pipe';
import { SubmissionViewComponent } from './components/submissions/submission-view/submission-view.component';
import { HighlightModule, HIGHLIGHT_OPTIONS } from 'ngx-highlightjs';
import { AssignmentDetailedComponent } from './components/assignment/assignment-detailed/assignment-detailed.component';
import { ErrorComponent } from './components/error/error.component';
import { FilterPipe } from './pipes/filter-pipe/filter.pipe';
import { SubmissionAddComponent } from './components/assignment/assignment-detailed/submission-add/submission-add.component';
import { SubmissionDetailedModalComponent } from './components/submissions/submission-detailed-modal/submission-detailed-modal.component';
import { WhitelistFileModalComponent } from './components/class/class-add/whitelist-file-modal/whitelist-file-modal.component';
import { PasswordResetComponent } from './components/first-screen/password-reset/password-reset.component';
import { JwtHelperService, JWT_OPTIONS } from '@auth0/angular-jwt';

@NgModule({
  declarations: [
    AppComponent,
    FirstScreenComponent,
    LoginMenuComponent,
    AppDescriptionComponent,
    RegisterComponent,
    DashboardComponent,
    NavbarComponent,
    SidemenuComponent,
    DashboardClassComponent,
    LoadingComponent,
    ClassComponent,
    AssignmentComponent,
    ClassAddComponent,
    FooterComponent,
    AssignmentAddComponent,
    NotificationsComponent,
    UserToStringPipe,
    AssignmentStatusPipe,
    AssignmentLimitSubmissionsPipe,
    UserProfileComponent,
    UserTypeToStringPipe,
    SubmissionViewComponent,
    AssignmentDetailedComponent,
    ErrorComponent,
    FilterPipe,
    SubmissionAddComponent,
    SubmissionDetailedModalComponent,
    WhitelistFileModalComponent,
    PasswordResetComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HighlightModule,
    HttpClientModule,
  ],
  providers: [
    {
      provide: HIGHLIGHT_OPTIONS,
      useValue: {
        fullLibraryLoader: () => import('highlight.js'),
      },
    },
    { provide: JWT_OPTIONS, useValue: JWT_OPTIONS },
    JwtHelperService,
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
