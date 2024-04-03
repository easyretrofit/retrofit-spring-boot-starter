package io.github.liuziyuan.retrofit.extension.spring.cloud.loadbalancer;

import io.github.liuziyuan.retrofit.core.RetrofitInterceptorExtension;
import io.github.liuziyuan.retrofit.core.exception.RetrofitExtensionException;
import io.github.liuziyuan.retrofit.core.extension.BaseInterceptor;
import io.github.liuziyuan.retrofit.core.proxy.BaseExceptionDelegate;

import java.lang.annotation.Annotation;

public class RetrofitLoadBalancerExtension implements RetrofitInterceptorExtension {
    @Override
    public Class<? extends Annotation> createAnnotation() {
        return RetrofitLoadBalancer.class;
    }

    @Override
    public Class<? extends BaseInterceptor> createInterceptor() {
        return RetrofitLoadBalancerInterceptor.class;
    }

    @Override
    public Class<? extends BaseExceptionDelegate<? extends RetrofitExtensionException>> createExceptionDelegate() {
        return null;
    }
}
