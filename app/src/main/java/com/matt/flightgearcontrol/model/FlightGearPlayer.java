package com.matt.flightgearcontrol.model;

import com.matt.flightgearcontrol.view_model.ViewModel;

import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class FlightGearPlayer {

    private String ip;
    private int port;
    private BlockingQueue<Runnable> dispatchQueue = new LinkedBlockingQueue<Runnable>();
    private PrintWriter out;
    private Socket fg;


    public FlightGearPlayer(String ip, int port, ViewModel viewModel) {
        this.port = port;
        this.ip = ip;
        try {
            this.fg = new Socket();
            this.fg.connect(new InetSocketAddress(ip, port), 2000);
            this.out = new PrintWriter(fg.getOutputStream(),true);
            viewModel.notifyConnected(true);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            dispatchQueue.take().run();
                        } catch (InterruptedException e) {
                            //Nothing in queue.
                        }
                    }
                }
            }).start();
        } catch (Exception e) {
            viewModel.notifyConnected(false);
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
