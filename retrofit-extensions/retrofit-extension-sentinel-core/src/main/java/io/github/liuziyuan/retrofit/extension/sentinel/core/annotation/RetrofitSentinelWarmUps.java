package io.github.liuziyuan.retrofit.extension.sentinel.core.annotation;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface RetrofitSentinelWarmUps {
    RetrofitSentinelWarmUpFlowRule[] value();
}
