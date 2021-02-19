import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Assignment, AssignmentStatus } from 'src/app/models/assignment-model';
import { Course } from 'src/app/models/class-model';
import { AssignmentService } from 'src/app/services/assignment-service/assignment.service';
import { CoursesService } from 'src/app/services/courses-service/courses.service';
import { UserServiceService } from 'src/app/services/user-service/user-service.service';

@Component({
  selector: 'app-assignment',
  templateUrl: './assignment.component.html',
  styleUrls: ['./assignment.component.scss'],
})
export class AssignmentComponent implements OnInit {
  @Input() canEdit: boolean = false;
  @Input() assignment: Assignment | null = null;
  @Input() course: Course;
  public showBuyingBox: boolean = false;

  public showMySubmissionsTable: boolean = true;
  public showBoughtSubmissionsTable: boolean = true;

  constructor(
    private router: Router,
    private assignmentServices: AssignmentService
  ) {}

  ngOnInit(): void {}

  public onEditButtonClick(): void {
    this.router.navigate([
      '/course/' +
        this.course.courseId +
        '/assignment/edit/' +
        this.assignment.assignmentId,
    ]);
  }

  public onSetVisibilityClick(): void {
    this.assignmentServices
      .inverseAssignmentVisibility(this.assignment)
      .subscribe((data) => {
        this.assignment = data;
      });
  }

  public get assignmentStatusEnum(): typeof AssignmentStatus {
    return AssignmentStatus;
  }

  private isActive(): boolean {
    if (this.assignment.archived) return false;
    const startDate: Date =
      this.assignment.startDate instanceof Date
        ? this.assignment.startDate
        : new Date(this.assignment.startDate);

    const todayTime = new Date().getTime();

    if (this.assignment.endDate == null)
      return startDate.getTime() <= todayTime;

    const endDate: Date =
      this.assignment.endDate instanceof Date
        ? this.assignment.endDate
        : new Date(this.assignment.endDate);

    if (startDate.getTime() <= todayTime)
      if (endDate == null) return true;
      else return todayTime <= endDate.getTime();

    return false;
  }

  public showAddSubmissionButton(): boolean {
    if (this.isActive())
      if (
        this.assignment.noOfSubmissionsTotal <
          this.assignment.maxSubmissionsTotal ||
        (this.assignment.maxSubmissionsTotal ?? 0) === 0
      )
        if (
          /* this.assignment.studentSubmissions?.length */ (null ?? 0) <
            this.assignment.maxSubmissionsPerStudent ||
          (this.assignment.maxSubmissionsPerStudent ?? 0) === 0
        )
          return true;
    return false;
  }

  public showDownloadMySubmissionButton(): boolean {
    if (this.assignment.archived) return false;
    /* return (this.assignment.studentSubmissions?.length ?? 0) > 0; */
    return true;
  }

  public showDownloadBoughtSubmissionButton(): boolean {
    if (this.assignment.archived) return false;
    /* return (this.assignment.boughtSubmissions?.length ?? 0) > 0; */
    return true;
  }

  public showBuySubmissionButton(): boolean {
    if (this.assignment.archived) return false;
    return true;
    return this.assignment.noOfSubmissionsStudent > 0;
  }

  public onAddSubmissionButtonPressed(): void {
    this.router.navigate([
      'course/' +
        this.course.courseId +
        '/assignment/' +
        this.assignment.assignmentId +
        '/submission/new',
    ]);
  }

  public onAssignmentPressed(): void {
    this.router.navigate([
      'course/' +
        this.course.courseId +
        '/assignment/' +
        this.assignment.assignmentId,
    ]);
  }

  public onAssignmentTitlePressed(): void {
    if (this.assignment.classroomUrl != null)
      window.open(this.assignment.classroomUrl, '_blank');
  }
}
