package projects.austin.gymrat.providers;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import projects.austin.gymrat.R;
import projects.austin.gymrat.model.Workout.Exercise.Exercise;
import projects.austin.gymrat.model.Workout.Exercise.ExerciseManager;
import projects.austin.gymrat.model.Workout.Exercise.ExerciseType;

/**
 * Created by Austin on 2017-06-07.
 */

public class ExerciseIO {
    private static ExerciseIO instance;
    private static final String EXERCISE_FILE = "exercise.json";
    private static final int DEFAULT_FILE = R.raw.default_exercises;

    private ExerciseIO(){

    }

    public static ExerciseIO getInstance(){
        if(instance == null){
            instance = new ExerciseIO();
        }
        return instance;
    }

    public void loadExercisesFromDisk(Context ctx){
        try {
            FileInputStream fileInputStream = ctx.openFileInput("exercises.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));
            StringBuilder sb = new StringBuilder();
            String line = "";
            while((line = reader.readLine()) != null){
                sb.append(line);
            }
            populateManagerFromJSONObject(new JSONObject(sb.toString()));


        } catch (IOException ioe){
            System.out.println("Corrupted Exercise File - IOException, loading default...");
            loadDefaultExercises(ctx);
        } catch (JSONException jse){
            System.out.println("Corrupted Exercise File - JSONException, loading default...");
            loadDefaultExercises(ctx);
        } finally{
            //always print the values inside the exercise manager
            System.out.println("ExerciseIO completed, displaying exercises contained in the manager");
           for(Exercise e: ExerciseManager.getInstance()){
               System.out.println(e.toString());
           }
        }
    }

    private void loadDefaultExercises(Context myContext){
        InputStream is = myContext.getResources().openRawResource(DEFAULT_FILE);
        try {
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            //build the return string
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(buffer);
            //build the file
            OutputStream fileOutput = myContext.openFileOutput(EXERCISE_FILE, Context.MODE_PRIVATE);
            fileOutput.write(buffer);
            //close streams
            fileOutput.close();
            outputStream.close();
            is.close();
            populateManagerFromJSONObject(new JSONObject(outputStream.toString()));
        } catch (IOException | JSONException e){
            e.printStackTrace();
        }
    }


    private void populateManagerFromJSONObject(JSONObject object) throws JSONException{
        ExerciseManager exerciseManager = ExerciseManager.getInstance();
        JSONArray jsonExercisesArray = (object.getJSONArray("Exercises"));
        for(int i = 0; i < jsonExercisesArray.length(); i++){
            JSONObject jsonExercise = new JSONObject(jsonExercisesArray.getString(i));
            String name = jsonExercise.getString("Name");
            String desc = jsonExercise.getString("Description");
            ExerciseType type = ExerciseType.getTypeFromString(jsonExercise.getString("Type"));
            Exercise exerciseToAdd = new Exercise(name, desc, type);
            exerciseManager.addExerciseIsUnique(exerciseToAdd);
        }

    }

    //TODO: Save function (database out, save to file)

}
