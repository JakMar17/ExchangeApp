package si.fri.jakmar.exchangeapp.backedn.email.exchangeappemails.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import si.fri.jakmar.exchangeapp.backedn.email.exchangeappemails.services.PasswordResetService;

import javax.mail.MessagingException;

@RestController
public class ResetPasswordApi {

    @Autowired
    PasswordResetService passwordResetService;

    @PostMapping("password-reset")
    public ResponseEntity resetPassword(@RequestParam String email, @RequestParam String resetId) {
        try {
            passwordResetService.generateAndSendEmail(email, resetId);
            return ResponseEntity.ok().build();
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
