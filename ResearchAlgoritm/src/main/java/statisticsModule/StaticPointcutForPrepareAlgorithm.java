package statisticsModule;


import org.springframework.aop.support.StaticMethodMatcherPointcut;

import java.lang.reflect.Method;

public class StaticPointcutForPrepareAlgorithm extends StaticMethodMatcherPointcut {

    public boolean matches(Method method, Class<?> aClass) {
        return "prepare".equals(method.getName());
    }
}
