package org.kovacstelekes.lambdatool;

import java.lang.reflect.Proxy;

public class ProxyLambdaTool<T> extends AbstractLambdaTool<T> {
    private ProxyLambdaTool(T proxy, CapturingInvocationHandler invocationHandler) {
        super(proxy, invocationHandler);
    }

    public static <T> ProxyLambdaTool<T> forType(Class<T> type) {
        CapturingInvocationHandler invocationHandler = new CapturingInvocationHandler();
        T proxy = type.cast(
                Proxy.newProxyInstance(
                        type.getClassLoader(),
                        new Class<?>[]{type},
                        invocationHandler)
        );
        return new ProxyLambdaTool<>(proxy, invocationHandler);
    }
}
