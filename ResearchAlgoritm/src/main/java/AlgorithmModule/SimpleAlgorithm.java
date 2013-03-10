package AlgorithmModule;




import FilterRuleModule.FilterRules;
import FilterRuleModule.Rule;
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

import java.util.*;


public class SimpleAlgorithm extends AbstractAlgorithm implements Runnable {

    Queue<byte[]> packets = new LinkedList<byte[]>();
    Thread thread;
    int count =1;

    public void run(){
        thread = Thread.currentThread();
        long t1 = getCurrentTime();
        applyAlgorithm();
        long t2 = getCurrentTime();
        System.out.println("Time of handle packet: " + calcTimeOfFiltration(t1,t2) + "ms");


    }
    public void next(byte[] packet){
        this.packets.add(packet);
    }


    protected void applyAlgorithm() {
        byte[] packetInByte =  packets.remove();
        JPacket packet = new JMemoryPacket(Ethernet.ID,packetInByte);
        HashMap <String,String> packetInHash =  encodePacketToHash(packet);
        String result = sequentialSearchFilterRules(packetInHash, filterRules);
        System.out.println("==============Packet " + count + "==============");
        Iterator it = packetInHash.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            System.out.println(pairs.getKey() + " = " + pairs.getValue());
            it.remove();
        }
        System.out.println("Application rule: " + result);
        System.out.println("==================================");
        count++;
    }

    private static String sequentialSearchFilterRules(HashMap<String, String> packetInHash, ArrayList<ArrayList<Rule>> filterRules) {
        boolean ruleIsMatch = false;
        for(int i=0; i< filterRules.size();i++){
            if(filterRules.get(i).isEmpty()){ //если в таблице нет правил, идем дальше
                continue;
            }
            for(int j=1;j< filterRules.get(i).size(); j++){
                Rule nextRule = filterRules.get(i).get(j);
                Iterator it = nextRule.getAllField().iterator();
                //Начинаем поочередно сравнивать каждое поле правила с соотв. полем пакета (если оно там есть)
                while (it.hasNext()){
                    String nextRuleField = (String)it.next();
                    Object nextRuleFieldValue = nextRule.get(nextRuleField);
                    if(nextRuleFieldValue!=null && packetInHash.containsKey(nextRuleField)){
                        if(nextRuleField.equals("ip_source")||nextRuleField.equals("ip_dest")){
                            if(Rule.compareIp(nextRuleFieldValue,packetInHash.get(nextRuleField)))
                                ruleIsMatch = true;

                            else ruleIsMatch=false;
                        }
                        else if(nextRuleField.equals("port_source")||nextRuleField.equals("port_dest")){
                            if(Rule.comparePort(nextRuleFieldValue,packetInHash.get(nextRuleField)))
                                ruleIsMatch = true;

                            else ruleIsMatch=false;

                        }
                        else if(nextRuleField.equals("protocols")){
                            if(Rule.compareProtocol(nextRuleFieldValue,packetInHash.get(nextRuleField)))
                                ruleIsMatch = true;

                            else ruleIsMatch=false;
                        }
                        else {
                            if(nextRuleFieldValue.equals(packetInHash.get(nextRuleField))){
                                ruleIsMatch = true;
                            }
                            else ruleIsMatch = false;

                        }
                        if(!ruleIsMatch) break;   //если хотя бы одно поле не совпало, правило не подходит

                    }
                }
                if(ruleIsMatch){ //если == true после проверки всех полей, значит правило подходит
                    if(nextRule.get("action").equals("accept") && i!= FilterRules.IP){
                        break;  //передаем на след. уровень
                    }
                    else  return i + ":" + nextRule.get("number") ;
                }

            } //если дошли до конца цикла, значит ни одно из правил не подошло, т.е. применяем глобальное

            //"применяем" правило только если оно на drop, pass или если это глобальное ip правило
            if(!filterRules.get(i).get(0).get("action").equals("accept") || i==FilterRules.IP){
                return i + ":"+"0";
            }
            //т.е., если глобальное не ip правило на accept, переходим к следующей таблице правил
        }

        return null;
    }



    protected long calcTimeOfFiltration(long t1, long t2) {
        return t2 - t1;
    }


    protected long getCurrentTime() {
        return System.currentTimeMillis();
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
                packetInHash.put("ip_source",FormatUtils.ip(arp.spa()));
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


}
