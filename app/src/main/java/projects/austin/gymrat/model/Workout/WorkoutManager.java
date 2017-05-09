package projects.austin.gymrat.model.Workout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * Created by Austin on 2017-05-02.
 */

public class WorkoutManager implements Iterable<Workout>{
    //singleton design pattern, holds all the workouts that one could do
    private static WorkoutManager instance = null;
    private HashMap<String, Workout> workoutCollection;

    private WorkoutManager() {
        workoutCollection = new HashMap<>();
    }

    public static WorkoutManager getInstance(){
        if(instance == null){
            instance = new WorkoutManager();
        }
        return instance;
    }

    public void addWorkout(Workout workout) {
        workoutCollection.put(workout.getName(), workout);
    }

    public boolean hasWorkout(String key){
        return (workoutCollection.keySet().contains(key));
    }

    public Workout getWorkout(String key){
        if (hasWorkout(key)) {
            return workoutCollection.get(key);
        } else return null;
    }

    public void removeWorkout(String key){
        if (hasWorkout(key)) {
            workoutCollection.remove(key);
        }
    }

    /* Filters the workouts folder for a given tag, unless "all" was specified */
    public List<CharSequence> getWorkoutsAsList(String tag) {
        ArrayList<CharSequence> bufferList = new ArrayList<CharSequence>();
        if (tag.toLowerCase().trim().equals("all")) {
            bufferList.addAll(workoutCollection.keySet());
        } else{
            for (Workout wo : workoutCollection.values()) {
                if (wo.hasTag(tag))
                    bufferList.add(wo.getName());
            }
        }
        return Collections.unmodifiableList(bufferList);
    }



    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        int counter = 0;
        for (Workout wo : workoutCollection.values()) {
            sb.append(String.format(Locale.CANADA,"[%d] %s", ++counter, wo.getName()));
        }
        return sb.toString();
    }


    @Override
    public Iterator<Workout> iterator() {
        return workoutCollection.values().iterator();
    }
}
