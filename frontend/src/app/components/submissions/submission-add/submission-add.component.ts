import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FilesApiService } from 'src/app/api/files-api/files-api.service';
import { SubmissionFilePair } from 'src/app/models/submission-model';

@Component({
  selector: 'app-submission-add',
  templateUrl: './submission-add.component.html',
  styleUrls: ['./submission-add.component.scss'],
})
export class SubmissionAddComponent implements OnInit {
  public uploadQueue: SubmissionFilePair[] = [];

  public checkboxMyWork: boolean = false;

  constructor(private fileApi: FilesApiService, private router: Router) {}

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

  private getNumberOfFile(file: File, input: boolean): string {
    let name = file.name;
    name = name.replace(input ? 'vhod' : 'izhod', '');
    return name.replace('.txt', '');
  }

  public onCheckboxMyWorkPressed(): void {
    this.checkboxMyWork = !this.checkboxMyWork;
    console.log(this.checkboxMyWork);
  }

  public onUploadButtonPressed(): void {
    if (
      !this.checkboxMyWork ||
      this.uploadQueue == null ||
      this.uploadQueue.length === 0
    )
      return;
    this.uploadQueue.forEach((e) =>
      this.fileApi
        .uploadFilePair(e.inputFile, e.outputFile)
        .subscribe((data) => console.log(data))
    );
  }

  public onTableRowDeletePressed(element: SubmissionFilePair): void {
    this.uploadQueue = this.uploadQueue.filter((e) => e !== element);
  }

  public onTableRowViewPressed(element: SubmissionFilePair): void {}
}
