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

        summary.put("Время работы",calculateTotalRunningTime(runningTime));
        return summary;
    }

    public static HashMap<String,Object> getFullStatistics(){
        HashMap<String,Object> full = new HashMap<String, Object>();
        full.put("Алгоритм",algorithName);
        full.put("Всего пакетов",numberOfPackets);
        full.put("Правил фильтрации", numberRules);
        full.put("Время применения алгоритма для 10ти пакетов", cloneRunningTime());
        full.put("Общее время выполнения алгоритма",calculateTotalRunningTime(runningTime));
        full.put("Время подготовки для 10 пакетов",calculatePreparationTime());
        full.put("Общее время подготовки алгоритма",calculateTotalRunningTime(preparationTime));
        full.put("Применено правил к пакету (в среднем)", calcEverageNumberOfRules());
        return full;
    }

    private static ArrayList<Double> cloneRunningTime() {
        ArrayList<Double> newRunningTime = new ArrayList<Double>();
        for(int i=0;i<runningTime.size();i++){
            newRunningTime.add(runningTime.get(i));
        }
        return newRunningTime;
    }

    private static int calcEverageNumberOfRules() {
        int sum = 0;
        for (int i=0;i<numberOfRulesApplyToOnePacket.size();i++){
           sum+=numberOfRulesApplyToOnePacket.get(i);
        }
        return sum/numberOfPackets;
    }

    private static ArrayList<Double> calculatePreparationTime() {
        ArrayList<Double> preparationTimeForTenPackets = new ArrayList<Double>();
        for(int i=0;i<preparationTime.size()/10;i++){
            double time=0;
            for(int j=0;j<10;j++){
                time+=preparationTime.get(i*10 + j);
            }
            preparationTimeForTenPackets.add(time);
        }

        return preparationTimeForTenPackets;
    }

    public static void resetAll(){
        algorithName ="";
        runningTime.clear();
        preparationTime.clear();
        numberOfRulesApplyToOnePacket.clear();
        numberOfPackets=0;
        numberRules=0;
        results.clear();
    }

    private static double calculateTotalRunningTime(List<Double> times){
        double fullRunningTime =0;
        for(Double time : times){
            fullRunningTime+=time;
        }
        return (double)Math.round(fullRunningTime * 100) / 100;
    }


    public static ArrayList<String> getResult() {
        ArrayList<String> newResult = new ArrayList<String>();
        for(int i=0;i<results.size();i++){
            newResult.add(results.get(i));
        }
        return  newResult;
    }
}
