import { UserTypeToStringPipe } from './user-type-to-string.pipe';

describe('UserTypeToStringPipe', () => {
  it('create an instance', () => {
    const pipe = new UserTypeToStringPipe();
    expect(pipe).toBeTruthy();
  });
});
