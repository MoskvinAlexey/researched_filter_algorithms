package statisticsModule;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.util.StopWatch;
import support.Writer;


public class StatisticsHandleGetMethodTime implements MethodInterceptor {

    Writer writer;



    public StatisticsHandleGetMethodTime(){
        writer = new Writer("data/statistics.txt");

    }


    /**
     * Совет "Вместо". Измеряет время работы метода. Используется для измерения времени подготовки алгоритма.
     */
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        StopWatch watch = new StopWatch();
        watch.start(methodInvocation.getMethod().getName());
        Object retVal = methodInvocation.proceed();
        watch.stop();
        writer.write(methodInvocation.getMethod().getName() + ": " + watch.getLastTaskTimeMillis() + " ms");
        System.out.println(methodInvocation.getMethod().getName() + ": " + watch.getLastTaskTimeMillis()+ " ms");
        return retVal;
    }
}
