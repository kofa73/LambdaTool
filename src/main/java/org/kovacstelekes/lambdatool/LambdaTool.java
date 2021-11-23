package org.kovacstelekes.lambdatool;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class LambdaTool<T> {
    private final T proxy;
    private final CapturingInvocationHandler invocationHandler;

    private LambdaTool(Class<T> type) {
        invocationHandler = new CapturingInvocationHandler();
        proxy = type.cast(
                Proxy.newProxyInstance(
                        type.getClassLoader(),
                        new Class<?>[]{type},
                        invocationHandler)
        );
    }

    public static <T> LambdaTool<T> forType(Class<T> type) {
        return new LambdaTool<>(type);
    }

    public Optional<Method> whichMethod(Consumer<T> parameterlessMethodInvocation) {
        return captureInvokedMethod(parameterlessMethodInvocation);
    }

    public Optional<Method> whichMethod(BiConsumer<T, ?> methodInvocationWithSingleParameter) {
        return captureInvokedMethod(proxy ->
                methodInvocationWithSingleParameter.accept(proxy, null)
        );
    }

    public Optional<Method> whichMethod(MethodCallWithTwoParameters<T> methodInvocationWithTwoParameters) {
        return captureInvokedMethod(proxy ->
                methodInvocationWithTwoParameters.accept(proxy, null, null)
        );
    }

    private Optional<Method> captureInvokedMethod(Consumer<T> invocation) {
        invocationHandler.reset();
        invocation.accept(proxy);
        return invocationHandler.invokedMethod();
    }

    public interface MethodCallWithTwoParameters<T> {
        void accept(T proxy, Object param1, Object param2);
    }

    private static class CapturingInvocationHandler implements InvocationHandler {
        private Method invokedMethod;

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) {
            // side effect: Optional<Method> is captured
            this.invokedMethod = method;
            // return value is ignored by caller
            return null;
        }

        private void reset() {
            invokedMethod = null;
        }

        private Optional<Method> invokedMethod() {
            return Optional.ofNullable(invokedMethod);
        }
    }
}
