package si.fri.jakmar.exchangeapp.backedn.email.exchangeappemails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import si.fri.jakmar.exchangeapp.backedn.email.exchangeappemails.email.Sender;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

@SpringBootApplication
public class ExchangeappEmailsApplication {

    @Autowired
    Sender sender;

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(ExchangeappEmailsApplication.class);
        application.run(args);
    }

    @Bean
    public CommonsRequestLoggingFilter requestLoggingFilter() {
        CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
        loggingFilter.setIncludeClientInfo(true);
        loggingFilter.setIncludeQueryString(true);
        loggingFilter.setIncludePayload(true);
        loggingFilter.setIncludeHeaders(false);
        return loggingFilter;
    }

}
