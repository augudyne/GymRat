package projects.austin.gymrat.model.workout.exercise;

import org.json.JSONArray;
import org.json.JSONTokener;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Created by Austin on 2017-06-07.
 */

public class ExerciseManager implements Iterable<Exercise> {
    private static ExerciseManager instance;
    private HashMap<String, Exercise> contents;
    private static final String TAG  = "ExerciseManager";

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

    public JSONArray getInstanceAsJSONArray(){
        JSONArray result = new JSONArray();
        for (Exercise e : contents.values()) {
            result.put(e.toJSONObject());
        }
        Log.d(TAG, result.toString());

        return result;
    }

    public List<String> getExercisesContainsString(String key){
        List<String> result = new ArrayList<>();
        for (String s : contents.keySet()) {
            if(s.toLowerCase().contains(key.toLowerCase())){
                result.add(s);
            }
        }

        return result;
    }
}
