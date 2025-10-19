package pl.tau.tracking.advice;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.tau.tracking.error.ErrorResponse;
import pl.tau.tracking.exception.TrackingNotFoundException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class TrackingControllerAdvice {

    @ExceptionHandler(TrackingNotFoundException.class)
    public ResponseEntity<ErrorResponse> trackingNotFound(TrackingNotFoundException exception, HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Not found")
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(404).body(errorResponse);
    }
}
