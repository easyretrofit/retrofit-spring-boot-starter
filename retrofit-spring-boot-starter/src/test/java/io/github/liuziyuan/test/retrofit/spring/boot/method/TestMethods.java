package io.github.liuziyuan.test.retrofit.spring.boot.method;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface TestMethods {
    TestMethod[] value();
}
