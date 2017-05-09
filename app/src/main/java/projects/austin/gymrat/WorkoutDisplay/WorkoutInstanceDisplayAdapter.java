package projects.austin.gymrat.WorkoutDisplay;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import projects.austin.gymrat.R;
import projects.austin.gymrat.model.Workout.Exercise;
import projects.austin.gymrat.model.Workout.ExerciseType;
import projects.austin.gymrat.model.Workout.WorkoutInstanceExercise;

/**
 * Created by Austin on 2017-05-05.
 */

public class WorkoutInstanceDisplayAdapter extends ArrayAdapter<WorkoutInstanceExercise>{
    private ArrayList<WorkoutInstanceExercise> listContents;
    private InputFilter[] myInputFilters;
    private Context myContext;

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View myResultView =  convertView;

        WorkoutExerciseHolder myWorkoutInfoHolder;
        if(myResultView == null){
            //then initialize and set the tag
            LayoutInflater lyInflater = LayoutInflater.from(getContext());
            myResultView = lyInflater.inflate(R.layout.fragment_workout_instance_row_layout, null);
            myWorkoutInfoHolder = new WorkoutExerciseHolder();
            myWorkoutInfoHolder.workoutExerciseName = (TextView) myResultView.findViewById(R.id.lbl_exName);
            myWorkoutInfoHolder.workoutExerciseDesc = (TextView) myResultView.findViewById((R.id.lbl_exDesc));
            myWorkoutInfoHolder.linearLayoutSetsDisplay = (LinearLayout) myResultView.findViewById(R.id.ly_numberOfRepsHolder);
            myWorkoutInfoHolder.addSetButton = (ImageButton) myResultView.findViewById(R.id.ibtn_addSetButton);
            myResultView.setTag(myWorkoutInfoHolder);
        } else {
            myWorkoutInfoHolder = (WorkoutExerciseHolder) myResultView.getTag();
        }
        WorkoutInstanceExercise workoutExerciseToDisplay = listContents.get(position);
        //the holder is now ready to receive values to display
        //1. Set the wo name and desc fields
        myWorkoutInfoHolder.workoutExerciseName.setText(workoutExerciseToDisplay.getName());
        myWorkoutInfoHolder.workoutExerciseDesc.setText(workoutExerciseToDisplay.getDescription());
        //2. Set the button to be able to add sets, inform datastructure that new set is added
        myWorkoutInfoHolder.addSetButton.setTag(position);//set button to know where to get information
        myWorkoutInfoHolder.addSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //add a textbox, as in create the view and add it
                EditText viewToAdd = makeRepsEditText("0");
                LinearLayout ly = (LinearLayout) ((Activity)getContext()).findViewById(R.id.ly_numberOfRepsHolder);
                ly.addView(viewToAdd);
                ImageButton myButton = (ImageButton) view;
                int position = (int) myButton.getTag();
                listContents.get(position).addSet();

            }
        });
        //3. Populate the holder by creating new txtInputViews with the proper default values
        //wipe it clean first
        myWorkoutInfoHolder.linearLayoutSetsDisplay.removeAllViews();
            //i. Create a txtInputObject for each set and save to a list
        ArrayList<EditText> listOfRepsDisplays = new ArrayList<>();
        for(int numberOfReps : workoutExerciseToDisplay.getListOfReps()){
            EditText singleSetRepDisplay = makeRepsEditText(String.format(Locale.CANADA, "%d", numberOfReps));
            listOfRepsDisplays.add(singleSetRepDisplay);
        }
        for (EditText oneRepDisplay : listOfRepsDisplays) {
            myWorkoutInfoHolder.linearLayoutSetsDisplay.addView(oneRepDisplay);
        }


        return myResultView;

    }

    private EditText makeRepsEditText(String textToDisplay){
        EditText result = new EditText(getContext());
        result.setText(textToDisplay);
        result.setFilters(myInputFilters);
        result.setTextSize(30);
        result.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                        EditText me = (EditText) view;
                        ((EditText) view).selectAll();
                return false;
            }
        });
        return result;
    }

    public WorkoutInstanceDisplayAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<WorkoutInstanceExercise> objects) {
        super(context, resource, objects);
        this.listContents = new ArrayList<>(objects);//made a copy, can modify directly
        this.myContext = context;
        this.myInputFilters = new InputFilter[]{
                new InputFilter() {
                    public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
                        StringBuilder sb = new StringBuilder();
                        for (int j = 0; j < charSequence.toString().toCharArray().length; j++) {
                            try {
                                String oneChar = (String) charSequence.subSequence(j, j + 1);
                                int charAsInt = Integer.parseInt(oneChar);
                                sb.append(oneChar);
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                                return "";
                            }
                        }
                        return sb.toString();
                    }
                },

                new InputFilter.LengthFilter(2)
        };
    }

    public List<WorkoutInstanceExercise> getListOfExercises(Activity act){
        List<WorkoutInstanceExercise> result = new ArrayList<>();
        for(int i = 0; i < listContents.size(); i++){
            Exercise myExercise = listContents.get(i);
            String name = myExercise.getName();
            String description = myExercise.getDescription();
            ExerciseType exerciseType = myExercise.getExerciseType();
            List<Integer> listOfReps = getListOfReps(act);
            WorkoutInstanceExercise bufferWorkoutExercise = new WorkoutInstanceExercise(name, description, exerciseType, listOfReps);
            result.add(bufferWorkoutExercise);
        }
        return result;
    }

    public List<Integer> getListOfReps(Activity act){
        //get the view and then look at the number of editTexts
        List<Integer> numberOfReps = new ArrayList<>();
        LinearLayout layout = (LinearLayout) act.findViewById(R.id.ly_numberOfRepsHolder);
        for(int i = 0; i < layout.getChildCount(); i++){
            EditText repInput = (EditText) layout.getChildAt(i);
            int nReps = Integer.parseInt(repInput.getText().toString());
            numberOfReps.add(nReps);
            System.out.println(String.format(Locale.CANADA, "%d found as reps, adding to list", nReps));
        }

        return numberOfReps;

    }


    private class WorkoutExerciseHolder{
        TextView workoutExerciseName;
        TextView workoutExerciseDesc;
        LinearLayout linearLayoutSetsDisplay; //horizontal
        ImageButton addSetButton;
    }

}
