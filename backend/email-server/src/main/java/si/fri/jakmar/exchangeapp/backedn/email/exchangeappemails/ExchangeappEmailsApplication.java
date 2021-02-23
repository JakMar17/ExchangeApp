package si.fri.jakmar.exchangeapp.backedn.email.exchangeappemails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import si.fri.jakmar.exchangeapp.backedn.email.exchangeappemails.email.Sender;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

@SpringBootApplication
public class ExchangeappEmailsApplication implements CommandLineRunner {

    @Autowired
    Sender sender;

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(ExchangeappEmailsApplication.class);
        Scanner scanner = new Scanner(System.in);
        String emailUsername = scanner.nextLine();
        String emailPassword = scanner.nextLine();
        scanner.close();

        Properties properties = new Properties();
        properties.put("spring.mail.username", emailUsername);
        properties.put("spring.mail.password", emailPassword);
        application.setDefaultProperties(properties);

        application.run(args);
    }

    @Override
    public void run(String... args) {
        System.out.println("Sending Email...");
        try {
            sender.sendEmail("jakob.marusic17@gmail.com");
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
        System.out.println("Done");
    }
}
