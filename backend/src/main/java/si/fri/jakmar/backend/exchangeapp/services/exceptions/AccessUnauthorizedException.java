package si.fri.jakmar.backend.exchangeapp.services.exceptions;

public class AccessUnauthorizedException extends Exception {
    public AccessUnauthorizedException(String message) {
        super(message);
    }
}
