import { HttpErrorResponse } from '@angular/common/http';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Assignment, SubmissionCheck } from 'src/app/models/assignment-model';
import { ExceptionWrapper } from 'src/app/models/error/http-response-error';
import { SubmissionFilePair } from 'src/app/models/submission-model';
import { AssignmentService } from 'src/app/services/assignment-service/assignment.service';
import { SubmissionService } from 'src/app/services/submission-service/submission.service';
import { Styles } from 'src/styles';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-submission-add',
  templateUrl: './submission-add.component.html',
  styleUrls: ['./submission-add.component.scss'],
})
export class SubmissionAddComponent implements OnInit {
  @Input() assignment: Assignment;
  @Output() onClose: EventEmitter<Assignment | null> = new EventEmitter();

  public uploadQueue: SubmissionFilePair[] = [];
  public checkboxMyWork: boolean = false;
  public errorMessageNewSubmission: string | null = null;

  public submissionButtonLoading: boolean = false;

  constructor(
    private submissionService: SubmissionService,
    private assignmentService: AssignmentService
  ) {}

  ngOnInit(): void {}

  public handleFileUpload($event: any): void {
    const files: FileList = $event.target.files;
    const filesArray = Array.from(files);
    const inputs = filesArray.filter((e) =>
      e.name.toLowerCase().includes('vhod')
    );
    const outputs = filesArray.filter((e) =>
      e.name.toLowerCase().includes('izhod')
    );

    this.uploadQueue = inputs.map((e) => {
      const output = outputs.find(
        (o) => this.getNumberOfFile(e, true) === this.getNumberOfFile(o, false)
      );

      return {
        inputFile: e,
        outputFile: output ?? null,
        inputName: e.name,
        outputName: output?.name ?? null,
        status: e != null && output != null ? 'READY_UPLOAD' : 'ERROR',
      };
    });
  }

  public onCheckboxMyWorkPressed(): void {
    this.checkboxMyWork = !this.checkboxMyWork;
  }

  public onUploadButtonPressed(): void {
    if (this.uploadQueue == null || this.uploadQueue.length === 0) {
      this.errorMessageNewSubmission = 'Ni kaj oddati';
      return;
    }

    if (!this.checkboxMyWork) {
      this.errorMessageNewSubmission = 'Oddaja mora biti vaše delo.';
      return;
    }

    this.errorMessageNewSubmission = null;

    this.submissionButtonLoading = true;
    this.submissionService
      .uploadFiles(this.uploadQueue, this.assignment)
      .subscribe(
        (data) => {
          this.assignment = data;
          this.closeModal(this.assignment);
          this.resetValues();
        },
        (error: HttpErrorResponse) => {
          console.error(error);

          if (error.status === 406) {
            Swal.fire({
              title:"Napaka",
              text: (error.error as ExceptionWrapper).body,
              icon: "error",
              confirmButtonColor: Styles.info
            });

            this.assignmentService
              .getAssignmentWithSubmissions(this.assignment)
              .subscribe((data) => {
                this.assignment = data;
                this.closeModal(this.assignment);
                this.resetValues();
              });
          }
        }
      );
  }

  public onTableRowDeletePressed(element: SubmissionFilePair): void {
    this.uploadQueue = this.uploadQueue.filter((e) => e !== element);
  }

  private getNumberOfFile(file: File, input: boolean): string {
    let name = file.name;
    name = name.replace(input ? 'vhod' : 'izhod', '');
    return name.replace('.txt', '');
  }

  public closeModal(assignment: Assignment | null): void {
    this.onClose.emit(assignment);
  }

  private resetValues(): void {
    this.uploadQueue = [];
    this.checkboxMyWork = false;
    this.submissionButtonLoading = true;
  }

  public get submissionCheck(): typeof SubmissionCheck {
    return SubmissionCheck;
  }
}
