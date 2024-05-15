package io.github.liuziyuan.retrofit.core;

public interface CDIBeanManager {

    <T> T getBean(Class<T> clazz);

    <T> T getBean(String name);

    <T> T getBean(String name, Class<T> clazz);
}
