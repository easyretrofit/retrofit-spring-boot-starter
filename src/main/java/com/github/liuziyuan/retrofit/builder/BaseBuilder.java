package com.github.liuziyuan.retrofit.builder;

/**
 * @author liuziyuan
 * @date 12/31/2021 12:47 PM
 */
public abstract class BaseBuilder<T> implements Builder<T> {

    /**
     * execute build
     * @return T
     */
    public abstract T executeBuild();

    @Override
    public T build() {
        return executeBuild();
    }
}
