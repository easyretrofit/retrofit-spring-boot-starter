package io.github.liuziyuan.retrofit;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
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
        final BeanDefinition beanDefinition = beanDefinitionRegistry.getBeanDefinition(RetrofitResourceContext.class.getName());
        if (beanDefinition != null) {
            RetrofitResourceContext context = (RetrofitResourceContext) beanFactory.getBean(RetrofitResourceContext.class.getName());
            context.setApplicationContext(applicationContext);
            beanDefinitionRegistry.removeBeanDefinition(RetrofitResourceContext.class.getName());
            BeanDefinitionBuilder builder;
            builder = BeanDefinitionBuilder.genericBeanDefinition(RetrofitResourceContext.class, () -> context);
            GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();
            beanDefinitionRegistry.registerBeanDefinition(RetrofitResourceContext.class.getName(), definition);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
