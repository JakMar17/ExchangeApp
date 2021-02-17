import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SubmissionDetailedModalComponent } from './submission-detailed-modal.component';

describe('SubmissionDetailedModalComponent', () => {
  let component: SubmissionDetailedModalComponent;
  let fixture: ComponentFixture<SubmissionDetailedModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SubmissionDetailedModalComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SubmissionDetailedModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
