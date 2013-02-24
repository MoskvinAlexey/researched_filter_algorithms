package AlgorithmModule;



import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.tcpip.Tcp;

import java.util.LinkedList;
import java.util.Queue;


public class SimpleAlgorithm extends AbstractAlgorithm implements Runnable {

    Queue<PcapPacket> packets = new LinkedList<PcapPacket>();
    Thread thread;
    int count =0;

    public void run(){
        thread = Thread.currentThread();
        long t1 = getCurrentTime();
        applyAlgorithm();
        long t2 = getCurrentTime();
        System.out.println("Time received packet: " + calcTimeOfFiltration(t1,t2) + "ms");


    }
    public void next(PcapPacket packet){
        this.packets.add(packet);
    }


    protected void applyAlgorithm() {

        count++;
        PcapPacket packet =  packets.remove();
        String sIP;
        String dIP;
        Ip4 ip = new Ip4();
        Tcp tcp = new Tcp();

        if (packet.hasHeader(ip)){

            sIP = org.jnetpcap.packet.format.FormatUtils.ip(ip.source());
            dIP = org.jnetpcap.packet.format.FormatUtils.ip(ip.destination());
            System.out.println("Пакет № " + count + ":" + sIP + ", " +dIP);

        }
        if(packet.hasHeader(tcp)){
            System.out.println(tcp.destination());
        }

//        try {
//             thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

    }


    protected long calcTimeOfFiltration(long t1, long t2) {
        return t2 - t1;
    }


    protected long getCurrentTime() {

        return System.currentTimeMillis();
    }
}
