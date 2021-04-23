package si.fri.jakmar.backend.exchangeapp.www;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
class StaticSitesConstants {
    public final String EMAIL_CONFIRMATION_BASE_URL;
    public final String PASSWORD_RESET_BASE_URL;
    public final String FRONTEND_BASE_URL;

    public StaticSitesConstants(Environment environment) {
        EMAIL_CONFIRMATION_BASE_URL = environment.getProperty("restapi.base.url") + "user/confirm-registration";
        PASSWORD_RESET_BASE_URL = environment.getProperty("restapi.base.url") + "user/reset-password";
        FRONTEND_BASE_URL = environment.getProperty("frontend.base.url");
    }
}
