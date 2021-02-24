package si.fri.jakmar.backend.exchangeapp.constants;

public final class UrlConstants {
    public static final String REGISTRATION_CONFIRMATION_EMAIL_URL = "http://localhost:8082/registration-confirmation?email={{email}}&registrationConfirmationId={{id}}";
    public static final String PASSWORD_RESET_EMAIL_URL = "http://localhost:8082/password-reset?email={{email}}&resetId={{id}}";
}
