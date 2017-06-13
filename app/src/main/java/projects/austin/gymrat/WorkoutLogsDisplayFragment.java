package projects.austin.gymrat;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import projects.austin.gymrat.adapters.WorkoutLogDisplayAdapter;
import projects.austin.gymrat.model.workout.logs.WorkoutLogManager;
import projects.austin.gymrat.providers.WorkoutLogIO;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WorkoutLogsDisplayFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WorkoutLogsDisplayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorkoutLogsDisplayFragment extends Fragment {


    private OnFragmentInteractionListener mListener;

    public WorkoutLogsDisplayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment WorkoutLogsDisplayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WorkoutLogsDisplayFragment newInstance() {
        WorkoutLogsDisplayFragment fragment = new WorkoutLogsDisplayFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //load the database
        WorkoutLogIO.getInstance().loadLogsFromFile(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View resultView = inflater.inflate(R.layout.fragment_display_logs, container, false);
        //populate the views
        WorkoutLogDisplayAdapter workoutLogDisplayAdapter = new WorkoutLogDisplayAdapter(getContext(),
                R.layout.workout_log_display_row, WorkoutLogManager.getInstance().getWorkoutLogs());
        ListView logsListView = (ListView) resultView.findViewById(R.id.lv_display_logs);
        logsListView.setAdapter(workoutLogDisplayAdapter);

        return resultView;
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
