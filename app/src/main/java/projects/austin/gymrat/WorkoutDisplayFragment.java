package projects.austin.gymrat;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import projects.austin.gymrat.adapters.WorkoutDisplayAdapter;
import projects.austin.gymrat.model.workout.exercise.Exercise;
import projects.austin.gymrat.model.workout.Workout;

import projects.austin.gymrat.model.workout.WorkoutManager;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WorkoutDisplayFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WorkoutDisplayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorkoutDisplayFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_WORKOUT_NAME = "workoutName";
    private String workoutName;
    private boolean onClickToggle;
    private OnFragmentInteractionListener mListener;

    public WorkoutDisplayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param workoutName The name of the workout
     * @return A new instance of fragment WorkoutDisplayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WorkoutDisplayFragment newInstance(String workoutName) {
        WorkoutDisplayFragment fragment = new WorkoutDisplayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_WORKOUT_NAME, workoutName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            workoutName = getArguments().getString(ARG_WORKOUT_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView =  inflater.inflate(R.layout.fragment_workout_display, container, false);

        //setup the button
        Button startWorkout = (Button) myView.findViewById(R.id.btn_startWorkout);
        startWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.addToBackStack(null);
                ft.replace(R.id.fragment_layout_container, WorkoutInstanceFragment.newInstance(workoutName));
                ft.commit();
            }
        });

        //get the workout
        Workout myWorkout = WorkoutManager.getInstance().getWorkout(workoutName);

        //get the views
        TextView workoutName = (TextView) myView.findViewById(R.id.lbl_workoutName);
        ListView exerciseList = (ListView) myView.findViewById(R.id.lv_exercises);

        //populate the views
        workoutName.setText(myWorkout.getName());
        ArrayAdapter<Exercise> workoutExerciseAdapter = new WorkoutDisplayAdapter(getContext(),
                R.layout.workout_display_item_row, myWorkout.getExerciseList());
        exerciseList.setAdapter(workoutExerciseAdapter);


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
