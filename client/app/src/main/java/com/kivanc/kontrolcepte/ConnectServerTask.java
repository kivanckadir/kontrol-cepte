package com.kivanc.kontrolcepte;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by Kivanc on 18.11.2017.
 */

public class ConnectServerTask extends AsyncTask<Void, Void, Boolean> implements Serializable{

    private static Socket socket;
    private static PrintWriter out;

    private String serverIPStr;
    private int portInt;

    private boolean isConnected = false;

    private ConnectActivity connectActivity;
    private EditText serverIPInput;
    private EditText portInput;
    private Button connectButton;
    private ProgressBar progressBar;

    public ConnectServerTask(ConnectActivity connectActivity) {
        this.connectActivity = connectActivity;
        this.serverIPStr = connectActivity.getServerIPStr();
        this.portInt = connectActivity.getPortInt();
        this.serverIPInput = connectActivity.getServerIPInput();
        this.portInput = connectActivity.getPortInput();
        this.connectButton = connectActivity.getConnectButton();
        this.progressBar = connectActivity.getProgressBar();
    }

    //
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        //Hide Keyboard
        View view = connectActivity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) connectActivity.getSystemService(connectActivity.getApplicationContext().INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        //Make Inactive the Views
        serverIPInput.setFocusable(false);
        serverIPInput.setFocusableInTouchMode(false);
        serverIPInput.setClickable(false);

        portInput.setFocusable(false);
        portInput.setFocusableInTouchMode(false);
        portInput.setClickable(false);

        connectButton.setText("CONNECTING...");
        connectButton.setFocusableInTouchMode(false);
        connectButton.setClickable(false);

        progressBar.setVisibility(View.VISIBLE);

    }

    @Override
    protected Boolean doInBackground(Void... params) {

        boolean result = false;
        try {
            //socket = new Socket(serverIPStr, portInt);
            socket = new Socket();
            socket.connect(new InetSocketAddress(serverIPStr, portInt), 4000); //timeout is 5 sec
            result = true;
            if(result){
                try{
                    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true); //create output stream to send data to server
                }catch (IOException printWriterError){
                    Log.e("PrintWriterError","PrintWriter Couldn't Create.");
                    result = false;
                }
            }
        } catch (IOException e) {
            Log.e("SocketError","Socket Couldn't Create.");
            result = false;
        }
        return result;
    }


    @Override
    protected void onPostExecute(Boolean result)
    {
        //Make Usable the Views
        serverIPInput.setFocusable(true);
        serverIPInput.setFocusableInTouchMode(true);
        serverIPInput.setClickable(true);

        portInput.setFocusable(true);
        portInput.setFocusableInTouchMode(true);
        portInput.setClickable(true);

        connectButton.setText("CONNECT");
        connectButton.setFocusable(true);
        connectButton.setFocusableInTouchMode(false);
        connectButton.setClickable(true);

        progressBar.setVisibility(View.INVISIBLE);

        isConnected = result;

        if(isConnected) {
            Toast.makeText(connectActivity.getApplicationContext(), "Connected to the Server!", Toast.LENGTH_SHORT).show();
            Intent myIntent = new Intent(connectActivity, RemoteActivity.class);
            myIntent = new Intent(connectActivity, RemoteActivity.class);
            connectActivity.startActivity(myIntent);
        }

        else {
            serverIPInput.requestFocus();

            //Show Keyboard
            View view = connectActivity.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) connectActivity.getSystemService(connectActivity.getApplicationContext().INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }

            Toast.makeText(connectActivity.getApplicationContext(), "Couldn't Connect to the Server! Please Make Sure You Enter the Correct Server IP Address and Port Number Informations", Toast.LENGTH_LONG).show();
        }
    }

    public static Socket getSocket() {
        return socket;
    }

    public static PrintWriter getOut() {
        return out;
    }
}
