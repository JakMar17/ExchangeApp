import { UserToStringPipe } from './user-to-string.pipe';

describe('UserToStringPipe', () => {
  it('create an instance', () => {
    const pipe = new UserToStringPipe();
    expect(pipe).toBeTruthy();
  });
});
