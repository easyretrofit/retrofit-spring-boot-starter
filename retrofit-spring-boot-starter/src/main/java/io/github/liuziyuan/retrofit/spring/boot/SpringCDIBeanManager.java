package io.github.liuziyuan.retrofit.spring.boot;

import io.github.liuziyuan.retrofit.core.CDIBeanManager;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringCDIBeanManager implements CDIBeanManager {

    private ApplicationContext applicationContext;
    public SpringCDIBeanManager(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

}
