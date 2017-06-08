package projects.austin.gymrat.model.Logs;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import projects.austin.gymrat.model.Workout.Exercise.Exercise;
import projects.austin.gymrat.model.Workout.Workout;
import projects.austin.gymrat.model.exceptions.ExerciseNotFoundException;
import projects.austin.gymrat.model.exceptions.InvalidRepIndexException;

/**
 * Created by Austin on 2017-05-05.
 * Same as Workout, but has a date, and the ability to modify its fields
 */


public class WorkoutInstance extends Workout {
    private Date date;
    private Map<String,WorkoutInstanceExercise> exercises;

    /**
     * Constructor for WorkoutInstance, for when the Date is recorded as current time
     * @param name the name of the workout
     * @param exerciseList the list of exercises for this workout
     * @param tags the tags associated with this workout
     */
    public WorkoutInstance(String name, List<WorkoutInstanceExercise> exerciseList, List<String> tags) {
        this(new Date(System.currentTimeMillis()),
                name, exerciseList, tags);
    }

    /**
     * Create a workout instance from a given workout
     *
     * Make a copy of the workout, and then set the date
     * @param workoutSuper the workout to create the instance from

     */
    public WorkoutInstance(Workout workoutSuper){
        super.name = workoutSuper.getName();
        super.exercises = new HashMap<>(workoutSuper.getExercises());
        super.tags = new ArrayList<>(workoutSuper.getTags());
        this.exercises = new HashMap<>();
        for(Exercise e : super.exercises.values()){
            WorkoutInstanceExercise buffer = new WorkoutInstanceExercise(
                    e.getName(),
                    e.getDescription(),
                    e.getExerciseType(),
                    new ArrayList<Integer>(),
                    0
            );
            exercises.put(e.getName(), buffer);
        }
        this.date = (new Date(System.currentTimeMillis()));

    }


    /**
     * Constructor for WorkoutInstance, for when the Date is provided by the record (DatabaseIO)
     * @param date the Date on which the WorkoutInstance occurred
     * @param name the name of the workout
     * @param exerciseList the list of exercises for this workout
     * @param tags the tags associated with this workout
     */
    private WorkoutInstance(Date date, String name, List<WorkoutInstanceExercise> exerciseList, List<String> tags) {
        super();
        super.name = name;
        super.tags = tags;
        this.exercises = new HashMap<>();
        for(WorkoutInstanceExercise wie : exerciseList){
            this.exercises.put(wie.getName(), wie);
        }
        this.date = date;
    }

    public List<WorkoutInstanceExercise> getInstanceExerciseList() {
        return new ArrayList<>(exercises.values());
    }

    public Date getDate() {
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
            exercises.get(exerciseKey).changeRepNumber(index, newValue);
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
        exercises.get(exerciseKey).addSet();
        return exercises.get(exerciseKey).getNumberOfSets();
    }

    @Override
    public JSONObject toJSONObject(){
        JSONObject workoutInstanceAsJSON = new JSONObject();
        try {

            DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.CANADA);
            System.out.println("Date is: " + date);
            workoutInstanceAsJSON.put("Date", dateFormat.format(date));
            //build the workoutInstance json
            JSONObject myselfAsJSONObject = new JSONObject();
            try {
                myselfAsJSONObject.put("Name", name);
                JSONArray jsonArray = new JSONArray();
                for (WorkoutInstanceExercise e : exercises.values()) {
                    //an exercise has a name, its sets, AND its rest Interval
                    jsonArray.put(e.toJSONObject());
                }
                myselfAsJSONObject.put("ExerciseList", jsonArray);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return workoutInstanceAsJSON;
            // date = dateFormat.parse(); to retrieve
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }


}
