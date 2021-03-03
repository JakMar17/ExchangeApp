package si.fri.jakmar.backend.exchangeapp.security.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import si.fri.jakmar.backend.exchangeapp.constants.JwtConstants;
import si.fri.jakmar.backend.exchangeapp.database.entities.users.UserEntity;
import si.fri.jakmar.backend.exchangeapp.dtos.users.LoginUserDTO;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    private AuthenticationManager authenticationManager;
    private final Integer expirationTime;
    private final String jwtSecret;
    private final Integer refreshTime;

    public AuthenticationFilter(AuthenticationManager authenticationManager, String jwtSecret, Integer expirationTime, Integer refreshTime) {
        super(authenticationManager);
        this.authenticationManager = authenticationManager;
        this.expirationTime = expirationTime;
        this.jwtSecret = jwtSecret;
        this.refreshTime = refreshTime;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginUserDTO loginUser = new ObjectMapper()
                    .readValue(request.getInputStream(), LoginUserDTO.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginUser.getEmail(),
                            loginUser.getPassword(),
                            new ArrayList<>()
                    )
            );

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String token = JWT.create()
                .withSubject(((UserEntity) authResult.getPrincipal()).getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .sign(Algorithm.HMAC512(jwtSecret.getBytes(StandardCharsets.UTF_8)));

        response.addHeader(
                JwtConstants.EXPOSE_HEADER_NAME,
                String.format("%s, %s", JwtConstants.AUTHORIZATION_HEADER_NAME, JwtConstants.REFRESH_TIME_HEADER_NAME)
        );
        response.addHeader(JwtConstants.AUTHORIZATION_HEADER_NAME, JwtConstants.TOKEN_PREFIX + token);
        response.addHeader(JwtConstants.REFRESH_TIME_HEADER_NAME, refreshTime.toString());
    }


}
