package si.fri.jakmar.backend.exchangeapp.api.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import si.fri.jakmar.backend.exchangeapp.database.mysql.entities.users.UserEntity;
import si.fri.jakmar.backend.exchangeapp.dtos.users.RegisterUserDTO;
import si.fri.jakmar.backend.exchangeapp.dtos.users.UserDTO;
import si.fri.jakmar.backend.exchangeapp.exceptions.BadRequestException;
import si.fri.jakmar.backend.exchangeapp.exceptions.MailException;
import si.fri.jakmar.backend.exchangeapp.exceptions.RequestInvalidException;
import si.fri.jakmar.backend.exchangeapp.exceptions.general.AccessForbiddenException;
import si.fri.jakmar.backend.exchangeapp.exceptions.general.DataInvalidException;
import si.fri.jakmar.backend.exchangeapp.exceptions.general.DataNotFoundException;
import si.fri.jakmar.backend.exchangeapp.services.users.LoginServices;
import si.fri.jakmar.backend.exchangeapp.services.users.RegisterServices;
import si.fri.jakmar.backend.exchangeapp.services.users.exceptions.UserDoesNotExistsException;

import java.io.IOException;
import java.util.logging.Logger;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class LoginRegisterApi {

    private static final Logger logger = Logger.getLogger(LoginRegisterApi.class.getSimpleName());

    @Autowired
    private RegisterServices registerServices;

    @Autowired
    private LoginServices loginServices;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/register")
    public ResponseEntity<Boolean> registerNewUser(@RequestBody RegisterUserDTO user, @RequestParam(required = false, defaultValue = "true") Boolean sendEmail) throws Exception {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        boolean ok = registerServices.registerNewUser(user, sendEmail);
        return ResponseEntity.ok(ok);
    }

    @GetMapping
    public ResponseEntity<UserDTO> loginUser(@AuthenticationPrincipal UserEntity userEntity) throws AccessForbiddenException, UserDoesNotExistsException, DataInvalidException {
        var data = loginServices.loginUser(userEntity.getUsername());
        return ResponseEntity.ok(data);
    }

    @PostMapping("/confirm-registration")
    public ResponseEntity<?> confirmRegistration(@RequestParam String confirmationId) throws DataNotFoundException {
        registerServices.confirmEmail(confirmationId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/reset")
    public ResponseEntity<?> resetPasswordRequest(@RequestHeader String email) throws DataNotFoundException, IOException, MailException {
        registerServices.createPasswordResetRequest(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestHeader String email, @RequestHeader String resetId, @RequestHeader String newPassword) throws RequestInvalidException, DataNotFoundException {
        registerServices.resetPasswordForUser(email, resetId, newPassword);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/update-password")
    public ResponseEntity<?> updatedPassword(@AuthenticationPrincipal UserEntity user, @RequestParam String oldPassword, @RequestParam String newPassword) throws BadRequestException {
        registerServices.updatePasswordForUser(user, oldPassword, newPassword);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
