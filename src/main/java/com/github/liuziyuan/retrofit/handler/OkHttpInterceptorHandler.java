package com.github.liuziyuan.retrofit.handler;

import com.github.liuziyuan.retrofit.Handler;
import com.github.liuziyuan.retrofit.RetrofitResourceContext;
import com.github.liuziyuan.retrofit.extension.BaseInterceptor;
import lombok.SneakyThrows;
import okhttp3.Interceptor;

import java.lang.reflect.Constructor;

/**
 * @author liuziyuan
 * @date 1/14/2022 11:43 AM
 */
public class OkHttpInterceptorHandler implements Handler<Interceptor> {
    private final Class<? extends BaseInterceptor> interceptorClass;
    private RetrofitResourceContext resourceContext;

    public OkHttpInterceptorHandler(Class<? extends BaseInterceptor> interceptorClass, RetrofitResourceContext resourceContext) {
        this.interceptorClass = interceptorClass;
        this.resourceContext = resourceContext;
    }

    @SneakyThrows
    @Override
    public Interceptor generate() {
        Constructor<? extends BaseInterceptor> constructor = interceptorClass.getConstructor(RetrofitResourceContext.class);
        BaseInterceptor interceptor = constructor.newInstance(resourceContext);
        return interceptor;
    }
}
