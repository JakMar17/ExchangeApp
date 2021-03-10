import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subject } from 'rxjs';
import {
  Assignment,
  AssignmentStatus,
  SubmissionCheck,
} from 'src/app/models/assignment-model';
import { Submission } from 'src/app/models/submission-model';
import { AssignmentService } from 'src/app/services/assignment-service/assignment.service';
import { SubmissionService } from 'src/app/services/submission-service/submission.service';

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
    testType: SubmissionCheck.NONE,
  };

  public submissions: Submission[] = [];

  public courseId: number | null = null;
  public editing: boolean = false;

  public errorMessage: string | null = null;
  public submissionModalSubmission: Submission | null = null;

  public sourceFile: File | null = null;

  constructor(
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private assignmentService: AssignmentService,
    private submissionService: SubmissionService
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
            submissionService
              .getAllSubmissionsOfAssignment(this.assignment)
              .subscribe((submissions) => {
                this.submissions = submissions;
              });
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
    this.assignment.testType = submissionCheck;
  }

  public async saveAssignment(): Promise<void> {
    this.errorMessage = this.checkInputs();

    if (this.errorMessage == null) {
      const assignment: Assignment = await this.assignmentService
        .saveAssignment(this.assignment, this.courseId)
        .toPromise()
        .catch(
          (err: HttpErrorResponse) => (this.errorMessage = err.error.message)
        );

      console.log('assignment', assignment);

      if (assignment.testType === SubmissionCheck.AUTOMATIC) {
        await this.assignmentService
          .saveSourceCodeForAssignment(
            assignment,
            this.assignment.sourceName,
            this.assignment.sourceLanguage,
            this.assignment.sourceTimeout,
            this.sourceFile
          )
          .toPromise()
          .catch(
            (err: HttpErrorResponse) => (this.errorMessage = err.error.message)
          );
      }

      this.alertAndExit('Naloga je bila shranjena');
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

    if (this.assignment.testType === SubmissionCheck.AUTOMATIC) {
      if (this.emptyString(this.assignment.sourceName))
        return 'Ime programa ne sme biti prazno';
      if (this.emptyString(this.assignment.sourceLanguage))
        return 'Ime programskega jezika ne sme biti prazno';
      if (this.sourceFile == null && this.assignment.sourceId == null)
        return 'Izvorna koda za testiranje mora biti izbrana';
    }

    return null;
  }

  private emptyString(x: string): boolean {
    return x === null || x.length === 0;
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

  private alertAndExit(message: string): void {
    alert(message);
    this.navigateBackToCourse();
  }

  public onTableRowViewPressed(element: Submission): void {
    this.submissionService
      .getDetailedSubmission({ submissionId: element.submissionId })
      .subscribe((data) => {
        this.submissionModalSubmission = data;
      });
  }

  public onSubmissionDetailedModalClosed(): void {
    this.submissionModalSubmission = null;
  }

  public handleFileUpload($event: any): void {
    const files: FileList = $event.target.files;
    this.sourceFile = files[0];
  }

  public onSubmissionDownloadButtonPressed(): void {
    this.assignmentService.downloadSource(this.assignment);
  }
}
