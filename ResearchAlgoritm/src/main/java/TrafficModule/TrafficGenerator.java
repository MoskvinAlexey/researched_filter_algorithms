package TrafficModule;
import AlgorithmModule.AbstractAlgorithm;
import org.jnetpcap.Pcap;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TrafficGenerator implements Runnable {

    public AbstractAlgorithm algorithm;
    public ExecutorService es;
    final StringBuilder errbuf = new StringBuilder();
    final String file = "data/example";
    public Future future;

    public TrafficGenerator(AbstractAlgorithm algorithm){
        this.algorithm = algorithm;
        es = Executors.newSingleThreadExecutor();
    }


    public void run(){


        System.out.printf("Opening file for reading: %s%n", file);


        Pcap pcap = Pcap.openOffline(file, errbuf);

        if (pcap == null) {
            System.err.printf("Error while opening device for capture: "
                    + errbuf.toString());
            return;
        }


        PcapPacketHandler<String> jpacketHandler = new PcapPacketHandler<String>() {

            public void nextPacket(PcapPacket packet, String user) {

                algorithm.next(packet);
                future = es.submit(algorithm);

            }
        };


        try {
            pcap.loop(5, jpacketHandler, "jNetPcap rocks!");
        } finally {

            pcap.close();
        }

        while(!future.isDone()){

        }
        es.shutdown();
    }

}
