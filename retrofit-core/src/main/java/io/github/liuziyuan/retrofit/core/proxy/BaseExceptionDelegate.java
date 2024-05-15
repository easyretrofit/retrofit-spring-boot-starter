package io.github.liuziyuan.retrofit.core.proxy;

import io.github.liuziyuan.retrofit.core.exception.RetrofitExtensionException;


public abstract class BaseExceptionDelegate<T extends RetrofitExtensionException> implements ExceptionDelegate<RetrofitExtensionException> {
    private final Class<T> exceptionClassName;

    public BaseExceptionDelegate(Class<T> exceptionClassName) {
        this.exceptionClassName = exceptionClassName;
    }

    public Class<T> getExceptionClassName() {
        return exceptionClassName;
    }
}
