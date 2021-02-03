import { Component, OnInit } from '@angular/core';
import { HighlightModule, HIGHLIGHT_OPTIONS } from 'ngx-highlightjs';

@Component({
  selector: 'app-submission-view',
  templateUrl: './submission-view.component.html',
  styleUrls: ['./submission-view.component.scss'],
})
export class SubmissionViewComponent implements OnInit {
  constructor() {}

  code: string = 'class Anupam \n{ \n  public static void main(String...args) \n  { \n    System.out.println("HI this is Anupam Guin "); \n  } \n}';

  ngOnInit(): void {}

  ngAfterViewInit(): void {}
}
