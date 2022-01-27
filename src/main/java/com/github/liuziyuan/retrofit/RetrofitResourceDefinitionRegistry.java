package com.github.liuziyuan.retrofit;

import com.github.liuziyuan.retrofit.annotation.EnableRetrofit;
import com.github.liuziyuan.retrofit.annotation.RetrofitBuilder;
import com.github.liuziyuan.retrofit.annotation.RetrofitInterceptor;
import com.github.liuziyuan.retrofit.extension.UrlOverWriteInterceptor;
import com.github.liuziyuan.retrofit.handler.CallAdapterFactoryHandler;
import com.github.liuziyuan.retrofit.handler.ConverterFactoryHandler;
import com.github.liuziyuan.retrofit.handler.OkHttpClientBuilderHandler;
import com.github.liuziyuan.retrofit.handler.OkHttpInterceptorHandler;
import com.github.liuziyuan.retrofit.proxy.RetrofitServiceProxyFactory;
import com.github.liuziyuan.retrofit.resource.RetrofitClientBean;
import com.github.liuziyuan.retrofit.resource.RetrofitServiceBean;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.support.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author liuziyuan
 * @date 1/5/2022 11:22 AM
 */
@Slf4j
public class RetrofitResourceDefinitionRegistry implements ImportBeanDefinitionRegistrar, EnvironmentAware, ApplicationContextAware, ResourceLoaderAware {

