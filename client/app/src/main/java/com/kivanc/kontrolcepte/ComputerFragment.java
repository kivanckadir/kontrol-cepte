package com.kivanc.kontrolcepte;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.io.PrintWriter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ComputerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ComputerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ComputerFragment extends Fragment implements View.OnTouchListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private ImageButton home;
    private ImageButton music;
    private ImageButton mail;
    private ImageButton search;
    private ImageButton eject;
    private ImageButton shutdown;
    private ImageButton restart;

    private PrintWriter out;

    public ComputerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ComputerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ComputerFragment newInstance(String param1, String param2) {
        ComputerFragment fragment = new ComputerFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_computer, container, false);

        home = (ImageButton) view.findViewById(R.id.btn_home);
        music = (ImageButton) view.findViewById(R.id.btn_music);
        mail = (ImageButton) view.findViewById(R.id.btn_mail);
        search = (ImageButton) view.findViewById(R.id.btn_search);

        eject = (ImageButton) view.findViewById(R.id.btn_eject);
        shutdown = (ImageButton) view.findViewById(R.id.btn_shutdown);
        restart = (ImageButton) view.findViewById(R.id.btn_restart);

        home.setOnTouchListener(this);
        music.setOnTouchListener(this);
        mail.setOnTouchListener(this);
        search.setOnTouchListener(this);

        eject.setOnTouchListener(this);
        shutdown.setOnTouchListener(this);
        restart.setOnTouchListener(this);

        out = ConnectServerTask.getOut();
        return view;
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

        if(v.getId() == home.getId()){
            switch (motionEvent.getAction()){
                case MotionEvent.ACTION_UP:
                    out.println("shortcut_home");
                    break;
            }
        }

        if(v.getId() == music.getId()){
            switch (motionEvent.getAction()){
                case MotionEvent.ACTION_UP:
                    out.println("shortcut_music");
                    break;
            }
        }

        if(v.getId() == mail.getId()){
            switch (motionEvent.getAction()){
                case MotionEvent.ACTION_UP:
                    out.println("shortcut_mail");
                    break;
            }
        }

        if(v.getId() == search.getId()){
            switch (motionEvent.getAction()){
                case MotionEvent.ACTION_UP:
                    out.println("shortcut_search");
                    break;
            }
        }

        if(v.getId() == eject.getId()){
            switch (motionEvent.getAction()){
                case MotionEvent.ACTION_DOWN:
                    eject.setImageResource(R.drawable.ic_eject_press);
                    break;
                case MotionEvent.ACTION_UP:
                    eject.setImageResource(R.drawable.ic_cdrom_eject);
                    out.println("eject");
                    break;
            }
        }

        if(v.getId() == shutdown.getId()){
            switch (motionEvent.getAction()){
                case MotionEvent.ACTION_DOWN:
                    shutdown.setImageResource(R.drawable.ic_shutdown_press);
                    break;
                case MotionEvent.ACTION_UP:
                    shutdown.setImageResource(R.drawable.ic_shutdown_button);
                    out.println("shutdown");
                    break;
            }
        }

        if(v.getId() == restart.getId()){
            switch (motionEvent.getAction()){
                case MotionEvent.ACTION_DOWN:
                    restart.setImageResource(R.drawable.ic_restart_press);
                    break;
                case MotionEvent.ACTION_UP:
                    restart.setImageResource(R.drawable.ic_restart);
                    out.println("restart");
                    break;
            }
        }
        return false;
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
