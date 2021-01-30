import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Assignment } from 'src/app/models/assignment-model';
import { Course } from 'src/app/models/class-model';

@Component({
  selector: 'app-assignment',
  templateUrl: './assignment.component.html',
  styleUrls: ['./assignment.component.scss'],
})
export class AssignmentComponent implements OnInit {
  @Input() canEdit: boolean = false;
  @Input() assignment: Assignment | null = null;
  @Input() courseId: number;
  public showBuyingBox: boolean = false;

  constructor(private router: Router) {}

  ngOnInit(): void {}

  public onEditButtonClick(): void {
    this.router.navigate([
      '/course/' +
        this.courseId +
        '/assignment/edit/' +
        this.assignment.assignmentId,
    ]);
  }
}
