package io.github.liuziyuan.retrofit.spring.boot;

import io.github.liuziyuan.retrofit.core.Env;
import io.github.liuziyuan.retrofit.core.RetrofitResourceContext;
import io.github.liuziyuan.retrofit.core.RetrofitResourceScanner;
import io.github.liuziyuan.retrofit.core.resource.RetrofitBuilderBean;
import io.github.liuziyuan.retrofit.core.resource.RetrofitClientBean;
import io.github.liuziyuan.retrofit.core.resource.RetrofitApiServiceBean;
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
 * 当@EnableRetrofit注解被使用时，会根据注解生成RetrofitResourceContext对象，然后将它注册到spring容器中，后续会在RetrofitResourceDefinitionRegistry类中注册真正的API接口
 * 非常重要的，这里是无法获取到Spring上下文中的bean
 *
 * @author liuziyuan
 */
@Slf4j
public class RetrofitResourceImportDefinitionRegistry implements ImportBeanDefinitionRegistrar, EnvironmentAware, ResourceLoaderAware {

    private Environment environment;
    private ResourceLoader resourceLoader;
    private RetrofitResourceScanner scanner;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes enableRetrofitAnnoAttr = AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(EnableRetrofit.class.getName()));
        if (enableRetrofitAnnoAttr != null) {
            registerRetrofitAnnotationDefinitions(enableRetrofitAnnoAttr, registry);
        }
    }

    void registerRetrofitAnnotationDefinitions(AnnotationAttributes annoAttrs, BeanDefinitionRegistry registry) {
        final Set<Class<?>> retrofitBuilderClassSet = scanRetrofitResource(annoAttrs);
        final List<String> basePackages = getBasePackages(annoAttrs);
        RetrofitAnnotationBean annotationBean = new RetrofitAnnotationBean(basePackages, retrofitBuilderClassSet);
        if (!retrofitBuilderClassSet.isEmpty()) {
            BeanDefinitionBuilder builder;
            builder = BeanDefinitionBuilder.genericBeanDefinition(RetrofitAnnotationBean.class, () -> annotationBean);
            GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();
            registry.registerBeanDefinition(RetrofitAnnotationBean.class.getName(), definition);
        }
    }

    private Set<Class<?>> scanRetrofitResource(AnnotationAttributes annoAttrs) {
        // scan RetrofitResource
        scanner = new RetrofitResourceScanner();
        List<String> basePackages = getBasePackages(annoAttrs);
        return scanner.doScan(StringUtils.toStringArray(basePackages));
    }

    private List<String> getBasePackages(AnnotationAttributes annoAttrs) {
        List<String> basePackages = new ArrayList<>();
        basePackages.addAll(Arrays.stream(annoAttrs.getStringArray("value")).filter(StringUtils::hasText).collect(Collectors.toList()));
        basePackages.addAll(Arrays.stream(annoAttrs.getStringArray("basePackages")).filter(StringUtils::hasText).collect(Collectors.toList()));
        basePackages.addAll(Arrays.stream(annoAttrs.getClassArray("basePackageClasses")).map(ClassUtils::getPackageName).collect(Collectors.toList()));
        return basePackages;
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
