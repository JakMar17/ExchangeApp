import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Assignment } from 'src/app/models/assignment-model';
import { Submission } from 'src/app/models/submission-model';
import { SubmissionService } from 'src/app/services/submission-service/submission.service';

@Component({
  selector: 'app-submission-detailed-modal',
  templateUrl: './submission-detailed-modal.component.html',
  styleUrls: ['./submission-detailed-modal.component.scss'],
})
export class SubmissionDetailedModalComponent implements OnInit {
  @Input() submission: Submission;
  @Output() onClose: EventEmitter<Submission | null> = new EventEmitter();

  constructor(private submissionService: SubmissionService) {}

  ngOnInit(): void {
    console.log(this.submission);
  }

  public closeModal(): void {
    this.onClose.emit(null);
  }
}
