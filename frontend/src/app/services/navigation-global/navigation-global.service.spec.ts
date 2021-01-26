import { TestBed } from '@angular/core/testing';

import { NavigationGlobalService } from './navigation-global.service';

describe('NavigationGlobalService', () => {
  let service: NavigationGlobalService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(NavigationGlobalService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
