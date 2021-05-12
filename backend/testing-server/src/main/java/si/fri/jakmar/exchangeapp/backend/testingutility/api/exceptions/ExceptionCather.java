package si.fri.jakmar.exchangeapp.backend.testingutility.api.exceptions;

import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import si.fri.jakmar.exchangeapp.backend.testingutility.exceptions.CreatingEnvironmentException;
import si.fri.jakmar.exchangeapp.backend.testingutility.exceptions.DataNotFoundException;
import si.fri.jakmar.exchangeapp.backend.testingutility.exceptions.FileException;

@Log
@RestControllerAdvice
public class ExceptionCather {

    @ExceptionHandler(DataNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ExceptionWrapper<String>> handleDataNotFoundException(DataNotFoundException exception) {
        exception.printStackTrace();
        log.warning(exception.getMessage());
        return new ResponseEntity(new ExceptionWrapper<>(exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({FileException.class, CreatingEnvironmentException.class, Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ExceptionWrapper<String>> handleInternalException(Exception exception) {
        exception.printStackTrace();
        log.warning(exception.getMessage());
        return new ResponseEntity(new ExceptionWrapper<>(exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
