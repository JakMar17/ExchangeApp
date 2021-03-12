import { SubmissionStatusPipe } from './submission-status.pipe';

describe('SubmissionStatusPipe', () => {
  it('create an instance', () => {
    const pipe = new SubmissionStatusPipe();
    expect(pipe).toBeTruthy();
  });
});
