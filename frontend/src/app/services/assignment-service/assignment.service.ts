import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CoursesDammyData } from 'src/app/aaa_dummy-data/dummy-courses';
import { AssignmentsApiService } from 'src/app/api/assignments-api/assignments-api.service';
import { Assignment } from 'src/app/models/assignment-model';
import { Course } from 'src/app/models/class-model';
import { UserServiceService } from '../user-service/user-service.service';

@Injectable({
  providedIn: 'root',
})
export class AssignmentService {
  private courseDummy = new CoursesDammyData();

  constructor(
    private userService: UserServiceService,
    private assignmentApi: AssignmentsApiService
  ) {}

  public getCourseAssignments(course: Course): Observable<Assignment[]> {
    return this.assignmentApi.getAssignments(
      course.courseId,
      this.userService.userLoggedIn.personalNumber
    );
  }

  public inverseAssignmentVisibility(
    assignment: Assignment
  ): Observable<Assignment> {
    return this.assignmentApi.setAssignmentsVisibility(
      assignment.assignmentId,
      this.userService.userLoggedIn.personalNumber,
      !assignment.visible
    );
  }
}
