package io.github.liuziyuan.retrofit.spring.boot.util;

import org.springframework.context.ApplicationContext;

public class BeanUtil {

    public static <T> T getBean(Class<T> clazz, String beanName, ApplicationContext applicationContext) {

        return applicationContext.getBean(beanName, clazz);
    }
}
