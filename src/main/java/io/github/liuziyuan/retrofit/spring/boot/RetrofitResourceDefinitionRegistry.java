package io.github.liuziyuan.retrofit.spring.boot;

import io.github.liuziyuan.retrofit.core.RetrofitResourceContext;
import io.github.liuziyuan.retrofit.core.builder.BaseCallAdapterFactoryBuilder;
import io.github.liuziyuan.retrofit.core.builder.BaseConverterFactoryBuilder;
import io.github.liuziyuan.retrofit.core.resource.RetrofitBuilderBean;
import io.github.liuziyuan.retrofit.core.resource.RetrofitClientBean;
import io.github.liuziyuan.retrofit.core.resource.RetrofitApiServiceBean;
import io.github.liuziyuan.retrofit.core.resource.UrlStatus;
import io.github.liuziyuan.retrofit.spring.boot.generator.SpringBootRetrofitBuilderGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import retrofit2.Retrofit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Retrofit Resources Definition and Registry, including Retrofit objects and Retrofit API objects of dynamic proxy
 *
 * @author liuziyuan
 */
@Slf4j
public class RetrofitResourceDefinitionRegistry implements BeanDefinitionRegistryPostProcessor, ApplicationContextAware {

    private static final String WARNING_RETROFIT_CLIENT_EMPTY = "The [Retrofit Client] is not found in the 'RetrofitResourceContext'. You may not be using @RetrofitBuilder in the basePackages";
    private static final String RETROFIT_RESOURCE_CONTEXT_NOT_FOUND = "The 'RetrofitResourceContext' object not found in the Spring 'ApplicationContext'. You may not be using @EnableRetrofit";

    private ApplicationContext applicationContext;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        // TODO empty method, if you want to definition & registry , use it.
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        BeanDefinitionRegistry beanDefinitionRegistry = (BeanDefinitionRegistry) beanFactory;
        RetrofitResourceContext context;
        try {
            // set context
            context = (RetrofitResourceContext) beanFactory.getBean(RetrofitResourceContext.class.getName());
            beanFactory.autowireBean(context);
            List<RetrofitClientBean> retrofitClientBeanList = context.getRetrofitClients();
            // registry Retrofit object
            registryRetrofitInstance(beanDefinitionRegistry, retrofitClientBeanList, context);
            // registry proxy object of retrofit api interface
            registryRetrofitInterfaceProxy(beanDefinitionRegistry, retrofitClientBeanList);
            // set log
            setLog(context, applicationContext.getEnvironment());
        } catch (NoSuchBeanDefinitionException exception) {
            log.error(RETROFIT_RESOURCE_CONTEXT_NOT_FOUND);
            throw exception;
        }
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
            for (RetrofitApiServiceBean serviceBean : clientBean.getRetrofitServices()) {
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

    private void setLog(RetrofitResourceContext context, Environment environment) {
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

        if (context.getRetrofitClients().isEmpty()) {
            log.warn(WARNING_RETROFIT_CLIENT_EMPTY);
        }
        for (RetrofitClientBean retrofitClient : context.getRetrofitClients()) {
            final String retrofitInstanceName = retrofitClient.getRetrofitInstanceName();
            final String realHostUrl = retrofitClient.getRealHostUrl();
            if (retrofitClient.getUrlStatus().equals(UrlStatus.DYNAMIC_URL_ONLY)) {
                log.warn("---Retrofit Client : HostURL[Dummy]: {}, Retrofit instance name: {}", realHostUrl, retrofitInstanceName);
            } else {
                log.info("---Retrofit Client : HostURL: {}, Retrofit instance name: {}", realHostUrl, retrofitInstanceName);
            }
            retrofitClientDebugLog(retrofitClient);
            for (RetrofitApiServiceBean retrofitService : retrofitClient.getRetrofitServices()) {
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
        String validateEagerlyString = retrofitBuilder.getValidateEagerly();
        String inheritedInterceptor = retrofitClient.getInheritedInterceptors().toString();
        String interceptor = retrofitClient.getInterceptors().toString();
        log.debug("RetrofitClientBean: HostURL: {}; UrlStatus: {}; globalEnable: {}; CallAdapterFactory: {}; ConverterFactory:{}; callbackExecutor: {}; client: {}; callFactory: {}; validateEagerly: {}; inheritedInterceptor: {}; interceptor: {}",
                realHostUrl, retrofitClient.getUrlStatus(), globalEnable, CallAdapterFactoryString, ConverterFactoryString, callbackExecutorString, clientString, callFactoryString, validateEagerlyString, inheritedInterceptor, interceptor);

    }

}
