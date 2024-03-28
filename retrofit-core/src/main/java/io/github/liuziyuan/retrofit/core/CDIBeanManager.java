package io.github.liuziyuan.retrofit.core;

public interface CDIBeanManager {

    <T> T getBean(Class<T> clazz);
}
