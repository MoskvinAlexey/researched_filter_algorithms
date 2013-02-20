package ru;

import org.jnetpcap.packet.JPacket;
import java.util.*;

public class SimpleAlgorithm extends AbstractAlgorithm implements Runnable {

    JPacket packet;
    Thread thread;

    public void run(){
        thread = Thread.currentThread();
        long t1 = getCurrentTime();
        applyAlgorithm();
        long t2 = getCurrentTime();
        System.out.println("Time received packet: " + calcTimeOfFiltration(t1,t2) + "ms");


    }
    public void nextPacket(JPacket packet){
        this.packet=packet;
    }


    protected void applyAlgorithm() {
        System.out.println(packet);
        try {
             thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    protected long calcTimeOfFiltration(long t1, long t2) {
        return t2 - t1;
    }


    protected long getCurrentTime() {

        return System.currentTimeMillis();
    }
}
