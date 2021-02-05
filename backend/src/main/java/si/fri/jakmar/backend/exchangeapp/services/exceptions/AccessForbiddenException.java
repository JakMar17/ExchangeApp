package si.fri.jakmar.backend.exchangeapp.services.exceptions;

public class AccessForbiddenException extends Exception{
    public AccessForbiddenException(String message) {
        super(message);
    }
}
