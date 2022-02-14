package io.github.liuziyuan.retrofit.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author liuziyuan
 */
public class RetrofitServiceProxy<T> implements InvocationHandler {

    private final T t;

    public RetrofitServiceProxy(T t) {
        this.t = t;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(t, args);
    }
}
