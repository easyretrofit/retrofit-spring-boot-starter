package io.github.liuziyuan.retrofit;

import io.github.liuziyuan.retrofit.handler.RetrofitBuilderHandler;
import io.github.liuziyuan.retrofit.proxy.RetrofitServiceProxyFactory;
import io.github.liuziyuan.retrofit.resource.RetrofitClientBean;
import io.github.liuziyuan.retrofit.resource.RetrofitServiceBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import retrofit2.Retrofit;

import java.util.List;
import java.util.Objects;

/**
 * Retrofit Resources Definition and Registry
 *
 * @author liuziyuan
 */
public class RetrofitResourceDefinitionRegistry implements BeanDefinitionRegistryPostProcessor, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

        BeanDefinitionRegistry beanDefinitionRegistry = (BeanDefinitionRegistry) beanFactory;
        final BeanDefinition retrofitResourceContextBeanDefinition = beanDefinitionRegistry.getBeanDefinition(RetrofitResourceContext.class.getName());
        BeanDefinitionBuilder builder;
        if (retrofitResourceContextBeanDefinition != null) {
            RetrofitResourceContext context = (RetrofitResourceContext) beanFactory.getBean(RetrofitResourceContext.class.getName());
            context.setApplicationContext(applicationContext);
            beanDefinitionRegistry.removeBeanDefinition(RetrofitResourceContext.class.getName());
            List<RetrofitClientBean> retrofitClientBeanList = null;
            if (!context.getRetrofitClients().isEmpty()) {
                builder = BeanDefinitionBuilder.genericBeanDefinition(RetrofitResourceContext.class, () -> context);
                GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();
                beanDefinitionRegistry.registerBeanDefinition(RetrofitResourceContext.class.getName(), definition);
                retrofitClientBeanList = context.getRetrofitClients();
            }
            //registry Retrofit
            for (RetrofitClientBean clientBean : retrofitClientBeanList) {
                builder = BeanDefinitionBuilder.genericBeanDefinition(Retrofit.class, () -> {
                    RetrofitBuilderHandler retrofitBuilderHandler = new RetrofitBuilderHandler(clientBean, context);
                    final Retrofit.Builder retrofitBuilder = retrofitBuilderHandler.generate();
                    return retrofitBuilder.build();
                });
                GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();
                definition.addQualifier(new AutowireCandidateQualifier(Qualifier.class, clientBean.getRetrofitInstanceName()));
                beanDefinitionRegistry.registerBeanDefinition(clientBean.getRetrofitInstanceName(), definition);
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
                    beanDefinitionRegistry.registerBeanDefinition(serviceBean.getSelfClazz().getName(), definition);
                }
            }

        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
