package com.kivanc.kontrolcepte;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConnectActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText serverIPInput;
    private EditText portInput;
    private Button connectButton;
    private ProgressBar progressBar;

    private String serverIPStr;
    private int portInt;

    ConnectServerTask connectServerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        serverIPInput = (EditText) findViewById(R.id.serverIPInput_id);
        portInput = (EditText) findViewById(R.id.portInput_id);
        connectButton = (Button) findViewById(R.id.connectButton_id);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_id);
        progressBar.setVisibility(View.INVISIBLE);

        connectButton.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==connectButton.getId()){

            boolean isServerIPValid = false;
            boolean isPortValid     = false;

            if (checkEditText(serverIPInput)) {
                serverIPStr = serverIPInput.getText().toString();
                if (isServerIPValid(serverIPStr)) {
                    isServerIPValid = true;
                }
            }

            if (checkEditText(portInput)) {
                portInt = Integer.parseInt(portInput.getText().toString());
                if (isPortValid(portInt)) {
                    isPortValid = true;
                }
            }

            String errorStr = "";

            if (!isServerIPValid) {
                errorStr = "Server IP Address Is Not Valid!";
                serverIPInput.requestFocus();
            }

            else if (!isPortValid) {
                errorStr = "Port Number Is Not Valid!";
                portInput.requestFocus();
            }

            if (!errorStr.equals("")) {
                Toast.makeText(ConnectActivity.this, errorStr, Toast.LENGTH_LONG).show();
            }

            if (isServerIPValid && isPortValid) {

                connectServerTask = new ConnectServerTask(this);
                connectServerTask.execute();
            }
        }
    }

    public boolean checkEditText(EditText edit) {
        boolean result = true;
        String editStr = edit.getText().toString();
        if (editStr == null || editStr.equals("")) {
            return false;
        }
        return result;
    }

    public boolean isServerIPValid(String serverIP) {
        String IP_PATTERN = "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
        boolean result = true;

        Pattern pattern = Pattern.compile(IP_PATTERN);
        Matcher matcher = pattern.matcher(serverIP);

        if (!matcher.matches()) {
            return false;
        }
        return result;
    }

    public boolean isPortValid(int port) {
        boolean result = true;
        if (port < 0 || port > 65535) {
            return false;
        }
        return result;
    }

    public String getServerIPStr() {
        return serverIPStr;
    }

    public int getPortInt() {
        return portInt;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public EditText getServerIPInput() {
        return serverIPInput;
    }

    public EditText getPortInput() {
        return portInput;
    }

    public Button getConnectButton() {
        return connectButton;
    }
}

