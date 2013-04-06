package StatisticsModule;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.*;
import org.springframework.util.StopWatch;


public class StatisticsHandle implements MethodInterceptor {



    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        StopWatch watch = new StopWatch();
        watch.start();
        Object retVal = methodInvocation.proceed();
        watch.stop();
        System.out.println(methodInvocation.getMethod().getName() + ": " + watch.getLastTaskTimeMillis());
        return retVal;
    }
}