    private RetrofitResourceContext context;
    private Environment environment;
    private ApplicationContext applicationContext;
    private ResourceLoader resourceLoader;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes enableRetrofitAnnoAttr = AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(EnableRetrofit.class.getName()));
        if (enableRetrofitAnnoAttr != null) {
            this.registerRetrofitResourceBeanDefinitions(enableRetrofitAnnoAttr, registry);
        }
    }

    void registerRetrofitResourceBeanDefinitions(AnnotationAttributes annoAttrs, BeanDefinitionRegistry registry) {
        context = new RetrofitResourceContext();
        RetrofitResourceScanner scanner = new RetrofitResourceScanner();
        List<String> basePackages = new ArrayList<>();
        basePackages.addAll(Arrays.stream(annoAttrs.getStringArray("value")).filter(StringUtils::hasText).collect(Collectors.toList()));
        basePackages.addAll(Arrays.stream(annoAttrs.getStringArray("basePackages")).filter(StringUtils::hasText).collect(Collectors.toList()));
        basePackages.addAll(Arrays.stream(annoAttrs.getClassArray("basePackageClasses")).map(ClassUtils::getPackageName).collect(Collectors.toList()));
        final Set<Class<?>> retrofitBuilderClassSet = scanner.doScan(StringUtils.toStringArray(basePackages));
        RetrofitResourceBuilder retrofitResourceBuilder = new RetrofitResourceBuilder(environment);
        retrofitResourceBuilder.build(retrofitBuilderClassSet);
        final List<RetrofitClientBean> retrofitClientBeanList = retrofitResourceBuilder.getRetrofitClientBeanList();
        context.setRetrofitClients(retrofitClientBeanList);
        context.setApplicationContext(applicationContext);
        context.setEnvironment(environment);
        context.setResourceLoader(resourceLoader);
        BeanDefinitionBuilder builder;
        //registry RetrofitResourceContext
        if (!context.getRetrofitClients().isEmpty()) {
            builder = BeanDefinitionBuilder.genericBeanDefinition(RetrofitResourceContext.class, () -> context);
            GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();
            registry.registerBeanDefinition(RetrofitResourceContext.class.getName(), definition);
        }
        //registry Retrofit
        for (RetrofitClientBean clientBean : retrofitClientBeanList) {
            builder = BeanDefinitionBuilder.genericBeanDefinition(Retrofit.class, () -> {
                final Retrofit.Builder retrofitBuilder = getRetrofitBuilder(clientBean);
                return retrofitBuilder.build();
            });
            AbstractBeanDefinition definition = builder.getRawBeanDefinition();
            AutowireCandidateQualifier qualifier = new AutowireCandidateQualifier(Retrofit.class);
            qualifier.setAttribute("value", clientBean.getRetrofitInstanceName());
            definition.addQualifier(qualifier);
            registry.registerBeanDefinition(clientBean.getRetrofitInstanceName(), definition);
        }
        //registry proxy interface of retrofit
        for (RetrofitClientBean clientBean : retrofitClientBeanList) {
            for (RetrofitServiceBean serviceBean : clientBean.getRetrofitServices()) {
                builder = BeanDefinitionBuilder.genericBeanDefinition(serviceBean.getSelfClazz());
                GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();
                definition.getConstructorArgumentValues().addGenericArgumentValue(Objects.requireNonNull(definition.getBeanClassName()));
                definition.getConstructorArgumentValues().addGenericArgumentValue(serviceBean);
                definition.addQualifier(new AutowireCandidateQualifier(Qualifier.class, serviceBean.getSelfClazz().getName()));
                definition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
                definition.setBeanClass(RetrofitServiceProxyFactory.class);
                registry.registerBeanDefinition(serviceBean.getSelfClazz().getName(), definition);
            }
        }
    }

    @SneakyThrows
    private Retrofit.Builder getRetrofitBuilder(RetrofitClientBean clientBean) {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(clientBean.getRealHostUrl());
        setRetrofitCallAdapterFactory(clientBean, builder);
        setRetrofitConverterFactory(clientBean, builder);
        setRetrofitOkHttpClient(clientBean, builder);
        return builder;
    }

    private void setRetrofitCallAdapterFactory(RetrofitClientBean clientBean, Retrofit.Builder builder) {
        final RetrofitBuilder retrofitBuilder = clientBean.getRetrofitBuilder();
        final List<CallAdapter.Factory> callAdapterFactories = getCallAdapterFactories(retrofitBuilder.addCallAdapterFactory());
        if (!CollectionUtils.isEmpty(callAdapterFactories)) {
            callAdapterFactories.forEach(builder::addCallAdapterFactory);
        }
    }

    private void setRetrofitConverterFactory(RetrofitClientBean clientBean, Retrofit.Builder builder) {
        final RetrofitBuilder retrofitBuilder = clientBean.getRetrofitBuilder();
        final List<Converter.Factory> converterFactories = getConverterFactories(retrofitBuilder.addConverterFactory());
        if (!CollectionUtils.isEmpty(converterFactories)) {
            converterFactories.forEach(builder::addConverterFactory);
        }
    }

    @SneakyThrows
    private void setRetrofitOkHttpClient(RetrofitClientBean clientBean, Retrofit.Builder builder) {
        final RetrofitBuilder retrofitBuilder = clientBean.getRetrofitBuilder();
        final List<RetrofitInterceptor> interceptors = clientBean.getInterceptors();
        OkHttpClient.Builder okHttpClientBuilder;
        OkHttpClientBuilderHandler okHttpClientBuilderHandler;
        if (retrofitBuilder.client() != null) {
            okHttpClientBuilderHandler = new OkHttpClientBuilderHandler(retrofitBuilder.client());
            okHttpClientBuilder = okHttpClientBuilderHandler.generate();
        } else {
            okHttpClientBuilder = new OkHttpClient.Builder();
        }
        okHttpClientBuilder.addInterceptor(new UrlOverWriteInterceptor(context));
        final List<Interceptor> okHttpInterceptors = getOkHttpInterceptor(interceptors);

        okHttpInterceptors.forEach(okHttpClientBuilder::addInterceptor);
        builder.client(okHttpClientBuilder.build());
    }

    @SneakyThrows
    private List<Interceptor> getOkHttpInterceptor(List<RetrofitInterceptor> interceptors) {
        List<Interceptor> interceptorList = new ArrayList<>();
        OkHttpInterceptorHandler okHttpInterceptorHandler;
        for (RetrofitInterceptor interceptor : interceptors) {
            okHttpInterceptorHandler = new OkHttpInterceptorHandler(interceptor.handler(), context);
            final Interceptor generateInterceptor = okHttpInterceptorHandler.generate();
            interceptorList.add(generateInterceptor);
        }
        return interceptorList;
    }

    @SneakyThrows
    private List<CallAdapter.Factory> getCallAdapterFactories(Class<? extends CallAdapter.Factory>[] callAdapterFactoryClasses) {
        List<CallAdapter.Factory> callAdapterFactories = new ArrayList<>();
        CallAdapterFactoryHandler adapterFactoryHandler;
        for (Class<? extends CallAdapter.Factory> callAdapterFactoryClass : callAdapterFactoryClasses) {
            adapterFactoryHandler = new CallAdapterFactoryHandler(callAdapterFactoryClass);
            callAdapterFactories.add(adapterFactoryHandler.generate());
        }
        return callAdapterFactories;
    }

    @SneakyThrows
    private List<Converter.Factory> getConverterFactories(Class<? extends Converter.Factory>[] converterFactoryClasses) {
        List<Converter.Factory> converterFactories = new ArrayList<>();
        ConverterFactoryHandler converterFactoryHandler;
        for (Class<? extends Converter.Factory> converterFactoryClass : converterFactoryClasses) {
            converterFactoryHandler = new ConverterFactoryHandler(converterFactoryClass);
            converterFactories.add(converterFactoryHandler.generate());
        }
        return converterFactories;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
