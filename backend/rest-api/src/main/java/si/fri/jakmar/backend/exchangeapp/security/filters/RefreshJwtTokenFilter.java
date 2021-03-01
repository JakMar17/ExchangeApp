package si.fri.jakmar.backend.exchangeapp.security.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import si.fri.jakmar.backend.exchangeapp.constants.JwtConstants;
import si.fri.jakmar.backend.exchangeapp.database.entities.users.UserEntity;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class RefreshJwtTokenFilter extends OncePerRequestFilter {

    private final Integer expirationTime;
    private final String jwtSecret;
    private final Integer refreshTime;

    public RefreshJwtTokenFilter(Integer expirationTime, String jwtSecret, Integer refreshTime) {
        this.expirationTime = expirationTime;
        this.jwtSecret = jwtSecret;
        this.refreshTime = refreshTime;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(JwtConstants.HEADER_STRING);
        if(header == null || !header.startsWith(JwtConstants.TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        var token = header.replace(JwtConstants.TOKEN_PREFIX, "");
        if(tokenShouldBeRefreshed(token)) {
            token = createNewToken(token);
            response.addHeader(JwtConstants.HEADER_STRING, JwtConstants.TOKEN_PREFIX + token);
        }

        filterChain.doFilter(request, response);
    }


    private boolean tokenShouldBeRefreshed(String token) {
        var tokenExpiresAt = JWT.require(Algorithm.HMAC512(jwtSecret.getBytes()))
                .build()
                .verify(token)
                .getExpiresAt();

        var created = tokenExpiresAt.getTime() - expirationTime;

        var currentTime = System.currentTimeMillis();
        var refreshAfter = created + refreshTime;
        var expirationTime = tokenExpiresAt.getTime();

        var shouldRefresh = currentTime >= refreshAfter;
        var isExpired = expirationTime <= currentTime;

        return shouldRefresh && !isExpired;
    }

    private String createNewToken(String token) {
        String user = JWT.require(Algorithm.HMAC512(jwtSecret.getBytes()))
                .build()
                .verify(token.replace(JwtConstants.TOKEN_PREFIX, ""))
                .getSubject();

        return JWT.create()
                .withSubject(user)
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .sign(Algorithm.HMAC512(jwtSecret.getBytes(StandardCharsets.UTF_8)));
    }
}
