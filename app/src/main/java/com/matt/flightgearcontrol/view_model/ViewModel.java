package com.matt.flightgearcontrol.view_model;
import android.util.Log;
import android.view.View;

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
        this.model = new FlightGearPlayer(ip, port, this);
    }

    public void notifyConnected(boolean isConnected, int flag) {
        this.view.updateConnection(isConnected, flag);
    }

    public void setView(MainActivity view) {
        this.view = view;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void connect() {
        this.model = new FlightGearPlayer(this.ip, this.port, this);
    }

    public void disconnect() {
        this.model = null;
        this.view.updateConnection(false, 2);
    }

    private double convertValuesRudder(int value) {
        int oldRange = 100;
        int newRange = 2;
        return ((double)(value * newRange) / oldRange) - 1;
    }

    private double convertValuesThrottle(int value) {
        int oldRange = 100;
        int newRange = 1;
        return ((double)(value * newRange) / oldRange);
    }

    public void setRudder(int rudder) throws InterruptedException {
        if (this.model != null) {
            this.model.setRudder(convertValuesRudder(rudder));

        }
    }

    public void setThrottle(int throttle) throws InterruptedException {
        if (this.model != null) {
            this.model.setThrottle(convertValuesThrottle(throttle));
        }
    }

    public void setAileron(double a) throws InterruptedException {
        if (this.model != null) {
            this.model.setAileron(a);
        }
    }

    public void setElevator(double e) throws InterruptedException {
        if (this.model != null) {
            this.model.setElevator(e);
        }
    }

}
