package io.github.liuziyuan.retrofit.core.proxy;

import io.github.liuziyuan.retrofit.core.exception.RetrofitExtensionException;
import lombok.Getter;

@Getter
public abstract class BaseExceptionDelegate<T extends RetrofitExtensionException> implements ExceptionDelegate<RetrofitExtensionException> {
    private final Class<T> exceptionClassName;

    public BaseExceptionDelegate(Class<T> exceptionClassName) {
        this.exceptionClassName = exceptionClassName;
    }

}
