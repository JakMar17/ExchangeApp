import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SubmissionAddComponent } from './submission-add.component';

describe('SubmissionAddComponent', () => {
  let component: SubmissionAddComponent;
  let fixture: ComponentFixture<SubmissionAddComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SubmissionAddComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SubmissionAddComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
