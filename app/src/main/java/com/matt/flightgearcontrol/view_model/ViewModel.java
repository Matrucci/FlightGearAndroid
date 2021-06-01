package com.matt.flightgearcontrol.view_model;
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



}
