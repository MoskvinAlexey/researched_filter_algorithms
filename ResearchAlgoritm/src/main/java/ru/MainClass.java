package ru;


public class MainClass {
    public static void main(String[] args){
        new Thread(new TrafficGenerator(new SimpleAlgorithm())).start();
    }

}
