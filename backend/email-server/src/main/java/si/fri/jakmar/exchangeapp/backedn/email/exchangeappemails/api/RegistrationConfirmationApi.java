package si.fri.jakmar.exchangeapp.backedn.email.exchangeappemails.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import si.fri.jakmar.exchangeapp.backedn.email.exchangeappemails.services.RegistrationConfirmationService;
import si.fri.jakmar.exchangeapp.backedn.email.exchangeappemails.templates.RegistrationConfirmationTemplateGenerator;

import javax.mail.MessagingException;

@RestController
public class RegistrationConfirmationApi {

    @Autowired
    RegistrationConfirmationService registrationConfirmationService;

    @PostMapping("/registration-confirmation")
    public ResponseEntity sendRegistrationConfirmation(@RequestParam String email, @RequestParam String registrationConfirmationId) {
        try {
            registrationConfirmationService.generateAndSendEmail(email, registrationConfirmationId);
            return ResponseEntity.ok().build();
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
