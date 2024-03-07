package io.github.liuziyuan.retrofit;

import io.github.liuziyuan.retrofit.core.annotation.RetrofitDynamicBaseUrl;
import io.github.liuziyuan.retrofit.core.annotation.RetrofitInterceptor;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

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
