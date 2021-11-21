package org.kovacstelekes.lambdatool;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
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

    public Method whichMethod(Consumer<T> parameterlessMethodInvocation) {
        return captureInvokedMethod(parameterlessMethodInvocation);
    }

    public Method whichMethod(BiConsumer<T, ?> methodInvocationWithSingleParameter) {
        return captureInvokedMethod(proxy ->
                methodInvocationWithSingleParameter.accept(proxy, null)
        );
    }

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

    public interface MethodCallWithTwoParameters<T> {
        void accept(T proxy, Object param1, Object param2);
    }

    /**
     * An implementation of the {@link InvocationHandler} interface, which captures the method that was invoked and
     * returns {@code null}. It does NOT invoke the original method.
     * <p>
     * The captured method is stored in a private field and can be queried later calling {@link #invokedMethod()}
     */
    private static class CapturingInvocationHandler implements InvocationHandler {
        private Method invokedMethod;

        /**
         * Reset the invoked method. It is needed to allow repetitive call to the same {@link BbLambdaTool} instance.
         * If the method is not reset and the same instance is used for multiple calls, the captured method may be the
         * last one that was successfully captured, instead of {@code null} when the
         * {@link BbLambdaTool#whichMethod(Consumer) whichMethod()} is invoked for a reference or lambda expression that
         * cannot be captured.
         */
        void reset() {
            invokedMethod = null;
        }
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) {
            // side effect: method is captured
            this.invokedMethod = method;
            // return value is ignored by caller
            return null;
        }

        private Method invokedMethod() {
            return invokedMethod;
        }
    }
}
