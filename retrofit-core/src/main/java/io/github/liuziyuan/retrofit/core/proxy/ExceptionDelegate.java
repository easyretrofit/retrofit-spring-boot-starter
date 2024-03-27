package io.github.liuziyuan.retrofit.core.proxy;

import io.github.liuziyuan.retrofit.core.exception.RetrofitExtensionException;

import java.lang.reflect.Method;

public interface ExceptionDelegate<T extends RetrofitExtensionException> {

    Object invoke(Object proxy, Method method, Object[] args, RetrofitExtensionException throwable);
}
