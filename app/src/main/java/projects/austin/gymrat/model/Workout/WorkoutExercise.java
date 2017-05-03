package projects.austin.gymrat.model.Workout;

import java.util.ArrayList;

import projects.austin.gymrat.model.Workout.Workout;

/**
 * Created by Austin on 2017-05-02.
 * A realized instance of an exercise during a workout
 * Consists of:
 * - Sets of : ExerciseType
 * - Number of Sets
 * - Number of Repetitions per Set
 * - Rest Time
 */

public class WorkoutExercise {
    Exercise exercise;
    int numberOfSets; //numberOfReps.length()
    ArrayList<Integer>  numberOfReps;

    public WorkoutExercise(Exercise exercise, int nSets, ArrayList<Integer> numberOfReps) {
        this.exercise = exercise;
        this.numberOfSets = nSets;
        this.numberOfReps = numberOfReps;
    }

    public int getNumberOfSets() {
        return numberOfSets;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public ArrayList<Integer> getNumberOfReps() {
        return numberOfReps;
    }
}
