package si.fri.jakmar.backend.exchangeapp.api.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import si.fri.jakmar.backend.exchangeapp.database.repositories.user.UserRepository;

@RestController
@RequestMapping("/user")
public class UserApi {

    @Autowired
    UserRepository userRepository;

    //@GetMapping("/transaction-history")
    //public List<SubmissionDTO>
}
