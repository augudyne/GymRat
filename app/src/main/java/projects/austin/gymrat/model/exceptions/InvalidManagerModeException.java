package projects.austin.gymrat.model.exceptions;

/**
 * Created by Austin on 2017-05-27.
 */

public class InvalidManagerModeException extends RuntimeException {
    public InvalidManagerModeException() {
        super();
    }

    public InvalidManagerModeException(String message) {
        super(message);
    }
}
