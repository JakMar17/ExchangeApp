package si.fri.jakmar.backend.exchangeapp.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

public class JwtConstants {
    public static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    public static final String EXPOSE_HEADER_NAME = "Access-Control-Expose-Headers";
    public static final String REFRESH_TIME_HEADER_NAME = "Refresh-Token-After";
    public static final String TOKEN_PREFIX = "Bearer ";
}
