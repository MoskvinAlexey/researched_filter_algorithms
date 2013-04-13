package statisticsModule;


import algorithmModule.AbstractAlgorithm;
import filterRuleModule.FilterRules;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Advisor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;

public class WrapInProxy {

    public static AbstractAlgorithm wrapAlgorithmInProxy(AbstractAlgorithm target){
        Pointcut pointcutForAlgorithm = new StaticPointcutForAlgorithm();
        Pointcut pointcutForPrepareAlgorithm = new StaticPointcutForPrepareAlgorithm();
        Advice getAverageMethodTime = new StatisticsHandleGetAverageMethodTime();
        Advice getMethodTime = new StatisticsHandleGetMethodTime();
        Advisor firstAdvisor = new DefaultPointcutAdvisor(pointcutForAlgorithm, getAverageMethodTime);
        Advisor secondAdvisor = new DefaultPointcutAdvisor(pointcutForPrepareAlgorithm, getMethodTime);
        ProxyFactory pf = new ProxyFactory();
        pf.addAdvisor(0,firstAdvisor);
        pf.addAdvisor(1,secondAdvisor);
        pf.setTarget(target);
        return (AbstractAlgorithm) pf.getProxy();
    }

    public static FilterRules wrapFilterRulesInpRoxy(FilterRules target){
        Pointcut pc = new StaticPointcutForFilterRules();
        Advice advice = new StatisticsHandleGetCountCallMethod();
        Advisor advisor = new DefaultPointcutAdvisor(pc, advice);
        ProxyFactory pf = new ProxyFactory();
        pf.addAdvisor(advisor);
        pf.setTarget(target);
        return (FilterRules) pf.getProxy();

    }
}
