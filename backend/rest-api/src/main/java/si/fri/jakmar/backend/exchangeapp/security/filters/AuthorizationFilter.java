package si.fri.jakmar.backend.exchangeapp.security.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import si.fri.jakmar.backend.exchangeapp.constants.JwtConstants;
import si.fri.jakmar.backend.exchangeapp.security.UserDetailsServiceImpl;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class AuthorizationFilter extends BasicAuthenticationFilter {

    private final String jwtSecret;
    private final UserDetailsServiceImpl userDetailsService;

    public AuthorizationFilter(AuthenticationManager authenticationManager, String jwtSecret, UserDetailsServiceImpl userDetailsService) {
        super(authenticationManager);
        this.jwtSecret = jwtSecret;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(JwtConstants.AUTHORIZATION_HEADER_NAME);
        if (header == null || !header.startsWith(JwtConstants.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(JwtConstants.AUTHORIZATION_HEADER_NAME);
        if (token != null) {
            String user = JWT.require(Algorithm.HMAC512(jwtSecret.getBytes()))
                    .build()
                    .verify(token.replace(JwtConstants.TOKEN_PREFIX, ""))
                    .getSubject();

            if (user != null) {
                return new UsernamePasswordAuthenticationToken(
                        userDetailsService.loadUserByUsername(user),
                        null,
                        new ArrayList<>());
            }
            return null;
        }
        return null;
    }
}
