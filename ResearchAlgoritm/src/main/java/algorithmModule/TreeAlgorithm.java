package algorithmModule;


import filterRuleModule.FilterRules;
import filterRuleModule.Rule;
import org.jnetpcap.packet.JMemoryPacket;
import org.jnetpcap.packet.JPacket;
import org.jnetpcap.packet.format.FormatUtils;
import org.jnetpcap.protocol.JProtocol;
import org.jnetpcap.protocol.lan.Ethernet;
import org.jnetpcap.protocol.network.Arp;
import org.jnetpcap.protocol.network.Icmp;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.tcpip.Tcp;
import org.jnetpcap.protocol.tcpip.Udp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;


public class TreeAlgorithm extends AbstractAlgorithm {

    private boolean isBuildTree = false;
    private ArrayList<HashMap<Pair,HashMap>> filterTrees;




    protected String applyAlgorithm(Object packet) {

        String result = searchInTree((HashMap<String, String>) packet);

        return result;
    }



    protected Object prepare(byte[] packet) {
        if(!isBuildTree){
            buildTrees();
            isBuildTree = true;
        }
        JPacket jPacket = new JMemoryPacket(Ethernet.ID, packet);
        HashMap <String,String> packetInHash =  encodePacketToHash(jPacket);
        return packetInHash;
    }

    private void buildTrees() {
        filterTrees = new ArrayList<HashMap<Pair, HashMap>>();

        for(int i=0; i< filterRules.sizeFilterRules();i++){
            if(filterRules.filterRuleOfOneTypeIsEmpty(i)){
                filterTrees.add(null);
                continue;
            }
            filterTrees.add(buildTree(i));

        }
    }

    private String searchInTree(HashMap<String, String> packet) {

        for(int i=0; i<filterTrees.size();i++){
            HashMap<Pair,HashMap> nextLevel = filterTrees.get(i);
            if(nextLevel == null) continue;
            boolean findMatch;

            while (true){
                Iterator it = nextLevel.keySet().iterator();
                findMatch = false;

                while(it.hasNext()){
                    Pair nextPair = (Pair) it.next();
                    String ruleField = nextPair.key;
                    String ruleFieldValue = nextPair.value;
                    if(ruleField.equals("action")){
                        return i + " : " + ruleFieldValue;
                    }

                    if(!packet.containsKey(ruleField)){
                       packet.put(ruleField,"any");
                    }

                    if(ruleField.equals("ip_source")||ruleField.equals("ip_dest")){
                        if(Rule.compareIp( ruleFieldValue, packet.get(ruleField)))  {
                            nextLevel = nextLevel.get(nextPair);
                            findMatch = true;
                            break;
                        }

                    }
                    else if(ruleField.equals("port_source")||ruleField.equals("port_dest")){
                        if(Rule.comparePort(ruleFieldValue,packet.get(ruleField))){
                            nextLevel = nextLevel.get(nextPair);
                            findMatch = true;
                            break;
                        }
                    }
                    else if(ruleField.equals("protocols")){
                        if(Rule.compareProtocol( ruleFieldValue, packet.get(ruleField))){
                            nextLevel = nextLevel.get(nextPair);
                            findMatch = true;
                            break;
                        }
                    }
                    else {
                        if(ruleFieldValue.equals(packet.get(ruleField))) {
                            nextLevel = nextLevel.get(nextPair);
                            findMatch = true;
                            break;
                        }

                    }

                }
                if (!findMatch){   //если не нашли совпадений, применяем глобальное
                    if(!filterRules.getFilterRuleSingle(i,0).get("action").equals("accept") || i== FilterRules.IP){
                        return i + ":"+"0";
                    }
                    else
                        break;  //если глобальное на accept и это не ip, переходи к следующему дереву
                }

            }

        }

        return null;  //!!!!!
    }


