package si.fri.jakmar.exchangeapp.backedn.email.exchangeappemails.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import si.fri.jakmar.exchangeapp.backedn.email.exchangeappemails.constants.UrlConstants;
import si.fri.jakmar.exchangeapp.backedn.email.exchangeappemails.email.Sender;
import si.fri.jakmar.exchangeapp.backedn.email.exchangeappemails.templates.PasswordResetTemplateGenerator;

import javax.mail.MessagingException;

@Service
public class PasswordResetService {

    private static final String subject = "Ponastavitev gesla";

    @Autowired
    PasswordResetTemplateGenerator passwordResetTemplateGenerator;

    @Autowired
    Sender sender;

    @Autowired
    UrlConstants urlConstants;

    public void generateAndSendEmail(String email, String resetId) throws MessagingException {
        String emailBody = passwordResetTemplateGenerator.createTemplate(
                email,
                urlConstants.PASSWORD_RESET_URL_BASE.replace("{{id}}", resetId).replace("{{email}}", email)
        );
        sender.sendEmailTemplate(email, subject, emailBody);
    }
}
