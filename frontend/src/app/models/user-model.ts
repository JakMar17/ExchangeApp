import { Course } from "./class-model";

export interface User {
  email?: string;
  name?: string;
  surname?: string;
  password?: string;
  personalNumber?: string;
  myCourses?: Course[];
  userType?: string;
}

export enum UserType {
  OTHER,
  STUDENT,
  PROFFESOR,
  ADMIN
}

export interface UserCourse {
  user?: User;
  courseUserType?: UserType;
  course?: Course;
  coins?: number;
}