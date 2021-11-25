package org.kovacstelekes.lambdatool;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class AbstractLambdaTool<T> implements LambdaTool<T> {
    private final T target;
    private final MethodCapturingCallback callback;

    protected AbstractLambdaTool(T target, MethodCapturingCallback callback) {
        this.target = target;
        this.callback = callback;
    }

    public Optional<Method> whichMethod(Consumer<T> parameterlessMethodInvocation) {
        return captureInvokedMethod(parameterlessMethodInvocation);
    }

    public Optional<Method> whichMethod(BiConsumer<T, ?> methodInvocationWithSingleParameter) {
        return captureInvokedMethod(enhancer ->
                methodInvocationWithSingleParameter.accept(enhancer, null)
        );
    }

    public Optional<Method> whichMethod(MethodCallWithTwoParameters<T> methodInvocationWithTwoParameters) {
        return captureInvokedMethod(enhancer ->
                methodInvocationWithTwoParameters.accept(enhancer, null, null)
        );
    }

    protected Optional<Method> captureInvokedMethod(Consumer<T> invocation) {
        callback.reset();
        invocation.accept(target);
        return callback.invokedMethod();
    }

    protected static abstract class MethodCapturingCallback {
        private Method capturedMethod;

        protected void reset() {
            capturedMethod = null;
        }

        protected Object captureMethodAndReturnNull(Method method) {
            this.capturedMethod = method;
            return null;
        }

        protected Optional<Method> invokedMethod() {
            return Optional.ofNullable(capturedMethod);
        }
    }
}
