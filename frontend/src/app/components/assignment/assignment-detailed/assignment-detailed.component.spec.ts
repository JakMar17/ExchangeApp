import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AssignmentDetailedComponent } from './assignment-detailed.component';

describe('AssignmentDetailedComponent', () => {
  let component: AssignmentDetailedComponent;
  let fixture: ComponentFixture<AssignmentDetailedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AssignmentDetailedComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AssignmentDetailedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
