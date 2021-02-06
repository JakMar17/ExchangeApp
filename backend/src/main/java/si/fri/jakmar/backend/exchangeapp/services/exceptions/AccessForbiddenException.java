package si.fri.jakmar.backend.exchangeapp.services.exceptions;

/**
 * thrown when given UserEntity is forbidden to access/view/edit given data
 */
public class AccessForbiddenException extends Exception{
    public AccessForbiddenException(String message) {
        super(message);
    }
}
