package io.github.liuziyuan.retrofit.extension.spring.cloud.loadbalancer;

import io.github.liuziyuan.retrofit.core.annotation.RetrofitDynamicBaseUrl;
import io.github.liuziyuan.retrofit.core.annotation.RetrofitInterceptor;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@RetrofitDynamicBaseUrl
public @interface RetrofitLoadBalancer {
    @AliasFor(
            annotation = RetrofitDynamicBaseUrl.class,
            attribute = "value"
    )
    String name() default "";

    RetrofitInterceptor extensions() default @RetrofitInterceptor(handler = RetrofitLoadBalancerInterceptor.class);
}
