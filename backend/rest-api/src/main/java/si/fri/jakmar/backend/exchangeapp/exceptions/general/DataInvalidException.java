package si.fri.jakmar.backend.exchangeapp.exceptions.general;

/**
 * thrown when data is in some sort invalid
 */
public class DataInvalidException extends Exception{
    public DataInvalidException(String message) {
        super(message);
    }
}
