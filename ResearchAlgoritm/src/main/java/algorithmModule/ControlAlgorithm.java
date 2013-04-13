package algorithmModule;


import support.Writer;

import java.util.*;

public class ControlAlgorithm implements Runnable {

    public AbstractAlgorithm algorithm;
    Queue<byte[]> packets = new LinkedList<byte[]>();
    int count =1;
    public Writer writer;




    public void run() {
        writer = new Writer("data/results.txt");
        byte[] packetInByte =  packets.remove();
        Object packet = algorithm.prepare(packetInByte);
        String result = algorithm.applyAlgorithm(packet);
        System.out.println("To packet № " + count + " apply rule " + result);
        writer.write("To packet № " + count + " apply rule " + result);
        count++;
    }

    public void setAlgorithm(AbstractAlgorithm alg){
        this.algorithm = alg;
    }


    public void next(byte[] packet){
        this.packets.add(packet);
    }
}
