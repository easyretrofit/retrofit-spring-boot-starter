package io.github.liuziyuan.retrofit.core.generator;

import io.github.liuziyuan.retrofit.core.AppContext;
import io.github.liuziyuan.retrofit.core.Env;
import io.github.liuziyuan.retrofit.core.Generator;
import io.github.liuziyuan.retrofit.core.RetrofitResourceContext;
import io.github.liuziyuan.retrofit.core.annotation.RetrofitInterceptor;
import io.github.liuziyuan.retrofit.core.exception.NoSuchBeanDefinitionException;
import io.github.liuziyuan.retrofit.core.extension.BaseInterceptor;
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
    private AppContext applicationContext;
    private BaseInterceptor interceptor;

    public OkHttpInterceptorGenerator(RetrofitInterceptor retrofitInterceptor, RetrofitResourceContext resourceContext, AppContext applicationContext) {
        this.retrofitInterceptor = retrofitInterceptor;
        this.interceptorClass = retrofitInterceptor.handler();
        this.resourceContext = resourceContext;
        this.interceptor = null;
        this.applicationContext = applicationContext;
    }

    @SneakyThrows
    @Override
    public Interceptor generate() {
        interceptor = applicationContext.getBean(retrofitInterceptor.handler());
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
