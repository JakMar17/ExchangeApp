import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

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
import { ClassNotificationsComponent } from './components/class/class-notifications/class-notifications.component';
import { FormsModule } from '@angular/forms';
import { ClassAddComponent } from './components/class/class-add/class-add.component';
import { FooterComponent } from './components/navigation/footer/footer.component';

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
    ClassNotificationsComponent,
    ClassAddComponent,
    FooterComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
