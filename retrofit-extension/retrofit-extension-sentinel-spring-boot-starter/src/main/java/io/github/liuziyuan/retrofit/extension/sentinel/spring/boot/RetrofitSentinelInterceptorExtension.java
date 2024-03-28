package io.github.liuziyuan.retrofit.extension.sentinel.spring.boot;

import io.github.liuziyuan.retrofit.core.RetrofitInterceptorExtension;
import io.github.liuziyuan.retrofit.core.exception.RetrofitExtensionException;
import io.github.liuziyuan.retrofit.core.extension.BaseInterceptor;
import io.github.liuziyuan.retrofit.core.proxy.BaseExceptionDelegate;
import io.github.liuziyuan.retrofit.extension.sentinel.core.annotation.RetrofitSentinelResource;
import io.github.liuziyuan.retrofit.extension.sentinel.core.interceptor.RetrofitSentinelInterceptor;
import io.github.liuziyuan.retrofit.extension.sentinel.core.interceptor.SentinelBlockExceptionFallBackHandler;

import java.lang.annotation.Annotation;

public class RetrofitSentinelInterceptorExtension implements RetrofitInterceptorExtension {

    @Override
    public Class<? extends Annotation> createAnnotation() {
        return RetrofitSentinelResource.class;
    }

    @Override
    public Class<? extends BaseInterceptor> createInterceptor() {
        return RetrofitSentinelInterceptor.class;
    }

    @Override
    public Class<? extends BaseExceptionDelegate<? extends RetrofitExtensionException>> createExceptionDelegate() {
        return SentinelBlockExceptionFallBackHandler.class;
    }

}
