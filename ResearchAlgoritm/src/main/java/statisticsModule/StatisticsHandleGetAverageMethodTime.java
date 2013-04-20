package statisticsModule;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.util.StopWatch;
import support.Writer;


public class StatisticsHandleGetAverageMethodTime implements MethodInterceptor {

    int countCallMethod = 0;
    long methodTime = 0;
    Writer writer;

    public StatisticsHandleGetAverageMethodTime(){
        writer = new Writer("data/statistics.txt");
    }

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
           writer.write(methodInvocation.getMethod().getName() + " for 10 packet: " + (float) methodTime / 10L + " ms") ;

            System.out.println(methodInvocation.getMethod().getName() + "for 10 packet: " +  (float) methodTime/10L+  " ms");
            methodTime= 0;
            countCallMethod=0;
        }
        writer.write("Used rules for packet: " + StatisticsHandleGetCountCallMethod.getCountMethodCall() + " rules");
        System.out.println("Used rules for packet: " + StatisticsHandleGetCountCallMethod.getCountMethodCall() + " rules");
        StatisticsHandleGetCountCallMethod.resetCountMethodCall();

        return retVal;
    }


}
