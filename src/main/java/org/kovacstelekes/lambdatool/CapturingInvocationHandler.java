package org.kovacstelekes.lambdatool;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

class CapturingInvocationHandler extends AbstractLambdaTool.MethodCapturingCallback implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        return captureMethodAndReturnNull(method);
    }
}
