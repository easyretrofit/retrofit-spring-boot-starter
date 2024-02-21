package io.github.liuziyuan.retrofit.core;

import io.github.liuziyuan.retrofit.core.exception.NoSuchBeanDefinitionException;

public interface AppContext {

    <T> T getBean(Class<T> clazz);

    Object getBean(String var1);
}
