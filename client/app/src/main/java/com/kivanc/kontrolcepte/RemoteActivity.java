package com.kivanc.kontrolcepte;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.KeyEvent.Callback;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.NumberFormat;
import java.text.ParsePosition;


public class RemoteActivity extends AppCompatActivity implements Callback {

    Fragment activeFragment;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    Socket socket;
    PrintWriter out;

    String keyString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if(savedInstanceState!=null){
            activeFragment = getSupportFragmentManager().getFragment(savedInstanceState, "activeFragment");
        }

        else{
            activeFragment = new MouseFragment();
        }

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content, activeFragment).commit();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "activeFragment", activeFragment);
    }

    @Override
    protected void onStart() {
        super.onStart();
        socket = ConnectServerTask.getSocket();
        out = ConnectServerTask.getOut();
    }

    @Override
    protected void onPause() {
        super.onPause();
        out.println("onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        out.println("onDestroy");
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    activeFragment = new MouseFragment();
                    fragmentTransaction.replace(R.id.content, activeFragment).commit();
                    return true;
                case R.id.navigation_dashboard:
                    activeFragment = new KeyboardFragment();
                    fragmentTransaction.replace(R.id.content, activeFragment).commit();
                    //Show Keyboard

                    InputMethodManager imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                    return true;
                case R.id.navigation_notifications:
                    activeFragment = new MediaFragment();
                    fragmentTransaction.replace(R.id.content, activeFragment).commit();
                    return true;
                case R.id.navigation_computer:
                    activeFragment = new ComputerFragment();
                    fragmentTransaction.replace(R.id.content, activeFragment).commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        if(event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode()!=59 && event.getKeyCode()!=KeyEvent.KEYCODE_BACK){

            String key = String.valueOf((char)event.getUnicodeChar());
            boolean hasUpperCase = !key.equals(key.toLowerCase());
            boolean hasLowerCase = !key.equals(key.toUpperCase());
            boolean isNumeric = isNumeric(key);

            if(hasUpperCase){
                out.println("uppercase_"+key.toUpperCase());
            }
            else if(hasLowerCase){
                out.println("lowercase_" + key);
            }
            else if(isNumeric) {
                out.println("numeric_" + key);
            }

            else{
                if(event.getKeyCode() == 67){
                    key = "backspace";
                }
                else if(event.getKeyCode() == 62){
                    key = "space";
                }
                else if(event.getKeyCode() == 66){
                    key = "enter";
                }
                out.println("special_" + key);
            }
            Log.i("key", key);
        }

        return super.dispatchKeyEvent(event);
    }

    public static boolean isNumeric(String str)
    {
        NumberFormat formatter = NumberFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);
        formatter.parse(str, pos);
        return str.length() == pos.getIndex();
    }
}
