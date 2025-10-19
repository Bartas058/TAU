package pl.tau.tracking.exception;

public class TrackingNotFoundException extends RuntimeException {
    public TrackingNotFoundException(String message) {
        super(message);
    }
}
