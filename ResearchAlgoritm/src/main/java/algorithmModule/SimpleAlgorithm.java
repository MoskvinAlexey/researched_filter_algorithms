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

import java.util.*;


public class SimpleAlgorithm extends AbstractAlgorithm {



    protected String applyAlgorithm(Object packet) {

       String result = sequentialSearchFilterRules((HashMap<String, String>) packet);
       return result;
    }

    protected Object prepare(byte[] packetInByte) {
        JPacket jPacket = new JMemoryPacket(Ethernet.ID, packetInByte);

        HashMap <String,String> packetInHash =  encodePacketToHash(jPacket);

        return  packetInHash;
    }


    /**
     * Последовательное применения правил фильтрации из filterRules к пакету packetInHash.
     * Проверка идет исходя из содержания правила: сравниваются только те поля которые есть и в правиле и в пакете
     * Правило считается подходящим, если все поля правила совпали с соответствующими полями пакета.
     * Применения правил происходит последовательно, согласно структуре заданной в filterRules.
     * Если правило является подходящим, но является правилом "на пропуск" или "на передачу" и в структуре filterRules есть
     * правила более "высокого" уровня, такое правило не применяется.
     *
     * Правила, имеющие индекс 0 в массивах в структуре filterRules являются глобальными и проверяются последними в своих
     * массивах.     *
     *
     * @param packetInHash - пакет в формате хэш таблицы (для перевода пакета из формата Jpacket в хэш таблицу используется
     * метод SimpleAlgorithm.encodePacketToHash(JPacket packet)

     *
     * @return строка в формате type:num, где type - целое число, соотв. типу правила (mac - 0, arp- 1, ip -2), num - номер правила
     */

    private String sequentialSearchFilterRules(HashMap<String, String> packetInHash) {
        boolean ruleIsMatch = false;
        for(int i=0; i< filterRules.sizeFilterRules();i++){
            if(filterRules.filterRuleOfOneTypeIsEmpty(i)){ //если в таблице нет правил, идем дальше
                continue;
            }
            for(int j=1;j< filterRules.sizeFilterRuleOfOneType(i); j++){
                Rule nextRule = filterRules.getFilterRuleSingle(i,j);
                Iterator it = nextRule.getAllField().iterator();
                //Начинаем поочередно сравнивать каждое поле правила с соотв. полем пакета (если оно там есть)
                while (it.hasNext()){
                    String nextRuleField = (String)it.next();
                    Object nextRuleFieldValue = nextRule.get(nextRuleField);
                    if(nextRuleFieldValue!=null && packetInHash.containsKey(nextRuleField)){
                        if(nextRuleField.equals("ip_source")||nextRuleField.equals("ip_dest")){
                            if(Rule.compareIp((String)nextRuleFieldValue,packetInHash.get(nextRuleField)))
                                ruleIsMatch = true;

                            else ruleIsMatch=false;
                        }
                        else if(nextRuleField.equals("port_source")||nextRuleField.equals("port_dest")){
                            if(Rule.comparePort((String) nextRuleFieldValue,packetInHash.get(nextRuleField)))
                                ruleIsMatch = true;

                            else ruleIsMatch=false;

                        }
                        else if(nextRuleField.equals("protocols")){
                            if(Rule.compareProtocol((String)nextRuleFieldValue,packetInHash.get(nextRuleField)))
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
            if(!filterRules.getFilterRuleSingle(i,0).get("action").equals("accept") || i==FilterRules.IP){
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

    /**
     * Метод предназначен для перевод пакета типа JPacket в хэш таблицу
     * Метод анализирует заголовки пакета канального, сетевого и транспортного уровня. В зависимости от наличия заголовков
     * в таблицу добавляются соответствующие значения. Ниже перечисленые протоколы, пакеты которых может разбирать метод
     * и соответствующие ключи, попадающие в таблице при наличии заголовков данных протоколов.
     *
     * Канальный уровень:
     * Ethetnet: mac_source, mac_dest
     *
     * Сетевой уровень:
     * Arp: ip_source, ip_dest, arp_opcode
     * Ip4: ip_source, ip_dest, protocols(вложенный протокол транспортного уровня)
     * Icmp: icmp_type (целое число, обозначающее тип сообщения)
     *
     * Транспортный уровень:
     * Tcp: port_source, port_dest
     * Udp: port_source, port_dest
     *
     * @param packet пакет типа JPacket библиотеки jnetpcap
     *
     * @return хэш таблица с соответствующими ключами и значениями
     */

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
