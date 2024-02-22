package io.github.liuziyuan.retrofit.core;

import io.github.liuziyuan.retrofit.core.extension.BaseInterceptor;

import java.lang.annotation.Annotation;

public abstract class Extension {


    public abstract Class<? extends Annotation> createAnnotation();
    public abstract Class<?> createInterceptor();
}
