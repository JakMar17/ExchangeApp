import { Pipe, PipeTransform } from '@angular/core';
import { SubmissionCheck } from 'src/app/models/assignment-model';
import {
  SubmissionCorrectnessStatus,
  SubmissionSimilarityStatus,
} from 'src/app/models/submission-model';

@Pipe({
  name: 'submissionStatus',
})
export class SubmissionStatusPipe implements PipeTransform {
  transform(
    submissionStatus: SubmissionCorrectnessStatus | SubmissionSimilarityStatus
  ): string {
    switch (submissionStatus) {
      case SubmissionCorrectnessStatus.OK:
      case SubmissionSimilarityStatus.OK:
        return 'Pravilen';
      case SubmissionCorrectnessStatus.NOK:
      case SubmissionSimilarityStatus.NOK:
        return 'Napačen';
      case SubmissionCorrectnessStatus.COMPILE_ERROR:
        return 'Napaka pri prevajanju izvorne kode';
      case SubmissionCorrectnessStatus.TIMEOUT:
        return 'Prekoračitev časovne omejitve';
      case SubmissionCorrectnessStatus.PENDING_REVIEW:
      case SubmissionSimilarityStatus.PENDING_REVIEW:
        return 'V čakalni vrsti';

      case SubmissionSimilarityStatus.NOT_TESTED:
        return 'Brez testa';
      case SubmissionSimilarityStatus.WARNING:
        return 'Opozorilo';
    }
  }
}
