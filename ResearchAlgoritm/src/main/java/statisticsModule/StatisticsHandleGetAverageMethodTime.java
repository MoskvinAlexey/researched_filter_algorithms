package statisticsModule;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.util.StopWatch;


public class StatisticsHandleGetAverageMethodTime implements MethodInterceptor {

    int countCallMethod = 0;
    long methodTime = 0;




    /**
     * Совет "вместо". Измеряет время работы алгоритма в среднем за 10 вызовов. Также фиксирует количество обращений к
     * правилам фильтрации после каждого вызова
     * *
     */
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {


        StopWatch watch = new StopWatch();
        watch.start(methodInvocation.getMethod().getName());
        Object retVal = methodInvocation.proceed();
        watch.stop();

        methodTime+=watch.getLastTaskTimeMillis();
        countCallMethod++;

        if(countCallMethod==10){
            CollectStatistics.addRunningTime((double) methodTime / 10L);
            System.out.println(methodInvocation.getMethod().getName() + "for 10 packet: " +  (double) methodTime/10L+ " ms");
            methodTime= 0;
            countCallMethod=0;
        }
        CollectStatistics.addNumberOfRulesApplyToOnePacket(StatisticsHandleGetCountCallMethod.getCountMethodCall());
        System.out.println("Used rules for packet: " + StatisticsHandleGetCountCallMethod.getCountMethodCall() + " rules");
        StatisticsHandleGetCountCallMethod.resetCountMethodCall();

        return retVal;
    }


}
