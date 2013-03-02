package FilterRuleModule;

import java.util.HashMap;
import java.util.Map;

public class Rule {
    private String [] ruleAttributes = {"action", "in", "out", "protocol", "dIp", "sIp", "dPort", "sPort",
                                        "type_codeICMP", "preced", "tos", "frag", "frag_len", "ttl"};
    private Map <String, String> ruleValue;
//    private String action;
//    private String in;
//    private String out;
//    private String protocol;
//    private String dIp;
//    private String sIp;
//    private String dPort;
//    private String sPort;
//    private String type_codeICMP;
//    private String preced;
//    private String tos;
//    private String frag;
//    private String frag_len;
//    private String ttl;

    public Rule(Map<String, String> ruleArgs){
        ruleValue = new HashMap<String, String>();
        for (int i=0; i< ruleAttributes.length; i++){
            ruleValue.put(ruleAttributes[i], ruleArgs.get(ruleAttributes[i]));
        }
    }

}
