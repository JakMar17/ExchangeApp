package si.fri.jakmar.backend.exchangeapp.services.users.exceptions;

/**
 * thrown when given UserEntity does not exists in database
 */
public class UserDoesNotExistsException extends Exception{

    public UserDoesNotExistsException(String message) {
        super(message);
    }
}
