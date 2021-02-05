package si.fri.jakmar.backend.exchangeapp.api.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import si.fri.jakmar.backend.exchangeapp.api.wrappers.exceptions.ExceptionWrapper;
import si.fri.jakmar.backend.exchangeapp.services.DTOwrappers.users.LoginUserDTO;
import si.fri.jakmar.backend.exchangeapp.services.DTOwrappers.users.RegisterUserDTO;
import si.fri.jakmar.backend.exchangeapp.services.exceptions.AccessForbiddenException;
import si.fri.jakmar.backend.exchangeapp.services.exceptions.DataInvalidException;
import si.fri.jakmar.backend.exchangeapp.services.users.LoginServices;
import si.fri.jakmar.backend.exchangeapp.services.users.RegisterServices;
import si.fri.jakmar.backend.exchangeapp.services.users.exceptions.UserDoesNotExistsException;
import si.fri.jakmar.backend.exchangeapp.services.users.exceptions.UserExistsException;

import java.util.logging.Logger;

@RestController
@RequestMapping("/user/")
@CrossOrigin("*")
public class LoginRegisterApi {

    private static final  Logger logger = Logger.getLogger(LoginRegisterApi.class.getSimpleName());

    @Autowired
    private RegisterServices registerServices;

    @Autowired
    private LoginServices loginServices;

    @PostMapping("register")
    public ResponseEntity<Object> registerNewUser(@RequestBody RegisterUserDTO user) {
        try {
            boolean ok = registerServices.registerNewUser(user);
            return ResponseEntity.ok(ok);
        } catch (UserExistsException e) {
            logger.warning(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ExceptionWrapper(e));
        }
    }

    @PostMapping("login")
    public ResponseEntity<Object> loginUser(@RequestBody LoginUserDTO loginData) {
        try {
            return ResponseEntity.ok(loginServices.loginUser(loginData.getEmail(), loginData.getPassword()));
        } catch (DataInvalidException e) {
            logger.severe(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ExceptionWrapper(e));
        } catch (UserDoesNotExistsException e) {
            logger.warning(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionWrapper(e));
        } catch (AccessForbiddenException e) {
            logger.warning(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ExceptionWrapper(e));
        }
    }

    //reset password
    //confirm mail
}
