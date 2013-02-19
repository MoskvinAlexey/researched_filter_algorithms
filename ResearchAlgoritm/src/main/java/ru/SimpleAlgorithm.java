package ru;

import org.jnetpcap.packet.JPacket;
import java.util.*;

public class SimpleAlgorithm extends AbstractAlgorithm implements Runnable {

    JPacket packet;

    public void run(){
        double t1 = getCurrentTime();
        applyAlgorithm();
        double t2 = getCurrentTime();
        System.out.println("Time received packet: " + calcTimeOfFiltration(t1,t2));


    }
    public void nextPacket(JPacket packet){
        this.packet=packet;
    }


    protected void applyAlgorithm() {
        System.out.println(packet);
        try {
             Thread.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    protected double calcTimeOfFiltration(double t1, double t2) {
        return t2 - t1;
    }


    protected double getCurrentTime() {
        Calendar calendar = new GregorianCalendar();
        return calendar.get(Calendar.SECOND);
    }
}
