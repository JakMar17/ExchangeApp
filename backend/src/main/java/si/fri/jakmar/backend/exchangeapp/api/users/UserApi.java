package si.fri.jakmar.backend.exchangeapp.api.users;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import si.fri.jakmar.backend.exchangeapp.database.entities.users.UserEntity;
import si.fri.jakmar.backend.exchangeapp.database.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users/")
public class UserApi {

    @Autowired
    UserRepository userRepository;

    @GetMapping
    public ResponseEntity<Iterable<UserEntity>> getUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @GetMapping("user")
    public ResponseEntity<UserEntity> getUserByPersonalNumber(@RequestParam String personalNumber) {
        return ResponseEntity.ok(userRepository.findUserByPersonalNumber(personalNumber));
    }
}
