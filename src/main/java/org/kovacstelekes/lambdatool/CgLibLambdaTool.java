package org.kovacstelekes.lambdatool;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class CgLibLambdaTool<T>  implements LambdaTool<T> {
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

    @Override
    public Method whichMethod(Consumer<T> parameterlessMethodInvocation) {
        return captureInvokedMethod(parameterlessMethodInvocation);
    }

    @Override
    public Method whichMethod(BiConsumer<T, ?> methodInvocationWithSingleParameter) {
        return captureInvokedMethod(enhancer ->
                methodInvocationWithSingleParameter.accept(enhancer, null)
        );
    }

    @Override
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

    private static class CapturingInterceptor implements MethodInterceptor {
        private Method invokedMethod;

        @Override
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) {
            invokedMethod = method;
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
