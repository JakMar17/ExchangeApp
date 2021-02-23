package si.fri.jakmar.exchangeapp.backedn.email.exchangeappemails.email;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;

@Component
public class Sender  {
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(String to) throws MessagingException, IOException {
        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
        helper.setTo(to);
        helper.setSubject("Testing from Spring Boot");

        var path = new ClassPathResource("templates/password-reset.html");
        String theString = IOUtils.toString(path.getInputStream(), Charset.defaultCharset());
        helper.setFrom("exchangeapp@outlook.com");
        helper.setText(theString, true);

        javaMailSender.send(msg);
    }
}
