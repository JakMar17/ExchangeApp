package si.fri.jakmar.backend.exchangeapp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import si.fri.jakmar.backend.exchangeapp.security.filters.AuthenticationFilter;
import si.fri.jakmar.backend.exchangeapp.security.filters.AuthorizationFilter;
import si.fri.jakmar.backend.exchangeapp.security.filters.RefreshJwtTokenFilter;

@Configuration
@EnableWebSecurity
public class ApiSecurity extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${jwt.expiration-time}")
    private Integer expirationTime;
    @Value("${jwt.refresh-time}")
    private Integer refreshTime;
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers("/user/**", "/user/register", "/user/confirm-registration", "/user/reset", "/user/update-password").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(new AuthenticationFilter(authenticationManager(), jwtSecret, expirationTime))
                .addFilter(new AuthorizationFilter(authenticationManager(), jwtSecret))
                .addFilterAfter(new RefreshJwtTokenFilter(expirationTime, jwtSecret, refreshTime), AuthorizationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
