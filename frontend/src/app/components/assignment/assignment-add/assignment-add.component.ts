import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
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

  public errorMessage: string | null = null;

  public editing: boolean = false;

  constructor(
    private activatedRoute: ActivatedRoute,
    private assignmentService: AssignmentService
  ) {
    this.activatedRoute.params.subscribe((params) => {
      console.log(params);
      if (params != null) {
        const courseId = params.courseId;
        const assignmentId = params.assignmentId;

        this.editing = true;

        if (courseId != null && assignmentId != null)
          assignmentService
            .getAssignemntFromCourse(courseId, assignmentId)
            .subscribe(
              (data) => (this.assignment = data),
              (err) => console.log(err)
            );
      }
    });
  }

  ngOnInit(): void {}

  public changeSubmissionCheck(check: SubmissionCheck): void {
    this.assignment.submissionCheck = check;
  }

  public saveAssignment(): void {
    this.errorMessage = this.checkInputs();

    if (this.errorMessage == null) {
      //todo shrani
    }
  }

  public exitWithoutSave(): void {}

  public archiveAssignment(): void {}

  public deleteAssignment(): void {}

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
}
