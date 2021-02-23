package si.fri.jakmar.backend.exchangeapp.api.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import si.fri.jakmar.backend.exchangeapp.dtos.users.LoginUserDTO;
import si.fri.jakmar.backend.exchangeapp.dtos.users.RegisterUserDTO;
import si.fri.jakmar.backend.exchangeapp.exceptions.general.AccessForbiddenException;
import si.fri.jakmar.backend.exchangeapp.exceptions.general.DataInvalidException;
import si.fri.jakmar.backend.exchangeapp.services.users.LoginServices;
import si.fri.jakmar.backend.exchangeapp.services.users.RegisterServices;
import si.fri.jakmar.backend.exchangeapp.services.users.exceptions.UserDoesNotExistsException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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
    public ResponseEntity<Object> registerNewUser(@RequestBody RegisterUserDTO user) throws Exception {
        boolean ok = registerServices.registerNewUser(user);
        return ResponseEntity.ok(ok);
    }

    @PostMapping("login")
    public ResponseEntity<Object> loginUser(@RequestBody LoginUserDTO loginData) throws AccessForbiddenException, UserDoesNotExistsException, DataInvalidException {
        var data = loginServices.loginUser(loginData.getEmail(), loginData.getPassword());
        return ResponseEntity.ok(data);
    }

    //reset password
    //confirm mail
}
