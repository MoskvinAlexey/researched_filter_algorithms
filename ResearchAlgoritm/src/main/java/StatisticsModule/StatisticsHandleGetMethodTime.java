package StatisticsModule;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.*;
import org.springframework.util.StopWatch;


public class StatisticsHandleGetMethodTime implements MethodInterceptor {

    int countCallMethod = 0;
    long methodTime = 0;



    public Object invoke(MethodInvocation methodInvocation) throws Throwable {

        StopWatch watch = new StopWatch();
        watch.start(methodInvocation.getMethod().getName());
        Object retVal = methodInvocation.proceed();
        watch.stop();

        methodTime+=watch.getLastTaskTimeMillis();
        countCallMethod++;

        if(countCallMethod==10){
            System.out.println(methodInvocation.getMethod().getName() + ": " +  (float) methodTime/10L);
            methodTime= 0;
            countCallMethod=0;
        }

        return retVal;
    }
}
