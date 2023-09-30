package ivan.distance.exception;

public class NonExistingRoute extends RuntimeException {
    public NonExistingRoute(String message) {
        super(message);
    }
}
