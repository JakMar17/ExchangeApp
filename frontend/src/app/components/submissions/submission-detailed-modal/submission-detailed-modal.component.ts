import { AfterViewInit, ChangeDetectorRef, Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Assignment } from 'src/app/models/assignment-model';
import {
  Submission,
  SubmissionSimilarity,
  SubmissionStatus,
} from 'src/app/models/submission-model';
import { SubmissionService } from 'src/app/services/submission-service/submission.service';

@Component({
  selector: 'app-submission-detailed-modal',
  templateUrl: './submission-detailed-modal.component.html',
  styleUrls: ['./submission-detailed-modal.component.scss'],
})
export class SubmissionDetailedModalComponent implements OnInit, AfterViewInit {
  @Input() submission: Submission;
  @Input() assignment: Assignment;
  @Output() onClose: EventEmitter<Submission | null> = new EventEmitter();

  public showInputPanel: boolean = true;
  public showOutputPanel: boolean = true;
  public showErrorPanel: boolean = true;
  public showError: boolean = true;
  public similarities: SubmissionSimilarity[] = [];

  constructor(private submissionService: SubmissionService, private ref: ChangeDetectorRef) {}

  ngOnInit(): void {
    console.log(this.submission);

    this.showError =
      this.submission.status === SubmissionStatus.NOK ||
      this.submission.status === SubmissionStatus.COMPILE_ERROR;

    this.getSubmissionSimilarities();
  }

  ngAfterViewInit(): void {
    this.ref.detectChanges();
  }

  public closeModal(): void {
    this.onClose.emit(null);
  }

  public get submissionStatus(): typeof SubmissionStatus {
    return SubmissionStatus;
  }

  public getSubmissionSimilarities(): void {
    this.submissionService
      .getSubmissionsSimilarity(this.submission)
      .subscribe((d) => {
        this.similarities = d;
        console.log(this.similarities);
      });
  }
}
