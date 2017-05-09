package projects.austin.gymrat.model.Logs;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import projects.austin.gymrat.model.Workout.Workout;
import projects.austin.gymrat.model.Workout.WorkoutInstanceExercise;

/**
 * Created by Austin on 2017-05-05.
 */

public class WorkoutInstance extends Workout {
    private String date;
    private List<WorkoutInstanceExercise> listOfExercises;

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
     * Constructor for WorkoutInstance, for when the Date is provided by the record (DatabaseIO)
     * @param date the Date on which the WorkoutInstance occurred
     * @param name the name of the workout
     * @param exerciseList the list of execises for this workout
     * @param tags the tags associated with this workout
     */
    private WorkoutInstance(String date, String name, List<WorkoutInstanceExercise> exerciseList, List<String> tags) {
        super();
        super.name = name;
        super.tags = tags;
        this.listOfExercises = exerciseList;
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
