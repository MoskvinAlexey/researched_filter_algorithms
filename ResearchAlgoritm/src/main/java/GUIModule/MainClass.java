package GUIModule;



import AlgorithmModule.AbstractAlgorithm;
import AlgorithmModule.ControlAlgorithm;
import AlgorithmModule.SimpleAlgorithm;
import FilterRuleModule.FilterRules;
import StatisticsModule.StatisticsHandle;
import TrafficModule.TrafficGenerator;
import org.springframework.aop.framework.ProxyFactory;

public class MainClass {
    public static void main(String[] args){

        new FilterRules("data/rules");

        AbstractAlgorithm target = new SimpleAlgorithm();

        ProxyFactory pf = new ProxyFactory();
        pf.addAdvice(new StatisticsHandle());
        pf.setTarget(target);
        AbstractAlgorithm simpleAlg = (AbstractAlgorithm) pf.getProxy();

        ControlAlgorithm algUnderControl = new ControlAlgorithm();
        algUnderControl.setAlgorithm(simpleAlg);

        Thread th = new Thread(new TrafficGenerator(algUnderControl, "data/example"));
        th.start();
        try {
            th.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Done!");


    }

}
