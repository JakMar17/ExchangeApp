import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-dashboard-class',
  templateUrl: './dashboard-class.component.html',
  styleUrls: ['./dashboard-class.component.scss']
})
export class DashboardClassComponent implements OnInit {

  constructor(private router: Router) { }

  ngOnInit(): void {
  }

  /**
   * TODO rename to onClassPresed
   * navigate to selected class
   */
  public onClick(): void {
    this.router.navigate(['/class/6']);
  }
}
