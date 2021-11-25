package org.kovacstelekes.lambdatool;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface LambdaTool<T> {
    Optional<Method> whichMethod(Consumer<T> parameterlessMethodInvocation);

    Optional<Method> whichMethod(BiConsumer<T, ?> methodInvocationWithSingleParameter);

    Optional<Method> whichMethod(MethodCallWithTwoParameters<T> methodInvocationWithTwoParameters);

    interface MethodCallWithTwoParameters<T> {
        void accept(T proxy, Object param1, Object param2);
    }
}
