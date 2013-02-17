package ru;


import java.util.ArrayList;



public abstract class AbstractAlgorithm implements Runnable {

    public ArrayList<Byte> filterRules;

    public AbstractAlgorithm(){
        filterRules = loadFilterRules();
    }

    public ArrayList<Byte> loadFilterRules(){
        return FilterRules.getFilterRules();

    }


    public void run(){
       double t1=getCurrentTime();
       applyAlgorithm();
       double t2 = getCurrentTime();
       calcTimeOfFiltration(t1, t2);
   }

    protected abstract void applyAlgorithm();

    protected abstract void calcTimeOfFiltration(double t1, double t2);

    protected abstract double getCurrentTime();



}
