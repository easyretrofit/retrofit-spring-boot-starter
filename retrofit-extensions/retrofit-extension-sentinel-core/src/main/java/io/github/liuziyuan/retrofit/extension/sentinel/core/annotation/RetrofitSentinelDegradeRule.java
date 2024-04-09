package io.github.liuziyuan.retrofit.extension.sentinel.core.annotation;

import io.github.liuziyuan.retrofit.extension.sentinel.core.resource.BaseDegradeRuleConfig;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Repeatable(RetrofitSentinelDegrades.class)
public @interface RetrofitSentinelDegradeRule {

    /**
     * if default, use class_name.method_name
     *
     * @return resource name
     */
    String resourceName() default "";

    Class<? extends BaseDegradeRuleConfig> config() default BaseDegradeRuleConfig.class;

    String fallbackMethod();
}
