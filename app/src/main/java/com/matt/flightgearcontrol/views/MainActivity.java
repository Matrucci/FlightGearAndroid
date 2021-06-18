package com.matt.flightgearcontrol.views;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.google.android.material.snackbar.Snackbar;
import com.matt.flightgearcontrol.R;
import com.matt.flightgearcontrol.model.FlightGearPlayer;
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

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.app_name);

        this.vm = new ViewModel();
        this.vm.setView(this);

        findViewsById();
        setActionListeners();

    }

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

    private void connectClicked() {
        if (validateInput()) {
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

    private boolean validateInput() {
        String ip, port;
        ip = this.ip.getText().toString();
        port = this.port.getText().toString();

        if (ip.isEmpty() || port.isEmpty()) {
            return false;
        }

        try {
            if (Integer.parseInt(port) > 65535 || Integer.parseInt(port) < 0) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

        String[] ipSplit = ip.split("\\.");

        if (ipSplit.length != 4) {
            return false;
        }

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

    private void findViewsById() {
        this.ip = (EditText) findViewById(R.id.ip);
        this.port  = (EditText) findViewById(R.id.port);
        this.connect = (Button) findViewById(R.id.connect);
        this.rudder = (SeekBar) findViewById(R.id.rudder);
        this.throttle = (VerticalSeekBar) findViewById(R.id.throttle);
        this.joystick = (Joystick) findViewById(R.id.joystick);
    }

    public void updateConnection(boolean isConnected) {
        if (isConnected) {
            Log.i("Main", "connected");
            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.controllers);
            relativeLayout.setVisibility(View.VISIBLE);
            this.connect.setText("Connected!");
            this.connect.setBackgroundColor(Color.parseColor("#0aaaf5"));
        } else {
            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.rel);
            Snackbar snackbar = Snackbar
                    .make(relativeLayout, R.string.unable_to_connect, Snackbar.LENGTH_LONG);

            snackbar.show();
        }

    }

}