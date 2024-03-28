package io.github.liuziyuan.retrofit.extension.sentinel.core;

import io.github.liuziyuan.retrofit.core.exception.RetrofitExtensionException;

public abstract class BaseFallBack<T extends RetrofitExtensionException> {

    protected abstract void getFallBackException(T e);

    public void setException(T exception) {
        getFallBackException(exception);
    }

}
