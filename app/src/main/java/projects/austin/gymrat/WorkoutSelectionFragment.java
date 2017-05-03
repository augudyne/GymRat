package projects.austin.gymrat;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import projects.austin.gymrat.model.Workout.WorkoutsManager;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WorkoutSelectionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WorkoutSelectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorkoutSelectionFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_MUSCLE_GROUP = "muscleGroup";

    // TODO: Rename and change types of parameters
    private String muscleGroup;
    private OnFragmentInteractionListener mListener;
    private int check;
    public WorkoutSelectionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param muscleGroup The musclegroup for the workout
     * @return A new instance of fragment WorkoutSelectionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WorkoutSelectionFragment newInstance(String muscleGroup) {
        WorkoutSelectionFragment fragment = new WorkoutSelectionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MUSCLE_GROUP, muscleGroup);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.muscleGroup = getArguments().getString(ARG_MUSCLE_GROUP);
        }
        check = 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_workout_selection, container, false);


        //populate the spinner entries
        Spinner workoutSelection = (Spinner) myView.findViewById(R.id.spn_selectWorkout);
        ArrayList<CharSequence> workoutOptionsList = new ArrayList<>();
        workoutOptionsList.add("Select a workout...");
        workoutOptionsList.addAll(WorkoutsManager.getInstance().getWorkoutsAsList(muscleGroup));
        ArrayAdapter<CharSequence> workoutOptionsAdapter =
                new ArrayAdapter<CharSequence>(getActivity(), R.layout.workout_selection_dropdown_item, workoutOptionsList);
        workoutSelection.setSelection(0, false);
        workoutSelection.setAdapter(workoutOptionsAdapter);
        workoutSelection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (++check > 1 && i != 0) {
                    CharSequence selectedItem = (CharSequence) adapterView.getItemAtPosition(i);
                    Toast.makeText(getActivity(), "Selected workout: " + selectedItem, Toast.LENGTH_LONG).show();
                    //show the fragment
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_layout_container, WorkoutDisplayFragment.newInstance(selectedItem.toString()));
                    ft.commit();
                }
                System.out.println("Making sure that the selection was proccd");
                System.out.println(check);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
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
}
