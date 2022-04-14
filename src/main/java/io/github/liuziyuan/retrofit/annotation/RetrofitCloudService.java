package io.github.liuziyuan.retrofit.annotation;

import io.github.liuziyuan.retrofit.extension.RetrofitCloudInterceptor;
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
