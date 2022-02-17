package io.github.liuziyuan.retrofit;

import io.github.liuziyuan.retrofit.generator.RetrofitBuilderGenerator;
import io.github.liuziyuan.retrofit.proxy.RetrofitServiceProxyFactory;
import io.github.liuziyuan.retrofit.resource.RetrofitClientBean;
import io.github.liuziyuan.retrofit.resource.RetrofitServiceBean;
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
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
