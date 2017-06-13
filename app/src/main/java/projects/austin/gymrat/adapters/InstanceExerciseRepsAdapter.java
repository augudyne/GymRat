package projects.austin.gymrat.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.List;
import java.util.Locale;

import projects.austin.gymrat.model.workout.logs.WorkoutInstanceExercise;
import projects.austin.gymrat.model.workout.logs.WorkoutInstanceManager;

/**
 * Created by Austin on 2017-05-11.
 */

public class InstanceExerciseRepsAdapter extends RecyclerView.Adapter<InstanceExerciseRepsAdapter.ViewHolder> {
    private WorkoutInstanceExercise thisExercise;
    private List<Integer> listOfReps;
    private InputFilter[] myInputFilters = new InputFilter[]{
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

    public InstanceExerciseRepsAdapter(WorkoutInstanceExercise thisExercise) {
        this.thisExercise = thisExercise;
        this.listOfReps = this.thisExercise.getListOfReps();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        EditText repInput = makeRepsEditText("0", parent.getContext());
        return new ViewHolder(repInput);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.repInput.setText(String.format(Locale.CANADA, "%s", thisExercise.getListOfReps().get(position).toString()));
        holder.repInput.addTextChangedListener(new repTextWatcher(thisExercise.getName(),position));

    }

    private class repTextWatcher implements TextWatcher{
        private String exerciseName;
        private int myPosition;


        public repTextWatcher(String exerciseName, int position) {
            this.exerciseName = exerciseName;
            this.myPosition = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            WorkoutInstanceManager.getInstance().modifyRepCount(exerciseName, myPosition, Integer.parseInt(editable.toString()));
        }
    }

    public void addSet(Context ctx){
        this.thisExercise.addSet();
        notifyDataSetChanged();
    }

    public void removeSet(Context ctx){
        this.thisExercise.removeSet();
        notifyDataSetChanged();
    }


    private EditText makeRepsEditText(String textToDisplay, Context ctx){
        EditText result = new EditText(ctx);
        result.setText(textToDisplay);
        result.setInputType(InputType.TYPE_CLASS_NUMBER);
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
    @Override
    public int getItemCount() {
        return listOfReps.size();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder{
        EditText repInput;
        public ViewHolder(EditText itemView) {
            super(itemView);
            repInput = itemView;
        }
    }
}
