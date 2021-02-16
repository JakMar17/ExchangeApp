import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FilesApiService } from 'src/app/api/files-api/files-api.service';
import { Assignment } from 'src/app/models/assignment-model';
import { Course } from 'src/app/models/class-model';
import {
  Submission,
  SubmissionFilePair,
} from 'src/app/models/submission-model';
import { AssignmentService } from 'src/app/services/assignment-service/assignment.service';
import { CoursesService } from 'src/app/services/courses-service/courses.service';
import { SubmissionService } from 'src/app/services/submission-service/submission.service';
import { UserServiceService } from 'src/app/services/user-service/user-service.service';

@Component({
  selector: 'app-assignment-detailed',
  templateUrl: './assignment-detailed.component.html',
  styleUrls: ['./assignment-detailed.component.scss'],
})
export class AssignmentDetailedComponent implements OnInit {
  public loading: boolean = true;

  public uploadQueue: SubmissionFilePair[] = [];
  public checkboxMyWork: boolean = false;

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

  public submissionBuyQuantityInput: number = 0;

  public errorMessageNewSubmission: string | null = null;

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
          this.assignment.noOfSubmissionsTotal <=
            this.assignment.maxSubmissionsTotal) &&
        this.assignment.noOfSubmissionsStudent <=
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

  public handleFileUpload($event: any): void {
    const files: FileList = $event.target.files;
    const filesArray = Array.from(files);
    const inputs = filesArray.filter((e) =>
      e.name.toLowerCase().includes('vhod')
    );
    const outputs = filesArray.filter((e) =>
      e.name.toLowerCase().includes('izhod')
    );

    this.uploadQueue = inputs.map((e) => {
      const output = outputs.find(
        (o) => this.getNumberOfFile(e, true) === this.getNumberOfFile(o, false)
      );

      return {
        inputFile: e,
        outputFile: output ?? null,
        inputName: e.name,
        outputName: output?.name ?? null,
        status: e != null && output != null ? 'READY_UPLOAD' : 'ERROR',
      };
    });
  }

  private getNumberOfFile(file: File, input: boolean): string {
    let name = file.name;
    name = name.replace(input ? 'vhod' : 'izhod', '');
    return name.replace('.txt', '');
  }

  public onCheckboxMyWorkPressed(): void {
    this.checkboxMyWork = !this.checkboxMyWork;
  }

  public onUploadButtonPressed(): void {
    if (this.uploadQueue == null || this.uploadQueue.length === 0) {
      this.errorMessageNewSubmission = 'Ni kaj oddati';
      return;
    }

    if (!this.checkboxMyWork) {
      this.errorMessageNewSubmission = 'Oddaja mora biti vaše delo.';
      return;
    }

    this.errorMessageNewSubmission = null;

    /* [...this.uploadQueue].forEach((e) => {
      this.submissionService
        .uploadFilePair(e.inputFile, e.outputFile, this.assignment)
        .subscribe(
          (data) => {
            this.assignment.mySubmissions.push(data);
            this.assignment.noOfSubmissionsStudent++;
            this.assignment.noOfSubmissionsTotal++;
            this.removeFromQueue(e);

            this.assignBooleanValuesToActionButtons();
          },
          (err: HttpErrorResponse) => {
            if (this.errorMessageNewSubmission === null)
              this.errorMessageNewSubmission = '';
            this.errorMessageNewSubmission += '\n' + err.error.message;
            this.checkboxMyWork = true;
          }
        );
    }); */

    this.submissionService
      .uploadFiles([...this.uploadQueue], this.assignment)
      .then((observable) =>
        observable.subscribe((data) => (this.assignment = data))
      );

    this.uploadQueue = [];
    this.showAddSubmissionBox = false;
    this.checkboxMyWork = false;
  }

  private removeFromQueue(element: SubmissionFilePair): void {
    this.uploadQueue = this.uploadQueue.filter((e) => e !== element);
  }

  public onTableRowDeletePressed(element: SubmissionFilePair): void {
    this.uploadQueue = this.uploadQueue.filter((e) => e !== element);
  }

  public onTableRowViewPressed(element: Submission): void {
    this.router.navigate([
      '/course/' +
        this.course.courseId +
        '/assignment/' +
        this.assignment.assignmentId +
        '/submission/' +
        element.submissionId,
    ]);
  }

  public onTableRowDownloadPressed(element: Submission): void {}

  public onSubmissionsBuyButtonPressed(): void {
    this.submissionService
      .buySubmissions(this.assignment, this.submissionBuyQuantityInput)
      .subscribe((data) => {
        if (this.assignment.boughtSubmissions === null)
          this.assignment.boughtSubmissions = [];
        data.forEach((e) => {
          this.assignment.boughtSubmissions.push(e);
          this.course.usersCoins -= this.assignment.coinsPrice;
        });
      });
  }
}
