package io.github.liuziyuan.retrofit.springboot;

import io.github.liuziyuan.retrofit.core.AppContext;
import org.springframework.context.ApplicationContext;

public class SpringAppContext implements AppContext {

    private ApplicationContext applicationContext;

    public SpringAppContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public <T> T getBean(Class<T> clazz) {
        if (applicationContext.containsBean(clazz.getName())) {
            return applicationContext.getBean(clazz);
        }
        return null;
    }

    @Override
    public Object getBean(String s) {
        if (applicationContext.containsBean(s)) {
            return applicationContext.getBean(s);
        }
        return null;
    }
}
