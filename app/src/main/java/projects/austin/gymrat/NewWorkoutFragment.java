package projects.austin.gymrat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.CompletionInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import projects.austin.gymrat.adapters.NewExerciseRepsAdapter;
import projects.austin.gymrat.adapters.WorkoutDisplayAdapter;
import projects.austin.gymrat.model.workout.exercise.Exercise;
import projects.austin.gymrat.model.workout.exercise.ExerciseManager;
import projects.austin.gymrat.model.workout.logs.WorkoutInstance;
import projects.austin.gymrat.model.workout.exercise.ExerciseType;
import projects.austin.gymrat.model.workout.logs.WorkoutInstanceExercise;
import projects.austin.gymrat.model.workout.WorkoutManager;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewWorkoutFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewWorkoutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewWorkoutFragment extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_TAG= "tag";

    // TODO: Rename and change types of parameters
    private String tag;

    private OnFragmentInteractionListener mListener;

    public NewWorkoutFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment NewWorkoutFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewWorkoutFragment newInstance(String param1) {
        NewWorkoutFragment fragment = new NewWorkoutFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TAG, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tag = getArguments().getString(ARG_TAG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_add_workout, container, false);
        final EditText nameInput = (EditText) myView.findViewById(R.id.txt_form_WorkoutName);
        nameInput.setHint("Workout Name");
        final ListView exerciseListView = (ListView) myView.findViewById(R.id.lv_form_Exercises);
        final ListView tagsListView = (ListView) myView.findViewById(R.id.lv_form_Tags);
        nameInput.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);


        //construct the buttons and listViews
        setupExerciseList(myView);
        setupTagsList(myView);


        //setup the confirm button
        Button confirmWorkout = (Button) myView.findViewById(R.id.btn_form_Confirm);
        confirmWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nameInput.getText().toString().equals("")){
                    nameInput.requestFocus();
                    Toast.makeText(getContext(), "Please input a name for the workout", Toast.LENGTH_SHORT).show();
                } else if (exerciseListView.getAdapter().getCount() == 0){
                    Toast.makeText(getContext(), "Please add least one exercise", Toast.LENGTH_SHORT).show();
                }
                else {
                    String workoutName = nameInput.getText().toString();
                    List<WorkoutInstanceExercise> listOfExercises = new ArrayList<WorkoutInstanceExercise>();
                    ListAdapter exerciseListAdapter = exerciseListView.getAdapter();
                    for (int i = 0; i < exerciseListAdapter.getCount(); i++) {
                        listOfExercises.add((WorkoutInstanceExercise) exerciseListAdapter.getItem(i));
                    }
                    List<String> listOfTags = new ArrayList<String>();
                    ListAdapter tagsListAdapter = tagsListView.getAdapter();
                    for (int i = 0; i < tagsListAdapter.getCount(); i++) {
                        listOfTags.add((String) tagsListAdapter.getItem(i));
                    }

                    WorkoutManager.getInstance().addWorkout(new WorkoutInstance(workoutName, listOfExercises, listOfTags));
                    Toast.makeText(getContext(), "Successfully added workout...popping backstack", Toast.LENGTH_SHORT).show();
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });

        return myView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void setupExerciseList(View currentView){
        Button addExercise = (Button) currentView.findViewById(R.id.btn_form_AddExercise);
        ListView exerciseListView = (ListView) currentView.findViewById(R.id.lv_form_Exercises);

        final WorkoutDisplayAdapter exerciseListAdapter = new WorkoutDisplayAdapter(currentView.getContext(), R.layout.workout_display_item_row);
        exerciseListView.setAdapter(exerciseListAdapter);

        exerciseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //TODO : Implement modification of an exercise
                System.out.println("Selected item " + i + " in exerciseListView");
                Toast.makeText(view.getContext(), "This feature has not been implemented", Toast.LENGTH_SHORT).show();
            }
        });

        exerciseListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int i, long l) {
                PopupMenu exerciseListMenu = new PopupMenu(getActivity(), view);
                exerciseListMenu.inflate(R.menu.exercise_list_popup_menu);
                exerciseListMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId() == R.id.itm_remove){
                            //remove item was selected
                            exerciseListAdapter.remove((exerciseListAdapter.getItem(i)));
                            return true;
                        }
                        return false;
                    }
                });
                exerciseListMenu.show();
                return false;
            }
        });

        addExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO : Implement adding an exercise activity/dialog
                System.out.println("Clicked on AddExercise");
                Toast.makeText(view.getContext(), "This feature has not been implemented", Toast.LENGTH_SHORT).show();
            }
        });

        addExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View promptView = getActivity().getLayoutInflater().inflate(R.layout.form_add_exercise, null);
                final AutoCompleteTextView nameInput = (AutoCompleteTextView) promptView.findViewById(R.id.txt_ExerciseName);
                final EditText descInput = (EditText) promptView.findViewById(R.id.txt_ExerciseDescription);
                final EditText restInterval = (EditText) promptView.findViewById(R.id.txt_RestInterval);
                final RecyclerView setsView = (RecyclerView) promptView.findViewById(R.id.rv_Sets);
                final Spinner spinnerSelection = (Spinner) promptView.findViewById(R.id.spn_ExerciseType);
                //setup ExerciseType Spinner
                spinnerSelection.setAdapter(new ArrayAdapter<>(getContext(), R.layout.workout_selection_item, ExerciseType.values()));
                //setup sets and reps RecyclerView
                Button addSet = (Button) promptView.findViewById(R.id.btn_AddSet);
                addSet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((NewExerciseRepsAdapter) setsView.getAdapter()).addRep();
                    }
                });
                Button removeSet = (Button) promptView.findViewById(R.id.btn_RemoveSet);
                removeSet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((NewExerciseRepsAdapter) setsView.getAdapter()).removeRep();
                    }
                });
                setsView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                setsView.setAdapter(new NewExerciseRepsAdapter());
                //link the view to the AlertDialog
                builder.setView(promptView);
                builder.setPositiveButton("Add", null);
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });


                //setup the Exercise Name auto-completion (search)
                final ArrayAdapter<String> exerciseNameSuggestionsAdapter = new ArrayAdapter<>(getContext(), R.layout.string_display_layout);
                nameInput.setAdapter(exerciseNameSuggestionsAdapter);
                nameInput.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        String searchString = nameInput.getText().toString();
                        List<String> listOfSuggestions = ExerciseManager.getInstance().getExercisesContainsString(searchString);
                        //set the adapter to display these suggestions
                        exerciseNameSuggestionsAdapter.clear();
                        exerciseNameSuggestionsAdapter.addAll(listOfSuggestions);
                    }
                });

                nameInput.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        //user selected a suggestion, update the information to reflect the exercise found
                        String exerciseString = (String) adapterView.getAdapter().getItem(i);
                        Exercise exerciseFound = ExerciseManager.getInstance().getExercise(exerciseString);
                        descInput.setText(exerciseFound.getDescription());
                        //select the exerciseType in the spinner
                        SpinnerAdapter adapter = spinnerSelection.getAdapter();
                        for(int j = 0; j  < adapter.getCount(); j++ ){
                            if(adapter.getItem(j).equals(exerciseFound.getExerciseType())){
                                spinnerSelection.setSelection(j);
                                break;
                            }
                        }
                    }
                });



                final AlertDialog myDialog = builder.create();
                myDialog.show();
                //Immediate override method to prevent closing the prompt if input is invalid
                myDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                                //when user submits the exercise, do input checking
                                String name = nameInput.getText().toString();
                                String rest = restInterval.getText().toString();
                                String desc = descInput.getText().toString();
                                NewExerciseRepsAdapter myAdapter = (NewExerciseRepsAdapter) setsView.getAdapter();
                                System.out.println(name);
                                System.out.println(rest);
                                System.out.println(desc);
                                System.out.println(myAdapter.getListOfReps());

                                if(name.equals("")|| myAdapter.getListOfReps().contains(0)) {
                                    Toast.makeText(getContext(), "One of the inputs is missing/invalid" , Toast.LENGTH_SHORT).show();
                                } else {
                                    int interval;
                                    if(rest.equals("")) interval = 0;
                                    else interval = Integer.parseInt(rest);
                                    //add the exercise to our workout
                                    WorkoutInstanceExercise buffer = new WorkoutInstanceExercise(name, desc, (ExerciseType) spinnerSelection.getSelectedItem(), myAdapter.getListOfReps(), interval);
                                    System.out.println("Adding exercise: " + buffer.toString());
                                    exerciseListAdapter.add(buffer);

                                    myDialog.dismiss();
                                }
                    }
                });

            }
        });
    }


    private void setupTagsList(View currentView){
        Button addTag = (Button) currentView.findViewById(R.id.btn_form_AddTag);
        final ListView tagsListView = (ListView) currentView.findViewById(R.id.lv_form_Tags);

        final ArrayAdapter<String> tagsListAdapter = new ArrayAdapter<>(currentView.getContext(), R.layout.string_display_layout);
        if(tag != null) tagsListAdapter.add(tag);
        tagsListView.setAdapter(tagsListAdapter);

        tagsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //TODO : Implement modification of a tag
                System.out.println("Selected item " + i + " in tagsListView");
                Toast.makeText(view.getContext(), "This feature has not been implemented", Toast.LENGTH_SHORT).show();
            }
        });

        addTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO : Implement adding an exercise activity/dialog
                System.out.println("Clicked on AddTag");
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                final EditText tagInput = new EditText(getContext());
                tagInput.setHint("Input tag...");
                tagInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);
                builder.setView(tagInput);
                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        tagsListAdapter.add(tagInput.getText().toString());
                        dialogInterface.dismiss();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.show();
            }
        });
    }
}
