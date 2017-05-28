package projects.austin.gymrat;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TimerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TimerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimerFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_SECONDS = "RestIntervalSeconds";

    // TODO: Rename and change types of parameters
    private int restInterval;
    private MediaPlayer mediaPlayer;

    private OnFragmentInteractionListener mListener;

    public TimerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param seconds the number of seconds between sets
     * @return A new instance of fragment TimerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TimerFragment newInstance(int seconds) {
        TimerFragment fragment = new TimerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECONDS, seconds);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            restInterval = getArguments().getInt(ARG_SECONDS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View resultView = inflater.inflate(R.layout.fragment_timer, container, false);
        final TextView timerDisplay = (TextView) resultView.findViewById(R.id.lbl_timerDisplay);
        mediaPlayer = MediaPlayer.create(getContext(), R.raw.alarm_sound);
        Button resetButton = (Button) resultView.findViewById(R.id.btn_resetTimer);
        final CountDownTimer myTimer = new CountDownTimer(restInterval * 1000 + 100, 1000) {
            @Override
            public void onTick(long l) {
                int secondsLeft = (int) l / 1000;
                System.out.println("Seconds Left: " + secondsLeft);
                int minutes = secondsLeft / 60;
                int seconds = secondsLeft - minutes * 60;
                if (seconds == 0) {
                    String text = minutes + " : 00";
                    timerDisplay.setText(text);
                } else if (seconds < 10){
                    String text = minutes + " : 0" + seconds;
                    timerDisplay.setText(text);
                }else {
                        timerDisplay.setText(minutes + " : " + seconds);
                    }
            }

            @Override
            public void onFinish() {
                timerDisplay.setText("0:00");
                mediaPlayer.start();
            }
        }.start();

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                myTimer.cancel();
                myTimer.start();
            }
        });
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
        mediaPlayer.stop();
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
