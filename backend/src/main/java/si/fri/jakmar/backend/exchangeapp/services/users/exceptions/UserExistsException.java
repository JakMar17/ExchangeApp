package si.fri.jakmar.backend.exchangeapp.services.users.exceptions;

public class UserExistsException extends Exception{
    public UserExistsException(String message) {
        super(message);
    }
}
