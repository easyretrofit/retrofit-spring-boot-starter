package io.github.liuziyuan.test.retrofit.spring.boot.extension;

import io.github.liuziyuan.retrofit.core.InterceptorExtension;
import io.github.liuziyuan.retrofit.core.extension.BaseInterceptor;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

@Component
public class RetrofitTestExtension implements InterceptorExtension {
    @Override
    public Class<? extends Annotation> createAnnotation() {
        return RetrofitTestAnno.class;
    }

    @Override
    public Class<? extends BaseInterceptor> createInterceptor() {
        return RetrofitTestInterceptor.class;
    }
}
