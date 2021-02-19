import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { User } from 'src/app/models/user-model';

@Component({
  selector: 'app-whitelist-file-modal',
  templateUrl: './whitelist-file-modal.component.html',
  styleUrls: ['./whitelist-file-modal.component.scss'],
})
export class WhitelistFileModalComponent implements OnInit {
  @Output() onClose: EventEmitter<User[] | null> = new EventEmitter();

  public file: File | null = null;
  public processingFile: boolean = false;

  constructor() {}

  ngOnInit(): void {}

  public handleFileUpload($event: any): void {
    const files: FileList = $event.target.files;
    console.log(files);
    this.file = files[0];
  }

  public closeModal(): void {
    this.onClose.emit(null);
  }

  public readFile(): void {
    this.processingFile = true;

    const fileReader = new FileReader();
    fileReader.onload = (e) => {
      const result: string = fileReader.result.toString();
      const lines = result.split('\n');
      const users: User[] = lines.map((element) =>
        element.includes('@') ? { email: element } : { personalNumber: element }
      );

      console.log(users);
      this.onClose.emit(users);
    };

    fileReader.readAsText(this.file);
  }
}
