package projects.austin.gymrat.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import projects.austin.gymrat.R;
import projects.austin.gymrat.model.Workout.WorkoutInstanceExercise;

/**
 * Created by Austin on 2017-05-05.
 */

public class WorkoutInstanceDisplayAdapter extends ArrayAdapter<WorkoutInstanceExercise>{
    private ArrayList<WorkoutInstanceExercise> listContents;


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View myResultView =  convertView;

        WorkoutExerciseHolder myWorkoutInfoHolder;
        if(myResultView == null){
            //then initialize and set the tag
            LayoutInflater lyInflater = LayoutInflater.from(getContext());
            myResultView = lyInflater.inflate(R.layout.fragment_workout_instance_row_layout, parent, false);
            myWorkoutInfoHolder = new WorkoutExerciseHolder();
            myWorkoutInfoHolder.workoutExerciseName = (TextView) myResultView.findViewById(R.id.lbl_exerciseTitle);
            myWorkoutInfoHolder.workoutExerciseDesc = (TextView) myResultView.findViewById((R.id.lbl_exerciseTitle));
            myWorkoutInfoHolder.rv_repsDisplay = (RecyclerView) myResultView.findViewById(R.id.rv_instanceRowRecyclerView);
            myWorkoutInfoHolder.btn_addSet = (Button) myResultView.findViewById(R.id.btn_addSet);
            myWorkoutInfoHolder.btn_removeSet = (Button) myResultView.findViewById(R.id.btn_removeSet);
            myWorkoutInfoHolder.position = position;
            myResultView.setTag(myWorkoutInfoHolder);
        } else {
            myWorkoutInfoHolder = (WorkoutExerciseHolder) myResultView.getTag();
        }
        WorkoutInstanceExercise workoutExerciseToDisplay = listContents.get(position);

        //the holder is now ready to receive values to display
        //1. Set the wo name and desc fields
        myWorkoutInfoHolder.workoutExerciseName.setText(workoutExerciseToDisplay.getName());
        myWorkoutInfoHolder.workoutExerciseDesc.setText(workoutExerciseToDisplay.getDescription());
        //2. Build our recycler view
        myWorkoutInfoHolder.rv_repsDisplay.setHasFixedSize(false); // needs to grow with the number of edit texts
        myWorkoutInfoHolder.rv_repsDisplay.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        //3. set the recycler view adapter
        InstanceExerciseRepsAdapter myAdapter = new InstanceExerciseRepsAdapter(workoutExerciseToDisplay);

        myWorkoutInfoHolder.rv_repsDisplay.setAdapter(myAdapter);


        //4. make the button that adds EditTexts to the recycler view
        myWorkoutInfoHolder.btn_addSet.setTag(myAdapter);
        myWorkoutInfoHolder.btn_addSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* On click, do the following
                    1. Add an editText object, that filters for only numbers to the reps display container
                 */
                InstanceExerciseRepsAdapter associatedAdapter = (InstanceExerciseRepsAdapter) view.getTag();
                associatedAdapter.addSet(getContext());
            }
        });
        myWorkoutInfoHolder.btn_removeSet.setTag(myAdapter);
        myWorkoutInfoHolder.btn_removeSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InstanceExerciseRepsAdapter associatedAdapter = (InstanceExerciseRepsAdapter) view.getTag();
                associatedAdapter.removeSet(getContext());
            }
        });

        return myResultView;

    }





    public WorkoutInstanceDisplayAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<WorkoutInstanceExercise> objects) {
        super(context, resource, objects);
        this.listContents = new ArrayList<>(objects);//made a copy, can modify directly

    }

    public WorkoutInstanceDisplayAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
        this.listContents = new ArrayList<>();
    }

    private class WorkoutExerciseHolder{
        TextView workoutExerciseName;
        TextView workoutExerciseDesc;
        RecyclerView rv_repsDisplay;
        Button btn_addSet;
        Button btn_removeSet;
        int position;

    }

}
