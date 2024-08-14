package io.github.liuziyuan.test.retrofit.spring.boot.extension;

import io.github.easyretrofit.core.RetrofitInterceptorExtension;
import io.github.easyretrofit.core.exception.RetrofitExtensionException;
import io.github.easyretrofit.core.extension.BaseInterceptor;
import io.github.easyretrofit.core.proxy.BaseExceptionDelegate;
import io.github.easyretrofit.core.proxy.ExceptionDelegate;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

@Component
public class RetrofitTestExtension implements RetrofitInterceptorExtension {
    @Override
    public Class<? extends Annotation> createAnnotation() {
        return RetrofitTestAnno.class;
    }

    @Override
    public Class<? extends BaseInterceptor> createInterceptor() {
        return RetrofitTestInterceptor.class;
    }

    @Override
    public Class<? extends BaseExceptionDelegate<? extends RetrofitExtensionException>> createExceptionDelegate() {
        return MyExceptionDelegate.class;
    }

}
