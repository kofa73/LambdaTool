package org.kovacstelekes.lambdatool;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class BbLambdaTool<T> {
    private final T buddy;
    private final CapturingInvocationHandler invocationHandler;

    private BbLambdaTool(Class<T> type) {
        try {
            invocationHandler = new CapturingInvocationHandler();
            buddy = new ByteBuddy()
                    .subclass(type)
                    .method(ElementMatchers.any())
                    .intercept(InvocationHandlerAdapter.of(invocationHandler))
                    .make()
                    .load(type.getClassLoader())
                    .getLoaded()
                    .newInstance();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> BbLambdaTool<T> forType(Class<T> type) {
        return new BbLambdaTool<>(type);
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
        invocationHandler.reset();
        invocation.accept(buddy);
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

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) {
            // side effect: method is captured
            this.invokedMethod = method;
            // return value is ignored by caller
            return null;
        }

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

        /**
         *
         * @return the method that was captured by the {@link InvocationHandler}
         */
        Method invokedMethod() {
            return invokedMethod;
        }
    }
}
