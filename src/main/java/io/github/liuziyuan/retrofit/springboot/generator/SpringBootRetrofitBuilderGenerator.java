package io.github.liuziyuan.retrofit.springboot.generator;

import io.github.liuziyuan.retrofit.core.RetrofitResourceContext;
import io.github.liuziyuan.retrofit.core.extension.*;
import io.github.liuziyuan.retrofit.core.generator.RetrofitBuilderGenerator;
import io.github.liuziyuan.retrofit.core.resource.RetrofitClientBean;
import org.springframework.context.ApplicationContext;

public class SpringBootRetrofitBuilderGenerator extends RetrofitBuilderGenerator {
    private final ApplicationContext applicationContext;

    public SpringBootRetrofitBuilderGenerator(RetrofitClientBean clientBean, RetrofitResourceContext context, ApplicationContext applicationContext) {
        super(clientBean, context);
        this.applicationContext = applicationContext;
    }

    @Override
    public BaseCallFactoryBuilder buildInjectionCallFactory(Class<? extends BaseCallFactoryBuilder> clazz) {
        if (applicationContext.containsBean(clazz.getName())) {
            return applicationContext.getBean(clazz);
        }
        return null;
    }

    @Override
    public BaseCallBackExecutorBuilder buildInjectionCallBackExecutor(Class<? extends BaseCallBackExecutorBuilder> clazz) {
        if (applicationContext.containsBean(clazz.getName())) {
            return applicationContext.getBean(clazz);
        }
        return null;
    }

    @Override
    public BaseOkHttpClientBuilder buildInjectionOkHttpClient(Class<? extends BaseOkHttpClientBuilder> clazz) {
        if (applicationContext.containsBean(clazz.getName())) {
            return applicationContext.getBean(clazz);
        }
        return null;
    }

    @Override
    public BaseInterceptor buildInjectionInterceptor(Class<? extends BaseInterceptor> clazz) {
        if (applicationContext.containsBean(clazz.getName())) {
            return applicationContext.getBean(clazz);
        }
        return null;
    }

    @Override
    public BaseCallAdapterFactoryBuilder buildInjectionCallAdapterFactor(Class<? extends BaseCallAdapterFactoryBuilder> clazz) {
        if (applicationContext.containsBean(clazz.getName())) {
            return applicationContext.getBean(clazz);
        }
        return null;
    }

    @Override
    public BaseConverterFactoryBuilder buildInjectionConverterFactory(Class<? extends BaseConverterFactoryBuilder> clazz) {
        if (applicationContext.containsBean(clazz.getName())) {
            return applicationContext.getBean(clazz);
        }
        return null;
    }
}
