package si.fri.jakmar.backend.exchangeapp.constants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
public class UrlConstants {
    public final String REGISTRATION_CONFIRMATION_EMAIL_URL;
    public final String PASSWORD_RESET_EMAIL_URL;

    public UrlConstants(Environment environment) {
        REGISTRATION_CONFIRMATION_EMAIL_URL = environment.getProperty("email-server.base.url") + "registration-confirmation?email={{email}}&registrationConfirmationId={{id}}";
        PASSWORD_RESET_EMAIL_URL = environment.getProperty("email-server.base.url") + "password-reset?email={{email}}&resetId={{id}}";
    }
}
