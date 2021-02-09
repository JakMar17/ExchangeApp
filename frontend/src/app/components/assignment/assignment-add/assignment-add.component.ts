import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import {
  Assignment,
  AssignmentStatus,
  SubmissionCheck,
} from 'src/app/models/assignment-model';
import { AssignmentService } from 'src/app/services/assignment-service/assignment.service';

@Component({
  selector: 'app-assignment-add',
  templateUrl: './assignment-add.component.html',
  styleUrls: ['./assignment-add.component.scss'],
})
export class AssignmentAddComponent implements OnInit {
  public assignment: Assignment = {
    title: '',
    startDate: new Date(),
    coinsPerSubmission: 1,
    coinsPrice: 1,
    notifyOnEmail: false,
    visible: true,
  };

  public courseId: number | null = null;
  public editing: boolean = false;

  public errorMessage: string | null = null;

  constructor(
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private assignmentService: AssignmentService
  ) {
    this.activatedRoute.params.subscribe((params) => {
      if (params != null) {
        this.courseId = params.courseId;
        this.assignment.assignmentId = params.assignmentId;

        if (this.courseId != null && this.assignment.assignmentId != null) {
          this.editing = true;
          assignmentService.getAssignment(this.assignment).subscribe((data) => {
            this.assignment = data;
            console.log(data);
          });
        }
      }
    });
  }

  ngOnInit(): void {}

  public changeSubmissionCheck(check: SubmissionCheck): void {
    //this.assignment.submissionCheck = check;
  }

  public onPlagiarismWarningCheckboxClick(): void {
    if (this.assignment.plagiarismWarning == null)
      this.assignment.plagiarismWarning = 80;
    else this.assignment.plagiarismWarning = null;
  }

  public onPlagiarismLevelCheckboxClick(): void {
    if (this.assignment.plagiarismLevel == null)
      this.assignment.plagiarismLevel = 95;
    else this.assignment.plagiarismLevel = null;
  }

  public onSubmissionCheckTabChange(submissionCheck: SubmissionCheck): void {
    //this.assignment.submissionCheck = submissionCheck;
  }

  public saveAssignment(): void {
    console.log(this.courseId);
    this.errorMessage = this.checkInputs();

    if (this.errorMessage == null) {
      this.assignmentService
        .saveAssignment(this.assignment, this.courseId)
        .subscribe(
          () => this.alertAndExit('Naloga je bila shranjena'),
          (err: HttpErrorResponse) => (this.errorMessage = err.error.message)
        );
    }
  }

  public exitWithoutSave(): void {
    this.navigateBackToCourse();
  }

  public archiveAssignment(): void {
    this.assignment.archived = true;
    this.assignmentService.archiveAssignment(this.assignment).subscribe(
      () => this.alertAndExit('Naloga je bila arhivirana'),
      (err: HttpErrorResponse) => (this.errorMessage = err.error.message)
    );
  }

  public deleteAssignment(): void {
    this.assignmentService.deleteAssignment(this.assignment).subscribe(
      () => this.alertAndExit('Naloga je bila izbrisana'),
      (err: HttpErrorResponse) => (this.errorMessage = err.error.message)
    );
  }

  private checkInputs(): string | null {
    if (this.assignment.title.length == 0)
      return 'Ime naloge mora biti izpolnjeno';

    if (this.assignment.inputExtension == null)
      return 'Tip vhodne datoteke mora biti izpolnjen';

    if (this.assignment.outputExtension == null)
      return 'Tip izhodne datoteke mora biti izpolnjen';

    /* if (this.assignment.status == null)
      this.assignment.status = AssignmentStatus.ACTIVE; */

    return null;
  }

  public get submissionCheckEnum(): typeof SubmissionCheck {
    return SubmissionCheck;
  }

  public onStartDateInputChange($event: any): void {
    const newDate = new Date($event);
    if (!isNaN(newDate.getTime())) this.assignment.startDate = newDate;
  }

  public onEndDateInputChange($event: any): void {
    const newDate = new Date($event);
    if (!isNaN(newDate.getTime())) this.assignment.endDate = newDate;
  }

  private navigateBackToCourse(): void {
    this.router.navigate(['/course/' + this.courseId]);
  }

  private alertAndExit(message: string) {
    alert(message);
    this.navigateBackToCourse();
  }
}
