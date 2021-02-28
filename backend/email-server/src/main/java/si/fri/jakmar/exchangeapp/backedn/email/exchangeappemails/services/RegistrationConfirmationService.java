package si.fri.jakmar.exchangeapp.backedn.email.exchangeappemails.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import si.fri.jakmar.exchangeapp.backedn.email.exchangeappemails.constants.UrlConstants;
import si.fri.jakmar.exchangeapp.backedn.email.exchangeappemails.email.Sender;
import si.fri.jakmar.exchangeapp.backedn.email.exchangeappemails.templates.RegistrationConfirmationTemplateGenerator;

import javax.mail.MessagingException;

@Service
public class RegistrationConfirmationService {

    private static final String subject = "Potrditev registracije";

    @Autowired
    RegistrationConfirmationTemplateGenerator registrationConfirmationTemplateGenerator;

    @Autowired
    Sender sender;

    @Autowired
    UrlConstants urlConstants;

    public void generateAndSendEmail(String email, String registrationId) throws MessagingException {
        String link = urlConstants.EMAIL_CONFIRMATION_URL_BASE.replace("{{email}}", email).replace("{{id}}", registrationId);
        String emailBody = registrationConfirmationTemplateGenerator.createTemplate(email, link);
        sender.sendEmailTemplate(email, subject, emailBody);
    }
}
