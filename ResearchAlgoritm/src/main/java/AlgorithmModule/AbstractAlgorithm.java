package AlgorithmModule;


import FilterRuleModule.Rule;
import FilterRuleModule.FilterRules;
import java.util.ArrayList;



public abstract class AbstractAlgorithm implements Runnable {

    public ArrayList<ArrayList<Rule>> filterRules;

    public AbstractAlgorithm(){
        filterRules = loadFilterRules();
    }

    public ArrayList<ArrayList<Rule>> loadFilterRules(){
        return FilterRules.getFilterRules();

    }

    public abstract void next(byte[] packet);

    protected abstract void applyAlgorithm();

    protected abstract long calcTimeOfFiltration(long t1, long t2);

    protected abstract long getCurrentTime();



}
