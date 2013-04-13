package filterRuleModule;

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


    public static boolean compareIp(String ipInRule, String ipInPacket) {
        if(ipInRule.equals("any")){
            return true;
        }
        else
        {

        byte [] network = new byte[4];
        byte [] networkMask = new byte[] { -1, -1, -1, -1 };

        String ipPart = ipInRule;
        String[] ipParts;
        ipParts =  ipInRule.split("/"); //разбираем ip адрес и маску подсети

        switch (ipParts.length)
        {
            case 2:  //задана маска
                ipPart = ipParts[0];


                String[] partsOfNetMask = ipParts[1].split("\\.");
                if (partsOfNetMask.length == 1){ //задана маска в формате /dd
                    int p0;
                    p0 = Integer.parseInt(partsOfNetMask[0]);
                    int fullMask = -1 << (32 - p0);
                    networkMask[0] = (byte) ((fullMask & 0xFF000000) >>> 24);
                    networkMask[1] = (byte) ((fullMask & 0x00FF0000) >>> 16);
                    networkMask[2] = (byte) ((fullMask & 0x0000FF00) >>> 8);
                    networkMask[3] = (byte) (fullMask & 0x000000FF);
                }


                else{//задана маска в формате /ddd.ddd.ddd.ddd

                    toBytes(ipParts[1], networkMask); //ipParts[1] - заданная маска
                }

            case 1: //задан только ip адрес
                toBytes(ipPart, network);
                break;
        }
        return match(ipInPacket, networkMask, network);

        }

    }

    public static boolean comparePort(String portInRule, String portInPacket) {

        if(portInRule.equals(portInPacket) || portInRule.equals("any"))
            return true;
        else{
            String[] partsPortInRule = portInRule.split(",");
            for(int i=0; i<partsPortInRule.length;i++){
                String[] range = partsPortInRule[i].split("-");
                if(range.length==1){
                    if(partsPortInRule[i].equals(portInPacket)) return true;
                }
                else{
                int min = Integer.parseInt(range[0]);
                int max = Integer.parseInt(range[1]);
                int val  = Integer.parseInt(portInPacket);

                if(val>=min && val <=max) return true;

                }
                }
            }
        return false;

    }

    public static boolean compareProtocol(String protocolInRule, String protocolInPacket){
        String [] partsProtocolInRule = protocolInRule.split(",");
        if(partsProtocolInRule.length==1)
            return protocolInRule.equals(protocolInPacket) || protocolInRule.equals("any");
        else{
            for(int i=0; i< partsProtocolInRule.length;i++){
                if(partsProtocolInRule[i].equals(protocolInPacket)) return true;
            }
        }
        return false;
    }



    /**
     * Метод перевода ip адреса в массив байт
     * @param ipAddr - ip, который необходимо перевести
     * @param bytes - массив байт, в который записывается ip
     */
    private  static void toBytes(String ipAddr, byte[] bytes){
        String[] ipParts = ipAddr.split("\\.");
        for (int i = 0; i < ipParts.length; i++){
            int p = Integer.parseInt(ipParts[i]);
            bytes[i] = (byte) (p < 128 ? p : p - 256);
        }

    }

    /**
     * Метод проверяет принадлежит ли ip адрес ipIn сети network с маской networkMask
     * @param ipIn
     * @param networkMask
     * @param network
     * @return
     */
    private static boolean match(String ipIn, byte [] networkMask, byte [] network)
    {
        byte[] bytes = new byte[4];
        toBytes(ipIn, bytes);
        for (int i = 0; i < 4; i++)
        {
            if ((bytes[i] & networkMask[i]) != (network[i] & networkMask[i]))
            {
                return false;
            }
        }
        return true;
    }
}
