package org.kovacstelekes.lambdatool;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class CgLibLambdaTool<T> extends AbstractLambdaTool<T> {
    private CgLibLambdaTool(T cgLibEnhancer, CapturingInterceptor interceptor) {
        super(cgLibEnhancer, interceptor);
    }

    public static <T> CgLibLambdaTool<T> forType(Class<T> type) {
        CapturingInterceptor interceptor = new CapturingInterceptor();
        T cgLibEnhancer = type.cast(
                Enhancer.create(type, interceptor)
        );
        return new CgLibLambdaTool<>(cgLibEnhancer, interceptor);
    }

    private static class CapturingInterceptor extends MethodCapturingCallback implements MethodInterceptor {
        @Override
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) {
            return captureMethodAndReturnNull(method);
        }
    }
}
