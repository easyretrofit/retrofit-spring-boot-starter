package io.github.liuziyuan.retrofit.spring.boot;

import io.github.liuziyuan.retrofit.core.Env;
import io.github.liuziyuan.retrofit.core.RetrofitResourceContext;
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

        RetrofitBuilderBean retrofitBuilderBean = setRetrofitBuilderBean();
        // init RetrofitResourceContext by RetrofitResourceContextBuilder
        RetrofitResourceContext context = initRetrofitResourceContext(retrofitBuilderClassSet, retrofitBuilderBean);
        //registry RetrofitResourceContext
        registryRetrofitResourceContext(registry, context);
    }

    private RetrofitBuilderBean setRetrofitBuilderBean() {
        RetrofitBuilderBean retrofitBuilderBean = new RetrofitBuilderBean();
        GlobalParamConfigSetting globalParamConfigSetting;
        RetrofitGlobalConfigProperties properties = new RetrofitGlobalConfigProperties();
        properties.setByEnvironment(environment);
        globalParamConfigSetting = new GlobalParamConfigSetting(properties, getBeanInstance());
        if (globalParamConfigSetting.enable()) {
            retrofitBuilderBean.setEnable(globalParamConfigSetting.enable());
            retrofitBuilderBean.setOverwriteType(globalParamConfigSetting.overwriteType());
            retrofitBuilderBean.setBaseUrl(globalParamConfigSetting.globalBaseUrl());
            retrofitBuilderBean.setClient(globalParamConfigSetting.globalOkHttpClientBuilderClazz());
            retrofitBuilderBean.setCallFactory(globalParamConfigSetting.globalCallFactoryBuilderClazz());
            retrofitBuilderBean.setCallbackExecutor(globalParamConfigSetting.globalCallBackExecutorBuilderClazz());
            retrofitBuilderBean.setAddConverterFactory(globalParamConfigSetting.globalConverterFactoryBuilderClazz());
            retrofitBuilderBean.setAddCallAdapterFactory(globalParamConfigSetting.globalCallAdapterFactoryBuilderClazz());
            retrofitBuilderBean.setValidateEagerly(globalParamConfigSetting.globalValidateEagerly());
        }
        return retrofitBuilderBean;
    }

    private <T> T getBeanInstance() {
        SpringBootRetrofitResourceScanner scanner = new SpringBootRetrofitResourceScanner();
        Set<Class<?>> retrofitResources = scanner.getRetrofitResource(RetrofitComponent.class, "io.github.liuziyuan.retrofit.spring.boot");
        for (Class<?> retrofitResource : retrofitResources) {
            if (Arrays.stream(retrofitResource.getInterfaces()).anyMatch(c -> "GlobalParamConfig".equalsIgnoreCase(c.getSimpleName()))) {
                try {
                    return (T) retrofitResource.newInstance();
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InstantiationException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return null;
    }


    private void registryRetrofitResourceContext(BeanDefinitionRegistry registry, RetrofitResourceContext context) {
        BeanDefinitionBuilder builder;
        builder = BeanDefinitionBuilder.genericBeanDefinition(RetrofitResourceContext.class, () -> context);
        GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();
        registry.registerBeanDefinition(RetrofitResourceContext.class.getName(), definition);
    }

    private RetrofitResourceContext initRetrofitResourceContext(Set<Class<?>> retrofitBuilderClassSet, RetrofitBuilderBean retrofitBuilderBean) {

        Env env = new SpringBootEnv(environment);
        SpringBootRetrofitResourceContextBuilder retrofitResourceContextBuilder = new SpringBootRetrofitResourceContextBuilder(env);
        retrofitResourceContextBuilder.build(retrofitBuilderClassSet, retrofitBuilderBean);
        final List<RetrofitClientBean> retrofitClientBeanList = retrofitResourceContextBuilder.getRetrofitClientBeanList();
        final Map<String, RetrofitApiServiceBean> retrofitServiceBeanHashMap = retrofitResourceContextBuilder.getRetrofitServiceBeanHashMap();
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
