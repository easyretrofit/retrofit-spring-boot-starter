package io.github.liuziyuan.retrofit.spring.boot;

import io.github.liuziyuan.retrofit.core.*;
import io.github.liuziyuan.retrofit.core.resource.RetrofitBuilderBean;
import io.github.liuziyuan.retrofit.core.resource.RetrofitClientBean;
import io.github.liuziyuan.retrofit.core.resource.RetrofitApiServiceBean;
import io.github.liuziyuan.retrofit.core.resource.UrlStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import retrofit2.Retrofit;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Retrofit Resources Definition and Registry, including Retrofit objects and Retrofit API objects of dynamic proxy
 * Retrofit 资源的注册器，包括 Retrofit 对象和动态代理的 Retrofit API 对象
 * 非常重要，这里是可以通过ConfigurableListableBeanFactory获取到上下文中的bean
 *
 * @author liuziyuan
 */
@Slf4j
public class RetrofitResourceDefinitionRegistry implements BeanDefinitionRegistryPostProcessor, ApplicationContextAware, EnvironmentAware {
    private ApplicationContext applicationContext;

    private Environment environment;

    /**
     * 定义于 BeanDefinitionRegistryPostProcessor 接口中。
     * 在Spring IoC容器注册完所有的Bean Definition（即解析并加载了所有XML配置文件或注解扫描完成之后）但尚未创建任何bean实例之前调用。
     * 通过此方法，开发者可以添加、修改或删除已注册的Bean Definition，比如动态注册额外的bean或者调整原有的bean定义属性。
     *
     * @param beanDefinitionRegistry
     * @throws BeansException
     */
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        // TODO empty method, if you want to definition & registry , use it.
    }

    /**
     * 定义于 BeanFactoryPostProcessor 接口中。
     * 在Spring IoC容器已经完成Bean Definition的注册并且Bean Factory已经创建出来，但在初始化单个bean实例（调用其构造函数或工厂方法）之前调用。
     * 通过此方法，开发者能够访问和修改bean工厂级别的共享配置，例如更改bean定义的属性值（如注入的依赖项）、设置Bean的scope等，但它不适用于改变Bean实例化后的状态。
     *
     * @param beanFactory
     * @throws BeansException
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        BeanDefinitionRegistry beanDefinitionRegistry = (BeanDefinitionRegistry) beanFactory;
        RetrofitResourceContext context;
        try {
            // get RetrofitAnnotationBean
            RetrofitAnnotationBean retrofitAnnotationBean = (RetrofitAnnotationBean) beanFactory.getBean(RetrofitAnnotationBean.class.getName());
            // get RetrofitInterceptorExtension
            List<RetrofitInterceptorExtension> retrofitInterceptorExtensions = getRetrofitInterceptorExtensions(beanFactory);
            // get RetrofitBuilderExtension
            RetrofitBuilderExtension retrofitBuilderExtension = getRetrofitBuilderExtension(beanFactory);
            // init RetrofitResourceContext
            context = initRetrofitResourceContext(retrofitAnnotationBean.getRetrofitBuilderClassSet(), retrofitBuilderExtension, retrofitInterceptorExtensions);
            // registry RetrofitResourceContext
            registryRetrofitResourceContext(beanDefinitionRegistry, context);
            // get RetrofitClientBean
            List<RetrofitClientBean> retrofitClientBeanList = context.getRetrofitClients();
            // registry Retrofit object
            registryRetrofitInstance(beanDefinitionRegistry, retrofitClientBeanList, context);
            // registry proxy object of retrofit api interface
            registryRetrofitInterfaceProxy(beanDefinitionRegistry, retrofitClientBeanList);
            // set log
            setLog(context);
        } catch (NoSuchBeanDefinitionException exception) {
            log.info(Constants.RETROFIT_BUILDER_NOT_FOUND);
        }
    }

    private List<RetrofitInterceptorExtension> getRetrofitInterceptorExtensions(ConfigurableListableBeanFactory beanFactory) {
        String[] interceptorExtensionBeanNames = beanFactory.getBeanNamesForType(RetrofitInterceptorExtension.class);
        List<RetrofitInterceptorExtension> beans = new ArrayList<>();
        for (String beanName : interceptorExtensionBeanNames) {
            RetrofitInterceptorExtension myBean = beanFactory.getBean(beanName, RetrofitInterceptorExtension.class);
            beans.add(myBean);
        }
        return beans;
    }

    private RetrofitBuilderExtension getGlobalConfigExtension(ConfigurableListableBeanFactory beanFactory) {
        String[] globalConfigExtensionBeanNames = beanFactory.getBeanNamesForType(RetrofitBuilderExtension.class);
        RetrofitBuilderExtension bean = null;
        if (globalConfigExtensionBeanNames.length > 1) {
            log.warn("Multiple GlobalConfigExtension interface found, please check your configuration.");
        } else if (globalConfigExtensionBeanNames.length == 1) {
            bean = beanFactory.getBean(globalConfigExtensionBeanNames[0], RetrofitBuilderExtension.class);

        } else {
            return null;
        }
        return bean;
    }

    private RetrofitBuilderExtension getRetrofitBuilderExtension(ConfigurableListableBeanFactory beanFactory) {
        RetrofitGlobalConfigProperties properties = new RetrofitGlobalConfigProperties().generate(environment);
        RetrofitBuilderExtension globalConfigExtension = getGlobalConfigExtension(beanFactory);
        return new SpringBootGlobalConfig(properties, globalConfigExtension);
    }

    private void registryRetrofitResourceContext(BeanDefinitionRegistry registry, RetrofitResourceContext context) {
        BeanDefinitionBuilder builder;
        builder = BeanDefinitionBuilder.genericBeanDefinition(RetrofitResourceContext.class, () -> context);
        GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();
        registry.registerBeanDefinition(RetrofitResourceContext.class.getName(), definition);
    }

    private RetrofitResourceContext initRetrofitResourceContext(Set<Class<?>> retrofitBuilderClassSet, RetrofitBuilderExtension retrofitBuilderExtension, List<RetrofitInterceptorExtension> retrofitInterceptorExtensions) {
        Env env = new SpringBootEnv(environment);
        RetrofitResourceContextBuilder retrofitResourceContextBuilder = new RetrofitResourceContextBuilder(env);
        retrofitResourceContextBuilder.build(retrofitBuilderClassSet, retrofitBuilderExtension, retrofitInterceptorExtensions);
        final List<RetrofitClientBean> retrofitClientBeanList = retrofitResourceContextBuilder.getRetrofitClientBeanList();
        final Map<String, RetrofitApiServiceBean> retrofitServiceBeanHashMap = retrofitResourceContextBuilder.getRetrofitServiceBeanHashMap();
        return new RetrofitResourceContext(retrofitClientBeanList, retrofitServiceBeanHashMap);
    }

    private void registryRetrofitInstance(BeanDefinitionRegistry beanDefinitionRegistry, List<RetrofitClientBean> retrofitClientBeanList, RetrofitResourceContext context) {
        for (RetrofitClientBean clientBean : retrofitClientBeanList) {
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(Retrofit.class, () -> {
                SpringBootRetrofitBuilderGenerator retrofitBuilderGenerator = new SpringBootRetrofitBuilderGenerator(clientBean, context, applicationContext);
                final Retrofit.Builder retrofitBuilder = retrofitBuilderGenerator.generate();
                return retrofitBuilder.build();
            });
            GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();
            definition.addQualifier(new AutowireCandidateQualifier(Qualifier.class, clientBean.getRetrofitInstanceName()));
            beanDefinitionRegistry.registerBeanDefinition(clientBean.getRetrofitInstanceName(), definition);
        }
    }

    private void registryRetrofitInterfaceProxy(BeanDefinitionRegistry beanDefinitionRegistry, List<RetrofitClientBean> retrofitClientBeanList) {
        for (RetrofitClientBean clientBean : retrofitClientBeanList) {
            for (RetrofitApiServiceBean serviceBean : clientBean.getRetrofitApiServiceBeans()) {
                BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(serviceBean.getSelfClazz());
                GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();
                definition.getConstructorArgumentValues().addGenericArgumentValue(Objects.requireNonNull(definition.getBeanClassName()));
                definition.getConstructorArgumentValues().addGenericArgumentValue(serviceBean);
                definition.addQualifier(new AutowireCandidateQualifier(Qualifier.class, serviceBean.getSelfClazz().getName()));
                definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
                definition.setBeanClass(RetrofitServiceProxyFactory.class);
                beanDefinitionRegistry.registerBeanDefinition(serviceBean.getSelfClazz().getName(), definition);
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private void setLog(RetrofitResourceContext context) {
        log.info("\n" +
                        "__________        __                 _____.__  __   \n" +
                        "\\______   \\ _____/  |________  _____/ ____\\__|/  |_ \n" +
                        " |       _// __ \\   __\\_  __ \\/  _ \\   __\\|  \\   __\\\n" +
                        " |    |   \\  ___/|  |  |  | \\(  <_> )  |  |  ||  |  \n" +
                        " |____|_  /\\___  >__|  |__|   \\____/|__|  |__||__|  \n" +
                        "        \\/     \\/                                   \n" +
                        "::Retrofit Spring Boot Starter ::          ({})\n" +
                        "::Retrofit ::                              ({})\n",
                this.getClass().getPackage().getImplementationVersion(),
                "v2.9.0");

        for (RetrofitClientBean retrofitClient : context.getRetrofitClients()) {
            final String retrofitInstanceName = retrofitClient.getRetrofitInstanceName();
            final String realHostUrl = retrofitClient.getRealHostUrl();
            if (retrofitClient.getUrlStatus().equals(UrlStatus.DYNAMIC_URL_ONLY)) {
                log.warn("---Retrofit Client : HostURL[Dummy]: {}, Retrofit instance name: {}", realHostUrl, retrofitInstanceName);
            } else {
                log.info("---Retrofit Client : HostURL: {}, Retrofit instance name: {}", realHostUrl, retrofitInstanceName);
            }
            retrofitClientDebugLog(retrofitClient);
            for (RetrofitApiServiceBean retrofitService : retrofitClient.getRetrofitApiServiceBeans()) {
                final Class<?> selfClazz = retrofitService.getSelfClazz();
                final Class<?> parentClazz = retrofitService.getParentClazz();
                String parentClazzName = null;
                if (!parentClazz.getName().equals(selfClazz.getName())) {
                    parentClazzName = parentClazz.getName();
                }
                log.info("|--API Services: Interface name: {} , Parent Interface name: {}", selfClazz.getName(), parentClazzName);
            }
        }
    }

    private void retrofitClientDebugLog(RetrofitClientBean retrofitClient) {
        final String realHostUrl = retrofitClient.getRealHostUrl();
        RetrofitBuilderBean retrofitBuilder = retrofitClient.getRetrofitBuilder();
        final String globalEnable = retrofitBuilder.isEnable() ? "true" : "false";
        String CallAdapterFactoryString = StringUtils.join(Arrays.stream(retrofitBuilder.getAddCallAdapterFactory()).map(Class::getSimpleName).collect(Collectors.toList()), ",");
        String ConverterFactoryString = StringUtils.join(Arrays.stream(retrofitBuilder.getAddConverterFactory()).map(Class::getSimpleName).collect(Collectors.toList()), ",");
        String callbackExecutorString = retrofitBuilder.getCallbackExecutor().getSimpleName();
        String clientString = retrofitBuilder.getClient().getSimpleName();
        String callFactoryString = retrofitBuilder.getCallFactory().getSimpleName();
        String validateEagerlyString = retrofitBuilder.isValidateEagerly() ? "true" : "false";
        String inheritedInterceptor = retrofitClient.getInheritedInterceptors().toString();
        String interceptor = retrofitClient.getInterceptors().toString();
        log.debug("RetrofitClientBean: HostURL: {}; UrlStatus: {}; globalEnable: {}; CallAdapterFactory: {}; ConverterFactory:{}; callbackExecutor: {}; client: {}; callFactory: {}; validateEagerly: {}; inheritedInterceptor: {}; interceptor: {}",
                realHostUrl, retrofitClient.getUrlStatus(), globalEnable, CallAdapterFactoryString, ConverterFactoryString, callbackExecutorString, clientString, callFactoryString, validateEagerlyString, inheritedInterceptor, interceptor);

    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
