package si.fri.jakmar.backend.exchangeapp.services.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import si.fri.jakmar.backend.exchangeapp.database.entities.users.UserEntity;
import si.fri.jakmar.backend.exchangeapp.database.repositories.UserRepository;
import si.fri.jakmar.backend.exchangeapp.services.exceptions.DataNotFoundException;

@Component
public class UserServices {

    @Autowired
    private UserRepository userRepository;

    public UserEntity getUserByPersonalNumber(String personalNumber) throws DataNotFoundException {
        var users = userRepository.findUsersByPersonalNumber(personalNumber);
        if(users == null || users.size() == 0)
            throw new DataNotFoundException("Uporabnik s podano številko ne obstaja");
        else
            return users.get(0);
    }

    public UserEntity getUserByEmail(String email) throws DataNotFoundException {
        var users = userRepository.findUsersByEmail(email);
        if(users == null || users.size() == 0)
            throw new DataNotFoundException("Uporabnik s podanim epoštnim naslovom ne obstaja");
        else
            return users.get(0);
    }

    public UserEntity getUserByEmailOrPersonalNumber(String personalNumber, String email) throws DataNotFoundException {
        UserEntity u = null;
        if(personalNumber != null)
            u =  getUserByPersonalNumber(personalNumber);
        else
            u = getUserByEmail(email);

        return u;
    }
}
