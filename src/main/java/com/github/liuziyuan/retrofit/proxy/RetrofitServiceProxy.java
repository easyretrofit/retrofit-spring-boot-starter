package com.github.liuziyuan.retrofit.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author liuziyuan
 * @date 12/31/2021 3:27 PM
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
