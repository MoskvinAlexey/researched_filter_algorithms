package statisticsModule;


import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;


public class StatisticsHandleGetCountCallMethod implements MethodInterceptor {

    static int countMethodCall = 0;

    public Object invoke(MethodInvocation methodInvocation) throws Throwable {

        countMethodCall++;
        Object retVal = methodInvocation.proceed();
        return retVal;
    }

    static int getCountMethodCall(){
        return countMethodCall;
    }

    static void resetCountMethodCall(){
        countMethodCall = 0;
    }
}
