package com.matt.flightgearcontrol.views;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.google.android.material.snackbar.Snackbar;
import com.matt.flightgearcontrol.R;
import com.matt.flightgearcontrol.view_model.ViewModel;
import com.matt.flightgearcontrol.widget.VerticalSeekBar;

public class MainActivity extends AppCompatActivity {
    private ViewModel vm;

    //View elements
    private EditText ip;
    private EditText port;
    private Button connect;
    private VerticalSeekBar throttle;
    private SeekBar rudder;
    private Joystick joystick;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Enabling threading
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //Setting up the new title.
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.app_name);

        //Creating the ViewModel.
        this.vm = new ViewModel();
        this.vm.setView(this);

        findViewsById();
        setActionListeners();

    }

    /*********************************************
     * Setting the listeners for the view objects.
     *********************************************/
    private void setActionListeners() {

        this.connect.setOnClickListener(
                v -> connectClicked()
        );

        this.rudder.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        try {
                            vm.setRudder(progress);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );

        this.throttle.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        try {
                            vm.setThrottle(progress);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );

    }

    /*******************************************************************
     * The connect button was clicked.
     * Setting up the connection and telling the ViewModel to connect.
     *****************************************************************/
    private void connectClicked() {
        if (isValidateInput()) {
            try {
                int _port = Integer.parseInt(this.port.getText().toString());
                this.vm.setIp(this.ip.getText().toString());
                this.vm.setPort(_port);
                this.vm.connect();

                this.joystick.onChange = (a, e) -> {
                    this.vm.setAileron(a);
                    this.vm.setElevator(e);
                };
            } catch (Exception e) {
                return;
            }
        } else { //wrong input
            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.rel);
            Snackbar snackbar = Snackbar
                    .make(relativeLayout, R.string.network_path_error, Snackbar.LENGTH_LONG);

            snackbar.show();
        }
    }

    /****************************************************
     * Checks to see if the input IP and port is valid.
     * @return - true/false according to the input.
     ***************************************************/
    private boolean isValidateInput() {
        String ip, port;
        //Getting the text from the view.
        ip = this.ip.getText().toString();
        port = this.port.getText().toString();

        //If the text is empty the input isn't valid.
        if (ip.isEmpty() || port.isEmpty()) {
            return false;
        }

        //If the port isn't a number or if it's out of range, the input is not valid.
        try {
            if (Integer.parseInt(port) > 65535 || Integer.parseInt(port) < 0) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

        //Splitting the IP by dots.
        String[] ipSplit = ip.split("\\.");

        //If we don't have 4 parts of the IP, the input isn't valid.
        if (ipSplit.length != 4) {
            return false;
        }

        //If one of the parts isn't a number or it's not in the range of normal IP,
        //the input isn't valid.
        for (int i = 0; i < ipSplit.length; i++) {
            try {
                int seg = Integer.parseInt(ipSplit[i]);
                if (seg < 0 || seg > 255) {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    /*******************************
     * Finding all views by the id.
     ******************************/
    private void findViewsById() {
        this.ip = findViewById(R.id.ip);
        this.port = findViewById(R.id.port);
        this.connect = findViewById(R.id.connect);
        this.rudder = findViewById(R.id.rudder);
        this.throttle = findViewById(R.id.throttle);
        this.joystick = findViewById(R.id.joystick);
    }

    /********************************************************************
     * Updating the view if the connection to the server was successful.
     * @param isConnected - boolean to say if we managed to connect.
     *******************************************************************/
    public void updateConnection(boolean isConnected) {
        //We managed to connect.
        if (isConnected) {
            //Showing the flight controls.
            RelativeLayout relativeLayout = findViewById(R.id.controllers);
            relativeLayout.setVisibility(View.VISIBLE);
            //Updating the connect button.
            this.connect.setText(R.string.connected);
            this.connect.setBackgroundColor(Color.parseColor("#0aaaf5"));
        } else { //We didn't manage to connect.
            //Showing a snackbar with an error.
            RelativeLayout relativeLayout = findViewById(R.id.rel);
            Snackbar snackbar = Snackbar
                    .make(relativeLayout, R.string.unable_to_connect, Snackbar.LENGTH_LONG);
            snackbar.show();
        }

    }

}