import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WhitelistFileModalComponent } from './whitelist-file-modal.component';

describe('WhitelistFileModalComponent', () => {
  let component: WhitelistFileModalComponent;
  let fixture: ComponentFixture<WhitelistFileModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ WhitelistFileModalComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(WhitelistFileModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
