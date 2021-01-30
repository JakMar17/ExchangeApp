import {
  Assignment,
  AssignmentStatus,
  SubmissionCheck,
} from '../models/assignment-model';

const assignments: Assignment[] = [
  {
    title: '1. domača naloga',
    assignmentId: 457,
    classromUrl: 'www.google.com',
    description: 'To je prva domača naloga pri predmetu',
    startDate: new Date(Date.parse('2020-01-01')),
    endDate: new Date(Date.parse('2020-02-01')),
    maxSubmissionsTotal: 30,
    coinsPerSubmission: 2,
    coinsPrice: 2,
    inputDataType: '.txt',
    outputDataType: '.txt',
    submissionCheck: SubmissionCheck.NONE,
    submissionNotify: false,
    status: AssignmentStatus.ACTIVE,
    visible: true,
    noOfSubmisions: 45,
  },
  {
    title: '2. domača naloga',
    assignmentId: 47,
    classromUrl: 'www.google.com',
    description: 'To je prva domača naloga pri predmetu',
    startDate: new Date(Date.parse('2020-02-01')),
    maxSubmissionsTotal: 30,
    coinsPerSubmission: 2,
    coinsPrice: 2,
    inputDataType: '.txt',
    outputDataType: '.txt',
    submissionCheck: SubmissionCheck.NONE,
    submissionNotify: false,
    status: AssignmentStatus.ACTIVE,
    visible: true,
    noOfSubmisions: 45,
  },
];