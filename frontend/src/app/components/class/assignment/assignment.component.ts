import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Assignment, AssignmentStatus } from 'src/app/models/assignment-model';
import { UserServiceService } from 'src/app/services/user-service/user-service.service';

@Component({
  selector: 'app-assignment',
  templateUrl: './assignment.component.html',
  styleUrls: ['./assignment.component.scss'],
})
export class AssignmentComponent implements OnInit {
  @Input() canEdit: boolean = false;
  @Input() assignment: Assignment | null = null;
  @Input() courseId: number;
  public showBuyingBox: boolean = false;

  constructor(
    private router: Router,
    private userService: UserServiceService
  ) {}

  ngOnInit(): void {}

  public onEditButtonClick(): void {
    this.router.navigate([
      '/course/' +
        this.courseId +
        '/assignment/edit/' +
        this.assignment.assignmentId,
    ]);
  }

  public onSetVisibilityClick(visible: boolean): void {
    //TODO create service that sets visibility on backend
    this.assignment.visible = visible;
  }

  public get assignmentStatusEnum(): typeof AssignmentStatus {
    return AssignmentStatus;
  }

  private isActive(): boolean {
    const todayTime = new Date().getTime();
    if (this.assignment.status === AssignmentStatus.ACTIVE)
      if (
        this.assignment.startDate.getTime() <= todayTime &&
        todayTime <= this.assignment.endDate.getTime()
      )
        return true;

    return false;
  }

  public showAddSubmissionButton(): boolean {
    if (this.isActive())
      if (
        this.assignment.noOfSubmisions < this.assignment.maxSubmissionsTotal ||
        (this.assignment.maxSubmissionsTotal ?? 0) === 0
      )
        if (
          (this.assignment.studentSubmissions?.length ?? 0) <
            this.assignment.maxSubmissionsStudent ||
          (this.assignment.maxSubmissionsStudent ?? 0) === 0
        )
          return true;
    return false;
  }

  public showDownloadMySubmissionButton(): boolean {
    return (this.assignment.studentSubmissions?.length ?? 0) > 0;
  }

  public showDownloadBoughtSubmissionButton(): boolean {
    return (this.assignment.boughtSubmissions?.length ?? 0) > 0;
  }

  public showBuySubmissionButton(): boolean {
    //todo dodaj check, ce ima user dovolj zetonov
    return this.assignment.noOfSubmisions > 0;
  }
}
