package io.github.liuziyuan.retrofit.extension.sentinel.core.annotation;

import io.github.liuziyuan.retrofit.extension.sentinel.core.resource.BaseFlowRuleConfig;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Repeatable(RetrofitSentinelFlowRules.class)
public @interface RetrofitSentinelFlowRule {
    /**
     * if default, use class_name.method_name
     *
     * @return resource name
     */
    String resourceName() default "";

    Class<? extends BaseFlowRuleConfig> config() default BaseFlowRuleConfig.class;

    String fallbackMethod();
}
