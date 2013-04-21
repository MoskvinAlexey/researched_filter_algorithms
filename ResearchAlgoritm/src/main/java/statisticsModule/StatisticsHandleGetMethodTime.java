package statisticsModule;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.util.StopWatch;



public class StatisticsHandleGetMethodTime implements MethodInterceptor {







    /**
     * Совет "Вместо". Измеряет время работы метода. Используется для измерения времени подготовки алгоритма.
     */
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        StopWatch watch = new StopWatch();
        watch.start(methodInvocation.getMethod().getName());
        Object retVal = methodInvocation.proceed();
        watch.stop();
        CollectStatistics.addPrepationTime(watch.getLastTaskTimeMillis());
        System.out.println(methodInvocation.getMethod().getName() + ": " + watch.getLastTaskTimeMillis()+ " ms");
        return retVal;
    }
}
