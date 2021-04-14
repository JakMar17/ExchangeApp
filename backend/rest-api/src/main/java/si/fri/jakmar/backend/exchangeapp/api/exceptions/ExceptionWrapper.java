package si.fri.jakmar.backend.exchangeapp.api.exceptions;

public class ExceptionWrapper {
    private final String body;

    public ExceptionWrapper(Exception e) {
        this.body = e.getMessage();
    }

    public ExceptionWrapper(String message) {
        this.body = message;
    }

    public String getBody() {
        return body;
    }
}
