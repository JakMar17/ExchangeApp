package si.fri.jakmar.exchangeapp.backedn.email.exchangeappemails.constants;

public final class UrlConstants {
    //localhost
    public static final String PASSWORD_RESET_URL_BASE = "http://localhost:8081/password-reset?passwordResetId={{id}}&email={{email}}";
    public static final String EMAIL_CONFIRMATION_URL_BASE = "http://localhost:8081/email-confirmation?email={{email}}&confirmationId={{id}}";
}
