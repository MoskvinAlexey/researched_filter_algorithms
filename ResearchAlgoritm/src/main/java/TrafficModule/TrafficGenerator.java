package TrafficModule;
import AlgorithmModule.AbstractAlgorithm;
import AlgorithmModule.ControlAlgorithm;
import org.jnetpcap.Pcap;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TrafficGenerator implements Runnable {

    public ControlAlgorithm controlAlgorithm;
    public ExecutorService es;
    final StringBuilder errbuf = new StringBuilder();
    final String file;
    public Future future;
    long timestamp;
    byte [] packetInByte;

    public TrafficGenerator(ControlAlgorithm controlAlgorithm, String filename){
        file = filename;
        this.controlAlgorithm = controlAlgorithm;
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

                controlAlgorithm.next(packetInByte);
                future = es.submit(controlAlgorithm);

            }
        };

        try {

            pcap.loop(10, jpacketHandler, "");

        } finally {

            pcap.close();
        }

        while(!future.isDone()){
           //wait when last packet will handle
        }
        es.shutdown();
    }

}
