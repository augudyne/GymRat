package projects.austin.gymrat.model.Workout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import projects.austin.gymrat.model.exceptions.InvalidRepIndexException;

/**
 * Created by Austin on 2017-05-08.
 */

public class WorkoutInstanceExercise extends Exercise {
    private List<Integer> listOfReps; //The list reps in each set
    private int restInterval; //The rest between sets in seconds
    private int numberOfSets;  //The number of total sets


    public WorkoutInstanceExercise(String name, String description, ExerciseType exerciseType, List<Integer> listOfReps, int restInterval) {
        super(name, description, exerciseType);
        this.listOfReps = listOfReps;
        this.numberOfSets = listOfReps.size();
        this.restInterval = restInterval;
    }


    public WorkoutInstanceExercise(String name, String description, ExerciseType exerciseType, List<Integer> listOfReps) {
        this(name, description, exerciseType, listOfReps, 0);
    }



    public static WorkoutInstanceExercise getWorkoutInstanceExerciseFromJSONString(String workoutInstanceAsJSONString) {
        try {
            JSONObject exerciseJSON = new JSONObject(workoutInstanceAsJSONString);
            String name = exerciseJSON.getString("Name");
            String description = exerciseJSON.getString("Description");
            int restInterval = 0;
            if(exerciseJSON.has("Rest")) restInterval = exerciseJSON.getInt("Rest");
            ExerciseType exerciseType = ExerciseType.getTypeFromString(exerciseJSON.getString("ExerciseType"));
            List<Integer> listOfReps = listOfRepsFromJSONArray(exerciseJSON.getJSONArray("Reps"));
            return new WorkoutInstanceExercise(name, description, exerciseType, listOfReps, restInterval);
        } catch (JSONException jse) {
            jse.printStackTrace();
            return null;
        }
    }
    public List<Integer> getListOfReps() {
        return Collections.unmodifiableList(listOfReps);
    }

    public int getNumberOfSets() {
        return numberOfSets;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder(super.toString()).append("\nReps: ");
        //example:
        // 8, 8, 6, BO, BO
        for(int i = 0; i < listOfReps.size(); i++) {
            sb.append(listOfReps.get(i));
            if(i < (listOfReps.get(i) - 1)){
                sb.append(", ");
            }
        }
        return sb.toString();
    }
    @Override
    public JSONObject toJSONObject(){
        try {
            JSONObject parentJSONObj = super.toJSONObject();
            parentJSONObj.put("Reps", new JSONArray(listOfReps));
            return parentJSONObj;
        } catch (JSONException jse) {
            jse.printStackTrace();
            return null;
        }
    }

    private static List<Integer> listOfRepsFromJSONArray(JSONArray arrayOfReps) throws JSONException{
        List<Integer> listOfReps = new ArrayList<>();
        for(int i = 0; i < arrayOfReps.length();i++) {
            int rep = arrayOfReps.getInt(i);
            listOfReps.add(rep);
        }
        return listOfReps;
    }

    public int getRestInterval() {
        return restInterval;
    }

    public void addSet(){
        numberOfSets++;
        listOfReps.add(0);
    }

    public void removeSet(){
        numberOfSets--;
        listOfReps.remove(listOfReps.size() - 1);
    }

    /**
     * Attempts to change the rep number at reps[index] to newValue
     * @param index the index of the rep (e.g. position in setInputContainer)
     * @param newValue the value to set it to (parse int from the edittext
     */
    public void changeRepNumber(int index, int newValue) throws InvalidRepIndexException {
        if (!(index < listOfReps.size())) {
            throw new InvalidRepIndexException(String.format(Locale.CANADA, "Number of Reps: %d\nIndex Given: %d", listOfReps.size(), index));
        }
        listOfReps.remove(index);
        listOfReps.add(index, newValue);
    }



}
