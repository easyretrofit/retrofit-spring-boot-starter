package io.github.liuziyuan.retrofit.extension.sentinel.core.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface RetrofitSentinelFlowRules {
    RetrofitSentinelFlowRule[] value();
}
