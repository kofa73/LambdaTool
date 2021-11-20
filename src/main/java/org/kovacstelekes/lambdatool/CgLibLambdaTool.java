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
        invocation.accept(cgLibEnhancer);
        return interceptor.invokedMethod();
    }

    public interface MethodCallWithTwoParameters<T> {
        void accept(T proxy, Object param1, Object param2);
    }

    private static class CapturingInterceptor implements MethodInterceptor {
        private Method invokedMethod;

        public Method invokedMethod() {
            return invokedMethod;
        }

        @Override
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) {
            invokedMethod = method;
            return null;
        }
    }
}
