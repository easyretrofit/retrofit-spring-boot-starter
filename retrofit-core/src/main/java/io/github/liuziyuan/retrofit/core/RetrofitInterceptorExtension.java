package io.github.liuziyuan.retrofit.core;

import io.github.liuziyuan.retrofit.core.extension.BaseInterceptor;
import io.github.liuziyuan.retrofit.core.proxy.BaseExceptionDelegate;
import io.github.liuziyuan.retrofit.core.exception.RetrofitExtensionException;

import java.lang.annotation.Annotation;

public interface RetrofitInterceptorExtension {

    Class<? extends Annotation> createAnnotation();

    Class<? extends BaseInterceptor> createInterceptor();

    Class<? extends BaseExceptionDelegate<? extends RetrofitExtensionException>> createExceptionDelegate();

}
