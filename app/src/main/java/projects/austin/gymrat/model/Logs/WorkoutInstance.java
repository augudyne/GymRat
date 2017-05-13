package projects.austin.gymrat.model.Logs;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import projects.austin.gymrat.model.Workout.Workout;
import projects.austin.gymrat.model.Workout.WorkoutInstanceExercise;
import projects.austin.gymrat.model.exceptions.ExerciseNotFoundException;
import projects.austin.gymrat.model.exceptions.InvalidRepIndexException;

/**
 * Created by Austin on 2017-05-05.
 * Same as Workout, but has a date, and the ability to modify its fields
 */


public class WorkoutInstance extends Workout {
    private String date;

    /**
     * Constructor for WorkoutInstance, for when the Date is recorded as current time
     * @param name the name of the workout
     * @param exerciseList the list of exercises for this workout
     * @param tags the tags associated with this workout
     */
    public WorkoutInstance(String name, List<WorkoutInstanceExercise> exerciseList, List<String> tags) {
        super(name, exerciseList, tags);
        this.date = DateFormat.getDateInstance(DateFormat.LONG, Locale.CANADA).format(new Date(System.currentTimeMillis()));
    }

    /**
     * Make a copy of the workout, and then set the date
     * @param workoutSuper the workout to create the instance from

     */
    public WorkoutInstance(Workout workoutSuper){
        super.name = workoutSuper.getName();
        super.exerciseDictionary = workoutSuper.getExerciseDictionary();
        super.tags = workoutSuper.getTags();
        this.date = DateFormat.getDateInstance(DateFormat.LONG, Locale.CANADA).format(new Date(System.currentTimeMillis()));

    }


    /**
     * Constructor for WorkoutInstance, for when the Date is provided by the record (DatabaseIO)
     * @param date the Date on which the WorkoutInstance occurred
     * @param name the name of the workout
     * @param exerciseList the list of execises for this workout
     * @param tags the tags associated with this workout
     */
    private WorkoutInstance(String date, String name, List<WorkoutInstanceExercise> exerciseList, List<String> tags) {
        super(name, exerciseList, tags);
        this.date = date;
    }

    /**
     * Factory design pattern for creating a WorkoutInstance from String input - required for input scrubbing
     * @param instanceAsJSONString the JSONString of the WorkoutInstance from DatabaseIO
     * @return a new instance of WorkoutInstance made from given String or null if invalid input
     */
    public static WorkoutInstance newInstance(String instanceAsJSONString){
        //factory design pattern in order to accommodate input cleaning
        try {
            JSONObject myWorkoutObject = new JSONObject(instanceAsJSONString);
            //get the date
            String date = myWorkoutObject.getString("Date");
            String name = getNameFromJSON(myWorkoutObject);
            List<WorkoutInstanceExercise> listOfExercises = getWorkoutExercisesFromJSON(myWorkoutObject);
            if(listOfExercises == null){
                return null;
                //parent should handle this
            }
            List<String> listOfTags = getTagsFromJSON(myWorkoutObject);
            return new WorkoutInstance(date, name, listOfExercises, listOfTags);
        } catch (JSONException jse) {
            jse.printStackTrace();
            System.out.println("Unable to create the object");
            return null;
        }
    }

    public String getDate() {
        return date;
    }


    /**
     * Attempts to change the number of reps at index x of exercise [key]
     * @param exerciseKey the key of the exercise to change : String
     * @param index the index position of the rep to modify : int
     * @return true if successfully changed, false otherwise. Throws exercise not found exception
     */
    public boolean modifyReps(String exerciseKey, int index, int newValue) throws ExerciseNotFoundException {
        try {
            exerciseDictionary.get(exerciseKey).changeRepNumber(index, newValue);
            return true;
        } catch (InvalidRepIndexException ire){
            //thrown when trying to change rep number, but rep index does not exist
            ire.printStackTrace();
            return false;
        } catch (NullPointerException npe) {
            //thrown when the exercise was not found in the keys
            throw new ExerciseNotFoundException("Unable to find exercise: " + exerciseKey + " in " + name);
        }
    }

    public int addSet(String exerciseKey, int index, int newValue) {
        exerciseDictionary.get(exerciseKey).addSet();
        return exerciseDictionary.get(exerciseKey).getNumberOfSets();
    }

    public JSONObject toJSONObject(Context cxt){
        JSONObject workoutInstanceAsJSON = new JSONObject();
        try {

            DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(cxt);
            workoutInstanceAsJSON.put("Date", dateFormat.format(date));
            workoutInstanceAsJSON.put("Workout", super.toJSONObject());
            return workoutInstanceAsJSON;
            // date = dateFormat.parse(); to retrieve
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }


}
