package io.github.liuziyuan.retrofit.core;

import java.lang.annotation.Annotation;

public abstract class Extension {


    public abstract Class<? extends Annotation> createAnnotation();
    public abstract Class<?> createInterceptor();
}
