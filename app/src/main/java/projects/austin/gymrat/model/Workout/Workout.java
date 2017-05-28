package projects.austin.gymrat.model.Workout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Austin on 2017-05-02.
 * A workout consists of an arbitrary number of Exercises
 */

public class Workout {
    protected String name;
    protected Map<String, WorkoutInstanceExercise> exerciseDictionary;
    protected List<String> tags;


    public String getName() {
        return name;
    }

    public List<WorkoutInstanceExercise> getExerciseList() {
        return Collections.unmodifiableList(new ArrayList<WorkoutInstanceExercise>(exerciseDictionary.values()));
    }

    public Map<String, WorkoutInstanceExercise> getExerciseDictionary() {
        return exerciseDictionary;
    }

    public List<String> getTags() {
        return tags;
    }

    public Workout(String name, List<WorkoutInstanceExercise> exerciseList, List<String> tags) {
        this.name = name;
        this.exerciseDictionary = new HashMap<>();
        for (WorkoutInstanceExercise we : exerciseList) {
            this.exerciseDictionary.put(we.getName(), we);
        }
        this.tags = tags;
    }



    public boolean hasTag(String tag){
        return tags.contains(tag);
    }

    public void addExercise(WorkoutInstanceExercise exercise){
        this.exerciseDictionary.put(exercise.getName(), exercise);
    }

    public void removeExercise(WorkoutInstanceExercise exercise) {
        if(this.exerciseDictionary.containsKey(exercise.getName())){
            this.exerciseDictionary.remove(exercise.getName());
        }
    }

    public void removeExercise(Exercise exercise){
        if(this.exerciseDictionary.containsKey(exercise.getName())){
            this.exerciseDictionary.remove(exercise.getName());
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
            List<WorkoutInstanceExercise> listOfExercises = getWorkoutExercisesFromJSON(myWorkoutObject);
            //get the list of tags
            List<String> listOfTags = getTagsFromJSON(myWorkoutObject);

            this.name = nameBuffer;
            this.exerciseDictionary = new HashMap<>();
            for (WorkoutInstanceExercise we : listOfExercises) {
                this.exerciseDictionary.put(we.getName(), we);
            }
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
            for (WorkoutInstanceExercise we : exerciseDictionary.values()) {
                jsonArray.put(we.toJSONObject());
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

    protected static List<WorkoutInstanceExercise> getWorkoutExercisesFromJSON(JSONObject jsonObject) throws JSONException {
        JSONArray arrayOfExercises = jsonObject.getJSONArray("ExerciseList");
        List<WorkoutInstanceExercise> listOfExercises = new ArrayList<>();
        for(int i = 0; i < arrayOfExercises.length(); i++) {
            String myExerciseAsString = arrayOfExercises.getString(i);
            WorkoutInstanceExercise exerciseInstance = WorkoutInstanceExercise.getWorkoutInstanceExerciseFromJSONString(myExerciseAsString);
            listOfExercises.add(exerciseInstance);
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
