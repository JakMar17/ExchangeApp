import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CoursesDammyData } from 'src/app/aaa_dummy-data/dummy-courses';
import { Assignment } from 'src/app/models/assignment-model';

@Injectable({
  providedIn: 'root',
})
export class AssignmentService {
  private courseDummy = new CoursesDammyData();

  constructor() {}

  public getAssignemntFromCourse(
    courseId: number,
    assignmentId: number
  ): Observable<Assignment> {
    return this.courseDummy.getAssignemtn(courseId, assignmentId);
  }

  public saveAssignment(courseId: number, assignment: Assignment): Observable<Assignment> {
    return this.courseDummy.saveAssignment(courseId, assignment);
  }
}
