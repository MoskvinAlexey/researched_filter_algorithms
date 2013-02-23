package FilterRuleModule;

import java.util.HashMap;
import java.util.Map;

public class IpRule {
    private String [] ruleAtributs = {"action", "in", "out", "protocol", "dIp", "sIp", "dPort", "sPort",
                                        "type_codeICMP", "preced", "tos", "frag", "frag_len", "ttl"};
    private Map <String, String> ruleValue = new HashMap<String, String>();
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

    public IpRule(Map<String, String> ruleArgs){
        for (int i=0; i< ruleAtributs.length; i++){
            ruleValue.put(ruleAtributs[i], ruleArgs.get(ruleAtributs[i]));
        }
    }

}
