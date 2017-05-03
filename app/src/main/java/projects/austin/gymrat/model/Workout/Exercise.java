package projects.austin.gymrat.model.Workout;

/**
 * Created by Austin on 2017-05-02.
 * A particular exercise has:
 * - A name
 * - (TODO: Name variants)
 * - Description
 * - ExerciseType
 *
 */

public class Exercise {
    private String name;
    private String description;
    private ExerciseType exerciseType;

    public Exercise(String name, String description, ExerciseType exerciseType) {
        this.name = name;
        this.description = description;
        this.exerciseType = exerciseType;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ExerciseType getExerciseType() {
        return exerciseType;
    }
}
