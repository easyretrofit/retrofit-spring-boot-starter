package io.github.easyretrofit.spring.boot;

import io.github.easyretrofit.core.CDIBeanManager;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;

public class SpringCDIBeanManager implements CDIBeanManager {

    private ApplicationContext applicationContext;

    public SpringCDIBeanManager(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public <T> T getBean(Class<T> clazz) {
        try {
            return applicationContext.getBean(clazz);
        } catch (NoSuchBeanDefinitionException ex) {
            return null;
        }

    }

    @Override
    public <T> T getBean(String name) {
        try {
            return (T) applicationContext.getBean(name);
        } catch (NoSuchBeanDefinitionException ex) {
            return null;
        }
    }

    @Override
    public <T> T getBean(String name, Class<T> clazz) {
        try {
            return applicationContext.getBean(name, clazz);
        } catch (NoSuchBeanDefinitionException ex) {
            return null;
        }
    }

}
