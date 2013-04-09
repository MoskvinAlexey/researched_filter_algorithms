package StatisticsModule;


import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;


public class StatisticsHandleGetCountCallMethod implements MethodInterceptor {

    int countMethodCall = 0;

    public Object invoke(MethodInvocation methodInvocation) throws Throwable {

        System.out.println(countMethodCall++);
        Object retVal = methodInvocation.proceed();





        return retVal;
    }
}
