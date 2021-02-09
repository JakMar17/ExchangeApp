package si.fri.jakmar.backend.exchangeapp.services.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import si.fri.jakmar.backend.exchangeapp.database.repositories.UserRepository;
import si.fri.jakmar.backend.exchangeapp.mappers.UsersMappers;
import si.fri.jakmar.backend.exchangeapp.dtos.users.UserDTO;
import si.fri.jakmar.backend.exchangeapp.services.exceptions.AccessForbiddenException;
import si.fri.jakmar.backend.exchangeapp.services.exceptions.DataInvalidException;
import si.fri.jakmar.backend.exchangeapp.services.users.exceptions.UserDoesNotExistsException;

@Component
public class LoginServices {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UsersMappers usersMappers;

    /**
     * checks email & password combination in database and return UserDTO if combination is OK
     * @param email user's email
     * @param password user's password
     * @return user that corresponds to email & password combination
     * @throws DataInvalidException invalid database state - there are more then one users that corresponds to combination
     * @throws UserDoesNotExistsException invalid username password combination
     * @throws AccessForbiddenException user hasn't respond to confirmation email
     */
    public UserDTO loginUser(String email, String password) throws DataInvalidException, UserDoesNotExistsException, AccessForbiddenException {
        var users = userRepository.getUserByEmailAndPassword(email, password);

        if(users == null || users.size() == 0)
            throw new UserDoesNotExistsException("Neveljaven epoštni naslov ali geslo");
        else if(users.size() > 1)
            throw new DataInvalidException("V bazi obstaja več kot en uporabnik z enako epošto in geslom");
        else if(users.get(0).getRegistrationStatus().getStatus().equals("PENDING_EMAIL"))
            throw new AccessForbiddenException("Uporabnik ni potrdil svojega epoštnega naslova");
        else
            return usersMappers.castUserEntityToUserDTO(users.get(0), true);
    }
}
