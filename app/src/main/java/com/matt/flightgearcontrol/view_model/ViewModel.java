package com.matt.flightgearcontrol.view_model;
import android.util.Log;

import com.matt.flightgearcontrol.model.FlightGearPlayer;
import com.matt.flightgearcontrol.views.MainActivity;

public class ViewModel {
    private String ip;
    private int port;
    private MainActivity view;
    private FlightGearPlayer model;

    public ViewModel() {
        this.ip = "";
        this.port = 0;
    }

    public ViewModel(String ip, int port) {
        this.ip = ip;
        this.port = port;
        this.model = new FlightGearPlayer(ip, port);
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void connect() {
        if (this.model == null) {
            this.model = new FlightGearPlayer(this.ip, this.port);
        }
    }

    private double convertValuesRudder(int value) {
        int oldRange = 100;
        int newRange = 2;
        return (double)((double)(value * newRange) / oldRange) - 1;
    }

    private double convertValuesThrottle(int value) {
        int oldRange = 100;
        int newRange = 1;
        return (double)((double)(value * newRange) / oldRange);
    }

    public void setRudder(int rudder) throws InterruptedException {
        if (this.model != null) {
            //Log.i("MAIN", "" + convertValuesRudder(rudder));
            this.model.setRudder(convertValuesRudder(rudder));

        }
    }

    public void setThrottle(int throttle) throws InterruptedException {
        if (this.model != null) {
            //Log.i("MAIN", "" + convertValues(throttle));
            //this.model.setThrottle(convertValues(throttle));
            this.model.setThrottle(convertValuesThrottle(throttle));
        }
    }



}
