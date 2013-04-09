package StatisticsModule;


import org.springframework.aop.support.StaticMethodMatcherPointcut;

import java.lang.reflect.Method;

public class StaticPointcutForApplyAlgorithm extends StaticMethodMatcherPointcut {

    public boolean matches(Method method, Class<?> aClass) {
        return "applyAlgorithm".equals(method.getName());
    }
}
