package si.fri.jakmar.backend.exchangeapp.services.users.exceptions;

public class UserDoesNotExistsException extends Exception{

    public UserDoesNotExistsException(String message) {
        super(message);
    }
}
