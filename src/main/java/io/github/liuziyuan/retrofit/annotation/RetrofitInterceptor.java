package io.github.liuziyuan.retrofit.annotation;

import io.github.liuziyuan.retrofit.extension.BaseInterceptor;

import java.lang.annotation.*;

/**
 * Annotation of OkHttpClient Interceptor
 * @author liuziyuan
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Repeatable(Interceptors.class)
public @interface RetrofitInterceptor {
    Class<? extends BaseInterceptor> handler();

    String[] include() default {"/**"};

    String[] exclude() default {};

    int sort() default 0;
}
