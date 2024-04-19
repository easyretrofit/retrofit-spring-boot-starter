package io.github.liuziyuan.retrofit.extension.spring.cloud.loadbalancer;

import io.github.liuziyuan.retrofit.core.annotation.RetrofitDynamicBaseUrl;
import io.github.liuziyuan.retrofit.core.annotation.RetrofitInterceptor;
import io.github.liuziyuan.retrofit.core.annotation.RetrofitInterceptorParam;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@RetrofitInterceptor(handler = RetrofitLoadBalancerInterceptor.class)
public @interface RetrofitLoadBalancer {
    String name() default "";

    RetrofitInterceptorParam extensions() default @RetrofitInterceptorParam();
}
