package FilterRuleModule;

import java.util.HashMap;
import java.util.Map;

public class Rule {
    private String [] ruleAttributes = {"action","mac_source","mac_dest","arp_message","ip_source","ip_dest","port_source",
                                          "port_dest","protocols", "icmp_code"};
    private Map <String, String> ruleValue;


    public Rule(Map<String, String> ruleArgs){
        ruleValue = new HashMap<String, String>();
        for (int i=0; i< ruleAttributes.length; i++){
            ruleValue.put(ruleAttributes[i], ruleArgs.get(ruleAttributes[i])); //записываем соответствующие значение или null
        }
    }

    public String toString(){
        return ruleValue.toString();
    }
}
