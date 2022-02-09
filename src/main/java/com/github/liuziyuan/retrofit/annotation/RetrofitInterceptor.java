package com.github.liuziyuan.retrofit.annotation;

import com.github.liuziyuan.retrofit.extension.BaseInterceptor;

import java.lang.annotation.*;

/**
 * @author liuziyuan
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Repeatable(Interceptors.class)
public @interface RetrofitInterceptor {
    Class<? extends BaseInterceptor> handler();
}
