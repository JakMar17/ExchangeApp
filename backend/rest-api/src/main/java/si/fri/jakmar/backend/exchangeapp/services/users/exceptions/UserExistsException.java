package si.fri.jakmar.backend.exchangeapp.services.users.exceptions;

/**
 * thrown when given UserEntity already exists in database (but shouldn't)
 */
public class UserExistsException extends Exception{
    public UserExistsException(String message) {
        super(message);
    }
}
