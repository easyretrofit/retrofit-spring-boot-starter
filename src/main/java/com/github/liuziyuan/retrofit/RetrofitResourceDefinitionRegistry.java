package com.github.liuziyuan.retrofit;

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
 * @date 2/8/2022 3:07 PM
 */
public class RetrofitResourceDefinitionRegistry implements BeanDefinitionRegistryPostProcessor, ApplicationContextAware {

    private ApplicationContext applicationContext;


    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

        RetrofitResourceContext context = (RetrofitResourceContext) beanFactory.getBean(RetrofitResourceContext.class.getName());
        context.setApplicationContext(applicationContext);

        BeanDefinitionRegistry beanDefinitionRegistry = (BeanDefinitionRegistry) beanFactory;
        final BeanDefinition beanDefinition = beanDefinitionRegistry.getBeanDefinition(RetrofitResourceContext.class.getName());
        if (beanDefinition != null) {
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
