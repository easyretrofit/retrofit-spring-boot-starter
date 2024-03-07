package io.github.liuziyuan.retrofit;

import io.github.liuziyuan.retrofit.core.Extension;
import io.github.liuziyuan.retrofit.spring.boot.RetrofitComponent;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

@Component
@RetrofitComponent
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