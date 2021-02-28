package si.fri.jakmar.backend.exchangeapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import si.fri.jakmar.backend.exchangeapp.storage.FileStorageService;

import javax.annotation.Resource;
import java.util.Scanner;

@SpringBootApplication
public class ExchangeAppApplication implements CommandLineRunner {

    @Resource
    private FileStorageService fileStorageService;

    public static void main(String[] args) {
        SpringApplication.run(ExchangeAppApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        fileStorageService.init();
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
