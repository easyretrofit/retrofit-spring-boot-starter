package io.github.liuziyuan.retrofit.core;

public interface AppContext {

    <T> T getBean(Class<T> clazz);
}
