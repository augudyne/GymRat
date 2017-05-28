package projects.austin.gymrat.providers;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;

import projects.austin.gymrat.R;
import projects.austin.gymrat.model.Workout.ExerciseType;
import projects.austin.gymrat.model.Workout.Workout;
import projects.austin.gymrat.model.Workout.WorkoutInstanceExercise;
import projects.austin.gymrat.model.Workout.WorkoutManager;

/**
 * Created by Austin on 2017-05-02.
 * Singleton design
 * Gets and saves the workouts to JSON - if it doesn't exist, we should initialize our defaults
 */

public class WorkoutsIO {
    private static WorkoutsIO instance = null;
    private static final String WORKOUTS_FILE_NAME = "workouts.json";


    private WorkoutsIO() {
    }

    public static WorkoutsIO getInstance() {
        if (instance == null) {
            instance = new WorkoutsIO();
        }
        return instance;
    }


    /**
     * Loaidng the JSON of Workouts stored on disk.
     * @param myContext the context of the request for IO (to access the file)
     * @return the JSONObject on disk or the default workouts from raw assets
     */
    public JSONObject getJSONDisk(Context myContext) {
        try {
            InputStream ioStream = myContext.openFileInput(WORKOUTS_FILE_NAME);
            BufferedReader br = new BufferedReader(new InputStreamReader(ioStream));
            StringBuilder sb = new StringBuilder();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
           return new JSONObject(sb.toString());
        } catch (FileNotFoundException fileNotFoundException) {
            //create the default workout file (add to to resources?)
            System.out.println("File Not Found Exception in WorkoutsIO");
            return getDefaultWorkouts(myContext);
        } catch (IOException ioException) {
            System.out.println("Unable to readline in WorkoutsIO");
            return getDefaultWorkouts(myContext);
        } catch (JSONException jsonException) {
            System.out.println("Read file, but was invalid JSON input, cannot parse as JSON");
            return getDefaultWorkouts(myContext);
        }
    }

    /**
     * Wipes the current database of workouts and replaces it with toLoad
     * @param toLoad the new workoutDatabase to load : JSONARRAY
     * @param cxt the context that is making this request
     */
    public void resetDatabaseFromJSON(JSONArray toLoad, Context cxt){
        /* !! Warning, will wipe and repopulate from JSONArray of workouts !! */
        try {
            for (int i = 0; i < toLoad.length(); i++) {
                JSONObject workoutObject = toLoad.getJSONObject(i);
                //get the name of the workout
                String woName = workoutObject.getString("Name");

                //get the tags for the workout
                JSONArray woTags = workoutObject.getJSONArray("Tags");
                ArrayList<String> bufferTagsList = new ArrayList<>();
                for(int j = 0; j < woTags.length(); j++) {
                    bufferTagsList.add(woTags.getString(j).trim().toLowerCase());
                }

                //get the exercises in the workout
                JSONArray woExercises = workoutObject.getJSONArray("Exercises");
                ArrayList<WorkoutInstanceExercise> bufferList = new ArrayList<>();
                for(int j = 0; j < woExercises.length(); j++) {
                    //parse an exercise, has a name, description, type, number of sets, and a list of the sets
                    JSONObject exerciseObject = woExercises.getJSONObject(j);
                    String name = exerciseObject.getString("Name");
                    String description = exerciseObject.getString("Description");
                    ExerciseType type = ExerciseType.getTypeFromString(exerciseObject.getString("Type"));

                    //get the sets
                    JSONArray exerciseSets = exerciseObject.getJSONArray("Sets");
                    ArrayList<Integer> listOfReps = new ArrayList<>();
                    for(int k = 0; k < exerciseSets.length();k++){
                        listOfReps.add(exerciseSets.getInt(k));
                    }
                    int rest;
                    try {
                         rest = exerciseObject.getInt("Rest");
                    } catch (JSONException jse){
                        rest = 0;
                    }
                    bufferList.add(new WorkoutInstanceExercise(name, description, type , listOfReps, rest));
                }

                //add the workout to the manager
                WorkoutManager.getInstance().addWorkout(new Workout(woName, bufferList, bufferTagsList));
            }
        } catch (JSONException jse){
            System.out.println("Unable to load a workout object from the given JSONArray");
            jse.printStackTrace();
        }
    }

    /* Returns the JSONObject of the default workouts */

    /**
     * Returns the default workouts from raw assets.
     * @param myContext the context that is making this request
     * @return the default workouts as  JSONObject
     */
    private JSONObject getDefaultWorkouts(Context myContext){
        InputStream is = myContext.getResources().openRawResource(R.raw.default_workouts);
        try {
            byte[] buffer = new byte[is.available()];
            is.read(buffer);

            //build the return string
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(buffer);
            //build the file
            OutputStream fileOutput = myContext.openFileOutput(WORKOUTS_FILE_NAME, Context.MODE_PRIVATE);
            fileOutput.write(buffer);
            //close streams
            fileOutput.close();
            outputStream.close();
            is.close();
            return new JSONObject(outputStream.toString());
        } catch (IOException e){
            e.printStackTrace();
            return null;
        } catch (JSONException jse){
            jse.printStackTrace();
            return null;
        }
    }

    /**
     * A wrapper function for fetching from disk and loading into database (composite)
     * @param cxt the context making this request
     * @param resetDefault if true, will override workoutlog with default from raw assets
     *
     * @return true if handled, false otherwise
     */
    public boolean getJSONAndLoad(Context cxt, boolean resetDefault){
        JSONObject toLoad;
        if(resetDefault){
           toLoad = getDefaultWorkouts(cxt);
        } else {
            toLoad = getJSONDisk(cxt);
        }
        if(toLoad == null){
            return false;
        }
        try {
            JSONArray arrayOfWorkouts = toLoad.getJSONArray("Workouts");
            resetDatabaseFromJSON(arrayOfWorkouts, cxt);
            return true;
        }  catch (JSONException jse){
            System.out.println("Was unable to find the field Workouts");
            jse.printStackTrace();

            toLoad = getDefaultWorkouts(cxt);
            try {
                JSONArray arrayOfWorkouts = toLoad.getJSONArray("Workouts");
                resetDatabaseFromJSON(arrayOfWorkouts, cxt);
                return true;
            } catch (NullPointerException | JSONException e){
                e.printStackTrace();
                System.out.println("This is very bad, the default raw workout json is corrupted");
            }
            return false;
        }
    }
}
