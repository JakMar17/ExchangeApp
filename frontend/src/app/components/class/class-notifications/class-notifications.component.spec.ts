import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClassNotificationsComponent } from './class-notifications.component';

describe('ClassNotificationsComponent', () => {
  let component: ClassNotificationsComponent;
  let fixture: ComponentFixture<ClassNotificationsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ClassNotificationsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ClassNotificationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
