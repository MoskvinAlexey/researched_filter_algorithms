package FilterRuleModule;


import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class FilterRules {

    public File fileWithFilterRules;
    public static ArrayList<ArrayList<Rule>> filterRules = new ArrayList<ArrayList<Rule>>();
    public static ArrayList<Rule> ipFilterRules= new ArrayList<Rule>();
    public static ArrayList<Rule> macFilterRules = new ArrayList<Rule>();
    public static ArrayList<Rule> arpFilterRules= new ArrayList<Rule>();
    public static String [] ipRuleAttributes = {"number","action","ip_sorce","port_source","ip_dest","port_dest",
                                                "protocols", "icmp_code"};
    public static String [] arpRuleAttributes = {"number","action","mac_source","ip_sorce","mac_dest","ip_dest",
                                                 "arp_messager"};
    public static String [] macRuleAttributes = {"number","action", "mac_source", "mac_dest"};

    public FilterRules(String fileName){
        fileWithFilterRules = new File(fileName);
        if(!fileWithFilterRules.exists()) {
            System.out.println("File " + fileName + " is not exist!");
            System.exit(1);           //add cath Exception
        }

        try {
            loadFilterRulesFromFile(fileWithFilterRules);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static ArrayList<ArrayList<Rule>> getFilterRules(){
        return filterRules;
    }

    public static void loadFilterRulesFromFile(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file.getAbsoluteFile()));
        String nextLine;
        while((nextLine = br.readLine())!=null){
            parseCurrentRule(nextLine);
        }
        br.close();

        Collections.sort(macFilterRules);
        filterRules.add(macFilterRules);
        Collections.sort(arpFilterRules);
        filterRules.add(arpFilterRules);
        Collections.sort(ipFilterRules);
        filterRules.add(ipFilterRules);
    }

    private static void parseCurrentRule(String nextLine) {
        Map <String,String> ruleValue = new HashMap<String, String>();
        String[] nextRule;
        nextRule = nextLine.split(":");
        if(nextRule.length >=3){
            if(nextRule[0].equals("mac")){
                for(int i=0;i< nextRule.length - 1;i++){

                    ruleValue.put(macRuleAttributes[i], nextRule[i+1]);
                }
                macFilterRules.add(new Rule(ruleValue));
            }
            if(nextRule[0].equals("arp")){
                for(int i=0;i< nextRule.length - 1;i++){

                    ruleValue.put(arpRuleAttributes[i], nextRule[i+1]);
                }
                arpFilterRules.add(new Rule(ruleValue));

            }
            if(nextRule[0].equals("ip")){
                for(int i=0;i< nextRule.length - 1;i++){

                    ruleValue.put(ipRuleAttributes[i], nextRule[i+1]);
                }
                ipFilterRules.add(new Rule(ruleValue));
            }

        }

    }
}
