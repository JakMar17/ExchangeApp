import { Pipe, PipeTransform } from '@angular/core';
import { SubmissionCheck } from 'src/app/models/assignment-model';
import { SubmissionStatus } from 'src/app/models/submission-model';

@Pipe({
  name: 'submissionStatus',
})
export class SubmissionStatusPipe implements PipeTransform {
  transform(submissionStatus: SubmissionStatus): string {
    switch (submissionStatus) {
      case SubmissionStatus.OK:
        return 'Pravilen';
      case SubmissionStatus.NOK:
        return 'Napačen';
      case SubmissionStatus.COMPILE_ERROR:
        return 'Napaka pri prevajanju izvorne kode';
      case SubmissionStatus.TIMEOUT:
        return 'Prekoračitev časovne omejitve';
      case SubmissionStatus.PENDING_REVIEW:
        return 'V čakalni vrsti';
    }
  }
}
