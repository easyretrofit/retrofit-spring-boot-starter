package io.github.liuziyuan.retrofit.spring.boot;

import io.github.liuziyuan.retrofit.core.RetrofitResourceContext;
import io.github.liuziyuan.retrofit.core.builder.*;
import io.github.liuziyuan.retrofit.core.extension.BaseInterceptor;
import io.github.liuziyuan.retrofit.core.generator.RetrofitBuilderGenerator;
import io.github.liuziyuan.retrofit.core.resource.RetrofitClientBean;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;

/**
 * Core中RetrofitBuilderGenerator接口的实现，由于Core并没有依赖Spring框架，所以并不能获取SpringBoot中注入的bean，所以这里需要重写
 * 当用户定义Builder，且使用@Component注入这个Builder类到SpringBoot容器中，这里就可以通过SpringBoot的ApplicationContext获取
 */
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
