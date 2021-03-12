import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Assignment } from 'src/app/models/assignment-model';
import { Course } from 'src/app/models/class-model';
import { Submission, SubmissionStatus } from 'src/app/models/submission-model';
import { AssignmentService } from 'src/app/services/assignment-service/assignment.service';
import { CoursesService } from 'src/app/services/courses-service/courses.service';
import { SubmissionService } from 'src/app/services/submission-service/submission.service';

@Component({
  selector: 'app-assignment-detailed',
  templateUrl: './assignment-detailed.component.html',
  styleUrls: ['./assignment-detailed.component.scss'],
})
export class AssignmentDetailedComponent implements OnInit, OnDestroy {
  public loading: boolean = true;

  public course: Course | null = null;
  public assignment: Assignment | null = null;
  public mySubmissions: Submission[] = [];
  public boughtSubmissions: Submission[] = [];

  public showAddSubmissionBox: boolean = false;
  public showBuySubmissionBox: boolean = false;
  public showAddSubmissionButton: boolean = false;
  public showDownloadMySubmissions: boolean = false;
  public showBuySubmissionsButton: boolean = false;
  public showDownloadBoughtSubmissionsButton: boolean = false;

  public submissionModalSubmission: Submission | null = null;

  public submissionBuyQuantityInput: number = 0;

  public buyingErrorMesage: string | null = null;

  public updateWorker: any | null = null;

  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private assignmentService: AssignmentService,
    private courseService: CoursesService,
    private submissionService: SubmissionService
  ) {
    activatedRoute.params.subscribe((params) => {
      const courseId = params.courseId;
      const assignmentId = params.assignmentId;
      assignmentService
        .getAssignmentWithSubmissions({ assignmentId })
        .subscribe(
          (data) => {
            this.assignment = data;
            this.loading = false;
            this.assignBooleanValuesToActionButtons();
          },
          (err) => console.error(err)
        );

      courseService.getCourse(courseId).subscribe((data) => {
        this.course = data;
        this.assignBooleanValuesToActionButtons();
      });
    });
  }

  ngOnInit(): void {}

  private assignBooleanValuesToActionButtons(): void {
    if (
      this.assignment?.mySubmissions != null &&
      this.assignment?.mySubmissions?.length > 0
    )
      this.showDownloadMySubmissions = true;
    if (
      this.assignment?.boughtSubmissions != null &&
      this.assignment?.boughtSubmissions?.length > 0
    )
      this.showDownloadBoughtSubmissionsButton = true;

    if (
      this.course?.usersCoins >= this.assignment?.coinsPrice &&
      this.assignment?.noOfSubmissionsTotal > 0
    )
      this.showBuySubmissionsButton = true;

    if (this.assignmnetIsActive()) {
      if (this.assignment.maxSubmissionsPerStudent == null)
        this.showAddSubmissionButton = true;
      else if (
        (this.assignment.maxSubmissionsTotal == null ||
          this.assignment.noOfSubmissionsTotal <
            this.assignment.maxSubmissionsTotal) &&
        this.assignment.noOfSubmissionsStudent <
          this.assignment.maxSubmissionsPerStudent
      )
        this.showAddSubmissionButton = true;
    }
  }

  private assignmnetIsActive(): boolean {
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

  public onTableRowViewPressed(element: Submission): void {
    this.submissionService
      .getDetailedSubmission({ submissionId: element.submissionId })
      .subscribe((data) => {
        this.submissionModalSubmission = data;
      });
  }

  public onTableRowDownloadPressed(element: Submission): void {
    this.submissionService.downloadSubmission(element);
  }

  public onSubmissionsBuyButtonPressed(): void {
    this.buyingErrorMesage = null;
    this.submissionService
      .buySubmissions(this.assignment, this.submissionBuyQuantityInput)
      .subscribe(
        (data) => {
          if (this.assignment.boughtSubmissions === null)
            this.assignment.boughtSubmissions = [];
          data.forEach((e) => {
            this.assignment.boughtSubmissions.push(e);
            this.course.usersCoins -= this.assignment.coinsPrice;
            this.assignBooleanValuesToActionButtons();
          });
        },
        (error: HttpErrorResponse) => {
          this.buyingErrorMesage = error.error.message;
        }
      );
  }

  public downloadMySubmissions(): void {
    this.submissionService.downloadMySubmissions(this.assignment);
  }

  public onSubmissionAddModalClosed(assignment: Assignment | null): void {
    this.showAddSubmissionBox = false;
    if (assignment != null) this.assignment = assignment;
    this.assignBooleanValuesToActionButtons();

    this.updateWorker = setInterval(() => {
      console.log('posodabljam');
      this.assignmentService
        .getAssignmentWithSubmissions(assignment)
        .toPromise()
        .then((a) => (this.assignment = a))
        .catch(() => console.log('error'));
    }, 10000);
  }

  public onSubmissionDetailedModalClosed(): void {
    this.submissionModalSubmission = null;
  }

  public get submissionStatus(): typeof SubmissionStatus {
    return SubmissionStatus;  
  }

  ngOnDestroy(): void {
    if (this.updateWorker != null) clearInterval(this.updateWorker);
  }
}
