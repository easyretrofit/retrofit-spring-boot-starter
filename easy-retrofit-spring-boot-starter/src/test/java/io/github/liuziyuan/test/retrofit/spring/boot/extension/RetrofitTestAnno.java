package io.github.liuziyuan.test.retrofit.spring.boot.extension;

import io.github.easyretrofit.core.annotation.InterceptorType;
import io.github.easyretrofit.core.annotation.RetrofitDynamicBaseUrl;
import io.github.easyretrofit.core.annotation.RetrofitInterceptor;
import io.github.easyretrofit.core.annotation.RetrofitInterceptorParam;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@RetrofitInterceptor(handler = RetrofitTestInterceptor.class)
public @interface RetrofitTestAnno {

    String name() default "";

    RetrofitInterceptorParam extension() default @RetrofitInterceptorParam;
}
