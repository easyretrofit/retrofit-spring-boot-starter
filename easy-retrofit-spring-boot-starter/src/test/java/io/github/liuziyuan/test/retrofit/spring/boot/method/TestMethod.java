package io.github.liuziyuan.test.retrofit.spring.boot.method;

import io.github.easyretrofit.core.annotation.RetrofitDynamicBaseUrl;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Repeatable(TestMethods.class)
public @interface TestMethod {

    String value() default "";

    int port() default 0;
}
