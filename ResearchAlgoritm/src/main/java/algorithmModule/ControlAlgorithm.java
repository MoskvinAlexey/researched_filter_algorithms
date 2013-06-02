package algorithmModule;


import statisticsModule.CollectStatistics;


import java.util.*;

public class ControlAlgorithm implements Runnable {

    public AbstractAlgorithm algorithm;
    Queue<byte[]> packets = new LinkedList<byte[]>();
    int count =1;
    static HashMap<String,String> algorithms = new HashMap<String, String>();






    public void run() {

        byte[] packetInByte =  packets.remove();
        Object packet = algorithm.prepare(packetInByte);
        String result = algorithm.applyAlgorithm(packet);
        System.out.println("To packet № " + count + " apply rule " + result);
        CollectStatistics.addResults("К пакету № " + count + " применили правило " + result);
        count++;
    }

    public void setAlgorithm(AbstractAlgorithm alg){
        this.algorithm = alg;
    }


    public void next(byte[] packet){
        this.packets.add(packet);
    }

    public static String getAlgorithmClassName(String algorithmName){
       if (algorithms.size()==0) initAlgorithmsHashMap();
       return algorithms.get(algorithmName);

    }
    public static Set<String> getAllAlgorithmsName (){
        if (algorithms.size()==0) initAlgorithmsHashMap();
        return algorithms.keySet();

    }

    private static void initAlgorithmsHashMap(){
        algorithms.put("Последовательный поиск", "algorithmModule.SimpleAlgorithm");
        algorithms.put("Дерево решений", "algorithmModule.TreeAlgorithm");

    }
}
