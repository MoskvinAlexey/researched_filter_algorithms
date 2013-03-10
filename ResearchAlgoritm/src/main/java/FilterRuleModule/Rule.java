package FilterRuleModule;

import java.util.HashMap;

import java.util.Set;

public class Rule implements Comparable {
    private String [] ruleAttributes = {"number","action","mac_source","mac_dest","arp_opcode","ip_source","ip_dest","port_source",
                                          "port_dest","protocols", "icmp_type"};
    protected HashMap <String, String> ruleValue;


    public Rule(HashMap<String, String> ruleArgs){
        ruleValue = new HashMap<String, String>();
        for (int i=0; i< ruleAttributes.length; i++)
            ruleValue.put(ruleAttributes[i], ruleArgs.get(ruleAttributes[i])); //записываем соответствующие значение или null
    }

    public String toString(){
        return ruleValue.toString();
    }
    public Set getAllField(){
        return ruleValue.keySet();
    }
    public String get(String key){
        return ruleValue.get(key);
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


    public static boolean compareIp(Object ipInRule, String ipInPacket) {
        return ipInRule.equals(ipInPacket) || ipInRule.equals("any");
    }

    public static boolean comparePort(Object portInRule, String portInPacket) {
        return portInRule.equals(portInPacket) || portInRule.equals("any");
    }

    public static boolean compareProtocol(Object protocolInRule, String protocolInPacket) {
        return protocolInRule.equals(protocolInPacket) || protocolInRule.equals("any");
    }
}
