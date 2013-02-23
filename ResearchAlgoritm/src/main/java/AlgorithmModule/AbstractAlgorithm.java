package AlgorithmModule;


import org.jnetpcap.packet.JPacket;
import FilterRuleModule.FilterRules;

import java.util.ArrayList;



public abstract class AbstractAlgorithm implements Runnable {

    public ArrayList<Byte> filterRules;

    public AbstractAlgorithm(){
        filterRules = loadFilterRules();
    }

    public ArrayList<Byte> loadFilterRules(){
        return FilterRules.getFilterRules();

    }

    public abstract void nextPacket(JPacket packet);

    protected abstract void applyAlgorithm();

    protected abstract long calcTimeOfFiltration(long t1, long t2);

    protected abstract long getCurrentTime();



}