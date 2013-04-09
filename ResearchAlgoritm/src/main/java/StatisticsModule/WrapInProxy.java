package StatisticsModule;


import AlgorithmModule.AbstractAlgorithm;
import FilterRuleModule.FilterRules;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Advisor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;

public class WrapInProxy {

    public static AbstractAlgorithm wrapAlgorithmInProxy(AbstractAlgorithm target){
        Pointcut pc = new StaticPointcutForApplyAlgorithm();
        Advice advice = new StatisticsHandleGetMethodTime();
        Advisor advisor = new DefaultPointcutAdvisor(pc, advice);
        ProxyFactory pf = new ProxyFactory();
        pf.addAdvisor(advisor);
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
