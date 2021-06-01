package com.matt.flightgearcontrol.views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.matt.flightgearcontrol.R;
import com.matt.flightgearcontrol.view_model.ViewModel;

public class MainActivity extends AppCompatActivity {
    private ViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}