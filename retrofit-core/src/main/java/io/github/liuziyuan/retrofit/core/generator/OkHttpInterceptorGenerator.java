package io.github.liuziyuan.retrofit.core.generator;

import io.github.liuziyuan.retrofit.core.RetrofitResourceContext;
import io.github.liuziyuan.retrofit.core.annotation.RetrofitInterceptor;
import io.github.liuziyuan.retrofit.core.extension.BaseInterceptor;
import io.github.liuziyuan.retrofit.core.resource.RetrofitInterceptorBean;
import lombok.SneakyThrows;
import okhttp3.Interceptor;

import java.lang.reflect.Constructor;

/**
 * Generate OkHttpInterceptor instance
 *
 * @author liuziyuan
 */
public abstract class OkHttpInterceptorGenerator implements Generator<Interceptor> {
    private final Class<? extends BaseInterceptor> interceptorClass;
    private final RetrofitInterceptorBean retrofitInterceptor;
    private final RetrofitResourceContext resourceContext;
    private BaseInterceptor interceptor;

    public OkHttpInterceptorGenerator(RetrofitInterceptorBean retrofitInterceptor, RetrofitResourceContext resourceContext) {
        this.retrofitInterceptor = retrofitInterceptor;
        this.interceptorClass = retrofitInterceptor.getHandler();
        this.resourceContext = resourceContext;
        this.interceptor = null;
    }

    public abstract BaseInterceptor buildInjectionObject(Class<? extends BaseInterceptor> clazz);

    @SneakyThrows
    @Override
    public Interceptor generate() {
        interceptor = buildInjectionObject(retrofitInterceptor.getHandler());
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
            interceptor.setInclude(retrofitInterceptor.getInclude());
            interceptor.setExclude(retrofitInterceptor.getExclude());
        }
        return interceptor;

    }
}
