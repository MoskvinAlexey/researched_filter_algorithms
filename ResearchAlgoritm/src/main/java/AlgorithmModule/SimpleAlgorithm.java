package AlgorithmModule;


import org.jnetpcap.packet.JPacket;
import org.jnetpcap.protocol.network.Ip4;




public class SimpleAlgorithm extends AbstractAlgorithm implements Runnable {

    JPacket packet;
    Thread thread;
    int count =0;

    public void run(){
        thread = Thread.currentThread();
        long t1 = getCurrentTime();
        applyAlgorithm();
        long t2 = getCurrentTime();
        System.out.println("Time received packet: " + calcTimeOfFiltration(t1,t2) + "ms");


    }
    public void nextPacket(JPacket packet){
        this.packet=packet;
    }


    protected void applyAlgorithm() {
        count++;
        byte[] sIP = new byte[4];
        byte[] dIP = new byte[4];

        Ip4 ip = new Ip4();
        if (packet.hasHeader(ip)){
            ip.source(sIP);
            ip.destination(dIP);

            String sourceIP = org.jnetpcap.packet.format.FormatUtils.ip(sIP);
            String destinationIP = org.jnetpcap.packet.format.FormatUtils.ip(dIP);

            System.out.println("Пакет № " + count + ":" + sourceIP + ", " +destinationIP);
            //System.out.println(packet);
        }
        try {
             thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    protected long calcTimeOfFiltration(long t1, long t2) {
        return t2 - t1;
    }


    protected long getCurrentTime() {

        return System.currentTimeMillis();
    }
}
