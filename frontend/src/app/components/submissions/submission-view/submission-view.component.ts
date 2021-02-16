import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HighlightModule, HIGHLIGHT_OPTIONS } from 'ngx-highlightjs';
import { SubmissionApiService } from 'src/app/api/submission-api/submission-api.service';
import { Submission } from 'src/app/models/submission-model';
import { SubmissionService } from 'src/app/services/submission-service/submission.service';

@Component({
  selector: 'app-submission-view',
  templateUrl: './submission-view.component.html',
  styleUrls: ['./submission-view.component.scss'],
})
export class SubmissionViewComponent implements OnInit {
  public submission: Submission;

  constructor(
    private submissionService: SubmissionService,
    private activatedRoute: ActivatedRoute
  ) {
    activatedRoute.params.subscribe((params) => {
      const submissionId = params.submissionId;
      if (submissionId != null)
        submissionService
          .getDetailedSubmission({ submissionId })
          .subscribe((data) => {
            console.log(data);
            this.submission = data;
          });
    });
  }

  code: string =
    'class Anupam \n{ \n  public static void main(String...args) \n  { \n    System.out.println("HI this is Anupam Guin "); \n  } \n}';

  ngOnInit(): void {}

  ngAfterViewInit(): void {}
}
