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
    private BaseInterceptor componentInterceptor;

    public OkHttpInterceptorHandler(Class<? extends BaseInterceptor> interceptorClass, RetrofitResourceContext resourceContext, BaseInterceptor componentInterceptor) {
        this.interceptorClass = interceptorClass;
        this.resourceContext = resourceContext;
        this.componentInterceptor = componentInterceptor;
    }

    @SneakyThrows
    @Override
    public Interceptor generate() {
        if (componentInterceptor != null) {
            return componentInterceptor;
        }
        if (interceptorClass != null) {
            Constructor<? extends BaseInterceptor> constructor = interceptorClass.getConstructor(RetrofitResourceContext.class);
            BaseInterceptor interceptor = constructor.newInstance(resourceContext);
            return interceptor;
        }
        return null;

    }
}
