package org.kovacstelekes.lambdatool;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.matcher.ElementMatchers;

public class BbLambdaTool<T> extends AbstractLambdaTool<T> {
    private BbLambdaTool(T buddy, CapturingInvocationHandler invocationHandler) {
        super(buddy, invocationHandler);
    }

    public static <T> BbLambdaTool<T> forType(Class<T> type) {
        CapturingInvocationHandler invocationHandler = new CapturingInvocationHandler();
        T buddy;
        try {
            buddy = new ByteBuddy()
                    .subclass(type)
                    .method(ElementMatchers.any())
                    .intercept(InvocationHandlerAdapter.of(invocationHandler))
                    .make()
                    .load(type.getClassLoader())
                    .getLoaded()
                    .getConstructor()
                    .newInstance();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
        return new BbLambdaTool<>(buddy, invocationHandler);
    }
}
