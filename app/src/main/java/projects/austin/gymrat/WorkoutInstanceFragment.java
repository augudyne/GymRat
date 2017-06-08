package projects.austin.gymrat;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import projects.austin.gymrat.adapters.WorkoutInstanceDisplayAdapter;
import projects.austin.gymrat.model.Logs.WorkoutInstance;
import projects.austin.gymrat.model.Logs.WorkoutInstanceExercise;
import projects.austin.gymrat.model.Logs.WorkoutInstanceManager;
import projects.austin.gymrat.model.Logs.WorkoutLogManager;
import projects.austin.gymrat.model.Workout.WorkoutManager;
import projects.austin.gymrat.providers.WorkoutLogIO;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WorkoutInstanceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WorkoutInstanceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorkoutInstanceFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_WORKOUT_NAME = "workoutName";
    private static final String TAG_FRAGMENT = "WorkoutInstanceFragment";

    // TODO: Rename and change types of parameters
    private String workoutName;
    private WorkoutInstance currentWorkoutInstance;

    private OnFragmentInteractionListener mListener;

    public WorkoutInstanceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param theWorkoutName given list of strings of the exercises to do.
     * @return A new instance of fragment WorkoutInstanceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WorkoutInstanceFragment newInstance(String theWorkoutName) {
        WorkoutInstanceFragment fragment = new WorkoutInstanceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_WORKOUT_NAME, theWorkoutName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            workoutName = getArguments().getString(ARG_WORKOUT_NAME);
            WorkoutInstanceManager wm = WorkoutInstanceManager.getInstance();
            wm.setCurrentWorkoutInstance(WorkoutManager.getInstance().getWorkout(workoutName));
            this.currentWorkoutInstance = wm.getCurrentWorkoutInstance();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_workout_instance_fragment_layout, container, false);
        WorkoutInstanceDisplayAdapter listViewAdapter = new WorkoutInstanceDisplayAdapter(getContext(),
                R.layout.fragment_workout_instance_row_layout,
                currentWorkoutInstance.getInstanceExerciseList()
        );

        ListView myListView = (ListView) myView.findViewById(R.id.lv_workoutInstance);

        myListView.setAdapter(listViewAdapter);

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //when a row is selected, toggle between the rest timer and workout view
                Log.d(TAG_FRAGMENT, "Row " + i + " was selected");
                WorkoutInstanceDisplayAdapter myAdapter = (WorkoutInstanceDisplayAdapter) adapterView.getAdapter();
                WorkoutInstanceExercise myExercise =  myAdapter.getItem(i);
                Log.d(TAG_FRAGMENT, myExercise.toString());
                if(myExercise != null && myExercise.getRestInterval() != 0){
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_layout_container, TimerFragment.newInstance(myExercise.getRestInterval()));
                    ft.addToBackStack(null);
                    ft.commit();
                } else {
                    //show toast
                    Toast.makeText(getContext(), "No rest interval for this exercise", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //setup the save workout button
        Button saveWorkoutButton = (Button) myView.findViewById(R.id.btn_saveInstance);
        saveWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WorkoutInstance workoutToSave = WorkoutInstanceManager.getInstance().getCurrentWorkoutInstance();
                WorkoutLogManager.getInstance().addWorkoutInstance(workoutToSave);

                Log.d(TAG_FRAGMENT, workoutToSave.toJSONObject().toString());
                WorkoutLogIO.getInstance().writeLogsToFile(getContext());
                Toast.makeText(getContext(), "Saved workout instance " + workoutName, Toast.LENGTH_SHORT).show();
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
