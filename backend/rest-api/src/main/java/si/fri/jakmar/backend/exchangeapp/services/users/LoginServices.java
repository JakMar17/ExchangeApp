package si.fri.jakmar.backend.exchangeapp.services.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import si.fri.jakmar.backend.exchangeapp.database.mysql.entities.users.UserRegistrationStatus;
import si.fri.jakmar.backend.exchangeapp.database.mysql.repositories.user.UserRepository;
import si.fri.jakmar.backend.exchangeapp.dtos.users.UserDTO;
import si.fri.jakmar.backend.exchangeapp.exceptions.general.AccessForbiddenException;
import si.fri.jakmar.backend.exchangeapp.exceptions.general.DataInvalidException;
import si.fri.jakmar.backend.exchangeapp.services.users.exceptions.UserDoesNotExistsException;

@Component
public class LoginServices {

    @Autowired
    private UserRepository userRepository;

    /**
     * checks email & password combination in database and return UserDTO if combination is OK
     *
     * @param email user's email
     * @return user that corresponds to email & password combination
     * @throws DataInvalidException       invalid database state - there are more then one users that corresponds to combination
     * @throws UserDoesNotExistsException invalid username password combination
     * @throws AccessForbiddenException   user hasn't respond to confirmation email
     */
    public UserDTO loginUser(String email) throws DataInvalidException, UserDoesNotExistsException, AccessForbiddenException {
        var users = userRepository.findUsersByEmail(email);

        if (users.isEmpty())
            throw new UserDoesNotExistsException("Neveljaven epoštni naslov ali geslo");
        else if (users.get().getRegistrationStatus() == UserRegistrationStatus.PENDING_CONFIRMATION)
            throw new AccessForbiddenException("Uporabnik ni potrdil svojega epoštnega naslova");
        else
            return UserDTO.castFromEntity(users.get(), true, true);
    }
}
