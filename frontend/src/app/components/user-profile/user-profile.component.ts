import { Component, OnInit } from '@angular/core';
import { User } from 'src/app/models/user-model';
import { UserServiceService } from 'src/app/services/user-service/user-service.service';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.scss']
})
export class UserProfileComponent implements OnInit {

  public user: User | null;
  public showPasswordChangeModal: boolean = false;

  constructor(private userServiece: UserServiceService) {
    this.user = userServiece.userLoggedIn;
  }

  ngOnInit(): void {
  }

  public onModalClosed($event: any): void {
    this.showPasswordChangeModal = false;
  }
}
