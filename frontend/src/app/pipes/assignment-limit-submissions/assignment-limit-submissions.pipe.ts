import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'assignmentLimitSubmissions',
})
export class AssignmentLimitSubmissionsPipe implements PipeTransform {
  transform(limit: number | null): string {
    return limit == null || limit == 0 ? 'brez' : limit.toString();
  }
}
