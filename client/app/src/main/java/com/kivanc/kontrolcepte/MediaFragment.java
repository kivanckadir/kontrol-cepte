package com.kivanc.kontrolcepte;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.PrintWriter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MediaFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MediaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MediaFragment extends Fragment implements View.OnTouchListener, View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private ImageButton stop;
    private ImageButton previous;
    private ImageButton playPause;
    private ImageButton next;

    private ImageView mute;
    private ImageView volumeDown;
    private ImageView volumeUp;

    private SeekBar volumeBar;
    private TextView volumeTextView;
    private PrintWriter out;

    private int volumeValue = 50;

    public MediaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MediaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MediaFragment newInstance(String param1, String param2) {
        MediaFragment fragment = new MediaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_media, container, false);

        stop = (ImageButton) view.findViewById(R.id.btn_stop);
        previous = (ImageButton) view.findViewById(R.id.btn_previous);
        playPause = (ImageButton) view.findViewById(R.id.btn_play_pause);
        next = (ImageButton) view.findViewById(R.id.btn_next);
        mute = (ImageView) view.findViewById(R.id.btn_mute);
        volumeDown = (ImageView) view.findViewById(R.id.btn_volume_down);
        volumeUp = (ImageView) view.findViewById(R.id.btn_volume_up);
        volumeBar = (SeekBar) view.findViewById(R.id.volumeBar);
        volumeTextView = (TextView) view.findViewById(R.id.volumeTextView);

        stop.setOnClickListener(this);
        previous.setOnClickListener(this);
        playPause.setOnClickListener(this);
        next.setOnClickListener(this);
        mute.setOnClickListener(this);
        volumeDown.setOnClickListener(this);
        volumeUp.setOnClickListener(this);

        stop.setOnTouchListener(this);
        previous.setOnTouchListener(this);
        playPause.setOnTouchListener(this);
        next.setOnTouchListener(this);
        mute.setOnTouchListener(this);
        volumeDown.setOnTouchListener(this);
        volumeUp.setOnTouchListener(this);

        volumeBar.setOnSeekBarChangeListener(this);

        if(savedInstanceState!=null){
            volumeBar.setProgress(savedInstanceState.getInt("volumeValue"));
        }

        out = ConnectServerTask.getOut();

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("volumeValue", volumeValue);
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
            //throw new RuntimeException(context.toString()
              //      + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {

        if(v.getId()==stop.getId()) {
            switch (motionEvent.getAction()){
                case MotionEvent.ACTION_DOWN:
                    stop.setImageResource(R.drawable.ic_stop_press);
                    stop.setBackgroundColor(Color.rgb(193,193,193));
                    break;
                case MotionEvent.ACTION_UP:
                    stop.setImageResource(R.drawable.ic_stop);
                    stop.setBackgroundColor(Color.TRANSPARENT);
                    stop.performClick();
                    break;
            }
        }

        if(v.getId()==previous.getId()) {
            switch (motionEvent.getAction()){
                case MotionEvent.ACTION_DOWN:
                    previous.setImageResource(R.drawable.ic_previous);
                    previous.setBackgroundColor(Color.rgb(193,193,193));
                    break;
                case MotionEvent.ACTION_UP:
                    previous.setImageResource(R.drawable.ic_previous);
                    previous.setBackgroundColor(Color.TRANSPARENT);
                    previous.performClick();
                    break;
            }
        }

        if(v.getId()==playPause.getId()) {
            switch (motionEvent.getAction()){
                case MotionEvent.ACTION_DOWN:
                    playPause.setImageResource(R.drawable.ic_play_pause_press);
                    playPause.setBackgroundColor(Color.rgb(193,193,193));
                    break;
                case MotionEvent.ACTION_UP:
                    playPause.setImageResource(R.drawable.ic_play_pause);
                    playPause.setBackgroundColor(Color.TRANSPARENT);
                    playPause.performClick();
                    break;
            }
        }

        if(v.getId()==next.getId()) {
            switch (motionEvent.getAction()){
                case MotionEvent.ACTION_DOWN:
                    next.setImageResource(R.drawable.ic_next_press);
                    next.setBackgroundColor(Color.rgb(193,193,193));
                    break;
                case MotionEvent.ACTION_UP:
                    next.setImageResource(R.drawable.ic_next);
                    next.setBackgroundColor(Color.TRANSPARENT);
                    next.performClick();
                    break;
            }
        }

        if(v.getId()==mute.getId()) {
            switch (motionEvent.getAction()){
                case MotionEvent.ACTION_DOWN:
                    mute.setImageResource(R.drawable.ic_mute_press);
                    //mute.setBackgroundColor(Color.rgb(193,193,193));
                    break;
                case MotionEvent.ACTION_UP:
                    mute.setImageResource(R.drawable.ic_mute);
                    mute.performClick();
                    break;
            }
        }

        if(v.getId()==volumeDown.getId()) {
            switch (motionEvent.getAction()){
                case MotionEvent.ACTION_DOWN:
                    volumeDown.setImageResource(R.drawable.ic_volume_down_press);
                    //volumeDown.setBackgroundColor(Color.rgb(193,193,193));
                    break;
                case MotionEvent.ACTION_UP:
                    volumeDown.setImageResource(R.drawable.ic_volume_down);
                    volumeDown.performClick();
                    break;
            }
        }

        if(v.getId()==volumeUp.getId()) {
            switch (motionEvent.getAction()){
                case MotionEvent.ACTION_DOWN:
                    volumeUp.setImageResource(R.drawable.ic_volume_up_press);
                    //volumeUp.setBackgroundColor(Color.rgb(193,193,193));
                    break;
                case MotionEvent.ACTION_UP:
                    volumeUp.setImageResource(R.drawable.ic_volume_up);
                    volumeUp.performClick();
                    break;
            }
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()== stop.getId()) {
            out.println("stop");
        }

        if(v.getId()== previous.getId()) {
            out.println("previous");
        }

        if(v.getId()== playPause.getId()) {
            out.println("play_pause");
        }

        if(v.getId()== next.getId()) {
            out.println("next");
        }

        if(v.getId()== mute.getId()) {
            out.println("mute");
        }

        if(v.getId()== volumeDown.getId()) {
            out.println("volume_down");
        }

        if(v.getId()== volumeUp.getId()) {
            out.println("volume_up");
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if(seekBar.getId() == volumeBar.getId()){
            out.println("volume_" + i);
            volumeValue = i;
            volumeTextView.setText(String.valueOf(volumeValue));
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

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
