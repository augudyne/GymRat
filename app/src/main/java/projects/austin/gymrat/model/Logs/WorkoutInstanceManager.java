package projects.austin.gymrat.model.Logs;

import android.util.Log;

import projects.austin.gymrat.model.Workout.Workout;

/**
 * Created by Austin on 2017-05-11.
 *
 * Singleton design pattern - only one workout instance at a time
 * We will use this to store our current workout information
 * Will be polled by the Database IO.
 */


public class WorkoutInstanceManager {
    private static final String TAG = "WorkoutInstanceManager";
    public static WorkoutInstanceManager myManager;
    private static WorkoutInstance workoutInstance;
    private static boolean editMode;
    private static boolean isAttached; //true if there is an instance currently attached
    public WorkoutInstanceManager(){
    }

    public static WorkoutInstanceManager getInstance(){
        if (myManager == null){
            myManager = new WorkoutInstanceManager();
        }
        return myManager;
    }

    public void setCurrentWorkoutInstance(Workout workoutToInstantiate){
        workoutInstance = new WorkoutInstance(workoutToInstantiate);
    }


    public WorkoutInstance getCurrentWorkoutInstance(){
        return workoutInstance;
    }


    public void modifyRepCount(String exerciseName, int position, int newRep){
            if (workoutInstance.modifyReps(exerciseName, position, newRep)) {
                Log.d(TAG, "Successfully changed the rep number to " + newRep);
            } else {
                Log.d(TAG, "Did not change the rep number: unhandled");
            }

    }

    public void addRep(String exerciseName, int position, int newRep) {
        Log.d(TAG, "Number Of Sets: " + workoutInstance.addSet(exerciseName, position, newRep));

    }
}
