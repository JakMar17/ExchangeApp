import { Pipe, PipeTransform } from '@angular/core';
import { Submission, SubmissionSimilarityStatus, SubmissionCorrectnessStatus } from 'src/app/models/submission-model';

@Pipe({
  name: 'submissionStatusInTable'
})
export class SubmissionStatusInTablePipe implements PipeTransform {

  transform(submission: Submission): string {
    if(submission.similarityStatus === SubmissionSimilarityStatus.OK || submission.similarityStatus === SubmissionSimilarityStatus.WARNING && submission.correctnessStatus === SubmissionCorrectnessStatus.OK) {
      return "OK"
    } else {
      return "Ni ok";
    }
  }

}
