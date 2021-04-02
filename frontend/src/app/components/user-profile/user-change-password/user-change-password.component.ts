import { HttpErrorResponse } from '@angular/common/http';
import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { ExceptionWrapper } from 'src/app/models/error/http-response-error';
import { UserServiceService } from 'src/app/services/user-service/user-service.service';
import { Styles } from 'src/styles';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-user-change-password',
  templateUrl: './user-change-password.component.html',
  styleUrls: ['./user-change-password.component.scss'],
})
export class UserChangePasswordComponent implements OnInit {
  @Output()
  private onClose: EventEmitter<any> = new EventEmitter();

  public showLoading: boolean = false;
  public passwordOldInput: string = '';
  public passwordNewInput: string = '';
  public passwordNew2Input: string = '';
  public errorMessage: string | null = null;

  constructor(private userService: UserServiceService) {}

  ngOnInit(): void {}

  public async updatePassword(): Promise<void> {
    this.errorMessage = null;
    if (
      this.passwordNew2Input.length === 0 ||
      this.passwordNewInput.length === 0 ||
      this.passwordOldInput.length === 0
    )
      this.errorMessage = 'Polja morajo biti izpolnjena';
    else if (this.passwordNewInput !== this.passwordNew2Input)
      this.errorMessage = 'Novi gesli se ne ujemata';

    if (this.errorMessage !== null) return null;

    this.showLoading = true;
    this.userService
      .resetUsersPassword(this.passwordOldInput, this.passwordNewInput)
      .subscribe(
        () => {
          Swal.fire({
            title: 'Geslo je bilo uspešno posodobljeno',
            icon: 'success',
            confirmButtonColor: Styles.info,
          }).then(() => this.closeModal());
        },
        (err: HttpErrorResponse) => {
          console.log('error', err);
          const error = err.error as ExceptionWrapper;
          this.errorMessage = error.body;
          this.showLoading = false;
        }
      );
  }

  public closeModal(data: any = null): void {
    this.onClose.emit(data);
  }
}
