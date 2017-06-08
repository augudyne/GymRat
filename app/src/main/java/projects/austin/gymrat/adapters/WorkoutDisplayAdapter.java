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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import projects.austin.gymrat.R;
import projects.austin.gymrat.model.Logs.WorkoutInstanceExercise;
import projects.austin.gymrat.model.Workout.Exercise.Exercise;

/**
 * Created by Austin on 2017-05-02.
 */

public class WorkoutDisplayAdapter extends ArrayAdapter<Exercise>{
    private List<Exercise> exerciseList;

    private class ExerciseHolder{
        TextView title;
        TextView description;
        TextView displayExtraNumberInfo;
    }

    public WorkoutDisplayAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Exercise> objects) {
        super(context, resource, objects);
        exerciseList = objects;
    }

    public WorkoutDisplayAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
        exerciseList = new ArrayList<>();
    }

    @Override
    public void add(@Nullable Exercise object) {
        super.add(object);
        exerciseList.add(object);
        notifyDataSetChanged();
    }

    @Override
    public void remove(@Nullable Exercise object) {
        super.remove(object);
        exerciseList.remove(object);
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
            result.setTag(exerciseHolder);
        } else {
            exerciseHolder = (ExerciseHolder) result.getTag();
        }

        //now set the the values knowing we have all views in our holder
        Exercise myExercise = exerciseList.get(position);
        exerciseHolder.title.setText(myExercise.getName());
        exerciseHolder.description.setText(myExercise.getDescription());

        return result;

    }

    private class msgObject{
        private boolean isDisplaySets;
        private String [] txtToDisplay; //0 is for sets, 1 is for reps

        private msgObject(boolean isDisplaySets, String[] txtToDisplay) {
            this.isDisplaySets = isDisplaySets;
            this.txtToDisplay = txtToDisplay;
        }

        public boolean isDisplaySets() {

            return isDisplaySets;
        }

        public void setDisplaySets(boolean displaySets) {
            isDisplaySets = displaySets;
        }

        public String[] getTxtToDisplay() {
            return txtToDisplay;
        }

        public void setTxtToDisplay(String[] txtToDisplay) {
            this.txtToDisplay = txtToDisplay;
        }

        public void toggleDisplayText(){
            this.isDisplaySets = !this.isDisplaySets;
        }
    }
}
