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
    submissionCheck: SubmissionCheck.NONE,
    submissionNotify: false,
    visible: true,
    noOfSubmisions: 0,
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
        const assignmentId = params.assignmentId;

        if (this.courseId != null && assignmentId != null) {
          this.editing = true;
          assignmentService
            .getAssignemntFromCourse(this.courseId, assignmentId)
            .subscribe((data) => (this.assignment = data));
        }
      }
    });
  }

  ngOnInit(): void {
    console.log(this.assignment);
  }

  public changeSubmissionCheck(check: SubmissionCheck): void {
    this.assignment.submissionCheck = check;
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
    this.assignment.submissionCheck = submissionCheck;
  }

  public saveAssignment(): void {
    this.errorMessage = this.checkInputs();
    console.log(this.assignment);

    if (this.errorMessage == null) {
      this.assignmentService
        .saveAssignment(this.courseId, this.assignment)
        .subscribe((data) => {
          alert('Naloga je shranjena (' + data.assignmentId + ')');
          this.navigateBackToCourse();
        });
    }
  }

  public exitWithoutSave(): void {
    this.navigateBackToCourse();
  }

  public archiveAssignment(): void {
    this.assignment.status = AssignmentStatus.ARCHIVED;
    this.saveAssignment();
  }

  public deleteAssignment(): void {
    this.assignment.status = AssignmentStatus.DELETED;
    this.saveAssignment();
  }

  private checkInputs(): string | null {
    if (this.assignment.title.length == 0)
      return 'Ime naloge mora biti izpolnjeno';

    if (this.assignment.inputDataType == null)
      return 'Tip vhodne datoteke mora biti izpolnjen';

    if (this.assignment.outputDataType == null)
      return 'Tip izhodne datoteke mora biti izpolnjen';

    if (this.assignment.status == null)
      this.assignment.status = AssignmentStatus.ACTIVE;

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
}
