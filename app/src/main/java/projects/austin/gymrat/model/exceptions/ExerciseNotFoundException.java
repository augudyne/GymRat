package projects.austin.gymrat.model.exceptions;

/**
 * Created by Austin on 2017-05-12.
 */

public class ExerciseNotFoundException extends RuntimeException {
    public ExerciseNotFoundException() {
    }

    public ExerciseNotFoundException(String message) {
        super(message);
    }
}
