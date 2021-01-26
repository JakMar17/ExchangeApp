import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AssignmentAddComponent } from './components/assignment/assignment-add/assignment-add.component';
import { ClassAddComponent } from './components/class/class-add/class-add.component';
import { ClassComponent } from './components/class/class.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { FirstScreenComponent } from './components/first-screen/first-screen.component';

const routes: Routes = [
  {
    path: "",
    component: FirstScreenComponent
  },
  {
    path: "dashboard",
    component: DashboardComponent
  },
  {
    path: "class/add",
    component: ClassAddComponent
  },
  {
    path: "class/edit/:classID",
    component: ClassAddComponent
  },
  {
    path: "class/:classID",
    component: ClassComponent
  },
  {
    path: "assignment/add",
    component: AssignmentAddComponent
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
