package projects.austin.gymrat.providers;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import projects.austin.gymrat.model.Logs.WorkoutInstance;
import projects.austin.gymrat.model.Logs.WorkoutLogManager;

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
    public boolean writeLogsToFile(Context cxt){
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


            WorkoutLogManager workoutLogManager = WorkoutLogManager.getInstance();
            for(int i = 0; i < arrayOfWorkoutInstances.length(); i++) {
                String jsonAsString = arrayOfWorkoutInstances.getString(i);
                WorkoutInstance workoutInstance = WorkoutInstance.newInstance(jsonAsString);
                if (workoutInstance != null) {
                    workoutLogManager.addWorkoutInstance(workoutInstance);
                    Log.d(TAG, workoutInstance.toJSONObject().toString());
                } else {
                    Log.e(TAG, "Invalid JSON File...leaving manager unpopulated");
                    return;
                }
            }

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

}
