package projects.austin.gymrat.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Filter;

/**
 * Created by Austin on 2017-05-27.
 */

public class NewExerciseRepsAdapter extends RecyclerView.Adapter<NewExerciseRepsAdapter.RepsViewHolder> {


    ArrayList<Integer> listOfReps;
    public NewExerciseRepsAdapter() {
        this.listOfReps = new ArrayList<>();
    }

    @Override
    public RepsViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        EditText repsText = new EditText(parent.getContext());
        repsText.setInputType(InputType.TYPE_CLASS_NUMBER);
        InputFilter[] filters = {new InputFilter.LengthFilter(2)};
        repsText.setFilters(filters);
        repsText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {

                EditText myView = (EditText) view;
                RepsViewHolder viewTag = (RepsViewHolder) myView.getTag();
                System.out.println("Before: " + listOfReps);
                if(!hasFocus) {
                    //unfocused, update the data;

                    listOfReps.remove(viewTag.getLayoutPosition());
                    try {
                        listOfReps.add(viewTag.getLayoutPosition(), Integer.parseInt(String.format(Locale.CANADA, "%s", myView.getText().toString())));
                    }catch (NumberFormatException nfe){
                        listOfReps.add(viewTag.getLayoutPosition(), 0);
                    }

                } else {
                    ((EditText) view).setText("");
                }
                System.out.println("After: " + listOfReps);
            }
        });
        return new RepsViewHolder(repsText);
    }

    @Override
    public void onBindViewHolder(RepsViewHolder holder, int position) {
        holder.repDisplay.setText(String.format(Locale.CANADA, "%s", listOfReps.get(position).toString()));
    }

    @Override
    public int getItemCount() {
        return listOfReps.size();
    }

    public ArrayList<Integer> getListOfReps(){
        return listOfReps;
    }

    public void addRep(){
        listOfReps.add(listOfReps.size(), 0);
        notifyDataSetChanged();
    }

    public void removeRep(){
        if(listOfReps.size() > 0) {
            listOfReps.remove(listOfReps.size() - 1);
            notifyDataSetChanged();
        }
    }

    public class RepsViewHolder extends RecyclerView.ViewHolder{
        EditText repDisplay;
        public RepsViewHolder(EditText itemView) {
            super(itemView);
            repDisplay = itemView;
            repDisplay.setTag(this);
        }
    }
}
