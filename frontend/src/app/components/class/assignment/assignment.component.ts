import { Component, Input, OnInit } from '@angular/core';
import { Assignment } from 'src/app/models/assignment-model';

@Component({
  selector: 'app-assignment',
  templateUrl: './assignment.component.html',
  styleUrls: ['./assignment.component.scss']
})
export class AssignmentComponent implements OnInit {

  @Input() canEdit: boolean = false;
  @Input() assignment: Assignment | null = null;
  public showBuyingBox: boolean = false;

  constructor() { }

  ngOnInit(): void {
  }

}
