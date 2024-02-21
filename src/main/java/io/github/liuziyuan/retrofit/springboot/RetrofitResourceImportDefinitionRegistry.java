package io.github.liuziyuan.retrofit.springboot;

import io.github.liuziyuan.retrofit.core.Env;
import io.github.liuziyuan.retrofit.core.RetrofitResourceContext;
import io.github.liuziyuan.retrofit.core.RetrofitResourceScanner;
import io.github.liuziyuan.retrofit.core.resource.RetrofitClientBean;
import io.github.liuziyuan.retrofit.core.resource.RetrofitServiceBean;
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
        // scan RetrofitResource
        final Set<Class<?>> retrofitBuilderClassSet = scanRetrofitResource(annoAttrs);
        // init RetrofitResourceContext by RetrofitResourceContextBuilder
        RetrofitResourceContext context = initRetrofitResourceContext(retrofitBuilderClassSet);
        //registry RetrofitResourceContext
        registryRetrofitResourceContext(registry, context);
    }

    private void registryRetrofitResourceContext(BeanDefinitionRegistry registry, RetrofitResourceContext context) {
        BeanDefinitionBuilder builder;
        builder = BeanDefinitionBuilder.genericBeanDefinition(RetrofitResourceContext.class, () -> context);
        GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();
        registry.registerBeanDefinition(RetrofitResourceContext.class.getName(), definition);
    }

    private RetrofitResourceContext initRetrofitResourceContext(Set<Class<?>> retrofitBuilderClassSet) {

        Env env = new SpringBootEnv(environment);
        RetrofitResourceContextBuilder retrofitResourceContextBuilder = new RetrofitResourceContextBuilder(env);
        retrofitResourceContextBuilder.build(retrofitBuilderClassSet);
        final List<RetrofitClientBean> retrofitClientBeanList = retrofitResourceContextBuilder.getRetrofitClientBeanList();
        final Map<String, RetrofitServiceBean> retrofitServiceBeanHashMap = retrofitResourceContextBuilder.getRetrofitServiceBeanHashMap();
        return new RetrofitResourceContext(retrofitClientBeanList, retrofitServiceBeanHashMap);
    }

    private Set<Class<?>> scanRetrofitResource(AnnotationAttributes annoAttrs) {
        // scan RetrofitResource
        SpringBootRetrofitResourceScanner scanner = new SpringBootRetrofitResourceScanner();
        List<String> basePackages = new ArrayList<>();
        basePackages.addAll(Arrays.stream(annoAttrs.getStringArray("value")).filter(StringUtils::hasText).collect(Collectors.toList()));
        basePackages.addAll(Arrays.stream(annoAttrs.getStringArray("basePackages")).filter(StringUtils::hasText).collect(Collectors.toList()));
        basePackages.addAll(Arrays.stream(annoAttrs.getClassArray("basePackageClasses")).map(ClassUtils::getPackageName).collect(Collectors.toList()));
        return scanner.doScan(StringUtils.toStringArray(basePackages));
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
