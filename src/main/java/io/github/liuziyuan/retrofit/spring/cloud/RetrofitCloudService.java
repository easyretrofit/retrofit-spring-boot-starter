package io.github.liuziyuan.retrofit.spring.cloud;

import io.github.liuziyuan.retrofit.core.annotation.RetrofitDynamicBaseUrl;
import io.github.liuziyuan.retrofit.core.annotation.RetrofitInterceptor;
import io.github.liuziyuan.retrofit.spring.cloud.RetrofitCloudInterceptor;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author liuziyuan
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@RetrofitDynamicBaseUrl
@RetrofitInterceptor(handler = RetrofitCloudInterceptor.class)
public @interface RetrofitCloudService {
    @AliasFor(
            annotation = RetrofitDynamicBaseUrl.class,
            attribute = "value"
    )
    String name() default "";
}
