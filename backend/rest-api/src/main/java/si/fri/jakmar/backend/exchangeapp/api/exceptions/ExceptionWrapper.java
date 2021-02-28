package si.fri.jakmar.backend.exchangeapp.api.exceptions;

public class ExceptionWrapper {
    private String message;

    public ExceptionWrapper(Exception e) {
        this.message = e.getMessage();
    }

    public ExceptionWrapper(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
