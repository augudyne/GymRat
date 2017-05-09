package projects.austin.gymrat.model.Logs;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import projects.austin.gymrat.model.Workout.Workout;

/**
 * Created by Austin on 2017-05-05.
 */

public class WorkoutLogManager implements Iterable<WorkoutInstance>{
    private static WorkoutLogManager instance;
    private Map<String, WorkoutInstance> listOfWorkouts;

    public static WorkoutLogManager getInstance(){
        if(instance == null){
            instance = new WorkoutLogManager();
        }
        return instance;
    }

    private WorkoutLogManager() {
        this.listOfWorkouts = new TreeMap<>();
    }


    public void addWorkoutInstance(WorkoutInstance workoutInstance) {
        listOfWorkouts.put(workoutInstance.getDate(), workoutInstance);
    }

    public List<WorkoutInstance> getWorkoutLogs(){
        List<WorkoutInstance> result = new ArrayList<>(listOfWorkouts.values());
        return result;
    }


    @Override
    public Iterator<WorkoutInstance> iterator() {
        return listOfWorkouts.values().iterator();
    }

    public JSONArray toJSONArray(){
        JSONArray result = new JSONArray();
        for (Map.Entry<String, WorkoutInstance> entry : listOfWorkouts.entrySet()) {
            JSONObject buffer = new JSONObject();
            try {
                buffer.put("Date", entry.getKey());
                buffer.put("WorkoutInstance", entry.getValue().toJSONObject());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            result.put(buffer);
        }
        return result;
    }


}
