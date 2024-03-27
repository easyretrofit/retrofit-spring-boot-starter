package io.github.liuziyuan.retrofit.extension.sentinel.core.annotation;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface RetrofitSentinelRateLimiterFlowRule {
    int count() default 4000;

    String limitApp() default RuleConstant.LIMIT_APP_DEFAULT;

    int rateLimit() default 500;

    int grade() default RuleConstant.FLOW_GRADE_QPS;

    String fallbackMethod();
}
