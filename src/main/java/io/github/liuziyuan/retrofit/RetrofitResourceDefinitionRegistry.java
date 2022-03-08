package io.github.liuziyuan.retrofit;

import io.github.liuziyuan.retrofit.generator.RetrofitBuilderGenerator;
import io.github.liuziyuan.retrofit.proxy.RetrofitServiceProxyFactory;
import io.github.liuziyuan.retrofit.resource.RetrofitClientBean;
import io.github.liuziyuan.retrofit.resource.RetrofitServiceBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import retrofit2.Retrofit;

import java.util.List;
import java.util.Objects;

/**
 * Retrofit Resources Definition and Registry, including Retrofit objects and Retrofit API objects of dynamic proxy
 *
 * @author liuziyuan
 */
@Slf4j
public class RetrofitResourceDefinitionRegistry implements BeanDefinitionRegistryPostProcessor, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        // TODO empty method, if you want to definition & registry , use it.
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        BeanDefinitionRegistry beanDefinitionRegistry = (BeanDefinitionRegistry) beanFactory;
        BeanDefinitionBuilder builder;
        RetrofitResourceContext context = (RetrofitResourceContext) beanFactory.getBean(RetrofitResourceContext.class.getName());
        context.setApplicationContext(applicationContext);
        beanFactory.autowireBean(context);
        List<RetrofitClientBean> retrofitClientBeanList = context.getRetrofitClients();
        //registry Retrofit object
        for (RetrofitClientBean clientBean : retrofitClientBeanList) {
            builder = BeanDefinitionBuilder.genericBeanDefinition(Retrofit.class, () -> {
                RetrofitBuilderGenerator retrofitBuilderGenerator = new RetrofitBuilderGenerator(clientBean, context);
                final Retrofit.Builder retrofitBuilder = retrofitBuilderGenerator.generate();
                return retrofitBuilder.build();
            });
            GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();
            definition.addQualifier(new AutowireCandidateQualifier(Qualifier.class, clientBean.getRetrofitInstanceName()));
            beanDefinitionRegistry.registerBeanDefinition(clientBean.getRetrofitInstanceName(), definition);
        }
        //registry  proxy object of retrofit api interface
        for (RetrofitClientBean clientBean : retrofitClientBeanList) {
            for (RetrofitServiceBean serviceBean : clientBean.getRetrofitServices()) {
                builder = BeanDefinitionBuilder.genericBeanDefinition(serviceBean.getSelfClazz());
                GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();
                definition.getConstructorArgumentValues().addGenericArgumentValue(Objects.requireNonNull(definition.getBeanClassName()));
                definition.getConstructorArgumentValues().addGenericArgumentValue(serviceBean);
                definition.addQualifier(new AutowireCandidateQualifier(Qualifier.class, serviceBean.getSelfClazz().getName()));
                definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
                definition.setBeanClass(RetrofitServiceProxyFactory.class);
                beanDefinitionRegistry.registerBeanDefinition(serviceBean.getSelfClazz().getName(), definition);
            }
        }
        setLog(context);
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
                "::Retrofit ::                              ({})\n", "v0.0.9", "v2.9.0");
        for (RetrofitClientBean retrofitClient : context.getRetrofitClients()) {
            final String retrofitInstanceName = retrofitClient.getRetrofitInstanceName();
            final String realHostUrl = retrofitClient.getRealHostUrl();
            log.debug("---Retrofit Client : HostURL: {}, Retrofit instance name: {}", realHostUrl, retrofitInstanceName);
            for (RetrofitServiceBean retrofitService : retrofitClient.getRetrofitServices()) {
                final Class<?> selfClazz = retrofitService.getSelfClazz();
                final Class<?> parentClazz = retrofitService.getParentClazz();
                String parentClazzName = null;
                if (!parentClazz.getName().equals(selfClazz.getName())) {
                    parentClazzName = parentClazz.getName();
                }
                log.debug("|--API Services: Interface name: {} , Parent Interface name: {}", selfClazz.getName(), parentClazzName);
            }
        }
    }
}
