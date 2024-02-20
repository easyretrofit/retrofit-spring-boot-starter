package io.github.liuziyuan.retrofit.core.extension;

/**
 * abstract class of Builder
 * @author liuziyuan
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
