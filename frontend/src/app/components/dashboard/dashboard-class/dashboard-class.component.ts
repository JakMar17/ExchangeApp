import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Course } from 'src/app/models/class-model';

@Component({
  selector: 'app-dashboard-class',
  templateUrl: './dashboard-class.component.html',
  styleUrls: ['./dashboard-class.component.scss']
})
export class DashboardClassComponent implements OnInit {

  @Input() course: Course | null = null;

  constructor(private router: Router) {
  }

  ngOnInit(): void {

  }

  /**
   * TODO rename to onClassPresed
   * navigate to selected class
   */
  public onClick(): void {
    this.router.navigate(['/course/' + this.course?.courseId]);
  }
}
