package projects.austin.gymrat.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import projects.austin.gymrat.R;
import projects.austin.gymrat.model.workout.logs.WorkoutInstance;

/**
 * Created by Austin on 2017-05-08.
 */

public class WorkoutLogDisplayAdapter extends ArrayAdapter<WorkoutInstance> {
    private List<WorkoutInstance> listOfWorkoutInstances;

    private class WorkoutInstanceHolder{
        TextView titleDisplay;
        TextView dateDisplay;
    }

    public WorkoutLogDisplayAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<WorkoutInstance> objects) {
        super(context, resource, objects);
        listOfWorkoutInstances = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        WorkoutInstanceHolder workoutInstanceHolder = null;
        if (convertView == null) {
            //make the view
            LayoutInflater lyInflator = LayoutInflater.from(parent.getContext());
            convertView = lyInflator.inflate(R.layout.workout_log_display_row, null);
            //set the tag
            workoutInstanceHolder = new WorkoutInstanceHolder();
            workoutInstanceHolder.titleDisplay = (TextView) convertView.findViewById(R.id.lbl_workoutName);
            workoutInstanceHolder.dateDisplay = (TextView) convertView.findViewById(R.id.lbl_workoutDate);
            convertView.setTag(workoutInstanceHolder);
        } else {
            workoutInstanceHolder = (WorkoutInstanceHolder) convertView.getTag();
        }

        //set the information
        WorkoutInstance workoutInstance = listOfWorkoutInstances.get(position);
        workoutInstanceHolder.titleDisplay.setText(workoutInstance.getName());
        workoutInstanceHolder.dateDisplay.setText(workoutInstance.getDate().toString());
        return convertView;
    }

}

