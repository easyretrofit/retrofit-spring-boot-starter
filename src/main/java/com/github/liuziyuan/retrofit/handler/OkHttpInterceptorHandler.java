package com.github.liuziyuan.retrofit.handler;

import com.github.liuziyuan.retrofit.Handler;
import com.github.liuziyuan.retrofit.extension.BaseInterceptor;
import lombok.SneakyThrows;
import okhttp3.Interceptor;

/**
 * @author liuziyuan
 * @date 1/14/2022 11:43 AM
 */
public class OkHttpInterceptorHandler implements Handler<Interceptor> {
    private final Class<? extends BaseInterceptor> interceptorClass;

    public OkHttpInterceptorHandler(Class<? extends BaseInterceptor> interceptorClass) {
        this.interceptorClass = interceptorClass;
    }

    @SneakyThrows
    @Override
    public Interceptor generate() {
        return interceptorClass.newInstance();
    }
}
