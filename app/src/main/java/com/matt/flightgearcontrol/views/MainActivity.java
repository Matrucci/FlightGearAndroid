package com.matt.flightgearcontrol.views;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.material.snackbar.Snackbar;
import com.matt.flightgearcontrol.R;
import com.matt.flightgearcontrol.view_model.ViewModel;

public class MainActivity extends AppCompatActivity {
    private ViewModel vm;

    //View elements
    private EditText ip;
    private EditText port;
    private Button connect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        //assert actionBar != null;
        actionBar.setTitle("Take Flight!");

        findViewsById();
        this.connect.setOnClickListener(
                v -> connectClicked()
        );
    }

    private void connectClicked() {
        if (validateInput()) {
            try {
                int _port = Integer.parseInt(this.port.getText().toString());
                this.vm.setIp(this.ip.getText().toString());
                this.vm.setPort(_port);
                this.vm.connect();
            } catch (Exception e) {
                return;
            }
        } else { //wrong input
            RelativeLayout linearLayout = (RelativeLayout) findViewById(R.id.rel);
            Snackbar snackbar = Snackbar
                    .make(linearLayout, "Wrong IP or port number", Snackbar.LENGTH_LONG);

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
    }

}