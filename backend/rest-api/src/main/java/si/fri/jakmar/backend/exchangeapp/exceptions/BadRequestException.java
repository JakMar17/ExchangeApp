package si.fri.jakmar.backend.exchangeapp.exceptions;

public class BadRequestException extends Exception{
    public BadRequestException(String message) {
        super(message);
    }
}
