package io.github.liuziyuan.retrofit.handler;

import io.github.liuziyuan.retrofit.Handler;
import io.github.liuziyuan.retrofit.RetrofitResourceContext;
import io.github.liuziyuan.retrofit.extension.BaseInterceptor;
import lombok.SneakyThrows;
import okhttp3.Interceptor;

import java.lang.reflect.Constructor;

/**
 * @author liuziyuan
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
