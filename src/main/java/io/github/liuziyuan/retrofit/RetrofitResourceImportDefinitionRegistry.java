package io.github.liuziyuan.retrofit;

import io.github.liuziyuan.retrofit.annotation.EnableRetrofit;
import io.github.liuziyuan.retrofit.annotation.RetrofitBuilder;
import io.github.liuziyuan.retrofit.annotation.RetrofitInterceptor;
import io.github.liuziyuan.retrofit.extension.UrlOverWriteInterceptor;
import io.github.liuziyuan.retrofit.handler.*;
import io.github.liuziyuan.retrofit.proxy.RetrofitServiceProxyFactory;
import io.github.liuziyuan.retrofit.resource.RetrofitClientBean;
import io.github.liuziyuan.retrofit.resource.RetrofitServiceBean;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.support.*;
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
 */
@Slf4j
public class RetrofitResourceImportDefinitionRegistry implements ImportBeanDefinitionRegistrar, EnvironmentAware, ResourceLoaderAware {

    private RetrofitResourceContext context;
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
                RetrofitBuilderHandler retrofitBuilderHandler = new RetrofitBuilderHandler(clientBean, context);
                final Retrofit.Builder retrofitBuilder = retrofitBuilderHandler.generate();
                return retrofitBuilder.build();
            });
            AbstractBeanDefinition definition = builder.getRawBeanDefinition();
            definition.addQualifier(new AutowireCandidateQualifier(Qualifier.class, clientBean.getRetrofitInstanceName()));
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

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
