package projects.austin.gymrat.model.workout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import projects.austin.gymrat.model.workout.logs.WorkoutInstanceExercise;
import projects.austin.gymrat.model.workout.exercise.Exercise;
import projects.austin.gymrat.model.workout.exercise.ExerciseManager;

/**
 * Created by Austin on 2017-05-02.
 * A workout consists of an arbitrary number of Exercises
 */

public class Workout {
    protected String name;
    protected Map<String, Exercise> exercises;
    protected List<String> tags;


    public String getName() {
        return name;
    }

    public List<Exercise> getExerciseList() {
        return Collections.unmodifiableList(new ArrayList<Exercise>(exercises.values()));
    }

    public Map<String, Exercise> getExercises() {
        return Collections.unmodifiableMap(exercises);
    }

    public List<String> getTags() {
        return tags;
    }

    public Workout(String name, List<Exercise> exerciseList, List<String> tags) {
        this.name = name;
        this.exercises = new HashMap<>();
        for (Exercise w : exerciseList) {
            this.exercises.put(w.getName(), w);
        }
        this.tags = tags;
    }



    public boolean hasTag(String tag){
        return tags.contains(tag);
    }

    public void addExercise(WorkoutInstanceExercise exercise){
        this.exercises.put(exercise.getName(), exercise);
    }

    public void removeExercise(WorkoutInstanceExercise exercise) {
        if(this.exercises.containsKey(exercise.getName())){
            this.exercises.remove(exercise.getName());
        }
    }

    public void removeExercise(Exercise exercise){
        if(this.exercises.containsKey(exercise.getName())){
            this.exercises.remove(exercise.getName());
        }
    }



    protected Workout(){
        //empty constructor for children
    }
    public Workout(String jsonInput) {
        try {
            JSONObject myWorkoutObject = new JSONObject(jsonInput);
            //get the name
            String nameBuffer = getNameFromJSON(myWorkoutObject);
            //get the list of exercises
            this.exercises = getWorkoutExercisesFromJSON(myWorkoutObject);
            //get the list of tags
            List<String> listOfTags = getTagsFromJSON(myWorkoutObject);

            this.name = nameBuffer;
            this.exercises = new HashMap<>();
            this.tags = listOfTags;
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    //JSON UTILITY FUNCTIONS
    public JSONObject toJSONObject(){
        JSONObject myJSONObject = new JSONObject();
        try {
            myJSONObject.put("Name", name);
            JSONArray jsonArray = new JSONArray();
            for (String exerciseName : exercises.keySet()) {
                jsonArray.put(exerciseName);
            }
            myJSONObject.put("ExerciseList", jsonArray);
            return myJSONObject;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected static String getNameFromJSON(JSONObject js) {
        String result;
        try {
            result = js.getString("Name");
        } catch (JSONException jse) {
            jse.printStackTrace();
            result = "Default Workout Name";
        }
        return result;
    }

    protected static Map<String,Exercise> getWorkoutExercisesFromJSON(JSONObject jsonObject) throws JSONException {
        JSONArray arrayOfExercises = jsonObject.getJSONArray("ExerciseList");
        Map<String,Exercise> listOfExercises = new HashMap<>();
        for(int i = 0; i < arrayOfExercises.length(); i++) {
            String myExerciseAsString = arrayOfExercises.getString(i).toLowerCase();
            Exercise exerciseInstance = ExerciseManager.getInstance().getExercise(myExerciseAsString);
            listOfExercises.put(myExerciseAsString, exerciseInstance);
        }
        return listOfExercises;
    }

    protected static List<String> getTagsFromJSON(JSONObject jsonObject) throws JSONException {
        JSONArray arrayOfTags = new JSONArray(jsonObject.getJSONArray("Tags"));
        List<String> listOfTags = new ArrayList<>();
        for(int i = 0; i < arrayOfTags.length(); i++) {
            listOfTags.add(arrayOfTags.getString(i));
        }
        return listOfTags;
    }
}
