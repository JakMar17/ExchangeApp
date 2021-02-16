drop database ExchangeApp;
create database ExchangeApp;
use ExchangeApp;


/*==============================================================*/
/* Table: assignment                                            */
/*==============================================================*/
create table assignment
(
   assignment_id        int not null auto_increment,
   submission_check_id  int not null,
   course_id            int not null,
   user_id              int not null,
   submission_check_url_id int,
   assignment_title     varchar(100) not null,
   assignment_classroom_url varchar(100),
   assignment_description text,
   start_date           timestamp not null,
   end_date             timestamp,
   max_submissions_total int,
   max_submissions_student int,
   coins_per_submission int not null,
   coins_price          int not null,
   input_data_type      varchar(10) not null,
   output_data_type     varchar(10) not null,
   submission_notify    int not null,
   plagiarism_warning   int,
   plagiarism_level     int,
   visible              int not null,
   assignment_date_created timestamp not null,
   assignment_archived int not null,
   primary key (assignment_id)
);

/*==============================================================*/
/* Table: course                                                */
/*==============================================================*/
create table course
(
   course_id            int not null auto_increment,
   access_password_id   int,
   access_level_id      int not null,
   user_id              int not null,
   course_title         varchar(100) not null,
   course_description   text,
   course_classroom_url  varchar(100),
   initial_coins        int not null,
   course_created       timestamp not null,
   course_archived      int not null,
   primary key (course_id)
);

/*==============================================================*/
/* Table: course_access_level                                   */
/*==============================================================*/
create table course_access_level
(
   access_level_id      int not null auto_increment,
   access_level_description varchar(100) not null,
   primary key (access_level_id)
);

/*==============================================================*/
/* Table: course_access_password                                */
/*==============================================================*/
create table course_access_password
(
   access_password      varchar(100) not null,
   access_password_id   int not null auto_increment,
   course_id            int not null,
   primary key (access_password_id)
);

/*==============================================================*/
/* Table: guardian                                              */
/*==============================================================*/
create table guardian
(
   user_id              int not null,
   course_id            int not null,
   primary key (user_id, course_id)
);

/*==============================================================*/
/* Table: purchase                                              */
/*==============================================================*/
create table purchase
(
   purchase_id          int not null auto_increment,
   submission_id        int not null,
   user_id              int not null,
   purchase_date        timestamp not null,
   primary key (purchase_id)
);

/*==============================================================*/
/* Table: student_blacklist                                     */
/*==============================================================*/
create table student_blacklist
(
   user_id              int not null,
   course_id            int not null,
   primary key (user_id, course_id)
);

/*==============================================================*/
/* Table: user_registration_status                              */
/*==============================================================*/
create table user_registration_status
(
   registration_status_id int not null auto_increment,
   registration_status  varchar(128) not null,
   primary key (registration_status_id)
);

/*==============================================================*/
/* Table: student_signed_in                                     */
/*==============================================================*/
create table student_signed_in
(
   user_id              int not null,
   course_id            int not null,
   primary key (user_id, course_id)
);

/*==============================================================*/
/* Table: student_whitelist                                     */
/*==============================================================*/
create table student_whitelist
(
   user_id              int not null,
   course_id            int not null,
   primary key (user_id, course_id)
);

/*==============================================================*/
/* Table: submission                                            */
/*==============================================================*/
create table submission
(
   submission_id        int not null auto_increment,
   submission_status_id int not null,
   user_id              int not null,
   assignment_id        int not null,
   file_key             varchar(100) not null,
   submission_created   timestamp not null,
   primary key (submission_id)
);

/*==============================================================*/
/* Table: submission_check                                      */
/*==============================================================*/
create table submission_check
(
   submission_check_id  int not null auto_increment,
   submission_check_description varchar(100) not null,
   primary key (submission_check_id)
);

/*==============================================================*/
/* Table: submission_check_url                                  */
/*==============================================================*/
create table submission_check_url
(
   submission_check_url_id int not null auto_increment,
   check_url            varchar(100) not null,
   primary key (submission_check_url_id)
);

/*==============================================================*/
/* Table: submission_status                                     */
/*==============================================================*/
create table submission_status
(
   submission_status_id int not null auto_increment,
   submission_status_description varchar(100) not null,
   primary key (submission_status_id)
);

/*==============================================================*/
/* Table: user                                                  */
/*==============================================================*/
create table user
(
   user_id              int not null auto_increment,
   user_type_id         int not null,
   registration_status_id int not null,
   email                varchar(100) not null,
   name                 varchar(100) not null,
   surname              varchar(100) not null,
   password             varchar(100) not null,
   personal_number      char(8) not null,
   user_created         timestamp not null,
   primary key (user_id)
);

/*==============================================================*/
/* Table: user_type                                             */
/*==============================================================*/
create table user_type
(
   user_type_id         int not null auto_increment,
   user_type_description varchar(100) not null,
   primary key (user_type_id)
);

/*==============================================================*/
/* Table: login                                                 */
/*==============================================================*/
create table login
(
   login_id             int not null,
   user_id              int not null,
   login_date           timestamp not null,
   login_ok             int not null,
   login_ip             char(128),
   primary key (login_id)
);

