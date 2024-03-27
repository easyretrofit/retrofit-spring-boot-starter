package io.github.liuziyuan.retrofit.core.proxy;

import io.github.liuziyuan.retrofit.core.exception.RetrofitExtensionException;

import java.lang.reflect.Method;

public class ExceptionDelegator<T extends RetrofitExtensionException> implements ExceptionDelegate<T> {

    private final ExceptionDelegate<T> exceptionDelegate;

    public ExceptionDelegator(ExceptionDelegate<T> exceptionDelegate) {
        this.exceptionDelegate = exceptionDelegate;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args, RetrofitExtensionException throwable) {
        return exceptionDelegate.invoke(proxy, method, args, throwable);
    }
}
