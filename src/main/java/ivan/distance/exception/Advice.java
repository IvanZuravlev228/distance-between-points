package ivan.distance.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class Advice {
    @ExceptionHandler(InvalidDataPoints.class)
    public ResponseEntity<Response> invalidPointsHandler() {
        Response response = new Response("Latitude values should be in the range "
                + "of -90 to 90 degrees, and longitude values should be "
                + "in the range of -180 to 180 degrees");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConnectionException.class)
    public ResponseEntity<Response> osrmConnectionHandler() {
        return new ResponseEntity<>(new Response("Something went wrong with connection to OSRM"),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NonExistingRoute.class)
    public ResponseEntity<Response> nonExistingRouteHandler() {
        return new ResponseEntity<>(new Response("There is no path to the given points"),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Response> parameterHandler() {
        return new ResponseEntity<>(new Response("There should be 4 parameters"),
                HttpStatus.BAD_REQUEST);
    }
}
