package io.github.liuziyuan.retrofit.springboot.generator;

import io.github.liuziyuan.retrofit.core.RetrofitResourceContext;
import io.github.liuziyuan.retrofit.core.builder.*;
import io.github.liuziyuan.retrofit.core.generator.RetrofitBuilderGenerator;
import io.github.liuziyuan.retrofit.core.resource.RetrofitClientBean;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;

public class SpringBootRetrofitBuilderGenerator extends RetrofitBuilderGenerator {
    private final ApplicationContext applicationContext;

    public SpringBootRetrofitBuilderGenerator(RetrofitClientBean clientBean, RetrofitResourceContext context, ApplicationContext applicationContext) {
        super(clientBean, context);
        this.applicationContext = applicationContext;
    }

    @Override
    public BaseCallFactoryBuilder buildInjectionCallFactory(Class<? extends BaseCallFactoryBuilder> clazz) {
        try {
            return applicationContext.getBean(clazz);
        } catch (NoSuchBeanDefinitionException e) {
            return null;
        }
    }

    @Override
    public BaseCallBackExecutorBuilder buildInjectionCallBackExecutor(Class<? extends BaseCallBackExecutorBuilder> clazz) {
        try {
            return applicationContext.getBean(clazz);
        } catch (NoSuchBeanDefinitionException e) {
            return null;
        }
    }

    @Override
    public BaseOkHttpClientBuilder buildInjectionOkHttpClient(Class<? extends BaseOkHttpClientBuilder> clazz) {
        try {
            return applicationContext.getBean(clazz);
        } catch (NoSuchBeanDefinitionException e) {
            return null;
        }
    }

    @Override
    public BaseInterceptor buildInjectionInterceptor(Class<? extends BaseInterceptor> clazz) {
        try {
            return applicationContext.getBean(clazz);
        } catch (NoSuchBeanDefinitionException e) {
            return null;
        }
    }

    @Override
    public BaseCallAdapterFactoryBuilder buildInjectionCallAdapterFactor(Class<? extends BaseCallAdapterFactoryBuilder> clazz) {
        try {
            return applicationContext.getBean(clazz);
        } catch (NoSuchBeanDefinitionException e) {
            return null;
        }
    }

    @Override
    public BaseConverterFactoryBuilder buildInjectionConverterFactory(Class<? extends BaseConverterFactoryBuilder> clazz) {
        try {
            return applicationContext.getBean(clazz);
        } catch (NoSuchBeanDefinitionException e) {
            return null;
        }
    }
}
