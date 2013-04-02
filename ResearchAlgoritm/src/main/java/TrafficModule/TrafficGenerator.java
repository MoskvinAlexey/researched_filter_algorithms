package TrafficModule;
import AlgorithmModule.AbstractAlgorithm;
import org.jnetpcap.Pcap;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;
import org.springframework.util.StopWatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TrafficGenerator implements Runnable {

    public AbstractAlgorithm algorithm;
    public ExecutorService es;
    final StringBuilder errbuf = new StringBuilder();
    final String file;
    public Future future;
    long timestamp;
    byte [] packetInByte;

    public TrafficGenerator(AbstractAlgorithm algorithm, String filename){
        file = filename;
        this.algorithm = algorithm;
        es = Executors.newSingleThreadExecutor();
        timestamp = 0;
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

                long tempTimestamp=packet.getCaptureHeader().timestampInMillis();
                if(timestamp!=0){
                    try {
                        Thread.currentThread().sleep(tempTimestamp-timestamp);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                timestamp=tempTimestamp;
                packetInByte = packet.getByteArray(0, packet.size());
                algorithm.next(packetInByte);

                future = es.submit(algorithm);

            }
        };

        try {
            StopWatch watch = new StopWatch();
            watch.start();
            pcap.loop(10, jpacketHandler, "");
            watch.stop();
            System.out.println("Time of take packet from file: " + watch.getLastTaskTimeMillis() + "ms");
        } finally {

            pcap.close();
        }

        while(!future.isDone()){
           //wait when last packet will handle
        }
        es.shutdown();
    }

}
