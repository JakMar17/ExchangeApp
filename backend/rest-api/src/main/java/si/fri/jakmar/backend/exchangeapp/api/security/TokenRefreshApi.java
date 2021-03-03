package si.fri.jakmar.backend.exchangeapp.api.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import si.fri.jakmar.backend.exchangeapp.constants.JwtConstants;
import si.fri.jakmar.backend.exchangeapp.exceptions.BadRequestException;
import si.fri.jakmar.backend.exchangeapp.exceptions.general.AccessUnauthorizedException;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@RestController
@RequestMapping("/token")
public class TokenRefreshApi {

    @Value("${jwt.expiration-time}")
    private Integer expirationTime;
    @Value("${jwt.refresh-time}")
    private Integer refreshTime;
    @Value("${jwt.secret}")
    private String jwtSecret;


    @GetMapping("/refresh")
    public ResponseEntity<Object> refreshToken(@RequestHeader(name = "Authorization") String bearer) throws BadRequestException, AccessUnauthorizedException {
        if(bearer == null || !bearer.startsWith(JwtConstants.TOKEN_PREFIX))
            throw new BadRequestException("Authorization header is missing or is invalid");

        var token = bearer.replace(JwtConstants.TOKEN_PREFIX, "");

        if(tokenIsExpired(token))
            throw new AccessUnauthorizedException("Token is expired");

        HttpHeaders headers = new HttpHeaders();
        headers.add(JwtConstants.EXPOSE_HEADER_NAME, JwtConstants.AUTHORIZATION_HEADER_NAME);
        headers.add(JwtConstants.AUTHORIZATION_HEADER_NAME, JwtConstants.TOKEN_PREFIX + createNewToken(token));

        return ResponseEntity.ok().headers(headers).build();
    }

    private boolean tokenIsExpired(String token) {
        var expiresAt = JWT.require(Algorithm.HMAC512(jwtSecret.getBytes(StandardCharsets.UTF_8)))
                .build()
                .verify(token)
                .getExpiresAt();

        return expiresAt.getTime() < System.currentTimeMillis();
    }

    private String createNewToken(String oldToken) {
        String subject = JWT.require(Algorithm.HMAC512(jwtSecret.getBytes(StandardCharsets.UTF_8)))
                .build()
                .verify(oldToken)
                .getSubject();

        return JWT.create()
                .withSubject(subject)
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .sign(Algorithm.HMAC512(jwtSecret.getBytes(StandardCharsets.UTF_8)));
    }
}