    private HashMap<Pair, HashMap> buildTree(int rulesType) {
        HashMap<Pair,HashMap> filterTree = new HashMap<Pair, HashMap>();

        for(int j=1;j< filterRules.sizeFilterRuleOfOneType(rulesType); j++){
            Rule nextRule = filterRules.getFilterRuleSingle(rulesType,j);
            Iterator it = nextRule.getAllField().iterator();
            HashMap<Pair,HashMap> nextLevel = filterTree;
            while (it.hasNext()){

                String nextRuleField = (String)it.next();
                String nextRuleFieldValue = nextRule.get(nextRuleField);

                if(nextRuleField.equals("number") || nextRuleField.equals("action") || nextRuleFieldValue == null ){
                   continue;
                }

                Pair nextPair = new Pair(nextRuleField, nextRuleFieldValue);
                if(!nextLevel.containsKey(nextPair)){
                      nextLevel.put(nextPair, new HashMap<Pair,HashMap>());
                }
                nextLevel = nextLevel.get(nextPair);
            }
            String actionValue = nextRule.get("action");
            nextLevel.put(new Pair("action", actionValue), null);
        }


        return filterTree;
    }

    private static HashMap<String,String> encodePacketToHash(JPacket packet){
        //System.out.println(packet);
        HashMap<String,String> packetInHash = new HashMap<String, String>();
        if (packet.hasHeader(JProtocol.ETHERNET_ID)) {
            Ethernet eth = new Ethernet();
            packet.getHeader(eth);


            packetInHash.put("mac_source", convertMacAddress(eth.source()));
            packetInHash.put("mac_dest", convertMacAddress(eth.destination()));

            if (packet.hasHeader(JProtocol.ARP_ID)) {
                Arp arp = new Arp();
                packet.getHeader(arp);
                packetInHash.put("ip_source", FormatUtils.ip(arp.spa()));
                packetInHash.put("ip_dest",FormatUtils.ip(arp.tpa()));
                packetInHash.put("arp_opcode",arp.operationEnum().toString());


            }
            else if (packet.hasHeader(JProtocol.IP4_ID)) {
                Ip4 ip = new Ip4();
                packet.getHeader(ip);
                packetInHash.put("ip_source",FormatUtils.ip(ip.source()));
                packetInHash.put("ip_dest",FormatUtils.ip(ip.destination()));
                packetInHash.put("protocols",convertProtocol(ip.typeDescription()));

                if(packet.hasHeader(JProtocol.ICMP_ID)){
                    Icmp icmp = new Icmp();
                    packet.getHeader(icmp);
                    packetInHash.put("icmp_type",Integer.toString(icmp.type()));
                }
                else if (packet.hasHeader(JProtocol.TCP_ID)) {
                    Tcp tcp = new Tcp();
                    packet.getHeader(tcp);
                    packetInHash.put("port_source", Integer.toString(tcp.source()));
                    packetInHash.put("port_dest", Integer.toString(tcp.destination()));
                }
                else if(packet.hasHeader(JProtocol.UDP_ID)){
                    Udp udp = new Udp();
                    packet.getHeader(udp);
                    packetInHash.put("port_source", Integer.toString(udp.source()));
                    packetInHash.put("port_dest", Integer.toString(udp.destination()));

                }

                else{
                    //здесь можно добавить парсинг пакетов других протоколов транспортного уровня
                }

            }
            else {
                // здесь можно добавить парсинг пакетов других протоколов сетевого уровня
            }

        }
        return  packetInHash;
    }
    private static String convertMacAddress(byte[] mac){
        String macInStr = FormatUtils.mac(mac);
        return macInStr.replace(":","").toLowerCase(Locale.ENGLISH);
    }

    private static String convertProtocol(String protocol){
        protocol = protocol.replace("next: ","");
        String [] words = protocol.split(" ");
        char [] letters = new char [words.length +1];
        for(int i=0;i<words.length;i++){
            letters[i]=words[i].charAt(0);
        }
        letters[words.length] = 'p';
        return new String(letters).toLowerCase(Locale.ENGLISH);

    }

    public class Pair{

        public String key;
        public String value;

        public Pair(String key, String value){

            this.key = key;
            this.value = value;
        }
        public int hashCode(){
            return key.hashCode() + value.hashCode();
        }

        public boolean equals(Object o){
            Pair pair = (Pair) o;
            return(key.equals(pair.key) && value.equals(pair.value));
        }

    }
}
