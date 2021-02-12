import { TestBed } from '@angular/core/testing';

import { SubmissionApiService } from './submission-api.service';

describe('SubmissionApiService', () => {
  let service: SubmissionApiService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SubmissionApiService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
