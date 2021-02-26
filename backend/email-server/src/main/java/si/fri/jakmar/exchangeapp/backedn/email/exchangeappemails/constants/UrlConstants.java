package si.fri.jakmar.exchangeapp.backedn.email.exchangeappemails.constants;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public final class UrlConstants {
    //localhost
    public final String PASSWORD_RESET_URL_BASE;
    public final String EMAIL_CONFIRMATION_URL_BASE;

    public UrlConstants(Environment environment) {
        PASSWORD_RESET_URL_BASE = environment.getProperty("static-sites.base.url") + "password-reset?passwordResetId={{id}}&email={{email}}";
        EMAIL_CONFIRMATION_URL_BASE = environment.getProperty("static-sites.base.url") + "email-confirmation?email={{email}}&confirmationId={{id}}";
    }
}
