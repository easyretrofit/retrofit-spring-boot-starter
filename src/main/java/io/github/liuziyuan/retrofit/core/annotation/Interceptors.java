package io.github.liuziyuan.retrofit.core.annotation;

import io.github.liuziyuan.retrofit.core.annotation.RetrofitInterceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author liuziyuan
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Interceptors {
    RetrofitInterceptor[] value();
}
