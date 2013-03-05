package FilterRuleModule;

import java.util.HashMap;
import java.util.Map;

public class Rule implements Comparable {
    private String [] ruleAttributes = {"number","action","mac_source","mac_dest","arp_opcode","ip_source","ip_dest","port_source",
                                          "port_dest","protocols", "icmp_type"};
    public Map <String, String> ruleValue;


    public Rule(Map<String, String> ruleArgs){
        ruleValue = new HashMap<String, String>();
        for (int i=0; i< ruleAttributes.length; i++){
            ruleValue.put(ruleAttributes[i], ruleArgs.get(ruleAttributes[i])); //записываем соответствующие значение или null
        }
    }

    public String toString(){
        return ruleValue.toString();
    }


    public int compareTo(Object object) {
        Rule tmpRule = (Rule) object;
        int number = Integer.parseInt(tmpRule.ruleValue.get("number"));
        int thisNumber = Integer.parseInt(this.ruleValue.get("number"));
        if(thisNumber<number)
            return -1;
        else if (thisNumber > number)
            return 1;
        return 0;
    }
}
