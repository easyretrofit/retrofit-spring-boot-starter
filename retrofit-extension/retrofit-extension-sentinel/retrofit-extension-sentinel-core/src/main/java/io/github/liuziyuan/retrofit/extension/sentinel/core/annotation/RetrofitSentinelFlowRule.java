package io.github.liuziyuan.retrofit.extension.sentinel.core.annotation;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Repeatable(RetrofitSentinelFlowRules.class)
public @interface RetrofitSentinelFlowRule {
    int grade() default 1;

    double count() default 0;

    String limitApp() default RuleConstant.LIMIT_APP_DEFAULT;

    int strategy() default 0;

    int controlBehavior() default 0;

    int warmUpPeriodSec() default 10;

    int maxQueueingTimeMs() default 500;

    String fallbackMethod();
}
