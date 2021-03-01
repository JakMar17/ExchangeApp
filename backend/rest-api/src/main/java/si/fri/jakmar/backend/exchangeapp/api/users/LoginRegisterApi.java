package si.fri.jakmar.backend.exchangeapp.api.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import si.fri.jakmar.backend.exchangeapp.dtos.users.LoginUserDTO;
import si.fri.jakmar.backend.exchangeapp.dtos.users.RegisterUserDTO;
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
@RequestMapping("/user/")
@CrossOrigin("*")
public class LoginRegisterApi {

    private static final Logger logger = Logger.getLogger(LoginRegisterApi.class.getSimpleName());

    @Autowired
    private RegisterServices registerServices;

    @Autowired
    private LoginServices loginServices;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("register")
    public ResponseEntity<Object> registerNewUser(@RequestBody RegisterUserDTO user) throws Exception {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        boolean ok = registerServices.registerNewUser(user);
        return ResponseEntity.ok(ok);
    }

    @PostMapping("login")
    public ResponseEntity<Object> loginUser(@RequestBody LoginUserDTO loginData) throws AccessForbiddenException, UserDoesNotExistsException, DataInvalidException {
        var data = loginServices.loginUser(loginData.getEmail(), loginData.getPassword());
        return ResponseEntity.ok(data);
    }

    @PostMapping("confirm-registration")
    public ResponseEntity<Object> confirmRegistration(@RequestParam String confirmationId) throws DataNotFoundException {
        registerServices.confirmEmail(confirmationId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("reset")
    public ResponseEntity<Object> resetPasswordRequest(@RequestHeader String email) throws DataNotFoundException, IOException, MailException {
        registerServices.createPasswordResetRequest(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("update-password")
    public ResponseEntity<Object> updatePassword(@RequestHeader String email, @RequestHeader String resetId, @RequestHeader String newPassword) throws RequestInvalidException, DataNotFoundException {
        registerServices.updatePasswordForUser(email, resetId, newPassword);
        return ResponseEntity.ok().build();
    }
}
