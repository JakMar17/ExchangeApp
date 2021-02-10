import { User } from 'src/app/models/user-model';

export interface Notification {
  notificationId?: number;
  title: string;
  body: string;
  created: Date;
  author: User;
}
