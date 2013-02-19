package ru;


import org.jnetpcap.packet.JPacket;

import java.util.ArrayList;



public abstract class AbstractAlgorithm implements Runnable {

    public ArrayList<Byte> filterRules;

    public AbstractAlgorithm(){
        filterRules = loadFilterRules();
    }

    public ArrayList<Byte> loadFilterRules(){
        return FilterRules.getFilterRules();

    }

    protected abstract void nextPacket(JPacket packet);

    protected abstract void applyAlgorithm();

    protected abstract double calcTimeOfFiltration(double t1, double t2);

    protected abstract double getCurrentTime();



}
