package GUIModule;



import algorithmModule.AbstractAlgorithm;
import algorithmModule.ControlAlgorithm;
import algorithmModule.SimpleAlgorithm;
import filterRuleModule.FilterRules;
import statisticsModule.WrapInProxy;
import trafficModule.TrafficGenerator;


public class ConsoleStart {
    public static void main(String[] args){

        FilterRules filterRules=WrapInProxy.wrapFilterRulesInpRoxy(new FilterRules());
        filterRules.loadFilterRules("data/rules");


        AbstractAlgorithm simpleAlg = WrapInProxy.wrapAlgorithmInProxy(new SimpleAlgorithm());
        simpleAlg.setFilterRules(filterRules);

        ControlAlgorithm algUnderControl = new ControlAlgorithm();
        algUnderControl.setAlgorithm(simpleAlg);

        Thread th = new Thread(new TrafficGenerator(algUnderControl, "data/example2"));
        th.start();
        try {
            th.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Done!");


    }

}
