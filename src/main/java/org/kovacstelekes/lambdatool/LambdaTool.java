package org.kovacstelekes.lambdatool;

import java.lang.reflect.Method;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface LambdaTool<T> {
    Method whichMethod(Consumer<T> parameterlessMethodInvocation);

    Method whichMethod(BiConsumer<T, ?> methodInvocationWithSingleParameter);

    Method whichMethod(MethodCallWithTwoParameters<T> methodInvocationWithTwoParameters);

    interface MethodCallWithTwoParameters<T2> {
        void accept(T2 proxy, Object param1, Object param2);
    }
}
