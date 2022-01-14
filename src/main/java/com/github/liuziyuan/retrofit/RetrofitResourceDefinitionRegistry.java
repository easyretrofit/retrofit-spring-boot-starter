package com.github.liuziyuan.retrofit;

import com.github.liuziyuan.retrofit.annotation.EnableRetrofit;
import com.github.liuziyuan.retrofit.annotation.RetrofitBuilder;
import com.github.liuziyuan.retrofit.annotation.RetrofitInterceptor;
import com.github.liuziyuan.retrofit.extension.OkHttpClientBuilder;
import com.github.liuziyuan.retrofit.factory.RetrofitResourceBuilder;
import com.github.liuziyuan.retrofit.factory.RetrofitResourceScanner;
import com.github.liuziyuan.retrofit.model.RetrofitClientBean;
import com.github.liuziyuan.retrofit.model.RetrofitServiceBean;
import com.github.liuziyuan.retrofit.proxy.RetrofitServiceProxyFactory;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.support.*;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;
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
@Component
public class RetrofitResourceDefinitionRegistry implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware {

    private Environment environment;
    private ResourceLoader resourceLoader;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes enableRetrofitAnnoAttr = AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(EnableRetrofit.class.getName()));
        if (enableRetrofitAnnoAttr != null) {
            this.registerRetrofitResourceBeanDefinitions(enableRetrofitAnnoAttr, registry);
        }
    }

    void registerRetrofitResourceBeanDefinitions(AnnotationAttributes annoAttrs, BeanDefinitionRegistry registry) {
        RetrofitResourceScanner scanner = new RetrofitResourceScanner();
        List<String> basePackages = new ArrayList();
        basePackages.addAll(Arrays.stream(annoAttrs.getStringArray("value")).filter(StringUtils::hasText).collect(Collectors.toList()));
        basePackages.addAll(Arrays.stream(annoAttrs.getStringArray("basePackages")).filter(StringUtils::hasText).collect(Collectors.toList()));
        basePackages.addAll(Arrays.stream(annoAttrs.getClassArray("basePackageClasses")).map(ClassUtils::getPackageName).collect(Collectors.toList()));
        final Set<Class<?>> retrofitBuilderClassSet = scanner.doScan(StringUtils.toStringArray(basePackages));
        RetrofitResourceBuilder retrofitResourceBuilder = new RetrofitResourceBuilder(environment);
        retrofitResourceBuilder.build(retrofitBuilderClassSet);
        final List<RetrofitClientBean> retrofitClientBeanList = retrofitResourceBuilder.getRetrofitClientBeanList();
        BeanDefinitionBuilder builder;
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
        for (RetrofitClientBean clientBean : retrofitClientBeanList) {
            for (RetrofitServiceBean serviceBean : clientBean.getRetrofitServices()) {
                builder = BeanDefinitionBuilder.genericBeanDefinition(serviceBean.getSelfClazz());
                GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();
                definition.getConstructorArgumentValues().addGenericArgumentValue(Objects.requireNonNull(definition.getBeanClassName()));
                definition.getConstructorArgumentValues().addGenericArgumentValue(serviceBean);
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
        if (retrofitBuilder.client() != null) {
            final OkHttpClientBuilder reflectOkHttpClientBuilder = retrofitBuilder.client().newInstance();
            okHttpClientBuilder = reflectOkHttpClientBuilder.getOkHttpClientBuilder();
        } else {
            okHttpClientBuilder = new OkHttpClient.Builder();
        }
        final List<Interceptor> okHttpInterceptors = getOkHttpInterceptor(interceptors);
        okHttpInterceptors.forEach(okHttpClientBuilder::addInterceptor);
        builder.client(okHttpClientBuilder.build());
    }

    @SneakyThrows
    private List<Interceptor> getOkHttpInterceptor(List<RetrofitInterceptor> interceptors) {
        List<Interceptor> interceptorList = new ArrayList<>();
        for (RetrofitInterceptor interceptor : interceptors) {
            interceptorList.add(interceptor.handler().newInstance());
        }
        return interceptorList;
    }

    @SneakyThrows
    private List<CallAdapter.Factory> getCallAdapterFactories(Class<? extends CallAdapter.Factory>[] callAdapterFactoryClasses) {
        List<CallAdapter.Factory> callAdapterFactories = new ArrayList<>();
        for (Class<? extends CallAdapter.Factory> callAdapterFactoryClass : callAdapterFactoryClasses) {
            CallAdapter.Factory factory = callAdapterFactoryClass.newInstance();
            callAdapterFactories.add(factory);
        }
        return callAdapterFactories;
    }

    @SneakyThrows
    private List<Converter.Factory> getConverterFactories(Class<? extends Converter.Factory>[] converterFactoryClasses) {
        List<Converter.Factory> converterFactories = new ArrayList<>();
        for (Class<? extends Converter.Factory> converterFactoryClass : converterFactoryClasses) {
            Converter.Factory factory = converterFactoryClass.newInstance();
            converterFactories.add(factory);
        }
        return converterFactories;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
