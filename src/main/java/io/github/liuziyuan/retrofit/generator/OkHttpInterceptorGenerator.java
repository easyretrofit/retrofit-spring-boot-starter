package io.github.liuziyuan.retrofit.generator;

import io.github.liuziyuan.retrofit.Generator;
import io.github.liuziyuan.retrofit.RetrofitResourceContext;
import io.github.liuziyuan.retrofit.annotation.RetrofitInterceptor;
import io.github.liuziyuan.retrofit.extension.BaseInterceptor;
import lombok.SneakyThrows;
import okhttp3.Interceptor;

import java.lang.reflect.Constructor;

/**
 * Generate OkHttpInterceptor instance
 *
 * @author liuziyuan
 */
public class OkHttpInterceptorGenerator implements Generator<Interceptor> {
    private final Class<? extends BaseInterceptor> interceptorClass;
    private final RetrofitInterceptor retrofitInterceptor;
    private final RetrofitResourceContext resourceContext;
    private BaseInterceptor interceptor;

    public OkHttpInterceptorGenerator(RetrofitInterceptor retrofitInterceptor, RetrofitResourceContext resourceContext, BaseInterceptor componentInterceptor) {
        this.retrofitInterceptor = retrofitInterceptor;
        this.interceptorClass = retrofitInterceptor.handler();
        this.resourceContext = resourceContext;
        this.interceptor = componentInterceptor;
    }

    @SneakyThrows
    @Override
    public Interceptor generate() {
        if (interceptor == null && interceptorClass != null) {
            Constructor<? extends BaseInterceptor> constructor;
            BaseInterceptor interceptorInstance;
            try {
                constructor = interceptorClass.getConstructor(RetrofitResourceContext.class);
                interceptorInstance = constructor.newInstance(resourceContext);
            } catch (NoSuchMethodException exception) {
                interceptorInstance = interceptorClass.newInstance();
            }
            interceptor = interceptorInstance;
        }
        if (interceptor != null) {
            interceptor.setInclude(retrofitInterceptor.include());
            interceptor.setExclude(retrofitInterceptor.exclude());
        }
        return interceptor;

    }
}
