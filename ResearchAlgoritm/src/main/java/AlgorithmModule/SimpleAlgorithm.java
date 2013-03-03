package AlgorithmModule;




import org.jnetpcap.packet.JMemoryPacket;
import org.jnetpcap.packet.JPacket;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.protocol.lan.Ethernet;
import org.jnetpcap.protocol.network.Arp;
import org.jnetpcap.protocol.network.Icmp;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.tcpip.Tcp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;


public class SimpleAlgorithm extends AbstractAlgorithm implements Runnable {

    Queue<byte[]> packets = new LinkedList<byte[]>();
    Thread thread;

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

    }


    protected long calcTimeOfFiltration(long t1, long t2) {
        return t2 - t1;
    }


    protected long getCurrentTime() {

        return System.currentTimeMillis();
    }

    private static HashMap<String,String> encodePacketToHash(JPacket packet){
        Ethernet eth = new Ethernet();
        Arp arp = new Arp();
        Ip4 ip = new Ip4();
        Tcp tcp = new Tcp();
        Icmp icmp = new Icmp();

        String sIP;
        String dIP;

        HashMap<String,String> packetInHash = new HashMap<String, String>();
        if (packet.hasHeader(ip)){

            sIP = org.jnetpcap.packet.format.FormatUtils.ip(ip.source());
            dIP = org.jnetpcap.packet.format.FormatUtils.ip(ip.destination());

        }
        if(packet.hasHeader(tcp)){
            System.out.println(tcp.destination());
        }

        return  packetInHash;
    }

    private static boolean comparePacketWithRule( HashMap<String,String> packetInHash) {
        return false;
    }
}
