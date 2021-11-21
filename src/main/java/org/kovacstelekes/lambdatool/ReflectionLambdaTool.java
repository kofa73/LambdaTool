package org.kovacstelekes.lambdatool;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ReflectionLambdaTool<T> implements LambdaTool<T> {
    private final T proxy;
    private final CapturingInvocationHandler invocationHandler;

    static <T> ReflectionLambdaTool<T> forType(Class<T> type) {
        return new ReflectionLambdaTool<>(type);
    }

    private ReflectionLambdaTool(Class<T> type) {
        invocationHandler = new CapturingInvocationHandler();
        proxy = type.cast(
                Proxy.newProxyInstance(
                        type.getClassLoader(),
                        new Class<?>[]{type},
                        invocationHandler)
        );
    }

    @Override
    public Method whichMethod(Consumer<T> parameterlessMethodInvocation) {
        return captureInvokedMethod(parameterlessMethodInvocation);
    }

    @Override
    public Method whichMethod(BiConsumer<T, ?> methodInvocationWithSingleParameter) {
        return captureInvokedMethod(proxy ->
                methodInvocationWithSingleParameter.accept(proxy, null)
        );
    }

    @Override
    public Method whichMethod(MethodCallWithTwoParameters<T> methodInvocationWithTwoParameters) {
        return captureInvokedMethod(proxy ->
                methodInvocationWithTwoParameters.accept(proxy, null, null)
        );
    }

    private Method captureInvokedMethod(Consumer<T> invocation) {
        invocationHandler.reset();
        invocation.accept(proxy);
        return invocationHandler.invokedMethod();
    }

    private static class CapturingInvocationHandler implements InvocationHandler {
        private Method invokedMethod;

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) {
            // side effect: method is captured
            this.invokedMethod = method;
            // return value is ignored by caller
            return null;
        }

        private void reset() {
            invokedMethod = null;
        }

        private Method invokedMethod() {
            return invokedMethod;
        }
    }
}
