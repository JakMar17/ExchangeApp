import {
  AfterViewInit,
  ChangeDetectorRef,
  Component,
  EventEmitter,
  Input,
  OnInit,
  Output,
} from '@angular/core';
import { Assignment } from 'src/app/models/assignment-model';
import {
  Submission,
  SubmissionCorrectnessStatus,
  SubmissionSimilarity,
  SubmissionSimilarityStatus,
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

  constructor(
    private submissionService: SubmissionService,
    private ref: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    console.log(this.submission);

    this.showError =
      this.submission.correctnessStatus === SubmissionCorrectnessStatus.NOK ||
      this.submission.correctnessStatus === SubmissionCorrectnessStatus.COMPILE_ERROR;

    this.getSubmissionSimilarities();
  }

  ngAfterViewInit(): void {
    this.ref.detectChanges();
  }

  public closeModal(): void {
    this.onClose.emit(null);
  }

  public get submissionCorrectnessStatus(): typeof SubmissionCorrectnessStatus {
    return SubmissionCorrectnessStatus;
  }

  public get submissionSimilarityStatus(): typeof SubmissionSimilarityStatus {
    return SubmissionSimilarityStatus;
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
