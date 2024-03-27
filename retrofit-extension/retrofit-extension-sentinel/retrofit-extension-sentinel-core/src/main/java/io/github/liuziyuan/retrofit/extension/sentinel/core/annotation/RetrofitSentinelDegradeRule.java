package io.github.liuziyuan.retrofit.extension.sentinel.core.annotation;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;

import java.lang.annotation.*;
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface RetrofitSentinelDegradeRule {
    int grade() default 0;
    double count() default 0;
    int timeWindow() default 0;
    String limitApp() default RuleConstant.LIMIT_APP_DEFAULT;
    int minRequestAmount() default 5;
    double slowRatioThreshold() default 1.0;
    int statIntervalMs() default 1000;
    String fallbackMethod();
}
