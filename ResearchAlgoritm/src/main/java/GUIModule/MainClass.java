package GUIModule;


import AlgorithmModule.SimpleAlgorithm;
import FilterRuleModule.FilterRules;
import TrafficModule.TrafficGenerator;

public class MainClass {
    public static void main(String[] args){
//        FilterRules filterRules = new FilterRules("data/rules");
        Thread th = new Thread(new TrafficGenerator(new SimpleAlgorithm(), "data/example"));
        th.start();
        try {
            th.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Done!");


    }

}
