package io.github.liuziyuan.retrofit.springboot;

public interface AppContext {

    <T> T getBean(Class<T> clazz);
}
