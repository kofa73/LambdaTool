package org.kovacstelekes.lambdatool;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class CgLibLambdaTool<T> {
    private final T cgLibEnhancer;
    private final CapturingInterceptor interceptor;

    private CgLibLambdaTool(Class<T> type) {
        interceptor = new CapturingInterceptor();
        cgLibEnhancer = type.cast(
                Enhancer.create(type, interceptor)
        );
    }

    public static <T> CgLibLambdaTool<T> forType(Class<T> type) {
        return new CgLibLambdaTool<>(type);
    }

    public Method whichMethod(Consumer<T> parameterlessMethodInvocation) {
        return captureInvokedMethod(parameterlessMethodInvocation);
    }

    public Method whichMethod(BiConsumer<T, ?> methodInvocationWithSingleParameter) {
        return captureInvokedMethod(enhancer ->
                methodInvocationWithSingleParameter.accept(enhancer, null)
        );
    }

    public Method whichMethod(MethodCallWithTwoParameters<T> methodInvocationWithTwoParameters) {
        return captureInvokedMethod(enhancer ->
                methodInvocationWithTwoParameters.accept(enhancer, null, null)
        );
    }

    private Method captureInvokedMethod(Consumer<T> invocation) {
        interceptor.reset();
        invocation.accept(cgLibEnhancer);
        return interceptor.invokedMethod();
    }

    public interface MethodCallWithTwoParameters<T> {
        void accept(T proxy, Object param1, Object param2);
    }

    /**
     * An implementation of the {@link MethodInterceptor} interface, which captures the method that was invoked and
     * returns {@code null}. It does NOT invoke the original method.
     * <p>
     * The captured method is stored in a private field and can be queried later calling {@link #invokedMethod()}
     */
    private static class CapturingInterceptor implements MethodInterceptor {
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

        private Method invokedMethod() {
            return invokedMethod;
        }

        @Override
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) {
            invokedMethod = method;
            return null;
        }
    }
}
