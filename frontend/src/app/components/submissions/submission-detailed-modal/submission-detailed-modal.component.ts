import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Assignment } from 'src/app/models/assignment-model';
import { Submission, SubmissionStatus } from 'src/app/models/submission-model';
import { SubmissionService } from 'src/app/services/submission-service/submission.service';

@Component({
  selector: 'app-submission-detailed-modal',
  templateUrl: './submission-detailed-modal.component.html',
  styleUrls: ['./submission-detailed-modal.component.scss'],
})
export class SubmissionDetailedModalComponent implements OnInit {
  @Input() submission: Submission;
  @Output() onClose: EventEmitter<Submission | null> = new EventEmitter();

  public showInputPanel: boolean = true;
  public showOutputPanel: boolean = true;
  public showErrorPanel: boolean = true;
  public showError: boolean = true;

  constructor(private submissionService: SubmissionService) {}

  ngOnInit(): void {
    console.log(this.submission);

    this.showError =
      this.submission.status === SubmissionStatus.NOK ||
      this.submission.status === SubmissionStatus.COMPILE_ERROR;
  }

  public closeModal(): void {
    this.onClose.emit(null);
  }

  public get submissionStatus(): typeof SubmissionStatus {
    return SubmissionStatus;
  }
}
