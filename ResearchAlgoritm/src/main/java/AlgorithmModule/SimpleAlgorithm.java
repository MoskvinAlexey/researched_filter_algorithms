package AlgorithmModule;




import org.jnetpcap.packet.JMemoryPacket;
import org.jnetpcap.packet.JPacket;
import org.jnetpcap.packet.format.FormatUtils;
import org.jnetpcap.protocol.JProtocol;
import org.jnetpcap.protocol.lan.Ethernet;
import org.jnetpcap.protocol.network.Arp;
import org.jnetpcap.protocol.network.Icmp;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.tcpip.Tcp;

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
        System.out.println("Time received packet: " + calcTimeOfFiltration(t1,t2) + "ms");


    }
    public void next(byte[] packet){
        this.packets.add(packet);
    }


    protected void applyAlgorithm() {
        byte[] packetInByte =  packets.remove();
        JPacket packet = new JMemoryPacket(Ethernet.ID, packetInByte);
        HashMap <String,String> packetInHash =  encodePacketToHash(packet);

        System.out.println("==============Packet " + count + "==============");
        Iterator it = packetInHash.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            System.out.println(pairs.getKey() + " = " + pairs.getValue());
            it.remove();
        }
        System.out.println("==================================");
        count++;
    }


    protected long calcTimeOfFiltration(long t1, long t2) {
        return t2 - t1;
    }


    protected long getCurrentTime() {

        return System.currentTimeMillis();
    }

    private static HashMap<String,String> encodePacketToHash(JPacket packet){
        HashMap<String,String> packetInHash = new HashMap<String, String>();
        if (packet.hasHeader(JProtocol.ETHERNET_ID)) {
            Ethernet eth = new Ethernet();
            packet.getHeader(eth);
            packetInHash.put("mac_source", FormatUtils.mac(eth.source()));
            packetInHash.put("mac_dest", FormatUtils.mac(eth.destination()));

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
                packetInHash.put("protocols",ip.typeDescription().replace("next:",""));

                if(packet.hasHeader(JProtocol.ICMP_ID)){
                    Icmp icmp = new Icmp();
                    packet.getHeader(icmp);
                    packetInHash.put("icmp_type",Integer.toString(icmp.type()));
                }
                else if (packet.hasHeader(JProtocol.TCP_ID)) {
                    Tcp tcp = new Tcp();
                    packetInHash.put("port_source", Integer.toString(tcp.source()));
                    packetInHash.put("port_dest", Integer.toString(tcp.destination()));
                }

                else{
                    // Here you can add translations of other headers on transport layer
                }

            }
            else {
                // Here you can add translations of other headers on network layer
            }

        }

        return  packetInHash;
    }

    private static boolean comparePacketWithRule( HashMap<String,String> packetInHash) {
        return false;
    }
}
