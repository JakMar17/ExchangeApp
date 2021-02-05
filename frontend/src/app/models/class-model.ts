import { Notification } from "../components/notifications/models/notification-interface";
import { Assignment } from "./assignment-model";
import { User } from "./user-model";

export interface Course {
  courseId: number;
  title: string;
  guardianMain: User;
  guardians?: User[];
  description: string;
  notifications?: Notification[];
  assignments?: Assignment[];
  classroomURL?: string;

  initialCoins?: number;
  accessLevel?: CourseAccess;
  accessPassword?: string;

  studentWhitelist?: User[];
  studentBlacklist?: User[];

  students?: User[];
}

export interface CourseCategory {
  courseCategoryName: string;
  courses?: Course[];
}

export enum CourseAccess {
  PUBLIC,
  SECURED_PASSWORD,
  SECURED_WHITELIST
}