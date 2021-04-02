package si.fri.jakmar.backend.exchangeapp.database.mysql.entities.submissions;

public enum SubmissionStatus {
    PENDING_REVIEW,
    OK,
    NOK,
    NOK_PLAGIARISM,
    TIMEOUT,
    COMPILE_ERROR
}
