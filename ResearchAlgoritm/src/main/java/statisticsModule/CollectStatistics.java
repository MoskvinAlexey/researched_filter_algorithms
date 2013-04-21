package statisticsModule;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CollectStatistics {

    private static String algorithName ="";
    private static  List<Double> runningTime= new ArrayList<Double>();
    private static  List<Double> preparationTime =new ArrayList<Double>();
    private static  List<Integer> numberOfRulesApplyToOnePacket= new ArrayList<Integer>();
    private static  List<String> results= new ArrayList<String>();
    private static int numberOfPackets =0;
    private static int numberRules=0;

    public static  void addRunningTime(double value){
        runningTime.add(value);
    }

    public static void addPrepationTime(double value){
        preparationTime.add(value);
    }

    public static void addNumberOfRulesApplyToOnePacket(int value){
        numberOfRulesApplyToOnePacket.add(value);
    }
    public static void addResults(String value){
        results.add(value);
    }

    public static void setNumberOfPackets(int value){
        numberOfPackets = value;
    }

    public static void setNumberRules(int value){
        numberRules = value;
    }

    public static void setAlgorithmName(String value){
        algorithName = value;
    }

    public static HashMap<String,Object> getSummaryStatistics(){
        HashMap<String,Object> summary = new HashMap<String, Object>();
        summary.put("Алгоритм",algorithName);
        summary.put("Всего пакетов",numberOfPackets);
        summary.put("Правил фильтрации", numberRules);
        double fullRunningTime =0;
        for(Double time : runningTime){
             fullRunningTime+=time;
        }
        summary.put("Время работы",fullRunningTime);
        return summary;
    }

    public static HashMap<String,Object> getFullStatistics(){
        return null;
    }




}
