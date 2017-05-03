package projects.austin.gymrat.WorkoutDisplay;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import projects.austin.gymrat.R;
import projects.austin.gymrat.model.Workout.Exercise;
import projects.austin.gymrat.model.Workout.WorkoutExercise;

/**
 * Created by Austin on 2017-05-02.
 */

public class WorkoutDisplayAdapter extends ArrayAdapter<WorkoutExercise>{
    private List<WorkoutExercise> exerciseList;

    private class ExerciseHolder{
        TextView title;
        TextView description;
        TextView displayExtraNumberInfo;
    }

    public WorkoutDisplayAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<WorkoutExercise> objects) {
        super(context, resource, objects);
        exerciseList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View result = convertView;
        ExerciseHolder exerciseHolder = null;

        if(result == null){
            //view was not made yet
            LayoutInflater lyInflater = LayoutInflater.from(getContext());
            result = lyInflater.inflate(R.layout.workout_display_item_row, null);
            exerciseHolder = new ExerciseHolder();
            exerciseHolder.title = (TextView) result.findViewById(R.id.lbl_exName);
            exerciseHolder.description = (TextView) result.findViewById(R.id.lbl_exDesc);
            exerciseHolder.displayExtraNumberInfo = (TextView) result.findViewById(R.id.lbl_exExtra);
            result.setTag(exerciseHolder);
        } else {
            exerciseHolder = (ExerciseHolder) result.getTag();
        }

        //now set the the values knowing we have all views in our holder
        WorkoutExercise myExercise = exerciseList.get(position);
        exerciseHolder.title.setText(myExercise.getExercise().getName());
        exerciseHolder.description.setText(myExercise.getExercise().getDescription());
        exerciseHolder.displayExtraNumberInfo.setText(String.format(Locale.CANADA, "%d",myExercise.getNumberOfSets()));

        return result;

    }
}
