package si.fri.jakmar.backend.exchangeapp.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import si.fri.jakmar.backend.exchangeapp.exceptions.BadRequestException;
import si.fri.jakmar.backend.exchangeapp.exceptions.FileException;
import si.fri.jakmar.backend.exchangeapp.exceptions.RequestInvalidException;
import si.fri.jakmar.backend.exchangeapp.exceptions.general.AccessForbiddenException;
import si.fri.jakmar.backend.exchangeapp.exceptions.general.AccessUnauthorizedException;
import si.fri.jakmar.backend.exchangeapp.exceptions.general.DataNotFoundException;
import si.fri.jakmar.backend.exchangeapp.exceptions.submissions.OverMaximumNumberOfSubmissions;
import si.fri.jakmar.backend.exchangeapp.services.users.exceptions.UserDoesNotExistsException;

import java.util.logging.Logger;

@ControllerAdvice
public class ExceptionCatcher {

    private Logger logger = Logger.getLogger(ExceptionWrapper.class.getSimpleName());

    @ExceptionHandler(value = {DataNotFoundException.class})
    public ResponseEntity<ExceptionWrapper> handleDataNotFoundException(DataNotFoundException exception) {
        logger.warning(exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionWrapper(exception.getMessage()));
    }

    @ExceptionHandler(value = {AccessForbiddenException.class})
    public ResponseEntity<ExceptionWrapper> handleAccessForbiddenException(AccessForbiddenException exception) {
        logger.warning(exception.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ExceptionWrapper(exception.getMessage()));
    }

    @ExceptionHandler(value = {AccessUnauthorizedException.class, UserDoesNotExistsException.class})
    public ResponseEntity<ExceptionWrapper> handleAccessUnauthorizedException(Exception exception) {
        logger.warning(exception.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ExceptionWrapper(exception.getMessage()));
    }

    @ExceptionHandler(value = {FileException.class})
    public ResponseEntity<ExceptionWrapper> handleFileException(Exception exception) {
        logger.warning(exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ExceptionWrapper(exception.getMessage()));
    }

    @ExceptionHandler(value = {OverMaximumNumberOfSubmissions.class})
    public ResponseEntity<ExceptionWrapper> handleOverMaximumNumberOfSubmissionsException(Exception exception) {
        logger.warning(exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new ExceptionWrapper(exception.getMessage()));
    }

    @ExceptionHandler(value = {RequestInvalidException.class})
    public ResponseEntity<ExceptionWrapper> handleRequestInvalidException(Exception exception) {
        logger.warning(exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ExceptionWrapper(exception.getMessage()));
    }

    @ExceptionHandler(value = {BadRequestException.class})
    public ResponseEntity<ExceptionWrapper> handleBadRequestException(Exception exception) {
        logger.severe(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionWrapper(exception.getMessage()));
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<ExceptionWrapper> handleException(Exception exception) {
        logger.severe(exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ExceptionWrapper(exception.getMessage()));
    }


}
