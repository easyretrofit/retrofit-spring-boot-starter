package io.github.liuziyuan.retrofit.extension;

/**
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
