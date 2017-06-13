package projects.austin.gymrat.providers;

import android.content.Context;
import android.util.Log;

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
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import projects.austin.gymrat.model.workout.logs.WorkoutInstance;
import projects.austin.gymrat.model.workout.logs.WorkoutInstanceExercise;
import projects.austin.gymrat.model.workout.logs.WorkoutLogManager;
import projects.austin.gymrat.model.workout.exercise.Exercise;
import projects.austin.gymrat.model.workout.exercise.ExerciseManager;
import projects.austin.gymrat.model.workout.exercise.ExerciseType;

/**
 * Created by Austin on 2017-05-05.
 */

public class WorkoutLogIO {
    private static final String WORKOUT_LOGS_FILE_NAME ="workoutLogs.json";
    private static final String TAG = "WorkoutLogIO";

    private static WorkoutLogIO instance;

    private WorkoutLogIO() {
    }

    public static WorkoutLogIO getInstance() {
        if (instance == null) {
            instance = new WorkoutLogIO();
        }
        return instance;
    }

    /**
     * Fetch the WorkoutLogs file from disk, returning null if not found
     * @param cxt the Context requesting the WorkoutLog
     * @return the JSONArray of logs or null
     */
    public JSONArray getJSONFromFileOrNull(Context cxt) {
        try {
            InputStream fileInput = cxt.openFileInput(WORKOUT_LOGS_FILE_NAME);
            byte[] buffer = new byte[fileInput.available()];
            fileInput.read(buffer);
            OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byteArrayOutputStream.write(buffer);
            fileInput.close();
            byteArrayOutputStream.close();
            return new JSONArray(byteArrayOutputStream.toString());
        } catch (FileNotFoundException e) {
            //return null
            e.printStackTrace();
            return null;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        } catch (JSONException jse) {
            jse.printStackTrace();
            return null;
        }
    }

    /**
     * Saves the current workout logs to file
     * @param cxt The Context that wants to save logs
     * @return true if handled, otherwise false
     */
    public static boolean writeLogsToFile(Context cxt){
        try {
            OutputStream fileOutput = cxt.openFileOutput(WORKOUT_LOGS_FILE_NAME, Context.MODE_PRIVATE);
            String toWrite = WorkoutLogManager.getInstance().toJSONArray().toString();
            fileOutput.write(toWrite.getBytes());
            System.out.println("Writing to File...:\n" + toWrite);
            fileOutput.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("System directory for files must not exist...");
            return false;
        }
    }

    /**
     * Fetch the WorkoutLogs file from disk and load into manager, does not flag errors
     * - Use other version for stability
     * @param cxt the Context requesting the WorkoutLog
     */
    public void loadLogsFromFile(Context cxt){
        //get the file and pass the objects to the WorkoutLogManager
        try {
            InputStream ioStream = cxt.openFileInput(WORKOUT_LOGS_FILE_NAME);
            BufferedReader br = new BufferedReader(new InputStreamReader(ioStream));
            StringBuilder sb = new StringBuilder();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            JSONArray arrayOfWorkoutInstances = new JSONArray(sb.toString());

            populateWorkoutLogManager(arrayOfWorkoutInstances);

        } catch (FileNotFoundException fnf) {
            fnf.printStackTrace();
            Log.d(TAG, "Could not find the specified file");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            Log.d(TAG, "Unable to access the input stream");
        } catch (JSONException jse) {
            jse.printStackTrace();
            Log.d(TAG, "Unable to make JSON from String");
        }
    }


    private void populateWorkoutLogManager(JSONArray array) throws JSONException{

        WorkoutLogManager workoutLogManager = WorkoutLogManager.getInstance();

        for(int i = 0; i < array.length(); i++) {
            JSONObject jsonLogInstance = array.getJSONObject(i);
            Date instanceDate;
            try {
                instanceDate = DateFormat.getDateInstance(DateFormat.LONG, Locale.CANADA).parse(jsonLogInstance.getString("Date"));
            }catch (ParseException pe){
                pe.printStackTrace();
                instanceDate = new Date(System.currentTimeMillis());
            }
            JSONObject workoutObject = jsonLogInstance.getJSONObject("WorkoutInstance");
            String workoutName = workoutObject.getString("Name");
            JSONArray exerciseArray = workoutObject.getJSONArray("ExerciseList");
            List<WorkoutInstanceExercise> instanceExerciseList = new ArrayList<>();
            ExerciseManager manager = ExerciseManager.getInstance();
            for(int j = 0; j < exerciseArray.length(); j++) {
                //parsing workout instance exercise
                JSONObject anExercise = exerciseArray.getJSONObject(j);
                String name = anExercise.getString("Name");
                Exercise parentExercise = manager.getExercise(name);
                String description = parentExercise.getDescription();
                ExerciseType type = parentExercise.getExerciseType();
                JSONArray repsArray = anExercise.getJSONArray("Reps");

                List<Integer> listOfReps = new ArrayList<>();
                for (int k = 0; k < repsArray.length(); k++) {
                    listOfReps.add(repsArray.getInt(k));
                }
                int restInterval = 0;
                try {
                    restInterval = anExercise.getInt("Rest");
                } catch (JSONException jse) {
                    //unable to find the rest
                    jse.printStackTrace();

                }
                instanceExerciseList.add(new WorkoutInstanceExercise(name, description, type, listOfReps, restInterval));
            }

            List<String> tags = new ArrayList<>();
            try{
                JSONArray tagsArray = workoutObject.getJSONArray("Tags");
                for(int j = 0; j < tagsArray.length(); j++){
                    String aTag = tagsArray.getString(j);
                }
            } catch (JSONException jse){
                //just continue
                jse.printStackTrace();
            }

            WorkoutInstance myInstance = new WorkoutInstance(workoutName, instanceExerciseList, tags);

            workoutLogManager.addWorkoutInstance(myInstance);
            Log.d(TAG, myInstance.toJSONObject().toString());

        }

    }


}
