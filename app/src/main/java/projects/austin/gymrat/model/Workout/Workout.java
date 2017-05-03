package projects.austin.gymrat.model.Workout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Austin on 2017-05-02.
 * A workout consists of an arbitrary number of Exercises
 */

public class Workout {
    String name;
    List<WorkoutExercise> exerciseList;
    List<String> tags;


    public String getName() {
        return name;
    }

    public List<WorkoutExercise> getExerciseList() {
        return exerciseList;
    }

    public List<String> getTags() {
        return tags;
    }

    public Workout(String name, List<WorkoutExercise> exerciseList, List<String> tags) {
        this.name = name;

        this.exerciseList = exerciseList;
        this.tags = tags;
    }

    public boolean hasTag(String tag){
        return tags.contains(tag);
    }

    public void addExercise(WorkoutExercise exercise){
        this.exerciseList.add(exercise);
    }

    public void removeExercise(WorkoutExercise exercise) {
        if(this.exerciseList.contains(exercise)){
            this.exerciseList.remove(exercise);
        }
    }
}
