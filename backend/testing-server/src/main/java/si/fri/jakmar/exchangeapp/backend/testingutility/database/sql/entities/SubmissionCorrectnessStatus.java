package si.fri.jakmar.backend.exchangeapp.database.mysql.entities.submissions;

public enum SubmissionCorrectnessStatus {
    PENDING_REVIEW,
    OK,
    NOK,
    TIMEOUT,
    COMPILE_ERROR
}
