package si.fri.jakmar.exchangeapp.backedn.email.exchangeappemails.email;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import si.fri.jakmar.exchangeapp.backedn.email.exchangeappemails.constants.EmailConstants;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.Charset;

@Component
public class Sender  {
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmailTemplate(String emailTo, String subject, String emailBody) throws MessagingException {
        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);

        helper.setTo(emailTo);
        helper.setSubject(subject);
        helper.setFrom(EmailConstants.emailFrom);
        helper.setText(emailBody, true);

        javaMailSender.send(msg);
    }
}
