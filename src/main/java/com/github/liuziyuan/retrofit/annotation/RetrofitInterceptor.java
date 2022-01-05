package com.github.liuziyuan.retrofit.annotation;

import java.lang.annotation.*;

/**
 * @author liuziyuan
 * @date 1/5/2022 5:21 PM
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Repeatable(Interceptors.class)
public @interface RetrofitInterceptor {
}
