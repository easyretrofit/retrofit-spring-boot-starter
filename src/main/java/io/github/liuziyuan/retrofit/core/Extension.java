package io.github.liuziyuan.retrofit.core;

import io.github.liuziyuan.retrofit.core.extension.BaseInterceptor;

public abstract class Extension {


    public abstract Class<?> annotationClass();

    public abstract BaseInterceptor createInterceptor(RetrofitResourceContext context);
}
