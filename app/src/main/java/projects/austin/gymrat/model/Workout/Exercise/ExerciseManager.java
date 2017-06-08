package projects.austin.gymrat.model.Workout.Exercise;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Created by Austin on 2017-06-07.
 */

public class ExerciseManager implements Iterable<Exercise> {
    private static ExerciseManager instance;
    private HashMap<String, Exercise> contents;

    private ExerciseManager() {
        contents = new HashMap<>();
    }

    @Override
    public Iterator<Exercise> iterator() {
        return contents.values().iterator();
    }

    public static ExerciseManager getInstance(){
        if(instance == null){
            instance = new ExerciseManager();
        }

        return instance;
    }

    public Exercise getExercise(String exerciseName) throws NoSuchElementException{
        if(contents.containsKey(exerciseName)){
            return contents.get(exerciseName);
        } else
        {
            throw new NoSuchElementException();
        }
    }

    public Map<String,Exercise> getExercises(){
        return Collections.unmodifiableMap(contents);
    }


    /**
     * returns true if added unique element, false otherwise
     */
    public boolean addExerciseIsUnique(Exercise exerciseToAdd){
        if(contents.containsKey(exerciseToAdd.getName())){
            contents.put(exerciseToAdd.getName(), exerciseToAdd);
            return false;
        }else{
            contents.put(exerciseToAdd.getName(), exerciseToAdd);
            return true;
        }
    }
}
