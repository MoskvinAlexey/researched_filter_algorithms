package statisticsModule;


import org.springframework.aop.support.StaticMethodMatcherPointcut;

import java.lang.reflect.Method;

public class StaticPointcutForFilterRules extends StaticMethodMatcherPointcut {

    public boolean matches(Method method, Class<?> aClass) {
        return "getFilterRuleSingle".equals(method.getName());
    }
}
