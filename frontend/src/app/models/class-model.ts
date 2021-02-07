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
  accessLevel?: string;
  accessPassword?: string;

  studentsBlacklisted?: User[];
  studentsWhitelisted?: User[];

  students?: User[];
  userCanEditCourse?: boolean;
  userHasAdminRights?: boolean;
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