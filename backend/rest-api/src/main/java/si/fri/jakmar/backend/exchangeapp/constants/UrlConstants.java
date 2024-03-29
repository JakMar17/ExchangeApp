package si.fri.jakmar.backend.exchangeapp.constants;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class UrlConstants {
//    za email
    public final String REGISTRATION_CONFIRMATION_EMAIL_URL;
    public final String PASSWORD_RESET_EMAIL_URL;
    public final String TEST_CORRECTNESS_URL;
    public final String TEST_SIMILARITY_ASSIGNMENT_URL;

    public UrlConstants(Environment environment) {
        REGISTRATION_CONFIRMATION_EMAIL_URL = environment.getProperty("email-server.base.url") + "registration-confirmation?email={{email}}&registrationConfirmationId={{id}}";
        PASSWORD_RESET_EMAIL_URL = environment.getProperty("email-server.base.url") + "password-reset?email={{email}}&resetId={{id}}";
        TEST_CORRECTNESS_URL = environment.getProperty("testing-server.base.url") + "correctness/test";
        TEST_SIMILARITY_ASSIGNMENT_URL = environment.getProperty("testing-server.base.url") + "similarity/test";
    }
}
