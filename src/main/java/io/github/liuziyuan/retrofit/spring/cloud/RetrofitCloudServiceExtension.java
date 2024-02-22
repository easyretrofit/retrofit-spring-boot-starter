package io.github.liuziyuan.retrofit.spring.cloud;

import io.github.liuziyuan.retrofit.core.Extension;

import java.lang.annotation.Annotation;

public class RetrofitCloudServiceExtension extends Extension {
    @Override
    public Class<? extends Annotation> createAnnotation() {
        return RetrofitCloudService.class;
    }

    @Override
    public Class<?> createInterceptor() {
        return RetrofitCloudInterceptor.class;
    }
}
