package FilterRuleModule;


import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class FilterRules {

    public File fileWithFilterRules;
    public static ArrayList<Rule> filterRules= new ArrayList<Rule>();
    public static ArrayList<Rule> ipFilterRules= new ArrayList<Rule>();
    public static ArrayList<Rule> macFilterRules = new ArrayList<Rule>();
    public static ArrayList<Rule> arpFilterRules= new ArrayList<Rule>();
    public static String [] ipRuleAttributes = {"action","ip_sorce","port_source","ip_dest","port_dest", "protocols", "icmp_code"};
    public static String [] arpRuleAttributes = {"action","mac_source","ip_sorce","mac_dest","ip_dest","arp_messager"};
    public static String [] macRuleAttributes = {"action", "mac_source", "mac_dest"};

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

    public static ArrayList<Rule> getFilterRules(){
        return filterRules;
    }

    public static void loadFilterRulesFromFile(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file.getAbsoluteFile()));
        String nextLine;
        while((nextLine = br.readLine())!=null){
            parseCurrentRule(nextLine);
        }
        br.close();
        filterRules.addAll(macFilterRules);
        filterRules.addAll(arpFilterRules);
        filterRules.addAll(ipFilterRules);
        for(int i =0;i<filterRules.size();i++){
           System.out.println(filterRules.get(i));
        }

    }

    private static void parseCurrentRule(String nextLine) {
        Map <String,String> ruleValue = new HashMap<String, String>();
        String[] nextRule;
        nextRule = nextLine.split(":");
        if(nextRule.length >=3){
            if(nextRule[0].equals("mac")){
                for(int i=0;i< nextRule.length - 2;i++){    //первые два атрибута правила не записываются

                    ruleValue.put(macRuleAttributes[i], nextRule[i+2]);
                }
                macFilterRules.add(Integer.parseInt(nextRule[1]),new Rule(ruleValue));
            }
            if(nextRule[0].equals("arp")){
                for(int i=0;i< nextRule.length - 2;i++){    //первые два атрибута правила не записываются

                    ruleValue.put(arpRuleAttributes[i], nextRule[i+2]);
                }
                arpFilterRules.add(Integer.parseInt(nextRule[1]),new Rule(ruleValue));

            }
            if(nextRule[0].equals("ip")){
                for(int i=0;i< nextRule.length - 2;i++){    //первые два атрибута правила не записываются

                    ruleValue.put(ipRuleAttributes[i], nextRule[i+2]);
                }
                ipFilterRules.add(Integer.parseInt(nextRule[1]),new Rule(ruleValue));
            }

        }

    }
}
