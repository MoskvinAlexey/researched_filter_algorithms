package filterRuleModule;


import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import statisticsModule.CollectStatistics;



public class FilterRules {

    public static final int IP =2;
    public static final int ARP=1;
    public static final int MAC=0;

    public File fileWithFilterRules;
    private static ArrayList<ArrayList<Rule>> filterRules = new ArrayList<ArrayList<Rule>>();
    private static ArrayList<Rule> ipFilterRules= new ArrayList<Rule>();
    private static ArrayList<Rule> macFilterRules = new ArrayList<Rule>();
    private static ArrayList<Rule> arpFilterRules= new ArrayList<Rule>();

    public static String [] ipRuleAttributes = {"number","action","ip_source","port_source","ip_dest","port_dest",
                                                "protocols", "icmp_type"};
    public static String [] arpRuleAttributes = {"number","action","mac_source","ip_sorce","mac_dest","ip_dest",
                                                 "arp_opcode"};
    public static String [] macRuleAttributes = {"number","action", "mac_source", "mac_dest"};





    public void loadFilterRules(String fileName){
        fileWithFilterRules = new File(fileName);
        if(!fileWithFilterRules.exists()) {
            System.out.println("File " + fileName + " is not exist!");
            System.exit(1);
        }

        try {
            loadFilterRulesFromFile(fileWithFilterRules);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadFilterRulesFromFile(File file) throws IOException {
        int countOfLine = 0;
        BufferedReader br = new BufferedReader(new FileReader(file.getAbsoluteFile()));
        String nextLine;
        while((nextLine = br.readLine())!=null){
            if(!nextLine.equals("")){
                countOfLine++;
                parseCurrentRule(nextLine);
            }
        }
        br.close();

        CollectStatistics.setNumberRules(countOfLine);
        Collections.sort(macFilterRules);
        filterRules.add(MAC,macFilterRules);
        Collections.sort(arpFilterRules);
        filterRules.add(ARP,arpFilterRules);
        Collections.sort(ipFilterRules);
        filterRules.add(IP,ipFilterRules);
    }

    /**
     * Метод переводит строку, которая содержит правило в фильтрации в хэш таблицу
     * Затем с помощью данной хэш таблицы создает объект класса Rule и в зависимости от типа правила
     * кладет его в тот или иной массив
     *
     * @param nextLine - строка, содержащая правило фильтрации
     *
     *
     */
    private static void parseCurrentRule(String nextLine) {
        HashMap <String,String> ruleValue = new HashMap<String, String>();
        String[] nextRule;
        nextRule = nextLine.split(":");
        if(nextRule.length >=3){ //рассматриваем только правила с заполненными обязательными полями
            if(nextRule[0].equals("mac")){
                for(int i=0;i< nextRule.length - 1;i++){
                    if(nextRule[i+1].equals("")){ //если в правиле пропущено значение
                        ruleValue.put(macRuleAttributes[i], "any");
                    }
                    else{
                        ruleValue.put(macRuleAttributes[i], nextRule[i+1]);
                    }

                }
                if(nextRule.length < macRuleAttributes.length){ //если в правиле не заполнены необязательные поля

                    for(int i=nextRule.length -1;i<macRuleAttributes.length;i++){
                        ruleValue.put(macRuleAttributes[i],"any");
                    }
                }
                macFilterRules.add(new Rule(ruleValue));
            }
            if(nextRule[0].equals("arp")){
                for(int i=0;i< nextRule.length - 1;i++){

                    if(nextRule[i+1].equals("")){
                        ruleValue.put(arpRuleAttributes[i], "any");
                    }
                    else{
                        ruleValue.put(arpRuleAttributes[i], nextRule[i+1]);
                    }
                }
                if(nextRule.length < arpRuleAttributes.length){
                    for(int i=nextRule.length-1;i<arpRuleAttributes.length;i++){
                        ruleValue.put(arpRuleAttributes[i],"any");
                    }
                }
                arpFilterRules.add(new Rule(ruleValue));

            }
            if(nextRule[0].equals("ip")){
                for(int i=0;i< nextRule.length - 1;i++){

                    if(nextRule[i+1].equals("")){
                        ruleValue.put(ipRuleAttributes[i], "any");
                    }
                    else{
                        ruleValue.put(ipRuleAttributes[i], nextRule[i+1]);
                    }
                }
                if(nextRule.length < ipRuleAttributes.length){
                    for(int i=nextRule.length -1;i<ipRuleAttributes.length;i++){
                        ruleValue.put(ipRuleAttributes[i],"any");
                    }
                }
                ipFilterRules.add(new Rule(ruleValue));
            }

        }

    }

    /**
     *
     * @param ruleType - тип правил фильтрации: IP =2, ARP=1, MAC=0
     * @param ruleNumber
     * @return правило фильтрации из таблицы ruleType, с номером в таблице ruleNumber
     */

    public  Rule getFilterRuleSingle(int ruleType, int ruleNumber){
        return filterRules.get(ruleType).get(ruleNumber);
    }

    public  int sizeFilterRules(){
        return filterRules.size();
    }

    public  int sizeFilterRuleOfOneType(int ruleType){
        return filterRules.get(ruleType).size();
    }

    public  boolean filterRuleOfOneTypeIsEmpty(int ruleType){
        return filterRules.get(ruleType).isEmpty();
    }

    public static void resetFilterRules(){
        ipFilterRules.clear();
        macFilterRules.clear();
        arpFilterRules.clear();
        filterRules.clear();
    }
}
