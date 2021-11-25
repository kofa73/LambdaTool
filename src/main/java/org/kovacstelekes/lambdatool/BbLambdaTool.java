package org.kovacstelekes.lambdatool;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.InvocationHandler;

public class BbLambdaTool<T> extends AbstractLambdaTool<T> {
    private BbLambdaTool(T buddy, CapturingInvocationHandler invocationHandler) {
        super(buddy, invocationHandler);
    }

    public static <T> BbLambdaTool<T> forType(Class<T> type) {
        CapturingInvocationHandler invocationHandler = new CapturingInvocationHandler();
        T buddy = createBuddy(type, invocationHandler);
        return new BbLambdaTool<>(buddy, invocationHandler);
    }

    private static <T> T createBuddy(Class<T> type, InvocationHandler invocationHandler) {
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
        return buddy;
    }
}
