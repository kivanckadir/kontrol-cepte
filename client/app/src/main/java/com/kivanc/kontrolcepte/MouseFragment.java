package com.kivanc.kontrolcepte;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Timer;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MouseFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MouseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MouseFragment extends Fragment implements View.OnTouchListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private boolean isMouseMoved = false;

    private double initX = 0;
    private double initY = 0;
    private double distanceX = 0;
    private double distanceY = 0;

    private Socket socket;
    private PrintWriter out;

    private OnFragmentInteractionListener mListener;

    View touchBoard;
    View leftClick;
    View rightClick;

    public MouseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MouseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MouseFragment newInstance(String param1, String param2) {
        MouseFragment fragment = new MouseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        View view = inflater.inflate(R.layout.fragment_mouse, container, false);

        touchBoard  = (View) view.findViewById(R.id.touch_board);
        leftClick   = (View) view.findViewById(R.id.btn_left_click);
        rightClick  = (View) view.findViewById(R.id.btn_right_click);

        touchBoard.setOnTouchListener(this);
        leftClick.setOnTouchListener(this);
        rightClick.setOnTouchListener(this);

        socket = ConnectServerTask.getSocket();
        out = ConnectServerTask.getOut();

        return view;
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


    //On Touch Listener
    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {
        if(v.getId()==touchBoard.getId()) {
            if(socket.isConnected() && out!=null){
                //Down -> Move -> Up or Down -> Up
                switch(motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        //save X and Y positions when user touches the TouchBoard
                        initX = motionEvent.getX();
                        initY = motionEvent.getY();
                        isMouseMoved = false;
                        break;

                    case MotionEvent.ACTION_MOVE:
                        distanceX = motionEvent.getX() - initX; //Mouse movement in x direction
                        distanceY = motionEvent.getY() - initY; //Mouse movement in y direction

                        /*set init to new position so that continuous mouse movement is captured*/
                        initX = motionEvent.getX();
                        initY = motionEvent.getY();

                        if(distanceX !=0|| distanceY !=0){
                            out.println(distanceX +","+ distanceY); //send mouse movement to server
                        }

                        isMouseMoved = true;
                        break;

                    case MotionEvent.ACTION_UP:
                        //consider a tap only if usr did not move mouse after ACTION_DOWN
                        if(!isMouseMoved){
                            out.println("left_click");
                        }
                        break;
                }
            }
        }

        if(v.getId()==leftClick.getId()) {
            if(socket.isConnected() && out!=null){
                //Down -> Move -> Up or Down -> Up
                switch(motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        out.println("left_click_press");
                        leftClick.setBackgroundColor(Color.rgb(132,132,132));
                        break;
                    case MotionEvent.ACTION_UP:
                        out.println("left_click_release");
                        leftClick.setBackgroundColor(Color.rgb(214,215,215));
                        break;
                }
            }
        }

        if(v.getId()==rightClick.getId()) {
            if(socket.isConnected() && out!=null){
                //Down -> Move -> Up or Down -> Up
                switch(motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        out.println("right_click_press");
                        rightClick.setBackgroundColor(Color.rgb(132,132,132));
                        break;

                    case MotionEvent.ACTION_UP:
                        out.println("right_click_release");
                        rightClick.setBackgroundColor(Color.rgb(214,215,215));
                        break;
                }
            }
        }

        return true;
    }
}
