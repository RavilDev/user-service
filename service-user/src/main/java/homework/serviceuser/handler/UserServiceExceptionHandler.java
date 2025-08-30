package homework.serviceuser.handler;

import homework.serviceuser.exception.UserNotFoundException;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.Collections;
import java.util.Objects;

@RestControllerAdvice
public class UserServiceExceptionHandler {
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<?> handleValidationException(HandlerMethodValidationException ex) {
        String errorMessage = ex.getAllErrors().stream()
                .map(MessageSourceResolvable::getDefaultMessage)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse("Validation failed");

        return ResponseEntity.badRequest()
                .body(Collections.singletonMap("message", errorMessage));
    }


    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(UserNotFoundException ex) {
        String errorMessage = ex.getMessage();
        return ResponseEntity.
                status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap("message", errorMessage));
    }
}
