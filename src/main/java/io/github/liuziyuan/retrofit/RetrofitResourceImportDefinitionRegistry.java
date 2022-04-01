package io.github.liuziyuan.retrofit;

import io.github.liuziyuan.retrofit.annotation.EnableRetrofit;
import io.github.liuziyuan.retrofit.resource.RetrofitClientBean;
import io.github.liuziyuan.retrofit.resource.RetrofitServiceBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * When @EnableRetrofit is used, this class will generate the RetrofitResourceContext object according to the Annotation, and define and register it in the spring container
 *
 * @author liuziyuan
 */
@Slf4j
public class RetrofitResourceImportDefinitionRegistry implements ImportBeanDefinitionRegistrar, EnvironmentAware, ResourceLoaderAware {

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
        RetrofitResourceContext context = new RetrofitResourceContext();

        // scan RetrofitResource
        RetrofitResourceScanner scanner = new RetrofitResourceScanner();
        List<String> basePackages = new ArrayList<>();
        basePackages.addAll(Arrays.stream(annoAttrs.getStringArray("value")).filter(StringUtils::hasText).collect(Collectors.toList()));
        basePackages.addAll(Arrays.stream(annoAttrs.getStringArray("basePackages")).filter(StringUtils::hasText).collect(Collectors.toList()));
        basePackages.addAll(Arrays.stream(annoAttrs.getClassArray("basePackageClasses")).map(ClassUtils::getPackageName).collect(Collectors.toList()));
        final Set<Class<?>> retrofitBuilderClassSet = scanner.doScan(StringUtils.toStringArray(basePackages));

        // init RetrofitResourceContext by RetrofitResourceContextBuilder
        RetrofitResourceContextBuilder retrofitResourceContextBuilder = new RetrofitResourceContextBuilder(environment);
        retrofitResourceContextBuilder.build(retrofitBuilderClassSet);
        final List<RetrofitClientBean> retrofitClientBeanList = retrofitResourceContextBuilder.getRetrofitClientBeanList();
        final Map<String, RetrofitServiceBean> retrofitServiceBeanHashMap = retrofitResourceContextBuilder.getRetrofitServiceBeanHashMap();
        context.setRetrofitClients(retrofitClientBeanList);
        context.setRetrofitServices(retrofitServiceBeanHashMap);
        context.setEnvironment(environment);
        context.setResourceLoader(resourceLoader);

        //registry RetrofitResourceContext
        BeanDefinitionBuilder builder;
        builder = BeanDefinitionBuilder.genericBeanDefinition(RetrofitResourceContext.class, () -> context);
        GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();
        registry.registerBeanDefinition(RetrofitResourceContext.class.getName(), definition);
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
