package si.fri.jakmar.backend.exchangeapp.services.exceptions;

/**
 * thrown when searched data does not exists
 */
public class DataNotFoundException extends Exception{
    public DataNotFoundException(String message) {
        super(message);
    }

    public DataNotFoundException() {

    }
}