alter table assignment add constraint FK_assignment_author foreign key (user_id)
      references user (user_id) on delete restrict on update restrict;

alter table assignment add constraint FK_assignment_submission_check foreign key (submission_check_id)
      references submission_check (submission_check_id) on delete restrict on update restrict;

alter table assignment add constraint FK_assignment_submission_url foreign key (submission_check_url_id)
      references submission_check_url (submission_check_url_id) on delete restrict on update restrict;

alter table assignment add constraint FK_course_assignments foreign key (course_id)
      references course (course_id) on delete restrict on update restrict;

alter table course add constraint FK_course_access_policy foreign key (access_level_id)
      references course_access_level (access_level_id) on delete restrict on update restrict;

alter table course add constraint FK_course_passwod foreign key (access_password_id)
      references course_access_password (access_password_id) on delete restrict on update restrict;

alter table course add constraint FK_guardian_main foreign key (user_id)
      references user (user_id) on delete restrict on update restrict;

alter table course_access_password add constraint FK_course_passwod2 foreign key (course_id)
      references course (course_id) on delete restrict on update restrict;

alter table guardian add constraint FK_guardian foreign key (user_id)
      references user (user_id) on delete restrict on update restrict;

alter table guardian add constraint FK_guardian2 foreign key (course_id)
      references course (course_id) on delete restrict on update restrict;

alter table login add constraint FK_user_login foreign key (user_id)
      references user (user_id) on delete restrict on update restrict;

alter table purchase add constraint FK_submission_bought foreign key (submission_id)
      references submission (submission_id) on delete restrict on update restrict;

alter table purchase add constraint FK_user_bought foreign key (user_id)
      references user (user_id) on delete restrict on update restrict;

alter table student_blacklist add constraint FK_student_blacklist foreign key (user_id)
      references user (user_id) on delete restrict on update restrict;

alter table student_blacklist add constraint FK_student_blacklist2 foreign key (course_id)
      references course (course_id) on delete restrict on update restrict;

alter table student_signed_in add constraint FK_student_signed_in foreign key (user_id)
      references user (user_id) on delete restrict on update restrict;

alter table student_signed_in add constraint FK_student_signed_in2 foreign key (course_id)
      references course (course_id) on delete restrict on update restrict;

alter table student_whitelist add constraint FK_student_whitelist foreign key (user_id)
      references user (user_id) on delete restrict on update restrict;

alter table student_whitelist add constraint FK_student_whitelist2 foreign key (course_id)
      references course (course_id) on delete restrict on update restrict;

alter table submission add constraint FK_assignment_submission foreign key (assignment_id)
      references assignment (assignment_id) on delete restrict on update restrict;

alter table submission add constraint FK_status_of_submission foreign key (submission_status_id)
      references submission_status (submission_status_id) on delete restrict on update restrict;

alter table submission add constraint FK_submission_author foreign key (user_id)
      references user (user_id) on delete restrict on update restrict;

alter table user add constraint FK_registration_status foreign key (registration_status_id)
      references user_registration_status (registration_status_id) on delete restrict on update restrict;

alter table user add constraint FK_type_of_user foreign key (user_type_id)
      references user_type (user_type_id) on delete restrict on update restrict;


INSERT INTO ExchangeApp.user_type (user_type_id, user_type_description) VALUES (1, 'ADMIN');
INSERT INTO ExchangeApp.user_type (user_type_id, user_type_description) VALUES (2, 'PROFESSOR');
INSERT INTO ExchangeApp.user_type (user_type_id, user_type_description) VALUES (3, 'STUDENT');

INSERT INTO ExchangeApp.course_access_level (access_level_id, access_level_description) VALUES (1, 'PUBLIC');
INSERT INTO ExchangeApp.course_access_level (access_level_id, access_level_description) VALUES (2, 'PASSWORD');
INSERT INTO ExchangeApp.course_access_level (access_level_id, access_level_description) VALUES (3, 'WHITELIST');

INSERT INTO ExchangeApp.submission_check (submission_check_id, submission_check_description) VALUES (1, 'NONE');
INSERT INTO ExchangeApp.submission_check (submission_check_id, submission_check_description) VALUES (2, 'MANUAL');
INSERT INTO ExchangeApp.submission_check (submission_check_id, submission_check_description) VALUES (3, 'AUTOMATIC');

INSERT INTO ExchangeApp.submission_status (submission_status_id, submission_status_description) VALUES (1, 'PENDING_REVIEW');
INSERT INTO ExchangeApp.submission_status (submission_status_id, submission_status_description) VALUES (2, 'PLAGIARISM');
INSERT INTO ExchangeApp.submission_status (submission_status_id, submission_status_description) VALUES (3, 'OK');
INSERT INTO ExchangeApp.submission_status (submission_status_id, submission_status_description) VALUES (4, 'NOK');

INSERT INTO ExchangeApp.user_registration_status (registration_status_id, registration_status) VALUES (1, 'PENDING_EMAIL');
INSERT INTO ExchangeApp.user_registration_status (registration_status_id, registration_status) VALUES (2, 'REGISTERED');

commit;