package TrafficModule;
import AlgorithmModule.AbstractAlgorithm;
import org.jnetpcap.packet.JMemoryPacket;
import org.jnetpcap.packet.JPacket;
import org.jnetpcap.protocol.JProtocol;
import org.jnetpcap.protocol.lan.Ethernet;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.tcpip.Tcp;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TrafficGenerator implements Runnable {

    public AbstractAlgorithm algorithm;
    public ExecutorService es;

    public TrafficGenerator(AbstractAlgorithm algorithm){
        this.algorithm = algorithm;
        es = Executors.newSingleThreadExecutor();
    }


    public void run(){
        JPacket packet =
                new JMemoryPacket(JProtocol.ETHERNET_ID,
                        " 001801bf 6adc0025 4bb7afec 08004500 "
                                + " 0041a983 40004006 d69ac0a8 00342f8c "
                                + " ca30c3ef 008f2e80 11f52ea8 4b578018 "
                                + " ffffa6ea 00000101 080a152e ef03002a "
                                + " 2c943538 322e3430 204e4f4f 500d0a");

        Ip4 ip = packet.getHeader(new Ip4());
        Tcp tcp = packet.getHeader(new Tcp());

        tcp.destination(80);

        ip.checksum(ip.calculateChecksum());
        tcp.checksum(tcp.calculateChecksum());
        packet.scan(Ethernet.ID);

        algorithm.nextPacket(packet);

        Future f1 = es.submit(algorithm);
        while(!f1.isDone()){

        }
        es.shutdown();
    }

}
