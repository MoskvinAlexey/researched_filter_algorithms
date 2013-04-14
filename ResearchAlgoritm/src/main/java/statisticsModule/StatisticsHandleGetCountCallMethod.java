package statisticsModule;


import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;


public class StatisticsHandleGetCountCallMethod implements MethodBeforeAdvice {

    static int countMethodCall = 0;

    public void before(Method method, Object[] objects, Object o) throws Throwable {
        countMethodCall++;
    }

    static int getCountMethodCall(){
        return countMethodCall;
    }
    static void resetCountMethodCall(){
        countMethodCall = 0;
    }



}
