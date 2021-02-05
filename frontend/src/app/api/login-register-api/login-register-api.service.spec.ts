import { TestBed } from '@angular/core/testing';

import { LoginRegisterApiService } from './login-register-api.service';

describe('LoginRegisterApiService', () => {
  let service: LoginRegisterApiService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LoginRegisterApiService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
