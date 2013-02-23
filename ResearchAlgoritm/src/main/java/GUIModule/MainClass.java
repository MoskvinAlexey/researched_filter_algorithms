package GUIModule;


import AlgorithmModule.SimpleAlgorithm;
import TrafficModule.TrafficGenerator;

public class MainClass {
    public static void main(String[] args){
        Thread th = new Thread(new TrafficGenerator(new SimpleAlgorithm()));
        th.start();
        try {
            th.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Done!");


    }

}
