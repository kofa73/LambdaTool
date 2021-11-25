package org.kovacstelekes.lambdatool;

public interface MethodCallWithTwoParameters<T> {
    void accept(T proxy, Object param1, Object param2);
}