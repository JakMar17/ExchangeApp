package si.fri.jakmar.backend.exchangeapp.exceptions.general;

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
