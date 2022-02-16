package io.github.liuziyuan.retrofit;

/**
 * Generate interface used internally
 * @author liuziyuan
 */
public interface Generator<T> {
    /**
     * To generate what you want
     * @return T
     */
    T generate();
}
