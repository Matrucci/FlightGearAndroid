package com.matt.flightgearcontrol.model;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class FlightGearPlayer {


    private String ip;
    private int port;
    /*
    private double rudder;
    private double throttle;
    private double aileron;
    private double elevator;

     */
    private BlockingQueue<Runnable> dispatchQueue = new LinkedBlockingQueue<Runnable>();
    private PrintWriter out;
    private Socket fg;



    public FlightGearPlayer(String ip, int port) {
        this.port = port;
        this.ip = ip;
        try {
            this.fg = new Socket(ip, port);
            this.out = new PrintWriter(fg.getOutputStream(),true);
            new Thread(new Runnable() {
                //Socket fg = new Socket(ip, port);
                //PrintWriter out = new PrintWriter(fg.getOutputStream(),true);
                @Override
                public void run() {
                    while (true) {
                        try {
                            dispatchQueue.take().run();
                        } catch (InterruptedException e) {
                            // okay, just terminate the dispatcher
                        }
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setAileron(double aileron) throws InterruptedException {
        dispatchQueue.put(
            new Runnable() {
                @Override
                public void run() {
                    out.print("set /controls/flight/aileron " + aileron + "\r\n");
                    out.flush();
                }
            }
        );
    }

    public void setElevator(double elevator) throws InterruptedException {
        dispatchQueue.put(
                new Runnable() {
                    @Override
                    public void run() {
                        out.print("set /controls/flight/elevator " + elevator + "\r\n");
                        out.flush();
                    }
                }
        );
    }

    public void setRudder(double rudder) throws InterruptedException {
        dispatchQueue.put(
                new Runnable() {
                    @Override
                    public void run() {
                        out.print("set /controls/flight/rudder " + rudder + "\r\n");
                        out.flush();
                    }
                }
        );
    }

    public void setThrottle(double throttle) throws InterruptedException {
        dispatchQueue.put(
                new Runnable() {
                    @Override
                    public void run() {
                        out.print("set /controls/engines/current-engine/throttle " + throttle + "\r\n");
                        out.flush();
                    }
                }
        );
    }
}
